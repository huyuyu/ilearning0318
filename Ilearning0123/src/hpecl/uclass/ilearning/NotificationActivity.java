/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：NotificationActivity.java
* Author：常青
* Version：1.1
* Date：2015/07/10
* Description：This java file is to show notification information.
* Modify history：
* 常青,create file,2015/07/10 
*/
package hpecl.uclass.ilearning;


import hpecl.uclass.Bean.Article;
import hpecl.uclass.adapter.ConcernReadingListAdapter;
import hpecl.uclass.analysis.JsoupAnalysisNotification;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.GetHtmlData;
import hpecl.uclass.uti.CustomListview.OnRefreshListener;

import java.util.ArrayList;

import com.example.ilearning.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



public class NotificationActivity extends Activity {
	String content = new String ();
	String getinfo = new String();
	//List<Article> articles = new ArrayList<Article>();
	CustomListview lv;
	ConcernReadingListAdapter adapter;
	ProgressDialog progressDialog;
	String cookie;
	String url;
	ArrayList<Article> notificationArr = new ArrayList<Article>();
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		
		lv = (CustomListview) findViewById(R.id.lv);
		sp = getSharedPreferences("cookie", MODE_PRIVATE);
	    cookie = sp.getString("cookie", "");
	    Intent intent = getIntent();
		url = intent.getStringExtra("url");
		Log.i("guo ", "0110  "+ url);
		Log.i("guo ", "0110  "+ cookie);
		
		new MyAsyncTask().execute();//执行异步线程
		
		progressDialog = ProgressDialog.show(NotificationActivity.this, "请稍等...", "获取数据中...", true);
		
		
}
		
		
	public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
		
		@Override
		protected String doInBackground(Integer... arg0) {
			
			JsoupAnalysisNotification jaf = new JsoupAnalysisNotification();
			notificationArr = (ArrayList<Article>)jaf.analysisInfo(GetHtmlData.doInBackground(url, "gbk", cookie));

			return null;
		
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			adapter = new ConcernReadingListAdapter(NotificationActivity.this, notificationArr);
			lv.setAdapter(adapter);
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			lv.setonRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void> () {
						  
						 @Override
						 protected Void doInBackground(Void... params) {
							 JsoupAnalysisNotification jaf = new JsoupAnalysisNotification();
							 notificationArr = (ArrayList<Article>)jaf.analysisInfo(GetHtmlData.doInBackground(Constant.A701NOTIFICATION, "gbk", cookie));
						              return null;
						         }
						                      
						 protected void onPostExecute(Void result) {
							         adapter.notifyDataSetChanged();
						             lv.onRefreshComplete();
						          };						                      
						                      }.execute();
				}
			});
			
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub
					String href = notificationArr.get(position-1).getHref();
					Log.i("cq", "1109href"+href);
					Intent intent = new Intent(); 
					intent.setClass(NotificationActivity.this, RefenceText.class); 
					intent.putExtra("Href", href); 
					intent.putExtra("cookie", cookie); 
					startActivity(intent); 
				}});

		}
	}
}

