package com.lyle.socket.copyFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName: Client
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午3:55:47
 */
public class Client {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8888);
		BufferedReader br = new BufferedReader(new FileReader("src/com/lyle/socket/copyFile/古风.txt"));
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		String line = null;
		while ((line = br.readLine()) != null) {
			out.println(line);
		}
		/*
		 * 结束流
		 */
		client.shutdownOutput();
		BufferedReader brSocket = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String response = brSocket.readLine();
		System.out.println(response);
		br.close();
		client.close();
	}
}
