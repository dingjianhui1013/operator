/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

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
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.dao.KeyManufacturerDao;

/**
 * key厂家信息Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class KeyManufacturerService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyManufacturerService.class);
	
	@Autowired
	private KeyManufacturerDao keyManufacturerDao;
	
	public KeyManufacturer get(Long id) {
		return keyManufacturerDao.findOne(id);
	}
	
	public Page<KeyManufacturer> find(Page<KeyManufacturer> page, KeyManufacturer keyManufacturer) {
		DetachedCriteria dc = keyManufacturerDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(keyManufacturer.getName())){
			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(keyManufacturer.getName())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyManufacturer.DEL_FLAG, KeyManufacturer.DEL_FLAG_NORMAL));
		return keyManufacturerDao.find(page, dc);
	}
	
	
	public List<KeyManufacturer> findAll() {
		DetachedCriteria dc = keyManufacturerDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyManufacturer.DEL_FLAG, KeyManufacturer.DEL_FLAG_NORMAL));
		return keyManufacturerDao.find(dc);
	}
	
	public List<KeyManufacturer> findByName(String name) {
		DetachedCriteria dc = keyManufacturerDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(name)){
			dc.add(Restrictions.eq("name",name));
		}
		dc.addOrder(Order.desc("id"));
		return keyManufacturerDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(KeyManufacturer keyManufacturer) {
		keyManufacturerDao.save(keyManufacturer);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyManufacturerDao.deleteById(id);
	}
	
}
