package com.shishu.utility.string;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrTest {
	
	public static String toStringHex1(String s) {  
		   byte[] baKeyword = new byte[s.length() / 2];  
		   for (int i = 0; i < baKeyword.length; i++) {  
		    try {  
		     baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(  
		       i * 2, i * 2 + 2), 16));  
		    } catch (Exception e) {  
		     e.printStackTrace();  
		    }  
		   }  
		   try {  
		    s = new String(baKeyword, "utf-8");// UTF-16le:Not  
		   } catch (Exception e1) {  
		    e1.printStackTrace();  
		   }  
		   return s;  
		}  

	public static String toStringHex2(String s) {  
		   byte[] baKeyword = new byte[s.length() / 2];  
		   for (int i = 0; i < baKeyword.length; i++) {  
		    try {  
		     baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(  
		       i * 2, i * 2 + 2), 16));  
		    } catch (Exception e) {  
		     e.printStackTrace();  
		    }  
		   }  
		   try {  
		    s = new String(baKeyword, "utf-8");// UTF-16le:Not  
		   } catch (Exception e1) {  
		    e1.printStackTrace();  
		   }  
		   return s;  
		}  
	
	public static String unicodeToString(String str) {
		 
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
 
        Matcher matcher = pattern.matcher(str);
 
        char ch;
 
        while (matcher.find()) {
 
            ch = (char) Integer.parseInt(matcher.group(2), 16);
 
            str = str.replace(matcher.group(1), ch + "");    
 
        }
 
        return str;
 
    }
	
	public static void test() {
		String hex = "\u6bcf\u5929\u8bfb\u70b9\u6545\u4e8b";
		hex = "\u5f90\u82e5\u7444\u7b2c\u4e00\u6b21\u2014\u2014\u5434\u5947\u9686\uff0c15\u5c81\u51fa\u9053\u4e0e\u201c\u56db\u7237\u201d\u76f8\u604b\uff0c\u540e\u906d\u5434\u5947\u9686\u6bcd\u4eb2\u963b\u62e6\u800c\u5206\u624b\u3002\u674e\u5609\u6b23\u7684\u7b2c\u4e00\u6b21\u2014\u2014\u502a\u9707\uff0c\u674e\u5609\u6b23\u53c2\u9009\u9999\u6e2f\u5c0f\u59d0\u65f6\u502a\u9707\u5c31\u662f\u8bc4\u59d4\uff0c\u7136\u540e\u674e\u5609\u5f97\u4e86\u51a0\u519b\uff0c\u4e24\u4eba\u8d70\u5230\u4e00\u8d77\u3002\u5f20\u67cf\u829d\u7684\u7b2c\u4e00\u6b21\u2014\u2014\u6731\u6c38\u68e0\uff0c\u636e\u8bf4\u5176\u5e38\u5e38\u5411\u670b\u53cb\u4eec\u70ab\u8000\uff1a\u522b\u63d0\u9648\u5c0f\u6625\u3001\u8c22\u9706\u950b\uff0c\u8981\u77e5\u9053\uff0c\u6211\u624d\u662f\u5f20\u67cf\u829d\u7b2c\u4e00\u4e2a\u7537\u4eba\u3002\u770b\u6765\u4e5f\u4e0d\u662f\u4ec0";
		hex = "\u5f90\u82e5\u7444\u6797\u5fc3\u5982\u5f20\u67cf\u829d \u5973\u661f\u201c\u521d\u591c\u201d\u90fd\u7ed9\u4e86\u8c01\uff1f";
		System.out.println(unicodeToString(hex));
	}
	
	public static void main(String[] args) {
		String test = "浙BSG082    ";
		
		System.out.println(StringUtil.replaceTab(test));
		
		System.out.println(StringUtil.replaceBlank(test));

	}

}
