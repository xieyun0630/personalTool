package top.xieyun.selenium;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * anki的卡片对象，每一个对象对应一个anki卡片
 */

public class MarkDownHtmlData {
	// 用于标识卡片的ID属性
	@ExcelProperty("sortedFiled")
	private String sortedFiled = "";
	// 问题字段，将问题单独提取出来放在一个字段
	@ExcelProperty("问题")
	private String problem = "";
	// 填空题卡片
	@ExcelProperty("答案")
	private String answer = "";

	public MarkDownHtmlData() {
		super();
	}

	public MarkDownHtmlData(String sortedFiled, String problem, String answer) {
		super();
		this.sortedFiled = sortedFiled;
		this.problem = problem;
		this.answer = answer;
	}

	public String getSortedFiled() {
		return sortedFiled;
	}

	public void setSortedFiled(String sortedFiled) {
		this.sortedFiled = sortedFiled;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "MarkDownHtmlData [sortedFiled=" + sortedFiled + ", problem=" + problem + ", answer=" + answer + "]";
	}

}
