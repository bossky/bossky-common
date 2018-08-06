package org.bossky.cache.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import org.bossky.cache.Cache;

/**
 * 弱引用缓存
 * 
 * @author daibo
 *
 */
public class WeakReferenceCache extends ReferenceCache implements Cache {

	public WeakReferenceCache() {
		super();
	}

	public WeakReferenceCache(int init) {
		super(init);
	}

	@Override
	protected Reference<Object> createReference(Object userValue) {
		return new WeakReference<Object>(userValue);
	}

}
