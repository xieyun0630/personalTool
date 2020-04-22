package top.xieyun.UfExcelVBA;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;

/**
 * UxTool
 */
public class UxTool {
    String sourceExcel = "C:\\ankiOutput\\testSheet.xlsx";
    public void readExcel(){
        EasyExcel.read(sourceExcel, ExcelObj.class, new ExcelObjListener())
        .extraRead(CellExtraTypeEnum.MERGE)
        .sheet("Sheet1").doRead();
    }
    public static void main(String[] args) {
        new UxTool().readExcel();
        System.out.println("ok!!!!!!!");
    }
}
