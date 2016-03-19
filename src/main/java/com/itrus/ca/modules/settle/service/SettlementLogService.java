/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.util.Date;
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
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.settle.entity.SettlementLog;
import com.itrus.ca.modules.settle.dao.SettlementLogDao;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 年限结算保存Service
 * @author DingJianHui
 * @version 2016-03-07
 */
@Component
@Transactional(readOnly = true)
public class SettlementLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SettlementLogService.class);
	
	@Autowired
	private SettlementLogDao settlementLogDao;
	
	public SettlementLog get(Long id) {
		return settlementLogDao.findOne(id);
	}
	
	public List<SettlementLog> find(SettlementLog settlementLog) {
		DetachedCriteria dc = settlementLogDao.createDetachedCriteria();
		if(settlementLog.getAppName()==null&&!"".equals(settlementLog.getProductName()))
		{
			dc.add(Restrictions.eq("comagentName",settlementLog.getComagentName()));
			dc.add(Restrictions.isNull("appName"));
			dc.add(Restrictions.eq("productName",settlementLog.getProductName()));
			dc.add(Restrictions.eq("startTime", settlementLog.getStartTime()));
			dc.add(Restrictions.eq("endTime", settlementLog.getEndTime()));
			
		}
		if(settlementLog.getAppName()!=null&&"".equals(settlementLog.getProductName()))
		{
			dc.add(Restrictions.eq("comagentName",settlementLog.getComagentName()));
			dc.add(Restrictions.eq("appName", settlementLog.getAppName()));
			dc.add(Restrictions.isNull("productName"));
			dc.add(Restrictions.eq("startTime", settlementLog.getStartTime()));
			dc.add(Restrictions.eq("endTime", settlementLog.getEndTime()));
		}
		if(settlementLog.getAppName()==null&&"".equals(settlementLog.getProductName()))
		{
			dc.add(Restrictions.eq("comagentName",settlementLog.getComagentName()));
			dc.add(Restrictions.isNull("appName"));
			dc.add(Restrictions.isNull("productName"));
			dc.add(Restrictions.eq("startTime", settlementLog.getStartTime()));
			dc.add(Restrictions.eq("endTime", settlementLog.getEndTime()));
		}
		if(settlementLog.getAppName()!=null&&!"".equals(settlementLog.getProductName()))
		{
			dc.add(Restrictions.eq("comagentName",settlementLog.getComagentName()));
			dc.add(Restrictions.eq("appName", settlementLog.getAppName()));
			dc.add(Restrictions.eq("productName",settlementLog.getProductName()));
			dc.add(Restrictions.eq("startTime", settlementLog.getStartTime()));
			dc.add(Restrictions.eq("endTime", settlementLog.getEndTime()));
		}
		dc.add(Restrictions.eq(SettlementLog.DEL_FLAG, SettlementLog.DEL_FLAG_NORMAL));
		return settlementLogDao.find(dc);
	}
	public List<SettlementLog> findAll(SettlementLog settlementLog,Date endTime,Date startTime) {
		DetachedCriteria dc = settlementLogDao.createDetachedCriteria();

		
		if(settlementLog.getComagentName()!=null)
		{
			dc.add(Restrictions.eq("comagentName",settlementLog.getComagentName()));
		}
		if(settlementLog.getAppName()!=null)
		{
			dc.add(Restrictions.eq("appName", settlementLog.getAppName()));
		}
		if(startTime!=null)
		{
			dc.add(Restrictions.ge("createDate",startTime));
		}
		if(endTime!=null)
		{
			dc.add(Restrictions.le("createDate", endTime));
		}
		dc.add(Restrictions.eq(SettlementLog.DEL_FLAG, SettlementLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return settlementLogDao.find(dc);
	}
	public Page<SettlementLog> find(Page<SettlementLog> page, SettlementLog settlementLog) {
		DetachedCriteria dc = settlementLogDao.createDetachedCriteria();
		return settlementLogDao.find(page,dc);
	}
//	public List<WorkDealInfo> findWorkDealInfos(SettlementLog settlementLog) {
//		DetachedCriteria dc = settlementLogDao.createDetachedCriteria();
//		
//		return settlementLogDao.find(dc);
//	}
	@Transactional(readOnly = false)
	public void save(SettlementLog settlementLog) {
		settlementLogDao.save(settlementLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		settlementLogDao.deleteById(id);
	}
	
}
