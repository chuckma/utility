package com.shishu.utility.map;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.shishu.utility.url.UrlUtil;

public class GeoUtil {
	
	private static final Logger log = LoggerFactory.getLogger(GeoUtil.class);
	private static final String geoUrl = "http://api.map.baidu.com/geocoder/v2/?output=xml&ak=DFbbaed951bb2145c655f7abb4c4417d&callback=showLocation&address=";
	
	/**
	 * 根据地址和所在城市获取百度地图坐标
	 * @param address
	 * @param city
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> getGeoPoint(String address, String city) throws IOException {
		String url = geoUrl.concat(address);
		if (city != null && !"n".equals(city) && !"".equals(city)) {
			url = url.concat("&city=").concat(city);
		}
		String rsp = UrlUtil.getURLContents(url);
		if(rsp == null || "".equals(rsp)) return null;
		InputStream is = new ByteArrayInputStream(rsp.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		try {
			builder = dbf.newDocumentBuilder();
			doc = builder.parse(is);
		} catch (ParserConfigurationException e) {
			log.error("parse error",e.getMessage());
			return null;
		} catch (SAXException e) {
			log.error("parse error",e.getMessage());
			return null;
		} catch (IOException e) {
			log.error("parse error",e.getMessage());
			return null;
		}
		Element root = doc.getDocumentElement();

		String status;// 返回结果状态值， 成功返回0
		String lng;// 坐标
		String lat;
		String precise;// 是否精确查找。1为精确查找，0为不精确
		String confidence;// 可信度
		String level;// 地址类型
        
		// 返回结果状态值
		if (root.getElementsByTagName("status").getLength() == 1
				&& root.getElementsByTagName("status").item(0).hasChildNodes()) {
			status = root.getElementsByTagName("status").item(0)
					.getFirstChild().getNodeValue();
		}else
			return null;
		// 坐标
		if (root.getElementsByTagName("lng").getLength() == 1
				&& root.getElementsByTagName("lng").item(0).hasChildNodes()) {
			lng = root.getElementsByTagName("lng").item(0)
					.getFirstChild().getNodeValue();
		}else
			return null;
		
		if (root.getElementsByTagName("lat").getLength() == 1
				&& root.getElementsByTagName("lat").item(0).hasChildNodes()) {
			lat = root.getElementsByTagName("lat").item(0)
					.getFirstChild().getNodeValue();
		}else
			return null;
		// 是否精确查找
		if (root.getElementsByTagName("precise").getLength() == 1
				&& root.getElementsByTagName("precise").item(0).hasChildNodes()) {
			precise = root.getElementsByTagName("precise").item(0)
					.getFirstChild().getNodeValue().equals("1")?"true":"false";
		}else
			return null;
		// 可信度
		if (root.getElementsByTagName("confidence").getLength() == 1
				&& root.getElementsByTagName("confidence").item(0).hasChildNodes()) {
			confidence = root.getElementsByTagName("confidence").item(0)
					.getFirstChild().getNodeValue();
		}else
			return null;
		// 地址类型
		if (root.getElementsByTagName("level").getLength() == 1
				&& root.getElementsByTagName("level").item(0).hasChildNodes()) {
			level = root.getElementsByTagName("level").item(0)
					.getFirstChild().getNodeValue();
		}else
			return null;

		if (status.equals("0") && !"".equals(lat) && !"".equals(lng)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("geo", lat + "," + lng);
			map.put("precise", precise);
			map.put("confidence", confidence);
			map.put("addressType", level);
			return map;
		} else
			return null;
	}

	public static void main(String[] args) throws IOException {
		long alltime = 0;
		
		for(int i=0;i<10;i++){
			long time = System.currentTimeMillis();
			Map<String, Object> map = getGeoPoint("伟业路1号","杭州");
			alltime += (System.currentTimeMillis()-time);
			for(Entry<String, Object> e: map.entrySet()){
				System.out.println(e.getKey()+" , "+e.getValue());
			}
		}
		
		System.out.println("time:"+alltime/10);
		/*for(Entry<String, Object> e: map.entrySet()){
			System.out.println(e.getKey()+" , "+e.getValue());
		}*/

	}

}
