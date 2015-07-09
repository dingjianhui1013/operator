/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.dao.ConfigChargeSupplierDetailDao;

/**
 * 供应商计费配置Service
 * @author ZhangShaoBo
 * @version 2014-06-11
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeSupplierDetailService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeSupplierDetailService.class);
	
	@Autowired
	private ConfigChargeSupplierDetailDao configChargeSupplierDetailDao;
	
	public ConfigChargeSupplierDetail get(Long id) {
		return configChargeSupplierDetailDao.findOne(id);
	}
	
	public Page<ConfigChargeSupplierDetail> find(Page<ConfigChargeSupplierDetail> page, ConfigChargeSupplierDetail configChargeSupplierDetail) {
		DetachedCriteria dc = configChargeSupplierDetailDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return configChargeSupplierDetailDao.find(page, dc);
	}
	
	public List<ConfigChargeSupplierDetail> findByChargeSupplierId(ConfigSupplierProductRelation configSupplierProductRelation){
		DetachedCriteria dc = configChargeSupplierDetailDao.createDetachedCriteria();
		dc.add(Restrictions.like("configSupplierProductRelation.id",configSupplierProductRelation.getId()));
		List<ConfigChargeSupplierDetail> list = configChargeSupplierDetailDao.find(dc);
		return list;
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigChargeSupplierDetail configChargeSupplierDetail) {
		configChargeSupplierDetailDao.save(configChargeSupplierDetail);
	}
	
	@Transactional(readOnly = false)
	public void deleteList(List<ConfigChargeSupplierDetail> list) {
		configChargeSupplierDetailDao.delete(list);
	}

	@Transactional(readOnly = false)
	public List<ConfigChargeSupplierDetail> findAll() {
		DetachedCriteria dc = configChargeSupplierDetailDao.createDetachedCriteria();
		return configChargeSupplierDetailDao.find(dc);
	}
	
}
