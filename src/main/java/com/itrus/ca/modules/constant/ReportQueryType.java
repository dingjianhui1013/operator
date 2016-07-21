package com.itrus.ca.modules.constant;


import java.util.HashMap;


/**
 * 报表查询类型
 * @author ZhangJingtao
 * 
 */
public class ReportQueryType {
	public static Integer TYPE_VALID_DEAL = 0;//有效业务
	public static Integer TYPE_NEW_DEAL = 1;//新增业务
	public static Integer TYPE_UPDATE_DEAL = 2;//更新业务
	public static Integer TYPE_UNUPDATE_DEAL = 3;//未更新业务
	public static Integer TYPE_MAINTENANCE_DEAL = 4;//维护业务
	
	
	
	public static HashMap<Integer,String> WorkDealInfoTypeMap = new HashMap<Integer, String>();
	
	static{
		WorkDealInfoTypeMap.put(TYPE_VALID_DEAL,"有效业务");
		WorkDealInfoTypeMap.put(TYPE_NEW_DEAL, "新增业务");
		WorkDealInfoTypeMap.put(TYPE_UPDATE_DEAL, "更新业务");
		WorkDealInfoTypeMap.put(TYPE_UNUPDATE_DEAL, "未更新业务");
		WorkDealInfoTypeMap.put(TYPE_MAINTENANCE_DEAL, "维护业务");
		
		
		
	}

}
