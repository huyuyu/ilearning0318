/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Sharing.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/10
* Description：This java file is to be used for showing sharing interface.
* Modify history：
* 郭靖东,create file,2015/08/10
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.analysis.JsoupAnalysisLogin;
import hpecl.uclass.form.FormSubmitSharing;
import hpecl.uclass.uti.Constant;


import com.example.ilearning.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Sharing extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 功能：编辑标题、内容，提交，形成文章，对应课程网站-我的文章-上传文档-增加上传文档-增加文章
	 * 
	 */
	Button submit;
	EditText eTitle,eContent,eintroduct,eauthor,einformation,ekeyword;
	String title,content,cookie,back,introduct,author,information,keyword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharing);
		submit = (Button) findViewById(R.id.submit);
		eTitle = (EditText) findViewById(R.id.etitle);
		eContent = (EditText) findViewById(R.id.econtent);
		eintroduct = (EditText) findViewById(R.id.eintroduc);
		eauthor = (EditText) findViewById(R.id.eauthor);
		einformation = (EditText) findViewById(R.id.einformation);
		ekeyword = (EditText) findViewById(R.id.ekeyword);
		Intent intent = getIntent();
		cookie = intent.getStringExtra("cookie");
		       
		
		
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				title = eTitle.getText().toString();
				content = eContent.getText().toString();
				introduct = eintroduct.getText().toString();
				author = eauthor.getText().toString();
				information = einformation.getText().toString();
				keyword = ekeyword.getText().toString();
				if(title.equals("")){
					Toast.makeText(Sharing.this, "请输入标题内容", Toast.LENGTH_SHORT).show();
				}
				else if(content.equals("")){
					Toast.makeText(Sharing.this, "请输入正文内容", Toast.LENGTH_SHORT).show();
				}
				else if(introduct.equals("")){
					Toast.makeText(Sharing.this, "请输入内容简介内容", Toast.LENGTH_SHORT).show();
				}
				else if(author.equals("")){
					Toast.makeText(Sharing.this, "请输入作者信息", Toast.LENGTH_SHORT).show();
				}
				else if(information.equals("")){
					Toast.makeText(Sharing.this, "请输入信息来源", Toast.LENGTH_SHORT).show();
				}
				else if(keyword.equals("")){
					Toast.makeText(Sharing.this, "请输入关键字", Toast.LENGTH_SHORT).show();
				}
				else{
					new MyAsy().execute();
				}
			}
		});
	}
	class MyAsy extends AsyncTask<String, String, String> {
		ProgressDialog progressDialog;
		String handleBack;
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(Sharing.this, "请稍等...", "数据提交中...", true);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			Toast.makeText(Sharing.this, handleBack, Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			back = FormSubmitSharing.post(Constant.Sharing0A701, cookie, title, keyword,introduct,author,information,content);
			JsoupAnalysisLogin shareback = new JsoupAnalysisLogin();
			handleBack = (String) shareback.analysisInfo(back);
			Log.i("guo", "0728  "+handleBack);
			return handleBack;
		}
		
	}
}
