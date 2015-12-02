package com.itrus.ca.modules.work.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
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
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.CreateExcelUtils;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.dao.WorkCertInfoDao;
import com.itrus.ca.modules.work.dao.WorkCertTrustApplyDao;
import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;

import antlr.Utils;

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

	private LogUtil logUtil = new LogUtil();
	private static final int CELL_NUM = 9;

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkDealInfoService.class);

	@Autowired
	private WorkDealInfoDao workDealInfoDao;

	@Autowired
	private WorkCertTrustApplyDao workCertTrustApplyDao;

	@Autowired
	private WorkCertInfoDao workCertInfoDao;

	@Autowired
	private ConfigAppService configAppService;

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
	
	static Long YEAR_MILL = 31536000000L;

	public WorkDealInfo get(Long id) {
		return workDealInfoDao.findOne(id);
	}

	public Page<WorkDealInfo> findAppList(Page<WorkDealInfo> page, WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
		}
		// dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG,
		// WorkDealInfo.DEL_FLAG_NORMAL));
		// dc.add(Restrictions.isNotNull("userSn"));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
				Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_RETURN_MONEY),
				Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_RETURN_WORK)));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findByIds(Page<WorkDealInfo> page, WorkDealInfo workDealInfo,
			ArrayList<Long> dealInfoIds) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
		}
		dc.add(Restrictions.in("id", dealInfoIds));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
				Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_RETURN_MONEY),
				Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_RETURN_WORK)));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> find(Page<WorkDealInfo> page, WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();

		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));

		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			dc.createAlias("workCompany", "workCompany");
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			dc.createAlias("workUser", "workUser");
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like("workUser.contactPhone",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like("workUser.contactTel",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoType() != null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workDealInfo.getDealInfoType()),
					Restrictions.eq("dealInfoType1", workDealInfo.getDealInfoType())));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		if (workDealInfo.getDelFlag() != null) {
			dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}
	public Page<WorkDealInfo> find11(Page<WorkDealInfo> page,WorkDealInfo workDealInfo,Long apply,Integer workType,Integer wdiStatus){
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));

		if (workDealInfo.getWorkCertInfo() != null) {
			dc.add(Restrictions.isNotNull("notafter"));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like("workUser.contactPhone",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like("workUser.contactTel",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() == null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"), Restrictions.eq("dealInfoStatus", "0"),Restrictions.eq("dealInfoStatus", "13")));
		} else {
			if (workDealInfo.getDealInfoStatus().equals("-1")) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
						Restrictions.eq("dealInfoStatus", "0"),Restrictions.eq("dealInfoStatus", "13")));
			} else {
				dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
			}
		}

		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType() != -1) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workDealInfo.getDealInfoType()),
						Restrictions.eq("dealInfoType1", workDealInfo.getDealInfoType())));
			}
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}
	public Page<WorkDealInfo> find1(Page<WorkDealInfo> page, WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
		}
		if (workDealInfo.getWorkCompany() != null) {
			dc.createAlias("workCompany", "workCompany");
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
		}
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findByStatus(Page<WorkDealInfo> page, WorkDealInfo workDealInfo) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		// dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));

		if (workDealInfo.getWorkCertInfo() != null) {
			dc.add(Restrictions.isNotNull("notafter"));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like("workUser.contactPhone",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like("workUser.contactTel",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() == null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"), Restrictions.eq("dealInfoStatus", "0"),Restrictions.eq("dealInfoStatus", "13")));
		} else {
			if (workDealInfo.getDealInfoStatus().equals("-1")) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
						Restrictions.eq("dealInfoStatus", "0"),Restrictions.eq("dealInfoStatus", "13")));
			} else {
				dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
			}
		}

		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType() != -1) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workDealInfo.getDealInfoType()),
						Restrictions.eq("dealInfoType1", workDealInfo.getDealInfoType())));
			}
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
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
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactPhone() != null
					&& !workDealInfo.getWorkUser().getContactPhone().equals("")) {
				dc.add(Restrictions.like("workUser.contactPhone",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactPhone()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like("workUser.contactTel",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() == null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"), Restrictions.eq("dealInfoStatus", "0")));
		} else {
			if (workDealInfo.getDealInfoStatus().equals("-1")) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", "12"),
						Restrictions.eq("dealInfoStatus", "0"),Restrictions.eq("dealInfoStatus", "13")));
			} else {
				dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
			}
		}

		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType() != -1) {
				dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workDealInfo.getDealInfoType()),
						Restrictions.eq("dealInfoType1", workDealInfo.getDealInfoType())));
			}
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		dc.add(Restrictions.isNotNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public Page<WorkDealInfo> find4Apply(Page<WorkDealInfo> page, WorkDealInfo workDealInfo, Date startTime,
			Date endTime , Long apply ) {
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
			dc.add(Restrictions.ge("notafter", endTime));
		}
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber()!=null && !workDealInfo.getWorkCompany().getOrganizationNumber().equals("") ) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getOrganizationNumber())));
				
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus()!= null && !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}

		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(page, dc);
	}
