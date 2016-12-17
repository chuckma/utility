package com.shishu.utility.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author lixiaoping
 * 
 * 日志工具类  slf4j + log4j
 *
 */
public class LogUtil {
	
	
	public static Logger getLogger(){
		return LoggerFactory.getLogger("com.shishu");
	}
	
	public static Logger getLogger(Class clazz) {
		return LoggerFactory.getLogger(clazz);
	}
	
	public static Logger getLogger(String name) {
		return LoggerFactory.getLogger(name);
	}

	
	

}
