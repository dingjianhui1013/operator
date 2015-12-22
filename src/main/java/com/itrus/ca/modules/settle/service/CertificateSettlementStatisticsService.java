/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.bean.StaticCertMonth;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.settle.dao.CertificateSettlementStatisticsDao;
import com.itrus.ca.modules.settle.entity.CertificateSettlementStatistics;
import com.itrus.ca.modules.settle.vo.CertificateSettlementStatisticsVO;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 证书结算统计表Service
 * 
 * @author qt
 * @version 2015-11-22
 */
@Service
@Transactional(readOnly = true)
public class CertificateSettlementStatisticsService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(CertificateSettlementStatisticsService.class);

	@Autowired
	private CertificateSettlementStatisticsDao certificateSettlementStatisticsDao;

	public CertificateSettlementStatistics get(Long id) {
		return certificateSettlementStatisticsDao.findOne(id);
	}

	public Page<CertificateSettlementStatistics> find(Page<CertificateSettlementStatistics> page,
			CertificateSettlementStatistics certificateSettlementStatistics) {
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return certificateSettlementStatisticsDao.find(page, dc);
	}

	public List<CertificateSettlementStatistics> getSum(ConfigApp configApp, Office office, Date startDate,
			Date endDate, Integer payType) {
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app", configApp));
		dc.add(Restrictions.eq("office", office));
		dc.add(Restrictions.gt("countDate", startDate));
		dc.add(Restrictions.lt("countDate", endDate));
		dc.add(Restrictions.eq("payType", payType));
		return certificateSettlementStatisticsDao.find(dc);
	}

	public List<CertificateSettlementStatistics> getSum1(ConfigApp configApp, Integer productType, Office office,
			Date startDate, Date endDate, Integer payType) {
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		if (!"-1".equals(productType.toString())) {
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
	public List<CertificateSettlementStatistics> countHisData(ConfigApp app, Date date) {
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app", app));
		dc.add(Restrictions.eq("countDate", date));
		List<CertificateSettlementStatistics> datas = certificateSettlementStatisticsDao.find(dc);
		if (datas != null && datas.size() != 0) {
			return datas;
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void deleteDataByDay(Date date) {
		DetachedCriteria dc = certificateSettlementStatisticsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("countDate", date));
		List<CertificateSettlementStatistics> datas = certificateSettlementStatisticsDao.find(dc);
		certificateSettlementStatisticsDao.delete(datas);

	}

	public List<CertificateSettlementStatisticsVO> findWorkList(Long apply, List<Long> officeIdsList, Date startDate,
			Date endDate) {

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, t.deal_info_type as dealInfoType ,p.product_name productName,t.year year,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,SYS_USER u "
				+ " where t.product_id = p.id and t.app_id =? and t.deal_info_type in(0,1) and t.deal_info_status  in(7,9) "
				+ " and t.create_by = u.id " + " and t.create_date > to_date(? ,'yyyy-MM-dd HH24:mi:ss')"
				+ " and t.create_date <= to_date(? ,'yyyy-MM-dd HH24:mi:ss')";
		if (officeIdsList != null && officeIdsList.size() > 0) {
			sql = sql + " and  u.office_id in(" + StringUtils.collectionToCommaDelimitedString(officeIdsList) + ")";
		}
		sql = sql + " group by  to_char(t.create_date,'YYYY-MM'),t.deal_info_type,p.product_name,t.year"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,t.deal_info_type,p.product_name,t.year";
		List<CertificateSettlementStatisticsVO> resultList = new ArrayList<CertificateSettlementStatisticsVO>();

		resultList = certificateSettlementStatisticsDao.findBySql(sql, CertificateSettlementStatisticsVO.class,			
				apply,DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	public HashMap<String, StaticCertMonth> getStaticMap(List<CertificateSettlementStatisticsVO> findWorkList) {
		HashMap<String, StaticCertMonth> monthMap = new HashMap<String, StaticCertMonth>();
		for (int i = 0; i < findWorkList.size(); i++) {
			CertificateSettlementStatisticsVO cssv = findWorkList.get(i);
			StaticCertMonth scm = monthMap.get(cssv.getMonth());
			if (scm == null) {
				scm = new StaticCertMonth();
			}
			if (cssv.getDealInfoType() == 0) {
				switch (cssv.getProductName()) {
				case "1":
					switch (cssv.getYear()) {
					case 1:
						scm.setOneAdd1(cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setOneAdd2(cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setOneAdd4(cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setOneAdd5(cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2":
					switch (cssv.getYear()) {
					case 1:
						scm.setTwoAdd1(cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setTwoAdd2(cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setTwoAdd4(cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setTwoAdd5(cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":
					switch (cssv.getYear()) {
					case 1:
						scm.setFourAdd1(cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setFourAdd2(cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setFourAdd4(cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setFourAdd5(cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			} else if (cssv.getDealInfoType() == 1) {
				switch (cssv.getProductName()) {
				case "1":
					switch (cssv.getYear()) {
					case 1:
						scm.setOneRenew1(cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setOneRenew2(cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setOneRenew4(cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setOneRenew5(cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2":
					switch (cssv.getYear()) {
					case 1:
						scm.setTwoRenew1(cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setTwoRenew2(cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setTwoRenew4(cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setTwoRenew5(cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":
					switch (cssv.getYear()) {
					case 1:
						scm.setFourRenew1(cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setFourRenew2(cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setFourRenew4(cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setFourRenew5(cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			}
			monthMap.put(cssv.getMonth(), scm);

		}
		return monthMap;
	}
}
