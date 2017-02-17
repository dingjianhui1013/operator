package com.itrus.ca.modules.work.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.message.vo.WorkDealInfoVo;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.CreateExcelUtils;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.task.entity.BatchUpdateInfoScca;
import com.itrus.ca.modules.task.service.BasicInfoSccaService;
import com.itrus.ca.modules.task.service.BatchUpdateInfoSccaService;
import com.itrus.ca.modules.work.dao.WorkCertInfoDao;
import com.itrus.ca.modules.work.dao.WorkCertTrustApplyDao;
import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.entity.MonthPayment;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfoExpView;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.vo.WorkDealInfoListVo;
import com.itrus.ca.modules.work.vo.WorkpaymentInfo_dealinfoVo;

/**
 * 业务办理Service
 * 
 * @author ZhangShaoBo
 * @version 2014-06-13
 */
/**
 * @author liumeng
 * 
 */
@Component
@Transactional(readOnly = true)
public class WorkDealInfoService extends BaseService {

	static Log log = LogFactory.getLog(WorkDealInfoService.class);
	private Log fixLog = LogFactory.getLog("fix");
	private Log exLog = LogFactory.getLog("ex");
	private Log updateLog = LogFactory.getLog("update");

	private LogUtil logUtil = new LogUtil();
	private static final int CELL_NUM = 9;

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(WorkDealInfoService.class);

	@Autowired
	private WorkDealInfoDao workDealInfoDao;

	@Autowired
	private WorkCertTrustApplyDao workCertTrustApplyDao;

	@Autowired
	private WorkCertInfoDao workCertInfoDao;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigRaAccountService raAccountService;

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	/*@Autowired
	private BatchUpdateInfoSccaService batchUpdateInfoSccaService;*/
	
	@Autowired
	private ConfigProductService configProductService;

	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;

	@Autowired
	private ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;

	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private WorkCompanyHisService workCompanyHisService;
	@Autowired
	private WorkUserHisService workUserHisService;

	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;

	@Autowired
	private WorkPayInfoService workPayInfoService;

	@Autowired
	private WorkLogService workLogService;

	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;
	@Autowired
	private BasicInfoSccaService basicInfoSccaService;

	static Long YEAR_MILL = 31536000000L;

	
	public WorkDealInfo get(Long id) {
		return workDealInfoDao.findOne(id);
	}

