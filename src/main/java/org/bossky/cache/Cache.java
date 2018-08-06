package org.bossky.cache;

/**
 * 缓存
 * 
 * @author daibo
 *
 */
public interface Cache {
	/**
	 * 获取对象
	 * 
	 * @param key
	 * @param T
	 * @return
	 */
	public Object get(Object key);

	/**
	 * 获取对象
	 * 
	 * @param key
	 * @param T
	 * @return
	 */
	public <T> T get(Object key, Class<T> type);

	/**
	 * 如果对象不存在，则存入，如果存在，刚返回已有对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object putIfAbsent(Object key, Object value);

	/**
	 * 如果对象不存在，则存入，如果存在，刚返回已有对象
	 * 
	 * @param key
	 * @param value
	 * @param type
	 * @return
	 */
	public <T> T putIfAbsent(Object key, Object value, Class<T> type);

	/**
	 * 存入对象，对象已存在则覆盖
	 * 
	 * @param key
	 * @param value
	 * @return 
	 */
	Object put(Object key, Object value);

	/**
	 * 存入对象，对象已存在则覆盖
	 * 
	 * @param key
	 * @param value
	 * @param type
	 * @return
	 */
	<T> T put(Object key, Object value, Class<T> type);

	/**
	 * 过期对象
	 * 
	 * @param key
	 */
	void evict(Object key);

	/**
	 * 清理缓存
	 */
	void clear();

}
