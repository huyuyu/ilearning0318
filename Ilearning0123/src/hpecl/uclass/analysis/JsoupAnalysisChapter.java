/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：JsoupAnalysisChapter.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/29
* Description：This java file is used for analysis concern reading.
* Modify history：
* 郭靖东,create file,2015/08/29
*/
package hpecl.uclass.analysis;

import hpecl.uclass.Bean.Article;
import hpecl.uclass.uti.Constant;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;

public class JsoupAnalysisChapter extends SuperAnalysis{
	
	ArrayList<Article> articlearry = new ArrayList<Article>();
	
	@Override
	public Object analysisInfo(String getinfo) {
		
		Document doc =  Jsoup.parse(getinfo);
	       
		Elements tests = doc.select("ul li a");
	    Log.i("cq", "1109tests"+tests);
	        
	    for(Element a : tests){
	    	if(!(a.attr("href").contains(Constant.A701BEFOR)))
				articlearry.add(new Article(Constant.A701BEFOR+a.attr("href"), a.attr("title")));
//	        	else
//	        		notificationArr.add(new Article(a.attr("href"), a.attr("title")));
        	}

		return articlearry;
	}
}
