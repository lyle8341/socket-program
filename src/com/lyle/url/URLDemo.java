package com.lyle.url;

import java.net.URL;

/**
 * @ClassName: URLDemo
 * @Description:
 * @author: Lyle
 * @date: 2017年4月4日 下午9:20:28
 */
public class URLDemo {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost/exercise-dev/date.htm?name=lyle");
		System.out.println("协议：-->" + url.getProtocol());
		System.out.println("host:-->" + url.getHost());
		System.out.println("port:-->" + url.getPort());
		System.out.println("file:-->" + url.getFile());
		System.out.println("path:-->" + url.getPath());
		System.out.println("query:-->" + url.getQuery());
		/**
		 * 协议：-->http<br>
		 * host:-->localhost<br>
		 * port:-->-1 =====>默认80端口<br>
		 * file:-->/exercise-dev/date.htm?name=lyle<br>
		 * path:-->/exercise-dev/date.htm <br>
		 * query:-->name=lyle<br>
		 */
	}
}
