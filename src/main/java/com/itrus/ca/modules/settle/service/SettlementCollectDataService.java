
package com.itrus.ca.modules.settle.service;



import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.settle.dao.SettlementCollectDataDao;
import com.itrus.ca.modules.settle.entity.SettlementCollectData;




/**
 * 年限结算保存Service
 * @author DingJianHui
 * @version 2016-03-07
 */
@Component
@Transactional(readOnly = true)
public class SettlementCollectDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SettlementLogService.class);
	
	@Autowired
	private SettlementCollectDataDao settlementCollectDataDao;
	
	public SettlementCollectData get(Long id) {
		return settlementCollectDataDao.findOne(id);
	}
	

	@Transactional(readOnly = false)
	public void save(SettlementCollectData settlementCollectData) {
		settlementCollectDataDao.save(settlementCollectData);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		settlementCollectDataDao.deleteById(id);
	}
	
	
	public List<SettlementCollectData>findByLogId(Long logId){
		DetachedCriteria dc = settlementCollectDataDao.createDetachedCriteria();
		
		dc.add(Restrictions.eq("settlementLogId", logId));
		return settlementCollectDataDao.find(dc);
	}
	
}
