package com.lyle.socket.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @ClassName: HexUtils
 * @Description: 把十六进制字符，原模样写入二进制文件
 * @author: <link>http://www.iteye.com/problems/47760</link>
 * @date: 2017年4月7日 下午2:17:05
 */
public final class HexUtils {

	/**
	 * precomputed translate table for chars 0..'f'
	 */
	private static byte[] correspondingNibble = new byte['f' + 1];
	// -------------------------- PUBLIC STATIC METHODS --------------------------

	/**
	 * Convert a hex string to an unsigned byte array. Permits upper or lower case hex.
	 * @param s String must have even number of characters. and be formed only of digits 0-9 A-F or
	 *            a-f. No spaces, minus or plus signs.
	 * @return corresponding unsigned byte array. see http://mindprod.com/jgloss/unsigned.html
	 */
	public static byte[] fromHexString(String s) {
		int stringLength = s.length();
		if ((stringLength & 0x1) != 0) {
			throw new IllegalArgumentException("fromHexString requires an even number of hex characters");
		}
		byte[] bytes = new byte[stringLength / 2];
		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			// You can store either unsigned 0..255 or signed -128..127 bytes in a byte type.
			bytes[j] = (byte) ((high << 4) | low);
		}
		return bytes;
	}

	// -------------------------- STATIC METHODS --------------------------
	static {
		// only 0..9 A..F a..f have meaning. rest are errors.
		for (int i = 0; i <= 'f'; i++) {
			correspondingNibble[i] = -1;
		}
		for (int i = '0'; i <= '9'; i++) {
			correspondingNibble[i] = (byte) (i - '0');
		}
		for (int i = 'A'; i <= 'F'; i++) {
			correspondingNibble[i] = (byte) (i - 'A' + 10);
		}
		for (int i = 'a'; i <= 'f'; i++) {
			correspondingNibble[i] = (byte) (i - 'a' + 10);
		}
	}

	/**
	 * convert a single char to corresponding nibble using a precalculated array. Based on code by:
	 * Brian Marquis Orion Group Software Engineers http://www.ogse.com
	 * @param c char to convert. must be 0-9 a-f A-F, no spaces, plus or minus signs.
	 * @return corresponding integer 0..15
	 * @throws IllegalArgumentException on invalid c.
	 */
	private static int charToNibble(char c) {
		if (c > 'f') {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
		int nibble = correspondingNibble[c];
		if (nibble < 0) {
			throw new IllegalArgumentException("Invalid hex character: " + c + " <--> " + (int) c);
		}
		return nibble;
	}

	/**
	 * code not used, for explanation only. convert a single char to corresponding nibble(半个字节).
	 * Slow version, easier to understand.
	 * @param c char to convert. must be 0-9 a-f A-F, no spaces, plus or minus signs.
	 * @return corresponding integer
	 */
	@SuppressWarnings("unused")
	private static int slowCharToNibble(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 0xa;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 0xa;
		} else {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
	}

	// --------------------------- main() method ---------------------------
	/**
	 * Test harness
	 * @param args not used
	 */
	public static void main(String[] args) {
		try {
			// BufferedReader br = new BufferedReader(new
			// FileReader("C:/Users/swere/Desktop/jk/str.txt"));
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream("C:/Users/swere/Desktop/str.txt"), "GB2312"));
			OutputStream os = new FileOutputStream("C:/Users/swere/Desktop/io.txt");
			String line = null;
			while ((line = br.readLine()) != null) {
				byte[] bytes = fromHexString(line);
				os.write(bytes);
				os.flush();
			}
			os.close();
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
