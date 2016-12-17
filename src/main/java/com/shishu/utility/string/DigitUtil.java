package com.shishu.utility.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitUtil {
	
	private static String regex="[0-9]+?";
	private static Pattern p=Pattern.compile(regex);
	
	
	public static int getNumber(String str,int index) {
		String dd = str.replaceAll("\\D", "_");
		String[] as = dd.split("_+");
        List<Integer> list = new ArrayList<Integer>();
        for(String aa : as) {
        	if(aa.trim().length() > 0) {
        		list.add(Integer.parseInt(aa.trim()));
        	}
        }
        if(list.size() > 0) {
        	return list.get(index);
        } else {
        	return 0;
        }
        
	}
	
	public static boolean isConvertNumber(String str, String[] numbers) {
		boolean flag = false;
		if(numbers == null) {
			return flag;
		}
		Matcher m=p.matcher(str);
		for(String num : numbers) {
			if(m.find() && str.contains(num)) {
				flag = true;
				break;
			}
		}
		return flag;
		
	}

}
