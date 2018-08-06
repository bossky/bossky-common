package org.bossky.common;

import org.bossky.common.util.Misc;

/**
 * id生成者
 * 
 * @author daibo
 *
 */
public class IdGenerater {
	/** 16进制的服务器id */
	private static String HEX_SERVER;

	static {
		String v = System.getProperty("bossky.server");
		HEX_SERVER = Misc.toHex(Misc.toInt(v));
	}

	private IdGenerater() {

	}

	public static String genId() {
		return Timestamp.nextHex() + HEX_SERVER;
	}

	public static String genId(String prefix) {
		return prefix + Timestamp.nextHex() + HEX_SERVER;
	}
}
