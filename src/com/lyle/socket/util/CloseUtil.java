package com.lyle.socket.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @ClassName: CloseUtil
 * @Description: TODO
 * @author: Lyle
 * @date: 2017年3月29日 下午2:32:05
 */
public class CloseUtil {

	public static <T extends Closeable> void closeAll(T... io) {
		for (T temp : io) {
			if (null != temp) {
				try {
					temp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
