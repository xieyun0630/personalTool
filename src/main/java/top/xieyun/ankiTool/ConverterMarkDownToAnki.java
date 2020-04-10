package top.xieyun.ankiTool;

import java.io.File;
import java.io.FileNotFoundException;

public interface ConverterMarkDownToAnki {
    /**
     * 为每一个标题添加一个唯一的标识，主要为文件名加时间戳。 Anki中不需要顺序问题，只需要一个唯一标识用以保存学习记录以及更新卡片笔记。
     * 
     * @param markDownFilePath 要添加标题标识的markdown文档的路径
     * @param targetDirePath   添加后的markdown文档存放路径
     */
    String markedID(String markDownFilePath, String targetDirePath);

    /**
     * 为每一个标题添加一个唯一的标识，主要为文件名加时间戳。 Anki中不需要顺序问题，只需要一个唯一标识用以保存学习记录以及更新卡片笔记。
     * 默认将生成的新文件夹以fileName_marked.md命名。 直接放在markDownFilePath相同路径下
     * 
     * @param markDownFilePath 要添加标题标识的markdown文档的路径
     * @return 返回添加了ID标识的Markdown笔记
     */
    String markedID(String markDownFilePath);

    /**
     * 指定一个文件夹，将此文件夹下所有的markdown文件加上标识。
     *
     * @param noteDirectionPath
     */
    void patchMarkedID(File noteDirectionPath) throws FileNotFoundException;

    /**
     * 输入一行markdown文本判断是否是标题行。
     * 
     * @param title 一行markdown格式的文本。
     * @return 判断是否是标题，是返回标题级数，不是标题返回-1。
     */
    int getTitleGrade(String title);

    /**
     * 将HTML文档转换为以标题为单位的Excel文件,转换到htmlFilePath是相同路径下。
     * 
     * @param htmlFilePath markdown转换后的HTML
     */
    void parseHTMLMarkdownToExcelFile(String htmlFilePath);

    /**
     * 将HTML文档转换为以标题为单位的Excel文件
     * 
     * @param htmlFilePath   markdown转换后的HTML
     * @param targetFilePath 转换后的Excel文件
     */
    void parseHTMLMarkdownToExcelFile(String htmlFilePath, String targetFilePath);
}
