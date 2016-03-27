/*
* Copyright  ?2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ?2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：LogIn.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/27
* Description：This java file is used for showing the login interface, and realizing the function of login.
* Modify history：
* 郭靖东,create file,2015/06/27  
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.Bean.Key;
import hpecl.uclass.analysis.JsoupAnalysisLogin;
import hpecl.uclass.form.FormSubmitLogin;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.GetImageFromUrl;
import hpecl.uclass.uti.IsNetworkAvailable;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ilearning.R;


public class LogIn extends Activity {
	Key imageKey;
	ImageView image;
	EditText eusername,epassword,eidentifyingcode;
	Button login,fresh,register;
	CheckBox cb;
	String username,password,key;
	String mainUrl = "http://www.grandsw.com:9191/class/";
	SharedPreferences sp,sp_conf;
	SharedPreferences.Editor spEditor;
	String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();//sd卡根目录
	String charset = "GB2312";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);
        
        sp = getSharedPreferences("user", MODE_PRIVATE);
        spEditor = sp.edit();
        
        init();
        Boolean net = IsNetworkAvailable.isNetworkAvailable(LogIn.this);
        if(net){
        	new MyGetBitMap().execute();
        	final Handler msgHandler = new Handler(){  
                  public void handleMessage(Message msg) {  
                          switch (msg.arg1) {  
                          case 1:  
                                  Toast.makeText(getApplicationContext(),"登录成功！", Toast.LENGTH_SHORT).show();  
                                  break;  
                          case 2:
                          		Toast.makeText(getApplicationContext(),"登录失败！", Toast.LENGTH_SHORT).show();  
                          		break;  
                          default:  
                                  break;  
                          }  
                  }  
          };
              login.setOnClickListener(new OnClickListener() {
      			
      			@Override
      			public void onClick(View arg0) {
      				// TODO Auto-generated method stub
      				if(!eusername.getText().toString().equals("")){
      					username = eusername.getText().toString();
      				}else{
      					Toast.makeText(getApplicationContext(), "Please, input username!",Toast.LENGTH_SHORT ).show();
      				}
      				if(!epassword.getText().toString().equals("")){
      					password = epassword.getText().toString();
      				}else{
      					Toast.makeText(getApplicationContext(), "Please, input password!",Toast.LENGTH_SHORT ).show();
      				}
      				if(!eidentifyingcode.getText().toString().equals("")){
      					key = eidentifyingcode.getText().toString();
      				}else{
      					Toast.makeText(getApplicationContext(), "please, input key!",Toast.LENGTH_SHORT ).show();
      				}

      				
      				new Thread(new Runnable() {
      					
      					@Override
      					public void run() {
      						// TODO Auto-generated method stub
      						//submit form，get back info，0 is the body of HTML 1 is the cookie of the HTML。
      						ArrayList<String> backCookie = FormSubmitLogin.post(Constant.FORMURL,username, password,key,imageKey);						
      						//get login info success or faile, back from body.
      						JsoupAnalysisLogin jal = new JsoupAnalysisLogin();
      						String info = (String) jal.analysisInfo(backCookie.get(0));
      						Log.i("guo", "login 0706 "+info);
      						//bring cookie access to the home pager, 		
//      						String backInfo = GetHtmlData.doInBackground(mainUrl, charset,backCookie.get(1));
//      						Log.i("guo", "0722   "+  backCookie.get(1));
//      						String backInfo1 = GetHtmlData.doInBackground("www.grandsw.com:9191/class/e/action/ShowInfo?classid=97&id=854", charset,backCookie.get(1));
//      						Log.i("guo", "0707   "+backInfo1);
      						//show the info on the UI, because of the Thread, use Handle Message.						
      						Message msg = msgHandler.obtainMessage(); 		
      						
      						if(info.endsWith("登陆成功!")){		
      							msg.arg1 = 1;  						
      							msgHandler.sendMessage(msg);
      							Intent intent = new Intent(LogIn.this,DownLoadActivity.class);
      							intent.putExtra("username", username);
      							intent.putExtra("password", password);	
      							intent.putExtra("cookie", backCookie.get(1));
      							startActivity(intent);
      							finish();
      						}else {
      							msg.arg1 = 2;
      							msgHandler.sendMessage(msg);
      						}
      							
      						
      						
      					}
      				}).start();
      				
      			}
      		});
              fresh.setOnClickListener(new OnClickListener() {
      			
      			@Override
      			public void onClick(View v) {				
      				new MyGetBitMap().execute();
      			}
      		});
              register.setOnClickListener(new OnClickListener() {
      			
      			@Override
      			public void onClick(View v) {
      				Intent intent = new Intent(hpecl.uclass.ilearning.LogIn.this,hpecl.uclass.ilearning.RegisterActivity.class);
      				startActivity(intent);
      			}
      		});
              
              
              cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){  
                  @Override  
                  public void onCheckedChanged(CompoundButton buttonView,  
                          boolean isChecked) {  
                      // TODO Auto-generated method stub  
                      if(isChecked){  
                          
                          String username = eusername.getText().toString();
                          String password = epassword.getText().toString();
                          spEditor.putString("username", username);
                          spEditor.putString("password", password);
                          spEditor.commit();
                          
                      }else{  
                            
                      }  
                  }  
              });  
        }
        
        else{
        	Toast.makeText(LogIn.this, "应用需要连接网络，请连接网络！!", Toast.LENGTH_LONG).show();
        }
        
      
        
        
        
    }

    private void init() {
		// TODO Auto-generated method stub
    	eusername = (EditText) findViewById(R.id.eusername);
    	epassword = (EditText) findViewById(R.id.epassword);
    	String username = sp.getString("username", ""); 
        String password = sp.getString("password", "");
    	eusername.setText(username);
    	epassword.setText(password);
    	eidentifyingcode = (EditText) findViewById(R.id.eidentifyingcode);
    	login = (Button) findViewById(R.id.login);
    	register = (Button) findViewById(R.id.register);
    	fresh = (Button) findViewById(R.id.fresh);
    	image = (ImageView) findViewById(R.id.identifyingcodepicture);
    	cb = (CheckBox) findViewById(R.id.checkBox);
	}



	class MyGetBitMap extends AsyncTask<String ,String ,String >{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();			
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			imageKey =  GetImageFromUrl.getImageBitmap(Constant.IMAGEKEY);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			image.setImageBitmap(imageKey.getBitmap());
		}
    	
    }
	
	
}
