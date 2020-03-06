package top.xieyun.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.pegdown.PegDownProcessor;

public class PageGenerator {

	public void generateHtml(File mdFile) throws IOException {
		String s = FileUtils.readFileToString(mdFile, "utf-8");
		PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
		String s1 = pdp.markdownToHtml(s);
		System.out.println(s1);
	}

	public static void main(String[] args) throws IOException {
		PageGenerator pageGenerator = new PageGenerator();
		pageGenerator.generateHtml(new File("C:\\Users\\hezp\\Desktop\\mdnote\\javascript_info.md"));
	}

}