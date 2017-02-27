/**

f * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.service.SelfAreaService;
import com.itrus.ca.modules.self.service.SelfImageService;
import com.itrus.ca.modules.sys.entity.CommonAttach;
import com.itrus.ca.modules.sys.service.CommonAttachService;
import com.itrus.ca.modules.sys.utils.UserUtils;
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
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 业务变更补办吊销Controller
 * 
 * @author ZhaoShaoBo
 * @version 2014-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workDealInfoOperation")
public class WorkDealInfoOperationController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(WorkDealInfoOperationController.class);
	
	
	
	
	@Autowired
	private WorkLogService workLogService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;

	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigProductService configProductService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private WorkCompanyHisService workCompanyHisService;

	@Autowired
	private WorkUserHisService workUserHisService;
	
	@Autowired
	private WorkPayInfoService workPayInfoService;
	
	@Autowired
	ConfigRaAccountService raAccountService;
	
	@Autowired
	private CommonAttachService attachService;
	
	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;
	
	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;
	
	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	@Autowired
	private ConfigChargeAgentService chargeAgentService;
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	
	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;
	
	@Autowired
	private SelfImageService selfImageService;
	
	@Autowired
	private SelfAreaService selfAreaService;
	
	private LogUtil logUtil = new LogUtil();

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "revokeFrom")
	public String revokeFrom(Long id, Model model, RedirectAttributes redirectAttributes) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		model.addAttribute("workDealInfo", workDealInfo);
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点吊销！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
//		List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
//		model.addAttribute("workLog", list);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		return "modules/work/workDealInfoRevoke";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "changeFrom")
	public String changeFrom(Long id, Model model, RedirectAttributes redirectAttributes) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		model.addAttribute("workDealInfo", workDealInfo);
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点变更！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}
//		List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
//		model.addAttribute("workLog", list);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		return "modules/work/workDealInfoChange";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "changeSave")
	public String changeSave(Long id, String orgExpirationTime,
			String selectLv, String comCertificateType,
			String comCertficateNumber, String comCertficateTime,
			String legalName, String s_province, String s_city,
			String s_county, String address, String companyMobile,
			String remarks, Integer workType, String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel, String recordContent,
			Integer dealInfType, Integer dealInfType1, Integer year,
			Model model, String pName, String pEmail, String pIDCard,String contactSex,
			Boolean manMadeDamage, Long workDealInfoId) {
		if (workDealInfoId!=null) {
			workDealInfoService.deleteWork(workDealInfoId);
		}
		// 保存单位信息	
		WorkDealInfo workDealInfo1 = workDealInfoService.get(id);
		WorkCompany workCompany = workDealInfo1.getWorkCompany();
		workCompany.setComCertificateType(comCertificateType);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(comCertficateTime+" 00:00:00");
			ts1 = Timestamp.valueOf(orgExpirationTime+" 00:00:00");
		} catch (Exception e) {
			// TODO: handle exception
		}
		workCompany.setComCertficateTime(ts);
		workCompany.setComCertficateNumber(comCertficateNumber);
		workCompany.setSelectLv(selectLv);
		workCompany.setOrgExpirationTime(ts1);
		workCompany.setProvince(s_province);
		workCompany.setLegalName(legalName);
		workCompany.setCity(s_city);
		workCompany.setDistrict(s_county);
		workCompany.setAddress(address);
		workCompany.setCompanyMobile(companyMobile);
		workCompany.setRemarks(remarks);
		workCompanyService.save(workCompany);
		WorkCompanyHis companyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(companyHis);
		// 保存经办人信息
		WorkUser workUser = workDealInfo1.getWorkUser();
		workUser.setContactName(contactName);
		workUser.setStatus(1);
		workUser.setConCertType(conCertType);
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactEmail(contacEmail);
		workUser.setContactPhone(contactPhone);
		workUser.setContactTel(contactTel);
		workUserService.save(workUser);
		WorkUserHis userHis = workUserService.change(workUser, companyHis);
		workUserHisService.save(userHis);
		// 保存业务信息

		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setManMadeDamage(manMadeDamage);
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUserHis(userHis);
		workDealInfo.setWorkCompanyHis(companyHis);
		workDealInfo.setConfigProduct(workDealInfo1.getConfigProduct());
		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_INFORMATION_REROUTE);
		if (dealInfType1 != null) {
			workDealInfo.setDealInfoType1(dealInfType1);
		}
		if (year==null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
		}
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		workDealInfo.setPrevId(workDealInfo1.getId());
		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}
		
		// 保存申请人信息
		if (workDealInfo.getConfigProduct().getProductName()
				.equals("2")
				|| workDealInfo.getConfigProduct().getProductName()
						.equals("3")) {
			WorkCertApplyInfo workCertApplyInfo = workDealInfo1
					.getWorkCertInfo().getWorkCertApplyInfo();
			workCertApplyInfo.setName(pName);
			workCertApplyInfo.setEmail(pEmail);
			workCertApplyInfo.setIdCard(pIDCard);
			workCertApplyInfo.setProvince(workCompany.getProvince());
			workCertApplyInfo.setCity(workCompany.getCity());

			workCertApplyInfoService.save(workCertApplyInfo);

			WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
			// 保存work_cert_info
			WorkCertInfo workCertInfo = new WorkCertInfo();
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfo.setRenewalPrevId(oldCertInfo.getId());
			workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
			workCertInfoService.save(workCertInfo);
			oldCertInfo.setRenewalNextId(workCertInfo.getId());
			workCertInfoService.save(oldCertInfo);
			workDealInfo.setWorkCertInfo(workCertInfo);
		} else {
			WorkCertApplyInfo workCertApplyInfo = workDealInfo1
					.getWorkCertInfo().getWorkCertApplyInfo();
			workCertApplyInfo.setName(workCompany.getCompanyName());
			workCertApplyInfo.setEmail(workUser.getContactEmail());
			workCertApplyInfo.setMobilePhone(workCompany.getCompanyMobile());
			workCertApplyInfo.setProvince(workCompany.getProvince());
			workCertApplyInfo.setCity(workCompany.getCity());
			workCertApplyInfoService.save(workCertApplyInfo);

			WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
			// 保存work_cert_info
			WorkCertInfo workCertInfo = new WorkCertInfo();
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfo.setRenewalPrevId(oldCertInfo.getId());
			workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
			workCertInfoService.save(workCertInfo);
			oldCertInfo.setRenewalNextId(workCertInfo.getId());
			workCertInfoService.save(oldCertInfo);
			workDealInfo.setWorkCertInfo(workCertInfo);
		}
//		List<ConfigAgentOfficeRelation> li = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
//		if (li.size()>0) {
//			workDealInfo.setCommercialAgent(li.get(0).getConfigCommercialAgent());
//		}
		workDealInfoService.delete(workDealInfo1.getId());
		workDealInfoService.save(workDealInfo);
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workCompany);
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		// 计算更新金额
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigProduct().getChargeAgentId());
		model.addAttribute("chargeAgent", chargeAgent);
		Double money = configChargeAgentDetailService.selectMoney(chargeAgent,
				WorkDealInfoType.TYPE_INFORMATION_REROUTE, null, workDealInfo
						.getConfigProduct().getProductLabel());
		model.addAttribute("type5", money);
		if (dealInfType1 != null) {
			Double money1 = configChargeAgentDetailService.selectMoney(
					chargeAgent, dealInfType1, year, workDealInfo
							.getConfigProduct().getProductLabel());
			//新增这个更新年数需要的钱
			Double addMoney = configChargeAgentDetailService.selectMoney(
					chargeAgent, WorkDealInfoType.TYPE_ADD_CERT, year, workDealInfo
					.getConfigProduct().getProductLabel());
			if (money+money1>addMoney) {
				model.addAttribute("addMoney", addMoney);
			}
			model.addAttribute("type2", money1);
		}
		if (workDealInfo.getConfigProduct().getProductLabel() == 0) {
			model.addAttribute("lable", "通用");
		}
		if (workDealInfo.getConfigProduct().getProductLabel() == 1) {
			model.addAttribute("lable", "专用");
		}
		model.addAttribute("year", workDealInfo1.getYear());
		model.addAttribute("workDealInfo", workDealInfo);
		String product = ProductType.getProductTypeName(Integer
				.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		model.addAttribute("dealInfoType", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice()
				.getName());
		model.addAttribute("date", sdf.format(new Date()));
		model.addAttribute("dealInfoType", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		
		logUtil.saveSysLog("业务中心", "业务变更：编号"+workDealInfo.getId()+"单位名称："+workDealInfo.getWorkCompany().getCompanyName(), "");
		return "modules/work/workDealInfoChangeLoad";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "reissueFrom")
	public String reissueFrom(Long id, Model model, RedirectAttributes redirectAttributes) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		model.addAttribute("workDealInfo", workDealInfo);
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
//		List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
//		model.addAttribute("workLog", list);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		return "modules/work/workDealInfoReissue";
	}

	static Long MILL = 86400000L;

	private int getLastCertDay(Date notAfter) {
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong<nowLong) {
			return 0;
		}
		
		long d = (notAfterLong - nowLong)/MILL;
		
		long hour1=(notAfterLong - nowLong)%MILL;
		if (hour1>0) {
			d+=1;
		}
		
		return (int) d;
	}

	/**
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @param recordContent
	 * @param workDealInfoId
	 * @param dealInfType
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "reissueSave")
	public String reissueSave(Model model,
			RedirectAttributes redirectAttributes, String recordContent,
			Long workDealInfoId, Integer dealInfType, Integer dealInfType1,
			Integer year, Long newInfoId) {
		if (newInfoId!=null) {
			workDealInfoService.deleteWork(newInfoId);
		}
		// 新建业务信息
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		workDealInfo.setConfigCommercialAgent(workDealInfo1
				.getConfigCommercialAgent());
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		workDealInfo.setWorkUser(workDealInfo1.getWorkUser());
		workDealInfo.setWorkCompany(workDealInfo1.getWorkCompany());
		workDealInfo.setWorkUserHis(workDealInfo1.getWorkUserHis());
		workDealInfo.setWorkCompanyHis(workDealInfo1.getWorkCompanyHis());
		workDealInfo.setConfigProduct(workDealInfo1.getConfigProduct());
		if (year==null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
		}
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		if (dealInfType == 2) {
			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_LOST_CHILD);
		} else {
			workDealInfo
					.setDealInfoType(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
		}
		workDealInfo.setDealInfoType1(dealInfType1);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}
		workDealInfo.setPrevId(workDealInfo1.getId());
//		List<ConfigAgentOfficeRelation> li = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
//		if (li.size()>0) {
//			workDealInfo.setCommercialAgent(li.get(0).getConfigCommercialAgent());
//		}

		WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
		workCertApplyInfo.setName(workDealInfo1.getWorkCompany()
				.getCompanyName());
		workCertApplyInfo.setEmail(workDealInfo1.getWorkUser()
				.getContactEmail());
		workCertApplyInfo.setMobilePhone(workDealInfo1.getWorkCompany()
				.getCompanyMobile());
		workCertApplyInfo.setProvince(workDealInfo1.getWorkCompany()
				.getProvince());
		workCertApplyInfo.setCity(workDealInfo1.getWorkCompany().getCity());
		workCertApplyInfoService.save(workCertApplyInfo);

		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		// 保存work_cert_info
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfoService.delete(workDealInfo1.getId());
		workDealInfoService.save(workDealInfo);
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		// 计算更新金额
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigProduct().getChargeAgentId());
		model.addAttribute("chargeAgent", chargeAgent);
		if (dealInfType == 2) {
			Double money = configChargeAgentDetailService.selectMoney(
					chargeAgent, workDealInfo.getDealInfoType(), null,
					workDealInfo.getConfigProduct().getProductLabel());
			model.addAttribute("type3", money);
		}
		if (dealInfType == 3) {
			Double money1 = configChargeAgentDetailService.selectMoney(
					chargeAgent, workDealInfo.getDealInfoType(), null,
					workDealInfo.getConfigProduct().getProductLabel());
			model.addAttribute("type4", money1);
		}
		if (dealInfType1 != null) {
			Double money2 = configChargeAgentDetailService.selectMoney(
					chargeAgent, dealInfType1, year, workDealInfo
							.getConfigProduct().getProductLabel());
			model.addAttribute("type2", money2);
		}
		if (workDealInfo.getConfigProduct().getProductLabel() == 0) {
			model.addAttribute("lable", "通用");
		}
		if (workDealInfo.getConfigProduct().getProductLabel() == 1) {
			model.addAttribute("lable", "专用");
		}
		model.addAttribute("workDealInfo", workDealInfo);
		String product = ProductType.getProductTypeName(Integer
				.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice()
				.getName());
		model.addAttribute("date", sdf.format(new Date()));
		model.addAttribute("dealInfoType", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		logUtil.saveSysLog("业务中心", "业务补办：编号"+workDealInfo.getId()+"单位名称："+workDealInfo.getWorkCompany().getCompanyName(), "");
		
		return "modules/work/workDealInfoReissueLoad";
	}

	@RequestMapping("make")
	public String make(Long id, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		
		if(workDealInfo.getDealInfoType()!=null&&workDealInfo.getDealInfoType1()==null){
			if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)) {
				model.addAttribute("isShow", "No");
			}else{
				
				model.addAttribute("isShow", "Yes");
			}
		}else if (workDealInfo.getDealInfoType()==null&&workDealInfo.getDealInfoType1()==null) {
				model.addAttribute("isShow", "No");
		}else{
			
			
			model.addAttribute("isShow", "Yes");
		}
		
		ArrayList<Integer> dealInfoTypes = new ArrayList<Integer>();
		if (workDealInfo.getDealInfoType()!=null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType());
		}
		if(workDealInfo.getDealInfoType1()!=null){
			dealInfoTypes.add(workDealInfo.getDealInfoType1());
		}
		if(workDealInfo.getDealInfoType2()!=null){
			dealInfoTypes.add(workDealInfo.getDealInfoType2());
		}
		if(workDealInfo.getDealInfoType3()!=null){
			dealInfoTypes.add(workDealInfo.getDealInfoType3());
		}
		if(dealInfoTypes.size()==1){
			if(dealInfoTypes.get(0).equals(1)){
				model.addAttribute("onlyUpdate","onlyUpdate");
			}
		}
		
		
		//经信委
		if(workDealInfo.getExpirationDate()!=null){
			
			workDealInfo.setAddCertDays(StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate())-workDealInfo.getYear()*365-workDealInfo.getLastDays());
			
			workDealInfoService.save(workDealInfo);
			
			model.addAttribute("expirationDate", workDealInfo.getExpirationDate());
			model.addAttribute("addCertDays",StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate())-workDealInfo.getYear()*365-workDealInfo.getLastDays());
			model.addAttribute("validiteDays",StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate()));
		}

		//解决迁移导致的多证书编号错误问题,让用户前台输入进行修改
		if(workDealInfo.getDealInfoType2()==WorkDealInfoType.TYPE_INFORMATION_REROUTE){
			model.addAttribute("isChangeBusiness", true);
		}
		
		//秘钥长度可选
		if(raAccount.getKeyLen()!=null){
			model.addAttribute("keyLen", raAccount.getKeyLen());
		}
		
		
		if (workDealInfo.getPrevId()!=null) {
			//获取上一张证书的签名证书序列号
			WorkDealInfo oldDealInfo = workDealInfoService.get(workDealInfo.getPrevId());
			try {
				model.addAttribute("signSerialNumber", oldDealInfo.getWorkCertInfo().getSerialnumber().toLowerCase());
			} catch (Exception e) {
			}
		}
		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "modules/work/workDealInfoAuditMake";
			} else if (workDealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			} else if (workDealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_RETURN_MONEY)){
				return "modules/work/workDealInfoBackMoneyAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType1() != null) {
			if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType1()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "modules/work/workDealInfoAuditMake";
			} else if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType1().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			} else if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_RETURN_MONEY)){
				return "modules/work/workDealInfoBackMoneyAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType2() != null) {
			if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType2()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "modules/work/workDealInfoAuditMake";
			} else if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType2().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			} else if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_RETURN_MONEY)){
				return "modules/work/workDealInfoBackMoneyAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType3() != null) {
			if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType3()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "modules/work/workDealInfoAuditMake";
			} else if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType3().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			} else if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_RETURN_MONEY)){
				return "modules/work/workDealInfoBackMoneyAuditMake";
			}
		}
		
		return "modules/work/workDealInfoAuditMake";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping("errorForm")
	public String errorForm(Long id, Model model,RedirectAttributes redirectAttributes) {
		String url = Global.getConfig("images.path");
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		if(workDealInfo.getDealInfoType()!=null&&workDealInfo.getDealInfoType()==0){ //新增证书的重新编辑
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName()+"##"+attachs.get(0).getStatus();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName()+"##"+attachs.get(i).getStatus();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			WorkCompany workCompany = workDealInfo.getWorkCompany();
			WorkUser workUser = workDealInfo.getWorkUser();
			model.addAttribute("workCompany", workCompany);
			model.addAttribute("workUser", workUser);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("user", UserUtils.getUser());
			model.addAttribute("date", sdf.format(new Date()));
			if (workDealInfo.getWorkCertInfo() != null) {
				model.addAttribute("workCertApplyInfo", workDealInfo
						.getWorkCertInfo().getWorkCertApplyInfo());
			}
			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
			
			
			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(workDealInfo.getConfigProduct().getId());		
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
			}
			
			model.addAttribute("boundLabelList", nameSet);
			model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			model.addAttribute("workLog", list);
			model.addAttribute("workDealInfo", workDealInfo);
			//获得应用下的产品
			List<ConfigProduct> products = configProductService.findByApp(workDealInfo.getConfigApp().getId());
			List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
			for (int i = 0; i < products.size(); i++) {
				String ssssi = ProductType.productTypeStrMap.get(products.get(i).getProductName())+"["+(products.get(i).getProductLabel()==0?"通用":"专用")+"]";
				ProductTypeObj obj = new ProductTypeObj(products.get(i).getId().intValue(), ssssi);
				listProductTypeObjs.add(obj);
			}
			model.addAttribute("proList", listProductTypeObjs);
			
			if(workDealInfo.getExpirationDate()!=null){
				model.addAttribute("expirationDate", workDealInfo.getExpirationDate());	
			}		
			
			
			//获得省和市对应self_area表中的id
			if(workDealInfo.getWorkCompany().getProvince()!=null&&!workDealInfo.getWorkCompany().getProvince().isEmpty()){
				String provinceId = selfAreaService.findByAreaName(workDealInfo.getWorkCompany().getProvince()).getAreaId();
				model.addAttribute("provinceId", provinceId);
				if(workDealInfo.getWorkCompany().getCity()!=null&&!workDealInfo.getWorkCompany().getCity().isEmpty()){
					
					String cityId = selfAreaService.findByProvinceName(workDealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
					model.addAttribute("cityId", cityId);
				}
				
			}
			
			System.out.println("325846");
			return "modules/work/workDealInfoErrorForm";
		}else{
			//类型判断
			Integer dealInfoType = workDealInfo.getDealInfoType();
			Integer dealInfoType1 = workDealInfo.getDealInfoType1();
			Integer dealInfoType2 = workDealInfo.getDealInfoType2();
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName()+"##"+attachs.get(0).getStatus();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName()+"##"+attachs.get(i).getStatus();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			//区别再次编辑的页面
			model.addAttribute("iseditor", "iseditor");
			model.addAttribute("workLog", list);
			
			if(dealInfoType!=null){
				if(dealInfoType==1){//更新证书
					if(dealInfoType1!=null){
						if(dealInfoType1==2){//遗失补办
							if(dealInfoType2!=null){
								if(dealInfoType2==4){//信息变更
									return typeForm("1,2,3",workDealInfo,model,"1",redirectAttributes);
								}
							}
							//更新+遗失补办
							return typeForm(",2,3",workDealInfo,model,"1",redirectAttributes);
						}else if(dealInfoType1==3){
							if(dealInfoType2!=null){
								if(dealInfoType2==4){//信息变更
									return typeForm("1,2,3",workDealInfo,model,"2",redirectAttributes);
								}
							}
							//更新+损坏更换
							return typeForm(",2,3",workDealInfo,model,"2",redirectAttributes);
						}
					}else{
						if(dealInfoType2!=null){
							if(dealInfoType2==4){//信息变更
								return typeForm("1,3",workDealInfo,model,"1",redirectAttributes);
							}
						}
						return typeForm(",3",workDealInfo,model,"1",redirectAttributes);					
					}
				}else{
					return null;
				}
			}else{
				if(dealInfoType1!=null){
					if(dealInfoType1==2){//遗失补办
						if(dealInfoType2!=null){
							if(dealInfoType2==4){//+信息变更
								return typeForm("1,2",workDealInfo,model,"1",redirectAttributes);	
							}
						}
						return typeForm(",2",workDealInfo,model,"1",redirectAttributes);	
					}else if(dealInfoType1==3){//损坏更换
						if(dealInfoType2!=null){
							if(dealInfoType2==4){//+信息变更
								return typeForm("1,2",workDealInfo,model,"2",redirectAttributes);	
							}
						}
						return typeForm(",2",workDealInfo,model,"2",redirectAttributes);	
					}
				}else{
					if(dealInfoType2!=null){
						return typeForm("1",workDealInfo,model,"1",redirectAttributes);	
					}
				}
			}
			
			return null;
		}
	}

	public String typeForm(String dealType, WorkDealInfo workDealInfo,
			Model model, String reissueType,
			RedirectAttributes redirectAttributes) {
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId()
					.equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		workDealInfo.setIsMainTain("mainTain");
		workDealInfoService.save(workDealInfo);
		String[] type = dealType.replace(",", " ").split(" ");
		for (int i = 0; i < type.length; i++) {
			if (type[i].equals("1")) {
				model.addAttribute("change", "1");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点变更！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("2")) {
				if (reissueType.equals("1")) {
					model.addAttribute("reissue", "1");
				} else {
					model.addAttribute("reissue", "2");
				}
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("3")) {
				model.addAttribute("update", "3");

				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				String[] years = configChargeAgentDetailService
						.getChargeAgentYears(configProduct.getChargeAgentId(),
								WorkDealInfoType.TYPE_UPDATE_CERT);
				for (int j = 0; j < years.length; j++) {
					switch (years[j]) {
					case "1":
						model.addAttribute("year1", true);
						break;
					case "2":
						model.addAttribute("year2", true);
						break;
					case "4":
						model.addAttribute("year4", true);
						break;
					case "5":
						model.addAttribute("year5", true);
						break;
					}
				}

					
				 if (!inOffice) { redirectAttributes.addAttribute("fd",
				UUID.randomUUID().toString()); addMessage(redirectAttributes,
				"请到业务办理网点更新！"); return "redirect:" + Global.getAdminPath() +
						"/work/workDealInfo/?repage"; }
					 
			} else if (type[i].equals("4")) {
				model.addAttribute("revoke", "4");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点吊销！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("5")) {

				model.addAttribute("reissue", "2");

				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			}
		}
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}
		model.addAttribute("pro", ProductType.productTypeStrMap);

		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));

		ConfigChargeAgent chargeAgent = null;
		if (workDealInfo.getConfigChargeAgentId() != null) {
			chargeAgent = chargeAgentService.get(workDealInfo
					.getConfigChargeAgentId());

		}
		// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

		if (workDealInfo.getPayType() == null) {
			workDealInfo
					.setPayType(Integer.parseInt(chargeAgent.getTempStyle()));
		}

		model.addAttribute("workDealInfo", workDealInfo);

		// 获得应用下的产品
		List<ConfigProduct> products = configProductService
				.findByAppAndProName(workDealInfo.getConfigApp().getId(),
						workDealInfo.getConfigProduct().getProductName());
		List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
		for (int i = 0; i < products.size(); i++) {
			String ssssi = ProductType.productTypeStrMap.get(products.get(i)
					.getProductName())
					+ "["
					+ (products.get(i).getProductLabel() == 0 ? "通用" : "专用")
					+ "]";
			ProductTypeObj obj = new ProductTypeObj(products.get(i).getId()
					.intValue(), ssssi);
			listProductTypeObjs.add(obj);
		}
		model.addAttribute("proList", listProductTypeObjs);

		model.addAttribute("expirationDate",StringHelper.getLastDateOfCurrentYear());
		
		//获得省和市对应self_area表中的id
		if(workDealInfo.getWorkCompany().getProvince()!=null&&!workDealInfo.getWorkCompany().getProvince().isEmpty()){
			String provinceId = selfAreaService.findByAreaName(workDealInfo.getWorkCompany().getProvince()).getAreaId();
			model.addAttribute("provinceId", provinceId);
			if(workDealInfo.getWorkCompany().getCity()!=null&&!workDealInfo.getWorkCompany().getCity().isEmpty()){
				
				String cityId = selfAreaService.findByProvinceName(workDealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
				model.addAttribute("cityId", cityId);
			}
		
		}
		
		if (dealType.indexOf("1") >= 0) {

			model.addAttribute("isOK", "isNo");

		} else {

			if (dealType.indexOf("3") >= 0) {

				model.addAttribute("isOK", "isYes");
			} else {

				model.addAttribute("isOK", "isNo");

			}
		}

		model.addAttribute("dealType", dealType);

		ArrayList<String> dealInfoTypes = new ArrayList<String>();
		for (int i = 0; i < type.length; i++) {
			if (type[i] != null && !type[i].equals("")) {
				dealInfoTypes.add(type[i]);
			}
		}

		if (workDealInfo.getSelfApplyId() != null) {
			model.addAttribute("imgUrl", Global.getConfig("images.path"));
			SelfImage selfImage = selfImageService
					.findByApplicationId(workDealInfo.getSelfApplyId());
			workDealInfo.setSelfImage(selfImage);
		}
		if (workDealInfo.getSelfImage() != null) {
			model.addAttribute("imgUrl", Global.getConfig("images.path"));
		}

		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals("1")) {

				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				return "modules/work/maintain/workDealInfoMaintainReturnChange";
			} else if (dealInfoTypes.get(0).equals("2")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainReturnLost";
			} else if (dealInfoTypes.get(0).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
			} else if (dealInfoTypes.get(0).equals("4")) {
				ConfigChargeAgent agent = configChargeAgentService
						.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				return "modules/work/maintain/workDealInfoMaintainReturnRevoke";
			} /*else if (dealInfoTypes.get(0).equals("5")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// key升级
				model.addAttribute("isKeyUpgrade", 1);
				return "modules/work/maintain/workDealInfoMaintainLost";

			}
*/
		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals("1")
					&& dealInfoTypes.get(1).equals("2")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

				return "modules/work/maintain/workDealInfoMaintainReturnChange";
			} else if (dealInfoTypes.get(0).equals("2")
					&& dealInfoTypes.get(1).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}
				model.addAttribute("boundLabelList", nameSet);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
			} else if (dealInfoTypes.get(0).equals("1")
					&& dealInfoTypes.get(1).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainReturnUpdateChange";
			}
		} else if (dealInfoTypes.size() == 3) {

			ConfigProduct configProduct = workDealInfo.getConfigProduct();
			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
						.getTempStyle()));
			}

			model.addAttribute("boundLabelList", nameSet);

			if (chargeAgent != null) {
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			}

			// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			return "modules/work/maintain/workDealInfoMaintainReturnUpdateChange";
		} else {
			return "modules/work/workDealInfoMaintain";
		}

		return "modules/work/workDealInfoMaintain";
	}
	
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "errorSave")
	public String errorSave(Model model, RedirectAttributes redirectAttributes,
			Long appId, String product, Integer dealInfType,

			Integer dealInfType1, Integer year, Date expirationDate,   //到期时间  和年限二选一 
			Long workDealInfoId,

			

			Integer yar, Long companyId, String companyName,
			String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer classifying, String pName, String pEmail, String pIDCard) {
		ConfigApp configApp = configAppService.get(appId);
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(
				companyName, organizationNumber);
		List<ConfigProduct> list = configProductService.findByIdOrName(product,
				appId);
		ConfigProduct configProduct = list.get(0);
		// 保存单位信息
		workCompany.setCompanyName(companyName);
		workCompany.setCompanyType(companyType);
		workCompany.setComCertificateType(comCertificateType);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		workCompany.setComCertficateTime(ts);
		workCompany.setComCertficateNumber(comCertficateNumber);
		workCompany.setSelectLv(selectLv);
		workCompany.setOrganizationNumber(organizationNumber);
		try {
			ts1 = Timestamp.valueOf(orgExpirationTime + " 00:00:00");
			ts = Timestamp.valueOf(comCertficateTime + " 00:00:00");
		} catch (Exception e) {
			// TODO: handle exception
		}
		workCompany.setOrgExpirationTime(ts1);
		workCompany.setProvince(s_province);
		workCompany.setLegalName(legalName);
		workCompany.setCity(s_city);
		workCompany.setDistrict(s_county);
		workCompany.setAddress(address);
		workCompany.setCompanyMobile(companyMobile);
		workCompany.setRemarks(remarks);

		// 保存经办人信息
		WorkUser workUser = new WorkUser();
		workUser.setId(workuserId);
		workUser.setStatus(1);
		workUser.setContactName(contactName);
		workUser.setConCertType(conCertType);
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactEmail(contacEmail);
		workUser.setContactPhone(contactPhone);
		workUser.setContactTel(contactTel);
		workUser.setWorkCompany(workCompany);
		// 保存work_deal-info
		WorkDealInfo workDealInfo = null;
		if (workDealInfoId != null) {
			workDealInfo = workDealInfoService.get(workDealInfoId);
		} else {
			workDealInfo = new WorkDealInfo();
		}
		if (configApp == null) {
			model.addAttribute("workUser", workUser);
			model.addAttribute("workCompany", workCompany);
			model.addAttribute("product", product);
			model.addAttribute("dealInfType", dealInfType);
			model.addAttribute("year", year);

			return "modules/work/workDealInfoForm";
		}
		workCompanyService.save(workCompany);
		workUserService.save(workUser);

		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(configApp);
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		workDealInfo.setConfigApp(configApp);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setConfigProduct(configProduct);
		workDealInfo.setDealInfoType(dealInfType);
		workDealInfo.setDealInfoType1(dealInfType1);


	

		//经信委
		if(year!=null&&expirationDate==null){

			workDealInfo.setYear(year);

			workDealInfo.setExpirationDate(null);
		}else{
			workDealInfo.setYear(StringHelper.getDvalueYear(expirationDate));
			workDealInfo.setExpirationDate(expirationDate);

		}
		workDealInfo.setClassifying(classifying);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));

		WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(workCompanyHis);
		workDealInfo.setWorkCompanyHis(workCompanyHis);
		WorkUserHis workUserHis = workUserService.change(workUser,
				workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		if (workDealInfo.getConfigProduct().getProductName()
				.equals("2")
				|| workDealInfo.getConfigProduct().getProductName()
						.equals("3")) {
			WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
			workCertApplyInfo.setName(pName);
			workCertApplyInfo.setEmail(pEmail);
			workCertApplyInfo.setIdCard(pIDCard);
			workCertApplyInfo.setProvince(workCompany.getProvince());
			workCertApplyInfo.setCity(workCompany.getCity());

			workCertApplyInfoService.save(workCertApplyInfo);
			// 保存work_cert_info
			WorkCertInfo workCertInfo = new WorkCertInfo();
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfoService.save(workCertInfo);
			workDealInfo.setWorkCertInfo(workCertInfo);
		} else {
			WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
			workCertApplyInfo.setName(workCompany.getCompanyName());
			workCertApplyInfo.setEmail(workUser.getContactEmail());
			workCertApplyInfo.setMobilePhone(workCompany.getCompanyMobile());
			workCertApplyInfo.setProvince(workCompany.getProvince());
			workCertApplyInfo.setCity(workCompany.getCity());
			workCertApplyInfoService.save(workCertApplyInfo);

			WorkCertInfo workCertInfo = new WorkCertInfo();
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfoService.save(workCertInfo);
			workDealInfo.setWorkCertInfo(workCertInfo);
		}
//		List<ConfigAgentOfficeRelation> li = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
//		if (li.size()>0) {
//			workDealInfo.setCommercialAgent(li.get(0).getConfigCommercialAgent());
//		}
		workDealInfoService.save(workDealInfo);
		// 录入人日志保存
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(configApp);
		workLog.setWorkCompany(workCompany);
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);

