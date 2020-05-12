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
        String result = matchString(data.getCell1(), "\\w+.java");
        data.setCell1(result);
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(final AnalysisContext context) {
        List<ExcelObj> filteredList = list.stream().filter(o -> o.getCell9().contains("○"))
                .collect(Collectors.toList());

        String currentCell1 = filteredList.get(0).getCell1();
        ;
        for (ExcelObj o : list) {
            if (o.getCell1().isEmpty()) {
                o.setCell1(currentCell1);
            } else {
                currentCell1 = o.getCell1();
            }
        }

        String currentCell2 = filteredList.get(0).getCell2();
        ;
        for (ExcelObj o : list) {
            if (o.getCell2().isEmpty()) {
                o.setCell2(currentCell2);
            } else {
                currentCell2 = o.getCell2();
            }
        }

        String currentCell3 = filteredList.get(0).getCell3();
        ;
        for (ExcelObj o : list) {
            if (o.getCell3().isEmpty()) {
                o.setCell3(currentCell3);
            } else {
                currentCell3 = o.getCell3();
            }
        }

        String currentCell4 = filteredList.get(0).getCell4();
        ;
        for (ExcelObj o : list) {
            if (o.getCell4().isEmpty()) {
                o.setCell4(currentCell4);
            } else {
                currentCell4 = o.getCell4();
            }
        }

        String currentCell5 = filteredList.get(0).getCell5();
        ;
        for (ExcelObj o : list) {
            if (o.getCell5().isEmpty()) {
                o.setCell5(currentCell5);
            } else {
                currentCell5 = o.getCell5();
            }
        }

        String currentCell6 = filteredList.get(0).getCell6();
        ;
        for (ExcelObj o : list) {
            if (o.getCell6().isEmpty()) {
                o.setCell6(currentCell6);
            } else {
                currentCell6 = o.getCell6();
            }
        }

        for (ExcelObj excelObj : filteredList) {

            String sheetName = excelObj.getCell2() + "-" + excelObj.getCell7();
            excelObj.setSheetName(sheetName);

            String sheetType = "";
            if (excelObj.getCell5().matches("\\d{0,2}")) {
                sheetType = "①";
            } else if (excelObj.getCell6().matches("\\d{0,2}")) {
                sheetType = "②";
            }
            excelObj.setSheetType(sheetType);

            String messageId = "";
            SearchFileObj searchFileObj = new SearchFileObj();
            searchFileObj.setFileName(excelObj.getCell1());
            searchFileObj.setFileNo(Integer.parseInt(excelObj.getCell8()));
            try {
                if (excelObj.getCell10().equals("UFA")) {
                    searchFile(new File("D:\\UF_SVN\\060_プログラミング\\202004_UF改修\\UFA-WebApp"), searchFileObj);
                } else if (excelObj.getCell10().equals("UFC")) {
                    searchFile(new File("D:\\UF_SVN\\060_プログラミング\\202004_UF改修\\UFC-WebApp"), searchFileObj);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            messageId = matchString(searchFileObj.getResultLine(), "U[FI][A-Z]{2}[AT\\d]{2}\\d{3}[A-Z]");
            excelObj.setMessageId(messageId);

            String message = "";
            try {
                if (excelObj.getCell10().equals("UFA")) {
                    message = findMessage(new File(
                            "D:\\All-In-One-IDE4.7_UF\\workspace\\UFA-WebApp\\WebEnv\\src\\00.default\\etc\\msg\\UxAdmMessage.csv"),
                            messageId);
                } else if (excelObj.getCell10().equals("UFC")) {
                    message = findMessage(new File(
                            "D:\\All-In-One-IDE4.7_UF\\workspace\\UFC-WebApp\\WebEnv\\src\\00.default\\etc\\msg\\UxAdmMessage.csv"),
                            messageId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            excelObj.setMessage(message);
            if(message.length()>10){
                excelObj.setMessageType("" + message.charAt(message.length()-1));
            }

        }

        // 创建Excel文件并且写入过滤后的记录列表。
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(new File("C:\\ankiOutput\\result.xlsx"),
                ExcelObj.class);
        excelWriterBuilder.sheet(1).doWrite(filteredList);
    }

    public String matchString(String str, String regex) {
        if(str !=null && !str.isEmpty()){
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(str);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "";
    }

    public String findMessage(File sourceFile, String messageId) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(sourceFile));
        List<String> message = br.lines().filter(line -> line.contains(messageId)).collect(Collectors.toList());
        br.close();
        System.out.println("结果集大小：" + message.size());
        if (message.size() > 0) {
            return message.get(0);
        } else {
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
                        try{
                            final String line = br.lines().collect(Collectors.toList()).get(searchFileObj.fileNo - 1);
                            searchFileObj.setResultLine(line);
                        }catch(Exception e){
                            System.out.println("出错对象："+searchFileObj);
                        }
                        
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