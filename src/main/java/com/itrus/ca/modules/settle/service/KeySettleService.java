package com.itrus.ca.modules.settle.service;
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
import com.itrus.ca.modules.settle.entity.KeyPurchase;
import com.itrus.ca.modules.settle.entity.KeySettle;
import com.itrus.ca.modules.settle.entity.SettleKey;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.settle.dao.KeyPurchaseDao;
import com.itrus.ca.modules.settle.dao.KeySettleDao;

/*
 * Key结算功能Service
 * @author qt
 * @version 2015-11-11
 */
@Component
@Transactional(readOnly = true)
public class KeySettleService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeySettleService.class);
	
	@Autowired
	private KeySettleDao keySettleDao;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	@Autowired
	private KeyPurchaseService KeyPurchaseService;
	
	public KeySettle get(Long id) {
		return keySettleDao.findOne(id);
	}
	
	public Page<KeySettle> find(Page<KeySettle> page, KeySettle keySettle,Long configSupplierId, Long generalId,Long KeyPurchaseId,Double keySubtotal,Long keyTotalQuantity,Double keyTotalAmount) {
		DetachedCriteria dc = keySettleDao.createDetachedCriteria();
		
		dc.addOrder(Order.desc("id"));
		return keySettleDao.find(page, dc);
	}
	
	public List<KeySettle> exportSettle(Long configSupplierId, Long generalId) {
		DetachedCriteria dc = keySettleDao.createDetachedCriteria();
		if (configSupplierId!=null) {
			dc.add(Restrictions.eq("configSupplier.id", configSupplierId));
		}
		if (generalId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", generalId));
		}
		
		dc.addOrder(Order.desc("id"));
		return keySettleDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(KeySettle keySettle) {
		keySettleDao.save(keySettle);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keySettleDao.deleteById(id);
	}
	
}
