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
import com.itrus.ca.modules.statistic.dao.StatisticAppDataDao;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 日经营统计Service
 * 
 * @author ZhangJingtao
 * @version 2014-07-15
 */
@Component
@Transactional(readOnly = true)
public class StatisticAppDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(StatisticAppDataService.class);

	@Autowired
	private StatisticAppDataDao statisticAppDataDao;

	public StatisticAppData get(Long id) {
		return statisticAppDataDao.findOne(id);
	}

	public List<StatisticAppData> findByCreateDateAndOffice(Long office,
			Date startTime, Date endTime) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();

		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office));
		}
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(endTime);
		startCal.add(Calendar.DATE,1);
		if (startTime != null) {
			dc.add(Restrictions.ge("statisticDate", startTime));
		}
		if (endTime != null) {
			dc.add(Restrictions.lt("statisticDate", startCal.getTime()));
		}

		//dc.addOrder(Order.desc("id"));
		dc.addOrder(Order.desc("statisticDate"));
		return statisticAppDataDao.find(dc);
	}
	public List<StatisticAppData> findByCreateDateAndOffice(Long office,
			Date startTime, Date endTime,List<Long> appIds) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();

		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office));
		}
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(endTime);
		startCal.add(Calendar.DATE,1);
		if (startTime != null) {
			dc.add(Restrictions.ge("statisticDate", startTime));
		}
		if (endTime != null) {
			dc.add(Restrictions.lt("statisticDate", startCal.getTime()));
		}
		if(appIds.size()>0&&appIds!=null)
		{
			dc.add(Restrictions.in("app.id", appIds));
		}
		//dc.addOrder(Order.desc("id"));
		dc.addOrder(Order.desc("statisticDate"));
		return statisticAppDataDao.find(dc);
	}
	public List<StatisticAppData> findByMonth(ConfigApp configApp, Long office,
			Date startTime, Date endTime) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();

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
		return statisticAppDataDao.find(dc);
	}
	public List<StatisticAppData> findByMonth(Long office) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();
		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office));
		}
		dc.addOrder(Order.desc("id"));
		return statisticAppDataDao.find(dc);
	}
	public List<StatisticAppData> findByMonth(Long office,List<Long> appIds) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();
		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office));
		}
		if(appIds.size()>0&&appIds!=null)
		{
			dc.add(Restrictions.in("app.id", appIds));
		}
		dc.addOrder(Order.desc("id"));
		return statisticAppDataDao.find(dc);
	}
	@Transactional(readOnly = false)
	public void save(StatisticAppData statisticAppData) {
		statisticAppDataDao.save(statisticAppData);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
	}

	@Transactional(readOnly = false)
	public void deleteDayData(Date day) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();
		dc.add(Restrictions.ge("statisticDate", day));
		List<StatisticAppData> appDatas = statisticAppDataDao.find(dc);
		statisticAppDataDao.delete(appDatas);
	}
	
	@Transactional(readOnly = false)
	public void deleteDayData(Date day,Long officeId) {
		DetachedCriteria dc = statisticAppDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.ge("statisticDate", day));
		dc.add(Restrictions.eq("office.id", officeId));
		List<StatisticAppData> appDatas = statisticAppDataDao.find(dc);
		statisticAppDataDao.delete(appDatas);
	}

}
