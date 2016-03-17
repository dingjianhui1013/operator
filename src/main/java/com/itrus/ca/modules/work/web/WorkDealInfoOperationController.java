/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.utils.RaAccountUtil;
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
			addMessage(redirectAttributes, "请到业务办理网点补办！");
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
	public String errorForm(Long id, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
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
		model.addAttribute("proList", ProductType.getProductTypeList());
		return "modules/work/workDealInfoErrorForm";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "errorSave")
	public String errorSave(Model model, RedirectAttributes redirectAttributes,
			Long appId, String product, Integer dealInfType,
			Integer dealInfType1, Integer year, Long workDealInfoId,
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

		if (year==null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
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
	@RequestMapping(value = "maintainSaveChange")
	public String maintainSaveChange(Long workDealInfoId, String orgExpirationTime,
			String selectLv, String comCertificateType, String organizationNumber,
			String comCertficateNumber, String comCertficateTime, String companyName,String companyType,
			String legalName, String s_province, String s_city,
			String s_county, String address, String companyMobile,
			String remarks, Integer workType, String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel, String recordContent,
			
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			Integer dealInfoType2,Integer dealInfoType1,Boolean manMadeDamage,
			
			Model model, String pName, String pEmail, String pIDCard,String contactSex,String areaRemark,
		 Long newInfoId, RedirectAttributes redirectAttributes) {
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
		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
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
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo.setPayType(agentId);
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		
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
	 * 遗失补办界面保存方法
	 * 
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveLost")
	public String maintainSaveLost(Long workDealInfoId, 			
			
			String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel,String contactSex, String recordContent,
			
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			Integer dealInfoType1,
			
			Boolean manMadeDamage,Model model, RedirectAttributes redirectAttributes) {
		
		
		
		
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
		workDealInfo.setYear(0);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		
		if (dealInfoType1==1) {
			workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_LOST_CHILD);
		}else if(dealInfoType1==2){
			workDealInfo.setDealInfoType1(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
		}
		if (manMadeDamage!=null) {
			workDealInfo.setManMadeDamage(manMadeDamage);
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
	@RequestMapping(value = "maintainSaveUpdate")
	public String maintainSaveUpdate(Long workDealInfoId, 
			
			String companyName,String companyType,String organizationNumber,String orgExpirationTime,String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime,String legalName, String s_province, String s_city,
			String s_county, String areaRemark,String address, String companyMobile,String remarks, 
			
			Integer year,
			Integer dealInfoType,Integer dealInfoType1,Integer dealInfoType2 , 
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			String contactName,String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel,String contactSex, Boolean manMadeDamage,
			Model model, String recordContent
			,String pName , String pIDCard , String pEmail
			, RedirectAttributes redirectAttributes
			) {
		//保存新业务信息(dealInfoType存在即为更新，dealInfoType1存在即为补办(1：遗失补办，2：损坏更换)，dealInfoType2存在即为变更)
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
		WorkCompanyHis companyHis = null;
		

			//变更业务保存单位信息
		WorkCompany workCompany = new WorkCompany();
		WorkCompany oldWorkCompany = workDealInfo1.getWorkCompany();
		
		if(companyName!=null && !companyName.equals("")){
			workCompany.setCompanyName(companyName);
		}else{
			workCompany.setCompanyName(oldWorkCompany.getCompanyName());
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
		workCompanyService.save(workCompany);
		companyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(companyHis);
		
		
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
				workUserService.save(workUser);
				WorkUserHis userHis = workUserService.change(workUser, companyHis);
				workUserHisService.save(userHis);
		
		
			//workDealInfo1
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size()>0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());//劳务关系外键
		}
		

		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo.setPayType(agentId);
		
		
		ConfigChargeAgent agent = bound.getAgent();
		
		Integer reseUpdateNum = agent.getReserveUpdateNum();
		Integer surUpdateNum = agent.getSurplusUpdateNum();
		agent.setReserveUpdateNum(reseUpdateNum+1);
		agent.setSurplusUpdateNum(surUpdateNum-1);
		configChargeAgentService.save(agent);
		
		
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		
		
		
		
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

		if (manMadeDamage!=null) {
			workDealInfo.setManMadeDamage(manMadeDamage);
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
		//workDealInfo.setPayType(workDealInfo1.getPayType());
		//workDealInfo.setConfigChargeAgentId(workDealInfo1.getConfigChargeAgentId());
		
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		
		workDealInfoService.save(workDealInfo);
		
		ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
		dealInfoBound.setDealInfo(workDealInfo);
		dealInfoBound.setAgent(agent);
		configAgentBoundDealInfoService.save(dealInfoBound);
		logUtil.saveSysLog("计费策略模版", "计费策略模版："+agent.getId()+"--业务编号："+workDealInfo.getId()+"--关联成功!", "");
		
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
	
	
	
	
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveReturnUpdate")
	public String maintainSaveReturnUpdate(Long workDealInfoId, 
			
			String companyName,String companyType,String organizationNumber,String orgExpirationTime,String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime,String legalName, String s_province, String s_city,
			String s_county, String areaRemark,String address, String companyMobile,String remarks, 
			
			Integer year,
			Integer dealInfoType,Integer dealInfoType1,Integer dealInfoType2 , 
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			String contactName,String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel,String contactSex, Boolean manMadeDamage,
			Model model, String recordContent
			,String pName , String pIDCard , String pEmail
			, RedirectAttributes redirectAttributes
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
	
		if (year == null) {
			workDealInfo1.setYear(0);
		} else {
			workDealInfo1.setYear(year);
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
		workDealInfoService.save(workDealInfo1);
		
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
