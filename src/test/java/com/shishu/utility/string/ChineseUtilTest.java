package com.shishu.utility.string;

public class ChineseUtilTest {
	
	public static void main(String[] args) {
		String ll = "SSS中国人ddddd";
		ll = "ssssdddd!@#$%dddd";
		System.out.println(ChineseUtil.containsChinese(ll));
	}

}