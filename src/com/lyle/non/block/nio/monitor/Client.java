package com.lyle.non.block.nio.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Client {

	private static ByteBuffer sendBuf = ByteBuffer.allocate(1024);

	private static ByteBuffer respBuf = ByteBuffer.allocate(1024);

	private static Charset CHARSET = Charset.forName("GB2312");

	private static Map<SelectionKey, Object> sessionMessage = new ConcurrentHashMap<SelectionKey, Object>();

	public static void main(String[] args) throws IOException {
		// 多路复用器
		Selector selector = Selector.open();
		// 客户端
		SocketChannel client = SocketChannel.open();
		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_CONNECT);// 注册连接事件
		client.connect(new InetSocketAddress("localhost", 8888));// 连接服务器,触发connect事件
		// 监听客户端事件
		listener(client, selector);
	}

	private static void listener(SocketChannel client, Selector selector) throws IOException {
		while (true) {
			int eventCount = selector.select();// 如果有客户端连接过来，就会被轮询出来
			if (eventCount <= 0) {// 小于等于0，说明没有事件触发
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();// 获取有事件触发的selectkey
			Iterator<SelectionKey> iter = keys.iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				// 处理事件
				try {
					process(client, selector, key);
				} catch (IOException e) {// 客户端异常关闭
					// close(key);
				}
				iter.remove();// 处理完移除这个事件
			}
		}
	}

	private static void process(SocketChannel client, Selector selector, SelectionKey key) throws IOException {
		Scanner scan = new Scanner(System.in);
		SocketChannel channel = null;
		if (key.isValid() && key.isConnectable()) {// 事件是否是有效的
			channel = (SocketChannel) key.channel();
			if (channel.isConnectionPending()) {// 如果正在连接，完成连接
				channel.configureBlocking(false);
				channel.finishConnect();
				System.out.println("客户端连接成功");
			}
			client.register(selector, SelectionKey.OP_WRITE);// 客户端连接上了，可以写了
		} else if (key.isValid() && key.isWritable()) {// 可写了，向服务器端发送消息
			System.out.println("可以写了-----");
			String line = scan.next();
			if (line.equals("finsh")) {
				System.out.println("关闭了...");
				key.cancel();
				client.close();
				System.exit(1);
			} else {
				sendBuf.clear();
				sendBuf.put(("client write:[" + line + "]").getBytes(CHARSET));
				sendBuf.flip();
				client.write(sendBuf);// 响应客户端
				key.interestOps(SelectionKey.OP_READ);// 写完数据后，这个客户端在多路复用器上的事件又由write变为read，可以读了
			}
		} else if (key.isValid() && key.isReadable()) {// 可读,获取服务器端响应
			System.out.println("可以读了-----");
			respBuf.clear();// 清空缓冲区
			int length = client.read(respBuf);// 将数据读到缓冲区
			if (length > 0) {// 读取到内容的时候
				String message = new String(respBuf.array(), 0, length, CHARSET);
				System.out.println("client read:[" + message + "]");
				key.interestOps(SelectionKey.OP_WRITE);// 读完数据后，这个客户端在多路复用器上的事件由read变为write，可以写了
			}
		}
	}
}
