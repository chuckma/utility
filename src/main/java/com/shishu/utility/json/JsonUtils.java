package com.shishu.utility.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shishu.utility.bean.PBeanUtils;
import com.shishu.utility.string.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;


/**
 * 
 * Json工具类.
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:07:39
 */
public class JsonUtils {

	private JsonUtils() {
	}

	private static String[] EXCLUDES = { "start", "limit", "sort", "dir" ,"data"};

	// ----------------------------------------------------------------- BEAN 2 Json
	/**
	 * 对象转换成Json字符串
	 * @param bean 将转换为Json字符串的对象
	 */
	public static String bean2Json(Object o) {
		return bean2Json(o, EXCLUDES);
	}
	public static String bean2Json(Object o, String dateFormat) {
		return bean2Json(o, EXCLUDES, dateFormat);
	}

	public static String bean2Json(Object o, Integer count) {
		return appendResult(o, count, EXCLUDES);
	}
	
	public static String arrayObj2Json(List<?> l){
		return JSONArray.fromObject(l).toString();
	}

	/**
	 * Page对象转换成Json字符串
	 * @param page 将转换为Json字符串的Page对象
	 */
	/*public static String bean2Json(Page page) {
		//System.out.println(bean2Json(page, EXCLUDES));
		return bean2Json(page, EXCLUDES);
	}*/
	
	/**
	 * Page对象转换成Json字符串,带自定义日期转换格式
	 * @param page page对象
	 * @param dateFormat 日期格式
	 * @return
	 */
	/*public static String bean2Json(Page page, String dateFormat) {
		return bean2Json(page, EXCLUDES, dateFormat);
	}*/
	
