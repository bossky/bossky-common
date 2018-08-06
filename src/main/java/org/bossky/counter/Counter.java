package org.bossky.counter;

/**
 * 计数器
 * 
 * @author bo
 *
 */
public interface Counter {
	/**
	 * 获取值
	 * 
	 * @param key
	 * @return
	 */
	public long get(String key);

	/**
	 * 设置值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long set(String key, long value);

	/**
	 * 累加1
	 * 
	 * @param key
	 * @return
	 */
	public long inc(String key);

	/**
	 * 累加step
	 * 
	 * @param key
	 * @param step
	 * @return
	 */
	public long inc(String key, long step);

	/**
	 * 累减1
	 * 
	 * @param key
	 * @return
	 */
	public long dec(String key);

	/**
	 * 累减step
	 * 
	 * @param key
	 * @param step
	 * @return
	 */
	public long dec(String key, long step);

}
