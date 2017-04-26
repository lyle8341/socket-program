package com.lyle.socket.Monitor;

import java.io.DataOutputStream;
import java.net.Socket;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lyle.socket.util.MonitorUtils;

/**
 * @ClassName: Client
 * @Description:
 * @author: Lyle
 * @date: 2017年4月5日 下午1:34:01
 */
public class Send {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8888);
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		Multimap<Integer, String> multiMap = ArrayListMultimap.create();
		MonitorUtils.createMap(multiMap, "温度|11");
		MonitorUtils.createMap(multiMap, "电压|23");
		MonitorUtils.createMap(multiMap, "天气|43");
		MonitorUtils.sendMsg(dos, multiMap);
	}
}
