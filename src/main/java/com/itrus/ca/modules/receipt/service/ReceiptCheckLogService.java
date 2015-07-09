/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.service;

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
import com.itrus.ca.modules.receipt.entity.ReceiptCheckLog;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.dao.ReceiptCheckLogDao;

/**
 * 盘点信息Service
 * @author WHW
 * @version 2014-07-02
 */
@Component
@Transactional(readOnly = true)
public class ReceiptCheckLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptCheckLogService.class);
	
	@Autowired
	private ReceiptCheckLogDao receiptCheckLogDao;
	
	public ReceiptCheckLog get(Long id) {
		return receiptCheckLogDao.findOne(id);
	}
	
	public Page<ReceiptCheckLog> find(Page<ReceiptCheckLog> page, ReceiptCheckLog receiptCheckLog) {
		DetachedCriteria dc = receiptCheckLogDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptCheckLog.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptCheckLog.getName()+"%"));
//		}
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		if(receiptCheckLog.getReceiptDepotInfo()!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptCheckLog.getReceiptDepotInfo()));
		}
		dc.addOrder(Order.desc("id"));
		return receiptCheckLogDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ReceiptCheckLog receiptCheckLog) {
		receiptCheckLogDao.save(receiptCheckLog);
	}

	@Transactional(readOnly = false)
	public ReceiptCheckLog findLestMoney(ReceiptDepotInfo receiptDepotInfo) {
		DetachedCriteria dc = receiptCheckLogDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		if(receiptDepotInfo!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		}
		dc.addOrder(Order.desc("id"));
		List<ReceiptCheckLog> logs = receiptCheckLogDao.find(dc);
		if(logs.size()>0){
			return logs.get(0);
		}
		return null;
	}

	public List<ReceiptCheckLog> selectByDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		DetachedCriteria dc = receiptCheckLogDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		return receiptCheckLogDao.find(dc);
	}
	
	public List<ReceiptCheckLog> selectByDepotInfoF(ReceiptDepotInfo receiptDepotInfo) {
		DetachedCriteria dc = receiptCheckLogDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("status",0));
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		return receiptCheckLogDao.find(dc);
	}
	
//	@Transactional(readOnly = false)
//	public void delete(Long id) {
//		receiptCheckLogDao.deleteById(id);
//	}
	
}
