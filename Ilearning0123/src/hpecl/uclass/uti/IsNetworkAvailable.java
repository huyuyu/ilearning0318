/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：IsNetworkAvailable.java
* Author：郭靖东
* Version：1.1
* Date：2015/09/12
* Description：This java file is to get the state of network.
* Modify history：
* 郭靖东,create file,2015/09/12
*/
package hpecl.uclass.uti;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IsNetworkAvailable {
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        	
        } 
        /*如果仅仅是用来判断网络连接
　　　　　　  则可以使用 cm.getActiveNetworkInfo().isAvailable();  */
        else {
            NetworkInfo[] info = cm.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
    } 
}
