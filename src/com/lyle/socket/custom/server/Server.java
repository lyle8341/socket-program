package com.lyle.socket.custom.server;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName: Server
 * @Description:
 *               <p>
 *               客户端：浏览器 <br>
 *               服务器：自定义
 *               </p>
 * @author: Lyle
 * @date: 2017年4月4日 下午1:23:12
 */
public class Server {

	/*
	 * 浏览器地址：http://1xx.1xx.x.xxx:8888/
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		System.out.println(client.getInetAddress().getHostAddress() + "...connected");
		InputStream in = client.getInputStream();
		byte[] buff = new byte[1024];
		int len = in.read(buff);
		/*
		 * 请求头和请求体
		 */
		System.out.println(new String(buff, 0, len));
		PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
		pw.println("<font color='red' size='7'>hello kitty</font>");
		client.close();
		server.close();
	}
}
