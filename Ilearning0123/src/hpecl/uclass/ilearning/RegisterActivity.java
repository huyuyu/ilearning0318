/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：RegisterActivity.java
* Author：常青
* Version：1.1
* Date：2015/08/20
* Description：This java file is to be used for showing register interface.
* Modify history：
* 常青,create file,2015/08/20
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.analysis.JsoupAnalysisLogin;
import hpecl.uclass.analysis.JsoupAnalysisRegister;
import hpecl.uclass.form.FormSubmitRegister;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.GetHtmlData;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilearning.R;

public class RegisterActivity extends Activity implements OnClickListener{
	
	
	String value;//groupid value
	String username,password,repassword,mail,academy,phone,name,info;
	Handler handle;
	private EditText mUser;
	private EditText mPassword;
	private EditText mRePassword;
	private EditText mMail;
	private EditText mPhone,mAcademy,mName;
	
	private Button mRegisterBtn;
	private Button mBackBtn;
	
	ActionBar actionBar; //声明ActionBar
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉actionbar
		setContentView(R.layout.activity_register);
		
		actionBar = getActionBar(); //得<span></span>到ActionBar
		actionBar.hide(); //隐藏ActionBar
		handle = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what){
				case 0:
					Toast.makeText(RegisterActivity.this, info, Toast.LENGTH_SHORT).show();
					if(info.equals("注册成功，正在进入主页。。。。。。")){
						finish();
					}
					break;
				}
			}
			
		};
		
		mUser = (EditText) findViewById(R.id.register_user_edit);
		mPassword = (EditText) findViewById(R.id.register_passwd_edit);
		mRePassword = (EditText) findViewById(R.id.re_register_passwd_edit);
		mMail = (EditText) findViewById(R.id.register_mail_edit);
		mAcademy = (EditText) findViewById(R.id.register_academy_edit);
		mName = (EditText) findViewById(R.id.register_realname_edit);
		
		mPhone = (EditText) findViewById(R.id.register_phone_edit);

		mRegisterBtn = (Button) findViewById(R.id.register_register_btn);
		mRegisterBtn.setOnClickListener(this);
		mBackBtn = (Button) findViewById(R.id.btn_back);
		mBackBtn.setOnClickListener(this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				JsoupAnalysisRegister jar = new JsoupAnalysisRegister();
				value = (String) jar.analysisInfo(GetHtmlData.doInBackground(Constant.REGISTER, "GB2312", ""));
				Log.i("guo", "0826    "+ value);
			}
		}).start();
	}
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.register_register_btn:
				reigster();			
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}


	private void reigster() {
		
			
		
		username = mUser.getText().toString();
		password = mPassword.getText().toString();
		repassword = mRePassword.getText().toString();
		mail = mMail.getText().toString();
		academy = mAcademy.getText().toString();
		name = mName.getText().toString();
		phone = mPhone.getText().toString();
		
		if(username.equals("")){
			Toast.makeText(RegisterActivity.this, "用户名不能为空！！", Toast.LENGTH_SHORT).show();
		}
		else if(password.equals("")){
			Toast.makeText(RegisterActivity.this, "密码不能为空！！", Toast.LENGTH_SHORT).show();
		}
		else if(password.length() < 8){
			Toast.makeText(RegisterActivity.this, "密码长度不能少于8位！！", Toast.LENGTH_SHORT).show();
		}
		else if(repassword.equals("")){
			Toast.makeText(RegisterActivity.this, "重复密码不能为空！！", Toast.LENGTH_SHORT).show();
		}
		else if(mail.equals("")){
			Toast.makeText(RegisterActivity.this, "邮箱不能为空！！", Toast.LENGTH_SHORT).show();
		}
		else if(academy.equals("")){
			Toast.makeText(RegisterActivity.this, "院系不能为空！！", Toast.LENGTH_SHORT).show();
		}		
		else if(name.equals("")){
			Toast.makeText(RegisterActivity.this, "真实姓名不能为空！！", Toast.LENGTH_SHORT).show();
		}		
		else if(phone.equals("")){
			Toast.makeText(RegisterActivity.this, "手机不能为空！！", Toast.LENGTH_SHORT).show();
		}		
		else if(!password.equals(repassword)){
			Toast.makeText(RegisterActivity.this, "您两次密码输入不一致！！", Toast.LENGTH_SHORT).show();
		}		  
				
		else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String back = FormSubmitRegister.post(Constant.FORMURL, username, password, value, mail, phone, name, academy);
					Log.i("guo", "0826    "+ back);
					JsoupAnalysisLogin fsl = new JsoupAnalysisLogin();
					info = (String) fsl.analysisInfo(back);
					Message m = new Message();
					m.what = 0;
					handle.sendMessage(m);
				}
			}).start();
		}
		
		
		
		
	}
}
