/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.bean.StaticCertMonth;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.settle.dao.CertificateSettlementStatisticsDao;
import com.itrus.ca.modules.settle.entity.CertificateSettlementStatistics;
import com.itrus.ca.modules.settle.vo.Certificate;
import com.itrus.ca.modules.settle.vo.CertificateF;
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

	public List<CertificateSettlementStatisticsVO> findWorkList(Long apply, String productType, String workTypes,
			String officeIdsList, String agentId, Date startDate, Date endDate, boolean multiType) {
		if (multiType)
			return findMulitWorkList(apply, productType, workTypes, officeIdsList, agentId, startDate, endDate);

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, greatest(NVL(t.deal_info_type,-1) , NVL(t.deal_info_type1,-1) , NVL(t.deal_info_type2,-1 )) as dealInfoType ,p.product_name productName,t.year year,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,SYS_USER u ,config_agent_bound_deal_info b "
				+ " where t.product_id = p.id and t.app_id =?  and t.deal_info_status  in(7,9) and b.deal_info = t.id "
				+ " and t.create_by = u.id " + " and t.create_date > to_date(? ,'yyyy-MM-dd HH24:mi:ss')"
				+ " and t.create_date <= to_date(? ,'yyyy-MM-dd HH24:mi:ss')";
		if (StringUtils.isNotBlank(officeIdsList)) {
			sql = sql + " and  u.office_id in(" + officeIdsList + ")";
		}
		if (StringUtils.isNotBlank(productType)) {
			sql = sql + " and  p.product_name in(" + productType + ")";
		}
		if (StringUtils.isNotBlank(agentId)) {
			sql = sql + " and  b.agent_id = " + agentId;
		}
		if (StringUtils.isNotBlank(workTypes)) {
			sql = sql + "and ((t.deal_info_type in(" + workTypes
					+ ") and t.deal_info_type1 is null and t.deal_info_type2 is null ) " + "or (t.deal_info_type1 in("
					+ workTypes + ") and t.deal_info_type is null and t.deal_info_type2 is null) "
					+ "or (t.deal_info_type2 in(" + workTypes + ")"
					+ " and t.deal_info_type is null and t.deal_info_type1 is null))";
		} else { // 默认只查 新增和更新
			sql = sql + "and t.deal_info_type in(0,1) and t.deal_info_type1 is null and t.deal_info_type2 is null";
		}
		sql = sql
				+ " group by  to_char(t.create_date,'YYYY-MM'),greatest(NVL(t.deal_info_type,-1) , NVL(t.deal_info_type1,-1) , NVL(t.deal_info_type2,-1 )) ,p.product_name,t.year"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,greatest(NVL(t.deal_info_type,-1) , NVL(t.deal_info_type1,-1) , NVL(t.deal_info_type2,-1 )) ,p.product_name,t.year";
		List<CertificateSettlementStatisticsVO> resultList = new ArrayList<CertificateSettlementStatisticsVO>();

		resultList = certificateSettlementStatisticsDao.findBySql(sql, CertificateSettlementStatisticsVO.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	public List<CertificateSettlementStatisticsVO> findMulitWorkList(Long apply, String productType, String workTypes,
			String officeIdsList, String agentId, Date startDate, Date endDate) {

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, (NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) as dealInfoType ,p.product_name productName,t.year year,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,SYS_USER u ,config_agent_bound_deal_info b "
				+ " where t.product_id = p.id and t.app_id =?  and t.deal_info_status  in(7,9) and b.deal_info = t.id "
				+ " and t.create_by = u.id " + " and t.create_date > to_date(? ,'yyyy-MM-dd HH24:mi:ss')"
				+ " and t.create_date <= to_date(? ,'yyyy-MM-dd HH24:mi:ss')";
		if (StringUtils.isNotBlank(officeIdsList)) {
			sql = sql + " and  u.office_id in(" + officeIdsList + ")";
		}
		if (StringUtils.isNotBlank(productType)) {
			sql = sql + " and  p.product_name in(" + productType + ")";
		}
		if (StringUtils.isNotBlank(agentId)) {
			sql = sql + " and  b.agent_id = " + agentId;
		}
		if (StringUtils.isNotBlank(workTypes)) {
			sql = sql + "and ((t.deal_info_type in(" + workTypes
					+ ") and (t.deal_info_type1 is not null or t.deal_info_type2 is not null) ) "
					+ "or (t.deal_info_type1 in(" + workTypes
					+ ") and (t.deal_info_type is not null or t.deal_info_type2 is not null)) "
					+ "or (t.deal_info_type2 in(" + workTypes + ")"
					+ " and ( t.deal_info_type is not null or t.deal_info_type1 is not null)))";
		} else { // 默认只查 新增和更新
			sql = sql
					+ "and t.deal_info_type in(0,1) and (t.deal_info_type1 is not null or t.deal_info_type2 is not null)";
		}
		sql = sql
				+ " group by  to_char(t.create_date,'YYYY-MM'),(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year";
		List<CertificateSettlementStatisticsVO> resultList = new ArrayList<CertificateSettlementStatisticsVO>();

		resultList = certificateSettlementStatisticsDao.findBySql(sql, CertificateSettlementStatisticsVO.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	

	public List<CertificateSettlementStatisticsVO> findWorkList1(Long apply, String productType, String workTypes,
			String officeIdsList, String agentId, Date startDate, Date endDate, boolean multiType) {
		if (multiType)
			return findMulitWorkList(apply, productType, workTypes, officeIdsList, agentId, startDate, endDate);

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, greatest(NVL(t.deal_info_type,-1) , NVL(t.deal_info_type1,-1) , NVL(t.deal_info_type2,-1 )) as dealInfoType ,p.product_name productName,t.year year,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,SYS_USER u ,config_agent_bound_deal_info b "
				+ " where t.product_id = p.id and t.app_id =?  and t.deal_info_status  in(7,9) and b.deal_info = t.id "
				+ " and t.create_by = u.id " + " and t.create_date > to_date(? ,'yyyy-MM-dd HH24:mi:ss')"
				+ " and t.create_date <= to_date(? ,'yyyy-MM-dd HH24:mi:ss')";
		if (StringUtils.isNotBlank(officeIdsList)) {
			sql = sql + " and  u.office_id in(" + officeIdsList + ")";
		}
		if (StringUtils.isNotBlank(productType)) {
			sql = sql + " and  p.product_name in(" + productType + ")";
		}
		if (StringUtils.isNotBlank(agentId)) {
			sql = sql + " and  b.agent_id = " + agentId;
		}
		if (StringUtils.isNotBlank(workTypes)) {
			sql = sql + "and (t.deal_info_type in(" + workTypes + ") or t.deal_info_type1 in(" + workTypes
					+ ") or t.deal_info_type2 in(" + workTypes + "))  ";

		} else { // 默认只查 新增和更新
			sql = sql
					+ "and t.deal_info_type in(0,1) and (t.deal_info_type1 is not null or t.deal_info_type2 is not null)";
		}
		sql = sql
				+ " group by  to_char(t.create_date,'YYYY-MM'),greatest(NVL(t.deal_info_type,-1) , NVL(t.deal_info_type1,-1) , NVL(t.deal_info_type2,-1 )) ,p.product_name,t.year"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,greatest(NVL(t.deal_info_type,-1) , NVL(t.deal_info_type1,-1) , NVL(t.deal_info_type2,-1 )) ,p.product_name,t.year";
		List<CertificateSettlementStatisticsVO> resultList = new ArrayList<CertificateSettlementStatisticsVO>();

		resultList = certificateSettlementStatisticsDao.findBySql(sql, CertificateSettlementStatisticsVO.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	public List<CertificateSettlementStatisticsVO> findMulitWorkList1(Long apply, String productType, String workTypes,
			String officeIdsList, String agentIds, Date startDate, Date endDate) {

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, (NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) as dealInfoType ,p.product_name productName,t.year year,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,SYS_USER u ,config_agent_bound_deal_info b "
				+ " where t.product_id = p.id and t.app_id =?  and t.deal_info_status  in(7,9) and b.deal_info = t.id "
				+ " and t.create_by = u.id " + " and t.create_date > to_date(? ,'yyyy-MM-dd HH24:mi:ss')"
				+ " and t.create_date <= to_date(? ,'yyyy-MM-dd HH24:mi:ss')";
		if (StringUtils.isNotBlank(officeIdsList)) {
			sql = sql + " and  u.office_id in(" + officeIdsList + ")";
		}
		if (StringUtils.isNotBlank(productType)) {
			sql = sql + " and  p.product_name in(" + productType + ")";
		}
		if (StringUtils.isNotBlank(agentIds)) {
			sql = sql + " and  b.agent_id in (" + agentIds +")";
		}
		if (StringUtils.isNotBlank(workTypes)) {
			sql = sql + "and (t.deal_info_type in(" + workTypes + ") or t.deal_info_type1 in(" + workTypes
					+ ") or t.deal_info_type2 in(" + workTypes + "))";
		} else { // 默认只查 新增和更新
			sql = sql
					+ "and t.deal_info_type in(0,1) and (t.deal_info_type1 is not null or t.deal_info_type2 is not null)";
		}
		sql = sql
				+ " group by  to_char(t.create_date,'YYYY-MM'),(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year";
		List<CertificateSettlementStatisticsVO> resultList = new ArrayList<CertificateSettlementStatisticsVO>();

		resultList = certificateSettlementStatisticsDao.findBySql(sql, CertificateSettlementStatisticsVO.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	public HashMap<String, CertificateF> getStaticMap1(List<CertificateSettlementStatisticsVO> findWorkList1) {
		HashMap<String, CertificateF> monthMap = new HashMap<String, CertificateF>();
		Integer zerro = new Integer(0); 
		//初始化total防止下面出现空指针
		CertificateF total = new CertificateF(
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro,  
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, 
				 zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro, zerro,zerro
				 );
		for (int i = 0; i < findWorkList1.size(); i++) {
			CertificateSettlementStatisticsVO cssv = findWorkList1.get(i);
			CertificateF scm = monthMap.get(cssv.getMonth());
			if (scm == null) {
				scm = new CertificateF();
			}
			if (cssv.getDealInfoType() == 0)// 新增
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setXzqyadd1(cssv.getWorkCount().intValue());
						total.setXzqyadd1(total.getXzqyadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setXzqyadd2(cssv.getWorkCount().intValue());
						total.setXzqyadd2(total.getXzqyadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setXzqyadd3(cssv.getWorkCount().intValue());
						total.setXzqyadd3(total.getXzqyadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setXzqyadd4(cssv.getWorkCount().intValue());
						total.setXzqyadd4(total.getXzqyadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setXzqyadd5(cssv.getWorkCount().intValue());
						total.setXzqyadd5(total.getXzqyadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setXzgrQadd1(cssv.getWorkCount().intValue());
						total.setXzgrQadd1(total.getXzgrQadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setXzgrQadd2(cssv.getWorkCount().intValue());
						total.setXzgrQadd2(total.getXzgrQadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setXzgrQadd3(cssv.getWorkCount().intValue());
						total.setXzgrQadd3(total.getXzgrQadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setXzgrQadd4(cssv.getWorkCount().intValue());
						total.setXzgrQadd4(total.getXzgrQadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setXzgrQadd5(cssv.getWorkCount().intValue());
						total.setXzgrQadd5(total.getXzgrQadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setXzgrGadd1(cssv.getWorkCount().intValue());
						total.setXzgrGadd1(total.getXzgrGadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setXzgrGadd2(cssv.getWorkCount().intValue());
						total.setXzgrGadd2(total.getXzgrGadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setXzgrGadd3(cssv.getWorkCount().intValue());
						total.setXzgrGadd3(total.getXzgrGadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setXzgrGadd4(cssv.getWorkCount().intValue());
						total.setXzgrGadd4(total.getXzgrGadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setXzgrGadd5(cssv.getWorkCount().intValue());
						total.setXzgrGadd5(total.getXzgrGadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setXzjgadd1(cssv.getWorkCount().intValue());
						total.setXzjgadd1(total.getXzjgadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setXzjgadd2(cssv.getWorkCount().intValue());
						total.setXzjgadd2(total.getXzjgadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setXzjgadd3(cssv.getWorkCount().intValue());
						total.setXzjgadd3(total.getXzjgadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setXzjgadd4(cssv.getWorkCount().intValue());
						total.setXzjgadd4(total.getXzjgadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setXzjgadd5(cssv.getWorkCount().intValue());
						total.setXzjgadd5(total.getXzjgadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			} else if (cssv.getDealInfoType() == 100) {
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setGxqyadd1(cssv.getWorkCount().intValue());
						total.setGxqyadd1(total.getGxqyadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setGxqyadd2(cssv.getWorkCount().intValue());
						total.setGxqyadd2(total.getGxqyadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setGxqyadd3(cssv.getWorkCount().intValue());
						total.setGxqyadd3(total.getGxqyadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setGxqyadd4(cssv.getWorkCount().intValue());
						total.setGxqyadd4(total.getGxqyadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setGxqyadd5(cssv.getWorkCount().intValue());
						total.setGxqyadd5(total.getGxqyadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setGxgrQadd1(cssv.getWorkCount().intValue());
						total.setGxgrQadd1(total.getGxgrQadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setGxgrQadd2(cssv.getWorkCount().intValue());
						total.setGxgrQadd2(total.getGxgrQadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setGxgrQadd3(cssv.getWorkCount().intValue());
						total.setGxgrQadd3(total.getGxgrQadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setGxgrQadd4(cssv.getWorkCount().intValue());
						total.setGxgrQadd4(total.getGxgrQadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setGxgrQadd5(cssv.getWorkCount().intValue());
						total.setGxgrQadd5(total.getGxgrQadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setGxgrGadd1(cssv.getWorkCount().intValue());
						total.setGxgrGadd1(total.getGxgrGadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setGxgrGadd2(cssv.getWorkCount().intValue());
						total.setGxgrGadd2(total.getGxgrGadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setGxgrGadd3(cssv.getWorkCount().intValue());
						total.setGxgrGadd3(total.getGxgrGadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setGxgrGadd4(cssv.getWorkCount().intValue());
						total.setGxgrGadd4(total.getGxgrGadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setGxgrGadd5(cssv.getWorkCount().intValue());
						total.setGxgrGadd5(total.getGxgrGadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setGxjgadd1(cssv.getWorkCount().intValue());
						total.setGxjgadd1(total.getGxjgadd1() +cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setGxjgadd2(cssv.getWorkCount().intValue());
						total.setGxjgadd2(total.getGxjgadd2() +cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setGxjgadd3(cssv.getWorkCount().intValue());
						total.setGxjgadd3(total.getGxjgadd3() +cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setGxjgadd4(cssv.getWorkCount().intValue());
						total.setGxjgadd4(total.getGxjgadd4() +cssv.getWorkCount().intValue());
						break;
					case 5:
						scm.setGxjgadd5(cssv.getWorkCount().intValue());
						total.setGxjgadd5(total.getGxjgadd5() +cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			} else if (cssv.getDealInfoType() == 20) {
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setLostCerateqy(cssv.getWorkCount().intValue());
					total.setLostCerateqy(total.getLostCerateqy()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setLostCerategrQ(cssv.getWorkCount().intValue());
					total.setLostCerategrQ(total.getLostCerategrQ()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setLostCerategrG(cssv.getWorkCount().intValue());
					total.setLostCerategrG(total.getLostCerategrG()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setLostCeratejg(cssv.getWorkCount().intValue());
					total.setLostCeratejg(total.getLostCeratejg()+cssv.getWorkCount().intValue());
					break;

				}
			} else if (cssv.getDealInfoType() == 30) {
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setDamageCertificateqy(cssv.getWorkCount().intValue());
					total.setDamageCertificateqy(total.getDamageCertificateqy()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setDamageCertificategrQ(cssv.getWorkCount().intValue());
					total.setDamageCertificategrQ(total.getDamageCertificategrQ()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setDamageCertificategrG(cssv.getWorkCount().intValue());
					total.setDamageCertificategrG(total.getDamageCertificategrG()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setDamageCertificatejg(cssv.getWorkCount().intValue());
					total.setDamageCertificatejg(total.getDamageCertificatejg()+cssv.getWorkCount().intValue());
					break;

				}
			} else if (cssv.getDealInfoType() == 004) {
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setModifyNumqy(cssv.getWorkCount().intValue());
					total.setModifyNumqy(total.getModifyNumqy()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setModifyNumgrQ(cssv.getWorkCount().intValue());
					total.setModifyNumgrQ(total.getModifyNumgrQ()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setModifyNumgrG(cssv.getWorkCount().intValue());
					total.setModifyNumgrG(total.getModifyNumgrG()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setModifyNumjg(cssv.getWorkCount().intValue());
					total.setModifyNumjg(total.getModifyNumjg()+cssv.getWorkCount().intValue());
					break;

				}
			}else if (cssv.getDealInfoType() == 24) {
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setChangeLostqyNum(cssv.getWorkCount().intValue());
					total.setChangeLostqyNum(total.getChangeLostqyNum()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setChangeLostgrQNum(cssv.getWorkCount().intValue());
					total.setChangeLostgrQNum(total.getChangeLostgrQNum()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setChangeLostgrGNum(cssv.getWorkCount().intValue());
					total.setChangeLostgrGNum(total.getChangeLostgrGNum()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setChangeLostjgNum(cssv.getWorkCount().intValue());
					total.setChangeLostjgNum(total.getChangeLostjgNum()+cssv.getWorkCount().intValue());
					break;

				}
			} else if (cssv.getDealInfoType() == 34) {
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setChangeReplaceqyNum(cssv.getWorkCount().intValue());
					total.setChangeReplaceqyNum(total.getChangeReplaceqyNum()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setChangeReplacegrQNum(cssv.getWorkCount().intValue());
					total.setChangeReplacegrQNum(total.getChangeReplacegrQNum()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setChangeReplacegrGNum(cssv.getWorkCount().intValue());
					total.setChangeReplacegrGNum(total.getChangeReplacegrGNum()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setChangeReplacejgNum(cssv.getWorkCount().intValue());
					total.setChangeReplacejgNum(total.getChangeReplacejgNum()+cssv.getWorkCount().intValue());
					break;

				}
			}  
			else if (cssv.getDealInfoType() == 104)// 更新+变更
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateChangeqyNum(cssv.getWorkCount().intValue());
						total.setUpdateChangeqyNum(total.getUpdateChangeqyNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateChangeqyNum2(cssv.getWorkCount().intValue());
						total.setUpdateChangeqyNum2(total.getUpdateChangeqyNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateChangeqyNum3(cssv.getWorkCount().intValue());
						total.setUpdateChangeqyNum3(total.getUpdateChangeqyNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateChangeqyNum4(cssv.getWorkCount().intValue() );
						total.setUpdateChangeqyNum4(total.getUpdateChangeqyNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateChangeqyNum5(cssv.getWorkCount().intValue());
						total.setUpdateChangeqyNum5(total.getUpdateChangeqyNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateChangegrQNum(cssv.getWorkCount().intValue());
						total.setUpdateChangegrQNum(total.getUpdateChangegrQNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateChangegrQNum2(cssv.getWorkCount().intValue());
						total.setUpdateChangegrQNum2(total.getUpdateChangegrQNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateChangegrQNum3(cssv.getWorkCount().intValue());
						total.setUpdateChangegrQNum3(total.getUpdateChangegrQNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateChangegrQNum4(cssv.getWorkCount().intValue() );
						total.setUpdateChangegrQNum4(total.getUpdateChangegrQNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateChangegrQNum5(cssv.getWorkCount().intValue());
						total.setUpdateChangegrQNum5(total.getUpdateChangegrQNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateChangegrGNum(cssv.getWorkCount().intValue());
						total.setUpdateChangegrGNum(total.getUpdateChangegrGNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateChangegrGNum2(cssv.getWorkCount().intValue());
						total.setUpdateChangegrGNum2(total.getUpdateChangegrGNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateChangegrGNum3(cssv.getWorkCount().intValue());
						total.setUpdateChangegrGNum3(total.getUpdateChangegrGNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateChangegrGNum4(cssv.getWorkCount().intValue() );
						total.setUpdateChangegrGNum4(total.getUpdateChangegrGNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateChangegrGNum5(cssv.getWorkCount().intValue());
						total.setUpdateChangegrGNum5(total.getUpdateChangegrGNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateChangejgNum(cssv.getWorkCount().intValue());
						total.setUpdateChangejgNum(total.getUpdateChangejgNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateChangejgNum2(cssv.getWorkCount().intValue());
						total.setUpdateChangejgNum2(total.getUpdateChangejgNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateChangejgNum3(cssv.getWorkCount().intValue());
						total.setUpdateChangejgNum3(total.getUpdateChangejgNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateChangejgNum4(cssv.getWorkCount().intValue() );
						total.setUpdateChangejgNum4(total.getUpdateChangejgNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateChangejgNum5(cssv.getWorkCount().intValue());
						total.setUpdateChangejgNum5(total.getUpdateChangejgNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			} else if (cssv.getDealInfoType() == 120)// 更新+遗失补办
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateLostqyNum(cssv.getWorkCount().intValue());
						total.setUpdateLostqyNum(total.getUpdateLostqyNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateLostqyNum2(cssv.getWorkCount().intValue());
						total.setUpdateLostqyNum2(total.getUpdateLostqyNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateLostqyNum3(cssv.getWorkCount().intValue());
						total.setUpdateLostqyNum3(total.getUpdateLostqyNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateLostqyNum4(cssv.getWorkCount().intValue() );
						total.setUpdateLostqyNum4(total.getUpdateLostqyNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateLostqyNum5(cssv.getWorkCount().intValue());
						total.setUpdateLostqyNum5(total.getUpdateLostqyNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateLostgrQNum(cssv.getWorkCount().intValue());
						total.setUpdateLostgrQNum(total.getUpdateLostgrQNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateLostgrQNum2(cssv.getWorkCount().intValue());
						total.setUpdateLostgrQNum2(total.getUpdateLostgrQNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateLostgrQNum3(cssv.getWorkCount().intValue());
						total.setUpdateLostgrQNum3(total.getUpdateLostgrQNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateLostgrQNum4(cssv.getWorkCount().intValue() );
						total.setUpdateLostgrQNum4(total.getUpdateLostgrQNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateLostgrQNum5(cssv.getWorkCount().intValue());
						total.setUpdateLostgrQNum5(total.getUpdateLostgrQNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateLostgrGNum(cssv.getWorkCount().intValue());
						total.setUpdateLostgrGNum(total.getUpdateLostgrGNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateLostgrGNum2(cssv.getWorkCount().intValue());
						total.setUpdateLostgrGNum2(total.getUpdateLostgrGNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateLostgrGNum3(cssv.getWorkCount().intValue());
						total.setUpdateLostgrGNum3(total.getUpdateLostgrGNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateLostgrGNum4(cssv.getWorkCount().intValue() );
						total.setUpdateLostgrGNum4(total.getUpdateLostgrGNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateLostgrGNum5(cssv.getWorkCount().intValue());
						total.setUpdateLostgrGNum5(total.getUpdateLostgrGNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateLostjgNum(cssv.getWorkCount().intValue());
						total.setUpdateLostjgNum(total.getUpdateLostjgNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateLostjgNum2(cssv.getWorkCount().intValue());
						total.setUpdateLostjgNum2(total.getUpdateLostjgNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateLostjgNum3(cssv.getWorkCount().intValue());
						total.setUpdateLostjgNum3(total.getUpdateLostjgNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateLostjgNum4(cssv.getWorkCount().intValue() );
						total.setUpdateLostjgNum4(total.getUpdateLostjgNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateLostjgNum5(cssv.getWorkCount().intValue());
						total.setUpdateLostjgNum5(total.getUpdateLostjgNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			}else if (cssv.getDealInfoType() == 130)// 更新+损坏更换
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateReplaceqyNum(cssv.getWorkCount().intValue());
						total.setUpdateReplaceqyNum(total.getUpdateReplaceqyNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateReplaceqyNum2(cssv.getWorkCount().intValue());
						total.setUpdateReplaceqyNum2(total.getUpdateReplaceqyNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateReplaceqyNum3(cssv.getWorkCount().intValue());
						total.setUpdateReplaceqyNum3(total.getUpdateReplaceqyNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateReplaceqyNum4(cssv.getWorkCount().intValue() );
						total.setUpdateReplaceqyNum4(total.getUpdateReplaceqyNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateReplaceqyNum5(cssv.getWorkCount().intValue());
						total.setUpdateReplaceqyNum5(total.getUpdateReplaceqyNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateReplacegrQNum(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrQNum(total.getUpdateReplacegrQNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateReplacegrQNum2(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrQNum2(total.getUpdateReplacegrQNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateReplacegrQNum3(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrQNum3(total.getUpdateReplacegrQNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateReplacegrQNum4(cssv.getWorkCount().intValue() );
						total.setUpdateReplacegrQNum4(total.getUpdateReplacegrQNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateReplacegrQNum5(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrQNum5(total.getUpdateReplacegrQNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateReplacegrGNum(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrGNum(total.getUpdateReplacegrGNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateReplacegrGNum2(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrGNum2(total.getUpdateReplacegrGNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateReplacegrGNum3(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrGNum3(total.getUpdateReplacegrGNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateReplacegrGNum4(cssv.getWorkCount().intValue() );
						total.setUpdateReplacegrGNum4(total.getUpdateReplacegrGNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateReplacegrGNum5(cssv.getWorkCount().intValue());
						total.setUpdateReplacegrGNum5(total.getUpdateReplacegrGNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setUpdateReplacejgNum(cssv.getWorkCount().intValue());
						total.setUpdateReplacejgNum(total.getUpdateReplacejgNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setUpdateReplacejgNum2(cssv.getWorkCount().intValue());
						total.setUpdateReplacejgNum2(total.getUpdateReplacejgNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setUpdateReplacejgNum3(cssv.getWorkCount().intValue());
						total.setUpdateReplacejgNum3(total.getUpdateReplacejgNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setUpdateReplacejgNum4(cssv.getWorkCount().intValue() );
						total.setUpdateReplacejgNum4(total.getUpdateReplacejgNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setUpdateReplacejgNum5(cssv.getWorkCount().intValue());
						total.setUpdateReplacejgNum5(total.getUpdateReplacejgNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			}else if (cssv.getDealInfoType() == 130) //变更+遗失补办
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setChangeLostqyNum(cssv.getWorkCount().intValue());
					total.setChangeLostqyNum(total.getChangeLostqyNum()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setChangeLostgrQNum(cssv.getWorkCount().intValue());
					total.setChangeLostgrQNum(total.getChangeLostgrQNum()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setChangeLostgrGNum(cssv.getWorkCount().intValue());
					total.setChangeLostgrGNum(total.getChangeLostgrGNum()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setChangeLostjgNum(cssv.getWorkCount().intValue());
					total.setChangeLostjgNum(total.getChangeLostjgNum()+cssv.getWorkCount().intValue());
					break;

				}
			} else if (cssv.getDealInfoType() == 140)//变更+损坏更换
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					scm.setChangeReplaceqyNum(cssv.getWorkCount().intValue());
					total.setChangeReplaceqyNum(total.getChangeReplaceqyNum()+cssv.getWorkCount().intValue());
					break;

				case "2": // 个人证书（企业）

					scm.setChangeReplacegrQNum(cssv.getWorkCount().intValue());
					total.setChangeReplacegrQNum(total.getChangeReplacegrQNum()+cssv.getWorkCount().intValue());
					break;

				case "6":// 个人证书（机构）

					scm.setChangeReplacegrGNum(cssv.getWorkCount().intValue());
					total.setChangeReplacegrGNum(total.getChangeReplacegrGNum()+cssv.getWorkCount().intValue());
					break;

				case "3":// 机构证书

					scm.setChangeReplacejgNum(cssv.getWorkCount().intValue());
					total.setChangeReplacejgNum(total.getChangeReplacejgNum()+cssv.getWorkCount().intValue());
					break;

				}
			}else if (cssv.getDealInfoType() == 124)// 更新+遗失补办+变更
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateLostqyNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostqyNum(total.getChangeUpdateLostqyNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateLostqyNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostqyNum2(total.getChangeUpdateLostqyNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateLostqyNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostqyNum3(total.getChangeUpdateLostqyNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateLostqyNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateLostqyNum4(total.getChangeUpdateLostqyNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateLostqyNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostqyNum5(total.getChangeUpdateLostqyNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateLostgrQNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrQNum(total.getChangeUpdateLostgrQNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateLostgrQNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrQNum2(total.getChangeUpdateLostgrQNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateLostgrQNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrQNum3(total.getChangeUpdateLostgrQNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateLostgrQNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateLostgrQNum4(total.getChangeUpdateLostgrQNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateLostgrQNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrQNum5(total.getChangeUpdateLostgrQNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateLostgrGNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrGNum(total.getChangeUpdateLostgrGNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateLostgrGNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrGNum2(total.getChangeUpdateLostgrGNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateLostgrGNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrGNum3(total.getChangeUpdateLostgrGNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateLostgrGNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateLostgrGNum4(total.getChangeUpdateLostgrGNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateLostgrGNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostgrGNum5(total.getChangeUpdateLostgrGNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateLostjgNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostjgNum(total.getChangeUpdateLostjgNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateLostjgNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostjgNum2(total.getChangeUpdateLostjgNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateLostjgNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostjgNum3(total.getChangeUpdateLostjgNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateLostjgNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateLostjgNum4(total.getChangeUpdateLostjgNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateLostjgNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateLostjgNum5(total.getChangeUpdateLostjgNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			}else if (cssv.getDealInfoType() == 134)// 更新+损坏更换+变更
			{
				switch (cssv.getProductName()) {
				case "1": // 企业证书
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateReplaceqyNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplaceqyNum(total.getChangeUpdateReplaceqyNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateReplaceqyNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplaceqyNum2(total.getChangeUpdateReplaceqyNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateReplaceqyNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplaceqyNum3(total.getChangeUpdateReplaceqyNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateReplaceqyNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateReplaceqyNum4(total.getChangeUpdateReplaceqyNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateReplaceqyNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplaceqyNum5(total.getChangeUpdateReplaceqyNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "2": // 个人证书（企业）
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateReplacegrQNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrQNum(total.getChangeUpdateReplacegrQNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateReplacegrQNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrQNum2(total.getChangeUpdateReplacegrQNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateReplacegrQNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrQNum3(total.getChangeUpdateReplacegrQNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateReplacegrQNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateReplacegrQNum4(total.getChangeUpdateReplacegrQNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateReplacegrQNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrQNum5(total.getChangeUpdateReplacegrQNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "6":// 个人证书（机构）
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateReplacegrGNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrGNum(total.getChangeUpdateReplacegrGNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateReplacegrGNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrGNum2(total.getChangeUpdateReplacegrGNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateReplacegrGNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrGNum3(total.getChangeUpdateReplacegrGNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateReplacegrGNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateReplacegrGNum4(total.getChangeUpdateReplacegrGNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateReplacegrGNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacegrGNum5(total.getChangeUpdateReplacegrGNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;
				case "3":// 机构证书
					switch (cssv.getYear()) {
					case 1:
						scm.setChangeUpdateReplacejgNum(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacejgNum(total.getChangeUpdateReplacejgNum()+cssv.getWorkCount().intValue());
						break;
					case 2:
						scm.setChangeUpdateReplacejgNum2(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacejgNum2(total.getChangeUpdateReplacejgNum2()+cssv.getWorkCount().intValue());
						break;
					case 3:
						scm.setChangeUpdateReplacejgNum3(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacejgNum3(total.getChangeUpdateReplacejgNum3()+cssv.getWorkCount().intValue());
						break;
					case 4:
						scm.setChangeUpdateReplacejgNum4(cssv.getWorkCount().intValue() );
						total.setChangeUpdateReplacejgNum4(total.getChangeUpdateReplacejgNum4()+cssv.getWorkCount().intValue() );
						break;
					case 5:
						scm.setChangeUpdateReplacejgNum5(cssv.getWorkCount().intValue());
						total.setChangeUpdateReplacejgNum5(total.getChangeUpdateReplacejgNum5()+cssv.getWorkCount().intValue());
						break;
					}
					break;

				}
			}

			monthMap.put(cssv.getMonth(), scm);
		}
		monthMap.put("total", total);
		return monthMap;
	}

	private CertificateF CertificateF(Integer zerro, Integer zerro2,
			Integer zerro3, Integer zerro4, Integer zerro5, Integer zerro6,
			Integer zerro7, Integer zerro8, Integer zerro9, Integer zerro10,
			Integer zerro11, Integer zerro12, Integer zerro13, Integer zerro14,
			Integer zerro15, Integer zerro16, Integer zerro17, Integer zerro18,
			Integer zerro19, Integer zerro20, Integer zerro21, Integer zerro22,
			Integer zerro23, Integer zerro24, Integer zerro25, Integer zerro26,
			Integer zerro27, Integer zerro28, Integer zerro29, Integer zerro30,
			Integer zerro31, Integer zerro32, Integer zerro33, Integer zerro34,
			Integer zerro35, Integer zerro36, Integer zerro37, Integer zerro38,
			Integer zerro39, Integer zerro40, Integer zerro41, Integer zerro42,
			Integer zerro43, Integer zerro44, Integer zerro45, Integer zerro46,
			Integer zerro47, Integer zerro48, Integer zerro49, Integer zerro50,
			Integer zerro51, Integer zerro52, Integer zerro53, Integer zerro54,
			Integer zerro55, Integer zerro56, Integer zerro57, Integer zerro58,
			Integer zerro59, Integer zerro60, Integer zerro61, Integer zerro62,
			Integer zerro63, Integer zerro64, Integer zerro65, Integer zerro66,
			Integer zerro67, Integer zerro68, Integer zerro69, Integer zerro70,
			Integer zerro71, Integer zerro72, Integer zerro73, Integer zerro74,
			Integer zerro75, Integer zerro76, Integer zerro77, Integer zerro78,
			Integer zerro79, Integer zerro80, Integer zerro81, Integer zerro82,
			Integer zerro83, Integer zerro84, Integer zerro85, Integer zerro86,
			Integer zerro87, Integer zerro88, Integer zerro89, Integer zerro90,
			Integer zerro91, Integer zerro92, Integer zerro93, Integer zerro94,
			Integer zerro95, Integer zerro96, Integer zerro97, Integer zerro98,
			Integer zerro99, Integer zerro100, Integer zerro101,
			Integer zerro102, Integer zerro103, Integer zerro104,
			Integer zerro105, Integer zerro106, Integer zerro107,
			Integer zerro108, Integer zerro109, Integer zerro110,
			Integer zerro111, Integer zerro112, Integer zerro113,
			Integer zerro114, Integer zerro115, Integer zerro116,
			Integer zerro117, Integer zerro118, Integer zerro119,
			Integer zerro120, Integer zerro121, Integer zerro122,
			Integer zerro123, Integer zerro124, Integer zerro125,
			Integer zerro126, Integer zerro127, Integer zerro128,
			Integer zerro129, Integer zerro130, Integer zerro131,
			Integer zerro132, Integer zerro133, Integer zerro134,
			Integer zerro135, Integer zerro136, Integer zerro137,
			Integer zerro138, Integer zerro139, Integer zerro140,
			Integer zerro141, Integer zerro142, Integer zerro143,
			Integer zerro144, Integer zerro145, Integer zerro146,
			Integer zerro147, Integer zerro148, Integer zerro149,
			Integer zerro150, Integer zerro151, Integer zerro152,
			Integer zerro153, Integer zerro154, Integer zerro155,
			Integer zerro156, Integer zerro157, Integer zerro158,
			Integer zerro159, Integer zerro160) {
		// TODO Auto-generated method stub
		return null;
	}
}
