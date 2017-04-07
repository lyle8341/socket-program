package com.lyle.socket.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

/**
 * @ClassName: MonitorUtils
 * @Description:监控对接工具类
 * @author: Lyle
 * @date: 2017年4月6日 上午10:25:07
 */
public class MonitorUtils {

	/**
	 * @Title: createMap
	 * @Description: 把消息封装到map中
	 * @param multiMap
	 * @param msg
	 * @return: void
	 */
	public static void createMap(Multimap<Integer, String> multiMap, String msg) {
		multiMap.put(msg.getBytes().length, msg);
	}

	/**
	 * @Title: sendMsg
	 * @Description: 根据给定协议发送消息
	 * @param client
	 * @throws IOException
	 * @return: void
	 */
	public static void sendMsg(DataOutputStream dos, Multimap<Integer, String> content) throws IOException {
		// -------------------包头--------------------
		// 包头标记4byte--byte
		byte[] head = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		dos.write(head);
		// 协议版本4byte--byte
		byte[] version = { 0x02, 0x00, 0x00, 0x01 };
		dos.write(version);
		// 命令类型4byte--int
		byte[] type = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		dos.write(type);
		// 包体长度4byte
		// 所有的组长度
		long allGroupLength = (content.size()) * 2;
		// 所有的内容长度
		long allContentLength = 0;
		if (null != content && content.size() > 0) {
			Set<Integer> keySet = content.keySet();
			for (Integer key : keySet) {
				Collection<String> value = content.get(key);
				Iterator<String> iterator = value.iterator();
				while (iterator.hasNext()) {
					allContentLength += iterator.next().getBytes().length;
				}
			}
		}
		System.out.println("包体长度：" + (allGroupLength + allContentLength));
		byte[] bodyLength = longToDword(allGroupLength + allContentLength);
		dos.write(bodyLength);
		// 保留4byte
		byte[] retain = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		dos.write(retain);
		dos.write(retain);
		dos.write(retain);
		dos.write(retain);
		// -------------------包体---------------------
		// 组长度2byte--short+内容(key|value)
		if (null != content && content.size() > 0) {
			Set<Integer> keySet = content.keySet();
			for (Integer key : keySet) {
				Collection<String> value = content.get(key);
				Iterator<String> iterator = value.iterator();
				while (iterator.hasNext()) {
					dos.writeShort(key);
					dos.write(iterator.next().getBytes("GB2312"));
				}
			}
		}
		dos.flush();
	}

	/**
	 * @Title: parseResponse
	 * @Description:解析服务端发送的内容
	 * @param dis
	 * @throws IOException
	 * @return: void
	 */
	public static void parseResponse(DataInputStream dis, List<String> params) throws IOException {
		for (int i = 0; i < 12; i++) {
			dis.readByte();
		}
		// 包体长度
		byte[] bodyLength = new byte[4];
		dis.read(bodyLength);
		long totalLength = dwordBytesToLong(bodyLength);
		System.out.println("包体长度：" + totalLength);
		for (int i = 0; i < 16; i++) {
			dis.readByte();
		}
		int len = 0;
		int group = 1;
		byte[] buff = null;
		if (totalLength > 0) {
			while (totalLength > len) {
				short temp = dis.readShort();
				buff = new byte[temp];
				System.out.println("【组" + group++ + "的内容长度：---->】" + temp);
				dis.read(buff);
				params.add(new String(buff, 0, temp, "GB2312"));
				System.out.println(new String(buff, 0, temp, "GB2312"));
				len += temp + 2;
			}
		}
	}

	/**
	 * @Title: dwordBytesToLong
	 * @Description: dword转为long
	 * @param data
	 * @return: long
	 */
	public static long dwordBytesToLong(byte[] data) {
		return (data[3] << 8 * 3) + (data[2] << 8 * 2) + (data[1] << 8) + data[0];
	}

	/**
	 * @Title: longToDword
	 * @Description: long类型转为dword
	 * @param value
	 * @return: byte[]
	 */
	public static byte[] longToDword(long value) {
		byte[] data = new byte[4];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (value >> (8 * i));
		}
		return data;
	}
}
