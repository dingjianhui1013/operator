/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.service;

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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.statistic.entity.StatisticCertData;
import com.itrus.ca.modules.statistic.dao.StatisticCertDataDao;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 证书发放统计Service
 * @author HUHAO
 * @version 2014-07-08
 */
@Component
@Transactional(readOnly = true)
public class StatisticCertDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(StatisticCertDataService.class);
	
	@Autowired
	private StatisticCertDataDao statisticCertDataDao;
	
	public StatisticCertData get(Long id) {
		return statisticCertDataDao.findOne(id);
	}
	
	public Page<StatisticCertData> find(Page<StatisticCertData> page, StatisticCertData statisticCertData) {
		DetachedCriteria dc = statisticCertDataDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(statisticCertData.getName())){
//			dc.add(Restrictions.like("name", "%"+statisticCertData.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(StatisticCertData.DEL_FLAG, StatisticCertData.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return statisticCertDataDao.find(page, dc);
	}
	public List<StatisticCertData> getSum(ConfigApp configApp, Office office, Date startDate,
			Date endDate, Integer payType){
		DetachedCriteria dc = statisticCertDataDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app", configApp));
		dc.add(Restrictions.eq("office", office));    
		dc.add(Restrictions.gt("countDate", startDate));
		dc.add(Restrictions.lt("countDate", endDate));
		dc.add(Restrictions.eq("payType", payType));
		return statisticCertDataDao.find(dc);
	}
	
	public List<StatisticCertData> getSum1(ConfigApp configApp,Integer productType, Office office, Date startDate,
			Date endDate, Integer payType){
		DetachedCriteria dc = statisticCertDataDao.createDetachedCriteria();
		if(!"-1".equals(productType.toString())){
		dc.add(Restrictions.eq("productType", productType));
		}
		dc.add(Restrictions.eq("app", configApp));
		dc.add(Restrictions.eq("office", office));    
		dc.add(Restrictions.ge("countDate", startDate));
		dc.add(Restrictions.le("countDate", endDate));
		dc.add(Restrictions.eq("payType", payType));
		return statisticCertDataDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(StatisticCertData statisticCertData) {
		statisticCertDataDao.save(statisticCertData);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
//	statisticCertDataDao.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public List<StatisticCertData> countHisData(ConfigApp app,Date date){
		DetachedCriteria dc = statisticCertDataDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app", app));
		dc.add(Restrictions.eq("countDate", date));
		List<StatisticCertData> datas = statisticCertDataDao.find(dc);
		if (datas!=null&&datas.size()!=0) {
			return datas;
		}else {
			return null;
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteDataByDay(Date date){
		DetachedCriteria dc = statisticCertDataDao.createDetachedCriteria();
		dc.add(Restrictions.eq("countDate", date));
		List<StatisticCertData> datas = statisticCertDataDao.find(dc);
		statisticCertDataDao.delete(datas);
		
	}
}
