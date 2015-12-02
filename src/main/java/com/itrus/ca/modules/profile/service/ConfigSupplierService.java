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
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.profile.dao.ConfigSupplierDao;

/**
 * 供应商Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class ConfigSupplierService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigSupplierService.class);
	
	@Autowired
	private ConfigSupplierDao configSupplierDao;
	
	public ConfigSupplier get(Long id) {
		return configSupplierDao.findOne(id);
	}
	
	public Page<ConfigSupplier> find(Page<ConfigSupplier> page, ConfigSupplier configSupplier) {
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configSupplier.getSupplierName())){
			dc.add(Restrictions.like("supplierName", "%"+EscapeUtil.escapeLike(configSupplier.getSupplierName())+"%"));
		}
		dc.add(Restrictions.eq("delFlag", configSupplier.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configSupplierDao.find(page, dc);
	}
	
	public Page<ConfigSupplier> findByKey(Page<ConfigSupplier> page, ConfigSupplier configSupplier) {
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configSupplier.getSupplierName())){
			dc.add(Restrictions.like("supplierName", "%"+EscapeUtil.escapeLike(configSupplier.getSupplierName())+"%"));
		}
		dc.add(Restrictions.eq("supplierType",1));
		dc.add(Restrictions.eq("delFlag", configSupplier.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configSupplierDao.find(page, dc);
	}

	public ConfigSupplier findBySupplierName(String  supplierName,Long supplierId){
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		dc.add(Restrictions.eq("supplierName", supplierName));
		ConfigSupplier configSupplier = new ConfigSupplier();
		dc.add(Restrictions.eq("delFlag", configSupplier.DEL_FLAG_NORMAL));
		if (supplierId!=null) {
			dc.add(Restrictions.ne("id", supplierId));
		}
		if (configSupplierDao.find(dc).size()>0) {
			return configSupplierDao.find(dc).get(0);
		}else {
			return null;
		}
	}
	
	public List<ConfigSupplier> findByCert(){
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		dc.add(Restrictions.eq("supplierType", 0));
		dc.add(Restrictions.eq("delFlag", ConfigSupplier.DEL_FLAG_NORMAL));
		return configSupplierDao.find(dc);
	}
	
	public List<ConfigSupplier> findByKey(){
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		dc.add(Restrictions.eq("supplierType", 1));
		dc.add(Restrictions.eq("delFlag", ConfigSupplier.DEL_FLAG_NORMAL));
		return configSupplierDao.find(dc);
	}
	public ConfigSupplier findByOneSupplierId(Long supplierId) {
		return configSupplierDao.findOne(supplierId);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(ConfigSupplier configSupplier) {
		configSupplierDao.save(configSupplier);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configSupplierDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	public List<ConfigSupplier> findAllSupplier() {
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ConfigSupplier.DEL_FLAG,ConfigSupplier.DEL_FLAG_NORMAL));
		return configSupplierDao.find(dc);
	}
	
	public ConfigSupplier findByName(String  supplierName){
		DetachedCriteria dc = configSupplierDao.createDetachedCriteria();
		dc.add(Restrictions.eq("supplierName", supplierName));
		ConfigSupplier configSupplier = new ConfigSupplier();
		dc.add(Restrictions.eq("delFlag", configSupplier.DEL_FLAG_NORMAL));
		if (configSupplierDao.find(dc).size()>0) {
			return configSupplierDao.find(dc).get(0);
		}else {
			return null;
		}
	}
	
	
	
}
