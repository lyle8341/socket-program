package com.lyle.socket.Monitor;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lyle.socket.util.MonitorUtils;

/**
 * @ClassName: TT
 * @Description:
 * @author: Lyle
 * @date: 2017年4月7日 下午4:51:15
 */
public class TT {

	public static void main(String[] args) throws Exception {
		outFile();
		// byte[] b = new byte[-2];//java.lang.NegativeArraySizeException
	}

	private static void outFile() throws FileNotFoundException, IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("C:/Users/swere/Desktop/send.txt"));
		Multimap<Integer, String> multiMap = ArrayListMultimap.create();
		MonitorUtils.createMap(multiMap, "温度|15");
		MonitorUtils.createMap(multiMap, "电压|220");
		MonitorUtils.sendMsg(dos, multiMap);
	}
}
