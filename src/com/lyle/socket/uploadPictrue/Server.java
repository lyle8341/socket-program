package com.lyle.socket.uploadPictrue;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName: Server
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午6:14:30
 */
public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		InputStream in = client.getInputStream();
		FileOutputStream fos = new FileOutputStream("src/com/lyle/socket/uploadPictrue/qrcode-bat.jpg");
		byte[] buff = new byte[1024];
		int len = 0;
		while ((len = in.read(buff)) != -1) {
			fos.write(buff, 0, len);
		}
		OutputStream out = client.getOutputStream();
		out.write("上传成功".getBytes());
		fos.close();
		server.close();
	}
}
