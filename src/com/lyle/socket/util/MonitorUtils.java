package com.lyle.socket.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Multimap;

/**
 * @ClassName: MonitorUtils
 * @Description:监控对接工具类
 * @author: Lyle
 * @date: 2017年4月6日 上午10:25:07
 */
public class MonitorUtils {

	private static final String CHARSETNAME = "GB2312";

	/**
	 * @Title: createMap
	 * @Description: 把消息封装到map中
	 * @param multiMap
	 * @param msg
	 * @return: void
	 */
	public static void createMap(Multimap<Integer, String> multiMap, String msg) {
		try {
			multiMap.put(msg.getBytes(CHARSETNAME).length, msg);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的字符集！【" + CHARSETNAME + "】");
		}
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
		int allGroupLength = (content.size()) * 2;
		// 所有的内容长度
		int allContentLength = 0;
		if (null != content && content.size() > 0) {
			Set<Integer> keySet = content.keySet();
			for (Integer key : keySet) {
				Collection<String> value = content.get(key);
				Iterator<String> iterator = value.iterator();
				while (iterator.hasNext()) {
					allContentLength += iterator.next().getBytes(CHARSETNAME).length;
				}
			}
		}
		System.out.println("发送包体长度：" + (allGroupLength + allContentLength));
		byte[] bodyLength = int2byte(allGroupLength + allContentLength);
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
					dos.write(shortToByte((short) key.intValue()));
					dos.write(iterator.next().getBytes(CHARSETNAME));
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
		ArrayUtils.reverse(bodyLength);
		int totalLength = byteToInt(bodyLength);
		System.out.println("接收包体长度：" + totalLength);
		for (int i = 0; i < 16; i++) {
			dis.readByte();
		}
		int len = 0;
		int group = 1;
		byte[] buff = null;
		if (totalLength > 0) {
			while (totalLength > len) {
				buff = new byte[2];
				dis.read(buff);
				int temp = byteToShort(buff);
				buff = new byte[temp];
				dis.read(buff);
				params.add(new String(buff, 0, temp, CHARSETNAME));
				System.out.println(new String(buff, 0, temp, CHARSETNAME));
				len += temp + 2;
				System.out.println("【组" + group++ + "的内容长度：---->】" + temp + "   |   字节数：" + len);
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

	/**
	 * @Title: byteToInt
	 * @Description: 字节数组转int
	 * @param b
	 * @return: int
	 */
	public static int byteToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static void main(String[] args) {
		byte[] b = { 0x0F, 0x00 };
		System.out.println(byteToShort(b));
		// byte[] b = { 0x00, 0x00, 0x00, 0x17 };
		// ArrayUtils.reverse(b);
		// System.out.println(byteToInt(b));
	}

	public static byte[] shortToByte(short s) {
		byte[] b = new byte[2];
		b[1] = (byte) (s >> 8);
		b[0] = (byte) (s >> 0);
		return b;
	}

	public static short byteToShort(byte[] b) {
		return (short) (((b[1] << 8) | b[0] & 0xff));
	}

	/**
	 * @Title: int2byte
	 * @Description: int转byte数组
	 * @param res
	 * @return: byte[]
	 */
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}
}
