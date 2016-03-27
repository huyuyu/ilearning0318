/*
 * Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University�? All rights reserved.
 * 版权�?�? ©2015 北京大学软件与微电子学院 高�?�能嵌入式计算实验室
 * filename: TCPClientForPF.java
 * Author: JohnnyCao
 * Version: 1.9.1
 * Date: 2015/1/25
 * Description: This Class is used for translate the private file between client and server.
 * Modify history: JohnnyCao, create file,2015/1/25
 */package hpecl.uclass.uti;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import android.util.Log;

public class TCPClientForPF extends Thread {

	String ip;

	int port;

	String filePath;

	byte[] key;

	Socket soc;


	public TCPClientForPF(String ip, int port, String filePath)
			throws Exception {
		this.filePath = filePath;
		this.ip = ip;
		this.port = port;
		Log.i("sdfa", "0103   "+ ip);
		Log.i("sdfa", "0103   "+ port);
		Log.i("sdfa", "0103   "+ filePath);
		
		
	}

	public void run() {
		try {
			File file = new File(filePath);
			soc = new Socket(ip, port);
			DataInputStream fin = new DataInputStream(new BufferedInputStream(
					new FileInputStream(filePath)));
			DataOutputStream fout = new DataOutputStream(soc.getOutputStream());
			Log.i("sdfa", "0103   "+ " 线程启动");
			fout.writeUTF(file.getName());
			fout.flush();

			int bufferSize1 = 8192;
			byte[] buf = new byte[bufferSize1];

			while (true) {
				int read = 0;
				if (fin != null) {
					read = fin.read(buf);
					Log.i("read11", "read长度11" + read);
				}

				if (read == -1) {
					break;
				}
				// cc212
				// 调用加密方法，如果能填满buffer，采用NoPadding填充方式
				// try {
				//
				// //AES encryption加密
				// buf=AesCrypt.AesEncrpt(buf, key);
				//
				//
				// } catch (Exception e) {
				//
				// e.printStackTrace();
				// }
				fout.write(buf, 0, read);
			}
			fout.flush();
			// 注意关闭socket链接哦，不然客户端会等待server的数据过来，
			// 直到socket超时，导致数据不完整�?
			fin.close();
			soc.close();
			Log.i("time2", "cccc");
			System.out.println("文件传输完成");

			// 设置标志�?,表示文件已经下载完成，MainActicity中就可以使用�?
//			MainActivity.downloadFlag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
