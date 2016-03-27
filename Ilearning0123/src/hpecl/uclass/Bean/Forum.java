/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Forum.java
* Author：郭靖东
* Version：1.0
* Date：2015/07/12
* Description：This java file is the object class, include four private params.
* Modify history：
* 郭靖东,create file,2015/07/12
*/
package hpecl.uclass.Bean;

public class Forum {
	private String name;
	private String content;
	private String time;
	private String reply;
	
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	public Forum(String name, String content,String time,String reply) {
		super();
		this.name = name;
		this.content = content;
		this.time = time;
		this.reply = reply;
	}
	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}
	public Forum(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
