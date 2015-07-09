/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.service;

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
import com.itrus.ca.modules.receipt.entity.ReceiptLog;
import com.itrus.ca.modules.receipt.dao.ReceiptLogDao;

/**
 * 发票定时统计Service
 * @author WangHongwei
 * @version 2014-07-15
 */
@Component
@Transactional(readOnly = true)
public class ReceiptLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptLogService.class);
	
	@Autowired
	private ReceiptLogDao receiptLogDao;
	
	public ReceiptLog get(Long id) {
		return receiptLogDao.findOne(id);
	}
	
	public Page<ReceiptLog> find(Page<ReceiptLog> page, ReceiptLog receiptLog) {
		DetachedCriteria dc = receiptLogDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return receiptLogDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ReceiptLog receiptLog) {
		receiptLogDao.save(receiptLog);
	}
	
	
}