//		// 判断是否开户
//		List<WorkDealInfo> wdi = workDealInfoService.selectAccount(
//				workDealInfo.getConfigApp(), workDealInfo.getConfigProduct(),
//				workDealInfo.getWorkCompany(), workDealInfo.getId());
		boolean account = false;
		if (WorkDealInfoType.TYPE_ADD_CERT.equals(workDealInfo.getDealInfoType())) {
			account = false;
		} else {
			account = true;
		}
		Integer lable = workDealInfo.getConfigProduct().getProductLabel();
		if (lable == 0) {
			model.addAttribute("lable", "通用");
		}
		if (lable == 1) {
			model.addAttribute("lable", "专用");
		}
		model.addAttribute("workDealInfo", workDealInfo);
		String prod = ProductType.getProductTypeName(Integer
				.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", prod);
		int type = workDealInfo.getDealInfoType();
		model.addAttribute("dealInfoType", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigProduct().getChargeAgentId());

		if (!account) {
			Double money = configChargeAgentDetailService.selectMoney(
					chargeAgent, WorkDealInfoType.TYPE_OPEN_ACCOUNT, null,
					workDealInfo.getConfigProduct().getProductLabel());
			model.addAttribute("type0", money);
		}
		Double money = configChargeAgentDetailService.selectMoney(chargeAgent,
				type, workDealInfo.getYear(), workDealInfo.getConfigProduct()
						.getProductLabel());

		if (type == 0) {
			model.addAttribute("type1", money);
		}
		if (type == 1) {
			model.addAttribute("type2", money);
		}
		if (type == 2) {
			model.addAttribute("type3", money);
		}
		if (type == 3) {
			model.addAttribute("type4", money);
		}
		if (type == 5) {
			model.addAttribute("type5", money);
		}
		if (type == 6) {
			model.addAttribute("type6", money);
		}
		if (workDealInfo.getDealInfoType1() != null) {
			int type1 = workDealInfo.getDealInfoType1();
			Double money1 = configChargeAgentDetailService.selectMoney(
					chargeAgent, type1, workDealInfo.getYear(), workDealInfo
							.getConfigProduct().getProductLabel());
			if (type1 == 0) {
				model.addAttribute("type1", money1);
			}
			if (type1 == 1) {
				model.addAttribute("type2", money1);
			}
			if (type1 == 2) {
				model.addAttribute("type3", money1);
			}
			if (type1 == 3) {
				model.addAttribute("type4", money1);
			}
			if (type1 == 5) {
				model.addAttribute("type5", money1);
			}
			if (type1 == 6) {
				model.addAttribute("type6", money1);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice()
				.getName());
		model.addAttribute("date", sdf.format(new Date()));
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String officeName = workDealInfo.getWorkCompany().getCompanyName();
			String userName = workDealInfo.getCreateBy().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney()) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getMoney().toString(), officeName, userName,
						date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank()) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos()) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getPosMoney().toString(), officeName, userName,
						date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay()) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getAlipayMoney().toString(), officeName,
						userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodGov()) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						"", officeName,
						userName, date, "政府统一采购", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodContract()) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						"", officeName,
						userName, date, "合同采购", "" };
				payInfos.add(s);
			}
		}
		List<WorkFinancePayInfoRelation> pay = workFinancePayInfoRelationService
				.findByPayInfo(workDealInfo.getWorkPayInfo());

		model.addAttribute("fpir", pay);
		model.addAttribute("infos", payInfos);
		// 缴费信息方式
		model.addAttribute("chargeAgent", chargeAgent);
		addMessage(redirectAttributes, "录入信息成功");
		logUtil.saveSysLog("业务中心", "审核失败再次编辑：编号"+workDealInfo.getId()+"单位名称："+workDealInfo.getWorkCompany().getCompanyName(), "");
		return "modules/work/workDealInfoErrorLoad";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSave")
	public String maintainSave(Long workDealInfoId, String orgExpirationTime,
			String selectLv, String comCertificateType, String organizationNumber,
			String comCertficateNumber, String comCertficateTime, String companyName,
			String legalName, String s_province, String s_city,
			String s_county, String address, String companyMobile,
			String remarks, Integer workType, String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel, String recordContent,
			Integer dealInfoType, Integer dealInfoType1, Integer dealInfoType2,
			Integer dealInfoType3, Integer year, Integer payType,
			Model model, String pName, String pEmail, String pIDCard,String contactSex,String areaRemark,
			Boolean manMadeDamage, Long newInfoId) {
		//是否返回造成业务重复，如重复则删除
		if (newInfoId != null) {
			workDealInfoService.deleteWork(newInfoId);
		}
		//保存新业务信息(dealInfoType存在即为更新，dealInfoType1存在即为补办(1：遗失补办，2：损坏更换)，dealInfoType2存在即为变更)
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		WorkCompany workCompany = null;
		WorkUser workUser = null;
		WorkCompanyHis companyHis = null;
		WorkUserHis userHis = null;
		// 保存经办人信息
		workUser = workDealInfo1.getWorkUser();
		workUser.setContactName(contactName);
		workUser.setStatus(1);
		workUser.setConCertType(conCertType);
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactEmail(contacEmail);
		workUser.setContactPhone(contactPhone);
		workUser.setContactTel(contactTel);
		workUserService.save(workUser);
		userHis = workUserService.change(workUser, companyHis);
		workUserHisService.save(userHis);
		
		
		if (dealInfoType2 != null) {
			//变更业务保存单位信息
			workCompany = workDealInfo1.getWorkCompany();
			workCompany.setComCertificateType(comCertificateType);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Timestamp ts1 = new Timestamp(System.currentTimeMillis());
			try {
				ts = Timestamp.valueOf(comCertficateTime+" 00:00:00");
				ts1 = Timestamp.valueOf(orgExpirationTime+" 00:00:00");
			} catch (Exception e) {
				// TODO: handle exception
			}
			//4月17号修改，变更业务可变更单位名称以及组织机构代码
			workCompany.setCompanyName(companyName);
			workCompany.setOrganizationNumber(organizationNumber);
			
			workCompany.setComCertficateTime(ts);
			workCompany.setComCertficateNumber(comCertficateNumber);
			workCompany.setSelectLv(selectLv);
			workCompany.setOrgExpirationTime(ts1);
			workCompany.setProvince(s_province);
			workCompany.setLegalName(legalName);
			workCompany.setCity(s_city);
			workCompany.setDistrict(s_county);
			workCompany.setAddress(address);
			workCompany.setCompanyMobile(companyMobile);
			workCompany.setRemarks(remarks);
			workCompany.setAreaRemark(areaRemark);
			workCompanyService.save(workCompany);
			companyHis = workCompanyService.change(workCompany);
			workCompanyHisService.save(companyHis);
			
			
		} else {
			workUser = workDealInfo1.getWorkUser();
			workCompany = workDealInfo1.getWorkCompany();
			userHis = workDealInfo1.getWorkUserHis();
			companyHis = workDealInfo1.getWorkCompanyHis();
		}
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUserHis(userHis);
		workDealInfo.setWorkCompanyHis(companyHis);
		workDealInfo.setConfigProduct(workDealInfo1.getConfigProduct());
		if (year == null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
		}
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		if (dealInfoType != null) {
			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
		} 
		if (dealInfoType1 != null) {
			if (dealInfoType1 == 1) {
				workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_LOST_CHILD);
			} 
			if (dealInfoType1 == 2) {
				workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
			}
		}
		if (dealInfoType2 != null) {
			workDealInfo.setDealInfoType2(WorkDealInfoType.TYPE_INFORMATION_REROUTE);
		}
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		workDealInfo.setPrevId(workDealInfo1.getId());
		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}

		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo()
				.getWorkCertApplyInfo();
		if (dealInfoType2 != null) {
			// 保存申请人信息
			if (workDealInfo.getConfigProduct().getProductName()
					.equals("2")
					|| workDealInfo.getConfigProduct().getProductName()
							.equals("3")) {
				workCertApplyInfo = workDealInfo1
						.getWorkCertInfo().getWorkCertApplyInfo();
				workCertApplyInfo.setName(pName);
				workCertApplyInfo.setEmail(pEmail);
				workCertApplyInfo.setIdCard(pIDCard);
				workCertApplyInfo.setProvince(workCompany.getProvince());
				workCertApplyInfo.setCity(workCompany.getCity());
				workCertApplyInfoService.save(workCertApplyInfo);
			} else {
				workCertApplyInfo = workDealInfo1
						.getWorkCertInfo().getWorkCertApplyInfo();
				workCertApplyInfo.setName(workCompany.getCompanyName());
				workCertApplyInfo.setEmail(workUser.getContactEmail());
				workCertApplyInfo.setMobilePhone(workCompany.getCompanyMobile());
				workCertApplyInfo.setProvince(workCompany.getProvince());
				workCertApplyInfo.setCity(workCompany.getCity());
				workCertApplyInfoService.save(workCertApplyInfo);

			}
		} 
		
		
		
		
		
		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfoService.delete(workDealInfo1.getId());
		workDealInfo.setPayType(payType);
		workDealInfoService.save(workDealInfo);
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}
	
	
	
	
	/*
	 * 信息变更界面保存方法
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveChange" , method = RequestMethod.POST)
	public String maintainSaveChange(Long workDealInfoId, String orgExpirationTime,
			String selectLv, String comCertificateType, String organizationNumber,
			String comCertficateNumber, String comCertficateTime, String companyName,String twoLevelCompanyName,String companyType,
			String legalName, String s_province, String s_city,
			Integer certSort,
			String s_county, String address, String companyMobile,
			String remarks, Integer workType, String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel, String recordContent,
			@RequestParam(value="product", required=true)Long productId,
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			Integer dealInfoType2,Integer dealInfoType1,Boolean manMadeDamage,
			Model model, String pName, String pEmail, String pIDCard,String contactSex,String areaRemark,
		 Long newInfoId, RedirectAttributes redirectAttributes,
		 @RequestParam(value="companyImage", required=false) MultipartFile companyImage ,
		 @RequestParam(value="transactorImage", required=false) MultipartFile transactorImage,
		 HttpServletRequest request
			) {
		//是否返回造成业务重复，如重复则删除
		if (newInfoId != null) {
			workDealInfoService.deleteWork(newInfoId);
		}
		//保存新业务信息(dealInfoType存在即为更新，dealInfoType1存在即为补办(1：遗失补办，2：损坏更换)，dealInfoType2存在即为变更)
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
		WorkCompany workCompany = null;
		WorkUser workUser = null;
		WorkCompanyHis companyHis = null;
		WorkUserHis userHis = null;

			//变更业务保存单位信息
			workCompany = new WorkCompany();
			workCompany.setComCertificateType(comCertificateType);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Timestamp ts1 = new Timestamp(System.currentTimeMillis());
			try {
				ts = Timestamp.valueOf(comCertficateTime+" 00:00:00");
				ts1 = Timestamp.valueOf(orgExpirationTime+" 00:00:00");
			} catch (Exception e) {
				// TODO: handle exception
			}
			//4月17号修改，变更业务可变更单位名称以及组织机构代码
			workCompany.setCompanyName(companyName);
			workCompany.setTwoLevelCompanyName(twoLevelCompanyName);
			workCompany.setOrganizationNumber(organizationNumber);
			workCompany.setCompanyType(companyType);
			
			workCompany.setComCertficateTime(ts);
			workCompany.setComCertficateNumber(comCertficateNumber);
			workCompany.setSelectLv(selectLv);
			workCompany.setOrgExpirationTime(ts1);
			workCompany.setProvince(s_province);
			workCompany.setLegalName(legalName);
			workCompany.setCity(s_city);
			workCompany.setDistrict(s_county);
			workCompany.setAddress(address);
			workCompany.setCompanyMobile(companyMobile);
			workCompany.setRemarks(remarks);
			workCompany.setAreaRemark(areaRemark);
			workCompanyService.save(workCompany);
			companyHis = workCompanyService.change(workCompany);
			workCompanyHisService.save(companyHis);
		
			// 保存经办人信息
			workUser = new WorkUser();
			workUser.setWorkCompany(workCompany);
			workUser.setContactName(contactName);
			workUser.setStatus(1);
			workUser.setConCertType(conCertType);
			workUser.setConCertNumber(conCertNumber);
			workUser.setContactEmail(contacEmail);
			workUser.setContactPhone(contactPhone);
			workUser.setContactTel(contactTel);
			workUser.setContactSex(contactSex);
			workUserService.save(workUser);
			userHis = workUserService.change(workUser, companyHis);
			workUserHisService.save(userHis);
	
			//workDealInfo1
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		workDealInfo.setCertSort(certSort);
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUserHis(userHis);
		workDealInfo.setWorkCompanyHis(companyHis);
		
		ConfigProduct configProduct = configProductService.get(productId);
		
		workDealInfo.setConfigProduct(configProduct);
		workDealInfo.setYear(0);
	
		
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		
		workDealInfo.setDealInfoType2(WorkDealInfoType.TYPE_INFORMATION_REROUTE);
		
		if (dealInfoType1 != null) {
			if (dealInfoType1 == 1) {
				workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_LOST_CHILD);
			} 
			if (dealInfoType1 == 2) {
				workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
			}
		}
		
		if (manMadeDamage!=null) {
			workDealInfo.setManMadeDamage(manMadeDamage);
		}
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		workDealInfo.setPrevId(workDealInfo1.getId());
		if (workDealInfo1.getWorkCertInfo().getNotafter()!=null&&workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}
		WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		workCertApplyInfoService.save(workCertApplyInfo);
		
		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfoService.delete(workDealInfo1.getId());
		workDealInfo.setPayType(workDealInfo1.getPayType());
		
		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo.setPayType(agentId);
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		if (companyImage!=null || transactorImage!=null) {
			SelfImage image = new SelfImage();
			String companyImageName = saveToux(companyImage,"company", workDealInfo.getWorkCompany().getCompanyType());
			if (companyImageName !=null ) {
				image.setCompanyImage(companyImageName);
			}else{
				
				if (workDealInfo1.getSelfImage()!=null) {
					image.setCompanyImage(workDealInfo1.getSelfImage().getCompanyImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setCompanyImage(selfImage.getCompanyImage());
				}
				
			}
			
			
			String transactorImageName = saveToux(transactorImage,"yourSelf", workDealInfo.getWorkUser().getConCertType());
			if(transactorImageName!=null){
				image.setTransactorImage(transactorImageName);
			}else{
				if(workDealInfo1.getSelfImage()!=null){
					
					image.setTransactorImage(workDealInfo1.getSelfImage().getTransactorImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setTransactorImage(selfImage.getTransactorImage());
				}
				
			}
			image.setCreatedate(new Date());
			image.setStatus(BaseEntity.SHOW);
			selfImageService.save(image);
			workDealInfo.setSelfImage(image);
		}
		workDealInfoService.save(workDealInfo);
		
		String imgNames=request.getParameter("imgNames");     
		List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
		//把以前查询出来
		Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
		for(CommonAttach c:befor){
			map.put(c.getAttachName(), c);
		}
		
		
		
		//
		
		
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			
			CommonAttach attach = null;
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					CommonAttach cattach = new CommonAttach(comm);
					cattach.setWorkDealInfo(workDealInfo);
					cattach.setStatus(null);
					attachService.saveAttach(cattach);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						attach.setWorkDealInfo(workDealInfo);
						attach.setStatus(null);
						attachService.saveAttach(attach);
					}
				}
			}
			/*for(String s:map.keySet()){
				CommonAttach comm = map.get(s);
				comm.setStatus(-1);
				attachService.saveAttach(comm);
			}*/
		}	
		
		
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		
		
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}
	
	/*
	 * 信息补录保存方法
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainAddSave" , method = RequestMethod.POST)
	public String maintainAddSave(Long workDealInfoId, String recordContent,
			Model model,RedirectAttributes redirectAttributes,
		 HttpServletRequest request
			) {

		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		
		String imgNames=request.getParameter("imgNames");     
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			
			CommonAttach attach = null;
			//把以前查询出来
			List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
			Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
			for(CommonAttach c:befor){
				map.put(c.getAttachName(), c);
			}
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					comm.setWorkDealInfo(workDealInfo);
					comm.setStatus(null);
					attachService.saveAttach(comm);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						attach.setWorkDealInfo(workDealInfo);
						attach.setStatus(-2);
						attach.setRemark("补录图片");
						attachService.saveAttach(attach);
					}
				}
			}
		}	
		
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/list";
	}
	
	/**
     * 保存证件图片
     * 
     * @param file
     */
    private String saveToux(MultipartFile file,String type ,String name) {
        try {
        	
        	if(file.getSize()<1){
        		return null;
        	}
        	
            String path = Global.getConfig("images.url");
            File saveFile = new File(path);
            String nameType = "";
            // 如果目录不存在就创建
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if(type.equalsIgnoreCase("company")){
            	 if(name.equalsIgnoreCase("1")){
                     nameType= "QY-";
                 }else if(name.equalsIgnoreCase("2")){
                     nameType= "SY-";
                 }else if(name.equalsIgnoreCase("4")){
                     nameType= "SH-";
                 }else if(name.equalsIgnoreCase("5")){
                     nameType= "QT-";
                 }
            }else if(type.equalsIgnoreCase("yourSelf")){
            	 if(name.equalsIgnoreCase("0")){
                     nameType= "SFZ-";
                 }else if(name.equalsIgnoreCase("1")){
                     nameType= "JGZ-";
                 }else if(name.equalsIgnoreCase("2")){
                     nameType= "QT-";
                 }
            }
          
            InputStream is = file.getInputStream();
            String fileName = file.getOriginalFilename();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timeS = df.format(new Date());
            fileName = nameType + timeS + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
            byte[] buffer = new byte[1024 * 1024];
            int byteread = 0;
            while ((byteread = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteread);
                fos.flush();
            }
            fos.close();
            is.close();
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	
	
	
	
	/*
	 * 遗失补办界面保存方法
	 * 
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveLost")
	public String maintainSaveLost(Long workDealInfoId, 			
			
			Integer certSort,
			
			String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel,String contactSex, String recordContent,
			@RequestParam(value="product", required=true) Long productId,
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			Integer dealInfoType1,
			/*Integer isKeyUpgrade,*/
			Boolean manMadeDamage,Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value="companyImage", required=false) MultipartFile companyImage ,
			 @RequestParam(value="transactorImage", required=false) MultipartFile transactorImage,
			 HttpServletRequest request
			) {
		
		
		
		
		//保存新业务信息(dealInfoType存在即为更新，dealInfoType1存在即为补办(1：遗失补办，2：损坏更换)，dealInfoType2存在即为变更)
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
		
		WorkCompany workCompany = null;
		WorkUser workUser = null;
		WorkCompanyHis companyHis = null;
		WorkUserHis userHis = null;
		

			//变更业务保存单位信息
			workCompany = workDealInfo1.getWorkCompany();
			workCompanyService.save(workCompany);
			companyHis = workCompanyService.change(workCompany);
			workCompanyHisService.save(companyHis);
		
			
			
			// 保存经办人信息
			workUser = new WorkUser();
			WorkUser oldUser = workDealInfo1.getWorkUser(); 
			
			if (contactName!=null&&!contactName.equals("")) {
				workUser.setContactName(contactName);
			}else{
				workUser.setContactName(oldUser.getContactName());
			}
			workUser.setStatus(1);
			if (conCertType!=null&&!conCertType.equals("")) {
				workUser.setConCertType(conCertType);
			}else{
				workUser.setContactName(oldUser.getConCertType());
			}
			if (conCertNumber!=null&&!conCertNumber.equals("")) {
				workUser.setConCertNumber(conCertNumber);
			}else{
				workUser.setConCertNumber(oldUser.getConCertNumber());
			}
			if (contactPhone!=null&&!contactPhone.equals("")) {
				workUser.setContactPhone(contactPhone);
			}else{
				workUser.setContactPhone(oldUser.getContactPhone());
			}
			if (contactTel!=null&&!contactTel.equals("")) {
				workUser.setContactTel(contactTel);
			}else{
				workUser.setContactTel(oldUser.getContactTel());
			}
			if (contactSex!=null&&!contactSex.equals("")) {
				workUser.setContactSex(contactSex);
			}else{
				workUser.setContactSex(oldUser.getContactSex());
			}
			workUser.setContactEmail(contacEmail);
			workUser.setWorkCompany(workCompany);
			workUserService.save(workUser);
			userHis = workUserService.change(workUser, companyHis);
			workUserHisService.save(userHis);
			
			
			
			//workDealInfo1
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		workDealInfo.setCertSort(certSort);
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUserHis(userHis);
		workDealInfo.setWorkCompanyHis(companyHis);
		
		ConfigProduct configProduct = configProductService.get(productId);
		
		
		workDealInfo.setConfigProduct(configProduct);
		workDealInfo.setYear(0);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		
		if (dealInfoType1==1) {
			workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_LOST_CHILD);
		}else if(dealInfoType1==2){
			workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
		}
		
	/*	//key升级
		if(isKeyUpgrade!=null&&isKeyUpgrade == 1){
			workDealInfo.setIsKeyUpgrade(1);
		}*/
		
		
		if (manMadeDamage!=null) {
			workDealInfo.setManMadeDamage(manMadeDamage);
		}
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		workDealInfo.setPrevId(workDealInfo1.getId());
		if (workDealInfo1.getWorkCertInfo().getNotafter()!=null&&workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}
		//保存经办人信息
		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		workCertApplyInfoService.save(workCertApplyInfo);
		
		
		
		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfoService.delete(workDealInfo1.getId());
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo.setPayType(agentId);
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		
		if (companyImage!=null || transactorImage!=null) {
			SelfImage image = new SelfImage();
			String companyImageName = saveToux(companyImage, "company",workDealInfo.getWorkCompany().getCompanyType());
			if (companyImageName !=null ) {
				image.setCompanyImage(companyImageName);
			}else{
				
				if (workDealInfo1.getSelfImage()!=null) {
					image.setCompanyImage(workDealInfo1.getSelfImage().getCompanyImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setCompanyImage(selfImage.getCompanyImage());
				}
				
			}
			
			
			String transactorImageName = saveToux(transactorImage, "yourSelf",workDealInfo.getWorkUser().getConCertType());
			if(transactorImageName!=null){
				image.setTransactorImage(transactorImageName);
			}else{
				if(workDealInfo1.getSelfImage()!=null){
					
					image.setTransactorImage(workDealInfo1.getSelfImage().getTransactorImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setTransactorImage(selfImage.getTransactorImage());
				}
				
			}
			image.setCreatedate(new Date());
			image.setStatus(BaseEntity.SHOW);
			selfImageService.save(image);
			workDealInfo.setSelfImage(image);
		}
		
		
		workDealInfoService.save(workDealInfo);
		
		String imgNames=request.getParameter("imgNames");     
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			
			CommonAttach attach = null;
			//把以前查询出来
			List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
			Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
			for(CommonAttach c:befor){
				map.put(c.getAttachName(), c);
			}
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					CommonAttach cattach = new CommonAttach(comm);
					cattach.setWorkDealInfo(workDealInfo);
					cattach.setStatus(null);
					attachService.saveAttach(cattach);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						attach.setWorkDealInfo(workDealInfo);
						attach.setStatus(null);
						attachService.saveAttach(attach);
					}
				}
			}
			/*for(String s:map.keySet()){
				CommonAttach comm = map.get(s);
				comm.setStatus(-1);
				attachService.saveAttach(comm);
			}*/
		}	
		
				
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}
	
	
	
	/*
	 * 信息变更界面保存方法
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveUpdate")
	public String maintainSaveUpdate(Long workDealInfoId, 
			String companyName,String twoLevelCompanyName,String companyType,String organizationNumber,String orgExpirationTime,String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime,String legalName, String s_province, String s_city,
			String s_county, String areaRemark,String address, String companyMobile,String remarks, 
			@RequestParam(value="product", required=true) Long productId ,
			Integer year,Date expirationDate,//经信委
			Integer certSort,
			Integer dealInfoType,Integer dealInfoType1,Integer dealInfoType2 , 
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			String contactName,String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel,String contactSex, Boolean manMadeDamage,
			Model model, String recordContent
			,String pName , String pIDCard , String pEmail
			, RedirectAttributes redirectAttributes,
			@RequestParam(value="companyImage", required=false) MultipartFile companyImage ,
			 @RequestParam(value="transactorImage", required=false) MultipartFile transactorImage,
			 HttpServletRequest request
			) {
		
		long start = System.currentTimeMillis();
		
		//保存新业务信息(dealInfoType存在即为更新，dealInfoType1存在即为补办(1：遗失补办，2：损坏更换)，dealInfoType2存在即为变更)
		
		long start1 = System.currentTimeMillis();
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		long end1 = System.currentTimeMillis();
		log.debug("更新业务获取父workDealInfo用时:"+(end1-start1)+"ms");
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
		WorkCompanyHis companyHis = null;
		

			//变更业务保存单位信息
		start1 = System.currentTimeMillis();
		WorkCompany workCompany = new WorkCompany();
		WorkCompany oldWorkCompany = workDealInfo1.getWorkCompany();
		
		if(companyName!=null && !companyName.equals("")){
			workCompany.setCompanyName(companyName);
		}else{
			workCompany.setCompanyName(oldWorkCompany.getCompanyName());
		}
		if(twoLevelCompanyName!=null && !twoLevelCompanyName.equals("")){
			workCompany.setTwoLevelCompanyName(twoLevelCompanyName);
		}else{
			workCompany.setTwoLevelCompanyName(oldWorkCompany.getTwoLevelCompanyName());
		}
		if(companyType!=null && !companyType.equals("")){
			workCompany.setCompanyType(companyType);
		}else{
			workCompany.setCompanyType(oldWorkCompany.getCompanyType());
		}
		if(organizationNumber!=null && !organizationNumber.equals("")){
			workCompany.setOrganizationNumber(organizationNumber);
		}else{
			workCompany.setOrganizationNumber(oldWorkCompany.getOrganizationNumber());
		}
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(comCertficateTime+" 00:00:00");
			ts1 = Timestamp.valueOf(orgExpirationTime+" 00:00:00");
		} catch (Exception e) {
			// TODO: handle exception
		}

		workCompany.setOrgExpirationTime(ts1);
		workCompany.setComCertficateTime(ts);
		
		if(selectLv!=null && !selectLv.equals("")){
			workCompany.setSelectLv(selectLv);
		}
		if(comCertificateType!=null && !comCertificateType.equals("")){
			workCompany.setComCertificateType(comCertificateType);
		}
		if(comCertficateNumber!=null && !comCertficateNumber.equals("")){
			workCompany.setComCertficateNumber(comCertficateNumber);
		}
		if(legalName!=null && !legalName.equals("")){
			workCompany.setLegalName(legalName);
		}
		if(s_province!=null && !s_province.equals("")){
			workCompany.setProvince(s_province);
		}
		if(s_city!=null && !s_city.equals("")){
			workCompany.setCity(s_city);
		}
		if(s_county!=null && !s_county.equals("")){
			workCompany.setDistrict(s_county);
		}	
		if(areaRemark!=null && !areaRemark.equals("")){
			workCompany.setAreaRemark(areaRemark);
		}	
		if(address!=null && !address.equals("")){
			workCompany.setAddress(address);
		}
		if(address!=null && !address.equals("")){
			workCompany.setCompanyMobile(companyMobile);
		}
		if(remarks!=null && !remarks.equals("")){
			workCompany.setRemarks(remarks);
		}
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装workCompany用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		workCompanyService.save(workCompany);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交workCompany用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		companyHis = workCompanyService.change(workCompany);
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装WorkCompanyHis用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		workCompanyHisService.save(companyHis);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交WorkCompanyHis用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		// 保存经办人信息
				WorkUser workUser = workDealInfo1.getWorkUser();
				workUser = new WorkUser();
				WorkUser oldUser = workDealInfo1.getWorkUser(); 
				
				if (contactName!=null&&!contactName.equals("")) {
					workUser.setContactName(contactName);
				}else{
					workUser.setContactName(oldUser.getContactName());
				}
				workUser.setStatus(1);
				if (conCertType!=null&&!conCertType.equals("")) {
					workUser.setConCertType(conCertType);
				}else{
					workUser.setContactName(oldUser.getConCertType());
				}
				if (conCertNumber!=null&&!conCertNumber.equals("")) {
					workUser.setConCertNumber(conCertNumber);
				}else{
					workUser.setConCertNumber(oldUser.getConCertNumber());
				}
				if (contactPhone!=null&&!contactPhone.equals("")) {
					workUser.setContactPhone(contactPhone);
				}else{
					workUser.setContactPhone(oldUser.getContactPhone());
				}
				if (contactTel!=null&&!contactTel.equals("")) {
					workUser.setContactTel(contactTel);
				}else{
					workUser.setContactTel(oldUser.getContactTel());
				}
				if (contactSex!=null&&!contactSex.equals("")) {
					workUser.setContactSex(contactSex);
				}else{
					workUser.setContactSex(oldUser.getContactSex());
				}
				if (contacEmail!=null&&!contacEmail.equals("")) {
					workUser.setContactEmail(contacEmail);
				}else{
					workUser.setContactEmail(oldUser.getContactEmail());;
				}
				workUser.setWorkCompany(workCompany);
				end1 = System.currentTimeMillis();
				log.debug("更新业务组装WorkUser用时:"+(end1-start1)+"ms");
				
				start1 = System.currentTimeMillis();
				workUserService.save(workUser);
				end1 = System.currentTimeMillis();
				log.debug("更新业务提交workUser用时:"+(end1-start1)+"ms");
				
				start1 = System.currentTimeMillis();
				WorkUserHis userHis = workUserService.change(workUser, companyHis);
				end1 = System.currentTimeMillis();
				log.debug("更新业务组装WorkUserHis用时:"+(end1-start1)+"ms");
				
				start1 = System.currentTimeMillis();
				workUserHisService.save(userHis);
		        end1 = System.currentTimeMillis();
		        log.debug("更新业务提交WorkUserHis用时:"+(end1-start1)+"ms");
		
			//workDealInfo1
		start1 = System.currentTimeMillis();        
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		workDealInfo.setCertSort(certSort);
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		

		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo.setPayType(agentId);
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装workDealInfo第一部分用时:"+(end1-start1)+"ms");
		
		
		start1 = System.currentTimeMillis();
		ConfigChargeAgent agent = bound.getAgent();
		
		Integer reseUpdateNum = agent.getReserveUpdateNum();
		Integer surUpdateNum = agent.getSurplusUpdateNum();
		agent.setReserveUpdateNum(reseUpdateNum+1);
		agent.setSurplusUpdateNum(surUpdateNum-1);
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装ConfigChargeAgent用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		configChargeAgentService.save(agent);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交ConfigChargeAgent用时:"+(end1-start1)+"ms");
		
		
		start1 = System.currentTimeMillis();
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
	
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUserHis(userHis);
		workDealInfo.setWorkCompanyHis(companyHis);
		
		ConfigProduct configProduct = configProductService.get(productId);
		
		workDealInfo.setConfigProduct(configProduct);
	
		//经信委
		if(year!=null&&expirationDate==null){
				workDealInfo.setYear(year);
		}else{
				workDealInfo.setYear(StringHelper.getDvalueYear(expirationDate));
				workDealInfo.setExpirationDate(expirationDate);
		}
	
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		
		if (dealInfoType != null) {
			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
		}
		if (dealInfoType1 != null) {
			if (dealInfoType1 == 1) {
				workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_LOST_CHILD);
			} 
			if (dealInfoType1 == 2) {
				workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
			}
		}
		if (dealInfoType2 != null) {
			workDealInfo.setDealInfoType2(WorkDealInfoType.TYPE_INFORMATION_REROUTE);
		}

		if (manMadeDamage!=null) {
			workDealInfo.setManMadeDamage(manMadeDamage);
		}
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		workDealInfo.setPrevId(workDealInfo1.getId());
		if (workDealInfo1.getWorkCertInfo().getNotafter()!=null&&workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装workDealInfo第二部分用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
		WorkCertApplyInfo oldWorkCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
		
		
		if (pName!=null && !pName.equals("")) {
			workCertApplyInfo.setName(pName);
		}else{
			workCertApplyInfo.setName(oldWorkCertApplyInfo.getName());
		}
		if (pEmail!=null && !pEmail.equals("")) {
			workCertApplyInfo.setEmail(pEmail);
		}else{
			workCertApplyInfo.setEmail(oldWorkCertApplyInfo.getEmail());
		}
		if (pIDCard!=null && !pIDCard.equals("")) {
			workCertApplyInfo.setIdCard(pIDCard);
		}else{
			workCertApplyInfo.setIdCard(oldWorkCertApplyInfo.getIdCard());
		}
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装workCertApplyInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		workCertApplyInfoService.save(workCertApplyInfo);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交workCertApplyInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装workCertInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		workCertInfoService.save(workCertInfo);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交workCertInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装父workCertInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		workCertInfoService.save(oldCertInfo);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交父workCertInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfoService.delete(workDealInfo1.getId());
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装workDealInfo第三部分用时:"+(end1-start1)+"ms");
		
		if (companyImage!=null || transactorImage!=null) {
			start1 = System.currentTimeMillis();
			SelfImage image = new SelfImage();
			String companyImageName = saveToux(companyImage,"company", workDealInfo.getWorkCompany().getCompanyType());
			if (companyImageName !=null ) {
				image.setCompanyImage(companyImageName);
			}else{
				
				if (workDealInfo1.getSelfImage()!=null) {
					image.setCompanyImage(workDealInfo1.getSelfImage().getCompanyImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setCompanyImage(selfImage.getCompanyImage());
				}
				
			}
			
			
			String transactorImageName = saveToux(transactorImage, "yourSelf",workDealInfo.getWorkUser().getConCertType());
			if(transactorImageName!=null){
				image.setTransactorImage(transactorImageName);
			}else{
				if(workDealInfo1.getSelfImage()!=null){
					
					image.setTransactorImage(workDealInfo1.getSelfImage().getTransactorImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setTransactorImage(selfImage.getTransactorImage());
				}
				
			}
			image.setCreatedate(new Date());
			image.setStatus(BaseEntity.SHOW);
			selfImageService.save(image);
			workDealInfo.setSelfImage(image);
			end1 = System.currentTimeMillis();
			log.debug("更新业务组装workDealInfo图片部分用时:"+(end1-start1)+"ms");
		}
		
		start1 = System.currentTimeMillis();
		workDealInfoService.save(workDealInfo);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交workDealInfo用时:"+(end1-start1)+"ms");
		
		start1 = System.currentTimeMillis();
		ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
		dealInfoBound.setDealInfo(workDealInfo);
		dealInfoBound.setAgent(agent);
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装ConfigAgentBoundDealInfo用时:"+(end1-start1)+"ms");
		start1 = System.currentTimeMillis();
		configAgentBoundDealInfoService.save(dealInfoBound);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交ConfigAgentBoundDealInfo用时:"+(end1-start1)+"ms");
		
		String imgNames=request.getParameter("imgNames");     
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			
			CommonAttach attach = null;
			//把以前查询出来
			List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
			Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
			for(CommonAttach c:befor){
				map.put(c.getAttachName(), c);
			}
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					CommonAttach cattach = new CommonAttach(comm);
					cattach.setWorkDealInfo(workDealInfo);
					cattach.setStatus(null);
					attachService.saveAttach(cattach);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						attach.setWorkDealInfo(workDealInfo);
						attach.setStatus(null);
						attachService.saveAttach(attach);
					}
				}
			}
			/*for(String s:map.keySet()){
				CommonAttach comm = map.get(s);
				comm.setStatus(-1);
				attachService.saveAttach(comm);
			}*/
		}	
		
		logUtil.saveSysLog("计费策略模版", "计费策略模版："+agent.getId()+"--业务编号："+workDealInfo.getId()+"--关联成功!", "");
		
		
		
		// 保存日志信息
		start1 = System.currentTimeMillis();
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		end1 = System.currentTimeMillis();
		log.debug("更新业务组装WorkLog用时:"+(end1-start1)+"ms");
		start1 = System.currentTimeMillis();
		workLogService.save(workLog);
		end1 = System.currentTimeMillis();
		log.debug("更新业务提交WorkLog用时:"+(end1-start1)+"ms");
		
		long end = System.currentTimeMillis();
		log.debug("更新业务提交总用时:"+(end-start)+"ms======业务ID:"+workDealInfo.getId());
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}
	
	
	
	
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveReturnUpdate")
	public String maintainSaveReturnUpdate(Long workDealInfoId, 
			
			String companyName,String companyType,String organizationNumber,String orgExpirationTime,String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime,String legalName, String s_province, String s_city,
			String s_county, String areaRemark,String address, String companyMobile,String remarks, 
			
			Integer year, Date expirationDate,  //经信委到期时间 和年限二选一 
			Integer dealInfoType,Integer dealInfoType1,Integer dealInfoType2 , 
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			String contactName,String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel,String contactSex, Boolean manMadeDamage,
			Model model, String recordContent
			,String pName , String pIDCard , String pEmail
			, RedirectAttributes redirectAttributes,
			 @RequestParam(value="companyImage", required=false) MultipartFile companyImage ,
			 @RequestParam(value="transactorImage", required=false) MultipartFile transactorImage,
			 HttpServletRequest request
			) {
		//保存新业务信息(dealInfoType存在即为更新，dealInfoType1存在即为补办(1：遗失补办，2：损坏更换)，dealInfoType2存在即为变更)
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		if (!workDealInfo1.getConfigChargeAgentId().equals(bound.getAgent().getId())) {
			ConfigChargeAgent agentOri =  configChargeAgentService.get(workDealInfo1.getConfigChargeAgentId());
			agentOri.setSurplusNum(agentOri.getSurplusNum()+1);
			agentOri.setReserveNum(agentOri.getReserveNum()-1);
			configChargeAgentService.save(agentOri);
			
			ConfigAgentBoundDealInfo boundOri = 
					configAgentBoundDealInfoService.findByAgentIdDealId(workDealInfo1.getConfigChargeAgentId(),workDealInfo1.getId());
			if (boundOri!=null) {
				configAgentBoundDealInfoService.deleteById(boundOri.getId());
			}
			ConfigChargeAgent agent = bound.getAgent();
			agent.setReserveNum(agent.getReserveNum()+1);
			agent.setSurplusNum(agent.getSurplusNum()-1);
			configChargeAgentService.save(agent);
			ConfigAgentBoundDealInfo boundDealNow = new ConfigAgentBoundDealInfo();
			boundDealNow.setAgent(bound.getAgent());
			boundDealNow.setDealInfo(workDealInfo1);
			configAgentBoundDealInfoService.save(boundDealNow);
		}
		
		workDealInfo1.setPayType(agentId);
		workDealInfo1.setConfigChargeAgentId(bound.getAgent().getId());
		
		
		WorkCompanyHis companyHis = null;
		// 保存经办人信息
		WorkUser workUser = workDealInfo1.getWorkUser();
		if (contactName!=null&&!contactName.equals("")) {
			workUser.setContactName(contactName);
		}
		workUser.setStatus(1);
		if (conCertType!=null&&!conCertType.equals("")) {
			workUser.setConCertType(conCertType);
		}
		if (conCertNumber!=null&&!conCertNumber.equals("")) {
			workUser.setConCertNumber(conCertNumber);
		}
		workUser.setContactEmail(contacEmail);
		if (contactPhone!=null&&!contactPhone.equals("")) {
			workUser.setContactPhone(contactPhone);
		}
		if (contactTel!=null&&!contactTel.equals("")) {
			workUser.setContactTel(contactTel);
		}
		if (contactSex!=null&&!contactSex.equals("")) {
			workUser.setContactSex(contactSex);
		}
		workUserService.save(workUser);
		WorkUserHis userHis = workUserService.change(workUser, companyHis);
		workUserHisService.save(userHis);

			//变更业务保存单位信息
		WorkCompany workCompany = workDealInfo1.getWorkCompany();
		
		if(companyName!=null && !companyName.equals("")){
			workCompany.setCompanyName(companyName);
		}
		if(companyType!=null && !companyType.equals("")){
			workCompany.setCompanyType(companyType);
		}
		if(organizationNumber!=null && !organizationNumber.equals("")){
			workCompany.setOrganizationNumber(organizationNumber);
		}
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(comCertficateTime+" 00:00:00");
			ts1 = Timestamp.valueOf(orgExpirationTime+" 00:00:00");
		} catch (Exception e) {
			// TODO: handle exception
		}

		workCompany.setOrgExpirationTime(ts1);
		workCompany.setComCertficateTime(ts);
		
		if(selectLv!=null && !selectLv.equals("")){
			workCompany.setSelectLv(selectLv);
		}
		if(comCertificateType!=null && !comCertificateType.equals("")){
			workCompany.setComCertificateType(comCertificateType);
		}
		if(comCertficateNumber!=null && !comCertficateNumber.equals("")){
			workCompany.setComCertficateNumber(comCertficateNumber);
		}
		if(legalName!=null && !legalName.equals("")){
			workCompany.setLegalName(legalName);
		}
		if(s_province!=null && !s_province.equals("")){
			workCompany.setProvince(s_province);
		}
		if(s_city!=null && !s_city.equals("")){
			workCompany.setCity(s_city);
		}
		if(s_county!=null && !s_county.equals("")){
			workCompany.setDistrict(s_county);
		}	
		if(areaRemark!=null && !areaRemark.equals("")){
			workCompany.setAreaRemark(areaRemark);
		}	
		if(address!=null && !address.equals("")){
			workCompany.setAddress(address);
		}
		if(address!=null && !address.equals("")){
			workCompany.setCompanyMobile(companyMobile);
		}
		if(remarks!=null && !remarks.equals("")){
			workCompany.setRemarks(remarks);
		}
		workCompanyService.save(workCompany);
		companyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(companyHis);
		
		
		workDealInfo1.setWorkUser(workUser);
		workDealInfo1.setWorkCompany(workCompany);
		workDealInfo1.setWorkUserHis(userHis);
		workDealInfo1.setWorkCompanyHis(companyHis);
	
		//经信委
				if(year!=null&&expirationDate==null){
					workDealInfo1.setYear(year);
					workDealInfo1.setExpirationDate(null);
				}else{
					workDealInfo1.setYear(StringHelper.getDvalueYear(expirationDate));
					workDealInfo1.setExpirationDate(expirationDate);
				}
	
		workDealInfo1.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo1.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
	
		if (manMadeDamage!=null) {
			workDealInfo1.setManMadeDamage(manMadeDamage);
		}
	
