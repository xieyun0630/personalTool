package personalTool;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import top.xieyun.ankiTool.ConverterMarkDownToAnki;
import top.xieyun.ankiTool.Impl.ConverterMarkDownToAnkiImpl;

public class test {
	ConverterMarkDownToAnki converterMarkDownToAnki = new ConverterMarkDownToAnkiImpl();

	@Test
	public void testMarkedID() {
		converterMarkDownToAnki.markedID("C:\\note\\codeNote\\02 javascript语言\\javascript_info.md");
	}

	@Test
	public void testParseHTML() {
		converterMarkDownToAnki
				.parseHTMLMarkdownToExcelFile("C:\\Users\\Administrator\\Desktop\\mdnote\\javascript_info.html");
	}

	@Test
	public void testPatch() throws FileNotFoundException {
		File file = new File("C:\\Users\\Administrator\\Desktop\\codeNote");
		converterMarkDownToAnki.patchMarkedID(file);
	}
}
