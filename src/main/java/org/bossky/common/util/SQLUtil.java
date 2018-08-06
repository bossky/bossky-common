package org.bossky.common.util;

/**
 * sql工具类
 * 
 * @author daibo
 *
 */
public class SQLUtil {
	/**
	 * 转义对象
	 * 
	 * @param val
	 * @return
	 */
	public static String escape(Object obj) {
		if (null == obj) {
			return "";
		}
		return escape(obj.toString());
	}

	/** 是否转义utf-16的字符 */
	private static boolean ESCAPE_UTF_16 = Misc.eq("true", System.getProperty("org.bossky.escape_utf_16"));

	/**
	 * 转义字符串
	 * 
	 * @param val
	 * @return
	 */
	public static String escape(String val) {
		if (null == val) {
			return "";
		}
		StringBuilder sb = new StringBuilder(val.length());
		for (int i = 0; i < val.length(); i++) {
			char ch = val.charAt(i);
			if ('\\' == ch) {
				sb.append("\\\\");
			} else if ('\'' == ch) {
				sb.append("''");
			}
			// 注意：下面这两段是代理区。即第1——16平面的间接表示，四个字节的汉字就在这里表示
			// D800-DBFF：High-half zone of UTF-16
			// DC00-DFFF：Low-half zone of UTF-16
			if (ESCAPE_UTF_16 && ch >= 0xD800 && ch <= 0xDFFF) {
				sb.append(" ");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

}