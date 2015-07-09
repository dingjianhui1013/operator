/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

import java.util.Date;
import java.util.List;

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
import com.itrus.ca.modules.key.entity.KeyCheckLog;
import com.itrus.ca.modules.key.dao.KeyCheckLogDao;

/**
 * 库存统计Service
 * @author HUHAO
 * @version 2014-07-01
 */
@Component
@Transactional(readOnly = true)
public class KeyCheckLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyCheckLogService.class);
	
	@Autowired
	private KeyCheckLogDao keyCheckLogDao;
	
	public KeyCheckLog get(Long id) {
		return keyCheckLogDao.findOne(id);
	}
	
	public Page<KeyCheckLog> find(Page<KeyCheckLog> page, KeyCheckLog keyCheckLog) {
		DetachedCriteria dc = keyCheckLogDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyCheckLog.getName())){
//			dc.add(Restrictions.like("name", "%"+keyCheckLog.getName()+"%"));
//		}
		dc.add(Restrictions.eq(KeyCheckLog.DEL_FLAG, KeyCheckLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyCheckLogDao.find(page, dc);
	}
	
	public Page<KeyCheckLog> findBydepotIdForShow(Page<KeyCheckLog> page,Long depotId,Date startTime,Date endTime,Long supplierId,Long geneId) {
		DetachedCriteria dc = keyCheckLogDao.createDetachedCriteria();
		
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		if (startTime!=null) {
			dc.add(Restrictions.ge("startDate", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.le("endDate", endTime));
		}
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (geneId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", geneId));
		}
		dc.add(Restrictions.eq(KeyCheckLog.DEL_FLAG, KeyCheckLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("checkNumber"));
		return keyCheckLogDao.find(page,dc);
	}

	
	
	
	
	
	
	public List<KeyCheckLog> findByDepotId(Long depotId) {
		DetachedCriteria dc = keyCheckLogDao.createDetachedCriteria();

		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyCheckLog.DEL_FLAG, KeyCheckLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyCheckLogDao.find(dc);
	}
	
	public List<KeyCheckLog> findOrderByCheckNumber(Long depotId) {
		DetachedCriteria dc = keyCheckLogDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyCheckLog.DEL_FLAG, KeyCheckLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("checkNumber"));
		return keyCheckLogDao.find(dc);
	}
	
	public List<KeyCheckLog> findByCheckNumber(Long depotId,int checkNumber) {
		DetachedCriteria dc = keyCheckLogDao.createDetachedCriteria();
		dc.add(Restrictions.eq("checkNumber", checkNumber));
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyCheckLog.DEL_FLAG, KeyCheckLog.DEL_FLAG_NORMAL));
		return keyCheckLogDao.find(dc);
	}
	
	public List<KeyCheckLog> findByCheckNumberIsReset(Long depotId,int checkNumber) {
		DetachedCriteria dc = keyCheckLogDao.createDetachedCriteria();
		dc.add(Restrictions.eq("isReset", true));
		dc.add(Restrictions.eq("checkNumber", checkNumber));
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyCheckLog.DEL_FLAG, KeyCheckLog.DEL_FLAG_NORMAL));
		return keyCheckLogDao.find(dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyCheckLog keyCheckLog) {
		keyCheckLogDao.save(keyCheckLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyCheckLogDao.deleteById(id);
	}
	
}
