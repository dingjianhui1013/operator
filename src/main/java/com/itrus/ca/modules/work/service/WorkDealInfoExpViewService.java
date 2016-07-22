/**
 *2016年7月19日 下午1:54:43
 */
package com.itrus.ca.modules.work.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.work.dao.WorkDealInfoExpViewDao;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfoExpView;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.vo.WorkDealInfoExpViewVO;

/**
 * @author: liubin
 *
 */
@Component
@Transactional(readOnly = true)
public class WorkDealInfoExpViewService extends BaseService {
	static Log log = LogFactory.getLog(WorkDealInfoExpViewService.class);

	private LogUtil logUtil = new LogUtil();

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(WorkDealInfoExpViewService.class);

	@Autowired
	private WorkDealInfoExpViewDao workDealInfoExpViewDao;

	public Page<WorkDealInfo> find(HttpServletRequest request,
			HttpServletResponse response, WorkDealInfoExpViewVO query,
			boolean findAll) {
		DetachedCriteria dc = workDealInfoExpViewDao.createDetachedCriteria();

		if (query.getWorkDealInfo().getInputUser() != null
				&& query.getWorkDealInfo().getInputUser().getName() != null) {
			if (query.getWorkDealInfo().getInputUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("inputUserId"));
			} else if (!query.getWorkDealInfo().getInputUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.add(Restrictions.like("inputUserName", "%"
						+ query.getWorkDealInfo().getInputUser().getName()
						+ "%"));
			}
		}

