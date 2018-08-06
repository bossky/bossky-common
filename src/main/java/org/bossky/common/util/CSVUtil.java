package org.bossky.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bossky.common.Converter;
import org.bossky.common.support.TranformList;

/**
 * CSV工具类
 * 
 * CSV 进行中的标准化<a href=
 * "https://zh.wikipedia.org/wiki/%E9%80%97%E5%8F%B7%E5%88%86%E9%9A%94%E5%80%BC">参考</a>
 * 
 * <pre>
 * “CSV”格式中大量变体的存在说明并没有一个“CSV标准”。[5][6]在常见用法中，几乎任何定界符分隔的文本数据都可以被统称为“CSV”文件。不同的CSV格式可能不会兼容。
 *不过，RFC 4180是一个将CSV正式化的努力。它定义了互联网媒体类型“text/csv”，并且采用它的规则的CSV文件将会具有广泛的可移植性。它有如下要求：
 *以（CR/LF）字符结束的DOS风格的行（最后一行可选）。
 *一条可选的表头记录（没有可靠的方式来检测它是否存在，所以导入时必须谨慎）。
 *每条记录“应当”包含同样数量的逗号分隔字段。
 *任何字段都可以被包裹（用双引号）。
 *包含换行符、双引号和/或逗号的字段应当被包裹。（否则，文件很可能不能被正确处理）。
 *字段中的一个（双）引号字符必须被表示为两个（双）引号字符。
 *这个格式很简单，可以被大部分声称可以读取CSV文件的程序处理。例外是（a）程序可以不支持在被包裹的字段中换行，（b）程序可以将可选的表头当作数据，或者将第一个数据行当作可选的表头。
 * </pre>
 * 
 * @author bossky
 *
 */
public class CSVUtil {
	/** 包裹符 */
	private static char WRAP_CHAR = '"';
	/** 分隔符 */
	private static char SPLIT_CHAR = ',';
	/** 需要包裹的字符 */
	private static char[] NEED_WRAP = { '\n', '/', WRAP_CHAR, SPLIT_CHAR };

	public static void main(String[] args) {
		List<String> arr = Arrays.asList("HelloWorld", "Hello,World", "Hello\"World\"", null, "null", "", "end", ",,,");
		System.out.println(arr);
		System.out.println(arr.size());
		System.out.println(arr.get(3) == null);
		System.out.println(arr.get(4) == null);
		String v = to(arr);
		System.out.println(v);
		List<String> list = from(v);
		System.out.println(list);
		System.out.println(list.size());
		System.out.println(list.get(3) == null);
		System.out.println(list.get(4) == null);
	}

	/**
	 * 转换成CSV的一列
	 * 
	 * @param values
	 * @return
	 */
	public static String to(List<String> values) {
		if (values.isEmpty()) {
			return "";
		}
		if (values.size() == 1) {
			return toValue(values.get(0));
		}
		StringBuilder sb = new StringBuilder();
		sb.append(toValue(values.get(0)));
		for (int i = 1; i < values.size(); i++) {
			sb.append(SPLIT_CHAR);
			sb.append(toValue(values.get(i)));
		}
		return sb.toString();
	}

