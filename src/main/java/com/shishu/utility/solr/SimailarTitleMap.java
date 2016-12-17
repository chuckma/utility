package com.shishu.utility.solr;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimailarTitleMap {
	/**
	 * 为相似标题去重
	 */
	
	private float SIMAILAR = 0.6f;
	private Map<String, Object> titleMap;
	
	public SimailarTitleMap(){
		titleMap = new LinkedHashMap<String, Object>();
	}
	
	/**
	 * 判断容器中是否包含与key类似的标题
	 * @param key
	 * @return
	 */
	public boolean containsSimilarKey(String key){
		if(titleMap.containsKey(key))
			return true;
		else{
			for(String mapkey:titleMap.keySet())
				if(Similarity.compare(mapkey, key) > SIMAILAR) return true;
			return false;
		}
	}
	
	/**
	 * 获取容器中与key类似的标题
	 * @param key
	 * @return null：没有找到类似的标题
	 */
	public String getSimilarKey(String key){
		if(titleMap.containsKey(key))
			return key;
		else{
			for(String similarkey:titleMap.keySet())
				if(Similarity.compare(similarkey, key) > SIMAILAR) return similarkey;
			return null;
		}
	}
	
	public Object get(String key){
		return titleMap.get(key);
	}
	
	public void put(String key,Object value){
		titleMap.put(key, value);
	}
	
	public Map<String, Object> getTitleMap(){
		return titleMap;
	}

}

