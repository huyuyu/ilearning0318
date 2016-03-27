/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：ForumActivity.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/20
* Description：This java file is used for showing the forum, and realizing the function of forum.
* Modify history：
* 郭靖东,create file,2015/06/20  
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.Bean.Forum;
import hpecl.uclass.Bean.Key;
import hpecl.uclass.adapter.ForumListAdapter;
import hpecl.uclass.analysis.JsoupAnalysisForum;
import hpecl.uclass.form.FormSubmitForum;
import hpecl.uclass.form.FormSubmitQuestion;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.GetHtmlData;
import hpecl.uclass.uti.GetImageFromUrl;
import hpecl.uclass.uti.CustomListview.OnRefreshListener;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilearning.R;

public class ForumActivity extends Activity {
	CustomListview lv;
	ArrayList<Forum> forumArr;
	Key imageKey;
	ImageView image;
	EditText eKey,eText;
	Button submit;
	
	TextView title;
	LayoutInflater infla;
	private ProgressDialog progressDialog = null;
	String username,password,cookie,url,id,sTitle,classid;
	int activity;
	Intent intentData;
	ForumListAdapter adapter;
	int position = 0;
	SharedPreferences.Editor editor;
	SharedPreferences sp;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		infla = LayoutInflater.from(this);
		
		progressDialog = ProgressDialog.show(ForumActivity.this, "请稍等...", "获取数据中...", true);
		editor = getSharedPreferences("position", MODE_PRIVATE).edit();
		
		sp = getSharedPreferences("position", MODE_PRIVATE);
		intentData = getIntent();
		username = intentData.getStringExtra("username");
		password = intentData.getStringExtra("password");
		cookie = intentData.getStringExtra("cookie");
		url = intentData.getStringExtra("url");
		activity = intentData.getIntExtra("activity", 0);//1代表forumActivity，2代表questionActivity  以便提交form表单
		id = intentData.getStringExtra("id");
		classid = intentData.getStringExtra("classid");	
		sTitle = intentData.getStringExtra("sTitle");
		Log.i("guo ", "0818  "+id);
		Log.i("guo ", "0818  "+url);
		new MyAsyncTask().execute();
	}
	
	
public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
		
		@Override
		protected String doInBackground(Integer... arg0) {
			
			Log.i("guo", "0626  "+"0A701课程论坛点击了");
			JsoupAnalysisForum jaf = new JsoupAnalysisForum();
			forumArr = (ArrayList<Forum>) jaf.analysisInfo(GetHtmlData.doInBackground(url, "gb2312",cookie));
			imageKey = GetImageFromUrl.getImageBitmap(Constant.IMAGEKEY);
			editor.putInt("position", -1);
			editor.commit();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			setContentView(R.layout.forum);	
			lv = (CustomListview) findViewById(R.id.forumlistview);   
			title = (TextView) findViewById(R.id.name);
			
		    View footView = infla.inflate(R.layout.underlistview, null);   
		       
		    lv.addFooterView(footView, null, true);
		    switch(activity){
		    	case 1:
		    		title.setText(sTitle);
		    		
		    		break;
		    	case 2:
		    		title.setText(sTitle);
		    		
		    
		    }
			image = (ImageView) footView.findViewById(R.id.identifyingcodepicture);
			eKey = (EditText) footView.findViewById(R.id.eidentifyingcode);
			eText = (EditText) footView.findViewById(R.id.etext);
			submit = (Button) footView.findViewById(R.id.submit);
			image.setImageBitmap(imageKey.getBitmap());
			adapter = new ForumListAdapter(ForumActivity.this, forumArr,eText,lv,editor);
			lv.setAdapter(adapter);
			lv.setonRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void> () {
						  
						 @Override
						 protected Void doInBackground(Void... params) {
						              JsoupAnalysisForum jaf = new JsoupAnalysisForum();
						              forumArr = (ArrayList<Forum>) jaf.analysisInfo(GetHtmlData.doInBackground(url, "gb2312",cookie));
						              return null;
						         }
						                      
						 protected void onPostExecute(Void result) {
						             adapter.notifyDataSetChanged();
						             lv.onRefreshComplete();
						          };						                      
						                      }.execute();
				}
			});
			progressDialog.dismiss();
			
			submit.setOnClickListener(new OnClickListener() {
				
				
				@Override
				public void onClick(View arg0) {
					final String localKey = eKey.getText().toString();
					final String localText = eText.getText().toString();
					

					if(localKey.equals("")){
						Toast.makeText(ForumActivity.this, "请输入验证码",Toast.LENGTH_SHORT ).show();
					}
						
					else if(localText.equals("")){
						Toast.makeText(ForumActivity.this, "请输入评论内容",Toast.LENGTH_SHORT ).show();
					}else{
					switch(activity){
						case 1:  //ForumActivity 的form提交
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									position = sp.getInt("position", -1);
									Log.i("guo", "0720  "+position);
									
									if(position != -1){
											FormSubmitForum.post(Constant.FORMURL,username, password,localKey,localText,forumArr.get(position).getReply(),classid,id);
											new MyAsyncTask().execute();
									}
		
									else{
										FormSubmitForum.post(Constant.FORMURL,username, password,localKey,localText,"0",classid,id);
										new MyAsyncTask().execute();
									}									
									}
								
							}).start();
							break;
						case 2: //QuestionActivity 的form提交
							new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									position = sp.getInt("position", -1);
									Log.i("guo", "0720  "+position);									
									if(position != -1){
											FormSubmitQuestion.post(Constant.FORMURL,username, password,localKey,localText,forumArr.get(position).getReply(),id);
											new MyAsyncTask().execute();
									}

									
									else{
										FormSubmitQuestion.post(Constant.FORMURL,username, password,localKey,localText,"0",id);
										new MyAsyncTask().execute();
									}
										
									}
								
							}).start();
								break;
					
					
						}
					
					}
					
					
				}
			});
			super.onPostExecute(result);
			
		}
		
	}
}
