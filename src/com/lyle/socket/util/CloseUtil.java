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

	@SafeVarargs
	public static <T extends Closeable> void closeAll(T... stream) {
		for (T temp : stream) {
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
