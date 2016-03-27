/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Key.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/12
* Description：This java file is the object class, include tow private params.
* Modify history：
* 郭靖东,create file,2015/07/12
*/
package hpecl.uclass.Bean;

import android.graphics.Bitmap;

public class Key {
	private Bitmap bitmap;
	private String cookie;
	
	private Key(Bitmap bitmap, String cookie) {
		super();
		this.bitmap = bitmap;
		this.cookie = cookie;
	}
	public static Key get(Bitmap bitmap, String cookie){
		Key key = new Key(bitmap,cookie);
		return key;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}
