package top.xieyun.UfExcelVBA;

import com.alibaba.excel.EasyExcel;

/**
 * UxTool
 */
public class UxTool {
    String sourceExcel = "D:\\UF_SVN\\300_プロジェクト管理\\202004_UF改修\\作業一覧.xlsx";
    public void readExcel(){
        EasyExcel.read(sourceExcel, ExcelObj.class, new ExcelObjListener())
        .sheet("作業一覧").doRead();
    }
    public static void main(String[] args) {
        new UxTool().readExcel();
        System.out.println("abc");
    }
}
