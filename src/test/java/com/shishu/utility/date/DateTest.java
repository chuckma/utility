package com.shishu.utility.date;

import java.util.Date;

public class DateTest {
	
	public static void main(String[] args) {
		//System.out.println(DateUtil.getFormattedDate("2016-01-21 17:21:00").getTime());
		
		long dd = DateUtil.getFormattedDate("2016-01-21 17:21").getTime();
		
		System.out.println(new Date(dd));
	}

}
