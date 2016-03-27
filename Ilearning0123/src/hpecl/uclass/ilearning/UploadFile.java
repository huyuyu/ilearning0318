/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：UploadFile.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/11
* Description：This java file is to be used for showing file upload interface, not used now.
* Modify history：
* 郭靖东,create file,2015/08/11
*/
package hpecl.uclass.ilearning;


import hpecl.uclass.form.FormSubmitUploadFileClass;
import hpecl.uclass.uti.Constant;

import com.example.ilearning.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UploadFile extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * 功能：编辑文档标题、学生姓名，学生学号，指导教师、文档简介、上传文件，对应课程网站-我的文章-上传文档-增加上传文档-增加上传文档
	 */
	EditText etitle,estudentName,estudentNumber,teacher,introduction;
	Button uploadFile,submit;
	String path = "",title,studentName,studentNumber,sTeacher,sIntroduction,cookie,backInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classuploadfile);
		init();
		Intent intent = getIntent();
		cookie = intent.getStringExtra("cookie");
		uploadFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UploadFile.this,ClassFileManagerment.class);
				startActivityForResult(intent, Constant.RESULTCODE);
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				title = etitle.getText().toString();
				studentName = estudentName.getText().toString();
				studentNumber = estudentNumber.getText().toString();
				sTeacher = teacher.getText().toString();
				sIntroduction = introduction.getText().toString();
				if(title.isEmpty()){
					Toast.makeText(UploadFile.this, "请输入文档标题", Toast.LENGTH_SHORT).show();
				}
				else if(studentName.isEmpty()){
					Toast.makeText(UploadFile.this, "请输入学生姓名", Toast.LENGTH_SHORT).show();
				}
				else if(studentNumber.isEmpty()){
					Toast.makeText(UploadFile.this, "请输入学生学号", Toast.LENGTH_SHORT).show();
				}
				else if(sTeacher.isEmpty()){
					Toast.makeText(UploadFile.this, "请输入指导教师", Toast.LENGTH_SHORT).show();
				}
				else if(sIntroduction.isEmpty()){
					Toast.makeText(UploadFile.this, "请输入文档简介", Toast.LENGTH_SHORT).show();
				}
				else if(path.isEmpty()){
					Toast.makeText(UploadFile.this, "请选择上传文件", Toast.LENGTH_SHORT).show();
				}
				else{
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
//							backInfo = FormSubmitUploadFileClass.post(Constant.UPLOADCLASSFILE, cookie, path, title, studentName, studentNumber, sTeacher, sIntroduction);
						}
					}).start();
					
					
//					JsoupAnalysisLogin jal = new JsoupAnalysisLogin();
//					String info =  (String) jal.analysisInfo(backInfo);
//					Toast.makeText(UploadFile.this,info, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
	}
	
		

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Constant.RESULTCODE){
			path = data.getStringExtra("path");
			Log.i("guo", "0806   " +path);
		}
		
		
	}

	private void init() {
		// TODO Auto-generated method stub
		etitle = (EditText) findViewById(R.id.etitle);
		estudentName = (EditText) findViewById(R.id.estudentname);
		estudentNumber = (EditText) findViewById(R.id.estudentnumber);
		teacher = (EditText) findViewById(R.id.eteacher);
		introduction = (EditText) findViewById(R.id.eintroduction);
		uploadFile = (Button) findViewById(R.id.selectfile);
		submit = (Button) findViewById(R.id.submit);
	}
	
	
	
}
