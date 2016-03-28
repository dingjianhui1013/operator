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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.logging.Log;
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

		String sql = "select to_char(t.create_date,'YYYY-MM') as month, (NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) as dealInfoType ,p.product_name productName,t.year year,(case pay.relation_method when 1 then 10 when 2 then 1 when 3 then 100 when 4 then 1000 else  NVL(pay.method_alipay,0)*1000+ NVL(pay.method_bank,0)*100  + NVL(pay.method_money,0 )*10 +NVL(pay.method_pos,0 ) end) as paymethod ,count(t.id) workCount "
				+ " from WORK_DEAL_INFO t, CONFIG_PRODUCT p,config_agent_bound_deal_info b , WORK_PAY_INFO pay "
				+ " where t.product_id = p.id and t.app_id =?  and t.deal_info_status  in(7,9) and b.deal_info = t.id  and t.pay_id = pay.id "
				+ " and pay.del_flag =0" + " and t.create_date > to_date(? ,'yyyy-MM-dd HH24:mi:ss')"
				+ " and t.create_date <= to_date(? ,'yyyy-MM-dd HH24:mi:ss')";
		if (StringUtils.isNotBlank(officeIdsList)) {
			sql = sql + " and  t.office_id in(" + officeIdsList + ")";
		}
		if (StringUtils.isNotBlank(productType)) {
			sql = sql + " and  p.product_name in(" + productType + ")";
		}
		if (StringUtils.isNotBlank(agentId)) {
			sql = sql + " and  b.agent_id in  (" + agentId + ") ";
		}
		if (StringUtils.isNotBlank(workTypes)) {
			sql = sql + " and (t.deal_info_type in(" + workTypes + ") or t.deal_info_type1 in(" + workTypes
					+ ") or t.deal_info_type2 in(" + workTypes + "))";
		} else { // 默认只查 新增和更新
			sql = sql
					+ " and t.deal_info_type in(0,1) and (t.deal_info_type1 is not null or t.deal_info_type2 is not null)";
		}
		sql = sql
				+ " group by  to_char(t.create_date,'YYYY-MM'),(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year ,(case pay.relation_method when 1 then 10 when 2 then 1 when 3 then 100 when 4 then 1000 else  NVL(pay.method_alipay,0)*1000+ NVL(pay.method_bank,0)*100  + NVL(pay.method_money,0 )*10 +NVL(pay.method_pos,0 ) end)"
				+ " order by to_char(t.create_date,'YYYY-MM') asc,(NVL(t.deal_info_type,0)*100+ NVL(t.deal_info_type1,0)*10+ NVL(t.deal_info_type2,0 )) ,p.product_name,t.year";
		List<PaymethodCertificateSettleVo> resultList = new ArrayList<PaymethodCertificateSettleVo>();

		resultList = paymethodCertificateSettleDao.findBySql(sql, PaymethodCertificateSettleVo.class, apply,
				DateFormatUtils.format(startDate, "yyyy-MM-dd") + " 00:00:00",
				DateFormatUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");

		return resultList;
	}

	public HashMap<String, CertificatePayMethod> getStaticMap(List<PaymethodCertificateSettleVo> findMulitWorkList1) {
		HashMap<String, CertificatePayMethod> monthMap = new HashMap<String, CertificatePayMethod>();
		CertificatePayMethod total = new CertificatePayMethod();
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
				handleAddNewStatic(scm, cssv, total);
			} else if (cssv.getDealInfoType() == 100) { // 更新
				handleRefreshStatic(scm, cssv, total);
			} else if (cssv.getDealInfoType() == 20) {// 遗失补办
				handleLostStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 30) { // 损坏更换
				handleDamageStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 4) { // 信息变更
				handleModifyStatic(scm, cssv, total);
			} else if (cssv.getDealInfoType() == 24) { // 遗失补办+信息变更
				handleChangeLostStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 34) { // 损坏更换+更新
				handleChangeReplaceStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 104) {// 更新+变更
				handleUpdateChangeStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 120) {// 更新+遗失补办
				handleUpdateLostStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 130) {// 更新+损坏更换
				handleUpdateReplaceStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 124) {// 更新+遗失补办+变更
				handleChangeUpdateLostStatic(scm, cssv, total);

			} else if (cssv.getDealInfoType() == 134) {// 更新+损坏更换+变更
				handleChangeUpdateReplaceStatic(scm, cssv, total);
			}

			monthMap.put(cssv.getMonth(), scm);
		}
		monthMap.put("total", total);
		// monthMap.put("totalColumn");
		return monthMap;
	}

	public CertificatePayMethodDetails handleCertificatePayMethodDetails(CertificatePayMethodDetails cpd,
			PaymethodCertificateSettleVo pcsv) {
		if (cpd == null) {
			cpd = new CertificatePayMethodDetails();
		}
		switch (pcsv.getPAYMETHOD()) {
		case 1: // pos
			cpd.setMethodPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodPosCount());
			cpd.addMethod("1", "POS");
			break;
		case 10: // money
			cpd.setMethodMoneyCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodMoneyCount());
			cpd.addMethod("10", "现金");
			break;
		case 11: // money and pos
			cpd.setMethodMoneyAndPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodMoneyAndPosCount());
			cpd.addMethod("11", "现金+POS");
			break;
		case 100: //
			cpd.setMethodBankCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodBankCount());
			cpd.addMethod("100", "转账");
			break;
		case 101: //
			cpd.setMethodBankAndPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodBankAndPosCount());
			cpd.addMethod("101", "转账+POS");
			break;
		case 110: //
			cpd.setMethodBankAndMoneyCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodBankAndMoneyCount());
			cpd.addMethod("110", "转账+现金");
			break;
		case 1000: //
			cpd.setMethodAlipayCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayCount());
			cpd.addMethod("1000", "支付宝");
			break;
		case 1001: //
			cpd.setMethodAlipayAndPosCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayAndPosCount());
			cpd.addMethod("1001", "支付宝+POS");
			break;
		case 1010: //
			cpd.setMethodAlipayAndMoneyCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayAndMoneyCount());
			cpd.addMethod("1010", "支付宝+现金");
			break;
		case 1100: //
			cpd.setMethodAlipayAndBankCount(pcsv.getWorkCount().intValue());
			cpd.setTotalCount(cpd.getTotalCount() + cpd.getMethodAlipayAndBankCount());
			cpd.addMethod("1100", "支付宝+转账");
			break;
		}
		return cpd;
	}

	private void handleAddNewStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzqyadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd1(cpd);
				total.getXzqyadd1().getMethods().putAll(cpd.getMethods());
				total.getXzqyadd1().setTotalCount(total.getXzqyadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));
			
				break;
			case 2:
				cpd = scm.getXzqyadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd2(cpd);
				total.getXzqyadd2().getMethods().putAll(cpd.getMethods());
				total.getXzqyadd2().setTotalCount(total.getXzqyadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzqyadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd3(cpd);
				total.getXzqyadd3().getMethods().putAll(cpd.getMethods());
				total.getXzqyadd3().setTotalCount(total.getXzqyadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzqyadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd4(cpd);
				total.getXzqyadd4().getMethods().putAll(cpd.getMethods());
				total.getXzqyadd4().setTotalCount(total.getXzqyadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzqyadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzqyadd5(cpd);
				total.getXzqyadd5().getMethods().putAll(cpd.getMethods());
				total.getXzqyadd5().setTotalCount(total.getXzqyadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrQadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd1(cpd);
				total.getXzgrQadd1().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd1()
						.setTotalCount(total.getXzgrQadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getXzgrQadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd2(cpd);
				total.getXzgrQadd2().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd2()
						.setTotalCount(total.getXzgrQadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzgrQadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd3(cpd);
				total.getXzgrQadd3().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd3()
						.setTotalCount(total.getXzgrQadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzgrQadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd4(cpd);
				total.getXzgrQadd4().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd4()
						.setTotalCount(total.getXzgrQadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzgrQadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd5(cpd);
				total.getXzgrQadd2().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd2()
						.setTotalCount(total.getXzgrQadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrGadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd1(cpd);
				total.getXzgrGadd1().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd1()
						.setTotalCount(total.getXzgrGadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getXzgrGadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd2(cpd);
				total.getXzgrGadd2().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd2()
						.setTotalCount(total.getXzgrGadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzgrGadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd3(cpd);
				total.getXzgrGadd3().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd3()
						.setTotalCount(total.getXzgrGadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzgrGadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd4(cpd);
				total.getXzgrGadd4().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd4()
						.setTotalCount(total.getXzgrGadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzgrGadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd5(cpd);
				total.getXzgrGadd5().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd5()
						.setTotalCount(total.getXzgrGadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzjgadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd1(cpd);
				total.getXzjgadd1().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd1().setTotalCount(total.getXzjgadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getXzjgadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd2(cpd);
				total.getXzjgadd2().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd2().setTotalCount(total.getXzjgadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzjgadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd3(cpd);
				total.getXzjgadd3().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd3().setTotalCount(total.getXzjgadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzjgadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd4(cpd);
				total.getXzjgadd4().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd4().setTotalCount(total.getXzjgadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzjgadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd5(cpd);
				total.getXzjgadd5().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd5().setTotalCount(total.getXzjgadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}

	private void handleRefreshStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getGxqyadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd1(cpd);
				total.getGxqyadd1().getMethods().putAll(cpd.getMethods());
				total.getGxqyadd1().setTotalCount(total.getGxqyadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getGxqyadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd2(cpd);
				total.getGxqyadd2().getMethods().putAll(cpd.getMethods());
				total.getGxqyadd2().setTotalCount(total.getGxqyadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getGxqyadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd3(cpd);
				total.getGxqyadd3().getMethods().putAll(cpd.getMethods());
				total.getGxqyadd3().setTotalCount(total.getGxqyadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getGxqyadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd4(cpd);
				total.getGxqyadd4().getMethods().putAll(cpd.getMethods());
				total.getGxqyadd4().setTotalCount(total.getGxqyadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getGxqyadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setGxqyadd5(cpd);
				total.getGxqyadd5().getMethods().putAll(cpd.getMethods());
				total.getGxqyadd5().setTotalCount(total.getGxqyadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrQadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd1(cpd);
				total.getXzgrQadd1().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd1()
						.setTotalCount(total.getXzgrQadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getXzgrQadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd2(cpd);
				total.getXzgrQadd2().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd2()
						.setTotalCount(total.getXzgrQadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzgrQadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd3(cpd);
				total.getXzgrQadd3().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd3()
						.setTotalCount(total.getXzgrQadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzgrQadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd4(cpd);
				total.getXzgrQadd4().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd4()
						.setTotalCount(total.getXzgrQadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzgrQadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrQadd5(cpd);
				total.getXzgrQadd5().getMethods().putAll(cpd.getMethods());
				total.getXzgrQadd5()
						.setTotalCount(total.getXzgrQadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzgrGadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd1(cpd);
				total.getXzgrGadd1().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd1()
						.setTotalCount(total.getXzgrGadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getXzgrGadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd2(cpd);
				total.getXzgrGadd2().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd2()
						.setTotalCount(total.getXzgrGadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzgrGadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd3(cpd);
				total.getXzgrGadd3().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd3()
						.setTotalCount(total.getXzgrGadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzgrGadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd4(cpd);
				total.getXzgrGadd4().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd4()
						.setTotalCount(total.getXzgrGadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzgrGadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzgrGadd5(cpd);
				total.getXzgrGadd5().getMethods().putAll(cpd.getMethods());
				total.getXzgrGadd5()
						.setTotalCount(total.getXzgrGadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getXzjgadd1();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd1(cpd);
				total.getXzjgadd1().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd1().setTotalCount(total.getXzjgadd1().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getXzjgadd2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd2(cpd);
				total.getXzjgadd2().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd2().setTotalCount(total.getXzjgadd2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getXzjgadd3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd3(cpd);
				total.getXzjgadd3().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd3().setTotalCount(total.getXzjgadd3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getXzjgadd4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd4(cpd);
				total.getXzjgadd4().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd4().setTotalCount(total.getXzjgadd4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getXzjgadd5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setXzjgadd5(cpd);
				total.getXzjgadd5().getMethods().putAll(cpd.getMethods());
				total.getXzjgadd5().setTotalCount(total.getXzjgadd5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}

	private void handleLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getLostCerateqy();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCerateqy(cpd);
			total.getLostCerateqy().getMethods().putAll(cpd.getMethods());
			total.getLostCerateqy()
					.setTotalCount(total.getLostCerateqy().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;
		case "2": // 个人证书（企业）
			cpd = scm.getLostCerategrQ();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCerategrQ(cpd);
			total.getLostCerategrQ().getMethods().putAll(cpd.getMethods());
			total.getLostCerategrQ()
					.setTotalCount(total.getLostCerategrQ().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "6":// 个人证书（机构）
			cpd = scm.getLostCerategrG();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCerategrG(cpd);
			total.getLostCerategrG().getMethods().putAll(cpd.getMethods());
			total.getLostCerategrG()
					.setTotalCount(total.getLostCerategrG().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "3":// 机构证书
			cpd = scm.getLostCeratejg();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setLostCeratejg(cpd);
			total.getLostCeratejg().getMethods().putAll(cpd.getMethods());
			total.getLostCeratejg()
					.setTotalCount(total.getLostCeratejg().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		}
	}

	private void handleDamageStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getDamageCertificateqy();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificateqy(cpd);
			total.getDamageCertificateqy().getMethods().putAll(cpd.getMethods());
			total.getDamageCertificateqy()
					.setTotalCount(total.getDamageCertificateqy().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;
		case "2": // 个人证书（企业）
			cpd = scm.getDamageCertificategrQ();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificategrQ(cpd);
			total.getDamageCertificategrQ().getMethods().putAll(cpd.getMethods());
			total.getDamageCertificategrQ()
					.setTotalCount(total.getDamageCertificategrQ().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "6":// 个人证书（机构）
			cpd = scm.getDamageCertificategrG();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificategrG(cpd);

			total.getDamageCertificategrG().getMethods().putAll(cpd.getMethods());
			total.getDamageCertificategrG()
					.setTotalCount(total.getDamageCertificategrG().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "3":// 机构证书
			cpd = scm.getDamageCertificatejg();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setDamageCertificatejg(cpd);
			total.getDamageCertificatejg().getMethods().putAll(cpd.getMethods());
			total.getDamageCertificatejg()
					.setTotalCount(total.getDamageCertificatejg().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		}
	}

	private void handleModifyStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getModifyNumqy();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumqy(cpd);
			total.getModifyNumqy().getMethods().putAll(cpd.getMethods());
			total.getModifyNumqy()
					.setTotalCount(total.getModifyNumqy().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;
		case "2": // 个人证书（企业）
			cpd = scm.getModifyNumgrQ();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumgrQ(cpd);
			total.getModifyNumgrQ().getMethods().putAll(cpd.getMethods());
			total.getModifyNumgrQ()
					.setTotalCount(total.getModifyNumgrQ().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "6":// 个人证书（机构）
			cpd = scm.getModifyNumgrG();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumgrG(cpd);
			total.getModifyNumgrG().getMethods().putAll(cpd.getMethods());
			total.getModifyNumgrG()
					.setTotalCount(total.getModifyNumgrG().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "3":// 机构证书
			cpd = scm.getModifyNumjg();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setModifyNumjg(cpd);
			total.getModifyNumjg().getMethods().putAll(cpd.getMethods());
			total.getModifyNumjg()
					.setTotalCount(total.getModifyNumjg().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		}
	}

	private void handleChangeLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getChangeLostqyNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostqyNum(cpd);
			total.getChangeLostqyNum().getMethods().putAll(cpd.getMethods());
			total.getChangeLostqyNum()
					.setTotalCount(total.getChangeLostqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;
		case "2": // 个人证书（企业）
			cpd = scm.getChangeLostgrQNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostgrQNum(cpd);

			total.getChangeLostgrQNum().getMethods().putAll(cpd.getMethods());
			total.getChangeLostgrQNum()
					.setTotalCount(total.getChangeLostgrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "6":// 个人证书（机构）
			cpd = scm.getChangeLostgrGNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostgrGNum(cpd);
			total.getChangeLostqyNum().getMethods().putAll(cpd.getMethods());
			total.getChangeLostqyNum()
					.setTotalCount(total.getChangeLostqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "3":// 机构证书
			cpd = scm.getChangeLostjgNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeLostjgNum(cpd);

			total.getChangeLostjgNum().getMethods().putAll(cpd.getMethods());
			total.getChangeLostjgNum()
					.setTotalCount(total.getChangeLostjgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		}
	}

	private void handleChangeReplaceStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {
		case "1": // 企业证书
			cpd = scm.getChangeReplaceqyNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplaceqyNum(cpd);
			total.getChangeReplaceqyNum().getMethods().putAll(cpd.getMethods());
			total.getChangeReplaceqyNum()
					.setTotalCount(total.getChangeReplaceqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;
		case "2": // 个人证书（企业）
			cpd = scm.getChangeReplacegrQNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplacegrQNum(cpd);
			total.getChangeReplacegrQNum().getMethods().putAll(cpd.getMethods());
			total.getChangeReplacegrQNum()
					.setTotalCount(total.getChangeReplacegrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "6":// 个人证书（机构）
			cpd = scm.getChangeReplacegrGNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplacegrGNum(cpd);
			total.getChangeReplacegrGNum().getMethods().putAll(cpd.getMethods());
			total.getChangeReplacegrGNum()
					.setTotalCount(total.getChangeReplacegrGNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		case "3":// 机构证书
			cpd = scm.getChangeReplacejgNum();
			cpd = handleCertificatePayMethodDetails(cpd, cssv);
			scm.setChangeReplacejgNum(cpd);
			total.getChangeReplacejgNum().getMethods().putAll(cpd.getMethods());
			total.getChangeReplacejgNum()
					.setTotalCount(total.getChangeReplacejgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			break;

		}
	}

	private void handleUpdateChangeStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangeqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum(cpd);
				total.getUpdateChangeqyNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangeqyNum()
						.setTotalCount(total.getUpdateChangeqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateChangeqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum2(cpd);
				total.getUpdateChangeqyNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangeqyNum2()
						.setTotalCount(total.getUpdateChangeqyNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateChangeqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum3(cpd);
				total.getUpdateChangeqyNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangeqyNum3()
						.setTotalCount(total.getUpdateChangeqyNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateChangeqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum4(cpd);
				total.getUpdateChangeqyNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangeqyNum4()
						.setTotalCount(total.getUpdateChangeqyNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateChangeqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangeqyNum5(cpd);
				total.getUpdateChangeqyNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangeqyNum5()
						.setTotalCount(total.getUpdateChangeqyNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangegrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum(cpd);
				total.getUpdateChangegrQNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrQNum()
						.setTotalCount(total.getUpdateChangegrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateChangegrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum2(cpd);
				total.getUpdateChangegrQNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrQNum2()
						.setTotalCount(total.getUpdateChangegrQNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateChangegrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum3(cpd);
				total.getUpdateChangegrQNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrQNum3()
						.setTotalCount(total.getUpdateChangegrQNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateChangegrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum4(cpd);
				total.getUpdateChangegrQNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrQNum4()
						.setTotalCount(total.getUpdateChangegrQNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			case 5:
				cpd = scm.getUpdateChangegrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrQNum5(cpd);
				total.getUpdateChangegrQNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrQNum5()
						.setTotalCount(total.getUpdateChangegrQNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangegrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum(cpd);
				total.getUpdateChangegrGNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrGNum()
						.setTotalCount(total.getUpdateChangegrGNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateChangegrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum2(cpd);
				total.getUpdateChangegrGNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrGNum2()
						.setTotalCount(total.getUpdateChangegrGNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateChangegrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum3(cpd);
				total.getUpdateChangegrGNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrGNum3()
						.setTotalCount(total.getUpdateChangegrGNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateChangegrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum4(cpd);
				total.getUpdateChangegrGNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrGNum4()
						.setTotalCount(total.getUpdateChangegrGNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateChangegrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangegrGNum5(cpd);
				total.getUpdateChangegrGNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangegrGNum5()
						.setTotalCount(total.getUpdateChangegrGNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateChangejgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum(cpd);
				total.getUpdateChangejgNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangejgNum()
						.setTotalCount(total.getUpdateChangejgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateChangejgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum2(cpd);
				total.getUpdateChangejgNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangejgNum2()
						.setTotalCount(total.getUpdateChangejgNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateChangejgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum3(cpd);
				total.getUpdateChangejgNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangejgNum3()
						.setTotalCount(total.getUpdateChangejgNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateChangejgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum4(cpd);
				total.getUpdateChangejgNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangejgNum4()
						.setTotalCount(total.getUpdateChangejgNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateChangejgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateChangejgNum5(cpd);
				total.getUpdateChangejgNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateChangejgNum5()
						.setTotalCount(total.getUpdateChangejgNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}

	private void handleUpdateLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum(cpd);
				total.getUpdateLostqyNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostqyNum()
						.setTotalCount(total.getUpdateLostqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateLostqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum2(cpd);
				total.getUpdateLostqyNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostqyNum2()
						.setTotalCount(total.getUpdateLostqyNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateLostqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum3(cpd);
				total.getUpdateLostqyNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostqyNum3()
						.setTotalCount(total.getUpdateLostqyNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateLostqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum4(cpd);
				total.getUpdateLostqyNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostqyNum4()
						.setTotalCount(total.getUpdateLostqyNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateLostqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostqyNum5(cpd);
				total.getUpdateLostqyNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostqyNum5()
						.setTotalCount(total.getUpdateLostqyNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostgrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum(cpd);
				total.getUpdateLostgrQNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrQNum()
						.setTotalCount(total.getUpdateLostgrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateLostgrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum2(cpd);
				total.getUpdateLostgrQNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrQNum2()
						.setTotalCount(total.getUpdateLostgrQNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateLostgrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum3(cpd);
				total.getUpdateLostgrQNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrQNum3()
						.setTotalCount(total.getUpdateLostgrQNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateLostgrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum4(cpd);
				total.getUpdateLostgrQNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrQNum4()
						.setTotalCount(total.getUpdateLostgrQNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateLostgrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrQNum5(cpd);
				total.getUpdateLostgrQNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrQNum5()
						.setTotalCount(total.getUpdateLostgrQNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostgrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum(cpd);
				total.getUpdateLostgrGNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrGNum()
						.setTotalCount(total.getUpdateLostgrGNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateLostgrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum2(cpd);
				total.getUpdateLostgrGNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrGNum2()
						.setTotalCount(total.getUpdateLostgrGNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateLostgrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum3(cpd);
				total.getUpdateLostgrGNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrGNum3()
						.setTotalCount(total.getUpdateLostgrGNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateLostgrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum4(cpd);
				total.getUpdateLostgrGNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrGNum4()
						.setTotalCount(total.getUpdateLostgrGNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateLostgrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostgrGNum5(cpd);
				total.getUpdateLostgrGNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostgrGNum5()
						.setTotalCount(total.getUpdateLostgrGNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateLostjgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum(cpd);
				total.getUpdateLostjgNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostjgNum()
						.setTotalCount(total.getUpdateLostjgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateLostjgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum2(cpd);
				total.getUpdateLostjgNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostjgNum2()
						.setTotalCount(total.getUpdateLostjgNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateLostjgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum3(cpd);
				total.getUpdateLostjgNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostjgNum3()
						.setTotalCount(total.getUpdateLostjgNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateLostjgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum4(cpd);
				total.getUpdateLostjgNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostjgNum4()
						.setTotalCount(total.getUpdateLostjgNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateLostjgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateLostjgNum5(cpd);
				total.getUpdateLostjgNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateLostjgNum5()
						.setTotalCount(total.getUpdateLostjgNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}

	private void handleUpdateReplaceStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplaceqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum(cpd);
				total.getUpdateReplaceqyNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplaceqyNum()
						.setTotalCount(total.getUpdateReplaceqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateReplaceqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum2(cpd);
				total.getUpdateReplaceqyNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplaceqyNum2()
						.setTotalCount(total.getUpdateReplaceqyNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateReplaceqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum3(cpd);
				total.getUpdateReplaceqyNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplaceqyNum3()
						.setTotalCount(total.getUpdateReplaceqyNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateReplaceqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum4(cpd);
				total.getUpdateReplaceqyNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplaceqyNum4()
						.setTotalCount(total.getUpdateReplaceqyNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateReplaceqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplaceqyNum5(cpd);
				total.getUpdateReplaceqyNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplaceqyNum5()
						.setTotalCount(total.getUpdateReplaceqyNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplacegrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum(cpd);
				total.getUpdateReplacegrQNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrQNum()
						.setTotalCount(total.getUpdateReplacegrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateReplacegrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum2(cpd);
				total.getUpdateReplacegrQNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrQNum2().setTotalCount(
						total.getUpdateReplacegrQNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateReplacegrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum3(cpd);
				total.getUpdateReplacegrQNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrQNum3().setTotalCount(
						total.getUpdateReplacegrQNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateReplacegrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum4(cpd);
				total.getUpdateReplacegrQNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrQNum4().setTotalCount(
						total.getUpdateReplacegrQNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateReplacegrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrQNum5(cpd);
				total.getUpdateReplacegrQNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrQNum5().setTotalCount(
						total.getUpdateReplacegrQNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplacegrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum(cpd);
				total.getUpdateReplacegrGNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrGNum()
						.setTotalCount(total.getUpdateReplacegrGNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateReplacegrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum2(cpd);
				total.getUpdateReplacegrGNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrGNum2().setTotalCount(
						total.getUpdateReplacegrGNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateReplacegrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum3(cpd);
				total.getUpdateReplacegrGNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrGNum3().setTotalCount(
						total.getUpdateReplacegrGNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateReplacegrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum4(cpd);
				total.getUpdateReplacegrGNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrGNum4().setTotalCount(
						total.getUpdateReplacegrGNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateReplacegrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacegrGNum5(cpd);
				total.getUpdateReplacegrGNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacegrGNum5().setTotalCount(
						total.getUpdateReplacegrGNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getUpdateReplacejgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum(cpd);
				total.getUpdateReplacejgNum().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacejgNum()
						.setTotalCount(total.getUpdateReplacejgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getUpdateReplacejgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum2(cpd);
				total.getUpdateReplacejgNum2().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacejgNum2()
						.setTotalCount(total.getUpdateReplacejgNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getUpdateReplacejgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum3(cpd);
				total.getUpdateReplacejgNum3().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacejgNum3()
						.setTotalCount(total.getUpdateReplacejgNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getUpdateReplacejgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum4(cpd);
				total.getUpdateReplacejgNum4().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacejgNum4()
						.setTotalCount(total.getUpdateReplacejgNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getUpdateReplacejgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setUpdateReplacejgNum5(cpd);
				total.getUpdateReplacejgNum5().getMethods().putAll(cpd.getMethods());
				total.getUpdateReplacejgNum5()
						.setTotalCount(total.getUpdateReplacejgNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}

	private void handleChangeUpdateLostStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum(cpd);
				total.getChangeUpdateLostqyNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostqyNum().setTotalCount(
						total.getChangeUpdateLostqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateLostqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum2(cpd);
				total.getChangeUpdateLostqyNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostqyNum2().setTotalCount(
						total.getChangeUpdateLostqyNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateLostqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum3(cpd);
				total.getChangeUpdateLostqyNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostqyNum3().setTotalCount(
						total.getChangeUpdateLostqyNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateLostqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum4(cpd);
				total.getChangeUpdateLostqyNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostqyNum4().setTotalCount(
						total.getChangeUpdateLostqyNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateLostqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostqyNum5(cpd);
				total.getChangeUpdateLostqyNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostqyNum5().setTotalCount(
						total.getChangeUpdateLostqyNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostgrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum(cpd);
				total.getChangeUpdateLostgrQNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrQNum().setTotalCount(
						total.getChangeUpdateLostgrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateLostgrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum2(cpd);
				total.getChangeUpdateLostgrQNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrQNum2().setTotalCount(
						total.getChangeUpdateLostgrQNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			case 3:
				cpd = scm.getChangeUpdateLostgrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum3(cpd);
				total.getChangeUpdateLostgrQNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrQNum3().setTotalCount(
						total.getChangeUpdateLostgrQNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateLostgrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum4(cpd);
				total.getChangeUpdateLostgrQNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrQNum4().setTotalCount(
						total.getChangeUpdateLostgrQNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateLostgrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrQNum5(cpd);
				total.getChangeUpdateLostgrQNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrQNum5().setTotalCount(
						total.getChangeUpdateLostgrQNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostgrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum(cpd);
				total.getChangeUpdateLostgrGNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrGNum().setTotalCount(
						total.getChangeUpdateLostgrGNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateLostgrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum2(cpd);
				total.getChangeUpdateLostgrGNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrGNum2().setTotalCount(
						total.getChangeUpdateLostgrGNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateLostgrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum3(cpd);
				total.getChangeUpdateLostgrGNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrGNum3().setTotalCount(
						total.getChangeUpdateLostgrGNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateLostgrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum4(cpd);
				total.getChangeUpdateLostgrGNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrGNum4().setTotalCount(
						total.getChangeUpdateLostgrGNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateLostgrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostgrGNum5(cpd);
				total.getChangeUpdateLostgrGNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostgrGNum5().setTotalCount(
						total.getChangeUpdateLostgrGNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateLostjgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum(cpd);
				total.getChangeUpdateLostjgNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostjgNum().setTotalCount(
						total.getChangeUpdateLostjgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateLostjgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum2(cpd);
				total.getChangeUpdateLostjgNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostjgNum2().setTotalCount(
						total.getChangeUpdateLostjgNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateLostjgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum3(cpd);
				total.getChangeUpdateLostjgNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostjgNum3().setTotalCount(
						total.getChangeUpdateLostjgNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateLostjgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum4(cpd);
				total.getChangeUpdateLostjgNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostjgNum4().setTotalCount(
						total.getChangeUpdateLostjgNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateLostjgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateLostjgNum5(cpd);
				total.getChangeUpdateLostjgNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateLostjgNum5().setTotalCount(
						total.getChangeUpdateLostjgNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}

	private void handleChangeUpdateReplaceStatic(CertificatePayMethod scm, PaymethodCertificateSettleVo cssv,
			CertificatePayMethod total) {
		CertificatePayMethodDetails cpd;
		switch (cssv.getProductName()) {

		case "1": // 企业证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplaceqyNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum(cpd);
				total.getChangeUpdateReplaceqyNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplaceqyNum().setTotalCount(
						total.getChangeUpdateReplaceqyNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateReplaceqyNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum2(cpd);
				total.getChangeUpdateReplaceqyNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplaceqyNum2().setTotalCount(
						total.getChangeUpdateReplaceqyNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateReplaceqyNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum3(cpd);
				total.getChangeUpdateReplaceqyNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplaceqyNum3().setTotalCount(
						total.getChangeUpdateReplaceqyNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateReplaceqyNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum4(cpd);
				total.getChangeUpdateReplaceqyNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplaceqyNum4().setTotalCount(
						total.getChangeUpdateReplaceqyNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateReplaceqyNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplaceqyNum5(cpd);
				total.getChangeUpdateReplaceqyNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplaceqyNum5().setTotalCount(
						total.getChangeUpdateReplaceqyNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "2": // 个人证书（企业）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplacegrQNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum(cpd);
				total.getChangeUpdateReplacegrQNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrQNum().setTotalCount(
						total.getChangeUpdateReplacegrQNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateReplacegrQNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum2(cpd);
				total.getChangeUpdateReplacegrQNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrQNum2().setTotalCount(
						total.getChangeUpdateReplacegrQNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateReplacegrQNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum3(cpd);
				total.getChangeUpdateReplacegrQNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrQNum3().setTotalCount(
						total.getChangeUpdateReplacegrQNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateReplacegrQNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum4(cpd);
				total.getChangeUpdateReplacegrQNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrQNum4().setTotalCount(
						total.getChangeUpdateReplacegrQNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateReplacegrQNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrQNum5(cpd);
				total.getChangeUpdateReplacegrQNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrQNum5().setTotalCount(
						total.getChangeUpdateReplacegrQNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "6":// 个人证书（机构）
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplacegrGNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum(cpd);
				total.getChangeUpdateReplacegrGNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrGNum().setTotalCount(
						total.getChangeUpdateReplacegrGNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 2:
				cpd = scm.getChangeUpdateReplacegrGNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum2(cpd);
				total.getChangeUpdateReplacegrGNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrGNum2().setTotalCount(
						total.getChangeUpdateReplacegrGNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateReplacegrGNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum3(cpd);
				total.getChangeUpdateReplacegrGNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrGNum3().setTotalCount(
						total.getChangeUpdateReplacegrGNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateReplacegrGNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum4(cpd);
				total.getChangeUpdateReplacegrGNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrGNum4().setTotalCount(
						total.getChangeUpdateReplacegrGNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateReplacegrGNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacegrGNum5(cpd);
				total.getChangeUpdateReplacegrGNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacegrGNum5().setTotalCount(
						total.getChangeUpdateReplacegrGNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;
		case "3":// 机构证书
			switch (cssv.getYear()) {
			case 1:
				cpd = scm.getChangeUpdateReplacejgNum();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum(cpd);
				total.getChangeUpdateReplacejgNum().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacejgNum().setTotalCount(
						total.getChangeUpdateReplacejgNum().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

			case 2:
				cpd = scm.getChangeUpdateReplacejgNum2();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum2(cpd);
				total.getChangeUpdateReplacejgNum2().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacejgNum2().setTotalCount(
						total.getChangeUpdateReplacejgNum2().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 3:
				cpd = scm.getChangeUpdateReplacejgNum3();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum3(cpd);
				total.getChangeUpdateReplacejgNum3().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacejgNum3().setTotalCount(
						total.getChangeUpdateReplacejgNum3().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 4:
				cpd = scm.getChangeUpdateReplacejgNum4();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum4(cpd);
				total.getChangeUpdateReplacejgNum4().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacejgNum4().setTotalCount(
						total.getChangeUpdateReplacejgNum4().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			case 5:
				cpd = scm.getChangeUpdateReplacejgNum5();
				cpd = handleCertificatePayMethodDetails(cpd, cssv);
				scm.setChangeUpdateReplacejgNum5(cpd);
				total.getChangeUpdateReplacejgNum5().getMethods().putAll(cpd.getMethods());
				total.getChangeUpdateReplacejgNum5().setTotalCount(
						total.getChangeUpdateReplacejgNum5().getTotalCount() + (cssv.getPAYMETHOD()>0?cssv.getWorkCount().intValue():0));

				break;
			}
			break;

		}
	}
	
	public void mergeCell(HSSFSheet sheet,HSSFRow row ,int maxCol){
		String cellValue = row.getCell(1).getStringCellValue();;
		int cellStart=1;
		for(int i=2;i< maxCol;i++){
			if(row.getCell(i)!=null && !cellValue.equals(row.getCell(i).getStringCellValue())){
				cellValue = row.getCell(i).getStringCellValue();
				System.out.println("i:" +i +"\t" + row.getCell(i).getStringCellValue());
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cellStart, i-1));
				cellStart =i;
			}
			
			
		}		
	}
	
	
}