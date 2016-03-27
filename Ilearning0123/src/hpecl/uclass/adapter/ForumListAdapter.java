/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：ForumListAdapter.java
* Author：郭靖东
* Version：1.1
* Date：2015/07/08
* Description：This adapter is used for showing the content of forum by ListView.
* Modify history：
* 郭靖东,create file,2015/07/08
*/
package hpecl.uclass.adapter;

import hpecl.uclass.Bean.Forum;
import hpecl.uclass.uti.CustomListview;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ilearning.R;




public class ForumListAdapter extends BaseAdapter {

	private LayoutInflater mLI =null;
	private ArrayList<Forum> forumArry;


	private CustomListview lv;
	private SharedPreferences.Editor editor;
	
	EditText et;
	public ForumListAdapter(Context context,ArrayList<Forum> forumArry,EditText et,CustomListview lv,SharedPreferences.Editor editor){
		mLI = LayoutInflater.from(context);
		
		this.forumArry = forumArry;
		this.et = et;
		this.lv = lv;
		this.editor = editor;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return forumArry.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return forumArry.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh = new ViewHolder();
	
		if(arg1 == null){
			arg1 = mLI.inflate(R.layout.forumitem, null);
			vh.tv1 = (TextView) arg1.findViewById(R.id.name);
			vh.tv2 = (TextView) arg1.findViewById(R.id.time);
			vh.tv3 = (TextView) arg1.findViewById(R.id.text);
			vh.b = (Button) arg1.findViewById(R.id.reply);
			arg1.setTag(vh);
			
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tv1.setText(forumArry.get(position).getName());
		vh.tv2.setText(forumArry.get(position).getTime());
		vh.tv3.setText(forumArry.get(position).getContent());
		vh.b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(context,
//
//						"点击 " + forumArry.get(position).getName(),
//
//						Toast.LENGTH_SHORT).show();
				
				lv.setSelection(lv.getBottom());
				editor.putInt("position", position);
				editor.commit();
				Log.i("guo", "0720  "+position);
				et.setFocusable(true);
				et.setFocusableInTouchMode(true);
				et.requestFocus();
				et.findFocus();
//				et.setText(Html.fromHtml(  "<i><font color=#666666>回复"+forumArry.get(position).getName()+":"+"</font> </i>"));
				et.setHint("回复:");
//				et.setText("回复"+forumArry.get(position).getName()+":");
				
				
//				AlertDialog.Builder builder = new Builder(context);
//				final EditText eFileName =  new EditText(context);
//				builder.setTitle("请输入评论内容");
//				builder.setView(eFileName);
//				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						String reText = eFileName.getText().toString();
//						FormSubmitForum.post(Constant.FORMURL,username, password,eKey.getText().toString(),eText.getText().toString(),"");
//						 
//					}
//				});
//				builder.setNegativeButton("取消", null);
//				builder.create();
//				builder.show();
			}
		});
		return arg1;
	}
	
	public class ViewHolder{
		 TextView tv1;
		 TextView tv2;
		 TextView tv3;
		 Button b;
	 }
}
