/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
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
import com.itrus.ca.modules.settle.dao.KeyPurchaseDao;

/**
 * key采购记录Service
 * @author DingJianHui
 * @version 2015-11-09
 */
@Component
@Transactional(readOnly = true)
public class KeyPurchaseService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyPurchaseService.class);
	
	@Autowired
	private KeyPurchaseDao keyPurchaseDao;
	
	public KeyPurchase get(Long id) {
		return keyPurchaseDao.findOne(id);
	}
	
	public Page<KeyPurchase> find(Page<KeyPurchase> page, KeyPurchase keyPurchase) {
		DetachedCriteria dc = keyPurchaseDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyPurchase.getAppName())){
//			dc.add(Restrictions.like("appName", "%"+keyPurchase.getAppName()+"%"));
//		}
		if(keyPurchase.getAppName()!=null && !"".equals(keyPurchase.getAppName()))
		{
			dc.add(Restrictions.eq("appName", keyPurchase.getAppName()));
		}
		if(keyPurchase.getStorageDate()!=null && !"".equals( keyPurchase.getStorageDate()))
		{
			dc.add(Restrictions.eq("storageDate", keyPurchase.getStorageDate()));
		}
		if(keyPurchase.getStatus()!=null && !"".equals( keyPurchase.getStatus()))
		{
			dc.add(Restrictions.eq("status", keyPurchase.getStatus()));
		}
		dc.add(Restrictions.eq(KeyPurchase.DEL_FLAG, KeyPurchase.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyPurchaseDao.find(page, dc);
	}
	public List<KeyPurchase> find(KeyPurchase keyPurchase) {
		DetachedCriteria dc = keyPurchaseDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyPurchase.getAppName())){
//			dc.add(Restrictions.like("appName", "%"+keyPurchase.getAppName()+"%"));
//		}
		if(keyPurchase.getAppName()!=null && !"".equals(keyPurchase.getAppName()))
		{
			System.out.println("appName:"+keyPurchase.getAppName());
			dc.add(Restrictions.eq("appName", keyPurchase.getAppName()));
		}
		if(keyPurchase.getStorageDate()!=null && !"".equals( keyPurchase.getStorageDate()))
		{
			System.out.println("ssss:"+keyPurchase.getStorageDate());
			dc.add(Restrictions.eq("storageDate", keyPurchase.getStorageDate()));
		}
		if(keyPurchase.getStatus()!=null && !"".equals( keyPurchase.getStatus()))
		{
			System.out.println("status:"+keyPurchase.getStatus());
			dc.add(Restrictions.eq("status", keyPurchase.getStatus()));
		}
		dc.add(Restrictions.eq(KeyPurchase.DEL_FLAG, KeyPurchase.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyPurchaseDao.find(dc);
	}
	public List<KeyPurchase> findAll(KeyPurchase keyPurchase) {
		DetachedCriteria dc = keyPurchaseDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return keyPurchaseDao.find(dc);
	}
	public List<KeyPurchase> findByKeyName(String name) {
		DetachedCriteria dc = keyPurchaseDao.createDetachedCriteria();
		dc.add(Restrictions.eq("appName", name));
		dc.addOrder(Order.desc("id"));
		return keyPurchaseDao.find(dc);
	}
	public KeyPurchase findById(Long id)
	{
		return keyPurchaseDao.findOne(id);
	}
	@Transactional(readOnly = false)
	public void save(KeyPurchase keyPurchase) {
		keyPurchaseDao.save(keyPurchase);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyPurchaseDao.deleteById(id);
	}
	
}
