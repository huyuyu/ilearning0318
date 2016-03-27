package hpecl.uclass.uti;

import java.util.ArrayList;

import android.os.Environment;

/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Constant.java
* Author：郭靖东
* Version：1.1
* Date：2015/06/11
* Description：This java file is to save constant params.
* Modify history：
* 郭靖东,create file,2015/06/11
*/
public class Constant {
	
	public static final String IMAGEKEY = "http://www.grandsw.com:9191/class/e/ShowKey/?ecms";
//	public static final String FORUM_URL = "http://www.grandsw.com:9191/class/e/pl/index.php?page=0&start=0&classid=98&id=446";
	public static final String FORMURL = "http://www.grandsw.com:9191/class/e/enews/index.php";
//	public static final String A701 = "http://www.grandsw.com:9191/class/0a701/";
//	public static final String D701COURSEINFO = "http://www.grandsw.com:9191/class/0D701/courseInfo/";
//	public static final String PERSONALSPACE = "http://www.grandsw.com:9191/class/e/space/?userid=493";
//	public static final String DOWNLOAD = "http://www.grandsw.com:9191/class/e/action/ShowInfo?classid=65&id=836";
//	public static final String DOWNLOADTEXT = "http://www.grandsw.com:9191/class/download/coursenotes/LectureM00.zip";
	
	public static final String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
//	public static final String UPLOADDOC = "http://www.grandsw.com:9191/class/e/DoInfo/ChangeClass.php?mid=10";
//	public static final String CHOOSECLASSFORM = "http://www.grandsw.com:9191/class/e/DoInfo/AddInfo.php";
//	public static final String LABARTICLEUPLOAD = "http://www.grandsw.com:9191/class/e/DoInfo/AddInfo.php?mid=10&enews=MAddInfo&classid=84&Submit=%CC%ED%BC%D3%D0%C5%CF%A2";
	public static String Sharing0A701 = "http://www.grandsw.com:9191/class/e/DoInfo/ecms.php";
	public static final String REGISTER = "http://www.grandsw.com:9191/class/e/member/register/";
//	public static final String UPLOADCLASSFILE = "http://www.grandsw.com:9191/class/e/DoInfo/ecms.php";
	
	public static String QUESTIONFORANSWER = "";	
	public static String CONMUNICATE = "";
	public static String A701NOTIFICATION = "";
	public static String DOWNLOADTEST = "";
	public static String CHAPTER = "0";
	public static String FEEDBACK = "";
	public static String REFERENCE = "";
	public static String REFERENCECOURSE="";

	//目录
	public static String CATALOGLIST = "";
	
	
	//讨论专区从第0章到第n章  
	public static String DISSCUSS = "";
	public static String TOPICID = "";
	public static String QUESTIONID = "";
	public static String SHARINGID = "";
	
	
	
	//相关阅读
	public static String CONSERNREADING = "";
	public static final int RESULTCODE = 1000;

//	public static String A701REFERENCR = "";
//	public static String REFERENCE = "http://www.grandsw.com:9191/class/0a701/booksInfo/";
	public static final String A701BEFOR = "http://www.grandsw.com:9191";
	
	public static final int PORTFORPF = 8888;
	public static final int UDPPORT = 6000;
	public static final int RECV_BUF_SIZE = 4096;// 4KB

}
