package com.lyle.socket.uploadPictrue.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName: PicThread
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午10:10:18
 */
public class PicThread implements Runnable {

	private Socket client;

	public PicThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		String ip = client.getInetAddress().getHostAddress();
		int count = 1;
		try {
			System.out.println(ip + "--->connected");
			InputStream in = client.getInputStream();
			// 自定义文件名
			File file = new File(
					"src/com/lyle/socket/uploadPictrue/concurrent/qrcode-bat" + "(" + count + ")" + ".jpg");
			while (file.exists())
				file = new File(
						"src/com/lyle/socket/uploadPictrue/concurrent/qrcode-bat" + "(" + (count++) + ")" + ".jpg");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = in.read(buff)) != -1) {
				fos.write(buff, 0, len);
			}
			OutputStream out = client.getOutputStream();
			out.write("上传成功".getBytes());
			fos.close();
			client.close();
		} catch (Exception e) {
			throw new RuntimeException(ip + "--->上传失败");
		}
	}
}
