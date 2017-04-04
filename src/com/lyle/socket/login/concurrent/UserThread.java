package com.lyle.socket.login.concurrent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName: UserThread
 * @Description:
 * @author: Lyle
 * @date: 2017年4月4日 下午12:40:16
 */
public class UserThread implements Runnable {

	private Socket client;

	public UserThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		String ip = client.getInetAddress().getHostAddress();
		System.out.println(ip + "....connected");
		try {
			for (int x = 0; x < 3; x++) {
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String name = br.readLine();
				if (name == null)
					break;
				BufferedReader brFile = new BufferedReader(
						new FileReader("src/com/lyle/socket/login/concurrent/users.db"));
				PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
				String sql = null;
				boolean flag = false;
				while ((sql = brFile.readLine()) != null) {
					if (sql.equals(name)) {
						flag = true;
						break;
					}
				}
				if (flag) {
					System.out.println(name + "，已登录");
					pw.println(name + "，欢迎光临");
					break;
				} else {
					System.out.println(name + "，尝试登录");
					pw.println(name + "，用户名不存在");
				}
				brFile.close();
			}
			client.close();
		} catch (Exception e) {
			throw new RuntimeException("校验失败");
		}
	}
}
