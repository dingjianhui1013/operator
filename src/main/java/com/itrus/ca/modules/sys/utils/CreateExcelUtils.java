package com.itrus.ca.modules.sys.utils;

import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateExcelUtils {

	/*
	 * 创建excel表格
	 */
	public static XSSFWorkbook createExcel(String[] title){
		XSSFWorkbook book = new XSSFWorkbook();
		Sheet sheet0 = book.createSheet();
		sheet0.setDefaultColumnWidth(20);				//设置表格列宽为20字节
		CellStyle style = book.createCellStyle();		//新建样式
		Font font = book.createFont();					//新建字体
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);		//设置粗体
//		font.setFontHeightInPoints((short)12);			//设置字体高度
		style.setFont(font);
		Row row0 = sheet0.createRow(0);
		Cell cell = null;
		for(int i = 0; i < title.length; i++){
			cell = row0.createCell(i, Cell.CELL_TYPE_STRING);			//创建单元格，并设置单元格类型
			cell.setCellValue(title[i]);								//单元格表头赋值
			cell.setCellStyle(style);
		}
		return book;
	}
}
