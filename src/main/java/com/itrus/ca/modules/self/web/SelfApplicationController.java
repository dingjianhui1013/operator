/**
 */
package com.itrus.ca.modules.self.web;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.self.entity.SelfApplication;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.service.SelfApplicationService;
import com.itrus.ca.modules.self.utils.SelfApplicationStatus;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 申请表Controller
 * 
 * @author HuHao
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/self/selfApplication")
public class SelfApplicationController extends BaseController {

	@Autowired
	private SelfApplicationService selfApplicationService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private ConfigAppService configAppService;
	
    @Autowired
    private ConfigProductService configProductService;

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;
	
	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private WorkCompanyHisService workCompanyHisService;

	@Autowired
	private WorkUserHisService workUserHisService;

	@Autowired
	private WorkLogService workLogService;

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;

	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;

	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;

	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;

	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public SelfApplication get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return selfApplicationService.get(id);
		} else {
			return new SelfApplication();
		}
	}

	@RequiresPermissions("self:selfApplication:view")
	@RequestMapping(value = { "list", "" })
	public String list(SelfApplication selfApplication,Date startTime, Date endTime,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SelfApplication> page = selfApplicationService.find(
				new Page<SelfApplication>(request, response), selfApplication,startTime,endTime);
		model.addAttribute("page", page);
		model.addAttribute("status",selfApplication.getStatus());
		model.addAttribute("receiverType",selfApplication.getReceiverType());
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);
		return "modules/self/selfApplicationList";
	}
	
	@RequiresPermissions("self:selfApplication:view")
	@RequestMapping(value = "form")
	public String form(SelfApplication selfApplication, Model model) {
		Long appId = configAppService.findByAppname(selfApplication.getAppName()).getId();
		model.addAttribute("appId",appId );
		model.addAttribute("imgUrl",Global.getConfig("images.url"));
		 Set<SelfImage> set = selfApplication.getSelfImages();
         for (SelfImage selfImage : set) {
             if (selfImage.getStatus().equalsIgnoreCase("1")) {
                 model.addAttribute("companyImage", selfImage.getCompanyImage());
                 model.addAttribute("transactorImage", selfImage.getTransactorImage());
             }
         }
        WorkDealInfo workDealInfo = workDealInfoService.findApply(selfApplication.getId());
        if(workDealInfo!=null){
        	ConfigChargeAgent agentBound = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
        	model.addAttribute("tempName", agentBound.getTempName());
        	model.addAttribute("tempStyle", agentBound.getTempStyle());
        }
        ConfigApp configApp = configAppService.findByAppname(selfApplication.getAppName());
        model.addAttribute("applyFlag1", configApp.getApplyFlag1() == null ? "" : configApp.getApplyFlag1());
        model.addAttribute("applyFlag2", configApp.getApplyFlag2() == null ? "" : configApp.getApplyFlag2());
        ConfigProduct configProduct = configProductService.findByProductName(configApp.getId(),selfApplication.getProductName());
        model.addAttribute("productLabel", configProduct.getProductLabel());
		model.addAttribute("selfApplication", selfApplication);
		return "modules/self/selfApplicationForm";
	}


	@RequiresPermissions("self:selfApplication:view")
	@RequestMapping(value = "denyApply")
	public String denyApply(SelfApplication selfApplication, Model model,String denyText) {
		selfApplication.setStatus(SelfApplicationStatus.denyApply);
		selfApplication.setDenyText(denyText);
		selfApplicationService.save(selfApplication);
		return "redirect:" + Global.getAdminPath()+ "/self/selfApplication/?repage";
	}

	@RequiresPermissions("self:selfApplication:view")
	@RequestMapping(value = "agreeApply")
	public String agreeApply(SelfApplication selfApplication,Integer agentId, Long agentDetailId) {
		ConfigApp configApp = configAppService.findByAppname(selfApplication
				.getAppName());
		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(agentDetailId);
		ConfigChargeAgentDetail configChargeAgentDetail = configChargeAgentDetailService
				.getAgentDetail(bound.getAgent().getId(), 1, Integer.parseInt(selfApplication.getApplicationPeriod()));
		// 保存单位信息
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(
				selfApplication.getCompanyName(),
				selfApplication.getCompanyNumber());
		workCompany.setCompanyName(selfApplication.getCompanyName());
		workCompany.setCompanyType(selfApplication.getCompanyType());
		if (selfApplication.getCompanyType().equalsIgnoreCase("企业")) {
			workCompany.setCompanyType("1");
		} else if (selfApplication.getCompanyType().equalsIgnoreCase("事业")) {
			workCompany.setCompanyType("2");
		} else if (selfApplication.getCompanyType().equalsIgnoreCase("社会团体")) {
			workCompany.setCompanyType("4");
		} else if (selfApplication.getCompanyType().equalsIgnoreCase("其他")) {
			workCompany.setCompanyType("5");
		}
		if (selfApplication.getCompanyType().equalsIgnoreCase("企业")) {
			workCompany.setComCertificateType("0");
		} else if (selfApplication.getCompanyType().equalsIgnoreCase("事业")) {
			workCompany.setComCertificateType("1");
		} else if (selfApplication.getCompanyType().equalsIgnoreCase("社会团体")) {
			workCompany.setComCertificateType("2");
		} else if (selfApplication.getCompanyType().equalsIgnoreCase("其他")) {
			workCompany.setComCertificateType("3");
		}
		workCompany.setComCertficateNumber(selfApplication
				.getCompanyTypeNumber());
		workCompany.setOrganizationNumber(selfApplication.getCompanyNumber());
		workCompany.setRemarks(selfApplication.getRemarks());
		// 保存经办人信息
		WorkUser workUser = new WorkUser();
		workUser.setStatus(1);
		workUser.setContactName(selfApplication.getTransactorName());
		if(selfApplication.getTransactorDocumentType().equals("身份证")){
			workUser.setConCertType("0");
		}else if(selfApplication.getTransactorDocumentType().equals("军官证")){
			workUser.setConCertType("1");
		}else{
			workUser.setConCertType("2");
		}
		workUser.setConCertNumber(selfApplication.getTransactorDocumentNumber());
		workUser.setContactEmail(selfApplication.getTransactorEmail());
		workUser.setContactPhone(selfApplication.getTransactorPhone());
		workUser.setWorkCompany(workCompany);
		workCompanyService.save(workCompany);
		workUserService.save(workUser);
		// 保存work_deal-info
		WorkDealInfo workDealInfo = new WorkDealInfo();
		ConfigProduct configProduct = bound.getProduct();
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
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
		if(selfApplication.getDealId()!=null){
			WorkDealInfo workDealInfoHis  = workDealInfoService.get(selfApplication.getDealId());
			workDealInfoHis.setDelFlag(BaseEntity.SHOW);
			workDealInfoService.save(workDealInfoHis);
			workDealInfo.setPrevId(selfApplication.getDealId());
		}
		// 新增时扣减计费策略数量 
		ConfigChargeAgent agent = bound.getAgent();
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		if(selfApplication.getBusinessType().equalsIgnoreCase(SelfApplicationStatus.renovateApply)){
			//更新
			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
		}else if(selfApplication.getBusinessType().equalsIgnoreCase(SelfApplicationStatus.modifyApply)){
			//变更
			workDealInfo.setDealInfoType2(WorkDealInfoType.TYPE_INFORMATION_REROUTE);
		}else{
		//新增
		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
		}
		workDealInfo.setYear(Integer.parseInt(selfApplication.getApplicationPeriod()));
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(workCompanyHis);
		workDealInfo.setWorkCompanyHis(workCompanyHis);
		WorkUserHis workUserHis = workUserService.change(workUser,
				workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = null;
		WorkCertInfo workCertInfo = null;
		if (workDealInfo.getWorkCertInfo() != null) {
			workCertApplyInfo = workDealInfo.getWorkCertInfo()
					.getWorkCertApplyInfo();
		} else {
			workCertApplyInfo = new WorkCertApplyInfo();
		}
		workCertApplyInfo.setName(selfApplication.getTransactorName());
		workCertApplyInfo.setEmail(selfApplication.getTransactorEmail());
		workCertApplyInfo.setIdCard(selfApplication.getTransactorDocumentNumber());
		workCertApplyInfoService.save(workCertApplyInfo);
		if (workDealInfo.getWorkCertInfo() != null) {
			workCertInfo = workDealInfo.getWorkCertInfo();
		} else {
			workCertInfo = new WorkCertInfo();
		}
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfoService.save(workCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		workDealInfo.setSelfApplyId(selfApplication.getId());
		workDealInfoService.save(workDealInfo);
		Integer reseNum = agent.getReserveNum();
		Integer surNum = agent.getSurplusNum();
		agent.setReserveNum(reseNum + 1);
		agent.setSurplusNum(surNum - 1);
		configChargeAgentService.save(agent);
		ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
		dealInfoBound.setDealInfo(workDealInfo);
		ConfigChargeAgent agentBound = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
		dealInfoBound.setAgent(agentBound);
		configAgentBoundDealInfoService.save(dealInfoBound);
		logUtil.saveSysLog("计费策略模版", "计费策略模版：" + agent.getId() + "--业务编号："+ workDealInfo.getId() + "--关联成功!", "");
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
		WorkLog workLog = new WorkLog();
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(configApp);
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLog.setWorkCompany(workCompany);
		workLogService.save(workLog);
		logUtil.saveSysLog("业务办理", "新增业务保存：编号" + workDealInfo.getId() + "单位名称："+ workDealInfo.getWorkCompany().getCompanyName(), "");
		// 改变申请表的状态
		selfApplication.setStatus(SelfApplicationStatus.downApply);
		selfApplication.setMoney(configChargeAgentDetail.getMoney());
		selfApplicationService.save(selfApplication);
		return "redirect:" + Global.getAdminPath()+ "/self/selfApplication/?repage";
	}

}
