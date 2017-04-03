package com.lyle.socket.uploadFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName: Server
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午3:55:41
 */
public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter out = new PrintWriter(new File("src/com/lyle/socket/copyFile/古风-bat.txt"));
		String line = null;
		while ((line = br.readLine()) != null) {
			out.println(line);
		}
		PrintWriter response = new PrintWriter(client.getOutputStream(), true);
		response.println("上传成功");
		out.close();
		client.close();
		server.close();
	}
}
