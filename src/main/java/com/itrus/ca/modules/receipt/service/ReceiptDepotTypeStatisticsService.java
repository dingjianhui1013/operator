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
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotTypeStatistics;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.dao.ReceiptDepotTypeStatisticsDao;

/**
 * 发票库存详情Service
 * @author HuHao
 * @version 2015-03-18
 */
@Component
@Transactional(readOnly = true)
public class ReceiptDepotTypeStatisticsService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptDepotTypeStatisticsService.class);
	
	@Autowired
	private ReceiptDepotTypeStatisticsDao receiptDepotTypeStatisticsDao;
	
	public ReceiptDepotTypeStatistics get(Long id) {
		return receiptDepotTypeStatisticsDao.findOne(id);
	}
	
	public Page<ReceiptDepotTypeStatistics> find(Page<ReceiptDepotTypeStatistics> page, ReceiptDepotTypeStatistics receiptDepotTypeStatistics) {
		DetachedCriteria dc = receiptDepotTypeStatisticsDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptDepotTypeStatistics.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptDepotTypeStatistics.getName()+"%"));
//		}
		dc.add(Restrictions.eq(ReceiptDepotTypeStatistics.DEL_FLAG, ReceiptDepotTypeStatistics.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptDepotTypeStatisticsDao.find(page, dc);
	}

	public ReceiptDepotTypeStatistics findByTypeDepot(Long depotId , Long typeId){
		DetachedCriteria dc = receiptDepotTypeStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("receiptDepotInfo.id", depotId));
		dc.add(Restrictions.eq("receiptType.id", typeId));
		if (receiptDepotTypeStatisticsDao.find(dc).size()>0) {
			return receiptDepotTypeStatisticsDao.find(dc).get(0);
		}else{
			return null;
		}
	}
	
	public List<ReceiptDepotTypeStatistics> findByDepot(Long depotId){
		DetachedCriteria dc = receiptDepotTypeStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("receiptDepotInfo.id", depotId));
		return receiptDepotTypeStatisticsDao.find(dc);
	
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(ReceiptDepotTypeStatistics receiptDepotTypeStatistics) {
		receiptDepotTypeStatisticsDao.save(receiptDepotTypeStatistics);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptDepotTypeStatisticsDao.deleteById(id);
	}
	
}
