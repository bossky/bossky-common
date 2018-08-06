package org.bossky.common;

/**
 * 转换器
 * 
 * @author bossky
 *
 * @param <S>
 *            源对象
 * @param <T>
 *            转换来的对象
 */
public interface Converter<S, T> {
	/**
	 * 将S 转换成T
	 * 
	 * @param s
	 * @return
	 */
	public T to(S s);

	/**
	 * 将T还原成S
	 * 
	 * @param t
	 * @return
	 */
	public S from(T t);
}
