package com.lyle.url;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ClassName: URLDemo2
 * @Description:
 * @author: Lyle
 * @date: 2017年4月4日 下午9:20:28
 */
public class URLDemo2 {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:8080/exercise-dev/date.htm");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while ((len = in.read(buff)) != -1) {
			System.out.println(new String(buff, 0, len));
		}
		in.close();
	}
}
