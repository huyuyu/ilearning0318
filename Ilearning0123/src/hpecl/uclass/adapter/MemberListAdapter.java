/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：CatalogListAdapter.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/25
* Description：This adapter is used for showing the content of cataloglist by ListView.
* Modify history：
* 郭靖东,create file,2015/08/25  
*/

package hpecl.uclass.adapter;



import java.util.ArrayList;

import com.example.ilearning.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemberListAdapter extends BaseAdapter {
	private LayoutInflater mLI =null;
	private ArrayList<String> catalogArry;
	
	public MemberListAdapter(Context context,
			ArrayList<String> questionArry) {
		super();
	    mLI = LayoutInflater.from(context);
		this.catalogArry = questionArry;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return catalogArry.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return catalogArry.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh = new ViewHolder();
		
		if(arg1 == null){
			arg1 = mLI.inflate(R.layout.itemforquestion, null);
			vh.catalog = (TextView) arg1.findViewById(R.id.question);
			
			arg1.setTag(vh);
			
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		vh.catalog.setText(catalogArry.get(position).split(";")[1]);

		
		
		
		return arg1;
	}
 
	class ViewHolder{
		TextView catalog;
	}
}
