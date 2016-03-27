/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：FormSubmitLogin.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/21
* Description：This java file is used for submitting login form to website.
* Modify history：
* 郭靖东,create file,2015/07/21
*/
package hpecl.uclass.form;


import hpecl.uclass.Bean.Key;
import hpecl.uclass.uti.GetCookie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FormSubmitLogin {
	
	public static ArrayList<String> post(String sUrl,String username, String password,String key,Key imageKey) { 
		
		ArrayList<String> arry = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();

		String rawCookie = null;
		//HttpClient 方式post method
		/*
		HttpClient  httpClient = new DefaultHttpClient();  
		CookieStore cookieStore = (CookieStore) new BasicCookieStore();
	    
	    HttpPost httpPost = new HttpPost("http://www.grandsw.com:9191/class/e/enews/index.php");  
	  
	    try {  
	        // 为httpPost设置HttpEntity对象  
	        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	        
	        parameters.add(new BasicNameValuePair("ecmsfrom", ""));  
	        parameters.add(new BasicNameValuePair("enews", "login"));  
	        parameters.add(new BasicNameValuePair("username", username));  
	        parameters.add(new BasicNameValuePair("password", password));  
	        parameters.add(new BasicNameValuePair("key", key));  
	        parameters.add(new BasicNameValuePair("lifetime", "0")); 
	        parameters.add(new BasicNameValuePair("Submit", "登陆"));
	        HttpEntity entity = new UrlEncodedFormEntity(parameters,"gb2312");  
	        
	        httpPost.setEntity(entity);  
	        // httpClient执行httpPost表单提交  
	        HttpResponse response = httpClient.execute(httpPost);  
	        // 得到服务器响应实体对象  
	        HttpEntity responseEntity = response.getEntity();
	        
	        InputStream content = entity.getContent();//发出的form
	        InputStream recontent = responseEntity.getContent();//从服务器返回的信息
	        
	        String returnConnection = convertStreamToString(content);//发出的form表单信息
	        String returncontent = convertStreamToString(recontent);//从服务器返回的信息
    		 
    		Log.i("guo", "0604  "+returnConnection);
    		Log.i("guo", "0604  "+returncontent);
	        if (response.getStatusLine().getStatusCode() == 200) {  
	        	Log.i("guo", "0604"+EntityUtils  
	                    .toString(responseEntity, "gb2312"));
	        } else {  
	        	Log.i("guo", "0604"+"服务器无响应！！");
	           
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        // 释放资源  
	        httpClient.getConnectionManager().shutdown();  
	    } 
		
	    */
	    
		//HttpURLConnection方式 post method
		
		try {
			URL url = new URL(sUrl);
			
				try {
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					
					conn.setRequestProperty("Cookie", imageKey.getCookie());
					StringBuffer params = new StringBuffer();
			        // 表单参数与get形式一样
			        params.append("ecmsfrom").append("=").append("").append("&")
			        	  .append("enews").append("=").append("login").append("&")
			              .append("username").append("=").append(username).append("&")
			              .append("password").append("=").append(password).append("&")
			              .append("key").append("=").append(key).append("&")
			              .append("lifetime").append("=").append("0").append("&")
			              .append("Submit").append("=").append("登陆");

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
					rawCookie = GetCookie.getCookie(conn);
					
					arry.add(sb.toString());
					arry.add(rawCookie);
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arry;
	}
	
	
	
	
	
	
	
	
	
	
//	 private static String convertStreamToString(InputStream is) {
//		    BufferedReader reader = null;
//			try {
//				reader = new BufferedReader(new InputStreamReader(is,"GB2312"));
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
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
