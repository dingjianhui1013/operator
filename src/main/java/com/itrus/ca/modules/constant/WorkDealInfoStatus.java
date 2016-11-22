package com.itrus.ca.modules.constant;

import java.util.HashMap;

/**
 * 业务表的状态信息
 * @author ZhangShaoBo
 *
 */
public class WorkDealInfoStatus {
	public static String STATUS_ADD_USER = "0";//新增用户
	public static String STATUS_ABNORMAL_USER = "1";//异常业务
	public static String STATUS_REFUND_USER = "2";//退费用户
	public static String STATUS_APPROVE_FAILURE = "4";//审核不通过 -->验证拒绝
	public static String STATUS_IDENTIFY_FAILURE = "17";//鉴别拒绝
	public static String STATUS_ENTRY_SUCCESS = "5";//录入成功  第一个界面录入完成
	public static String STATUS_CERT_REVOKE = "6";//吊销。。业务完成
	public static String STATUS_CERT_OBTAINED = "7";//已获取
	public static String STATUS_CERT_TEMPED = "8";//临时保存
	public static String STATUS_CERT_WAIT = "9";//等待制证
	public static String STATUS_CERT_UNABLE = "10";//已失效
	public static String STATUS_OBTAINED_WAIT = "11";//待获取
	public static String STATUS_UPDATE_USER = "12";//未缴费，待审核（客户端更新可用）
	public static String STATUS_APPROVE_WAIT = "13";//已审核，待制证      --》  已验证，待制证
	public static String STATUS_VERIFICATE_WAIT = "16";//已鉴别，待验证
	
	public static String STATUS_UPDATE_FALLBACK_INFO = "15";//更新返回填写信息
	
public static HashMap<String,String> WorkDealInfoStatusMap = new HashMap<String, String>();
	
	static{
		WorkDealInfoStatusMap.put(STATUS_ADD_USER,"已缴费，待审核");
		WorkDealInfoStatusMap.put(STATUS_ABNORMAL_USER, "制证失败");
		WorkDealInfoStatusMap.put(STATUS_REFUND_USER, "已退费");
		WorkDealInfoStatusMap.put(STATUS_APPROVE_FAILURE, "验证拒绝");
		WorkDealInfoStatusMap.put(STATUS_IDENTIFY_FAILURE, "鉴别拒绝");	
		WorkDealInfoStatusMap.put(STATUS_ENTRY_SUCCESS, "录入成功");
		WorkDealInfoStatusMap.put(STATUS_CERT_TEMPED, "录入暂存");
		WorkDealInfoStatusMap.put(STATUS_CERT_REVOKE, "已吊销");
		WorkDealInfoStatusMap.put(STATUS_CERT_OBTAINED, "业务完成");
		WorkDealInfoStatusMap.put(STATUS_CERT_WAIT, "等待制证");
		WorkDealInfoStatusMap.put(STATUS_CERT_UNABLE, "已失效");
		WorkDealInfoStatusMap.put(STATUS_OBTAINED_WAIT, "审核通过");
		WorkDealInfoStatusMap.put(STATUS_UPDATE_USER, "未缴费，待审核");
		WorkDealInfoStatusMap.put(STATUS_UPDATE_FALLBACK_INFO, "更新返回填写信息");
		WorkDealInfoStatusMap.put(STATUS_APPROVE_WAIT, "已验证，待制证");
		WorkDealInfoStatusMap.put(STATUS_VERIFICATE_WAIT, "已鉴别，待验证");
	}
}
