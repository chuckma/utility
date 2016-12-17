package com.shishu.utility.solr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolrTransform {
	/**
	 * 将solr保留符号转义
	 * 
	 * @param input
	 * @return
	 */
	public static String transformSolrMetacharactor(String input) {
		StringBuffer sb = new StringBuffer();
		String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matcher.appendReplacement(sb, "\\\\" + matcher.group());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 将solr中以list表示的map转化为map
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, String> solrListToMap(ArrayList<String> list) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (String s : list) {
			String[] item = s.split(",");
			map.put(item[0], item[1]);
		}
		return map;
	}	
}
