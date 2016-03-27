package hpecl.uclass.service;


import hpecl.uclass.analysis.JsoupAnalysisNotificationTime;
import hpecl.uclass.ilearning.NotificationActivity;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.GetHtmlData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.example.ilearning.R;
//后台推送通知
public class BackService extends Service {

	String url,cookie;
	ArrayList <String> notificationTime = new ArrayList<String>();
	private NotificationManager manager;
	private Notification notification;
	private PendingIntent pi;
	SharedPreferences sp;
	SharedPreferences.Editor spe;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.i("guo ", "1230   "+ "sevice onStartCommand");
		url = Constant.A701NOTIFICATION;
		sp = getSharedPreferences("cookie", MODE_PRIVATE);
		cookie = sp.getString("cookie", "");
		return super.onStartCommand(intent, flags, startId);


	}

	@Override
	public void onCreate() {
//		Log.i("guo ", "1230   "+ "sevice onCreate");

		url = Constant.A701NOTIFICATION;
		sp = getSharedPreferences("cookie", MODE_PRIVATE);
		cookie = sp.getString("cookie", "");

		new Thread(new Runnable() {
			@Override
			public void run() {

				JsoupAnalysisNotificationTime jant = new JsoupAnalysisNotificationTime();
				notificationTime = (ArrayList<String>) jant.analysisInfo(GetHtmlData.doInBackground(url, "gbk", cookie));
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
				String date = df.format(new Date());// new Date()为获取当前系统时间
				for(int i = 0;i<notificationTime.size();i++){
					Log.i("guo ", "1230   "+ notificationTime.get(i));
					if(notificationTime.get(i).equals(date)){
						notification("您有课程通知！请查看！", "1", date);
					}
				}
//				Log.i("guo ", "1230   "+ "sevice thread running");
			}
		}).start();
		super.onCreate();





	}


	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.i("guo ", "1230   "+ "sevice onStart");

		url = Constant.A701NOTIFICATION;

		sp = getSharedPreferences("cookie", MODE_PRIVATE);
		cookie = sp.getString("cookie", "");
		super.onStart(intent, startId);

	}


	private void notification(String content, String number, String date) {
		// 获取系统的通知管理器
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.ic_launcher, content,
				System.currentTimeMillis());
		notification.defaults = Notification.DEFAULT_ALL; // 使用默认设置，比如铃声、震动、闪灯
		notification.flags = Notification.FLAG_AUTO_CANCEL; // 但用户点击消息后，消息自动在通知栏自动消失
		notification.flags |= Notification.FLAG_NO_CLEAR;// 点击通知栏的删除，消息不会依然不会被删除

		Intent intent = new Intent(BackService.this,
				NotificationActivity.class);
//    intent.putExtra("content", content);  
//    intent.putExtra("number", number);
//    intent.putExtra("date", date);  

//		Log.i("guo ", "0110   "+ url);
//		Log.i("guo ", "0110   "+ cookie);

		intent.putExtra("cookie", cookie);
		intent.putExtra("url", url);
		pi = PendingIntent.getActivity(BackService.this, 0, intent, 0);

		notification.setLatestEventInfo(BackService.this, "悦课通知", content, pi);

		// 将消息推送到状态栏
		manager.notify(0, notification);

	}
}
