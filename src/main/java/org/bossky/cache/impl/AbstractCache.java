package org.bossky.cache.impl;

import org.bossky.cache.Cache;

/**
 * 抽象缓存类
 * 
 * @author bo
 *
 */
public abstract class AbstractCache implements Cache {

	@Override
	public Object get(Object key) {
		return get(key, null);
	}

	@Override
	public Object put(Object key, Object value) {
		return put(key, value, null);
	}

	@Override
	public Object putIfAbsent(Object key, Object value) {
		return putIfAbsent(key, value, null);
	}
}
