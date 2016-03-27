/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：FormSubmitRegister.java
* Author：常青
* Version：1.1
* Date：2015/08/05
* Description：This java file is used for submitting register form to website.
* Modify history：
* 常青,create file,2015/08/05
*/
package hpecl.uclass.form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;






public class FormSubmitRegister {
	
	public static String post(String sUrl,String username, String password,String value,String email,String phone,String name,String academy) { 
		
//		ArrayList<String> arry = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();		
//		String rawCookie = null;    
		//HttpURLConnection方式 post method
		
		try {
			URL url = new URL(sUrl);
			
				try {
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					
//					conn.setRequestProperty("Cookie", imageKey.getCookie());
					StringBuffer params = new StringBuffer();
			        // 表单参数与get形式一样
			        params.append("groupid").append("=").append(value).append("&")
			        	  .append("enews").append("=").append("register").append("&")
			              .append("username").append("=").append(username).append("&")
			              .append("password").append("=").append(password).append("&")
			              .append("repassword").append("=").append(password).append("&")
			              .append("email").append("=").append(email).append("&")
			              .append("company").append("=").append(academy).append("&")
			              .append("truename").append("=").append(name).append("&")
			              .append("phone").append("=").append(phone).append("&")
			              .append("oicq").append("=").append("").append("&")
			              .append("filename").append("=").append("").append("&")
			              .append("address").append("=").append("").append("&")
			              .append("zip").append("=").append("").append("&")
			              .append("saytext").append("=").append("").append("&")
			              .append("Submit").append("=").append("马上注册");
			        
			       
			      //使用GB2312编码
			        byte[] bypes = params.toString().getBytes("GB2312");
			
			        conn.getOutputStream().write(bypes);// 输入参数  
			      		        
//					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//					out.writeBytes("username="+username+"&password="+password+"&lifetime=0"+"&key="+identifyingcode);
			        
					InputStream is = conn.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is,"GB2312"));
					
					String line;
					while((line = br.readLine()) != null){
						sb.append(line);
					}
//					rawCookie = GetCookie.getCookie(conn);
					
//					arry.add(sb.toString());
//					arry.add(rawCookie);
					
					
				
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		return sb.toString();
	    
	    
	    
	}
	
	
	
	
	
	
	
	
	
	
//	 private static String convertStreamToString(InputStream is) {
//		    BufferedReader reader = null;
//			try {
//				reader = new BufferedReader(new InputStreamReader(is,"GB2312"));
//			} catch (UnsupportedEncodingException e1) {

//				e1.printStackTrace();
//			}
//		          StringBuilder sb = new StringBuilder();
//		          String line = null;
//		          try {
//		               while ((line = reader.readLine()) != null) {
//		                    sb.append(line);
//		               }
//		          } catch (IOException e) {
//		               e.printStackTrace();
//		          } finally {
//		               try {
//		                    is.close();
//		               } catch (IOException e) {
//		                    e.printStackTrace();
//		               }
//		          }
//		          return sb.toString();
//		 }
	
}
