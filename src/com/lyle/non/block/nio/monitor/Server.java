package com.lyle.non.block.nio.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: Servere
 * @Description:
 * @author: Lyle
 * @date: 2017年4月14日 上午9:53:05
 */
public class Server {

	private static ByteBuffer recvBuf = ByteBuffer.allocate(1024);

	private static ByteBuffer respBuf = ByteBuffer.allocate(1024);

	private static Map<SelectionKey, Object> sessionMessage = new ConcurrentHashMap<>();

	private static Charset CHARSET = Charset.forName("GB2312");

	public static void main(String[] args) {
		start(8888);
	}

	public static void start(int port) {
		// 多路复用器
		try {
			Selector selector = Selector.open();
			ServerSocketChannel server = ServerSocketChannel.open();
			server.bind(new InetSocketAddress(port));
			server.configureBlocking(false);// 设置非阻塞
			/*
			 * server上注册一个多路复用器____事件标签OP_ACCEPT接收客户端连接 这样多路复用器将不断轮询server，当有客户端连接过来，就会被轮询出来
			 */
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The server is started!");
			// 启动监听
			listener(server, selector);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: listener
	 * @Description:
	 * @param server
	 * @param selector
	 * @return: void
	 * @throws IOException
	 */
	private static void listener(ServerSocketChannel server, Selector selector) throws IOException {
		while (true) {
			int eventCount = selector.select();// 如果有客户端连接过来，就会被轮询出来
			System.out.println("eventCount:" + eventCount);
			if (eventCount <= 0) {
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();// 获取有事件触发的selectKey
			Iterator<SelectionKey> its = keys.iterator();
			while (its.hasNext()) {
				SelectionKey key = its.next();
				// 处理事件
				try {
					process(server, selector, key);
				} catch (Exception e) {
					e.printStackTrace();
				}
				its.remove();// 处理完移除这个事件
			}
		}
	}

	/**
	 * @Title: process
	 * @Description:
	 * @param server
	 * @param selector
	 * @param key
	 * @return: void
	 * @throws IOException
	 */
	private static void process(ServerSocketChannel server, Selector selector, SelectionKey key) throws IOException {
		SocketChannel client = null;
		if (key.isValid() && key.isAcceptable()) {// 事件是否有效的
			client = server.accept();// 有客户端接入，把客户端连接通道也注册到多路复用器上
			client.configureBlocking(false);
			client.register(selector, SelectionKey.OP_READ);// 客户端连接过来注册读事件
		} else if (key.isValid() && key.isReadable()) {
			recvBuf.clear();// 清空缓冲区
			client = (SocketChannel) key.channel();// 通过key获取客户端，这个客户端是在Acceptable时连接过来的客户端
			int len = client.read(recvBuf);
			if (len > 0) {// 读到内容的时候
				String msg = new String(recvBuf.array(), 0, len, CHARSET);
				sessionMessage.put(key, msg);
				key.interestOps(SelectionKey.OP_WRITE);// 读完数据后，这个客户端在多路复用器上的事件有read变为write，可以写了
			} else {
				if (client.isConnected()) {
					System.out.println("客户端关闭");
					close(key);
				}
			}
		} else if (key.isValid() && key.isWritable()) {// 可写的,说明注册在多路复用上的客户端数据已经接收完了，可以往客户端写数据了
			if (!sessionMessage.containsKey(key)) {
				return;
			}
			// 获取到客户端发送过来的数据
			client = (SocketChannel) key.channel();
			ByteBuffer resp = handler(sessionMessage.get(key));// 将读取到的数据暴露给用户处理，得到响应数据
			respBuf.clear();
			respBuf.put(resp);
			respBuf.flip();
			client.write(respBuf);// 响应客户端
			key.interestOps(SelectionKey.OP_READ);// 写完数据后，这个客户端在多路复用器上的事件又由write变为read，可以读了
		} else {
			client = server.accept();// 有客户端接入,把客户端连接通道也注册到多路复用器上
			System.out.println("连接状态::" + client.isConnected());
		}
	}

	/**
	 * @Title: handler
	 * @Description:
	 * @param object
	 * @return
	 * @return: Object
	 */
	private static ByteBuffer handler(Object object) {
		System.out.println("server read:[" + object + "]");
		return ByteBuffer.wrap(("server write:[" + object + "]").getBytes(CHARSET));
	}

	/**
	 * @Title: close
	 * @Description:
	 * @param key
	 * @return: void
	 */
	private static void close(SelectionKey key) {
		key.cancel();// 取消注册
		SocketChannel client = (SocketChannel) key.channel();
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
