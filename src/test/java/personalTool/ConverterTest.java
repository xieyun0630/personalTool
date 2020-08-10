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
		final File file = new File("C:\\Users\\Yun\\Desktop\\note\\CSNote");
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
	public void parserFromDir() {
		parser.parseHTMLMarkdownFromDirection(new File("C:\\ankiOutput"));
	}

	@Test
	public void parser() {
		parser.parseHTMLMarkdownToExcelFile("C:\\ankiOutput\\vue.html");
		parser.parseHTMLMarkdownToExcelFile("C:\\ankiOutput\\mybatis.html");
		parser.parseHTMLMarkdownToExcelFile("C:\\ankiOutput\\css.html");
	}

	@Test 
	public void testAnkiTool() {
		File sourcePath = new File("C:\\Users\\Yun\\Desktop\\note\\CSNote");
		File targetPath = new File("C:\\Users\\Yun\\AppData\\Roaming\\Anki2\\Yun\\collection.media");
		try {
			AnkiTool.moveImagesToTargetPath(sourcePath, targetPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
