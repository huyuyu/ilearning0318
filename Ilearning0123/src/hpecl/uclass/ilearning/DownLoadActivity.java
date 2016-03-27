package hpecl.uclass.ilearning;

import hpecl.uclass.download.ConfigDownload;
import hpecl.uclass.download.CourseDownload;
import hpecl.uclass.download.MyDatabaseHelper;
import hpecl.uclass.download.MyTabCursor;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.ReadConfigFile;
import hpecl.uclass.uti.RoundProgressBar;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.ilearning.R;

public class DownLoadActivity extends Activity {

	Button downloadCourse1, downloadCourse2, downloadCourse3,openCourse1, openCourse2,openCourse3;
	RoundProgressBar roundProgressBar1, roundProgressBar2,roundProgressBar3;
	String charset = "GB2312", cookie, username, password;
	String back;
	Handler handle;
	private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static SQLiteOpenHelper helper;
	public static int flag = 0;

	SharedPreferences sp, sp_conf,sp0A701,sp0D701;
	SharedPreferences.Editor spe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		helper = new MyDatabaseHelper(DownLoadActivity.this);

		//从注册页面得到以下信息
		Intent intent = getIntent();
		cookie = intent.getStringExtra("cookie");
		username = intent.getStringExtra("username");
		password = intent.getStringExtra("password");

		sp = getSharedPreferences("cookie", MODE_PRIVATE);  //只可以被此用户使用
		spe = sp.edit();
		spe.putString("cookie", cookie);
		spe.commit();

		downloadCourse1 = (Button) findViewById(R.id.downloadcourse1);
		downloadCourse2 = (Button) findViewById(R.id.downloadcourse2);
		downloadCourse3 = (Button) findViewById(R.id.downloadcourse3);
		openCourse1 = (Button) findViewById(R.id.opencourse1);	
		openCourse2 = (Button) findViewById(R.id.opencourse2);
		openCourse3 = (Button) findViewById(R.id.opencourse3);
		roundProgressBar1 = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
		roundProgressBar2 = (RoundProgressBar) findViewById(R.id.roundProgressBar2);
		roundProgressBar3 = (RoundProgressBar) findViewById(R.id.roundProgressBar3);
		String courseFile = rootPath + "/uclass/course/A701/";
		String courseFile1 = rootPath + "/uclass/course/D701/";
		String courseFile2 = rootPath + "/uclass/course/html/";
		String confFile = rootPath + "/uclass/system_yk/";
		File fileCourse = new File(courseFile);
		File fileCourse1 = new File(courseFile1);
		File fileCourse2 = new File(courseFile2);
		File fileConfig = new File(confFile);
		if (!fileCourse.exists()) {
			fileCourse.mkdirs();
		}
		if (fileCourse.listFiles().length > 0
				&& fileConfig.listFiles().length > 0
				&& new MyTabCursor(DownLoadActivity.helper.getReadableDatabase()).isCourseExist("A701")) {
			roundProgressBar1.setVisibility(View.INVISIBLE);
			downloadCourse1.setVisibility(View.INVISIBLE);
			openCourse1.setVisibility(View.VISIBLE);
		} else {
			roundProgressBar1.setVisibility(View.INVISIBLE);
			openCourse1.setVisibility(View.INVISIBLE);
		}
		if (!fileCourse1.exists()) {
			fileCourse1.mkdirs();
		}
		if (fileCourse1.listFiles().length >0 
				&& fileConfig.listFiles().length > 0
				&& new MyTabCursor(
						DownLoadActivity.helper.getReadableDatabase())
						.isCourseExist("D701")) {
			roundProgressBar2.setVisibility(View.INVISIBLE);
			downloadCourse2.setVisibility(View.INVISIBLE);
			openCourse2.setVisibility(View.VISIBLE);
		} else {
			roundProgressBar2.setVisibility(View.INVISIBLE);
			openCourse2.setVisibility(View.INVISIBLE);
		}
		if (!fileCourse2.exists()) {
			fileCourse2.mkdirs();
		}
		if (fileCourse2.listFiles().length >0 
				&& fileConfig.listFiles().length > 0
				&& new MyTabCursor(
						DownLoadActivity.helper.getReadableDatabase())
						.isCourseExist("html")) {
			roundProgressBar3.setVisibility(View.INVISIBLE);
			downloadCourse3.setVisibility(View.INVISIBLE);
			openCourse3.setVisibility(View.VISIBLE);
		} else {
			roundProgressBar3.setVisibility(View.INVISIBLE);
			openCourse3.setVisibility(View.INVISIBLE);
		}
		// 课程0a701下载
		downloadCourse1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			// TODO Auto-generated method stub

