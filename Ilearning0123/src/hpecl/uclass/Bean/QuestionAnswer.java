/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：QuestionAnswer.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/12
* Description：This java file is the object class, include four private params.
* Modify history：
* 郭靖东,create file,2015/08/12
*/
package hpecl.uclass.Bean;

public class QuestionAnswer {
	private String question;
	private String asker;
	private String questionContent;
	private String url;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public QuestionAnswer(String question, String asker, String questionContent, String url) {
		super();
		this.question = question;
		this.asker = asker;
		this.questionContent = questionContent;
		this.url = url;
		
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAsker() {
		return asker;
	}
	public void setAsker(String asker) {
		this.asker = asker;
	}
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	
	
	
}
