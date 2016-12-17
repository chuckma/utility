package com.shishu.utility.exception;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.shishu.utility.resource.ReadResources;

/**
 * 
 * 默认加载异常信息
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午8:56:28
 */
@SuppressWarnings("serial")
public class ExceptionServletInit extends HttpServlet {
	private String exceptionConfig;

	@Override
	public void init(ServletConfig config) throws ServletException {
        InputStream instr = null;
        this.exceptionConfig = config.getInitParameter("exceptionConfig");
        // 加载默认配置文件
        if (null == this.exceptionConfig || this.exceptionConfig.trim().equals("")) {
            exceptionConfig = "business_exception_messages.xml";
        }
        super.init(config);
        try {
            instr = ReadResources.getResourceAsStream(exceptionConfig);
            XmlMessageBuilder.getMessageResources(instr);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	//added by yanghb20090116
        	try{
	        	if(instr!=null){
	        		instr.close();
	        	}
        	}catch(IOException e){
        		this.log("ExceptionServletInit文件流关闭异常");
        	}
        	
        }
    }
}
