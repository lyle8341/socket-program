package com.lyle.socket.Monitor.wasu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lyle.socket.util.CloseUtil;
import com.lyle.socket.util.MonitorUtils;

/**
 * @ClassName: WasuMonitorClient
 * @Description: 监控客户端
 * @author: Lyle
 * @date: 2017年4月14日 下午3:22:38
 */
public class WasuMonitorClient {

	private String host;

	private int port;

	// 秒
	private int timeout;

	/**
	 * @Title:WasuMonitorClient
	 * @Description:timeout/秒
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public WasuMonitorClient(String host, int port, int timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout * 1000;
	}

	public WasuMonitorClient(String host, int port) {
		// 默认超时60s
		this(host, port, 60);
	}

	public WasuMonitorClient() {
	}

	public void start() {
		Socket client = null;
		try {
			client = new Socket();
			SocketAddress endpoint = new InetSocketAddress(host, port);
			client.connect(endpoint, timeout);
			Multimap<Integer, String> content = ArrayListMultimap.create();
			MonitorUtils.sendMsg(new DataOutputStream(client.getOutputStream()), content);
			List<String> params = new ArrayList<String>();
			DataInputStream dis = new DataInputStream(client.getInputStream());
			MonitorUtils.parseResponse(dis, params);
		} catch (UnknownHostException e) {
			System.err.println("未知主机");
			CloseUtil.closeAll(client);
		} catch (IOException e) {
			System.err.println(e.toString());
			CloseUtil.closeAll(client);
		}
	}
}
