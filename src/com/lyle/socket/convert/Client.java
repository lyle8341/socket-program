package com.lyle.socket.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @ClassName: Client
 * @Description:
 * @author: Lyle
 * @date: 2017年4月2日 下午5:38:24
 */
public class Client {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8888);
		// 读取键盘输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// socket输出
		BufferedWriter bwSocket = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		// socket输入
		BufferedReader brSocket = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String line = null;
		while ((line = br.readLine()) != null) {
			if ("over".equals(line))
				break;
			bwSocket.write(line);
			bwSocket.newLine();// 结束符
			bwSocket.flush();// 刷出管道
			String str = brSocket.readLine();
			System.out.println("server:" + str);
		}
		br.close();
		client.close();
	}
}
