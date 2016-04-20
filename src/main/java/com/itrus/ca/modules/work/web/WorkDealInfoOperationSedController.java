/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.service.SelfImageService;
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
	private ConfigRaAccountService raAccountService;
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	
	@Autowired
	private SelfImageService selfImageService;
	
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
		 Long newInfoId, RedirectAttributes redirectAttributes,
		 @RequestParam(value="companyImage", required=false) MultipartFile companyImage ,
		 @RequestParam(value="transactorImage", required=false) MultipartFile transactorImage,
		 HttpServletRequest request
			
			) {
		
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
		
		if (companyImage!=null || transactorImage!=null) {
			SelfImage image = new SelfImage();
			String companyImageName = saveToux(companyImage, "company",workDealInfo1.getWorkCompany().getCompanyType());
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
			
			
			String transactorImageName = saveToux(transactorImage, "yourSelf",workDealInfo1.getWorkUser().getConCertType());
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
			Model model,RedirectAttributes redirectAttributes,
			 @RequestParam(value="companyImage", required=false) MultipartFile companyImage ,
			 @RequestParam(value="transactorImage", required=false) MultipartFile transactorImage,
			 HttpServletRequest request) {
		
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
	
	@RequestMapping("installCert")
	public String installCert(Long id, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		
		if (workDealInfo.getPrevId()!=null) {
			//获取上一张证书的签名证书序列号
			WorkDealInfo oldDealInfo = workDealInfoService.get(workDealInfo.getPrevId());
			try {
				model.addAttribute("signSerialNumber", oldDealInfo.getWorkCertInfo().getSerialnumber().toLowerCase());
			} catch (Exception e) {
			}
		}
		
		return "modules/work/maintain/workDealInfoMakeInstallFail";
	}
	
	
	/**
     * 保存证件图片
     * 
     * @param file
     */
    private String saveToux(MultipartFile file, String type ,String name) {
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
            fileName = name + timeS + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
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
	
	
}
