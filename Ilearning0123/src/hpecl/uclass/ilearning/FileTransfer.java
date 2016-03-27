package hpecl.uclass.ilearning;


import hpecl.uclass.Bean.People;
import hpecl.uclass.adapter.MemberListAdapter;
import hpecl.uclass.adapter.ShareFileAdapter;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.TCPServerForPF;
import hpecl.uclass.wifihot.WifiAdmin;
import hpecl.uclass.wifihot.WifiApAdmin;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ilearning.R;

public class FileTransfer extends Activity {


	String ip;
	String broadIP;
	String name;
	People me = new People();
	ArrayList<String> arrayMM = new ArrayList<String>();
	People mm = new People();
	WifiManager.MulticastLock mLock = null;
	private byte[] recvBuf = new byte[Constant.RECV_BUF_SIZE];
	private DatagramSocket mUdp;
	CustomListview listView;
	ListView listViewFile;
	Handler handle,handleFile;
	MemberListAdapter adapter;
	ShareFileAdapter shareFileAdapter;
	final String ONLINE = "在线";
	final String OFFLINE = "离线";
	int flag = 0,wifiFlag = 0;
	File fileHandle;
	Boolean bool = true;
	WifiAdmin wifiAdmin;
	File shareFile;
	public static final int WIFI_CONNECTED = 0x01;  
	public static final int WIFI_CONNECT_FAILED = 0x02;  
	int count = 0;
	ArrayList<File> shareedFileArray = new ArrayList<File>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filetransfer);
		WifiManager manager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		mLock = manager.createMulticastLock("test wifi"); // 开启udp报文权限
		mLock.acquire();
		flag = 0;
		
		Log.i("guo ","0112   "+Constant.rootPath);
		shareFile = new File(Constant.rootPath +"/uclass/shared/");
		if(!shareFile.exists()){
			shareFile.mkdirs();
		}			
		
		//获取网络状态
		wifiAdmin = new WifiAdmin(FileTransfer.this) {
			
			@Override
			public void onNotifyWifiConnected() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNotifyWifiConnectFailed() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void myUnregisterReceiver(BroadcastReceiver receiver) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Intent myRegisterReceiver(BroadcastReceiver receiver,
					IntentFilter filter) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		handle = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what)
				{
					case 1:
						adapter.notifyDataSetChanged();
						break;
					case 2:
						shareFileAdapter.notifyDataSetChanged();
						break;
					case 10://server 文件接收完成
						Toast.makeText(FileTransfer.this, "文件接收完成", Toast.LENGTH_SHORT).show();
						break;
					case 11:
						// dialog参数设置
						AlertDialog.Builder builder = new AlertDialog.Builder(
								FileTransfer.this); // 先得到构造器
						builder.setTitle("是否接受文件！"); // 设置标题
						// builder.setMessage("是否确认退出?"); //设置内容
						// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
						// 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								try {
									TCPServerForPF.startCopyThread();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});
						builder.setNegativeButton("取消", null);		
						builder.show();

						break;
					case 12:
						
						if(count == 0){
							Toast.makeText(FileTransfer.this, "创建者已退出！", Toast.LENGTH_SHORT).show();
							count++;
						}
						
						break;
						
				}
				
			}
			
		};
		//启动tcpserver
		TCPServerForPF tcpServer = new TCPServerForPF(handle,FileTransfer.this);
		tcpServer.start();
				
		listView = (CustomListview) findViewById(R.id.listview1);
		listViewFile = (ListView) findViewById(R.id.listviewfile);
	
		Intent intent = getIntent();
		
		String info = intent.getStringExtra("info");
		name = intent.getStringExtra("name");	
		wifiFlag = intent.getIntExtra("owner", 0);
		
		Toast.makeText(FileTransfer.this, info, Toast.LENGTH_SHORT).show();
		
			
		
