/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：QuestionActivity.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/31
* Description：This java file is to be used for showing question.
* Modify history：
* 郭靖东,create file,2015/08/31 
*/
package hpecl.uclass.ilearning;

import hpecl.uclass.Bean.QuestionAnswer;
import hpecl.uclass.adapter.QuestionListAdapter;
import hpecl.uclass.analysis.JsoupAnalysisQuestion;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.GetHtmlData;
import hpecl.uclass.uti.CustomListview.OnRefreshListener;

import java.util.ArrayList;

import com.example.ilearning.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;


public class QuestionActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * this is the activity of question for answer 
	 */
	Intent intentData;
	String cookie,username,password;
	ArrayList<QuestionAnswer> questionArr = new ArrayList<QuestionAnswer>();
	ProgressDialog progressDialog;
	CustomListview lv;
	QuestionListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		intentData = getIntent();
		cookie = intentData.getStringExtra("cookie");
		username = intentData.getStringExtra("username");
		password = intentData.getStringExtra("password");
		new MyAsyncTask().execute();
		progressDialog = ProgressDialog.show(QuestionActivity.this, "请稍等...", "获取数据中...", true);
	}
	public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			JsoupAnalysisQuestion jaf = new JsoupAnalysisQuestion();
			questionArr = (ArrayList<QuestionAnswer>) jaf.analysisInfo(GetHtmlData.doInBackground(Constant.QUESTIONFORANSWER, "gbk",cookie));
			
			
			return null;
			
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setContentView(R.layout.answerforquestion);
			TextView tx = (TextView) findViewById(R.id.name);
			tx.setText("答疑解惑（0A701）:");
			Button bt = (Button) findViewById(R.id.button);
			bt.setText("我要提问");
			
			
			
			bt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(QuestionActivity.this,AskQuestion.class);
					intent.putExtra("cookie", cookie);
					startActivity(intent);
					
				}
			});
			lv = (CustomListview) findViewById(R.id.questionlistview); 
			adapter = new QuestionListAdapter(QuestionActivity.this,questionArr);
			
			lv.setAdapter(adapter);
			lv.setonRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void> () {
						  
						 @Override
						 protected Void doInBackground(Void... params) {
						              JsoupAnalysisQuestion jaf = new JsoupAnalysisQuestion();
						  			 questionArr = (ArrayList<QuestionAnswer>) jaf.analysisInfo(GetHtmlData.doInBackground(Constant.QUESTIONFORANSWER, "gbk",cookie));
						              return null;
						         }
						                      
						 protected void onPostExecute(Void result) {
						             adapter.notifyDataSetChanged();
						             lv.onRefreshComplete();
						          };						                      
						                      }.execute();
				}
			});
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.i("guo", "0818  " + arg2);
					QuestionAnswer question = questionArr.get(arg2-1);
					String url = "http://www.grandsw.com:11000/" +question.getUrl();
					String id = url.split("=")[2];
					Intent intent = new Intent(QuestionActivity.this,ForumActivity.class);
					intent.putExtra("activity", 2);
					intent.putExtra("url", url);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					intent.putExtra("cookie", cookie);					
					intent.putExtra("id", id);					
					intent.putExtra("sTile", question.getQuestionContent());					
					
					startActivity(intent);
				}
			});
			
			progressDialog.dismiss();
			
			
		}
		
	}
	
	
	
}
