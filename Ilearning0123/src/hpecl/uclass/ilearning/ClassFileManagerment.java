/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：ClassFileManagerment.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/06
* Description：This java file is used for choosing a file from the SDcard, not used now.
* Modify history：
* 郭靖东,create file,2015/07/06  
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.adapter.FileListAdapter;
import hpecl.uclass.uti.AbilityAPI;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.GetHtmlData;
import hpecl.uclass.uti.UploadFile;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.ilearning.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.open189.api.http.Callback;


public class ClassFileManagerment extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private List<String> items;
	private List<String> paths;
	private String currentpath;
	private String rootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	FileListAdapter fAdapter;
	ListView lv;
	String app_id = "417879940000244158";
	String app_secret = "663364f728e6986183b8a77fed943784";
	String uploadFileId;
	File uploadFile;
	long fileSize,statusFileSize=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadfile);
		lv = (ListView) findViewById(R.id.filelist);
		getFileDir(rootPath);
		fAdapter = new FileListAdapter(ClassFileManagerment.this, items, paths);
		lv.setAdapter(fAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				File file = new File(paths.get(position));

				// 0=doc,1=media,2=image,3=apk,4=allfiles
				int i = 4;

				// filePath=docPath.get(position);
				int pos = position;
				if (file.canRead()) {
					if (file.isDirectory()) {
						// 如果是文件夹就运行getFileDir()
						getFileDir(paths.get(position));
						fAdapter = new FileListAdapter(ClassFileManagerment.this, items, paths);
						lv.setAdapter(fAdapter);
					} else {
						// 如果是文件调用fileHandle()
						fileHandle(file, pos, i);
					}
				} 
			}
		});
	}
	
	private void getFileDir(String filePath) {
		// 设置目前所存路径
		currentpath = filePath;
		items = new ArrayList<String>();
		paths = new ArrayList<String>();

		File f = new File(filePath);
		// ljj430若documents不存在则创建
		if (!f.exists())// 如果不存在
		{
			f.mkdirs();// 创建
		}
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			// 第一笔设定为[并到根目录]
			items.add("b1");
			paths.add(rootPath);
			// 第二笔设定为[并勺层]
			items.add("b2");
			paths.add(f.getParent());
		}
		// 将所有文件放入ArrayList中
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}
	}
	

	// 处理文件的method
		private void fileHandle(final File file, final int pos, final int i) {
			// 按下文件时的OnClickListener
			OnClickListener listener1 = new DialogInterface.OnClickListener() {



				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) // "打开文件","更改文件名","删除文件","添加共享文件"
					{
						// 选择的item为打开文件
//						upload(file);
						try {
//							Log.i("guo", "0731 "+file.getName());
//							uploadDepartmentTime(file);
							
							Intent intent = new Intent();
							intent.putExtra("path", file.getAbsolutePath());
							setResult(Constant.RESULTCODE, intent);
							finish();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				}

				

				
//				private long getFileSize(File file) throws Exception {
//					  long size = 0;
//					  if (file.exists()) {
//					   FileInputStream fis = null;
//					   fis = new FileInputStream(file);
//					   size = fis.available();
//					  } else {
//					   file.createNewFile();
//					   Log.e("获取文件大小", "文件不存在!");
//					  }
//					  return size;
//					 }
//				
				
				
				
				
				
				
				
			};

			// 选择几个文件时，弹出要处理文件的ListDialog
			String[] menu = { "选择文件" };
			new AlertDialog.Builder(ClassFileManagerment.this).setTitle("你要做什么?")
					.setItems(menu, listener1)
					.setPositiveButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
		}
		
