package top.xieyun.velocity;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.velocity.runtime.RuntimeConstants.*;

public class ExcelListener extends AnalysisEventListener<ExcelPojo> {
    /**
     * 自定义用于暂时存储data。
     * 可以通过实例获取该值
     */
    private final List<ExcelPojo> records = new ArrayList<>();

    @Override
    public void invoke(ExcelPojo object, AnalysisContext context) {
    	System.out.println(object.toString());
    	if(object.getXML_ELEMENTLIST1() != null)
    	object.setXML_ELEMENTLIST1(object.getXML_ELEMENTLIST1().replace("\"", ""));
    	if(object.getXML_ELEMENTLIST2() != null)
    	object.setXML_ELEMENTLIST2(object.getXML_ELEMENTLIST2().replace("\"", ""));
    	if(object.getXML_ELEMENTLIST3() != null)
    	object.setXML_ELEMENTLIST3(object.getXML_ELEMENTLIST3().replace("\"", ""));
        records.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        
        for(ExcelPojo pojo:this.records) {
       	 // 设置变量
            VelocityContext ctx = new VelocityContext();
            Template template = ve.getTemplate("template\\temp.vm","utf-8");
            ctx.put("EX",pojo);
            // 输出
            StringWriter sw = new StringWriter();
            template.merge(ctx,sw);

            try {
                String fileName = pojo.getWS_FORM()+".bas";
                FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Administrator\\Desktop\\project\\testTemplate\\" +fileName));
                fileWriter.append(sw.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}}