	/*
	 * public List<WorkDealInfo> getList(Iterable<Long> ids){
	 * 
	 * return (List<WorkDealInfo>) workDealInfoDao.findAll(ids); }
	 */
	public Page<WorkDealInfo> findAppList(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
		}
		// dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
		// WorkDealInfo.DEL_FLAG_NORMAL));
		// dc.add(Restrictions.isNotNull("userSn"));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoType", WorkDealInfoType.TYPE_RETURN_MONEY),
				Restrictions.eq("dealInfoType",
						WorkDealInfoType.TYPE_RETURN_WORK)));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findByIds(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, ArrayList<Long> dealInfoIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		/*
		 * if (workDealInfo.getWorkCompany() != null) { if
		 * (workDealInfo.getWorkCompany().getId() != null) {
		 * dc.add(Restrictions.eq("workCompany",
		 * workDealInfo.getWorkCompany())); } }
		 */
		dc.add(Restrictions.in("id", dealInfoIds));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoType", WorkDealInfoType.TYPE_RETURN_MONEY),
				Restrictions.eq("dealInfoType",
						WorkDealInfoType.TYPE_RETURN_WORK)));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> find(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));

		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			dc.createAlias("workCompany", "workCompany");
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			dc.createAlias("workUser", "workUser");
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactPhone",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactTel",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoType() != null) {
			dc.add(Restrictions.or(
					Restrictions.eq("dealInfoType",
							workDealInfo.getDealInfoType()),
					Restrictions.eq("dealInfoType1",
							workDealInfo.getDealInfoType())));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		if (workDealInfo.getDelFlag() != null) {
			dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
					WorkDealInfo.DEL_FLAG_NORMAL));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findByGuiDang(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));

		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			dc.createAlias("workCompany", "workCompany");
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			dc.createAlias("workUser", "workUser");
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactPhone",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactTel",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoType() != null) {
			dc.add(Restrictions.or(
					Restrictions.eq("dealInfoType",
							workDealInfo.getDealInfoType()),
					Restrictions.eq("dealInfoType1",
							workDealInfo.getDealInfoType())));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> find11(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, Long apply, Integer workType,
			Date makeCertStart, Date makeCertEnd, Date expiredStart,
			Date expiredEnd) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (workType != null) {
			dc.add(Restrictions.eq("dealInfoType", workType));
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}

		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber() != null
					&& !workDealInfo.getWorkCompany().getOrganizationNumber()
							.equals("")) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany()
								.getOrganizationNumber())));

			}
		}

		if (makeCertStart != null) {
			makeCertStart.setHours(0);
			makeCertStart.setMinutes(0);
			makeCertStart.setSeconds(0);
			dc.add(Restrictions.ge("businessCardUserDate", makeCertStart));
		}

		if (makeCertEnd != null) {
			makeCertEnd.setHours(23);
			makeCertEnd.setMinutes(59);
			makeCertEnd.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", makeCertEnd));
		}

		if (expiredStart != null) {
			expiredStart.setHours(0);
			expiredStart.setMinutes(0);
			expiredStart.setSeconds(0);
			dc.add(Restrictions.ge("notafter", expiredStart));
		}

		if (expiredEnd != null) {
			expiredEnd.setHours(23);
			expiredEnd.setMinutes(59);
			expiredEnd.setSeconds(59);
			dc.add(Restrictions.le("notafter", expiredEnd));
		}

		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(page, dc);
	}

	public List<WorkDealInfo> find12(WorkDealInfo workDealInfo, Long apply,
			Integer workType, Date makeCertStart, Date makeCertEnd,
			Date expiredStart, Date expiredEnd) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (workType != null) {
			dc.add(Restrictions.eq("dealInfoType", workType));
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}

		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber() != null
					&& !workDealInfo.getWorkCompany().getOrganizationNumber()
							.equals("")) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany()
								.getOrganizationNumber())));

			}
		}
		if (makeCertStart != null) {
			makeCertStart.setHours(0);
			makeCertStart.setMinutes(0);
			makeCertStart.setSeconds(0);
			dc.add(Restrictions.ge("businessCardUserDate", makeCertStart));
		}

		if (makeCertEnd != null) {
			makeCertEnd.setHours(23);
			makeCertEnd.setMinutes(59);
			makeCertEnd.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", makeCertEnd));
		}

		if (expiredStart != null) {
			expiredStart.setHours(0);
			expiredStart.setMinutes(0);
			expiredStart.setSeconds(0);
			dc.add(Restrictions.ge("notafter", expiredStart));
		}

		if (expiredEnd != null) {
			expiredEnd.setHours(23);
			expiredEnd.setMinutes(59);
			expiredEnd.setSeconds(59);
			dc.add(Restrictions.le("notafter", expiredEnd));
		}
		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(dc);
	}

	public Page<WorkDealInfo> find1(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			dc.createAlias("workCompany", "workCompany");
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findEnterprise(Page<WorkDealInfo> page,
			List<Long> companyIds, String productId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		String dealInfoStatus = WorkDealInfoStatus.STATUS_CERT_OBTAINED;
		if (companyIds != null && companyIds.size() > 0) {
			dc.createAlias("workCompany", "workCompany");
			dc.add(Restrictions.in("workCompany.id", companyIds));
		}
		if (productId != null) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.eq("configProduct.productName", productId));
		}
		if (dealInfoStatus != null) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findEnterprise2(Page<WorkDealInfo> page,
			List<Long> workdealinfoIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		String dealInfoStatus = WorkDealInfoStatus.STATUS_CERT_OBTAINED;
		if (dealInfoStatus != null) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}
		if (workdealinfoIds.size() > 0 && !workdealinfoIds.isEmpty()) {
			dc.add(Restrictions.in("id", workdealinfoIds));
		} else {
			dc.add(Restrictions.eq("id", -1L));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findEnterprise1(Page<WorkDealInfo> page,
			List<Long> companyIds, String productId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		String dealInfoStatus = WorkDealInfoStatus.STATUS_CERT_OBTAINED;
		if (companyIds != null && companyIds.size() > 0) {
			dc.createAlias("workCompany", "workCompany");
			dc.add(Restrictions.in("workCompany.id", companyIds));
		}
		if (productId != null) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.eq("configProduct.productName", productId));
		}
		if (dealInfoStatus != null) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}
		dc.add(Restrictions.isNotNull("prevId"));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public List<WorkDealInfo> findEnterprise(List<Long> companyIds,
			String productId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		String dealInfoStatus = WorkDealInfoStatus.STATUS_CERT_OBTAINED;
		if (companyIds != null && companyIds.size() > 0) {
			dc.createAlias("workCompany", "workCompany");
			dc.add(Restrictions.in("workCompany.id", companyIds));
		}
		if (productId != null) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.eq("configProduct.productName", productId));
		}
		if (dealInfoStatus != null) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}
		// dc.add(Restrictions.isNotNull("prevId"));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findPersonal(List<Long> companyIds,
			String productId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		String dealInfoStatus = WorkDealInfoStatus.STATUS_CERT_OBTAINED;
		if (companyIds != null && companyIds.size() > 0) {
			dc.createAlias("workCompany", "workCompany");
			dc.add(Restrictions.in("workCompany.id", companyIds));
		}
		if (productId != null) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.eq("configProduct.productName", productId));
		}
		if (dealInfoStatus != null) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.manyFind(dc, 10);
	}

	public List<WorkDealInfo> findPersonal1(List<Long> workdealinfoIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		String dealInfoStatus = WorkDealInfoStatus.STATUS_CERT_OBTAINED;

		if (dealInfoStatus != null) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}
		if (workdealinfoIds.size() > 0 && !workdealinfoIds.isEmpty()) {
			dc.add(Restrictions.in("id", workdealinfoIds));
		} else {
			dc.add(Restrictions.eq("id", -1L));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.manyFind(dc, 10);
	}

	public Page<WorkDealInfo> findByStatus(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.or(
				Restrictions.isNotNull("isIxin"),
				dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
						"officeId")));
		if (workDealInfo.getWorkCertInfo() != null) {
			dc.add(Restrictions.isNotNull("notafter"));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactPhone",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactTel",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() == null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
					Restrictions.eq("dealInfoStatus", "0"),
					Restrictions.eq("dealInfoStatus", "16"),
					Restrictions.eq("dealInfoStatus", "13")));
		} else {
			if (workDealInfo.getDealInfoStatus().equals("-1")) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
						Restrictions.eq("dealInfoStatus", "0"),
						Restrictions.eq("dealInfoStatus", "16"),
						Restrictions.eq("dealInfoStatus", "13")));
			} else {
				dc.add(Restrictions.eq("dealInfoStatus",
						workDealInfo.getDealInfoStatus()));
			}
		}

		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType() != -1) {
				dc.add(Restrictions.or(
						Restrictions.eq("dealInfoType",
								workDealInfo.getDealInfoType()),
						Restrictions.eq("dealInfoType1",
								workDealInfo.getDealInfoType())));
			}
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public List<WorkDealInfo> findByIsIxin(WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");

		Restrictions.eq("createBy.office", UserUtils.getUser().getOffice());
		if (workDealInfo.getWorkCertInfo() != null) {
			dc.add(Restrictions.isNotNull("notafter"));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactPhone",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactTel",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() == null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
					Restrictions.eq("dealInfoStatus", "0")));
		} else {
			if (workDealInfo.getDealInfoStatus().equals("-1")) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
						Restrictions.eq("dealInfoStatus", "0"),
						Restrictions.eq("dealInfoStatus", "13")));
			} else {
				dc.add(Restrictions.eq("dealInfoStatus",
						workDealInfo.getDealInfoStatus()));
			}
		}

		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType() != -1) {
				dc.add(Restrictions.or(
						Restrictions.eq("dealInfoType",
								workDealInfo.getDealInfoType()),
						Restrictions.eq("dealInfoType1",
								workDealInfo.getDealInfoType())));
			}
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.add(Restrictions.isNotNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public Page<WorkDealInfo> find4Apply(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, Date startTime, Date endTime, Long apply) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");

		dc.add(Restrictions.or(
				Restrictions.isNotNull("isIxin"),
				dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
						"officeId")));

		// dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
		// "officeId"));
		// dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
		// "createBy", UserUtils.getUser())));

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}

		if (startTime != null && endTime != null) {
			dc.add(Restrictions.ge("notafter", startTime));
			dc.add(Restrictions.ge("notafter", endTime));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber() != null
					&& !workDealInfo.getWorkCompany().getOrganizationNumber()
							.equals("")) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany()
								.getOrganizationNumber())));

			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfoListVo> find4Apply(Page<WorkDealInfoListVo> page,
			WorkDealInfo workDealInfo, Date startTime, Date endTime,
			Long apply, String productName, Date makeCertStartTime,
			Date makeCertEndTime) throws ParseException {

		// String sql = getFind4ApplySql(page, workDealInfo, startTime, endTime,
		// apply, productName, makeCertStartTime, makeCertEndTime);

		String sql = "select * "
				+ getFind4ApplySql(page, workDealInfo, startTime, endTime,
						apply, productName, makeCertStartTime, makeCertEndTime);

		String sqlCount = "select count(*) as ct "
				+ getFind4ApplySql(page, workDealInfo, startTime, endTime,
						apply, productName, makeCertStartTime, makeCertEndTime);

		Page<WorkDealInfoListVo> res = new Page<WorkDealInfoListVo>(
				page.getPageNo(), page.getPageSize());
		List<Map> lst = null;
		List<Map> ct = null;
		try {
			long start = System.currentTimeMillis();
			lst = workDealInfoDao.findBySQLListMap(sql, page.getPageNo(),
					page.getPageSize());
			long end = System.currentTimeMillis();

			log.debug("workDealInfoListQueryList用时:" + (end - start) + "ms");

			ct = workDealInfoDao.findBySQLListMap(sqlCount, 1, 1);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int count = new Integer(ct.get(0).get("CT").toString());
		res.setCount(count);
		List<WorkDealInfoListVo> resLst = new ArrayList<WorkDealInfoListVo>();
		for (Map e : lst) {
			WorkDealInfoListVo vo = new WorkDealInfoListVo();
			Iterator<String> it = e.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				if (k.equals("ID")) {
					if (e.get("ID") != null)
						vo.setId(new Long(e.get("ID").toString()));

				}
				if (k.equals("COMPANYNAME")) {
					if (e.get("COMPANYNAME") != null)
						vo.setCompanyName(e.get("COMPANYNAME").toString());
				}
				if (k.equals("CERTAPPLYINFONAME")) {
					if (e.get("CERTAPPLYINFONAME") != null)
						vo.setCertApplyInfoName(e.get("CERTAPPLYINFONAME")
								.toString());
				}

				if (k.equals("CONFIGAPPNAME")) {
					if (e.get("CONFIGAPPNAME") != null)
						vo.setConfigAppName(e.get("CONFIGAPPNAME").toString());
				}
				if (k.equals("PRODUCTNAME")) {
					if (e.get("PRODUCTNAME") != null)
						vo.setProductName(e.get("PRODUCTNAME").toString());
				}
				if (k.equals("DEALINFOTYPE")) {
					if (e.get("DEALINFOTYPE") != null)
						vo.setDealInfoType(new Integer(e.get("DEALINFOTYPE")
								.toString()));
				}
				if (k.equals("DEALINFOTYPE2")) {
					if (e.get("DEALINFOTYPE2") != null)
						vo.setDealInfoType2(new Integer(e.get("DEALINFOTYPE2")
								.toString()));
				}
				if (k.equals("DEALINFOTYPE3")) {
					if (e.get("DEALINFOTYPE3") != null)
						vo.setDealInfoType3(new Integer(e.get("DEALINFOTYPE3")
								.toString()));
				}
				if (k.equals("DEALINFOTYPE1")) {
					if (e.get("DEALINFOTYPE1") != null)
						vo.setDealInfoType1(new Integer(e.get("DEALINFOTYPE1")
								.toString()));
				}
				if (k.equals("NOTAFTER")) {
					if (e.get("NOTAFTER") != null)
						vo.setNotafter(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("NOTAFTER").toString()));
				}
				if (k.equals("NOTBEFORE")) {
					if (e.get("NOTBEFORE") != null)
						vo.setNotbefore(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("NOTBEFORE").toString()));
				}
				if (k.equals("KEYSN")) {
					if (e.get("KEYSN") != null)
						vo.setKeySn(e.get("KEYSN").toString());
				}
				
				if(k.equals("REMARKINFO")){
					if (e.get("REMARKINFO") != null)
						vo.setRemarkInfo(e.get("REMARKINFO").toString());
				}
				
				
				if (k.equals("SIGNDATE")) {
					if (e.get("SIGNDATE") != null)
						vo.setSignDate(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("SIGNDATE").toString()));
				}
				if (k.equals("ADDCERTDAYS")) {
					if (e.get("ADDCERTDAYS") != null)
						vo.setAddCertDays(new Integer(e.get("ADDCERTDAYS")
								.toString()));
				}
				if (k.equals("LASTDAYS")) {
					if (e.get("LASTDAYS") != null)
						vo.setLastDays(new Integer(e.get("LASTDAYS").toString()));
				}
				if (k.equals("SVN")) {
					if (e.get("SVN") != null)
						vo.setSvn(e.get("SVN").toString());
				}
				if (k.equals("YEAR")) {
					if (e.get("YEAR") != null)
						vo.setYear(new Integer(e.get("YEAR").toString()));
				}
				if (k.equals("DEALINFOSTATUS")) {
					if (e.get("DEALINFOSTATUS") != null)
						vo.setDealInfoStatus(e.get("DEALINFOSTATUS").toString());
				}
				if (k.equals("NAME")) {
					if (e.get("NAME") != null)
						vo.setCertApplyInfoName(e.get("NAME").toString());
				}
			}
			resLst.add(vo);
		}
		res.setList(resLst);
		return res;
	}

	private String getFind4ApplySql(Page<WorkDealInfoListVo> page,
			WorkDealInfo workDealInfo, Date startTime, Date endTime,
			Long apply, String productName, Date makeCertStartTime,
			Date makeCertEndTime) {
		StringBuffer sql = new StringBuffer();
		String sqls = dataScopeFilterByWorkDealInfo(UserUtils.getUser(),
				"areaId", "officeId").toString();
		sqls = sqls.substring(1, sqls.length() - 1);
		sqls = sqls.replace("officeId", "office_Id");
		sql.append(" from(select this_.id as id," + "this_.svn as svn,"
				+ "configapp5_.app_name as configAppName,"
				+ "workcompan2_.company_name as companyName,"
				+ "configprod7_.product_name as productName,"
				+ "this_.deal_info_type as dealInfoType,"
				+ "this_.deal_info_type1 as dealInfoType1,"
				+ "this_.deal_info_type2 as dealInfoType2,"
				+ "this_.deal_info_type3 as dealInfoType3,"
				+ "workcertin6_.notafter as notafter,"
				+ "workcertin6_.notbefore as notbefore,"
				+ "this_.key_sn as keySn,"
				+ "this_.remarkInfo as remarkInfo,"
				+ "workcertin6_.sign_date as signDate,"
				+ "this_.add_cert_days as addCertDays,"
				+ "this_.year as year ," + "this_.last_days as lastDays,"
				+ "this_.deal_info_status as dealInfoStatus,"
				+ "workCertApplyInfo7_.name as certApplyInfoName  ");
		sql.append("  from work_deal_info this_ ");
		// sql.append(" inner join sys_user createby3_ on this_.create_by=createby3_.id  left outer join sys_office office10_ on createby3_.company_id=office10_.id ");
		// sql.append("left outer join sys_area area11_ on office10_.area_id=area11_.id ");
		// sql.append("inner join sys_office office4_ on createby3_.office_id=office4_.id ");
		sql.append("inner join config_app configapp5_ on this_.app_id=configapp5_.id ");
		sql.append("left outer join config_project_type configproj14_ on configapp5_.config_product_type=configproj14_.id ");
		sql.append("inner join config_product configprod7_ on this_.product_id=configprod7_.id ");
		sql.append("left outer join config_app configapp16_ on configprod7_.app_id=configapp16_.id ");
		sql.append("inner join work_cert_info workcertin6_ on this_.cert_id=workcertin6_.id ");
		sql.append("inner join work_cert_apply_info workCertApplyInfo7_ on workcertin6_.apply_info=workCertApplyInfo7_.id ");
		sql.append(" inner join work_company workcompan2_ on this_.work_company_id=workcompan2_.id ");
		sql.append("left outer join work_pay_info workpayinf19_ on this_.pay_id=workpayinf19_.id ");
		sql.append("inner join work_user workuser1_ on this_.work_user_id=workuser1_.id ");
		if ("".equals(sqls)) {
			sql.append("where (this_.is_ixin is not null or 1=1) and this_.del_flag=0");
		} else {
			sql.append("where (this_.is_ixin is not null or (this_." + sqls
					+ ")) and this_.del_flag=0");
		}

		if (apply != null) {
			sql.append(" and configapp5_.id = " + apply);
		}
		if (startTime != null) {
			sql.append(" and workcertin6_.notafter >= TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");

		}

		if (endTime != null) {
			sql.append(" and workcertin6_.notafter <= TO_DATE ('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}

		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				sql.append(" and workcompan2_.id="
						+ workDealInfo.getWorkCompany().getId());
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				sql.append(" and workcompan2_.company_name like '%"
						+ workDealInfo.getWorkCompany().getCompanyName() + "%'");
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber() != null
					&& !workDealInfo.getWorkCompany().getOrganizationNumber()
							.equals("")) {
				sql.append(" and workcompan2_.organization_number = '"
						+ workDealInfo.getWorkCompany().getOrganizationNumber()
						+ "'");
			}
		}
		
		
		if(workDealInfo.getConfigProduct()!=null){
			if(workDealInfo.getConfigProduct().getProductLabel()!=null){
				sql.append(" and configprod7_.product_label ="
						+ workDealInfo.getConfigProduct().getProductLabel());
				
				
			}
		}
		

		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				sql.append(" and workuser1_.contact_name like '%"
						+ EscapeUtil.escapeLike(workDealInfo.getWorkUser()
								.getContactName()) + "%'");
			}
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			sql.append(" and this_.deal_info_status = "
					+ workDealInfo.getDealInfoStatus());
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			sql.append(" and this_.key_sn like '%"
					+ EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%'");
		}
		
		
		if (StringUtils.isNotEmpty(workDealInfo.getRemarkInfo())) {
			sql.append(" and this_.remarkinfo like '%"
					+ EscapeUtil.escapeLike(workDealInfo.getRemarkInfo()) + "%'");
		}
		
		
		if (workDealInfo.getStatus() != null) {
			sql.append(" and this_.status = " + workDealInfo.getStatus());
		}
		if (productName != null && !"".equals(productName)) {
			sql.append(" and configprod7_.product_name = " + productName);
		}
		if (makeCertStartTime != null && makeCertEndTime != null) {
			sql.append(" and workcertin6_.sign_date >= TO_DATE('"
					+ DateUtils.formatDate(makeCertStartTime,
							"yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			sql.append(" and workcertin6_.sign_date <= TO_DATE ('"
					+ DateUtils.formatDate(makeCertEndTime,
							"yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}
		sql.append(" order by this_.create_date desc,this_.svn asc)");
		return sql.toString();
	}

	public List<WorkDealInfo> find4ApplyIsIxin(WorkDealInfo workDealInfo,
			Date startTime, Date endTime, Long apply) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");

		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		// dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
		// "createBy", UserUtils.getUser())));

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}

		if (startTime != null && endTime != null) {
			dc.add(Restrictions.ge("notafter", startTime));
			dc.add(Restrictions.le("notafter", endTime));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber() != null
					&& !workDealInfo.getWorkCompany().getOrganizationNumber()
							.equals("")) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany()
								.getOrganizationNumber())));

			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}

		dc.add(Restrictions.isNotNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(dc);
	}

	public Page<WorkDealInfo> findByBatchAdd(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, Date startTime, Date endTime,
			Long agentId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("workCompany", "workCompany");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("createDate", startTime));
		}
		if (endTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			dc.add(Restrictions.le("createDate", endTime));
		}

		if (agentId != null) {
			dc.add(Restrictions.le("configChargeAgentId", agentId));
		}

		dc.add(Restrictions.eq("isSJQY", 2));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_APPROVE_WAIT));
		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findPage4CertList(Page<WorkDealInfo> page,
			Long apply, Date startTime, Date endTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		// dc.createAlias("workUser", "workUser");
		// dc.createAlias("workCompany", "workCompany");
		// dc.createAlias("createBy", "createBy");
		// dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.createAlias("dealInfoType"," dealInfoType");
		// dc.add(Restrictions.ne("dealInfoType",
		// WorkDealInfoType.TYPE_UNLOCK_CERT));
		// dc.add(Restrictions.ne("dealInfoType",
		// WorkDealInfoType.TYPE_PAY_REPLACED));

		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		dc.add(Restrictions.in("dealInfoStatus", new String[] {
				WorkDealInfoStatus.STATUS_CERT_REVOKE,
				WorkDealInfoStatus.STATUS_CERT_OBTAINED }));

		// dc.add(Restrictions.eq("dealInfoType",
		// WorkDealInfoType.WorkDealInfoTypeMapReplaced));
		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		// workcertinfo 在此处报流关闭错误，此处使用业务的createDate
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		/*
		 * if (certInfoList.size() > 0) { dc.add(Restrictions.in("workCertInfo",
		 * certInfoList)); } else { dc.add(Restrictions.eq("id", -1L)); //
		 * 其实取回来时空，为了过滤数据范围 }
		 */
		// i信和非i信都统计
		// dc.add(Restrictions.isNull("isIxin"));
		// 更新和更新之前的新增都要统计，因为更新把新增的业务链给标志delete了，所以去掉这个约束条件，统一由
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED来判断

		// 不统计吊销 变更缴费类型 退费 业务
		dc.add(Restrictions.or(Restrictions.and(
				Restrictions.ne("dealInfoType", 10),
				Restrictions.ne("dealInfoType", 11),
				Restrictions.ne("dealInfoType", 12)), Restrictions
				.isNull("dealInfoType")));
		dc.add(Restrictions.isNull("dealInfoType3"));

		// dc.add(Restrictions.or(Restrictions.ne("dealInfoType", 10),
		// Restrictions.ne("dealInfoType", 11),
		// Restrictions.ne("dealInfoType", 12),
		// Restrictions.isNull("dealInfoType")));
		// dc.add(Restrictions.ne("dealInfoType", 10));
		// dc.add(Restrictions.ne("dealInfoType", 11));
		// dc.add(Restrictions.ne("dealInfoType", 12));
		dc.add(Restrictions.in(WorkDealInfo.DEL_FLAG, new String[] {
				WorkDealInfo.DEL_FLAG_NORMAL, WorkDealInfo.DEL_FLAG_DELETE }));
		dc.addOrder(Order.desc("createDate"));

		return workDealInfoDao.find(page, dc);
	}

	public List<WorkDealInfo> findPage4CertListAll(Long apply, Date startTime,
			Date endTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		// dc.createAlias("workUser", "workUser");
		// dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");

		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(Restrictions.in("dealInfoStatus", new String[] {
				WorkDealInfoStatus.STATUS_CERT_REVOKE,
				WorkDealInfoStatus.STATUS_CERT_OBTAINED }));

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}

		// workcertinfo 在此处报流关闭错误，此处使用业务的createDate
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		// i信和非i信都统计
		// dc.add(Restrictions.isNull("isIxin"));
		// 更新和更新之前的新增都要统计，因为更新把新增的业务链给标志delete了，所以去掉这个约束条件，统一由
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED来判断
		dc.add(Restrictions.in(WorkDealInfo.DEL_FLAG, new String[] {
				WorkDealInfo.DEL_FLAG_NORMAL, WorkDealInfo.DEL_FLAG_DELETE }));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> find4CertList(Long apply, Date startTime,
			Date endTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		// dc.createAlias("workUser", "workUser");
		// dc.createAlias("workCompany", "workCompany");
		// dc.createAlias("createBy", "createBy");
		// dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.createAlias("dealInfoType"," dealInfoType");
		// dc.add(Restrictions.ne("dealInfoType",
		// WorkDealInfoType.TYPE_UNLOCK_CERT));
		// dc.add(Restrictions.ne("dealInfoType",
		// WorkDealInfoType.TYPE_PAY_REPLACED));

		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		dc.add(Restrictions.in("dealInfoStatus", new String[] {
				WorkDealInfoStatus.STATUS_CERT_REVOKE,
				WorkDealInfoStatus.STATUS_CERT_OBTAINED }));

		// dc.add(Restrictions.eq("dealInfoType",
		// WorkDealInfoType.WorkDealInfoTypeMapReplaced));
		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		// workcertinfo 在此处报流关闭错误，此处使用业务的createDate
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		/*
		 * if (certInfoList.size() > 0) { dc.add(Restrictions.in("workCertInfo",
		 * certInfoList)); } else { dc.add(Restrictions.eq("id", -1L)); //
		 * 其实取回来时空，为了过滤数据范围 }
		 */
		// i信和非i信都统计
		// dc.add(Restrictions.isNull("isIxin"));
		// 更新和更新之前的新增都要统计，因为更新把新增的业务链给标志delete了，所以去掉这个约束条件，统一由
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED来判断

		// 不统计吊销 变更缴费类型 退费 业务
		dc.add(Restrictions.or(Restrictions.and(
				Restrictions.ne("dealInfoType", 10),
				Restrictions.ne("dealInfoType", 11),
				Restrictions.ne("dealInfoType", 12)), Restrictions
				.isNull("dealInfoType")));
		dc.add(Restrictions.isNull("dealInfoType3"));

		// dc.add(Restrictions.or(Restrictions.ne("dealInfoType", 10),
		// Restrictions.ne("dealInfoType", 11),
		// Restrictions.ne("dealInfoType", 12),
		// Restrictions.isNull("dealInfoType")));
		// dc.add(Restrictions.ne("dealInfoType", 10));
		// dc.add(Restrictions.ne("dealInfoType", 11));
		// dc.add(Restrictions.ne("dealInfoType", 12));
		dc.add(Restrictions.in(WorkDealInfo.DEL_FLAG, new String[] {
				WorkDealInfo.DEL_FLAG_NORMAL, WorkDealInfo.DEL_FLAG_DELETE }));
		dc.addOrder(Order.desc("createDate"));

		return workDealInfoDao.find(dc);
	}

	public Page<WorkDealInfo> find(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, List<Long> officeIds, Long apply,
			String certType, Integer workType, Integer year,
			Date luruStartTime, Date luruEndTime, List<Long> offices,
			Date daoqiStartTime, Date daoqiEndTime, Date paymentStartTime,
			Date paymentEndTime, Date zhizhengStartTime, Date zhizhengEndTime

	) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("updateBy", "updateBy");

		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");

		if (workDealInfo.getInputUser() != null
				&& workDealInfo.getInputUser().getName() != null) {
			if (workDealInfo.getInputUser().getName().trim().replace(" ", "")
					.equals("客户端")) {

				dc.add(Restrictions.isNull("inputUser"));
			} else if (!workDealInfo.getInputUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("inputUser", "inputUser");
				dc.add(Restrictions.like("inputUser.name", "%"
						+ workDealInfo.getInputUser().getName() + "%"));
			}
		}

		// 此条件只能查出在客户端制证的业务,但并不包含全部的客户端业务，因为有的客户端业务是通过平台制证的
		if (workDealInfo.getBusinessCardUser() != null
				&& workDealInfo.getBusinessCardUser().getName() != null) {
			if (workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("businessCardUser"));
			} else if (!workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("businessCardUser", "businessCardUser");
				dc.add(Restrictions.like("businessCardUser.name", "%"
						+ workDealInfo.getBusinessCardUser().getName() + "%"));
			}
		}

		dc.add(Restrictions.in("officeId", offices));

		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + workDealInfo.getKeySn()
					+ "%"));
		}

		if (workDealInfo.getPayType() != null && workDealInfo.getPayType() != 0) {
			dc.add(Restrictions.eq("payType", workDealInfo.getPayType()));
		}

		if (workDealInfo.getAttestationUser() != null
				&& !workDealInfo.getAttestationUser().getName().equals("")) {
			dc.createAlias("attestationUser", "attestationUser");
			dc.add(Restrictions.like("attestationUser.name", "%"
					+ workDealInfo.getAttestationUser().getName() + "%"));
		}

		// workCompany.province
		// workCompany.city
		// workCompany.district
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo
					.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo
					.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo
					.getWorkCompany().getDistrict()));
		}

		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodMoney", true),
						Restrictions.eq("workPayInfo.relationMethod", 0)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodPos", true),
						Restrictions.eq("workPayInfo.relationMethod", 1)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodBank", true),
						Restrictions.eq("workPayInfo.relationMethod", 2)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodAlipay", true),
						Restrictions.eq("workPayInfo.relationMethod", 3)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		if (paymentStartTime != null) {
			dc.add(Restrictions.ge("payUserDate", paymentStartTime));
		}
		if (paymentEndTime != null) {
			paymentEndTime.setHours(23);
			paymentEndTime.setMinutes(59);
			paymentEndTime.setSeconds(59);
			dc.add(Restrictions.le("payUserDate", paymentEndTime));
		}

		if (luruStartTime != null) {
			dc.add(Restrictions.ge("inputUserDate", luruStartTime));
		}
		if (luruEndTime != null) {
			luruEndTime.setHours(23);
			luruEndTime.setMinutes(59);
			luruEndTime.setSeconds(59);
			dc.add(Restrictions.le("inputUserDate", luruEndTime));
		}

		if (daoqiStartTime != null) {
			dc.add(Restrictions.ge("notafter", daoqiStartTime));
		}
		if (daoqiEndTime != null) {
			daoqiEndTime.setHours(23);
			daoqiEndTime.setMinutes(59);
			daoqiEndTime.setSeconds(59);
			dc.add(Restrictions.le("notafter", daoqiEndTime));
		}

		if (zhizhengStartTime != null) {
			dc.add(Restrictions.ge("businessCardUserDate", zhizhengStartTime));
		}

		if (zhizhengEndTime != null) {
			Calendar zhizhengEndCalendar = Calendar.getInstance();
			zhizhengEndCalendar.setTime(zhizhengEndTime);
			zhizhengEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
			zhizhengEndCalendar.set(Calendar.MINUTE, 59);
			zhizhengEndCalendar.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate",
					zhizhengEndCalendar.getTime()));
		}

		if (officeIds != null && officeIds.size() > 0) {
			dc.add(Restrictions.in("officeId", officeIds));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (certType != null && !certType.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", certType));
		}

		if (workType != null) {

			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workType),
					Restrictions.eq("dealInfoType1", workType),
					Restrictions.eq("dealInfoType2", workType),
					Restrictions.eq("dealInfoType3", workType)));
		}

		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));

		return workDealInfoDao.find(page, dc);

	}

	public Page<WorkDealInfo> findCX(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, List<Long> officeIds, Long apply,
			String certType, Integer workType, Integer year,
			Date luruStartTime, Date luruEndTime, List<Long> offices,
			Date daoqiStartTime, Date daoqiEndTime, Date paymentStartTime,
			Date paymentEndTime, Date zhizhengStartTime, Date zhizhengEndTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("updateBy", "updateBy");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.in("officeId", offices));

		if (workDealInfo.getInputUser() != null
				&& workDealInfo.getInputUser().getName() != null) {
			if (workDealInfo.getInputUser().getName().trim().replace(" ", "")
					.equals("客户端")) {

				dc.add(Restrictions.isNull("inputUser"));
			} else if (!workDealInfo.getInputUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("inputUser", "inputUser");
				dc.add(Restrictions.like("inputUser.name", "%"
						+ workDealInfo.getInputUser().getName() + "%"));
			}
		}

		// 此条件只能查出在客户端制证的业务,但并不包含全部的客户端业务，因为有的客户端业务是通过平台制证的
		if (workDealInfo.getBusinessCardUser() != null
				&& workDealInfo.getBusinessCardUser().getName() != null) {
			if (workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("businessCardUser"));
			} else if (!workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("businessCardUser", "businessCardUser");
				dc.add(Restrictions.like("businessCardUser.name", "%"
						+ workDealInfo.getBusinessCardUser().getName() + "%"));
			}
		}

		// workUser.contactName
		// workUser.conCertNumber
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + workDealInfo.getKeySn()
					+ "%"));
		}

		if (workDealInfo.getPayType() != null && workDealInfo.getPayType() != 0) {
			dc.add(Restrictions.eq("payType", workDealInfo.getPayType()));
		}

		if (workDealInfo.getAttestationUser() != null
				&& !workDealInfo.getAttestationUser().getName().equals("")) {
			dc.createAlias("attestationUser", "attestationUser");
			dc.add(Restrictions.like("attestationUser.name", "%"
					+ workDealInfo.getAttestationUser().getName() + "%"));
		}

		// workCompany.province
		// workCompany.city
		// workCompany.district
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo
					.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo
					.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo
					.getWorkCompany().getDistrict()));
		}

		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodMoney", true),
						Restrictions.eq("workPayInfo.relationMethod", 0)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodPos", true),
						Restrictions.eq("workPayInfo.relationMethod", 1)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodBank", true),
						Restrictions.eq("workPayInfo.relationMethod", 2)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodAlipay", true),
						Restrictions.eq("workPayInfo.relationMethod", 3)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		// 查询条件由之前的鉴证时间改为缴费时间
		if (paymentStartTime != null) {
			dc.add(Restrictions.ge("payUserDate", paymentStartTime));
		}
		if (paymentEndTime != null) {
			paymentEndTime.setHours(23);
			paymentEndTime.setMinutes(59);
			paymentEndTime.setSeconds(59);
			dc.add(Restrictions.le("payUserDate", paymentEndTime));
		}

		if (luruStartTime != null) {
			dc.add(Restrictions.ge("inputUserDate", luruStartTime));
		}
		if (luruEndTime != null) {
			luruEndTime.setHours(23);
			luruEndTime.setMinutes(59);
			luruEndTime.setSeconds(59);
			dc.add(Restrictions.le("inputUserDate", luruEndTime));
		}

		if (daoqiStartTime != null) {
			dc.add(Restrictions.ge("notafter", daoqiStartTime));
		}
		if (daoqiEndTime != null) {
			daoqiEndTime.setHours(23);
			daoqiEndTime.setMinutes(59);
			daoqiEndTime.setSeconds(59);
			dc.add(Restrictions.le("notafter", daoqiEndTime));
		}

		if (zhizhengStartTime != null) {
			dc.add(Restrictions.ge("businessCardUserDate", zhizhengStartTime));
		}

		if (zhizhengEndTime != null) {
			Calendar zhizhengEndCalendar = Calendar.getInstance();
			zhizhengEndCalendar.setTime(zhizhengEndTime);
			zhizhengEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
			zhizhengEndCalendar.set(Calendar.MINUTE, 59);
			zhizhengEndCalendar.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate",
					zhizhengEndCalendar.getTime()));
		}

		if (officeIds != null && officeIds.size() > 0) {
			dc.add(Restrictions.in("officeId", officeIds));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (certType != null && !certType.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", certType));
		}

		if (workType != null) {

			dc.add(Restrictions.or(Restrictions.ne("dealInfoType", 11),
					Restrictions.isNull("dealInfoType")));
		}

		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE)));

		return workDealInfoDao.find(page, dc);

	}

	public List<WorkDealInfo> findCX(WorkDealInfo workDealInfo,
			List<Long> officeIds, Long apply, String certType,
			Integer workType, Integer year, Date luruStartTime,
			Date luruEndTime, List<Long> offices, Date daoqiStartTime,
			Date daoqiEndTime, Date paymentStartTime, Date paymentEndTime,
			Date zhizhengStartTime, Date zhizhengEndTime

	) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("updateBy", "updateBy");
		// dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		// workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.in("officeId", offices));

		if (workDealInfo.getInputUser() != null
				&& workDealInfo.getInputUser().getName() != null) {
			if (workDealInfo.getInputUser().getName().trim().replace(" ", "")
					.equals("客户端")) {

				dc.add(Restrictions.isNull("inputUser"));
			} else if (!workDealInfo.getInputUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("inputUser", "inputUser");
				dc.add(Restrictions.like("inputUser.name", "%"
						+ workDealInfo.getInputUser().getName() + "%"));
			}
		}

		// 此条件只能查出在客户端制证的业务,但并不包含全部的客户端业务，因为有的客户端业务是通过平台制证的
		if (workDealInfo.getBusinessCardUser() != null
				&& workDealInfo.getBusinessCardUser().getName() != null) {
			if (workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("businessCardUser"));
			} else if (!workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("businessCardUser", "businessCardUser");
				dc.add(Restrictions.like("businessCardUser.name", "%"
						+ workDealInfo.getBusinessCardUser().getName() + "%"));
			}
		}

		// workUser.contactName
		// workUser.conCertNumber
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + workDealInfo.getKeySn()
					+ "%"));
		}

		if (workDealInfo.getPayType() != null && workDealInfo.getPayType() != 0) {
			dc.add(Restrictions.eq("payType", workDealInfo.getPayType()));
		}

		if (workDealInfo.getAttestationUser() != null
				&& !workDealInfo.getAttestationUser().getName().equals("")) {
			dc.createAlias("attestationUser", "attestationUser");
			dc.add(Restrictions.like("attestationUser.name", "%"
					+ workDealInfo.getAttestationUser().getName() + "%"));
		}

		// workCompany.province
		// workCompany.city
		// workCompany.district
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo
					.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo
					.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo
					.getWorkCompany().getDistrict()));
		}

		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodMoney", true),
						Restrictions.eq("workPayInfo.relationMethod", 0)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodPos", true),
						Restrictions.eq("workPayInfo.relationMethod", 1)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodBank", true),
						Restrictions.eq("workPayInfo.relationMethod", 2)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodAlipay", true),
						Restrictions.eq("workPayInfo.relationMethod", 3)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		// 查询条件由之前的鉴证时间改为缴费时间
		if (paymentStartTime != null) {
			dc.add(Restrictions.ge("payUserDate", paymentStartTime));
		}
		if (paymentEndTime != null) {
			paymentEndTime.setHours(23);
			paymentEndTime.setMinutes(59);
			paymentEndTime.setSeconds(59);
			dc.add(Restrictions.le("payUserDate", paymentEndTime));
		}

		if (luruStartTime != null) {
			dc.add(Restrictions.ge("inputUserDate", luruStartTime));
		}
		if (luruEndTime != null) {
			luruEndTime.setHours(23);
			luruEndTime.setMinutes(59);
			luruEndTime.setSeconds(59);
			dc.add(Restrictions.le("inputUserDate", luruEndTime));
		}

		if (daoqiStartTime != null) {
			dc.add(Restrictions.ge("notafter", daoqiStartTime));
		}
		if (daoqiEndTime != null) {
			daoqiEndTime.setHours(23);
			daoqiEndTime.setMinutes(59);
			daoqiEndTime.setSeconds(59);
			dc.add(Restrictions.le("notafter", daoqiEndTime));
		}

		if (zhizhengStartTime != null) {
			dc.add(Restrictions.ge("businessCardUserDate", zhizhengStartTime));
		}

		if (zhizhengEndTime != null) {
			Calendar zhizhengEndCalendar = Calendar.getInstance();
			zhizhengEndCalendar.setTime(zhizhengEndTime);
			zhizhengEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
			zhizhengEndCalendar.set(Calendar.MINUTE, 59);
			zhizhengEndCalendar.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate",
					zhizhengEndCalendar.getTime()));
		}

		if (officeIds != null && officeIds.size() > 0) {
			dc.add(Restrictions.in("officeId", officeIds));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (certType != null && !certType.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", certType));
		}

		if (workType != null) {

			dc.add(Restrictions.or(Restrictions.ne("dealInfoType", 11),
					Restrictions.isNull("dealInfoType")));

			/* dc.add(Restrictions.ne("dealInfoType", 11)); */
		}

		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		// ProjectionList projectionList1 = Projections.projectionList();
		// projectionList1.add(Projections.sqlGroupProjection("dealInfoType",
		// "dealInfoType", null, null));

		return workDealInfoDao.find(dc);

	}

	public List<WorkDealInfo> find(WorkDealInfo workDealInfo,
			List<Long> officeIds, Long apply, String certType,
			Integer workType, Integer year, Date luruStartTime,
			Date luruEndTime, List<Long> offices, Date daoqiStartTime,
			Date daoqiEndTime, Date paymentStartTime, Date paymentEndTime,
			Date zhizhengStartTime, Date zhizhengEndTime

	) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("updateBy", "updateBy");

		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");

		dc.add(Restrictions.in("officeId", offices));

		if (workDealInfo.getInputUser() != null
				&& workDealInfo.getInputUser().getName() != null) {
			if (workDealInfo.getInputUser().getName().trim().replace(" ", "")
					.equals("客户端")) {

				dc.add(Restrictions.isNull("inputUser"));
			} else if (!workDealInfo.getInputUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("inputUser", "inputUser");
				dc.add(Restrictions.like("inputUser.name", "%"
						+ workDealInfo.getInputUser().getName() + "%"));
			}
		}

		// 此条件只能查出在客户端制证的业务,但并不包含全部的客户端业务，因为有的客户端业务是通过平台制证的
		if (workDealInfo.getBusinessCardUser() != null
				&& workDealInfo.getBusinessCardUser().getName() != null) {
			if (workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("客户端")) {
				dc.add(Restrictions.isNull("businessCardUser"));
			} else if (!workDealInfo.getBusinessCardUser().getName().trim()
					.replace(" ", "").equals("")) {
				dc.createAlias("businessCardUser", "businessCardUser");
				dc.add(Restrictions.like("businessCardUser.name", "%"
						+ workDealInfo.getBusinessCardUser().getName() + "%"));
			}
		}

		// workUser.contactName
		// workUser.conCertNumber
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + workDealInfo.getKeySn()
					+ "%"));
		}

		if (workDealInfo.getPayType() != null && workDealInfo.getPayType() != 0) {
			dc.add(Restrictions.eq("payType", workDealInfo.getPayType()));
		}

		if (workDealInfo.getAttestationUser() != null
				&& !workDealInfo.getAttestationUser().getName().equals("")) {
			dc.createAlias("attestationUser", "attestationUser");
			dc.add(Restrictions.like("attestationUser.name", "%"
					+ workDealInfo.getAttestationUser().getName() + "%"));
		}

		// workCompany.province
		// workCompany.city
		// workCompany.district
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo
					.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo
					.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo
					.getWorkCompany().getDistrict()));
		}

		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodMoney", true),
						Restrictions.eq("workPayInfo.relationMethod", 0)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodPos", true),
						Restrictions.eq("workPayInfo.relationMethod", 1)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodBank", true),
						Restrictions.eq("workPayInfo.relationMethod", 2)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(
						Restrictions.eq("workPayInfo.methodAlipay", true),
						Restrictions.eq("workPayInfo.relationMethod", 3)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		if (paymentStartTime != null) {
			dc.add(Restrictions.ge("payUserDate", paymentStartTime));
		}
		if (paymentEndTime != null) {
			paymentEndTime.setHours(23);
			paymentEndTime.setMinutes(59);
			paymentEndTime.setSeconds(59);
			dc.add(Restrictions.le("payUserDate", paymentEndTime));
		}

		if (luruStartTime != null) {
			dc.add(Restrictions.ge("inputUserDate", luruStartTime));
		}
		if (luruEndTime != null) {
			luruEndTime.setHours(23);
			luruEndTime.setMinutes(59);
			luruEndTime.setSeconds(59);
			dc.add(Restrictions.le("inputUserDate", luruEndTime));
		}

		if (daoqiStartTime != null) {
			dc.add(Restrictions.ge("notafter", daoqiStartTime));
		}
		if (daoqiEndTime != null) {
			daoqiEndTime.setHours(23);
			daoqiEndTime.setMinutes(59);
			daoqiEndTime.setSeconds(59);
			dc.add(Restrictions.le("notafter", daoqiEndTime));
		}

		if (zhizhengStartTime != null) {
			dc.add(Restrictions.ge("businessCardUserDate", zhizhengStartTime));
		}

		if (zhizhengEndTime != null) {
			Calendar zhizhengEndCalendar = Calendar.getInstance();
			zhizhengEndCalendar.setTime(zhizhengEndTime);
			zhizhengEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
			zhizhengEndCalendar.set(Calendar.MINUTE, 59);
			zhizhengEndCalendar.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate",
					zhizhengEndCalendar.getTime()));
		}

		if (officeIds != null && officeIds.size() > 0) {
			dc.add(Restrictions.in("officeId", officeIds));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (certType != null && !certType.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", certType));
		}

		if (workType != null) {
			// dc.add(Restrictions.eq("dealInfoType", workType));
			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workType),
					Restrictions.eq("dealInfoType1", workType),
					Restrictions.eq("dealInfoType2", workType),
					Restrictions.eq("dealInfoType3", workType)));
		}

		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		// ProjectionList projectionList1 = Projections.projectionList();
		// projectionList1.add(Projections.sqlGroupProjection("dealInfoType",
		// "dealInfoType", null, null));

		return workDealInfoDao.find(dc);

	}

	public Page<WorkDealInfo> find14(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, Long area, Long office, Long apply,
			Integer workType, List<WorkCertInfo> certInfoList,
			Date makeCertStart, Date makeCertEnd, Date expiredStart,
			Date expiredEnd) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		//dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");

		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo
					.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo
					.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo
					.getWorkCompany().getDistrict()));
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}

		if (office != null) {
			dc.add(Restrictions.eq("office.id", office));
		} else if (area != null) {

			dc.add(Restrictions.eq("workCompany.id", area));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}

		if (workType != null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workType),
					Restrictions.eq("dealInfoType1", workType),
					Restrictions.eq("dealInfoType2", workType),
					Restrictions.eq("dealInfoType3", workType)));

		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		// dc.add(Restrictions.isNotNull("isIxin"));
		// dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
		// WorkDealInfo.DEL_FLAG_NORMAL));
		// dc.add(Restrictions.eq("dealInfoStatus",
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		// ProjectionList projectionList1 = Projections.projectionList();
		// projectionList1.add(Projections.sqlGroupProjection("dealInfoType",
		// "dealInfoType", null, null));

		if (makeCertStart != null) {
			makeCertStart.setHours(0);
			makeCertStart.setMinutes(0);
			makeCertStart.setSeconds(0);
			dc.add(Restrictions.ge("businessCardUserDate", makeCertStart));
		}

		if (makeCertEnd != null) {
			makeCertEnd.setHours(23);
			makeCertEnd.setMinutes(59);
			makeCertEnd.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", makeCertEnd));
		}

		if (expiredStart != null) {
			expiredStart.setHours(0);
			expiredStart.setMinutes(0);
			expiredStart.setSeconds(0);
			dc.add(Restrictions.ge("notafter", expiredStart));
		}

		if (expiredEnd != null) {
			expiredEnd.setHours(23);
			expiredEnd.setMinutes(59);
			expiredEnd.setSeconds(59);
			dc.add(Restrictions.le("notafter", expiredEnd));
		}
		
		
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));

		return workDealInfoDao.find(page, dc);

	}

	public List<WorkDealInfo> find14(WorkDealInfo workDealInfo, Long area,
			Long office, Long apply, Integer workType, Date makeCertStart,
			Date makeCertEnd, Date expiredStart, Date expiredEnd) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		//dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");

		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo
					.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo
					.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo
					.getWorkCompany().getDistrict()));
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}

		if (office != null) {
			dc.add(Restrictions.eq("office.id", office));
		} else if (area != null) {

			dc.add(Restrictions.eq("workCompany.id", area));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}

		if (workType != null) {
			dc.add(Restrictions.eq("dealInfoType", workType));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}

		if (makeCertStart != null) {

			Calendar cal = Calendar.getInstance();

			cal.setTime(makeCertStart);

			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

			dc.add(Restrictions.ge("businessCardUserDate", cal.getTime()));
		}

		if (makeCertEnd != null) {

			Calendar cal = Calendar.getInstance();

			cal.setTime(makeCertEnd);

			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("businessCardUserDate", cal.getTime()));
		}

		if (expiredStart != null) {

			Calendar cal = Calendar.getInstance();

			cal.setTime(expiredStart);

			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

			dc.add(Restrictions.ge("notafter", cal.getTime()));
		}

		if (expiredEnd != null) {

			Calendar cal = Calendar.getInstance();

			cal.setTime(expiredEnd);

			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);

			dc.add(Restrictions.le("notafter", cal.getTime()));
		}
		
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));

		return workDealInfoDao.find(dc);

	}

	public List<WorkDealInfo> find14A(WorkDealInfo workDealInfo, Long area,
			Long office, Long apply, Integer workType,
			List<WorkCertInfo> certInfoList

	) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");

		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany()
						.getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber", "%"
					+ workDealInfo.getWorkCompany().getOrganizationNumber()
					+ "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%"
					+ workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser()
						.getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber", "%"
					+ workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		if (office != null) {
			dc.add(Restrictions.eq("office.id", office));
		} else if (area != null) {

			dc.add(Restrictions.eq("workCompany.id", area));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}

		if (workType != null) {
			dc.add(Restrictions.eq("dealInfoType", workType));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		// dc.add(Restrictions.isNotNull("isIxin"));
		// dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
		// WorkDealInfo.DEL_FLAG_NORMAL));
		// dc.add(Restrictions.eq("dealInfoStatus",
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		// ProjectionList projectionList1 = Projections.projectionList();
		// projectionList1.add(Projections.sqlGroupProjection("dealInfoType",
		// "dealInfoType", null, null));

		return workDealInfoDao.find(dc);

	}

	public Page<WorkDealInfo> findCustomer(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, Date startTime, Date endTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workCompany", "workCompany");

		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");

		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (startTime != null && endTime != null) {
			dc.add(Restrictions.ge("notafter", startTime));
			dc.add(Restrictions.le("notafter", endTime));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany",
						workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
			if (workDealInfo.getWorkCompany().getCompanyMobile() != null
					&& !workDealInfo.getWorkCompany().getCompanyMobile()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyMobile",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyMobile())
								+ "%"));
			}
		}
		if (workDealInfo.getConfigApp() != null) {
			if (workDealInfo.getConfigApp().getAppName() != null
					&& !workDealInfo.getConfigApp().getAppName().equals("")) {
				dc.add(Restrictions.eq("configApp.appName", workDealInfo
						.getConfigApp().getAppName()));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactName()) + "%"));
			}
			/*
			 * if (workDealInfo.getWorkUser().getContactPhone() != null &&
			 * !workDealInfo.getWorkUser().getContactPhone().equals("")) {
			 * dc.add(Restrictions.like("workUser.contactPhone", "%" +
			 * workDealInfo.getWorkUser().getContactPhone() + "%")); }
			 */
			if (workDealInfo.getWorkUser().getContactEmail() != null
					&& !workDealInfo.getWorkUser().getContactEmail().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactEmail",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactEmail()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like(
						"workUser.contactTel",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		
		//dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn",
					"%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	// 根据支付信息查询绑定的支付信息
	public Page<WorkDealInfo> findByFinanceId(Page<WorkDealInfo> page,
			List<Long> idList) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (idList.size() > 0) {
			dc.add(Restrictions.in("workPayInfo.id", idList));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public List<WorkDealInfo> findByFinanceId(List<Long> idList) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (idList.size() > 0) {
			dc.add(Restrictions.in("workPayInfo.id", idList));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据Keysn序列号，查询统计信息
	public List<WorkDealInfo> findByKeySn(String Keysn, int dealInfoType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}
		if (StringUtils.isNotEmpty(dealInfoType + "")) {
			dc.add(Restrictions.eq("dealInfoType", dealInfoType));
		}
		dc.add(Restrictions.eq("dealInfoStatus", "0"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findByKeySn(String Keysn, String certSn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}
		if (certSn != null) {
			dc.add(Restrictions.like("certSn",
					"%" + EscapeUtil.escapeLike(certSn) + "%"));
		}
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public Integer findByKey(String Keysn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}

		// a.DEAL_INFO_STATUS='6';
		/*
		 * dc.add(Restrictions.ne("dealInfoStatus",
		 * WorkDealInfoStatus.STATUS_CERT_REVOKE));
		 */
		// dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
		// WorkDealInfo.DEL_FLAG_NORMAL));
		if (workDealInfoDao.find(dc).size() > 0) {
			return workDealInfoDao.find(dc).size();
		} else {
			return 0;
		}
	}

	public Integer findByKey(String Keysn, Long id) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.ne("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE));
		// dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
		// WorkDealInfo.DEL_FLAG_NORMAL));
		if (workDealInfoDao.find(dc).size() > 0) {
			return workDealInfoDao.find(dc).size();
		} else {
			return 0;
		}
	}

	public List<WorkDealInfo> findByKeySnWithOutDel(String Keysn, String certSn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}
		if (certSn != null) {
			dc.add(Restrictions.eq("certSn", certSn));
		}
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findByManyCondition(String status, Date date,
			Integer year, Integer type) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.ge("createDate", date));
		dc.add(Restrictions.eq("dealInfoStatus", status));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", type),
				Restrictions.or(Restrictions.eq("dealInfoType1", type),
						Restrictions.or(Restrictions.eq("dealInfoType2", type),
								Restrictions.eq("dealInfoType3", type)))));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据引用名称，查询统计信息
	public List<WorkDealInfo> findByAppName(String appName) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appName != null) {
			dc.add(Restrictions.like("configApp.appName",
					"%" + EscapeUtil.escapeLike(appName) + "%"));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据应用名称，查询统计信息
	public List<WorkDealInfo> findByAppId(Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public Integer findPrevIdIsNull(Integer appId) {

		String sql = "select count(FIRST_CERT_SN) CT from (";
		sql = sql
				+ "select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO where PREV_ID is null ";
		sql = sql + " and FIRST_CERT_SN is not null ";
		sql = sql + " and FIRST_CERT_SN!='0' ";
		if (appId != null && appId.longValue() > 0) {
			sql = sql + " and APP_ID=" + appId;
		}
		sql += " group by FIRST_CERT_SN";
		sql += " order by c desc";
		sql += ")  a where a.c>1";
		List<Map> m = null;
		try {
			m = workDealInfoDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exLog.error(StringHelper.getStackInfo(e));
		}
		if (m == null || m.size() <= 0)
			return 0;
		return new Integer(m.get(0).get("CT").toString());
	}

	/**
	 * 查找previd为空,业务链数据大于1条的记录
	 * 
	 * @param appId
	 * @param count
	 * @return List<String>
	 */
	public List<String> findPreIdIsNullFirstCertSnByAppId(Integer appId,
			Integer count) {
		String sql = "select distinct FIRST_CERT_SN,a.c from (";
		sql = sql
				+ "select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO where PREV_ID is null  ";
		sql = sql + " and FIRST_CERT_SN is not null";
		sql = sql + " and FIRST_CERT_SN!='0'";
		if (appId != null && appId.longValue() > 0) {
			sql = sql + " and APP_ID=" + appId;
		}
		sql += " group by FIRST_CERT_SN";
		sql += " order by c desc";
		sql += ")  a where a.c>1";
		if (count != null && count.intValue() > 0) {
			sql = sql + " and rownum<=" + count;
		}

		List<Map> lst = null;
		List<String> res = new ArrayList<String>();
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			for (Map e : lst) {
				if (e.get("FIRST_CERT_SN") == null
						|| e.get("FIRST_CERT_SN").toString().trim().length() <= 0)
					continue;
				res.add(e.get("FIRST_CERT_SN").toString());
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			e.printStackTrace();
			exLog.error(StringHelper.getStackInfo(e));
		}
		return res;
	}

	/**
	 * 找到首条记录和首证书不符的记录(认为prev_id为空的即为首条)
	 * 
	 * @param appId
	 * @param count
	 * @return
	 */
	public List<String> findFirstCertSnByAppIdAndNotEquals(Integer appId,
			Integer count) {
		String sql = "select FIRST_CERT_SN from WORK_DEAL_INFO ";
		sql += " PREV_ID is null and CERT_SN!=FIRST_CERT_SN AND CERT_SN!='00'||FIRST_CERT_SN ";
		if (appId != null && appId.longValue() > 0) {
			sql = sql + " and APP_ID=" + appId;
		}
		if (count != null && count.intValue() > 0) {
			sql = sql + " and rownum<=" + count;
		}

		List<Map> lst = null;
		List<String> res = new ArrayList<String>();
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			for (Map e : lst) {
				if (e.get("FIRST_CERT_SN") == null
						|| e.get("FIRST_CERT_SN").toString().trim().length() <= 0)
					continue;
				res.add(e.get("FIRST_CERT_SN").toString());
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查所有业务链中有多条新增的首证书序列号
	 * 
	 * @param appId
	 * @param count
	 * @return
	 */
	public List<String> findFirstCertSnByAppIdAndMutilAdd(Integer appId,
			Integer count) {
		String sql = "select FIRST_CERT_SN from ( ";
		sql += " SELECT FIRST_CERT_SN,DEAL_INFO_TYPE,count(*) ct FROM WORK_DEAL_INFO WHERE FIRST_CERT_SN IN  ( ";
		sql += " SELECT first_cert_sn FROM (";
		sql += " SELECT COUNT (*) c, FIRST_CERT_SN FROM WORK_DEAL_INFO WHERE ";
		sql += " FIRST_CERT_SN!='0' ";
		if (appId != null && appId.longValue() > 0) {
			sql = sql + " and APP_ID=" + appId;
		}
		sql += " GROUP BY FIRST_CERT_SN) ";
		sql += " WHERE c > 1 ";
		sql += " ) and DEAL_INFO_TYPE='0' group by FIRST_CERT_SN,DEAL_INFO_TYPE order by ct desc ";
		sql += " ) where ct>1";
		if (count != null && count.intValue() > 0) {
			sql = sql + " and rownum<=" + count;
		}

		List<Map> lst = null;
		List<String> res = new ArrayList<String>();
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			for (Map e : lst) {
				if (e.get("FIRST_CERT_SN") == null
						|| e.get("FIRST_CERT_SN").toString().trim().length() <= 0)
					continue;
				res.add(e.get("FIRST_CERT_SN").toString());
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查所有首条记录不是新增类型的首证书
	 * 
	 * @param appId
	 * @param count
	 * @return
	 */
	public List<String> findFirstCertSnByAppIdAndFirstDataIsNotAdd(
			Integer appId, Integer count) {
		String sql = "select distinct FIRST_CERT_SN from WORK_DEAL_INFO where  FIRST_CERT_SN is not null ";
		sql += " and (DEAL_INFO_TYPE!=0 OR DEAL_INFO_TYPE is NULL) and PREV_ID is null ";
		if (appId != null && appId.longValue() > 0) {
			sql = sql + " and APP_ID=" + appId;
		}
		if (count != null && count.intValue() > 0) {
			sql = sql + " and rownum<=" + count;
		}
		sql += " group by FIRST_CERT_SN";

		List<Map> lst = null;
		List<String> res = new ArrayList<String>();
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			for (Map e : lst) {
				if (e.get("FIRST_CERT_SN") == null
						|| e.get("FIRST_CERT_SN").toString().trim().length() <= 0)
					continue;
				res.add(e.get("FIRST_CERT_SN").toString());
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// 根据应用名称和业务类型为新增查出业务Id
	public List<Object> findIdByAppIdAndDealInfoType(Long appId) {

		String hql = "select id from WorkDealInfo where dealInfoType = 0 and delFlag = 1 ";
		if (appId != null && appId.longValue() > 0) {
			hql = hql + " and configApp=" + appId;
		}

		hql += " order by id";

		List<Object> lst = null;

		try {
			lst = workDealInfoDao.find(hql);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lst;
	}

	public List<com.itrus.ca.modules.task.vo.WorkDealInfoVo> findByPrevId(
			Long dealInfoId) {

		String hql = "select level, id,PREV_ID,FIRST_CERT_SN";
		hql += " from WORK_DEAL_INFO start with ";
		hql += "id=";
		hql += dealInfoId;
		hql += " connect by prior id = PREV_ID order by level asc";

		/*
		 * List<WorkDealInfo> list = workDealInfoDao.find(hql);
		 * 
		 * return list;
		 */
		List<Map> lst = null;
		List<com.itrus.ca.modules.task.vo.WorkDealInfoVo> res = new ArrayList<com.itrus.ca.modules.task.vo.WorkDealInfoVo>();

		try {
			lst = workDealInfoDao.findBySQLListMap(hql, 0, 0);

			for (Map e : lst) {
				com.itrus.ca.modules.task.vo.WorkDealInfoVo vo = new com.itrus.ca.modules.task.vo.WorkDealInfoVo();

				Iterator<String> it = e.keySet().iterator();
				while (it.hasNext()) {
					String k = it.next();
					if (k.equals("LEVEL")) {
						vo.setLevel(new Long(e.get("LEVEL").toString()));
					}

					if (k.equals("ID")) {
						vo.setId(new Long(e.get("ID").toString()));
					}

					if (k.equals("PREV_ID")) {
						if (e.get("PREV_ID") != null) {
							vo.setPrevId(new Long(e.get("PREV_ID").toString()));
						}

					}
					if (k.equals("FIRST_CERT_SN")) {
						if (e.get("FIRST_CERT_SN") != null) {
							vo.setFirstCertSN(e.get("FIRST_CERT_SN").toString());
						} else {
							vo.setFirstCertSN("");
						}
					}
				}

				res.add(vo);

			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	public List<String> findFirstCertSnByAppId(Integer appId, Integer count) {

		String sql = "select distinct FIRST_CERT_SN from WORK_DEAL_INFO where  FIRST_CERT_SN is not null ";
		boolean hasWhere = false;
		if (appId != null && appId.longValue() > 0) {
			sql = sql + " and APP_ID=" + appId;
			hasWhere = true;
		}
		if (count != null && count.intValue() > 0) {
			sql = sql + " and rownum<=" + count;
		}
		sql += " group by FIRST_CERT_SN";

		List<Map> lst = null;
		List<String> res = new ArrayList<String>();
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			for (Map e : lst) {
				if (e.get("FIRST_CERT_SN") == null
						|| e.get("FIRST_CERT_SN").toString().trim().length() <= 0)
					continue;
				res.add(e.get("FIRST_CERT_SN").toString());
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public Integer getNeedFixPrevIdCount(String appid) {
		List<Map> m = null;
		String sql = getFixPrevIdSql(appid, true);
		try {
			m = workDealInfoDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exLog.error(StringHelper.getStackInfo(e));
		}
		if (m == null || m.size() <= 0)
			return 0;
		return new Integer(m.get(0).get("CT").toString());
	}

	public List<String> getNeedFixPrevId(String appid, Integer count) {
		String sql = getFixPrevIdSql(appid, false);
		if (count != null && count.intValue() > 0) {
			sql = sql + " and rownum<=" + count;
		}
		List<Map> lst = null;
		List<String> res = new ArrayList<String>();
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			for (Map e : lst) {
				if (e.get("FIRST_CERT_SN") == null
						|| e.get("FIRST_CERT_SN").toString().trim().length() <= 0)
					continue;
				res.add(e.get("FIRST_CERT_SN").toString());
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exLog.error(StringHelper.getStackInfo(e));
		}
		return res;
	}

	private String getFixPrevIdSql(String appid, boolean queryCount) {
		String sql = "select ";
		sql = queryCount ? sql + " count(*) CT" : sql
				+ " id,prev_id,first_cert_sn,SVN,DEL_FLAG ";
		sql += " from WORK_DEAL_INFO where  ";
		sql += " FIRST_CERT_SN in( ";
		sql += " select first_cert_sn from ( ";
		sql += " select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO ";
		sql = sql + " where APP_ID= " + appid;
		sql = sql + " and FIRST_CERT_SN!='0'";
		sql += " group by FIRST_CERT_SN order by c desc ";
		sql += " ) where c=1  ";
		sql += " ) and PREV_ID is not null ";
		return sql;
	}

	// 根据应用和时间，制作证书的集合
	public List<WorkDealInfo> findByAppIdDate(Long appId, Date date) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workCertInfo", "workCertInfo");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("workCertInfo.signDate", date));
		String[] proName = new String[] { "1", "2" };
		dc.add(Restrictions.in("configProduct.name", proName));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据应用ID查询出dealInfo信息
	public List<WorkDealInfo> findByAppId(List<Long> appids) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appids != null && appids.size() > 0) {
			dc.add(Restrictions.in("configApp.id", appids));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据公司，产品，应用查询WorkDealInfo
	public List<WorkDealInfo> selectAccount(ConfigApp configApp,
			ConfigProduct configProduct, WorkCompany workCompany, Long id) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.createAlias("workCompany", "workCompany");
		if (workCompany != null) {
			dc.add(Restrictions.eq("workCompany", workCompany));
		}
		if (configProduct != null) {
			dc.add(Restrictions.eq("configProduct", configProduct));
		}
		if (configApp != null) {
			dc.add(Restrictions.eq("configApp", configApp));
		}
		if (id != null) {
			dc.add(Restrictions.ne("id", id));
		}
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
	}

	// 按page查
	public Page<WorkDealInfo> findByDealPay(Page<WorkDealInfo> page,
			WorkDealInfo workDealInfo, Date startTime, Date endTime,
			Long appId, List<Long> officeIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodMoney", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodPos", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodBank", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodAlipay", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
		}
		if (endTime != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);
			dc.add(Restrictions.le("workPayInfo.createDate", calendar.getTime()));
		}

		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (officeIds != null && officeIds.size() > 0) {
			dc.add(Restrictions.in("officeId", officeIds));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	// 按list查
	public List<WorkDealInfo> findByDealPay(WorkDealInfo workDealInfo,
			Date startTime, Date endTime, Long appId, List<Long> officeIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		/*
		 * dc.createAlias("createBy", "createBy");
		 * dc.createAlias("createBy.office", "office");
		 */
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodMoney", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodPos", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodBank", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodAlipay", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
			if (officeIds != null && officeIds.size() > 0) {
				dc.add(Restrictions.in("officeId", officeIds));
			}
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
		}
		if (endTime != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);
			dc.add(Restrictions.le("workPayInfo.createDate", calendar.getTime()));
		}

		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}

		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @param dealInfoByAreaIds
	 * @param appId
	 * @return
	 */
	public Page<WorkDealInfo> findByDayPay(Page<WorkDealInfo> page,
			Date startTime, Date endTime, List<Long> officeids,
			List<Long> dealInfoByAreaIds, Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		if (startTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			// dc.add(Restrictions.eq("workPayInfo.delFlag",
			// WorkPayInfo.DEL_FLAG_NORMAL));
			// dc.add(Restrictions.eq("dealInfoStatus",
			// WorkDealInfoStatus.STATUS_CERT_OBTAINED));
			// if (workDealInfo.getWorkPayInfo() != null) {
			// if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
			// dc.add(Restrictions.eq("workPayInfo.methodMoney", true));
			// }
			// if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
			// dc.add(Restrictions.eq("workPayInfo.methodPos", true));
			// }
			// if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
			// dc.add(Restrictions.eq("workPayInfo.methodBank", true));
			// }
			// if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
			// dc.add(Restrictions.eq("workPayInfo.methodAlipay", true));
			// }
			// if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
			// dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			// }
			// if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
			// dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			// }
			// }
			// if (startTime != null) {
			// dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
			// dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			// }
			// if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
			// dc.add(Restrictions.in("id", dealInfoByAreaIds));
			// }
			// if (appId != null) {
			// dc.add(Restrictions.eq("configApp.id", appId));
			// }
			// if(officeids!=null)
			// {
			// dc.add(Restrictions.in("office.id", officeids));
			// }
			if (startTime != null) {
				dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
				dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			}
			if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
				dc.add(Restrictions.in("id", dealInfoByAreaIds));
			}
			if (appId != null) {
				dc.add(Restrictions.eq("configApp.id", appId));
			}

			if (officeids != null && officeids.size() > 0) {

				dc.add(Restrictions.in("office.id", officeids));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(page, dc);
		} else {
			return null;
		}

	}

	public List<WorkDealInfo> findByDayPay1(Date startTime, Date endTime,
			List<Long> officeids, Long appId) {
		long s = System.currentTimeMillis();
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		List<String> status = Lists.newArrayList();
		status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		status.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", status));
		if (startTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			if (startTime != null) {
				dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
				dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			}
			// if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
			// dc.add(Restrictions.in("id", dealInfoByAreaIds));
			// }
			if (appId != null) {
				dc.add(Restrictions.eq("configApp.id", appId));
			}

			if (officeids != null && officeids.size() > 0) {
				dc.add(Restrictions.in("officeId", officeids));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(dc);
		} else {
			return null;
		}

	}

	public List<MonthPayment> findByDayPay(Date startTime, Date endTime,
			List<Long> officeids, Long appId) throws ParseException {
		StringBuffer sqlBuffer = new StringBuffer();
		String sqlString = "SELECT p.id as workPayInfoId,"
				+ " w.office_id as officeId,"
				+ " p.CREATE_DATE as createDate,"
				+ " p.RELATION_METHOD as realtionMethod,"
				+ " p.method_pos as methodPos,"
				+ " p.POS_MONEY as posMoney,"
				+ " p.METHOD_BANK as methodBank,"
				+ " p.BANK_MONEY as bankMoney,"
				+ " p.METHOD_MONEY as methodMoney,"
				+ " p.MONEY as money,"
				+ " p.METHOD_ALIPAY as methodAlipay,"
				+ " p.ALIPAY_MONEY as alipayMoney"
				+ " FROM work_deal_info w"
				+ " INNER JOIN work_pay_info p"
				+ " ON w.pay_id =p.id"
				+ " WHERE w.pay_id IS NOT NULL"
				+ " AND p.del_flag =0"
				+ " AND w.deal_info_status  IN (7,9)"
				+ " AND (p.method_pos!=0 or p.METHOD_BANK!=0 OR p.METHOD_MONEY!=0 OR p.METHOD_ALIPAY!=0 or p.RELATION_METHOD IS NOT NULL)";
		sqlBuffer.append(sqlString);
		if (startTime != null) {
			startTime.setHours(0);
			startTime.setMinutes(0);
			startTime.setSeconds(00);
			sqlBuffer.append(" AND p.create_date>=to_date('"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(startTime) + "','yyyy-MM-dd HH24:mi:ss')");

		}

		if (endTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			sqlBuffer.append(" AND p.create_date<=to_date('"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(endTime) + "','yyyy-MM-dd HH24:mi:ss')");
		}

		if (appId != null) {
			sqlBuffer.append(" AND w.app_id=" + appId);
		}
		if (officeids != null && officeids.size() > 0) {
			sqlBuffer.append(" AND w.office_id in ("
					+ officeids.toString().substring(1,
							(officeids.toString().length() - 1)) + ")");
		}
		sqlBuffer.append(" ORDER BY p.create_date ASC");
		List<Object[]> list = workDealInfoDao.findBySql(sqlBuffer.toString());
		List<MonthPayment> lst = new ArrayList<MonthPayment>();
		for (int i = 0; i < list.size(); i++) {
			MonthPayment monthPayment = new MonthPayment();
			if (list.get(i)[0] != null) {
				monthPayment.setWorkPayInfoId(Long.parseLong(list.get(i)[0]
						.toString()));

			}
			if (list.get(i)[1] != null) {

				monthPayment.setOfficeId(Long.parseLong(list.get(i)[1]
						.toString()));
			}
			if (list.get(i)[2] != null) {

				monthPayment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd")
						.parse(list.get(i)[2].toString()));
			}
			if (list.get(i)[3] != null) {

				monthPayment.setRealtionMethod(list.get(i)[3].toString());
			}
			if (list.get(i)[4] != null) {
				monthPayment.setMethodPos(Integer.parseInt(list.get(i)[4]
						.toString()));

			}
			if (list.get(i)[5] != null) {
				monthPayment.setPosMoney(Integer.parseInt(list.get(i)[5]
						.toString()));

			}
			if (list.get(i)[6] != null) {
				monthPayment.setMethodBank(Integer.parseInt(list.get(i)[6]
						.toString()));

			}
			if (list.get(i)[7] != null) {

				monthPayment.setBankMoney(Integer.parseInt(list.get(i)[7]
						.toString()));
			}
			if (list.get(i)[8] != null) {

				monthPayment.setMethodMoney(Integer.parseInt(list.get(i)[8]
						.toString()));
			}
			if (list.get(i)[9] != null) {

				monthPayment.setMoney(Integer.parseInt(list.get(i)[9]
						.toString()));
			}
			if (list.get(i)[10] != null) {
				monthPayment.setMethodAlipay(Integer.parseInt(list.get(i)[10]
						.toString()));

			}
			if (list.get(i)[11] != null) {
				monthPayment.setAlipayMoney(Integer.parseInt(list.get(i)[11]
						.toString()));

			}
			lst.add(monthPayment);
		}

		return lst;

	}

	public Page<WorkDealInfo> findByProjectPay(Page<WorkDealInfo> page,
			Date startTime, Date endTime, List<Long> officeids,
			List<Long> dealInfoByAreaIds, Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		if (startTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);

			if (startTime != null) {
				dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
				dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			}
			if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
				dc.add(Restrictions.in("id", dealInfoByAreaIds));
			}
			if (appId != null) {
				dc.add(Restrictions.eq("configApp.id", appId));
			}
			if (officeids != null && officeids.size() > 0) {
				dc.add(Restrictions.in("office.id", officeids));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(page, dc);
		} else {
			return null;
		}

	}

	public List<WorkDealInfo> findByProjectYear(Date startTime, Date endTime,
			List<Long> dealInfoByAreaIds, Long appId, List<Long> offices) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		List<String> status = Lists.newArrayList();
		status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		status.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", status));
		// dc.add(Restrictions.eq("", ""));
		if (startTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			if (startTime != null) {
				dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
				dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			}
			if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
				dc.add(Restrictions.in("id", dealInfoByAreaIds));
			}
			if (appId != null) {
				dc.add(Restrictions.eq("configApp.id", appId));
			}
			if (offices.size() > 0 && !"".equals(offices)) {
				dc.add(Restrictions.in("office.id", offices));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(dc);
		} else {
			return null;
		}

	}

	public List<WorkDealInfo> findByProjectYear(Date startTime, Date endTime,
			List<Long> appIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("createBy", "createBy");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		List<String> status = Lists.newArrayList();
		status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		status.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", status));
		// dc.add(Restrictions.eq("", ""));
		if (startTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			if (startTime != null) {
				dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
				dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			}
			if (appIds != null && appIds.size() > 1) {
				dc.add(Restrictions.in("configApp.id", appIds));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(dc);
		} else {
			return null;
		}

	}

	public List<WorkDealInfo> findByProjectPay(Date startTime, Date endTime,
			List<Long> officeids, List<Long> dealInfoByAreaIds, Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		List<String> status = Lists.newArrayList();
		status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		status.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", status));
		if (startTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			// DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
			// dc.createAlias("workPayInfo", "workPayInfo");
			// dc.createAlias("createBy", "createBy");
			// dc.createAlias("createBy.office", "office");
			// dc.add(Restrictions.isNotNull("workPayInfo"));
			// dc.add(Restrictions.eq("workPayInfo.delFlag",
			// WorkPayInfo.DEL_FLAG_NORMAL));
			// dc.add(Restrictions.eq("dealInfoStatus",
			// WorkDealInfoStatus.STATUS_CERT_OBTAINED));
			if (startTime != null) {
				dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
				dc.add(Restrictions.le("workPayInfo.createDate", endTime));
			}
			if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
				dc.add(Restrictions.in("id", dealInfoByAreaIds));
			}
			if (appId != null) {
				dc.add(Restrictions.eq("configApp.id", appId));
			}
			if (officeids != null && officeids.size() > 0) {
				dc.add(Restrictions.in("office.id", officeids));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(dc);
		} else {
			return null;
		}

	}

	public List<WorkDealInfo> findByProjectPay(Date startTime, Date endTime,
			List<Long> officeIds, Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));

		dc.add(Restrictions.eq("workPayInfo.delFlag",
				WorkPayInfo.DEL_FLAG_NORMAL));
		List<String> status = Lists.newArrayList();
		status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		status.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", status));

		if (startTime != null) {
			startTime.setHours(00);
			startTime.setMinutes(00);
			startTime.setSeconds(00);

			dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
		}

		if (endTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);

			dc.add(Restrictions.le("workPayInfo.createDate", endTime));
		}

		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (officeIds != null && officeIds.size() > 0) {
			dc.add(Restrictions.in("officeId", officeIds));
		}

		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.eq("workPayInfo.methodPos", true));
		disjunction.add(Restrictions.eq("workPayInfo.methodMoney", true));
		disjunction.add(Restrictions.eq("workPayInfo.methodBank", true));
		disjunction.add(Restrictions.eq("workPayInfo.methodAlipay", true));
		disjunction.add(Restrictions.isNotNull("workPayInfo.relationMethod"));

		dc.add(disjunction);

		dc.addOrder(Order.asc("workPayInfo.createDate"));
		return workDealInfoDao.find(dc);

	}

	public WorkDealInfo findDealPayShow(Long dealInfoId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("id", dealInfoId));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc).get(0);
	}

	public List<WorkDealInfo> findByCertSn(String certSn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (certSn != null) {
			dc.add(Restrictions.eq("certSn", certSn));
		}
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
	}

	public WorkDealInfo findByCertSnOne(String certSn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (certSn != null) {
			dc.add(Restrictions.eq("certSn", certSn));
		}
		dc.addOrder(Order.desc("id"));

		List<WorkDealInfo> lst = workDealInfoDao.find(dc);
		if (lst == null || lst.size() <= 0)
			return null;
		return lst.get(0);
	}

	/**
	 * 不考虑删除的查询
	 * 
	 * @date:2014年8月21日
	 * @user:京涛
	 * @return_type:List<WorkDealInfo>
	 */
	public List<WorkDealInfo> findByCertSnIgnoreDel(String certSn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (certSn != null) {
			dc.add(Restrictions.eq("certSn", certSn));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findByCertSnKeySn(String certSn, String keySn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (certSn != null) {
			dc.add(Restrictions.eq("certSn", certSn));
		}
		if (keySn != null) {
			dc.add(Restrictions.eq("keySn", keySn));
		}
		dc.add(Restrictions.eq("dealInfoType", 0));
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
	}

	/**
	 * 用于结算处的业务明细
	 * 
	 * @param appId
	 * @param productId
	 * @param dealInfoType
	 * @param year
	 * @return
	 */
	public Page<WorkDealInfo> findDealInfo(Page<WorkDealInfo> page, Long appId,
			Long productId, Integer dealInfoType, Integer year, String agentName) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("configProduct.id", productId));
		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}
		if (agentName != null) {
			dc.createAlias("commercialAgent", "agent");
			dc.add(Restrictions.eq("agent.agentName", agentName));
		}
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType)));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("canSettle", true));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(page, dc);
	}

	public Long findDealInfoMax() {

		String sql = "select id from WORK_DEAL_INFO where rownum<=1 order by id desc";
		List<Map> lst = null;
		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			e.printStackTrace();
		}
		if (lst == null || lst.size() <= 0) {
			return 0l;
		} else {
			return new Long(lst.get(0).get("ID").toString());
		}

	}

	/**
	 * 用于结算处的业务明细
	 * 
	 * @param appId
	 * @param productId
	 * @param dealInfoType
	 * @param year
	 * @return
	 */
	public Page<WorkDealInfo> findDealInfos(Page<WorkDealInfo> page,
			Long appId, Long productId, Integer dealInfoType, Integer year,
			String agentName, Date startDate, Date endDate, boolean isCanSettle) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("configProduct.id", productId));
		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}
		if (agentName != null) {
			if (isCanSettle) {
				dc.createAlias("configCommercialAgent", "agent");
			} else {
				dc.createAlias("commercialAgent", "agent");
			}
			dc.add(Restrictions.eq("agent.agentName", agentName));
		}
		if (startDate != null) {
			dc.add(Restrictions.ge("createDate", startDate));
		}
		if (endDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.DATE, 1);
			endDate = calendar.getTime();
			dc.add(Restrictions.le("createDate", endDate));
		}
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType)));
		dc.addOrder(Order.desc("id"));
		if (isCanSettle) {
			dc.add(Restrictions.eq("canSettle", isCanSettle));// 劳务关系的不用canSettle来判断

		}
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findByFinance(Page<WorkDealInfo> page,
			List<WorkPayInfo> payInfoso) {

		List<Long> idList = Lists.newArrayList();
		for (int i = 0; i < payInfoso.size(); i++) {
			idList.add(payInfoso.get(i).getId());
		}
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();

		dc.add(Restrictions.in("workPayInfo.id", idList));

		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public List<WorkDealInfo> findByCompany(Long companyId) {
		return workDealInfoDao
				.find("from WorkDealInfo where work_company_id=?  order by create_date",
						companyId);
	}

	public List<WorkDealInfo> findListAppId(Long appId) {
		return workDealInfoDao.find("from WorkDealInfo where app_id=" + appId);
	}

	public String findSvnOne(String svn, Long appid) {
		String sql = "select svn from work_deal_info where svn like '%"
				+ svn + "%' and rownum<=1";
		if (appid != null && appid.longValue() > 0)
			sql += " and APP_ID=" + appid;
		sql += " order by svn desc";
		List<String> lst = workDealInfoDao.findBySql(sql);

		if (lst == null || lst.size() <= 0)
			return null;
		return lst.get(0);
	}

	@Transactional(readOnly = false)
	public void save(WorkDealInfo workDealInfo) {
		/*log.error("save dealInfo:[firstCertSN:" + workDealInfo.getFirstCertSN()
				+ "|prevId:" + workDealInfo.getPrevId() + "|certSN:"
				+ workDealInfo.getCertSn() + "|id:" + workDealInfo.getId()
				+ "]");*/
		if (StringHelper.isNull(workDealInfo.getFirstCertSN())) {
			// 如果是首条(无firstCertSn,certSn不是空,业务类型为新增)
			if (workDealInfo.getPrevId() == null) {
				if ((workDealInfo.getDealInfoType() != null && workDealInfo
						.getDealInfoType() == 0)
						&& !StringHelper.isNull(workDealInfo.getCertSn())) {
					String fcn = zeroProcess(workDealInfo.getCertSn());
					workDealInfo.setFirstCertSN(fcn);
				} else {
					// 记录进日志
					updateLog.error("only id..." + workDealInfo.getId());
				}

			} else {
				// 有prev_id则直接查上一条
				WorkDealInfo w = findPreDealInfo(workDealInfo.getPrevId());
				String fs = w == null ? null : w.getFirstCertSN();
				workDealInfo.setFirstCertSN(fs);
			}
		}
		workDealInfoDao.save(workDealInfo);
	}

	@Transactional(readOnly = false)
	public void save(List<WorkDealInfo> workDealInfo) {
		workDealInfoDao.save(workDealInfo);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		workDealInfoDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	public void delete(List<WorkDealInfo> dealInfos) {
		workDealInfoDao.delete(dealInfos);
	}

	@Transactional(readOnly = false)
	public void deleteWork(Long id) {
		workDealInfoDao.delete(id);
	}

	/**
	 * 查看本月归档数据
	 * 
	 * @param page
	 * @param workDealInfo
	 * @return
	 */
	public List<WorkDealInfo> findNum(WorkDealInfo workDealInfo, String timeStr) {
		return workDealInfoDao.findNum(timeStr);
	}

	public List<WorkDealInfo> findktydsb1(String keySn) {
		return workDealInfoDao.findktydsb1(keySn);
	}

	public List<WorkDealInfo> findByFirstCertSN(String firstCertSN) {
		return workDealInfoDao.findByFirstCertSN(firstCertSN);
	}

	public List<WorkDealInfo> findByFirstCertSNAndDealInfoType(
			String firstCertSN, Integer dealInfoType) {
		return workDealInfoDao.findByFirstCertSNAndDealInfoType(firstCertSN,
				dealInfoType);
	}

	public List<WorkDealInfo> findgxfw1(String certSn) {
		if (StringUtils.isEmpty(certSn)) {
			return null;
		}
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (!StringUtils.isEmpty(certSn)) {
			dc.add(Restrictions.eq("certSn", certSn));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findgx(Long id) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (id != null) {
			dc.createAlias("workCertInfo", "workCertInfo");
			dc.add(Restrictions.eq("workCertInfo.id", id));
		}
		return workDealInfoDao.find(dc);
	}

	/**
	 * 查询已存在的业务编号的数量
	 * 
	 * @param snStr
	 * @return
	 */
	public int countClientInfoAmount(String snStr) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.like("svn", EscapeUtil.escapeLike(snStr) + "%"));
		return (int) workDealInfoDao.count(dc);
	}

	public List<WorkDealInfo> getDealInfo(Office office, Long agentIds,
			Date startTime, Date endTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("commercialAgent", "commercialAgent");
		if (office != null) {
			dc.add(Restrictions.eq("createBy.office", office));
		}
		dc.add(Restrictions.eq("commercialAgent.id", agentIds));
		if (startTime != null && endTime != null) {
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}

		// dc.add(Restrictions.eq("canSettle", true)); canSettle 只是控制是否是市场推广的结算
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(dc);
	}

	public List<Object[]> getSum4Agent(Long officeId, Long agentId,
			Date startTime, Date endTime) {
		// 这个SQL 注入风险基本为0 因为要拼接的参数是Long或者integer，没有String进行拼接
		String sql = "SELECT D.APP_ID, D.PRODUCT_ID,D.DEAL_INFO_TYPE , COUNT (DISTINCT(D .ID)) FROM  WORK_DEAL_INFO D, WORK_CERT_INFO c,WORK_PAY_INFO P ,SYS_USER U "
				+ " WHERE D.DEAL_INFO_TYPE IS NOT NULL AND D .CERT_ID = c.ID AND D .PAY_ID = P .ID  AND D.CREATE_BY = U.ID AND P.MONEY >0"; // 此版本不统计退费
		sql += officeId == null ? "" : " and U.OFFICE_ID = " + officeId;
		sql += agentId == null ? "" : " and d.COMMERCIAL_AGENT_ID =" + agentId;
		sql += startTime == null ? "" : " AND c.CREATE_DATE >= TO_DATE('"
				+ DateUtils.formatDate(startTime, "yyyyMMdd")
				+ "', 'yyyymmdd')";
		sql += endTime == null ? "" : " AND c.CREATE_DATE < TO_DATE ('"
				+ DateUtils.formatDate(endTime, "yyyyMMdd") + "', 'yyyymmdd')";
		sql += " group by D.APP_ID, D.PRODUCT_ID, D.DEAL_INFO_TYPE";
		log.debug(sql);
		List<Object[]> resList = new ArrayList<Object[]>();
		resList = workDealInfoDao.findBySql(sql);
		// log.debug(resList.get(0));
		// log.debug(((Object[]) resList.get(0))[0] +":" +((Object[])
		// resList.get(0))[1]);

		return resList;
	}

	public List<Object[]> getSumType14Agent(Long officeId, Long agentId,
			Date startTime, Date endTime) {
		// 这个SQL 注入风险基本为0 因为要拼接的参数是Long或者integer，没有String进行拼接
		String sql = "SELECT D.APP_ID, D.PRODUCT_ID, D.DEAL_INFO_TYPE1 , COUNT (DISTINCT(D .ID)) FROM  WORK_DEAL_INFO D, WORK_CERT_INFO c,WORK_PAY_INFO P ,SYS_USER U "
				+ " WHERE D.DEAL_INFO_TYPE1 IS NOT NULL AND D .CERT_ID = c.ID AND D .PAY_ID = P .ID  AND D.CREATE_BY = U.ID AND P.MONEY >0"; // 此版本不统计退费
		sql += officeId == null ? "" : " and U.OFFICE_ID = " + officeId;
		sql += agentId == null ? "" : " and d.COMMERCIAL_AGENT_ID =" + agentId;
		sql += startTime == null ? "" : " AND c.CREATE_DATE >= TO_DATE('"
				+ DateUtils.formatDate(startTime, "yyyyMMdd")
				+ "', 'yyyymmdd')";
		sql += endTime == null ? "" : " AND c.CREATE_DATE < TO_DATE ('"
				+ DateUtils.formatDate(endTime, "yyyyMMdd") + "', 'yyyymmdd')";
		sql += " group by D.APP_ID, D.PRODUCT_ID, D.DEAL_INFO_TYPE1";
		log.debug(sql);
		List<Object[]> resList = new ArrayList<Object[]>();
		resList = workDealInfoDao.findBySql(sql);
		// log.debug(resList.get(0));
		// log.debug(((Object[]) resList.get(0))[0] +":" +((Object[])
		// resList.get(0))[1]);

		return resList;
	}

	/**
	 * 查询代理商结算数量接口
	 * 
	 * @param configCommercialAgentId
	 * @param workDealInfoType
	 * @param productName
	 * @param year
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<WorkDealInfo> getSumInfo(Long appId, Integer workDealInfoType,
			Long productId, Integer year, Date startDate, Date endDate,
			Long officeId, List<WorkPayInfo> workPayInfos) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.createAlias("createBy", "createBy");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.or(
				Restrictions.eq("dealInfoType", workDealInfoType),
				Restrictions.eq("dealInfoType1", workDealInfoType)));
		dc.add(Restrictions.eq("configProduct.id", productId));
		if (workPayInfos != null && workPayInfos.size() > 0) {
			dc.add(Restrictions.in("workPayInfo", workPayInfos));
		}
		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		if (startDate != null && endDate != null) {
			dc.add(Restrictions.ge("createDate", startDate));
			dc.add(Restrictions.le("createDate", endDate));
		}
		if (officeId != null) {
			dc.add(Restrictions.eq("createBy.office.id", officeId));
		}
		dc.add(Restrictions.eq("canSettle", true));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED)));
		return workDealInfoDao.find(dc);
	}

	/**
	 * 查询代理商结算数量接口
	 * 
	 * @param configCommercialAgentId
	 * @param workDealInfoType
	 * @param productName
	 * @param year
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object[]> getSumCount(Long appId, Integer workDealInfoType,
			Long productId, Integer year, Date startDate, Date endDate,
			Long officeId, Long agentId, boolean chargeMethodPos,
			boolean chargeMethodMoney, boolean chargeMethodBank,
			boolean chargeMethodGov, boolean chargeMethodContract) {
		// 这个SQL 注入风险基本为0 因为要拼接的参数是Long或者integer，没有String进行拼接
		String sql = "SELECT COUNT (DISTINCT(D .ID)),nvl(sum(p.work_payed_money),0) FROM  WORK_DEAL_INFO D, WORK_CERT_INFO c,WORK_PAY_INFO P ,SYS_USER U "
				+ " WHERE D .CERT_ID = c.ID AND D .PAY_ID = P .ID  AND D.CREATE_BY = U.ID AND P.MONEY >0"; // 此版本不统计退费
		sql += appId == null ? "" : " and d.app_id= " + appId;
		sql += productId == null ? "" : " and d.PRODUCT_ID = " + productId;
		sql += officeId == null ? "" : " and d.OFFICE_ID = " + officeId;

		sql += agentId == null ? "" : " and d.AGENT_ID =" + agentId;
		sql += " and (d.DEAL_INFO_TYPE =" + workDealInfoType
				+ " or d.DEAL_INFO_TYPE1 =" + workDealInfoType + ")";
		sql += year == null ? "" : " and d.YEAR = " + year;
		sql += startDate == null ? "" : " AND c.CREATE_DATE > TO_DATE('"
				+ DateUtils.formatDate(startDate, "yyyyMMdd")
				+ "', 'yyyymmdd')";
		sql += endDate == null ? "" : " AND c.CREATE_DATE < TO_DATE ('"
				+ DateUtils.formatDate(endDate, "yyyyMMdd") + "', 'yyyymmdd')";

		String condition = " and ( 1=0 "; // 确保程序里面有条件
		if (chargeMethodPos) {
			condition += " or p.METHOD_POS=1";
		}
		if (chargeMethodMoney) {
			condition += " or p.METHOD_MONEY=1";
		}
		if (chargeMethodBank) {
			condition += " or p.METHOD_BANK=1";
		}
		if (chargeMethodGov) {
			condition += " or p.METHOD_GOV=1";
		}
		if (chargeMethodContract) {
			condition += " or p.METHOD_CONTRACT=1";
		}
		condition += ")";
		sql = sql + condition;
		log.debug(sql);
		List<Object[]> resList = new ArrayList<Object[]>();
		resList = workDealInfoDao.findBySql(sql);
		// log.debug(resList.get(0));
		// log.debug(((Object[]) resList.get(0))[0] +":" +((Object[])
		// resList.get(0))[1]);

		return resList;
	}

	/**
	 * 根据经办人获得多证书编号
	 * 
	 * @return
	 */
	public Integer getMultiNumByWorkUser(Long userId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.add(Restrictions.eq("workUser.id", userId));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("obtainedDate"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {
			return dealInfos.get(0).getCertSort() + 1;
		}
	}

	/**
	 * 根据经办人获得多证书编号
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public Integer getNumByOrganizationNumber(String organizationNumber) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));

		dc.add(Restrictions.eq("workCompany.organizationNumber",
				organizationNumber));
		dc.addOrder(Order.asc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {

			for (int i = 0; i < dealInfos.size(); i++) {
				if (dealInfos.get(i).getDealInfoStatus()
						.equals(WorkDealInfoStatus.STATUS_CERT_REVOKE)) {
					return dealInfos.get(i).getCertSort();
				}
			}
			return dealInfos.get(dealInfos.size() - 1).getCertSort() + 1;
		}
	}

	public Integer getNumByOrganizationNumberCompanyName(
			String organizationNumber, String companyName) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		if (organizationNumber != null && !organizationNumber.equals("")) {
			dc.add(Restrictions.eq("workCompany.organizationNumber",
					organizationNumber));
		} else {
			dc.add(Restrictions.eq("workCompany.companyName", companyName));
		}
		dc.addOrder(Order.asc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {

			for (int i = 0; i < dealInfos.size(); i++) {
				if (dealInfos.get(i).getDealInfoStatus()
						.equals(WorkDealInfoStatus.STATUS_CERT_REVOKE)) {
					return dealInfos.get(i).getCertSort();
				}
			}
			return dealInfos.get(dealInfos.size() - 1).getCertSort() + 1;
		}
	}

	public Integer getNumByNameCard(String name, String card) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		if (card != null && !card.equals("")) {
			dc.add(Restrictions.eq("workUser.conCertNumber", card));
		} else {
			dc.add(Restrictions.eq("workUser.contactName", name));
		}
		dc.addOrder(Order.asc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {

			for (int i = 0; i < dealInfos.size(); i++) {
				if (dealInfos.get(i).getDealInfoStatus()
						.equals(WorkDealInfoStatus.STATUS_CERT_REVOKE)) {
					return dealInfos.get(i).getCertSort();
				}
			}
			return dealInfos.get(dealInfos.size() - 1).getCertSort() + 1;
		}
	}

	public List<WorkDealInfo> findSvn(String svn) {
		return workDealInfoDao.findSvn(svn);
	}

	
	
	public String getTimeSvn(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		User user = UserUtils.getUser();
		return "C-" + user.getOffice().getName() + "-"
				+ sdf.format(date).substring(2);
	}
	
	
	public int getNumSvn(String timeSvn){
		List<WorkDealInfo> list = findSvn("%" + timeSvn + "%");
		int num = 1;
		if (list.size() > 0) {
			String oldSvn = list.get(0).getSvn();

			String[] elm = StringHelper.splitStr(oldSvn, "-");
			if (elm == null || elm.length < 4
					|| !StringHelper.isDigit(elm[3])) {
				num = 1;
			} else {
				num = new Integer(elm[3]) + 1;
			}
		}
		
		return num;
	}
	
	public String getNumStrSvn(int num){
		String numStr = "";
		
		if (num > 0 && num < 10) {
			numStr = "00000" + num;
		} else if (num > 9 && num < 100) {
			numStr = "0000" + num;
		} else if (num > 99 && num < 1000) {
			numStr = "000" + num;
		} else if (num > 999 && num < 10000) {
			numStr = "00" + num;
		} else if (num > 9999 && num < 100000) {
			numStr = "0" + num;
		} else {
			numStr = "" + num;
		}
		
		
		return numStr;
	}
	
	
	
	/**
	 * 生成业务编号 serviceType是区分客户端办理和柜台办理 0是柜台，1是客户端
	 * 
	 * @param serviceType
	 * @return
	 */
	public String getSVN(Integer serviceType) {
		
		String svn = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		if (serviceType == 0) {
			try {
				String timeSvn = getTimeSvn();
				int num = getNumSvn(timeSvn);
				String numStr = getNumStrSvn(num);
	
				svn = timeSvn + numStr;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else {
			String searchText = "O-客户端-" + sdf.format(new Date());
			svn = outClientSn(searchText);
		}
		return svn;
	}

	@Transactional(readOnly = false)
	public void setSvnToNull(Long appid) {
		String sql = "update work_deal_info set svn=null where APP_ID=" + appid;
		try {
			workDealInfoDao.exeSql(sql);
		} catch (Exception e) {
		}
	}

	/**
	 * 修复前对库内一些错乱数据初始修复,包括：<br>
	 * 0.首证书，prev_id为空，并且证书号非空，业务状态为7，6（完成）的，把首证书设置成证书号<br>
	 * 1.修复吊销数据<br>
	 * 2.修复首证书未补0的数据<br>
	 * 3.修复新办的新增业务，首证书为0的数据<br>
	 * 
	 * @param appid
	 */
	public void initFix(Long appid) {
		// 0.首证书，prev_id为空，并且证书号非空，业务状态为7，6（完成）的，把首证书设置成证书号
		String sql = "update WORK_DEAL_INFO set FIRST_CERT_SN=cert_sn ";
		sql = sql + " WHERE APP_ID =" + appid;
		sql += " and DEAL_INFO_STATUS IN (7, 6) and FIRST_CERT_SN is null ";
		sql += " and PREV_ID is null and cert_sn is not null ";

		// 1.修复吊销数据
		String sql1 = "update WORK_DEAL_INFO set DEAL_INFO_TYPE=1";
		sql1 = sql1 + " WHERE APP_ID =" + appid;
		sql1 += " AND DEAL_INFO_TYPE is null ";
		sql1 += " AND DEAL_INFO_TYPE1 is null ";
		sql1 += " AND DEAL_INFO_TYPE2 is null ";
		sql1 += " AND DEAL_INFO_TYPE3 is null ";

		// 2.修复首证书未补0的数据
		String sql2 = "update WORK_DEAL_INFO set FIRST_CERT_SN='00'||FIRST_CERT_SN";
		sql2 = sql2 + " where APP_ID=" + appid + " and ( ";
		sql2 += "substr(FIRST_CERT_SN, 0,1)='8' or substr(FIRST_CERT_SN, 0,1)='9' ";
		sql2 += " or substr(FIRST_CERT_SN, 0,1)='A' or substr(FIRST_CERT_SN, 0,1)='B'";
		sql2 += " or substr(FIRST_CERT_SN, 0,1)='C' or substr(FIRST_CERT_SN, 0,1)='D'";
		sql2 += " or substr(FIRST_CERT_SN, 0,1)='E' or substr(FIRST_CERT_SN, 0,1)='F')";

		// 3.修复新办的新增业务，首证书为0的数据
		String sql3 = "update WORK_DEAL_INFO set FIRST_CERT_SN=CERT_SN where FIRST_CERT_SN='0' and ";
		sql3 = sql3 + " APP_ID=" + appid;
		sql3 += " and DEAL_INFO_TYPE=0 and PREV_ID is null";

		try {
			workDealInfoDao.exeSql(sql);
			workDealInfoDao.exeSql(sql1);
			workDealInfoDao.exeSql(sql2);
			workDealInfoDao.exeSql(sql3);
		} catch (Exception e) {
		}
	}

	/**
	 * 1.所有业务链数据大于1条，首证书序列号不为空的,设置为空 2.所有prev_id重复的数据，设置为空
	 * 
	 * @param appid
	 */
	public void setPrevIdToNull(Integer appid) {
		// String sql = "update work_deal_info set prev_id=null where APP_ID="
		// + appid;
		String sql = "update WORK_DEAL_INFO set PREV_ID=null where id in(";
		sql += "select id from WORK_DEAL_INFO where FIRST_CERT_SN in(";
		sql += "select FIRST_CERT_SN from (";
		sql += "select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO ";
		sql += " where PREV_ID is null and FIRST_CERT_SN is not null ";
		sql = sql + " and APP_ID=" + appid;
		sql += " group by FIRST_CERT_SN";
		sql += " order by c desc";
		sql += " ) a where a.c>1";
		sql += " ))";
		try {
			workDealInfoDao.exeSql(sql);
		} catch (Exception e) {
		}
		String sql_prevId = "update WORK_DEAL_INFO set PREV_ID=null ";
		sql_prevId += "where id in(";
		sql_prevId += "select id from WORK_DEAL_INFO where PREV_ID in(";
		sql_prevId += "select PREV_ID from (";
		sql_prevId += "select count(*) c,PREV_ID from WORK_DEAL_INFO ";
		sql_prevId = sql_prevId + " where app_id=" + appid;
		sql_prevId += " group by PREV_ID ";
		sql_prevId += " ) where c>1 ";
		sql_prevId += " ) ";
		sql_prevId += " ) ";
		try {
			workDealInfoDao.exeSql(sql_prevId);
		} catch (Exception e) {
		}
	}

	public String getSVN(String officeName) {
		return getSVN(officeName, null);
	}

	/**
	 * 根据原来的svn重新得到一条新的序列
	 * 
	 * @param po
	 * @return String
	 */
	public synchronized String getSVN(WorkDealInfo po, String officeName,
			Long appid, int num) {
		String svn = "";
		try {
			String poOldSvn = po.getSvn();
			String oldTimeSvn = poOldSvn.substring(0,
					poOldSvn.lastIndexOf("-") + 1);
			String old = findSvnOne(oldTimeSvn, appid);
			if (!StringHelper.isNull(old)) {
				String oldSvn = old;
				String[] elm = StringHelper.splitStr(oldSvn, "-");
				if (elm == null || elm.length < 4
						|| !StringHelper.isDigit(elm[3])) {
					num = 1;
				} else {
					num = new Integer(elm[3]) + 1;
				}
				oldTimeSvn = oldSvn.substring(0, oldSvn.lastIndexOf("-") + 1);
			}
			String numStr = StringHelper.completeText(
					new Integer(num).toString(), 6);
			svn = oldTimeSvn + numStr;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return svn;
	}

	/**
	 * @param officeName
	 * @param appid
	 * @param num
	 * @param modifyPrefix
	 *            (true=改变前缀,false=不改变前缀)
	 * @return String
	 */
	public synchronized String getSVN(String officeName, Long appid, int num,
			boolean modifyPrefix) {
		Date date = new Date();
		String svn = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		try {
			String timeSvn = "C-" + officeName + "-"
					+ sdf.format(date).substring(2);
			String oldTimeSvn = "C-" + officeName + "-";
			String old = findSvnOne(timeSvn, appid);
			if (!StringHelper.isNull(old)) {
				String oldSvn = old;
				String[] elm = StringHelper.splitStr(oldSvn, "-");
				if (elm == null || elm.length < 4
						|| !StringHelper.isDigit(elm[3])) {
					num = 1;
				} else {
					num = new Integer(elm[3]) + 1;
				}
				oldTimeSvn = oldSvn.substring(0, oldSvn.lastIndexOf("-") + 1);
			}
			String numStr = "";
			if (num > 0 && num < 10) {
				numStr = "00000" + num;
			} else if (num > 9 && num < 100) {
				numStr = "0000" + num;
			} else if (num > 99 && num < 1000) {
				numStr = "000" + num;
			} else if (num > 999 && num < 10000) {
				numStr = "00" + num;
			} else if (num > 9999 && num < 100000) {
				numStr = "0" + num;
			} else {
				numStr = "" + num;
			}
			if (modifyPrefix)
				svn = timeSvn + numStr;
			else
				svn = oldTimeSvn + numStr;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return svn;
	}

	public String getSVN(String officeName, Long appid) {
		Date date = new Date();
		String svn = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		try {
			String timeSvn = "C-" + officeName + "-"
					+ sdf.format(date).substring(2);
			String old = findSvnOne(timeSvn, appid);
			int num = 1;
			if (!StringHelper.isNull(old)) {
				String oldSvn = old;
				String[] elm = StringHelper.splitStr(oldSvn, "-");
				if (elm == null || elm.length < 4
						|| !StringHelper.isDigit(elm[3])) {
					num = 1;
				} else {
					num = new Integer(elm[3]) + 1;
				}
			}
			String numStr = "";
			if (num > 0 && num < 10) {
				numStr = "00000" + num;
			} else if (num > 9 && num < 100) {
				numStr = "0000" + num;
			} else if (num > 99 && num < 1000) {
				numStr = "000" + num;
			} else if (num > 999 && num < 10000) {
				numStr = "00" + num;
			} else if (num > 9999 && num < 100000) {
				numStr = "0" + num;
			} else {
				numStr = "" + num;
			}
			svn = timeSvn + numStr;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return svn;
	}

	/**
	 * 查询格式为O-客户端-年月的数量
	 * 
	 * @return
	 */
	public String outClientSn(String yearMonth) {
		int count1 = countClientInfoAmount(yearMonth);
		int count2 = countClientInfoTrustApplyAmount(yearMonth);
		return yearMonth + getNextNum(count1 + count2);
	}

	/**
	 * 生成 00xx四位数字
	 * 
	 * @param num
	 * @return
	 */
	public String getNextNum(Integer num) {
		num++;
		String result = "000" + num;
		return result.substring(result.length() - 4, result.length());
	}

	/**
	 * 根据企业获得多证书编号
	 * 
	 * @return
	 */
	public Integer getMultiNumByWorkCompany(Long companyId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.add(Restrictions.eq("workCompany.id", companyId));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("obtainedDate"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {
			return dealInfos.get(0).getCertSort() + 1;
		}
	}

	public boolean findByOffice(List<Long> officeIds) {

		StringBuffer sql = new StringBuffer();

		sql.append(
				" select count(*) as ct from work_deal_info wdi where wdi.office_id in (")
				.append(officeIds.get(0));

		for (int i = 1; i < officeIds.size(); i++) {
			sql.append(",").append(officeIds.get(i));
		}

		sql.append(")");

		List<Map> ct = null;

		try {
			ct = workDealInfoDao.findBySQLListMap(sql.toString(), 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Integer count = new Integer(ct.get(0).get("CT").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断dealInfo是否需要结算 前提：dealInfo存有代理商信息、关联了证书信息，证书一定有renewalPrevId
	 * 规则@see{WorkDealInfo}
	 * 
	 * @param dealInfoId
	 * @return
	 */
	public void checkWorkDealInfoNeedSettle(WorkDealInfo dealInfo) {
		try {
			// 结算次数
			int contractSettleTime = getSettleTimes(dealInfo
					.getConfigCommercialAgent().getAgentContractStart(),
					dealInfo.getConfigCommercialAgent().getAgentContractEnd());
			if (dealInfo.getWorkPayInfo().getMethodGov()
					|| dealInfo.getWorkPayInfo().getMethodContract()) {
				dealInfo.setCanSettle(false);
				return;
			}

			if (dealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {// 新增一定结算
				dealInfo.setCanSettle(true);
				// year和最大次数比较取小的
				if (dealInfo.getYear() > contractSettleTime) {
					dealInfo.setPhysicalLife(contractSettleTime);
					// dealInfo.setResidualLife(0);
					dealInfo.setResidualLife(dealInfo.getYear()
							- contractSettleTime);
				} else {
					dealInfo.setPhysicalLife(dealInfo.getYear());
					dealInfo.setResidualLife(0);
					// dealInfo.setResidualLife(contractSettleTime -
					// dealInfo.getYear());
				}
				return;

			}
			if (dealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| dealInfo.getDealInfoType1().equals(
							WorkDealInfoType.TYPE_UPDATE_CERT)
					|| dealInfo.getDealInfoType2().equals(
							WorkDealInfoType.TYPE_UPDATE_CERT)
					|| dealInfo.getDealInfoType3().equals(
							WorkDealInfoType.TYPE_UPDATE_CERT)) {
				WorkDealInfo firstDealInfo = getFirstCertDealInfo(dealInfo,
						dealInfo);// 获取第一张被结算的证书业务
				if (firstDealInfo.getConfigCommercialAgent().getId()
						.equals(dealInfo.getConfigCommercialAgent().getId())) {// 判断是否是同一代理商
					WorkCertInfo firstCertInfo = firstDealInfo
							.getWorkCertInfo();
					// 已结算次数
					int certSettleTime = getCertSettleTimes(
							firstCertInfo.getSignDate(), new Date());
					if (contractSettleTime > certSettleTime) {
						// contractSettleTime - certSettleTime跟year比较取小的
						dealInfo.setSettledLife(certSettleTime);
						if (dealInfo.getYear() > (contractSettleTime - certSettleTime)) {
							dealInfo.setPhysicalLife(Math
									.abs(contractSettleTime - certSettleTime));
							// dealInfo.setResidualLife(0);
							dealInfo.setResidualLife(dealInfo.getYear()
									- contractSettleTime - certSettleTime);
						} else {
							dealInfo.setPhysicalLife(dealInfo.getYear());
							// dealInfo.setResidualLife(contractSettleTime -
							// certSettleTime - dealInfo.getYear());
							dealInfo.setResidualLife(0);
						}
						dealInfo.setCanSettle(true);
					} else {
						dealInfo.setCanSettle(false);
						return;
					}
				} else {
					dealInfo.setCanSettle(false);
				}
			} else {
				dealInfo.setCanSettle(false);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			dealInfo.setCanSettle(false);
		}
	}

	public List<WorkDealInfo> findByDealInfoId(Long dealInfoId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("id", dealInfoId));
		return workDealInfoDao.find(dc);
	}

	/**
	 * 得到第一张被结算的业务
	 * 
	 * @param dealInfo
	 * @param curFirstSettleDealInfo
	 *            当前最早一笔的可结算业务
	 * @return
	 */
	private WorkDealInfo getFirstCertDealInfo(WorkDealInfo dealInfo,
			WorkDealInfo curFirstSettleDealInfo) {
		if (dealInfo.getPrevId() == null) {// 不需要判断是否是政府统一采购or合同采购，如果没有前一笔业务?通常是递归中找到了新增业务，前一个嵌套中已经确定这笔新增付款方式是结算的
			return curFirstSettleDealInfo;
		}
		WorkDealInfo prevDealInfo = workDealInfoDao.findOne(dealInfo
				.getPrevId());
		if (prevDealInfo.getWorkPayInfo().getMethodGov()
				|| prevDealInfo.getWorkPayInfo().getMethodContract()) {// 如果前一笔业务是是政府统一采购，那结算从这一笔开始
			curFirstSettleDealInfo = dealInfo;
			return getFirstCertDealInfo(prevDealInfo, curFirstSettleDealInfo);
		}
		// 只有新增和更新结算
		List<Integer> settleType = new ArrayList<Integer>();
		settleType.add(WorkDealInfoType.TYPE_ADD_CERT);
		settleType.add(WorkDealInfoType.TYPE_UPDATE_CERT);
		// 如果不是更新和新增 继续往前追
		if (!settleType.contains(prevDealInfo.getDealInfoType())
				&& !settleType.contains(prevDealInfo.getDealInfoType1())) {
			return getFirstCertDealInfo(prevDealInfo, curFirstSettleDealInfo);
		}
		if (prevDealInfo.getDealInfoType().equals(
				WorkDealInfoType.TYPE_ADD_CERT)) {// 新增一定是第一张结算证书,上面已规避付款方式问题，是否和第一个判断重复?
			if (!prevDealInfo.getCanSettle()) {// 退费导致新增补结算
				return curFirstSettleDealInfo;
			}
			return prevDealInfo;
		}
		// 前一笔业务是结算的更新业务,更新前一笔业务可能会有 补办、变更等等...这里要记录当前可结算的业务
		// 有可能新增业务是政府统一采购、合同采购，所以第一笔结算业务可能是新增
		if (prevDealInfo.getCanSettle()
				&& (prevDealInfo.getDealInfoType().equals(
						WorkDealInfoType.TYPE_UPDATE_CERT) || prevDealInfo
						.getDealInfoType1().equals(
								WorkDealInfoType.TYPE_UPDATE_CERT))) {
			curFirstSettleDealInfo = prevDealInfo;
		}

		return getFirstCertDealInfo(prevDealInfo, curFirstSettleDealInfo);
	}

	/**
	 * 根据合同开始结束时间计算结算次数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private Integer getSettleTimes(Date start, Date end) {
		if (start == null || end == null) {
			return 0;
		}
		Long startMill = start.getTime();
		Long endMill = end.getTime();
		if ((endMill - startMill) % YEAR_MILL == 0) {
			return (int) ((startMill - endMill) / YEAR_MILL);
		}
		return (int) ((end.getTime() - start.getTime()) / YEAR_MILL) + 1;
	}

	/**
	 * 计算证书已结算的次数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private Integer getCertSettleTimes(Date start, Date end) {
		if (start == null || end == null) {
			return 0;
		}
		return (int) ((start.getTime() - end.getTime()) / YEAR_MILL);
	}

	/**
	 * 查询office当前证书发放量 定时任务用
	 * 
	 * @param date
	 * @param officeId
	 * @return
	 */
	public int getCertPublishTimes(Date date, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("obtainedDate", date));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCountByFirstCertSN(String firstCertSN) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("firstCertSN", firstCertSN));
		return (int) workDealInfoDao.count(dc);
	}

	@Transactional(readOnly = false)
	public void modifySvn(Long id, String svn) {
		String sql = "update work_deal_info set svn='" + svn + "' where id="
				+ id;
		workDealInfoDao.exeSql(sql);
	}

	@Transactional(readOnly = false)
	public void modifyFirstCertSN(Long id, String firstCertSN) {
		try {
			String sql = "update work_deal_info set FIRST_CERT_SN='"
					+ firstCertSN + "' where id=" + id;
			workDealInfoDao.exeSql(sql);
		} catch (Exception e) {
		}
	}

	@Transactional(readOnly = false)
	public void modifyFirstCertSN(String oldFirstCertSN, String newFIrstCertSN) {
		String sql = "update work_deal_info set FIRST_CERT_SN='"
				+ newFIrstCertSN + "' where FIRST_CERT_SN='" + oldFirstCertSN
				+ "'";
		workDealInfoDao.exeSql(sql);
	}

	// @Transactional(readOnly = false)
	public void processSinglePreid(String firstCertSN) {
		List<WorkDealInfo> lst = findByFirstCertSN(firstCertSN);

		// 只有一条的情况
		if (lst != null && lst.size() == 1) {
			WorkDealInfo po = lst.get(0);
			basicInfoSccaService
					.deleteImpTempByFirstCertSN(po.getFirstCertSN());
			if (po.getPrevId() != null && po.getPrevId() > 0l) {
				// 如果存在prev_id,则认为是错位数据,将其首证书序列号按上一条调整
				WorkDealInfo pre = get(po.getPrevId());
				if (pre == null)
					return;
				String sql = "update work_deal_info set DEL_FLAG='0',FIRST_CERT_SN='"
						+ pre.getFirstCertSN() + "' where id=" + po.getId();
				try {
					workDealInfoDao.exeSql(sql);
				} catch (Exception e) {
					exLog.error(StringHelper.getStackInfo(e));
				}
				// 更新上一条的del_flag
				String sql2 = "update work_deal_info set DEL_FLAG='1' where id="
						+ pre.getId();
				try {
					workDealInfoDao.exeSql(sql2);
				} catch (Exception e) {
					exLog.error(StringHelper.getStackInfo(e));
				}
				return;
			}
			String sql = "update work_deal_info set DEL_FLAG='0' where id="
					+ po.getId();
			try {
				workDealInfoDao.exeSql(sql);
			} catch (Exception e) {
				exLog.error(StringHelper.getStackInfo(e));
			}
			return;
		}

		for (int i = 0; i < lst.size(); i++) {
			String sql = "";
			WorkDealInfo po = null;
			try {
				po = lst.get(i);
				WorkDealInfo pre = findPreByFirstCertSN(po);
				if (pre == null && i != (lst.size() - 1)) {
					continue;
				}
				if (pre != null) {
					po.setPrevId(pre.getId());
					po.setFirstCertSN(pre.getFirstCertSN());
				}
				if (i == 0) {
					po.setDelFlag("0");
				} else {
					po.setDelFlag("1");
				}

				// workDealInfoDao.save(po);
				sql = "update work_deal_info set DEL_FLAG='" + po.getDelFlag()
						+ "',prev_id=" + po.getPrevId() + ",FIRST_CERT_SN='"
						+ po.getFirstCertSN() + "' where id=" + po.getId();
			} catch (Exception ex) {
				ex.printStackTrace();
				exLog.error(StringHelper.getStackInfo(ex));
				continue;
			}

			try {
				workDealInfoDao.exeSql(sql);
				basicInfoSccaService.deleteImpTempByFirstCertSN(po
						.getFirstCertSN());
			} catch (Exception ex) {
				continue;
			}

		}
	}

	/**
	 * 补零处理,如果是0-7开头，不补0，从8到F开头，补两个0
	 * 
	 * @param firstCertSN
	 * @return String
	 */
	private String zeroProcess(String firstCertSN) {
		//
		String temp = firstCertSN;
		String f = firstCertSN.substring(0, 1);
		Integer first = 100;
		if (StringHelper.isDigit(f)) {
			first = new Integer(firstCertSN.substring(0, 1));
		}
		if (first.intValue() >= 8) {
			temp = "00" + firstCertSN;
		}
		return temp;
	}

	// @Transactional(readOnly = false)
	public void processPreId(List<String> all) {
		for (String e : all) {
			// 根据首张证书序列判定是否需要处理workDealInfo表内的preId字段
			if (StringHelper.isNull(e))
				continue;
			// 处理每个唯一证书序列号
			try {
				processSinglePreid(e);
			} catch (Exception ex) {
				ex.printStackTrace();
				exLog.error(StringHelper.getStackInfo(ex));
			}
		}

	}

	public int getCertPublishCount(Date date, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("officeId", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		// 更新除外 更新另算
		dc.add(Restrictions.or(Restrictions.isNull("dealInfoType"),
				Restrictions.ne("dealInfoType",
						WorkDealInfoType.TYPE_UPDATE_CERT)));

		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));

		dc.add(Restrictions.ge("businessCardUserDate", date));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		date = calendar.getTime();

		dc.add(Restrictions.lt("businessCardUserDate", date));

		dc.add(Restrictions.isNull("dealInfoType3"));
		List<WorkDealInfo> dealinfos = workDealInfoDao.find(dc);
		int createCount = dealinfos.size();

		dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE));
		dc.add(Restrictions.ge("obtainedDate", date));
		int dealInfoType = WorkDealInfoType.TYPE_RETURN_WORK;
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType), Restrictions
						.eq("dealInfoType2", dealInfoType), Restrictions.eq(
						"dealInfoType3", dealInfoType), Restrictions.eq(
						"dealInfoType", WorkDealInfoType.TYPE_PAY_REPLACED))); // 类型是退费或者变更，都要剔除掉
		dc.add(Restrictions.lt("obtainedDate", calendar.getTime()));
		int revertCount = (int) workDealInfoDao.count(dc);

		return createCount - revertCount;
	}

	public int getCertPublishTimes(Date date, Long officeId, Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("obtainedDate", date));
		dc.add(Restrictions.le("obtainedDate", endDate));
		return (int) workDealInfoDao.count(dc);
	}

	/**
	 * 查询应用下，key出库的数量
	 * 
	 * @param date
	 * @param officeId
	 * @param appId
	 * @return
	 */
	public int getKeyPublishTimes(Date date, Long officeId, Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("office.id", officeId));
		// // 新增、置换时才有key
		// dc.add(Restrictions.or(Restrictions.eq("dealInfoType",
		// WorkDealInfoType.TYPE_ADD_CERT), Restrictions.eq(
		// "dealInfoType", WorkDealInfoType.TYPE_DAMAGED_REPLACED),
		// Restrictions.eq("dealInfoType1",
		// WorkDealInfoType.TYPE_DAMAGED_REPLACED)));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.ge("obtainedDate", date));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		List<String> keys = new ArrayList<String>();
		for (WorkDealInfo workDealInfo : dealInfos) {
			if (!keys.contains(workDealInfo.getKeySn())) {
				keys.add(workDealInfo.getKeySn());
			}
		}
		return keys.size();
	}

	public int getKeyPublishTimes(Date date, Long officeId, Long appId,
			Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("office.id", officeId));
		// // 新增、置换时才有key
		// dc.add(Restrictions.or(Restrictions.eq("dealInfoType",
		// WorkDealInfoType.TYPE_ADD_CERT), Restrictions.eq(
		// "dealInfoType", WorkDealInfoType.TYPE_DAMAGED_REPLACED),
		// Restrictions.eq("dealInfoType1",
		// WorkDealInfoType.TYPE_DAMAGED_REPLACED)));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.ge("obtainedDate", date));
		dc.add(Restrictions.le("obtainedDate", endDate));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		List<String> keys = new ArrayList<String>();
		for (WorkDealInfo workDealInfo : dealInfos) {
			if (!keys.contains(workDealInfo.getKeySn())) {
				keys.add(workDealInfo.getKeySn());
			}
		}
		return keys.size();
	}

	/**
	 * 查询网点某个应用 业务类型数量
	 * 
	 * @param date
	 * @param officeId
	 * @param year
	 * @param appId
	 * @param dealInfoType
	 * @return
	 */
	public int getCertAppYearInfo(Date date, Long officeId, Integer year,
			Long appId, Integer dealInfoType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("obtainedDate", date));

		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType)));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoCount(Date date, Long officeId, Integer year,
			Long appId, Integer dealInfoType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");

		// 网点
		dc.add(Restrictions.eq("officeId", officeId));
		// 应用
		dc.add(Restrictions.eq("configApp.id", appId));
		// 制证日期
		dc.add(Restrictions.ge("businessCardUserDate", date)); // 改正
																// 之前用updatedate查
		// 制证日期 // 改为制证时间
		dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));
		// 不包含迁移数据
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));
		// 年限
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		
		dc.add(Restrictions.eq("dealInfoType", dealInfoType));
		
		dc.add(Restrictions.isNull("dealInfoType1"));
		dc.add(Restrictions.isNull("dealInfoType2"));
		dc.add(Restrictions.isNull("dealInfoType3"));

		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		System.out.println(workDealInfoDao.count(dc));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoCountOneDeal(Date date, Long officeId,
			Integer year, Long appId, Integer dealInfoType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");

		// 网点
		dc.add(Restrictions.eq("officeId", officeId));

		// 应用
		dc.add(Restrictions.eq("configApp.id", appId));

		// 年限
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}

		// 迁移除外
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));

		List<String> statusIntegers = new ArrayList<String>();
		if (dealInfoType.equals(1)) {
			dc.add(Restrictions.eq("dealInfoType", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType1"));
			dc.add(Restrictions.isNull("dealInfoType2"));
			dc.add(Restrictions.isNull("dealInfoType3"));
			//状态为已验证待制证
			statusIntegers.add(WorkDealInfoStatus.STATUS_APPROVE_WAIT);
			//按验证时间查询
			dc.add(Restrictions.ge("verifyUserDate", date));
			dc.add(Restrictions.lt("verifyUserDate", calendar.getTime()));
		} else if (dealInfoType.equals(4)) {
			dc.add(Restrictions.eq("dealInfoType2", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType"));
			dc.add(Restrictions.isNull("dealInfoType1"));
			dc.add(Restrictions.isNull("dealInfoType3"));
	
			dc.add(Restrictions.ge("businessCardUserDate", date));
			dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));

		} else if (dealInfoType.equals(2) || dealInfoType.equals(3)) {
			dc.add(Restrictions.eq("dealInfoType1", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType"));
			dc.add(Restrictions.isNull("dealInfoType2"));
			dc.add(Restrictions.isNull("dealInfoType3"));

			dc.add(Restrictions.ge("businessCardUserDate", date));
			dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));
		}

		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoUpdateChangeNum(Date date, Long officeId,
			Integer year, Long appId, Integer dealInfoTypeUpdate,
			Integer dealInfoTypeChange) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		
		// 迁移除外
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));

		//更新+变更 和 更新一样,验证之后就算
		dc.add(Restrictions.ge("verifyUserDate", date));
		dc.add(Restrictions.lt("verifyUserDate", calendar.getTime()));

		dc.add(Restrictions.eq("dealInfoType", dealInfoTypeUpdate));
		dc.add(Restrictions.isNull("dealInfoType1"));
		dc.add(Restrictions.eq("dealInfoType2", dealInfoTypeChange));
		dc.add(Restrictions.isNull("dealInfoType3"));

		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}

		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		statusIntegers.add(WorkDealInfoStatus.STATUS_APPROVE_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoUpdateLostReplaceNum(Date date, Long officeId,
			Integer year, Long appId, Integer dealInfoTypeUpdate,
			Integer dealInfoTypeLost) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		
		// 迁移除外
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
			Restrictions.isNull("isSJQY")));
		
		dc.add(Restrictions.ge("businessCardUserDate", date));
		dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));

		dc.add(Restrictions.eq("dealInfoType", dealInfoTypeUpdate));
		dc.add(Restrictions.eq("dealInfoType1", dealInfoTypeLost));
		dc.add(Restrictions.isNull("dealInfoType2"));
		dc.add(Restrictions.isNull("dealInfoType3"));

		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}

		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoChangeLostReplaceNum(Date date, Long officeId,
			Integer year, Long appId, Integer dealInfoTypeChange,
			Integer dealInfoTypeLostReplace) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		
		
		
		// 迁移除外
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
			Restrictions.isNull("isSJQY")));
		
		dc.add(Restrictions.ge("businessCardUserDate", date));
		dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));

		dc.add(Restrictions.eq("dealInfoType1", dealInfoTypeLostReplace));

		dc.add(Restrictions.eq("dealInfoType2", dealInfoTypeChange));
		dc.add(Restrictions.isNull("dealInfoType"));
		dc.add(Restrictions.isNull("dealInfoType3"));

		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}

		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoChangeLostReplaceUpdateNum(Date date,
			Long officeId, Integer year, Long appId,
			Integer dealInfoTypeChange, Integer dealInfoTypeLostReplace,
			Integer dealInfoTypeUpdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		
		// 迁移除外
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
			Restrictions.isNull("isSJQY")));
		
		
		dc.add(Restrictions.ge("businessCardUserDate", date));
		dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));
		dc.add(Restrictions.eq("dealInfoType", dealInfoTypeUpdate));
		dc.add(Restrictions.eq("dealInfoType1", dealInfoTypeLostReplace));
		dc.add(Restrictions.eq("dealInfoType2", dealInfoTypeChange));
		dc.add(Restrictions.isNull("dealInfoType3"));

		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}

		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfo(Date date, Long officeId, Integer year,
			Long appId, Integer dealInfoType, Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("obtainedDate", date));
		dc.add(Restrictions.le("obtainedDate", endDate));
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType)));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	/**
	 * 某个产品某种类型，某种业务的数量 只统计新增或更新！
	 * 
	 * @param date
	 * @param officeId
	 * @param year
	 * @param appId
	 * @param dealInfoType
	 * @param productType
	 * @param payType
	 *            0:自费 1:政府 2:合同
	 * @return
	 */
	public int getCertAppYearInfo(Date date, Long officeId, Integer year,
			Long appId, Integer dealInfoType, String productType,
			Integer payType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.ge("obtainedDate", date));
		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		if (productType != null) {
			dc.add(Restrictions.eq("configProduct.productName", productType));
		}
		// dc.add(Restrictions.in("configProduct.productName", productType));
		if (dealInfoType != null) {
			dc.add(Restrictions.or(
					Restrictions.eq("dealInfoType", dealInfoType),
					Restrictions.eq("dealInfoType1", dealInfoType)));
		} else {
			List<Integer> dealInfoTypes = new ArrayList<Integer>();
			dealInfoTypes.add(WorkDealInfoType.TYPE_ADD_CERT);
			dealInfoTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT);
			dc.add(Restrictions.or(
					Restrictions.in("dealInfoType", dealInfoTypes),
					Restrictions.in("dealInfoType1", dealInfoTypes)));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);

		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		if (payType != null) {
			if (payType.equals(1)) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (payType.equals(2)) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
			if (payType.equals(0)) {
				dc.add(Restrictions.and(
						Restrictions.ne("workPayInfo.methodGov", true),
						Restrictions.ne("workPayInfo.methodContract", true)));
			}
		}
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfo(Date date, Long officeId, Integer year,
			Long appId, Integer dealInfoType, List<String> productType,
			Integer payType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.ge("obtainedDate", date));
		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.in("configProduct.productName", productType));
		if (dealInfoType != null) {
			dc.add(Restrictions.or(
					Restrictions.eq("dealInfoType", dealInfoType),
					Restrictions.eq("dealInfoType1", dealInfoType)));
		} else {
			List<Integer> dealInfoTypes = new ArrayList<Integer>();
			dealInfoTypes.add(WorkDealInfoType.TYPE_ADD_CERT);
			dealInfoTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT);
			dc.add(Restrictions.or(
					Restrictions.in("dealInfoType", dealInfoTypes),
					Restrictions.in("dealInfoType1", dealInfoTypes)));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);

		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		if (payType != null) {
			if (payType.equals(1)) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (payType.equals(2)) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
			if (payType.equals(0)) {
				dc.add(Restrictions.and(
						Restrictions.ne("workPayInfo.methodGov", true),
						Restrictions.ne("workPayInfo.methodContract", true)));
			}
		}
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfo(Date date, Long officeId, Integer year,
			Long appId, Integer dealInfoType, List<String> productType,
			Integer payType, Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
		dc.add(Restrictions.ge("obtainedDate", date));
		dc.add(Restrictions.le("obtainedDate", endDate));
		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.in("configProduct.productName", productType));
		if (dealInfoType != null) {
			dc.add(Restrictions.or(
					Restrictions.eq("dealInfoType", dealInfoType),
					Restrictions.eq("dealInfoType1", dealInfoType)));
		} else {
			List<Integer> dealInfoTypes = new ArrayList<Integer>();
			dealInfoTypes.add(WorkDealInfoType.TYPE_ADD_CERT);
			dealInfoTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT);
			dc.add(Restrictions.or(
					Restrictions.in("dealInfoType", dealInfoTypes),
					Restrictions.in("dealInfoType1", dealInfoTypes)));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);

		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		if (payType != null) {
			if (payType.equals(1)) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (payType.equals(2)) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
			if (payType.equals(0)) {
				dc.add(Restrictions.and(
						Restrictions.ne("workPayInfo.methodGov", true),
						Restrictions.ne("workPayInfo.methodContract", true)));
			}
		}
		return (int) workDealInfoDao.count(dc);
	}

	public double getWorkPayMoney(Date date, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("obtainedDate", date));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalMoney = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
				totalMoney += dealInfo.getWorkPayInfo().getWorkPayedMoney();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalMoney;
	}

	public double getWorkPayMoney(Date date, Long officeId, Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("obtainedDate", date));
		dc.add(Restrictions.le("obtainedDate", endDate));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalMoney = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
				totalMoney += dealInfo.getWorkPayInfo().getWorkPayedMoney();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalMoney;
	}

	/**
	 * 获得应用发票总量
	 * 
	 * @param date
	 * @param officeId
	 * @param appId
	 * @return
	 */
	public double getWorkPayReceipt(Date date, Long officeId, Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.ge("obtainedDate", date));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalReceipt = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
				totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalReceipt;
	}

	public double getWorkPayReceiptCount(Date date, Long officeId, Long appId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workPayInfo", "workPayInfo");

		// 网点
		dc.add(Restrictions.eq("officeId", officeId));

		// 应用id
		dc.add(Restrictions.eq("configApp.id", appId));

		// 使用发票
		dc.add(Restrictions.eq("workPayInfo.userReceipt", true));

		// 非迁移
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));

		// 更新单算 其他一起算
		dc.add(Restrictions.or(Restrictions.and(Restrictions.and(Restrictions
				.or(Restrictions.ne("dealInfoType", 1),
						Restrictions.isNull("dealInfoType")), Restrictions.eq(
				"dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED)),
				Restrictions.and(
						Restrictions.ge("businessCardUserDate", date),
						Restrictions.lt("businessCardUserDate",
								calendar.getTime()))),
				Restrictions.and(Restrictions.and(Restrictions.eq(
						"dealInfoType", 1), Restrictions.or(Restrictions.eq(
						"dealInfoStatus",
						WorkDealInfoStatus.STATUS_APPROVE_WAIT), Restrictions
						.eq("dealInfoStatus",
								WorkDealInfoStatus.STATUS_CERT_OBTAINED))),
						Restrictions.and(
								Restrictions.ge("payUserDate", date),
								Restrictions.lt("payUserDate",
										calendar.getTime())))));

		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalReceipt = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {
				// 避免null的情况
				totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalReceipt;
	}

	public double getWorkPayReceipt(Date date, Long officeId, Long appId,
			Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.ge("obtainedDate", date));
		dc.add(Restrictions.le("obtainedDate", endDate));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalReceipt = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
				totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalReceipt;
	}

	/**
	 * 获得所有可统计结算的dealInfo
	 * 
	 * @return
	 */
	public List<WorkDealInfo> getAllObtainedDealInfo() {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		List<String> status = new ArrayList<String>();
		status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		status.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", status));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	/**
	 * 导入任务，根据首次证书的序列号查前一条记录
	 * 
	 * @param curretPo
	 * @return WorkDealInfo
	 */
	@Transactional(readOnly = true)
	public WorkDealInfo findPreByFirstCertSN(WorkDealInfo current) {

		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.lt("createDate", current.getCreateDate()));
		dc.add(Restrictions.eq("firstCertSN", current.getFirstCertSN()));
		dc.addOrder(Order.desc("createDate"));

		List<WorkDealInfo> lst = workDealInfoDao.find(dc);
		if (lst == null || lst.size() <= 0) {
			return null;
		}
		return lst.get(0);
	}

	/**
	 * 获取某个应用下当前有效的移动设备数量
	 * 
	 * @return
	 */
	public Integer getValidDeviceTotal(Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq("delFlag", WorkDealInfo.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("configApp.id", appId));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		Integer total = 0;
		for (WorkDealInfo workDealInfo : dealInfos) {
			try {
				total += workDealInfo.getWorkCertInfo().getTrustDeviceCount();
			} catch (Exception e) {
				continue;
			}
		}
		return total;
	}

	/**
	 * 获取当前需要创建自动更新服务的业务
	 * 
	 * @return
	 */
	public List<WorkDealInfo> getCurValidAddDealInfo() {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_ADD_CERT));
		dc.add(Restrictions.eq("delFlag", WorkDealInfo.DEL_FLAG_NORMAL));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		return dealInfos;
	}

	public List<WorkDealInfo> selectByApp(List<ConfigApp> configApps) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.in("configApp", configApps));
		// dc.add(Restrictions.ge("workCertInfo.notafter", new Date()));
		dc.add(Restrictions.ge("notafter", new Date()));
		return workDealInfoDao.find(dc);
	}

	/**
	 * 查询所有申请可信设备序号
	 * 
	 * @date:2014年8月21日
	 * @user:京涛
	 * @return_type:int
	 */
	public int countClientInfoTrustApplyAmount(String snStr) {
		DetachedCriteria dc = workCertTrustApplyDao.createDetachedCriteria();
		dc.add(Restrictions.like("sn", EscapeUtil.escapeLike(snStr) + "%"));
		return (int) workCertTrustApplyDao.count(dc);
	}

	public List<WorkDealInfo> findList() {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		// dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.eq("status", 0));
		// dc.add(Restrictions.eq("workPayInfo.delFlag",
		// WorkPayInfo.DEL_FLAG_NORMAL));
		// dc.add(Restrictions.eq("dealInfoStatus",
		// WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(dc);
	}

	public void exportExcelData(HttpServletRequest request,
			HttpServletResponse response) {
		String[] title = { "付款单位名称", "付款金额/元", "联系人", "联系方式", "付款时间", "付款方式",
				"区域", "网点", "记录人员" };
		String sheetName = "dealpaylist.xls";
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Type", "application/force-download");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(sheetName.getBytes("utf-8"), "ISO8859-1"));
			XSSFWorkbook wb = CreateExcelUtils.createExcel(title);
			wb = createDS(wb, findList());
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.close();
		} catch (Exception ex) {
			ex.getMessage();
			return;
		}
	}

	public XSSFWorkbook createDS(XSSFWorkbook wb, Collection<WorkDealInfo> coll)
			throws Exception {
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		WorkDealInfo workDealInfo = null;
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		try {
			ArrayList<WorkDealInfo> workDealInfoList = (ArrayList<WorkDealInfo>) coll;
			sheet = wb.getSheetAt(0);
			int collSize = workDealInfoList.size() + 1;
			for (int i = 0; i < collSize; i++) {
				if (i == 0) {
					continue;
				}
				row = sheet.createRow(i);
				workDealInfo = workDealInfoList.get(0);
				for (int j = 0; j < CELL_NUM; j++) {
					cell = row.createCell(j);
					switch (j) {
					case 0:
						cell.setCellValue(workDealInfo.getWorkCompany()
								.getCompanyName());
						break;
					case 1:
						cell.setCellValue(workDealInfo.getWorkPayInfo()
								.getWorkPayedMoney());
						break;
					case 2:
						cell.setCellValue(workDealInfo.getWorkUser()
								.getContactName());
						break;
					case 3:
						cell.setCellValue(workDealInfo.getWorkUser()
								.getContactPhone());
						break;
					case 4:
						cell.setCellValue(date.format(workDealInfo
								.getWorkPayInfo().getCreateDate()));
						break;
					case 5:
						cell.setCellValue(paymentMethod(
								new boolean[] {
										workDealInfo.getWorkPayInfo()
												.getMethodPos(),
										workDealInfo.getWorkPayInfo()
												.getMethodMoney(),
										workDealInfo.getWorkPayInfo()
												.getMethodBank(),
										workDealInfo.getWorkPayInfo()
												.getMethodAlipay(),
										workDealInfo.getWorkPayInfo()
												.getMethodContract(),
										workDealInfo.getWorkPayInfo()
												.getMethodGov() },
								workDealInfo.getWorkPayInfo()
										.getRelationMethod() != null ? workDealInfo
										.getWorkPayInfo().getRelationMethod()
										: 0));
						break;
					case 6:
						cell.setCellValue(workDealInfo.getCreateBy()
								.getOffice().getAreaName());
						break;
					case 7:
						cell.setCellValue(workDealInfo.getCreateBy()
								.getOffice().getName());
						break;
					case 8:
						cell.setCellValue(workDealInfo.getWorkPayInfo()
								.getCreateBy().getName());
						break;
					}
				}
				workDealInfoList.remove(0);
			}

		} catch (Exception ex) {
			ex.getMessage();
			throw new Exception();
		}
		return wb;
	}

	private String paymentMethod(boolean[] flag, Integer m) {
		StringBuffer sb = new StringBuffer();
		if (sb.length() == 0)
			if (flag[0]) {
				sb.append("POS收款");
			}
		if (sb.length() == 0)
			if (flag[1]) {
				sb.append("现金");
			}
		if (sb.length() == 0)
			if (flag[2]) {
				sb.append("银行转账");
			}
		if (sb.length() == 0)
			if (flag[3]) {
				sb.append("支付宝");
			}
		if (sb.length() == 0)
			if (flag[4]) {
				sb.append("合同采购");
			}
		if (sb.length() == 0)
			if (flag[5]) {
				sb.append("政府统一采购");
			}
		switch (m) {
		case 1:
			if (sb.length() == 0)
				sb.append("现金");
			break;
		case 2:
			if (sb.length() == 0)
				sb.append("POS收款");
			break;
		case 3:
			if (sb.length() == 0)
				sb.append("银行转账");
			break;
		case 4:
			if (sb.length() == 0)
				sb.append("支付宝转账");
			break;
		}
		if (sb.length() == 0) {
			sb.append("未知付款方式");
		}
		return sb.toString();
	}

	//
	@Transactional(readOnly = true)
	public List<WorkDealInfo> getWorkInfoByApp(Date date, ConfigApp configApp,
			String type) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configApp", configApp));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		Date startDate = null;
		Date dateEnd = null;
		List<WorkDealInfo> certInfos = new ArrayList<WorkDealInfo>();
		DetachedCriteria dcCert = workCertInfoDao.createDetachedCriteria();
		// 3个月内到期的证书
		if (type.equals("A3M")) {
			rightNow.add(Calendar.MONTH, 3);// 日期加3个月
			dateEnd = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", date, dateEnd));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
		}
		// 一个月内到期的证书
		if (type.equals("A1M")) {
			rightNow.add(Calendar.MONTH, 1);// 日期加1个月
			dateEnd = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", date, dateEnd));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
		}
		// 一个月过期期的证书
		if (type.equals("B1M")) {
			rightNow.add(Calendar.MONTH, -1);// 日期减1个月
			startDate = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, date));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
		}
		// 三个月过期期的证书
		if (type.equals("B3M")) {
			rightNow.add(Calendar.MONTH, -3);// 日期减3个月
			startDate = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, date));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
		}
		// 历史更新1年证书(已更新)
		if (type.equals("B1Y")) {
			rightNow.set(rightNow.get(Calendar.YEAR) - 1, 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR) - 1, 11, 31);
			dateEnd = rightNow.getTime();
			// dcCert.add(Restrictions.between("renewalDate", startDate,
			// dateEnd));
			// 证书更新日期用notberfore时间代替,因renewalDate没有值
			dcCert.add(Restrictions.between("notbefore", startDate, dateEnd));
			dcCert.add(Restrictions.isNotNull("renewalNextId"));
		}
		// 历史更新1年证书(应更新)
		if (type.equals("MB1Y")) {
			rightNow.set(rightNow.get(Calendar.YEAR) - 1, 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR) - 1, 11, 31);
			dateEnd = rightNow.getTime();
			// dcCert.add(Restrictions.between("renewalDate", startDate,
			// dateEnd));
			// 证书更新日期用notberfore时间代替,因renewalDate没有值
			dcCert.add(Restrictions.between("notbefore", startDate, dateEnd));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
			// 过滤没有被更新过
			dcCert.add(Restrictions.isNull("renewalNextId"));
		}
		// 历史更新2年证书(已更新)
		if (type.equals("B2Y")) {
			rightNow.set(rightNow.get(Calendar.YEAR) - 2, 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR) - 2, 11, 31);
			dateEnd = rightNow.getTime();
			// dcCert.add(Restrictions.between("renewalDate", startDate,
			// dateEnd));
			// 证书更新日期用notberfore时间代替,因renewalDate没有值
			dcCert.add(Restrictions.between("notbefore", startDate, dateEnd));
			dcCert.add(Restrictions.isNotNull("renewalPrevId"));
		}
		// 历史更新2年证书(应更新)
		if (type.equals("MB2Y")) {
			rightNow.set(rightNow.get(Calendar.YEAR) - 2, 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR) - 2, 11, 31);
			dateEnd = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, dateEnd));// notafter
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
			// 过滤没有被更新过
			dcCert.add(Restrictions.isNull("renewalNextId"));
		}
		// 历史更新3年证书(已更新)
		if (type.equals("B3Y")) {

			rightNow.set(rightNow.get(Calendar.YEAR) - 3, 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR) - 3, 11, 31);
			dateEnd = rightNow.getTime();
			// 证书更新日期用notberfore时间代替,因renewalDate没有值
			dcCert.add(Restrictions.between("notbefore", startDate, dateEnd));
			dcCert.add(Restrictions.isNotNull("renewalPrevId"));
			// dcCert.add(Restrictions.between("renewalDate", startDate,
			// dateEnd));
		}
		// 历史应该更新的 往前3年证书(应更新)
		if (type.equals("MB3Y")) {

			rightNow.set(rightNow.get(Calendar.YEAR) - 3, 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR) - 3, 11, 31);
			dateEnd = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, dateEnd));// notafter
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
			// 过滤没有被更新过
			dcCert.add(Restrictions.isNull("renewalNextId"));
		}
		// 本年内更新的
		if (type.equals("NEWY")) {
			rightNow.set(rightNow.get(Calendar.YEAR), 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR), 11, 31);
			dateEnd = rightNow.getTime();
			// dcCert.add(Restrictions.between("renewalDate", startDate,
			// dateEnd));
			// 证书更新日期用notberfore时间代替,因renewalDate没有值
			dcCert.add(Restrictions.between("notbefore", startDate, dateEnd));
			dcCert.add(Restrictions.isNotNull("renewalPrevId"));
		}
		// 本年内应该更新的
		if (type.equals("MNEWY")) {
			rightNow.set(rightNow.get(Calendar.YEAR), 0, 1);
			startDate = rightNow.getTime();
			rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(rightNow.get(Calendar.YEAR), 11, 31);
			dateEnd = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, dateEnd));// notafter
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"),
					Restrictions.ge("revokeDate", dateEnd)));
			// 过滤没有被更新过
			dcCert.add(Restrictions.isNull("renewalNextId"));
		}
		List<WorkCertInfo> workCertInfos = workCertInfoDao.find(dcCert);
		if (workCertInfos.size() > 0) {
			dc.add(Restrictions.in("workCertInfo", workCertInfos));
			certInfos = workDealInfoDao.find(dc);

		}
		return certInfos;

	}

	/**
	 * 
	 * @param yesterDay
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public int getCertPublishTimes(Date yesterDay, Date countDate, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("obtainedDate", yesterDay));
		dc.add(Restrictions.le("obtainedDate", countDate));
		return (int) workDealInfoDao.count(dc);
	}

	public double getWorkPayMoney(Date yesterDay, Date countDate, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("obtainedDate", yesterDay));
		dc.add(Restrictions.lt("obtainedDate", countDate));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalMoney = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
				totalMoney += dealInfo.getWorkPayInfo().getWorkPayedMoney();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalMoney;
	}

	public double getWorkPayMoneyCount(Date countDate, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("officeId", officeId));

		// 更新除外 更新另算
		dc.add(Restrictions.or(Restrictions.isNull("dealInfoType"),
				Restrictions.ne("dealInfoType",
						WorkDealInfoType.TYPE_UPDATE_CERT)));
		
		
		
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.ge("businessCardUserDate", countDate));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, 1);
		countDate = calendar.getTime();
		dc.add(Restrictions.lt("businessCardUserDate", countDate));
		dc.add(Restrictions.isNull("dealInfoType3"));
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalMoney = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {
				totalMoney += dealInfo.getWorkPayInfo().getWorkTotalMoney();

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalMoney;
	}

	public double getWorkPayMoneyCountByUpdate(Date countDate, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("officeId", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		// 状态为待制证或已获取
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.ge("payUserDate", countDate));

		// 更新单算
		dc.add(Restrictions.eq("dealInfoType",
				WorkDealInfoType.TYPE_UPDATE_CERT));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, 1);
		countDate = calendar.getTime();
		dc.add(Restrictions.lt("payUserDate", countDate));
		dc.add(Restrictions.isNull("dealInfoType3"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalMoney = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			totalMoney += dealInfo.getWorkPayInfo().getWorkTotalMoney();
		}
		return totalMoney;
	}

	public int getWorkPayCountByUpdate(Date countDate, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("officeId", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);

		// 状态为得到或待制证
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("payUserDate", countDate));

		// 业务类型为更新的 因为只算更新的
		dc.add(Restrictions.eq("dealInfoType",
				WorkDealInfoType.TYPE_UPDATE_CERT));
		dc.add(Restrictions.isNull("dealInfoType1"));
		dc.add(Restrictions.isNull("dealInfoType2"));
		dc.add(Restrictions.isNull("dealInfoType3"));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, 1);
		countDate = calendar.getTime();
		dc.add(Restrictions.lt("payUserDate", countDate));
		dc.add(Restrictions.isNull("dealInfoType3"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);

		return dealInfos.size();
	}

	public int getKeyPublishTimesCount(Date date, Long officeId, Long appId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		// dc.add(Restrictions.eq("office.id", officeId));
		// // 新增、置换时才有key
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType",
				WorkDealInfoType.TYPE_ADD_CERT), Restrictions.eq(
				"dealInfoType1", WorkDealInfoType.TYPE_DAMAGED_REPLACED),
				Restrictions.eq("dealInfoType1",
						WorkDealInfoType.TYPE_LOST_CHILD)));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.ge("businessCardUserDate", date));
		dc.add(Restrictions.lt("businessCardUserDate", calendar.getTime()));

		dc.add(Restrictions.or(Restrictions.isNull("isSJQY"),
				Restrictions.ne("isSJQY", 1)));

		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		List<String> keys = new ArrayList<String>();
		for (WorkDealInfo workDealInfo : dealInfos) {
			if (!keys.contains(workDealInfo.getKeySn())) {
				keys.add(workDealInfo.getKeySn());
			}

		}
		return keys.size();
	}

	// 根据单位名称，经办人查询出业务办理列表
	public List<WorkDealInfo> findByCompanyAttn(String companyString,
			String attn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();

		dc.createAlias("workUser", "workUser");
		if (attn != null && !attn.equals("")) {
			dc.add(Restrictions.like("workUser.contactName",
					"%" + EscapeUtil.escapeLike(attn) + "%"));
		}
		dc.createAlias("workCompany", "workCompany");
		if (companyString != null && !companyString.equals("")) {
			dc.add(Restrictions.like("workCompany.companyName", "%"
					+ EscapeUtil.escapeLike(companyString) + "%"));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据计费策略模版。判断是否计费策略模版办理过业务
	public List<WorkDealInfo> findByAgentId(Long agentId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configChargeAgentId", agentId));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public Integer afterDealInfoId(Long dealInfoId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.gt("id", dealInfoId));
		return workDealInfoDao.find(dc).size();
	}

	// 根据计费策略模版。判断是否计费策略模版办理过业务
	public List<WorkDealInfo> findByAgentIdProductId(Long agentId,
			Long productId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configChargeAgentId", agentId));
		dc.add(Restrictions.eq("configProduct.id", productId));
		dc.add(Restrictions.eq("delFlag", "0"));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	@Transactional(readOnly = false)
	public void deleteReturnById(Long id) {
		workDealInfoDao.deleteReturnById(id);
	}

	/**
	 * 根据组织机构代码获取多证书编号
	 * 
	 * @return
	 */
	public Integer getCertSortByOrganizationNumber(String organizationNumber,
			Integer productTdId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("workCompany.organizationNumber",
				organizationNumber));
		dc.add(Restrictions.eq("configProduct.productName",
				productTdId.toString()));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 0;
		} else {
			return dealInfos.get(0).getCertSort();
		}
	}

	/**
	 * 根据单位名称获取多证书编号
	 * 
	 * @return
	 */
	public Integer getCertSortByCompanyName(String companyName,
			Integer productTdId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("workCompany.companyName", companyName));
		dc.add(Restrictions.eq("configProduct.productName",
				productTdId.toString()));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 0;
		} else {
			return dealInfos.get(0).getCertSort();
		}
	}

	/**
	 * 个人证书根据持有人身份证号获取多证书编号
	 * 
	 * @return
	 */
	public Integer getCertSortByConCertNumber(String conCertNumber) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("workUser.conCertNumber", conCertNumber));
		dc.add(Restrictions.or(
				Restrictions.eq("configProduct.productName", "2"),
				Restrictions.eq("configProduct.productName", "6")));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 0;
		} else {
			return dealInfos.get(0).getCertSort();
		}
	}

	/**
	 * 个人证书根据持有人姓名获取多证书编号
	 * 
	 * @return
	 */
	public Integer getCertSortByContactName(String contactName) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("workUser.contactName", contactName));
		dc.add(Restrictions.or(
				Restrictions.eq("configProduct.productName", "2"),
				Restrictions.eq("configProduct.productName", "6")));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 0;
		} else {
			return dealInfos.get(0).getCertSort();
		}
	}

	@Transactional(readOnly = false)
	public JSONObject saveExcelDate(Long appId, String dealInfoType,
			String productT, String lableT, Integer agentIdT,
			Long agentDetailIdT, Integer yearT, Integer payMethodT,
			MultipartFile file, String ifExcel) {
		StringBuffer ifErr = new StringBuffer();
		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService
				.get(agentDetailIdT);
		ConfigChargeAgent agent = bound.getAgent();
		/*
		 * int pos = 0; int money = 0; int bank = 0; if
		 * (agent.getChargeMethodPos()) { pos = 1; } if
		 * (agent.getChargeMethodMoney()) { money = 1; } if
		 * (agent.getChargeMethodBank()) { bank = 1; } int total = pos + money +
		 * bank;
		 * 
		 * if (total != 1) { ifErr.append("该应用计费策略模板配置缴费方式不为一种，请确认！"); return
		 * ifErr(-1, ifErr.toString()); }
		 */
		/*
		 * 创建临时路径
		 */
		StringBuffer tempPath = new StringBuffer("E:/temp/");
		tempPath.append(System.currentTimeMillis()); // 获取系统以毫秒为单位的当前时间
		tempPath.append(ifExcel);// 获取参数@Param file的路径

		/*
		 * 创建临时excel文件存储@Param file数据
		 */
		File tempFile = new File(tempPath.toString());
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs(); // 获取父目录，创建
		}
		try {
			tempFile.createNewFile(); // 创建文件
			file.transferTo(tempFile); // 收到文件传输到目标文件
		} catch (IOException e) {
			ifErr.append("本地目录  E:/temp/ 不存在<br>");
			return ifErr(-1, ifErr.toString());
		}

		/*
		 * 解析@Param file文件
		 */
		Workbook wb = null;
		try {
			wb = createWB(tempFile.getAbsolutePath());
		} catch (Exception e) {
			ifErr.append("模板创建失败<br>");
			return ifErr(-1, ifErr.toString());
		}
		int rows = 0; // 总行数
		int cells = 0; // 总列数
		Sheet sheetAt0 = wb.getSheetAt(0); // 获取第一个工作表
		Row row = null;
		row = sheetAt0.getRow(1);
		if (!validTitle(row)) {
			ifErr.append("导入模板表头与下载模板不匹配，请确认！<br>");
			return ifErr(-1, ifErr.toString());
		}
		rows = sheetAt0.getPhysicalNumberOfRows();
		if (rows <= 2) {
			ifErr.append("模板中数据为空！<br>");
			return ifErr(-1, ifErr.toString());
		} else {
			int addNum = agent.getSurplusNum();

			if (addNum < (rows - 2) && !agent.getTempStyle().equals("1")) {
				ifErr.append("计费策略模板配置新增数量不足！<br>");
				return ifErr(-1, ifErr.toString());
			}
		}

		ConfigApp configApp = configAppService.get(appId);
		ConfigProduct configProduct = bound.getProduct();

		try {
			for (int i = 0; i < rows; i++) {

				if (i == 0 || i == 1) {
					continue;
				}
				row = sheetAt0.getRow(i);
				ConfigRaAccountExtendInfo configRaAccountExtendInfo = configRaAccountExtendInfoService
						.get(configProduct.getRaAccountExtedId());
				Set<String> extentInfos = RaAccountUtil
						.getExtendInfos(configRaAccountExtendInfo);
				for (String info : extentInfos) {
					if (info.equals("companyName")) {
						if (row.getCell(0) == null
								|| row.getCell(0).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1) + "行第1列单位名称不能为空！<br>");
						}
					} else if (info.equals("contactName")) {
						if (row.getCell(16) == null
								|| row.getCell(16).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1)
									+ "行第17列证书持有人名称不能为空！<br>");
						}
					} else if (info.equals("pName")) {
						if (row.getCell(23) == null
								|| row.getCell(23).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1) + "行第24列经办人姓名不能为空！<br>");
						}
					} else if (info.equals("organizationNumber")) {
						if (row.getCell(2) == null
								|| row.getCell(2).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1) + "行第3列组织机构代码不能为空！<br>");
						}
					} else if (info.equals("conCertNumber")) {
						if (row.getCell(18) == null
								|| row.getCell(18).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1)
									+ "行第19列证书持有人身份证号不能为空！<br>");
						}
					} else if (info.equals("contacEmail")) {
						if (row.getCell(19) == null
								|| row.getCell(19).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1)
									+ "行第20列证书持有人邮箱不能为空！<br>");
						}
					} else if (info.equals("pIDCard")) {
						if (row.getCell(24) == null
								|| row.getCell(24).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1)
									+ "行第25列经办人身份证号不能为空！<br>");
						}
					} else if (info.equals("contactTel")) {
						if (row.getCell(21) == null
								|| row.getCell(21).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1)
									+ "行第22列业务系统UID不能为空！<br>");
						}
					} else if (info.equals("comCertficateNumber")) {
						if (row.getCell(6) == null
								|| row.getCell(6).toString().replace(" ", "")
										.equals("")) {
							ifErr.append("第" + (i + 1) + "行第7列证件号不能为空！<br>");
						}
					}
				}

				SimpleDateFormat dnf = new SimpleDateFormat("yyyy-MM-dd");
				String orgExpirationTime = ""; // 组织机构代码有效期：
				if (row.getCell(3) != null
						&& !row.getCell(3).toString().replace(" ", "")
								.equals("")) {
					orgExpirationTime = row.getCell(3).toString()
							.replace(" ", "");
				}
				if (orgExpirationTime != null && !orgExpirationTime.equals("")) {
					try {
						Timestamp ts1 = new Timestamp(dnf.parse(
								orgExpirationTime).getTime());
					} catch (Exception e) {
						ifErr.append("第" + (i + 1) + "行第4列组织机构代码有效期格式不正确！<br>");
					}
				}
				String comCertficateTime = ""; // 单位证照有效期：
				if (row.getCell(7) != null
						&& !row.getCell(7).toString().replace(" ", "")
								.equals("")) {
					comCertficateTime = row.getCell(7).toString()
							.replace(" ", "");
				}
				if (comCertficateTime != null && !comCertficateTime.equals("")) {
					try {
						Timestamp ts = new Timestamp(dnf.parse(
								comCertficateTime).getTime());
					} catch (Exception e) {
						ifErr.append("第" + (i + 1) + "行第8列单位证照有效期格式不正确！<br>");
					}
				}
			}
			if (ifErr.toString().length() > 0) {
				return ifErr(-1, ifErr.toString());
			}

			for (int i = 0; i < rows; i++) {
				if (i == 0 || i == 1) {
					continue;
				}
				row = sheetAt0.getRow(i);

				String organizationNumber = "";
				if (row.getCell(2) != null
						&& !row.getCell(2).toString().replace(" ", "")
								.equals("")) {
					organizationNumber = row.getCell(2).toString()
							.replace(" ", "");
				}

				String companyName = "";
				if (row.getCell(0) != null
						&& !row.getCell(0).toString().replace(" ", "")
								.equals("")) {
					companyName = row.getCell(0).toString().replace(" ", "");
				}
				WorkCompany workCompany = workCompanyService
						.finByNameAndNumber(companyName, organizationNumber);

				if (companyName != null && !companyName.equals("")) {
					workCompany.setCompanyName(companyName);
				}
				String companyType = "";
				if (row.getCell(1) != null
						&& !row.getCell(1).toString().replace(" ", "")
								.equals("")) {
					companyType = row.getCell(1).toString().replace(" ", "");
				}
				if (companyType != null && !companyType.equals("")) {
					if (companyType.equals("企业")) {
						workCompany.setCompanyType("1");
					} else if (companyType.equals("事业单位")) {
						workCompany.setCompanyType("2");
					} else if (companyType.equals("政府机关")) {
						workCompany.setCompanyType("3");
					} else if (companyType.equals("社会团体")) {
						workCompany.setCompanyType("4");
					} else {
						workCompany.setCompanyType("5");
					}
				} else {
					workCompany.setCompanyType("1");
				}
				String comCertificateType = "";
				if (row.getCell(5) != null
						&& !row.getCell(5).toString().replace(" ", "")
								.equals("")) {
					comCertificateType = row.getCell(5).toString()
							.replace(" ", "");
				}
				if (comCertificateType != null
						&& !comCertificateType.equals("")) {
					if (comCertificateType.equals("营业执照")) {
						workCompany.setComCertificateType("0");
					} else if (comCertificateType.equals("事业单位法人登记证")) {
						workCompany.setComCertificateType("1");
					} else if (comCertificateType.equals("社会团体登记证")) {
						workCompany.setComCertificateType("2");
					} else {
						workCompany.setComCertificateType("3");
					}
				} else {
					workCompany.setComCertificateType("0");
				}
				SimpleDateFormat dnf = new SimpleDateFormat("yyyy-MM-dd");
				String orgExpirationTime = ""; // 组织机构代码有效期：
				if (row.getCell(3) != null
						&& !row.getCell(3).toString().replace(" ", "")
								.equals("")) {
					orgExpirationTime = row.getCell(3).toString()
							.replace(" ", "");
				}
				if (orgExpirationTime != null && !orgExpirationTime.equals("")) {
					Timestamp ts1 = new Timestamp(dnf.parse(orgExpirationTime)
							.getTime());
					workCompany.setOrgExpirationTime(ts1);
				}

				String comCertficateTime = ""; // 单位证照有效期：
				if (row.getCell(7) != null
						&& !row.getCell(7).toString().replace(" ", "")
								.equals("")) {
					comCertficateTime = row.getCell(7).toString()
							.replace(" ", "");
				}
				if (comCertficateTime != null && !comCertficateTime.equals("")) {
					Timestamp ts = new Timestamp(dnf.parse(comCertficateTime)
							.getTime());
					workCompany.setComCertficateTime(ts);
				}

				String comCertficateNumber = ""; // 证件号
				if (row.getCell(6) != null
						&& !row.getCell(6).toString().replace(" ", "")
								.equals("")) {
					comCertficateNumber = row.getCell(6).toString()
							.replace(" ", "");
				}
				if (comCertficateNumber != null
						&& !comCertficateNumber.equals("")) {
					workCompany.setComCertficateNumber(comCertficateNumber);
				}
				String selectLv = ""; // 服务级别
				if (row.getCell(4) != null
						&& !row.getCell(4).toString().replace(" ", "")
								.equals("")) {
					selectLv = row.getCell(4).toString().replace(" ", "");
				}
				if (selectLv != null && !selectLv.equals("")) {
					if (selectLv.equals("普通客户")) {
						workCompany.setSelectLv("1");
					} else {
						workCompany.setSelectLv("0");
					}
				} else {
					workCompany.setSelectLv("0");
				}
				if (organizationNumber != null
						&& !organizationNumber.equals("")) {
					workCompany.setOrganizationNumber(organizationNumber);
				}

				String s_province = ""; // 省
				if (row.getCell(9) != null
						&& !row.getCell(9).toString().replace(" ", "")
								.equals("")) {
					s_province = row.getCell(9).toString().replace(" ", "");
				}
				String s_city = ""; // 市
				if (row.getCell(10) != null
						&& !row.getCell(10).toString().replace(" ", "")
								.equals("")) {
					s_city = row.getCell(10).toString().replace(" ", "");
				}
				String s_county = ""; // 县
				if (row.getCell(11) != null
						&& !row.getCell(11).toString().replace(" ", "")
								.equals("")) {
					s_county = row.getCell(11).toString().replace(" ", "");
				}
				if (s_province != null && !s_province.equals("")) {
					workCompany.setProvince(s_province);
				}
				if (s_city != null && !s_city.equals("")) {
					workCompany.setCity(s_city);
				}
				if (s_county != null && !s_county.equals("")) {
					workCompany.setDistrict(s_county);
				}
				String areaRemark = ""; // 区域备注
				if (row.getCell(12) != null
						&& !row.getCell(12).toString().replace(" ", "")
								.equals("")) {
					areaRemark = row.getCell(12).toString().replace(" ", "");
				}
				if (areaRemark != null && !areaRemark.equals("")) {
					workCompany.setAreaRemark(areaRemark);
				}
				String legalName = ""; // 法人姓名
				if (row.getCell(8) != null
						&& !row.getCell(8).toString().replace(" ", "")
								.equals("")) {
					legalName = row.getCell(8).toString().replace(" ", "");
				}
				if (legalName != null && !legalName.equals("")) {
					workCompany.setLegalName(legalName);
				}

				String address = ""; // 街道地址：
				if (row.getCell(13) != null
						&& !row.getCell(13).toString().replace(" ", "")
								.equals("")) {
					address = row.getCell(13).toString().replace(" ", "");
				}
				if (address != null && !address.equals("")) {
					workCompany.setAddress(address);
				}
				String companyMobile = ""; // 单位联系电话：
				if (row.getCell(14) != null
						&& !row.getCell(14).toString().replace(" ", "")
								.equals("")) {
					companyMobile = row.getCell(14).toString().replace(" ", "");
				}
				if (companyMobile != null && !companyMobile.equals("")) {
					workCompany.setCompanyMobile(companyMobile);
				}
				String remarks = ""; // 备注信息
				if (row.getCell(15) != null
						&& !row.getCell(15).toString().replace(" ", "")
								.equals("")) {
					remarks = row.getCell(15).toString().replace(" ", "");
				}
				if (remarks != null && !remarks.equals("")) {
					workCompany.setRemarks(remarks);
				}
				// 保存经办人信息
				WorkUser workUser = new WorkUser();
				workUser.setStatus(1);

				String contactName = "";// 证书持有人姓名
				if (row.getCell(16) != null
						&& !row.getCell(16).toString().replace(" ", "")
								.equals("")) {
					contactName = row.getCell(16).toString().replace(" ", "");
				}
				if (contactName != null && !contactName.equals("")) {
					workUser.setContactName(contactName);
				}
				String conCertType = "";// 证书持有人证件
				if (row.getCell(17) != null
						&& !row.getCell(17).toString().replace(" ", "")
								.equals("")) {
					conCertType = row.getCell(17).toString().replace(" ", "");
				}
				if (conCertType != null && !conCertType.equals("")) {
					if (conCertType.equals("身份证")) {
						workUser.setConCertType("0");
					} else if (conCertType.equals("军官证")) {
						workUser.setConCertType("1");
					} else {
						workUser.setConCertType("2");
					}
				} else {
					workUser.setConCertType("0");
				}

				String conCertNumber = ""; // 证件号码
				if (row.getCell(18) != null
						&& !row.getCell(18).toString().replace(" ", "")
								.equals("")) {
					conCertNumber = row.getCell(18).toString().replace(" ", "");
				}
				if (conCertNumber != null && !conCertNumber.equals("")) {
					workUser.setConCertNumber(conCertNumber);
				}

				String contacEmail = ""; // 证书持有人电子邮件
				if (row.getCell(19) != null
						&& !row.getCell(19).toString().replace(" ", "")
								.equals("")) {
					contacEmail = row.getCell(19).toString().replace(" ", "");
				}
				if (contacEmail != null && !contacEmail.equals("")) {
					workUser.setContactEmail(contacEmail);
				}

				String contactPhone = ""; // 证书持有人手机号:
				if (row.getCell(20) != null
						&& !row.getCell(20).toString().replace(" ", "")
								.equals("")) {
					contactPhone = row.getCell(20).toString().replace(" ", "");
				}
				if (contactPhone != null && !contactPhone.equals("")) {
					workUser.setContactPhone(contactPhone);
				}

				String contactTel = ""; // 业务系统UID:
				if (row.getCell(21) != null
						&& !row.getCell(21).toString().replace(" ", "")
								.equals("")) {
					contactTel = row.getCell(21).toString().replace(" ", "");
				}
				if (contactTel != null && !contactTel.equals("")) {
					workUser.setContactTel(contactTel);
				}
				workUser.setWorkCompany(workCompany);

				String contactSex = ""; // 证书持有人性别:
				if (row.getCell(22) != null
						&& !row.getCell(22).toString().replace(" ", "")
								.equals("")) {
					contactSex = row.getCell(22).toString().replace(" ", "");
				}
				if (contactSex != null && !contactSex.equals("")) {
					workUser.setContactSex(contactSex);
				}
				WorkDealInfo workDealInfo = new WorkDealInfo();

				workDealInfo.setSvn(this.getSVN(0));

				workCompanyService.save(workCompany);
				workUserService.save(workUser);

				// 保存work_deal_info
				ConfigCommercialAgent commercialAgent = configAgentAppRelationService
						.findAgentByApp(configApp);
				workDealInfo.setConfigCommercialAgent(commercialAgent);
				List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
						.findByOffice(UserUtils.getUser().getOffice());
				if (configAgentOfficeRelations.size() > 0) {
					workDealInfo.setCommercialAgent(configAgentOfficeRelations
							.get(0).getConfigCommercialAgent());// 劳务关系外键
				}
				workDealInfo.setConfigApp(configApp);
				workDealInfo.setWorkUser(workUser);
				workDealInfo.setWorkCompany(workCompany);
				workDealInfo.setConfigProduct(configProduct);
				// 新增时扣减计费策略数量
				Integer reseNum = agent.getReserveNum();
				Integer surNum = agent.getSurplusNum();
				agent.setReserveNum(reseNum + 1);
				agent.setSurplusNum(surNum - 1);
				configChargeAgentService.save(agent);
				workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
				workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
				workDealInfo.setYear(yearT);
				workDealInfo
						.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
				workDealInfo.setCreateBy(UserUtils.getUser());
				workDealInfo.setCreateDate(new Date());
				workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));
				WorkCompanyHis workCompanyHis = workCompanyService
						.change(workCompany);
				workCompanyHisService.save(workCompanyHis);
				workDealInfo.setWorkCompanyHis(workCompanyHis);
				WorkUserHis workUserHis = workUserService.change(workUser,
						workCompanyHis);
				workUserHisService.save(workUserHis);
				workDealInfo.setWorkUserHis(workUserHis);
				// 保存申请人信息
				WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
				String pName = ""; // 经办人姓名
				if (row.getCell(23) != null
						&& !row.getCell(23).toString().replace(" ", "")
								.equals("")) {
					pName = row.getCell(23).toString().replace(" ", "");
				}
				if (pName != null && !pName.equals("")) {
					workCertApplyInfo.setName(pName);
				}
				String pIDCard = ""; // 身份证号
				if (row.getCell(24) != null
						&& !row.getCell(24).toString().replace(" ", "")
								.equals("")) {
					pIDCard = row.getCell(24).toString().replace(" ", "");
				}
				if (pIDCard != null && !pIDCard.equals("")) {
					workCertApplyInfo.setIdCard(pIDCard);
				}
				String pEmail = "";// 经办人邮箱
				if (row.getCell(25) != null
						&& !row.getCell(25).toString().replace(" ", "")
								.equals("")) {
					pEmail = row.getCell(25).toString().replace(" ", "");
				}
				if (pEmail != null && !pEmail.equals("")) {

					workCertApplyInfo.setEmail(pEmail);
				}
				workCertApplyInfoService.save(workCertApplyInfo);
				WorkCertInfo workCertInfo = new WorkCertInfo();
				workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
				workCertInfoService.save(workCertInfo);
				workDealInfo.setWorkCertInfo(workCertInfo);
				workDealInfo.setInputUser(UserUtils.getUser());
				workDealInfo.setPayUser(UserUtils.getUser());
				workDealInfo.setIsSJQY(2);
				workDealInfo.setAreaId(UserUtils.getUser().getOffice()
						.getParent().getId());
				workDealInfo.setOfficeId(UserUtils.getUser().getOffice()
						.getId());
				this.save(workDealInfo);
				ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
				dealInfoBound.setDealInfo(workDealInfo);
				ConfigChargeAgent agentBound = configChargeAgentService
						.get(workDealInfo.getConfigChargeAgentId());
				dealInfoBound.setAgent(agentBound);
				configAgentBoundDealInfoService.save(dealInfoBound);
				logUtil.saveSysLog("计费策略模版", "计费策略模版：" + agent.getId()
						+ "--业务编号：" + workDealInfo.getId() + "--关联成功!", "");
				// 录入人日志保存
				WorkLog workLog1 = new WorkLog();
				workLog1.setRecordContent("录入完毕");
				workLog1.setWorkDealInfo(workDealInfo);
				workLog1.setCreateDate(new Date());
				workLog1.setCreateBy(UserUtils.getUser());
				workLog1.setConfigApp(configApp);
				workLog1.setWorkCompany(workCompany);
				workLog1.setOffice(UserUtils.getUser().getOffice());
				workLogService.save(workLog1);

				WorkPayInfo workPayInfo = new WorkPayInfo();
				Double openAccountMoney = configChargeAgentDetailService
						.getChargeMoney(
								workDealInfo.getConfigChargeAgentId(),
								WorkDealInfoType
										.getWorkType(WorkDealInfoType.TYPE_OPEN_ACCOUNT),
								null);
				workPayInfo.setOpenAccountMoney(openAccountMoney);

				Double addCert = configChargeAgentDetailService.getChargeMoney(
						workDealInfo.getConfigChargeAgentId(), WorkDealInfoType
								.getWorkType(workDealInfo.getDealInfoType()),
						workDealInfo.getYear());
				workPayInfo.setAddCert(addCert);

				workPayInfo.setUpdateCert(0d);
				workPayInfo.setErrorReplaceCert(0d);
				workPayInfo.setLostReplaceCert(0d);
				workPayInfo.setInfoChange(0d);
				workPayInfo.setOldOpenAccountMoney(openAccountMoney);
				workPayInfo.setOldAddCert(addCert);
				workPayInfo.setOldUpdateCert(0d);
				workPayInfo.setOldErrorReplaceCert(0d);
				workPayInfo.setOldLostReplaceCert(0d);
				workPayInfo.setOldInfoChange(0d);
				double bindMoney = openAccountMoney + addCert;// 开户加新增的费用
				if (payMethodT == 0) {
					workPayInfo.setMethodPos(true);
					workPayInfo.setPosMoney(bindMoney);
				} else if (payMethodT == 1) {
					workPayInfo.setMethodMoney(true);
					workPayInfo.setMoney(bindMoney);
				} else if (payMethodT == 2) {
					workPayInfo.setMethodBank(true);
					workPayInfo.setBankMoney(bindMoney);
				}
				if (workDealInfo.getPayType().equals(2)) {
					workPayInfo.setMethodGov(true);
				} else if (workDealInfo.getPayType().equals(3)) {
					workPayInfo.setMethodContract(true);
				}
				workPayInfo.setWorkTotalMoney(bindMoney);
				workPayInfo.setWorkPayedMoney(bindMoney);
				workPayInfo.setUserReceipt(true);
				workPayInfo.setReceiptAmount(bindMoney);
				workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
				workPayInfoService.save(workPayInfo);
				this.checkWorkDealInfoNeedSettle(workDealInfo);
				workDealInfo
						.setDealInfoStatus(WorkDealInfoStatus.STATUS_APPROVE_WAIT);
				workDealInfo.setAttestationUser(UserUtils.getUser());
				workDealInfo.setAttestationUserDate(new Date());
				workDealInfo.setWorkPayInfo(workPayInfo);
				this.save(workDealInfo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ifErr(-1, ifErr.toString());
		}

		return ifErr(1, ifErr.toString());
	}

	@Transactional(readOnly = false)
	public JSONObject saveExcelDateSed(MultipartFile file, String ifExcel) {
		StringBuffer ifErr = new StringBuffer();
		/*
		 * 创建临时路径
		 */
		StringBuffer tempPath = new StringBuffer("C:/temp/");
		tempPath.append(System.currentTimeMillis()); // 获取系统以毫秒为单位的当前时间
		tempPath.append(ifExcel);// 获取参数@Param file的路径

		/*
		 * 创建临时excel文件存储@Param file数据
		 */
		File tempFile = new File(tempPath.toString());
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs(); // 获取父目录，创建
		}
		try {
			tempFile.createNewFile(); // 创建文件
			file.transferTo(tempFile); // 收到文件传输到目标文件
		} catch (IOException e) {
			ifErr.append("本地目录  F:/temp/ 不存在<br>");
			return ifErr(-1, ifErr.toString());
		}

		/*
		 * 解析@Param file文件
		 */
		Workbook wb = null;
		try {
			wb = createWB(tempFile.getAbsolutePath());
		} catch (Exception e) {
			ifErr.append("模板创建失败<br>");
			return ifErr(-1, ifErr.toString());
		}
		int rows = 0; // 总行数
		int cells = 0; // 总列数
		Sheet sheetAt0 = wb.getSheetAt(0); // 获取第一个工作表
		Row row = null;
		row = sheetAt0.getRow(1);
		if (!validTitle(row)) {
			ifErr.append("导入模板表头与下载模板不匹配，请确认！<br>");
			return ifErr(-1, ifErr.toString());
		}
		rows = sheetAt0.getPhysicalNumberOfRows();
		if (rows <= 2) {
			ifErr.append("模板中数据为空！<br>");
			return ifErr(-1, ifErr.toString());
		}

		return ifErr(1, ifErr.toString());
	}

	/**
	 * 创建workbook
	 */
	private Workbook createWB(String filePath) throws Exception {
		/*
		 * 判断路径为空或者不是xls或xlsx文件
		 */
		if (filePath.equals(null) || filePath.equals("")
				|| !filePath.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
			return null;
		}

		/*
		 * 判断文件是xls还是xlsx
		 */
		boolean ifExcel2003 = true;
		if (filePath.matches("^.+\\.(?i)(xlsx)$")) {
			ifExcel2003 = false;
		}

		/*
		 * 
		 */
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}

		/*
		 * 根据版本选择创建Workbook的方式
		 */
		Workbook wb = null;
		try {
			wb = ifExcel2003 ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
		} catch (IOException e) {
			throw new IOException();
		}

		/*
		 * 解析excel数据
		 */
		is.close();
		return wb;
	}

	public JSONObject ifErr(int status, String msg) {
		JSONObject json = new JSONObject();
		try {
			json.put("status", status);
			json.put("msg", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/*
	 * 校验excle表头
	 */
	private boolean validTitle(Row row) {
		if (!row.getCell(0).toString().replace(" ", "").equals("单位名称"))
			return false;
		if (!row.getCell(1).toString().replace(" ", "").equals("单位类型"))
			return false;
		if (!row.getCell(2).toString().replace(" ", "").equals("组织机构代码"))
			return false;
		if (!row.getCell(3).toString().replace(" ", "").equals("组织机构代码有效期"))
			return false;
		if (!row.getCell(4).toString().replace(" ", "").equals("服务级别"))
			return false;
		if (!row.getCell(5).toString().replace(" ", "").equals("单位证照"))
			return false;
		if (!row.getCell(6).toString().replace(" ", "").equals("证件号"))
			return false;
		if (!row.getCell(7).toString().replace(" ", "").equals("单位证照有效期"))
			return false;
		if (!row.getCell(8).toString().replace(" ", "").equals("法人姓名"))
			return false;
		if (!row.getCell(9).toString().replace(" ", "").equals("省"))
			return false;
		if (!row.getCell(10).toString().replace(" ", "").equals("市"))
			return false;
		if (!row.getCell(11).toString().replace(" ", "").equals("县"))
			return false;
		if (!row.getCell(12).toString().replace(" ", "").equals("区域备注"))
			return false;
		if (!row.getCell(13).toString().replace(" ", "").equals("街道地址"))
			return false;
		if (!row.getCell(14).toString().replace(" ", "").equals("单位联系电话"))
			return false;
		if (!row.getCell(15).toString().replace(" ", "").equals("备注信息"))
			return false;
		if (!row.getCell(16).toString().replace(" ", "").equals("证书持有人姓名"))
			return false;
		if (!row.getCell(17).toString().replace(" ", "").equals("证书持有人证件"))
			return false;
		if (!row.getCell(18).toString().replace(" ", "").equals("证件号码"))
			return false;
		if (!row.getCell(19).toString().replace(" ", "").equals("证书持有人电子邮件"))
			return false;
		if (!row.getCell(20).toString().replace(" ", "").equals("证书持有人手机号"))
			return false;
		if (!row.getCell(21).toString().replace(" ", "").equals("业务系统UID"))
			return false;
		if (!row.getCell(22).toString().replace(" ", "").equals("证书持有人性别"))
			return false;
		if (!row.getCell(23).toString().replace(" ", "").equals("经办人姓名"))
			return false;
		if (!row.getCell(24).toString().replace(" ", "").equals("身份证号"))
			return false;
		if (!row.getCell(25).toString().replace(" ", "").equals("经办人邮箱"))
			return false;
		return true;
	}

	public List<WorkDealInfo> findDealInfo(Long appId, List<Long> appIds,
			List<Long> productIds, Date start, Date end) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appId != null) {

			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (appIds.size() > 0) {
			dc.add(Restrictions.in("configApp.id", appIds));
		}
		if (productIds.size() > 0) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.in("configProduct.id", productIds));
		}
		if (start != null) {
			dc.add(Restrictions.ge("businessCardUserDate", start));
		}
		if (end != null) {
			dc.add(Restrictions.le("businessCardUserDate", end));
		}
		dc.add(Restrictions.eq("dealInfoType", 0));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(dc);
	}

	public WorkDealInfo findDealInfo(Long prvedId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("prevId", prvedId));
		if (workDealInfoDao.find(dc).size() > 0) {
			return workDealInfoDao.find(dc).get(0);
		} else {
			return null;
		}

	}

	public WorkDealInfo findNextDealInfo(Long id) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("prevId", id));
		if (workDealInfoDao.find(dc).size() > 0) {
			return workDealInfoDao.find(dc).get(0);
		} else {
			return null;
		}

	}

	// 找到父业务
	public WorkDealInfo findPreDealInfo(Long prvedId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("id", prvedId));
		if (workDealInfoDao.find(dc).size() > 0) {
			return workDealInfoDao.find(dc).get(0);
		} else {
			return null;
		}

	}

	public int getCertAppYearInfoCount1(Date startTime, Date endTime,
			Long officeId, Integer year, Long appId, Integer dealInfoType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")));
		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}

		dc.add(Restrictions.eq("dealInfoType", dealInfoType));
		dc.add(Restrictions.isNull("dealInfoType1"));
		dc.add(Restrictions.isNull("dealInfoType2"));
		dc.add(Restrictions.isNull("dealInfoType3"));
		/*
		 * dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
		 * Restrictions.eq("dealInfoType1", dealInfoType),
		 * Restrictions.eq("dealInfoType2", dealInfoType),
		 * Restrictions.eq("dealInfoType3", dealInfoType)));
		 */

		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));

		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoCountOneDeal1(Date startTime, Date endTime,
			Long officeId, Integer year, Long appId, Integer dealInfoType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		List<String> statusIntegers = new ArrayList<String>();

		if (dealInfoType.equals(1)) {
			dc.add(Restrictions.eq("dealInfoType", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType1"));
			dc.add(Restrictions.isNull("dealInfoType2"));
			dc.add(Restrictions.isNull("dealInfoType3"));
			statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		} else if (dealInfoType.equals(4)) {
			dc.add(Restrictions.eq("dealInfoType2", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType"));
			dc.add(Restrictions.isNull("dealInfoType1"));
			dc.add(Restrictions.isNull("dealInfoType3"));
			statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		} else if (dealInfoType.equals(2) || dealInfoType.equals(3)) {
			dc.add(Restrictions.eq("dealInfoType1", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType"));
			dc.add(Restrictions.isNull("dealInfoType2"));
			dc.add(Restrictions.isNull("dealInfoType3"));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));

		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoUpdateChangeNum1(Date startTime, Date endTime,
			Long officeId, Integer year, Long appId,
			Integer dealInfoTypeUpdate, Integer dealInfoTypeChange) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoType", dealInfoTypeUpdate));
		dc.add(Restrictions.isNull("dealInfoType1"));
		dc.add(Restrictions.eq("dealInfoType2", dealInfoTypeChange));
		dc.add(Restrictions.isNull("dealInfoType3"));
		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		System.out.println(workDealInfoDao.count(dc));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoUpdateLostReplaceNum1(Date startTime,
			Date endTime, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeUpdate, Integer dealInfoTypeLost) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoType", dealInfoTypeUpdate));
		dc.add(Restrictions.eq("dealInfoType1", dealInfoTypeLost));
		dc.add(Restrictions.isNull("dealInfoType2"));
		dc.add(Restrictions.isNull("dealInfoType3"));
		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoChangeLostReplaceNum1(Date startTime,
			Date endTime, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeChange, Integer dealInfoTypeLostReplace) {

		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoType1", dealInfoTypeLostReplace));
		dc.add(Restrictions.eq("dealInfoType2", dealInfoTypeChange));
		dc.add(Restrictions.isNull("dealInfoType"));
		dc.add(Restrictions.isNull("dealInfoType3"));
		// dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId",
				"officeId"));
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		System.out.println(workDealInfoDao.count(dc));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoChangeLostReplaceUpdateNum1(Date startTime,
			Date endTime, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeChange, Integer dealInfoTypeLostReplace,
			Integer dealInfoTypeUpdate) {

		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		// dc.createAlias("workCertInfo", "workCertInfo");
		// dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoType", dealInfoTypeUpdate));
		dc.add(Restrictions.eq("dealInfoType1", dealInfoTypeLostReplace));
		dc.add(Restrictions.eq("dealInfoType2", dealInfoTypeChange));
		dc.add(Restrictions.isNull("dealInfoType3"));

		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	/*
	 * public List<Projectcount> getCert(Date startTime, Date endTime,Long
	 * apply, Long officeId, Integer year, Long appId, Integer dealInfoType) {
	 * DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
	 * dc.createAlias("createBy", "createBy"); dc.createAlias("createBy.office",
	 * "office"); dc.createAlias("configApp", "configApp"); //
	 * dc.createAlias("workCertInfo", "workCertInfo"); //
	 * dc.add(Restrictions.eq("office.id", officeId));
	 * dc.add(Restrictions.eq("configApp.id", appId));
	 * dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
	 * List<String> statusIntegers = new ArrayList<String>();
	 * 
	 * if (dealInfoType.equals(1)) { dc.add(Restrictions.eq("dealInfoType",
	 * dealInfoType)); dc.add(Restrictions.isNull("dealInfoType1"));
	 * dc.add(Restrictions.isNull("dealInfoType2"));
	 * dc.add(Restrictions.isNull("dealInfoType3"));
	 * statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED); } else if
	 * (dealInfoType.equals(4)) { dc.add(Restrictions.eq("dealInfoType2",
	 * dealInfoType)); dc.add(Restrictions.isNull("dealInfoType"));
	 * dc.add(Restrictions.isNull("dealInfoType1"));
	 * dc.add(Restrictions.isNull("dealInfoType3"));
	 * statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED); } else if
	 * (dealInfoType.equals(2) || dealInfoType.equals(3)) {
	 * dc.add(Restrictions.eq("dealInfoType1", dealInfoType));
	 * dc.add(Restrictions.isNull("dealInfoType"));
	 * dc.add(Restrictions.isNull("dealInfoType2"));
	 * dc.add(Restrictions.isNull("dealInfoType3")); } if (year != 0) {
	 * dc.add(Restrictions.eq("year", year)); } SimpleDateFormat formater = new
	 * SimpleDateFormat("yyyy/MM/dd"); SimpleDateFormat formater2 = new
	 * SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); if (startTime != null && endTime
	 * != null) { try { startTime = formater2.parse(formater.format(startTime)+
	 * " 00:00:00"); endTime = formater2.parse(formater.format(endTime)+
	 * " 23:59:59"); } catch (ParseException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } dc.add(Restrictions.ge("createDate",
	 * startTime)); dc.add(Restrictions.le("createDate", endTime)); }
	 * statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
	 * statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
	 * dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
	 * 
	 * return null; }
	 */
	/*
	 * public int getCertAppYearInfoCount11(List<String> monthList,Long area,
	 * Long officeId,String tempStyle,String agentId, Integer year,Integer
	 * paymethod, Long appId, String dealInfoType) { DetachedCriteria dc =
	 * workDealInfoDao.createDetachedCriteria(); dc.createAlias("createBy",
	 * "createBy"); dc.createAlias("createBy.office", "office");
	 * dc.createAlias("configApp", "configApp"); //
	 * dc.createAlias("workCertInfo", "workCertInfo"); //
	 * dc.add(Restrictions.eq("office.id", officeId));
	 * dc.add(Restrictions.eq("configApp.id", appId));
	 * dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
	 * Restrictions.isNull("isSJQY")));
	 * dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy")); if
	 * (year != 0) { dc.add(Restrictions.eq("year", year)); } SimpleDateFormat
	 * formater = new SimpleDateFormat("yyyy/MM/dd"); SimpleDateFormat formater2
	 * = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); if (startTime != null &&
	 * endTime != null) { try { startTime =
	 * formater2.parse(formater.format(startTime)+ " 00:00:00"); endTime =
	 * formater2.parse(formater.format(endTime)+ " 23:59:59"); } catch
	 * (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } dc.add(Restrictions.ge("createDate", startTime));
	 * dc.add(Restrictions.le("createDate", endTime)); }
	 * dc.add(Restrictions.eq("dealInfoType", dealInfoType));
	 * dc.add(Restrictions.isNull("dealInfoType1"));
	 * dc.add(Restrictions.isNull("dealInfoType2"));
	 * dc.add(Restrictions.isNull("dealInfoType3"));
	 * 
	 * dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
	 * Restrictions.eq("dealInfoType1", dealInfoType),
	 * Restrictions.eq("dealInfoType2", dealInfoType),
	 * Restrictions.eq("dealInfoType3", dealInfoType)));
	 * 
	 * 
	 * List<String> statusIntegers = new ArrayList<String>();
	 * statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
	 * statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
	 * dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
	 * 
	 * return (int) workDealInfoDao.count(dc); }
	 */
	/*
	 * public List<WorkDealInfo> getCert(Date stratTime,Date endTime, List<Long>
	 * officeId,String tempStyle,String agentId, Long appId,String
	 * product,List<String>workType) { DetachedCriteria dc =
	 * workDealInfoDao.createDetachedCriteria(); dc.createAlias("workPayInfo",
	 * "workPayInfo"); dc.add(Restrictions.isNotNull("workPayInfo"));
	 * dc.createAlias("createBy", "createBy"); dc.createAlias("createBy.office",
	 * "office"); dc.createAlias("configProduct", "configProduct");
	 * dc.createAlias("configApp", "configApp");
	 * dc.add(Restrictions.eq("workPayInfo.delFlag",
	 * WorkPayInfo.DEL_FLAG_NORMAL)); List<String> status =
	 * Lists.newArrayList();
	 * status.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
	 * status.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
	 * dc.add(Restrictions.in("dealInfoStatus", status)); SimpleDateFormat
	 * formater = new SimpleDateFormat("yyyy/MM/dd"); SimpleDateFormat formater2
	 * = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); if (stratTime != null &&
	 * endTime != null) { try { stratTime =
	 * formater2.parse(formater.format(stratTime)+ " 00:00:00"); endTime =
	 * formater2.parse(formater.format(endTime)+ " 23:59:59"); } catch
	 * (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } dc.add(Restrictions.ge("createDate", stratTime));
	 * dc.add(Restrictions.le("createDate", endTime));
	 * 
	 * if (appId != null) { dc.add(Restrictions.eq("configApp.id", appId)); } if
	 * (officeId != null && officeId.size() > 0) {
	 * dc.add(Restrictions.in("office.id", officeId)); } if(agentId!=null){
	 * dc.add(Restrictions.eq("configProduct.chargeAgentId", agentId)); }else
	 * if(tempStyle!=null){ dc.add(Restrictions.eq("tempStyle", tempStyle)); }
	 * if(appId!=null){ dc.add(Restrictions.eq("configApp.id", appId)); }
	 * if(product!=null){ dc.add(Restrictions.eq("configProduct.productName",
	 * product)); } if(workType!=null&&workType.size()>0){ for(int
	 * i=0;i<workType.size();i++){ String value= workType.get(i); } } return
	 * workDealInfoDao.find(dc); } else { return null; }
	 * 
	 * }
	 */
	public int getCert(Long appId, Date startTime, Date endTime, Long officeId,
			Integer year, Integer payType, List<Long> productIds,
			Integer dealInfoType) {

		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.eq("office.id", officeId));

		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		if (payType != 0) {
			dc.add(Restrictions.eq("payType", payType));
		}
		if (productIds.size() > 0) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.in("configProduct.id", productIds));
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (startTime != null && endTime != null) {
			try {
				startTime = formater2.parse(formater.format(startTime)
						+ " 00:00:00");
				endTime = formater2.parse(formater.format(endTime)
						+ " 23:59:59");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		dc.add(Restrictions.eq("dealInfoType", dealInfoType));
		dc.add(Restrictions.isNull("dealInfoType1"));
		dc.add(Restrictions.isNull("dealInfoType2"));
		dc.add(Restrictions.isNull("dealInfoType3"));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public List<WorkDealInfo> findDealInfoByAdd(Long appId, List<Long> appIds,
			List<Long> productIds, Date start, Date end, Date contractStart,
			Date contractEnd) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appId != null) {

			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (appIds.size() > 0) {
			dc.add(Restrictions.in("configApp.id", appIds));
		}
		if (productIds.size() > 0) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.in("configProduct.id", productIds));
		}
		if (start != null) {
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(00);
			dc.add(Restrictions.ge("businessCardUserDate", start));
		}
		if (end != null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", end));
		}

		if (contractStart != null) {
			contractStart.setHours(0);
			contractStart.setMinutes(0);
			contractStart.setSeconds(00);
			dc.add(Restrictions.ge("businessCardUserDate", contractStart));
		}

		if (contractEnd != null) {
			contractEnd.setHours(23);
			contractEnd.setMinutes(59);
			contractEnd.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", contractEnd));
		}

		dc.add(Restrictions.eq("dealInfoType", 0));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findDealInfoByUpdate(Long appId,
			List<Long> appIds, List<Long> productIds, Date start, Date end) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appId != null) {

			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (appIds.size() > 0) {
			dc.add(Restrictions.in("configApp.id", appIds));
		}
		if (productIds.size() > 0) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.in("configProduct.id", productIds));
		}
		if (start != null) {
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(00);
			dc.add(Restrictions.ge("businessCardUserDate", start));
		}
		if (end != null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", end));
		}
		dc.add(Restrictions.eq("dealInfoType", 1));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));

		return workDealInfoDao.find(dc);
	}

	public List<ConfigProduct> findByDistinkIds(List<Long> ids) {
		return workDealInfoDao.findByDistinkIds(ids);
	}
	
	
	
	public List<ConfigProduct> findByDistinkIds(String remarkInfo) {
		return workDealInfoDao.findByDistinkIds(remarkInfo);
	}

	public WorkDealInfo findByRevokeStatus(String keySn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keySn", keySn));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_REVOKE));

		List<WorkDealInfo> list = workDealInfoDao.find(dc);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	public WorkDealInfo findByCertSnAndKeySn(String certSn, String keySn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.eq("workCertInfo.serialnumber", certSn));
		dc.add(Restrictions.eq("keySn", keySn));
		dc.add(Restrictions.isNotNull("prevId"));

		List<WorkDealInfo> infos = workDealInfoDao.find(dc);
		if (infos.size() == 1) {
			return infos.get(0);
		} else {
			return null;
		}
	}

	public WorkDealInfo findApply(Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (appId != null) {
			dc.add(Restrictions.eq("selfApplyId", appId));
		}
		dc.addOrder(Order.desc("id"));
		List<WorkDealInfo> list = workDealInfoDao.find(dc);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public List<WorkDealInfo> selectAllDateToUpdate(Long alias,
			String productName, String dealInfoStatus,
			String organizationNumber, String keySn, String companyName,
			Date startTime, Date endTime, Date makeCertStartTime,
			Date makeCertEndTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workCertInfo", "workCertInfo");

		if (alias != null) {
			dc.add(Restrictions.eq("configApp.id", alias));
		}

		if (productName != null && !productName.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", productName));
		}

		if (dealInfoStatus != null && !dealInfoStatus.equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus", dealInfoStatus));
		}

		if (organizationNumber != null && !organizationNumber.equals("")) {
			dc.add(Restrictions.eq("workCompany.organizationNumber",
					organizationNumber));
		}

		if (keySn != null && !keySn.equals("")) {
			dc.add(Restrictions.eq("keySn", keySn));
		}

		if (companyName != null && !companyName.equals("")) {
			dc.add(Restrictions.eq("workCompany.companyName", companyName));
		}

		if (startTime != null) {
			startTime.setHours(0);
			startTime.setMinutes(0);
			startTime.setSeconds(00);

			dc.add(Restrictions.ge("notafter", startTime));
		}

		if (endTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);

			dc.add(Restrictions.le("notafter", endTime));
		}

		if (makeCertStartTime != null) {
			makeCertStartTime.setHours(0);
			makeCertStartTime.setMinutes(0);
			makeCertStartTime.setSeconds(00);

			dc.add(Restrictions.ge("workCertInfo.signDate", makeCertStartTime));
		}

		if (makeCertEndTime != null) {
			makeCertEndTime.setHours(23);
			makeCertEndTime.setMinutes(59);
			makeCertEndTime.setSeconds(59);

			dc.add(Restrictions.le("workCertInfo.signDate", makeCertEndTime));
		}

		dc.add(Restrictions.eq("dealInfoStatus", "7"));

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 60);
		Date notafer = cal.getTime();
		dc.add(Restrictions.le("notafter", notafer));

		return workDealInfoDao.find(dc);

	}

	public Page<WorkDealInfoListVo> findAllDataToUpdate(
			Page<WorkDealInfoListVo> page, WorkDealInfo workDealInfo,
			Date startTime, Date endTime, Long apply, String productName,
			Date makeCertStartTime, Date makeCertEndTime) throws ParseException {

		String sql = "select * "
				+ getFind4ApplySql(page, workDealInfo, startTime, endTime,
						apply, productName, makeCertStartTime, makeCertEndTime);

		Page<WorkDealInfoListVo> res = new Page<WorkDealInfoListVo>(
				page.getPageNo(), page.getPageSize());
		List<Map> lst = null;

		try {
			lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e1) {

			e1.printStackTrace();
		}

		List<WorkDealInfoListVo> resLst = new ArrayList<WorkDealInfoListVo>();
		for (Map e : lst) {
			WorkDealInfoListVo vo = new WorkDealInfoListVo();
			Iterator<String> it = e.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				if (k.equals("ID")) {
					if (e.get("ID") != null)
						vo.setId(new Long(e.get("ID").toString()));

				}
				if (k.equals("COMPANYNAME")) {
					if (e.get("COMPANYNAME") != null)
						vo.setCompanyName(e.get("COMPANYNAME").toString());
				}
				if (k.equals("CERTAPPLYINFONAME")) {
					if (e.get("CERTAPPLYINFONAME") != null)
						vo.setCertApplyInfoName(e.get("CERTAPPLYINFONAME")
								.toString());
				}

				if (k.equals("CONFIGAPPNAME")) {
					if (e.get("CONFIGAPPNAME") != null)
						vo.setConfigAppName(e.get("CONFIGAPPNAME").toString());
				}
				if (k.equals("PRODUCTNAME")) {
					if (e.get("PRODUCTNAME") != null)
						vo.setProductName(e.get("PRODUCTNAME").toString());
				}
				if (k.equals("DEALINFOTYPE")) {
					if (e.get("DEALINFOTYPE") != null)
						vo.setDealInfoType(new Integer(e.get("DEALINFOTYPE")
								.toString()));
				}
				if (k.equals("DEALINFOTYPE2")) {
					if (e.get("DEALINFOTYPE2") != null)
						vo.setDealInfoType2(new Integer(e.get("DEALINFOTYPE2")
								.toString()));
				}
				if (k.equals("DEALINFOTYPE3")) {
					if (e.get("DEALINFOTYPE3") != null)
						vo.setDealInfoType3(new Integer(e.get("DEALINFOTYPE3")
								.toString()));
				}
				if (k.equals("DEALINFOTYPE1")) {
					if (e.get("DEALINFOTYPE1") != null)
						vo.setDealInfoType1(new Integer(e.get("DEALINFOTYPE1")
								.toString()));
				}
				if (k.equals("NOTAFTER")) {
					if (e.get("NOTAFTER") != null)
						vo.setNotafter(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("NOTAFTER").toString()));
				}
				if (k.equals("NOTBEFORE")) {
					if (e.get("NOTBEFORE") != null)
						vo.setNotbefore(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("NOTBEFORE").toString()));
				}
				if (k.equals("KEYSN")) {
					if (e.get("KEYSN") != null)
						vo.setKeySn(e.get("KEYSN").toString());
				}
				if (k.equals("SIGNDATE")) {
					if (e.get("SIGNDATE") != null)
						vo.setSignDate(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("SIGNDATE").toString()));
				}
				if (k.equals("ADDCERTDAYS")) {
					if (e.get("ADDCERTDAYS") != null)
						vo.setAddCertDays(new Integer(e.get("ADDCERTDAYS")
								.toString()));
				}
				if (k.equals("LASTDAYS")) {
					if (e.get("LASTDAYS") != null)
						vo.setLastDays(new Integer(e.get("LASTDAYS").toString()));
				}
				if (k.equals("SVN")) {
					if (e.get("SVN") != null)
						vo.setSvn(e.get("SVN").toString());
				}
				if (k.equals("YEAR")) {
					if (e.get("YEAR") != null)
						vo.setYear(new Integer(e.get("YEAR").toString()));
				}
				if (k.equals("DEALINFOSTATUS")) {
					if (e.get("DEALINFOSTATUS") != null)
						vo.setDealInfoStatus(e.get("DEALINFOSTATUS").toString());
				}
				if (k.equals("NAME")) {
					if (e.get("NAME") != null)
						vo.setCertApplyInfoName(e.get("NAME").toString());
				}
			}
			resLst.add(vo);
		}
		res.setList(resLst);
		return res;
	}

	public List<WorkDealInfo> findAllDeleteData(WorkDealInfo workDealInfo,
			Date startTime, Date endTime, Long agentId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("workCompany", "workCompany");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				dc.add(Restrictions.like(
						"workCompany.companyName",
						"%"
								+ EscapeUtil.escapeLike(workDealInfo
										.getWorkCompany().getCompanyName())
								+ "%"));
			}
		}

		if (startTime != null) {
			dc.add(Restrictions.ge("createDate", startTime));
		}
		if (endTime != null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			dc.add(Restrictions.le("createDate", endTime));
		}

		if (agentId != null) {
			dc.add(Restrictions.le("configChargeAgentId", agentId));
		}

		dc.add(Restrictions.eq("isSJQY", 2));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_APPROVE_WAIT));
		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
				WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(dc);
	}

	/**
	 * @author 萧龙纳云
	 */
	public List<WorkDealInfo> findDealInfoCollectAdds(Long appId,
			List<Long> productIds, Date start, Date end, Date contractStart,
			Date contractEnd) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appId != null) {

			dc.add(Restrictions.eq("configApp.id", appId));
		}

		if (productIds.size() > 0) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.in("configProduct.id", productIds));
		}
		if (start != null) {
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(00);
			dc.add(Restrictions.ge("businessCardUserDate", start));
		}
		if (end != null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", end));
		}

		if (contractStart != null) {
			contractStart.setHours(0);
			contractStart.setMinutes(0);
			contractStart.setSeconds(00);
			dc.add(Restrictions.ge("businessCardUserDate", contractStart));
		}

		if (contractEnd != null) {
			contractEnd.setHours(23);
			contractEnd.setMinutes(59);
			contractEnd.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", contractEnd));
		}

		dc.add(Restrictions.eq("dealInfoType", 0));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
	}

	/**
	 * @author 萧龙纳云
	 */
	public List<WorkDealInfo> findDealInfoCollectUpdates(Long appId,
			List<Long> productIds, Date start, Date end) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appId != null) {

			dc.add(Restrictions.eq("configApp.id", appId));
		}

		if (productIds.size() > 0) {
			dc.createAlias("configProduct", "configProduct");
			dc.add(Restrictions.in("configProduct.id", productIds));
		}
		if (start != null) {
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(00);
			dc.add(Restrictions.ge("businessCardUserDate", start));
		}
		if (end != null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			dc.add(Restrictions.le("businessCardUserDate", end));
		}

		dc.add(Restrictions.eq("dealInfoType", 1));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
	}

	/**
	 * @author 萧龙纳云
	 * 
	 *         根据业务链首条业务的证书序列号查找到日期参数之前的整个业务链
	 * @param firstCertSN
	 *            首张证书序列号
	 * @param endTime
	 *            前台输入日期
	 * @param endLastTime
	 *            截止日期
	 */

	public List<WorkDealInfo> findChainByFirstCertSN(String firstCertSN,
			Date endTime, Date endLastDate) {

		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("firstCertSN", firstCertSN));

		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
		dc.add(Restrictions.le("businessCardUserDate", endTime));

		endLastDate.setHours(23);
		endLastDate.setMinutes(59);
		endLastDate.setSeconds(59);
		dc.add(Restrictions.le("businessCardUserDate", endLastDate));

		dc.add(Restrictions.or(Restrictions.eq("dealInfoType",
				WorkDealInfoType.TYPE_ADD_CERT), Restrictions.eq(
				"dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT)));

		dc.addOrder(Order.asc("createDate"));

		return workDealInfoDao.find(dc);
	}

	/**
	 * @author 萧龙纳云
	 * 
	 *         根据firstCertSN字段得到业务链首张证书业务
	 * @param 首张证书序列号
	 * @return WorkDealInfo 业务链首条业务
	 */

	public WorkDealInfo findFirstByFirstCertSN(String firstCertSN) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();

		// 改: 由于导入的数据certsn与firstcertsn不一定一样 所以改用firstCertSn匹配,也一样 主要由新增那个条件控制
		// dc.add(Restrictions.eq("certSn", firstCertSN));

		dc.add(Restrictions.eq("firstCertSN", firstCertSN));

		dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_ADD_CERT));
		List<WorkDealInfo> lst = workDealInfoDao.find(dc);
		if (lst == null || lst.size() <= 0)
			return null;
		return lst.get(0);
	}
	
	
	
	/**
	 * @author 萧龙纳云
	 * 
	 *         根据firstCertSN字段得到业务链最后一张证书业务
	 * @param 首张证书序列号
	 * @return WorkDealInfo 业务链末条业务
	 */

	public WorkDealInfo findLastByFirstCertSN(String firstCertSN) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("firstCertSN", firstCertSN));
		dc.add(Restrictions.eq("delFlag", "0"));
		List<WorkDealInfo> lst = workDealInfoDao.find(dc);
		if (lst == null || lst.size() <= 0)
			return null;
		return lst.get(0);
	}
	
	
	

	/**
	 * 根据指定总数,查出没有first_cert_sn的数据
	 * 
	 * @param maxCount
	 * @return List<Long>
	 */
	@Transactional(readOnly = true)
	public List<Long> findNullFirstCertSNByCount(int maxCount, int appid) {
		String ct = "select count(*) from WORK_DEAL_INFO where FIRST_CERT_SN is null ";
		if (maxCount > 0) {
			ct += " and rownum<=" + maxCount;
		}
		if (appid > 0) {
			ct += " and APP_ID<=" + appid;
		}

		List l = workDealInfoDao.findBySql(ct);
		int c = new Integer(l.get(0).toString());

		String sql = "select id from WORK_DEAL_INFO where FIRST_CERT_SN is null";
		boolean loop = false;
		if (maxCount > 0) {
			sql += " and rownum<=" + maxCount;
		}
		if (appid > 0) {
			sql += " and APP_ID<=" + appid;
		}

		List<BigDecimal> lst = workDealInfoDao.findBySql(sql);
		List<Long> res = new ArrayList<Long>();
		for (BigDecimal e : lst) {
			res.add(e.longValue());
		}
		return res;
	}

	/**
	 * 找出所有没有first_cert_sn的记录
	 * 
	 * @return Long
	 */
	@Transactional(readOnly = false)
	public void fixAllDataFirstCertSN(int maxCount) {

		String ct = "select count(*) from WORK_DEAL_INFO where FIRST_CERT_SN is null ";
		if (maxCount > 0) {
			ct += " and rownum<=" + maxCount;
		}

		List l = workDealInfoDao.findBySql(ct);
		int c = new Integer(l.get(0).toString());

		String sql = "select id from WORK_DEAL_INFO where FIRST_CERT_SN is null";
		boolean loop = false;
		if (maxCount > 0) {
			sql += " and rownum<=" + maxCount;
		}

		List<BigDecimal> lst = workDealInfoDao.findBySql(sql);

		for (BigDecimal e : lst) {
			Long id = e.longValue();
			WorkDealInfo po = get(id);
			if (StringHelper.isNull(po.getFirstCertSN())) {
				fixFirstCertSN(po.getId());
			}
		}

	}

	/**
	 * 根据传入ID查业务链首条记录，如果首条记录没有cert_sn则返回null
	 * 
	 * @param workDealInfoId
	 * @return String
	 */
	public String findFirstCertSNById(Long workDealInfoId) {
		// 当以prev_id查询有数据时，则不是最后一条
		String sql = "select id,PREV_ID,CREATE_DATE,CERT_SN,FIRST_CERT_SN";
		sql += " from WORK_DEAL_INFO start with ";
		sql += "PREV_ID=";
		sql += workDealInfoId;
		sql += " connect by prior PREV_ID = id order by id desc";

		// // 当以prev_id查询无数据时，则认为是最后一条的ID，以这个ID为起点拉出所有业务链数据
		String sql2 = "select id,PREV_ID,CREATE_DATE,CERT_SN,FIRST_CERT_SN";
		sql2 += " from WORK_DEAL_INFO start with ";
		sql2 += "id=";
		sql2 += workDealInfoId;
		sql2 += " connect by prior PREV_ID = id order by id asc";

		try {
			List<Map> lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);
			if (lst == null || lst.size() <= 0) {
				lst = workDealInfoDao.findBySQLListMap(sql2, 0, 0);
			}
			if (lst == null || lst.size() <= 0)
				return null;

			// 如果该列表内存在prev_id是空的记录，则认为是首条
			boolean hasFirst = false;
			String first_cert_sn = null;
			for (Map e : lst) {
				Object tempid = e.get("PREV_ID");
				if (tempid == null) {
					hasFirst = true;
					first_cert_sn = e.get("FIRST_CERT_SN") == null ? null : e
							.get("FIRST_CERT_SN").toString();
					return first_cert_sn;
				}
			}

			if (!hasFirst) {
				return findFirstCertSNById(new Long(lst.get(0).get("ID")
						.toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 根据指定ID，按照preid查出一条完整业务链
	 * 
	 * @param workDealInfoId
	 * @return List<WorkDealInfo>
	 */
	@Transactional(readOnly = false)
	public void fixFirstCertSN(Long workDealInfoId) {
		WorkDealInfo po = get(workDealInfoId);
		String firstCertSn = findFirstCertSNById(workDealInfoId);
		po.setFirstCertSN(firstCertSn);

		String sql = "update work_deal_info set FIRST_CERT_SN='"
				+ po.getFirstCertSN() + "' where id=" + po.getId();
		workDealInfoDao.exeSql(sql);
	}

	/**
	 * 查询用支付信息支付的业务集合
	 * 
	 * WorkDealInfo,WorkPayInfo,FinancePaymentInfo,WorkFinancePayInfoRelation,
	 * ConfigApp 五张表联合查询
	 * 
	 * @param financePaymentInfo
	 * @return List<WorkpaymentInfo_dealinfoVo>
	 */
	public List<WorkpaymentInfo_dealinfoVo> findPaymentDeal(
			FinancePaymentInfo financePaymentInfo, Long appId,
			Date startCertTime, Date endCertTime, Date startPayTime,
			Date endPayTime, List<Long> officeList) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("select fpi.company as companyName,fpi.pay_date as dealPayDate,wpi.work_total_money as payMoney,fpi.remarks as remarks,ca.app_name as aliasName,wdi.business_card_user_date as signDate from work_deal_info wdi,");
		sql.append(" work_pay_info wpi,");
		sql.append(" finance_payment_info fpi,");
		sql.append(" work_finance_pay_info_relation wfpr,");
		sql.append(" config_app ca");

		// 几个表之间的逻辑关系
		sql.append(" where wdi.pay_id = wpi.id");
		sql.append(" and wdi.app_id = ca.id");
		sql.append(" and wfpr.work_pay_info = wpi.id");
		sql.append(" and wfpr.finance_pay_info = fpi.id");

		// 条件查询 appId
		if (appId != null) {
			sql.append(" and ca.id = ").append(appId);
		}

		// 条件查询 单位名称模糊查询
		if (financePaymentInfo.getCompany() != null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())) {
				sql.append(" and fpi.company like '%")
						.append(financePaymentInfo.getCompany()).append("%'");
			}
		}

		// 条件查询 制证初始时间
		if (startCertTime != null) {
			sql.append(" and wdi.business_card_user_date >= TO_DATE('"
					+ DateUtils
							.formatDate(startCertTime, "yyyy-MM-dd 00:00:00")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}

		// 条件查询 制证结束时间
		if (endCertTime != null) {
			sql.append(" and wdi.business_card_user_date <= TO_DATE('"
					+ DateUtils.formatDate(endCertTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}

		// 条件查询 支付初始时间
		if (startPayTime != null) {
			sql.append(" and fpi.pay_date >= TO_DATE('"
					+ DateUtils.formatDate(startPayTime, "yyyy-MM-dd 00:00:00")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}

		// 条件查询 支付结束时间
		if (endPayTime != null) {
			sql.append(" and fpi.pay_date <= TO_DATE('"
					+ DateUtils.formatDate(endPayTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}

		// 条件查询 所办业务要在网点权限范围内
		sql.append(" and wdi.office_id in (").append(officeList.get(0));

		for (int i = 1; i < officeList.size(); i++) {
			sql.append(",").append(officeList.get(i));
		}
		sql.append(")");

		sql.append(" and fpi.DEL_FLAG = ").append(DataEntity.DEL_FLAG_NORMAL);
		sql.append(" and fpi.quit_money_status != 1");
		sql.append(" order by fpi.pay_date asc");

		List<Map> lst = null;

		try {
			lst = workDealInfoDao.findBySQLListMap(sql.toString(), 0, 0);

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e1) {
			e1.printStackTrace();
		}

		List<WorkpaymentInfo_dealinfoVo> resLst = new ArrayList<WorkpaymentInfo_dealinfoVo>();
		for (Map e : lst) {
			WorkpaymentInfo_dealinfoVo vo = new WorkpaymentInfo_dealinfoVo();
			Iterator<String> it = e.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				if (k.equals("COMPANYNAME")) {
					if (e.get("COMPANYNAME") != null)
						vo.setCompanyName(e.get("COMPANYNAME").toString());
				}

				if (k.equals("DEALPAYDATE")) {
					if (e.get("DEALPAYDATE") != null)
						vo.setDealPayDate(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("DEALPAYDATE").toString()));

				}

				if (k.equals("PAYMONEY")) {
					if (e.get("PAYMONEY") != null)
						vo.setPayMoney(new Double(e.get("PAYMONEY").toString()));
				}

				if (k.equals("REMARKS")) {
					if (e.get("REMARKS") != null)
						vo.setRemarks(e.get("REMARKS").toString());
				}

				if (k.equals("ALIASNAME")) {
					if (e.get("ALIASNAME") != null)
						vo.setAliasName(e.get("ALIASNAME").toString());
				}

				if (k.equals("SIGNDATE")) {
					if (e.get("SIGNDATE") != null)
						vo.setSignDate(new SimpleDateFormat("yyyy-MM-dd")
								.parse(e.get("SIGNDATE").toString()));
				}

			}
			resLst.add(vo);
		}
		return resLst;

	}

	/**
	 * 修复首条记录的证书序列号和首证书序列号不符的记录(将首证书序列号更新为记录的证书序列号)
	 * 
	 * @param lst
	 */
	public void fixNotEquals(List<String> lst) {

	}

	/**
	 * 修复业务链中有多条新增类型的记录
	 * 
	 * @param lst
	 */
	public void fixMutiAdd(List<String> lst) {
		for (String e : lst) {
			try {
				// 查业务链中为新增的记录
				List<WorkDealInfo> link = findByFirstCertSNAndDealInfoType(e, 0);
				if (link == null || link.size() <= 0)
					continue;
				// 只有一条的情况
				if (link != null && link.size() == 1) {
					fixLog.error("业务链中只有一条新增记录,首证书序列号:" + e);
					continue;
				}
				// 如果是首条,检查业务类型，如果不是新增则改为新增
				WorkDealInfo p = link.get(0);
				if (p.getDealInfoType() == 0) {
					String update = "update work_deal_info set DEAL_INFO_TYPE=1 where id="
							+ p.getId();
					try {
						workDealInfoDao.exeSql(update);
					} catch (Exception ex) {
						continue;
					}
				}
			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 修复指定APPID的记录，让其业务链里的第一条记录的业务类型为新增
	 * 
	 * @param lst
	 */
	public void fixDealInfoType(List<String> lst) {
		for (String e : lst) {
			try {
				// 查业务链
				List<WorkDealInfo> link = findByFirstCertSN(e);
				if (link == null || link.size() <= 0)
					continue;
				// 只有一条的情况
				if (link != null && link.size() == 1) {
					WorkDealInfo po = link.get(0);
					String sql = "update work_deal_info set DEL_FLAG='0',PREV_ID=null,DEAL_INFO_TYPE=0,DEAL_INFO_TYPE1=null,DEAL_INFO_TYPE2=null,DEAL_INFO_TYPE3=null where id="
							+ po.getId();
					try {
						workDealInfoDao.exeSql(sql);
					} catch (Exception ex) {
						continue;
					}
				}
				// 如果是首条,检查业务类型，如果不是新增则改为新增
				WorkDealInfo p = link.get(link.size() - 1);
				String fcn = zeroProcess(p.getFirstCertSN());
				// 特殊情况为:如果证书序列号为空，且在首条，也允许更新
				if (StringHelper.isNull(p.getCertSn())
						|| p.getCertSn().equals(fcn)
						|| p.getDealInfoType() != 0) {
					if (p.getDealInfoType() == null || p.getDealInfoType() != 0) {
						String update = "update work_deal_info set DEAL_INFO_TYPE=0,DEAL_INFO_TYPE1=null,DEAL_INFO_TYPE2=null,DEAL_INFO_TYPE3=null where id="
								+ p.getId();
						try {
							workDealInfoDao.exeSql(update);
						} catch (Exception ex) {
							continue;
						}
						fixLog.error("首证书业务类型已处理为新增,首证书序列号:"
								+ p.getFirstCertSN() + " | id:" + p.getId());
					}
				} else {
					fixLog.error("业务链首条序列号和首证书序列号不符,首证书序列号:"
							+ p.getFirstCertSN() + " | id:" + p.getId());
				}
			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 修复指定的first_cert_sn错乱的记录<br>
	 * 逻辑为根据传入的first_cert_sn，去查对应的cert_sn记录(这记录应该是一条业务链)<br>
	 * 更新错位的first_cert_sn，然后将业务链重新串一遍<br>
	 * 
	 * @param lst
	 */
	@Transactional(readOnly = false)
	public void fixFirstCertSNByError(List<String> lst) {
		for (String e : lst) {
			// 先用certSn查出自己
			WorkDealInfo self = findByCertSnOne(e);

			if (self == null || StringHelper.isNull(self.getFirstCertSN())
					|| StringHelper.isNull(self.getCertSn()))
				continue;

			// 再以自己的cert_sn作为first_cert_sn去查业务链中仅有一条的，并且prev_id不为空的记录
			List<WorkDealInfo> l = findByFirstCertSN(self.getCertSn());
			if (l == null || l.size() <= 0 || l.size() < 1)
				continue;
			WorkDealInfo updatePo = l.get(0);
			// 如果上一条记录的
			if (l.size() == 1 && updatePo.getPrevId() == null
					&& !updatePo.getFirstCertSN().equals(updatePo.getCertSn())) {
				fixLog.error("业务链数据只有一条，但首证书和证书序列号不一致,id:" + updatePo.getId()
						+ "|firstCertSN:" + updatePo.getFirstCertSN()
						+ "|certSN:" + updatePo.getCertSn());
				continue;
			}

			WorkDealInfo pre = get(updatePo.getPrevId());
			if (pre == null)
				continue;
			try {
				// 更新错误的first_cert_sn
				modifyFirstCertSN(updatePo.getId(), pre.getFirstCertSN());
				// 重新串业务
				processSinglePreid(pre.getFirstCertSN());
			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	public void fixFirstCertSNByError2(List<String> lst) {
		for (String e : lst) {
			List<WorkDealInfo> l = findByFirstCertSN(e);
			if (l == null || l.size() <= 0)
				continue;
			WorkDealInfo self = l.get(0);
			try {

				// 重新串业务
				processSinglePreid(l.get(0).getFirstCertSN());
			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 
	 * 
	 * @param lst
	 */
	public void fixFirstCertSNByError3(List<String> lst) {
		for (String e : lst) {
			// 先用certSn查出自己
			WorkDealInfo self = findByCertSnOne(e);

			if (self == null || StringHelper.isNull(self.getFirstCertSN())
					|| StringHelper.isNull(self.getCertSn()))
				continue;

			// 再以自己的firstCertSN作为cert_sn去查业务链
			WorkDealInfo prevPo = findByCertSnOne(self.getFirstCertSN());
			if (prevPo == null) {
				fixLog.error("按本条的firstCertSN作为certSN未找到上一条记录,firstCertSN:"
						+ self.getFirstCertSN() + ",id:" + self.getId());
				continue;
			}
			try {

				List<WorkDealInfo> updateList = findByFirstCertSN(self
						.getFirstCertSN());
				for (WorkDealInfo el : updateList) {
					// fixLog.error("首证书变化:更新前 - " + self.getFirstCertSN()
					// + " , 更新后 - " + prevPo.getFirstCertSN() + " , id:"
					// + el.getId());
					// 更新错误的first_cert_sn
					modifyFirstCertSN(el.getId(), prevPo.getFirstCertSN());
				}
				// 重新串业务
				processSinglePreid(prevPo.getFirstCertSN());

			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 逐条检查并修复错位的首证书
	 * 
	 * @param lst
	 */
	public void fixErrorFirstCertSN(List<String> lst) {
		for (String e : lst) {
			// 计数器+1
			// FixErrorFirstCertSNThread.plusCount();
			// log.debug("fixErrorFirstCertSN count: " +
			// FixErrorFirstCertSNThread.getCount());

			List<WorkDealInfo> link = findByFirstCertSN(e);
			if (link == null || link.size() <= 0)
				continue;
			int first = link.size() == 0 ? 0 : link.size() - 1;
			// 取第一条数据
			WorkDealInfo p = link.get(first);

			// 如果第一条记录没有prevId，则跳过不处理
			if (p.getPrevId() == null)
				continue;
			// 如果有prevId则去查上一条记录
			if (p.getPrevId() != null && p.getPrevId() > 0) {
				WorkDealInfo prevPo = get(p.getPrevId());
				if (StringHelper.isNull(prevPo.getFirstCertSN()))
					continue;
				// 把原来错误的首证书更新为prevId查出记录的首证书
				modifyFirstCertSN(e, prevPo.getFirstCertSN());
				fixLog.error("错位首证书修复,原始首证书:" + e + ",更新为:"
						+ prevPo.getFirstCertSN());
				// 重新串业务
				processSinglePreid(prevPo.getFirstCertSN());
			}

		}
	}

	/**
	 * 修复无firstCertSn，有prevId记录
	 * 
	 * @param lst
	 */
	public void fixFirstCertSNByError4(List<String> lst) {
		for (String e : lst) {
			// 先查出自己
			WorkDealInfo self = get(new Long(e));

			if (self == null) {
				fixLog.error("修复“无firstCertSn，有prevId记录”时，未查到当前记录,id:" + e);
				continue;
			}
			WorkDealInfo prevPo = get(self.getPrevId());
			if (prevPo == null) {
				fixLog.error("修复“无firstCertSn，有prevId记录”时,未查到上一条记录,id:"
						+ self.getId() + ",prevId:" + self.getPrevId());
				continue;
			}

			if (StringHelper.isNull(prevPo.getFirstCertSN())) {
				fixLog.error("修复“无firstCertSn，有prevId记录”时,上一条记录未查到firstCertSN,id:"
						+ e + ",prev_id:" + self.getPrevId());
				continue;
			}
			try {
				// fixLog.error("首证书变化:更新前 - " + self.getFirstCertSN()
				// + " , 更新后 - " + prevPo.getFirstCertSN() + " , id:"
				// + self.getId());
				// 更新错误的first_cert_sn
				modifyFirstCertSN(self.getId(), prevPo.getFirstCertSN());
				// 重新串业务
				processSinglePreid(prevPo.getFirstCertSN());

			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 修复有prevId和首证书，但自己和prev_id的首证书不同的记录
	 * 
	 * @param lst
	 */
	public void fixFirstCertSNByError5(List<String> lst) {
		for (String e : lst) {
			// 先查出自己
			WorkDealInfo self = get(new Long(e));

			if (self == null) {
				fixLog.error("修复“有firstCertSn，有prevId记录，但首证书不同记录”时，未查到当前记录,id:"
						+ e);
				continue;
			}
			WorkDealInfo prevPo = get(self.getPrevId());
			if (prevPo == null) {
				fixLog.error("修复“有firstCertSn，有prevId记录，但首证书不同记录”时,未查到上一条记录,id:"
						+ self.getId() + ",prevId:" + self.getPrevId());
				continue;
			}

			if (StringHelper.isNull(prevPo.getFirstCertSN())) {
				fixLog.error("修复“有firstCertSn，有prevId记录，但首证书不同记录”时,上一条记录未查到firstCertSN,id:"
						+ e + ",prev_id:" + self.getPrevId());
				continue;
			}
			if (prevPo.getFirstCertSN().equals(self.getFirstCertSN())) {
				continue;
			}
			try {
				// fixLog.error("首证书变化:更新前 - " + self.getFirstCertSN()
				// + " , 更新后 - " + prevPo.getFirstCertSN() + " , id:"
				// + self.getId());
				// 更新错误的first_cert_sn
				modifyFirstCertSN(self.getId(), prevPo.getFirstCertSN());
				// 重新串业务
				// processSinglePreid(prevPo.getFirstCertSN());

			} catch (Exception ex) {
				// 事务问题，可忽略
				ex.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * 找出一个指定应用下需要修复的数据
	 * 
	 * @param appid
	 * @return List<WorkDealInfo>
	 */
	public List<WorkDealInfo> getNeedFixFirstCertSN(String appid) {
		String sql = "select ID,PREV_ID,DEL_FLAG,CERT_SN,CREATE_DATE,FIRST_CERT_SN from WORK_DEAL_INFO where CERT_SN in (";
		sql += " select first_cert_sn from WORK_DEAL_INFO where ";
		sql += " FIRST_CERT_SN in(";
		sql += " select first_cert_sn from (";
		sql = sql
				+ " select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO where APP_ID="
				+ appid;
		sql += " group by FIRST_CERT_SN order by c desc";
		sql += " ) where c=1 ";
		sql += ")";
		sql += ") and PREV_ID is not null";

		try {
			List<WorkDealInfo> res = new ArrayList<WorkDealInfo>();
			List<Map> lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);

			if (lst == null || lst.size() <= 0)
				return null;

			for (Map e : lst) {
				WorkDealInfo po = new WorkDealInfo();
				po.setId(new Long(e.get("ID").toString()));
				po.setPrevId(new Long(e.get("PREV_ID").toString()));
				po.setCertSn(e.get("CERT_SN").toString());
				po.setFirstCertSN(e.get("FIRST_CERT_SN").toString());
				res.add(po);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> getFirstCertSNListByAppid(String sql, String columnName) {
		try {
			List<String> res = new ArrayList<String>();
			List<Map> lst = workDealInfoDao.findBySQLListMap(sql, 0, 0);

			if (lst == null || lst.size() <= 0)
				return null;

			for (Map e : lst) {
				if (e.get(columnName) == null)
					continue;
				String f = e.get(columnName).toString();
				res.add(f);
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 业务链只有1条记录，有prev_id，但首证书序列号和自己的序列号不一致<br>
	 * 
	 * @param appid
	 * @return List<String>
	 */
	public List<String> getNeedFixFirstCertSNLst2(String appid) {
		String sql = "select id,prev_id,first_cert_sn,SVN,CERT_SN from WORK_DEAL_INFO where ";
		sql += "FIRST_CERT_SN in(";
		sql += "select first_cert_sn from (";
		sql = sql
				+ "select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO where APP_ID="
				+ appid;
		sql += " group by FIRST_CERT_SN order by c desc";
		sql += ") where c=1 ";
		sql += ") and PREV_ID is not null ";
		sql += " and ('00'||FIRST_CERT_SN!=CERT_SN and FIRST_CERT_SN!=CERT_SN)";
		return getFirstCertSNListByAppid(sql, "FIRST_CERT_SN");
	}

	/**
	 * 首条记录和首证书不符的
	 * 
	 * @param appid
	 * @return List<String>
	 */
	public List<String> getNeedFixFirstCertSNLst3(String appid) {
		String sql = "select id,PREV_ID,DEL_FLAG,CERT_SN,FIRST_CERT_SN,CREATE_DATE, ";
		sql += "DEAL_INFO_TYPE,DEAL_INFO_TYPE1,DEAL_INFO_TYPE2,DEAL_INFO_TYPE3,IS_SJQY";
		sql = sql + " from WORK_DEAL_INFO where app_id=" + appid;
		sql += " and FIRST_CERT_SN!='0'";
		sql += " and PREV_ID is null and CERT_SN!=FIRST_CERT_SN AND CERT_SN!='00'||FIRST_CERT_SN";
		return getFirstCertSNListByAppid(sql, "CERT_SN");
	}

	/**
	 * 业务链中不是第一条，首证书非空，prev_id非空，但prev_id的首证书和本条的首证书不一样的ID
	 * 
	 * @param appid
	 * @return List<String>
	 */
	public List<String> getNeedFixFirstCertSNLst5(String appid) {
		String sql = "select a.id,a.PREV_ID,a.DEL_FLAG,a.CERT_SN,a.FIRST_CERT_SN,a.CREATE_DATE,";
		sql += "a.DEAL_INFO_TYPE,a.DEAL_INFO_TYPE1,a.DEAL_INFO_TYPE2,a.DEAL_INFO_TYPE3,";
		sql += "a.IS_SJQY,a.DEAL_INFO_STATUS from WORK_DEAL_INFO a left join WORK_DEAL_INFO b";
		sql = sql
				+ " on a.PREV_ID=b.ID and a.PREV_ID is not null and a.APP_ID="
				+ appid;
		sql += " and a.DEL_FLAG=0";
		sql += " and a.FIRST_CERT_SN is not null where a.PREV_ID=b.ID and a.first_cert_sn!=b.first_cert_sn";
		sql += " and b.first_cert_sn!='0'";
		return getFirstCertSNListByAppid(sql, "ID");
	}

	/**
	 * 业务链有prev_id，无first_cert_sn<br>
	 * 
	 * @param appid
	 * @return List<String>(id)
	 */
	public List<String> getNeedFixFirstCertSNLst4(String appid) {
		String sql = "select id,PREV_ID,DEL_FLAG,CERT_SN,FIRST_CERT_SN,CREATE_DATE, ";
		sql += "DEAL_INFO_TYPE,DEAL_INFO_TYPE1,DEAL_INFO_TYPE2,DEAL_INFO_TYPE3,";
		sql += "IS_SJQY,DEAL_INFO_STATUS from WORK_DEAL_INFO";
		sql = sql + " where FIRST_CERT_SN is null and app_id=" + appid;
		sql += " and PREV_ID is not null";

		return getFirstCertSNListByAppid(sql, "ID");
	}

	/**
	 * 业务链中的数据只有一条，但根据其first_cert_sn，查出对应的cert_sn记录存在prev_id，<br>
	 * 因为有补零逻辑，所以需要下面二条SQL来验证<br>
	 * 
	 * 
	 * @param appid
	 * @return List<String>
	 */
	public List<String> getNeedFixFirstCertSNLst(String appid) {
		//
		String sql = "select id,prev_id,first_cert_sn,SVN,CERT_SN from WORK_DEAL_INFO where ";
		sql += " CERT_SN in(";
		sql += "select first_cert_sn from (";
		sql = sql
				+ " select count(*) c,FIRST_CERT_SN from WORK_DEAL_INFO where APP_ID="
				+ appid;
		sql += " group by FIRST_CERT_SN order by c desc";
		sql += " ) where c=1 ";
		sql += ") and PREV_ID is not null and FIRST_CERT_SN!=CERT_SN";

		return getFirstCertSNListByAppid(sql, "CERT_SN");
	}

	public List<WorkDealInfoVo> getAllByHql(String ids) {
		StringBuffer hql = new StringBuffer(
				"select new com.itrus.ca.modules.message.vo.WorkDealInfoVo(wdi.svn,wdi.keySn,wdi.dealInfoStatus,wdi.notafter ");

		hql.append(",wcai.name");

		hql.append(",wc.organizationNumber,wc.companyName,wc.legalName,wc.province,wc.city,wc.district,wc.address");

		hql.append(",ca.alias)");

		hql.append(" from WorkDealInfo wdi,WorkCertInfo wci,WorkCertApplyInfo wcai,WorkCompany wc,ConfigApp ca");

		hql.append(" where wdi.workCertInfo = wci.id");
		hql.append(" and wci.workCertApplyInfo = wcai.id");
		hql.append(" and wdi.workCompany = wc.id");
		hql.append(" and wdi.configApp = ca.id");
		hql.append(" and wdi.id in (").append(ids).append(")");

		List<WorkDealInfoVo> list = workDealInfoDao.find(hql.toString());

		return list;
	}

	public WorkDealInfoVo getSingleByHql(Long id) {
		StringBuffer hql = new StringBuffer(
				"select new com.itrus.ca.modules.message.vo.WorkDealInfoVo(wdi.svn,wdi.keySn,wdi.dealInfoStatus,wdi.notafter ");

		hql.append(",wcai.name");

		hql.append(",wc.organizationNumber,wc.companyName,wc.legalName,wc.province,wc.city,wc.district,wc.address");

		hql.append(",ca.alias)");

		hql.append(" from WorkDealInfo wdi,WorkCertInfo wci,WorkCertApplyInfo wcai,WorkCompany wc,ConfigApp ca");

		hql.append(" where wdi.workCertInfo = wci.id");
		hql.append(" and wci.workCertApplyInfo = wcai.id");
		hql.append(" and wdi.workCompany = wc.id");
		hql.append(" and wdi.configApp = ca.id");
		hql.append(" and wdi.id  =").append(id);

		List<WorkDealInfoVo> list = workDealInfoDao.find(hql.toString());

		return list.get(0);
	}

	public List<WorkDealInfoVo> findAllByHql(String ids) {
		StringBuffer hql = new StringBuffer(
				"select new com.itrus.ca.modules.message.vo.WorkDealInfoVo(wdi.svn,wdi.keySn,wdi.dealInfoStatus,wdi.notafter ");

		hql.append(",wcai.name");

		hql.append(",wc.organizationNumber,wc.companyName,wc.legalName,wc.province,wc.city,wc.district,wc.address");

		hql.append(",ca.alias");

		hql.append(",wu.contactPhone");

		hql.append(",wdi.id,wci.id,wc.id,ca.id,wu.id)");

		hql.append(" from WorkDealInfo wdi,WorkCertInfo wci,WorkCertApplyInfo wcai,WorkCompany wc,ConfigApp ca,WorkUser wu");

		hql.append(" where wdi.workCertInfo = wci.id");
		hql.append(" and wci.workCertApplyInfo = wcai.id");
		hql.append(" and wdi.workCompany = wc.id");
		hql.append(" and wdi.configApp = ca.id");
		hql.append(" and wdi.workUser = wu.id");
		hql.append(" and wdi.id in (").append(ids).append(")");

		List<WorkDealInfoVo> list = workDealInfoDao.find(hql.toString());

		return list;
	}

	public WorkDealInfoVo findSingleByHql(Long id) {
		StringBuffer hql = new StringBuffer(
				"select new com.itrus.ca.modules.message.vo.WorkDealInfoVo(wdi.svn,wdi.keySn,wdi.dealInfoStatus,wdi.notafter ");

		hql.append(",wcai.name");

		hql.append(",wc.organizationNumber,wc.companyName,wc.legalName,wc.province,wc.city,wc.district,wc.address");

		hql.append(",ca.alias");

		hql.append(",wu.contactPhone");

		hql.append(",wdi.id,wci.id,wc.id,ca.id,wu.id)");

		hql.append(" from WorkDealInfo wdi,WorkCertInfo wci,WorkCertApplyInfo wcai,WorkCompany wc,ConfigApp ca,WorkUser wu");

		hql.append(" where wdi.workCertInfo = wci.id");
		hql.append(" and wci.workCertApplyInfo = wcai.id");
		hql.append(" and wdi.workCompany = wc.id");
		hql.append(" and wdi.configApp = ca.id");
		hql.append(" and wdi.workUser = wu.id");
		hql.append(" and wdi.id =").append(id);

		List<WorkDealInfoVo> list = workDealInfoDao.find(hql.toString());

		return list.get(0);
	}

	public Page<WorkDealInfo> findByGuiDang(
			HttpServletRequest request,
			HttpServletResponse response,
			WorkDealInfo workDealInfo, Map<String, String> queryStr,boolean findAll) {
		
		//Page<WorkDealInfoExpView> lst = new Page<WorkDealInfoExpView>(request, response);
		Page<WorkDealInfo> page = new Page<WorkDealInfo>(request, response);
		String sql = "select * "
				+ getFind4GuiDangSql(workDealInfo, queryStr);
		
		
		String sqlCount = "select count(*) ct "
				+ getFind4GuiDangSql(workDealInfo, queryStr);

		try {
			List<Map> ct = workDealInfoDao.findBySQLListMap(sqlCount, 1, 1);
			
			int count = new Integer(ct.get(0).get("CT").toString());
			page.setCount(count);
		
			if (findAll)
				page.setPageSize(-1);

			List<Map> lst = workDealInfoDao.findBySQLListMap(sql, page.getPageNo(),
					page.getPageSize());
			if (lst == null || lst.size() <= 0) {
				return page;
			}
			//把lst 转换成对象
			List<WorkDealInfo> resList = viewToDealInfo(lst);
			page.setList(resList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return page;
	}

	/**
	 * 为了减少前台显示的重构部分，把视图转换成表对象
	 * 
	 * @param lst
	 * @return List<WorkDealInfo>
	 */
	private List<WorkDealInfo> viewToDealInfo(List<Map> lst) {
		List<WorkDealInfo> resList = new ArrayList<WorkDealInfo>();
		for (Map e : lst) {
			WorkDealInfo po = new WorkDealInfo();
			try {
				po = new WorkDealInfo();
				po.setId(Long.parseLong(e.get("ID").toString()));
				po.setSvn(e.get("SVN")==null?null:e.get("SVN").toString());
				
				WorkCompany company = new WorkCompany();
				company.setCompanyName(e.get("COMPANYNAME")==null?null:e.get("COMPANYNAME").toString());
				po.setWorkCompany(company);

				po.setSvn(e.get("CONFIGAPPNAME")==null?null:e.get("CONFIGAPPNAME").toString());
				
				ConfigApp configApp = new ConfigApp();
				configApp.setAppName(e.get("CONFIGAPPNAME")==null?null:e.get("CONFIGAPPNAME").toString());
				po.setConfigApp(configApp);
				
				po.setYear(e.get("YEAR")==null?null:Integer.parseInt(e.get("YEAR").toString()));
				po.setDealInfoStatus(e.get("CONFIGAPPNAME")==null?null:e.get("CONFIGAPPNAME").toString());
				
				po.setLastDays(e.get("LASTDAYS")==null?null:Integer.parseInt(e.get("LASTDAYS").toString()));
				
				ConfigProduct configProduct = new ConfigProduct();
				configProduct.setProductName(e.get("PRODUCTNAME")==null?null:e.get("PRODUCTNAME").toString());
				po.setConfigProduct(configProduct);
				
				po.setKeySn(e.get("KEYSN")==null?null:e.get("KEYSN").toString());
				
				po.setDealInfoType(e.get("DEALINFOTYPE")==null?null:Integer.parseInt(e.get("DEALINFOTYPE").toString()));
				po.setDealInfoType1(e.get("DEALINFOTYPE1")==null?null:Integer.parseInt(e.get("DEALINFOTYPE1").toString()));
				po.setDealInfoType2(e.get("DEALINFOTYPE2")==null?null:Integer.parseInt(e.get("DEALINFOTYPE2").toString()));
				po.setDealInfoType3(e.get("DEALINFOTYPE3")==null?null:Integer.parseInt(e.get("DEALINFOTYPE3").toString()));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				WorkCertInfo workCertInfo = new WorkCertInfo();
				workCertInfo.setSignDate(e.get("SIGNDATE")==null?null:sdf.parse(e.get("SIGNDATE").toString()));
				workCertInfo.setNotbefore(e.get("NOTBEFORE")==null?null:sdf.parse(e.get("NOTBEFORE").toString()));
				po.setWorkCertInfo(workCertInfo);
				
				
				WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
				workCertApplyInfo.setName(e.get("CERTAPPLYINFONAME")==null?null:e.get("CERTAPPLYINFONAME").toString());
				po.setWorkCertInfo(workCertInfo);
				
				po.setSvn(e.get("SVN")==null?null:e.get("SVN").toString());
				po.setAddCertDays(e.get("ADDCERTDAYS")==null?null:Integer.parseInt(e.get("ADDCERTDAYS").toString()));
				po.setNotafter(e.get("NOTAFTER")==null?null:sdf.parse(e.get("NOTAFTER").toString()));
				
				User updateUser = new User();
				updateUser.setName(e.get("UPDATENAME")==null?null:e.get("UPDATENAME").toString());
				po.setUpdateBy(updateUser);
				po.setArchiveDate(e.get("ARCHIVEDATE")==null?null:Timestamp.valueOf(e.get("ARCHIVEDATE").toString()));
				po.setUserSn(e.get("USERSN")==null?null:e.get("USERSN").toString());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			/*po.setDealInfoType(e.getDealInfoType());
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
			po.setConfigProduct(configProduct);*/

			resList.add(po);
		}
		return resList;
	}
	
	private String getFind4GuiDangSql(WorkDealInfo workDealInfo, Map<String, String> queryStr) {
		StringBuffer sql = new StringBuffer();
		String sqls = dataScopeFilterByWorkDealInfo(UserUtils.getUser(),
				"areaId", "officeId").toString();
		sqls = sqls.substring(1, sqls.length() - 1);
		sqls = sqls.replace("officeId", "office_Id");
		sql.append(" from(select wdi.id as id," + "wdi.svn as svn,"
				+ "ca.app_name as configAppName,"
				+ "wc.company_name as companyName,"
				+ "cp.product_name as productName,"
				+ "wdi.deal_info_type as dealInfoType,"
				+ "wdi.deal_info_type1 as dealInfoType1,"
				+ "wdi.deal_info_type2 as dealInfoType2,"
				+ "wdi.deal_info_type3 as dealInfoType3,"
				+ "wci.notafter as notafter,"
				+ "wci.notbefore as notbefore,"
				+ "wdi.key_sn as keySn,"
				+ "wci.sign_date as signDate,"
				+ "wdi.add_cert_days as addCertDays,"
				+ "wdi.year as year ," + "wdi.last_days as lastDays,"
				+ "wdi.deal_info_status as dealInfoStatus,"
				+ "su.name as updatename,"
				+ "wdi.archive_date as  archiveDate,"
				+ "wdi.user_sn as userSn,"   
				+ "workCertApplyInfo.name as certApplyInfoName  ");
		sql.append("  from work_deal_info wdi left join work_company wc on wdi.work_company_id=wc.id  ");
		sql.append("	left join config_product cp on wdi.product_id = cp.id ");
		sql.append("	left join config_charge_agent cca on wdi.config_agent_id = cca.id ");
		sql.append("	left join work_pay_info wpi on wdi.pay_id=wpi.id ");
		sql.append("	left join work_user wu on wu.id=wdi.work_user_id ");
		sql.append("	left join sys_user su on su.id=wdi.update_by  ");
		sql.append("	left join config_app ca on wdi.app_id=ca.id ");
		sql.append("	left join work_cert_info wci on wdi.cert_id=wci.id ");
		sql.append("    left join work_cert_apply_info workCertApplyInfo on wci.apply_info=workCertApplyInfo.id ");

		/*if ("".equals(sqls)) {
			sql.append("where (wdi.is_ixin is not null or 1=1) and wdi.del_flag=0");
		} else {
			sql.append("where (wdi.is_ixin is not null or (wdi." + sqls
					+ ")) and wdi.del_flag=0");
		}*/

		if ("".equals(sqls)) {
			sql.append("where (wdi.is_ixin is not null or 1=1) ");
		} else {
			sql.append("where (wdi.is_ixin is not null or (wdi." + sqls
					+ ")) ");
		}
		
		String area = queryStr.get("area");//受理区域
		String officeId = queryStr.get("officeId");//受理网点
		String certType = queryStr.get("certType");//产品名称
		String workType = queryStr.get("workType");//业务类型
		String payType = queryStr.get("payType");//计费策略
		String payMethod = queryStr.get("payMethod");//付款方式
		
		/*if (startTime != null) {
			sql.append(" and workcertin6_.notafter >= TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");

		}

		if (endTime != null) {
			sql.append(" and workcertin6_.notafter <= TO_DATE ('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}*/
		/*if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus",
					workDealInfo.getDealInfoStatus()));
		}
		*/
		
		//显示所有业务完成的数据
		sql.append(
				" and ( wdi.deal_info_status="+WorkDealInfoStatus.STATUS_CERT_OBTAINED 
				+" or ( wdi.deal_info_status="+WorkDealInfoStatus.STATUS_CERT_REVOKE
				//+" and (wdi.is_revoke_business <> 1 or wdi.is_revoke_business is null)"
				+ " ) )"
				);
		
		//单位名称
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				sql.append(" and wc.id="
						+ workDealInfo.getWorkCompany().getId());
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName()
							.equals("")) {
				sql.append(" and wc.company_name like '%"
						+ workDealInfo.getWorkCompany().getCompanyName() + "%'");
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber() != null
					&& !workDealInfo.getWorkCompany().getOrganizationNumber()
							.equals("")) {
				sql.append(" and wc.organization_number = '"
						+ workDealInfo.getWorkCompany().getOrganizationNumber()
						+ "'");
			}
			//省市县
			if (workDealInfo.getWorkCompany().getProvince() != null
					&& !workDealInfo.getWorkCompany().getProvince()
							.equals("") && !workDealInfo.getWorkCompany().getProvince()
							.equals("省份")) {
				sql.append(" and wc.province = '"
						+ workDealInfo.getWorkCompany().getProvince()
						+ "'");
			}
			if (workDealInfo.getWorkCompany().getCity() != null
					&& !workDealInfo.getWorkCompany().getCity()
							.equals("")&& !workDealInfo.getWorkCompany().getCity()
							.equals("地级市")) {
				sql.append(" and wc.city = '"
						+ workDealInfo.getWorkCompany().getCity()
						+ "'");
			}
			if (workDealInfo.getWorkCompany().getDistrict() != null
					&& !workDealInfo.getWorkCompany().getDistrict()
							.equals("")&& !workDealInfo.getWorkCompany().getDistrict()
							.equals("市、县级市")) {
				sql.append(" and wc.district = '"
						+ workDealInfo.getWorkCompany().getDistrict()
						+ "'");
			}
		}
		//联系人姓名 联系人电话 固定电话
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				sql.append(" and wu.contact_name like '%"
						+ EscapeUtil.escapeLike(workDealInfo.getWorkUser()
								.getContactName()) + "%'");
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				sql.append(" and wu.contact_tel like '%"
						+ EscapeUtil.escapeLike(workDealInfo.getWorkUser()
								.getContactTel()) + "%'");
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				sql.append(" and wu.contact_phone like '%"
						+ EscapeUtil.escapeLike(workDealInfo.getWorkUser()
								.getContactPhone()) + "%'");
			}
		}
		if (workDealInfo.getDealInfoStatus() != null
				&& !workDealInfo.getDealInfoStatus().equals("")) {
			sql.append(" and wdi.deal_info_status = "
					+ workDealInfo.getDealInfoStatus());
		}
		//key 序列号
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			sql.append(" and wdi.key_sn like '%"
					+ EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%'");
		}
		if (workDealInfo.getStatus() != null) {
			sql.append(" and wdi.status = " + workDealInfo.getStatus());
		}
		//受理区域，受理网点，产品名称
		if (area != null && !area.equals("")) {
			sql.append(" and wdi.area_id = " + area);
		}
		if (officeId != null && !officeId.equals("")) {
			sql.append(" and wdi.office_id = " + officeId);
		}
		if (certType != null && !certType.equals("")) {
			sql.append(" and cp.product_name = " + certType);
		}
		
		//业务类型 ,模板策略 ,付款方式
		if (workType != null && !workType.equals("")) {
			if (workType.equals("5")){
				sql.append(" and ( wdi.deal_info_type <> 11 or wdi.deal_info_type is null ");
			}else{
				sql.append(" and ( wdi.deal_info_type = "+workType+
						" or wdi.deal_info_type1 = "+workType+
						" or wdi.deal_info_type2 = "+workType+
						" or wdi.deal_info_type3 = "+workType+" )");
			}
		}
		if (workDealInfo.getPayType() != null && workDealInfo.getPayType()!=0) {
			sql.append(" and wdi.pay_type = " + workDealInfo.getPayType());
		}
		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				sql.append(" and (wpi.method_money=1 or wpi.relation_method=0) ");
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				sql.append(" and (wpi.method_pos=1 or wpi.relation_method=1) ");
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				sql.append(" and (wpi.method_bank=1 or wpi.relation_method=2) ");
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				sql.append(" and (wpi.method_alipay=1 or wpi.relation_method=3) ");
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				sql.append(" and wpi.method_gov = 1 ");
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				sql.append(" and wpi.method_contract = 1 ");
			}
		}
		/*if (makeCertStartTime != null && makeCertEndTime != null) {
			sql.append(" and workcertin6_.sign_date >= TO_DATE('"
					+ DateUtils.formatDate(makeCertStartTime,
							"yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			sql.append(" and workcertin6_.sign_date <= TO_DATE ('"
					+ DateUtils.formatDate(makeCertEndTime,
							"yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
		}*/
		sql.append(" order by wdi.create_date desc,wdi.svn asc)");
		return sql.toString();
	}
	
	
	
	
	
	@Transactional(readOnly=false)
	public void executeBatchUpdate(String[] infoIds,ConfigChargeAgent agent,Integer yearU,Date expirationDate, String methodPay){
		
		String timeSvn = getTimeSvn();
		int num = getNumSvn(timeSvn);
		
		User user = UserUtils.getUser();
		Office office = user.getOffice();
		
		Double receiptMoney = 0d;
		
		
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(office);
		ConfigAgentOfficeRelation configAgentOfficeRelation = null;
		if(configAgentOfficeRelations.size()>0){
			configAgentOfficeRelation = configAgentOfficeRelations.get(0);
		}
		
		List<ReceiptDepotInfo> depotInfos = receiptDepotInfoService.findDepotByOffice(office);
		ReceiptDepotInfo depotInfo = null;
		if(depotInfos.size()>0){
			depotInfo = depotInfos.get(0);
		}
		
		
		WorkDealInfo workDealInfo1 = null;
		
		
		Double money = configChargeAgentDetailService.getChargeMoney(agent.getId(), 1, yearU!=null?yearU:StringHelper.getDvalueYear(expirationDate));
	
		for (int i = 0; i < infoIds.length; i++) {
			try {
				Long infoId = Long.parseLong(infoIds[i]);
				
				receiptMoney += money;
				
				executeSingleUpdate(infoId,agent,yearU,expirationDate,methodPay,workDealInfo1,money,user,office,configAgentOfficeRelation,depotInfo,timeSvn,num,i);
			
			
			} catch (Exception e) {
				
				Integer availableNum= agent.getAvailableNum();
				Integer surUpdateNum = agent.getSurplusUpdateNum();
				agent.setAvailableNum(availableNum+1);
				agent.setSurplusUpdateNum(surUpdateNum - i);
				configChargeAgentService.save(agent);
				logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
					
				e.printStackTrace();
				break;
			}
			
			
	}
		
		
		if(depotInfo!=null){
			depotInfo.setReceiptResidue(depotInfo.getReceiptResidue() - receiptMoney);
			depotInfo.setReceiptOut(depotInfo.getReceiptOut() + receiptMoney);
			receiptDepotInfoService.save(depotInfo);	
		}
		
		Integer availableNum= agent.getAvailableNum();
		
		Integer surUpdateNum = agent.getSurplusUpdateNum();
		agent.setReserveUpdateNum(availableNum +infoIds.length+1 );
		agent.setSurplusUpdateNum(surUpdateNum - (infoIds.length+1));
		configChargeAgentService.save(agent);
		logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");	
	
	
	}
	
	
	@Transactional
	public void executeSingleUpdate(Long infoId,ConfigChargeAgent agent,Integer yearU,Date expirationDate, 
			String methodPay,WorkDealInfo workDealInfo1,Double money,User user,Office office,ConfigAgentOfficeRelation configAgentOfficeRelation,
			ReceiptDepotInfo depotInfo,String timeSvn,int num,int i){
		
		WorkCompanyHis companyHis = null;
		// 保存经办人信息
		WorkUser workUser = workDealInfo1.getWorkUser();
		WorkUserHis userHis = workUserService.change(workUser,companyHis);
		workUserHisService.save(userHis);
		// 变更业务保存单位信息
		WorkCompany workCompany = workDealInfo1.getWorkCompany();
		companyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(companyHis);

		// workDealInfo1
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		
		if (configAgentOfficeRelation!=null) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelation.getConfigCommercialAgent());// 劳务关系外键
		}

		workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));

		
		workDealInfo.setConfigChargeAgentId(agent.getId());
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUserHis(userHis);
		workDealInfo.setWorkCompanyHis(companyHis);
		workDealInfo.setConfigProduct(workDealInfo1.getConfigProduct());

		// 经信委
		if (yearU != null && expirationDate == null) {
			workDealInfo.setYear(yearU);
			workDealInfo.setExpirationDate(null);
		} else {
			workDealInfo.setYear(StringHelper.getDvalueYear(expirationDate));
			workDealInfo.setExpirationDate(expirationDate);
		}

		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
		workDealInfo.setCreateBy(user);
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setPrevId(workDealInfo1.getId());
		if(workDealInfo1.getRemarkInfo()==null){
			workDealInfo.setSvn(getSVN(0));	
		}else{
			int serno = num+i+1;
			String numStr = getNumStrSvn(serno);
			workDealInfo.setSvn(timeSvn+numStr);	
		}
		
		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo().getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}
		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
		workCertApplyInfoService.save(workCertApplyInfo);
		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate(workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		delete(workDealInfo1.getId());

		workDealInfo.setInputUser(user);
		workDealInfo.setInputUserDate(new Date());
		save(workDealInfo);

		ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
		dealInfoBound.setDealInfo(workDealInfo);
		dealInfoBound.setAgent(agent);
		configAgentBoundDealInfoService.save(dealInfoBound);
		logUtil.saveSysLog("计费策略模版", "计费策略模版：" + agent.getId()
				+ "--业务编号：" + workDealInfo.getId() + "--关联成功!", "");

		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent("批量更新");
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(user);
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(office);
		workLogService.save(workLog);

		WorkPayInfo workPayInfo = new WorkPayInfo();
		workPayInfo.setOpenAccountMoney(0d);
		workPayInfo.setAddCert(0d);

		

		if (methodPay.equals("0")) {
			workPayInfo.setMethodPos(true);
		} else if (methodPay.equals("1")) {
			workPayInfo.setMethodMoney(true);
		} else if (methodPay.equals("2")) {
			workPayInfo.setMethodBank(true);
		}

		if (agent.getTempStyle().equals("2")) {
			workPayInfo.setMethodGov(true);
		} else if (agent.getTempStyle().equals("3")) {
			workPayInfo.setMethodContract(true);
		} else {
			workPayInfo.setMethodGov(false);
			workPayInfo.setMethodContract(false);
		}

		workPayInfo.setUpdateCert(money);
		workPayInfo.setErrorReplaceCert(0d);
		workPayInfo.setLostReplaceCert(0d);
		workPayInfo.setInfoChange(0d);
		workPayInfo.setElectron(0d);
		workPayInfo.setOldOpenAccountMoney(0d);
		workPayInfo.setOldAddCert(0d);
		workPayInfo.setOldUpdateCert(0d);
		workPayInfo.setOldErrorReplaceCert(0d);
		workPayInfo.setOldLostReplaceCert(0d);
		workPayInfo.setOldInfoChange(0d);
		workPayInfo.setOldElectron(0d);

		double bindMoney = money;
		workPayInfo.setMoney(bindMoney);

		workPayInfo.setWorkTotalMoney(money);
		workPayInfo.setWorkPayedMoney(money);
		workPayInfo.setWorkReceivaMoney(money);
		workPayInfo.setUserReceipt(true);
		workPayInfo.setReceiptAmount(money);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);

		workDealInfo.setWorkPayInfo(workPayInfo);

		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_WAIT);

		checkWorkDealInfoNeedSettle(workDealInfo);

		workDealInfo.setPayUser(user);
		workDealInfo.setPayUserDate(new Date());

		workDealInfo.setAreaId(office.getParent().getId());
		workDealInfo.setOfficeId(office.getId());

		save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo,raAccount.getConfigRaAccountExtendInfo());

		logUtil.saveSysLog("业务中心", "业务维护：编号" + workDealInfo.getId()
				+ "缴费" + money + "元", "");
		ReceiptInvoice receiptInvoice = new ReceiptInvoice();

		
		receiptInvoice.setReceiptDepotInfo(depotInfo);
		receiptInvoice.setCompanyName(workDealInfo.getWorkCompany().getCompanyName());
		receiptInvoice.setReceiptMoney(money);
		receiptInvoice.setReceiptType(0);// 销售出库
		receiptInvoice.setDealInfoId(workDealInfo.getId());
		receiptInvoiceService.save(receiptInvoice);
		
	}
	
	
	
	private int getLastCertDay(Date notAfter) {
		
		Long MILL = 86400000L;
		
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong < nowLong) {
			return 0;
		}

		long d = (notAfterLong - nowLong) / MILL;

		long hour1 = (notAfterLong - nowLong) % MILL;
		if (hour1 > 0) {
			d += 1;
		}
		return (int) d;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
