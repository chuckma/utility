package com.shishu.utility.excel;

import java.util.List;


/** 
 * 导出excel对象接口类
 * @author: yanghb
 * @since: 2009-6-5  下午03:18:49
 * @history:
 ************************************************
 * @file: ExcelObj.java
 * @Copyright: 2008 HundSun Electronics Co.,Ltd.
 * All right reserved.
 ************************************************/
public interface ExcelObj {
	public final String squenceTag = "[#]";
	
	public final String numberTypeTag = "[n]";
	/** 
	 * 提供excel表头
	 * @return 
	 * @create  2009-6-5 下午03:18:41 yanghb
	 * @history  
	 */
	public List<String> putHead();
	
	
	/** 
	 * 提供excel行数据
	 * @return 
	 * @create  2009-6-5 下午03:19:36 yanghb
	 * @history  
	 */
	public List<String> putRowData();
}
