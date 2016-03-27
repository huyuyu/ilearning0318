/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：JsoupAnalysisRegister.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/25
* Description：This java file is used for analysis register information and get the group id value.
* Modify history：
* 郭靖东,create file,2015/08/25
*/
package hpecl.uclass.analysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupAnalysisRegister extends SuperAnalysis {

	@Override
	public Object analysisInfo(String getinfo) {
		// TODO Auto-generated method stub
		super.analysisInfo(getinfo);
		Document doc =  Jsoup.parse(getinfo);
	    Element link = doc.getElementById("groupid");
	    String value = link.attr("value").toString();
	    
		return value;
	}
	
}
