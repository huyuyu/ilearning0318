package hpecl.uclass.uti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ReadConfigFile {

	private static String str="";
	static SharedPreferences sp;
	static SharedPreferences.Editor spe;
	public static void getConstantParam(File readFile) {
		// TODO Auto-generated method stub
//
//		Intent intent=
//		String str=intent.getStringExtra("referenceLink");

		str=Constant.REFERENCECOURSE;
		if(str=="0A01"){
			Constant.REFERENCE="http://www.grandsw.com:9191/class/0a701/booksInfo/";
		}else if(str=="0D01"){
			Constant.REFERENCE="http://www.grandsw.com:9191/class/0D701/booksInfo1/";
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(readFile));
			String line = "";
			while((line = br.readLine()) != null){
				Log.d("yuyu", line);
				if(line.startsWith("NOTIFICATION")){
					Constant.A701NOTIFICATION = line.split("\"")[1];
				}else if(line.startsWith("QUESTIONFORANSWER")){
					Constant.QUESTIONFORANSWER = line.split("\"")[1];
				}else if(line.startsWith("CONMUNICATE")){
					Constant.CONMUNICATE = line.split("\"")[1];
				}else if(line.startsWith("DOWNLOADTEST")){
					Constant.DOWNLOADTEST = line.split("\"")[1];
				}else if(line.startsWith("CHAPTER")){
					Constant.CHAPTER = line.split("\"")[1];
				}else if(line.startsWith("DISSCUSS")){
					Constant.DISSCUSS = line.split("\"")[1];
				}else if(line.startsWith("CONSERNREADING")){
					Constant.CONSERNREADING = line.split("\"")[1];
				}else if(line.startsWith("CATALOGLIST")){					
					Constant.CATALOGLIST = line.split("\"")[1];
				}else if(line.startsWith("FEEDBACK")){					
					Constant.FEEDBACK = line.split("\"")[1];
				}else if(line.startsWith("FEEDBACK")){					
					Constant.FEEDBACK = line.split("\"")[1];
				}else if(line.startsWith("TOPICID")){					
					Constant.TOPICID = line.split("\"")[1];
				}else if(line.startsWith("QUESTIONID")){					
					Constant.QUESTIONID = line.split("\"")[1];
				}else if(line.startsWith("SHARINGID")){					
					Constant.SHARINGID = line.split("\"")[1];
				}
			}
						    			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
