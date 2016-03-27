/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：UploadFile.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/11
* Description：This java file is to upload file though http, not used now.
* Modify history：
* 郭靖东,create file,2015/08/11
*/
package hpecl.uclass.uti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class UploadFile {
	static File sdCardPath = Environment.getExternalStorageDirectory();
	private static File uploadFile;
	private static String uploadFileIdGloble;
	private static long statusFileSizeGloble;
	
	public static void uploadFile(String url,File file,String uploadFileId,long statusFileSize) throws MalformedURLException{
		URL rUrl = new URL(url);
//		file = new File(sdCardPath,"test.zip");
		uploadFile = file;
		uploadFileIdGloble = uploadFileId;
		statusFileSizeGloble = statusFileSize;
		Log.i("guo", "0731   "+ uploadFile);
		getHtmlContent(rUrl);
	}
	public static String getHtmlContent(URL url) {
		 StringBuffer contentBuffer = new StringBuffer();
		 Bundle params=new Bundle();		
		 int responseCode = -1;
		 HttpURLConnection con = null;
		
		 try {
			 
			 con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("PUT");
			 con.setConnectTimeout(60000);
			 con.setReadTimeout(6000000);
			 con.setRequestProperty("Edrive-UploadFileId", uploadFileIdGloble);
			 
			 con.connect();
			 
//		 // 获得网页返回信息码
//			 responseCode = con.getResponseCode();
//			 
//			 Log.i("guo", "0422"+responseCode);
//			 if (responseCode == -1) {
//			
//			 Log.i("guo", "0422  :  connection is failure...  "+url.toString() );
//			 con.disconnect();
//			 return null;
//			 }
//			 if (responseCode >= 400) // 请求失败
//			 {
//			 
//			 Log.i("guo", "0422  请求失败：get response code:   "+responseCode);
//			 con.disconnect();
//			 return null;
//		 }
			 
			 byte [] buffer = new byte [1024];
			 int i = 0;
			 FileInputStream fos = new FileInputStream(uploadFile);
			 OutputStream os = con.getOutputStream();
			// Log.i("guo", "0731   "+"....");
			 while((i = fos.read(buffer))!=-1){
				 Log.i("guo", "0731   "+"....");
				 os.write(buffer,0,i);
			 }
			 InputStream inStr = con.getInputStream();

			 InputStreamReader istreamReader = new InputStreamReader(inStr);
			 BufferedReader buffStr = new BufferedReader(istreamReader);

			 String str = null;
			 while ((str = buffStr.readLine()) != null){
				 contentBuffer.append(str);
			 }

			 Log.i("guo", "back0731   "+contentBuffer.toString());
			 
			 Log.i("guo", "finish ....................");

			 inStr.close();
			 fos.close();
			 os.close();

//			 inStr.close();
		 }catch (IOException e) {
			 e.printStackTrace();
			 contentBuffer = null;
			 System.out.println("error: " + url.toString());
		 } finally {
			 con.disconnect();
		 }
//		 Log.i("guo", "uploadurl   "+ contentBuffer.toString());
		return null;
		 }
}
