package com.shishu.utility.solr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SolrTimeUtil {
	private static final long HOURS8 = 1000 * 60 * 60 * 8;
	private static SimpleDateFormat solr_sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	/**
	 * 将solr时间转化为时间字符串
	 * @param date
	 * @return
	 */
	public static String getSolrTimeString(Date date){
		if(date == null){
			return null;
		}
		return sdf.format(date);
	}
	
	public static String getSolrTimeString(Date date, String dateFormat){
		if(date == null){
			return null;
		}
		SimpleDateFormat solrdf = new SimpleDateFormat(dateFormat);
		solrdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return solrdf.format(date);
	}
	
	/**
	 * 将solr时间转化为long
	 * @param date
	 * @return
	 */
	public static long getSolrTimeLong(Date date){
		return (date.getTime() - HOURS8);
	}
	
	/**
	 * 构造solr的时间过滤表达式
	 * @param preday 过滤条件，从preday天至今，0表示今天的起始时间
	 * @param field 要进行时间过滤的域名
	 * @return
	 */
	public static String getTimeFqByPre(int preday,String field){
		String start_time;
		if(preday == 0){
			start_time = "NOW+8HOURS/DAY";
		}else{
			start_time = "NOW+8HOURS".concat(String.valueOf(preday)).concat("DAYS/DAY");
		}
		return 	field.concat(":[").concat(start_time).concat(" TO NOW+8HOURS]");
		
	}
	
	/**
	 * 构造solr的时间过滤表达式
	 * @param start
	 * @param end
	 * @param field
	 * @return
	 */
	public static String getTimeFq(Date start, Date end, String field){
		if(start.getTime() >= end.getTime()){
			return null;
		}
		String start_time = solr_sdf.format(start);
		String end_time = solr_sdf.format(end);
		return field.concat(":[").concat(start_time).concat(" TO ").concat(end_time).concat("]");
	}
	
    public static void main(String[] args) throws ParseException{
    	System.out.println(((62362980873l)/(1000*60*60*24)));
    }

}
