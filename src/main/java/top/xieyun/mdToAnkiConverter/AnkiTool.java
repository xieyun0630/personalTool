package top.xieyun.mdToAnkiConverter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class AnkiTool {
    /**
     * 将源文件夹下所有的图片都拷贝到同一目标目录下
     * 
     * @param sourcePath 源文件夹目录
     * @param targetPath 目标文件夹目录
     */
    public static void moveImagesToTargetPath(File sourcePath, File targetPath) throws IOException {
        if (!sourcePath.exists())
            throw new IllegalArgumentException("目录：" + sourcePath + "不存在.");
        if (!sourcePath.isDirectory()) {
            throw new IllegalArgumentException(sourcePath + "不是目录。");
        }

        // 如果要遍历子目录下的内容就需要构造File对象做递归操作，File提供了直接返回File对象的API
        File[] files = sourcePath.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory())
                    // 递归
                    moveImagesToTargetPath(file,targetPath);
                else {
                    if (file.getName().endsWith(".png")) {
                        File targetFile= new File(targetPath.getAbsolutePath()+"\\"+file.getName());
                        FileUtils.copyFile(file, targetFile);
                    }
                }
            }
        }
    }
}