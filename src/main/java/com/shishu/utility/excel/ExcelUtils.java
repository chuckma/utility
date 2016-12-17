package com.shishu.utility.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * 
 * Excel相关工具类
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月24日 下午5:53:37
 */
public class ExcelUtils {
    private static final Log     log              = LogFactory.getLog(ExcelUtils.class);
    private static BeanUtilsBean beanUtilsBean    = new BeanUtilsBean();
    private static final String  DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String  DATE_FORMAT      = "yyyy-MM-dd";
    private static final String  TIME_FORMAT      = "HH:mm:ss";

    /**
     * @author: qiaoel@zjhcsosft.com
     * @date: 2014年2月24日 下午5:54:11
     */
    private static class DateConverter implements Converter {
        private Converter stringConverter = new StringConverter();

        public Object convert(Class type, Object value) {
            if (value instanceof java.sql.Timestamp) {
                DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);

                return format.format(value);
            } else if (value instanceof java.sql.Time) {
                DateFormat format = new SimpleDateFormat(TIME_FORMAT);

                return format.format(value);
            } else if (value instanceof java.sql.Date) {
                DateFormat format = new SimpleDateFormat(DATE_FORMAT);

                return format.format(value);
            } else if (value instanceof java.util.Date) {
                DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);

                return format.format(value);
            } else {
                return stringConverter.convert(type, value);
            }
        }
    }

    static {
        //date convert to String
        beanUtilsBean.getConvertUtils().register(new DateConverter(), String.class);
    }

    /**
     * data export on excel by sheetConfig of data and contents.
     *
     * @param sheetConfig
     * @param output
     */
    public static void exportToExcel(ExcelSheetConfig sheetConfig, OutputStream output)
            throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("begin export Excel file");
        }

        checkArgument(sheetConfig, sheetConfig.getDataSource(), output);

        HSSFWorkbook wb = new HSSFWorkbook();

        exportToExcel(sheetConfig, wb);

        try {
            wb.write(output);
        } catch (IOException e) {
            String msg = "excel export wrong ";

            log.error(msg, e);
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("sucess export" + sheetConfig.getDataSource().size() + "record");
        }
    }

    /**
     * sheetConfigs's Array Config ,let It'a data  export to sheet (eetConfigs.length-1), exceloutput
     *
     * @param sheetConfigs
     * @param output
     */
    public static void exportToExcel(ExcelSheetConfig[] sheetConfigs, OutputStream output)
            throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("begin export Excel");
        }

        if ((sheetConfigs == null) || (sheetConfigs.length == 0)) {
            String msg = "WorkSheetConfig must not null";

            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        HSSFWorkbook wb = new HSSFWorkbook();

        for (int i = 0; i < sheetConfigs.length; i++) {
            ExcelSheetConfig sheetConfig = sheetConfigs[i];
            Collection  exportData = sheetConfig.getDataSource();

            checkArgument(sheetConfig, exportData, output);
            exportToExcel(sheetConfig, wb);
        }

        try {
            wb.write(output);
        } catch (IOException e) {
            String msg = "excel xport wrong";

            log.error(msg, e);
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("success export" + sheetConfigs.length + "of Sheet");
        }
    }

    /**
     * @param sheetConfig
     * @param output
     * @param exportData
     * @param wb
     */
    private static void exportToExcel(ExcelSheetConfig sheetConfig, HSSFWorkbook wb) {
        Collection exportData = sheetConfig.getDataSource();
        HSSFSheet  sheet = wb.createSheet();

        //set work sheetname
        if (!StringUtils.isEmpty(sheetConfig.getSheetName())) {
            String sheetName = sheetConfig.getSheetName();

            if (wb.getSheet(sheetName) != null) {
                sheetName += wb.getNumberOfSheets();
            }

            wb.setSheetName(wb.getNumberOfSheets() - 1, sheetConfig.getSheetName());
        }

        //set excel show style
        HSSFCellStyle labelStyle = wb.createCellStyle();

        labelStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        labelStyle.setFillBackgroundColor((short) 0);

        HSSFFont labelFont = wb.createFont();

        labelFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        labelFont.setColor(HSSFFont.COLOR_NORMAL);
        labelStyle.setFont(labelFont);

        if (sheetConfig.getExportAlign() == ExcelSheetConfig.VERTICAL_ALIGN) {
            verticalExport(sheetConfig, exportData, sheet, labelStyle);
        } else if (sheetConfig.getExportAlign() == ExcelSheetConfig.HORIZONTAL_ALIGN) {
            horizontalExport(sheetConfig, exportData, sheet, labelStyle);
        }
    }

    /**
     * portrait export
     *
     * @param sheetConfig
     * @param exportData
     * @param sheet
     * @param labelStyle
     */
    private static void verticalExport(ExcelSheetConfig sheetConfig, Collection exportData,
                                       HSSFSheet sheet, HSSFCellStyle labelStyle) {
        int beginCol = sheetConfig.getBeginCol();
        int beginRow = sheetConfig.getBeginRow();
        int index    = 0;

        //export title
        HSSFRow  row = sheet.createRow(sheetConfig.getBeginRow());
        Iterator it = sheetConfig.getPropertyNames();

        while (it.hasNext()) {
            String propertyName = (String) it.next();

            if (StringUtils.isEmpty(propertyName)) {
                continue;
            }

            String labelName = sheetConfig.getLabel(propertyName);

            if (StringUtils.isEmpty(labelName)) {
                continue;
            }
            //add by chenzy
            sheet.setColumnWidth(index,sheetConfig.getLabelWidth(propertyName));
            HSSFCell cell = createCell(row, (short) (beginCol + index));

            cell.setCellValue(labelName);
            cell.setCellStyle(labelStyle);
            index++;
        }

        it = exportData.iterator();

        //export row data ֵ
        int rowIndex = beginRow + 1;

        while (it.hasNext()) {
            Object elem = it.next();

            row = sheet.createRow(rowIndex);

            if (elem == null) {
                rowIndex++;
                continue;
            }

            Iterator propIt       = sheetConfig.getPropertyNames();
            String   propertyName = null;

            //export column dataֵ
            int colIndex = beginCol;

            while (propIt.hasNext()) {
                propertyName = (String) propIt.next();

                if (StringUtils.isEmpty(propertyName)) {
                    continue;
                }

                HSSFCell cell          = createCell(row, colIndex);
                String   propertyValue = null;

                try {
                    propertyValue = beanUtilsBean.getProperty(elem, propertyName);
                } catch (Exception e) {
                    String msg = "get java bean wrong";

                    log.error(msg, e);
                    throw new RuntimeException(msg, e);
                }

                cell.setCellValue(propertyValue);
                colIndex++;
            }

            rowIndex++;
        }
    }

    /**
	 * landscape orientation export
	 * 
	 * @param sheetConfig
	 * @param exportData
	 * @param sheet
	 * @param labelStyle
	 */
	private static void horizontalExport(ExcelSheetConfig sheetConfig, Collection exportData, HSSFSheet sheet,
			HSSFCellStyle labelStyle) {
		int beginCol = sheetConfig.getBeginCol();
		int index = 0;
		Iterator it = sheetConfig.getPropertyNames();

		while (it.hasNext()) {
			HSSFRow row = sheet.createRow(sheetConfig.getBeginRow() + index);
			String propertyName = (String) it.next();

			if (StringUtils.isEmpty(propertyName)) {
				continue;
			}

			String labelName = sheetConfig.getLabel(propertyName);

			if (StringUtils.isEmpty(labelName)) {
				continue;
			}

			HSSFCell cell = createCell(row, (short) (beginCol));

			cell.setCellValue(labelName);
			cell.setCellStyle(labelStyle);
			index++;
		}

		it = exportData.iterator();

		// export every rowֵ
		int colIndex = beginCol + 1;

		while (it.hasNext()) {
			Object elem = it.next();

			if (elem == null) {
				colIndex++;
				continue;
			}

			Iterator propIt = sheetConfig.getPropertyNames();
			String propertyName = null;

			// export every columnֵ
			index = 0;

			while (propIt.hasNext()) {
				HSSFRow row = sheet.getRow(sheetConfig.getBeginRow() + index);

				propertyName = (String) propIt.next();

				if (StringUtils.isEmpty(propertyName)) {
					continue;
				}

				HSSFCell cell = createCell(row, colIndex);
				String propertyValue = null;

				try {
					propertyValue = beanUtilsBean.getProperty(elem, propertyName);
				} catch (Exception e) {
					String msg = "get java bean wrong";

					log.error(msg, e);
					throw new RuntimeException(msg, e);
				}

				cell.setCellValue(propertyValue);
				index++;
			}

			colIndex++;
		}
	}

    /**
	 * @param row
	 * @param colIndex
	 * 
	 * @return
	 */
    private static HSSFCell createCell(HSSFRow row, int colIndex) {
        HSSFCell cell = row.createCell(colIndex);

        // set cell,let it sustain china
        return cell;
    }

    /**
	 * check argument
	 * 
	 * @param label
	 * @param data
	 * @param output
	 * @param beginRow
	 * @param beginCol
	 */
    private static void checkArgument(ExcelSheetConfig sheetConfig, Collection exportData,
                                      OutputStream output) {
        if (sheetConfig == null) {
            String msg = "sheetConfig must be not null";

            log.error(msg);
            throw new NullPointerException();
        }

        if (sheetConfig.getPropertySize() == 0) {
            String msg = "not set Label,need to set bean property and name";

            log.error(msg);
            throw new NullPointerException();
        }

        if (exportData == null) {
            String msg = "must be set export data";

            log.error(msg);
            throw new NullPointerException(msg);
        }

        if (output == null) {
            String msg = "output must be not null";

            log.error(msg);
            throw new NullPointerException(msg);
        }

        if (sheetConfig.getBeginRow() < 0) {
            String msg = "begin row must be big than 0";

            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (sheetConfig.getBeginCol() < 0) {
            String msg = "begin column must big than 0";

            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if ((sheetConfig.getExportAlign() == ExcelSheetConfig.HORIZONTAL_ALIGN)
                    && ((sheetConfig.getBeginCol() + exportData.size()) > 1000000)) {
            String msg = "data must be less than "+ (1000000 - sheetConfig.getBeginCol()) + "record";

            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if ((sheetConfig.getExportAlign() == ExcelSheetConfig.VERTICAL_ALIGN)
                    && ((sheetConfig.getBeginCol() + exportData.size()) > 1000000)) {
            String msg = "column must be less than" + (1000000 - sheetConfig.getBeginRow()) + "column";

            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /**
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<String[]> readExcel(String filePath) {

		List<String[]> list = new ArrayList<String[]>();

		File file = new File(filePath);
		Workbook wb = null;

		try {
			wb = Workbook.getWorkbook(file);
			if (wb != null) {
				Sheet[] sheets = wb.getSheets();
				if (sheets != null && sheets.length > 0) {
					Sheet sheet = sheets[0];

					int rowNum = sheet.getRows();

					for (int i = 0; i < rowNum; i++) {
						Cell[] cells = sheet.getRow(i);
						if (cells != null && cells.length > 0) {
							String[] datas = new String[cells.length];

							int j = 0;
							for (Cell cell : cells) {
								datas[j] = cell.getContents();
								j++;
							}

							list.add(datas);
						}
					}
				}

			}
		} catch (IOException e) {
			log.error("readExcel(String)", e);
		} catch (BiffException e) {
			log.error("readExcel(String)", e);
		} finally {
			if (wb != null)
				wb.close();
		}

		return list;
	}
}
