/*
* Copyright  漏2015 by HPEC Lab锛孲chool of Software and Microelectronics锛孭eking University锛�All rights reserved.
* 鐗堟潈鎵�湁 漏2015 鍖椾含澶у杞欢涓庡井鐢靛瓙瀛﹂櫌 楂樻�鑳藉祵鍏ュ紡璁＄畻瀹為獙瀹�* filename锛歂otificationActivity.java
* Author锛氬父闈�* Version锛�.2
* Date锛�015/12/16
* Description锛歍his Activity is used for showing the welcome activity page change.
* Modify history锛�* 甯搁潚,create file,2015/12/16
*/

package hpecl.uclass.guider;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ilearning.R;


public class SettingScrollLayoutActivity extends Activity implements
		OnViewChangeListener {

	private ScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startBtn;
	private RelativeLayout mainRLayout;
	private LinearLayout pointLLayout;
	private LinearLayout leftLayout;
	private LinearLayout rightLayout;
	private LinearLayout animLayout;
	SharedPreferences sp;
	SharedPreferences.Editor editor; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 闅愯棌鏍囬鏍�		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 闅愯棌鐘舵�鏍�		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 寮哄埗绔栧睆
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
			
		setContentView(R.layout.startup);
		initView();
		
		
	}

	private void initView() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout); // 婊戝姩鎺т欢
		pointLLayout = (LinearLayout) findViewById(R.id.llayout); // 灏忓渾鐐圭殑甯冨眬
		mainRLayout = (RelativeLayout) findViewById(R.id.mainRLayout);
		startBtn = (Button) findViewById(R.id.startBtn); // 寮�鎸夐挳
		startBtn.setOnClickListener(onClick);
		animLayout = (LinearLayout) findViewById(R.id.animLayout); // 鍔ㄧ敾鏁堟灉
		leftLayout = (LinearLayout) findViewById(R.id.leftLayout); // 鍚戝乏鍔ㄧ敾鏁堟灉
		rightLayout = (LinearLayout) findViewById(R.id.rightLayout); // 鍚戝彸鍔ㄧ敾鏁堟灉
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				mScrollLayout.setVisibility(View.GONE);
				pointLLayout.setVisibility(View.GONE);
				animLayout.setVisibility(View.VISIBLE);
				mainRLayout.setBackgroundResource(R.drawable.whatsnew_bg);
				Animation leftOutAnimation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.translate_left);
				Animation rightOutAnimation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.translate_right);
				leftLayout.setAnimation(leftOutAnimation);
				rightLayout.setAnimation(rightOutAnimation);
				leftOutAnimation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						mainRLayout.setBackgroundColor(Color.BLACK);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						leftLayout.setVisibility(View.GONE);
						rightLayout.setVisibility(View.GONE);
						// Intent intent = new Intent(
						// ScrollLayoutActivity.this,
						// OtherActivity.class);
						
//						Intent intent = new Intent(SettingScrollLayoutActivity.this,
//								LogIn.class);
//						SettingScrollLayoutActivity.this.startActivity(intent);
						SettingScrollLayoutActivity.this.finish();

					}
				});
				break;
			}
		}
	};

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
}