//		sendbroadcast.start();//获取完ip才能发广播
		
		
		
		
		try {

			if (mUdp == null) {
				mUdp = new DatagramSocket(null);
				mUdp.setReuseAddress(true);
				mUdp.bind(new InetSocketAddress(Constant.UDPPORT));
			}
		} catch (Exception e) {
			try {
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		//扫描分享文件夹  每两秒扫描
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					File [] fileList = shareFile.listFiles();
					shareedFileArray.clear();
					for(int i = 0;i<fileList.length;i++){
						shareedFileArray.add(fileList[i]);
					}
					Log.i("guo ", "0112   共享文件扫描      ");
					if(fileList.length != 0){
						Message message = new Message();
						message.what = 2;
						handle.sendMessage(message);
					}
					
					if(wifiFlag == 0 && wifiAdmin.isWifiContected(FileTransfer.this) == WIFI_CONNECT_FAILED){
						Message m = new Message();
						m.what = 12;
						handle.sendMessage(m);					
						finish();
					}
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		
		
		//接收广播
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (mUdp != null && !mUdp.isClosed()) { // 初始化buffer
					for (int i = 0; i < Constant.RECV_BUF_SIZE; i++) {
						recvBuf[i] = 0;
					}
					DatagramPacket rdp = new DatagramPacket(recvBuf, recvBuf.length);
					try {
						// 接收广播发来的数据包
						mUdp.receive(rdp);
						Log.i("time", "receivetime");
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 对数据包进行解析
					parsePackage(new String(rdp.getData()).trim());
					Log.i("time", "parsetime");
					
				}
			}
			//接收到广播数据解析数据

				// TODO Auto-generated method stub
			private void parsePackage(String recvBuf) {
					
			int num = arrayMM.size();
			
			//添加在线成员
			if(recvBuf.startsWith(ONLINE) && arrayMM.indexOf(recvBuf) == -1){
				arrayMM.add(recvBuf);
			}
			Log.i("guo  ", "1230    删除               "+ recvBuf);
			//删除离线成员
			if(recvBuf.startsWith(OFFLINE)){
				String []string = recvBuf.split(";");
				String deletMember = ONLINE;
				for(int i = 1;i<string.length;i++){
					deletMember +=  ";"+string[i];
				}
				Log.i("guo  ", "1230    删除               "+ recvBuf);
				arrayMM.remove(deletMember);
			}
			//发送message通知list更新
			if(num != arrayMM.size()){
				Message message = new Message();
				message.what = 1;
				handle.sendMessage(message);
			}
			
			Log.i("guo  ", "1230  广播数据               "+ recvBuf);
		
				
			}
		}).start();
		
		adapter = new MemberListAdapter(FileTransfer.this, arrayMM);
		listView.setAdapter(adapter);
		shareFileAdapter = new ShareFileAdapter(FileTransfer.this,shareedFileArray);
		listViewFile.setAdapter(shareFileAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					
					String mm = arrayMM.get(arg2-1);		
					Log.i("guo  ", "0103     mm  "+mm);
					Intent intent = new Intent(FileTransfer.this,FileManagerment.class);
					intent.putExtra("mm", mm);
					startActivity(intent);
				
			}
		});
		listViewFile.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				fileHandle = shareedFileArray.get(arg2);
				AlertDialog.Builder builder = new AlertDialog.Builder(FileTransfer.this);
				builder.setTitle("要做什么");
				builder.setItems(new String[] {"打开","删除"}, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							openFile(fileHandle);
							break;
						case 1:
							deleteFile(fileHandle);
							break;
						}
						
						
						
						dialog.dismiss();
					}
				

					private void deleteFile(File fileHandle) {
						// TODO Auto-generated method stub
						fileHandle.delete();
						shareedFileArray.remove(fileHandle);
						shareFileAdapter.notifyDataSetChanged();
					}});
				builder.show();
				
				
			}});
		

		
		
		
		Log.i("guo ", "0103  "+  "server start");
		//发送自己信息的广播	
		
		new Thread(new Runnable() {
			
			
			@Override
			public void run() {
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ip = getLocalHostIp();
				broadIP = getBroadCastIP();

				me.setIp(ip);
				me.setName(name);
				
				
				while(bool){
					if(flag ==0){
						byte [] data = (ONLINE+";"+name+";"+ip).toString().getBytes();
						DatagramPacket dp;
													
							try {
								dp = new DatagramPacket(data, data.length,
										InetAddress.getByName(broadIP), Constant.UDPPORT);
								mUdp.send(dp);// 广播发送数据包
								Log.i("guo  ", "1230  发送数据               "+ ONLINE+";"+name+";"+ip);
								Thread.sleep(2000);
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}				
							
					}
					else{
						byte [] data = (OFFLINE+";"+name+";"+ip).toString().getBytes();
						DatagramPacket dp;
													
							try {
								dp = new DatagramPacket(data, data.length,
										InetAddress.getByName(broadIP), Constant.UDPPORT);
								mUdp.send(dp);// 广播发送数据包
								Log.i("guo  ", "1230  发送数据               "+ OFFLINE+";"+name+";"+ip);
								Thread.sleep(2000);
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
					}	
				}			
				
			}
		}).start();;
			
		
		
		
		
		
		
		
		

	}
	
	
	
	
	
		// 获得本地ip
			public String getLocalHostIp() {
				String ipaddress = "";
				try {
					for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
							.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
						NetworkInterface intf = mEnumeration.nextElement();
						for (Enumeration<InetAddress> enumIPAddr = intf
								.getInetAddresses(); enumIPAddr.hasMoreElements();) {
							InetAddress inetAddress = enumIPAddr.nextElement();
							// 如果不是回环地址
							if (!inetAddress.isLoopbackAddress()) {
								if (inetAddress.getHostAddress().contains("192.")) { // 直接返回本地IP地址
									ipaddress = inetAddress.getHostAddress()
											.toString();
									Log.i("LocalHostIp", "1227|" + ipaddress);
									return ipaddress;

								}

							}
						}
					}
				} catch (SocketException ex) {
					Log.e("Error", ex.toString());
				}
				return ipaddress;
			}
		//获取广播ip地址
			public String getBroadCastIP() {

				String ip = getLocalHostIp().substring(0,
						getLocalHostIp().lastIndexOf(".") + 1)
						+ "255";
				// 1226 PRINT IPG 得到192.168.43.255
				Log.i("BroadCastIp", "1226|" + ip);

				return ip;
			}
			
			
			//监听返回键
			@Override  
		    public boolean onKeyDown(int keyCode, KeyEvent event)  
		    {  
		        if (keyCode == KeyEvent.KEYCODE_BACK )  
		        {  
		            // 创建退出对话框  
		            AlertDialog isExit = new AlertDialog.Builder(this).create();  
		            // 设置对话框标题  
		            isExit.setTitle("系统提示");  
		            // 设置对话框消息  
		            isExit.setMessage("确定要退出吗");  
		            // 添加选择按钮并注册监听  
		            isExit.setButton("确定", listener);  
		            isExit.setButton2("取消", listener);  
		            // 显示对话框  
		            isExit.show();  
		  
		        }  
		          
		        return false;  
		          
		    }  
			
			
		    /**监听对话框里面的button点击事件*/  
			
		    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
		    {  
		        public void onClick(DialogInterface dialog, int which)  
		        {  
		            switch (which)  
		            {  
		            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
		            	flag = 1;
		            	WifiApAdmin.closeWifiAp(FileTransfer.this);	
		            	new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									Thread.sleep(10000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								bool = false;
							}
						}).start();
		                FileTransfer.this.finish();
		                
		                

		                break;  
		            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
		                break;  
		            default:  
		                break;  
		            }  
		        }  
		    };
		    
		    
		 // 手机打开文件的method
			private void openFile(File f) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				// 调用getMIMEType()来取得MimeType
				String type = getMIMEType(f);
				// 设定intent的file与MimeType
				intent.setDataAndType(Uri.fromFile(f), type);
				startActivity(intent);
			}
			// 判断文件MimeType的method
			private String getMIMEType(File f) {
				String type = "";
				String fName = f.getName();
				// 取得扩展名
				String end = fName
						.substring(fName.lastIndexOf(".") + 1, fName.length())
						.toLowerCase();

				// 按扩展名的类型决定MimeType
				if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
						|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
					type = "audio";
				} else if (end.equals("3gp") || end.equals("mp4")) {
					type = "video";
				} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
						|| end.equals("jpeg") || end.equals("bmp")) {
					type = "image";
				} else {
					type = "*";
				}
				// 如果无法直接打开，就弹出软件列表给用户选择
				type += "/*";
				return type;
			}
		    
		    
	
}
