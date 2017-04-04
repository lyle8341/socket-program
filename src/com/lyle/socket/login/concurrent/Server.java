package com.lyle.socket.login.concurrent;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName: Server
 * @Description:
 * @author: Lyle
 * @date: 2017年4月4日 下午12:14:55
 */
public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		while (true) {
			Socket client = server.accept();
			new Thread(new UserThread(client)).start();
		}
	}
}
