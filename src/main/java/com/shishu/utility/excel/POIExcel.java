package com.shishu.utility.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/*******************************************************************************
 * excel导出抽象类
 * 
 * @author: yanghb
 * @since: 2009-6-5 下午03:20:16
 * @history: ***********************************************
 * @file: POIExcel.java
 * @Copyright: 2008 HundSun Electronics Co.,Ltd. All right reserved.
 ******************************************************************************/
public class POIExcel<T extends ExcelObj> {
	protected final static Log log = LogFactory.getLog(POIExcel.class);

	private List<T> data;

	public List<T> getData() {
		return data;
	}

	/**
	 * setter要导出的数据
	 * 
	 * @param data
	 * @create 2009-6-6 下午02:22:42 yanghb
	 * @history
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * excel数据导出的公共操作的抽象
	 * 
	 * @param fOut
	 * @create 2009-6-5 下午03:21:47 yanghb
	 * @history
	 */
	public void getExcel(OutputStream fOut) {
		try {
			List data = getData();

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = null;
			HSSFCell cell = null;
			HSSFRow row = null;
			List<String> rowData = null;
			String value = null;
			List<String> head = null;
			// TODO 是否需要设置系统允许导出最大行
			for (int j = 0; j < data.size(); j++) {
				ExcelObj t = (ExcelObj) data.get(j);
				if (j == 0) {
					// 创建新的Excel 工作簿

					sheet = workbook.createSheet();

					// 设置表头
					row = sheet.createRow((short) 0);
					head = t.putHead();
					for (short i = 0; i < head.size(); i++) {
						cell = row.createCell(i);
						value = head.get(i);
						int indexFlag = value.indexOf(ExcelObj.numberTypeTag);
						if (indexFlag > -1) {
							cell.setCellValue(value.substring(indexFlag
									+ ExcelObj.numberTypeTag.length()));
						} else {
							cell.setCellValue(value);
						}
						sheet.setColumnWidth(i,
								(short) (head.get(i).length() * 600));
					}
					// 冻结首行header
					// sheet.createFreezePane(head.size()+1,1);
				}
				// 填充数据到工作簿中
				row = sheet.createRow(j + 1);
				rowData = t.putRowData();
				for (short k = 0; k < rowData.size(); k++) {
					cell = row.createCell(k);
					value = "";
					if (rowData.get(k) != null) {
						value = rowData.get(k);
					}
					// 如果是序号标记，记入行号
					if (ExcelObj.squenceTag.equals(value)) {
						cell.setCellValue(j + 1);
					} else {

						if (head != null
								&& head.get(k) != null
								&& head.get(k).startsWith(
										ExcelObj.numberTypeTag)) {
							try {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(Double.valueOf(value));
							} catch (Exception e) {
								log.error("POIExcel getExcel() 导出异常: " + e);
								cell.setCellValue(value);
							}
						} else {
							cell.setCellValue(value);
						}
						if (value != null) {
							short temWidth = (short) (value.length() * 600);
							if (sheet.getColumnWidth(k) < temWidth) {
								sheet.setColumnWidth(k, temWidth);
							}
						}
					}
				}
			}

			workbook.write(fOut);
			fOut.flush();
		} catch (IOException e) {
			log.error("POIExcel getExcel() 导出异常: " + e);
		}
	}

}
