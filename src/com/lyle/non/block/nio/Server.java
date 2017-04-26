package com.lyle.non.block.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: Server
 * @Description:
 * @author: Lyle
 * @date: 2017年4月13日 下午2:28:16
 */
public class Server {

	private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();

	private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

	private void start() {
		final int DEFAULT_PORT = 8888;
		try {
			Selector selector = Selector.open();
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			if (serverSocketChannel.isOpen() && selector.isOpen()) {
				serverSocketChannel.configureBlocking(false);
				serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
				serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				System.out.println("Wating for connections ...");
				while (true) {
					selector.select();
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						keys.remove();
						if (!key.isValid()) {
							continue;
						}
						if (key.isAcceptable()) {
							acceptOP(key, selector);
						} else if (key.isReadable()) {
							this.readOP(key);
						} else if (key.isWritable()) {
							this.writeOP(key);
						}
					}
				}
			} else {
				System.out.println("The server socket channel or selector cannot be opend!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: acceptOP
	 * @Description: TODO
	 * @param key
	 * @param selector
	 * @return: void
	 */
	private void acceptOP(SelectionKey key, Selector selector) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);
		System.out.println("Incoming connection from:" + socketChannel.getRemoteAddress());
		socketChannel.write(ByteBuffer.wrap("[from server]Hello\n".getBytes()));
		keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	/**
	 * @Title: writeOP
	 * @Description: TODO
	 * @param key
	 * @return: void
	 */
	private void writeOP(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		Iterator<byte[]> its = channelData.iterator();
		while (its.hasNext()) {
			byte[] it = its.next();
			its.remove();
			socketChannel.write(ByteBuffer.wrap(it));
		}
		key.interestOps(SelectionKey.OP_READ);
	}

	/**
	 * @Title: readOP
	 * @Description: TODO
	 * @param key
	 * @return: void
	 */
	private void readOP(SelectionKey key) {
		try {
			SocketChannel socketChannel = (SocketChannel) key.channel();
			buffer.clear();
			int numRead = -1;
			try {
				numRead = socketChannel.read(buffer);
			} catch (IOException e) {
				System.out.println("Cannot read error!");
			}
			if (numRead == -1) {
				this.keepDataTrack.remove(socketChannel);
				System.out.println("connection closed by:" + socketChannel.getRemoteAddress());
				socketChannel.close();
				key.cancel();
				return;
			}
			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);
			System.out.println(new String(data, "UTF-8") + " from " + socketChannel.getRemoteAddress());
			doEchoJob(key, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: doEchoJob
	 * @Description: TODO
	 * @param key
	 * @param data
	 * @return: void
	 */
	private void doEchoJob(SelectionKey key, byte[] data) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		channelData.add(data);
		key.interestOps(SelectionKey.OP_WRITE);
	}

	/**
	 * @Title: acceptOP
	 * @Description: TODO
	 * @param key
	 * @return: void
	 */
	public static void main(String[] args) {
		Server s = new Server();
		s.start();
	}
}
