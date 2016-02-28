/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 业务回退所有回退方法Controller
 * 
 * @author ZhaoShaoBo
 * @version 2014-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workDealInfoOperationSed")
public class WorkDealInfoOperationSedController extends BaseController {

	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private WorkUserService workUserService;
	
	@Autowired
	private WorkUserHisService workUserHisService;
	
	@Autowired
	private WorkCompanyService workCompanyService;
	
	@Autowired
	private WorkCompanyHisService workCompanyHisService;
	
	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;
	
	@Autowired
	private WorkLogService workLogService;
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	
	/*
	 * 信息变更界面保存方法
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveReturnChange")
	public String maintainSaveChange(Long workDealInfoId, String orgExpirationTime,
			String selectLv, String comCertificateType, String companyType, String organizationNumber,
			String comCertficateNumber, String comCertficateTime, String companyName,
			String legalName, String s_province, String s_city,
			String s_county, String address, String companyMobile,
			String remarks, String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel, String recordContent,
			Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			Model model, String pName, String pEmail, String pIDCard,String contactSex,String areaRemark,
		 Long newInfoId, RedirectAttributes redirectAttributes) {
		
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
		WorkCompanyHis companyHis = null;
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
		
		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		workCertApplyInfoService.save(workCertApplyInfo);
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo1.setPayType(agentId);
		workDealInfo1.setConfigChargeAgentId(bound.getAgent().getId());
		
		workDealInfo1.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo1.setCreateBy(UserUtils.getUser());
		workDealInfo1.setCreateDate(new Date());
		workDealInfo1.setInputUser(UserUtils.getUser());
		workDealInfo1.setInputUserDate(new Date());
		workDealInfo1.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo1.setOfficeId(UserUtils.getUser().getOffice().getId());
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
	

	/*
	 * 信息变更界面保存方法
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "maintainSaveReturnLost")
	public String maintainSaveReturnLost(Long workDealInfoId, 
			String contactName,
			String conCertType, String contacEmail, String conCertNumber,
			String contactPhone, String contactTel, String contactSex,
			String recordContent,Integer agentId,Long agentDetailId, //获取计费策略类型  获取计费策略模版
			Model model,RedirectAttributes redirectAttributes) {
		
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		if (workDealInfo1.getDelFlag().equals("1")) {
			addMessage(redirectAttributes, "此业务正在办理维护！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}
		
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
		WorkUserHis userHis = workUserService.change(workUser, workDealInfo1.getWorkCompanyHis());
		workUserHisService.save(userHis);
		
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
		workDealInfo1.setPayType(agentId);
		workDealInfo1.setConfigChargeAgentId(bound.getAgent().getId());
		
		workDealInfo1.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo1.setCreateBy(UserUtils.getUser());
		workDealInfo1.setCreateDate(new Date());
		workDealInfo1.setInputUser(UserUtils.getUser());
		workDealInfo1.setInputUserDate(new Date());
		workDealInfo1.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo1.setOfficeId(UserUtils.getUser().getOffice().getId());
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
