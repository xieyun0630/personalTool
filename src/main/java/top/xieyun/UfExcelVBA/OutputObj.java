package top.xieyun.UfExcelVBA;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class OutputObj {
    /**
     * sheetName
     */
    @ExcelProperty("sheetName")
    private String cell1 = "";
    /**
     * messageID
     */
    @ExcelProperty("messageID")
    private String cell2 = "";
    /**
     * message内容
     */
    @ExcelProperty("message内容")
    private String cell3 = "";
    /**
     * 改修ソースline
     */
    @ExcelProperty("改修ソースline")
    private String cell4 = "";
    /**
     * 修正点
     */
    @ExcelProperty("修正点")
    private String cell5 = "";
    /**
     * 功能名
     */
    @ExcelProperty("功能名")
    private String cell6 = "";
    /**
     * 画面名
     */
    @ExcelProperty("画面名")
    private String cell7 = "";
    /**
     * className
     */
    @ExcelProperty("className")
    private String cell8 = "";
}