package com.shishu.utility.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CronExpressionUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CronExpressionUtil.class);
	
	/**
	 * 
	 * @Description: 解析cron表达式，返回初始触发时间
	 * @param cronExpression
	 * @return 
	 * String
	 */
	public static String getFirstFireTime(String cronExpression) {

		try {
			CronExpression cron = new CronExpression(cronExpression);
			Date date = cron.getNextValidTimeAfter(new Date());
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			return dateFormat.format(date);
			
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
		return null;

	}
	
	public static long getFireTime(String cronExpression) {

		try {
			CronExpression cron = new CronExpression(cronExpression);
			Date date = cron.getNextValidTimeAfter(new Date());
			
			return date.getTime();
			
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
		return -1;

	}
	
	public static void main(String[] args) {
		
		String cron = "0 0 8 * * ?";
		
		System.out.println(CronExpressionUtil.getFirstFireTime(cron));
	}

}
