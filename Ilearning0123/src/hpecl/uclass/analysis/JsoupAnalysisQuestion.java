/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：JsoupAnalysisQuestion.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/09
* Description：This java file is used for analysis question and return question list.
* Modify history：
* 郭靖东,create file,2015/08/09
*/
package hpecl.uclass.analysis;

import hpecl.uclass.Bean.QuestionAnswer;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class JsoupAnalysisQuestion extends SuperAnalysis{
	private String question = "";
	private String asker;
	private String questionContent;
	private String consern;
	private String url;
	ArrayList<QuestionAnswer> questionArr = new ArrayList<QuestionAnswer>();
	@Override
	public Object analysisInfo(String getinfo) {
		// TODO Auto-generated method stub
		Document doc =  Jsoup.parse(getinfo);
	    Elements extra = doc.getElementsByClass("line_bottom");
	    Elements eQuestion = extra.select("td strong");
		Elements eAsker = extra.select("td div");
		Elements eQuestionContent = doc.getElementsByClass("smalltext");
		
		
	    for(int i = 0,j=0;i<extra.size();i++,j++){
	    	while(question.equals(eQuestion.get(j).text())){
	    		j++ ;
	    		
	    	}	
	    	
	    		Element urlMain = extra.get(i);
				Elements eUrl = urlMain.select("td a");
				url = eUrl.get(0).attr("href");
			
	    		
	    		question = eQuestion.get(j).text();
	    		asker = eAsker.get(i).text().split(" ")[0];
		    	questionContent = eQuestionContent.get(i).text();
		    	Log.i("guo", "0814 "+url);
		    	questionArr.add(new QuestionAnswer(question,asker,questionContent,url));
	    	
	    	
	    	
	    }
		
		return questionArr;
	}

	
}
