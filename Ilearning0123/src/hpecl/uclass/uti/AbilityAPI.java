/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Ability.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/11
* Description：This java file is to save constant params.
* Modify history：
* 郭靖东,create file,2015/06/11
*/
package hpecl.uclass.uti;

import cn.open189.api.http.HttpHelper;

import android.os.Bundle;
import cn.open189.api.http.Callback;
import cn.open189.api.http.requestlistener.DefaultRequestListener;
import cn.open189.api.util.DigestHelper;

//天翼云的API

/**
 *
 * 能力封装DEMO.
 * 
 * @Copyright: Copyright (c) 2008 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司 
 * @author 陈作�?? 
 * @version 1.00.00, Feb 29, 2012 9:24:25 AM
 * @history:
 * 
 */
public class AbilityAPI {
	
	 public static String getSign(Bundle params){
		//APP_ID+ACCESSS_TOKEN+TIMESTAMP(�������?)+STATE(�������?)+APP_SECRET
		String str = params.getString("app_id");
		str += params.getString("access_token");
		if (params.containsKey("timestamp")) {
			str += params.getString("timestamp");
		}
		if (params.containsKey("state")) {
			str += params.getString("state");
		}
		str += params.getString("app_secret");
		return DigestHelper.md5Hex(str);
	 }

	 public static void makeCall(String url,Bundle params,final Callback callback){
		 if(!params.containsKey("sign")){
	           params.putString("sign",getSign(params));
	        }
        HttpHelper.doGet(url, null, params, new DefaultRequestListener(callback));
//        HttpHelper.doPost(url, params, params, new DefaultRequestListener(callback));
        
	 }
	 public static void makePostCall(String url,Bundle params,final Callback callback){
		 if(!params.containsKey("sign")){
	           params.putString("sign",getSign(params));
	        }
//        HttpHelper.doGet(url, null, params, new DefaultRequestListener(callback));
        HttpHelper.doPost(url, params, params, new DefaultRequestListener(callback));
        
	 }
}
