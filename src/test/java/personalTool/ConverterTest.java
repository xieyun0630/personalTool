package personalTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import top.xieyun.mdToAnkiConverter.AnkiTool;
import top.xieyun.mdToAnkiConverter.HTMLParser;
import top.xieyun.mdToAnkiConverter.MDMarker;
import top.xieyun.mdToAnkiConverter.MDMerger;

public class ConverterTest {
	HTMLParser parser = new HTMLParser();
	MDMarker marker = new MDMarker();
	MDMerger merger = new MDMerger();

	@Test
	public void marker() {
		final File file = new File("C:\\note\\CSNote");
		try {
			marker.patchMarkedID(file);
		} catch (final FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	@Test
	public void merger() {

	}

	@Test
	public void parser() {
		parser.parseHTMLMarkdownToExcelFile("C:\\ankiOutput\\maven.html");
		parser.parseHTMLMarkdownToExcelFile("C:\\ankiOutput\\java_se.html");
	}

	@Test
	public void testAnkiTool() {
		File sourcePath = new File("C:\\note\\CSNote");
		File targetPath = new File("D:\\temp");
		try {
			AnkiTool.moveImagesToTargetPath(sourcePath, targetPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
