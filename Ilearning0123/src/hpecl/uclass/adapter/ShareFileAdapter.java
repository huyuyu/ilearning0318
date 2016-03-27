package hpecl.uclass.adapter;
/*
 * Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University�? All rights reserved.
 * 版权�?�? ©2015 北京大学软件与微电子学院 高�?�能嵌入式计算实验室
 * filename: ApkAdapter2.java
 * Author: LiHe
 * Version: 1.7
 * Date: 2012/9/25
 * Description: This adapter is used for showing the files, show by ListView.
 * Modify history: LiHe, create file,2012/9/25  
hpecl.uclass.adaptercl.umeeting.util;

/* import相关class */
import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ilearning.R;


/* 自定义的Adapter，继承android.widget.BaseAdapter */
public class ShareFileAdapter extends BaseAdapter {
	/*
	 * 变量声明 mIcon1：并到根目录的图文件 mIcon2：并到第几层的图? mIcon3：文件夹的图文件 mIcon4：文件的图片
	 */
	private LayoutInflater mInflater;
	private Bitmap mIcon4;
	private ArrayList<File> fileItems;


	/* MyAdapter的构造符，传入三个参数? */
	public ShareFileAdapter(Context context, ArrayList<File> it) {
		/* 参数初始化*/
		mInflater = LayoutInflater.from(context);
		fileItems = it;
		mIcon4 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.doc);
	}

	/* 继承BaseAdapter，需重写method */
	@Override
	public int getCount() {
		return fileItems.size();
	}

	@Override
	public Object getItem(int position) {
		return fileItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			/* 使用告定义的file_row作为Layout */
			convertView = mInflater.inflate(R.layout.sharefile_row, null);
			/* 初始化holder的text与icon */
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		/* 设定[文件或文件夹]的文字与icon */
			holder.text.setText(fileItems.get(position).getName());
			holder.icon.setImageBitmap(mIcon4);
			
		
		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		TextView text;
		ImageView icon;
	}
}