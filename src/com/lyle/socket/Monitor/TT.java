package com.lyle.socket.Monitor;

import com.lyle.socket.util.MonitorUtils;

/**
 * @ClassName: TT
 * @Description:
 * @author: Lyle
 * @date: 2017年4月7日 下午4:51:15
 */
public class TT {

	public static void main(String[] args) {
		byte[] buff = { (byte) 0x5D, 0x00, 0x00, 0x00 };
		System.out.println(MonitorUtils.dwordBytesToLong(buff));
		String s = "S0-E11-EV||保存模块6205|1#AL_虚拟设备|SetParam demo event|3|7|2009-12-02 09:55:12|";
		System.out.println(s.getBytes().length);
	}
}
