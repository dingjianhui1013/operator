/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.settle.dao.SettleSupplierCertDetailDao;
import com.itrus.ca.modules.settle.entity.SettleSupplierCertDetail;

/**
 * 供应商证书结算Service
 * 
 * @author WHW
 * @version 2014-07-17
 */
@Component
@Transactional(readOnly = true)
public class SettleSupplierCertDetailService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(SettleSupplierCertDetailService.class);

	@Autowired
	private SettleSupplierCertDetailDao settleSupplierCertDetailDao;

	public SettleSupplierCertDetail get(Long id) {
		return settleSupplierCertDetailDao.findOne(id);
	}

	public Page<SettleSupplierCertDetail> find(
			Page<SettleSupplierCertDetail> page,
			SettleSupplierCertDetail settleSupplierCertDetail) {
		DetachedCriteria dc = settleSupplierCertDetailDao
				.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return settleSupplierCertDetailDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(SettleSupplierCertDetail settleSupplierCertDetail) {
		settleSupplierCertDetailDao.save(settleSupplierCertDetail);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		settleSupplierCertDetailDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	public List<Map<String, Object>> findSupplierJS(
			ConfigSupplier configSupplier, String staDate, String endDate) {
		String timeSQL = "";
		if (StringUtils.isNotEmpty(staDate)) {
			timeSQL += " and settle_date >= to_date('" + staDate
					+ "','yyyy-mm-dd')";
		}
		if (StringUtils.isNotEmpty(staDate)) {
			timeSQL += " and settle_date <= to_date('" + endDate
					+ "','yyyy-mm-dd') ";
		}
		/*
		 * String strSQL =""; strSQL =
		 * "select ou as ou,product_type as product_type,SUM(s.amount_year1) as year1,SUM(s.amount_year2) as year2,SUM(s.amount_year4) as year4,SUM(s.amount_year5) as year5,SUM(s.change_amount) as change_amount,SUM(s.frw_bb_amount) as frw_bb_amount,SUM(s.replace_amount) as replace_amount,SUM(s.revoke_amount) as revoke_amount,SUM(s.rw_bb_amount) as rw_bb_amount,SUM(s.test_amount) as test_amount,SUM(s.total_amount) as total_amount,SUM(s.add_amount) as add_amount from settle_supplier_cert_detail s where 1=1"
		 * ; if(configSupplier.getId()!=null) strSQL =
		 * " and supplier_id="+configSupplier.getId(); strSQL = strSQL + timeSQL
		 * + " GROUP BY ou,product_type ORDER BY ou,product_type";
		 */
		// Query query = settleSupplierCertDetailDao.createSqlQuery(strSQL);
		Query query = settleSupplierCertDetailDao
				.createSqlQuery("select ou as ou,product_type as product_type,SUM(s.amount_year1) as year1,SUM(s.amount_year2) as year2,SUM(s.amount_year3) as year3,SUM(s.amount_year4) as year4,SUM(s.amount_year5) as year5,SUM(s.change_amount) as change_amount,SUM(s.frw_bb_amount) as frw_bb_amount,SUM(s.replace_amount) as replace_amount,SUM(s.revoke_amount) as revoke_amount,SUM(s.rw_bb_amount) as rw_bb_amount,SUM(s.test_amount) as test_amount,SUM(s.total_amount) as total_amount,SUM(s.add_amount) as add_amount from settle_supplier_cert_detail s where supplier_id="
						+ configSupplier.getId()
						+ timeSQL
						+ "  GROUP BY ou,product_type ORDER BY ou,product_type");
		List list = query.list();
		List result = query.setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}

	@Transactional(readOnly = false)
	public List<Map<String, Object>> findSupplierFKJS(
			ConfigSupplier configSupplier, String staDate, String endDate) {
		String timeSQL = "";
		if (staDate != null && !staDate.equals("")) {
			timeSQL += " and settle_date <= to_date('" + staDate
					+ "','yyyy-mm-dd hh24:mi:ss')";

		}
		if (endDate != null && !endDate.equals("")) {
			timeSQL += " and settle_date >= to_date('" + endDate
					+ "','yyyy-mm-dd hh24:mi:ss')";
		}
		Query query = settleSupplierCertDetailDao
				.createSqlQuery("select product_type as product_type,SUM(s.amount_year1) as year1,SUM(s.amount_year2) as year2,SUM(s.amount_year4) as year4,SUM(s.amount_year5) as year5,SUM(s.change_amount) as change_amount,SUM(s.frw_bb_amount) as frw_bb_amount,SUM(s.replace_amount) as replace_amount,SUM(s.revoke_amount) as revoke_amount,SUM(s.rw_bb_amount) as rw_bb_amount,SUM(s.test_amount) as test_amount,SUM(s.total_amount) as total_amount,SUM(s.add_amount) as add_amount from settle_supplier_cert_detail s where supplier_id="
						+ configSupplier.getId()
						+ timeSQL
						+ "  GROUP BY supplier_id,product_type");
		List list = query.list();
		List result = query.setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}

	public List<String> findOuByType(ConfigSupplier configSupplier,
			String staDate, String endDate) {
		if (configSupplier == null || configSupplier.getId() == null) {
			return null;
		}
		String timeSQL = "";
		if (staDate != null && !staDate.equals("")) {
			timeSQL += " and settle_date <= to_date('" + staDate
					+ "','yyyy-mm-dd hh24:mi:ss')";
		}
		if (endDate != null && !endDate.equals("")) {
			timeSQL += " and settle_date >= to_date('" + endDate
					+ "','yyyy-mm-dd hh24:mi:ss')";

		}
		String[] type = { "(3,6,2)", "(1)", "(3,6,2)", "(1)", "(3,6,2)", "(1)",
				"(3,6,2)", "(1)" , "(3,6,2)", "(1)",};
		int[] year = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5 };
		List<String> ouString = new ArrayList<String>();
		for (int i = 0; i < 11; i++) {
			Query query = null;
			if (i != 10) {
				query = settleSupplierCertDetailDao
						.createSqlQuery("select ou as ou from settle_supplier_cert_detail s where s.supplier_id="
								+ configSupplier.getId()
								+ " and s.product_type in "
								+ type[i]
								+ " and s.amount_year"
								+ year[i]
								+ "!=0 GROUP BY s.ou");
			} else {
				query = settleSupplierCertDetailDao
						.createSqlQuery("select ou as ou from settle_supplier_cert_detail s where s.supplier_id="
								+ configSupplier.getId()
								+ " and s.frw_bb_amount!=0 GROUP BY s.ou");
			}

			List list = query.list();
			String ous = "";
			for (int j = 0; j < list.size(); j++) {
				Object object = list.get(j);
				ous += (String) object + "/";
			}
			ouString.add(ous);
		}
		return ouString;
	}
	//
	// private void findSupplierJS1(List<SettleSupplierCertDetail>
	// list,ConfigSupplier configSupplier,Date staDate,Date endDate){
	// configSupplier.setId(14L);
	// //查询出SettleSupplierCertDetail OU的种类个数
	// DetachedCriteria dc =
	// settleSupplierCertDetailDao.createDetachedCriteria();
	// dc.createAlias("configSupplier", "configSupplier");
	// dc.add(Restrictions.eq("configSupplier", configSupplier));
	// if (staDate!=null) {
	// dc.add(Restrictions.ge("settleDate", staDate));
	// }
	// if (endDate!=null) {
	// dc.add(Restrictions.le("settleDate", endDate));
	// }
	// dc.setProjection(Property.forName("ou").group());
	// dc.setProjection(Property.forName("productType").group());
	// List<SettleSupplierCertDetail> list =
	// settleSupplierCertDetailDao.find(dc);
	// System.out.println(JSON.toJSONString(list));
	// findSupplierJS1(list,configSupplier,staDate,endDate);
	// SettleSupplierCertDetail settleSupplierCertDetail = new
	// SettleSupplierCertDetail();
	//
	//
	// for(int i = 0 ; i<list.size();i++){
	// DetachedCriteria dc =
	// settleSupplierCertDetailDao.createDetachedCriteria();
	// Object so = list.get(i);
	// dc.createAlias("configSupplier", "configSupplier");
	// dc.add(Restrictions.eq("configSupplier", configSupplier));
	// if (staDate!=null) {
	// dc.add(Restrictions.ge("settleDate", staDate));
	// }
	// if (endDate!=null) {
	// dc.add(Restrictions.le("settleDate", endDate));
	// }
	// dc.add(Restrictions.eq("ou", (String)so));
	// dc.setProjection(Property.forName("productType").group());
	// List<SettleSupplierCertDetail> details =
	// settleSupplierCertDetailDao.find(dc);
	// System.out.println(details.size());
	// }
	//
	// }
}
