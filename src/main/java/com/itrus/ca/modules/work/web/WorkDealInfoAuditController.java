/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.entity.FinanceQuitMoney;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.finance.service.FinanceQuitMoneyService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.dao.ConfigSupplierProductRelationDao;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.service.SelfAreaService;
import com.itrus.ca.modules.self.service.SelfImageService;
import com.itrus.ca.modules.service.CaService;
import com.itrus.ca.modules.settle.web.UpdateQuantityStatistics;
import com.itrus.ca.modules.statistic.service.StatisticCertDataService;
import com.itrus.ca.modules.sys.entity.CommonAttach;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.CommonAttachService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCertTrustApply;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCertTrustApplyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 业务审批Controller
 * 
 * @author WangHongwei
 * @version 2014年6月18日18:35:27
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workDealInfoAudit")
public class WorkDealInfoAuditController extends BaseController {

	
	@Autowired
	private CommonAttachService attachService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private WorkPayInfoService workPayInfoService;

	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;

	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;

	@Autowired
	private WorkCertTrustApplyService trustApplyService;

	@Autowired
	ConfigSupplierProductRelationDao configSupplierProductRelationDao;

	@Autowired
	private WorkLogService workLogService;

	@Autowired
	ConfigRaAccountService raAccountService;

	@Autowired
	StatisticCertDataService statisticCertDataService;

	@Autowired
	ReceiptDepotInfoService receiptDepotInfoService;

	@Autowired
	ReceiptEnterInfoService receiptEnterInfoService;

	@Autowired
	private KeyUsbKeyInvoiceService keyInvoiceService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;
	
	@Autowired
	CaService caService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	UpdateQuantityStatistics updateQuantityStatistics;

	@Autowired
	WorkUserHisService workUserHisService;

	@Autowired
	WorkUserService workUserService;

	@Autowired
	WorkCertApplyInfoService workCertApplyInfoService;

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;

	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;

	@Autowired
	private ConfigChargeAgentService chargeAgentService;

