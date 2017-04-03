package com.lyle.socket.uploadPictrue.concurrent;

/**
 * @ClassName: WhileTest
 * @Description:
 * @author: Lyle
 * @date: 2017年4月3日 下午11:42:49
 */
public class WhileTest {

	public static void main(String[] args) {
		// testIf("Object");
		testWhile();
	}

	public static void testIf(String type) {
		if (type.equals("Object"))
			System.out.println("相等");
		System.out.println("come in");
	}

	public static void testWhile() {
		int a = 1;
		while (a < 10)
			System.out.println("a:" + a++);
	}
}
