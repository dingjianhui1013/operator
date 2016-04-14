/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.self.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.dao.SelfImageDao;

/**
 * 证件图片Service
 * @author HuHao
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class SelfImageService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SelfImageService.class);
	
	@Autowired
	private SelfImageDao selfImageDao;
	
	public SelfImage get(Long id) {
		return selfImageDao.findOne(id);
	}
	
	public Page<SelfImage> find(Page<SelfImage> page, SelfImage selfImage) {
		DetachedCriteria dc = selfImageDao.createDetachedCriteria();
		dc.add(Restrictions.eq("status", BaseEntity.SHOW));
		dc.addOrder(Order.desc("id"));
		return selfImageDao.find(page, dc);
	}
	
	
	public SelfImage findByApplicationId(Long selfApplicationId ){
		DetachedCriteria dc = selfImageDao.createDetachedCriteria();
		dc.add(Restrictions.eq("selfApplication.id",selfApplicationId ));
		dc.addOrder(Order.desc("id"));
		return selfImageDao.find(dc).get(0);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(SelfImage selfImage) {
		selfImageDao.save(selfImage);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		selfImageDao.deleteById(id);
	}
	
}