//	public Page<WorkDealInfo> find4Apply(Page<WorkDealInfo> page, WorkDealInfo workDealInfo,Long apply,Integer companyID, String productType,Integer workType,
//			) {
//		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
//		dc.createAlias("workUser", "workUser");
//		dc.createAlias("workCompany", "workCompany");
//		dc.createAlias("createBy", "createBy");
//		dc.createAlias("createBy.office", "office");
//		dc.createAlias("configApp", "configApp");
//
//		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
//		// dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
//		// WorkDealInfoStatus.STATUS_CERT_OBTAINED), Restrictions.eq(
//		// "createBy", UserUtils.getUser())));
//
//		if (apply != null) {
//			dc.add(Restrictions.eq("configApp.id", apply));
//		}
//		
//		
//		if (startTime != null && endTime != null) {
//			dc.add(Restrictions.ge("notafter", startTime));
//			dc.add(Restrictions.le("notafter", endTime));
//		}
//		if (workDealInfo.getWorkCompany() != null) {
//			if (workDealInfo.getWorkCompany().getId() != null) {
//				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
//			}
//			if (workDealInfo.getWorkCompany().getCompanyName() != null
//					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
//				dc.add(Restrictions.like("workCompany.companyName",
//						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
//			}
//			if (workDealInfo.getWorkCompany().getOrganizationNumber()!=null && !workDealInfo.getWorkCompany().getOrganizationNumber().equals("") ) {
//				dc.add(Restrictions.like("workCompany.organizationNumber",
//						EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getOrganizationNumber())));
//				
//			}
//		}
//		if (workDealInfo.getWorkUser() != null) {
//			if (workDealInfo.getWorkUser().getContactName() != null
//					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
//				dc.add(Restrictions.like("workUser.contactName",
//						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
//			}
//		}
//		if (workDealInfo.getDealInfoStatus()!= null && !workDealInfo.getDealInfoStatus().equals("")) {
//			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
//		}
//		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
//			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
//		}
//		if (workDealInfo.getStatus() != null) {
//			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
//		}
//
//		dc.add(Restrictions.isNull("isIxin"));
//		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("createDate"));
//		return workDealInfoDao.find(page, dc);
//	}
	public Page<WorkDealInfo> find5pply(Page<WorkDealInfo> page, WorkDealInfo workDealInfo, 
			String companyName, Long apply ) {
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
		
		if (workDealInfo.getWorkCompany() != null) {
			if (workDealInfo.getWorkCompany().getId() != null) {
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber()!=null && !workDealInfo.getWorkCompany().getOrganizationNumber().equals("") ) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getOrganizationNumber())));
				
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus()!= null && !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}
		
		dc.add(Restrictions.isNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(page, dc);
	}


	public List<WorkDealInfo> find4ApplyIsIxin(WorkDealInfo workDealInfo, Date startTime,
			Date endTime , Long apply ) {
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
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
			if (workDealInfo.getWorkCompany().getOrganizationNumber()!=null && !workDealInfo.getWorkCompany().getOrganizationNumber().equals("") ) {
				dc.add(Restrictions.like("workCompany.organizationNumber",
						EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getOrganizationNumber())));
				
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus()!= null && !workDealInfo.getDealInfoStatus().equals("")) {
			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		if (workDealInfo.getStatus() != null) {
			dc.add(Restrictions.eq("status", workDealInfo.getStatus()));
		}

		dc.add(Restrictions.isNotNull("isIxin"));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return workDealInfoDao.find(dc);
	}
	public Page<WorkDealInfo> find(Page<WorkDealInfo> page, WorkDealInfo workDealInfo, Long area, Long office,
			Long apply, String certType, Integer workType, Integer year, Date luruStartTime, Date luruEndTime,
			List<Office> offices, Date daoqiStartTime, Date daoqiEndTime, Date jianzhengStartTime,
			Date jianzhengEndTime, List<WorkCertInfo> certInfoList

	) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("updateBy", "updateBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		// workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.in("createBy.office", offices));

		// workUser.contactName
		// workUser.conCertNumber
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName",
					"%" + workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber",
					"%" + workDealInfo.getWorkCompany().getOrganizationNumber() + "%"));
		}
		if (workDealInfo.getWorkUser() != null && StringUtils.isNotEmpty(workDealInfo.getWorkUser().getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%" + workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser().getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber",
					"%" + workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + workDealInfo.getKeySn() + "%"));
		}
		
		if (workDealInfo.getPayType() != null && workDealInfo.getPayType() != 0) {
			dc.add(Restrictions.eq("payType", workDealInfo.getPayType()));
		}
		
		if (workDealInfo.getInputUser()!=null && !workDealInfo.getInputUser().getName().equals("")) {
			dc.createAlias("inputUser", "inputUser");
			dc.add(Restrictions.like("inputUser.name","%" + workDealInfo.getInputUser().getName() + "%"));
		}
		if (workDealInfo.getAttestationUser()!=null && !workDealInfo.getAttestationUser().getName().equals("")) {
			dc.createAlias("attestationUser", "attestationUser");
			dc.add(Restrictions.like("attestationUser.name","%" + workDealInfo.getAttestationUser().getName() + "%"));
		}
		//businessCardUser
		if (workDealInfo.getBusinessCardUser()!=null && !workDealInfo.getBusinessCardUser().getName().equals("")) {
			dc.createAlias("businessCardUser", "businessCardUser");
			dc.add(Restrictions.like("businessCardUser.name","%" + workDealInfo.getBusinessCardUser().getName() + "%"));
		}
		
		
		
		
		
		
		// workCompany.province
		// workCompany.city
		// workCompany.district
		if (workDealInfo.getWorkCompany() != null && StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getProvince())
				&& !workDealInfo.getWorkCompany().getProvince().equals("省份")) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null && StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getCity())
				&& !workDealInfo.getWorkCompany().getCity().equals("地级市")) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null && StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getDistrict())
				&& !workDealInfo.getWorkCompany().getDistrict().equals("市、县级市")) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo.getWorkCompany().getDistrict()));
		}

		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodMoney", true),
						Restrictions.eq("workPayInfo.relationMethod", 0)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodPos", true),
						Restrictions.eq("workPayInfo.relationMethod", 1)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodBank", true),
						Restrictions.eq("workPayInfo.relationMethod", 2)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodAlipay", true),
						Restrictions.eq("workPayInfo.relationMethod", 3)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		if (jianzhengStartTime != null) {
			dc.add(Restrictions.ge("updateDate", jianzhengStartTime));
		}
		if (jianzhengEndTime != null) {
			jianzhengEndTime.setHours(23);
			jianzhengEndTime.setMinutes(59);
			jianzhengEndTime.setSeconds(59);
			dc.add(Restrictions.le("updateDate", jianzhengEndTime));
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

		if (certInfoList.size() > 0) {
			dc.add(Restrictions.in("workCertInfo", certInfoList));
		}

		if (office != null) {
			dc.add(Restrictions.eq("office.id", office));
		} else if (area != null) {

			dc.add(Restrictions.eq("company.id", area));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (certType != null && !certType.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", certType));
		}

		if (workType != null) {
			dc.add(Restrictions.eq("dealInfoType", workType));
		}

		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		// ProjectionList projectionList1 = Projections.projectionList();
		// projectionList1.add(Projections.sqlGroupProjection("dealInfoType",
		// "dealInfoType", null, null));

		return workDealInfoDao.find(page, dc);

	}

	public List<WorkDealInfo> find(WorkDealInfo workDealInfo, Long area, Long office, Long apply, String certType,
			Integer workType, Integer year, Date luruStartTime, Date luruEndTime, List<Office> offices,
			Date daoqiStartTime, Date daoqiEndTime, Date jianzhengStartTime, Date jianzhengEndTime,
			List<WorkCertInfo> certInfoList

	) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("createBy", "createBy");
		dc.createAlias("updateBy", "updateBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		// workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.in("createBy.office", offices));

		// workUser.contactName
		// workUser.conCertNumber
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName",
					"%" + workDealInfo.getWorkCompany().getCompanyName() + "%"));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getOrganizationNumber())) {
			dc.add(Restrictions.like("workCompany.organizationNumber",
					"%" + workDealInfo.getWorkCompany().getOrganizationNumber() + "%"));
		}
		if (workDealInfo.getWorkUser() != null && StringUtils.isNotEmpty(workDealInfo.getWorkUser().getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%" + workDealInfo.getWorkUser().getContactName() + "%"));
		}
		if (workDealInfo.getWorkUser() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkUser().getConCertNumber())) {
			dc.add(Restrictions.like("workUser.conCertNumber",
					"%" + workDealInfo.getWorkUser().getConCertNumber() + "%"));
		}

		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + workDealInfo.getKeySn() + "%"));
		}
		if (workDealInfo.getCreateBy() != null && StringUtils.isNotEmpty(workDealInfo.getCreateBy().getName())) {
			dc.add(Restrictions.like("createBy.name", "%" + workDealInfo.getCreateBy().getName() + "%"));
		}
		if (workDealInfo.getCreateBy() != null && StringUtils.isNotEmpty(workDealInfo.getUpdateBy().getName())) {
			dc.add(Restrictions.like("updateBy.name", "%" + workDealInfo.getUpdateBy().getName() + "%"));
		}
		if (workDealInfo.getPayType() != null && workDealInfo.getPayType() != 0) {
			dc.add(Restrictions.eq("payType", workDealInfo.getPayType()));
		}
		// workCompany.province
		// workCompany.city
		// workCompany.district
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getProvince())) {
			dc.add(Restrictions.eq("workCompany.province", workDealInfo.getWorkCompany().getProvince()));
		}
		if (workDealInfo.getWorkCompany() != null && StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getCity())) {
			dc.add(Restrictions.eq("workCompany.city", workDealInfo.getWorkCompany().getCity()));
		}
		if (workDealInfo.getWorkCompany() != null
				&& StringUtils.isNotEmpty(workDealInfo.getWorkCompany().getDistrict())) {
			dc.add(Restrictions.eq("workCompany.district", workDealInfo.getWorkCompany().getDistrict()));
		}

		if (workDealInfo.getWorkPayInfo() != null) {
			if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodMoney", true),
						Restrictions.eq("workPayInfo.relationMethod", 0)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodPos", true),
						Restrictions.eq("workPayInfo.relationMethod", 1)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodBank", true),
						Restrictions.eq("workPayInfo.relationMethod", 2)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
				dc.add(Restrictions.or(Restrictions.eq("workPayInfo.methodAlipay", true),
						Restrictions.eq("workPayInfo.relationMethod", 3)));
			}
			if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
			}
			if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
			}
		}

		if (jianzhengStartTime != null) {
			dc.add(Restrictions.ge("attestationUserDate", jianzhengStartTime));
		}
		if (jianzhengEndTime != null) {
			jianzhengEndTime.setHours(23);
			jianzhengEndTime.setMinutes(59);
			jianzhengEndTime.setSeconds(59);
			dc.add(Restrictions.le("attestationUserDate", jianzhengEndTime));
		}

		if (luruStartTime != null) {
			dc.add(Restrictions.ge("createDate", luruStartTime));
		}
		if (luruEndTime != null) {
			luruEndTime.setHours(23);
			luruEndTime.setMinutes(59);
			luruEndTime.setSeconds(59);
			dc.add(Restrictions.le("createDate", luruEndTime));
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

		if (certInfoList.size() > 0) {
			dc.add(Restrictions.in("workCertInfo", certInfoList));
		}

		

		if (office != null) {
			dc.add(Restrictions.eq("office.id", office));
		} else if (area != null) {

			dc.add(Restrictions.eq("company.id", area));
		}

		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if (certType != null && !certType.equals("")) {
			dc.add(Restrictions.eq("configProduct.productName", certType));
		}

		if (workType != null) {
			dc.add(Restrictions.eq("dealInfoType", workType));
		}

		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}

		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		// ProjectionList projectionList1 = Projections.projectionList();
		// projectionList1.add(Projections.sqlGroupProjection("dealInfoType",
		// "dealInfoType", null, null));

		return workDealInfoDao.find(dc);

	}

	public Page<WorkDealInfo> findCustomer(Page<WorkDealInfo> page, WorkDealInfo workDealInfo, Date startTime,
			Date endTime) {
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
				dc.add(Restrictions.eq("workCompany", workDealInfo.getWorkCompany()));
			}
			if (workDealInfo.getWorkCompany().getCompanyName() != null
					&& !workDealInfo.getWorkCompany().getCompanyName().equals("")) {
				dc.add(Restrictions.like("workCompany.companyName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkCompany().getCompanyName()) + "%"));
			}
		}
		if (workDealInfo.getConfigApp() != null) {
			if (workDealInfo.getConfigApp().getAppName() != null
					&& !workDealInfo.getConfigApp().getAppName().equals("")) {
				dc.add(Restrictions.eq("configApp.appName", workDealInfo.getConfigApp().getAppName()));
			}
		}
		if (workDealInfo.getWorkUser() != null) {
			if (workDealInfo.getWorkUser().getContactName() != null
					&& !workDealInfo.getWorkUser().getContactName().equals("")) {
				dc.add(Restrictions.like("workUser.contactName",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactName()) + "%"));
			}
			/*
			 * if (workDealInfo.getWorkUser().getContactPhone() != null &&
			 * !workDealInfo.getWorkUser().getContactPhone().equals("")) {
			 * dc.add(Restrictions.like("workUser.contactPhone", "%" +
			 * workDealInfo.getWorkUser().getContactPhone() + "%")); }
			 */
			if (workDealInfo.getWorkUser().getContactEmail() != null
					&& !workDealInfo.getWorkUser().getContactEmail().equals("")) {
				dc.add(Restrictions.like("workUser.contactEmail",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactEmail()) + "%"));
			}
			if (workDealInfo.getWorkUser().getContactTel() != null
					&& !workDealInfo.getWorkUser().getContactTel().equals("")) {
				dc.add(Restrictions.like("workUser.contactTel",
						"%" + EscapeUtil.escapeLike(workDealInfo.getWorkUser().getContactTel()) + "%"));
			}
		}
		if (workDealInfo.getDealInfoStatus() != null) {
			dc.add(Restrictions.eq("dealInfoStatus", workDealInfo.getDealInfoStatus()));
		}
		if (StringUtils.isNotEmpty(workDealInfo.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(workDealInfo.getKeySn()) + "%"));
		}
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}

	// 根据支付信息查询绑定的支付信息
	public Page<WorkDealInfo> findByFinanceId(Page<WorkDealInfo> page, List<Long> idList) {
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
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findByKeySn(String Keysn, String certSn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}
		if (certSn != null) {
			dc.add(Restrictions.like("certSn", "%" + EscapeUtil.escapeLike(certSn) + "%"));
		}
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public Integer findByKey(String Keysn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (Keysn != null) {
			dc.add(Restrictions.eq("keySn", Keysn));
		}

		// a.DEAL_INFO_STATUS='6';
		dc.add(Restrictions.ne("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE));
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	public List<WorkDealInfo> findByManyCondition(String status, Date date, Integer year, Integer type) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		if (year != null) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.ge("createDate", date));
		dc.add(Restrictions.eq("dealInfoStatus", status));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", type), Restrictions.or(
				Restrictions.eq("dealInfoType1", type),
				Restrictions.or(Restrictions.eq("dealInfoType2", type), Restrictions.eq("dealInfoType3", type)))));
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(dc);
	}

	// 根据引用名称，查询统计信息
	public List<WorkDealInfo> findByAppName(String appName) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appName != null) {
			dc.add(Restrictions.like("configApp.appName", "%" + EscapeUtil.escapeLike(appName) + "%"));
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
	public List<WorkDealInfo> selectAccount(ConfigApp configApp, ConfigProduct configProduct, WorkCompany workCompany,
			Long id) {
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
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
	}

	public Page<WorkDealInfo> findByDealPay(Page<WorkDealInfo> page, WorkDealInfo workDealInfo, Date startTime,
			Date endTime, List<Long> idList, List<Long> dealInfoByAreaIds, List<Long> dealInfoByOfficeAreaIds,
			Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.isNotNull("workPayInfo"));
		dc.add(Restrictions.eq("workPayInfo.delFlag", WorkPayInfo.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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

		// if(workDealInfo.getWorkPayInfo()!=null){
		// List<Criterion> expressions = new ArrayList<Criterion>();
		// SimpleExpression money = null;
		// SimpleExpression pos = null;
		// SimpleExpression bank = null;
		// SimpleExpression alipay = null;
		//
		// if (workDealInfo.getWorkPayInfo().getMethodMoney()!=null) {
		// money = Restrictions.eq("workPayInfo.methodMoney", true);
		// }
		// if (workDealInfo.getWorkPayInfo().getMethodPos()!=null) {
		// pos = Restrictions.eq("workPayInfo.methodPos", true);
		// }
		// if (workDealInfo.getWorkPayInfo().getMethodBank()!=null) {
		// bank = Restrictions.eq("workPayInfo.methodBank", true);
		// }
		// if (workDealInfo.getWorkPayInfo().getMethodAlipay()!=null) {
		// alipay = Restrictions.eq("workPayInfo.methodAlipay", true);
		// }
		// dc.add(Restrictions.or(money==null? Restrictions.eq("id",-1l):money,
		// pos==null? Restrictions.eq("id",-2l):pos,
		// bank==null? Restrictions.eq("id",-3l):bank,
		// alipay==null? Restrictions.eq("id",-4l):alipay));
		// }
		if (startTime != null) {
			dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
		}
		if (endTime != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);
			dc.add(Restrictions.le("workPayInfo.createDate", calendar.getTime()));
		}
		if (idList != null && idList.size() > 0) {
			dc.add(Restrictions.in("id", idList));
		}
		if (dealInfoByAreaIds != null && dealInfoByAreaIds.size() > 0) {
			dc.add(Restrictions.in("id", dealInfoByAreaIds));
		}
		if (dealInfoByOfficeAreaIds != null && dealInfoByOfficeAreaIds.size() > 0) {
			dc.add(Restrictions.in("id", dealInfoByOfficeAreaIds));
		}
		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}

		dc.addOrder(Order.desc("id"));
		return workDealInfoDao.find(page, dc);
	}
	/**
	 * @param startTime
	 * @param endTime
	 * @param dealInfoByAreaIds
	 * @param appId
	 * @return
	 */
	public List<WorkDealInfo> findByDayPay(Date startTime,Date endTime,List<Long> officeids,List<Long> dealInfoByAreaIds,
			Long appId) {
		if(startTime!=null)
		{
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
			dc.createAlias("workPayInfo", "workPayInfo");
			dc.createAlias("createBy", "createBy");
			dc.createAlias("createBy.office", "office");
			dc.add(Restrictions.isNotNull("workPayInfo"));
//			dc.add(Restrictions.eq("workPayInfo.delFlag", WorkPayInfo.DEL_FLAG_NORMAL));
//			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
//			if (workDealInfo.getWorkPayInfo() != null) {
//				if (workDealInfo.getWorkPayInfo().getMethodMoney() == true) {
//					dc.add(Restrictions.eq("workPayInfo.methodMoney", true));
//				}
//				if (workDealInfo.getWorkPayInfo().getMethodPos() == true) {
//					dc.add(Restrictions.eq("workPayInfo.methodPos", true));
//				}
//				if (workDealInfo.getWorkPayInfo().getMethodBank() == true) {
//					dc.add(Restrictions.eq("workPayInfo.methodBank", true));
//				}
//				if (workDealInfo.getWorkPayInfo().getMethodAlipay() == true) {
//					dc.add(Restrictions.eq("workPayInfo.methodAlipay", true));
//				}
//				if (workDealInfo.getWorkPayInfo().getMethodGov() == true) {
//					dc.add(Restrictions.eq("workPayInfo.methodGov", true));
//				}
//				if (workDealInfo.getWorkPayInfo().getMethodContract() == true) {
//					dc.add(Restrictions.eq("workPayInfo.methodContract", true));
//				}
//			}
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
			if(officeids!=null)
			{
				dc.add(Restrictions.in("office.id", officeids));
			}
			dc.addOrder(Order.asc("workPayInfo.createDate"));
			return workDealInfoDao.find(dc);
		}else
		{
			return null;
		}
		
	}
	public List<WorkDealInfo> findByProjectPay(Date startTime,Date endTime,List<Long> officeids,List<Long> dealInfoByAreaIds,
			Long appId) {
		if(startTime!=null)
		{
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
			dc.createAlias("workPayInfo", "workPayInfo");
			dc.createAlias("createBy", "createBy");
			dc.createAlias("createBy.office", "office");
			dc.add(Restrictions.isNotNull("workPayInfo"));
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
			if(officeids!=null&& officeids.size() > 0)
			{
				dc.add(Restrictions.in("office.id", officeids));
			}
			dc.addOrder(Order.desc("id"));
			return workDealInfoDao.find(dc);
		}else
		{
			return null;
		}
		
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
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		return workDealInfoDao.find(dc);
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
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
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
	public Page<WorkDealInfo> findDealInfo(Page<WorkDealInfo> page, Long appId, Long productId, Integer dealInfoType,
			Integer year, String agentName) {
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(page, dc);
	}

	public Long findDealInfoMax() {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(WorkDealInfo.DEL_FLAG, WorkDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		if (workDealInfoDao.find(dc).size() > 0) {
			return workDealInfoDao.find(dc).get(0).getId();
		} else {

			return 0l;
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
	public Page<WorkDealInfo> findDealInfos(Page<WorkDealInfo> page, Long appId, Long productId, Integer dealInfoType,
			Integer year, String agentName, Date startDate, Date endDate, boolean isCanSettle) {
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(page, dc);
	}

	public Page<WorkDealInfo> findByFinance(Page<WorkDealInfo> page, List<WorkPayInfo> payInfoso) {

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
		return workDealInfoDao.find("from WorkDealInfo where work_company_id=?  order by create_date", companyId);
	}

	@Transactional(readOnly = false)
	public void save(WorkDealInfo workDealInfo) {
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

	public List<WorkDealInfo> getDealInfo(Office office, Long agentIds, Date startTime, Date endTime) {
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(dc);
	}

	public List<Object[]> getSum4Agent(Long officeId, Long agentId, Date startTime, Date endTime) {
		// 这个SQL 注入风险基本为0 因为要拼接的参数是Long或者integer，没有String进行拼接
		String sql = "SELECT D.APP_ID, D.PRODUCT_ID,D.DEAL_INFO_TYPE , COUNT (DISTINCT(D .ID)) FROM  WORK_DEAL_INFO D, WORK_CERT_INFO c,WORK_PAY_INFO P ,SYS_USER U "
				+ " WHERE D.DEAL_INFO_TYPE IS NOT NULL AND D .CERT_ID = c.ID AND D .PAY_ID = P .ID  AND D.CREATE_BY = U.ID AND P.MONEY >0"; // 此版本不统计退费
		sql += officeId == null ? "" : " and U.OFFICE_ID = " + officeId;
		sql += agentId == null ? "" : " and d.COMMERCIAL_AGENT_ID =" + agentId;
		sql += startTime == null ? ""
				: " AND c.CREATE_DATE >= TO_DATE('" + DateUtils.formatDate(startTime, "yyyyMMdd") + "', 'yyyymmdd')";
		sql += endTime == null ? ""
				: " AND c.CREATE_DATE < TO_DATE ('" + DateUtils.formatDate(endTime, "yyyyMMdd") + "', 'yyyymmdd')";
		sql += " group by D.APP_ID, D.PRODUCT_ID, D.DEAL_INFO_TYPE";
		log.debug(sql);
		List<Object[]> resList = new ArrayList<Object[]>();
		resList = workDealInfoDao.findBySql(sql);
		// log.debug(resList.get(0));
		// log.debug(((Object[]) resList.get(0))[0] +":" +((Object[])
		// resList.get(0))[1]);

		return resList;
	}

	public List<Object[]> getSumType14Agent(Long officeId, Long agentId, Date startTime, Date endTime) {
		// 这个SQL 注入风险基本为0 因为要拼接的参数是Long或者integer，没有String进行拼接
		String sql = "SELECT D.APP_ID, D.PRODUCT_ID, D.DEAL_INFO_TYPE1 , COUNT (DISTINCT(D .ID)) FROM  WORK_DEAL_INFO D, WORK_CERT_INFO c,WORK_PAY_INFO P ,SYS_USER U "
				+ " WHERE D.DEAL_INFO_TYPE1 IS NOT NULL AND D .CERT_ID = c.ID AND D .PAY_ID = P .ID  AND D.CREATE_BY = U.ID AND P.MONEY >0"; // 此版本不统计退费
		sql += officeId == null ? "" : " and U.OFFICE_ID = " + officeId;
		sql += agentId == null ? "" : " and d.COMMERCIAL_AGENT_ID =" + agentId;
		sql += startTime == null ? ""
				: " AND c.CREATE_DATE >= TO_DATE('" + DateUtils.formatDate(startTime, "yyyyMMdd") + "', 'yyyymmdd')";
		sql += endTime == null ? ""
				: " AND c.CREATE_DATE < TO_DATE ('" + DateUtils.formatDate(endTime, "yyyyMMdd") + "', 'yyyymmdd')";
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
	public List<WorkDealInfo> getSumInfo(Long appId, Integer workDealInfoType, Long productId, Integer year,
			Date startDate, Date endDate, Long officeId, List<WorkPayInfo> workPayInfos) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("configProduct", "configProduct");
		dc.createAlias("createBy", "createBy");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", workDealInfoType),
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
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE),
				Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED)));
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
	public List<Object[]> getSumCount(Long appId, Integer workDealInfoType, Long productId, Integer year,
			Date startDate, Date endDate, Long officeId, Long agentId, boolean chargeMethodPos,
			boolean chargeMethodMoney, boolean chargeMethodBank, boolean chargeMethodGov,
			boolean chargeMethodContract) {
		// 这个SQL 注入风险基本为0 因为要拼接的参数是Long或者integer，没有String进行拼接
		String sql = "SELECT COUNT (DISTINCT(D .ID)),nvl(sum(p.work_payed_money),0) FROM  WORK_DEAL_INFO D, WORK_CERT_INFO c,WORK_PAY_INFO P ,SYS_USER U "
				+ " WHERE D .CERT_ID = c.ID AND D .PAY_ID = P .ID  AND D.CREATE_BY = U.ID AND P.MONEY >0"; // 此版本不统计退费
		sql += appId == null ? "" : " and d.app_id= " + appId;
		sql += productId == null ? "" : " and d.PRODUCT_ID = " + productId;
		sql += officeId == null ? "" : " and U.OFFICE_ID = " + officeId;
		sql += agentId == null ? "" : " and d.AGENT_ID =" + agentId;
		sql += " and (d.DEAL_INFO_TYPE =" + workDealInfoType + " or d.DEAL_INFO_TYPE1 =" + workDealInfoType + ")";
		sql += year == null ? "" : " and d.YEAR = " + year;
		sql += startDate == null ? ""
				: " AND c.CREATE_DATE > TO_DATE('" + DateUtils.formatDate(startDate, "yyyyMMdd") + "', 'yyyymmdd')";
		sql += endDate == null ? ""
				: " AND c.CREATE_DATE < TO_DATE ('" + DateUtils.formatDate(endDate, "yyyyMMdd") + "', 'yyyymmdd')";

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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
				Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));

		dc.add(Restrictions.eq("workCompany.organizationNumber", organizationNumber));
		dc.addOrder(Order.asc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {

			for (int i = 0; i < dealInfos.size(); i++) {
				if (dealInfos.get(i).getDealInfoStatus().equals(WorkDealInfoStatus.STATUS_CERT_REVOKE)) {
					return dealInfos.get(i).getCertSort();
				}
			}
			return dealInfos.get(dealInfos.size() - 1).getCertSort() + 1;
		}
	}

	public Integer getNumByOrganizationNumberCompanyName(String organizationNumber, String companyName) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
				Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		if (organizationNumber != null && !organizationNumber.equals("")) {
			dc.add(Restrictions.eq("workCompany.organizationNumber", organizationNumber));
		} else {
			dc.add(Restrictions.eq("workCompany.companyName", companyName));
		}
		dc.addOrder(Order.asc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {

			for (int i = 0; i < dealInfos.size(); i++) {
				if (dealInfos.get(i).getDealInfoStatus().equals(WorkDealInfoStatus.STATUS_CERT_REVOKE)) {
					return dealInfos.get(i).getCertSort();
				}
			}
			return dealInfos.get(dealInfos.size() - 1).getCertSort() + 1;
		}
	}

	public Integer getNumByNameCard(String name, String card) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workUser", "workUser");
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
				Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
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
				if (dealInfos.get(i).getDealInfoStatus().equals(WorkDealInfoStatus.STATUS_CERT_REVOKE)) {
					return dealInfos.get(i).getCertSort();
				}
			}
			return dealInfos.get(dealInfos.size() - 1).getCertSort() + 1;
		}
	}

	public List<WorkDealInfo> findSvn(String svn) {
		return workDealInfoDao.findSvn(svn);
	}

	/**
	 * 生成业务编号 serviceType是区分客户端办理和柜台办理 0是柜台，1是客户端
	 * 
	 * @param serviceType
	 * @return
	 */
	public String getSVN(Integer serviceType) {
		Date date = new Date();
		String svn = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		if (serviceType == 0) {
			try {
				User user = UserUtils.getUser();
				String timeSvn = "C-" + user.getOffice().getName() + "-" + sdf.format(date).substring(2);
				List<WorkDealInfo> list = findSvn("%" + timeSvn + "%");
				int num = 1;
				if (list.size() > 0) {
					String oldSvn = list.get(0).getSvn();
					num = Integer.parseInt(oldSvn.substring(oldSvn.length() - 3)) + 1;
				}
				String numStr = "";
				if (num > 0 && num < 10) {
					numStr = "000" + num;
				} else if (num > 9 && num < 100) {
					numStr = "00" + num;
				} else if (num > 99 && num < 1000) {
					numStr = "0" + num;
				} else {
					numStr = "" + num;
				}
				svn = timeSvn + numStr;
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			String searchText = "O-客户端-" + sdf.format(new Date());
			svn = outClientSn(searchText);
		}
		return svn;
	}

	public String getSVN(String officeName) {
		Date date = new Date();
		String svn = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		try {
			String timeSvn = "C-" + officeName + "-" + sdf.format(date).substring(2);
			List<WorkDealInfo> list = findSvn("%" + timeSvn + "%");
			int num = 1;
			if (list.size() > 0) {
				String oldSvn = list.get(0).getSvn();
				num = Integer.parseInt(oldSvn.substring(oldSvn.length() - 3)) + 1;
			}
			String numStr = "";
			if (num > 0 && num < 10) {
				numStr = "000" + num;
			} else if (num > 9 && num < 100) {
				numStr = "00" + num;
			} else if (num > 99 && num < 1000) {
				numStr = "0" + num;
			} else {
				numStr = "" + num;
			}
			svn = timeSvn + numStr;
		} catch (Exception e) {
			// TODO: handle exception
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("obtainedDate"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 1;
		} else {
			return dealInfos.get(0).getCertSort() + 1;
		}
	}

	public boolean findByOffice(List<Office> offices) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.in("createBy.office", offices));
		List<WorkCertInfo> list = workCertInfoDao.find(dc);
		if (list.size() > 0) {
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
			int contractSettleTime = getSettleTimes(dealInfo.getConfigCommercialAgent().getAgentContractStart(),
					dealInfo.getConfigCommercialAgent().getAgentContractEnd());
			if (dealInfo.getWorkPayInfo().getMethodGov() || dealInfo.getWorkPayInfo().getMethodContract()) {
				dealInfo.setCanSettle(false);
				return;
			}

			if (dealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)) {// 新增一定结算
				dealInfo.setCanSettle(true);
				// year和最大次数比较取小的
				if (dealInfo.getYear() > contractSettleTime) {
					dealInfo.setPhysicalLife(contractSettleTime);
					// dealInfo.setResidualLife(0);
					dealInfo.setResidualLife(dealInfo.getYear() - contractSettleTime);
				} else {
					dealInfo.setPhysicalLife(dealInfo.getYear());
					dealInfo.setResidualLife(0);
					// dealInfo.setResidualLife(contractSettleTime -
					// dealInfo.getYear());
				}
				return;

			}
			if (dealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| dealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| dealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| dealInfo.getDealInfoType3().equals(WorkDealInfoType.TYPE_UPDATE_CERT)) {
				WorkDealInfo firstDealInfo = getFirstCertDealInfo(dealInfo, dealInfo);// 获取第一张被结算的证书业务
				if (firstDealInfo.getConfigCommercialAgent().getId()
						.equals(dealInfo.getConfigCommercialAgent().getId())) {// 判断是否是同一代理商
					WorkCertInfo firstCertInfo = firstDealInfo.getWorkCertInfo();
					// 已结算次数
					int certSettleTime = getCertSettleTimes(firstCertInfo.getSignDate(), new Date());
					if (contractSettleTime > certSettleTime) {
						// contractSettleTime - certSettleTime跟year比较取小的
						dealInfo.setSettledLife(certSettleTime);
						if (dealInfo.getYear() > (contractSettleTime - certSettleTime)) {
							dealInfo.setPhysicalLife(Math.abs(contractSettleTime - certSettleTime));
							// dealInfo.setResidualLife(0);
							dealInfo.setResidualLife(dealInfo.getYear() - contractSettleTime - certSettleTime);
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
	private WorkDealInfo getFirstCertDealInfo(WorkDealInfo dealInfo, WorkDealInfo curFirstSettleDealInfo) {
		if (dealInfo.getPrevId() == null) {// 不需要判断是否是政府统一采购or合同采购，如果没有前一笔业务?通常是递归中找到了新增业务，前一个嵌套中已经确定这笔新增付款方式是结算的
			return curFirstSettleDealInfo;
		}
		WorkDealInfo prevDealInfo = workDealInfoDao.findOne(dealInfo.getPrevId());
		if (prevDealInfo.getWorkPayInfo().getMethodGov() || prevDealInfo.getWorkPayInfo().getMethodContract()) {// 如果前一笔业务是是政府统一采购，那结算从这一笔开始
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
		if (prevDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)) {// 新增一定是第一张结算证书,上面已规避付款方式问题，是否和第一个判断重复?
			if (!prevDealInfo.getCanSettle()) {// 退费导致新增补结算
				return curFirstSettleDealInfo;
			}
			return prevDealInfo;
		}
		// 前一笔业务是结算的更新业务,更新前一笔业务可能会有 补办、变更等等...这里要记录当前可结算的业务
		// 有可能新增业务是政府统一采购、合同采购，所以第一笔结算业务可能是新增
		if (prevDealInfo.getCanSettle() && (prevDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
				|| prevDealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_UPDATE_CERT))) {
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

	public int getCertPublishCount(Date date, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")
				));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));
		List<WorkDealInfo> dealinfos = workDealInfoDao.find(dc);
		int createCount = 0;
		for (int i = 0; i < dealinfos.size(); i++) {
			WorkDealInfo dealInfo = dealinfos.get(i);
			if (dealInfo.getDealInfoStatus().equals("9")) {
				if (dealInfo.getDealInfoType()!=null&&dealInfo.getDealInfoType().equals(1)) {
					createCount++;
				}else if(dealInfo.getDealInfoType2()!=null&&dealInfo.getDealInfoType2().equals(4)){
					createCount++;
				}else if(dealInfo.getDealInfoType().equals(1) && dealInfo.getDealInfoType2().equals(4)){
					createCount++;
				}
			}else{
				createCount++;
			}
		}
		dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE));
		dc.add(Restrictions.ge("obtainedDate", date));
		int dealInfoType = WorkDealInfoType.TYPE_RETURN_WORK;
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType), Restrictions.eq("dealInfoType2", dealInfoType),
				Restrictions.eq("dealInfoType3", dealInfoType),
				Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_PAY_REPLACED))); // 类型是退费或者变更，都要剔除掉
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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

	public int getKeyPublishTimes(Date date, Long officeId, Long appId, Date endDate) {
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
	public int getCertAppYearInfo(Date date, Long officeId, Integer year, Long appId, Integer dealInfoType) {
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

	public int getCertAppYearInfoCount(Date date, Long officeId, Integer year, Long appId, Integer dealInfoType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));
		dc.add(Restrictions.or(Restrictions.ne("isSJQY", 1),
				Restrictions.isNull("isSJQY")
				));
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
				Restrictions.eq("dealInfoType1", dealInfoType), Restrictions.eq("dealInfoType2", dealInfoType),
				Restrictions.eq("dealInfoType3", dealInfoType)));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoCountOneDeal(Date date, Long officeId, Integer year, Long appId,
			Integer dealInfoType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));
		List<String> statusIntegers = new ArrayList<String>();
		if (dealInfoType.equals(1)) {
			dc.add(Restrictions.eq("dealInfoType", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType1"));
			dc.add(Restrictions.isNull("dealInfoType2"));
			dc.add(Restrictions.isNull("dealInfoType3"));
			statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		} else if (dealInfoType.equals(4)) {
			dc.add(Restrictions.eq("dealInfoType2", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType"));
			dc.add(Restrictions.isNull("dealInfoType1"));
			dc.add(Restrictions.isNull("dealInfoType3"));
			statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		} else if (dealInfoType.equals(2) || dealInfoType.equals(3)) {
			dc.add(Restrictions.eq("dealInfoType1", dealInfoType));
			dc.add(Restrictions.isNull("dealInfoType"));
			dc.add(Restrictions.isNull("dealInfoType2"));
			dc.add(Restrictions.isNull("dealInfoType3"));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}

		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoUpdateChangeNum(Date date, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeUpdate, Integer dealInfoTypeChange) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));

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
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_WAIT);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfoUpdateLostReplaceNum(Date date, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeUpdate, Integer dealInfoTypeLost) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));

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

	public int getCertAppYearInfoChangeLostReplaceNum(Date date, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeChange, Integer dealInfoTypeLostReplace) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));

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

	public int getCertAppYearInfoChangeLostReplaceUpdateNum(Date date, Long officeId, Integer year, Long appId,
			Integer dealInfoTypeChange, Integer dealInfoTypeLostReplace, Integer dealInfoTypeUpdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));
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

	public int getCertAppYearInfo(Date date, Long officeId, Integer year, Long appId, Integer dealInfoType,
			Date endDate) {
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
	public int getCertAppYearInfo(Date date, Long officeId, Integer year, Long appId, Integer dealInfoType,
			String productType, Integer payType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
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
			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
					Restrictions.eq("dealInfoType1", dealInfoType)));
		} else {
			List<Integer> dealInfoTypes = new ArrayList<Integer>();
			dealInfoTypes.add(WorkDealInfoType.TYPE_ADD_CERT);
			dealInfoTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT);
			dc.add(Restrictions.or(Restrictions.in("dealInfoType", dealInfoTypes),
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
				dc.add(Restrictions.and(Restrictions.ne("workPayInfo.methodGov", true),
						Restrictions.ne("workPayInfo.methodContract", true)));
			}
		}
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfo(Date date, Long officeId, Integer year, Long appId, Integer dealInfoType,
			List<String> productType, Integer payType) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
		dc.add(Restrictions.ge("obtainedDate", date));
		if (appId != null) {
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		if (year != 0) {
			dc.add(Restrictions.eq("year", year));
		}
		dc.add(Restrictions.in("configProduct.productName", productType));
		if (dealInfoType != null) {
			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
					Restrictions.eq("dealInfoType1", dealInfoType)));
		} else {
			List<Integer> dealInfoTypes = new ArrayList<Integer>();
			dealInfoTypes.add(WorkDealInfoType.TYPE_ADD_CERT);
			dealInfoTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT);
			dc.add(Restrictions.or(Restrictions.in("dealInfoType", dealInfoTypes),
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
				dc.add(Restrictions.and(Restrictions.ne("workPayInfo.methodGov", true),
						Restrictions.ne("workPayInfo.methodContract", true)));
			}
		}
		return (int) workDealInfoDao.count(dc);
	}

	public int getCertAppYearInfo(Date date, Long officeId, Integer year, Long appId, Integer dealInfoType,
			List<String> productType, Integer payType, Date endDate) {
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
			dc.add(Restrictions.or(Restrictions.eq("dealInfoType", dealInfoType),
					Restrictions.eq("dealInfoType1", dealInfoType)));
		} else {
			List<Integer> dealInfoTypes = new ArrayList<Integer>();
			dealInfoTypes.add(WorkDealInfoType.TYPE_ADD_CERT);
			dealInfoTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT);
			dc.add(Restrictions.or(Restrictions.in("dealInfoType", dealInfoTypes),
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
				dc.add(Restrictions.and(Restrictions.ne("workPayInfo.methodGov", true),
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.or(Restrictions.eq("dealInfoStatus",
				WorkDealInfoStatus.STATUS_CERT_OBTAINED),
				Restrictions.eq("dealInfoStatus",WorkDealInfoStatus.STATUS_CERT_WAIT)
				));
		dc.add(Restrictions.gt("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.eq("workPayInfo.userReceipt", true));

		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalReceipt = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
				if(dealInfo.getIsSJQY()==null || !dealInfo.getIsSJQY().equals(1)){
					if (dealInfo.getDealInfoStatus().equals("9")) {
						if (dealInfo.getDealInfoType()!=null&&dealInfo.getDealInfoType().equals(1)) {
							totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();
						}else if(dealInfo.getDealInfoType2()!=null&&dealInfo.getDealInfoType2().equals(4)){
							totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();
						}else if(dealInfo.getDealInfoType().equals(1) && dealInfo.getDealInfoType2().equals(4)){
							totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();
						}
					}else{
						totalReceipt += dealInfo.getWorkPayInfo().getReceiptAmount();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalReceipt;
	}

	public double getWorkPayReceipt(Date date, Long officeId, Long appId, Date endDate) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
	 * 获取某个应用下当前有效的移动设备数量
	 * 
	 * @return
	 */
	public Integer getValidDeviceTotal(Long appId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
		//dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.eq("status", 0));
//		dc.add(Restrictions.eq("workPayInfo.delFlag", WorkPayInfo.DEL_FLAG_NORMAL));
		//dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		return workDealInfoDao.find(dc);
	}

	public void exportExcelData(HttpServletRequest request, HttpServletResponse response) {
		String[] title = { "付款单位名称", "付款金额/元", "联系人", "联系方式", "付款时间", "付款方式", "区域", "网点", "记录人员" };
		String sheetName = "业务款项统计.xlsx";
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Type", "application/force-download");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(sheetName.getBytes("utf-8"), "ISO8859-1"));
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

	public XSSFWorkbook createDS(XSSFWorkbook wb, Collection<WorkDealInfo> coll) throws Exception {
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
						cell.setCellValue(workDealInfo.getWorkCompany().getCompanyName());
						break;
					case 1:
						cell.setCellValue(workDealInfo.getWorkPayInfo().getWorkPayedMoney());
						break;
					case 2:
						cell.setCellValue(workDealInfo.getWorkUser().getContactName());
						break;
					case 3:
						cell.setCellValue(workDealInfo.getWorkUser().getContactPhone());
						break;
					case 4:
						cell.setCellValue(date.format(workDealInfo.getWorkPayInfo().getCreateDate()));
						break;
					case 5:
						cell.setCellValue(paymentMethod(
								new boolean[] { workDealInfo.getWorkPayInfo().getMethodPos(),
										workDealInfo.getWorkPayInfo().getMethodMoney(),
										workDealInfo.getWorkPayInfo().getMethodBank(),
										workDealInfo.getWorkPayInfo().getMethodAlipay(),
										workDealInfo.getWorkPayInfo().getMethodContract(),
										workDealInfo.getWorkPayInfo().getMethodGov() },
								workDealInfo.getWorkPayInfo().getRelationMethod() != null
										? workDealInfo.getWorkPayInfo().getRelationMethod() : 0));
						break;
					case 6:
						cell.setCellValue(workDealInfo.getCreateBy().getOffice().getAreaName());
						break;
					case 7:
						cell.setCellValue(workDealInfo.getCreateBy().getOffice().getName());
						break;
					case 8:
						cell.setCellValue(workDealInfo.getWorkPayInfo().getCreateBy().getName());
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
	public List<WorkDealInfo> getWorkInfoByApp(Date date, ConfigApp configApp, String type) {
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
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
		}
		// 一个月内到期的证书
		if (type.equals("A1M")) {
			rightNow.add(Calendar.MONTH, 1);// 日期加1个月
			dateEnd = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", date, dateEnd));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
		}
		// 一个月过期期的证书
		if (type.equals("B1M")) {
			rightNow.add(Calendar.MONTH, -1);// 日期减1个月
			startDate = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, date));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
		}
		// 三个月过期期的证书
		if (type.equals("B3M")) {
			rightNow.add(Calendar.MONTH, -3);// 日期减3个月
			startDate = rightNow.getTime();
			dcCert.add(Restrictions.between("notafter", startDate, date));
			// 过滤更新过的重复证书
			dcCert.add(Restrictions.isNull("renewalNextId"));
			// 在这时间内没有被吊销
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
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
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
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
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
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
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
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
			dcCert.add(Restrictions.or(Restrictions.isNull("revokeDate"), Restrictions.ge("revokeDate", dateEnd)));
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

	public double getWorkPayMoneyCount(Date yesterDay, Date countDate, Long officeId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(Restrictions.eq("createBy.office.id", officeId));
		List<String> statusIntegers = new ArrayList<String>();
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		statusIntegers.add(WorkDealInfoStatus.STATUS_CERT_REVOKE);
		dc.add(Restrictions.in("dealInfoStatus", statusIntegers));
		dc.add(Restrictions.ge("updateDate", countDate));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, 1);
		countDate = calendar.getTime();
		dc.add(Restrictions.lt("updateDate", countDate));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		double totalMoney = 0d;
		for (WorkDealInfo dealInfo : dealInfos) {
			try {// 避免null的情况
					// totalMoney +=
					// dealInfo.getWorkPayInfo().getWorkPayedMoney();
				if(dealInfo.getIsSJQY()==null || !dealInfo.getIsSJQY().equals(1)){
					if (dealInfo.getDealInfoStatus().equals("9")) {
						if (dealInfo.getDealInfoType()!=null&&dealInfo.getDealInfoType().equals(1)) {
							totalMoney += dealInfo.getWorkPayInfo().getWorkTotalMoney();
						}else if(dealInfo.getDealInfoType2()!=null&&dealInfo.getDealInfoType2().equals(4)){
							totalMoney += dealInfo.getWorkPayInfo().getWorkTotalMoney();
						}else if(dealInfo.getDealInfoType().equals(1) && dealInfo.getDealInfoType2().equals(4)){
							totalMoney += dealInfo.getWorkPayInfo().getWorkTotalMoney();
						}
					}else{
						totalMoney += dealInfo.getWorkPayInfo().getWorkTotalMoney();
						
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return totalMoney;
	}

	public int getKeyPublishTimesCount(Date date, Long officeId, Long appId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); //
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("office.id", officeId));
		// // 新增、置换时才有key
		 dc.add(Restrictions.or(Restrictions.eq("dealInfoType",
		 WorkDealInfoType.TYPE_ADD_CERT), Restrictions.eq(
		 "dealInfoType", WorkDealInfoType.TYPE_DAMAGED_REPLACED),
		 Restrictions.eq("dealInfoType1",
		 WorkDealInfoType.TYPE_DAMAGED_REPLACED)));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.ge("updateDate", date));
		dc.add(Restrictions.lt("updateDate", calendar.getTime()));

		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		List<String> keys = new ArrayList<String>();
		for (WorkDealInfo workDealInfo : dealInfos) {
			if(workDealInfo.getIsSJQY()==null || !workDealInfo.getIsSJQY().equals(1)){
				if (!keys.contains(workDealInfo.getKeySn())) {
					keys.add(workDealInfo.getKeySn());
				}
			}
		}
		return keys.size();
	}

	// 根据单位名称，经办人查询出业务办理列表
	public List<WorkDealInfo> findByCompanyAttn(String companyString, String attn) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();

		dc.createAlias("workUser", "workUser");
		if (attn != null && !attn.equals("")) {
			dc.add(Restrictions.like("workUser.contactName", "%" + EscapeUtil.escapeLike(attn) + "%"));
		}
		dc.createAlias("workCompany", "workCompany");
		if (companyString != null && !companyString.equals("")) {
			dc.add(Restrictions.like("workCompany.companyName", "%" + EscapeUtil.escapeLike(companyString) + "%"));
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
	public List<WorkDealInfo> findByAgentIdProductId(Long agentId, Long productId) {
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
	public Integer getCertSortByOrganizationNumber(String organizationNumber, Integer productTdId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("workCompany.organizationNumber", organizationNumber));
		dc.add(Restrictions.eq("configProduct.productName", productTdId.toString()));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
	public Integer getCertSortByCompanyName(String companyName, Integer productTdId) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("configProduct", "configProduct");
		dc.add(Restrictions.eq("workCompany.companyName", companyName));
		dc.add(Restrictions.eq("configProduct.productName", productTdId.toString()));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
		dc.add(Restrictions.or(Restrictions.eq("configProduct.productName", "2"),
				Restrictions.eq("configProduct.productName", "6")));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
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
		dc.add(Restrictions.or(Restrictions.eq("configProduct.productName", "2"),
				Restrictions.eq("configProduct.productName", "6")));
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.addOrder(Order.desc("certSort"));
		List<WorkDealInfo> dealInfos = workDealInfoDao.find(dc);
		if (dealInfos.size() == 0) {
			return 0;
		} else {
			return dealInfos.get(0).getCertSort();
		}
	}

	@Transactional(readOnly = false)
	public JSONObject saveExcelDate(MultipartFile file, String ifExcel) {
		StringBuffer ifErr = new StringBuffer();
		/*
		 * 创建临时路径
		 */
		StringBuffer tempPath = new StringBuffer("F:/temp/");
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

		try {
			Integer a = 0;
			for (int i = 0; i < rows; i++) {
				if (i == 0 || i == 1) {
					continue;
				}
				
				ConfigRaAccountExtendInfo configRaAccountExtendInfo = null;
				ConfigChargeAgentBoundConfigProduct bound = null;
				row = sheetAt0.getRow(i);
				if (row == null) {
					ifErr.append("第" + i + "行为空，导入失败，请重新导入数据！<br>");
					a = 1;
					return ifErr(-1, ifErr.toString());
				} else {
					if (row.getCell(0) != null && !row.getCell(0).toString().replace(" ", "").equals("")) {

						String appName = row.getCell(0).toString().replace(" ", "");

						ConfigApp app = configAppService.findByAppname(appName);
						if (app != null) {
							if (row.getCell(1) != null && !row.getCell(1).toString().replace(" ", "").equals("")) {
								String productName = row.getCell(1).toString().replace(" ", "");
								ConfigProduct configProduct = new ConfigProduct();
								configProduct.setConfigApp(app);
								List<ConfigProduct> list = configProductService.findByAppIdAndRa(configProduct);
								Integer productLaber = ProductType.productTypeIdMap.get(productName);
								if (productLaber == null) {
									a = 1;
									ifErr.append("第" + (i + 1) + "行第2列产品名称不存在！<br>");
									continue;
								} else {
									configProduct = null;
									int iconfig = 0;
									ConfigProduct configProductForAgent = new ConfigProduct();
									for (int j = 0; j < list.size(); j++) {
										if (list.get(j).getProductName().equals(productLaber.toString())) {
											configProduct = list.get(j);
											iconfig++;
										}
										String supCom = row.getCell(2).toString().replace(" ", "");
										Integer proLabel = 5;
										if (supCom.equals("通用")) {
											proLabel = 0;
										}else if(supCom.equals("专用")){
											proLabel = 1;
										}
										if (list.get(j).getProductName().equals(productLaber.toString())&&list.get(j).getProductLabel().equals(proLabel)) {
											configProductForAgent = list.get(j);
										}
										
										
										
									}
									if (configProduct == null) {
										a = 1;
										ifErr.append("第" + (i + 1) + "行第2列产品名称没有被产品名称配置！<br>");
										continue;
									} else {
										if (row.getCell(2) != null && !row.getCell(2).toString().replace(" ", "").equals("")) {
											String supCom = row.getCell(2).toString().replace(" ", "");
											Boolean isHave = false ;
											if (iconfig==2) {
												if (supCom.equals("通用")||supCom.equals("专用")) {
													isHave=true;
												}
											}else if(iconfig==1){
												if (supCom.equals("专用")) {
													isHave=true;
												}
											}
											if (isHave) {
												if (row.getCell(3) != null && !row.getCell(3).toString().replace(" ", "").equals("")) {
													String agentName = row.getCell(3).toString().replace(" ", "");
													
													bound = configChargeAgentBoundConfigProductService
															.findByProIdAgentName(configProductForAgent.getId(),agentName);	
													if (bound!=null) {
														ConfigChargeAgent agent = bound.getAgent();
														Boolean checkStyle = true;
														if (agent.getTempStyle().equals("2")||agent.getTempStyle().equals("3")) {
															if (agent.getSurplusNum()<rows-1) {
																checkStyle = false;
															}
														}
														if (checkStyle) {
															if (row.getCell(4) != null && !row.getCell(4).toString().replace(" ", "").equals("")) {
																String[] years = configChargeAgentDetailService
																		.getChargeAgentYears(agent.getId(),
																				0);
																configRaAccountExtendInfo = configRaAccountExtendInfoService
																		.get(configProductForAgent.getRaAccountExtedId());
																String year = row.getCell(4).toString().replace(" ", "");
																Boolean checkYear = false;
																for (int j = 0; j < years.length; j++) {
																	if (years[j].equals(year)) {
																		checkYear = true;
																		break;
																	}
																}
																if (!checkYear) {
																	a = 1;
																	ifErr.append("第" + (i + 1) + "行计费策略模板没有配置该新增年份！<br>");
																	continue;
																}
															}else{
																a = 1;
																ifErr.append("第" + (i + 1) + "行第5列申请年限不能为空！<br>");
																continue;
															}
														}else{
															a = 1;
															ifErr.append("本次导入的新增数量小于模板剩余新增数量，请检查并配置！<br>");
															continue;
															
														}
													}else{
														a = 1;
														ifErr.append("第" + (i + 1) + "行产品没有绑定该计费策略模板！<br>");
														continue;
													}
													
												}else{
													a = 1;
													ifErr.append("第" + (i + 1) + "行第4列计费策略模板名称不能为空！<br>");
													continue;
												}
											}else{
												a = 1;
												ifErr.append("第" + (i + 1) + "行第3列应用标识不存在或不支持通用类型！<br>");
												continue;
											}
										}else{
											a = 1;
											ifErr.append("第" + (i + 1) + "行第3列应用标识不能为空！<br>");
											continue;
										}
									}
								}
							} else {
								a = 1;
								ifErr.append("第" + (i + 1) + "行第2列产品名称不能为空！<br>");
								continue;
							}
						} else {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第1列应用名称不存在！<br>");
							continue;
						}
					} else {
						a = 1;
						ifErr.append("第" + (i + 1) + "行第1列应用名称不能为空！<br>");
						continue;
					}
				}
				//fields.add("companyName");//单位名称
				//fields.add("contactName");//证书持有人名称
				//fields.add("pName");//经办人名称
				//fields.add("organizationNumber");//组织机构代码
				//fields.add("conCertNumber");//证书持有人身份证号
				//fields.add("contacEmail");//证书持有人邮箱
				//fields.add("pIDCard");//经办人身份证号
				//fields.add("contactTel");//业务系统UID
				//fields.add("comCertficateNumber");//证件号
				Set<String> extentInfos  =   RaAccountUtil.getExtendInfos(configRaAccountExtendInfo);
				for (String info : extentInfos) {
					if(info.equals("companyName")){
						if (row.getCell(5) == null || row.getCell(5).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第6列单位名称不能为空！<br>");
						}
					}else if(info.equals("contactName")){
						if (row.getCell(21) == null || row.getCell(21).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第21列证书持有人名称不能为空！<br>");
						}
					}else if(info.equals("pName")){
						if (row.getCell(28) == null || row.getCell(28).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第29列经办人姓名不能为空！<br>");
						}
					}else if(info.equals("organizationNumber")){
						if (row.getCell(7) == null || row.getCell(7).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第8列组织机构代码不能为空！<br>");
						}
					}else if(info.equals("conCertNumber")){
						if (row.getCell(23) == null || row.getCell(23).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第24列证书持有人身份证号不能为空！<br>");
						}
					}else if(info.equals("contacEmail")){
						if (row.getCell(24) == null || row.getCell(24).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第25列证书持有人邮箱不能为空！<br>");
						}
					}else if(info.equals("pIDCard")){
						if (row.getCell(29) == null || row.getCell(29).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第30列经办人身份证号不能为空！<br>");
						}
					}else if(info.equals("contactTel")){
						if (row.getCell(24) == null || row.getCell(24).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第25列证书持有人邮箱不能为空！<br>");
						}
					}else if(info.equals("comCertficateNumber")){
						if (row.getCell(11) == null || row.getCell(11).toString().replace(" ", "").equals("")) {
							a = 1;
							ifErr.append("第" + (i + 1) + "行第12列证件号不能为空！<br>");
						}
					}
				}
				if (row.getCell(31)!= null || !row.getCell(31).toString().replace(" ", "").equals("")) {
					String payType = row.getCell(31).toString().replace(" ", "");
					if (payType.equals("现金")||payType.equals("POS付款")||payType.equals("财务支付")) {
						if (payType.equals("财务支付")) {
							if (row.getCell(32)!= null || !row.getCell(32).toString().replace(" ", "").equals("")) {
								String payCompanyName = row.getCell(32).toString().replace(" ", "");
								 List<FinancePaymentInfo> payMentInfo =  financePaymentInfoService.findByCompanyName(payCompanyName);
								if (payMentInfo.size()<1) {
									a = 1;
									ifErr.append("第" + (i + 1) + "行第33列付款单位名称的支付信息不存在！<br>");
								}
							}else{
								a = 1;
								ifErr.append("第" + (i + 1) + "行第33列付款单位名称不能为空！<br>");
							}
						}
					}else{
						a = 1;
						ifErr.append("第" + (i + 1) + "行第32列支付类型不存在！<br>");
						
					}
				}else{
					a = 1;
					ifErr.append("第" + (i + 1) + "行第32列支付类型不能为空！<br>");
				}
				
				if (a==1) {
					return ifErr(-1, ifErr.toString());
				}
				
				String appName = row.getCell(0).toString().replace(" ", "");
				ConfigApp configApp = configAppService.findByAppname(appName);
				String companyName = row.getCell(5).toString().replace(" ", "");
				String organizationNumber = "";
				if(row.getCell(7)!=null&&!row.getCell(7).toString().replace(" ", "").equals("")){
					organizationNumber = row.getCell(7).toString().replace(" ", "");
				}
				
				WorkCompany workCompany = workCompanyService.finByNameAndNumber(
						companyName, organizationNumber);
				
				ConfigProduct configProduct = bound.getProduct();
				if (companyName!=null&&!companyName.equals("")) {
					workCompany.setCompanyName(companyName);
				}
				String companyType = "";
				if(row.getCell(6)!=null&&!row.getCell(6).toString().replace(" ", "").equals("")){
					companyType = row.getCell(6).toString().replace(" ", "");
				}
				if(companyType!=null&&!companyType.equals("")){
					if (companyType.equals("企业")) {
						workCompany.setCompanyType("1");
					}else if(companyType.equals("事业单位")){
						workCompany.setCompanyType("2");
					}else if(companyType.equals("政府机关")){
						workCompany.setCompanyType("3");
					}else if(companyType.equals("社会团体")){
						workCompany.setCompanyType("4");
					}else{
						workCompany.setCompanyType("5");
					}
				}else{
					workCompany.setCompanyType("1");
				}
				String comCertificateType = "";
				if(row.getCell(10)!=null&&!row.getCell(10).toString().replace(" ", "").equals("")){
					comCertificateType = row.getCell(10).toString().replace(" ", "");
				}
				if(comCertificateType!=null&&!comCertificateType.equals("")){
					if (comCertificateType.equals("营业执照")) {
						workCompany.setComCertificateType("0");
					}else if(comCertificateType.equals("事业单位法人登记证")){
						workCompany.setComCertificateType("1");
					}else if(comCertificateType.equals("社会团体登记证")){
						workCompany.setComCertificateType("2");
					}else{
						workCompany.setComCertificateType("3");
					}
				}else{
					workCompany.setComCertificateType("0");
				}
				SimpleDateFormat dnf = new SimpleDateFormat("yyyy-MM-dd");
				
				String orgExpirationTime = ""; //组织机构代码有效期：
				if(row.getCell(8)!=null&&!row.getCell(8).toString().replace(" ", "").equals("")){
					orgExpirationTime = row.getCell(8).toString().replace(" ", "");
				}
				if(orgExpirationTime!=null&&!orgExpirationTime.equals("")){
					Timestamp ts1 = new Timestamp(dnf.parse(orgExpirationTime)
							.getTime());
					workCompany.setOrgExpirationTime(ts1);
				}
				
				String comCertficateTime = ""; //单位证照有效期：
				if(row.getCell(12)!=null&&!row.getCell(12).toString().replace(" ", "").equals("")){
					comCertficateTime = row.getCell(12).toString().replace(" ", "");
				}
				if(comCertficateTime!=null&&!comCertficateTime.equals("")){
					Timestamp ts = new Timestamp(dnf.parse(comCertficateTime)
							.getTime());
					workCompany.setComCertficateTime(ts);
				}
				
				
				
				String comCertficateNumber = ""; //证件号
				if(row.getCell(11)!=null&&!row.getCell(11).toString().replace(" ", "").equals("")){
					comCertficateNumber = row.getCell(11).toString().replace(" ", "");
				}
				if(comCertficateNumber!=null&&!comCertficateNumber.equals("")){
					workCompany.setComCertficateNumber(comCertficateNumber);
				}
				
				String selectLv = ""; //服务级别
				if(row.getCell(9)!=null&&!row.getCell(9).toString().replace(" ", "").equals("")){
					selectLv = row.getCell(9).toString().replace(" ", "");
				}
				if(selectLv!=null&&!selectLv.equals("")){
					if (selectLv.equals("普通客户")) {
						workCompany.setSelectLv("1");
					}else{
						workCompany.setSelectLv("0");
					}
				}else{
					workCompany.setSelectLv("0");
				}
				if(organizationNumber!=null&&!organizationNumber.equals("")){
					workCompany.setOrganizationNumber(organizationNumber);
				}
				
				String s_province = ""; //省
				if(row.getCell(14)!=null&&!row.getCell(14).toString().replace(" ", "").equals("")){
					s_province = row.getCell(14).toString().replace(" ", "");
				}
				String s_city = ""; //市
				if(row.getCell(15)!=null&&!row.getCell(15).toString().replace(" ", "").equals("")){
					s_city = row.getCell(15).toString().replace(" ", "");
				}
				String s_county = ""; //县
				if(row.getCell(16)!=null&&!row.getCell(16).toString().replace(" ", "").equals("")){
					s_county = row.getCell(16).toString().replace(" ", "");
				}
				
				if(s_province!=null&&!s_province.equals("")){
					workCompany.setProvince(s_province);
				}
				if(s_city!=null&&!s_city.equals("")){
					workCompany.setCity(s_city);
				}
				if(s_county!=null&&!s_county.equals("")){
					workCompany.setDistrict(s_county);
				}
				
				String areaRemark = ""; //区域备注
				if(row.getCell(17)!=null&&!row.getCell(17).toString().replace(" ", "").equals("")){
					areaRemark = row.getCell(17).toString().replace(" ", "");
				}
				if(areaRemark!=null&&!areaRemark.equals("")){
					workCompany.setAreaRemark(areaRemark);
				}
				String legalName = ""; //法人姓名
				if(row.getCell(13)!=null&&!row.getCell(13).toString().replace(" ", "").equals("")){
					legalName = row.getCell(13).toString().replace(" ", "");
				}
				if(legalName!=null&&!legalName.equals("")){
					workCompany.setLegalName(legalName);
				}
			
				String address = ""; //街道地址：
				if(row.getCell(18)!=null&&!row.getCell(18).toString().replace(" ", "").equals("")){
					address = row.getCell(18).toString().replace(" ", "");
				}
				if(address!=null&&!address.equals("")){
					workCompany.setAddress(address);
				}
				String companyMobile = ""; //单位联系电话：
				if(row.getCell(19)!=null&&!row.getCell(19).toString().replace(" ", "").equals("")){
					companyMobile = row.getCell(19).toString().replace(" ", "");
				}
				if(companyMobile!=null&&!companyMobile.equals("")){
					workCompany.setCompanyMobile(companyMobile);
				}
				String remarks = ""; //备注信息
				if(row.getCell(20)!=null&&!row.getCell(20).toString().replace(" ", "").equals("")){
					remarks = row.getCell(20).toString().replace(" ", "");
				}
				if(remarks!=null&&!remarks.equals("")){
					workCompany.setRemarks(remarks);
				}
				
				// 保存经办人信息
				WorkUser workUser = new WorkUser();
				workUser.setStatus(1);
				
				String contactName = "";//证书持有人姓名
				if(row.getCell(21)!=null&&!row.getCell(21).toString().replace(" ", "").equals("")){
					contactName = row.getCell(21).toString().replace(" ", "");
				}
				if(contactName!=null&&!contactName.equals("")){
					workUser.setContactName(contactName);
				}

				String conCertType = "";//证书持有人证件
				if(row.getCell(22)!=null&&!row.getCell(22).toString().replace(" ", "").equals("")){
					conCertType = row.getCell(22).toString().replace(" ", "");
				}
				if(conCertType!=null&&!conCertType.equals("")){
					if (conCertType.equals("身份证")) {
						workUser.setConCertType("0");
					}else if(conCertType.equals("军官证")){
						workUser.setConCertType("1");
					}else{
						workUser.setConCertType("2");
					}
				}else{
					workUser.setConCertType("0");
				}
				
				String conCertNumber = ""; //证件号码
				if(row.getCell(23)!=null&&!row.getCell(23).toString().replace(" ", "").equals("")){
					conCertNumber = row.getCell(23).toString().replace(" ", "");
				}
				if(conCertNumber!=null&&!conCertNumber.equals("")){
					workUser.setConCertNumber(conCertNumber);
				}
				
				String contacEmail = ""; //证书持有人电子邮件
				if(row.getCell(24)!=null&&!row.getCell(24).toString().replace(" ", "").equals("")){
					contacEmail = row.getCell(24).toString().replace(" ", "");
				}
				if(contacEmail!=null&&!contacEmail.equals("")){
					workUser.setContactEmail(contacEmail);
				}
			
				String contactPhone = ""; //证书持有人手机号:
				if(row.getCell(25)!=null&&!row.getCell(25).toString().replace(" ", "").equals("")){
					contactPhone = row.getCell(25).toString().replace(" ", "");
				}
				if(contactPhone!=null&&!contactPhone.equals("")){
					workUser.setContactPhone(contactPhone);
				}
				
				String contactTel = ""; //业务系统UID:
				if(row.getCell(26)!=null&&!row.getCell(26).toString().replace(" ", "").equals("")){
					contactTel = row.getCell(26).toString().replace(" ", "");
				}
				if(contactTel!=null&&!contactTel.equals("")){
					workUser.setContactTel(contactTel);
				}
				workUser.setWorkCompany(workCompany);
				
				String contactSex = ""; //证书持有人性别:
				if(row.getCell(27)!=null&&!row.getCell(27).toString().replace(" ", "").equals("")){
					contactSex = row.getCell(27).toString().replace(" ", "");
				}
				if(contactSex!=null&&!contactSex.equals("")){
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
					workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0)
							.getConfigCommercialAgent());// 劳务关系外键
				}
				workDealInfo.setConfigApp(configApp);
				workDealInfo.setWorkUser(workUser);
				workDealInfo.setWorkCompany(workCompany);
				workDealInfo.setConfigProduct(configProduct);
				
				//新增时扣减计费策略数量 
				ConfigChargeAgent agent = bound.getAgent();
				Integer reseNum = agent.getReserveNum();
				Integer surNum = agent.getSurplusNum();
				agent.setReserveNum(reseNum+1);
				agent.setSurplusNum(surNum-1);
				configChargeAgentService.save(agent);
				
				
				workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());		
				workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
				
				Integer year = Integer.parseInt(row.getCell(4).toString().replace(" ", ""));//申请年限
				workDealInfo.setYear(year);
				workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
				workDealInfo.setCreateBy(UserUtils.getUser());
				workDealInfo.setCreateDate(new Date());
				workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));

				WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
				workCompanyHisService.save(workCompanyHis);
				workDealInfo.setWorkCompanyHis(workCompanyHis);
				WorkUserHis workUserHis = workUserService.change(workUser,
						workCompanyHis);
				workUserHisService.save(workUserHis);
				workDealInfo.setWorkUserHis(workUserHis);
				// 保存申请人信息
				WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
				
				
				String pName = ""; //经办人姓名
				if(row.getCell(28)!=null&&!row.getCell(28).toString().replace(" ", "").equals("")){
					pName = row.getCell(28).toString().replace(" ", "");
				}
				if(pName!=null&&!pName.equals("")){
					workCertApplyInfo.setName(pName);
				}
				String pEmail = ""; //身份证号
				if(row.getCell(29)!=null&&!row.getCell(29).toString().replace(" ", "").equals("")){
					pEmail = row.getCell(29).toString().replace(" ", "");
				}
				if(pEmail!=null&&!pEmail.equals("")){
					workCertApplyInfo.setEmail(pEmail);
				}
				String pIDCard = "";//经办人邮箱
				if(row.getCell(30)!=null&&!row.getCell(30).toString().replace(" ", "").equals("")){
					pIDCard = row.getCell(30).toString().replace(" ", "");
				}
				if(pIDCard!=null&&!pIDCard.equals("")){
					workCertApplyInfo.setIdCard(pIDCard);
				}
				workCertApplyInfoService.save(workCertApplyInfo);

				WorkCertInfo workCertInfo = new WorkCertInfo();
				workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
				workCertInfoService.save(workCertInfo);
				workDealInfo.setWorkCertInfo(workCertInfo);
				workDealInfo.setInputUser(UserUtils.getUser());
				workDealInfo.setPayUser(UserUtils.getUser());
				workDealInfo.setIsSJQY(2);
				this.save(workDealInfo);
				ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
				dealInfoBound.setDealInfo(workDealInfo);
				ConfigChargeAgent agentBound =  configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				dealInfoBound.setAgent(agentBound);
				configAgentBoundDealInfoService.save(dealInfoBound);
				logUtil.saveSysLog("计费策略模版", "计费策略模版："+agent.getId()+"--业务编号："+workDealInfo.getId()+"--关联成功!", "");
				
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
				Double openAccountMoney = configChargeAgentDetailService.getChargeMoney(
						workDealInfo.getConfigChargeAgentId(),
						WorkDealInfoType.getWorkType(WorkDealInfoType.TYPE_OPEN_ACCOUNT),null);
				workPayInfo.setOpenAccountMoney(openAccountMoney);
				
				Double addCert = configChargeAgentDetailService
						.getChargeMoney(workDealInfo.getConfigChargeAgentId(), 
								WorkDealInfoType.getWorkType(workDealInfo.getDealInfoType()),workDealInfo.getYear());
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

				double bindMoney = openAccountMoney + addCert;//开户加新增的费用
				String payType = row.getCell(31).toString().replace(" ", "");
				if (payType.equals("现金")) {
					workPayInfo.setMethodMoney(true);
					workPayInfo.setMoney(bindMoney);
				}else if(payType.equals("POS付款")){
					workPayInfo.setMethodPos(true);
					workPayInfo.setPosMoney(bindMoney);
				}
				
				if (workDealInfo.getPayType().equals(2)) {
					workPayInfo.setMethodGov(true);
				}else if(workDealInfo.getPayType().equals(3)){
					workPayInfo.setMethodContract(true);
				}
				
				workPayInfo.setWorkTotalMoney(bindMoney);
				workPayInfo.setWorkPayedMoney(bindMoney);
				workPayInfo.setUserReceipt(true);
				workPayInfo.setReceiptAmount(bindMoney);
				workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
				workPayInfoService.save(workPayInfo);
				
				
				String payCompanyName = ""; 
				if(row.getCell(32)!=null&&!row.getCell(32).toString().replace(" ", "").equals("")){
					payCompanyName = row.getCell(32).toString().replace(" ", "");
				}
				
				List<FinancePaymentInfo> payMentInfos = financePaymentInfoService.findByCompanyName(payCompanyName);	
				
				if (payType.equals("财务支付") && payCompanyName!=null && !payCompanyName.equals("")) {
					for (int y = 0; y < payMentInfos.size(); y++) {
						if (bindMoney == 0d) {
							break;
						}
						FinancePaymentInfo financePaymentInfo = payMentInfos.get(y);
						WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
						if (bindMoney > financePaymentInfo.getResidueMoney()) {
							financePayInfoRelation.setMoney(financePaymentInfo
									.getResidueMoney());
							bindMoney = bindMoney
									- financePaymentInfo.getResidueMoney();
							financePaymentInfo.setResidueMoney((double) 0);
						} else {
							financePayInfoRelation.setMoney(bindMoney);
							financePaymentInfo.setResidueMoney(financePaymentInfo
									.getResidueMoney() - bindMoney);
							bindMoney = 0d;
						}
						if (financePaymentInfo.getBingdingTimes()==null) {
							financePaymentInfo.setBingdingTimes(1);
						} else {
							financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes()+1);
						}
						if (i == 0) {
							workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
						}
						financePaymentInfoService.save(financePaymentInfo);
						
						financePayInfoRelation
								.setFinancePaymentInfo(financePaymentInfo);
						financePayInfoRelation.setWorkPayInfo(workPayInfo);
						financePayInfoRelation.setSn(PayinfoUtil.getPayInfoNo());
						workFinancePayInfoRelationService.save(financePayInfoRelation);
					}
				}
				workPayInfoService.save(workPayInfo);
				workDealInfo.setWorkPayInfo(workPayInfo);
				workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ADD_USER);
				this.checkWorkDealInfoNeedSettle(workDealInfo);
				this.save(workDealInfo);
			}
			
			if (a == 1) {
				return ifErr(-1, ifErr.toString());
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
		if (filePath.equals(null) || filePath.equals("") || !filePath.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
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

	private JSONObject ifErr(int status, String msg) {
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
		if (!row.getCell(0).toString().replace(" ", "").equals("应用名称"))
			return false;
		if (!row.getCell(1).toString().replace(" ", "").equals("产品名称"))
			return false;
		if (!row.getCell(2).toString().replace(" ", "").equals("应用标识"))
			return false;
		if (!row.getCell(3).toString().replace(" ", "").equals("计费策略模板名称"))
			return false;
		if (!row.getCell(4).toString().replace(" ", "").equals("申请年限"))
			return false;
		if (!row.getCell(5).toString().replace(" ", "").equals("单位名称"))
			return false;
		if (!row.getCell(6).toString().replace(" ", "").equals("单位类型"))
			return false;
		if (!row.getCell(7).toString().replace(" ", "").equals("组织机构代码"))
			return false;
		if (!row.getCell(8).toString().replace(" ", "").equals("组织机构代码有效期"))
			return false;
		if (!row.getCell(9).toString().replace(" ", "").equals("服务级别"))
			return false;
		if (!row.getCell(10).toString().replace(" ", "").equals("单位证照"))
			return false;
		if (!row.getCell(11).toString().replace(" ", "").equals("证件号"))
			return false;
		if (!row.getCell(12).toString().replace(" ", "").equals("单位证照有效期"))
			return false;
		if (!row.getCell(13).toString().replace(" ", "").equals("法人姓名"))
			return false;
		if (!row.getCell(14).toString().replace(" ", "").equals("省"))
			return false;
		if (!row.getCell(15).toString().replace(" ", "").equals("市"))
			return false;
		if (!row.getCell(16).toString().replace(" ", "").equals("县"))
			return false;
		if (!row.getCell(17).toString().replace(" ", "").equals("区域备注"))
			return false;
		if (!row.getCell(18).toString().replace(" ", "").equals("街道地址"))
			return false;
		if (!row.getCell(19).toString().replace(" ", "").equals("单位联系电话"))
			return false;
		if (!row.getCell(20).toString().replace(" ", "").equals("备注信息"))
			return false;
		if (!row.getCell(21).toString().replace(" ", "").equals("证书持有人姓名"))
			return false;
		if (!row.getCell(22).toString().replace(" ", "").equals("证书持有人证件"))
			return false;
		if (!row.getCell(23).toString().replace(" ", "").equals("证件号码"))
			return false;
		if (!row.getCell(24).toString().replace(" ", "").equals("证书持有人电子邮件"))
			return false;
		if (!row.getCell(25).toString().replace(" ", "").equals("证书持有人手机号"))
			return false;
		if (!row.getCell(26).toString().replace(" ", "").equals("业务系统UID"))
			return false;
		if (!row.getCell(27).toString().replace(" ", "").equals("证书持有人性别"))
			return false;
		if (!row.getCell(28).toString().replace(" ", "").equals("经办人姓名"))
			return false;
		if (!row.getCell(29).toString().replace(" ", "").equals("身份证号"))
			return false;
		if (!row.getCell(30).toString().replace(" ", "").equals("经办人邮箱"))
			return false;
		if (!row.getCell(31).toString().replace(" ", "").equals("支付类型(现金、POS付款、财务支付)"))
			return false;
		if (!row.getCell(32).toString().replace(" ", "").equals("付款单位名称(当选择财务支付时填写)"))
			return false;
		return true;
	}

}