//		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
//			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
//					.getNotafter());
//			workDealInfo1.setLastDays(day);
//		} else {
//			workDealInfo1.setLastDays(0);
//		}
		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
		
		
		if (pName!=null && !pName.equals("")) {
			workCertApplyInfo.setName(pName);
		}
		if (pEmail!=null && !pEmail.equals("")) {
			workCertApplyInfo.setEmail(pEmail);
		}            
		if (pIDCard!=null && !pIDCard.equals("")) {
			workCertApplyInfo.setIdCard(pIDCard);
		}
		
		
		
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		workCertApplyInfoService.save(workCertApplyInfo);
		
		
		
		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate( workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo1.setWorkCertInfo(workCertInfo);
		
		//workDealInfo.setPayType(workDealInfo1.getPayType());
		//workDealInfo.setConfigChargeAgentId(workDealInfo1.getConfigChargeAgentId());
		if (companyImage!=null || transactorImage!=null) {
			SelfImage image = new SelfImage();
			String companyImageName = saveToux(companyImage,"company", workDealInfo1.getWorkCompany().getCompanyType());
			if (companyImageName !=null ) {
				image.setCompanyImage(companyImageName);
			}else{
				
				if (workDealInfo1.getSelfImage()!=null) {
					image.setCompanyImage(workDealInfo1.getSelfImage().getCompanyImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setCompanyImage(selfImage.getCompanyImage());
				}
				
			}
			
			
			String transactorImageName = saveToux(transactorImage,"yourSelf", workDealInfo1.getWorkUser().getConCertType());
			if(transactorImageName!=null){
				image.setTransactorImage(transactorImageName);
			}else{
				if(workDealInfo1.getSelfImage()!=null){
					
					image.setTransactorImage(workDealInfo1.getSelfImage().getTransactorImage());
				}else{
					SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo1.getSelfApplyId());
					image.setTransactorImage(selfImage.getTransactorImage());
				}
				
			}
			image.setCreatedate(new Date());
			image.setStatus(BaseEntity.SHOW);
			selfImageService.save(image);
			workDealInfo1.setSelfImage(image);
		}
		
		
		workDealInfoService.save(workDealInfo1);
		String imgNames=request.getParameter("imgNames");     
		List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
		//把以前查询出来
		Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
		for(CommonAttach c:befor){
			map.put(c.getAttachName(), c);
		}
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			CommonAttach attach = null;
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					comm.setWorkDealInfo(workDealInfo1);
					comm.setStatus(null);
					attachService.saveAttach(comm);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						attach.setWorkDealInfo(workDealInfo1);
						attach.setStatus(null);
						attachService.saveAttach(attach);
					}
				}
			}
		}	
		for(String s:map.keySet()){
			CommonAttach comm = map.get(s);
			comm.setStatus(-1);
			attachService.saveAttach(comm);
		}
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo1);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo1.getConfigApp());
		workLog.setWorkCompany(workDealInfo1.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo1.getId();
	}
	
	
	
	
	
	
	
}
