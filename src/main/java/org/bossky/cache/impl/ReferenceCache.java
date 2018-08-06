package org.bossky.cache.impl;

import java.lang.ref.Reference;
import java.util.HashMap;

import org.bossky.cache.Cache;

/**
 * 引用缓存
 * 
 * @author daibo
 *
 */
public abstract class ReferenceCache extends AbstractCache implements Cache {

	protected volatile HashMap<Object, Reference<Object>> store;

	public ReferenceCache() {
		this(100);
	}

	public ReferenceCache(int init) {
		store = new HashMap<>(init);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		Reference<Object> v = store.get(key);
		return safeConversion(v, type);
	}

	@Override
	public <T> T put(Object key, Object value, Class<T> type) {
		return safeConversion(this.store.put(key, createReference(value)), type);
	}

	@Override
	public <T> T putIfAbsent(Object key, Object value, Class<T> type) {
		T v = safeConversion(store.get(key), type);
		if (null == v) {
			synchronized (store) {
				v = safeConversion(store.get(key), type);
				if (null == v) {
					store.put(key, createReference(value));
				}
			}
		}
		return null;
	}

	@Override
	public void evict(Object key) {
		synchronized (store) {
			this.store.remove(key);
		}
	}

	@Override
	public void clear() {
		synchronized (store) {
			this.store.clear();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T safeConversion(Reference<Object> ref, Class<T> type) {
		if (null == ref) {
			return null;
		}
		Object value = ref.get();
		if (null != value && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return (T) value;
	}

	/**
	 * 创建引用
	 * 
	 * @param value
	 * @return
	 */
	protected abstract Reference<Object> createReference(Object value);
}
