/**
 *2011-2-17 上午10:40:57
 */
package com.itrus.ca.common.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author : Huyvanpull
 */
public class PoiExcelHelper{

    private static String sourceFile = "d:/文化彩铃内容.xls";

    private static String outFile = "d:/测试用.xls";

    /** 总行数 */
    private int totalRows = 0;

    /** 总列数 */
    private int totalCells = 0;


    /**
     * <ul>
     * <li>Description:[根据文件名读取excel文件]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * @param fileName
     * @param sheetAt
     * @return List
     */
    public List<ArrayList<String>> read(String fileName,int sheetAt){
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();

        /** 检查文件名是否为空或者是否是Excel格式的文件 */
        if(fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){
            return dataLst;
        }

        boolean isExcel2003 = true;
        /** 对文件的合法性进行验证 */
        if(fileName.matches("^.+\\.(?i)(xlsx)$")){
            isExcel2003 = false;
        }

        /** 检查文件是否存在 */
        File file = new File(fileName);
        if(file == null || !file.exists()){
            return dataLst;
        }

        try{
            /** 调用本类提供的根据流读取的方法 */
            dataLst = read(new FileInputStream(file),sheetAt,isExcel2003);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        /** 返回最后读取的结果 */
        return dataLst;
    }
    
    
    
    public List<ArrayList<String>> read(File file,int sheetAt){
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();

        /** 检查文件名是否为空或者是否是Excel格式的文件 */
//        if(fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){
//            return dataLst;
//        }

        boolean isExcel2003 = true;
        /** 对文件的合法性进行验证 */
        if(file.getName().matches("^.+\\.(?i)(xlsx)$")){
            isExcel2003 = false;
        }

        /** 检查文件是否存在 */
        if(file == null || !file.exists()){
            return dataLst;
        }

        try{
            /** 调用本类提供的根据流读取的方法 */
            dataLst = read(new FileInputStream(file),sheetAt,isExcel2003);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        /** 返回最后读取的结果 */
        return dataLst;
    }


    /**
     * <ul>
     * <li>Description:[根据流读取Excel文件]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * 
     * @param inputStream
     * @param isExcel2003
     * @return List
     */
    public List<ArrayList<String>> read(InputStream inputStream,int sheetAt,
            boolean isExcel2003){
        List<ArrayList<String>> dataLst = null;
        try{
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream)
                    : new XSSFWorkbook(inputStream);
            dataLst = read(wb,sheetAt);
        }catch(IOException e){
            e.printStackTrace();
        }
        return dataLst;
    }

    /**
     * <ul>
     * <li>Description:[得到总行数]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * 
     * @return int
     */
    public int getTotalRows(){
        return totalRows;
    }

    /**
     * <ul>
     * <li>Description:[得到总列数]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * 
     * @return int
     */
    public int getTotalCells(){
        return totalCells;
    }

    /**
     * <ul>
     * <li>Description:[读取数据]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * 
     * @param wb
     * @return List
     */
    private List<ArrayList<String>> read(Workbook wb,int sheetAt){
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();

        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(sheetAt);
        this.totalRows = sheet.getPhysicalNumberOfRows();
        if(this.totalRows >= 1 && sheet.getRow(0) != null){
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        /** 循环Excel的行 */
        for(int r = 0;r < this.totalRows;r ++ ){
            Row row = sheet.getRow(r);
            if(row == null){
                continue;
            }

            ArrayList<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            for(short c = 0;c < this.getTotalCells();c ++ ){
                Cell cell = row.getCell(c);
                String cellValue = "";
                if(cell == null){
                    rowLst.add(cellValue);
                    continue;
                }

                /** 处理数字型的,自动去零 */
                if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){                	
                    /** 在excel里,日期也是数字,在此要进行判断 */
                    if(HSSFDateUtil.isCellDateFormatted(cell)){                       
                        cellValue = new Long(cell.getDateCellValue().getTime()).toString();
                    }else{                    	
                        cellValue = getRightStr(cell.getNumericCellValue() + "");
                    }
                }
                /** 处理字符串型 */
                else if(Cell.CELL_TYPE_STRING == cell.getCellType()){
                    cellValue = cell.getStringCellValue();
                }
                /** 处理布尔型 */
                else if(Cell.CELL_TYPE_BOOLEAN == cell.getCellType()){
                    cellValue = cell.getBooleanCellValue() + "";
                }
                /** 其它的,非以上几种数据类型 */
                else{
                    cellValue = cell.toString() + "";
                }

                rowLst.add(cellValue);
            }
            dataLst.add(rowLst);
        }
        return dataLst;
    }

    /**
     * <ul>
     * <li>Description:[正确地处理整数后自动加零的情况]</li>
     * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
     * <li>Midified by [modifier] [modified time]</li>
     * <ul>
     * 
     * @param sNum
     * @return String
     */
    private String getRightStr(String sNum){
        DecimalFormat decimalFormat = new DecimalFormat("#.000000");
        String resultStr = decimalFormat.format(new Double(sNum));
        if(resultStr.matches("^[-+]?\\d+\\.[0]+$")){
            resultStr = resultStr.substring(0,resultStr.indexOf("."));
        }
        return resultStr;
    }

    private static void exportExcel() throws FileNotFoundException{
        FileOutputStream os = new FileOutputStream(new File(outFile));
    }

    private static void test() throws IOException{
        FileWriter fw = new FileWriter("d:\\hello.csv");

        for(int i = 0;i < 300000;i ++ ){
            System.out.println(i);
            fw.write("aaa,bbb,ccc\r\n");
        }
        fw.close();
    }


    public static void main(String[] args) throws Exception{
        // List<ArrayList<String>> dataLst = new
        // PoiExcelHelper().read(sourceFile,5);
        // System.out.println("文件总行数："+dataLst.size());
        //        
        // for(int i=0;i<dataLst.size();i++){
        // StringBuffer sb = new StringBuffer();
        // List<String> l = dataLst.get(i);
        // for(int j=0;j<l.size();j++){
        // sb.append(l.get(j)).append("\t");
        // }
        // if(!StringHelper.isNull(sb.toString()))
        // System.out.println(sb.toString());
        // }
        //        

        test();

    }
}
