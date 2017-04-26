package com.lyle.socket.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: DateUtils
 * @Description: TODO
 * @author: Lyle
 * @date: 2017年4月25日 下午8:28:30
 */
public class DateUtils {

	public static Date str2Date(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
