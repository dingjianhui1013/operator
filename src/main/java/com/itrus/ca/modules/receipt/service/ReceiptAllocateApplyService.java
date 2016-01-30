/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.service;

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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApply;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.dao.ReceiptAllocateApplyDao;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 调拨信息Service
 * @author WHW
 * @version 2014-07-02
 */
@Component
@Transactional(readOnly = true)
public class ReceiptAllocateApplyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptAllocateApplyService.class);
	
	@Autowired
	private ReceiptAllocateApplyDao receiptAllocateApplyDao;
	
	public ReceiptAllocateApply get(Long id) {
		return receiptAllocateApplyDao.findOne(id);
	}
	
	public Page<ReceiptAllocateApply> find(Page<ReceiptAllocateApply> page, ReceiptAllocateApply receiptAllocateApply, Date startTime, Date endTime) {
		DetachedCriteria dc = receiptAllocateApplyDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("createBy", "createBy");
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId", "officeId"));
		
		if(receiptAllocateApply.getReceiptDepotInfo()!=null){
			if(receiptAllocateApply.getReceiptDepotInfo().getReceiptName()!=null&&!receiptAllocateApply.getReceiptDepotInfo().getReceiptName().equals("")){
				dc.add(Restrictions.like("receiptDepotInfo.receiptName", "%"+EscapeUtil.escapeLike(receiptAllocateApply.getReceiptDepotInfo().getReceiptName())+"%"));
			}
		}
		if (receiptAllocateApply.getState()!=null) {
			dc.add(Restrictions.eq("state", receiptAllocateApply.getState()));
		}
		if (startTime!=null&&endTime!=null) {
			dc.add(Restrictions.ge("warehouseDate", startTime));
			dc.add(Restrictions.le("warehouseDate", endTime));
		}
		dc.add(Restrictions.eq(ReceiptAllocateApply.DEL_FLAG, ReceiptAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptAllocateApplyDao.find(page, dc);
	}
	
	
	
	public Page<ReceiptAllocateApply> findT(Page<ReceiptAllocateApply> page, ReceiptAllocateApply receiptAllocateApply, Date startTime, Date endTime) {
		DetachedCriteria dc = receiptAllocateApplyDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptAllocateApply.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptAllocateApply.getName()+"%"));
//		}
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("office", "office");
		if(receiptAllocateApply.getReceiptDepotInfo()!=null){
			if(receiptAllocateApply.getReceiptDepotInfo().getReceiptName()!=null&&!receiptAllocateApply.getReceiptDepotInfo().getReceiptName().equals("")){
				dc.add(Restrictions.like("receiptDepotInfo.receiptName", "%"+EscapeUtil.escapeLike(receiptAllocateApply.getReceiptDepotInfo().getReceiptName())+"%"));
			}
		}
		if(receiptAllocateApply.getOffice()!=null){
			if(receiptAllocateApply.getOffice().getId()!=null){
				dc.add(Restrictions.eq("officeId", receiptAllocateApply.getOffice().getId()));
			}
		}
		if (receiptAllocateApply.getState()!=null) {
			dc.add(Restrictions.eq("state", receiptAllocateApply.getState()));
		}
		if (startTime!=null&&endTime!=null) {
			dc.add(Restrictions.ge("warehouseDate", startTime));
			dc.add(Restrictions.le("warehouseDate", endTime));
		}
		dc.add(Restrictions.eq(ReceiptAllocateApply.DEL_FLAG, ReceiptAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptAllocateApplyDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ReceiptAllocateApply receiptAllocateApply) {
		receiptAllocateApplyDao.save(receiptAllocateApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptAllocateApplyDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	public List<ReceiptAllocateApply> selectByDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		DetachedCriteria dc = receiptAllocateApplyDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		return receiptAllocateApplyDao.find(dc);
	}
	
}
