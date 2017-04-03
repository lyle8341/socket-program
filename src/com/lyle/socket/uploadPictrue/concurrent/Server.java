package com.lyle.socket.uploadPictrue.concurrent;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName: Server
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午9:28:26
 */
public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		while (true) {
			Socket client = server.accept();
			new Thread(new PicThread(client)).start();
		}
	}
}
