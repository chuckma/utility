package com.shishu.utility.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.shishu.utility.resource.ReadResources;
import com.thoughtworks.xstream.XStream;



/**
 * 
 * XML帮助类
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:08:22
 */
@SuppressWarnings("unchecked")
public class XmlUtils {
	/**
	 * 读取指定XML文件的根元素.
	 */
	public static Element getRootElement(String fileURI, Class clazz) {
		Element rootElement = null;
		InputStream stream = null;
		try {
			stream = clazz.getResourceAsStream(fileURI);
			rootElement = new SAXBuilder().build(stream).getRootElement();
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return rootElement;
	}
	public static Element getRootElement(String fileURI) {
		try {
			return getRootElement(ReadResources.getResourceAsStream(fileURI));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Element getRootElement(Reader reader) {
		try {
			return new SAXBuilder().build(reader).getRootElement();
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally{
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	public static Element getRootElement(InputStream stream) {
		try {
			return new SAXBuilder().build(stream).getRootElement();
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally{
			if(null != stream){
				try {
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static Properties parseAttributes(Element element) {
		Properties attributes = new Properties();
		Iterator<Attribute> attributeNodes = element.getAttributes().iterator();
		while(attributeNodes.hasNext()){
			Attribute attribute = attributeNodes.next();
			attributes.put(attribute.getName(), attribute.getValue());
		}
		return attributes;
	}
	
	/**
	 * 将bean生成xml
	 * @Title: bean2xml 
	 * @param bean
	 * @return
	 * @return: String
	 */
	public static String bean2xml(Object bean){
		XStream xstream = new XStream();
		return xstream.toXML(bean);
	}
}
