package com.cn.jm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author admin
 *
 */
public class ExportExcelUtil {
	/** 文件存放路径 */
	private static final String FILE_PATH = PathKit.getWebRootPath() + "/excel";

	static {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static Workbook getDataFromExcel(String filePath) {
		// 判断是否为excel类型文件
		if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
			return null;
		}

		FileInputStream fis = null;
		Workbook wookbook = null;
		try {
			// 获取一个绝对地址的流
			fis = new FileInputStream(filePath);
		} catch (Exception e) {
			return null;
		}
		try {
			// 2003版本的excel，用.xls结尾
			wookbook = new HSSFWorkbook(fis);// 得到工作簿
		} catch (Exception ex) {
			try {
				// 2007版本的excel，用.xlsx结尾
				wookbook = new XSSFWorkbook(fis);// 得到工作簿
			} catch (IOException e) {
				return null;
			}
		}
		return wookbook;
	}

	/**
	 * 
	 * @Description: 导出提现订单表
	 * @author: LTS
	 * @data: 2018年12月13日下午5:14:57
	 * @param list
	 *            需要导出的数据集合
	 * @param fileName
	 *            文件名称
	 * @param header
	 *            导出的第一行标题 { "品牌中文", "品牌英文" }
	 * @param attrList
	 *            对应标题的字段集合
	 * @return
	 */
	@SuppressWarnings("resource")
	public static <M extends Model<M>> File exportFile(List<M> list, String fileName, String[] header,
			List<String> attrList) {
		File file = new File(FILE_PATH + "/" + fileName + "_" + System.currentTimeMillis() + ".xls");
		int count = 0;
		// 创建工作薄
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		// sheet:一张表的简称
		// row:表里的行
		// 创建工作薄中的工作表
		HSSFSheet hssfSheet = hssfWorkbook.createSheet();
		// 创建行
		HSSFRow row = hssfSheet.createRow(count);
		// 创建单元格，设置表头 创建列
		// 遍历表头
		for (int i = 0; i < header.length; i++) {
			// // 创建传入进来的表头的个数
			HSSFCell cell = row.createCell(i);
			// 表头的值就是传入进来的值
			cell.setCellValue(header[i]);
		}
		if (list != null) {
			// 获取所有的记录 有多少条记录就创建多少行
			for (M m : list) {
				// 把每一行的记录再次添加到表头下面 如果为空就为 "" 否则就为值
				count++;
				row = hssfSheet.createRow(count);
				int column = 0;
				for (String attr : attrList) {
					HSSFCell cell = row.createCell(column);
					String cellValue = m.getStr(attr);
					if (cellValue == null) {
						cellValue = "";
					}
					cell.setCellValue(cellValue);
					column++;
				}
			}
		}
		try {
			FileOutputStream fileOutputStreane = new FileOutputStream(file);
			hssfWorkbook.write(fileOutputStreane);
			fileOutputStreane.flush();
			fileOutputStreane.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	

	public static String getInsertValueSqlByWorkBook(Workbook workbook,List<Integer> cellNumList) {
		// 得到一个工作表
		Sheet sheet = workbook.getSheetAt(0);
		// 获得数据的总行数
		int totalRowNum = sheet.getLastRowNum();
		// 获得所有数据 将值拼接成(1,2),(1,2)形式返回
		StringBuilder insertValueSql = new StringBuilder(" ");
		final String left = "(";
		final String comma = ",";
		final String right = ")";
		final char colon = '\'';
		for (int i = 1; i <= totalRowNum; i++) {
			// 获得第i行对象
			Row row = sheet.getRow(i);
			insertValueSql.append(left);
			for(Integer cellNum : cellNumList) {
				Cell cell = row.getCell(cellNum);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String value = cell.getStringCellValue();
				insertValueSql.append(colon).append(value).append(colon).append(comma);
			}
			insertValueSql.append(right).append(comma);
		}
		return insertValueSql.substring(0, insertValueSql.length() - 1).replace(comma+right, right);
	}
}
