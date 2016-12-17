/**
 * 
 */
package com.shishu.utility.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * 
 * 输出xml工具类
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:11:27
 */
public class WriteXMLUtil {
	protected static final Log log = LogFactory.getLog(WriteXMLUtil.class);
	/**
	 *写xml的头
	 *@param encoding 编码方式
	 *@return string 
	 **/
	public static String writeProlog(String encoding) {
		return "<?xml version=\"1.0\"  encoding=\"" + encoding + "\"?>";
	}
	
	/**
	 * 写xml的DOCTYPE
	 * @param rootElement	the element of root
	 * @param description	description
	 * @param dtdLocation	location of dtd
	 * @return				string
	 * @throws IOException
	 */
	public static String writePublicDoctype(String rootElement,
			String description, String dtdLocation){
		return ("<!DOCTYPE " + rootElement + " PUBLIC \"" + description
				+ "\" \"" + dtdLocation + "\">\n\n");
	}
	
	
	/** 
	 * @method：writeElement  
	 * @param name		标签名称
	 * @param attName	属性名称	 * @param attValue	属性值	 * @return 			字符串 <标签名称 属性名称="属性值">
	 * 
	 */
	public static String writeElement(String name,String attName, 
			String attValue){
		StringBuffer buffer = new StringBuffer("");
		if(null != name){
			buffer.append('<');
			buffer.append(name);
			buffer.append(' ');
			buffer.append(attName);
			buffer.append("=\"");
			buffer.append(attValue);
			buffer.append("\">");
		}
		return buffer.toString();
	}
	/**
	 * @method：writeElement  
	 * @param name		标签名称
	 * @param hasSlash	是否有反斜杠
	 * @return 			 <name> or </name>字符串	 * 
	 */
	public static String writeElement(String name,boolean hasSlash){
		StringBuffer buffer = new StringBuffer("");
		if(null != name){
			buffer.append('<');
			if(hasSlash)
				buffer.append('/');
			buffer.append(name);
			buffer.append(">");
		}
		return buffer.toString();
	}
	public static String writeElement(String name){
		return writeElement(name,false);
	}
	
	/**
	 * 
	 * @param data	数据 
	 * @return		<![CDATA[数据]]>
	 */
	public static String writeCDATA(String data) {
		StringBuffer buffer = new StringBuffer("");
		if (data != null) {
			buffer.append("<![CDATA[");
			buffer.append(data);
			buffer.append("]]>");
		}
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param name	标签
	 * @param data	数据
	 * @return		<name><![CDATA[data]]></name>	
	 */
	public static String writeCDATAElement(String name, String data) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append(writeElement(name));
		buffer.append(writeCDATA(data));
		buffer.append(writeElement(name,true));
		return buffer.toString();
	}
	
	/**
	 * 写xml元素 包括标签 、数据、属性与属性值
	 * @param name
	 * @param data  非空的数据，否则不产生	 * @param attName
	 * @param attValue
	 * @return
	 */
	public static String writeCDATAElement(String name, String data,
			String attName, String attValue) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append(writeElement(name,attName,attValue));
		buffer.append(writeCDATA(data));
		buffer.append(writeElement(name,true));
		return buffer.toString();
	}
	public static int searchMethod(Method[] methods,String method){
		for(int i = 0,size = methods.length; i < size; i++){
			if(methods[i].getName().equals(method))
				return i;
		}
		return -1;
	}
	public static int binarySearchMethod(Method[] methods,String method){
		int low = 0;
		int high = methods.length-1;
		while (low <= high) {
		    int mid = (low + high) >>> 1;
		    String midVal = methods[mid].getName();
		    int cmp = midVal.compareTo(method);

		    if (cmp < 0)
		    	low = mid + 1;
		    else if (cmp > 0)
		    	high = mid - 1;
		    else
		    	return mid; // key found
		}
		return -(low + 1);  // key not found
	}

	/**
	 * @method：parse  
	 * @param is
	 * @return 
	 * @description：解析inputsource对象生成document.
	 * 
	 */
	public static Document parse(InputSource is)
	{
		try{
			return createDocumentBuilder().parse(is);
		}
		catch (SAXException e){
			log.info("Failed to parse the xml document", e);
		}
		catch (IOException e){
			log.info("Failed to parse the xml document", e);
		}
		return null;
	}
	
	/** 
	 * @method：parse  
	 * @param uri
	 * @return 
	 * @description：解析URI对象生成document.
	 * 
	 */
	public static Document parse(String uri)
	{
		return parse(new InputSource(uri));
	}
	
	/** 
	 * @method：parse  
	 * @param file	
	 * @return 
	 * @description：解析xml文件为document对象
	 * 
	 */
	public static Document parse(File file){
		try{
			return createDocumentBuilder().parse(file);
		}
		catch (SAXException e){
			log.info("Failed to parse the xmlf document", e);
		}
		catch (IOException e){
			log.info("Failed to parse the xml document", e);
		}
		return null;
	}
	
	/** 
	 * @method：parse  
	 * @param is
	 * @return 
	 * @description：解析InputStream对象生成Document
	 * 
	 */
	public static Document parse(InputStream is)
	{
		return parse(new InputSource(is));
	}
	
	/** 
	 * @method：parse  
	 * @param url
	 * @return 
	 * 
	 */
	public static Document parse(URL url){
		InputStream is = null;
		try{
			is = url.openStream();
			return createDocumentBuilder().parse(is);
		}
		catch (SAXException e){
			log.info("Failed to parse the xmlf document", e);
		}
		catch (IOException e){
			log.info("Failed to parse the xml document", e);
		}
		finally{
			if (is != null){
				try{
					is.close();
				}
				catch (IOException e){
					log.warn("Error closing stream of URL " + url, e);
				}
			}
		}
		return null;
	}

	
	/**
	 * @method：createDocumentBuilder  
	 * @return 		DocumentBuilder对象
	 * @description：创建xml的DocumentBuilder对象
	 * 
	 */
	public static DocumentBuilder createDocumentBuilder()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringComments(true);
		
		try{
			return dbf.newDocumentBuilder();
		}
		catch (ParserConfigurationException e){
			log.info("Failed to create a document builder factory", e);
		}
		return null;
	}
	
	/**
	 * @method：createDocument  
	 * @param sourceNode	the node
	 * @return 		a document having the specified node as root
	 * @description：创建带有root的Document对象
	 * 
	 */
	public static Document createDocument(Node sourceNode)
	{
		Document doc = createDocumentBuilder().newDocument();
		Node source;
		if (sourceNode.getNodeType() == Node.DOCUMENT_NODE) {
			source = ((Document) sourceNode).getDocumentElement();
		} else {
			source = sourceNode;
		}

		Node node = doc.importNode(source, true);
		doc.appendChild(node);
		
		return doc;
	}
	
	/**
	 * 以xml方式的String转换为org.w3c.Document对象
	 * @param str    以xml方式的String
	 * @return		org.w3c.Document对象
	 */
	public static Document parseString(String str){
		
		try{
			StringReader strReader = null;
			if(null != str){
				strReader = new StringReader(str);
				return createDocumentBuilder().parse(new InputSource(strReader));
			}
		}
		catch (SAXException e){
			log.info("Failed to parse the xmlf document", e);
		}
		catch (IOException e){
			log.info("Failed to parse the xml document", e);
		}
		return null;
	}

}
