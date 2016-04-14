package com.itrus.ca.modules.constant;

import java.util.HashMap;

/*
 * 印章业务办理类型
 * */


public class SignatureInfoType {
	public static String TYPE_ADD_SIGNATURE = "0";//新增印章
	public static String TYPE_CHANGE_SIGNATURE = "1";//变更印章
	public static String TYPE_UPDATE_SIGNATURE = "2";//更新印章
	public static String TYPE_REVOKE_SIGNATURE = "3";//吊销印章
	
	
	public static HashMap<String,String> SignatureInfoTypeMap = new HashMap<String, String>();
	
	
	static{
	    SignatureInfoTypeMap.put(TYPE_ADD_SIGNATURE, "新增印章");
	    SignatureInfoTypeMap.put(TYPE_CHANGE_SIGNATURE, "变更印章");
	    SignatureInfoTypeMap.put(TYPE_UPDATE_SIGNATURE, "更新印章");
	    SignatureInfoTypeMap.put(TYPE_REVOKE_SIGNATURE, "吊销印章");
	}
	
	
	
	
	
	public static String FINANCIAL_SEAL = "1"; //财务章
	public static String CONTRACT_SEAL = "2"; //合同章
	public static String PERSONAL_SEAL = "3"; //个人章
	public static String OFFICIAL_SEAL = "4"; //公章
	
	public static HashMap<String,String> SignatureTypeMap = new HashMap<String,String>();
	
	static{
		SignatureTypeMap.put(FINANCIAL_SEAL, "财务章");
		SignatureTypeMap.put(CONTRACT_SEAL, "合同章");
		SignatureTypeMap.put(PERSONAL_SEAL, "个人章");
		SignatureTypeMap.put(OFFICIAL_SEAL, "公章");
	}
	
	
	public static String getSignatureTypeName(String type){
		String name = "";
		if (type!=null&&!type.equals("")) {
			name = SignatureTypeMap.get(type);
			if (name==null) {
				name = "无法识别的业务类型";
			}
		}
		return name;
	}
	
}
