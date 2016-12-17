package com.shishu.utility.ip;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shishu.utility.url.UrlUtil;

public class IPUtil {
	
	/**
	 * 获取ip对应的地址
	 * @param ip
	 * @return
	 */
	public static String ipToCity(String ip) {
		final String preurl = "http://api.map.baidu.com/location/ip?ak=DFbbaed951bb2145c655f7abb4c4417d&ip=";
		String url = preurl.concat(ip);
		String content;
		try {
			content = UrlUtil.getURLContents(url);
		} catch (IOException e) {
			return null;
		}
		if(content.indexOf("\"city\":") > -1){
			String city = content.substring(content.indexOf("\"city\":"),content.indexOf(",", content.indexOf("\"city\":")));
			city = city.substring(8, city.length()-1);
			return unicodeToString(city);
		}
		return null;
	}
	
	private static String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}
	
	public static boolean isIP(String address) {
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	
	}

	public static void main(String[] args) throws IOException {
		//System.out.println(ipToCity("115.238.28.42"));
		
		System.out.println(isIP("www.ifeng.com"));
	}
}
