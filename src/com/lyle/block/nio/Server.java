package com.lyle.block.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @ClassName: Server
 * @author: Lyle
 * @date: 2017年4月13日 下午1:52:46
 */
public class Server {

	public static void main(String[] args) {
		final int DEFAULT_PORT = 8888;
		final String IP = "127.0.0.1";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		//
		try {
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			// continue if it was successfully created
			if (serverChannel.isOpen()) {
				// set the blocking mode
				serverChannel.configureBlocking(true);
				// set some options
				serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				// bind the server socket channel to local address
				serverChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
				// display a waiting message while ... waiting clients
				System.out.println("Waiting for connections ...");
				// wait for incoming connections
				while (true) {
					try (SocketChannel socketChannel = serverChannel.accept()) {
						System.out.println("Incoming connection from: " + socketChannel.getRemoteAddress());
						// transmitting data
						while (socketChannel.read(buffer) != -1) {
							buffer.flip();
							socketChannel.write(buffer);
							if (buffer.hasRemaining()) {
								buffer.compact();
							} else {
								buffer.clear();
							}
						}
					} catch (IOException ex) {
					}
				}
			} else {
				System.out.println("The server socket channel cannot be opened!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
