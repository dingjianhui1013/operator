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
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.dao.ReceiptTypeDao;

/**
 * 发票类型管理Service
 * @author HuHao
 * @version 2015-03-17
 */
@Component
@Transactional(readOnly = true)
public class ReceiptTypeService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptTypeService.class);
	
	@Autowired
	private ReceiptTypeDao receiptTypeDao;
	
	public ReceiptType get(Long id) {
		return receiptTypeDao.findOne(id);
	}
	
	public Page<ReceiptType> find(Page<ReceiptType> page, ReceiptType receiptType) {
		DetachedCriteria dc = receiptTypeDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptType.getTypeName())){
//			dc.add(Restrictions.like("name", "%"+receiptType.getTypeName()+"%"));
//		}
		dc.add(Restrictions.eq(ReceiptType.DEL_FLAG, ReceiptType.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptTypeDao.find(page, dc);
	}
	
	public List<ReceiptType> getAll(){
		DetachedCriteria dc = receiptTypeDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(ReceiptType.DEL_FLAG, ReceiptType.DEL_FLAG_NORMAL));
		return receiptTypeDao.find(dc);
	}
	
	
	public ReceiptType findByName(Integer name){
		DetachedCriteria dc = receiptTypeDao.createDetachedCriteria();
		dc.add(Restrictions.eq("typeName", name));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(ReceiptType.DEL_FLAG, ReceiptType.DEL_FLAG_NORMAL));
		return receiptTypeDao.find(dc).get(0);
	}
	
	
	@Transactional(readOnly = false)
	public void save(ReceiptType receiptType) {
		receiptTypeDao.save(receiptType);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptTypeDao.deleteById(id);
	}
	
}
