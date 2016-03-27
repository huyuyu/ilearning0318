/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：GestureHelper.java
* Author：常青
* Version：1.1
* Date：2015/08/18
* Description：This java file is to realize switch to next picture.
* Modify history：
* 常青,create file,2015/08/18
*/
package hpecl.uclass.uti;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class GestureHelper implements OnGestureListener {
	private GestureDetector gesture_detector;
	private int screen_width;
	private OnFlingListener listener_onfling;
	
	public static abstract class OnFlingListener {
		public abstract void OnFlingLeft();
		public abstract void OnFlingRight();
		public abstract void OnImage();
	}
	
	public GestureHelper(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screen_width = dm.widthPixels;       
		gesture_detector = new GestureDetector(context, this);
	}
	
	public void setOnFlingListener(OnFlingListener listener) {
		listener_onfling = listener;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return gesture_detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// 触发条件 �??
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像�??/�??
		final int FLING_MIN_DISTANCE = (int) (screen_width / 20.0f), FLING_MIN_VELOCITY = 50;
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			listener_onfling.OnFlingLeft();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			listener_onfling.OnFlingRight();
		}

		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		listener_onfling.OnImage();
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}