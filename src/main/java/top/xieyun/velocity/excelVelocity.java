package top.xieyun.velocity;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class excelVelocity {
    public static void main(String[] args) {
        String fileName = "C:\\Personal Project\\demo\\src\\main\\resources\\template\\EX.xlsx";
        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, ExcelPojo.class, new ExcelListener()).sheet().doRead();
        
    }
}
