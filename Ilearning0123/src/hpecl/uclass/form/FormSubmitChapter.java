/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：FormSubmitChapter.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/13
* Description：This java file is used for submitting chapter form to website.
* Modify history：
* 郭靖东,create file,2015/08/13
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
import java.util.ArrayList;

import android.util.Log;

public class FormSubmitChapter {
	public static ArrayList<String> post(String sUrl,String username, String password,String key,String text,String replyID,String id) { 
		
		ArrayList<String> arry = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();			
		
		String rawCookie = null;
		rawCookie = "ecmscheckplkey="+key;
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
					if(replyID.equals("0")){ params.append("username").append("=").append(username).append("&")
			              .append("password").append("=").append(password).append("&")
			              .append("key").append("=").append(key).append("&")
			              .append("saytext").append("=").append(text).append("&")		              
			              .append("imageField").append("=").append("提 交").append("&")
			              .append("id").append("=").append(id).append("&")
			              .append("classid").append("=").append(Constant.TOPICID).append("&")
			              .append("enews").append("=").append("AddPl").append("&")
			              .append("repid").append("=").append(replyID);
					}
					else{
						 params.append("username").append("=").append(username).append("&")
			              .append("password").append("=").append(password).append("&")
			              .append("key").append("=").append(key).append("&")
			              .append("saytext").append("=").append("[quote]"+replyID+"[/quote]"+text).append("&")			              
			              .append("imageField").append("=").append("提 交").append("&")
			              .append("id").append("=").append(id).append("&")
			              .append("classid").append("=").append(Constant.TOPICID).append("&")
			              .append("enews").append("=").append("AddPl").append("&")
			              .append("repid").append("=").append(replyID);
					}
			       
			        
			       
			      //使用GB2312编码
			        byte[] bypes = params.toString().getBytes("GB2312");
			
			        conn.getOutputStream().write(bypes);// 输入参数  
			      		        
//					
			        
					InputStream is = conn.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is,"GB2312"));
					
					String line;
					while((line = br.readLine()) != null){
						sb.append(line);
					}
					
					Log.i("guo", "0714   "+sb);
				
					
					arry.add(sb.toString());
					arry.add(rawCookie);
					
					
				
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		return arry;		    
	    
	}
}
