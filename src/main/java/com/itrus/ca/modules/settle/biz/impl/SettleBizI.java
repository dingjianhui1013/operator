package com.itrus.ca.modules.settle.biz.impl;

import com.itrus.ca.modules.profile.entity.ConfigSupplier;

/**
 * 自动供应商结算
 * 
 * @see{SettleSupplierCertDetail}
 * @author ZhangJingtao
 *
 */
public interface SettleBizI {
	/**
	 * 每次安装证书成功后需要调用一次，用于更新ou下证书的数量统计
	 * ou是签发证书时RA配置中的部门信息orgUnit
	 * @param ou
	 * @param type 增加类型，包括替换1、吊销2、测试3、非人为补签证书4、人为补签证书5、变更信息补签证书6
	 * @param amount 增加数量，通常是1x
	 * @param year 年限
	 * @param productType @see{ProductType}
	 * @param supplier 供应商
	 * @return
	 */
	public boolean updateOUSettleInfo(String ou,Integer productType,Integer type,Integer amount,Integer year,ConfigSupplier supplier);
}
