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
	final String ONLINE = "����";
	final String OFFLINE = "����";
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
		mLock = manager.createMulticastLock("test wifi"); // ����udp����Ȩ��
		mLock.acquire();
		flag = 0;
		
		Log.i("guo ","0112   "+Constant.rootPath);
		shareFile = new File(Constant.rootPath +"/uclass/shared/");
		if(!shareFile.exists()){
			shareFile.mkdirs();
		}			
		
		//��ȡ����״̬
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
					case 10://server �ļ��������
						Toast.makeText(FileTransfer.this, "�ļ��������", Toast.LENGTH_SHORT).show();
						break;
					case 11:
						// dialog��������
						AlertDialog.Builder builder = new AlertDialog.Builder(
								FileTransfer.this); // �ȵõ�������
						builder.setTitle("�Ƿ�����ļ���"); // ���ñ���
						// builder.setMessage("�Ƿ�ȷ���˳�?"); //��������
						// builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����
						// �����б���ʾ��ע���������б���ʾ�Ͳ�Ҫ����builder.setMessage()�ˣ������б������á�
						builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							
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
						builder.setNegativeButton("ȡ��", null);		
						builder.show();

						break;
					case 12:
						
						if(count == 0){
							Toast.makeText(FileTransfer.this, "���������˳���", Toast.LENGTH_SHORT).show();
							count++;
						}
						
						break;
						
				}
				
			}
			
		};
		//����tcpserver
		TCPServerForPF tcpServer = new TCPServerForPF(handle,FileTransfer.this);
		tcpServer.start();
				
		listView = (CustomListview) findViewById(R.id.listview1);
		listViewFile = (ListView) findViewById(R.id.listviewfile);
	
		Intent intent = getIntent();
		
		String info = intent.getStringExtra("info");
		name = intent.getStringExtra("name");	
		wifiFlag = intent.getIntExtra("owner", 0);
		
		Toast.makeText(FileTransfer.this, info, Toast.LENGTH_SHORT).show();
		
			
		
//		sendbroadcast.start();//��ȡ��ip���ܷ��㲥
		
		
		
		
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
		
		//ɨ������ļ���  ÿ����ɨ��
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					File [] fileList = shareFile.listFiles();
					shareedFileArray.clear();
					for(int i = 0;i<fileList.length;i++){
						shareedFileArray.add(fileList[i]);
					}
					Log.i("guo ", "0112   �����ļ�ɨ��      ");
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
		
		
		//���չ㲥
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (mUdp != null && !mUdp.isClosed()) { // ��ʼ��buffer
					for (int i = 0; i < Constant.RECV_BUF_SIZE; i++) {
						recvBuf[i] = 0;
					}
					DatagramPacket rdp = new DatagramPacket(recvBuf, recvBuf.length);
					try {
						// ���չ㲥���������ݰ�
						mUdp.receive(rdp);
						Log.i("time", "receivetime");
					} catch (IOException e) {
						e.printStackTrace();
					}
					// �����ݰ����н���
					parsePackage(new String(rdp.getData()).trim());
					Log.i("time", "parsetime");
					
				}
			}
			//���յ��㲥���ݽ�������

				// TODO Auto-generated method stub
			private void parsePackage(String recvBuf) {
					
			int num = arrayMM.size();
			
			//������߳�Ա
			if(recvBuf.startsWith(ONLINE) && arrayMM.indexOf(recvBuf) == -1){
				arrayMM.add(recvBuf);
			}
			Log.i("guo  ", "1230    ɾ��               "+ recvBuf);
			//ɾ�����߳�Ա
			if(recvBuf.startsWith(OFFLINE)){
				String []string = recvBuf.split(";");
				String deletMember = ONLINE;
				for(int i = 1;i<string.length;i++){
					deletMember +=  ";"+string[i];
				}
				Log.i("guo  ", "1230    ɾ��               "+ recvBuf);
				arrayMM.remove(deletMember);
			}
			//����message֪ͨlist����
			if(num != arrayMM.size()){
				Message message = new Message();
				message.what = 1;
				handle.sendMessage(message);
			}
			
			Log.i("guo  ", "1230  �㲥����               "+ recvBuf);
		
				
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
				builder.setTitle("Ҫ��ʲô");
				builder.setItems(new String[] {"��","ɾ��"}, new DialogInterface.OnClickListener(){

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
		//�����Լ���Ϣ�Ĺ㲥	
		
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
								mUdp.send(dp);// �㲥�������ݰ�
								Log.i("guo  ", "1230  ��������               "+ ONLINE+";"+name+";"+ip);
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
								mUdp.send(dp);// �㲥�������ݰ�
								Log.i("guo  ", "1230  ��������               "+ OFFLINE+";"+name+";"+ip);
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
	
	
	
	
	
		// ��ñ���ip
			public String getLocalHostIp() {
				String ipaddress = "";
				try {
					for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
							.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
						NetworkInterface intf = mEnumeration.nextElement();
						for (Enumeration<InetAddress> enumIPAddr = intf
								.getInetAddresses(); enumIPAddr.hasMoreElements();) {
							InetAddress inetAddress = enumIPAddr.nextElement();
							// ������ǻػ���ַ
							if (!inetAddress.isLoopbackAddress()) {
								if (inetAddress.getHostAddress().contains("192.")) { // ֱ�ӷ��ر���IP��ַ
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
		//��ȡ�㲥ip��ַ
			public String getBroadCastIP() {

				String ip = getLocalHostIp().substring(0,
						getLocalHostIp().lastIndexOf(".") + 1)
						+ "255";
				// 1226 PRINT IPG �õ�192.168.43.255
				Log.i("BroadCastIp", "1226|" + ip);

				return ip;
			}
			
			
			//�������ؼ�
			@Override  
		    public boolean onKeyDown(int keyCode, KeyEvent event)  
		    {  
		        if (keyCode == KeyEvent.KEYCODE_BACK )  
		        {  
		            // �����˳��Ի���  
		            AlertDialog isExit = new AlertDialog.Builder(this).create();  
		            // ���öԻ������  
		            isExit.setTitle("ϵͳ��ʾ");  
		            // ���öԻ�����Ϣ  
		            isExit.setMessage("ȷ��Ҫ�˳���");  
		            // ���ѡ��ť��ע�����  
		            isExit.setButton("ȷ��", listener);  
		            isExit.setButton2("ȡ��", listener);  
		            // ��ʾ�Ի���  
		            isExit.show();  
		  
		        }  
		          
		        return false;  
		          
		    }  
			
			
		    /**�����Ի��������button����¼�*/  
			
		    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
		    {  
		        public void onClick(DialogInterface dialog, int which)  
		        {  
		            switch (which)  
		            {  
		            case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����  
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
		            case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���  
		                break;  
		            default:  
		                break;  
		            }  
		        }  
		    };
		    
		    
		 // �ֻ����ļ���method
			private void openFile(File f) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				// ����getMIMEType()��ȡ��MimeType
				String type = getMIMEType(f);
				// �趨intent��file��MimeType
				intent.setDataAndType(Uri.fromFile(f), type);
				startActivity(intent);
			}
			// �ж��ļ�MimeType��method
			private String getMIMEType(File f) {
				String type = "";
				String fName = f.getName();
				// ȡ����չ��
				String end = fName
						.substring(fName.lastIndexOf(".") + 1, fName.length())
						.toLowerCase();

				// ����չ�������;���MimeType
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
				// ����޷�ֱ�Ӵ򿪣��͵�������б���û�ѡ��
				type += "/*";
				return type;
			}
		    
		    
	
}
