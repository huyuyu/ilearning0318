/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：PersonCload.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/31
* Description：This java file is to be used for cloud, not used now.
* Modify history：
* 郭靖东,create file,2015/06/31 
*/
package hpecl.uclass.ilearning;


import hpecl.uclass.uti.AbilityAPI;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ilearning.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.open189.api.AuthView;
import cn.open189.api.BigLoginButton;
import cn.open189.api.http.Callback;


public class PersonCload extends Activity {
	private static final int LOGIN_REQUEST_CODE = 1109;
	BigLoginButton button;
	Button bInfo,bCreateFolder,bGetFileList,bUploadFile;
	TextView tv ;
	String app_id = "417879940000244158";
	String app_secret = "663364f728e6986183b8a77fed943784";
	
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloud);
		editor = getSharedPreferences("token", MODE_PRIVATE).edit();
		
		button = (BigLoginButton) findViewById(R.id.buttonSmallLogin);
		tv = (TextView) findViewById(R.id.show);
		bInfo = (Button) findViewById(R.id.info);
		bCreateFolder = (Button) findViewById(R.id.creatfolder);
		bGetFileList = (Button) findViewById(R.id.getfilelist);
		bUploadFile = (Button) findViewById(R.id.uploadfile);
		SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
		String access_token = sp.getString("access_token", "");
		Log.i("guo", "0710   "+access_token);
		
		if(access_token.isEmpty()){
			getToken();
		}
		
		
		//cloud info
		bInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle params=new Bundle();
				SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
				String access_token = sp.getString("access_token", "");
				if(!access_token.isEmpty()){
					params.putString("format", "json");
					 params.putString("access_token", access_token);
					 params.putString("app_id", app_id);


					 AbilityAPI.makeCall("http://api.189.cn/ChinaTelecom/getUserInfo.action", params, new Callback() {
						@Override
						public void onSuccess(final Object object) {
							PersonCload.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									tv.setText(""+object);
								}
							});
						}
						@Override
						public void onFail(final int code,final Object object) {
							PersonCload.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {								
									tv.setText(code+":"+object);
									getToken();
								}
							});
						}
						@Override
						public void onException(final Throwable throwable) {
							PersonCload.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									tv.setText(throwable.getLocalizedMessage());
								}
							});
						}
					});
					
				}
				 
			}
		});
		//create file folder
		bCreateFolder.setOnClickListener(new OnClickListener() {
			String getFileName;
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(PersonCload.this);
				final EditText eFileName =  new EditText(PersonCload.this);
				builder.setTitle("请输入创建的文件夹名称");
				builder.setView(eFileName);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						getFileName = eFileName.getText().toString();
						Bundle params=new Bundle();
						SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
						String access_token = sp.getString("access_token", "");
						if(!access_token.isEmpty()){
							 params.putString("format", "json");
							 params.putString("access_token", access_token);
							 params.putString("app_id", app_id);
							 params.putString("folderName", getFileName);

							 AbilityAPI.makeCall("http://api.189.cn/ChinaTelecom/createFolder.action", params, new Callback() {
								@Override
								public void onSuccess(final Object object) {
									PersonCload.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											tv.setText(""+object);
										}
									});
								}
								@Override
								public void onFail(final int code,final Object object) {
									PersonCload.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											tv.setText(code+":"+object);
											getToken();
										}
									});
								}
								@Override
								public void onException(final Throwable throwable) {
									PersonCload.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											tv.setText(throwable.getLocalizedMessage());
										}
									});
								}
							});
						}
						 
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create();
				builder.show();
				 
			}
		});
		
		bGetFileList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFileList();
				
			}
		});
		bUploadFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(PersonCload.this,FileManagerment.class);
				startActivity(intent);
				
				
				
			}
		});
		
	}
	
	
	
	
	
	
	private void getFileList() {
		// TODO Auto-generated method stub
		Bundle params=new Bundle();
		SharedPreferences sp = getSharedPreferences("token",MODE_PRIVATE);
		String access_token = sp.getString("access_token", "");
		
		if(!access_token.isEmpty()){
			params.putString("format", "json");
			 params.putString("access_token", access_token);
			 params.putString("app_id", app_id);
			 params.putString("orderBy", "filename");
			 params.putInt("pageNum", 1);
			 params.putInt("pageSize", 20);

			 AbilityAPI.makeCall("http://api.189.cn/ChinaTelecom/listFiles.action", params, new Callback() {
				@Override
				public void onSuccess(final Object object) {
					PersonCload.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							JSONObject json = null;
							JSONArray jsonFolderArray,jsonFileArry;
							
							try {
								json = new JSONObject((String)object);
								jsonFolderArray = json.getJSONObject("fileList").getJSONArray("folder");
								jsonFileArry = json.getJSONObject("fileList").getJSONArray("file");
								for(int i = 0;i<jsonFolderArray.length();i++){
									JSONObject jsonObject = jsonFolderArray.getJSONObject(i);
									Log.i("guo", "0709  "+jsonObject.getString("name"));
									Log.i("guo", "0709  "+jsonObject.getString("createDate"));
								}
								for(int i = 0;i<jsonFileArry.length();i++){
									JSONObject jsonObject = jsonFileArry.getJSONObject(i);
									Log.i("guo", "0709  "+jsonObject.getString("name"));
									Log.i("guo", "0709  "+jsonObject.getString("createDate"));
									Log.i("guo", "0709  "+jsonObject.getString("size"));
								}
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
						}
					});
				}
				@Override
				public void onFail(final int code,final Object object) {
					PersonCload.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.setText(code+":"+object);
						}
					});
				}
				
				
				
				
				@Override
				public void onException(final Throwable throwable) {
					PersonCload.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.setText(throwable.getLocalizedMessage());
						}
					});
				}
			});
		}
		
		 
	}






	private void getToken() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PersonCload.this, AuthView.class);
	    intent.putExtra("app_id",app_id);	     
        intent.putExtra("app_secret",app_secret);
        intent.putExtra("display", "mobile"); 	
        intent.putExtra("redirect_uri","http://218.85.135.158:10008/open189-upc/callback.jsp"); 
	    startActivityForResult(intent, LOGIN_REQUEST_CODE);
	}

	/* Author: guo
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == LOGIN_REQUEST_CODE) {
			Bundle bundle = data.getExtras();
			
			StringBuilder sb = new StringBuilder();
			sb.append("res_code=" + bundle.getString("res_code") + "\n");   
			sb.append("res_message=" + bundle.getString("res_message") + "\n"); 
			sb.append("access_token=" + bundle.getString("access_token")+ "\n");
			String access_token = null;
			access_token = bundle.getString("access_token");
			editor.putString("access_token", access_token);
			editor.commit();
			sb.append("expires_in=" + bundle.getString("expires_in") + "\n");	
			sb.append("scope=" + bundle.getString("scope") + "\n");	
			sb.append("state=" + bundle.getString("state") + "\n");
			sb.append("open_id " + bundle.getString("open_id") + "\n"); 
			tv.setText(sb.toString());
		}
	}
	
	
	
}
