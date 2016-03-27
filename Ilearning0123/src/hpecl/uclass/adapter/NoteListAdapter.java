/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：CatalogListAdapter.java
* Author：郭威
* Version：1.1
* Date：2015/08/25
* Description：This adapter is used for showing the content of cataloglist by ListView.
* Modify history：
* 郭威,create file,2015/10/25  
*/
package hpecl.uclass.adapter;


import java.util.List;

import com.example.ilearning.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {
	private LayoutInflater myLI =null;
	private List<String> noteName;
	
	public NoteListAdapter(Context context,
			List<String> noteName) {
		super();
	    myLI = LayoutInflater.from(context);
		this.noteName = noteName;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noteName.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return noteName.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				ViewHolder vh = new ViewHolder();
				
				if(arg1 == null){
					arg1 = myLI.inflate(R.layout.itermfornotes, null);
					vh.notename = (TextView) arg1.findViewById(R.id.notename);
					
//					vh.questionContent = (TextView) arg1.findViewById(R.id.questioncontent);
//					vh.asker = (TextView) arg1.findViewById(R.id.asker);
					
					arg1.setTag(vh);
					
				}else{
					vh = (ViewHolder) arg1.getTag();
				}
				vh.notename.setText(noteName.get(arg0));
				
//				vh.questionContent.setText(questionArry.get(position).getQuestionContent());
//				vh.asker.setText(questionArry.get(position).getAsker());
				
				
				
				return arg1;
	}

	class ViewHolder{
		TextView notename;

	}
}
