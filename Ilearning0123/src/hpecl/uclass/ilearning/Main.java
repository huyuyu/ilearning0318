/*
 * Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
 * 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
 * filename：Main.java
 * Author：郭靖东
 * Version：1.1
 * Date：2015/07/31
 * Description：This java file is the main interface.
 * Modify history：
 * 郭靖东,create file,2015/07/31 
 * 郭威，modify file，2015/10/26;add notes module
 */
package hpecl.uclass.ilearning;

import hpecl.uclass.Bean.Article;
import hpecl.uclass.Bean.Forum;
import hpecl.uclass.Bean.Key;
import hpecl.uclass.Bean.QuestionAnswer;
import hpecl.uclass.adapter.CatalogListAdapter;
import hpecl.uclass.adapter.ConcernReadingListAdapter;
import hpecl.uclass.adapter.ForumListAdapter;
import hpecl.uclass.adapter.NoteListAdapter;
import hpecl.uclass.adapter.QuestionListAdapter;
import hpecl.uclass.analysis.JsoupAnalysisChapter;
import hpecl.uclass.analysis.JsoupAnalysisForum;
import hpecl.uclass.analysis.JsoupAnalysisQuestion;
import hpecl.uclass.download.MyTabCursor;
import hpecl.uclass.form.FormSubmitChapter;
import hpecl.uclass.noteDAO.Tb_note;
import hpecl.uclass.noteDAO.notesDAO;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.CustomListview.OnRefreshListener;
import hpecl.uclass.uti.GestureHelper;
import hpecl.uclass.uti.GestureHelper.OnFlingListener;
import hpecl.uclass.uti.GetHtmlData;
import hpecl.uclass.uti.GetImageFromUrl;
import hpecl.uclass.uti.ManaMySeekBar;
import hpecl.uclass.uti.SlidingLayout;
import hpecl.uclass.uti.ViewPagerIndicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilearning.R;

public class Main extends Activity {

	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private View lay1;
	private View lay2;
	private View lay3;
	private View lay4;
	private View lay5; // gw 28/10/15

	private View emptyListTextView;

	private CustomListview l1;
	private CustomListview l2;
	private CustomListview l3;
	private CustomListview l4;
	private CustomListview l5; // gw 28/10/15

	ClipboardManager clipBoard;
	int file_count;
	Key imageKey;
	ImageView keyImage;
	EditText eKey, eText;
	Button submit;
	private ManaMySeekBar mSeekBar;

	CatalogListAdapter catalogAdapter;
	ConcernReadingListAdapter nAdapter;
	ForumListAdapter commentAdapter;
	QuestionListAdapter qAdapter;
	NoteListAdapter tbNoteArrayAdapter;

	int position = 0;
	SharedPreferences.Editor editor;
	SharedPreferences sp;
	int fileNum = 0;//虚拟文件夹中文件数量
	private List<Tb_note> tbNotes = new ArrayList<Tb_note>();
	private List<String> noteName = new ArrayList<String>();
	private List<String> noteId = new ArrayList<String>();

	private ViewPagerIndicator mIndicator;
	private List<String> mDatas = Arrays.asList("相关阅读", "讨论专区", "答疑解惑", "我的笔记","目录");
	static ArrayList<String> catalogList = new ArrayList<String>();
	static ArrayList<Article> refenceArr = new ArrayList<Article>();
	ArrayList<QuestionAnswer> questionArr = new ArrayList<QuestionAnswer>();
	ArrayList<Forum> forumArr;
	/**
	 * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
	 */
	private SlidingLayout slidingLayout;

	/**
	 * menu按钮，点击按钮展示左侧布局，再点击一次隐藏左侧布局。
	 */
	private Button more, menuButton, uploadArticle, notificationView, refenceView, editNote,share;

	ImageView image;
	TextView tv, title;
	String coursePath = "", courseName = "",referenceLink="";

	/**
	 * 放在content布局中的ListView。
	 */
	// private ListView contentListView;

	LayoutInflater infla;
	View footView;
	AssetManager mAssetManager;
	Intent intentData;
	String chapterFileFolder;
	String username, password, cookie;
	String charset = "GB2312";
	String zipPassword = "hpecl701";

