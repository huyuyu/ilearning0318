/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：GetImageFromUrl.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/16
* Description：This java file is to get identify image.
* Modify history：
* 郭靖东,create file,2015/06/16
*/
package hpecl.uclass.uti;

import hpecl.uclass.Bean.Key;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;


public class GetImageFromUrl {
	/** 
     * 从指定URL获取图片 
     * @param url 
     * @return 
     */  
    public static Key getImageBitmap(String url){  
        URL imgUrl = null;  
        Key key = null;
        Bitmap bitmap = null;  
        try {  
            imgUrl = new URL(url);  
            HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();  
            conn.setDoInput(true);  
            conn.connect();  
            
            InputStream is = conn.getInputStream();  
            bitmap = BitmapFactory.decodeStream(is);
            
            Matrix matrix = new Matrix(); 
            matrix.postScale(2.0f,3.0f); //长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            
            
              
            
            String rawCookie = conn.getHeaderField("set-cookie");
            String cookie = handle(rawCookie);
            
            
            key = Key.get(resizeBmp,cookie);
            
            Log.i("guo","0629   " + key.getCookie());
            is.close();  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }catch(IOException e){  
            e.printStackTrace();  
        }  
        
        return key;  
    }

	private static String handle(String rawCookie) {
		// TODO Auto-generated method stub
		String [] cookies = rawCookie.split(";");
		
		return cookies[0];
	}
}
