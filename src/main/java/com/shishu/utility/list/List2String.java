package com.shishu.utility.list;

import java.util.List;

public class List2String {

	public static String joinList(List<String> list, String join_str){
		if(list == null || list.size() == 0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0,l=list.size();i<l;i++){
			sb.append(list.get(i));
			if(i+1<l){
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
