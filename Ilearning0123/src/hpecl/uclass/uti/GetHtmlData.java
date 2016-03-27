/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：GetHtmlData.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/15
* Description：This java file is to get HTML data.
* Modify history：
* 郭靖东,create file,2015/06/15
*/
package hpecl.uclass.uti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



import android.util.Log;

public class GetHtmlData {
		static String  getinfo;
		String imageUrl;
		
		//ArrayList<Article> articles = new ArrayList<Article>();
		
		public static String doInBackground(String url,String encode,String cookie) {
			// TODO Auto-generated method stub
			getinfo = getHTMLContent(url,encode,cookie);
//			Log.i("guo", "chapter0911   "+getinfo);
	       
			return getinfo;
		}
		 
		 public static String getHtmlContent(URL url, String encode,String cookie) {
			 StringBuffer contentBuffer = new StringBuffer();
			 			
			 int responseCode = -1;
			 HttpURLConnection con = null;
			
			 try {
//				 Log.i("guo", "0701  "+cookie);
				 con = (HttpURLConnection) url.openConnection();
				 con.setRequestMethod("POST");
				 con.setConnectTimeout(60000);
				 con.setReadTimeout(60000);
				 con.setRequestProperty("Cookie", cookie);
				 con.connect();
			 // 获得网页返回信息码
				 responseCode = con.getResponseCode();				 
				 Log.i("guo", "0422"+responseCode);
				 if (responseCode == -1) {
//				 	System.out.println(url.toString() + " : connection is failure...");
				 	Log.i("yuyu", url.toString());
					con.disconnect();
				 	return null;
				 }
				 if (responseCode >= 400) // 请求失败
				 {
				 	System.out.println("请求失败:get response code: " + responseCode);
					 con.disconnect();
					 return null;
			 }

				 InputStream inStr = con.getInputStream();
//				 Log.i("guo", "0422"+url);
				 InputStreamReader istreamReader = new InputStreamReader(inStr,encode);
				 BufferedReader buffStr = new BufferedReader(istreamReader);
				  
				 String str = null;
				 while ((str = buffStr.readLine()) != null)
					 contentBuffer.append(str);
				 	 inStr.close();
				 } catch (IOException e) {
					 e.printStackTrace();
					 contentBuffer = null;
					 System.out.println("error: " + url.toString());
			 } finally {
				 con.disconnect();
			 }
			 	return contentBuffer.toString();
			 }

			 public static String getHTMLContent(String url, String encode,String cookie) {
			 if (!url.toLowerCase().startsWith("http://")) {
				 url = "http://" + url;
			 }
			 try {
				URL rUrl = new URL(url);
			 	return getHtmlContent(rUrl, encode,cookie);
			 } catch (Exception e) {
			 	e.printStackTrace();
				 return null;
			 }
		 }
}
