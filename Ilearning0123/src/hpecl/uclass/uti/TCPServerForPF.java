/*
 * Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University�? All rights reserved.
 * 版权�?�? ©2015 北京大学软件与微电子学院 高�?�能嵌入式计算实验室
 * filename: TCPServerForPF.java
 * Author: LiHe
 * Version: 1.8
 * Date: 2013/1/25
 * Description: This Class is used for translate the private file between client and server.
 * Modify history: JohnnyCao, create file,2013/1/25
 * 
 */
package hpecl.uclass.uti;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

class CopyThread extends Thread {

	Socket socket = null;

	DataInputStream in = null;

	DataOutputStream fileOut = null;

	Context context;

	Handler copyThreadHandle;
	public CopyThread(Socket socket,Handler handler) throws IOException {
		this.socket = socket;
		copyThreadHandle = handler;
	}

	public void run() {
		try {
			String rootPath =  Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			String savePath = rootPath + "/" + "uclass" + "/" + "shared/";
			File saveFile = new File(savePath);
			if(!saveFile.exists()){
				saveFile.mkdirs();
			}
			int bufferSize1 = 8192;
			byte[] buf = new byte[bufferSize1];

			try {
				in = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
				if (in != null)
					in.close();
			}
			savePath += in.readUTF().toString();// 读取文件�?
			Log.i("sdfa", "0103   "+ savePath);

			fileOut = new DataOutputStream(new BufferedOutputStream(
					new BufferedOutputStream(new FileOutputStream(savePath))));

			// 写文�?
			while (true) {
				int read = 0;
				if (in != null) {
					read = in.read(buf);
					Log.i("15120", "guo read" + read);
				}
				if (read == -1) {
					break;
				}

				fileOut.write(buf, 0, read);
			}

			socket.close();
			fileOut.close();
			Message message = new Message();
			message.what = 10;
			copyThreadHandle.sendMessage(message);
			System.out.println("接收完成，文件存�?" + savePath + "\n");

		} catch (Exception e) {
			System.out.println("接收消息错误" + "\n");
			return;
		}
	}
}




public class TCPServerForPF extends Thread {

	ServerSocket ss;

	DataInputStream in = null;

	DataOutputStream fileOut = null;
	static Handler threadHandle;
	Context fileTransferContext;
	static Socket socket;

	public TCPServerForPF(Handler handle,Context context) {
		super();
		threadHandle = handle;
		fileTransferContext = context;
	}

	public void run() {
		try {
			ss = new ServerSocket(Constant.PORTFORPF);
			while (true) {
				socket = ss.accept();
				Log.i("sdfa", "0110   accept"+ "yes");
				Message m = new Message();
				m.what = 11;
				threadHandle.sendMessage(m);
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();

			try {
				if (ss != null)
					ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void socketClose() {
		try {
			if (ss != null)
				ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void startCopyThread() throws IOException{
		
		new CopyThread(socket, threadHandle).start();
		
	}
	
	
	

}
