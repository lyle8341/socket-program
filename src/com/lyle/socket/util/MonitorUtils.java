package com.lyle.socket.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Multimap;
import com.lyle.socket.Monitor.wasu.Warning;

/**
 * @ClassName: MonitorUtils
 * @Description:监控对接工具类
 * @author: Lyle
 * @date: 2017年4月6日 上午10:25:07
 */
public class MonitorUtils {

	private static final Charset CHARSETNAME = Charset.forName("GB2312");

	/**
	 * @Title: createMap
	 * @Description: 把消息封装到map中
	 * @param multiMap
	 * @param msg
	 * @return: void
	 */
	public static void createMap(Multimap<Integer, String> multiMap, String msg) {
		multiMap.put(msg.getBytes(CHARSETNAME).length, msg);
	}

	/**
	 * @Title: sendMsg
	 * @Description: 根据给定协议发送消息
	 * @param client
	 * @throws IOException
	 * @return: void
	 */
	public static void sendMsg(DataOutputStream dos, Multimap<Integer, String> content) {
		// -------------------包头--------------------
		// 包头标记4byte--byte
		byte[] head = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		try {
			dos.write(head);
		} catch (IOException e) {
			CloseUtil.closeAll(dos);
			System.err.println("发送[包头标记]时出错！");
		}
		// 协议版本4byte--byte
		byte[] version = { 0x02, 0x00, 0x00, 0x01 };
		try {
			dos.write(version);
		} catch (IOException e) {
			CloseUtil.closeAll(dos);
			System.err.println("发送[协议版本]时出错！");
		}
		// 命令类型4byte--int
		byte[] type = { (byte) 0x02, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };// 只接收事件
		try {
			dos.write(type);
		} catch (IOException e) {
			CloseUtil.closeAll(dos);
			System.err.println("发送[命令类型]时出错！");
		}
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
		try {
			dos.write(bodyLength);
		} catch (IOException e) {
			CloseUtil.closeAll(dos);
			System.err.println("发送[包体长度]时出错！");
		}
		// 保留4byte
		byte[] retain = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		try {
			dos.write(retain);
			dos.write(retain);
			dos.write(retain);
			dos.write(retain);
		} catch (IOException e) {
			CloseUtil.closeAll(dos);
			System.err.println("发送[保留]时出错！");
		}
		// -------------------包体---------------------
		// 组长度2byte--short+内容(key|value)
		if (null != content && content.size() > 0) {
			Set<Integer> keySet = content.keySet();
			for (Integer key : keySet) {
				Collection<String> value = content.get(key);
				Iterator<String> iterator = value.iterator();
				while (iterator.hasNext()) {
					try {
						dos.write(shortToByte((short) key.intValue()));
					} catch (IOException e) {
						CloseUtil.closeAll(dos);
						System.err.println("发送[组长度]时出错！");
					}
					try {
						dos.write(iterator.next().getBytes(CHARSETNAME));
					} catch (IOException e) {
						CloseUtil.closeAll(dos);
						System.err.println("发送[组内容]时出错！");
					}
				}
			}
		}
		try {
			dos.flush();
		} catch (IOException e) {
			CloseUtil.closeAll(dos);
			System.err.println("刷新管道出错！");
		}
	}

