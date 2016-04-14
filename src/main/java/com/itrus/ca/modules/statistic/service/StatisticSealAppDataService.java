/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.service;

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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.statistic.entity.StatisticAppData;
import com.itrus.ca.modules.statistic.entity.StatisticSealAppData;
import com.itrus.ca.modules.statistic.dao.StatisticSealAppDataDao;

/**
 * 印章日经营详细表Service
 * @author DingJianHui
 * @version 2016-03-23
 */
@Component
@Transactional(readOnly = true)
public class StatisticSealAppDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(StatisticSealAppDataService.class);
	
	@Autowired
	private StatisticSealAppDataDao statisticSealAppDataDao;
	
	public StatisticSealAppData get(Long id) {
		return statisticSealAppDataDao.findOne(id);
	}
	
	public Page<StatisticSealAppData> find(Page<StatisticSealAppData> page, StatisticSealAppData statisticSealAppData) {
		DetachedCriteria dc = statisticSealAppDataDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(statisticSealAppData.getName())){
//			dc.add(Restrictions.like("name", "%"+statisticSealAppData.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(StatisticSealAppData.DEL_FLAG, StatisticSealAppData.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return statisticSealAppDataDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(StatisticSealAppData statisticSealAppData) {
		statisticSealAppDataDao.save(statisticSealAppData);
	}
	
//	@Transactional(readOnly = false)
//	public void delete(Long id) {
//		statisticSealAppDataDao.deleteById(id);
//	}
	@Transactional(readOnly = false)
	public void deleteDayData(Date day) {
		DetachedCriteria dc = statisticSealAppDataDao.createDetachedCriteria();
		dc.add(Restrictions.ge("statisticDate", day));
		List<StatisticSealAppData> appDatas = statisticSealAppDataDao.find(dc);
		statisticSealAppDataDao.delete(appDatas);
	}
	
	@Transactional(readOnly = false)
	public void deleteDayData(Date day,Long officeId) {
		DetachedCriteria dc = statisticSealAppDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.ge("statisticDate", day));
		dc.add(Restrictions.eq("office.id", officeId));
		List<StatisticSealAppData> appDatas = statisticSealAppDataDao.find(dc);
		statisticSealAppDataDao.delete(appDatas);
	}
	public List<StatisticSealAppData> findByDateAndOffice(Long officeId,List<Long> appIds,Date startTime,Date endTime)
	{
		DetachedCriteria dc= statisticSealAppDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		if(officeId!=null)
		{
			dc.add(Restrictions.eq("office.id",officeId));
		}
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(endTime);
		startCal.add(Calendar.DATE,1);
		if(startTime!=null)
		{
			dc.add(Restrictions.ge("statisticDate", startTime));
		}
		if(endTime!=null)
		{
			dc.add(Restrictions.lt("statisticDate", startCal.getTime()));
		}
		if(appIds.size()>0&&appIds!=null)
		{
			dc.add(Restrictions.in("app.id", appIds));
		}
		dc.addOrder(Order.desc("statisticDate"));
		return statisticSealAppDataDao.find(dc);
	}

	public List<StatisticSealAppData> findByMonth(Long office,List<Long> appIds) {
		DetachedCriteria dc = statisticSealAppDataDao.createDetachedCriteria();
		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office));
		}
		if(appIds.size()>0&&appIds!=null)
		{
			dc.add(Restrictions.in("app.id", appIds));
		}
		dc.addOrder(Order.desc("id"));
		return statisticSealAppDataDao.find(dc);
	}
	public List<StatisticSealAppData> findByMonth(ConfigApp configApp, Long office,
			Date startTime, Date endTime) {
		DetachedCriteria dc = statisticSealAppDataDao.createDetachedCriteria();

		if (configApp != null) {
			dc.createAlias("app", "app");
			dc.add(Restrictions.eq("app", configApp));
		}

		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office));
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("statisticDate", startTime));
		}
		if (endTime != null) {
			dc.add(Restrictions.le("statisticDate", endTime));
		}

		dc.addOrder(Order.desc("id"));
		return statisticSealAppDataDao.find(dc);
	}
}
