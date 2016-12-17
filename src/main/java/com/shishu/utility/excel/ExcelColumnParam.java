package com.shishu.utility.excel;

/**
 * excel列参数
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:32:20
 */
public class ExcelColumnParam {
	private String fieldName;
	private String label;
	private short labelWidth;
	public short getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(short labelWidth) {
		this.labelWidth = labelWidth;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
