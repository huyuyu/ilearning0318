/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Article.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/04
* Description：This java file is the object class, include double private params.
* Modify history：
* 郭靖东,create file,2015/07/04
*/
package hpecl.uclass.Bean;

public class Article {
	private String	href;
	private String	title;

	public Article(String href, String title) {
		this.href = href;
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
