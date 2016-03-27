/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：ConcernReadingListAdapter.java
* Author：郭靖东
* Version：1.1
* Date：2015/08/18
* Description：This adapter is used for showing the content of concern reading by ListView.
* Modify history：
* 郭靖东,create file,2015/08/18
*/
package hpecl.uclass.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ilearning.R;

public class WifiListAdapter extends BaseAdapter {

	private ArrayList<String> mList;
	private LayoutInflater mLI = null;

	public WifiListAdapter(Context myAsyncTask, ArrayList<String> mList) {
		super();
		mLI = LayoutInflater.from(myAsyncTask);
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh = new ViewHolder();
		if (arg1 == null) {
			arg1 = mLI.inflate(R.layout.notification_item, null);
			vh.tv1 = (TextView) arg1.findViewById(R.id.tv1);
			//vh.tv2 = (TextView) arg1.findViewById(R.id.tv2);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		vh.tv1.setText(mList.get(arg0));
		//vh.tv2.setText(mList.get(arg0).getHref());

		return arg1;
	}

	/*
	 * ViewHolder将需要缓存的view封装好，convertView的setTag才是将这些缓存起来供下次调用。
	 * 当你的listview里布局多样化的时候 viewholder的作用体现明显，效率再一次提高。
	 * View的findViewById()方法也是比较耗时的，因此需要考虑只调用一次，
	 * 之后就用View.getTag()方法来获得ViewHolder对象。
	 */
	public class ViewHolder {
		TextView tv1;
	}
}
