package com.itrus.ca.modules.constant;

import java.util.HashMap;

/**
 * 印章业务状态信息
 * @author ZhangShaoBo
 *
 */
public class SignatureInfoStatus {
	//印章业务状态
	public static String STATUS_ADD_Info = "0";//缴费完成
	public static String STATUS_ABNORMAL_USER = "1";//签章完成
	public static String STATUS_REFUND_USER = "2";//签章失败
	public static String STATUS_REVOKE_USER = "3";//已吊销
	
	//印章状态
	public static String STATUS_START = "0";//启用
	public static String STATUS_UNSTART = "1";//不可用
	public static String STATUS_UPDATE = "2";//续期
	public static String STATUS_CHANGE = "3";//变更  
	public static String STATUS_CANCEL = "4";//注销
	
	
	public static HashMap<String,String> signatureInfoMap = new HashMap<String, String>();
	

	public static HashMap<String,String> statusMap = new HashMap<String, String>();
	static{
		signatureInfoMap.put(STATUS_ADD_Info,"缴费完成");
		signatureInfoMap.put(STATUS_ABNORMAL_USER, "签章完成");
		signatureInfoMap.put(STATUS_REFUND_USER, "签章失败");
		signatureInfoMap.put(STATUS_REVOKE_USER, "已吊销");
		
		
		statusMap.put(STATUS_START,"启用");
		statusMap.put(STATUS_UNSTART, "不可用");
		statusMap.put(STATUS_UPDATE, "续期");
		statusMap.put(STATUS_CHANGE, "变更");
		statusMap.put(STATUS_CANCEL, "注销");
		
	}
}
