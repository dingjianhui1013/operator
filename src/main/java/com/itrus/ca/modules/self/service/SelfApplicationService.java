/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.self.service;

import java.util.Date;

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
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.modules.self.entity.SelfApplication;
import com.itrus.ca.modules.self.dao.SelfApplicationDao;

/**
 * 申请表Service
 * @author HuHao
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class SelfApplicationService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SelfApplicationService.class);
	
	@Autowired
	private SelfApplicationDao selfApplicationDao;
	
	public SelfApplication get(Long id) {
		return selfApplicationDao.findOne(id);
	}
	
	public Page<SelfApplication> find(Page<SelfApplication> page, SelfApplication selfApplication,Date startTime, Date endTime) {
		DetachedCriteria dc = selfApplicationDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(selfApplication.getCompanyName())){
			dc.add(Restrictions.like("companyName", "%"+selfApplication.getCompanyName()+"%"));
		}
		if (StringUtils.isNotEmpty(selfApplication.getTransactorName())){
			dc.add(Restrictions.like("transactorName", "%"+selfApplication.getTransactorName()+"%"));
		}
		if (StringUtils.isNotEmpty(selfApplication.getStatus())){
			dc.add(Restrictions.like("status", "%"+selfApplication.getStatus()+"%"));
		}
		if (startTime!=null){
			dc.add(Restrictions.ge("createdate", DateUtils.parseDate( DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01"))));
		}
		if (endTime!=null){
			dc.add(Restrictions.le("createdate", DateUtils.parseDate(DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59"))));
		}
		dc.add(Restrictions.ne("status", BaseEntity.DEL_FLAG));
		dc.addOrder(Order.desc("id"));
		return selfApplicationDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SelfApplication selfApplication) {
		selfApplicationDao.save(selfApplication);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		selfApplicationDao.deleteById(id);
	}
	
}
