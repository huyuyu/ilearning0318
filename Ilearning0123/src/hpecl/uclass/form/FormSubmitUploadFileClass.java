/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：FormSubmitUploadFileClass.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/09
* Description：This java file is used for submitting file to website, not used now.
* Modify history：
* 郭靖东,create file,2015/08/09
*/
package hpecl.uclass.form;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class FormSubmitUploadFileClass {

	
	public static String post(String sUrl,String cookie,String path,String title,String studentName,String studentNumber,String teacher,String introduction) { 
		
	
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
					long    curDate    =    System.currentTimeMillis();//获取当前时间  
					// 表单参数与get形式一样
//					if(className.equals("0A701")){
						 params.append("mid").append("=").append("10").append("&")			        
			              .append("enews").append("=").append("MAddInfo").append("&")			              			             
			              .append("classid").append("=").append("84").append("&")
			              .append("addnews").append("=").append("提交").append("&")
			              .append("title").append("=").append(title).append("&")
			              .append("originality").append("=").append(introduction).append("&")
			              .append("filepass").append("=").append(curDate+"").append("&")
			              .append("fuzeren").append("=").append(studentNumber).append("&")
			              .append("id").append("=").append("").append("&")
			              .append("facultyadviser").append("=").append(teacher).append("&")
			              .append("groupmember").append("=").append(studentName);
					
//					else{
//						params.append("mid").append("=").append("1").append("&")			        
//			              .append("enews").append("=").append("MAddInfo").append("&")			              			             
//			              .append("classid").append("=").append("97").append("&")
//			              .append("Submit").append("=").append("添加信息");
//					}
				   
					
				
			       
			        
			       
			      //使用GB2312编码
			        byte[] bypes = params.toString().getBytes("GB2312");
			
			        OutputStream dos = conn.getOutputStream();// 输入参数  
			        dos.write(bypes);
			        dos.write("downpathfile=".getBytes());
			        FileInputStream fis = new FileInputStream(path);  
			        byte[] buffer = new byte[8192]; // 8k  
			        int count = 0;  
			        while ((count = fis.read(buffer)) != -1)  
			        {  
			          dos.write(buffer, 0, count);  
			    
			        }  
			        fis.close();
//					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//					out.writeBytes("username="+username+"&password="+password+"&lifetime=0"+"&key="+identifyingcode);
			        
					InputStream is = conn.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is,"GB2312"));
					
					String line;
					while((line = br.readLine()) != null){
						sb.append(line);
					}
					
					Log.i("guo", "0806  提交返回  "+sb);
				//	rawCookie = GetCookie.getCookie(conn);
					
			
					
					
					
				
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
