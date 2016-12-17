package com.shishu.utility.exception;

import java.util.Locale;

/**
 * 
 * 业务异常基类.
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月24日 下午6:58:49
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    private Object[] args;
    private String message = "";
    private String code = "";

    // private ErrorMessage errorMessage;
    public BusinessException() {
    	
    }
    
    public BusinessException(String message){
    	this.setMessage(message);
    }
    
    private static String createFriendlyErrMsg(String msgBody){
    	String prefixStr = "抱歉，";
    	String suffixStr = " 请稍后再试或与管理员联系！";
    	StringBuffer friendlyErrMsg = new StringBuffer("");
    	friendlyErrMsg.append(prefixStr);
    	friendlyErrMsg.append(msgBody);
    	friendlyErrMsg.append(suffixStr);
    	return friendlyErrMsg.toString();
    }


    public BusinessException(String messageCode, Object... args) {
        message = ErrorMessage.getErrorMessage(messageCode, args);
    }

    /**
     * 为了保持兼容,保留了这个方法,但是原功能已经不存在,请不要再使用这个方法
     */
    public BusinessException(String messageCode, Locale locale, Object... args) {
        message = ErrorMessage.getErrorMessage(messageCode, args);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
	public String getMessage() {
        return message;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
