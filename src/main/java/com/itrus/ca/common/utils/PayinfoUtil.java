package com.itrus.ca.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成付费流水号
 * @author ZhangJingtao
 *
 */
public class PayinfoUtil {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * 生成流水号
	 * @return
	 */
	public static String getPayInfoNo(){
		return sdf.format(new Date());
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(PayinfoUtil.getPayInfoNo());;
	}
}
