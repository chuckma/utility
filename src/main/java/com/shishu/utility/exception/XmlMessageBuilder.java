package com.shishu.utility.exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * 
 * 解析XML并对异常信息进行组装
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:02:02
 */
public class XmlMessageBuilder {
	private static final String EXCEPTION_CONFIG_ELEMENT = "exceptioninfo";

	private static final String EXCEPTION_ELEMENT = "exception";

	public XmlMessageBuilder() {
	}

	/**
	 * 组装异常提示信息
	 * 
	 * @param inputstream
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	public synchronized static ErrorMessage getMessageResources(InputStream inputstream)
			throws IOException, JDOMException, Exception {
		ErrorMessage errorMessage = new ErrorMessage();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(inputstream);
		Element root = doc.getRootElement();
		String rootName = root.getName();
		if (rootName.equals(EXCEPTION_CONFIG_ELEMENT)) {
			List children = root.getChildren();
			for (int i = 0; i < children.size(); i++) {
				Element child = (Element) children.get(i);
				if (child.getName().equals(EXCEPTION_ELEMENT))
					errorMessage.putMsgFile(child.getText().trim());
			}
		} else {
			throw new IOException("The root tag of the ExceptionConfig XML document must be '"
					+ EXCEPTION_CONFIG_ELEMENT + "'.");
		}
		return errorMessage;
	}

	public void redeploy() throws IOException, JDOMException, Exception {
        InputStream instr = null;
        try{
        	instr = getClass().getResourceAsStream("/expinfo.xml");
        	getMessageResources(instr);
        }finally{
			try{
				if(instr!=null){
					instr.close();
				}
			}catch(Exception e){
				throw new Exception("XmlMessageBuilder:/expinfo.xml 关闭错误");
			}
        }
        
    }
}
