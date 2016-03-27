package hpecl.uclass.ilearning;


import hpecl.uclass.download.MyTabCursor;
import hpecl.uclass.image.ZoomImageView;
import hpecl.uclass.uti.Constant;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.ilearning.R;

public class ImageZoom extends Activity {
	private ZoomImageView zoomImg;
	private ArrayList<String> imagePathList = new ArrayList<String>();
	int index;
	AssetManager mAssetManager;
	Matrix matrix = new Matrix();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagezoom);
		zoomImg = (ZoomImageView) findViewById(R.id.image);
		Intent intent = getIntent();
//		imagePathList = intent.getStringArrayListExtra("imagePathList");
		int couting = intent.getIntExtra("couting", -1);
		String chapterFileFolder = intent.getStringExtra("chapterFileFolder");
		String course = intent.getStringExtra("course");
		
//		Log.i("adsf", "1122          "+imagePathList+couting+chapterFileFolder);
		matrix.postScale(1.2f,1.2f); //长和宽放大缩小的比例
		//获取asset资源文件
		mAssetManager = getAssets();

		if(couting>0){
			
			String file_begin = new MyTabCursor(DownLoadActivity.helper
					.getReadableDatabase()).filterByCountingAndChapter(
					"Slide" + couting + ".JPG", chapterFileFolder, course);
			Log.i("guo  ", "0109  " + file_begin);
			FileInputStream fis = null;
			BufferedInputStream bs = null;
			try {
				fis = new FileInputStream(Constant.rootPath+"/uclass/course/"+course+"/ch"
						+ chapterFileFolder + ".vmdk");
				fis.skip(Long.parseLong(file_begin));

				bs = new BufferedInputStream(fis);
				Bitmap btp = BitmapFactory.decodeStream(bs);
				zoomImg.setImage(btp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*zoomImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("adsf", "1122          "+"2222222222222222222222");
				finish();
			}
		});*/
		//zoomImg.onTouchEvent(event);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return zoomImg.onTouchEvent(event);
	}

	
	
}
