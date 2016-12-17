package com.shishu.utility.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CronTest {

	public static void main(String[] args) {
		String cron = "0 0/5 * * * ?";
		cron = "0 0/5 13 * * ?";
		cron = "0 30 10-12 * * ?";
		// cron = "0 0/30 8-10 5,20 * ?";
		// cron = "0 0,4,50 * 1,9 * ? *";
		// cron = "* 45 12,23 * * ?";
		cron = "0 0 8,15,22 * * ?";

		System.out.println(CronExpressionUtil.getFirstFireTime(cron));

		System.out.println(CronExpressionUtil.getFireTime(cron));

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date date = new Date(CronExpressionUtil.getFireTime(cron));
		
		System.out.println(dateFormat.format(date));

	}

}
