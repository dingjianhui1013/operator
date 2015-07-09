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
import com.itrus.ca.modules.receipt.entity.ReceiptCheckLogDetail;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.dao.ReceiptCheckLogDetailDao;

/**
 * 发票盘点详情Service
 * @author HuHao
 * @version 2015-03-20
 */
@Component
@Transactional(readOnly = true)
public class ReceiptCheckLogDetailService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptCheckLogDetailService.class);
	
	@Autowired
	private ReceiptCheckLogDetailDao receiptCheckLogDetailDao;
	
	public ReceiptCheckLogDetail get(Long id) {
		return receiptCheckLogDetailDao.findOne(id);
	}
	
	public Page<ReceiptCheckLogDetail> find(Page<ReceiptCheckLogDetail> page, ReceiptCheckLogDetail receiptCheckLogDetail) {
		DetachedCriteria dc = receiptCheckLogDetailDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptCheckLogDetail.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptCheckLogDetail.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ReceiptCheckLogDetail.DEL_FLAG, ReceiptCheckLogDetail.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptCheckLogDetailDao.find(page, dc);
	}
	
	public ReceiptCheckLogDetail findByCheckType(Long checkId,Long typeId) {
		DetachedCriteria dc = receiptCheckLogDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("receiptCheckLog.id", checkId));
		dc.add(Restrictions.eq("receiptType.id", typeId));
		dc.add(Restrictions.eq("type", 5));
		if (receiptCheckLogDetailDao.find(dc).size()>0) {
			return receiptCheckLogDetailDao.find(dc).get(0);
		}else{
			return null;
		}
	}
	
	public List<ReceiptCheckLogDetail> findByType(Integer type,Long checkId) {
		DetachedCriteria dc = receiptCheckLogDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("receiptCheckLog.id", checkId));
		dc.add(Restrictions.eq("type", type));
		dc.addOrder(Order.desc("id"));
		return receiptCheckLogDetailDao.find(dc);
	}
	
	public ReceiptCheckLogDetail findByTypeCheckType(Integer type,Long checkId,Long receiptTypeId) {
		DetachedCriteria dc = receiptCheckLogDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("receiptCheckLog.id", checkId));
		dc.add(Restrictions.eq("receiptType.id", receiptTypeId));
		dc.add(Restrictions.eq("type", type));
		if (receiptCheckLogDetailDao.find(dc).size()>0) {
			return receiptCheckLogDetailDao.find(dc).get(0);
		}else {
			return null;
		}
	}
	

	
	@Transactional(readOnly = false)
	public void save(ReceiptCheckLogDetail receiptCheckLogDetail) {
		receiptCheckLogDetailDao.save(receiptCheckLogDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptCheckLogDetailDao.deleteById(id);
	}

}
