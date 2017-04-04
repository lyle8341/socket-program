package com.lyle.socket.login.concurrent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName: Client
 * @Description:
 * @author: Lyle
 * @date: 2017年4月4日 下午12:15:09
 */
public class Client {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("", 8888);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader brSocket = new BufferedReader(new InputStreamReader(client.getInputStream()));
		for (int x = 0; x < 3; x++) {
			String line = br.readLine();// Ctrl+C--->
			System.out.println("跟踪：---->" + line);
			if (line == null)
				break;
			out.println(line);
			String response = brSocket.readLine();
			System.out.println(response);
			if (response.contains("欢迎"))
				break;
		}
		br.close();
		client.close();
	}
}
