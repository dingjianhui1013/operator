/**
 *2016年7月19日 下午1:54:43
 */
package com.itrus.ca.modules.work.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.utils.excel.ExportExcel;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.dao.WorkDealInfoExpViewDao;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfoExpView;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.vo.WorkDealInfoExpViewVO;
import com.itrus.ca.modules.work.vo.WorkDealInfoVo;

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

	/**
	 * 从视图中查询数据
	 * 
	 * @param request
	 * @param response
	 * @param query
	 * @param findAll
	 * @param isDX
	 * @return Page<WorkDealInfoExpView>
	 */
	public Page<WorkDealInfoExpView> findByView(HttpServletRequest request,
			HttpServletResponse response, WorkDealInfoExpViewVO query,
			boolean findAll, boolean isDX) {
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
			query.getPaymentStartTime().setHours(00);
			query.getPaymentStartTime().setMinutes(00);
			query.getPaymentStartTime().setSeconds(00);
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
			if (isDX) {
				dc.add(Restrictions.or(Restrictions.ne("dealInfoType", 11),
						Restrictions.isNull("dealInfoType")));
			} else {
				dc.add(Restrictions.or(
						Restrictions.eq("dealInfoType", query.getWorkType()),
						Restrictions.eq("dealInfoType1", query.getWorkType()),
						Restrictions.eq("dealInfoType2", query.getWorkType()),
						Restrictions.eq("dealInfoType3", query.getWorkType())));
			}
		}

		if (query.getYear() != null) {
			dc.add(Restrictions.eq("year", query.getYear()));
		}

		if (isDX) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
					WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		} else {
			/*dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
					WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
					"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));*/
			
			
			Disjunction dis1 = Restrictions.disjunction();  
            
			dis1.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));  
			dis1.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE));
            
			Disjunction dis2 = Restrictions.disjunction();  
            
			dis2.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));  
			dis2.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE));
			dis2.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_WAIT));
              

			
			
			dc.add(Restrictions.or(
					Restrictions.and(Restrictions.or(Restrictions.ne("dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT), Restrictions.isNull("dealInfoType")), dis1),
					Restrictions.and(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT), dis2)));
			
			
		}

		Page<WorkDealInfoExpView> lst = new Page<WorkDealInfoExpView>(request,
				response);
		if (findAll)
			lst.setPageSize(-1);
		lst = workDealInfoExpViewDao.find(lst, dc);
		return lst;
	}

	/**
	 * 代替原来的find和findCX两个方法
	 * 
	 * @param request
	 * @param response
	 * @param query
	 * @param findAll
	 *            (是否查询所有数据)
	 * @param isDx
	 *            (是否吊销)
	 * @return
	 */
	public Page<WorkDealInfo> find(HttpServletRequest request,
			HttpServletResponse response, WorkDealInfoExpViewVO query,
			boolean findAll, boolean isDx) {
		Page<WorkDealInfoExpView> lst = new Page<WorkDealInfoExpView>(request,
				response);
		Page<WorkDealInfo> res = new Page<WorkDealInfo>(request, response);
		lst = findByView(request, response, query, findAll, isDx);
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
			po.setId(e.getId());
			po.setSvn(e.getSvn());
			po.setDealInfoType(e.getDealInfoType());
			po.setDealInfoType1(e.getDealInfoType1());
			po.setDealInfoType2(e.getDealInfoType2());
			po.setDealInfoType3(e.getDealInfoType3());
			po.setKeySn(e.getKeySN());
			po.setAddCertDays(e.getAddCertDays());
			po.setYear(e.getYear());
			po.setLastDays(e.getLastDays());
			po.setDealInfoStatus(e.getDealInfoStatus());
			po.setBusinessCardUserDate(e.getBusinessCardUserDate());
			po.setOfficeId(e.getOfficeId());
			po.setNotafter(e.getNotAfter());

			User inputUser = new User();
			inputUser.setId(e.getInputUserId());
			inputUser.setName(e.getInputUserName());
			po.setInputUser(inputUser);

			ConfigApp configApp = new ConfigApp();
			configApp.setId(e.getAppId());
			configApp.setAlias(e.getAppAlias());
			po.setConfigApp(configApp);

			WorkCompany company = new WorkCompany();
			company.setId(e.getWorkCompanyId());
			company.setCompanyName(e.getCompanyName());
			company.setOrganizationNumber(e.getOrganizationNumber());
			po.setWorkCompany(company);

			WorkUser workUser = new WorkUser();
			workUser.setId(e.getWorkUserId());
			workUser.setContactName(e.getContactName());
			workUser.setConCertNumber(e.getConCertNumber());
			po.setWorkUser(workUser);

			WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
			workCertApplyInfo.setName(e.getCertApplyInfoName());

			WorkCertInfo workCertInfo = new WorkCertInfo();
			workCertInfo.setId(e.getCertId());
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfo.setSignDate(e.getSignDate());
			workCertInfo.setNotafter(e.getNotAfter());
			workCertInfo.setNotbefore(e.getNotbefore());
			po.setWorkCertInfo(workCertInfo);

			ConfigProduct configProduct = new ConfigProduct();
			configProduct.setId(e.getProductId());
			configProduct.setProductName(e.getProductName());
			configProduct.setId(e.getProductId());
			po.setConfigProduct(configProduct);

			resList.add(po);
		}
		return resList;
	}

	/**
	 * @param lst
	 * @param fileName
	 * @throws IOException
	 */
	public void exportExcel(Map<String, String> query,
			List<WorkDealInfoExpView> list, String fileName,
			String information, HttpServletResponse response)
			throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		WorkDealInfoStatus workDealInfoStatus = new WorkDealInfoStatus();

		final List<WorkDealInfoVo> workDealInfoVos = new ArrayList<WorkDealInfoVo>();
		String dealInfoType = null;
		String dealInfoType1 = null;
		String dealInfoType2 = null;
		String dealInfoType3 = null;
		List<String> varNameList = getVarNameList(information);

		for (final WorkDealInfoExpView dealInfo : list) {
			final WorkDealInfoVo dealInfoVo = new WorkDealInfoVo();
			// 业务编号
			if (information.indexOf(",ywbh") > -1) {
				dealInfoVo.setSvn(dealInfo.getSvn());
			}
			// 鉴证人
			if (information.indexOf(",jzr") > -1)
				dealInfoVo.setAttestAtionUserName(dealInfo
						.getAttestationUserName());
			// 产品名称
			if (information.indexOf(",cpmc") > -1) {
				ProductType p = new ProductType();
				dealInfoVo.setProductName(p.getProductTypeName(new Integer(
						dealInfo.getProductName())));
			}
			// 产品标识
			if (information.indexOf(",cpbs") > -1) {
				if (dealInfo.getProductLabel() == 0) {
					dealInfoVo.setProductLabel("通用");
				} else {
					dealInfoVo.setProductLabel("专用");
				}
			}
			// 业务类型
			if (information.indexOf(",ywlx") > -1) {
				if (dealInfo.getDealInfoType() == null) {
					dealInfoType = "";
				} else {
					dealInfoType = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType());
				}
				if (dealInfo.getDealInfoType1() == null) {
					dealInfoType1 = "";
				} else {
					dealInfoType1 = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType1());
				}
				if (dealInfo.getDealInfoType2() == null) {
					dealInfoType2 = "";
				} else {
					dealInfoType2 = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType2());
				}
				if (dealInfo.getDealInfoType3() == null) {
					dealInfoType3 = "";
				} else {
					dealInfoType3 = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType3());
				}
				dealInfoVo.setDealInfoType(dealInfoType + "" + dealInfoType1
						+ "" + dealInfoType2 + "" + dealInfoType3);
			}
			// 经办人姓名
			if (information.indexOf(",jbrxm") > -1) {
				dealInfoVo.setCertApplyInfoName(dealInfo
						.getWorkUserHIsContactName());
			}
			// 经办人手机号
			if (information.indexOf(",jbrphone") > -1) {
				dealInfoVo.setCertApplyInfoPhone(dealInfo
						.getWorkUserHIsContactPhone());
			}
			// key编码
			if (information.indexOf(",keybm") > -1) {
				dealInfoVo.setKeySn(dealInfo.getKeySN());
			}
			// 录入人
			if (information.indexOf(",lrr") > -1) {
				dealInfoVo.setInputUserName(dealInfo.getInputUserName());
			}
			// 单位名称
			if (information.indexOf(",dwmcc") > -1) {
				dealInfoVo.setCompanyName(dealInfo.getCompanyName());
			}
			// 收费方式
			if (information.indexOf(",sffs") > -1) {
				String payUserType = "";
				if (dealInfo.getMethodPos()) {
					payUserType = "POS机";
				} else if (dealInfo.getMethodBank()) {
					payUserType = "银行转账";
				} else if (dealInfo.getMethodMoney()) {
					payUserType = "现金";
				} else if (dealInfo.getMethodAlipay()) {
					payUserType = "支付宝";
				} else if (dealInfo.getMethodContract()) {
					payUserType = "合同采购";
				} else if (dealInfo.getMethodGov()) {
					payUserType = "政府采购";
				}
				dealInfoVo.setPayType(payUserType);
			}
			// 应用名称
			if (information.indexOf(",yymc") > -1) {
				dealInfoVo.setAppName(dealInfo.getAppName());
			}
			// 行政所属区
			if (information.indexOf(",xzssq") > -1) {
				dealInfoVo.setAreaName(dealInfo.getAreaName());
			}
			// 计费策略类型
			if (information.indexOf(",jfcllx") > -1) {
				String tempStyleName = "";
				// 1标准 2政府统一采购 3合同采购
				if (dealInfo.getTempStyle().equals("1")) {
					tempStyleName = "标准";
				} else if (dealInfo.getTempStyle().equals("2")) {
					tempStyleName = "政府统一采购";
				} else if (dealInfo.getTempStyle().equals("3")) {
					tempStyleName = "合同采购";
				}
				dealInfoVo.setTempStyle(tempStyleName);
			}
			// 收费金额
			if (information.indexOf(",sfje") > -1) {
				dealInfoVo.setTotalMoney(StringHelper
						.formartDecimalToStr(dealInfo.getWorkTotalMoney()));
			}
			// 收费人
			if (information.indexOf(",sfr") > -1) {
				dealInfoVo.setPayUserName(dealInfo.getPayUserName());
			}
			// 录入日期
			if (information.indexOf(",lrdate") > -1) {
				if (dealInfo.getInputUserDate() != null) {
					dealInfoVo.setInputDate(StringHelper.getSystime(
							"yyyy-MM-dd HH:mm:ss", dealInfo.getInputUserDate()
									.getTime()));
				} else {
					dealInfoVo.setInputDate("");
				}
			}
			// 制证日期
			if (information.indexOf(",zzdate") > -1) {
				if (dealInfo.getSignDate() != null) {
					String signDateString = dfm.format(dealInfo.getSignDate());
					dealInfoVo.setSignDateString(signDateString);
				} else {
					dealInfoVo.setSignDateString("");

				}
			}
			// 鉴证日期
			// if (information.indexOf(",jzdate") > -1) {
			// if (dealInfo.getAttestationUserDate() != null) {
			// dealInfoVo.setAttestAtionUserDate(StringHelper.getSystime(
			// "yyyy-MM-dd HH:mm:ss", dealInfo
			// .getAttestationUserDate().getTime()));
			// } else {
			// dealInfoVo.setAttestAtionUserDate("");
			// }
			// }
			// 持有人姓名
			if (information.indexOf(",cyrxm") > -1) {
				dealInfoVo.setContactName(dealInfo.getContactName());
			}
			// 计费策略模板
			if (information.indexOf(",jfclmb") > -1) {
				dealInfoVo.setTempName(dealInfo.getTempName());
			}
			// 到期日期
			if (information.indexOf(",dqrq") > -1) {
				String notafterString = dealInfo.getNotAfter() == null ? ""
						: df.format(dealInfo.getNotAfter());
				dealInfoVo.setNotAfter(notafterString);
			}
			// 制证人
			if (information.indexOf(",zzr") > -1) {
				dealInfoVo.setBusinessCardUserName(dealInfo
						.getBusinessCardUserName());
			}
			// 业务状态
			if (information.indexOf(",ywzt") > -1) {
				dealInfoVo
						.setDealInfoStatus(workDealInfoStatus.WorkDealInfoStatusMap
								.get(dealInfo.getDealInfoStatus()));
			}
			// 收费日期
			if (information.indexOf(",sfdate") > -1) {
				if (dealInfo.getPayUserDate() != null) {
					dealInfoVo.setPayDate(StringHelper.getSystime(
							"yyyy-MM-dd HH:mm:ss", dealInfo.getPayUserDate()
									.getTime()));
				} else {
					dealInfoVo.setPayDate("");
				}
			}
			// 有效期
			if (information.indexOf(",yxq") > -1) {
				if (dealInfo.getAddCertDays() == null) {
					dealInfoVo.setCertDays((dealInfo.getYear() == null ? 0
							: dealInfo.getYear())
							* 365
							+ (dealInfo.getLastDays() == null ? 0 : dealInfo
									.getLastDays()) + "（天）");
				} else {
					dealInfoVo.setCertDays(dealInfo.getYear() * 365
							+ dealInfo.getLastDays()
							+ dealInfo.getAddCertDays() + "（天）");
				}
			}
			// 经办人邮箱
			if (information.indexOf(",jbremail") > -1) {
				dealInfoVo.setCertApplyInfoEmail(dealInfo.getContactEmail());
			}
			// 别名
			if (information.indexOf(",bm") > -1) {
				dealInfoVo.setAlias(dealInfo.getAppAlias());
			}

			workDealInfoVos.add(dealInfoVo);
		}

		String queryContent = "业务查询"; // getQueryContent(query);
		new ExportExcel(queryContent, WorkDealInfoVo.class, varNameList)
				.setDataList(workDealInfoVos).write(response, fileName)
				.dispose();
	}

	/**
	 * 根据输入的查询条件把他输出到excel文件内
	 * 
	 * @param query
	 * @return String
	 */
	private String getQueryContent(Map<String, String> query) {
		StringBuffer sb = new StringBuffer("业务查询\r\n");
		Iterator<String> it = query.keySet().iterator();
		while (it.hasNext()) {
			String k = it.next();
			sb.append(k).append(":").append(query.get(k)).append("|");
		}
		return sb.toString();
	}

	/**
	 * 查出EXCEL需要导出的列对应的变量名
	 * 
	 * @param information
	 * @return List<String>
	 */
	private List<String> getVarNameList(String information) {
		List<String> varNameList = new ArrayList<String>();
		// 业务编号
		if (information.indexOf(",ywbh") > -1) {
			varNameList.add("svn");
		}
		// 鉴证人
		if (information.indexOf(",jzr") > -1) {
			varNameList.add("attestAtionUserName");
		}
		// 产品名称
		if (information.indexOf(",cpmc") > -1) {

			varNameList.add("productName");
		}
		// 产品标识
		if (information.indexOf(",cpbs") > -1) {
			varNameList.add("productLabel");
		}
		// 业务类型
		if (information.indexOf(",ywlx") > -1) {
			varNameList.add("dealInfoType");
		}
		// 经办人姓名
		if (information.indexOf(",jbrxm") > -1) {
			varNameList.add("certApplyInfoName");
		}
		// 经办人手机号
		if (information.indexOf(",jbrphone") > -1) {
			varNameList.add("certApplyInfoPhone");
		}
		// key编码
		if (information.indexOf(",keybm") > -1) {
			varNameList.add("keySn");
		}
		// 录入人
		if (information.indexOf(",lrr") > -1) {
			varNameList.add("inputUserName");
		}
		// 单位名称
		if (information.indexOf(",dwmcc") > -1) {
			varNameList.add("companyName");
		}
		// 付费方式
		if (information.indexOf(",sffs") > -1) {
			varNameList.add("payType");
		}
		// 应用名称
		if (information.indexOf(",yymc") > -1) {
			varNameList.add("appName");
		}
		// 行政所属区
		if (information.indexOf(",xzssq") > -1) {
			varNameList.add("areaName");
		}
		// 计费策略类型
		if (information.indexOf(",jfcllx") > -1) {
			varNameList.add("tempStyle");
		}
		// 收费金额
		if (information.indexOf(",sfje") > -1) {
			varNameList.add("totalMoney");
		}
		// 收费人
		if (information.indexOf(",sfr") > -1) {
			varNameList.add("payUserName");
		}
		// 录入日期
		if (information.indexOf(",lrdate") > -1) {
			varNameList.add("inputDate");
		}
		// 制证日期
		if (information.indexOf(",zzdate") > -1) {
			varNameList.add("signDateString");
		}
		// 鉴定日期
		if (information.indexOf(",jzdate") > -1) {
			varNameList.add("attestAtionUserDate");
		}
		// 证书持有人
		if (information.indexOf(",cyrxm") > -1) {
			varNameList.add("contactName");
		}
		// 计费策略模板
		if (information.indexOf(",jfclmb") > -1) {
			varNameList.add("tempName");
		}
		// 到期日期
		if (information.indexOf(",dqrq") > -1) {
			varNameList.add("notAfter");
		}
		// 制证人
		if (information.indexOf(",zzr") > -1) {
			varNameList.add("businessCardUserName");
		}
		// 业务状态
		if (information.indexOf(",ywzt") > -1) {
			varNameList.add("dealInfoStatus");
		}
		// 收费日期
		if (information.indexOf(",sfdate") > -1) {
			varNameList.add("payDate");
		}
		// 有效期
		if (information.indexOf(",yxq") > -1) {
			varNameList.add("certDays");
		}
		// 经办人邮箱
		if (information.indexOf(",jbremail") > -1) {
			varNameList.add("certApplyInfoEmail");
		}
		// 别名
		if (information.indexOf(",bm") > -1) {
			varNameList.add("alias");
		}
		return varNameList;
	}
}