	/**
	 * 转换成CSV值
	 * 
	 * @param value
	 * @return 返回处理后的字符串。 <br/>
	 *         特殊约定
	 *         <li>空串返回""</li>
	 *         <li>空返回null字符串</li>
	 *         <li>null字符串加上包裹符，即 {@link #WRAP_CHAR}+null+{@link #WRAP_CHAR}
	 *         </li>
	 */
	public static String toValue(String value) {
		if (null == value) {
			return "null";
		}
		if (value.length() == 0) {
			return "";
		}
		if ("null".equals(value)) {
			return WRAP_CHAR + "null" + WRAP_CHAR;
		}
		boolean needEscape = false;// 需要转义?
		boolean needWrap = false;// 需要包裹?
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch == WRAP_CHAR) {
				needEscape = true;// 最糟糕的情况，要转义和包裹
				break;
			}
			if (isNeedWrap(ch)) {
				needWrap = true;// 需要包裹，还要看看需不需要转义
			}
		}
		if (needEscape) {
			StringBuilder sb = new StringBuilder();
			sb.append(WRAP_CHAR);
			for (int i = 0; i < value.length(); i++) {
				char ch = value.charAt(i);
				if (ch == WRAP_CHAR) {
					sb.append(WRAP_CHAR);// 增加一个WRAP_CHAR转义
				}
				sb.append(ch);
			}
			sb.append(WRAP_CHAR);
			return sb.toString();
		}
		if (needWrap) {
			return WRAP_CHAR + value + WRAP_CHAR;
		}
		return value;
	}

	/**
	 * 将CSV转换成字符串列表
	 * 
	 * @param value
	 * @return
	 */
	public static List<String> from(String value) {
		if (null == value) {
			return null;
		}
		if (value.length() == 0) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean notWrap = true;
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (notWrap) {
				if (ch == SPLIT_CHAR) {
					String result = sb.toString();// 分隔了
					if ("null".equals(result)) {
						list.add(null);
					} else {
						list.add(result);
					}
					sb = new StringBuilder();
					continue;
				}
				if (ch == WRAP_CHAR) {
					notWrap = false;// 进入包裹
					continue;
				}
				sb.append(ch);// 添加普通字符
				continue;
			}
			// 在包裹中的
			if (ch == WRAP_CHAR) {
				if ((i + 1) == value.length()) {
					// 结束了
					list.add(sb.toString());
					sb = null;
					break;
				}
				char next = value.charAt(i + 1);
				if (next == WRAP_CHAR) {
					i++;// 转义符,跳过一个
				} else if (next == SPLIT_CHAR) {
					i++;// 跳过分隔符
					notWrap = true;// 退出包裹了
					String result = sb.toString();
					list.add(result);// 分隔了
					sb = new StringBuilder();
					continue;
				} else {
					notWrap = true;// 退出包裹了
					continue;
				}
			}
			sb.append(ch);
		}
		if (null != sb) {
			String result = sb.toString();
			if ("null".equals(result)) {
				list.add(null);
			} else {
				list.add(result);
			}
		}
		return list;
	}

	/* 是否需要包裹 */
	private static boolean isNeedWrap(char ch) {
		for (char c : NEED_WRAP) {
			if (c == ch) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数字列表转换成CSV字符串
	 * 
	 * @author bossky
	 *
	 */
	public static class NumberListToStringCSV implements Converter<List<Number>, String> {

		@Override
		public String to(List<Number> s) {
			if (null == s) {
				return null;
			}
			if (s.isEmpty()) {
				return "";
			}
			if (s.size() == 1) {
				return String.valueOf(s.get(0));
			}
			StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(s.get(0)));
			for (int i = 1; i < s.size(); i++) {
				sb.append(CSVUtil.SPLIT_CHAR);
				sb.append(String.valueOf(s.get(i)));
			}
			return sb.toString();
		}

		@Override
		public List<Number> from(String t) {
			if (null == t) {
				return null;
			}
			if (t.length() == 0) {
				return Collections.emptyList();
			}
			List<String> list = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			boolean isDouble = false;
			for (int i = 0; i < t.length(); i++) {
				char ch = t.charAt(i);
				if (ch == SPLIT_CHAR) {
					list.add(sb.toString());
					sb = new StringBuilder();
					continue;
				}
				if (ch == '.') {
					isDouble = true;
				}
				sb.append(ch);
			}
			list.add(sb.toString());
			sb = null;// for gc
			if (isDouble) {
				return new TranformList<Number, String>(list) {

					@Override
					public Number tranform(String v) {
						return Double.parseDouble(v);
					}
				};
			}
			return new TranformList<Number, String>(list) {

				@Override
				public Number tranform(String v) {
					if ("null".equals(v)) {
						return null;
					}
					return Long.parseLong(v);
				}
			};
		}

	}

	/**
	 * 将字符串列表转换成字符串
	 * 
	 * @author bossky
	 *
	 */
	public static class StringListToStringCSV implements Converter<List<String>, String> {

		@Override
		public String to(List<String> s) {
			return CSVUtil.to(s);
		}

		@Override
		public List<String> from(String t) {
			return CSVUtil.from(t);
		}

	}

}
