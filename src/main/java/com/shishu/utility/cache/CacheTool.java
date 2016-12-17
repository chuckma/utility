package com.shishu.utility.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 
 * 缓存工具
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:39:24
 */
public class CacheTool {
	int cacheSize = 0;

	static final int defaultSize = 10;

	float loadFactor = 0.75f; // default

	LinkedHashMap map;

	private static CacheTool cache = null;

	private CacheTool(int cacheSize) {
		this.cacheSize = cacheSize;
		map = new LinkedHashMap(cacheSize, loadFactor, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry eldest) {
				return size() > CacheTool.this.cacheSize;
				// return false;
			}
		};
	}

	public static CacheTool getInstance() {
		if (cache == null) {
			cache = new CacheTool(defaultSize);
		}
		return cache;
	}

	public static CacheTool getInstance(int cacheSize) {
		if (cache == null) {
			cache = new CacheTool(cacheSize);
		}
		return cache;
	}

	public void setSize() {

	}

	public synchronized void clear() {
		map.clear();
	}

	public synchronized Object get(Object key) {
		return map.get(key);
	}

	public synchronized void put(Object key, Object value) {
		map.put(key, value);
	}

	public synchronized Object remove(Object key) {
		return map.remove(key);
	}

	public synchronized int size() {
		return map.size();
	}

	public synchronized Collection values() {
		return map.values();
	}

}
