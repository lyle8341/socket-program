package com.lyle.socket.uploadPictrue.concurrent;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName: Client
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午6:14:39
 */
public class Client {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8888);
		FileInputStream fis = new FileInputStream("src/com/lyle/socket/uploadPictrue/concurrent/qrcode.jpg");
		OutputStream out = client.getOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while ((len = fis.read(buff)) != -1) {
			out.write(buff, 0, len);
		}
		/*
		 * 告诉服务端数据已写完
		 */
		client.shutdownOutput();
		InputStream in = client.getInputStream();
		byte[] response = new byte[1024];
		int num = in.read(response);
		System.out.println(new String(response, 0, num));
		fis.close();
		client.close();
	}
}
