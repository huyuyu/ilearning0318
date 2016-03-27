/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：RefenceText.java
* Author：郭靖东
* Version：1.1
* Date：2015/09/10
* Description：This java file is to be used for showing Reference reading content.
* Modify history：
* 郭靖东,create file,2015/09/10
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.Bean.Forum;
import hpecl.uclass.Bean.Key;
import hpecl.uclass.adapter.MyArrayAdapter;
import hpecl.uclass.analysis.JsoupAnalysisLogin;
import hpecl.uclass.form.FormSubmitConcernReading;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.GestureHelper;
import hpecl.uclass.uti.GestureHelper.OnFlingListener;
import hpecl.uclass.uti.GetHtmlData;
import hpecl.uclass.uti.GetImageFromUrl;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilearning.R;
import com.loopj.android.image.SmartImageView;




@SuppressLint("SetJavaScriptEnabled")
public class RefenceText extends Activity {

	LayoutInflater infla;
	MyArrayAdapter textAdapter;
	ArrayList<Forum> forumArr;
	ArrayList<String> imageArry = new ArrayList<String>();
	String back,key,sayText,backInfo;
	Key imageKey;
	String commentURL = "";
	String getHref,cookie,username,password,commentNumber,itemContent,commentHref,title;
	Element littleTitle;
	Elements span;
	CustomListview lv;
	ImageView image;
	Spanned spImage;
	Bitmap bitmap;
	Button submit,comment;
	TextView tv;
	EditText et,etext;
	SmartImageView myImage;
	int couting = 0;
	private GestureHelper gh;//手势
	ArrayList<String> textItem = new ArrayList<String>();
	private ProgressDialog progressDialog = null;
	Handler handle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		infla = LayoutInflater.from(this);
		getHref = getIntent().getStringExtra("Href");
		cookie = getIntent().getStringExtra("cookie");
		username = getIntent().getStringExtra("username");
		password = getIntent().getStringExtra("password");
		title = getIntent().getStringExtra("title");
		Log.i("cq", "0730"+title);
		gh = new GestureHelper(RefenceText.this); 
		
		
		//图片滑动监听
				gh.setOnFlingListener(new OnFlingListener() {  
		            @Override  
		            public void OnFlingLeft() {  
		                //向左滑动  
		            	++couting;		            	
		            	if(couting >= imageArry.size()){
		            		Toast.makeText(RefenceText.this, couting+"/"+imageArry.size(), Toast.LENGTH_SHORT).show();
		            		couting = imageArry.size()-1;
		            		if(imageArry.get(couting).startsWith("/class")){
								myImage.setImageUrl(Constant.A701BEFOR+imageArry.get(couting).trim());
							}else{
								Log.i("guo","1204    image   " +imageArry.get(couting));
								myImage.setImageUrl(imageArry.get(couting).trim());
							}
		            		
		            	}else{
		            		if(imageArry.get(couting).startsWith("/class")){
								myImage.setImageUrl(Constant.A701BEFOR+imageArry.get(couting).trim());
							}else{
								Log.i("guo","1204    image   " +imageArry.get(couting));
								myImage.setImageUrl(imageArry.get(couting).trim());
							}
		            		Toast.makeText(RefenceText.this, couting+1+"/"+imageArry.size(), Toast.LENGTH_SHORT).show();		
		    		        Log.i("cq", "1109"+  couting);
		            	}
		            }  
		              
		            @Override  
		            public void OnFlingRight() {  
		                //向右滑动  
		            	if(couting>=1){
		            		--couting;
		            	}
		         
		            	if(couting == 0){
		            		Toast.makeText(RefenceText.this, couting+1+"/"+imageArry.size(), Toast.LENGTH_SHORT).show();
//		            		Toast.makeText(RefenceText.this, "这是第一张", Toast.LENGTH_SHORT).show();
		            		if(imageArry.get(couting).startsWith("/class")){
								myImage.setImageUrl(Constant.A701BEFOR+imageArry.get(couting).trim());
							}else{
								Log.i("guo","1204    image   " +imageArry.get(couting));
								myImage.setImageUrl(imageArry.get(couting).trim());
							}
		            	}else{
		            		Toast.makeText(RefenceText.this, couting+1+"/"+imageArry.size(), Toast.LENGTH_SHORT).show();
		            		if(imageArry.get(couting).startsWith("/class")){
								myImage.setImageUrl(Constant.A701BEFOR+imageArry.get(couting).trim());
							}else{
								Log.i("guo","1204    image   " +imageArry.get(couting));
								myImage.setImageUrl(imageArry.get(couting).trim());
							}
		            	}
		            }

					@Override
					public void OnImage() {
						// TODO Auto-generated method stub
						
					}
		        });
		

