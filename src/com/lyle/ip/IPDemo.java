package com.lyle.ip;

import java.net.InetAddress;

/**
 * @ClassName: IPDemo
 * @Description:IP地址<------->InetAddress
 * @author: Lyle
 * @date: 2017年4月9日 下午9:26:08
 */
public class IPDemo {

	public static void main(String[] args) throws Exception {
		// InetAddress ip = InetAddress.getLocalHost();
		// System.out.println(ip.getHostAddress());
		// =============================
		InetAddress ia = InetAddress.getByName("www.baidu.com");
		System.out.println(ia);
	}
}
