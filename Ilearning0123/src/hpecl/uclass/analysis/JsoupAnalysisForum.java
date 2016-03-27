/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：JsoupAnalysisForum.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/29
* Description：This java file is used for analysis forum.
* Modify history：
* 郭靖东,create file,2015/07/29
*/
package hpecl.uclass.analysis;

import hpecl.uclass.Bean.Forum;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;


public class JsoupAnalysisForum extends SuperAnalysis{
	
	ArrayList<Forum> forumArr = new ArrayList<Forum>();
	
	@Override
	public Object analysisInfo(String getinfo) {
		Log.i("guo ", "0818  "+getinfo);
		Document doc =  Jsoup.parse(getinfo);
	    Elements names = doc.getElementsByClass("name");
	    Elements contents = doc.getElementsByClass("text");
	    Elements times = doc.select("td").select("table").select("td").select("div").select("font");
	    Elements reply = doc.getElementsByClass("re").select("a");
	    
		    for(int i = 0, j = 0;i<names.size();i++,j+=3){	
		    	if(!contents.get(i).select("table").text().isEmpty()){
		    		String [] tempReply = reply.get(j).attr("onclick").split(";");
		    		String [] numbers = tempReply[1].split("'");
		    		String temp = contents.get(i).select("table").text();
//		    		String subTemp = temp.substring(temp.length()*3/4,temp.length());
		    		String  text = contents.get(i).text().substring(temp.length(),contents.get(i).text().length());
		    		Log.i("guo", "0910  "+temp);
//		    		if(text.length >1 )
		    		forumArr.add(new Forum(names.get(i).text(), temp + "\n"+"\n" +"回复："+text,times.get(i).text(),numbers[1]));
//		    		else
//		    		forumArr.add(new Forum(names.get(i).text(), text[0],times.get(i).text(),numbers[1]));
		    		
		    	}
		    		
		    		 
		    	else{
		    		String [] tempReply = reply.get(j).attr("onclick").split(";");
		    		String [] numbers = tempReply[1].split("'");
		    		forumArr.add(new Forum(names.get(i).text(), contents.get(i).text(),times.get(i).text(),numbers[1]));
		    	}
		    
	    }

	     for (Forum forum : forumArr) {
	     		Log.i("guo", "0626   "+forum.getName());
	     		Log.i("guo", "0626   "+forum.getContent());
	     		Log.i("guo", "0626   "+forum.getTime());
	     		Log.i("guo", "0626   "+forum.getReply());
	     		
			}

		return forumArr;
	}

	

	
}
