/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;
import com.itrus.ca.modules.statistic.entity.StatisticSealDayData;
import com.itrus.ca.modules.statistic.dao.StatisticSealDayDataDao;

/**
 * 印章日经营Service
 * @author DingJianHui
 * @version 2016-03-23
 */
@Component
@Transactional(readOnly = true)
public class StatisticSealDayDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(StatisticSealDayDataService.class);
	
	@Autowired
	private StatisticSealDayDataDao statisticSealDayDataDao;
	
	public StatisticSealDayData get(Long id) {
		return statisticSealDayDataDao.findOne(id);
	}
	
	public Page<StatisticSealDayData> find(Page<StatisticSealDayData> page, StatisticSealDayData statisticSealDayData) {
		DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(statisticSealDayData.getName())){
//			dc.add(Restrictions.like("name", "%"+statisticSealDayData.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(StatisticSealDayData.DEL_FLAG, StatisticSealDayData.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return statisticSealDayDataDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(StatisticSealDayData statisticSealDayData) {
		statisticSealDayDataDao.save(statisticSealDayData);
	}
	
//	@Transactional(readOnly = false)
//	public void delete(Long id) {
//		statisticSealDayDataDao.deleteById(id);
//	}
	
	// 获取统计日期前多少天没有进行统计的,不做全部汇总，逐条判断 //xx天内，直接在sql中使用to_date(xxxx)-30
		@Transactional(readOnly = true)
		public List<String> getDateNoStatic(String countDate,int days,Long officeId){
			HashSet<String> dateStrings = new HashSet<String>();
			// 发票入库没有，判断发票出库是否有业务没有进行每日经营统计
			String sql  ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from RECEIPT_INVOICE k , RECEIPT_DEPOT_INFO d  where  k.DEPOT_ID = d.ID AND d.office_id = ? AND k.RECEIPT_TYPE=3 AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
					+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_SEAL_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
			List<String> list = statisticSealDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);

			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					dateStrings.add(list.get(i));
				}
			}
			// 发票出库没有，判断是否有业务没有进行每日经营统计
			sql ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from SIGNATURE_INFO k , SYS_USER d  where  k.CREATE_BY = d.ID AND d.office_id = ?  AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
					+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_SEAL_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
			list = statisticSealDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					dateStrings.add(list.get(i));
				}
			}
			List<String> resultList = new ArrayList<String>(); 
			for (String s : dateStrings) {
				resultList.add(s);
			}
			
			
			return list;
		}
		
		@Transactional(readOnly = true)
		public List<StatisticSealDayData> getStatisticDayDataList(String countDate,Long officeId) {
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			dc.createAlias("sysOffice", "office");
			dc.add(Restrictions.eq("statisticDate", DateUtils.parseDate(countDate)));
			dc.add(Restrictions.eq("office.id", officeId ));
			List<StatisticSealDayData> statisticSealDayDataList = statisticSealDayDataDao
					.find(dc);
			return statisticSealDayDataList;
		}
		
		@Transactional(readOnly = false)
		public void deleteDayData(Date day) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			try {
				dc.add(Restrictions.eq("statisticDate", sdf.parse(sdf.format(day))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<StatisticSealDayData> datas = statisticSealDayDataDao.find(dc);
			statisticSealDayDataDao.delete(datas);
		}
		@Transactional(readOnly = false)
		public void deleteDayData(Date day,Long officeId) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			dc.createAlias("sysOffice", "office");
			try {
				dc.add(Restrictions.eq("statisticDate", sdf.parse(sdf.format(day))));
				dc.add(Restrictions.eq("office.id", officeId));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<StatisticSealDayData> datas = statisticSealDayDataDao.find(dc);
			statisticSealDayDataDao.delete(datas);
		}
		
		@Transactional(readOnly = false)
		public List<StatisticSealDayData> getPrevDayDataList(Date curDate,Long officeId) {
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			dc.createAlias("sysOffice", "office");
			dc.add(Restrictions.eq("office.id", officeId));
			dc.add(Restrictions.lt("statisticDate",	curDate));
			dc.addOrder(Order.desc("statisticDate"));
			return  statisticSealDayDataDao.find(dc);
		}
		public Page<StatisticSealDayData> findByDay(Page<StatisticSealDayData> page, StatisticSealDayData statisticSealDayData,Date startTime,Date endTime,Long officeId)
		{
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			dc.createAlias("sysOffice", "office");
			if (officeId != null) {

				dc.add(Restrictions.eq("office.id", officeId));
			}

			if (startTime != null) {
				dc.add(Restrictions.ge("statisticDate", startTime));
			}

			if (endTime != null) {
				dc.add(Restrictions.le("statisticDate", endTime));
			}

			dc.addOrder(Order.desc("statisticDate"));
			
			return statisticSealDayDataDao.find(page,dc);
		}
		public List<StatisticSealDayData> findByDay(Date startTime,Date endTime,Long officeId)
		{
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			dc.createAlias("sysOffice", "office");
			if (officeId != null) {

				dc.add(Restrictions.eq("office.id", officeId));
			}

			if (startTime != null) {
				dc.add(Restrictions.ge("statisticDate", startTime));
			}

			if (endTime != null) {
				dc.add(Restrictions.le("statisticDate", endTime));
			}

			dc.addOrder(Order.desc("statisticDate"));
			
			return statisticSealDayDataDao.find(dc);
		}

		public List<StatisticSealDayData> findByMonth(Long office, Date start,
				Date end) {
			DetachedCriteria dc = statisticSealDayDataDao.createDetachedCriteria();
			dc.createAlias("sysOffice", "office");
			if (office != null) {

				dc.add(Restrictions.eq("office.id", office));
			}

			if (start != null) {
				dc.add(Restrictions.ge("statisticDate", start));
			}

			if (end != null) {
				dc.add(Restrictions.le("statisticDate", end));
			}

			dc.addOrder(Order.desc("statisticDate"));
			
			return statisticSealDayDataDao.find(dc);
		}
			
}
