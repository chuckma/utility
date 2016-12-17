/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shishu.utility.string;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * A collection of String processing utility methods.
 */
public class StringUtil {

	/**
	 * Returns a copy of <code>s</code> padded with trailing spaces so that it's
	 * length is <code>length</code>. Strings already <code>length</code>
	 * characters long or longer are not altered.
	 */
	public static String rightPad(String s, int length) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = length - s.length(); i > 0; i--)
			sb.append(" ");
		return sb.toString();
	}

	/**
	 * Returns a copy of <code>s</code> padded with leading spaces so that it's
	 * length is <code>length</code>. Strings already <code>length</code>
	 * characters long or longer are not altered.
	 */
	public static String leftPad(String s, int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = length - s.length(); i > 0; i--)
			sb.append(" ");
		sb.append(s);
		return sb.toString();
	}

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };

	/**
	 * Convenience call for {@link #toHexString(byte[], String, int)}, where
	 * <code>sep = null; lineLen = Integer.MAX_VALUE</code>.
	 * 
	 * @param buf
	 */
	public static String toHexString(byte[] buf) {
		return toHexString(buf, null, Integer.MAX_VALUE);
	}

	/**
	 * Get a text representation of a byte[] as hexadecimal String, where each
	 * pair of hexadecimal digits corresponds to consecutive bytes in the array.
	 * 
	 * @param buf
	 *            input data
	 * @param sep
	 *            separate every pair of hexadecimal digits with this separator,
	 *            or null if no separation is needed.
	 * @param lineLen
	 *            break the output String into lines containing output for
	 *            lineLen bytes.
	 */
	public static String toHexString(byte[] buf, String sep, int lineLen) {
		if (buf == null)
			return null;
		if (lineLen <= 0)
			lineLen = Integer.MAX_VALUE;
		StringBuffer res = new StringBuffer(buf.length * 2);
		for (int i = 0; i < buf.length; i++) {
			int b = buf[i];
			res.append(HEX_DIGITS[(b >> 4) & 0xf]);
			res.append(HEX_DIGITS[b & 0xf]);
			if (i > 0 && (i % lineLen) == 0)
				res.append('\n');
			else if (sep != null && i < lineLen - 1)
				res.append(sep);
		}
		return res.toString();
	}

	/**
	 * Convert a String containing consecutive (no inside whitespace)
	 * hexadecimal digits into a corresponding byte array. If the number of
	 * digits is not even, a '0' will be appended in the front of the String
	 * prior to conversion. Leading and trailing whitespace is ignored.
	 * 
	 * @param text
	 *            input text
	 * @return converted byte array, or null if unable to convert
	 */
	public static byte[] fromHexString(String text) {
		text = text.trim();
		if (text.length() % 2 != 0)
			text = "0" + text;
		int resLen = text.length() / 2;
		int loNibble, hiNibble;
		byte[] res = new byte[resLen];
		for (int i = 0; i < resLen; i++) {
			int j = i << 1;
			hiNibble = charToNibble(text.charAt(j));
			loNibble = charToNibble(text.charAt(j + 1));
			if (loNibble == -1 || hiNibble == -1)
				return null;
			res[i] = (byte) (hiNibble << 4 | loNibble);
		}
		return res;
	}

	private static final int charToNibble(char c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		} else if (c >= 'a' && c <= 'f') {
			return 0xa + (c - 'a');
		} else if (c >= 'A' && c <= 'F') {
			return 0xA + (c - 'A');
		} else {
			return -1;
		}
	}

	/**
	 * Checks if a string is empty (ie is null or empty).
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || (str.equals(""));
	}

	/**
	 * 比较两个（只有一处不同的）字符串，解析出模版
	 * 
	 * @param s1
	 *            字符串1
	 * @param s2
	 *            字符串2
	 * @return 模版
	 * 
	 * @author wangtao
	 */
	public static String prepareURLString(String s1, String s2) {
		int len1 = s1.length();
		int len2 = s2.length();

		char[] b1 = new char[len1];
		char[] b2 = new char[len2];

		s1.getChars(0, len1, b1, 0);
		s2.getChars(0, len2, b2, 0);

		StringBuilder stringBuilder = new StringBuilder("");

		// 比较两个字符串并把不同的字符替换为@
		int dis = Math.abs(len1 - len2);
		int j = 0;
		char[] high, low;

		if (len1 > len2) {
			high = b1;
			low = b2;
		} else {
			high = b2;
			low = b1;
		}

		for (int i = 0; i < high.length; i++) {

			if (high[i] == low[j]) {
				stringBuilder.append(high[i]);
			} else if (stringBuilder.charAt(stringBuilder.length() - 1) != '@') {
				stringBuilder.append("@");
			}

			if (stringBuilder.charAt(stringBuilder.length() - 1) == '@' && dis > 0) {
				dis--;
			} else {
				j++;
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * 通过模版匹配字符串，解析出与模板不相同的字符
	 * 
	 * @param temp 模版
	 * @param s 字符串
	 * @return 不同的字符串
	 * 
	 * @author wangtao
	 */
	public static String extractDifferenceString(String temp, String s) {
		String[] temps = temp.split("@");
		for (int i = 0; i < temps.length; i++) {
			s = s.replaceAll(quoteReplacement(temps[i]), "");
		}
		return s.trim();
	}

	/**
	 * 对string进行反义，使"\\"，"$","?","+","*"进行反义
	 * 
	 * @param s 待反义字符串
	 * @return 反义后的字符串
	 * 
	 * @author wangtao
	 */
	public static String quoteReplacement(String s) {
		if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1) && (s.indexOf('?') == -1)
				&& (s.indexOf('+') == -1) && (s.indexOf('*') == -1)) {
			return s;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' || c == '$' || c == '?' || c == '+' || c == '*') {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 生成N个零的字符串
	 * 
	 * @author wangtao 2013-12-16
	 */
	public static String zero(int len) {
		StringBuilder zero = new StringBuilder();
		for (int j = 0; j < len; j++) {
			zero.append("0");
		}
		return zero.toString();
	}
	
	public static String javabeanToString(Object javabean){ 
		return ReflectionToStringBuilder.toString(javabean, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/**
	 * 字符串包含
	 * 
	 * @author wangtao 2014-4-29
	 */
	public static boolean isContainChar(CharSequence pat,CharSequence sub){
		for (int i = 0; i < sub.length(); i++) {
			char subChar = sub.charAt(i);
			boolean isContain = false;
			for (int j = 0; j < pat.length(); j++) {
				if (pat.charAt(j) == subChar) {
					isContain = true;
				}
			}
			if (!isContain) {
				return false;
			}
		}
		return true;
	}
	
	

    /**
     * lucene转义字符
     * @param str
     * @return
     * @create 2008-11-26 上午09:38:09 wanghh
     * @history
     */
    public static String transform(String str) {
        if (str == null)
            return str;
        String resultStr = str;
        String[] strArray = new String[] { "\\", "\"", "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^",
                "~", "*", "?", ":" };
        for (int i = 0; i < strArray.length; i++) {
            resultStr = replace(resultStr, strArray[i], "\\" + strArray[i]);
        }
        return resultStr;
    }

    /**
     * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * 
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank(&quot;&quot;)        = true
     * StringUtil.isBlank(&quot; &quot;)       = true
     * StringUtil.isBlank(&quot;bob&quot;)     = false
     * StringUtil.isBlank(&quot;  bob  &quot;) = false
     * </pre>
     * 
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     * @create 2009-1-6 下午01:49:48 yanghb
     * @history
     */
    public static boolean isBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
	 * 
	 * @date：2013年9月7日
	 * @Description：驼峰规则
	 * @param name
	 * @return
	 */
	public static String toCamel(String name){
		return toCamel(name,true);
	}
	/**
	 * 
	 * @date：2013年9月7日
	 * @Description：驼峰规则
	 * @param name
	 * @param forClass
	 * @return
	 */
	public static String toCamel(String name,boolean forClass){
		if(isBlank(name)){
			return null;
		}
		name = name.toLowerCase();
		String[] temp = name.split("_");
		if(temp.length<2){
			if(forClass){

				String firstChar = name.substring(0, 1);
				return firstChar.toUpperCase()+name.substring(1);
			}
			return name;
		}
		StringBuffer sb = new StringBuffer();
		String firstSub =temp[0];

		if(firstSub.length()>1){
			String firstChar = firstSub.substring(0, 1);
			if(forClass){
				firstChar = firstChar.toUpperCase();
			}
			sb.append(firstChar);
			sb.append(firstSub.substring(1));
			
		}else{
			if(forClass){
				sb.append(firstSub.toUpperCase());
			}else{
				sb.append(firstSub);
			}
		}
		for(int i=1;i<temp.length;i++){
			String item = temp[i];
			String first = item.substring(0, 1);
			first = first.toUpperCase();
			sb.append(first);
			sb.append(item.substring(1));
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperFirst(String str){
		String firstChar = str.substring(0, 1);
		return firstChar.toUpperCase()+str.substring(1);
	}
	/**
	 * 
	 * @date：2013年9月7日
	 * @Description：命名惯例转换
	 * @param packge
	 * @return
	 */
	public static String packge2path(String packge){
		return packge = packge.replaceAll("\\.", "/");
	}

    
    /** 
     * 组合成新的URL地址,把第二个参数连接到第一个参数后面组合成新URL,如果第二个参数是以http://,ftp://,https://开头的话,就不组合,直接返回第二个参数的值
     * @param baseUrlStr
     * @param nextUrlStr
     * @return 
    * @create  Jan 19, 2009 9:34:50 AM yuancong
    * @history  
    */
    public static String unite2UrlStr(String baseUrlStr, String nextUrlStr) {
        String tmp_baseUrlStr = baseUrlStr;
        String tmp_nextUrlStr = nextUrlStr;
        if (isBlank(tmp_baseUrlStr)) {
            return nextUrlStr;
        }
        if (isBlank(tmp_nextUrlStr)) {
            return baseUrlStr;
        }
        if (tmp_nextUrlStr.startsWith("http://") || tmp_nextUrlStr.startsWith("ftp://") || tmp_nextUrlStr.startsWith("https://")) {
            return tmp_nextUrlStr;
        }
        tmp_baseUrlStr = tmp_baseUrlStr.replace("\\", "/");
        tmp_nextUrlStr = tmp_nextUrlStr.replace("\\", "/");
        if (!tmp_baseUrlStr.endsWith("/")) {
            tmp_baseUrlStr = tmp_baseUrlStr + "/";
        }
        if (tmp_nextUrlStr.startsWith("/")) {
            tmp_nextUrlStr = tmp_nextUrlStr.substring(1);
        }
        return tmp_baseUrlStr + tmp_nextUrlStr;
    }
    
    /** 
     * 取得客户端IP,解决使用代理得不到客户端IP的BUG
     * @param request
     * @return 
     * @create  2009-2-24 上午09:19:02 yanghb
     * @history  
     */
    public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
    
    public static void setCookie(HttpServletResponse response, String CookieName, String CookieVal, int CookieAge)
    throws UnsupportedEncodingException
	{
	    Cookie cookie = new Cookie(CookieName, URLEncoder.encode(CookieVal, "utf-8"));
	    cookie.setMaxAge(CookieAge);
	    cookie.setPath("/");
	    response.addCookie(cookie);
	}
	
	public static String getCookie(HttpServletRequest request, String CookieName)
	    throws UnsupportedEncodingException
	{
	    Cookie cookies[] = request.getCookies();
	    if(cookies == null)
	        return null;
	    for(int i = 0; i < cookies.length; i++)
	        if(cookies[i].getName().equals(CookieName))
	            return URLDecoder.decode(cookies[i].getValue(), "utf-8");
	
	    return null;
	}
	
	public static String[] getCookie(HttpServletRequest request)
	    throws UnsupportedEncodingException
	{
	    Cookie cookies[] = request.getCookies();
	    ArrayList al = new ArrayList();
	    if(cookies == null)
	        return null;
	    for(int i = 0; i < cookies.length; i++)
	        al.add(cookies[i].getName() + " = " + URLDecoder.decode(cookies[i].getValue(), "utf-8"));
	
	    return (String[])al.toArray(new String[0]);
	}
	
	
	/**
	 * @param str
	 * @param oldStr
	 * @param newStr
	 * @return
	 */
	public static String replace(String str, String oldStr, String newStr) {
		if (str != null) {
			int index = 0;
			int oldLen = oldStr.length();
			// oldStr字符串长度
			int newLen = newStr.length();
			// newStr字符串长度
			while (true) {
				index = str.indexOf(oldStr, index);
				if (index == -1) {
					return str;
				} else {
					str = str.substring(0, index) + newStr
							+ str.substring(index + oldLen);
					index += newLen;
				}
			}
		} else {
			return "";
		}
	}

	/**
	 * 将带有'_'的单词转成驼峰状形式
	 * 
	 * @param tableName
	 * @return
	 */
	public static String nameMapping(String tableName) {
		if (tableName.contains("_")) {
			String[] words = tableName.split("_");
			String str = words[0].toLowerCase();
			for (int i = 1; i < words.length; i++) {
				str += wordMapping(words[i]);
			}
			return str;
		} else {
			return wordMapping(tableName);
		}
	}

	/**
	 * 将单词首字母大写，其余字母小写
	 * 
	 * @param word
	 * @return
	 */
	public static String wordMapping(String word) {
		return Character.toUpperCase(word.charAt(0))
				+ word.substring(1, word.length()).toLowerCase();
	}
	
	/**
	 * 隐藏身份证后四位
	 * @param idCard
	 * @return
	 */
	public static String hideLastFourNumb(String idCard) {
		if(!isBlank(idCard) && idCard.length() > 4){
			String replaceStr = idCard.substring(0, (idCard.length() - 4));
			replaceStr += "****";
			return replaceStr;
		}
		return idCard;
	}
	
	/**
	 * 将字符串中的空格、换行、回车、水平制表符去掉
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * 将字符串中换行、回车、水平制表符去掉
	 * @param str
	 * @return
	 */
	public static String replaceTab(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * Make a string representation of the exception.
	 * @param e The exception to stringify
	 * @return A string with exception name and call stack.
	 */
	public static String stringifyException(Throwable e) {
		StringWriter stm = new StringWriter();
		PrintWriter wrt = new PrintWriter(stm);
		e.printStackTrace(wrt);
		wrt.close();
		return stm.toString();
	}
	
	public static void main(String[] args) {
		// if (args.length != 1)
		// System.out.println("Usage: StringUtil <encoding name>");
		// else
		// System.out.println(args[0] + " is resolved to " +
		// EncodingDetector.resolveEncodingAlias(args[0]));
		prepareURLString("http://bbs.hangzhou.com.cn/forum-11-1.html",
				"http://bbs.hangzhou.com.cn/forum-11-2.html");
		
		System.out.println(isContainChar("土默特左旗","土1左旗"));
	}
}
