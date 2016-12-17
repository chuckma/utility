package com.shishu.utility.url;

import java.net.MalformedURLException;

public class UrlUtilTest {
	
	public static void isTopURLTest() {
		String url = "http://blog.csdn.net/w20101114/article/details/8201474";
		url = "http://12212.com";
		//url = "http://stackoverflow.com/questions/24538991/java-regex-how-to-match-url-path";
		//url = "http://121.40.71.49:8090/datacentre";
		//url = "http://www.ifeng.com";
		//url = "ifeng.com";
		url = "http://ddddd";
		//url = "http://www.zjnu.edu.cn/cont/5_1.html";
		System.out.println(UrlUtil.isTopURL(url));
	}
	
	public static void domainNameTest() throws MalformedURLException {
		String url = "http://blog.csdn.net/w20101114/article/details/8201474";
		url = "http://ent.ifeng.com/";
		url = "http://mt0.haibao.cn/img/340_256_100_0/1442462421.5147/f180a40c83bcf5b7a3888cd1fffdea43.jpg";
		//url = "http://12212.com";
		//url = "http://stackoverflow.com/questions/24538991/java-regex-how-to-match-url-path";
		//url = "http://121.40.71.49:8090/datacentre";
		//url = "www.ifeng.com";
		//url = "ifeng.com";
		//url = "http://ddddd";
		url = "http://www.haibao.com/";
		url = "http://www.ifeng.com";
		
		System.out.println(UrlUtil.GetDomainName(url));
	}
	
	public static void isConnectTest() {
		String url = "http://blog.csdn.net/w20101114/article/details/8201474";
		url = "http://ent.ifeng.com/";
		url = "http://12212.com";
		url = "http://stackoverflow.com/questions/24538991/java-regex-how-to-match-url-path";
		url = "http://121.40.71.49:8090/datacentre";
		url = "http://www.ifeng.com";
		url = "http://ifeng.com";
		url = "http://ddddd";
		System.out.println(UrlUtil.isConnect(url));
	}
	
	public static void isValidationTest() {
		String[] excludeIPs = {"60.191.124.236","0:0:0:0:0:0:0:0"};
		String url = "http://blog.csdn.net/w20101114/article/details/8201474";
		url = "http://12212.com";
		//url = "http://stackoverflow.com/questions/24538991/java-regex-how-to-match-url-path";
		//url = "http://121.40.71.49:8090/datacentre";
		//url = "http://www.ifeng.com";
		//url = "http://ifeng.com";
		//url = "http://ddddd";
		//url = "http://toutiao.com/";
		//url = "https://www.tmall.com/";
		//url = "https://miao.tmall.com/?spm=3.7396704.20000005.d2.0L9BIR&abbucket=&acm=tt-1138874-37187.1003.8.74460&aldid=74460&abtest=&scm=1003.8.tt-1138874-37187.OTHER_1434846931363_74460&pos=2";
		//url = "http://sports.ifeng.com/";
		//url = "http://dfasdfasd.com";
		//url = "http://www.zjnu.edu.cn/cont/5_1.html";
		//url = "www.jsdatum.com";
		//url = "http://192.168.1.55:8090/datacentre";
		//url = "http://www.insigma.com.cn";
		//url = "http://www.zju.edu.cn";
		//url = "www.qq.com";
		//url = "www.gov.cn";
		//url = "http://www.sepb.gov.cn/fa/cms/shhj/index.htm";
		System.out.println(UrlUtil.isValidation(url, excludeIPs));
	}
	
	public static void reverseUrlTest() {
		String url = "http://roll.tech.sina.com.cn/s/channel.php?ch=05#col=30&spec=&type=&ch=05&k=&offset_page=0&offset_num=0&num=60&asc=&page=1";
		url = "http://roll.tech.sina.com.cn/s/channel.php?ch=05&col=30&spec=&type=&ch=05&k=&offset_page=0&offset_num=0&num=60&asc=&page=1";
		url = "http://img.yixieshi.com/09262040L-0.jpg!680";
		//url = "http://img2.donews.com/2015/1012/79957967.png.450.jpg";
		
		//url = "https://list.taobao.com/itemlist/default.htm?cat=51108009&viewIndex=1&as=0&spm=a2106.2206569.0.0.JfpsoM&atype=b&style=grid&same_info=1&isnew=2&tid=0&_input_charset=utf-8&s=0#oo#https://list.taobao.com/itemlist/default.htm?_input_charset=utf-8&json=on&atype=b&cat=51108009&style=grid&as=0&viewIndex=1&spm=a2106.2206569.0.0.JfpsoM&same_info=1&isnew=2&pSize=96&_ksTS=1449215819811_21&callback=jsonp22&s=0#oo#center";
		url = "http://weibo.com/u/3840731341?is_hot=1";
		System.out.println(UrlUtil.reverseUrl(url));
	}
	
	public static void unreverseUrlTest() {
		String url = "cn.com.sina.tech.roll:http/s/channel.php?ch=05&col=30&spec=&type=&ch=05&k=&offset_page=0&offset_num=0&num=60&asc=&page=1";
		url = "com.taobao.list:https/itemlist/default.htm?cat=51108009&viewIndex=1&as=0&spm=a2106.2206569.0.0.JfpsoM&atype=b&style=grid&same_info=1&isnew=2&tid=0&_input_charset=utf-8&s=0#oo#https://list.taobao.com/itemlist/default.htm?_input_charset=utf-8&json=on&atype=b&cat=51108009&style=grid&as=0&viewIndex=1&spm=a2106.2206569.0.0.JfpsoM&same_info=1&isnew=2&pSize=96&_ksTS=1449215819811_21&callback=jsonp22&s=0#oo#center";
		
		//url = "com.donews.img2:http/2015/1012/79957967.png.450.jpg";
		
		//url = "com.yixieshi.img:http/09262040L-0.jpg!680";
		
		System.out.println(UrlUtil.unreverseUrl(url));
	
	}
	
	public static void absUrlTest() {
		String url = "http://www.lagou.com";
		String rurl = "image2/M00/05/D4/CgqLKVX4C-6AJZ47AAAwXY0DbKA883.png";
		
		System.out.println(UrlUtil.absUrl(url, rurl));
	}
	
	public static void main(String[] args) throws MalformedURLException {
		//isValidationTest();
		//domainNameTest();
		
		//isTopURLTest();
		reverseUrlTest();
		
		//unreverseUrlTest();
		
		//absUrlTest();
		
		
		
		
		
	}

}
