package com.shishu.utility.url;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.shishu.utility.domain.DomainSuffixes;
import com.shishu.utility.ip.IPUtil;
import com.shishu.utility.json.JsonUtils;
import com.shishu.utility.log.LogUtil;

public class UrlUtil {
	

	private static Logger log = LogUtil.getLogger(UrlUtil.class);
	private static Pattern IP_PATTERN = Pattern
			.compile("(\\d{1,3}\\.){3}(\\d{1,3})");

	/**
	 * url转化为id
	 * 
	 * @param url
	 * @return
	 */
	public static String reverseUrl(String url) {
		
		String[] params = url.split("#oo#");
		
		
		
		try {
			
			if(params.length < 3) {
				return reverseUrl(new URL(url));
			} else {
				return reverseUrl(new URL(params[0])) + "#oo#" + params[1] + "#oo#" +params[2];
			}
			
			
		} catch (MalformedURLException e) {
			log.error("reverseUrl error " + url);
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * URL转化为id
	 * 
	 * @param url
	 * @return
	 */
	public static String reverseUrl(URL url) {
		String host = url.getHost();
		String file = url.getFile();
		String protocol = url.getProtocol();
		int port = url.getPort();

		StringBuilder buf = new StringBuilder();

		/* reverse host */
		reverseAppendSplits(host, buf);

		/* add protocol */
		buf.append(':');
		buf.append(protocol);

		/* add port if necessary */
		if (port != -1) {
			buf.append(':');
			buf.append(port);
		}

		/* add path */
		if (file.length() > 0 && '/' != file.charAt(0)) {
			buf.append('/');
		}
		buf.append(file);

		return buf.toString();
	}

	/**
	 * 
	 * @param string
	 * @param buf
	 */
	public static void reverseAppendSplits(String string, StringBuilder buf) {
		String[] splits = StringUtils.split(string, '.');
		if (splits.length > 0) {
			for (int i = splits.length - 1; i > 0; i--) {
				buf.append(splits[i]);
				buf.append('.');
			}
			buf.append(splits[0]);
		} else {
			buf.append(string);
		}
	}

	/**
	 * id转化为url
	 * 
	 * @param reversedUrl
	 * @return
	 */
	public static String unreverseUrl(String reversedUrl) {
		StringBuilder buf = new StringBuilder(reversedUrl.length() + 2);

		int pathBegin = reversedUrl.indexOf('/');
		if (pathBegin == -1)
			pathBegin = reversedUrl.length();
		String sub = reversedUrl.substring(0, pathBegin);

		String[] splits = StringUtils.split(sub, ':'); // {<reversed host>,
														// <port>, <protocol>}

		buf.append(splits[1]); // add protocol
		buf.append("://");
		reverseAppendSplits(splits[0], buf); // splits[0] is reversed
		// host
		if (splits.length == 3) { // has a port
			buf.append(':');
			buf.append(splits[2]);
		}
		buf.append(reversedUrl.substring(pathBegin));
		return buf.toString();
	}

	/**
	 * 获取url 的域名
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public static String GetDomainName(String url) throws MalformedURLException {
		if (url == null) {
			return null;
		}
		if(!(url.startsWith("http://") || url.startsWith("https://"))) {
			url = "http://" + url;
		}
		URL u = new URL(url);
		String domain = u.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	/**
	 * 获取url 的host
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public static String GetHost(String url) throws MalformedURLException {
		if (url == null) {
			return null;
		}
		URL u = new URL(url);
		return u.getHost();
	}

	/**
	 * 获取url内容
	 * 
	 * @param strURL
	 * @return
	 * @throws IOException
	 */
	public static String getURLContents(String strURL) throws IOException {
		StringBuffer sb = new StringBuffer();
		URL url = new URL(strURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setReadTimeout(5000);
		httpConn.setConnectTimeout(5000);
		httpConn.setRequestMethod("GET");// 设置请求方法
		httpConn.setDoOutput(true);// 可以输出
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String temp;
		while ((temp = br.readLine()) != null) {
			if (temp != null && temp.length() > 0) {
				sb.append(temp);
			}
		}
		br.close();
		isr.close();
		httpConn.disconnect();
		return sb.toString();
	}

	/**
	 * Returns the domain name of the url. The domain name of a url is the
	 * substring of the url's hostname, w/o subdomain names. As an example <br>
	 * <code>
	 *  getDomainName(conf, new URL(http://lucene.apache.org/))
	 *  </code><br>
	 * will return <br>
	 * <code> apache.org</code>
	 * */
	public static String getDomainName(URL url) {
		DomainSuffixes tlds = DomainSuffixes.getInstance();
		String host = url.getHost();
		// it seems that java returns hostnames ending with .
		if (host.endsWith("."))
			host = host.substring(0, host.length() - 1);
		if (IP_PATTERN.matcher(host).matches())
			return host;

		int index = 0;
		String candidate = host;
		for (; index >= 0;) {
			index = candidate.indexOf('.');
			String subCandidate = candidate.substring(index + 1);
			if (tlds.isDomainSuffix(subCandidate)) {
				return candidate;
			}
			candidate = subCandidate;
		}
		return candidate;
	}

	/**
	 * 获取压缩网址
	 * @param long_url
	 * @return
	 * @throws IOException
	 */
	public static String getShortUrl(String long_url) throws IOException {
		StringBuffer sb = new StringBuffer();
		URL url = new URL("http://dwz.cn/create.php");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setReadTimeout(10000);
		httpConn.setConnectTimeout(10000);
		httpConn.setRequestMethod("GET");// 设置请求方法
		httpConn.setDoOutput(true);// 可以输出
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());
		String content = "url=" + URLEncoder.encode(long_url, "utf-8");
		out.writeBytes(content);

		out.flush();
		out.close(); // flush and close
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String temp;
		while ((temp = br.readLine()) != null) {
			if (temp != null && temp.length() > 0) {
				sb.append(temp);
			}
		}
		br.close();
		isr.close();
		httpConn.disconnect();
		String result = sb.toString();
		try {
			Map<String, Object> map = JsonUtils.json2Bean(result, Map.class);
			if((int)map.get("status") == 0){
				return (String) map.get("tinyurl");
			}
		} catch (Exception e) {
		}
		return long_url;
	}
	
	public static boolean isTopURL(String str){
	      //转换为小写
	      str = str.toLowerCase();
	      String domainRules = "com.cn|net.cn|org.cn|gov.cn|edu.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
	      String regex = "^((https|http|ftp|rtsp|mms)?://)"  
	              + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
	             + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184  
	               + "|" // 允许IP和DOMAIN（域名） 
	            + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名-   
	               + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名  
	              + "("+domainRules+"))" // first level domain- .com or .museum  
	              + "(:[0-9]{1,4})?" // 端口- :80  
	              + "((/?)|" // a slash isn't required if there is no file name  
	              + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
	      Pattern pattern = Pattern.compile(regex);
	      Matcher isUrl = pattern.matcher(str);
	      return isUrl.matches();
	  }

	public static void main(String[] args) throws IOException {
		log.info(getShortUrl("http://companies.caixin.com/2015-03-16/100791567.html"));
	}
	
	/**
	 * 功能：检测当前URL是否可连接或是否有效, 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
	 * 
	 * @param urlStr
	 *            指定URL网络地址
	 * @return URL
	 */
	public static synchronized URL isConnect(String urlStr) {
		HttpURLConnection con;
		URL url = null;
		int state = -1;
		int counts = 0;
		if (urlStr == null || urlStr.length() <= 0) {
			return null;
		}
		while (counts < 5) {
			try {
				url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				System.out.println(counts + "= " + state);
				if (state == 200) {
					System.out.println("URL可用！");
				}
				break;
			} catch (Exception ex) {
				counts++;
				System.out.println("URL不可用，连接第 " + counts + " 次");
				urlStr = null;
				continue;
			}
		}
		return url;
	}
	
	public static boolean isValidation(String url, String[] excludeIPs) {
		
		InetAddress[] address = null;
		String host = null;
		try {
			host = UrlUtil.GetDomainName(url);
			address = InetAddress.getAllByName(host);
		} catch (MalformedURLException e) {
			log.error(url + " URL不合规则" + e.getMessage(),e);
		} catch (UnknownHostException e) {
			if(!host.startsWith("www.")) {
				try {
					address = InetAddress.getAllByName("www." + host);
				} catch (UnknownHostException e1) {
					log.error(host + " 未知主机名" + e1.getMessage(),e1);
				}
			} else {
				log.error(host + " 未知主机名" + e.getMessage(),e);
			}
			
		}
		
		if(address == null) {
			return false;
		}
		
		for(InetAddress addr : address) {
			
			for(String ip : excludeIPs) {
				if(ip.equals(addr.getHostAddress())) {
					
					if (!IPUtil.isIP(host)) {
						host = "www." + host;
						if(isValidation2(host,excludeIPs)) {
							return true;
						}
					}
					
					return false;
				}
			}
		}
		
		return true;

	}
	
	private static boolean isValidation2(String host, String[] excludeIPs) {

		InetAddress[] address = null;
		try {
			address = InetAddress.getAllByName(host);
		} catch (UnknownHostException e) {
			log.error(host + " 未知主机名" + e.getMessage(), e);
		}

		if (address == null) {
			return false;
		}

		for (InetAddress addr : address) {

			for (String ip : excludeIPs) {
				if (ip.equals(addr.getHostAddress())) {
					return false;
				}
			}
		}

		return true;

	}
	
	public static String absUrl(String baseUri,String relativeUrl) {


        URL base;
        try {
            try {
                base = new URL(baseUri);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its own, so try that
                URL abs = new URL(relativeUrl);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
            if (relativeUrl.startsWith("?"))
            	relativeUrl = base.getPath() + relativeUrl;
            URL abs = new URL(base, relativeUrl);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

}
