package com.shishu.utility.excel;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.shishu.utility.bean.PBeanUtils;



/**
 * 
 * Sheet配置类
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:31:08
 */
@SuppressWarnings("unchecked")
public class ExcelSheetConfig {

    public static final int VERTICAL_ALIGN = 0;


    public static final int HORIZONTAL_ALIGN = 1;
    private Map<String,ExcelColumnParam> labels = new HashMap<String,ExcelColumnParam>();

    private List<String> propNames = new ArrayList<String>();

    private int exportAlign;

    private int beginRow;

    private int beginCol;

    //SheetName
    private String sheetName;

    private Collection dataSource = new ArrayList();

    public static ExcelSheetConfig createExcelSheetConfig(JSONArray array,List list){
    	ExcelSheetConfig sheet = new ExcelSheetConfig();
    	
    	putLabelToExcelSheetConfig(array, sheet);
    	List dataList = new ArrayList();
    	for(int i=0;i<list.size();i++){
    		Object object = list.get(i);
    		Map mapdata = sheet.getMapData(object,sheet.getPropertyNamesEx());
    		dataList.add(mapdata);
    	}
    	sheet.setDataSource(dataList);
    	
    	return sheet;
    }
    
    public static ExcelSheetConfig createExcelSheetConfigByMapList(JSONArray array,List<Map<String,Object>> mapList){
    	ExcelSheetConfig sheet = new ExcelSheetConfig();


    	putLabelToExcelSheetConfig(array, sheet);
    	sheet.setDataSource(mapList);
    	
    	return sheet;
    }
    
	private static void putLabelToExcelSheetConfig(JSONArray array, ExcelSheetConfig sheet) {
		for (int j = 0; j < array.size(); j++) {
    		ExcelColumnParam  columnParam = new ExcelColumnParam();
    		columnParam.setFieldName(array.getJSONObject(j).getString("fieldName"));
    		columnParam.setLabel(array.getJSONObject(j).getString("label"));
    		columnParam.setLabelWidth((short)(array.getJSONObject(j).getInt("labelWidth")*50));
            sheet.putLabel(columnParam.getFieldName(),columnParam);
		}
	}
    
    public Map getMapData(Object srcObject, List propes) {

		Map map = new HashMap();
		String propertyName;
		for (int i = 0; i < propes.size(); i++) {
			propertyName = (String) propes.get(i);
			Object propertyValue;
			try {
				propertyValue = PBeanUtils.GetBeanPropertyValue(srcObject, propertyName);
				map.put(propertyName, propertyValue);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return map;
	}
    /**
	 * @param propertyName
	 * @param columnParam
	 * 
	 */
    public void putLabel(String propertyName, ExcelColumnParam columnParam) {
        if (StringUtils.isEmpty(propertyName)) {
            return;
        }

        if (!propNames.contains(propertyName)) {
            propNames.add(propertyName);
        }

        labels.put(propertyName, columnParam);
    }
    /**
     * @method getLabel
     * @param propertyName
     * @return String
     * @function getLabel
     */
    public String getLabel(String propertyName) {
        return  (labels.get(propertyName)).getLabel();
    }
    
    public short getLabelWidth(String propertyName) {
        return  (labels.get(propertyName)).getLabelWidth();
    }
    /**
     * @method getDataSource
     * @return Collection
     * @function getDataSource
     */
    public Collection getDataSource() {
        return dataSource;
    }

    /**
     * @method setDataSource
     * @param dataSource
     * @function setDataSource
     */
    public void setDataSource(Collection dataSource) {
        if (dataSource == null) {
            this.dataSource.clear();
        }

        this.dataSource = dataSource;
    }

    public int getExportAlign() {
        return exportAlign;
    }

    /**
     * @method setExportAlign
     * @param exportAlign
     * @function setExportAlign
     */
    public void setExportAlign(int exportAlign) {
        this.exportAlign = exportAlign;
    }

    /**
     * @method getBeginCol
     * @return int
     * @function getBeginCol
     */
    public int getBeginCol() {
        return beginCol;
    }

    /**
     * @method setBeginCol
     * @param beginCol
     * @function setBeginCol
     */
    public void setBeginCol(int beginCol) {
        this.beginCol = beginCol;
    }

    /**
     * @method getBeginRow
     * @return int
     * @function getBeginRow
     */
    public int getBeginRow() {
        return beginRow;
    }

    /**
     * @method setBeginRow
     * @param beginRow
     * @function setBeginRow
     */
    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    /**
     * @method getPropertyNames
     * @return Iterator
     * @function getPropertyNames
     */
    public Iterator getPropertyNames() {
        return propNames.iterator();
    }
    public List getPropertyNamesEx() {
        return propNames;
    }

    /**
     * @method getPropertySize
     * @return int
     * @function getPropertySize
     */
    public int getPropertySize() {
        return propNames.size();
    }

    /**
     * @method getSheetName
     * @return String
     * @function getSheetName
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * @method setSheetName
     * @param sheetName
     * @function setSheetName
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