	/**
	 * 对象转换成Json字符串
	 * @param bean 将转换为Json字符串的对象，不可为<code>null<code>
	 * @param excludes 不转换的属性数组
	 */
	public static String bean2Json(Object o, String[] excludes) {
		if (o == null) {
			throw new IllegalArgumentException("object is null while write the Json string...");
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new SqlDateProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Integer.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Long.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
		String  jsonString = JSONObject.fromObject(o, jsonConfig).toString();
		jsonString = jsonString.replace("result", "rows");
		return jsonString;
	}
	
	
	/**
	 * 数组转换成Json字符串
	 * @param bean 将转换为Json字符串的对象，不可为<code>null<code>
	 *
	 */
	public static String beanlist2Json(List<?> o) {
		if (o == null) {
			throw new IllegalArgumentException("object is null while write the Json string...");
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(EXCLUDES);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new SqlDateProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Integer.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Long.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
		String  jsonString = JSONArray.fromObject(o, jsonConfig).toString();
		return jsonString;
	}
	
	/**
	 * 对象转换成Json字符串
	 * @param bean 将转换为Json字符串的对象，不可为<code>null<code>
	 * @param excludes 不转换的属性数组
	 */
	public static String bean2JsonArray(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("object is null while write the Json string...");
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new SqlDateProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Integer.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Long.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
		String jsonString = JSONArray.fromObject(o, jsonConfig).toString();

		return jsonString;
	}
	
	/**
	 * 自定义日期格式转换
	 * @param o
	 * @param excludes
	 * @param dateFormat
	 * @return
	 */
	public static String bean2Json(Object o, String[] excludes, String dateFormat) {
		if (o == null) {
			throw new IllegalArgumentException("object is null while write the Json string...");
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new SqlDateProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Integer.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(java.lang.Long.class, new BaseProcessor());
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor(dateFormat));
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new DateJsonValueProcessor(dateFormat));
		String jsonString = JSONArray.fromObject(o, jsonConfig).toString();

		// 默认输出的格式如下：[{"result":[{"id":0},{"id":1}],"totalCount":2}]
		// Ext.data.JsonReader支持的格式则为：{"result":[{"id":0},{"id":1}],"totalCount":2}
		// 去掉JsonArray自动生成最前面的"["和最后的"]"，暂时没找到对应的API处理:)
		jsonString = jsonString.substring(1, jsonString.length());
		jsonString = jsonString.substring(0, jsonString.length() - 1);

		return jsonString;
	}

	public static String appendResult(Object o,Integer count, String[] excludes){
		String jsonString = bean2Json(o, excludes);
		jsonString = "{\"results\":" + count + ",\"rows\":[" + jsonString + "]}";
		return jsonString;
	}
	// ----------------------------------------------------------------- Json 2 BEAN
	/**
	 * 根据json数据创建指定的POJO.
	 * @param jsonData
	 *            json数据字符串，如：{"id":"1"}
	 * @param clazz
	 *            需要转换成bean的具体类型	 * @return T
	 */
	public static <T> T json2Bean(String jsonData, Class<T> clazz) throws Exception {
		if (StringUtil.isBlank(jsonData)) {
			return clazz.newInstance();
		}

		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		//return json2Bean(jsonObject, clazz);
		return  (T) JSONObject.toBean(jsonObject, clazz); //wgh
	}

	/**
	 * 根据json数据创建指定的POJO List.
	 * @param jsonData
	 *            json数据字符串，如：{"id":"1"}
	 * @param clazz
	 *            需要转换成bean的具体类型	 * @return List<T>
	 */
	public static <T> List<T> json2List(String jsonData, Class<T> clazz) throws Exception {
		if (StringUtil.isBlank(jsonData)) {
			return new ArrayList<T>(0);
		}

		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		return (List<T>) JSONArray.toCollection(jsonArray, clazz);
	}

	/**
	 * **********************************************
	 * @method：getJsonCombox
	 * @param map 根据下拉类型获取BaseConfigObj中定义的Map
	 * @return 前台JsonReader所需的数据形式,如：{'totalCount':2,'rows':[{ 'id': 1, 'text': 'Bill'},{'id':2,'text':'Ben'}]}
	 * @throws Exception
	 * @description：根据后台定义组装前台JSON下拉列表数据
	 * @create:Oct 27, 2008-5:42:51 PM huangrh
	 */
	public static String getJsonCombox(Map<String, String> map){
		StringBuffer strBuffer = new StringBuffer();
		if(map.isEmpty()){
			strBuffer.append("{\"result\":[],").append("\"totalCount\":0}");
			return strBuffer.toString();
		}
		
		strBuffer.append("{\"result\":[");
		//strBuffer.append("{\"id\":\"\",\"text\":\" - 请选择 - \"},");
		
		for (Object object : map.entrySet()) {
			Map.Entry entry = (Map.Entry) object;
			String propertyName = entry.getKey().toString();
			String propertyValue = entry.getValue().toString();
			strBuffer.append("{\"id\":\"" + propertyName + "\",\"text\":\"" + propertyValue + "\"},");
		}
		String buffer = strBuffer.substring(0, strBuffer.length()-1);
		buffer = buffer + "],\"totalCount\":" + map.size() + "}";
		return buffer;
	}
	
	/**
     * @author wangtao
     * @return 前台JsonReader所需的数据形式,如：{'total':2,'rows':[{ 'id': 1, 'text': 'Bill'},{'id':2,'text':'Ben'}]}
     */
    public static String map2Json(Map<String, String> map){
        StringBuffer strBuffer = new StringBuffer();
        if(map.isEmpty()){
            strBuffer.append("{\"total\":" + map.size() + ",\"data\":[]}");
            return strBuffer.toString();
        }
        
        strBuffer.append("{\"total\":" + map.size() + ",\"data\":[");
        
        for (Object object : map.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String propertyName = entry.getKey().toString();
            String propertyValue = entry.getValue().toString().replace("\"", "\\\"");
//                  .replace("'", "&prime;").replace("\"", "\\\';");
            strBuffer.append("{\"key\":\"" + propertyName + "\",\"value\":\"" + propertyValue + "\"},");
        }
        String buffer = strBuffer.substring(0, strBuffer.length()-1);
        buffer = buffer + "]}";
        return buffer;
    }
    
    /**
     * @author wangtao
     * @return 封装list中的String值为Json对象List，数据形式类似[{'name': 1},{'name':2}]
     */
    public static String list2ComboxJson(List<String> list){
        StringBuffer strBuffer = new StringBuffer();
        if(list.isEmpty()){
            strBuffer.append("[]}");
            return strBuffer.toString();
        }
        
        strBuffer.append("[");
        
        for (String record : list) {
            strBuffer.append("{\"name\":\"" + record + "\"},");
        }
        String buffer = strBuffer.substring(0, strBuffer.length()-1);
        buffer = buffer + "]";
        return buffer;
    }

	@SuppressWarnings("unchecked")
	private static <T> T json2Bean(JSONObject jsonObject, Class<T> clazz) throws Exception {
		T entity = clazz.newInstance();

		for (Object object : jsonObject.entrySet()) {
			Map.Entry entry = (Map.Entry) object;
			String propertyName = entry.getKey().toString();
			String propertyValue = entry.getValue().toString();

			PBeanUtils.setBeanPropertyByName(entity, propertyName, propertyValue);
		}

		return entity;
	}
}

/**
 * java.sql.Date处理类.
 */
class SqlDateProcessor implements JsonValueProcessor {
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return null;
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		return arg1 == null ? "" : "" + arg1 + "";
	}
}

/**
 * 基本类型处理类.
 */
class BaseProcessor implements JsonValueProcessor {
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return null;
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		return arg1 == null ? "" : "" + arg1 + "";
	}
}

class DateJsonValueProcessor implements JsonValueProcessor {  
    private String format = " yyyy-MM-dd HH:mm:ss";  
  
    public DateJsonValueProcessor() {  
    }  
  
    public DateJsonValueProcessor(String format) {  
        this.format = format;  
    }  
  
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {  
        String[] obj = {};  
        if(value!=null)  
        {  
            if (value instanceof Date[]) {  
                SimpleDateFormat sf = new SimpleDateFormat(format);  
                Date[] dates = (Date[]) value;  
                obj = new String[dates.length];  
                for (int i = 0; i < dates.length; i++) {  
                    obj[i] = sf.format(dates[i]);  
                }  
            }  
        }  
        return obj;  
    }  
  
    public Object processObjectValue(String key, Object value,  
            JsonConfig jsonConfig) {  
        if(value!=null)  
        {  
            if (value instanceof Date) {  
                String str = new SimpleDateFormat(format).format((Date) value);  
                return  str;  
            }  
        }  
        return "";  
    }  
  
    public String getFormat() {  
        return format;  
    }  
  
    public void setFormat(String format) {  
        this.format = format;  
    }  
  
}  
