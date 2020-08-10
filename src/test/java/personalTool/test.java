package personalTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class test {
    @Test
    public void method1() {
        for (int i = 1; i < 40; i++) {
            System.out.println("@ExcelProperty(\"column\")");
            System.out.println("private String cell" + i + " = \"\";");
        }
    }

    @Test
    public void changeFileName() {
        List<JapanneseAnkiCard> cards = new ArrayList<>();
        File file = new File("E:\\新标日语mp3素材");
        Arrays.stream(file.listFiles()).sorted((file1, file2) -> {
            return file1.getName().compareTo(file2.getName());
        }).forEach(lessonDir -> {
            JapanneseAnkiCard card = new JapanneseAnkiCard();
            for (File sentenceFile : lessonDir.listFiles()) {
                card.setLessonName(sentenceFile.getName());
            }
        });
    }

    @Test
    public void testReg(){
        System.out.println(trimMultibyteSpace(null)== null);
        System.out.println(trimMultibyteSpace("").equals( ""));
        System.out.println(trimMultibyteSpace("12").equals( "12"));
        System.out.println(trimMultibyteSpace("a b").equals( "a b"));
        System.out.println(trimMultibyteSpace("      a b        ").equals( "a b"));
        System.out.println(trimMultibyteSpace("    a b").equals( "a b"));
        System.out.println(trimMultibyteSpace("a b              ").equals( "a b"));
        System.out.println(trimMultibyteSpace("").equals( ""));
        System.out.println(trimMultibyteSpace(" ").equals( ""));
    }

    public static String trimMultibyteSpace(final String sourceString) {
        if (sourceString == null || sourceString.length() == 0) {
            return sourceString;
        }
        // 空白のみのパターンのみ先に戻します.
        if (Pattern.matches("^([\\s\\u3000]*)$", sourceString)) {
            return "";
        }
        // 第一段階で後続文字列のtrimをおこないます.
        final Pattern firstTrimPattern
                = Pattern.compile("^(.*)([^\\s\\u3000])([\\s\\u3000]+)$");
        final Matcher firstMatcher = firstTrimPattern.matcher(sourceString);
        String result = firstMatcher.replaceAll("$1$2");
        // 第二段階で先行文字列のtrimをおこないます.
        final Pattern secondTrimPattern
                = Pattern.compile("^([\\s\\u3000]+)([^\\s\\u3000])(.*)$");
        final Matcher secondMatcher = secondTrimPattern.matcher(result);
        result = secondMatcher.replaceAll("$2$3");
        return result;
    }
}