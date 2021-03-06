package com.lyle.socket.Monitor;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.lyle.socket.util.MonitorUtils;

/**
 * @ClassName: App
 * @Description:
 * @author: Lyle
 * @date: 2017年4月5日 下午5:00:11
 */
public class App {

	public static void main(String[] args) throws Exception {
		List<String> params = new ArrayList<>();
		// FileInputStream fis = new FileInputStream("C:/Users/swere/Desktop/4.14/测试1.bin");
		// FileInputStream fis = new FileInputStream("C:/Users/swere/Desktop/4.14/测试2.bin");
		// FileInputStream fis = new FileInputStream("C:/Users/swere/Desktop/4.14/测试3.bin");
		FileInputStream fis = new FileInputStream("C:/Users/swere/Desktop/4.14/测试4.bin");
		DataInputStream dis = new DataInputStream(fis);
		MonitorUtils.parseResponse(dis, params);
	}
}