	@Autowired
	private FinanceQuitMoneyService financeQuitMoneyService;

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	@Autowired
	private ConfigProductService configProductService;
	@Autowired
	private SelfAreaService selfAreaService;
	@Autowired
	private SelfImageService selfImageService;

	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public WorkDealInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return workDealInfoService.get(id);
		} else {
			return new WorkDealInfo();
		}
	}

	@ModelAttribute
	public WorkCertTrustApply getTrustApply(@RequestParam(required = false) Long id) {
		if (id != null) {
			return trustApplyService.get(id);
		} else {
			return new WorkCertTrustApply();
		}
	}

	/**
	 * 签证业务
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(WorkDealInfo workDealInfo, String dealInfoStatus, HttpServletRequest request,
			Integer dealInfoType, HttpServletResponse response, Model model) {
		workDealInfo.setDealInfoStatus(dealInfoStatus);
		workDealInfo.setDealInfoType(dealInfoType);
		Page<WorkDealInfo> page = workDealInfoService.findByStatus(new Page<WorkDealInfo>(request, response),
				workDealInfo);
//		List<WorkDealInfo> noIxInfos = page.getList();
//		List<WorkDealInfo> isIxInfos = workDealInfoService.findByIsIxin(workDealInfo);
//		noIxInfos.addAll(isIxInfos);
//		page.setList(noIxInfos);
		model.addAttribute("page", page);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		return "modules/work/workDealInfoAuditListF";
	}

	@RequestMapping(value = "listTrustCountApply")
	public String listTrustCountApply(Model model, HttpServletRequest request, HttpServletResponse response,
			WorkCertTrustApply workCertTrustApply) {
		Page<WorkCertTrustApply> page = trustApplyService.find(new Page<WorkCertTrustApply>(request, response),
				workCertTrustApply);
		model.addAttribute("page", page);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("product", ProductType.productTypeStrMap);
		model.addAttribute("dealInfoType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("workCertTrustApply", workCertTrustApply);
		return "modules/work/workDealInfoAuditListA";
	}

	/**
	 * 异常业务
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("exceptionList")
	public String exceptionList(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);
		Page<WorkDealInfo> page = workDealInfoService.find(new Page<WorkDealInfo>(request, response), workDealInfo);
		model.addAttribute("page", page);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		return "modules/work/workDealInfoAuditListT";
	}

	/**
	 * 签证任务审批
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("auditFrom")
	public String auditFrom(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model,RedirectAttributes redirectAttributes) {
		String url = Global.getConfig("images.path");
		String name = workDealInfo.getWorkUser().getContactName();
		//新增   验证第一页
		model.addAttribute("pageType", "audit");
		if(workDealInfo.getDealInfoType()==WorkDealInfoType.TYPE_ADD_CERT){
			model.addAttribute("proType", ProductType.productTypeStrMap);
			model.addAttribute("workDealInfo", workDealInfo);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("user", UserUtils.getUser());
			model.addAttribute("date", sdf.format(new Date()));
			if (workDealInfo.getId() != null) {
				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
			}
			if (workDealInfo.getWorkCertInfo() != null) {
				model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
			}
			if (workDealInfo.getDealInfoStatus().equals(WorkDealInfoStatus.STATUS_UPDATE_USER)) {
				model.addAttribute("canEdit", true);
			} else {
				model.addAttribute("canEdit", false);
			}
			
			if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
				
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}
				
				model.addAttribute("boundLabelList", nameSet);
				ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				
			} else {
				ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				
			}
			
			if(workDealInfo.getExpirationDate()!=null){
				model.addAttribute("expirationDate", workDealInfo.getExpirationDate());
			}
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			return "modules/work/workDealInfoAuditACUForm";
		}else{
			if (workDealInfo.getId() != null) {
				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
			}
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			
			//类型判断
			Integer dealInfoType = workDealInfo.getDealInfoType();
			Integer dealInfoType1 = workDealInfo.getDealInfoType1();
			Integer dealInfoType2 = workDealInfo.getDealInfoType2();
			
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

	/**
	 * 验证
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("verifyFrom")
	public String verifyFrom(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model,RedirectAttributes redirectAttributes) {
		String url = Global.getConfig("images.path");
		//新增   验证第一页
		model.addAttribute("pageType", "verify");
		if(workDealInfo.getDealInfoType()==WorkDealInfoType.TYPE_ADD_CERT){
			model.addAttribute("proType", ProductType.productTypeStrMap);
			model.addAttribute("workDealInfo", workDealInfo);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("user", UserUtils.getUser());
			model.addAttribute("date", sdf.format(new Date()));
			if (workDealInfo.getId() != null) {
				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
			}
			if (workDealInfo.getWorkCertInfo() != null) {
				model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
			}
			if (workDealInfo.getDealInfoStatus().equals(WorkDealInfoStatus.STATUS_UPDATE_USER)) {
				model.addAttribute("canEdit", true);
			} else {
				model.addAttribute("canEdit", false);
			}
			
			if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
				
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}
				
				model.addAttribute("boundLabelList", nameSet);
				ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				
			} else {
				ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				
			}
			
			if(workDealInfo.getExpirationDate()!=null){
				model.addAttribute("expirationDate", workDealInfo.getExpirationDate());
			}
			

			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			return "modules/work/workDealInfoAuditACUForm";
		}else{
			if (workDealInfo.getId() != null) {
				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
			}
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			
			//类型判断
			Integer dealInfoType = workDealInfo.getDealInfoType();
			Integer dealInfoType1 = workDealInfo.getDealInfoType1();
			Integer dealInfoType2 = workDealInfo.getDealInfoType2();
			
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

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				return "modules/work/maintain/workDealInfoMaintainChangeCheck";
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

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainLostCheck";
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

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateCheck";
			} else if (dealInfoTypes.get(0).equals("4")) {
				ConfigChargeAgent agent = configChargeAgentService
						.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				return "modules/work/maintain/workDealInfoMaintainRevoke";
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

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

				return "modules/work/maintain/workDealInfoMaintainChangeCheck";
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
				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateCheck";
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

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateChangeCheck";
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

			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			model.addAttribute("workLog", list);

			if (chargeAgent != null) {
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			}

			// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			return "modules/work/maintain/workDealInfoMaintainUpdateChangeCheck";
		} else {
			return "modules/work/workDealInfoMaintain";
		}
		
		return "modules/work/workDealInfoMaintain";
	}
	
	@RequestMapping("updateLoad")
	public String updateLoad(WorkDealInfo workDealInfo, String recordContent, Integer year, HttpServletRequest request,
			HttpServletResponse response, Model model, String contactName, String conCertType, String contacEmail,
			String conCertNumber, String contactPhone, String contactTel, String pName, String contactSex,
			String pEmail, String pIDCard, Integer agentId, Long agentDetailId) {
		workDealInfo.setYear(year);
		// 标注为i信端更新

		workDealInfo.setPayType(agentId);
		if(agentDetailId!=null){
			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(agentDetailId);
			
			workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
			
		}
		// workDealInfo.setIsIxin(true);
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
		// 保存经办人信息
		WorkUser workUser = workDealInfo.getWorkUser();
		workUser.setContactName(contactName);
		workUser.setStatus(1);
		workUser.setConCertType(conCertType);
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactEmail(contacEmail);
		workUser.setContactPhone(contactPhone);
		workUser.setContactTel(contactTel);
		workUser.setContactSex(contactSex);
		workUserService.save(workUser);
		// 保存经办人历史记录
		WorkUserHis his = workUserService.change(workUser, workDealInfo.getWorkCompanyHis());
		workUserHisService.save(his);
		workDealInfo.setWorkUserHis(his);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = workDealInfo.getWorkCertInfo().getWorkCertApplyInfo();
		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);
		workCertApplyInfo.setProvince(workDealInfo.getWorkCompany().getProvince());
		workCertApplyInfo.setCity(workDealInfo.getWorkCompany().getCity());

		workCertApplyInfoService.save(workCertApplyInfo);

		WorkCertInfo oldCertInfo = workDealInfo.getWorkCertInfo();
		oldCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfoService.save(oldCertInfo);
		workDealInfoService.save(workDealInfo);

		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}

	/**
	 * 审批第二页面
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("auditLoad")
	public String auditLoad(WorkDealInfo workDealInfo, String recordContent, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		model.addAttribute("product",
				ProductType.getProductTypeName(Integer.parseInt(workDealInfo.getConfigProduct().getProductName())));
		model.addAttribute("dealInfoType", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		model.addAttribute("workPayInfo", workDealInfo.getWorkPayInfo());
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String companyName = workDealInfo.getWorkCompany().getCompanyName();
			String userName = workDealInfo.getPayUser().getName();
			String officeName = workDealInfo.getPayUser().getOffice().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney() && payInfo.getMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getMoney().toString(), officeName,
						userName, date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getPosMoney().toString(), officeName,
						userName, date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getAlipayMoney().toString(),
						officeName, userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			/*
			 * if (payInfo.getMethodGov()) { String[] s = new String[] {
			 * payInfo.getSn(), companyName, "", officeName, userName, date,
			 * "政府统一采购", "" }; payInfos.add(s); } if
			 * (payInfo.getMethodContract()) { String[] s = new String[] {
			 * payInfo.getSn(), companyName, "", officeName, userName, date,
			 * "合同采购", "" }; payInfos.add(s); }
			 */
		}
		List<WorkFinancePayInfoRelation> list = workFinancePayInfoRelationService
				.findByPayInfo(workDealInfo.getWorkPayInfo());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(1)) {

				workDealInfo.getWorkPayInfo().setMethodMoney(true);
				workDealInfo.getWorkPayInfo()
						.setMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getMoney());

			} else if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(2)) {

				workDealInfo.getWorkPayInfo().setMethodPos(true);
				workDealInfo.getWorkPayInfo()
						.setPosMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getPosMoney());

			} else if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(3)) {

				workDealInfo.getWorkPayInfo().setMethodBank(true);
				workDealInfo.getWorkPayInfo()
						.setBankMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getBankMoney());

			} else if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(4)) {

				workDealInfo.getWorkPayInfo().setMethodAlipay(true);
				workDealInfo.getWorkPayInfo()
						.setAlipayMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getAlipayMoney());
			}

		}

		model.addAttribute("workDealInfo", workDealInfo);
		// workDealInfo.workPayInfo.methodMoney
		// workDealInfo.workPayInfo.methodBank
		model.addAttribute("fpir", list);
		model.addAttribute("infos", payInfos);
		
		model.addAttribute("pageType", "audit");
		return "modules/work/workDealInfoAuditLoad";
	}
	
	/**
	 * 验证第二页面
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("verifyLoad")
	public String verifyLoad(WorkDealInfo workDealInfo, String recordContent, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		model.addAttribute("product",
				ProductType.getProductTypeName(Integer.parseInt(workDealInfo.getConfigProduct().getProductName())));
		model.addAttribute("dealInfoType", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		model.addAttribute("workPayInfo", workDealInfo.getWorkPayInfo());
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String companyName = workDealInfo.getWorkCompany().getCompanyName();
			String userName = workDealInfo.getPayUser().getName();
			String officeName = workDealInfo.getPayUser().getOffice().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney() && payInfo.getMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getMoney().toString(), officeName,
						userName, date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getPosMoney().toString(), officeName,
						userName, date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), companyName, payInfo.getAlipayMoney().toString(),
						officeName, userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			/*
			 * if (payInfo.getMethodGov()) { String[] s = new String[] {
			 * payInfo.getSn(), companyName, "", officeName, userName, date,
			 * "政府统一采购", "" }; payInfos.add(s); } if
			 * (payInfo.getMethodContract()) { String[] s = new String[] {
			 * payInfo.getSn(), companyName, "", officeName, userName, date,
			 * "合同采购", "" }; payInfos.add(s); }
			 */
		}
		List<WorkFinancePayInfoRelation> list = workFinancePayInfoRelationService
				.findByPayInfo(workDealInfo.getWorkPayInfo());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(1)) {

				workDealInfo.getWorkPayInfo().setMethodMoney(true);
				workDealInfo.getWorkPayInfo()
						.setMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getMoney());

			} else if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(2)) {

				workDealInfo.getWorkPayInfo().setMethodPos(true);
				workDealInfo.getWorkPayInfo()
						.setPosMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getPosMoney());

			} else if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(3)) {

				workDealInfo.getWorkPayInfo().setMethodBank(true);
				workDealInfo.getWorkPayInfo()
						.setBankMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getBankMoney());

			} else if (list.get(i).getFinancePaymentInfo().getPaymentMethod().equals(4)) {

				workDealInfo.getWorkPayInfo().setMethodAlipay(true);
				workDealInfo.getWorkPayInfo()
						.setAlipayMoney(list.get(i).getMoney() + workDealInfo.getWorkPayInfo().getAlipayMoney());
			}

		}

		model.addAttribute("workDealInfo", workDealInfo);
		// workDealInfo.workPayInfo.methodMoney
		// workDealInfo.workPayInfo.methodBank
		model.addAttribute("fpir", list);
		model.addAttribute("infos", payInfos);
		model.addAttribute("pageType", "verify");
		return "modules/work/workDealInfoAuditLoad";
	}

	/**
	 * 跳转退款页面
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("backMoneyFrom")
	public String backMoneyFrom(WorkDealInfo workDealInfo, Integer type, HttpServletRequest request,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("workDealInfo", workDealInfo);
		if (!UserUtils.getUser().getOffice().getId().equals(workDealInfo.getCreateBy().getOffice().getId())) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到" + workDealInfo.getCreateBy().getOffice().getName() + "网点进行退费！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
		}
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String companyName = workDealInfo.getWorkCompany().getCompanyName();
			String officeName = workDealInfo.getCreateBy().getOffice().getName();
			String userName = workDealInfo.getCreateBy().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney() && payInfo.getMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getMoney().toString(), companyName,
						userName, date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getBankMoney().toString(), companyName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getPosMoney().toString(), companyName,
						userName, date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getAlipayMoney().toString(),
						companyName, userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
		}
		List<WorkFinancePayInfoRelation> list = workFinancePayInfoRelationService
				.findByPayInfo(workDealInfo.getWorkPayInfo());

		if (type == 0) {
			model.addAttribute("bgType", true);
			model.addAttribute("revoke", false);
			model.addAttribute("tKey", false);

			ConfigProduct configProduct = workDealInfo.getConfigProduct();

			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
			}

			model.addAttribute("boundLabelList", nameSet);

		} else {
			model.addAttribute("bgType", false);
			model.addAttribute("revoke", true);
			model.addAttribute("tKey", true);
		}
		ConfigChargeAgent agent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
		model.addAttribute("agent", agent);

		model.addAttribute("fpir", list);
		model.addAttribute("infos", payInfos);
		model.addAttribute("signSerialNumber", workDealInfo.getWorkCertInfo().getSerialnumber().toLowerCase());
		return "modules/work/workDealInfoAuditBack";
	}

	@RequestMapping("jujueFrom")
	public String jujueFrom(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		model.addAttribute("workDealInfo", workDealInfo);
		return "modules/work/workDealInfoAuditJuJue";
	}

	@RequestMapping("jujue")
	public String jujue(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
			Long dealId = workDealInfo.getPrevId();
			WorkDealInfo dealInfo = workDealInfoService.get(dealId);
			dealInfo.setDelFlag("0");
			workDealInfoService.save(dealInfo);

			workDealInfo.setDelFlag("1");
		} else {
			if(workDealInfo.getStatus()==0){
				workDealInfo.setDealInfoStatus("17");
			}else{				
				workDealInfo.setDealInfoStatus("4");
			}
		}
		WorkLog workLog = new WorkLog();
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setCreatTime(new Date());
		workLog.setOffice(workDealInfo.getCreateBy().getOffice());
		workLog.setRecordContent(workDealInfo.getRemarks());
		workLog.setRemarks(workDealInfo.getRemarks());
		// workDealInfo.setIsIxin(true);
		workDealInfoService.save(workDealInfo);

		workLog.setWorkDealInfo(workDealInfo);
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		// 退费
		if (workDealInfo.getWorkPayInfo() != null) {
			WorkPayInfo workPayInfo = workPayInfoService.get(workDealInfo.getWorkPayInfo().getId());
			Iterator<WorkFinancePayInfoRelation> iterator = workPayInfo.getWorkFinancePayInfoRelations().iterator();
			while (iterator.hasNext()) {
				WorkFinancePayInfoRelation relation = iterator.next();
				FinancePaymentInfo fpr = relation.getFinancePaymentInfo();
				fpr.setBingdingTimes(fpr.getBingdingTimes() - 1);
				// fpr.setPaymentMoney(fpr.getPaymentMoney()+workDealInfo.getWorkPayInfo().getWorkReceivaMoney());
				// fpr.setResidueMoney(fpr.getResidueMoney()
				// + workDealInfo.getWorkPayInfo().getWorkReceivaMoney());
				fpr.setResidueMoney(fpr.getResidueMoney() + relation.getMoney());
				workFinancePayInfoRelationService.delete(relation.getId());
			}
			workDealInfo.setWorkPayInfo(null);
		}

		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/list";
	}

	/**
	 * 退费处理 （退还钱款）
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @param receiptAmount
	 * @param model
	 * @param revoke
	 * @param keySn
	 * @return
	 */
	@RequestMapping("backMoney1")
	public String backMoney1(Long id, HttpServletRequest request, HttpServletResponse response, String receiptAmount,
			Model model, Integer revoke, 
			@RequestParam(required = false)String keySn
			) {
		WorkDealInfo dealInfo = workDealInfoService.get(id);
		WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
		if ("1".equals(dealInfo.getDelFlag())) {// 判断是否进行过退费，防止浏览器回退再次退费
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/list";

		}
		// 去掉代理商结算
		dealInfo.setCanSettle(false);// 退费不可被结算
		dealInfo.setClassifying(0);// 结算年限为0年
		workDealInfoService.save(dealInfo);

		ConfigRaAccount raAccount = raAccountService.get(dealInfo.getConfigProduct().getRaAccountId());
		// 生成新的业务类型为退费
		WorkDealInfo workDealInfo = new WorkDealInfo();

		// 获取上个业务的payInfo钱数改成负数保存新的业务中
		
		//改:生成一个新的payInfo
		WorkPayInfo newpayInfo = new WorkPayInfo();
	
		newpayInfo.setSn(PayinfoUtil.getPayInfoNo());
		newpayInfo.setWorkTotalMoney(payInfo.getWorkTotalMoney());
		newpayInfo.setWorkPayedMoney(0d);
		newpayInfo.setWorkReceivaMoney(payInfo.getWorkReceivaMoney());
		newpayInfo.setReceiptAmount(payInfo.getReceiptAmount());
		newpayInfo.setMethodMoney(payInfo.getMethodMoney());
		newpayInfo.setMethodAlipay(payInfo.getMethodAlipay());
		newpayInfo.setMethodBank(payInfo.getMethodBank());
		newpayInfo.setMethodContract(payInfo.getMethodContract());
		newpayInfo.setMethodGov(payInfo.getMethodGov());
		newpayInfo.setMethodPos(payInfo.getMethodPos());
		newpayInfo.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
		newpayInfo.setCreateDate(new Date());
		
		
		if (payInfo.getMoney() > 0d) {
			newpayInfo.setMoney(-payInfo.getMoney());
		}else{newpayInfo.setMoney(0d);}
		if (payInfo.getAlipayMoney() > 0d) {
			newpayInfo.setAlipayMoney(-payInfo.getAlipayMoney());
		}else{newpayInfo.setAlipayMoney(0d);}
		if (payInfo.getPosMoney() > 0d) {
			newpayInfo.setPosMoney(-payInfo.getPosMoney());
		}else{newpayInfo.setPosMoney(0d);}
		if (payInfo.getBankMoney() > 0d) {
			newpayInfo.setBankMoney(-payInfo.getBankMoney());
		}else{newpayInfo.setBankMoney(0d);}
		workPayInfoService.save(newpayInfo);

		// 变更relation表money变成负数，退还绑定钱数，次数。
		Iterator<WorkFinancePayInfoRelation> iterator = payInfo.getWorkFinancePayInfoRelations().iterator();
		while (iterator.hasNext()) {
			WorkFinancePayInfoRelation relation = iterator.next();
			FinancePaymentInfo fpr = relation.getFinancePaymentInfo();
			fpr.setBingdingTimes(fpr.getBingdingTimes() - 1);
			// fpr.setPaymentMoney(fpr.getPaymentMoney()+workDealInfo.getWorkPayInfo().getWorkReceivaMoney());
			fpr.setResidueMoney(fpr.getResidueMoney() + relation.getMoney());
			financePaymentInfoService.save(fpr);
			relation.setMoney(-relation.getMoney());
			workFinancePayInfoRelationService.save(relation);
		}
		// 吊销证书
		if (revoke == 0) {
			// 为新的业务信息按吊销逻辑填入信息
			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_RETURN_WORK);
			workDealInfo.setYear(0);
			workDealInfo.setConfigApp(dealInfo.getConfigApp());
			ConfigCommercialAgent commercialAgent = configAgentAppRelationService
					.findAgentByApp(workDealInfo.getConfigApp());
			workDealInfo.setConfigCommercialAgent(commercialAgent);
			workDealInfo.setConfigCommercialAgent(dealInfo.getConfigCommercialAgent());
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(UserUtils.getUser().getOffice());
			if (configAgentOfficeRelations.size() > 0) {
				workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
			}
			workDealInfo.setWorkUser(dealInfo.getWorkUser());
			workDealInfo.setWorkCompany(dealInfo.getWorkCompany());
			workDealInfo.setWorkUserHis(dealInfo.getWorkUserHis());
			workDealInfo.setWorkCompanyHis(dealInfo.getWorkCompanyHis());
			workDealInfo.setConfigProduct(dealInfo.getConfigProduct());
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_REVOKE);
			workDealInfo.setCreateBy(UserUtils.getUser());
			workDealInfo.setCreateDate(new Date());
			workDealInfo.setSvn(workDealInfoService.getSVN(0));
			workDealInfo.setPrevId(dealInfo.getId());
			workDealInfo.setObtainedDate(new Date());// 证书获取时间
			// workDealInfo.setWorkCertInfo(dealInfo.getWorkCertInfo());
			// workcertinfo继承上次业务的
			WorkCertInfo workCertInfo = workCertInfoService.get(dealInfo.getWorkCertInfo().getId());
			workDealInfo.setWorkCertInfo(workCertInfo);
			workDealInfo.setCanSettle(false);// 退费不可被结算
			workDealInfo.setClassifying(0);// 结算年限为0年
			// 存入修改过的payinfo
			workDealInfo.setWorkPayInfo(newpayInfo);
			workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
			workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
			workDealInfoService.save(workDealInfo);

			FinanceQuitMoney quitMoney = new FinanceQuitMoney();
			quitMoney.setQuitMoney(workDealInfo.getWorkPayInfo().getWorkTotalMoney());
			quitMoney.setQuitDate(new Date());
			quitMoney.setQuitWindow(UserUtils.getUser().getOffice().getName());
			quitMoney.setQuitReason("现金退费");
			quitMoney.setRession("现金退费");
			quitMoney.setWorkDealInfo(workDealInfo);
			quitMoney.setStatus("1");
			financeQuitMoneyService.save(quitMoney);

			ConfigSupplier supplier = getSupplier(Integer.valueOf(dealInfo.getConfigProduct().getProductName()));
			if (supplier != null) {
				// 去掉供应商结算
				updateQuantityStatistics.delOUSettleInfo(raAccount.getAccountOrgUnit(),
						Integer.valueOf(dealInfo.getConfigProduct().getProductName()), getDealInfoAddType(dealInfo), 1,
						dealInfo.getYear(), supplier, dealInfo.getObtainedDate());
			}

			try {
				caService.revokeCaCert(dealInfo.getCertSn(), "", raAccount);
				WorkCertInfo certInfo = dealInfo.getWorkCertInfo();
				certInfo.setStatus(3);
				certInfo.setRevokeDate(new Date());
				workCertInfoService.save(certInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 删除以前的dealinfo使其不再列表显示
			workDealInfoService.delete(dealInfo.getId());
		} else {
			// 取这笔退费业务的上次业务信息
			WorkDealInfo wDealInfo = workDealInfoService.get(dealInfo.getPrevId());
			// 生成新的业务信息，类型为退费
			// 为新的业务信息按未吊销逻辑填入信息

			WorkDealInfo infos = workDealInfoService.get(dealInfo.getPrevId());

			if (infos.getDealInfoType() != null) {
				workDealInfo.setDealInfoType(infos.getDealInfoType());
			} else if (infos.getDealInfoType1() != null) {
				workDealInfo.setDealInfoType(infos.getDealInfoType1());
			} else if (infos.getDealInfoType2() != null) {
				workDealInfo.setDealInfoType(infos.getDealInfoType2());
			} else if (infos.getDealInfoType3() != null) {
				workDealInfo.setDealInfoType(infos.getDealInfoType3());
			}

			workDealInfo.setYear(0);
			workDealInfo.setConfigApp(wDealInfo.getConfigApp());
			ConfigCommercialAgent commercialAgent = configAgentAppRelationService
					.findAgentByApp(workDealInfo.getConfigApp());
			workDealInfo.setConfigCommercialAgent(commercialAgent);
			workDealInfo.setConfigCommercialAgent(wDealInfo.getConfigCommercialAgent());
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(UserUtils.getUser().getOffice());
			if (configAgentOfficeRelations.size() > 0) {
				workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
			}
			workDealInfo.setWorkUser(wDealInfo.getWorkUser());
			workDealInfo.setWorkCompany(wDealInfo.getWorkCompany());
			workDealInfo.setWorkUserHis(wDealInfo.getWorkUserHis());
			workDealInfo.setWorkCompanyHis(wDealInfo.getWorkCompanyHis());
			workDealInfo.setConfigProduct(wDealInfo.getConfigProduct());
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			workDealInfo.setCreateBy(UserUtils.getUser());
			workDealInfo.setCreateDate(new Date());
			workDealInfo.setSvn(workDealInfoService.getSVN(0));
			workDealInfo.setPrevId(dealInfo.getId());
			// 取上次业务多证书编号
			workDealInfo.setCertSort(wDealInfo.getCertSort());
			// 证书获取时间
			workDealInfo.setObtainedDate(new Date());

			workDealInfo.setCanSettle(false);// 退费不可被结算
			workDealInfo.setClassifying(0);// 结算年限为0年
			// 存入修改过的payinfo
			workDealInfo.setWorkPayInfo(newpayInfo);

			// 如果退费业务为变更或者更新，吊销该业务证书并依照该业务上次业务数据重新制证
			boolean bFlag = false;// WorkDealInfoType.TYPE_UPDATE_CERT.equals(dealInfo.getDealInfoType())
									// ||
									// WorkDealInfoType.TYPE_INFORMATION_REROUTE.equals(dealInfo.getDealInfoType2())
			if (bFlag) {
				try {
					caService.revokeCaCert(dealInfo.getCertSn(), "", raAccount);
					WorkCertInfo certInfo = dealInfo.getWorkCertInfo();
					certInfo.setStatus(3);
					certInfo.setRevokeDate(new Date());
					workCertInfoService.save(certInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 重新存入certInfo以便保存制证数据
				WorkCertInfo newCertInfo = new WorkCertInfo();
				newCertInfo.setWorkCertApplyInfo(wDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
				workCertInfoService.save(newCertInfo);
				workDealInfo.setWorkCertInfo(newCertInfo);
				// 证书期限 = 原证书的上一张证书的剩余天数
				if (wDealInfo.getWorkCertInfo().getNotafter().after(new Date())) {
					int day = getLastCertDay(wDealInfo.getWorkCertInfo().getNotafter());
					workDealInfo.setLastDays(day);
				} else {
					workDealInfo.setLastDays(0);
				}
				// 该退费业务状态改为待制证
				workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_WAIT);
			} else {
				// workcertinfo继承上次业务的
				WorkCertInfo workCertInfo = workCertInfoService.get(wDealInfo.getWorkCertInfo().getId());
				workDealInfo.setWorkCertInfo(workCertInfo);
			}
			workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
			workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
			workDealInfoService.save(workDealInfo);
			// 删除以前的dealinfo使其不再列表显示
			workDealInfoService.delete(dealInfo.getId());
		}
		// 退key
		if (keySn != null && !keySn.equals("")) {
			keyInvoiceService.saveKeyUsbKey1(keySn);
		}
		// 退发票
		try {
			if (!receiptAmount.equals("") && Double.valueOf(receiptAmount) != 0d) {
				// 当实际余额 大约 库存余额
				ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService
						.findDepotByOffice(dealInfo.getCreateBy().getOffice()).get(0);
				// 修改余额
				receiptDepotInfo
						.setReceiptResidue(receiptDepotInfo.getReceiptResidue() + Double.valueOf(receiptAmount));
				receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal() + Double.valueOf(receiptAmount));

				// 创建入库信息
				ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
				receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
				receiptEnterInfo.setNow_Money(Double.valueOf(receiptAmount));
				receiptEnterInfo.setBeforMoney(
						receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue() - Double.valueOf(receiptAmount));
				receiptEnterInfo.setReceiptMoney(receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue());
				receiptEnterInfo.setReceiptType(3);// 退费入库
				receiptEnterInfoService.save(receiptEnterInfo);

				logUtil.saveSysLog("业务办理退费", "库房" + receiptDepotInfo.getReceiptName() + "添加入库信息成功", "");
				receiptDepotInfoService.save(receiptDepotInfo);
				logUtil.saveSysLog("业务办理退费", "修改库房" + receiptDepotInfo.getReceiptName() + "发票余额，异常入库", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		logUtil.saveSysLog("业务办理", "异常业务退费：编号" + dealInfo.getId(), "");
		
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/list";
	}

	
	
	@RequestMapping("backMoney")
	public String backMoney(Long id, HttpServletRequest request, HttpServletResponse response, Long agentDetailId,
			Integer iseq, Model model) {
		WorkDealInfo dealInfo = workDealInfoService.get(id);
		WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
		try {
			// 新建payInfo保存原来的payInfo数据
			WorkDealInfo workDealInfo = new WorkDealInfo();
			
			workDealInfo.setDealInfoType(dealInfo.getDealInfoType());
			workDealInfo.setDealInfoType1(dealInfo.getDealInfoType1());
			workDealInfo.setDealInfoType2(dealInfo.getDealInfoType2());
			workDealInfo.setDealInfoType3(WorkDealInfoType.TYPE_PAY_REPLACED);
			
			workDealInfo.setYear(dealInfo.getYear());
			workDealInfo.setConfigApp(dealInfo.getConfigApp());
			ConfigCommercialAgent commercialAgent = configAgentAppRelationService
					.findAgentByApp(workDealInfo.getConfigApp());
			workDealInfo.setConfigCommercialAgent(commercialAgent);
			workDealInfo.setConfigCommercialAgent(dealInfo.getConfigCommercialAgent());
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(UserUtils.getUser().getOffice());
			if (configAgentOfficeRelations.size() > 0) {
				workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 鍔冲姟鍏崇郴澶栭敭
			}
			
			
			workDealInfo.setCertSn(dealInfo.getCertSn());
			workDealInfo.setCertSort(dealInfo.getCertSort());
			workDealInfo.setWorkUser(dealInfo.getWorkUser());
			workDealInfo.setWorkCompany(dealInfo.getWorkCompany());
			workDealInfo.setWorkUserHis(dealInfo.getWorkUserHis());
			workDealInfo.setWorkCompanyHis(dealInfo.getWorkCompanyHis());
			workDealInfo.setConfigProduct(dealInfo.getConfigProduct());
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			workDealInfo.setCreateBy(UserUtils.getUser());
			workDealInfo.setCreateDate(new Date());
			//workDealInfo.setSvn(dealInfo.getSvn());
			workDealInfo.setSvn(workDealInfoService.getSVN(0));
			workDealInfo.setPrevId(dealInfo.getId());
			workDealInfo.setObtainedDate(new Date());
			workDealInfo.setKeySn(dealInfo.getKeySn());
			workDealInfo.setNotafter(dealInfo.getNotafter());
			workDealInfo.setWorkCertInfo(dealInfo.getWorkCertInfo());

			// 鏂板缓payInfo淇濆瓨鍘熸潵鐨刾ayInfo鏁版嵁

			WorkPayInfo workPayInfo = new WorkPayInfo();

			if (iseq.equals(1) || iseq.equals(2)) {
				Double oldAdd = 0d;
				Double newAdd = 0d;
				if (dealInfo.getDealInfoType() != null) {
					if (dealInfo.getDealInfoType().equals(0)) {
						
						newAdd += configChargeAgentDetailService.getChargeMoney(agentDetailId, 4,0);
						oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
								4, null);
						
						oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
								dealInfo.getDealInfoType(), dealInfo.getYear());
						newAdd += configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType(), dealInfo.getYear());
						workPayInfo.setOpenAccountMoney(configChargeAgentDetailService.getChargeMoney(agentDetailId,
								4, null));
						workPayInfo.setAddCert(configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType(), dealInfo.getYear()));
					} else if (dealInfo.getDealInfoType().equals(1)) {
						oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
								dealInfo.getDealInfoType(), dealInfo.getYear());
						newAdd += configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType(), dealInfo.getYear());
						workPayInfo.setUpdateCert(configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType(), dealInfo.getYear()));
					}
				}
				if (dealInfo.getDealInfoType1() != null) {
					if (dealInfo.getDealInfoType1().equals(2)) {
						oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
								dealInfo.getDealInfoType1(), null);
						newAdd += configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType1(), null);
						workPayInfo.setErrorReplaceCert(configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType1(), null));

					} else if (dealInfo.getDealInfoType1().equals(3)) {
						oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
								dealInfo.getDealInfoType1(), null);
						newAdd += configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType1(), null);

						workPayInfo.setErrorReplaceCert(configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType1(), null));
					}

				}
				if (dealInfo.getDealInfoType2() != null) {
					if (dealInfo.getDealInfoType2().equals(4)) {
						oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
								dealInfo.getDealInfoType2(), null);
						newAdd += configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType2(), null);
						workPayInfo.setInfoChange(configChargeAgentDetailService.getChargeMoney(agentDetailId,
								dealInfo.getDealInfoType2(), null));
					}
				}
				
				workPayInfo.setElectron(payInfo.getElectron());
				if(iseq.equals(1)){
					if (newAdd < oldAdd) {
						workPayInfo.setMoney(newAdd - oldAdd);
					}else{
						workPayInfo.setMoney(oldAdd - newAdd);
					}
				}
				
				if(iseq.equals(2)){
					if (newAdd > oldAdd) {
						workPayInfo.setMoney(newAdd - oldAdd);
					}else{
						workPayInfo.setMoney(oldAdd - newAdd);
					}
				}
				
				workPayInfo.setAlipayMoney(0d);
				workPayInfo.setPosMoney(0d);
				workPayInfo.setBankMoney(0d);
				workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
				workPayInfo.setWorkTotalMoney(newAdd);
				workPayInfo.setWorkPayedMoney(0d);
				workPayInfo.setWorkReceivaMoney(payInfo.getWorkReceivaMoney());
				workPayInfo.setReceiptAmount(newAdd);
				workPayInfo.setMethodMoney(true);
				workPayInfo.setMethodAlipay(false);
				workPayInfo.setMethodBank(false);
				workPayInfo.setMethodContract(payInfo.getMethodContract());
				workPayInfo.setMethodGov(payInfo.getMethodGov());
				workPayInfo.setMethodPos(false);
				workPayInfoService.save(workPayInfo);

				if (newAdd > oldAdd) {
					ReceiptInvoice receiptInvoice = new ReceiptInvoice();
					Office office = dealInfo.getCreateBy().getOffice();
					List<ReceiptDepotInfo> depotInfos = receiptDepotInfoService.findDepotByOffice(office);
					receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
					receiptInvoice.setCompanyName(workDealInfo.getWorkCompany().getCompanyName());
					receiptInvoice.setReceiptMoney(newAdd - oldAdd);
					receiptInvoice.setReceiptType(0);
					receiptInvoice.setDealInfoId(workDealInfo.getId());
					receiptInvoiceService.save(receiptInvoice);
					ReceiptDepotInfo receiptDepotInfo = depotInfos.get(0);
					receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue() - (newAdd - oldAdd));
					receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut() + (newAdd - oldAdd));
					receiptDepotInfoService.save(receiptDepotInfo);
				} else if (newAdd < oldAdd) {
					FinanceQuitMoney quitMoney = new FinanceQuitMoney();
					quitMoney.setQuitMoney(oldAdd - newAdd);
					quitMoney.setQuitDate(new Date());
					quitMoney.setQuitWindow(UserUtils.getUser().getOffice().getName());
					quitMoney.setQuitReason("变更缴费类型退费");
					quitMoney.setRession("变更缴费类型退费");
					quitMoney.setWorkDealInfo(dealInfo);
					quitMoney.setStatus("1");
					financeQuitMoneyService.save(quitMoney);
				}
			} else if (iseq.equals(3)) {
				workPayInfo.setOpenAccountMoney(payInfo.getOpenAccountMoney());
				workPayInfo.setAddCert(payInfo.getAddCert());
				workPayInfo.setUpdateCert(payInfo.getUpdateCert());
				workPayInfo.setErrorReplaceCert(payInfo.getErrorReplaceCert());
				workPayInfo.setLostReplaceCert(payInfo.getLostReplaceCert());
				workPayInfo.setInfoChange(payInfo.getInfoChange());
				workPayInfo.setElectron(payInfo.getElectron());
				if (payInfo.getMoney() > 0d) {
					workPayInfo.setMoney(payInfo.getMoney());
				}
				if (payInfo.getAlipayMoney() > 0d) {
					workPayInfo.setAlipayMoney(payInfo.getAlipayMoney());
				}
				if (payInfo.getPosMoney() > 0d) {
					workPayInfo.setPosMoney(payInfo.getPosMoney());
				}
				if (payInfo.getBankMoney() > 0d) {
					workPayInfo.setBankMoney(payInfo.getBankMoney());
				}
				workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
				workPayInfo.setWorkTotalMoney(payInfo.getWorkTotalMoney());
				workPayInfo.setWorkPayedMoney(0d);
				workPayInfo.setWorkReceivaMoney(payInfo.getWorkReceivaMoney());
				workPayInfo.setReceiptAmount(payInfo.getReceiptAmount());
				workPayInfo.setMethodMoney(payInfo.getMethodMoney());
				workPayInfo.setMethodAlipay(payInfo.getMethodAlipay());
				workPayInfo.setMethodBank(payInfo.getMethodBank());
				workPayInfo.setMethodContract(payInfo.getMethodContract());
				workPayInfo.setMethodGov(payInfo.getMethodGov());
				workPayInfo.setMethodPos(payInfo.getMethodPos());
				workPayInfoService.save(workPayInfo);
			}
			workDealInfo.setWorkPayInfo(workPayInfo);
			workDealInfo.setLastDays(dealInfo.getLastDays());

			ConfigChargeAgent agent = chargeAgentService.get(agentDetailId);

			workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));
			workDealInfo.setConfigChargeAgentId(agentDetailId);
			

			workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
			workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
			
			workDealInfoService.save(workDealInfo);

			workDealInfoService.delete(dealInfo.getId());

			ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
			dealInfoBound.setDealInfo(workDealInfo);
			dealInfoBound.setAgent(agent);
			configAgentBoundDealInfoService.save(dealInfoBound);
			logUtil.saveSysLog("计费策略模版", "计费策略模版：" + dealInfo.getId() + "--业务编号：" + dealInfo.getId() + "--关联成功!", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtil.saveSysLog("业务办理", "变更缴费方式：编号" + dealInfo.getId(), "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/list";
	}
	
	/**
	 * 验证证书
	 * 更改鉴别状态，加入鉴别人  返回审核列表
	 * @return
	 */
	@RequestMapping("verifyDealInfo")
	public String verifyDealInfo(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());	
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);

		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_VERIFICATE_WAIT);

		workDealInfo.setAttestationUser(UserUtils.getUser());
		workDealInfo.setAttestationUserDate(new Date());
		workDealInfoService.save(workDealInfo);
		
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/list";
	}	
	
	/**
	 * 制作证书
	 * 
	 * @return
	 */
	@RequestMapping("makeDealInfo")
	public String makeDealInfo(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		//新增
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());	
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		if(workDealInfo.getDealInfoType()!=null&&workDealInfo.getDealInfoType()==0){
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_APPROVE_WAIT);
			workDealInfo.setVerifyUser(UserUtils.getUser());
			workDealInfo.setVerifyUserDate(new Date());
			workDealInfoService.save(workDealInfo);
			
			//秘钥长度
			if(raAccount.getKeyLen()!=null){
				model.addAttribute("keyLen", raAccount.getKeyLen());
			}
			
			
			//经信委
			if(workDealInfo.getExpirationDate()!=null){
				model.addAttribute("expirationDate", workDealInfo.getExpirationDate());
				model.addAttribute("addCertDays",StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate())-workDealInfo.getYear()*365);
				model.addAttribute("validiteDays",StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate()));
			}
			
			model.addAttribute("pt", ProductType.productTypeStrMap);
			model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
			return "modules/work/workDealInfoAuditMake";
		}else{
			
			//其他
			workDealInfo = workDealInfoService.get(workDealInfo.getId());
			model.addAttribute("pt", ProductType.productTypeStrMap);
			model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
			
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_APPROVE_WAIT);
			workDealInfo.setVerifyUser(UserUtils.getUser());
			workDealInfo.setVerifyUserDate(new Date());
			workDealInfoService.save(workDealInfo);
			
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
		
	}

	/**
	 * 删除处理
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workDealInfoService.delete(id);
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		
		Long prevId = workDealInfo.getPrevId();
		if (prevId != null) {
			workDealInfoService.deleteReturnById(prevId);
		}
		Long agentId = workDealInfo.getConfigChargeAgentId();
		ConfigChargeAgent agent = configChargeAgentService.get(agentId);
		agent.setSurplusNum(agent.getSurplusNum() + 1);
		agent.setReserveNum(agent.getReserveNum() - 1);
		configChargeAgentService.save(agent);

		ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(), id);
		if (bound != null) {
			configAgentBoundDealInfoService.deleteById(bound.getId());
		}

		addMessage(redirectAttributes, "删除业务成功");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/exceptionList?dealInfoStatus=1&repage";
	}

	/**
	 * 编辑页面
	 */
	@RequestMapping("updateDealInfoFrom")
	public String pudateDealInfoFrom(WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("workDealInfo", workDealInfo);
		return "modules/work/workDealInfoAuditUpdate";
	}

	private Integer getDealInfoAddType(WorkDealInfo dealInfo) {
		List<Integer> allTypes = new ArrayList<Integer>();
		allTypes.add(dealInfo.getDealInfoType());
		allTypes.add(dealInfo.getDealInfoType1());
		allTypes.add(dealInfo.getDealInfoType2());
		allTypes.add(dealInfo.getDealInfoType3());

		if (allTypes.contains(WorkDealInfoType.TYPE_UPDATE_CERT)) {
			return 1;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
			return dealInfo.getManMadeDamage() ? 5 : 4;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_LOST_CHILD)) {
			return 5;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_INFORMATION_REROUTE)) {
			return 6;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_ADD_CERT)) {
			return -1;
		}
		return -1;
	}

	/**
	 * 根据类型查询供应商
	 * 
	 * @param productType
	 * @return
	 */
	private ConfigSupplier getSupplier(Integer productType) {
		try {
			DetachedCriteria dc = configSupplierProductRelationDao.createDetachedCriteria();
			dc.createAlias("configSupplier", "configSupplier");
			dc.add(Restrictions.eq("productType", productType));
			dc.add(Restrictions.eq("configSupplier.delFlag", ConfigSupplier.DEL_FLAG_NORMAL));
			List<ConfigSupplierProductRelation> relations = configSupplierProductRelationDao.find(dc);
			return relations.get(0).getConfigSupplier();
		} catch (Exception e) {
			return null;
		}
	}

	static Long MILL = 86400000L;

	private int getLastCertDay(Date notAfter) {
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong < nowLong) {
			return 0;
		}
		long d = (notAfterLong - nowLong)/MILL;
		long hour1=(notAfterLong - nowLong)%MILL;
		if (hour1>0) {
			d+=1;
		}
		return (int) d;
	}

	@RequestMapping(value = "getAgentMoney")
	@ResponseBody
	public String getAgentMoney(Long dealInfoId, Long newAgentId) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
			Double oldAdd = 0d;
			Double newAdd = 0d;
			if (dealInfo.getDealInfoType() != null) {
				if (dealInfo.getDealInfoType().equals(0)) {// 新增
					
					//开户费
					oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),4, 0);
					newAdd += configChargeAgentDetailService.getChargeMoney(newAgentId, 4,0);
					//新增费
					oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
							dealInfo.getDealInfoType(), dealInfo.getYear());
					newAdd += configChargeAgentDetailService.getChargeMoney(newAgentId, dealInfo.getDealInfoType(),
							dealInfo.getYear());

				} else if (dealInfo.getDealInfoType().equals(1)) {// 更新
					oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
							dealInfo.getDealInfoType(), dealInfo.getYear());
					newAdd += configChargeAgentDetailService.getChargeMoney(newAgentId, dealInfo.getDealInfoType(),
							dealInfo.getYear());

				}

			}
			if (dealInfo.getDealInfoType1() != null) {
				if (dealInfo.getDealInfoType1().equals(2)) {
					oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
							dealInfo.getDealInfoType(), null);
					newAdd += configChargeAgentDetailService.getChargeMoney(newAgentId, dealInfo.getDealInfoType(),
							null);

				} else if (dealInfo.getDealInfoType1().equals(3)) {
					oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
							dealInfo.getDealInfoType(), null);
					newAdd += configChargeAgentDetailService.getChargeMoney(newAgentId, dealInfo.getDealInfoType(),
							null);
				}

			}
			if (dealInfo.getDealInfoType2() != null) {
				if (dealInfo.getDealInfoType2().equals(4)) {
					oldAdd += configChargeAgentDetailService.getChargeMoney(dealInfo.getConfigChargeAgentId(),
							dealInfo.getDealInfoType(), null);
					newAdd += configChargeAgentDetailService.getChargeMoney(newAgentId, dealInfo.getDealInfoType(),
							null);
				}
			}
			json.put("oldAdd", oldAdd);
			json.put("newAdd", newAdd);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 1);
		}

		json.put("status", 0);

		return json.toString();
	}

	/**
	 * 老的变更缴费类型方法 退费处理页面 （改变缴费方式）
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * 
	 * 		@RequestMapping("backMoney") public String backMoney(Long id,
	 *         HttpServletRequest request, HttpServletResponse response, String
	 *         receiptAmount, Long agentDetailId, Model model) { WorkDealInfo
	 *         dealInfo = workDealInfoService.get(id); // 修改状态为退费 0为新建 1为异常 //
	 *         workDealInfo.setDealInfoStatus("2"); //
	 *         workDealInfoService.save(workDealInfo); // 查询当前业务完成时间
	 *         if("1".equals(dealInfo.getDelFlag())){//判断是否进行过退费，防止浏览器回退再次退费
	 *         return "redirect:" + Global.getAdminPath() +
	 *         "/work/workDealInfo/list";
	 * 
	 *         } WorkPayInfo payInfo = dealInfo.getWorkPayInfo(); try {
	 * 
	 * 
	 * 
	 *         // boolean needFixStatistic = false;
	 * 
	 *         // List<Integer> fixTypes = new ArrayList<Integer>(); //
	 *         fixTypes.add(WorkDealInfoType.TYPE_ADD_CERT); //
	 *         fixTypes.add(WorkDealInfoType.TYPE_UPDATE_CERT); // if
	 *         (payInfo.getMethodAlipay() || payInfo.getMethodBank() // ||
	 *         payInfo.getMethodMoney() || payInfo.getMethodPos()) { //
	 *         needFixStatistic = true; // } // Integer type = 2;// 更新 // if
	 *         (dealInfo.getDealInfoType() != null &&
	 *         dealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)
	 *         ) { // type = 1;// 新增 // } // Integer year = dealInfo.getYear();
	 *         // if (fixTypes.contains(dealInfo.getDealInfoType()) // ||
	 *         fixTypes.contains(dealInfo.getDealInfoType())) {// 修正历史数据 //
	 *         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //
	 *         Date date = dealInfo.getObtainedDate(); // if (needFixStatistic
	 *         && date != null) { // try { // Date staticsDate =
	 *         sdf.parse(sdf.format(date)); // List<StatisticCertData> certData
	 *         = statisticCertDataService //
	 *         .countHisData(dealInfo.getConfigApp(), staticsDate); // if
	 *         (certData != null) { // StatisticCertData self = new
	 *         StatisticCertData(); // StatisticCertData contract = new
	 *         StatisticCertData(); // StatisticCertData gov = new
	 *         StatisticCertData(); // StatisticCertData fixObj = new
	 *         StatisticCertData(); // for (StatisticCertData d : certData) { //
	 *         if (d.getPayType().equals(0)) { // self = d; // } // if
	 *         (d.getPayType().equals(1)) { // gov = d; // } // if
	 *         (d.getPayType().equals(2)) { // contract = d; // } // } // if
	 *         (method.equals("0")) { // fixObj = gov; // } else { // fixObj =
	 *         contract; // } // if (type.equals(2)) {// 更新 // switch (year) {
	 *         // case 1: // self.setRenew1(self.getRenew1() - 1); //
	 *         fixObj.setRenew1(fixObj.getRenew1() + 1); // break; // case 2: //
	 *         self.setRenew2(self.getRenew2() - 1); //
	 *         fixObj.setRenew2(fixObj.getRenew2() + 1); // break; // case 4: //
	 *         self.setRenew4(self.getRenew4() - 1); //
	 *         fixObj.setRenew4(fixObj.getRenew4() + 1); // break; // } // }
	 *         else {// 新增 // switch (year) { // case 1: //
	 *         self.setAdd1(self.getAdd1() - 1); //
	 *         fixObj.setAdd1(fixObj.getAdd1() + 1); // break; // case 2: //
	 *         self.setAdd2(self.getAdd2() - 1); //
	 *         fixObj.setAdd2(fixObj.getAdd2() + 1); // break; // case 4: //
	 *         self.setAdd4(self.getAdd4() - 1); //
	 *         fixObj.setAdd4(fixObj.getAdd4() + 1); // break; // } // } //
	 *         statisticCertDataService.save(self); //
	 *         statisticCertDataService.save(fixObj); // } // } catch (Exception
	 *         e) { // e.printStackTrace(); // } // } // } //
	 *         生成新的业务，类型为退费,保存原来的payInfo WorkDealInfo workDealInfo = new
	 *         WorkDealInfo();
	 *         workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_PAY_REPLACED);
	 *         workDealInfo.setYear(dealInfo.getYear());
	 *         workDealInfo.setConfigApp(dealInfo.getConfigApp());
	 *         ConfigCommercialAgent commercialAgent =
	 *         configAgentAppRelationService
	 *         .findAgentByApp(workDealInfo.getConfigApp());
	 *         workDealInfo.setConfigCommercialAgent(commercialAgent);
	 *         workDealInfo.setConfigCommercialAgent(dealInfo
	 *         .getConfigCommercialAgent()); List
	 *         <ConfigAgentOfficeRelation> configAgentOfficeRelations =
	 *         configAgentOfficeRelationService
	 *         .findByOffice(UserUtils.getUser().getOffice()); if
	 *         (configAgentOfficeRelations.size() > 0) {
	 *         workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0)
	 *         .getConfigCommercialAgent());// 劳务关系外键 } //取上次业务多证书编号
	 *         workDealInfo.setCertSort(dealInfo.getCertSort());
	 *         workDealInfo.setWorkUser(dealInfo.getWorkUser());
	 *         workDealInfo.setWorkCompany(dealInfo.getWorkCompany());
	 *         workDealInfo.setWorkUserHis(dealInfo.getWorkUserHis());
	 *         workDealInfo.setWorkCompanyHis(dealInfo.getWorkCompanyHis());
	 *         workDealInfo.setConfigProduct(dealInfo.getConfigProduct());
	 *         workDealInfo.setDealInfoStatus(WorkDealInfoStatus.
	 *         STATUS_CERT_OBTAINED);
	 *         workDealInfo.setCreateBy(UserUtils.getUser());
	 *         workDealInfo.setCreateDate(new Date());
	 *         //workDealInfo.setSvn(workDealInfoService.getSVN(0));
	 *         workDealInfo.setSvn(dealInfo.getSvn());
	 *         workDealInfo.setPrevId(dealInfo.getId()); // 证书获取时间
	 *         workDealInfo.setObtainedDate(new Date()); // workcertinfo继承上次业务的
	 *         WorkCertInfo workCertInfo =
	 *         workCertInfoService.get(dealInfo.getWorkCertInfo().getId());
	 *         workDealInfo.setKeySn(dealInfo.getKeySn());
	 *         workDealInfo.setNotafter(dealInfo.getNotafter());
	 *         workDealInfo.setWorkCertInfo(workCertInfo);
	 * 
	 *         // 新建payInfo保存原来的payInfo数据 WorkPayInfo workPayInfo = new
	 *         WorkPayInfo();
	 *         workPayInfo.setOpenAccountMoney(payInfo.getOpenAccountMoney());
	 *         workPayInfo.setAddCert(payInfo.getAddCert());
	 *         workPayInfo.setUpdateCert(payInfo.getUpdateCert());
	 *         workPayInfo.setErrorReplaceCert(payInfo.getErrorReplaceCert());
	 *         workPayInfo.setLostReplaceCert(payInfo.getLostReplaceCert());
	 *         workPayInfo.setInfoChange(payInfo.getInfoChange());
	 *         workPayInfo.setElectron(payInfo.getElectron()); if
	 *         (payInfo.getMoney() > 0d) {
	 *         workPayInfo.setMoney(-payInfo.getMoney()); } if
	 *         (payInfo.getAlipayMoney() > 0d) {
	 *         workPayInfo.setAlipayMoney(-payInfo.getAlipayMoney()); } if
	 *         (payInfo.getPosMoney() > 0d) {
	 *         workPayInfo.setPosMoney(-payInfo.getPosMoney()); } if
	 *         (payInfo.getBankMoney() > 0d) {
	 *         workPayInfo.setBankMoney(-payInfo.getBankMoney()); }
	 *         workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
	 *         workPayInfo.setWorkTotalMoney(-payInfo.getWorkTotalMoney());
	 *         workPayInfo.setWorkPayedMoney(0d);
	 *         workPayInfo.setWorkReceivaMoney(payInfo.getWorkReceivaMoney());
	 *         workPayInfo .setReceiptAmount(payInfo.getReceiptAmount() -
	 *         Double.valueOf(receiptAmount.equals("") ? "0" : receiptAmount));
	 *         workPayInfo.setMethodMoney(payInfo.getMethodMoney());
	 *         workPayInfo.setMethodAlipay(payInfo.getMethodAlipay());
	 *         workPayInfo.setMethodBank(payInfo.getMethodBank());
	 *         workPayInfo.setMethodContract(payInfo.getMethodContract());
	 *         workPayInfo.setMethodGov(payInfo.getMethodGov());
	 *         workPayInfo.setMethodPos(false);
	 *         workPayInfoService.save(workPayInfo);
	 *         workDealInfo.setWorkPayInfo(workPayInfo);
	 *         workDealInfo.setLastDays(dealInfo.getLastDays());
	 * 
	 *         ConfigChargeAgent agent = chargeAgentService.get(agentDetailId);
	 * 
	 * 
	 *         workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));
	 *         workDealInfo.setConfigChargeAgentId(agent.getId());
	 * 
	 *         workDealInfoService.save(workDealInfo);
	 * 
	 *         // 原来的payInfo改变付款方式 payInfo.setMethodMoney(false);
	 *         payInfo.setMethodAlipay(false); payInfo.setMethodBank(false); //
	 *         payInfo.setMethodContract(method.equals("0") ? false : true); //
	 *         payInfo.setMethodGov(method.equals("1") ? false : true);
	 *         payInfo.setMethodPos(false); payInfo.setMoney(0d);
	 *         payInfo.setPosMoney(0d); payInfo.setBankMoney(0d);
	 *         payInfo.setAlipayMoney(0d); workPayInfoService.save(payInfo);
	 *         dealInfo.setWorkPayInfo(payInfo);
	 *         workDealInfoService.save(dealInfo);// 删除以前的dealinfo使其不再列表显示
	 * 
	 *         workDealInfoService.delete(dealInfo.getId());
	 * 
	 *         // 变更relation表money变成负数，退还绑定钱数，次数。 // Iterator
	 *         <WorkFinancePayInfoRelation> iterator = payInfo //
	 *         .getWorkFinancePayInfoRelations().iterator(); // while
	 *         (iterator.hasNext()) { // WorkFinancePayInfoRelation relation =
	 *         iterator.next(); // FinancePaymentInfo fpr =
	 *         relation.getFinancePaymentInfo(); //
	 *         fpr.setBingdingTimes(fpr.getBingdingTimes() - 1); // //
	 *         fpr.setPaymentMoney(fpr.getPaymentMoney()+workDealInfo.
	 *         getWorkPayInfo().getWorkReceivaMoney()); //
	 *         fpr.setResidueMoney(fpr.getResidueMoney() + relation.getMoney());
	 *         // financePaymentInfoService.save(fpr); //
	 *         WorkFinancePayInfoRelation financePayInfoRelation = new
	 *         WorkFinancePayInfoRelation(); //
	 *         financePayInfoRelation.setMoney(-relation.getMoney()); //
	 *         financePayInfoRelation.setFinancePaymentInfo(fpr); //
	 *         financePayInfoRelation.setWorkPayInfo(workPayInfo); //
	 *         financePayInfoRelation.setSn(workPayInfo.getSn()); //
	 *         workFinancePayInfoRelationService.save(financePayInfoRelation);
	 *         // } // if (!receiptAmount.equals("") // &&
	 *         Double.valueOf(receiptAmount) != 0d // &&
	 *         Double.valueOf(receiptAmount) != 0d) { // // 当实际余额 大约 库存余额 //
	 *         ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService //
	 *         .findDepotByOffice(dealInfo.getCreateBy().getOffice()) //
	 *         .get(0); // // 修改余额 //
	 *         receiptDepotInfo.setReceiptResidue(receiptDepotInfo //
	 *         .getReceiptResidue() + Double.valueOf(receiptAmount)); // //
	 *         创建入库信息 // ReceiptEnterInfo receiptEnterInfo = new
	 *         ReceiptEnterInfo(); //
	 *         receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo); //
	 *         receiptEnterInfo.setNow_Money(Double.valueOf(receiptAmount)); //
	 *         receiptEnterInfo.setBeforMoney(receiptEnterInfo //
	 *         .getReceiptDepotInfo().getReceiptResidue() // -
	 *         Double.valueOf(receiptAmount)); //
	 *         receiptEnterInfo.setReceiptMoney(receiptEnterInfo //
	 *         .getReceiptDepotInfo().getReceiptResidue()); //
	 *         receiptEnterInfo.setReceiptType(3);// 退费入库 //
	 *         receiptEnterInfoService.save(receiptEnterInfo); //
	 *         logUtil.saveSysLog("业务办理退费", // "库房" +
	 *         receiptDepotInfo.getReceiptName() + "添加入库信息成功", // ""); //
	 *         receiptDepotInfoService.save(receiptDepotInfo); //
	 *         logUtil.saveSysLog("业务办理退费", // "修改库房" +
	 *         receiptDepotInfo.getReceiptName() // + "发票余额，异常入库", ""); // }
	 * 
	 * 
	 *         } catch (Exception e) { e.printStackTrace(); }
	 *         logUtil.saveSysLog("业务办理", "异常业务退费：编号" + dealInfo.getId(), "");
	 *         return "redirect:" + Global.getAdminPath() +
	 *         "/work/workDealInfo/list"; }
	 */

}
