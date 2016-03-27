/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：GetCookie.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/15
* Description：This java file is to get cookie.
* Modify history：
* 郭靖东,create file,2015/06/15
*/
package hpecl.uclass.uti;



import java.net.HttpURLConnection;


import java.util.List;
import java.util.Map;




public class GetCookie {
	
	public static String getCookie(HttpURLConnection conn){
		
		Map<String, List<String>> rawCookie = null;
		String rawCookies = null;
	
 
        rawCookie = conn.getHeaderFields();
        rawCookies = setCookies(rawCookie);		
		return rawCookies;
		
	}
	
	
	
	/**
	 * 锟芥储cookie
	 * @param headerFields 
	 */
	public static String setCookies(Map<String, List<String>> headerFields) {
		if (null == headerFields) {
			return null;
		}
		List<String> cookies = headerFields.get("Set-Cookie");
		if (null == cookies) {
			return null;
		}
		StringBuffer cookieString = new StringBuffer();
		String handleCookie;
		for (int i = 0;i<4;i++) {
			handleCookie = handleCookie(cookies.get(i));
			if(i == 3){
				cookieString.append(handleCookie);
			}else{
				
				cookieString.append(handleCookie+";");
			}

		}
		return cookieString.toString();
		
		
	}
	
	
public static String handleCookie(String cookie){
		
		String [] cookies = cookie.split(";");			
		return cookies[0];
		
		
		
		}
}
