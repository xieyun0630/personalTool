package top.xieyun.ankiTool.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.excel.EasyExcel;

import top.xieyun.ankiTool.ConverterMarkDownToAnki;
import top.xieyun.selenium.MarkDownHtmlData;

public class ConverterMarkDownToAnkiImpl implements ConverterMarkDownToAnki {

	@Override
	public void parseHTMLMarkdownToExcelFile(String htmlFilePath) {
		// 获取目标文件名
		String targetDirePath = htmlFilePath.substring(0, htmlFilePath.length() - 5) + ".xlsx";
		parseHTMLMarkdownToExcelFile(htmlFilePath, targetDirePath);
	}

	@Override
	public void parseHTMLMarkdownToExcelFile(String htmlFilePath, String targetFilePath) {
		// excel记录列表
		List<MarkDownHtmlData> records = new ArrayList<>();
		// 标题数组
		final List<String> HEADERS = Arrays.asList(new String[] { "h1", "h2", "h3", "h4", "h5", "h6" });
		// HTML文件
		File markedMD = new File(htmlFilePath);
		// excel文件存放位置
		File targetExcel = new File(targetFilePath);

		// 解析html文件
		Document htmlDoc = null;
		try {
			htmlDoc = Jsoup.parse(markedMD, "utf-8");
			// 设置源代码格式为无格式字符
			htmlDoc.outputSettings().indentAmount(0).prettyPrint(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 查找所有标题的父元素
		Element write = htmlDoc.getElementById("write");
		// 获得所有标题与内容
		Elements elements = write.children();

		// 创建Excel文件并且写入过滤后的记录列表。
		EasyExcel.write(targetExcel, MarkDownHtmlData.class).sheet("javascript2").doWrite(records);
	}

	@Override
	public String markedID(String markDownFilePath) {
		// 获取目标文件名
		String targetDirePath = markDownFilePath.substring(0, markDownFilePath.length() - 3) + "_marked.md";
		return markedID(markDownFilePath, targetDirePath);
	}

	/**
	 * 为每一个标题添加一个唯一的标识，主要为文件名加时间戳。 Anki中不需要顺序问题，只需要一个唯一标识用以保存学习记录以及更新卡片笔记。
	 *
	 * @param markDownFilePath 要添加标题标识的markdown文档的路径
	 * @param targetDirePath   添加后的markdown文档存放路径
	 */
	@Override
	public String markedID(String markDownFilePath, String targetDirePath) {
		// 获取不带后缀的文件名
		String fileName = markDownFilePath.substring(markDownFilePath.lastIndexOf("\\") + 1);
		fileName = fileName.substring(0, fileName.length() - 3);

		// 保存处理后得markdown文本,读取时自动丢失了换行符，这里得添上
		StringJoiner content = new StringJoiner("\n");
		// 保存新标记的笔记。
		StringJoiner newMarkedContent = new StringJoiner("\n");
		try {
			// 创建文件的reader
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(markDownFilePath)));

			// 一行代码将整个文件按行读取为List，这里会丢失换行符
			List<String> lines = new BufferedReader(new FileReader(markDownFilePath)).lines()
					.collect(Collectors.toList());

			// 判断当前行是否处新添加的笔记状态
			boolean isNewNote = false;
			// 判断当前行是否处于代码块flag
			boolean isCodeFlag = false;
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				// 忽略代码中的#符号
				if (line.trim().startsWith("```")) {
					isCodeFlag = !isCodeFlag;
				}
				// 判断当前行是否是标题行
				if (!isCodeFlag && getTitleGrade(line) > 0 && getTitleGrade(line) < 7) {

					// 判断当前行是否已经有ID标识
					int lastLabelIndex = line.lastIndexOf("[\t]");
					String substring = "";
					if (lastLabelIndex > 0) {
						substring = line.substring(lastLabelIndex);
					}
					if (substring.isEmpty()) {
						isNewNote = true;
						// 没有ID标识情况
						String currentTime = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
						Thread.sleep(1);
						lines.set(i, line + " [\t](" + fileName + "_" + currentTime + ")");
					} else {
						isNewNote = false;
					}
				}
				if (isNewNote) {
					newMarkedContent.add(lines.get(i));
				}
			}

			lines.forEach(content::add);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (newMarkedContent.length() != 0) {
				// 文件有过修改
				// 将处理后的文本写入文件
				Files.write(Paths.get(targetDirePath), content.toString().getBytes());
				return newMarkedContent.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(fileName + ":文件没有新的笔记");
		// 没有新笔记返回null。
		return null;
	}

	@Override
	public void patchMarkedID(File direction) throws FileNotFoundException {
		if (!direction.exists())
			throw new IllegalArgumentException("目录：" + direction + "不存在.");
		if (!direction.isDirectory()) {
			throw new IllegalArgumentException(direction + "不是目录。");
		}

		// 如果要遍历子目录下的内容就需要构造File对象做递归操作，File提供了直接返回File对象的API
		File[] files = direction.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isDirectory())
					// 递归
					patchMarkedID(file);
				else {
					if (file.getName().endsWith(".md")) {
						markedID(file.getAbsolutePath());
					}
				}
			}
		}
	}

	/**
	 * 输入一行markdown文本判断是否是标题行。
	 *
	 * @param title 一行markdown格式的文本。
	 * @return 判断是否是标题，是返回标题级数，不是标题返回-1。
	 */
	@Override
	public int getTitleGrade(String title) {
		int titleGrade = 0;
		title = title.trim();
		String[] titleCodes = new String[] { "#", "##", "###", "####", "#####", "######" };
		for (String x : titleCodes) {
			if (title.startsWith(x)) {
				return x.length();
			}
		}
		return -1;
	}
}
