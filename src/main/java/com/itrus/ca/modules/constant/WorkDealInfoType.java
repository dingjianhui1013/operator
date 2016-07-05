package com.itrus.ca.modules.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.entity.WorkDealInfoTypeObj;

/**
 * 业务办理类型
 * @author ZhangJingtao
 *
 */
public class WorkDealInfoType {
	public static Integer TYPE_ADD_CERT = 0;//新增证书
	public static Integer TYPE_UPDATE_CERT = 1;//更新证书
	public static Integer TYPE_LOST_CHILD = 2;//遗失补办
	public static Integer TYPE_DAMAGED_REPLACED = 3;//损坏更换
	public static Integer TYPE_INFORMATION_REROUTE = 4;//信息变更
	public static Integer TYPE_REVOKE_CERT = 5;//证书吊销 
	public static Integer TYPE_ELECTRONIC_SEAL = 6;//电子签章
	public static Integer TYPE_TRUST_MOBILE = 7;//可信移动设备
	public static Integer TYPE_UNLOCK_CERT = 8;//key解锁
	public static Integer TYPE_OPEN_ACCOUNT = 9;//开户费
	public static Integer TYPE_RETURN_MONEY = 10;//退费
	public static Integer TYPE_RETURN_WORK = 11;//业务撤销
	public static Integer TYPE_PAY_REPLACED = 12;//变更缴费方式
	
	public static HashMap<Integer,String> WorkDealInfoTypeMap = new HashMap<Integer, String>();
	public static HashMap<Integer,String> WorkDealInfoTypeMapNew = new HashMap<Integer, String>();
	public static HashMap<Integer,String> WorkDealInfoTypeMapLess=new HashMap<Integer,String>();
	static{
		WorkDealInfoTypeMap.put(TYPE_ADD_CERT,"新增证书");
		WorkDealInfoTypeMap.put(TYPE_UPDATE_CERT, "更新证书");
		WorkDealInfoTypeMap.put(TYPE_LOST_CHILD, "遗失补办");
		WorkDealInfoTypeMap.put(TYPE_DAMAGED_REPLACED, "损坏更换");
		WorkDealInfoTypeMap.put(TYPE_INFORMATION_REROUTE, "信息变更");
		WorkDealInfoTypeMap.put(TYPE_REVOKE_CERT, "证书吊销");
		/*WorkDealInfoTypeMap.put(TYPE_ELECTRONIC_SEAL, "电子签章");
		WorkDealInfoTypeMap.put(TYPE_TRUST_MOBILE, "可信移动设备");*/
		WorkDealInfoTypeMap.put(TYPE_UNLOCK_CERT, "key解锁");
		/*WorkDealInfoTypeMap.put(TYPE_UNLOCK_CERT, "开户费");
		WorkDealInfoTypeMap.put(TYPE_RETURN_MONEY, "退费");*/
		WorkDealInfoTypeMap.put(TYPE_RETURN_WORK, "业务撤销");
		WorkDealInfoTypeMap.put(TYPE_PAY_REPLACED, "变更缴费方式");
		
		WorkDealInfoTypeMapNew.put(TYPE_ADD_CERT,"新增证书");
		WorkDealInfoTypeMapNew.put(TYPE_UPDATE_CERT, "更新证书");
		WorkDealInfoTypeMapNew.put(TYPE_LOST_CHILD, "遗失补办");
		WorkDealInfoTypeMapNew.put(TYPE_DAMAGED_REPLACED, "损坏更换");
		WorkDealInfoTypeMapNew.put(TYPE_INFORMATION_REROUTE, "信息变更");
		WorkDealInfoTypeMapNew.put(TYPE_REVOKE_CERT, "证书吊销");
		WorkDealInfoTypeMapNew.put(TYPE_ELECTRONIC_SEAL, "电子签章");
		WorkDealInfoTypeMapNew.put(TYPE_TRUST_MOBILE, "可信移动设备");
		WorkDealInfoTypeMapNew.put(TYPE_UNLOCK_CERT, "key解锁");
		WorkDealInfoTypeMapNew.put(TYPE_PAY_REPLACED, "变更缴费方式");
		
		WorkDealInfoTypeMapLess.put(TYPE_ADD_CERT,"新增证书");
		WorkDealInfoTypeMapLess.put(TYPE_UPDATE_CERT, "更新证书");
		WorkDealInfoTypeMapLess.put(TYPE_LOST_CHILD, "遗失补办");
		WorkDealInfoTypeMapLess.put(TYPE_DAMAGED_REPLACED, "损坏更换");
		WorkDealInfoTypeMapLess.put(TYPE_INFORMATION_REROUTE, "信息变更");
		
	}
	
	public static String getDealInfoTypeName(Integer id){
		String name = "";
		if (id!=null) {
			name = WorkDealInfoTypeMap.get(id);
			if (name==null) {
				name = "无法识别的业务类型";
			}
		}
		return name;
	}
	
	public static Integer getWorkType(int dealInfoType){
		Integer workType = null;
		if (dealInfoType==0) {
			workType = WorkType.TYPE_ADD;
		} else if (dealInfoType==1) {
			workType = WorkType.TYPE_UPDATE;
		} else if (dealInfoType==2) {
			workType = WorkType.TYPE_REISSUE;
		} else if (dealInfoType==3) {
			workType = WorkType.TYPE_DAMAGE;
		} else if (dealInfoType==4) {
			workType = WorkType.TYPE_CHANGE;
		} else if (dealInfoType==5) {
			workType = null;
		} else if (dealInfoType==6) {
			workType = null;
		} else if (dealInfoType==7) {
			workType = WorkType.TYPE_TRUST;
		} else if (dealInfoType==8) {
			workType = null;
		} else if (dealInfoType==9) {
			workType = WorkType.TYPE_OPEN;
		} 
		return workType;
	}
	
	
	public List<WorkDealInfoTypeObj> getProductTypeList(){
		List<WorkDealInfoTypeObj> list = new ArrayList<WorkDealInfoTypeObj>();
		for (Integer id: WorkDealInfoTypeMap.keySet()) {
			WorkDealInfoTypeObj obj = new WorkDealInfoTypeObj(id, WorkDealInfoTypeMap.get(id));
			list.add(obj);
		}
		return list;
	} 
	
	public List<WorkDealInfoTypeObj> getProductTypeListNew(){
		List<WorkDealInfoTypeObj> list = new ArrayList<WorkDealInfoTypeObj>();
		for (Integer id: WorkDealInfoTypeMapNew.keySet()) {
			WorkDealInfoTypeObj obj = new WorkDealInfoTypeObj(id, WorkDealInfoTypeMapNew.get(id));
			list.add(obj);
		}
		return list;
	} 
	public List<WorkDealInfoTypeObj> getProductTypeListLess(){
		List<WorkDealInfoTypeObj> list = new ArrayList<WorkDealInfoTypeObj>();
		for (Integer id: WorkDealInfoTypeMapLess.keySet()) {
			WorkDealInfoTypeObj obj = new WorkDealInfoTypeObj(id, WorkDealInfoTypeMapLess.get(id));
			list.add(obj);
		}
		return list;
	} 
	
}
