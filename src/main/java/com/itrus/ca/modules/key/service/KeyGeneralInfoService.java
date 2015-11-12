/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

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
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.dao.KeyGeneralInfoDao;
import com.itrus.ca.modules.settle.entity.KeyPurchase;

/**
 * key类型信息Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class KeyGeneralInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyGeneralInfoService.class);
	
	@Autowired
	private KeyGeneralInfoDao keyGeneralInfoDao;
	
	public KeyGeneralInfo get(Long id) {
		return keyGeneralInfoDao.findOne(id);
	}
	
	public Page<KeyGeneralInfo> find(Page<KeyGeneralInfo> page, KeyGeneralInfo keyGeneralInfo,Long supplierId) {
		DetachedCriteria dc = keyGeneralInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(keyGeneralInfo.getName())){
			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(keyGeneralInfo.getName())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("configSupplier.id", supplierId));
		dc.add(Restrictions.eq(KeyGeneralInfo.DEL_FLAG, KeyGeneralInfo.DEL_FLAG_NORMAL));
		return keyGeneralInfoDao.find(page, dc);
	}
	
	public List<KeyGeneralInfo> findAll() {
		DetachedCriteria dc = keyGeneralInfoDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyGeneralInfo.DEL_FLAG, KeyGeneralInfo.DEL_FLAG_NORMAL));
		return keyGeneralInfoDao.find(dc);
	}

	public List<KeyGeneralInfo> findByName(String name) {
		DetachedCriteria dc = keyGeneralInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(name)){
			dc.add(Restrictions.eq("name",name));
		}
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyGeneralInfo.DEL_FLAG, KeyGeneralInfo.DEL_FLAG_NORMAL));
		return keyGeneralInfoDao.find(dc);
	}
	
	public KeyGeneralInfo findInfoByName(String name) {
		DetachedCriteria dc = keyGeneralInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(name)){
			dc.add(Restrictions.eq("name",name));
		}
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyGeneralInfo.DEL_FLAG, KeyGeneralInfo.DEL_FLAG_NORMAL));
		List<KeyGeneralInfo> infos =  keyGeneralInfoDao.find(dc);
		if (infos!=null&&infos.size()!=0) {
			return infos.get(0);
		}else {
			return null;
		}
	}
	public KeyGeneralInfo findById(Long id)
	{
		return keyGeneralInfoDao.findOne(id);
	}
	
	public List<KeyGeneralInfo> findBySupplierId(Long supplierId) {
		DetachedCriteria dc = keyGeneralInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configSupplier.id", supplierId));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyGeneralInfo.DEL_FLAG, KeyGeneralInfo.DEL_FLAG_NORMAL));
		return keyGeneralInfoDao.find(dc);
	}
	
	public List<KeyGeneralInfo> findByGeneIds(List<Long> geneIds) {
		DetachedCriteria dc = keyGeneralInfoDao.createDetachedCriteria();
		dc.setResultTransformer(dc.DISTINCT_ROOT_ENTITY);
		dc.add(Restrictions.in("id", geneIds));
		dc.add(Restrictions.eq(KeyGeneralInfo.DEL_FLAG, KeyGeneralInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyGeneralInfoDao.find(dc);
	}
	
	
	
	
	
	
	
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyGeneralInfo keyGeneralInfo) {
		keyGeneralInfoDao.save(keyGeneralInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyGeneralInfoDao.deleteById(id);
	}
	
}
