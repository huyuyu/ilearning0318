/*
 * Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
 * 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
 * filename：CourseDownload.java
 * Author：郭靖东
 * Version：1.1
 * Date：2015/08/22
 * Description：This java file is used for downloading course.
 * Modify history：
 * 郭靖东,create file,2015/08/22
 */
package hpecl.uclass.download;

import hpecl.uclass.uti.RoundProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class CourseDownload {
	static String zipPassword = "hpecl701";

	static String getinfo;
	String imageUrl;
	static File sdDir = null;

	static File downLoad;
	static String fName;

	private static MyTabOperate operate;
	private static Context context;
	static RoundProgressBar roundProgressBar;

	private static SQLiteOpenHelper help;
	public static int fileLength;
	public static String filename;
	public static String courseDir;
	static int size = 0;

	public static String courseDownload(SQLiteOpenHelper helper, String url,
			String encode, String fileName, RoundProgressBar roundProgressBar1,
			String fileDir) {
		// TODO Auto-generated method stub
		sdDir = Environment.getExternalStorageDirectory();// 获取根目录
		downLoad = new File(sdDir, fileDir);
		fName = fileName;
		help = helper;
		courseDir = fileDir;
		roundProgressBar = roundProgressBar1;
		if (!downLoad.exists()) {
			if (downLoad.mkdir())
				Log.i("guo ", "makedir   " + "成功创建下载文件夹");
		}
		size = 0;
		getinfo = getHtmlContent(url, encode);
		Log.i("guo ", "0810   " + getinfo);

		return getinfo;
	}

	public static String getHtmlContent(URL url, String encode)
			throws ZipException {

		int responseCode = -1;
		HttpURLConnection con = null;

		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);
			// con.setRequestProperty("Cookie", cookie);
			// String fileName = con.getHeaderField(6);

			con.connect();
			// 获得网页返回信息码
			responseCode = con.getResponseCode();
			fileLength = con.getContentLength();
			filename = con.getHeaderField("Content-Disposition");
			roundProgressBar.setMax(fileLength);
			Log.i("guo", "0422    " + fileLength + "      name   " + filename);
			if (responseCode == -1) {
				System.out.println(url.toString()
						+ " : connection is failure...");
				con.disconnect();
				return null;
			}
			if (responseCode >= 400) // 请求失败
			{
				System.out.println("请求失败:get response code: " + responseCode);
				con.disconnect();
				return null;
			}
			// 目标文件
			FileOutputStream fos = new FileOutputStream(downLoad.toString()
					+ "/" + fName);
			// 字节流转换成字符流
			// OutputStreamWriter fOutstreamReader = new
			// OutputStreamWriter(fos,encode);
			// BufferedWriter fBuffStr = new BufferedWriter(fOutstreamReader);

			// String file = con.getContent().toString();
			// Log.i("download ", "1227    下载         "+file);
			InputStream inStr = con.getInputStream();

			Log.i("guo", "0422" + url);

			int back = 0;
			byte[] buffer = new byte[4096];
			while ((back = inStr.read(buffer)) != -1) {
				size += back;
				roundProgressBar.setProgress(size);
				Log.i("download ", "1227    下载         " + back);
				fos.write(buffer, 0, back);
			}
			File file = new File(downLoad.toString() + "/" + fName);
			Log.i("download ", "1227    下载         " + file);
			// 解压下载的zip包
			extract(file);
			Log.i("download ", "1227    下载         " + "解压完成");
			inStr.close();
			fos.close();
			// 生成虚拟文件
			makeVmFile(context);
			
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			con.disconnect();
		}
		return "success";
	}

	public static String getHtmlContent(String url, String encode) {
		if (!url.toLowerCase().startsWith("http://")) {
			url = "http://" + url;
		}
		try {
			URL rUrl = new URL(url);
			return getHtmlContent(rUrl, encode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static void extract(File f) throws ZipException, IOException {
		ZipFile zipFile;
		zipFile = new ZipFile(f.toString());
		zipFile.setFileNameCharset("GBK");
		if (zipFile.isEncrypted()) {
			zipFile.setPassword(zipPassword);
		}

		List fileHeaderList = zipFile.getFileHeaders();
		for (int i = 0; i < fileHeaderList.size(); i++) {

			FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
			if (fileHeader != null) {

				String outFilePath = sdDir + "/coursePicture/" + courseDir
						+ "/" + fileHeader.getFileName();
				File outFile = new File(outFilePath);

				if (fileHeader.isDirectory()) {
					outFile.mkdirs();
					continue;
				}

				File parentDir = outFile.getParentFile();
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				ZipInputStream is = zipFile.getInputStream(fileHeader);
				OutputStream os = new FileOutputStream(outFile);

				int readLen = -1;
				byte[] buff = new byte[4096];
				// byte[] encrptPassword
				while ((readLen = is.read(buff)) != -1) {
					// AesCrypt.AesEncrpt(buff[readLen], password);
					os.write(buff, 0, readLen);
				}

				os.close();
				os = null;
				is.close();
				is = null;

				UnzipUtil.applyFileAttributes(fileHeader, outFile);
			}
		}
	}

	public static void makeVmFile(Context context) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			final String sd_path = Environment.getExternalStorageDirectory()
					.toString() + File.separator;
			File file = new File(sd_path + "uclass" + File.separator + "course"
					+ File.separator + courseDir);

			if (!file.exists()) {
				file.mkdirs();
			}

			File file_2 = new File(sd_path + "coursePicture" + File.separator
					+ courseDir + File.separator);
			final File[] list_3 = file_2.listFiles();
			Log.i("list size", "list_size" + list_3.length);
//			if(list_3.length == 1){
//				File[] list_4 = list_3[0].listFiles();
//				for (File f : list_4) {
//					String file_name = f.getName();
//					String file_length = f.length() + "";
//					String chapter = "00";
//
//					Date date = new Date();
//					DateFormat dateFormat = DateFormat.getDateTimeInstance();
//					String save_time = dateFormat.format(date);
//
//					String vm_path = sd_path + "uclass" + File.separator
//							+ "course" + File.separator + courseDir
//							+ File.separator + "ch00" + ".vmdk";
//					File vm_file = new File(vm_path);
//					String file_begin = null;
//					if (!vm_file.exists()) {
//						try {
//							vm_file.createNewFile();
//							file_begin = "0";
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					} else {
//						file_begin = vm_file.length() + "";
//					}
//
//					int file_count = list_4.length;
//					operate = new MyTabOperate(help.getWritableDatabase());
//					operate.insertByCV(file_name, file_length, file_begin,
//							save_time, chapter, file_count, courseDir);
//
//					BufferedInputStream bis = null;
//					BufferedOutputStream bos = null;
//
//					try {
//						FileInputStream fis = new FileInputStream(
//								f.getAbsolutePath());
//						FileOutputStream fos = new FileOutputStream(vm_path,
//								true);
//
//						bis = new BufferedInputStream(fis);
//						bos = new BufferedOutputStream(fos);
//
//						int line = 0;
//						while ((line = bis.read()) != -1) {
//							bos.write(line);
//						}
//					} catch (FileNotFoundException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					} finally {
//						if (bis != null) {
//							try {
//								bis.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//						if (bos != null) {
//							try {
//								bos.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//				deleteDir(list_3[0]);
//			}else{
			for (int i = 0; i < list_3.length; i++) {

				File[] list_4 = list_3[i].listFiles();
				for (File f : list_4) {
					String file_name = f.getName();
					String file_length = f.length() + "";
					String chapter = list_3[i].getName().replaceAll("[^0-9]+",
							"");

					Date date = new Date();
					DateFormat dateFormat = DateFormat.getDateTimeInstance();
					String save_time = dateFormat.format(date);

					String vm_path = sd_path + "uclass" + File.separator
							+ "course" + File.separator + courseDir
							+ File.separator + list_3[i].getName() + ".vmdk";
					File vm_file = new File(vm_path);
					String file_begin = null;
					if (!vm_file.exists()) {
						try {
							vm_file.createNewFile();
							file_begin = "0";
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						file_begin = vm_file.length() + "";
					}

					int file_count = list_4.length;
					operate = new MyTabOperate(help.getWritableDatabase());
					operate.insertByCV(file_name, file_length, file_begin,
							save_time, chapter, file_count, courseDir);

					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;

					try {
						FileInputStream fis = new FileInputStream(
								f.getAbsolutePath());
						FileOutputStream fos = new FileOutputStream(vm_path,
								true);

						bis = new BufferedInputStream(fis);
						bos = new BufferedOutputStream(fos);

						int line = 0;
						while ((line = bis.read()) != -1) {
							bos.write(line);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (bis != null) {
							try {
								bis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (bos != null) {
							try {
								bos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				deleteDir(list_3[i]);
			}
//			}
		}
	}

	public static void deleteDir(File dir) {
		File[] file = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				deleteDir(file[i]);
			} else {
				file[i].delete();
			}
		}
		dir.delete();
	}

	// 递归求取目录文件个数
	public int getlist(File f) {
		int size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
}
