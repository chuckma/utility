package com.shishu.utility.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import com.shishu.utility.resource.ReadResources;

/**
 * 
 * 业务异常信息管理类
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午8:55:44
 */
public class ErrorMessage {
	private List<String> propertiesFile;
	private static Properties propertie = new Properties();
	private String errorCode;
	private String errorMessage;

	/**
	 * 通过error的code号，以及参数，得到对应的错误信息
	 * @param errorCode
	 * @param args
	 */
	public static String getErrorMessage(String errorCode, Object... paramsArgs) {
		String message = "";
		// if ("en".equals(locale.getLanguage())) {
		if (null == propertie) {
			return "系统在初始化errorMessage时失败！";
		}
		if (propertie.containsKey(errorCode)) {
			message = MessageFormat.format(propertie.get(errorCode).toString(),
					paramsArgs);
		} else if (propertie.containsKey("Exception_0001")) {
			message = MessageFormat.format(propertie.get("Exception_0001")
					.toString(), paramsArgs);
		} else {
			message = "未知异常号，没有对应的消息.";
		}
		// }
		return message;
	}

	/**
	 * 初始化，把所有的错误信息配置文件中的配置加载进来
	 * 
	 */
	public boolean initMessages() {
		if (null == propertiesFile || propertiesFile.size() <= 0) {
			return false;
		}
		for (String filePath : propertiesFile) {
			putMsgFile(filePath);
		}
		return true;
	}

	public void putMsgFile(String fileName) {
		try {
			Properties tmpPropertie = new Properties();
			InputStream inputFile = ReadResources.getResourceAsStream(fileName); // new
			try {
				tmpPropertie.load(inputFile);// 取得这个配置
				if (null == propertie) {
					propertie = tmpPropertie;
				} else {
					propertie.putAll(tmpPropertie);// 合并到一起
				}
			} finally {
				// added by yanghb20090116
				if (inputFile != null) {
					inputFile.close();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径:" + fileName
					+ " 错误或者文件不存在");
		} catch (IOException ex) {
			System.out.println("装载文件 " + fileName + " --->失败!");
		}
	}

	public List<String> getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(List<String> propertiesFile) {
		this.propertiesFile = propertiesFile;
		initMessages();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