	String[] url = Constant.DISSCUSS.split("\\;"); // 讨论专区

	String[] urlChapter = Constant.CONSERNREADING.split(";");// 相关阅读

	String chapterURL = "", consernReadingURL = "";
	private ProgressDialog progressDialog = null;
	private GestureHelper gh;
	Matrix matrix = new Matrix();
	private int couting, chapter;
	SharedPreferences sharedPreferences;
	String id;
	BitmapFactory.Options options = new BitmapFactory.Options();

	// 存取数据
	public static final String SETTING_INFOS = "SETTINGInfos";
	public static final String COUNTING = "COUNTING";
	String[] catalogARRAY;
	Handler handler;
	Boolean globalFlag = true;

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		globalFlag = false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		catalogARRAY = Constant.CATALOGLIST.split(";");
		intentData = getIntent();
		username = intentData.getStringExtra("username");
		password = intentData.getStringExtra("password");
		cookie = intentData.getStringExtra("cookie");
		coursePath = intentData.getStringExtra("course");
		courseName = intentData.getStringExtra("courseName");

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					// 加载图片
					String file_begin = new MyTabCursor(
							DownLoadActivity.helper.getReadableDatabase())
							.filterByCountingAndChapter("Slide" + couting
									+ ".JPG", chapterFileFolder, coursePath);
					Log.i("guo  ", "0109  " + file_begin);

