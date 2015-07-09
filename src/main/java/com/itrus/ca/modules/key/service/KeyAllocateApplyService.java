/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

import java.util.Calendar;
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
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.key.dao.KeyAllocateApplyDao;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 调拨管理Service
 * @author HUHAO
 * @version 2014-06-28
 */
@Component
@Transactional(readOnly = true)
public class KeyAllocateApplyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyAllocateApplyService.class);
	
	@Autowired
	private KeyAllocateApplyDao keyAllocateApplyDao;
	
	public KeyAllocateApply get(Long id) {
		return keyAllocateApplyDao.findOne(id);
	}
	
	public Page<KeyAllocateApply> find(Page<KeyAllocateApply> page, KeyAllocateApply keyAllocateApply) {
		DetachedCriteria dc = keyAllocateApplyDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyAllocateApply.getName())){
//			dc.add(Restrictions.like("name", "%"+keyAllocateApply.getName()+"%"));
//		}
		dc.add(Restrictions.eq(KeyAllocateApply.DEL_FLAG, KeyAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDao.find(page, dc);
	}
	
	
	
	public List<KeyAllocateApply> findByDepotId(Long depotId) {
		DetachedCriteria dc = keyAllocateApplyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyAllocateApply.DEL_FLAG, KeyAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDao.find(dc);
	}
	
	
	
	
	public Page<KeyAllocateApply> assessmentFind(Page<KeyAllocateApply> page, KeyAllocateApply keyAllocateApply, Date startTime, Date endTime, Long office) {
		DetachedCriteria dc = keyAllocateApplyDao.createDetachedCriteria();
		dc.createAlias("createBy.office", "office");
		dc.createAlias("createBy", "createBy");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		
		if (startTime!=null&&endTime!=null) {

			dc.add(Restrictions.ge("warehouseDate", startTime));
			dc.add(Restrictions.le("warehouseDate", endTime));
		}
		if (keyAllocateApply.getState()!=null) {
			dc.add(Restrictions.eq("state", keyAllocateApply.getState()));
		}
		if (office!=null) {
			dc.add(Restrictions.eq("office.id", office));
		}
		dc.add(Restrictions.eq(KeyAllocateApply.DEL_FLAG, KeyAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDao.find(page, dc);
	}
	
	
	public Page<KeyAllocateApply> findByDepotName(Page<KeyAllocateApply> page, KeyAllocateApply keyAllocateApply, Date startTime, Date endTime) {
		DetachedCriteria dc = keyAllocateApplyDao.createDetachedCriteria();
		dc.createAlias("createBy.office", "office");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (startTime!=null&&endTime!=null) {
			dc.add(Restrictions.ge("warehouseDate", startTime));
			dc.add(Restrictions.le("warehouseDate", endTime));
		}
		if (keyAllocateApply.getState()!=null) {
			dc.add(Restrictions.eq("state", keyAllocateApply.getState()));
		}
		dc.add(Restrictions.eq(KeyAllocateApply.DEL_FLAG, KeyAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDao.find(page, dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyAllocateApply keyAllocateApply) {
		keyAllocateApplyDao.save(keyAllocateApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyAllocateApplyDao.deleteById(id);
	}
	
}
