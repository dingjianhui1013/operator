/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

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
import com.itrus.ca.modules.settle.entity.WorkDealInfo_settlementLog;
import com.itrus.ca.modules.settle.dao.WorkDealInfo_settlementLogDao;

/**
 * 年费结算保存Service
 * @author DingJianHui
 * @version 2016-03-09
 */
@Component
@Transactional(readOnly = true)
public class WorkDealInfo_settlementLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkDealInfo_settlementLogService.class);
	
	@Autowired
	private WorkDealInfo_settlementLogDao workDealInfo_settlementLogDao;
	
	public WorkDealInfo_settlementLog get(Long id) {
		return workDealInfo_settlementLogDao.findOne(id);
	}
	
	public Page<WorkDealInfo_settlementLog> find(Page<WorkDealInfo_settlementLog> page, WorkDealInfo_settlementLog workDealInfo_settlementLog) {
		DetachedCriteria dc = workDealInfo_settlementLogDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(workDealInfo_settlementLog.getName())){
//			dc.add(Restrictions.like("name", "%"+workDealInfo_settlementLog.getName()+"%"));
//		}
		dc.add(Restrictions.eq(WorkDealInfo_settlementLog.DEL_FLAG, WorkDealInfo_settlementLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfo_settlementLogDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkDealInfo_settlementLog workDealInfo_settlementLog) {
		workDealInfo_settlementLogDao.save(workDealInfo_settlementLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		workDealInfo_settlementLogDao.deleteById(id);
	}
	
}
