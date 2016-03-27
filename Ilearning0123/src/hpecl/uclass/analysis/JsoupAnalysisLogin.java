/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：JsoupAnalysisLogin.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/02
* Description：This java file is used for returning login information.
* Modify history：
* 郭靖东,create file,2015/07/02
*/
package hpecl.uclass.analysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupAnalysisLogin extends SuperAnalysis {

	@Override
	public Object analysisInfo(String getinfo) {
		// TODO Auto-generated method stub
		super.analysisInfo(getinfo);
		Document doc =  Jsoup.parse(getinfo);
	    Elements link = doc.select("b");
	    String url = link.text();
		return url;
	    
		
	}
	
}
