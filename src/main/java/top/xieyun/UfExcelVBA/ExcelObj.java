package top.xieyun.UfExcelVBA;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * ExcelObj
 */
@Data
public class ExcelObj {
    
    @ExcelProperty
    private String cell1 = "";

    @ExcelProperty
    private String cell2 = "";

    @ExcelProperty
    private String cell3 = "";

    @ExcelProperty
    private String cell4 = "";

    @ExcelProperty
    private String cell5 = "";

    @ExcelProperty
    private String cell6 = "";

    @ExcelProperty
    private String cell7 = "";

    @ExcelProperty
    private String cell8 = "";

    @ExcelProperty
    private String cell9 = "";

    @ExcelProperty
    private String cell10 = "";

    @ExcelProperty
    private String sheetName = "";

    @ExcelProperty
    private String sheetType = "";

    @ExcelProperty
    private String messageId = "";

    @ExcelProperty
    private String message = "";

    @ExcelProperty
    private String messageType = "";
}