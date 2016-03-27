package hpecl.uclass.ilearning;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.ilearning.R;

public class LightActivity extends Activity{

	private SeekBar seekBar;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light);
	
	seekBar = (SeekBar) findViewById(R.id.seekBar);

	// 进度条绑定最大亮度，255是最大亮度
	seekBar.setMax(255);
	// 取得当前亮度
	int normal = Settings.System.getInt(getContentResolver(),
			Settings.System.SCREEN_BRIGHTNESS, 255);
	// 进度条绑定当前亮度
	seekBar.setProgress(normal);

	seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 取得当前进度
			int tmpInt = seekBar.getProgress();

			// 当进度小于80时，设置成80，防止太黑看不见的后果。
			if (tmpInt < 50) {
				tmpInt = 50;
			}

			// 根据当前进度改变亮度
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, tmpInt);
			tmpInt = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, -1);
			WindowManager.LayoutParams wl = getWindow().getAttributes();

			float tmpFloat = (float) tmpInt / 255;
			if (tmpFloat > 0 && tmpFloat <= 1) {
				wl.screenBrightness = tmpFloat;
			}
			getWindow().setAttributes(wl);
//			stopAutoBrightness(LightActivity.this);
//			setBrightness(LightActivity.this, tmpInt);
			
			

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
		}
	});
	
	
	  
	  
     

	
	}
	/** * 设置亮度 */
	
	 public static void setBrightness(Activity activity, int brightness) {     
		    
	  
	    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();     
	  
	     lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);    
	     Log.d("lxy", "set  lp.screenBrightness == " + lp.screenBrightness);  
	  
	     activity.getWindow().setAttributes(lp);   
	     }



	 /** * 停止自动亮度调节 */  
	  
	    public static void stopAutoBrightness(Activity activity) {     
	  
		     Settings.System.putInt(activity.getContentResolver(),            
		  
		     Settings.System.SCREEN_BRIGHTNESS_MODE,             
		  
		     Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  
	     }




}