		progressDialog = ProgressDialog.show(RefenceText.this, "请稍等...", "获取数据中...", true);
		new MyAsyncTask().execute();
		handle = new Handler() {  
	          public void handleMessage(Message msg) {   
	               switch (msg.what) {   
	                    case 1:   
							Toast.makeText(RefenceText.this, backInfo, Toast.LENGTH_LONG).show();
	                         break;   
	               }   
	               super.handleMessage(msg);   
	          }   
	     };
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gh.onTouchEvent(event);
	}
	
	class MyAsyncTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {

			back = GetHtmlData.doInBackground(getHref, "GB2312", cookie);
			
			imageKey =  GetImageFromUrl.getImageBitmap(Constant.IMAGEKEY);
			Log.i("guo", "0915  "+back);
			Document doc =  Jsoup.parse(back);

		       //取出class标签为box的元素集 
		       littleTitle = doc.getElementById("text");
		       
//		       Log.i("guo", "1117  "+littleTitle   );
		       Elements src = doc.select("td a span script");
		       Elements href = doc.select("table tr td a");
		       if(!src.isEmpty()){
		    	   
			       for(Element e : src){
			    	   commentURL = Constant.A701BEFOR +e.attr("src");
			       }

				   commentNumber =  GetHtmlData.doInBackground(commentURL, "GB2312", cookie).split("'")[1];

		       }
		       if(!href.isEmpty()){
			       for(Element e : href){
			    	   Log.i("guo ", "0912 "+e.toString());
			    	   if(e.text().contains("共有")){
			    		   commentHref = Constant.A701BEFOR +e.attr("href");
			    		   Log.i("guo ", "0912 "+commentHref);
			    	   }
			    	   
			       }
		       }
		       
		       //去除span信息
		       if(littleTitle != null && !(littleTitle.select("span[style*=display:none]").isEmpty())){
		    	   littleTitle.select("span[style*=display:none]").remove();
		       }
		       
		       if(littleTitle != null){
		    	   itemContent = littleTitle.toString();	
//		    	   littleTitle.select(cssQuery)
		       }
		       if(littleTitle != null){
		    	   Elements imgs = littleTitle.select("img");
		    	   for(Element e : imgs){
		    		   Log.i("guo ", "1204   "+e.attr("src"));
		    		   imageArry.add(e.attr("src").toString());
		    	   }
		       }
		     
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			setContentView(R.layout.notification_text);
			lv =  (CustomListview) findViewById(R.id.text);
//			tv = (TextView) findViewById(R.id.tv);
			View footView = infla.inflate(R.layout.listviewfooter, null);
			image = (ImageView) footView.findViewById(R.id.image);
			myImage = (SmartImageView) findViewById(R.id.my_image);
			//滑动手势
//			lv.setMovementMethod(ScrollingMovementMethod.getInstance());
			if(!imageArry.isEmpty()){
				if(imageArry.get(couting).startsWith("/class")){
					myImage.setImageUrl(Constant.A701BEFOR+imageArry.get(couting).trim());
				}else{
					Log.i("guo","1204    image   " +imageArry.get(couting));
					myImage.setImageUrl(imageArry.get(couting).trim());
				}
				//计时10s check第一张图片是否加载成功，如果不成功，加载第二张
				new Thread(new Runnable(){   
				    public void run(){   				    	
				    Message message = new Message();   
		            message.what = 10000;
				        try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}   

				        handle.sendMessage(message); //告诉主线程执行任务   

				    }   

				}).start ();
				
				handle = new Handler() {  
			          public void handleMessage(Message msg) {   
			               switch (msg.what) {   
			                    case 30000:   
							        if(myImage.getDrawable() == null){
										Toast.makeText(RefenceText.this, "该图片无法加载！当前为其下一张图片！", Toast.LENGTH_SHORT).show();
										couting++;
										if(couting < imageArry.size() ){
												if( imageArry.get(couting).startsWith("/class")){
													myImage.setImageUrl(Constant.A701BEFOR+imageArry.get(couting).trim());
												}else{
													Log.i("guo","1204    image   " +imageArry.get(couting));
													myImage.setImageUrl(imageArry.get(couting).trim());
													}
											
										}
										
									}	
			                         break;   
			               }   
			               super.handleMessage(msg);   
			          }   
			     }; 
				
		}
		else {
			myImage.setVisibility(View.GONE);
		}
