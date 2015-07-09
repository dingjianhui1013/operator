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
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApplyDetail;
import com.itrus.ca.modules.receipt.dao.ReceiptAllocateApplyDetailDao;

/**
 * 申请发票类型详情管理Service
 * @author HuHao
 * @version 2015-03-17
 */
@Component
@Transactional(readOnly = true)
public class ReceiptAllocateApplyDetailService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptAllocateApplyDetailService.class);
	
	@Autowired
	private ReceiptAllocateApplyDetailDao receiptAllocateApplyDetailDao;
	
	public ReceiptAllocateApplyDetail get(Long id) {
		return receiptAllocateApplyDetailDao.findOne(id);
	}
	
	public Page<ReceiptAllocateApplyDetail> find(Page<ReceiptAllocateApplyDetail> page, ReceiptAllocateApplyDetail receiptAllocateApplyDetail) {
		DetachedCriteria dc = receiptAllocateApplyDetailDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptAllocateApplyDetail.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptAllocateApplyDetail.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ReceiptAllocateApplyDetail.DEL_FLAG, ReceiptAllocateApplyDetail.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptAllocateApplyDetailDao.find(page, dc);
	}
	
	public List<ReceiptAllocateApplyDetail> getByApplyId(Long applyId){
		DetachedCriteria dc = receiptAllocateApplyDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("receiptAllocateApply.id",applyId ));
		dc.addOrder(Order.desc("id"));
		return receiptAllocateApplyDetailDao.find(dc);
		
	}
	
	
	@Transactional(readOnly = false)
	public void save(ReceiptAllocateApplyDetail receiptAllocateApplyDetail) {
		receiptAllocateApplyDetailDao.save(receiptAllocateApplyDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptAllocateApplyDetailDao.deleteById(id);
	}
	
}