		// 此条件只能查出在客户端制证的业务,但并不包含全部的客户端业务，因为有的客户端业务是通过平台制证的
		if (query.getWorkDealInfo().getBusinessCardUser() != null
				&& query.getWorkDealInfo().getBusinessCardUser().getName() != null) {
			if (query.getWorkDealInfo().getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("businessCardUser"));
			} else if (!query.getWorkDealInfo().getBusinessCardUser().getName()
					.trim().replace(" ", "").equals("")) {
				dc.add(Restrictions.like("businessCardUserName", "%"
						+ query.getWorkDealInfo().getBusinessCardUser()
								.getName() + "%"));
			}
		}

		dc.add(Restrictions.in("officeId", query.getOfficeIds()));

		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getCompanyName())) {
			dc.add(Restrictions.like("companyName", "%"
					+ query.getWorkDealInfo().getWorkCompany().getCompanyName()
					+ "%"));
		}
		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getOrganizationNumber())) {
			dc.add(Restrictions.like("organizationNumber", "%"
					+ query.getWorkDealInfo().getWorkCompany()
							.getOrganizationNumber() + "%"));
		}
		if (query.getWorkDealInfo().getWorkUser() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo().getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("contactName", "%"
					+ query.getWorkDealInfo().getWorkUser().getContactName()
					+ "%"));
		}
		if (query.getWorkDealInfo().getWorkUser() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo().getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("conCertNumber", "%"
					+ query.getWorkDealInfo().getWorkUser().getConCertNumber()
					+ "%"));
		}

		if (StringUtils.isNotEmpty(query.getWorkDealInfo().getKeySn())) {
			dc.add(Restrictions.like("keySN", "%"
					+ query.getWorkDealInfo().getKeySn() + "%"));
		}

		if (query.getWorkDealInfo().getPayType() != null
				&& query.getWorkDealInfo().getPayType() != 0) {
			dc.add(Restrictions.eq("payType", query.getWorkDealInfo()
					.getPayType()));
		}

		if (query.getWorkDealInfo().getAttestationUser() != null
				&& !query.getWorkDealInfo().getAttestationUser().getName()
						.equals("")) {
			dc.add(Restrictions.like("attestationUserName", "%"
					+ query.getWorkDealInfo().getAttestationUser().getName()
					+ "%"));
		}

		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getProvince())
				&& !query.getWorkDealInfo().getWorkCompany().getProvince()
						.equals("省份")) {
			dc.add(Restrictions.eq("province", query.getWorkDealInfo()
					.getWorkCompany().getProvince()));
		}
		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getCity())
				&& !query.getWorkDealInfo().getWorkCompany().getCity()
						.equals("地级市")) {
			dc.add(Restrictions.eq("city", query.getWorkDealInfo()
					.getWorkCompany().getCity()));
		}
		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getDistrict())
				&& !query.getWorkDealInfo().getWorkCompany().getDistrict()
						.equals("市、县级市")) {
			dc.add(Restrictions.eq("district", query.getWorkDealInfo()
					.getWorkCompany().getDistrict()));
		}

		if (query.getWorkDealInfo().getWorkPayInfo() != null) {
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodMoney", true),
						Restrictions.eq("relationMethod", 0)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodPos", true),
						Restrictions.eq("relationMethod", 1)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodBank", true),
						Restrictions.eq("relationMethod", 2)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodAlipay", true),
						Restrictions.eq("relationMethod", 3)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("methodGov", true));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("methodContract", true));
			}
		}

		if (query.getPaymentStartTime() != null) {
			dc.add(Restrictions.ge("payUserDate", query.getPaymentStartTime()));
		}
		if (query.getPaymentEndTime() != null) {
			query.getPaymentEndTime().setHours(23);
			query.getPaymentEndTime().setMinutes(59);
			query.getPaymentEndTime().setSeconds(59);
			dc.add(Restrictions.le("payUserDate", query.getPaymentEndTime()));
		}

		if (query.getLuruStartTime() != null) {
			dc.add(Restrictions.ge("inputUserDate", query.getLuruStartTime()));
		}
		if (query.getLuruStartTime() != null) {
			query.getLuruStartTime().setHours(23);
			query.getLuruStartTime().setMinutes(59);
			query.getLuruStartTime().setSeconds(59);
			dc.add(Restrictions.le("inputUserDate", query.getLuruStartTime()));
		}

		if (query.getDaoqiStartTime() != null) {
			dc.add(Restrictions.ge("notAfter", query.getDaoqiStartTime()));
		}
		if (query.getDaoqiEndTime() != null) {
			query.getDaoqiEndTime().setHours(23);
			query.getDaoqiEndTime().setMinutes(59);
			query.getDaoqiEndTime().setSeconds(59);
			dc.add(Restrictions.le("notAfter", query.getDaoqiEndTime()));
		}

		if (query.getZhizhengStartTime() != null) {
			dc.add(Restrictions.ge("businessCardUserDate",
					query.getZhizhengStartTime()));
		}

		if (query.getZhizhengEndTime() != null) {
			Calendar zhizhengEndCalendar = Calendar.getInstance();
			zhizhengEndCalendar.setTime(query.getZhizhengEndTime());
			zhizhengEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
			zhizhengEndCalendar.set(Calendar.MINUTE, 59);
			zhizhengEndCalendar.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate",
					zhizhengEndCalendar.getTime()));
		}

		if (query.getOffices() != null && query.getOffices().size() > 0) {
			dc.add(Restrictions.in("officeId", query.getOffices()));
		}

		if (query.getApply() != null) {
			dc.add(Restrictions.eq("appId", query.getApply()));
		}
		if (query.getCertType() != null && !query.getCertType().equals("")) {
			dc.add(Restrictions.eq("productName", query.getCertType()));
		}

		if (query.getWorkType() != null) {

			dc.add(Restrictions.or(
					Restrictions.eq("dealInfoType", query.getWorkType()),
					Restrictions.eq("dealInfoType1", query.getWorkType()),
					Restrictions.eq("dealInfoType2", query.getWorkType()),
					Restrictions.eq("dealInfoType3", query.getWorkType())));
		}

		if (query.getYear() != null) {
			dc.add(Restrictions.eq("year", query.getYear()));
		}

		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));

		Page<WorkDealInfo> res = new Page<WorkDealInfo>(request, response);
		Page<WorkDealInfoExpView> lst = new Page<WorkDealInfoExpView>(request,
				response);
		if (findAll)
			lst.setPageSize(-1);
		lst = workDealInfoExpViewDao.find(lst, dc);

		if (lst == null || lst.getList() == null || lst.getList().size() <= 0) {
			return res;
		}
		List<WorkDealInfo> resList = viewToDealInfo(lst.getList());

		res.setCount(lst.getCount());
		res.setPageNo(lst.getPageNo());
		res.setPageSize(lst.getPageSize());
		res.setList(resList);
		return res;
	}

	public Page<WorkDealInfo> findCX(HttpServletRequest request,
			HttpServletResponse response, WorkDealInfoExpViewVO query,
			boolean findAll) {
		DetachedCriteria dc = workDealInfoExpViewDao.createDetachedCriteria();
		dc.add(Restrictions.in("officeId", query.getOfficeIds()));
		if (query.getWorkDealInfo().getInputUser() != null
				&& query.getWorkDealInfo().getInputUser().getName() != null) {
			if (query.getWorkDealInfo().getInputUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("inputUserId"));
			} else if (!query.getWorkDealInfo().getInputUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.add(Restrictions.like("inputUserName", "%"
						+ query.getWorkDealInfo().getInputUser().getName()
						+ "%"));
			}
		}

		// 此条件只能查出在客户端制证的业务,但并不包含全部的客户端业务，因为有的客户端业务是通过平台制证的
		if (query.getWorkDealInfo().getBusinessCardUser() != null
				&& query.getWorkDealInfo().getBusinessCardUser().getName() != null) {
			if (query.getWorkDealInfo().getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("businessCardUser"));
			} else if (!query.getWorkDealInfo().getBusinessCardUser().getName()
					.trim().replace(" ", "").equals("")) {
				dc.add(Restrictions.like("businessCardUserName", "%"
						+ query.getWorkDealInfo().getBusinessCardUser()
								.getName() + "%"));
			}
		}

		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getCompanyName())) {
			dc.add(Restrictions.like("companyName", "%"
					+ query.getWorkDealInfo().getWorkCompany().getCompanyName()
					+ "%"));
		}
		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getOrganizationNumber())) {
			dc.add(Restrictions.like("organizationNumber", "%"
					+ query.getWorkDealInfo().getWorkCompany()
							.getOrganizationNumber() + "%"));
		}
		if (query.getWorkDealInfo().getWorkUser() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo().getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("contactName", "%"
					+ query.getWorkDealInfo().getWorkUser().getContactName()
					+ "%"));
		}
		if (query.getWorkDealInfo().getWorkUser() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo().getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("conCertNumber", "%"
					+ query.getWorkDealInfo().getWorkUser().getConCertNumber()
					+ "%"));
		}

		if (StringUtils.isNotEmpty(query.getWorkDealInfo().getKeySn())) {
			dc.add(Restrictions.like("keySN", "%"
					+ query.getWorkDealInfo().getKeySn() + "%"));
		}

		if (query.getWorkDealInfo().getPayType() != null
				&& query.getWorkDealInfo().getPayType() != 0) {
			dc.add(Restrictions.eq("payType", query.getWorkDealInfo()
					.getPayType()));
		}

		if (query.getWorkDealInfo().getAttestationUser() != null
				&& !query.getWorkDealInfo().getAttestationUser().getName()
						.equals("")) {
			dc.add(Restrictions.like("attestationUserName", "%"
					+ query.getWorkDealInfo().getAttestationUser().getName()
					+ "%"));
		}

		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getProvince())
				&& !query.getWorkDealInfo().getWorkCompany().getProvince()
						.equals("省份")) {
			dc.add(Restrictions.eq("province", query.getWorkDealInfo()
					.getWorkCompany().getProvince()));
		}
		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getCity())
				&& !query.getWorkDealInfo().getWorkCompany().getCity()
						.equals("地级市")) {
			dc.add(Restrictions.eq("city", query.getWorkDealInfo()
					.getWorkCompany().getCity()));
		}
		if (query.getWorkDealInfo().getWorkCompany() != null
				&& StringUtils.isNotEmpty(query.getWorkDealInfo()
						.getWorkCompany().getDistrict())
				&& !query.getWorkDealInfo().getWorkCompany().getDistrict()
						.equals("市、县级市")) {
			dc.add(Restrictions.eq("district", query.getWorkDealInfo()
					.getWorkCompany().getDistrict()));
		}

		if (query.getWorkDealInfo().getWorkPayInfo() != null) {
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodMoney", true),
						Restrictions.eq("relationMethod", 0)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodPos", true),
						Restrictions.eq("relationMethod", 1)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodBank", true),
						Restrictions.eq("relationMethod", 2)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(Restrictions.eq("methodAlipay", true),
						Restrictions.eq("relationMethod", 3)));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("methodGov", true));
			}
			if (query.getWorkDealInfo().getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("methodContract", true));
			}
		}

		// 查询条件由之前的鉴证时间改为缴费时间
		if (query.getPaymentStartTime() != null) {
			dc.add(Restrictions.ge("payUserDate", query.getPaymentStartTime()));
		}
		if (query.getPaymentEndTime() != null) {
			query.getPaymentEndTime().setHours(23);
			query.getPaymentEndTime().setMinutes(59);
			query.getPaymentEndTime().setSeconds(59);
			dc.add(Restrictions.le("payUserDate", query.getPaymentEndTime()));
		}

		if (query.getLuruStartTime() != null) {
			dc.add(Restrictions.ge("inputUserDate", query.getLuruStartTime()));
		}
		if (query.getLuruEndTime() != null) {
			query.getLuruEndTime().setHours(23);
			query.getLuruEndTime().setMinutes(59);
			query.getLuruEndTime().setSeconds(59);
			dc.add(Restrictions.le("inputUserDate", query.getLuruEndTime()));
		}

		if (query.getDaoqiStartTime() != null) {
			dc.add(Restrictions.ge("notAfter", query.getDaoqiStartTime()));
		}
		if (query.getDaoqiEndTime() != null) {
			query.getDaoqiEndTime().setHours(23);
			query.getDaoqiEndTime().setMinutes(59);
			query.getDaoqiEndTime().setSeconds(59);
			dc.add(Restrictions.le("notAfter", query.getDaoqiEndTime()));
		}

		if (query.getZhizhengStartTime() != null) {
			dc.add(Restrictions.ge("businessCardUserDate",
					query.getZhizhengStartTime()));
		}

		if (query.getZhizhengEndTime() != null) {
			Calendar zhizhengEndCalendar = Calendar.getInstance();
			zhizhengEndCalendar.setTime(query.getZhizhengEndTime());
			zhizhengEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
			zhizhengEndCalendar.set(Calendar.MINUTE, 59);
			zhizhengEndCalendar.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate",
					zhizhengEndCalendar.getTime()));
		}

		if (query.getOffices() != null && query.getOffices().size() > 0) {
			dc.add(Restrictions.in("officeId", query.getOffices()));
		}

		if (query.getApply() != null) {
			dc.add(Restrictions.eq("appId", query.getApply()));
		}
		if (query.getCertType() != null && !query.getCertType().equals("")) {
			dc.add(Restrictions.eq("productName", query.getCertType()));
		}

		if (query.getWorkType() != null) {
			dc.add(Restrictions.or(Restrictions.ne("dealInfoType", 11),
					Restrictions.isNull("dealInfoType")));
		}

		if (query.getYear() != null) {
			dc.add(Restrictions.eq("year", query.getYear()));
		}

		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE)));

		Page<WorkDealInfo> res = new Page<WorkDealInfo>(request, response);
		Page<WorkDealInfoExpView> lst = new Page<WorkDealInfoExpView>(request,
				response);
		if (findAll)
			lst.setPageSize(-1);
		lst = workDealInfoExpViewDao.find(lst, dc);
		if (lst == null || lst.getList() == null || lst.getList().size() <= 0) {
			return res;
		}
		List<WorkDealInfo> resList = viewToDealInfo(lst.getList());

		res.setCount(lst.getCount());
		res.setPageNo(lst.getPageNo());
		res.setPageSize(lst.getPageSize());
		res.setList(resList);
		return res;
	}

	/**
	 * 为了减少前台显示的重构部分，把视图转换成表对象
	 * 
	 * @param lst
	 * @return List<WorkDealInfo>
	 */
	private List<WorkDealInfo> viewToDealInfo(List<WorkDealInfoExpView> lst) {
		List<WorkDealInfo> resList = new ArrayList<WorkDealInfo>();
		for (WorkDealInfoExpView e : lst) {
			WorkDealInfo po = new WorkDealInfo();
			po.setSvn(e.getSvn());

			ConfigApp configApp = new ConfigApp();
			configApp.setAlias(e.getAppAlias());
			po.setConfigApp(configApp);

			WorkCompany company = new WorkCompany();
			company.setCompanyName(e.getCompanyName());
			company.setOrganizationNumber(e.getOrganizationNumber());
			po.setWorkCompany(company);

			WorkUser workUser = new WorkUser();
			workUser.setContactName(e.getContactName());
			workUser.setConCertNumber(e.getConCertNumber());
			po.setWorkUser(workUser);

			WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
			workCertApplyInfo.setName(e.getCertApplyInfoName());
			WorkCertInfo workCertInfo = new WorkCertInfo();
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfo.setSignDate(e.getSignDate());
			workCertInfo.setNotafter(e.getNotAfter());
			po.setWorkCertInfo(workCertInfo);

			ConfigProduct configProduct = new ConfigProduct();
			configProduct.setProductName(e.getProductName());
			configProduct.setId(e.getProductId());
			po.setConfigProduct(configProduct);

			po.setDealInfoType(e.getDealInfoType());
			po.setDealInfoType1(e.getDealInfoType1());
			po.setDealInfoType2(e.getDealInfoType2());
			po.setDealInfoType3(e.getDealInfoType3());
			po.setKeySn(e.getKeySN());
			po.setAddCertDays(e.getAddCertDays());
			po.setYear(e.getYear());
			po.setLastDays(e.getLastDays());
			po.setDealInfoStatus(e.getDealInfoStatus());
			po.setId(e.getId());
			resList.add(po);
		}
		return resList;
	}
}
