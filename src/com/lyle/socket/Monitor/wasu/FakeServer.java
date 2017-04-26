package com.lyle.socket.Monitor.wasu;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lyle.socket.util.MonitorUtils;

/**
 * @ClassName: FakeServer
 * @Description:
 * @author: Lyle
 * @date: 2017年4月14日 下午4:31:12
 */
public class FakeServer {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		while (true) {
			Thread.sleep(1000);
			Multimap<Integer, String> multiMap = ArrayListMultimap.create();
			MonitorUtils.createMap(multiMap, "温度|11");
			MonitorUtils.createMap(multiMap, "电压|23");
			MonitorUtils.createMap(multiMap, "天气|43");
			MonitorUtils.sendMsg(dos, multiMap);
		}
	}
}
