/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：AddTopic.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/31
* Description：This java file is used for submitting the topic to the website.
* Modify history：
* 郭靖东,create file,2015/08/31  
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.analysis.JsoupAnalysisLogin;
import hpecl.uclass.form.FormSubmitAddTopic;

import hpecl.uclass.uti.Constant;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilearning.R;

public class AddTopic extends Activity {
	EditText equestion,ekeyword,edescribequestion,esubmitter;
	Button submit;
	String cookie;
	String info;
	Handler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic);
		init();
		Intent intent = getIntent();
		cookie = intent.getStringExtra("cookie");
		
		myHandler = new Handler() {  
	          public void handleMessage(Message msg) {   
	               switch (msg.what) {   
	                    case 1:   
	                         Toast.makeText(AddTopic.this, info, Toast.LENGTH_SHORT).show();	                         
	                         break;   
	               }   
	               if(info.contains("成功")){
		               finish();
	               }

	               super.handleMessage(msg);   
	          }   
	     };
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String title = equestion.getText().toString();
				final String keyboard = ekeyword.getText().toString();
				final String smallText = edescribequestion.getText().toString();
				final String writer = esubmitter.getText().toString();
				if(title.equals("") && 
						keyboard.equals("") && 
						smallText.equals("") && 
						writer.equals("")){
					
					Toast.makeText(AddTopic.this, "不能都为空，请重新输入！", Toast.LENGTH_SHORT).show();
					
				}
				else{
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							String back = FormSubmitAddTopic.post(Constant.Sharing0A701, cookie, title, keyboard, smallText, writer);
							JsoupAnalysisLogin jal = new JsoupAnalysisLogin();
							info = (String) jal.analysisInfo(back);
							Message message = new Message();   
		                    message.what = 1;   
		                      
		                    AddTopic.this.myHandler.sendMessage(message);
						}
					}).start();
				}
				
			}
		});
		
		
		
		
		
		
		
		
	}
	private void init() {
		equestion = (EditText) findViewById(R.id.equestion);
		ekeyword = (EditText) findViewById(R.id.ekeyword);
		edescribequestion = (EditText) findViewById(R.id.edescribequestion);
		esubmitter = (EditText) findViewById(R.id.esubmitter);
		submit = (Button) findViewById(R.id.submit);
	}
}