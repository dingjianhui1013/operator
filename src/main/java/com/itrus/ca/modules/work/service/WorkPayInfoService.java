/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

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
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.dao.WorkPayInfoDao;

/**
 * 绑定信息Service
 * @author HUHAO
 * @version 2014-06-13
 */
@Component
@Transactional(readOnly = true)
public class WorkPayInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkPayInfoService.class);
	
	@Autowired
	private WorkPayInfoDao workPayInfoDao;
	
	public WorkPayInfo get(Long id) {
		return workPayInfoDao.findOne(id);
	}
	
	public Page<WorkPayInfo> find(Page<WorkPayInfo> page, WorkPayInfo workPayInfo) {
		DetachedCriteria dc = workPayInfoDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(workPayInfo.getName())){
//			dc.add(Restrictions.like("name", "%"+workPayInfo.getName()+"%"));
//		}
		dc.add(Restrictions.eq(WorkPayInfo.DEL_FLAG, WorkPayInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workPayInfoDao.find(page, dc);
	}
	
	public Page<WorkPayInfo> findList(Page<WorkPayInfo> page, WorkPayInfo workPayInfo,List<Long> idList) {
		DetachedCriteria dc = workPayInfoDao.createDetachedCriteria();
		
		if (idList!=null&&idList.size()>0) {
			dc.add(Restrictions.in("financePaymentInfo.id", idList));
		}
//		if (StringUtils.isNotEmpty(workPayInfo.getName())){
//			dc.add(Restrictions.like("name", "%"+workPayInfo.getName()+"%"));
//		}
		dc.add(Restrictions.eq(WorkPayInfo.DEL_FLAG, WorkPayInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workPayInfoDao.find(page, dc);
	}
	
	
	public List<WorkPayInfo> findByFinance(FinancePaymentInfo financePaymentInfo) {
		DetachedCriteria dc = workPayInfoDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(workPayInfo.getName())){
//			dc.add(Restrictions.like("name", "%"+workPayInfo.getName()+"%"));
//		}
		dc.createAlias("financePaymentInfo", "financePaymentInfo");
		dc.add(Restrictions.eq("financePaymentInfo.id",financePaymentInfo.getId() ));
		dc.addOrder(Order.desc("id"));
		return workPayInfoDao.find(dc);
	}
	
	public List<WorkPayInfo> findByPayType(boolean chargeMethodPos, boolean chargeMethodMoney,
			boolean chargeMethodBank, /*Boolean chargeMethodAlipay,*/
			boolean chargeMethodGov, boolean chargeMethodContract) {
		DetachedCriteria dc = workPayInfoDao.createDetachedCriteria();
		if(chargeMethodPos)
		{
			dc.add(Restrictions.eq("methodPos",chargeMethodPos));
		}
		if(chargeMethodMoney)
		{
			dc.add(Restrictions.eq("methodMoney",chargeMethodMoney));
		}
		if(chargeMethodBank)
		{
			dc.add(Restrictions.eq("methodBank",chargeMethodBank));
		}
		/*if(chargeMethodAlipay)
		{
			dc.add(Restrictions.eq("methodAlipay",chargeMethodAlipay));
		}*/
		if(chargeMethodGov)
		{
			dc.add(Restrictions.eq("methodGov",chargeMethodGov));
		}
		if(chargeMethodContract)
		{
			dc.add(Restrictions.eq("methodContract",chargeMethodContract));
		}
		/*dc.addOrder(Order.desc("id"));*/
		return workPayInfoDao.find(dc);
	}	
	
	
	public WorkPayInfo findShow(WorkPayInfo workPayInfo) {
		DetachedCriteria dc = workPayInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(WorkPayInfo.DEL_FLAG, WorkPayInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workPayInfoDao.find(dc).get(0);
	}
	
	
	@Transactional(readOnly = false)
	public void save(WorkPayInfo workPayInfo) {
		workPayInfoDao.save(workPayInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkPayInfo> workPayInfo) {
		workPayInfoDao.save(workPayInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		workPayInfoDao.deleteById(id);
	}
	
}
