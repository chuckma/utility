package com.shishu.utility.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUrl {
	public static boolean isUrl(String urlString){
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
		Pattern patt = Pattern.compile(regex );
		Matcher matcher = patt.matcher(urlString);
		return matcher.matches();
	}
	
	public static void main(String args[]){
		String url = "http://cn.bing.com/news/search?q=%E6%B8%A9%E5%B7%9E+%E8%84%8F&p1=%5bNewsVertical+SortByDate%3d%221%22+Interval%3d%227%22%5d&first=1";
		System.out.println(isUrl(url));
	}
}
