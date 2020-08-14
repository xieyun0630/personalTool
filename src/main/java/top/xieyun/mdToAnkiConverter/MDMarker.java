package top.xieyun.mdToAnkiConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 标记对象：标记md笔记中的标题行
 * 
 * @author Administrator
 *
 */
public class MDMarker {
	/**
	 * 为每一个标题添加一个唯一的标识，主要为文件名加时间戳。 Anki中不需要顺序问题，只需要一个唯一标识用以保存学习记录以及更新卡片笔记。
	 * 
	 * @param markDownFilePath 要添加标题标识的markdown文档的路径
	 * @return 返回添加了ID标识的Markdown笔记
	 */
	public String markedID(String markDownFilePath) {
		return markedID(markDownFilePath, markDownFilePath);
	}

	/**
	 * 为每一个标题添加一个唯一的标识，主要为文件名加时间戳。 Anki中不需要顺序问题，只需要一个唯一标识用以保存学习记录以及更新卡片笔记。
	 * 
	 * @param markDownFilePath 要添加标题标识的markdown文档的路径以及文件名
	 * @param targetDirePath   添加后的markdown文档存放路径以及文件名
	 */
	public String markedID(String markDownFilePath, String targetDirePath) {
		// 获取不带后缀的文件名
		String fileName = markDownFilePath.substring(markDownFilePath.lastIndexOf("\\") + 1);
		fileName = fileName.substring(0, fileName.length() - 3);

		// 保存处理后得markdown文本,读取时自动丢失了换行符，这里得添上
		StringJoiner content = new StringJoiner("\n");

		// 保存新标记的笔记。
		StringJoiner newMarkedContent = new StringJoiner("\n");

		try (BufferedReader br = new BufferedReader(new FileReader(markDownFilePath))) {
			// 一行代码将整个文件按行读取为List，这里会丢失换行符
			List<String> lines = br.lines().collect(Collectors.toList());

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
					String substring = "";

					int lastLabelIndexWithTab = line.lastIndexOf("[\t]");
					if (lastLabelIndexWithTab > 0) {
						substring = line.substring(lastLabelIndexWithTab);
					}

					int lastLabelIndexWithWhiteSpace = line.lastIndexOf("[ ]");
					if (lastLabelIndexWithWhiteSpace > 0) {
						substring = line.substring(lastLabelIndexWithWhiteSpace);
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
	
    public void patchMarkedID(File direction) throws FileNotFoundException {
        if (!direction.exists())
            throw new IllegalArgumentException("目录：" + direction + "不存在.");
        if (!direction.isDirectory()) {
            throw new IllegalArgumentException(direction + "不是目录。");
        }

        //如果要遍历子目录下的内容就需要构造File对象做递归操作，File提供了直接返回File对象的API
        File[] files = direction.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory())
                    //递归
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
	public int getTitleGrade(String title) {
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
