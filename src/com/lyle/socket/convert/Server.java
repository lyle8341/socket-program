package com.lyle.socket.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName: Server
 * @Description:
 * @author: Lyle
 * @date: 2017年4月2日 下午5:38:15
 */
public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		BufferedReader brSocket = new BufferedReader(new InputStreamReader(client.getInputStream()));
		BufferedWriter bwSocket = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		String line = null;
		while ((line = brSocket.readLine()) != null) {
			bwSocket.write(line.toUpperCase());
			bwSocket.newLine();
			bwSocket.flush();
		}
		// client.close();//Client.java已经关闭了client
		server.close();
	}
}
