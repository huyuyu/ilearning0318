/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：JsoupAnalysisNotification.java
* Author：常青
* Version：1.1
* Date：2015/07/31
* Description：This java file is used for analysis notification.
* Modify history：
* 常青,create file,2015/07/31
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


public class JsoupAnalysisNotification extends SuperAnalysis{
	
	ArrayList<Article> notificationArr = new ArrayList<Article>();
	
	@Override
	public Object analysisInfo(String getinfo) {
//		Log.i("cq ", "0820  "+getinfo);

		Document doc =  Jsoup.parse(getinfo);//解析HTML字符串返回一个Document实现
		Log.i("cq", "0820doc"+doc);
		
		Elements littleTitle = doc.getElementsByClass("box");//取出class标签为box的元素集
		Log.i("cq", "0820littleTitle"+littleTitle);

		//第一个box
		Element test = littleTitle.get(0);
	    Log.i("cq", "0820test"+test);
	       
		Elements tests = test.select("ul li a");//多个选择器组合，查找匹配任一选择器的唯一元素
	    Log.i("cq", "1109tests"+tests);
	        
	    for(Element a : tests){
	       	notificationArr.add(new Article(Constant.A701BEFOR+a.attr("href"), a.attr("title")));
       	}
        for (Article article : notificationArr) {
        	Log.i("cq", "1109"+article.getTitle());
        	Log.i("cq", "1109"+article.getHref());
		}

		return notificationArr;
	}

	

	
}
