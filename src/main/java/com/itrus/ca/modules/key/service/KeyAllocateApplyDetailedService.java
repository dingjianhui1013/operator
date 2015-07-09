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
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.key.entity.KeyAllocateApplyDetailed;
import com.itrus.ca.modules.key.dao.KeyAllocateApplyDao;
import com.itrus.ca.modules.key.dao.KeyAllocateApplyDetailedDao;

/**
 * 调拨管理Service
 * @author CaoYichao
 * @version 2014-07-10
 */
@Component
@Transactional(readOnly = true)
public class KeyAllocateApplyDetailedService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyAllocateApplyService.class);
	
	@Autowired
	private  KeyAllocateApplyDetailedDao  keyAllocateApplyDetailedDao;
	
	public KeyAllocateApplyDetailed get(Long id) {
		return keyAllocateApplyDetailedDao.findOne(id);
	}
	
	public Page<KeyAllocateApplyDetailed> find(Page<KeyAllocateApplyDetailed> page, KeyAllocateApplyDetailed keyAllocateApplyDetailed) {
		DetachedCriteria dc = keyAllocateApplyDetailedDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyAllocateApply.getName())){
//			dc.add(Restrictions.like("name", "%"+keyAllocateApply.getName()+"%"));
//		}
		//dc.add(Restrictions.eq(KeyAllocateApply.DEL_FLAG, KeyAllocateApply.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDetailedDao.find(page, dc);
	}
	
	
	public List<KeyAllocateApplyDetailed> findByApplyId(Long applyId) {
		DetachedCriteria dc = keyAllocateApplyDetailedDao.createDetachedCriteria();
		dc.createAlias("keyAllocateApply", "keyAllocateApply");
		dc.add(Restrictions.eq("keyAllocateApply.id", applyId));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDetailedDao.find(dc);
	}
	
	public List<KeyAllocateApplyDetailed> findByApplyIdAndKeyType(Long applyId,Long keyType,Long badKeyType) {
		DetachedCriteria dc = keyAllocateApplyDetailedDao.createDetachedCriteria();
		dc.createAlias("keyAllocateApply", "keyAllocateApply");
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.createAlias("badKeyGeneralInfo", "badKeyGeneralInfo");
		dc.add(Restrictions.eq("keyAllocateApply.id", applyId));
		dc.add(Restrictions.eq("keyGeneralInfo.id", keyType));
		
		
		
		if(badKeyType!=0){
			dc.add(Restrictions.eq("badKeyGeneralInfo.id", badKeyType));
		}
		
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDetailedDao.find(dc);
	}
	
	public List<KeyAllocateApplyDetailed> findByApplyIdAndBadKeyType(Long applyId,Long badKeyType,Long keyType) {
		DetachedCriteria dc = keyAllocateApplyDetailedDao.createDetachedCriteria();
		dc.createAlias("keyAllocateApply", "keyAllocateApply");
		dc.createAlias("badKeyGeneralInfo", "badKeyGeneralInfo");
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyAllocateApply.id", applyId));
		dc.add(Restrictions.eq("badKeyGeneralInfo.id", badKeyType));
		dc.add(Restrictions.eq("keyGeneralInfo.id", keyType));
		dc.addOrder(Order.desc("id"));
		return keyAllocateApplyDetailedDao.find(dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyAllocateApplyDetailed keyAllocateApplyDetailed) {
		keyAllocateApplyDetailedDao.save(keyAllocateApplyDetailed);
	}
	
	/*@Transactional(readOnly = false)
	public void delete(Long id) {
		keyAllocateApplyDao.deleteById(id);
	}*/
	
}
