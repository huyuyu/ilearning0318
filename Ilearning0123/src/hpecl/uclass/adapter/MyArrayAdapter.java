package hpecl.uclass.adapter;

/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：MyArrayAdapter.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/28
* Description：This adapter is used for showing the content of html by textview, and supporting the url in the content can be click.
* Modify history：
* 郭靖东,create file,2015/08/28  
*/

import java.util.ArrayList;

import hpecl.uclass.ilearning.RefenceText;
import hpecl.uclass.ilearning.TextURLWebView;
import hpecl.uclass.uti.Constant;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ilearning.R;

public class MyArrayAdapter extends BaseAdapter{

	private LayoutInflater mLI = null;
	String texts= new String();
	static Context context=null;

	CharSequence spImage;
	ProgressDialog progress;
	ViewHolder vh = new ViewHolder();
	static String cookie;
	static String html = "<h1>this is h1</h1>"
			+ "<p>This text is normal</p>"
			+ "<img src='http://www.embedded.com/ContentEETimes/Images/01tmcconnel/NI%20Modeling%20Part%202%20Fig%205%20500.jpg' />"
			+"<img src='https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png' />";
	public MyArrayAdapter(Context context,String texts,String cookie,ProgressDialog pd) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mLI = LayoutInflater.from(context);
		this.texts = texts;
		this.cookie = cookie;
		this.progress = pd;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return texts;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		if(arg1 == null){
			arg1 = mLI.inflate(R.layout.itemconsernreading, null);
			vh.tv = (TextView) arg1.findViewById(R.id.tv);

			arg1.setTag(vh);
		}
		else{
			vh = (ViewHolder) arg1.getTag();
		}


		new MyAsyncTask().execute();
		
		return arg1;
	}
private static class MyURLSpan extends ClickableSpan{   
        
        private String mUrl;   
        MyURLSpan(String url) {   
            mUrl =url;      
        }   
        @Override
        public void onClick(View widget) {
//        	Log.i("guo ", "1203   s"+mUrl);
        	if(mUrl.contains("/class")){
            	if(!mUrl.contains("www.")){
            		mUrl = Constant.A701BEFOR + mUrl;
    	        }
            	Intent intent = new Intent(); 
				intent.setClass(context, RefenceText.class); 
				intent.putExtra("Href", mUrl); 
				intent.putExtra("cookie", cookie); 
				context.startActivity(intent); 
            }else{
        	Intent intent = new Intent(context,TextURLWebView.class);
        	intent.putExtra("url", mUrl);
        	context.startActivity(intent);
            }
        }   
    }
	class ViewHolder{
		TextView tv;

	}
	
	//显示图片,加载图片
	class MyAsyncTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
				return null;
			}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			vh.tv.setText(Html.fromHtml(texts));
			progress.dismiss();

			vh.tv.setMovementMethod(LinkMovementMethod.getInstance()); 
			
			//实现textview中链接点击
	        CharSequence text = vh.tv.getText();   
	        
	        if(text instanceof Spannable){   
	            int end = text.length();   
	            Spannable sp = (Spannable)vh.tv.getText();   
	            URLSpan[] urls=sp.getSpans(0, end, URLSpan.class);    
	            SpannableStringBuilder style=new SpannableStringBuilder(text);   
	            style.clearSpans();//should clear old spans   
	            for(URLSpan url : urls){   
	                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());  
//	                Log.i("guo ", "1203g"+url.getURL());
	                style.setSpan(myURLSpan,sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }   
	            vh.tv.setText(style);   
	        }
		}
	}
}