package hpecl.uclass.download;

import hpecl.uclass.uti.RoundProgressBar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.lingala.zip4j.exception.ZipException;
import android.os.Environment;
import android.util.Log;

public class ConfigDownload {
	static String zipPassword = "hpecl701";

	static String getinfo;
	String imageUrl;
	static File sdDir = null;

	static File downLoad;
	static String fName;

	static RoundProgressBar roundProgressBar;

	public static int fileLength;
	public static String filename;

	public static String courseDownload(String url,String encode, String fileName) {
		sdDir = Environment.getExternalStorageDirectory();// ???????
		downLoad = new File(sdDir, "/uclass/system_yk");
		fName = fileName;
		if (!downLoad.exists()) {
			if (downLoad.mkdirs())
				Log.i("guo ", "makedir   " + "????????????????");
		}

		getinfo = getHtmlContent(url, encode);
//		Log.i("guo ", "0810   " + getinfo);

		return getinfo;
	}

	public static String getHtmlContent(URL url, String encode) throws ZipException {

		int responseCode = -1;
		HttpURLConnection con = null;

		try {
//			Log.i("guo", "0422    getHtmlContent");
			con = (HttpURLConnection) url.openConnection();
//			Log.i("guo", "0422    getHtmlContent");
			con.setRequestMethod("GET");
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);
			// con.setRequestProperty("Cookie", cookie);
			// String fileName = con.getHeaderField(6);
			con.connect();
			// ???????????????
			responseCode = con.getResponseCode();
//			fileLength =  con.getContentLength();
//			filename = con.getHeaderField("Content-Disposition");
			Log.i("yuyu", "0422    "+responseCode);
			if (responseCode == -1) {
				System.out.println(url.toString()+ " : connection is failure...");
				con.disconnect();
				return null;
			}
			if (responseCode >= 400) {
				// ???????
				System.out.println("???????:get response code: " + responseCode);
				con.disconnect();
				return null;
			}
			// ??????
			FileOutputStream fos = new FileOutputStream(downLoad.toString()+ "/" + fName);
			// ???????????????
			// OutputStreamWriter fOutstreamReader = new OutputStreamWriter(fos,encode);
			// BufferedWriter fBuffStr = new BufferedWriter(fOutstreamReader);

			// String file = con.getContent().toString();
			// Log.i("download ", "1227    ????         "+file);
			InputStream inStr = con.getInputStream();
			Log.i("guo", "0422" + url);

			int size = 0;
			int back = 0;
			byte[] buffer = new byte[4096];
			while ((back = inStr.read(buffer)) != -1) {
				size += back;
//				Log.i("download ", "1227    ????      " + back);
				fos.write(buffer, 0, back);
			}
			File file = new File(downLoad.toString() + "/" + fName);
//			Log.i("download ", "1227    ????         " + file);
//			Log.i("download ", "1227    ????         " + "??????");
			inStr.close();
			fos.close();

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
}
