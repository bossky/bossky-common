package org.bossky.counter;

/**
 * 计数器集全
 * 
 * @author bo
 *
 */
public interface Counters {
	/**
	 * 打开一个计数器
	 * 
	 * @param name
	 * @return
	 */
	public Counter openCounter(String name);
}
