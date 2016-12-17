package com.shishu.utility.url;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtil {
	
	/**
	 * 获得某域名的IP地址
	 * 
	 * @param domain 域名
	 * @return ips
	 */
	public static InetAddress[] getServerIP(String domain) {
		InetAddress[] myServer = null;
		try {
			myServer = InetAddress.getAllByName(domain);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return (myServer);
	}

}
