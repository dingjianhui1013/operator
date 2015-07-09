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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.dao.ConfigSupplierDao;
import com.itrus.ca.modules.profile.dao.ConfigSupplierProductRelationDao;

/**
 * 供应商产品配置Service
 * @author ZhangShaoBo
 * @version 2014-06-09
 */
@Component
@Transactional(readOnly = true)
public class ConfigSupplierProductRelationService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigSupplierProductRelationService.class);
	
	@Autowired
	private ConfigSupplierProductRelationDao configSupplierProductRelationDao;
	
	@Autowired
	private ConfigSupplierDao configSupplierDao;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	public ConfigSupplierProductRelation get(Long id) {
		return configSupplierProductRelationDao.findOne(id);
	}
	
	public Page<ConfigSupplierProductRelation> find(Page<ConfigSupplierProductRelation> page, ConfigSupplierProductRelation configSupplierProductRelation) {
		DetachedCriteria dc = configSupplierProductRelationDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		dc.createAlias("configSupplier", "configSupplier");
		//按供应商名称查询
		if (configSupplierProductRelation.getConfigSupplier()!=null&&!com.itrus.ca.common.utils.StringUtils.isEmpty(configSupplierProductRelation.getConfigSupplier().getSupplierName())) {
			dc.add(Restrictions.like("configSupplier.supplierName", "%"+EscapeUtil.escapeLike(configSupplierProductRelation.getConfigSupplier().getSupplierName())+"%"));
		}
		//按产品名称查询
		if (configSupplierProductRelation.getProductType()!=null) {
			dc.add(Restrictions.eq("productType", configSupplierProductRelation.getProductType()));
		}
		dc.add(Restrictions.eq("configSupplier.delFlag", ConfigSupplier.DEL_FLAG_NORMAL));
		return configSupplierProductRelationDao.find(page, dc);
	}
	
	public List<ConfigSupplierProductRelation> findAll(){
		List<ConfigSupplierProductRelation> list = configSupplierProductRelationDao.find("from ConfigSupplierProductRelation", null);
		return list;
	}
	
	public List<ConfigSupplierProductRelation> findBySupplier(ConfigSupplier configSupplier){
		DetachedCriteria dc = configSupplierProductRelationDao.createDetachedCriteria();
		dc.add(Restrictions.like("configSupplier.id",configSupplier.getId()));
		List<ConfigSupplierProductRelation> list = configSupplierProductRelationDao.find(dc);
		return list;
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigSupplierProductRelation configSupplierProductRelation) {
		configSupplierProductRelationDao.save(configSupplierProductRelation);
	}
	
	
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configSupplierProductRelationDao.delete(id);
	}
	
}
