package hpecl.uclass.guider;

import hpecl.uclass.ilearning.LogIn;
import hpecl.uclass.uti.Constant;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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


public class ScrollLayoutActivity extends Activity implements
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
		sp = getSharedPreferences("firstUser", MODE_PRIVATE);
		int flag = sp.getInt("flag", 0);
		if(flag == 1){
			Intent intent = new Intent(ScrollLayoutActivity.this,
					LogIn.class);
			ScrollLayoutActivity.this.startActivity(intent);
			ScrollLayoutActivity.this.finish();

		}else{
			setContentView(R.layout.startup);
			initView();
			File file = new File(Constant.rootPath+"/uclass");
			
			if(file.exists()){
				deleteDir(file);
				Log.i("guo ", "000000000000000000000000     ");
			}
		}
		
	}
	
	
	private boolean deleteDir(File dir) {
		// TODO Auto-generated method stub
		 if (dir.isDirectory()) {
	            String[] children = dir.list();
	            //递归删除目录中的子目录下
	            for (int i=0; i<children.length; i++) {
	                boolean success = deleteDir(new File(dir, children[i]));
	                if (!success) {
	                    return false;
	                }
	            }
	        }
	        // 目录此时为空，可以删除
	        return dir.delete();
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
						editor = sp.edit();
						editor.putInt("flag", 1);
						editor.commit();
						Intent intent = new Intent(ScrollLayoutActivity.this,
								LogIn.class);
						ScrollLayoutActivity.this.startActivity(intent);
						ScrollLayoutActivity.this.finish();

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