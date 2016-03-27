package hpecl.uclass.ilearning;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ilearning.R;

 
public class TextURLWebView extends Activity {
	 private WebView webview;  
	    @Override 
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.webview);  
	        Intent intent = getIntent();
	        String cookie = intent.getStringExtra("cookie") +";domain=http://www.grandsw.com;path=/;Expires=Thu, 2 Aug 2021 20:47:11 UTC;";
	        String url = intent.getStringExtra("url");

	        webview = (WebView) findViewById(R.id.webview);  
	        //????WebView???????????Javascript???  
	        
	        Log.i("guo ", "1014 "+url);
	        Log.i("guo ", "1014 "+cookie);
	        
	        webview.getSettings().setJavaScriptEnabled(true); 
	        // ?????????????? 
	        webview.getSettings().setSupportZoom(true); 
	        // ???ó?????????? 
	        webview.getSettings().setBuiltInZoomControls(true);
	        //?????????????
	        webview.getSettings().setUseWideViewPort(true);
	        webview.getSettings().setAppCacheEnabled(true);
	        //????????
	        webview.getSettings().setBlockNetworkImage(true); 
	        //????Web???  
	        webview.setWebViewClient(new WebViewClient());  
//	        webview.setWebChromeClient(new WebChromeClient());
	        CookieSyncManager.createInstance(this);    
	        CookieManager cookieManager = CookieManager.getInstance();
	        cookieManager.removeSessionCookie();
	        cookieManager.removeAllCookie();
	        cookieManager.setAcceptCookie(true);
	        cookieManager.setCookie("http://www.grandsw.com", cookie );                 
	        CookieSyncManager.getInstance().sync();   
	        
	        Map<String, String> abc = new HashMap<String, String>();
	        abc.put("Cookie", cookie);
	        //???????????????  
	        webview.loadUrl(url,abc);  
	        
	    }  
	      
	    @Override 
	    //???????  
	    //????Activity???onKeyDown(int keyCoder,KeyEvent event)????  
	    public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
	            //goBack()???????WebView????????  
//	            Log.i("guo ", "101s4 "+webview.goBack());
	            if(webview.canGoBack()){
	            	 webview.goBack();
	            }else{
	            	finish();
	            }
	            return true;  
	        }  
	        return false;  
	    }  
	      
	 
}
