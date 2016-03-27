/*
* Copyright  漏2015 by HPEC Lab锛孲chool of Software and Microelectronics锛孭eking University锛�All rights reserved.
* 鐗堟潈鎵�湁 漏2015 鍖椾含澶у杞欢涓庡井鐢靛瓙瀛﹂櫌 楂樻�鑳藉祵鍏ュ紡璁＄畻瀹為獙瀹�
* filename锛歂otificationActivity.java
* Author锛氬父闈�
* Version锛�.2
* Date锛�015/11/30
* Description锛歍his java file is to show setup information.
* Modify history锛�
* 甯搁潚,create file,2015/11/30 
*/
package hpecl.uclass.ilearning;




import hpecl.uclass.guider.SettingScrollLayoutActivity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilearning.R;

public class MoreActivity extends Activity {

	private TextView aboutIt,contactus,light,guider,shareClean,noti,rest;
	String getHref,cookie,username,password,commentNumber,itemContent,commentHref,title;
	SharedPreferences sp ;
	SharedPreferences.Editor spe ;
	private NotificationManager manager;  
    private Notification notification;
    private PendingIntent pi;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		init();
		getHref = getIntent().getStringExtra("Href");
		cookie = getIntent().getStringExtra("cookie");
		username = getIntent().getStringExtra("username");
		password = getIntent().getStringExtra("password");
		title = getIntent().getStringExtra("title");
		
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		spe = sp.edit();
		
		aboutIt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MoreActivity.this,AboutIt.class);
				startActivity(intent);
			}
		});
		contactus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(); 
				intent.setClass(MoreActivity.this, RefenceText.class); 
				intent.putExtra("Href", getHref); 
				intent.putExtra("cookie", cookie); 
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("title", "联系我们");				
				startActivity(intent); 
			}
		});
		light.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MoreActivity.this, LightActivity.class);
				startActivity(intent);
			}
		});
		shareClean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MoreActivity.this);

				builder.setTitle("分享缓存清空");
				builder.setMessage("确定要清空分享缓存吗？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String rootPath =  Environment.getExternalStorageDirectory()
								.getAbsolutePath();
						String savePath = rootPath + "/" + "uclass" + "/" + "shared/";
						File saveFile = new File(savePath);
						File [] files = saveFile.listFiles(); 
						if(saveFile.exists()){
							for(File f : files){
								f.delete();
							}
							
							Toast.makeText(MoreActivity.this, "缓存清除成功！", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(MoreActivity.this, "缓存已清除！", Toast.LENGTH_SHORT).show();
						}
					}
					
				});

				builder.setNegativeButton("否", null);

				builder.show();
				
			}
		});
		
		noti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
				builder.setTitle("消息通知");
				builder.setItems(new String[] {"打开消息通知","关闭消息通知"}, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							spe.putInt("消息",0);//打开是0
							spe.commit();
							break;
						case 1:
							spe.putInt("消息", 1);//关闭是1
							spe.commit();	
							break;
						}
						
						dialog.dismiss();
					}
					
				});
				builder.show();
				
			}
		});
		
		guider.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MoreActivity.this,SettingScrollLayoutActivity.class);
				startActivity(intent);
			}
		});
		
		rest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder aBuilder = new AlertDialog.Builder(MoreActivity.this);
				aBuilder.setTitle("选择休息提醒时间！");
				aBuilder.setSingleChoiceItems(new String[] {"1分钟","20分钟","30分钟","45分钟","60分钟"}, 0,  
					    new DialogInterface.OnClickListener() { 
					                          
					       public void onClick(DialogInterface dialog, int which) { 

					    	   switch (which) {
					    	case 0:
					    		new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(60000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										notification("已经1分钟，请您注意休息！","1");
									}
								}).start();
								break;
							case 1:
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(1200000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										notification("已经20分钟，请您注意休息！","1");
									}
								}).start();
								break;
							case 2:
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(1800000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										notification("已经30分钟，请您注意休息！","1");
									}
								}).start();
								
							case 3:
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(2700000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										notification("已经45分钟，请您注意休息！","1");
									}
								}).start();

							case 4:
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(3600000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										notification("已经60分钟，请您注意休息！","1");
									}
								}).start();
							}
							   dialog.dismiss();
					       } 
					    } 
					  );
	
				aBuilder.show();
			}
		});
}
	
	private void init() {
		aboutIt = (TextView) findViewById(R.id.aboutit);
		contactus = (TextView) findViewById(R.id.contactus);
		light = (TextView) findViewById(R.id.light);
		guider = (TextView) findViewById(R.id.guider);
		shareClean = (TextView) findViewById(R.id.share);
		guider = (TextView) findViewById(R.id.guider);	
		noti = (TextView) findViewById(R.id.noti);
		rest = (TextView) findViewById(R.id.rest);
	}
	@SuppressWarnings({ "deprecation" })
	private void notification(String content, String number) {  
	    // 获取系统的通知管理器  
	    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	    notification = new Notification(R.drawable.ic_launcher, content,System.currentTimeMillis());
	    notification.defaults = Notification.DEFAULT_ALL; // 使用默认设置，比如铃声、震动、闪灯  
	    notification.flags = Notification.FLAG_AUTO_CANCEL; // 但用户点击消息后，消息自动在通知栏自动消失  
//	    notification.flags |= Notification.FLAG_NO_CLEAR;// 点击通知栏的删除，消息不会依然不会被删除  
//	    Intent intent = new Intent(MoreActivity.this, NotificationActivity.class);
//	    intent.putExtra("content", content);  
//	    intent.putExtra("number", number);
//	    intent.putExtra("date", date);  
//	    Intent intent = new Intent(Intent.ACTION_MAIN);
//	    pi = PendingIntent.getActivity(MoreActivity.this, 0, intent, 0);  
	      
	    notification.setLatestEventInfo(MoreActivity.this, "悦课通知：", content, pi);  

	    // 将消息推送到状态栏  
	    manager.notify(0, notification);
	}
}