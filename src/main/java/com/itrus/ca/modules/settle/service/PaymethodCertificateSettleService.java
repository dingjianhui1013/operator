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
import com.itrus.ca.modules.settle.dao.PaymethodCertificateSettleDao;
import com.itrus.ca.modules.settle.entity.PaymethodCertificateSettle;
import com.itrus.ca.modules.settle.vo.Certificate;
import com.itrus.ca.modules.settle.vo.CertificatePayMethod;
import com.itrus.ca.modules.settle.vo.CertificatePayMethodDetails;
import com.itrus.ca.modules.settle.vo.PaymethodCertificateSettleVo;

/**
 * 支付方式证书结算Service
 * 
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

	public Page<PaymethodCertificateSettle> find(Page<PaymethodCertificateSettle> page,
			PaymethodCertificateSettle paymethodCertificateSettle) {
		DetachedCriteria dc = paymethodCertificateSettleDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(paymethodCertificateSettle.getName())) {
			dc.add(Restrictions.like("name", "%" + paymethodCertificateSettle.getName() + "%"));
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

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, (NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) as dealInfoType ,p.product_name productName,t.year year,(NVL(pay.method_alipay,0)*1000+ NVL(pay.method_bank,0)*100  + NVL(pay.method_money,0 )*10 +NVL(pay.method_pos,0 )) as paymethod ,count(t.id) workCount "
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
				+ " group by  to_char(t.create_date,'YYYY-MM'),(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year ,(NVL(pay.method_alipay,0)*1000+ NVL(pay.method_bank,0)*100 + NVL(pay.method_money,0 )*10 +NVL(pay.method_pos,0 ))"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year";
		List<PaymethodCertificateSettleVo> resultList = new ArrayList<PaymethodCertificateSettleVo>();

		resultList = paymethodCertificateSettleDao.findBySql(sql, PaymethodCertificateSettleVo.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	public HashMap<String, Object> getStaticMap(List<PaymethodCertificateSettleVo> findMulitWorkList1) {
		HashMap<String, Object> monthMap = new HashMap<String, Object>();
		Certificate total = new Certificate();
		Certificate totalCoumn = new Certificate();
		for (int i = 0; i < findMulitWorkList1.size(); i++) {
			PaymethodCertificateSettleVo cssv = findMulitWorkList1.get(i);
			Object o = monthMap.get(cssv.getMonth());
			CertificatePayMethod scm;
			if (o == null) {
				scm = new CertificatePayMethod();
			} else {
				scm = (CertificatePayMethod) o;
			}
			if (cssv.getDealInfoType() == 0)// 新增
			{
				handleAddNewStatic(scm, cssv, total, totalCoumn);
			} else if (cssv.getDealInfoType() == 100) { // 更新
				handleRefreshStatic(scm, cssv, total, totalCoumn);
			} else if (cssv.getDealInfoType() == 20) {// 遗失补办
				handleLostStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 30) { // 损坏更换
				handleDamageStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 4) { // 信息变更
				handleModifyStatic(scm, cssv, total, totalCoumn);
			} else if (cssv.getDealInfoType() == 24) { // 遗失补办+信息变更
				handleChangeLostStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 34) { // 损坏更换+更新
				handleChangeReplaceStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 104) {// 更新+变更
				handleUpdateChangeStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 120) {// 更新+遗失补办
				handleUpdateLostStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 130) {// 更新+损坏更换
				handleUpdateReplaceStatic(scm, cssv, total, totalCoumn);

			} else if (cssv.getDealInfoType() == 124) {// 更新+遗失补办+变更
				handleChangeUpdateLostStatic(scm, cssv, total, totalCoumn);
				
			} else if (cssv.getDealInfoType() == 134) {// 更新+损坏更换+变更
				handleChangeUpdateReplaceStatic(scm, cssv, total, totalCoumn);			
			}

			monthMap.put(cssv.getMonth(), scm);
		}
		monthMap.put("total", total);
		return monthMap;
	}

	public CertificatePayMethodDetails handleCertificatePayMethodDetails(CertificatePayMethodDetails cpd,
			PaymethodCertificateSettleVo pcsv) {
		if(cpd ==null){
			cpd = new CertificatePayMethodDetails();
		}
		switch (pcsv.getPAYMETHOD()) {
		case 1: // pos
			cpd.setMethodPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodPosCount());
			cpd.addMethod("1");
			break;
		case 10: // money
			cpd.setMethodMoneyCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodMoneyCount());
			cpd.addMethod("10");
			break;
		case 11: // money and pos
			cpd.setMethodMoneyAndPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodMoneyAndPosCount());
			cpd.addMethod("11");
			break;
		case 100: //
			cpd.setMethodBankCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodBankCount());
			cpd.addMethod("100");
			break;
		case 101: //
			cpd.setMethodBankAndPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodBankAndPosCount());
			cpd.addMethod("101");
			break;
		case 110: //
			cpd.setMethodBankAndMoneyCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodBankAndMoneyCount());
			cpd.addMethod("110");
			break;
		case 1000: //
			cpd.setMethodAlipayCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayCount());
			cpd.addMethod("1000");
			break;
		case 1001: //
			cpd.setMethodAlipayAndPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayAndPosCount());
			cpd.addMethod("1001");
			break;
		case 1010: //
			cpd.setMethodAlipayAndMoneyCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayAndMoneyCount());
			cpd.addMethod("1010");
			break;
		case 1100: //
			cpd.setMethodAlipayAndBankCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayAndBankCount());
			cpd.addMethod("1100");
			break;
		}
		return cpd;
	}

	private void handleAddNewStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzqyadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd1(cpd);
				total.setXzqyadd1(total.getXzqyadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzqyadd1(totalCoumn.getXzqyadd1() > cpd.getMethods().size() ? totalCoumn.getXzqyadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzqyadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd2(cpd);
				total.setXzqyadd2(total.getXzqyadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzqyadd2(totalCoumn.getXzqyadd2() > cpd.getMethods().size() ? totalCoumn.getXzqyadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzqyadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd3(cpd);
				total.setXzqyadd3(total.getXzqyadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzqyadd3(totalCoumn.getXzqyadd3() > cpd.getMethods().size() ? totalCoumn.getXzqyadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzqyadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd4(cpd);
				total.setXzqyadd4(total.getXzqyadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzqyadd4(totalCoumn.getXzqyadd4() > cpd.getMethods().size() ? totalCoumn.getXzqyadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzqyadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd5(cpd);
				total.setXzqyadd5(total.getXzqyadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzqyadd5(totalCoumn.getXzqyadd5() > cpd.getMethods().size() ? totalCoumn.getXzqyadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrQadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd1(cpd);
				total.setXzgrQadd1(total.getXzgrQadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd1(totalCoumn.getXzgrQadd1() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzgrQadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd2(cpd);
				total.setXzgrQadd2(total.getXzgrQadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd2(totalCoumn.getXzgrQadd2() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzgrQadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd3(cpd);
				total.setXzgrQadd3(total.getXzgrQadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd3(totalCoumn.getXzgrQadd3() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzgrQadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd4(cpd);
				total.setXzgrQadd4(total.getXzgrQadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd4(totalCoumn.getXzgrQadd4() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzgrQadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd5(cpd);
				total.setXzgrQadd5(total.getXzgrQadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd5(totalCoumn.getXzgrQadd5() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrGadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd1(cpd);
				total.setXzgrGadd1(total.getXzgrGadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd1(totalCoumn.getXzgrGadd1() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzgrGadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd2(cpd);
				total.setXzgrGadd2(total.getXzgrGadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd2(totalCoumn.getXzgrGadd2() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzgrGadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd3(cpd);
				total.setXzgrGadd3(total.getXzgrGadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd3(totalCoumn.getXzgrGadd3() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzgrGadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd4(cpd);
				total.setXzgrGadd4(total.getXzgrGadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd4(totalCoumn.getXzgrGadd4() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzgrGadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd5(cpd);
				total.setXzgrGadd5(total.getXzgrGadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd5(totalCoumn.getXzgrGadd5() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzjgadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd1(cpd);
				total.setXzjgadd1(total.getXzjgadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd1(totalCoumn.getXzjgadd1() > cpd.getMethods().size() ? totalCoumn.getXzjgadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzjgadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd2(cpd);
				total.setXzjgadd2(total.getXzjgadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd2(totalCoumn.getXzjgadd2() > cpd.getMethods().size() ? totalCoumn.getXzjgadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzjgadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd3(cpd);
				total.setXzjgadd3(total.getXzjgadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd3(totalCoumn.getXzjgadd3() > cpd.getMethods().size() ? totalCoumn.getXzjgadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzjgadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd4(cpd);
				total.setXzjgadd4(total.getXzjgadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd4(totalCoumn.getXzjgadd4() > cpd.getMethods().size() ? totalCoumn.getXzjgadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzjgadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd5(cpd);
				total.setXzjgadd5(total.getXzjgadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd5(totalCoumn.getXzjgadd5() > cpd.getMethods().size() ? totalCoumn.getXzjgadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}

	private void handleRefreshStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getGxqyadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd1(cpd);
				total.setGxqyadd1(total.getGxqyadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setGxqyadd1(totalCoumn.getGxqyadd1() > cpd.getMethods().size() ? totalCoumn.getGxqyadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getGxqyadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd2(cpd);
				total.setGxqyadd2(total.getGxqyadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setGxqyadd2(totalCoumn.getGxqyadd2() > cpd.getMethods().size() ? totalCoumn.getGxqyadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getGxqyadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd3(cpd);
				total.setGxqyadd3(total.getGxqyadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setGxqyadd3(totalCoumn.getGxqyadd3() > cpd.getMethods().size() ? totalCoumn.getGxqyadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getGxqyadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd4(cpd);
				total.setGxqyadd4(total.getGxqyadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setGxqyadd4(totalCoumn.getGxqyadd4() > cpd.getMethods().size() ? totalCoumn.getGxqyadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getGxqyadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd5(cpd);
				total.setGxqyadd5(total.getGxqyadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setGxqyadd5(totalCoumn.getGxqyadd5() > cpd.getMethods().size() ? totalCoumn.getGxqyadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrQadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd1(cpd);
				total.setXzgrQadd1(total.getXzgrQadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd1(totalCoumn.getXzgrQadd1() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzgrQadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd2(cpd);
				total.setXzgrQadd2(total.getXzgrQadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd2(totalCoumn.getXzgrQadd2() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzgrQadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd3(cpd);
				total.setXzgrQadd3(total.getXzgrQadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd3(totalCoumn.getXzgrQadd3() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzgrQadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd4(cpd);
				total.setXzgrQadd4(total.getXzgrQadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd4(totalCoumn.getXzgrQadd4() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzgrQadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd5(cpd);
				total.setXzgrQadd5(total.getXzgrQadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrQadd5(totalCoumn.getXzgrQadd5() > cpd.getMethods().size() ? totalCoumn.getXzgrQadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrGadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd1(cpd);
				total.setXzgrGadd1(total.getXzgrGadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd1(totalCoumn.getXzgrGadd1() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzgrGadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd2(cpd);
				total.setXzgrGadd2(total.getXzgrGadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd2(totalCoumn.getXzgrGadd2() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzgrGadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd3(cpd);
				total.setXzgrGadd3(total.getXzgrGadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd3(totalCoumn.getXzgrGadd3() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzgrGadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd4(cpd);
				total.setXzgrGadd4(total.getXzgrGadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd4(totalCoumn.getXzgrGadd4() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzgrGadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd5(cpd);
				total.setXzgrGadd5(total.getXzgrGadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzgrGadd5(totalCoumn.getXzgrGadd5() > cpd.getMethods().size() ? totalCoumn.getXzgrGadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzjgadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd1(cpd);
				total.setXzjgadd1(total.getXzjgadd1() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd1(totalCoumn.getXzjgadd1() > cpd.getMethods().size() ? totalCoumn.getXzjgadd1()
						: cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getXzjgadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd2(cpd);
				total.setXzjgadd2(total.getXzjgadd2() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd2(totalCoumn.getXzjgadd2() > cpd.getMethods().size() ? totalCoumn.getXzjgadd2()
						: cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getXzjgadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd3(cpd);
				total.setXzjgadd3(total.getXzjgadd3() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd3(totalCoumn.getXzjgadd3() > cpd.getMethods().size() ? totalCoumn.getXzjgadd3()
						: cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getXzjgadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd4(cpd);
				total.setXzjgadd4(total.getXzjgadd4() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd4(totalCoumn.getXzjgadd4() > cpd.getMethods().size() ? totalCoumn.getXzjgadd4()
						: cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getXzjgadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd5(cpd);
				total.setXzjgadd5(total.getXzjgadd5() + cssv.getWorkCount().intValue());
				totalCoumn.setXzjgadd5(totalCoumn.getXzjgadd5() > cpd.getMethods().size() ? totalCoumn.getXzjgadd5()
						: cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}

	private void handleLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getLostCerateqy();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCerateqy(cpd);
			total.setLostCerateqy(total.getLostCerateqy() + cssv.getWorkCount().intValue());
			totalCoumn.setLostCerateqy(totalCoumn.getLostCerateqy() > cpd.getMethods().size()
					? totalCoumn.getLostCerateqy() : cpd.getMethods().size());// 最大列数
			break;
		case "2": // 个人证书（企业）
			cpd = scm.getLostCerategrQ();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCerategrQ(cpd);
			total.setLostCerategrQ(total.getLostCerategrQ() + cssv.getWorkCount().intValue());
			totalCoumn.setLostCerategrQ(totalCoumn.getLostCerategrQ() > cpd.getMethods().size()
					? totalCoumn.getLostCerategrQ() : cpd.getMethods().size());// 最大列数
			break;

		case "6":// 个人证书（机构）
			cpd = scm.getLostCerategrG();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCerategrG(cpd);
			total.setLostCerategrG(total.getLostCerategrG() + cssv.getWorkCount().intValue());
			totalCoumn.setLostCerategrG(totalCoumn.getLostCerategrG() > cpd.getMethods().size()
					? totalCoumn.getLostCerategrG() : cpd.getMethods().size());// 最大列数
			break;

		case "3":// 机构证书
			cpd = scm.getLostCeratejg();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCeratejg(cpd);
			total.setLostCeratejg(total.getLostCeratejg() + cssv.getWorkCount().intValue());
			totalCoumn.setLostCeratejg(totalCoumn.getLostCeratejg() > cpd.getMethods().size()
					? totalCoumn.getLostCeratejg() : cpd.getMethods().size());// 最大列数
			break;

		}
	}

	private void handleDamageStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getDamageCertificateqy();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificateqy(cpd);
			total.setDamageCertificateqy(total.getDamageCertificateqy() + cssv.getWorkCount().intValue());
			totalCoumn.setDamageCertificateqy(totalCoumn.getDamageCertificateqy() > cpd.getMethods().size()
					? totalCoumn.getDamageCertificateqy() : cpd.getMethods().size());// 最大列数
			break;
		case "2": // 个人证书（企业）
			cpd = scm.getDamageCertificategrQ();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificategrQ(cpd);
			total.setDamageCertificategrQ(total.getDamageCertificategrQ() + cssv.getWorkCount().intValue());
			totalCoumn.setDamageCertificategrQ(totalCoumn.getDamageCertificategrQ() > cpd.getMethods().size()
					? totalCoumn.getDamageCertificategrQ() : cpd.getMethods().size());// 最大列数
			break;

		case "6":// 个人证书（机构）
			cpd = scm.getDamageCertificategrG();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificategrG(cpd);
			total.setDamageCertificategrG(total.getDamageCertificategrG() + cssv.getWorkCount().intValue());
			totalCoumn.setDamageCertificategrG(totalCoumn.getDamageCertificategrG() > cpd.getMethods().size()
					? totalCoumn.getDamageCertificategrG() : cpd.getMethods().size());// 最大列数
			break;

		case "3":// 机构证书
			cpd = scm.getDamageCertificatejg();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificatejg(cpd);
			total.setDamageCertificatejg(total.getDamageCertificatejg() + cssv.getWorkCount().intValue());
			totalCoumn.setDamageCertificatejg(totalCoumn.getDamageCertificatejg() > cpd.getMethods().size()
					? totalCoumn.getDamageCertificatejg() : cpd.getMethods().size());// 最大列数
			break;

		}
	}

	private void handleModifyStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getModifyNumqy();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumqy(cpd);
			total.setModifyNumqy(total.getModifyNumqy() + cssv.getWorkCount().intValue());
			totalCoumn.setModifyNumqy(totalCoumn.getModifyNumqy() > cpd.getMethods().size()
					? totalCoumn.getModifyNumqy() : cpd.getMethods().size());// 最大列数
			break;
		case "2": // 个人证书（企业）
			cpd = scm.getModifyNumgrQ();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumgrQ(cpd);
			total.setModifyNumgrQ(total.getModifyNumgrQ() + cssv.getWorkCount().intValue());
			totalCoumn.setModifyNumgrQ(totalCoumn.getModifyNumgrQ() > cpd.getMethods().size()
					? totalCoumn.getModifyNumgrQ() : cpd.getMethods().size());// 最大列数
			break;

		case "6":// 个人证书（机构）
			cpd = scm.getModifyNumgrG();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumgrG(cpd);
			total.setModifyNumgrG(total.getModifyNumgrG() + cssv.getWorkCount().intValue());
			totalCoumn.setModifyNumgrG(totalCoumn.getModifyNumgrG() > cpd.getMethods().size()
					? totalCoumn.getModifyNumgrG() : cpd.getMethods().size());// 最大列数
			break;

		case "3":// 机构证书
			cpd = scm.getModifyNumjg();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumjg(cpd);
			total.setModifyNumjg(total.getModifyNumjg() + cssv.getWorkCount().intValue());
			totalCoumn.setModifyNumjg(totalCoumn.getModifyNumjg() > cpd.getMethods().size()
					? totalCoumn.getModifyNumjg() : cpd.getMethods().size());// 最大列数
			break;

		}
	}

	private void handleChangeLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getChangeLostqyNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostqyNum(cpd);
			total.setChangeLostqyNum(total.getChangeLostqyNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeLostqyNum(totalCoumn.getChangeLostqyNum() > cpd.getMethods().size()
					? totalCoumn.getChangeLostqyNum() : cpd.getMethods().size());// 最大列数
			break;
		case "2": // 个人证书（企业）
			cpd = scm.getChangeLostgrQNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostgrQNum(cpd);
			total.setChangeLostgrQNum(total.getChangeLostgrQNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeLostgrQNum(totalCoumn.getChangeLostgrQNum() > cpd.getMethods().size()
					? totalCoumn.getChangeLostgrQNum() : cpd.getMethods().size());// 最大列数
			break;

		case "6":// 个人证书（机构）
			cpd = scm.getChangeLostgrGNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostgrGNum(cpd);
			total.setChangeLostgrGNum(total.getChangeLostgrGNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeLostgrGNum(totalCoumn.getChangeLostgrGNum() > cpd.getMethods().size()
					? totalCoumn.getChangeLostgrGNum() : cpd.getMethods().size());// 最大列数
			break;

		case "3":// 机构证书
			cpd = scm.getChangeLostjgNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostjgNum(cpd);
			total.setChangeLostjgNum(total.getChangeLostjgNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeLostjgNum(totalCoumn.getChangeLostjgNum() > cpd.getMethods().size()
					? totalCoumn.getChangeLostjgNum() : cpd.getMethods().size());// 最大列数
			break;

		}
	}

	private void handleChangeReplaceStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			Certificate total, Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getChangeReplaceqyNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplaceqyNum(cpd);
			total.setChangeReplaceqyNum(total.getChangeReplaceqyNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeReplaceqyNum(totalCoumn.getChangeReplaceqyNum() > cpd.getMethods().size()
					? totalCoumn.getChangeReplaceqyNum() : cpd.getMethods().size());// 最大列数
			break;
		case "2": // 个人证书（企业）
			cpd = scm.getChangeReplacegrQNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplacegrQNum(cpd);
			total.setChangeReplacegrQNum(total.getChangeReplacegrQNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeReplacegrQNum(totalCoumn.getChangeReplacegrQNum() > cpd.getMethods().size()
					? totalCoumn.getChangeReplacegrQNum() : cpd.getMethods().size());// 最大列数
			break;

		case "6":// 个人证书（机构）
			cpd = scm.getChangeReplacegrGNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplacegrGNum(cpd);
			total.setChangeReplacegrGNum(total.getChangeReplacegrGNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeReplacegrGNum(totalCoumn.getChangeReplacegrGNum() > cpd.getMethods().size()
					? totalCoumn.getChangeReplacegrGNum() : cpd.getMethods().size());// 最大列数
			break;

		case "3":// 机构证书
			cpd = scm.getChangeReplacejgNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplacejgNum(cpd);
			total.setChangeReplacejgNum(total.getChangeReplacejgNum() + cssv.getWorkCount().intValue());
			totalCoumn.setChangeReplacejgNum(totalCoumn.getChangeReplacejgNum() > cpd.getMethods().size()
					? totalCoumn.getChangeReplacejgNum() : cpd.getMethods().size());// 最大列数
			break;

		}
	}

	private void handleUpdateChangeStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			Certificate total, Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangeqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum(cpd);
				total.setUpdateChangeqyNum(total.getUpdateChangeqyNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangeqyNum(totalCoumn.getUpdateChangeqyNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangeqyNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateChangeqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum2(cpd);
				total.setUpdateChangeqyNum2(total.getUpdateChangeqyNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangeqyNum2(totalCoumn.getUpdateChangeqyNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangeqyNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateChangeqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum3(cpd);
				total.setUpdateChangeqyNum3(total.getUpdateChangeqyNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangeqyNum3(totalCoumn.getUpdateChangeqyNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangeqyNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateChangeqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum4(cpd);
				total.setUpdateChangeqyNum4(total.getUpdateChangeqyNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangeqyNum4(totalCoumn.getUpdateChangeqyNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangeqyNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateChangeqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum5(cpd);
				total.setUpdateChangeqyNum5(total.getUpdateChangeqyNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangeqyNum5(totalCoumn.getUpdateChangeqyNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangeqyNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangegrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum(cpd);
				total.setUpdateChangegrQNum(total.getUpdateChangegrQNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrQNum(totalCoumn.getUpdateChangegrQNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrQNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateChangegrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum2(cpd);
				total.setUpdateChangegrQNum2(total.getUpdateChangegrQNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrQNum2(totalCoumn.getUpdateChangegrQNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrQNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateChangegrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum3(cpd);
				total.setUpdateChangegrQNum3(total.getUpdateChangegrQNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrQNum3(totalCoumn.getUpdateChangegrQNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrQNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateChangegrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum4(cpd);
				total.setUpdateChangegrQNum4(total.getUpdateChangegrQNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrQNum4(totalCoumn.getUpdateChangegrQNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrQNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateChangegrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum5(cpd);
				total.setUpdateChangegrQNum5(total.getUpdateChangegrQNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrQNum5(totalCoumn.getUpdateChangegrQNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrQNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangegrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum(cpd);
				total.setUpdateChangegrGNum(total.getUpdateChangegrGNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrGNum(totalCoumn.getUpdateChangegrGNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrGNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateChangegrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum2(cpd);
				total.setUpdateChangegrGNum2(total.getUpdateChangegrGNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrGNum2(totalCoumn.getUpdateChangegrGNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrGNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateChangegrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum3(cpd);
				total.setUpdateChangegrGNum3(total.getUpdateChangegrGNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrGNum3(totalCoumn.getUpdateChangegrGNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrGNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateChangegrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum4(cpd);
				total.setUpdateChangegrGNum4(total.getUpdateChangegrGNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrGNum4(totalCoumn.getUpdateChangegrGNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrGNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateChangegrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum5(cpd);
				total.setUpdateChangegrGNum5(total.getUpdateChangegrGNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangegrGNum5(totalCoumn.getUpdateChangegrGNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangegrGNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangejgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum(cpd);
				total.setUpdateChangejgNum(total.getUpdateChangejgNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangejgNum(totalCoumn.getUpdateChangejgNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangejgNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateChangejgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum2(cpd);
				total.setUpdateChangejgNum2(total.getUpdateChangejgNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangejgNum2(totalCoumn.getUpdateChangejgNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangejgNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateChangejgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum3(cpd);
				total.setUpdateChangejgNum3(total.getUpdateChangejgNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangejgNum3(totalCoumn.getUpdateChangejgNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangejgNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateChangejgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum4(cpd);
				total.setUpdateChangejgNum4(total.getUpdateChangejgNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangejgNum4(totalCoumn.getUpdateChangejgNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangejgNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateChangejgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum5(cpd);
				total.setUpdateChangejgNum5(total.getUpdateChangejgNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateChangejgNum5(totalCoumn.getUpdateChangejgNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateChangejgNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}

	private void handleUpdateLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv, Certificate total,
			Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum(cpd);
				total.setUpdateLostqyNum(total.getUpdateLostqyNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostqyNum(totalCoumn.getUpdateLostqyNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostqyNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateLostqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum2(cpd);
				total.setUpdateLostqyNum2(total.getUpdateLostqyNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostqyNum2(totalCoumn.getUpdateLostqyNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostqyNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateLostqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum3(cpd);
				total.setUpdateLostqyNum3(total.getUpdateLostqyNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostqyNum3(totalCoumn.getUpdateLostqyNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostqyNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateLostqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum4(cpd);
				total.setUpdateLostqyNum4(total.getUpdateLostqyNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostqyNum4(totalCoumn.getUpdateLostqyNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostqyNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateLostqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum5(cpd);
				total.setUpdateLostqyNum5(total.getUpdateLostqyNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostqyNum5(totalCoumn.getUpdateLostqyNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostqyNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostgrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum(cpd);
				total.setUpdateLostgrQNum(total.getUpdateLostgrQNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrQNum(totalCoumn.getUpdateLostgrQNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrQNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateLostgrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum2(cpd);
				total.setUpdateLostgrQNum2(total.getUpdateLostgrQNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrQNum2(totalCoumn.getUpdateLostgrQNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrQNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateLostgrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum3(cpd);
				total.setUpdateLostgrQNum3(total.getUpdateLostgrQNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrQNum3(totalCoumn.getUpdateLostgrQNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrQNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateLostgrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum4(cpd);
				total.setUpdateLostgrQNum4(total.getUpdateLostgrQNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrQNum4(totalCoumn.getUpdateLostgrQNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrQNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateLostgrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum5(cpd);
				total.setUpdateLostgrQNum5(total.getUpdateLostgrQNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrQNum5(totalCoumn.getUpdateLostgrQNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrQNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostgrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum(cpd);
				total.setUpdateLostgrGNum(total.getUpdateLostgrGNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrGNum(totalCoumn.getUpdateLostgrGNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrGNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateLostgrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum2(cpd);
				total.setUpdateLostgrGNum2(total.getUpdateLostgrGNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrGNum2(totalCoumn.getUpdateLostgrGNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrGNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateLostgrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum3(cpd);
				total.setUpdateLostgrGNum3(total.getUpdateLostgrGNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrGNum3(totalCoumn.getUpdateLostgrGNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrGNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateLostgrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum4(cpd);
				total.setUpdateLostgrGNum4(total.getUpdateLostgrGNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrGNum4(totalCoumn.getUpdateLostgrGNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrGNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateLostgrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum5(cpd);
				total.setUpdateLostgrGNum5(total.getUpdateLostgrGNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostgrGNum5(totalCoumn.getUpdateLostgrGNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostgrGNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostjgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum(cpd);
				total.setUpdateLostjgNum(total.getUpdateLostjgNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostjgNum(totalCoumn.getUpdateLostjgNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostjgNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateLostjgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum2(cpd);
				total.setUpdateLostjgNum2(total.getUpdateLostjgNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostjgNum2(totalCoumn.getUpdateLostjgNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostjgNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateLostjgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum3(cpd);
				total.setUpdateLostjgNum3(total.getUpdateLostjgNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostjgNum3(totalCoumn.getUpdateLostjgNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostjgNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateLostjgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum4(cpd);
				total.setUpdateLostjgNum4(total.getUpdateLostjgNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostjgNum4(totalCoumn.getUpdateLostjgNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostjgNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateLostjgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum5(cpd);
				total.setUpdateLostjgNum5(total.getUpdateLostjgNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateLostjgNum5(totalCoumn.getUpdateLostjgNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateLostjgNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}

	private void handleUpdateReplaceStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			Certificate total, Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplaceqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum(cpd);
				total.setUpdateReplaceqyNum(total.getUpdateReplaceqyNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplaceqyNum(totalCoumn.getUpdateReplaceqyNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplaceqyNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateReplaceqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum2(cpd);
				total.setUpdateReplaceqyNum2(total.getUpdateReplaceqyNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplaceqyNum2(totalCoumn.getUpdateReplaceqyNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplaceqyNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateReplaceqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum3(cpd);
				total.setUpdateReplaceqyNum3(total.getUpdateReplaceqyNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplaceqyNum3(totalCoumn.getUpdateReplaceqyNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplaceqyNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateReplaceqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum4(cpd);
				total.setUpdateReplaceqyNum4(total.getUpdateReplaceqyNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplaceqyNum4(totalCoumn.getUpdateReplaceqyNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplaceqyNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateReplaceqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum5(cpd);
				total.setUpdateReplaceqyNum5(total.getUpdateReplaceqyNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplaceqyNum5(totalCoumn.getUpdateReplaceqyNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplaceqyNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplacegrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum(cpd);
				total.setUpdateReplacegrQNum(total.getUpdateReplacegrQNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrQNum(totalCoumn.getUpdateReplacegrQNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrQNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateReplacegrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum2(cpd);
				total.setUpdateReplacegrQNum2(total.getUpdateReplacegrQNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrQNum2(totalCoumn.getUpdateReplacegrQNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrQNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateReplacegrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum3(cpd);
				total.setUpdateReplacegrQNum3(total.getUpdateReplacegrQNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrQNum3(totalCoumn.getUpdateReplacegrQNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrQNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateReplacegrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum4(cpd);
				total.setUpdateReplacegrQNum4(total.getUpdateReplacegrQNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrQNum4(totalCoumn.getUpdateReplacegrQNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrQNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateReplacegrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum5(cpd);
				total.setUpdateReplacegrQNum5(total.getUpdateReplacegrQNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrQNum5(totalCoumn.getUpdateReplacegrQNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrQNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplacegrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum(cpd);
				total.setUpdateReplacegrGNum(total.getUpdateReplacegrGNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrGNum(totalCoumn.getUpdateReplacegrGNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrGNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateReplacegrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum2(cpd);
				total.setUpdateReplacegrGNum2(total.getUpdateReplacegrGNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrGNum2(totalCoumn.getUpdateReplacegrGNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrGNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateReplacegrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum3(cpd);
				total.setUpdateReplacegrGNum3(total.getUpdateReplacegrGNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrGNum3(totalCoumn.getUpdateReplacegrGNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrGNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateReplacegrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum4(cpd);
				total.setUpdateReplacegrGNum4(total.getUpdateReplacegrGNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrGNum4(totalCoumn.getUpdateReplacegrGNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrGNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateReplacegrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum5(cpd);
				total.setUpdateReplacegrGNum5(total.getUpdateReplacegrGNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacegrGNum5(totalCoumn.getUpdateReplacegrGNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacegrGNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplacejgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum(cpd);
				total.setUpdateReplacejgNum(total.getUpdateReplacejgNum() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacejgNum(totalCoumn.getUpdateReplacejgNum() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacejgNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getUpdateReplacejgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum2(cpd);
				total.setUpdateReplacejgNum2(total.getUpdateReplacejgNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacejgNum2(totalCoumn.getUpdateReplacejgNum2() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacejgNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getUpdateReplacejgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum3(cpd);
				total.setUpdateReplacejgNum3(total.getUpdateReplacejgNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacejgNum3(totalCoumn.getUpdateReplacejgNum3() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacejgNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getUpdateReplacejgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum4(cpd);
				total.setUpdateReplacejgNum4(total.getUpdateReplacejgNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacejgNum4(totalCoumn.getUpdateReplacejgNum4() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacejgNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getUpdateReplacejgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum5(cpd);
				total.setUpdateReplacejgNum5(total.getUpdateReplacejgNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setUpdateReplacejgNum5(totalCoumn.getUpdateReplacejgNum5() > cpd.getMethods().size()
						? totalCoumn.getUpdateReplacejgNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}

	private void handleChangeUpdateLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			Certificate total, Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum(cpd);
				total.setChangeUpdateLostqyNum(total.getChangeUpdateLostqyNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostqyNum(totalCoumn.getChangeUpdateLostqyNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostqyNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateLostqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum2(cpd);
				total.setChangeUpdateLostqyNum2(total.getChangeUpdateLostqyNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostqyNum2(totalCoumn.getChangeUpdateLostqyNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostqyNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateLostqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum3(cpd);
				total.setChangeUpdateLostqyNum3(total.getChangeUpdateLostqyNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostqyNum3(totalCoumn.getChangeUpdateLostqyNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostqyNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateLostqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum4(cpd);
				total.setChangeUpdateLostqyNum4(total.getChangeUpdateLostqyNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostqyNum4(totalCoumn.getChangeUpdateLostqyNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostqyNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateLostqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum5(cpd);
				total.setChangeUpdateLostqyNum5(total.getChangeUpdateLostqyNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostqyNum5(totalCoumn.getChangeUpdateLostqyNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostqyNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostgrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum(cpd);
				total.setChangeUpdateLostgrQNum(total.getChangeUpdateLostgrQNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrQNum(totalCoumn.getChangeUpdateLostgrQNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrQNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateLostgrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum2(cpd);
				total.setChangeUpdateLostgrQNum2(total.getChangeUpdateLostgrQNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrQNum2(totalCoumn.getChangeUpdateLostgrQNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrQNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateLostgrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum3(cpd);
				total.setChangeUpdateLostgrQNum3(total.getChangeUpdateLostgrQNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrQNum3(totalCoumn.getChangeUpdateLostgrQNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrQNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateLostgrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum4(cpd);
				total.setChangeUpdateLostgrQNum4(total.getChangeUpdateLostgrQNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrQNum4(totalCoumn.getChangeUpdateLostgrQNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrQNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateLostgrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum5(cpd);
				total.setChangeUpdateLostgrQNum5(total.getChangeUpdateLostgrQNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrQNum5(totalCoumn.getChangeUpdateLostgrQNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrQNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostgrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum(cpd);
				total.setChangeUpdateLostgrGNum(total.getChangeUpdateLostgrGNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrGNum(totalCoumn.getChangeUpdateLostgrGNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrGNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateLostgrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum2(cpd);
				total.setChangeUpdateLostgrGNum2(total.getChangeUpdateLostgrGNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrGNum2(totalCoumn.getChangeUpdateLostgrGNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrGNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateLostgrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum3(cpd);
				total.setChangeUpdateLostgrGNum3(total.getChangeUpdateLostgrGNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrGNum3(totalCoumn.getChangeUpdateLostgrGNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrGNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateLostgrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum4(cpd);
				total.setChangeUpdateLostgrGNum4(total.getChangeUpdateLostgrGNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrGNum4(totalCoumn.getChangeUpdateLostgrGNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrGNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateLostgrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum5(cpd);
				total.setChangeUpdateLostgrGNum5(total.getChangeUpdateLostgrGNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostgrGNum5(totalCoumn.getChangeUpdateLostgrGNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostgrGNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostjgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum(cpd);
				total.setChangeUpdateLostjgNum(total.getChangeUpdateLostjgNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostjgNum(totalCoumn.getChangeUpdateLostjgNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostjgNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateLostjgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum2(cpd);
				total.setChangeUpdateLostjgNum2(total.getChangeUpdateLostjgNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostjgNum2(totalCoumn.getChangeUpdateLostjgNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostjgNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateLostjgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum3(cpd);
				total.setChangeUpdateLostjgNum3(total.getChangeUpdateLostjgNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostjgNum3(totalCoumn.getChangeUpdateLostjgNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostjgNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateLostjgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum4(cpd);
				total.setChangeUpdateLostjgNum4(total.getChangeUpdateLostjgNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostjgNum4(totalCoumn.getChangeUpdateLostjgNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostjgNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateLostjgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum5(cpd);
				total.setChangeUpdateLostjgNum5(total.getChangeUpdateLostjgNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateLostjgNum5(totalCoumn.getChangeUpdateLostjgNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateLostjgNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}
	
	private void handleChangeUpdateReplaceStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			Certificate total, Certificate totalCoumn) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplaceqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum(cpd);
				total.setChangeUpdateReplaceqyNum(total.getChangeUpdateReplaceqyNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplaceqyNum(totalCoumn.getChangeUpdateReplaceqyNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplaceqyNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateReplaceqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum2(cpd);
				total.setChangeUpdateReplaceqyNum2(total.getChangeUpdateReplaceqyNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplaceqyNum2(totalCoumn.getChangeUpdateReplaceqyNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplaceqyNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateReplaceqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum3(cpd);
				total.setChangeUpdateReplaceqyNum3(total.getChangeUpdateReplaceqyNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplaceqyNum3(totalCoumn.getChangeUpdateReplaceqyNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplaceqyNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateReplaceqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum4(cpd);
				total.setChangeUpdateReplaceqyNum4(total.getChangeUpdateReplaceqyNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplaceqyNum4(totalCoumn.getChangeUpdateReplaceqyNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplaceqyNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateReplaceqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum5(cpd);
				total.setChangeUpdateReplaceqyNum5(total.getChangeUpdateReplaceqyNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplaceqyNum5(totalCoumn.getChangeUpdateReplaceqyNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplaceqyNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplacegrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum(cpd);
				total.setChangeUpdateReplacegrQNum(total.getChangeUpdateReplacegrQNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrQNum(totalCoumn.getChangeUpdateReplacegrQNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrQNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateReplacegrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum2(cpd);
				total.setChangeUpdateReplacegrQNum2(total.getChangeUpdateReplacegrQNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrQNum2(totalCoumn.getChangeUpdateReplacegrQNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrQNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateReplacegrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum3(cpd);
				total.setChangeUpdateReplacegrQNum3(total.getChangeUpdateReplacegrQNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrQNum3(totalCoumn.getChangeUpdateReplacegrQNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrQNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateReplacegrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum4(cpd);
				total.setChangeUpdateReplacegrQNum4(total.getChangeUpdateReplacegrQNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrQNum4(totalCoumn.getChangeUpdateReplacegrQNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrQNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateReplacegrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum5(cpd);
				total.setChangeUpdateReplacegrQNum5(total.getChangeUpdateReplacegrQNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrQNum5(totalCoumn.getChangeUpdateReplacegrQNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrQNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplacegrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum(cpd);
				total.setChangeUpdateReplacegrGNum(total.getChangeUpdateReplacegrGNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrGNum(totalCoumn.getChangeUpdateReplacegrGNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrGNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateReplacegrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum2(cpd);
				total.setChangeUpdateReplacegrGNum2(total.getChangeUpdateReplacegrGNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrGNum2(totalCoumn.getChangeUpdateReplacegrGNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrGNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateReplacegrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum3(cpd);
				total.setChangeUpdateReplacegrGNum3(total.getChangeUpdateReplacegrGNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrGNum3(totalCoumn.getChangeUpdateReplacegrGNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrGNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateReplacegrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum4(cpd);
				total.setChangeUpdateReplacegrGNum4(total.getChangeUpdateReplacegrGNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrGNum4(totalCoumn.getChangeUpdateReplacegrGNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrGNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateReplacegrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum5(cpd);
				total.setChangeUpdateReplacegrGNum5(total.getChangeUpdateReplacegrGNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacegrGNum5(totalCoumn.getChangeUpdateReplacegrGNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacegrGNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplacejgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum(cpd);
				total.setChangeUpdateReplacejgNum(total.getChangeUpdateReplacejgNum() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacejgNum(totalCoumn.getChangeUpdateReplacejgNum() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacejgNum() : cpd.getMethods().size());// 最大列数
				break;
			case 2:
				cpd = scm.getChangeUpdateReplacejgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum2(cpd);
				total.setChangeUpdateReplacejgNum2(total.getChangeUpdateReplacejgNum2() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacejgNum2(totalCoumn.getChangeUpdateReplacejgNum2() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacejgNum2() : cpd.getMethods().size());// 最大列数
				break;
			case 3:
				cpd = scm.getChangeUpdateReplacejgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum3(cpd);
				total.setChangeUpdateReplacejgNum3(total.getChangeUpdateReplacejgNum3() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacejgNum3(totalCoumn.getChangeUpdateReplacejgNum3() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacejgNum3() : cpd.getMethods().size());// 最大列数
				break;
			case 4:
				cpd = scm.getChangeUpdateReplacejgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum4(cpd);
				total.setChangeUpdateReplacejgNum4(total.getChangeUpdateReplacejgNum4() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacejgNum4(totalCoumn.getChangeUpdateReplacejgNum4() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacejgNum4() : cpd.getMethods().size());// 最大列数
				break;
			case 5:
				cpd = scm.getChangeUpdateReplacejgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum5(cpd);
				total.setChangeUpdateReplacejgNum5(total.getChangeUpdateReplacejgNum5() + cssv.getWorkCount().intValue());
				totalCoumn.setChangeUpdateReplacejgNum5(totalCoumn.getChangeUpdateReplacejgNum5() > cpd.getMethods().size()
						? totalCoumn.getChangeUpdateReplacejgNum5() : cpd.getMethods().size());// 最大列数
				break;
			}
			break;

		}
	}
}