//		myImage.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Toast.makeText(RefenceText.this, couting+1+"/"+imageArry.size(), Toast.LENGTH_SHORT).show();
//			}
//		});
			submit = (Button) footView.findViewById(R.id.submit);
			comment = (Button) footView.findViewById(R.id.comment);
			et = (EditText) footView.findViewById(R.id.eidentify);
			etext = (EditText) footView.findViewById(R.id.etext);
			//判断littleTile是否为空，如果为空时，结束该界面，并返回
			Log.i("guo", "1117  "+littleTitle   );
			if(littleTitle == null){
//				Intent intent = new Intent(RefenceText.this,TextURLWebView.class);
//	        	intent.putExtra("url", getHref);
//	        	intent.putExtra("cookie", cookie);
//	        	startActivity(intent);
				Toast.makeText(RefenceText.this, "移动端暂时无法访问，请使用浏览器访问该页面！", Toast.LENGTH_LONG).show();
//				Log.i("guo", "1117  "+littleTitle   );
				finish();
			}else{
				
							
				textAdapter = new MyArrayAdapter(RefenceText.this,itemContent,cookie, progressDialog);
				lv.setAdapter(textAdapter);
				comment.setText("共有"+commentNumber+"条评论");	
				image.setImageBitmap(imageKey.getBitmap());
				lv.addFooterView(footView, null, true);
						
			}
			//评论button点击，跳转到相应评论界面
			comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {		
							Intent intent = new Intent(RefenceText.this,ForumActivity.class);
							
							intent.putExtra("activity", 1);
							intent.putExtra("url", commentHref);
							intent.putExtra("username", username);
							intent.putExtra("password", password);	
							intent.putExtra("cookie", cookie);
							Log.i("guo", "0912  "+commentHref);
							String [] comment = commentHref.split("&");
							String id = comment[1].split("id=")[1];
							String classid = comment[0].split("classid=")[1];
							intent.putExtra("id", id);
							intent.putExtra("classid", classid);
							intent.putExtra("sTitle", title);
							startActivity(intent);
						}
					
					
				
			});
			//提交留言，点击后提交form表单到服务器
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					key = et.getText().toString();
					sayText = etext.getText().toString();
					if(sayText.equals("")){
						Toast.makeText(RefenceText.this, "请输入评论内容！", Toast.LENGTH_LONG).show();
					}
					
					if(key.equals("")){
						Toast.makeText(RefenceText.this, "请输入验证码！", Toast.LENGTH_LONG).show();
					}
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String [] comment = commentHref.split("&");
							String id = comment[1].split("id=")[1];
							String classid = comment[0].split("classid=")[1];
							Log.i("guo ", "0915  "+id + classid);
							String back = FormSubmitConcernReading.post(Constant.FORMURL, key, cookie,sayText, "0", classid, id);
							JsoupAnalysisLogin jal = new JsoupAnalysisLogin();
							backInfo = (String) jal.analysisInfo(back);
							Log.i("guo", "back "+backInfo);
							if(backInfo.equals("增加评论成功")){
								new MyAsyncTask().execute();
							}
							 Message message = new Message();   
			                 message.what = 1;   
			                      
			                 handle.sendMessage(message);  

						}
					}).start();
				}
			});
					
		}
		
		
	}

	
	
}
