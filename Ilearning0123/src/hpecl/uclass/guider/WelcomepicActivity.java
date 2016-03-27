/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：NotificationActivity.java
* Author：常青
* Version：1.2
* Date：2015/12/14
* Description：This Activity is used for showing the first image when enter the application.
* Modify history：
* 常青,create file,2015/12/14
*/

package hpecl.uclass.guider;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ilearning.R;

public class WelcomepicActivity extends Activity {

	public int screenWidth;
	public int screenHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 强制竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_welcomepic);
		// 获得屏幕大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		// 设置图片大小适应屏幕
		Bitmap bm = BitmapFactory.decodeStream(getResources().openRawResource(
				R.drawable.welcomepic));
		Bitmap newBm = scaleImg(bm, screenWidth, screenHeight);
		ImageView imageView;
		imageView = (ImageView) findViewById(R.id.file_image);
		imageView.setImageBitmap(newBm);

		new Handler().postDelayed(new Runnable() {
			@Override
			// 10
			public void run() {
					WelcomepicActivity.this.startActivity(
							new Intent(WelcomepicActivity.this,
									ScrollLayoutActivity.class));
					WelcomepicActivity.this.finish();
				
			}
		}, 2000);
	}

	protected Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}
}
