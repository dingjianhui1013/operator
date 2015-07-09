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
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.dao.KeyDepotGeneralStatisticsDao;

/**
 * 库存统计Service
 * @author HUHAO
 * @version 2014-06-30
 */
@Component
@Transactional(readOnly = true)
public class KeyDepotGeneralStatisticsService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyDepotGeneralStatisticsService.class);
	
	@Autowired
	private KeyDepotGeneralStatisticsDao keyDepotGeneralStatisticsDao;
	
	public KeyDepotGeneralStatistics get(Long id) {
		return keyDepotGeneralStatisticsDao.findOne(id);
	}
	
	public Page<KeyDepotGeneralStatistics> find(Page<KeyDepotGeneralStatistics> page, KeyDepotGeneralStatistics keyDepotGeneralStatistics) {
		DetachedCriteria dc = keyDepotGeneralStatisticsDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyDepotGeneralStatistics.getName())){
//			dc.add(Restrictions.like("name", "%"+keyDepotGeneralStatistics.getName()+"%"));
//		}
		//dc.add(Restrictions.eq(KeyDepotGeneralStatistics.DEL_FLAG, KeyDepotGeneralStatistics.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyDepotGeneralStatisticsDao.find(page, dc);
	}
	
	public List<KeyDepotGeneralStatistics> findByDepotId(Long depotId) {
		DetachedCriteria dc = keyDepotGeneralStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
	//	dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyDepotGeneralStatisticsDao.find(dc);
	}
	
	public List<KeyDepotGeneralStatistics> findAll() {
		DetachedCriteria dc = keyDepotGeneralStatisticsDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return keyDepotGeneralStatisticsDao.find(dc);
	}
	
	public List<KeyDepotGeneralStatistics> findByKeyId(Long keyId) {
		DetachedCriteria dc = keyDepotGeneralStatisticsDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
	//	dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyDepotGeneralStatisticsDao.find(dc);
	}
	
	
	public List<KeyDepotGeneralStatistics> findByDepotIdAndGeneId(Long depotId,Long GeneId) {
		DetachedCriteria dc = keyDepotGeneralStatisticsDao.createDetachedCriteria();
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq("keyGeneralInfo.id", GeneId));
	//	dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyDepotGeneralStatisticsDao.find(dc);
	}
	
	//消息设置调用的库存预警值接口
	public List<KeyDepotGeneralStatistics> findByAlarm() {
		
		List<KeyDepotGeneralStatistics> statistics =   this.findAll();
		
		for (int i = statistics.size()-1; i >=0; i--) {
			if (statistics.get(i).getAlarmValue()==null) {
				statistics.get(i).setAlarmValue(0);
			}
			if (statistics.get(i).getInCount()>statistics.get(i).getAlarmValue()) {
				statistics.remove(i);
			}
		}
		return statistics;
	}
	
	
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyDepotGeneralStatistics keyDepotGeneralStatistics) {
		keyDepotGeneralStatisticsDao.save(keyDepotGeneralStatistics);
	}
	
	@Transactional(readOnly = false)
	public void saveList(List<KeyDepotGeneralStatistics> page) {
		keyDepotGeneralStatisticsDao.save(page);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		//keyDepotGeneralStatisticsDao.deleteById(id);
	}
	
}
