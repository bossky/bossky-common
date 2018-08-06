package org.bossky.cache.impl;

import java.util.WeakHashMap;

import org.bossky.cache.Cache;

/**
 * 基于WeakHashMap的缓存
 * 
 * @author bo
 *
 */
public class WeakHashMapCache implements Cache {

	WeakHashMap<Object, Object> data;

	public WeakHashMapCache() {
		this(100);
	}

	public WeakHashMapCache(int init) {
		data = new WeakHashMap<>(init);
	}

	@Override
	public Object get(Object key) {
		return get(key, null);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		Object value = data.get(key);
		return safeConversion(value, type);
	}

	@Override
	public Object put(Object key, Object value) {
		return put(key, value, null);
	}

	@Override
	public <T> T put(Object key, Object value, Class<T> type) {
		return safeConversion(data.put(wrapKey(key), value), type);
	}

	@Override
	public Object putIfAbsent(Object key, Object value) {
		return putIfAbsent(key, value, null);
	}

	@Override
	public <T> T putIfAbsent(Object key, Object value, Class<T> type) {
		return safeConversion(data.putIfAbsent(wrapKey(key), value), type);
	}

	@Override
	public void evict(Object key) {
		data.remove(key);
	}

	@Override
	public void clear() {
		data.clear();
	}

	/**
	 * 包装key
	 * 
	 * @param key
	 * @return
	 */
	private Object wrapKey(Object key) {
		if (key instanceof String) {
			return new String((String) key);
		} else if (key instanceof Long) {
			return new Long((Long) key);
		} else if (key instanceof Integer) {
			return new Integer((Integer) key);
		} else if (key instanceof Short) {
			return new Short((Short) key);
		} else if (key instanceof Double) {
			return new Double((Double) key);
		} else if (key instanceof Float) {
			return new Float((Float) key);
		} else if (key instanceof Boolean) {
			return new Boolean((Boolean) key);
		} else if (key instanceof Byte) {
			return new Byte((Byte) key);
		} else if (key instanceof Character) {
			return new Character((Character) key);
		}
		return key;
	}

	@SuppressWarnings("unchecked")
	private <T> T safeConversion(Object value, Class<T> type) {
		if (null != value && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return (T) value;
	}

	@Override
	public String toString() {
		return data.toString();
	}

	interface ReferenceWithKey {
		public Object getKey();

		public Object getValue();
	}

	public static void main(String[] args) {
		WeakHashMapCache cache = new WeakHashMapCache(100);
		cache.put("Hello1", "Hello1");
		Object v = cache.get("Hello1");
		System.out.println(v);
		System.gc();
		System.runFinalization();
		System.out.println(cache.get("Hello1"));
		System.out.println(v);
	}
}
