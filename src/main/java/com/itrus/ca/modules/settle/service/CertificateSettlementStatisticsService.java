/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

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
import com.itrus.ca.modules.settle.entity.CertificateSettlementStatistics;
import com.itrus.ca.modules.statistic.entity.StatisticCertData;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.settle.dao.CertificateSettlementStatisticsDao;

/**
 * 证书结算统计表Service
 * @author qt
 * @version 2015-11-22
 */
@Component
@Transactional(readOnly = true)
public class CertificateSettlementStatisticsService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(CertificateSettlementStatisticsService.class);
	
	@Autowired
	private CertificateSettlementStatisticsDao certificateSettlementStatisticsDao;
	
	public CertificateSettlementStatistics get(Long id) {
		return certificateSettlementStatisticsDao.findOne(id);
	}
	
	public Page<CertificateSettlementStatistics> find(Page<CertificateSettlementStatistics> page, CertificateSettlementStatistics certificateSettlementStatistics) {
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return certificateSettlementStatisticsDao.find(page, dc);
	}
	
	public List<CertificateSettlementStatistics> getSum(ConfigApp configApp, Office office, Date startDate,
			Date endDate, Integer payType){
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app", configApp));
		dc.add(Restrictions.eq("office", office));    
		dc.add(Restrictions.gt("countDate", startDate));
		dc.add(Restrictions.lt("countDate", endDate));
		dc.add(Restrictions.eq("payType", payType));
		return certificateSettlementStatisticsDao.find(dc);
	}
	
	public List<CertificateSettlementStatistics> getSum1(ConfigApp configApp,Integer productType, Office office, Date startDate,
			Date endDate, Integer payType){
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		if(!"-1".equals(productType.toString())){
		dc.add(Restrictions.eq("productType", productType));
		}
		dc.add(Restrictions.eq("app", configApp));
		dc.add(Restrictions.eq("office", office));    
		dc.add(Restrictions.gt("countDate", startDate));
		dc.add(Restrictions.lt("countDate", endDate));
		dc.add(Restrictions.eq("payType", payType));
		return certificateSettlementStatisticsDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(CertificateSettlementStatistics certificateSettlementStatistics) {
		certificateSettlementStatisticsDao.save(certificateSettlementStatistics);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	}
	
	@Transactional(readOnly = true)
	public List<CertificateSettlementStatistics> countHisData(ConfigApp app,Date date){
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app", app));
		dc.add(Restrictions.eq("countDate", date));
		List<CertificateSettlementStatistics> datas = certificateSettlementStatisticsDao.find(dc);
		if (datas!=null&&datas.size()!=0) {
			return datas;
		}else {
			return null;
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteDataByDay(Date date){
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("countDate", date));
		List<CertificateSettlementStatistics> datas = certificateSettlementStatisticsDao.find(dc);
		certificateSettlementStatisticsDao.delete(datas);
		
	}
}
