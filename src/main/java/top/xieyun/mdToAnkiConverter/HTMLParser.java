package top.xieyun.mdToAnkiConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;

import top.xieyun.selenium.MarkDownHtmlData;

/**
 * 分析对象：分析指定文件夹下所有HTML为anki格式的txt文本到该文件夹下
 * 
 * @author Administrator
 *
 */
public class HTMLParser {
	public static void main(String[] args) {
		new HTMLParser().parseHTMLMarkdownToExcelFile("C:\\ankiOutput\\javascript_info.html", "C:\\ankiOutput\\javascript_info.xlsx");
	}
	
    public void parseHTMLMarkdownToExcelFile(String htmlFilePath) {
        //获取目标文件名
        String targetDirePath = htmlFilePath.substring(0, htmlFilePath.length() - 5) + ".xlsx";
        parseHTMLMarkdownToExcelFile(htmlFilePath, targetDirePath);
    }

    public void parseHTMLMarkdownToExcelFile(String htmlFilePath, String targetFilePath) {
        //excel记录列表
        List<MarkDownHtmlData> records = new ArrayList<>();
        
		//读取时自动丢失了换行符，这里得添上
		StringJoiner content = new StringJoiner("\n");

        //标题数组
        final List<String> HEADERS = Arrays.asList(new String[]{"h1", "h2", "h3", "h4", "h5", "h6"});
        //HTML文件
        File markedMD = new File(htmlFilePath);
        //excel文件存放位置
        File targetExcel = new File(targetFilePath);

        // 解析html文件
        Document htmlDoc = null;
        try {
            htmlDoc = Jsoup.parse(markedMD, "utf-8");
            //设置源代码格式为无格式字符
            htmlDoc.outputSettings().indentAmount(0).prettyPrint(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //查找所有标题的父元素
        Element write = htmlDoc.getElementById("write");
        //获得所有标题与内容
        Elements elements = write.children();


        //循环单个excel记录的读取
        MarkDownHtmlData record = null;
        for (Element element : elements) {
            if (HEADERS.contains(element.tagName())) {
                // 如果当前遍历的元素是标题元素的情况
                if (record != null) {
                    //判断上一个excel记录是否为空,不为空则添加到列表。
                    records.add(record);
                }

                // 新记录:保存从html中读取的元素
                record = new MarkDownHtmlData();

                // 读取所有的链接元素
                Elements allA = element.getElementsByTag("a");
                // 获取到保存笔记ID的<a>元素的href中笔记ID的信息
                String href = allA.get(allA.size() - 1).attr("href");
                // 将笔记ID提取到记录中
                record.setSortedFiled(href);
                // 将标题元素提取到记录中
                record.setProblem(element.toString());

                // 测试：打印
                System.out.println(record.getSortedFiled() + "::" + element);
            } else {
                // 如果当前遍历的元素不是标题元素的情况
                if (record != null) {
                    // 將内容元素保存到记录中答案属性中
                    Elements imgs = element.getElementsByTag("img");
                    imgs.forEach(img -> {
                        String src = img.attr("src");
                        String imgPath = src.substring(src.indexOf("/") + 1);
                        img.attr("src", imgPath);
                    });
                    String answer = record.getAnswer() + element.outerHtml();
                    record.setAnswer(answer);
                }
            }
        }

        System.out.println("过滤前卡片组的大小：" + records.size());

        //过滤掉没有内容的的记录。
        records = records.stream()
                .filter(markDownHtmlData -> {
                    //过滤掉没有内容的的记录。
                    return markDownHtmlData.getAnswer() != null && !markDownHtmlData.getAnswer().isEmpty();
                })
                .collect(Collectors.toList());

        System.out.println("过滤后卡片组的大小：" + records.size());

        //获取文件名
        String HTMLFileName = new File(htmlFilePath).getName();
        String fileName = HTMLFileName.substring(0, HTMLFileName.length()-5);
        
        //将问题加到anki格式的背面
        records.replaceAll(o -> {
            o.setAnswer(o.getProblem() + o.getAnswer());
            o.setLabel(fileName);
            return o;
        });

        // 创建Excel文件并且写入过滤后的记录列表。
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(targetExcel, MarkDownHtmlData.class);
        excelWriterBuilder.sheet(1).doWrite(records);
    }
}
