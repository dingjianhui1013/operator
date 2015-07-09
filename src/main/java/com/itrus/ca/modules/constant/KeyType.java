package com.itrus.ca.modules.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.itrus.ca.modules.key.entity.KeyTypeObj;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
/**
 * 产品类型定义
 * 后期初始化后需要改造
 * @author ZhangJingtao
 *
 */
public class KeyType {
	public static String KEY_TYPE_ENT="蓝天";
	public static String KEY_TYPE_PERE="白云";
	public static String KEY_TYPE_PERO="黑土";
	public static String KEY_TYPE_ORG="黄沙";
	public static HashMap<Integer,String> KeyTypeMap = new HashMap<Integer, String>();
	public static HashMap<String,String> KeyTypeStrMap = new HashMap<String, String>();
	
	static{
		KeyTypeMap.put(1, KEY_TYPE_ENT);
		KeyTypeMap.put(2, KEY_TYPE_PERE);
		KeyTypeMap.put(3, KEY_TYPE_PERO);
		KeyTypeMap.put(4, KEY_TYPE_ORG);
		
		KeyTypeStrMap.put("1", KEY_TYPE_ENT);
		KeyTypeStrMap.put("2", KEY_TYPE_PERE);
		KeyTypeStrMap.put("3", KEY_TYPE_PERO);
		KeyTypeStrMap.put("4", KEY_TYPE_ORG);
	}
	
	
	
	public static String getKeyTypeName(Integer id){
		String name = KeyTypeMap.get(id);
		if (name==null) {
			return "无法识别的产品";
		}else {
			return name;
		}
	}
	
	public List<KeyTypeObj> getTypeList(){
		List<KeyTypeObj> list = new ArrayList<KeyTypeObj>();
		for (Integer id: KeyTypeMap.keySet()) {
			KeyTypeObj obj = new KeyTypeObj(id, KeyTypeMap.get(id));
			list.add(obj);
		}
		return list;
	} 
	

}
