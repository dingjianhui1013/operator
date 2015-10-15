package com.itrus.ca.modules.statistic.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.modules.statistic.dao.StatisticDayDataDao;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 调拨管理Service
 * 
 * @author CaoYichao
 * @version 2014-07-08
 */
@Component
@Transactional(readOnly = true)
public class StatisticDayDataService extends BaseService {

	/*
	 * @SuppressWarnings("unused") private static Logger logger =
	 * LoggerFactory.getLogger(KeyAllocateApplyService.class);
	 */

	@Autowired
	private StatisticDayDataDao statisticDayDataDao;

	public StatisticDayData get(Long id) {
		return statisticDayDataDao.findOne(id);
	}

	public Page<StatisticDayData> find(Page<StatisticDayData> page,
			StatisticDayData statisticDayData) {
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return statisticDayDataDao.find(page, dc);
	}

	public Page<StatisticDayData> findByDay(Page<StatisticDayData> page,
			StatisticDayData statisticDayData, Long office, Date startTime,
			Date endTime) {
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");

		if (office != null) {

			dc.add(Restrictions.eq("office.id", office));
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("statisticDate", startTime));
		}

		if (endTime != null) {
			dc.add(Restrictions.le("statisticDate", endTime));
		}

		dc.addOrder(Order.desc("statisticDate"));
		return statisticDayDataDao.find(page, dc);
	}
	
	public List<StatisticDayData> findByDay(StatisticDayData statisticDayData, Long office, Date startTime,
			Date endTime) {
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");

		if (office != null) {

			dc.add(Restrictions.eq("office.id", office));
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("statisticDate", startTime));
		}

		if (endTime != null) {
			dc.add(Restrictions.le("statisticDate", endTime));
		}

		dc.addOrder(Order.desc("statisticDate"));
		return statisticDayDataDao.find(dc);
	}

	public List<StatisticDayData> getYesterDayDatas(Date yesterDay,
			Office office) {
		// 得到前一天数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.eq("office.id", office.getId()));
		try {
			dc.add(Restrictions.eq("statisticDate",
					sdf.parse(sdf.format(yesterDay))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statisticDayDataDao.find(dc);
	}
	// 得到上一条数据
	public List<StatisticDayData> getPrevDayDatas(Date curDate,
			Long officeId) {
		
		
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.lt("statisticDate",	curDate));
		dc.addOrder(Order.desc("statisticDate"));
		Page<StatisticDayData>	ps1 = new Page<StatisticDayData>(0,1); //第0页，每页1条
		Page<StatisticDayData>	ps = statisticDayDataDao.find(ps1, dc);
		
		return ps.getList();
	}
	@Transactional(readOnly = false)
	public void save(StatisticDayData data) {
		statisticDayDataDao.save(data);
	}

	public List<StatisticDayData> findByMonth(Long office, Date startTime,
			Date endTime) {
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");

		if (office != null) {

			dc.add(Restrictions.eq("office.id", office));
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("createDate", startTime));
		}

		if (endTime != null) {
			dc.add(Restrictions.lt("createDate", endTime));
		}

		dc.addOrder(Order.desc("id"));
		return statisticDayDataDao.find(dc);
	}

	@Transactional(readOnly = false)
	public void deleteDayData(Date day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		try {
			dc.add(Restrictions.eq("statisticDate", sdf.parse(sdf.format(day))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<StatisticDayData> datas = statisticDayDataDao.find(dc);
		statisticDayDataDao.delete(datas);
	}
	
	@Transactional(readOnly = false)
	public void deleteDayData(Date day,Long officeId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		try {
			dc.add(Restrictions.eq("statisticDate", sdf.parse(sdf.format(day))));
			dc.add(Restrictions.eq("office.id", officeId));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<StatisticDayData> datas = statisticDayDataDao.find(dc);
		statisticDayDataDao.delete(datas);
	}

	/*
	 * @Transactional(readOnly = false) public void save(KeyAllocateApply
	 * keyAllocateApply) { keyAllocateApplyDao.save(keyAllocateApply); }
	 */

	/*
	 * @Transactional(readOnly = false) public void delete(Long id) {
	 * keyAllocateApplyDao.deleteById(id); }
	 */
	@Transactional(readOnly = true)
	public List<StatisticDayData> getStatisticDayDataList(String countDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		try {
			dc.add(Restrictions.eq("statisticDate", sdf.parse(countDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<StatisticDayData> statisticDayDataList = statisticDayDataDao
				.find(dc);
		return statisticDayDataList;
	}
	//获取某天，某个officeId的日经营统计数据
	@Transactional(readOnly = true)
	public List<StatisticDayData> getStatisticDayDataList(String countDate,Long officeId) {
		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.eq("statisticDate", DateUtils.parseDate(countDate)));
		dc.add(Restrictions.eq("office.id", officeId ));
		List<StatisticDayData> statisticDayDataList = statisticDayDataDao
				.find(dc);
		return statisticDayDataList;
	}


	// 获取统计日期前多少天没有进行统计的,不做全部汇总，逐条判断 //xx天内，直接在sql中使用to_date(xxxx)-30
	@Transactional(readOnly = true)
	public List<String> getDateNoStatic(String countDate,int days,Long officeId){
	
		String sql ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from KEY_USB_KEY k , KEY_USB_KEY_DEPOT d  where  k.USB_KEY_DEPOT_ID = d.ID AND d.office_id = ?  AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
				+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
		List<String> list = statisticDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);
		if(list.size()>0){
			return list;
		}
		// 入库没有，判断出库
		sql ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from key_usb_key_invoice k , KEY_USB_KEY_DEPOT d  where  k.USB_KEY_DEPOT_ID = d.ID AND d.office_id = ?  AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
				+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
		list = statisticDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);

		if(list.size()>0){
			return list;
		}
		// UKey 出库没有，判断发票入库是否有业务没有进行每日经营统计
		sql ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from RECEIPT_ENTER_INFO k , RECEIPT_DEPOT_INFO d  where  k.DEPOT_ID = d.ID AND d.office_id = ?  AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
				+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
		list = statisticDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);
		
		if(list.size()>0){
			return list;
		}
		// 发票入库没有，判断发票出库是否有业务没有进行每日经营统计
		sql ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from RECEIPT_INVOICE k , RECEIPT_DEPOT_INFO d  where  k.DEPOT_ID = d.ID AND d.office_id = ?  AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
				+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
		list = statisticDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);

		if(list.size()>0){
			return list;
		}
		// 发票出库没有，判断是否有业务没有进行每日经营统计
		sql ="select  T.DDATE FROM( select  distinct(TO_CHAR(k.CREATE_DATE,'yyyy-MM-dd')) as DDATE from WORK_DEAL_INFO k , SYS_USER d  where  k.CREATE_BY = d.ID AND d.office_id = ?  AND k.CREATE_DATE >= TO_DATE(?, 'yyyy-MM-dd')-"+days+" AND k.CREATE_DATE < TO_DATE(?, 'yyyy-MM-dd')) t "
				+" where t.DDATE NOT IN( select  distinct(TO_CHAR(S.STATISTIC_DATE,'yyyy-MM-dd')) from STATISTIC_DAY_DATA s where s.STATISTIC_DATE >= TO_DATE( ? , 'yyyy-MM-dd')-"+days+" and s.OFFICE_ID= ?) ORDER BY T.DDATE ASC";
		list = statisticDayDataDao.findBySql(sql, officeId,countDate,countDate,countDate,officeId);


		
//		SimpleDateFormat sdf = new SimpleDateFormat();
//		DetachedCriteria dc = statisticDayDataDao.createDetachedCriteria();
//		try {
//			dc.add(Restrictions.eq("statisticDate", sdf.parse(countDate)));
//		} catch (ParseException e) {
//			e.printStackTrace();T
//		}
//		List<StatisticDayData> 	statisticDayDataList = statisticDayDataDao.find(dc);
		return list;
	}
}