					FileInputStream fis = null;
					BufferedInputStream bs = null;
					try {
						fis = new FileInputStream("/mnt/sdcard/uclass/course/"
								+ coursePath + "/ch" + chapterFileFolder
								+ ".vmdk");
						if(file_begin!=null)
						fis.skip(Long.parseLong(file_begin));

						bs = new BufferedInputStream(fis);
						Bitmap btp = BitmapFactory.decodeStream(bs);
						image.setImageBitmap(btp);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				case 99:
					catalogAdapter.notifyDataSetChanged();
					break;
				}

			}

		};

		infla = LayoutInflater.from(this);
		init();
		InitViewPager();// 初始化viewpager
		footView = infla.inflate(R.layout.underlistview, null);
		l2.addFooterView(footView, null, true);// 不能放到postexcuse，会重复添加footer
		
		
		tv.setText(username);
		if(courseName.length()>8){
			title.setTextSize(20);
		}
		title.setText(courseName);
		
		
		options.inSampleSize = 1;
		options.inPurgeable = true;
		options.inTempStorage = new byte[100 * 1024];
		progressDialog = ProgressDialog.show(Main.this, "请稍等...", "获取数据中...",true);

		// 这句话不能放在全局里
		sharedPreferences = getSharedPreferences(coursePath+"preferences", MODE_PRIVATE);
		sp = getSharedPreferences("position", MODE_PRIVATE);
		editor = sp.edit();
		// 读取数据
		couting = sharedPreferences.getInt(COUNTING, -1);
		chapter = sharedPreferences.getInt("CHAPTER", 0);
		if (chapter >= Integer.parseInt(Constant.CHAPTER)) {
			chapter = 0;
		}
		// 获取asset资源文件
		// mAssetManager = getAssets();
		// 拼凑目录
		combine();
		Log.i("guo  ", "0110   file count   " + file_count);
		chapterURL = url[chapter];
		consernReadingURL = urlChapter[chapter];
		id = chapterURL.split("=")[2];
		if (couting == -1) {
			couting = 1;
		}

		// 当assets文件数量少于couting时
		if (couting > new MyTabCursor(
				DownLoadActivity.helper.getReadableDatabase())
				.getFileCounting(chapterFileFolder,coursePath)) {
			couting = 1;
		}
		Log.i("guo", "0818 " + couting);
		new MyAsyncTask().execute();// 执行异步线程 显示图片、评论、参考资料

		gh = new GestureHelper(Main.this);

		l4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 存取couting值
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("CHAPTER", arg2 - 1);
				editor.commit();
				chapter = arg2 - 1;
				combine();

				couting = 1;
				mSeekBar.setProgress(couting);
				chapterURL = url[chapter];
				consernReadingURL = urlChapter[chapter];
				id = chapterURL.split("=")[2];// 回复提交form表单中的id
				progressDialog = ProgressDialog.show(Main.this, "请稍等...",	"获取数据中...", true);
				new MyAsyncTask().execute();
			}
		});

		// 参考资料下拉刷新监听
		l1.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				new AsyncTask<Void, Void, Void>() {

					@SuppressWarnings("unchecked")
					@Override
					protected Void doInBackground(Void... params) {
						JsoupAnalysisChapter jan = new JsoupAnalysisChapter();
						refenceArr = (ArrayList<Article>) jan
								.analysisInfo(GetHtmlData.doInBackground(
										consernReadingURL,  "gbk", cookie));
						return null;
					}

					protected void onPostExecute(Void result) {
						nAdapter.notifyDataSetChanged();
						l1.onRefreshComplete();
					}

					;
				}.execute();
			}
		});
		// 相关阅读点击监听
		l1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String href = refenceArr.get(position - 1).getHref();
				Log.i("cq", "1109href" + href);
				Intent intent = new Intent();
				intent.setClass(Main.this, RefenceText.class);
				intent.putExtra("Href", href);
				intent.putExtra("cookie", cookie);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("title", refenceArr.get(position - 1).getTitle());

				startActivity(intent);
			}
		});

		// 图片滑动监听
		gh.setOnFlingListener(new OnFlingListener() {
			@Override
			public void OnFlingLeft() {
				// 向左滑动
				++couting;

				if (couting > file_count) {
					Toast.makeText(Main.this, "这是最后一张", Toast.LENGTH_SHORT)
							.show();
					couting = file_count;
					String file_begin = new MyTabCursor(DownLoadActivity.helper
							.getReadableDatabase()).filterByCountingAndChapter(
							"Slide" + couting + ".JPG", chapterFileFolder,
							coursePath);
					Log.i("guo  ", "0109 file_begin " + file_begin);
					FileInputStream fis = null;
					BufferedInputStream bs = null;
					try {
						fis = new FileInputStream("/mnt/sdcard/uclass/course/"
								+ coursePath + "/ch" + chapterFileFolder
								+ ".vmdk");
						if (file_begin != null)
							fis.skip(Long.parseLong(file_begin));

						bs = new BufferedInputStream(fis);
						Bitmap btp = BitmapFactory.decodeStream(bs);
						image.setImageBitmap(btp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					String file_begin = new MyTabCursor(DownLoadActivity.helper
							.getReadableDatabase()).filterByCountingAndChapter(
							"Slide" + couting + ".JPG", chapterFileFolder,
							coursePath);
					Log.i("guo  ", "0109 file_begin  " + file_begin);
					FileInputStream fis = null;
					BufferedInputStream bs = null;
					try {
						fis = new FileInputStream("/mnt/sdcard/uclass/course/"
								+ coursePath + "/ch" + chapterFileFolder
								+ ".vmdk");
						if (file_begin != null)
							fis.skip(Long.parseLong(file_begin));

						bs = new BufferedInputStream(fis);
						Bitmap btp = BitmapFactory.decodeStream(bs);
						image.setImageBitmap(btp);
					} catch (IOException e) {
						e.printStackTrace();
					}
					mSeekBar.setProgress(couting);
				}

				// 存取couting值
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("COUNTING", couting);
				editor.commit();

			}

			@Override
			public void OnFlingRight() {
				// 向右滑动
				if (couting > 1) {
					--couting;
					// index = imagePathList.indexOf(chapterFileFolder +
					// "/Slide"
					// + couting + ".JPG");
				}

				if (couting == 1) {
					Toast.makeText(Main.this, "这是第一张", Toast.LENGTH_SHORT)
							.show();
					// index = imagePathList.indexOf(chapterFileFolder +
					// "/Slide"
					// + couting + ".JPG");
					String file_begin = new MyTabCursor(DownLoadActivity.helper
							.getReadableDatabase()).filterByCountingAndChapter(
							"Slide" + couting + ".JPG", chapterFileFolder,
							coursePath);
					Log.i("guo  ", "0109  " + file_begin);
					FileInputStream fis = null;
					BufferedInputStream bs = null;
					try {
						fis = new FileInputStream("/mnt/sdcard/uclass/course/"
								+ coursePath + "/ch" + chapterFileFolder
								+ ".vmdk");
						if (file_begin != null)
							fis.skip(Long.parseLong(file_begin));

						bs = new BufferedInputStream(fis);
						Bitmap btp = BitmapFactory.decodeStream(bs);
						image.setImageBitmap(btp);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {

					String file_begin = new MyTabCursor(DownLoadActivity.helper
							.getReadableDatabase()).filterByCountingAndChapter(
							"Slide" + couting + ".JPG", chapterFileFolder,
							coursePath);
					Log.i("guo  ", "0109  " + file_begin);
					FileInputStream fis = null;
					BufferedInputStream bs = null;
					try {
						fis = new FileInputStream("/mnt/sdcard/uclass/course/"
								+ coursePath + "/ch" + chapterFileFolder
								+ ".vmdk");
						if (file_begin != null)
							fis.skip(Long.parseLong(file_begin));

						bs = new BufferedInputStream(fis);
						Bitmap btp = BitmapFactory.decodeStream(bs);
						image.setImageBitmap(btp);
					} catch (IOException e) {
						e.printStackTrace();
					}

					mSeekBar.setProgress(couting);
					Log.i("cq", "1109" + couting);

				}
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("COUNTING", couting);
				editor.commit();
			}

			@Override
			public void OnImage() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Main.this, ImageZoom.class);
				// intent.putStringArrayListExtra("imagePathList",
				// imagePathList);
				intent.putExtra("couting", couting);
				intent.putExtra("chapterFileFolder", chapterFileFolder);
				intent.putExtra("course", coursePath);
				startActivity(intent);
			}

		});

		// 将监听滑动事件绑定在contentListView上
		// slidingLayout.setScrollEvent(mPager);

		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 实现点击一下menu展示左侧布局，再点击一下隐藏左侧布局的功能
				if (slidingLayout.isLeftLayoutVisible()) {
					slidingLayout.scrollToRightLayout();
				} else {
					slidingLayout.scrollToLeftLayout();
				}
			}
		});

		// 交流共享button
		uploadArticle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Main.this,
						NotificationActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("cookie", cookie);
				intent.putExtra("url", Constant.CONMUNICATE);
				startActivity(intent);
			}
		});

		// 课程通知button
		notificationView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Main.this, NotificationActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("cookie", cookie);
				intent.putExtra("url", Constant.A701NOTIFICATION);
				startActivity(intent);
			}

		});

		//课程参考资料

		refenceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated methodstub
				Intent intent = new Intent(Main.this, ReferenceActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("cookie", cookie);
				intent.putExtra("url", Constant.REFERENCE);
				startActivity(intent);
			}

		});

		/*
		 * //天翼云存储 cloadSave.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent intent = new Intent(Main.this,PersonCload.class);
		 * startActivity(intent); } });
		 */

		/*
		 * //zip解压 zip.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Log.i("guo ", "0810  "+ "点击解压  "+rootPath);
		 * 
		 * try { File zipFiles = new File(rootPath+"/D701下载/"); File[] files =
		 * zipFiles.listFiles(); if(files != null){ for(File f : files){
		 * Log.i("guo ", "0812  "+ f.toString()); extract(f); }
		 * 
		 * }
		 * 
		 * 
		 * 
		 * } catch (ZipException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * 
		 * } });
		 */
		// 添加笔记按钮
		editNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// dialog list
				final String items[] = { "添加笔记", "交流共享", "我要提问", "我要加贴" };
				// dialog参数设置
				AlertDialog.Builder builder = new AlertDialog.Builder(Main.this); // 先得到构造器
				builder.setTitle("请选择"); // 设置标题
				// builder.setMessage("是否确认退出?"); //设置内容
				// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
				// 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// Toast.makeText(Main.this, items[which],
						// Toast.LENGTH_SHORT).show();
						switch (which) {
						case 0:
							String noteid = "0";
							int flag = 0;
							Intent myintent = new Intent(Main.this,
									EditNote.class);
							Bundle bundle = new Bundle();
							bundle.putInt("flag", flag);
							bundle.putString("noteid", noteid);
							bundle.putString("name", username);
							bundle.putString("chapter",
									catalogList.get(chapter));
							myintent.putExtras(bundle);
							startActivityForResult(myintent, 1);
							break;
						case 1:// 交流共享
							Intent intent = new Intent(Main.this, Sharing.class);
							intent.putExtra("cookie", cookie);
							startActivity(intent);
							break;
						case 2:// 我要提问
							Intent intent2 = new Intent(Main.this,
									AskQuestion.class);
							intent2.putExtra("cookie", cookie);
							startActivity(intent2);
							break;
						case 3:// 我要加贴
							Intent intent3 = new Intent(Main.this,
									AddTopic.class);
							intent3.putExtra("cookie", cookie);
							startActivity(intent3);
							break;
						}
					}
				});
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// Toast.makeText(Main.this, "确定",
								// Toast.LENGTH_SHORT).show();
							}
						});
				builder.create().show();

			}
		});

	}

	private void combine() {
		// TODO Auto-generated method stub

		if (chapter < 10) {
			chapterFileFolder = "0" + chapter;
		} else {
			chapterFileFolder = "" + chapter;
		}
		file_count = new MyTabCursor(
				DownLoadActivity.helper.getReadableDatabase())
				.getFileCounting(chapterFileFolder,coursePath );
		Log.i("guo  ", "0109 file_count" + file_count);
		Log.i("guo  ", "0109	" + chapterFileFolder);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gh.onTouchEvent(event);
	}

	class MyAsyncTask extends AsyncTask<String, String, String> {

		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			JsoupAnalysisForum jaf = new JsoupAnalysisForum();
			forumArr = (ArrayList<Forum>) jaf.analysisInfo(GetHtmlData.doInBackground(chapterURL, "gb2312", cookie));
			imageKey = GetImageFromUrl.getImageBitmap(Constant.IMAGEKEY);
			editor.putInt("position", -1);
			editor.commit();
			JsoupAnalysisChapter jan = new JsoupAnalysisChapter();
			refenceArr = (ArrayList<Article>) jan.analysisInfo(GetHtmlData.doInBackground(consernReadingURL, "gbk", cookie));
			JsoupAnalysisQuestion jaq = new JsoupAnalysisQuestion();
			questionArr = (ArrayList<QuestionAnswer>) jaq.analysisInfo(GetHtmlData.doInBackground(Constant.QUESTIONFORANSWER, "gbk", cookie));

			if (tbNotes.isEmpty()) {
				notesDAO notesinfo = new notesDAO(Main.this);
				tbNotes = notesinfo.getUserData(username);

				if (!tbNotes.isEmpty()) {

					// listLoadingProgressBar.setVisibility(View.GONE);

					for (Tb_note tbNote : tbNotes) {
						noteId.add(String.valueOf(tbNote.getId()));
						noteName.add(tbNote.getName());

					}
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// 图片加载
			String file_begin = new MyTabCursor(
					DownLoadActivity.helper.getReadableDatabase())
					.filterByCountingAndChapter("Slide" + couting + ".JPG",
							chapterFileFolder, coursePath);

			Log.i("guo  ", "0109  " + file_begin);
			Log.i("guo  ", "0109  " + "Slide" + couting + ".JPG");
			Log.i("guo  ", "0109  " + coursePath);

			FileInputStream fis = null;
			BufferedInputStream bs = null;
			try {
				fis = new FileInputStream("/mnt/sdcard/uclass/course/"
						+ coursePath + "/ch" + chapterFileFolder + ".vmdk");
				if(file_begin!=null)
				fis.skip(Long.parseLong(file_begin));

				bs = new BufferedInputStream(fis);
				Bitmap btp = BitmapFactory.decodeStream(bs);
				image.setImageBitmap(btp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("guo  ", "0110   file count   " + file_count);

			mSeekBar.setMax(file_count);// 设置bar的最大值，应为图片数量
			mSeekBar.setProgress(couting);
			// 设置监听
			mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					// mSeekBar.setSeekBarText("mana"+"\t\n"+progress);
					mSeekBar.setSeekBarText("\n" + progress  + "/"
							+ file_count);
					if (progress == 0) {
						couting = 1;
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putInt("COUNTING", couting);
						editor.commit();

					} else {
						couting = progress;
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putInt("COUNTING", couting);
						editor.commit();

					}
					Log.i("guo", "1207    " + progress);

				}
			});
			share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// dialog list
					final String items[] = { "应用分享", "面对面分享" };
					// dialog参数设置
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Main.this); // 先得到构造器
					builder.setTitle("请选择"); // 设置标题
					// builder.setMessage("是否确认退出?"); //设置内容
					// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
					// 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									// Toast.makeText(Main.this, items[which],
									// Toast.LENGTH_SHORT).show();
									switch (which) {
									case 0:
										String strDlgTitle = "对话框标题 - 分享文字";
										String strSubject = "我的主题";
										String strContent = "我的分享内容";
										String resultString = "";
										clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
										if (clipBoard.hasPrimaryClip()) {
											ClipData clipData = clipBoard
													.getPrimaryClip();
											int count = clipData.getItemCount();

											for (int i = 0; i < count; ++i) {

												ClipData.Item item = clipData
														.getItemAt(i);
												CharSequence str = item
														.coerceToText(Main.this);
												resultString += str;
											}
										}
										if (!resultString.equals("")) {
											strContent = resultString;
										}
										shareText(strDlgTitle, strSubject,
												strContent);
										break;
									case 1:// 面对面分享
											// Toast.makeText(Main.this,
											// "开发中，请耐心等待……",
											// Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(Main.this,
												WifiHotActivity.class);
										intent.putExtra("ssid", username);
										startActivity(intent);
										break;
									}
								}

								/**
								 * 分享文字内容
								 * 
								 * @param dlgTitle
								 *            分享对话框标题
								 * @param subject
								 *            主题
								 * @param content
								 *            分享内容（文字）
								 */
								private void shareText(String dlgTitle,
										String subject, String content) {
									if (content == null || "".equals(content)) {
										return;
									}
									Intent intent = new Intent(
											Intent.ACTION_SEND);
									intent.setType("text/plain");
									if (subject != null && !"".equals(subject)) {
										intent.putExtra(Intent.EXTRA_SUBJECT,
												subject);
									}

									intent.putExtra(Intent.EXTRA_TEXT, content);

									// 设置弹出框标题
									if (dlgTitle != null
											&& !"".equals(dlgTitle)) { // 自定义标题
										startActivity(Intent.createChooser(
												intent, dlgTitle));
									} else { // 系统默认标题
										startActivity(intent);
									}
								}
							});
					builder.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			});

			catalogAdapter = new CatalogListAdapter(Main.this, catalogList);
			l4.setAdapter(catalogAdapter);
			nAdapter = new ConcernReadingListAdapter(Main.this, refenceArr);
			l1.setAdapter(nAdapter);

			tbNoteArrayAdapter = new NoteListAdapter(Main.this, noteName);
			l5.setAdapter(tbNoteArrayAdapter);
			l5.setEmptyView(emptyListTextView);

			l5.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, final int position, long id) {
					// notesListView.setClickable(false);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Main.this);
					builder.setMessage("删除该条笔记？");
					builder.setTitle("提示");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String noteid = noteId.get(position - 1);

									Log.d("note", "noteid=" + noteid);

									Log.d("note",
											"notename="
													+ noteName
															.get(position - 1));
									notesDAO notesDAO = new notesDAO(Main.this);
									Log.d("note",
											"noteid2="
													+ notesDAO
															.find(Integer
																	.parseInt(noteid))
															.getId());
									notesDAO.delete(Integer.parseInt(noteid));
									// Log.d("note", "noteid3=" +
									// notesDAO.find(Integer.parseInt(noteid)).getId());
									tbNotes.remove(position - 1);
									noteId.remove(position - 1);
									noteName.remove(position - 1);
									tbNoteArrayAdapter.notifyDataSetChanged();
									// refreshNotesList();

								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					builder.create().show();
					return true;
				}
			});
			l5.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// callback.onNoteDetailsOpen(adapter.getItem(position));
					String noteid = noteId.get(position - 1);
					int flag = 1;
					Intent myintent = new Intent(Main.this, EditNote.class);

					Bundle bundle = new Bundle();
					bundle.putInt("flag", flag);
					bundle.putString("noteid", noteid);
					bundle.putString("name", username);
					myintent.putExtras(bundle);
					startActivityForResult(myintent, 1);

				}
			});
			// 目录刷新
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					File vmPath = new File(Constant.rootPath
							+ "/uclass/course/" + coursePath + "/");
					while (globalFlag) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						int localFileNum = vmPath.listFiles().length;
						if(fileNum == localFileNum){
							
						}else{
							fileNum = vmPath.listFiles().length;
							catalogList.clear();
							for (int i = 0; i < fileNum; i++) {
								catalogList.add(catalogARRAY[i]);							
							}
							Message message = new Message();
							message.what = 99;
							handler.sendMessage(message);
						}
					}

				}
			}).start();
			// footer 布局

			keyImage = (ImageView) footView
					.findViewById(R.id.identifyingcodepicture);
			eKey = (EditText) footView.findViewById(R.id.eidentifyingcode);
			eText = (EditText) footView.findViewById(R.id.etext);
			submit = (Button) footView.findViewById(R.id.submit);
			keyImage.setImageBitmap(imageKey.getBitmap());
			commentAdapter = new ForumListAdapter(Main.this, forumArr, eText,
					l2, editor);
			l2.setAdapter(commentAdapter);
			l2.setonRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void>() {

						@SuppressWarnings("unchecked")
						@Override
						protected Void doInBackground(Void... params) {
							JsoupAnalysisForum jaf = new JsoupAnalysisForum();
							forumArr = (ArrayList<Forum>) jaf
									.analysisInfo(GetHtmlData.doInBackground(
											chapterURL, "gb2312", cookie));
							return null;
						}

						protected void onPostExecute(Void result) {
							commentAdapter.notifyDataSetChanged();
							l2.onRefreshComplete();
						};
					}.execute();
				}
			});

			qAdapter = new QuestionListAdapter(Main.this, questionArr);

			l3.setAdapter(qAdapter);
			l3.setonRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void>() {

						@SuppressWarnings("unchecked")
						@Override
						protected Void doInBackground(Void... params) {
							JsoupAnalysisQuestion jaf = new JsoupAnalysisQuestion();
							questionArr = (ArrayList<QuestionAnswer>) jaf
									.analysisInfo(GetHtmlData.doInBackground(
											Constant.QUESTIONFORANSWER, "gbk",
											cookie));
							return null;
						}

						protected void onPostExecute(Void result) {
							qAdapter.notifyDataSetChanged();
							l3.onRefreshComplete();
						};
					}.execute();
				}
			});

			l3.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.i("guo", "0818  " + arg2);
					QuestionAnswer question = questionArr.get(arg2 - 1);
					String url = "http://www.grandsw.com:9191/"
							+ question.getUrl();
					String id = url.split("=")[2];
					Intent intent = new Intent(Main.this, ForumActivity.class);
					intent.putExtra("activity", 2);
					intent.putExtra("url", url);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					intent.putExtra("cookie", cookie);
					intent.putExtra("id", id);
					intent.putExtra("classid", 104);
					intent.putExtra("sTitle", question.getQuestionContent());

					startActivity(intent);
				}
			});

			submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					final String localKey = eKey.getText().toString();
					final String localText = eText.getText().toString();

					if (localKey.equals("")) {
						Toast.makeText(Main.this, "请输入验证码", Toast.LENGTH_SHORT)
								.show();
					}

					else if (localText.equals("")) {
						Toast.makeText(Main.this, "请输入评论内容", Toast.LENGTH_SHORT)
								.show();
					} else {

						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								position = sp.getInt("position", -1);
								Log.i("guo", "0720  " + position);

								if (position != -1) {
									FormSubmitChapter.post(Constant.FORMURL,
											username, password, localKey,
											localText, forumArr.get(position)
													.getReply(), id);
									new MyAsyncTask().execute();
								}

								else {
									FormSubmitChapter.post(Constant.FORMURL,
											username, password, localKey,
											localText, "0", id);
									new MyAsyncTask().execute();
								}
							}

						}).start();
					}
					eKey.setText("");
					eText.setText("");

				}
			});
			progressDialog.dismiss();

			more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Main.this, MoreActivity.class);

					intent.putExtra("Href", Constant.FEEDBACK);
					intent.putExtra("cookie", cookie);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					intent.putExtra("title", "用户反馈");
					startActivity(intent);
				}
			});

		}

	}

	private void refreshNotesList() {

		noteId.clear();
		noteName.clear();
		tbNotes.clear();
		notesDAO notesinfo = new notesDAO(this);
		tbNotes = notesinfo.getUserData(username);
		if (tbNotes.isEmpty()) {
		} else {
			for (Tb_note tbNote : tbNotes) {
				noteId.add(String.valueOf(tbNote.getId()));
				noteName.add(tbNote.getName());
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，回传的是RESULT_OK
		case RESULT_OK:
			refreshNotesList();
			tbNoteArrayAdapter.notifyDataSetChanged();
			mPager.getAdapter().notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		tv = (TextView) findViewById(R.id.username);
		title = (TextView) findViewById(R.id.title);
		menuButton = (Button) findViewById(R.id.menuButton);
		editNote = (Button) findViewById(R.id.editNote);// by gw
		share = (Button) findViewById(R.id.share);
		// contentListView = (ListView) findViewById(R.id.contentList);
		// courseForum = (Button) findViewById(R.id.topic);
		uploadArticle = (Button) findViewById(R.id.uploadarticle);
		more = (Button) findViewById(R.id.more);
		// uploadfile = (Button) findViewById(R.id.uploadfile);
		// cloadSave = (Button) findViewById(R.id.cloud);
		// zip = (Button) findViewById(R.id.zip);
		// downLoad = (Button) findViewById(R.id.download);
		image = (ImageView) findViewById(R.id.image);
		notificationView = (Button) findViewById(R.id.notification);
		mSeekBar = (ManaMySeekBar) findViewById(R.id.seekBar);
		refenceView = (Button) findViewById(R.id.reference);
		// questionForAnswer = (Button) findViewById(R.id.questionforanswer);

	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
		// 设置Tab上的标题
		mIndicator.setTabItemTitles(mDatas);
		listViews = new ArrayList<View>(); // private List<View> listViews;
											// Tab页面列表

		LayoutInflater mInflater = getLayoutInflater();

		lay1 = mInflater.inflate(R.layout.lay1, null);
		lay2 = mInflater.inflate(R.layout.lay2, null);
		lay3 = mInflater.inflate(R.layout.lay3, null);
		lay4 = mInflater.inflate(R.layout.lay4, null);
		lay5 = mInflater.inflate(R.layout.lay5, null); // by gw

		l1 = (CustomListview) lay1.findViewById(R.id.listview1);

		l2 = (CustomListview) lay2.findViewById(R.id.listview2);

		l3 = (CustomListview) lay3.findViewById(R.id.listview3);

		l4 = (CustomListview) lay4.findViewById(R.id.listview4);

		l5 = (CustomListview) lay5.findViewById(R.id.listview5); // by gw

		emptyListTextView = lay5.findViewById(R.id.empty);
		emptyListTextView.setAlpha(0);
		emptyListTextView.animate().alpha(1).setDuration(2000);

		listViews.add(lay1);
		listViews.add(lay2);
		listViews.add(lay3);
		listViews.add(lay5);// by gw
		listViews.add(lay4);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mIndicator.setViewPager(mPager, 0);
		mPager.setCurrentItem(0);
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

}
