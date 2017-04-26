package com.lyle.socket.Monitor;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.lyle.socket.util.MonitorUtils;

public class Receive {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		DataInputStream dis = new DataInputStream(client.getInputStream());
		List<String> params = new ArrayList<>();
		MonitorUtils.parseResponse(dis, params);
	}
}
