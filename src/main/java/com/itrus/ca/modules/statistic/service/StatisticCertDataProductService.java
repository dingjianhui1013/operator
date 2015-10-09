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
import com.itrus.ca.modules.statistic.entity.StatisticCertDataProduct;
import com.itrus.ca.modules.statistic.dao.StatisticCertDataProductDao;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 证书发放统计Service
 * @author ZhangJingtao
 * @version 2014-07-16
 */
@Component
@Transactional(readOnly = true)
public class StatisticCertDataProductService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(StatisticCertDataProductService.class);
	
	@Autowired
	private StatisticCertDataProductDao statisticCertDataProductDao;
	
	public StatisticCertDataProduct get(Long id) {
		return statisticCertDataProductDao.findOne(id);
	}
	
	public Page<StatisticCertDataProduct> find(Page<StatisticCertDataProduct> page, StatisticCertDataProduct statisticCertDataProduct) {
		DetachedCriteria dc = statisticCertDataProductDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return statisticCertDataProductDao.find(page, dc);
	}
	
	public List<StatisticCertDataProduct> getSum(Integer productType, Office office, Date startTime,
			Date endTime){
		DetachedCriteria dc = statisticCertDataProductDao.createDetachedCriteria();
		if (office != null) {
			dc.add(Restrictions.eq("office", office));
		}
		if (startTime!=null) {
			dc.add(Restrictions.ge("countDate", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.lt("countDate", endTime));
		}
		dc.add(Restrictions.eq("productType", productType));
		return statisticCertDataProductDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(StatisticCertDataProduct statisticCertDataProduct) {
		statisticCertDataProductDao.save(statisticCertDataProduct);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	}
	
	@Transactional(readOnly = false)
	public void deleteDayData(Date day){
		DetachedCriteria dc = statisticCertDataProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("countDate", day));
		List<StatisticCertDataProduct> daList = statisticCertDataProductDao.find(dc);
		statisticCertDataProductDao.delete(daList);
	}
}