				if (flag == 0) {
					roundProgressBar1.setVisibility(View.VISIBLE);
					downloadCourse1.setVisibility(View.INVISIBLE);
//					roundProgressBar1.setMax(45539700);
					flag++;
					Toast.makeText(DownLoadActivity.this, "开始下载",Toast.LENGTH_SHORT).show();
					new Thread(new Runnable() {
						@Override
						public void run() {
//							Log.i("guo ", "0810   " + "开始下载");
							// 先下载配置文件
							String a701 = "http://www.grandsw.com:9191/class/download/coursenotes/0A701.sys";
							ConfigDownload.courseDownload(a701, charset,"0A701.sys");
//							Log.i("guo  ", "0111    config 下载完成");
							sp_conf = getSharedPreferences("sys_conf",MODE_PRIVATE);
							// 读取配置文件
							String selectFile = sp_conf.getString("selectfile","0A701.sys");
							// 设置配置文件路径
//							Log.i("guo ", "0107  " + selectFile);
							String conf = rootPath + "/uclass/system_yk";
							File conFile = new File(conf);
							if (!conFile.exists()) {
								conFile.mkdir();
							}
							File readFile = new File(conf + "/" + selectFile);
							if (readFile.exists()) {
								// 读取配置文件，赋值
								ReadConfigFile.getConstantParam(readFile);
								sp = getSharedPreferences("0A701Chapter",MODE_PRIVATE);
								spe = sp.edit();
								spe.putString("0A701Chapter", Constant.CHAPTER);
								spe.commit();
								// 启动service 监听推送消息
								Intent serverintent = new Intent(DownLoadActivity.this,hpecl.uclass.service.BackService.class);
								serverintent.putExtra("username", username);
								serverintent.putExtra("password", password);
//								Log.i("guo ", "1230    " + "开启service");
								startService(serverintent);
								String chapterNum = sp.getString("0A701Chapter", "0");
//								Log.i("guo", "0227  " + chapterNum);
								for (int i = 0; i < Integer
										.parseInt(chapterNum); i++) {
									// 开始下载课件
									if (i < 10) {
										back = CourseDownload.courseDownload(
												helper,"http://www.grandsw.com:9191/class/download/coursenotes/0A701/ch0"
														+ i + ".zip", charset,
												"ch0" + i + ".zip",
												roundProgressBar1, "A701");
										Message message = new Message();
										message.what = 1;
										handle.sendMessage(message);
									} else {
										back = CourseDownload.courseDownload(
												helper,
												"http://www.grandsw.com:9191/class/download/coursenotes/0A701/ch"
														+ i + ".zip", charset,
												"ch" + i + ".zip",
												roundProgressBar1, "A701");
									}

								}
								Message message = new Message();
								message.what = 2;
								handle.sendMessage(message);
								flag--;
							}

						}

					}).start();
				} else {
					Toast.makeText(DownLoadActivity.this, "请完成当前下载！",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		// 课程0d701下载
		downloadCourse2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// roundProgressBar2.setVisibility(View.VISIBLE);

				if (flag == 0) {
					roundProgressBar2.setVisibility(View.VISIBLE);
					downloadCourse2.setVisibility(View.INVISIBLE);
//					roundProgressBar2.setMax(26854000);
					flag++;
					Toast.makeText(DownLoadActivity.this, "开始下载",
							Toast.LENGTH_SHORT).show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							Log.i("guo ", "0810   " + "开始下载");
							// 先下载配置文件
							String d701 = "http://www.grandsw.com:9191/class/download/coursenotes/0D701.sys";
							ConfigDownload.courseDownload(d701, charset, "0D701.sys");
//							Log.i("guo  ", "0111    config 下载完成");
							sp_conf = getSharedPreferences("sys_conf",MODE_PRIVATE);
							// 读取配置文件
							String selectFile = sp_conf.getString("selectfile","0D701.sys");
							// 设置配置文件路径
//							Log.i("guo ", "0107  " + selectFile);
							String conf = rootPath + "/uclass/system_yk";
							File conFile = new File(conf);
							if (!conFile.exists()) {
								conFile.mkdir();
							}
							File readFile = new File(conf + "/" + selectFile);
							if (readFile.exists()) {
								// 读取配置文件，赋值
								ReadConfigFile.getConstantParam(readFile);

								sp = getSharedPreferences("0D701Chapter",MODE_PRIVATE);
								spe = sp.edit();
								spe.putString("0D701Chapter", Constant.CHAPTER);
								spe.commit();
								// 启动service 监听推送消息
								Intent serverintent = new Intent(DownLoadActivity.this,hpecl.uclass.service.BackService.class);
								serverintent.putExtra("username", username);
								serverintent.putExtra("password", password);
								Log.i("guo ", "1230    " + "开启service");
								startService(serverintent);
								String chapterNum = sp.getString("0D701Chapter", "0");
								Log.i("guo", "0227  " + chapterNum);
								for (int i = 0; i < Integer
										.parseInt(chapterNum); i++) {
									// 开始下载课件
									if (i < 10) {
										back = CourseDownload.courseDownload(
												helper,
												"http://www.grandsw.com:9191/class/download/coursenotes/0D701/ch0"
														+ i + ".zip", charset,
												"ch0" + i + ".zip",
												roundProgressBar2, "D701");
										Message message = new Message();
										message.what = 4;
										handle.sendMessage(message);
									} else {
										back = CourseDownload.courseDownload(
												helper,
												"http://www.grandsw.com:9191/class/download/coursenotes/0D701/ch"
														+ i + ".zip", charset,
												"ch" + i + ".zip",
												roundProgressBar2, "D701");
									}

								}
								Message message = new Message();
								message.what = 3;
								handle.sendMessage(message);
								flag--;
							}

						}

					}).start();
				} else {
					Toast.makeText(DownLoadActivity.this, "请完成当前下载！",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		//html课件
		downloadCourse3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// roundProgressBar2.setVisibility(View.VISIBLE);

				if (flag == 0) {
					roundProgressBar3.setVisibility(View.VISIBLE);
					downloadCourse3.setVisibility(View.INVISIBLE);
//					roundProgressBar2.setMax(26854000);
					flag++;
					Toast.makeText(DownLoadActivity.this, "开始下载",
							Toast.LENGTH_SHORT).show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							Log.i("guo ", "0810   " + "开始下载");
							// 先下载配置文件
							String html = "http://www.grandsw.com:9191/class/download/coursenotes/html5.sys";
							ConfigDownload.courseDownload(html, charset,
									"html5.sys");
							Log.i("guo  ", "0111    config 下载完成");
							sp_conf = getSharedPreferences("sys_conf",
									MODE_PRIVATE);
							// 读取配置文件
							String selectFile = sp_conf.getString("selectfile",
									"html5.sys");
							// 设置配置文件路径
							Log.i("guo ", "0107  " + selectFile);
							String conf = rootPath + "/uclass/system_yk";
							File conFile = new File(conf);
							if (!conFile.exists()) {
								conFile.mkdir();
							}
							File readFile = new File(conf + "/" + selectFile);
							if (readFile.exists()) {
								// 读取配置文件，赋值
								ReadConfigFile.getConstantParam(readFile);

//								sp = getSharedPreferences("htmlChapter",
//										MODE_PRIVATE);
//								spe = sp.edit();
//								spe.putString("htmlChapter", Constant.CHAPTER);
//								spe.commit();
								// 启动service 监听推送消息
								Intent serverintent = new Intent(
										DownLoadActivity.this,
										hpecl.uclass.service.BackService.class);
								serverintent.putExtra("username", username);
								serverintent.putExtra("password", password);
								Log.i("guo ", "1230    " + "开启service");
								startService(serverintent);
//								String chapterNum = sp.getString(
//										"htmlChapter", "0");
//								Log.i("guo", "0227  " + chapterNum);
								
									// 开始下载课件
								back = CourseDownload.courseDownload(
												helper,
												"http://www.grandsw.com:9191/class/download/coursenotes/0D701/html5lab.zip",
												charset, "html5lab.zip",
												roundProgressBar3, "html");
								Message message = new Message();
								message.what = 5;
								handle.sendMessage(message);
								Message message1 = new Message();
								message1.what = 6;
								handle.sendMessage(message1);
								flag--;
							}

						}

					}).start();
				} else {
					Toast.makeText(DownLoadActivity.this, "请完成当前下载！",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		
		openCourse1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sp_conf = getSharedPreferences("sys_conf", MODE_PRIVATE);
				// 读取配置文件
				String selectFile = sp_conf.getString("selectfile", "0A701.sys");
				// 设置配置文件路径
				Log.i("guo ", "0107  " + selectFile);
				String conf = rootPath + "/uclass/system_yk";
				File conFile = new File(conf);
				if (!conFile.exists()) {
					conFile.mkdir();
				}
				File readFile = new File(conf + "/" + selectFile);

				Constant.REFERENCECOURSE="0A01";
				ReadConfigFile.getConstantParam(readFile); // 读取配置文件

				Intent intent = new Intent(DownLoadActivity.this, Main.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("cookie", cookie);
				intent.putExtra("course", "A701");
				intent.putExtra("courseName", "嵌入式系统概论");

				startActivity(intent);
			}
		});

		openCourse2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sp_conf = getSharedPreferences("sys_conf", MODE_PRIVATE);
				// 读取配置文件
				String selectFile = sp_conf.getString("selectfile", "0D701.sys");
				// 设置配置文件路径
				Log.i("guo ", "0107  " + selectFile);
				String conf = rootPath + "/uclass/system_yk";
				File conFile = new File(conf);
				if (!conFile.exists()) {
					conFile.mkdir();
				}
				File readFile = new File(conf + "/" + selectFile);

				Constant.REFERENCECOURSE="0D01";
				ReadConfigFile.getConstantParam(readFile); // 读取配置文件

				Intent intent = new Intent(DownLoadActivity.this, Main.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("cookie", cookie);
				intent.putExtra("course", "D701");
				intent.putExtra("courseName", "移动通信服务终端");
				intent.putExtra("referenceLink",Constant.REFERENCE);

				startActivity(intent);
			}
		});

		openCourse3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				sp_conf = getSharedPreferences("sys_conf", MODE_PRIVATE);
				// 读取配置文件
				String selectFile = sp_conf.getString("selectfile", "html5.sys");
				// 设置配置文件路径
				Log.i("guo ", "0107  " + selectFile);
				String conf = rootPath + "/uclass/system_yk";
				File conFile = new File(conf);
				if (!conFile.exists()) {
					conFile.mkdir();
				}
				File readFile = new File(conf + "/" + selectFile);

				Constant.REFERENCECOURSE="0D01";
				ReadConfigFile.getConstantParam(readFile); // 读取配置文件

				Intent intent = new Intent(DownLoadActivity.this, Main.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("cookie", cookie);
				intent.putExtra("course", "html");//课程名称
				intent.putExtra("courseName", "HTML5技术介绍与实验");//主页面课程名称显示
				intent.putExtra("referenceLink",Constant.REFERENCE);

				startActivity(intent);
			}
		});
		
		handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					openCourse1.setVisibility(View.VISIBLE);
					break;
				case 2:
					Toast.makeText(DownLoadActivity.this, "下载完成！",Toast.LENGTH_SHORT).show();
					roundProgressBar1.setVisibility(View.INVISIBLE);
					break;
				case 3:
					Toast.makeText(DownLoadActivity.this, "下载完成！",Toast.LENGTH_SHORT).show();
					roundProgressBar2.setVisibility(View.INVISIBLE);
					break;
				case 4:
					openCourse2.setVisibility(View.VISIBLE);
					break;
				case 5:
					openCourse3.setVisibility(View.VISIBLE);
					break;
				case 6:
					Toast.makeText(DownLoadActivity.this, "下载完成！",Toast.LENGTH_SHORT).show();
					roundProgressBar3.setVisibility(View.INVISIBLE);
					break;
				default:
					break;
				}
			}

		};
	}
}
