/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：FormSubmitAddTopic.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/28
* Description：This java file is used for submitting addtopic form to website.
* Modify history：
* 郭靖东,create file,2015/08/28
*/
package hpecl.uclass.form;

import hpecl.uclass.uti.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class FormSubmitAddTopic {

	public static String post(String sUrl,String cookie,String title,String keyboard,String smallText,String writer) { 
		
		
		StringBuffer sb = new StringBuffer();			
		
		String rawCookie = null;
		rawCookie = cookie;
		//HttpURLConnection方式 post method
		
		try {
			URL url = new URL(sUrl);
			
				try {
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);					
					conn.setRequestProperty("Cookie", rawCookie);
					StringBuffer params = new StringBuffer();
					// 表单参数与get形式一样
					long    curDate    =    System.currentTimeMillis();//获取当前时间       
					
					Log.i("guo", "0728   "+curDate);

					
					params.append("enews").append("=").append("MAddInfo").append("&")			        
		              .append("classid").append("=").append(Constant.TOPICID).append("&")			              			             
		              .append("id").append("=").append("").append("&")
		              .append("filepass").append("=").append(curDate+"").append("&")
		              .append("mid").append("=").append("14").append("&")
		              .append("title").append("=").append(title).append("&")		              
		              .append("keyboard").append("=").append(keyboard).append("&")		              
		              .append("smalltext").append("=").append(smallText).append("&")
		              .append("writer").append("=").append(writer).append("&")
		              .append("addnews").append("=").append("提交");     
			       
			      //使用GB2312编码
			        byte[] bypes = params.toString().getBytes("GB2312");
			
			        conn.getOutputStream().write(bypes);// 输入参数  		        
					InputStream is = conn.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is,"GB2312"));				
					String line;
					while((line = br.readLine()) != null){
						sb.append(line);
					}
					
					Log.i("guo", "0728   "+sb);			
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();		    
	    
	}
}
