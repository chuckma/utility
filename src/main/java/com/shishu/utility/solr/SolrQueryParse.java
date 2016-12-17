package com.shishu.utility.solr;
/**
 * 
 * @author wgh
 *
 */
public class SolrQueryParse {
	/**
	 * 将筛选字符串解析为solr可接受的语法
	 * @param keywords 用'+'表示包含关系，用'-'表示不包含关系
	 * @return
	 */
	public static String parse(String keywords){
		keywords  = "(\"" + keywords + "\")";
		if(keywords.indexOf("+") > 0 || keywords.indexOf("-") > 0){
			keywords = keywords.replaceAll("\\+", "\" AND \"");
			keywords = keywords.replaceAll("\\-", "\" NOT \"");
		}
		return keywords;
	}
	
	public static String parse(String[] keywords_arr){
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for(int i=0,l=keywords_arr.length;i<l;i++){
			String keywords = keywords_arr[i];
			keywords  = "(\"" + keywords + "\")";
			if(keywords.indexOf("+") > 0 || keywords.indexOf("-") > 0){
				keywords = keywords.replaceAll("\\+", "\" AND \"");
				keywords = keywords.replaceAll("\\-", "\" NOT \"");
			}
			sb.append(keywords);
			if(i+1 < l){
			    sb.append(" OR ");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(SolrQueryParse.parse("浙江+科技-互联网"));
		System.out.println(SolrQueryParse.parse("浙江+科技-互联网,杭州+环保-科技-政府".split(",")));
	}
}