//		
//		 public static String getFileMD5(File file) {
//			  if (!file.isFile()) {
//			   return null;
//			  }
//			  MessageDigest digest = null;
//			  FileInputStream in = null;
//			  byte buffer[] = new byte[1024];
//			  int len;
//			  try {
//			   digest = MessageDigest.getInstance("MD5");
//			   in = new FileInputStream(file);
//			   while ((len = in.read(buffer, 0, 1024)) != -1) {
//			    digest.update(buffer, 0, len);
//			   }
//			   in.close();
//			  } catch (Exception e) {
//			   e.printStackTrace();
//			   return null;
//			  }
//			  BigInteger bigInt = new BigInteger(1, digest.digest());
//			  return bigInt.toString(16);
//			 }
		 
		 
//		 private void upload(File file) {
//				// TODO Auto-generated method stub
//				Bundle params=new Bundle();
//				SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
//				final String access_token = sp.getString("access_token", "");
//				if(!access_token.isEmpty()){
//					 params.putString("format", "json");
//					 params.putString("access_token", access_token);
//					 params.putString("app_id", app_id);
//			
//			
//					 AbilityAPI.makeCall("http://api.189.cn/ChinaTelecom/getFileUploadUrl.action", params, new Callback() {
//						@Override
//						public void onSuccess(final Object object) {
//							FileManagerment.this.runOnUiThread(new Runnable() {
//								
//								@Override
//								public void run() {
//									try {
//										JSONObject json = new JSONObject((String)object);
//										final String uploadURL = json.getString("FileUploadUrl")+"&app_id="+app_id+"&access_token="+access_token;
//										new Thread(new Runnable() {
//											
//											@Override
//											public void run() {
//												// TODO Auto-generated method stub
//												try {
//													UploadFIle.uploadFile(uploadURL);
//												} catch (MalformedURLException e) {
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//											}
//										}).start();
////										tv.setText(""+uploadURL);
//									} catch (JSONException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//									
//									
//								}
//							});
//						}
//			
//						@Override
//						public void onException(Throwable arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//			
//						@Override
//						public void onFail(int arg0, Object arg1) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//					});
//				}
//			}
		 
//		 private void uploadDepartmentTime(File file) throws Exception {
//				// TODO Auto-generated method stub
//			    
//				Bundle params=new Bundle();
//				SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
//				final String access_token = sp.getString("access_token", "");
//				String fileName = file.getName();
//				fileSize = file.length();
//				String MD5 = getFileMD5(file);
//				
//				Date time=new Date(file.lastModified());
//				
//				Format simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//				String lastWrite = simpleFormat.format(time);
//				//Log.i("guo", "0731 "+lastWrite);
//				String localPath = file.getPath();
//				uploadFile = file;
//				if(!access_token.isEmpty()){
//					 params.putString("format", "json");
//					 params.putString("access_token", access_token);
//					 params.putString("app_id", app_id);
//					 params.putString("filename", fileName);
//					 params.putLong("size", fileSize);
//					 params.putString("md5", MD5);
//					 params.putString("lastWrite", lastWrite);
//					 params.putString("localPath", localPath);
//					 
//					 AbilityAPI.makePostCall("http://api.189.cn/ChinaTelecom/createUploadFile.action", params, new Callback() {
//						@Override
//						public void onSuccess(final Object object) {
//							ClassFileManagerment.this.runOnUiThread(new Runnable() {
//								
//								@Override
//								public void run() {
//									try {
//										JSONObject json = new JSONObject((String)object);
//										String res_message = json.getString("res_message");
//										uploadFileId = json.getString("uploadFileId");
//										final String uploadURL = json.getString("fileUploadUrl")+"&app_id="+app_id+"&access_token="+access_token;
////										final String uploadURL = "http://upload.cloud.189.cn/v5/uploadFileData.action"+"?app_id="+app_id+"&access_token="+access_token;
//										Log.i("guo", "0731 "+res_message);
//										 Log.i("guo", "0731 "+uploadFileId);
//										 Log.i("guo", "0731 "+fileSize);
//										 Log.i("guo", "0731 "+json.getString("fileDataExists"));
//										 Log.i("guo", "0731 "+uploadURL);
//										 
//									 if(!json.getString("fileDataExists").equals("1")){
//										new Thread(new Runnable() {
//											
//											@Override
//											public void run() {
//												// TODO Auto-generated method stub
//												try {
////													while(statusFileSize<fileSize){
////														
////													}
//													
////													Bundle params=new Bundle();
////													params.putString("app_id", app_id);
////													params.putString("access_token", access_token);
////													params.putString("uploadFileId", uploadFileId);
//													//文件断点上传
//													UploadFIle.uploadFile(uploadURL,uploadFile,uploadFileId,statusFileSize);
//													Log.i("guo", "0731 "+"文件上传");
//													//获取文件上传状态
//													 
//													 
//													 
//												} catch (MalformedURLException e) {
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//											}
//										}).start();
////										tv.setText(""+uploadURL);
//										}else{
//											Toast.makeText(ClassFileManagerment.this, "文件已存在", Toast.LENGTH_SHORT).show();
//										}
//									} catch (JSONException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//									
//									
//								}
//							});
//						}
//			
//						@Override
//						public void onException(Throwable arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//			
//						@Override
//						public void onFail(int arg0, Object arg1) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//					});
//				}
//			}
}
