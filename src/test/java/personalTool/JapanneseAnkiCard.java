package personalTool;

import lombok.Data;

/**
 * JapanneseAnkiCard
 */
@Data
public class JapanneseAnkiCard {
    /**
     * 课程号
     */
    public String lessonName;
    /**
     * 原句
     */
    public String sentenceOrigin;
    /**
     * 对应翻译的中文
     */
    public String sentencetranslated;
    /**
     * 音频文件名
     */
    public String audioSentenceFileName;
}