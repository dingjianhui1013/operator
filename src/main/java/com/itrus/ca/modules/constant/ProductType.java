package com.itrus.ca.modules.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.alibaba.druid.support.logging.Log;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
/**
 * 产品类型定义
 * 后期初始化后需要改造
 * key必须为integer
 * @author ZhangJingtao
 *
 */
public class ProductType {
	public static String PRODUCT_TYPE_ENT="企业证书";
	public static String PRODUCT_TYPE_PERE="个人证书(企业)";
	public static String PRODUCT_TYPE_PERO="个人证书(机构)";
	public static String PRODUCT_TYPE_ORG="机构证书";
	public static String PRODUCT_TYPE_MOB="可信移动设备";
//	public static String PRODUCT_TYPE_SIG="电子签章";
	public static String PRODUCT_TYPE_NULL="无法识别的产品";

	public static HashMap<Integer,String> productTypeMap = new HashMap<Integer, String>();
	public static HashMap<String,String> productTypeStrMap = new HashMap<String, String>();
	public static HashMap<String,Integer> productTypeIdMap = new HashMap<String, Integer>();
	public static HashMap<Integer,String> proTypeMap = new HashMap<Integer, String>();
	public static HashMap<Integer,String> productTypeEnglishMap = new HashMap<Integer, String>();
	
	
	static{
		productTypeMap.put(1, PRODUCT_TYPE_ENT);
		productTypeMap.put(2, PRODUCT_TYPE_PERE);
		productTypeMap.put(3, PRODUCT_TYPE_ORG);
		productTypeMap.put(4, PRODUCT_TYPE_MOB);
//		productTypeMap.put(5, PRODUCT_TYPE_SIG);
		productTypeMap.put(6, PRODUCT_TYPE_PERO);
		
		productTypeStrMap.put("1", PRODUCT_TYPE_ENT);
		productTypeStrMap.put("2", PRODUCT_TYPE_PERE);
		productTypeStrMap.put("3", PRODUCT_TYPE_ORG);
		productTypeStrMap.put("4", PRODUCT_TYPE_MOB);
//		productTypeStrMap.put("5", PRODUCT_TYPE_SIG);
		productTypeStrMap.put("6", PRODUCT_TYPE_PERO);
		
		productTypeIdMap.put(PRODUCT_TYPE_ENT,1);
		productTypeIdMap.put(PRODUCT_TYPE_PERE,2);
		productTypeIdMap.put(PRODUCT_TYPE_ORG,3);
		productTypeIdMap.put(PRODUCT_TYPE_MOB,4);
//		productTypeIdMap.put(PRODUCT_TYPE_SIG,5);
		productTypeIdMap.put(PRODUCT_TYPE_PERO,6);

		proTypeMap.put(1, PRODUCT_TYPE_ENT);
		proTypeMap.put(2, PRODUCT_TYPE_PERE);
		proTypeMap.put(3, PRODUCT_TYPE_ORG);
		proTypeMap.put(4, PRODUCT_TYPE_MOB);
		//proTypeMap.put(5, PRODUCT_TYPE_NULL);
		proTypeMap.put(6, PRODUCT_TYPE_PERO);
		
		productTypeEnglishMap.put(1, "Organizational");
		productTypeEnglishMap.put(2, "Personal");
		productTypeEnglishMap.put(3, "Personal");
		productTypeEnglishMap.put(4, "Organizational");
		productTypeEnglishMap.put(6, "Personal");
		
	}
	
	
	
	public static String getProductTypeName(Integer id){
		String name = productTypeMap.get(id);
		if (name==null) {
			return "无法识别的产品";
		}else {
			return name;
		}
	}
	
	public static List<ProductTypeObj> getProductTypeList(){
		List<ProductTypeObj> list = new ArrayList<ProductTypeObj>();
		for (Integer id: productTypeMap.keySet()) {
			ProductTypeObj obj = new ProductTypeObj(id, productTypeMap.get(id));
			list.add(obj);
		}
		return list;
	}

	public static List<ProductTypeObj> getProTypeList(){
		List<ProductTypeObj> list = new ArrayList<ProductTypeObj>();
		for (Integer id: proTypeMap.keySet()) {
			ProductTypeObj obj = new ProductTypeObj(id, proTypeMap.get(id));
			list.add(obj);
		}
		return list;
	}



	/*统计定时任务查询的列表*/
	public static List<ProductTypeObj> getProductTypeAutoTask(){
		List<ProductTypeObj> list = new ArrayList<ProductTypeObj>();
		for (Integer id: productTypeMap.keySet()) {
			if(id==1 || id==2 || id==6 ||id==3){
			ProductTypeObj obj = new ProductTypeObj(id, productTypeMap.get(id));
			list.add(obj);
				continue;
			}else{
				continue;
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		for (String string : productTypeStrMap.keySet()) {
			System.out.println(string+":"+productTypeStrMap.get(string));
		}
	}
	

}