	/**
	 * @Title: parseResponse
	 * @Description:解析服务端发送的内容
	 * @param dis
	 * @throws IOException
	 * @return: void
	 */
	public static void parseResponse(DataInputStream dis, List<String> params) {
		boolean flag = true;
		while (flag) {
			try {
				while (dis.available() > 0) {
					for (int i = 0; i < 12; i++) {
						try {
							dis.readByte();
						} catch (IOException e) {
							CloseUtil.closeAll(dis);
							flag = false;
							System.err.println("读取[包头标记|协议版本|命令类型]时出错！");
						}
					}
					// 包体长度
					byte[] bodyLength = new byte[4];
					try {
						dis.read(bodyLength);
					} catch (IOException e) {
						CloseUtil.closeAll(dis);
						flag = false;
						System.err.println("读取[包体长度]时出错！");
					}
					/*
					 * ArrayUtils.reverse(bodyLength); int totalLength = byteToInt(bodyLength);
					 */
					int totalLength = getLength(bodyLength);
					System.out.println("接收包体长度：" + totalLength);
					for (int i = 0; i < 16; i++) {
						try {
							dis.readByte();
						} catch (IOException e) {
							CloseUtil.closeAll(dis);
							flag = false;
							System.err.println("读取[保留]时出错！");
						}
					}
					int len = 0;
					/* int group = 1; */
					byte[] buff = null;
					if (totalLength > 0) {
						while (totalLength > len) {
							buff = new byte[2];
							try {
								dis.read(buff);
							} catch (IOException e) {
								CloseUtil.closeAll(dis);
								flag = false;
								System.err.println("读取[组长度]时出错！");
							}
							int temp = byteToShort(buff);
							buff = new byte[temp];
							try {
								dis.read(buff);
							} catch (IOException e) {
								CloseUtil.closeAll(dis);
								flag = false;
								System.out.println("读取[组内容]时出错！");
							}
							params.add(new String(buff, CHARSETNAME));
							len += temp + 2;
							/*
							 * System.out.println("【组" + group++ + "的内容长度：->" + temp + "】,内容【" + new
							 * String(buff, CHARSETNAME) + "】" + " | 截止当前读取的总字节数：" + len);
							 */
							// System.out.println(new String(buff, CHARSETNAME));
						}
					}
				}
			} catch (IOException e) {
				CloseUtil.closeAll(dis);
				flag = false;
				System.err.println("检查是否有可读数据时候发生错误！");
			}
			try {
				processData(params);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				CloseUtil.closeAll(dis);
				flag = false;
				System.err.println("客户端休息时候发生错误！");
			} catch (Exception e) {
				CloseUtil.closeAll(dis);
				flag = false;
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * @Title: processData
	 * @Description: 处理数据
	 * @param params
	 * @return: void
	 * @throws Exception
	 */
	private static void processData(List<String> params) throws Exception {
		Iterator<String> its = params.iterator();
		while (its.hasNext()) {
			String value = its.next();
			String[] kv = value.split("\\|\\|");
			if (kv.length == 2) {
				// 处理
				String[] values = kv[1].split("\\|");
				if (values.length == 7) {
					Warning warn = converToWarning(kv[0], values);
					System.out.println(warn);
				} else {
					throw new Exception("事件值格式不对，可能是接收数据不全");
				}
				its.remove();// 移除
			} else {
				throw new Exception("事件格式不对！可能是接收的数据不全");
			}
		}
	}

	/**
	 * @param kv
	 * @Title: converToWarning
	 * @Description: 把截取的每段数据封装到对象
	 * @param values
	 * @return: void
	 */
	private static Warning converToWarning(String kv, String[] values) {
		Warning w = new Warning();
		w.setEventKey(kv);
		w.setStationName(values[0]);
		w.setEventSource(values[1]);
		w.setEventContent(values[2]);
		w.setEventLevel(values[3]);
		w.setEventType(values[4]);
		w.setEventHappen(DateUtils.str2Date(values[5]));
		w.setEventId(values[6]);
		return w;
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
		// byte[] b = { (byte) 0xD8, (byte) 0xB1 };
		// System.out.println(byteToShort(b));
		byte[] b = { (byte) 0xCA, (byte) 0xFF, 0x0F, 0x00 };
		System.out.println(getLength(b));
		ArrayUtils.reverse(b);
		System.out.println(byteToInt(b));
		// byte[] b = { (byte) 0xA1, 0x68, 0x0A, 0x00 };
		// System.out.println(dwordBytesToLong(b));
	}

	public static byte[] shortToByte(short s) {
		byte[] b = new byte[2];
		b[0] = (byte) (s >> 0);
		b[1] = (byte) (s >> 8);
		return b;
	}

	public static short byteToShort(byte[] b) {
		return (short) (((b[0] & 0xff) | (b[1] << 8)));
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

	/**
	 * @Title: getLength
	 * @Description: 解析包体长度数组
	 * @param head
	 * @return: int
	 */
	public static int getLength(byte[] head) {
		return (head[0] & 0xFF) | ((head[1] & 0xFF) << 8) | ((head[2] & 0xFF) << 16) | ((head[3] & 0xFF) << 24);
	}
}
