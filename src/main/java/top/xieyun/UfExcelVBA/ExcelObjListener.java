package top.xieyun.UfExcelVBA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;

/**
 * ExcelObjListener
 */
public class ExcelObjListener extends AnalysisEventListener<ExcelObj> {
    List<ExcelObj> list = new ArrayList<>();
    List<OutputObj> outputList = new ArrayList<>();

    @Override
    public void invoke(final ExcelObj data, final AnalysisContext context) {
        final OutputObj outputObj = new OutputObj();
        String classFileName = "";
        if(data.getCell9().equals("○")){
                    outputObj.setCell1(data.getCell2() + "-" + data.getCell7());

        Pattern pattern = Pattern.compile("\\w+\\.java");
        Matcher matcher = pattern.matcher(data.getCell1());
        if (matcher.find()) {
            classFileName = matcher.group();
        }
        
        if(data.getCell8().matches("\\d+")){
            SearchFileObj searchFileObj = new SearchFileObj();
            searchFileObj.setFileName(classFileName);
            searchFileObj.setFileNo(Integer.parseInt(data.getCell8()));
            try {
                if (data.getCell11().equals("UFA")) {
                    searchFile(new File("E:\\search\\UFA-WebApp"), searchFileObj);
                }
    
                if (data.getCell11().equals("UFC")) {
                    searchFile(new File("E:\\search\\UFC-WebApp"), searchFileObj);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String messageId = "";
            if (searchFileObj.getResultLine() != null) {
                
                Pattern p2 = Pattern.compile("UF[A-Z]{2}[AT\\d]{2}\\d{3}[A-Z]");
                Matcher matcher2 = p2.matcher(searchFileObj.getResultLine());
                if(matcher2.find()){
                    messageId = matcher2.group();
                }
                outputObj.setCell2(messageId);
            }

            try {
                String resultMessage = null;
                if(messageId.matches("\\w{10}")){
                    if (data.getCell11().equals("UFA")) {
                        resultMessage = findMessage(new File(
                                "D:\\All-In-One-IDE4.7_UF\\workspace\\UFA-WebApp\\WebEnv\\src\\00.default\\etc\\msg\\UxAdmMessage.csv"),
                                messageId);
                    }
        
                    if (data.getCell11().equals("UFC")) {
                        resultMessage = findMessage(new File(
                                "D:\\All-In-One-IDE4.7_UF\\workspace\\UFC-WebApp\\WebEnv\\src\\00.default\\etc\\msg\\UxAdmMessage.csv"),
                                messageId);
                    }
                }
                outputObj.setCell3(resultMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String lineNo = data.getCell8();
        outputObj.setCell4(lineNo);

        String cellE = data.getCell5();
        String cellF = data.getCell6();
        if (cellE.equals("ー") && cellF.matches("\\d+")) {
            outputObj.setCell5("②");
        }
        if (cellF.equals("ー") && cellE.matches("\\d+")) {
            outputObj.setCell5("①");
        }

        outputObj.setCell6(data.getCell3());
        outputObj.setCell7(data.getCell4());

        outputObj.setCell8(classFileName);

        outputList.add(outputObj);


        }

    }

    @Override
    public void doAfterAllAnalysed(final AnalysisContext context) {
        // 创建Excel文件并且写入过滤后的记录列表。
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(new File("C:\\ankiOutput\\result.xlsx"),
                OutputObj.class);
        excelWriterBuilder.sheet(1).doWrite(outputList);
    }

    public String findMessage(File sourceFile, String messageId) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(sourceFile));
        ;
        List<String> message = br.lines().filter(line -> line.contains(messageId)).collect(Collectors.toList());
        br.close();

        System.out.println("结果集大小：" + message.size());
        if(message.size()>0){
            return message.get(0);
        }else{
            return "";
        }
    }

    public void searchFile(final File direction, SearchFileObj searchFileObj) throws FileNotFoundException {

        if (!direction.exists())
            throw new IllegalArgumentException("目录：" + direction + "不存在.");
        if (!direction.isDirectory()) {
            throw new IllegalArgumentException(direction + "不是目录。");
        }

        // 如果要遍历子目录下的内容就需要构造File对象做递归操作，File提供了直接返回File对象的API
        final File[] files = direction.listFiles();
        if (files != null && files.length > 0) {
            for (final File file : files) {
                if (file.isDirectory())
                    // 递归
                    searchFile(file, searchFileObj);
                else {
                    if (file.getName().equals(searchFileObj.fileName)) {
                        final BufferedReader br = new BufferedReader(new FileReader(file));
                        final String line = br.lines().collect(Collectors.toList()).get(searchFileObj.fileNo - 1);
                        searchFileObj.setResultLine(line);
                        try {
                            br.close();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        ExcelObjListener excelObjListener = new ExcelObjListener();
        try {
            SearchFileObj searchFileObj = new SearchFileObj();
            searchFileObj.setFileName("InitNoticeResultsServiceImpl.java");
            searchFileObj.setFileNo(145);
            excelObjListener.searchFile(new File("E:\\search\\UFA-WebApp"), searchFileObj);
            int messageIdFirst = searchFileObj.getResultLine().lastIndexOf("UfBusinessException(\"")
                    + "UfBusinessException(\"".length();
            String messageId = searchFileObj.getResultLine().substring(messageIdFirst, messageIdFirst + 10);
            System.out.println(messageId);

            try {
                String resultMessage = excelObjListener.findMessage(new File(
                        "D:\\All-In-One-IDE4.7_UF\\workspace\\UFA-WebApp\\WebEnv\\src\\00.default\\etc\\msg\\UxAdmMessage.csv"),
                        messageId);
                System.out.println(resultMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}