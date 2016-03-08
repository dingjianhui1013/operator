/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import com.itrus.ca.modules.settle.entity.PaymethodCertificateSettle;
import com.itrus.ca.modules.settle.vo.Certificate;
import com.itrus.ca.modules.settle.vo.CertificateF;
import com.itrus.ca.modules.settle.vo.CertificateSettlementStatisticsVO;
import com.itrus.ca.modules.settle.vo.PaymethodCertificateSettleVo;
import com.itrus.ca.modules.settle.dao.PaymethodCertificateSettleDao;

/**
 * 支付方式证书结算Service
 * @author qt
 * @version 2016-01-31
 */
@Component
@Transactional(readOnly = true)
public class PaymethodCertificateSettleService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(PaymethodCertificateSettleService.class);
	
	@Autowired
	private PaymethodCertificateSettleDao paymethodCertificateSettleDao;
	
	public PaymethodCertificateSettle get(Long id) {
		return paymethodCertificateSettleDao.findOne(id);
	}
	
	public Page<PaymethodCertificateSettle> find(Page<PaymethodCertificateSettle> page, PaymethodCertificateSettle paymethodCertificateSettle) {
		DetachedCriteria dc = paymethodCertificateSettleDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(paymethodCertificateSettle.getName())){
			dc.add(Restrictions.like("name", "%"+paymethodCertificateSettle.getName()+"%"));
		}
		dc.add(Restrictions.eq(PaymethodCertificateSettle.DEL_FLAG, PaymethodCertificateSettle.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return paymethodCertificateSettleDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymethodCertificateSettle paymethodCertificateSettle) {
		paymethodCertificateSettleDao.save(paymethodCertificateSettle);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		paymethodCertificateSettleDao.deleteById(id);
	}
	public List<PaymethodCertificateSettleVo> findMulitWorkList1(Long apply, String productType, String workTypes,
			String officeIdsList, String agentId, Date startDate, Date endDate) {

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, (NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) as dealInfoType ,p.product_name productName,t.year year,(NVL(pay.method_alipay,0)*100000+ NVL(pay.method_bank,0)*10000+ NVL(pay.method_contract,0 )*1000 + NVL(pay.method_gov,0 )*100 + NVL(pay.method_money,0 )*10 +NVL(pay.method_pos,0 )) as payMethod ,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,SYS_USER u ,config_agent_bound_deal_info b , WORK_PAY_INFO pay "
				+ " where t.product_id = p.id and t.app_id =?  and t.deal_info_status  in(7,9) and b.deal_info = t.id  and t.pay_id = pay.id "
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
					+ ") or t.deal_info_type2 in(" + workTypes + "))";
		} else { // 默认只查 新增和更新
			sql = sql
					+ "and t.deal_info_type in(0,1) and (t.deal_info_type1 is not null or t.deal_info_type2 is not null)";
		}
		sql = sql
				+ " group by  to_char(t.create_date,'YYYY-MM'),(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year ,(NVL(pay.method_alipay,0)*100000+ NVL(pay.method_bank,0)*10000+ NVL(pay.method_contract,0 )*1000 + NVL(pay.method_gov,0 )*100 + NVL(pay.method_money,0 )*10 +NVL(pay.method_pos,0 ))"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year";
		List<PaymethodCertificateSettleVo> resultList = new ArrayList<PaymethodCertificateSettleVo>();

		resultList = paymethodCertificateSettleDao.findBySql(sql, PaymethodCertificateSettleVo.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}
	public HashMap<String, Certificate> getStaticMap1(List<PaymethodCertificateSettleVo> findMulitWorkList1) {
		HashMap<String, Certificate> monthMap = new HashMap<String, Certificate>();
		Certificate total = new Certificate();
		for (int i = 0; i < findMulitWorkList1.size(); i++) {
			PaymethodCertificateSettleVo cssv = findMulitWorkList1.get(i);
			Certificate scm = monthMap.get(cssv.getMonth());
			if (scm == null) {
				scm = new Certificate();
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
}
