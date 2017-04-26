package com.lyle.non.block.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @ClassName: Client
 * @Description:
 * @author: Lyle
 * @date: 2017年4月13日 下午2:28:25
 */
public class Client {

	public static void main(String[] args) {
		final int DEFAULT_PORT = 8888;
		final String IP = "localhost";
		ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
		ByteBuffer randomBuffer;
		CharBuffer charBuffer;
		Charset charset = Charset.defaultCharset();
		CharsetDecoder decoder = charset.newDecoder();
		try {
			Selector selector = Selector.open();
			SocketChannel socketChannel = SocketChannel.open();
			if (socketChannel.isOpen() && selector.isOpen()) {
				socketChannel.configureBlocking(false);
				socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
				socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
				socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
				socketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT));
				System.out.println("Localhost: " + socketChannel.getRemoteAddress());
				while (selector.select(1000) > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> its = keys.iterator();
					while (its.hasNext()) {
						SelectionKey key = its.next();
						its.remove();
						try {
							SocketChannel keySocketChannel = (SocketChannel) key.channel();
							if (key.isConnectable()) {
								System.out.println("I am connected!");
								if (keySocketChannel.isConnectionPending()) {
									keySocketChannel.finishConnect();
								}
								while (keySocketChannel.read(buffer) != -1) {
									buffer.flip();
									charBuffer = decoder.decode(buffer);
									System.out.println(charBuffer.toString());
									if (buffer.hasRemaining()) {
										buffer.compact();
									} else {
										buffer.clear();
									}
									int r = new Random().nextInt(100);
									if (r == 50) {
										System.out.println("50 was generated! close the socket channel");
										break;
									} else {
										randomBuffer = ByteBuffer.wrap("[from client]Random number:"
												.concat(String.valueOf(r)).getBytes("UTF-8"));
										keySocketChannel.write(randomBuffer);
										try {
											Thread.sleep(1500);
										} catch (InterruptedException e) {
											//
										}
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				System.out.println("The socket channel or selector cannot be opened!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
