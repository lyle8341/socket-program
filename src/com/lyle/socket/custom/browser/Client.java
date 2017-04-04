package com.lyle.socket.custom.browser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName: Client
 * @Description: 自定义浏览器
 * @author: Lyle
 * @date: 2017年4月4日 下午3:16:56
 */
public class Client {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8080);
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		out.println("GET / HTTP/1.1");
		out.println("Accept: */*");
		out.println("Accept-Language: zh-CN");
		out.println("Host: 1xx.1xx.1.1xx:8080");
		// out.println("Connection: closed");//请求完立即断掉
		out.println("Connection: keep-alive");// 请求完继续保持连接
		out.println();
		out.println();
		BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String line = null;
		while ((line = br.readLine()) != null) {
			//
			System.out.println(line);
		}
		client.close();
	}
}
