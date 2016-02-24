/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCertTrustApply;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCertTrustApplyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;

/**
 * 申请移动设备数量Controller
 * @author ZhangJingtao
 * @version 2014-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workCertTrustApply")
public class WorkCertTrustApplyController extends BaseController {

	@Autowired
	private WorkCertTrustApplyService workCertTrustApplyService;
	
	@Autowired
	private WorkLogService logService;

	@Autowired
	private WorkDealInfoService dealInfoService;
	
	@Autowired
	private ConfigChargeAgentService configChargeAgentService;
	
	@Autowired
	private WorkPayInfoService workPayInfoService;
	
	@Autowired
	FinancePaymentInfoService financePaymentInfoService;
	
	@Autowired
	WorkFinancePayInfoRelationService workFinancePayInfoRelationService;
	
	@Autowired
	WorkCertInfoService workCertInfoService;
	
	@Autowired
	ReceiptInvoiceService receiptInvoiceService;
	
	@ModelAttribute
	public WorkCertTrustApply get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workCertTrustApplyService.get(id);
		}else{
			return new WorkCertTrustApply();
		}
	}
	
	@RequiresPermissions("work:workCertTrustApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkCertTrustApply workCertTrustApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<WorkCertTrustApply> page = workCertTrustApplyService.find(new Page<WorkCertTrustApply>(request, response), workCertTrustApply); 
        model.addAttribute("page", page);
		return "modules/work/workCertTrustApplyList";
	}

	@RequiresPermissions("work:workCertTrustApply:view")
	@RequestMapping(value = "form")
	public String form(WorkCertTrustApply workCertTrustApply, Model model) {
		model.addAttribute("workCertTrustApply", workCertTrustApply);
		return "modules/work/workCertTrustApplyForm";
	}

	@RequiresPermissions("work:workCertTrustApply:edit")
	@RequestMapping(value = "save")
	public String save(WorkCertTrustApply workCertTrustApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workCertTrustApply)){
			return form(workCertTrustApply, model);
		}
		workCertTrustApplyService.save(workCertTrustApply);
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCertTrustApply/?repage";
	}
	
	@RequiresPermissions("work:workCertTrustApply:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workCertTrustApplyService.delete(id);
		addMessage(redirectAttributes, "删除申请移动设备数量成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCertTrustApply/?repage";
	}
	
	@RequestMapping(value="auditFrom")
	public String auditFrom(Long id,Model model){
		WorkCertTrustApply workCertTrustApply = workCertTrustApplyService.get(id);
		model.addAttribute("trust", workCertTrustApply);
		model.addAttribute("cert", workCertTrustApply.getWorkCertInfo());
     	Set<WorkDealInfo> infos = workCertTrustApply.getWorkCertInfo().getWorkDealInfos();
		model.addAttribute("dealInfo", infos.toArray()[0]);
		model.addAttribute("productType", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		return "modules/work/workCertTrustApplyAuditForm";
	}
	
	@RequestMapping(value = "auditNext")
	public String auditNext(Long id,Long dealInfoId,Long workCompanyId,Long appId,String recordContent,Model model){
		WorkLog log = new WorkLog();
		ConfigApp app = new ConfigApp();
		app.setId(appId);
		log.setConfigApp(app);
		WorkCompany company = new WorkCompany();
		company.setId(workCompanyId);
		log.setWorkCompany(company);
		WorkDealInfo dealInfo = new WorkDealInfo();
		dealInfo.setId(dealInfoId);
		log.setWorkDealInfo(dealInfo);
		log.setRecordContent(recordContent);
		logService.save(log);
		WorkDealInfo workDealInfo = dealInfoService.get(dealInfoId);
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigChargeAgentId());
		
		model.addAttribute("applyId", id);
		model.addAttribute("apply", workCertTrustApplyService.get(id));
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("product", ProductType.productTypeStrMap);
		model.addAttribute("chargeAgent", chargeAgent);
		model.addAttribute("officeName", UserUtils.getUser().getOffice().getName());
		model.addAttribute("userName", UserUtils.getUser().getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		return "modules/work/workCertTrustApplyAuditPaymentForm";
	}
	
	@RequestMapping(value="audit")
	public String audit(Integer agree,Long applyId,@RequestParam(required=false)String suggest,RedirectAttributes redirectAttributes){
		//保存workPaymentInfo
		
		//更新workTrustApply状态
		//更新workCertInfo中可信设备数量的状态
		if (agree==0) {//拒绝
			WorkCertTrustApply apply = workCertTrustApplyService.get(applyId);
			apply.setStatus(-1);
			apply.setSuggest(suggest);
			workCertTrustApplyService.save(apply);
		}
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+Global.getAdminPath()+"/work/workDealInfoAudit/listTrustCountApply";
	}
	
	@RequestMapping(value = "forbidForm")
	public String forbidForm(Long applyId,Model model){
		model.addAttribute("id", applyId);
		return "modules/work/workCertTrustApplyAuditForbidForm";
	}

	/**
	 * 保存财务信息
	 * @param workDealInfoId
	 * @param openAccountMoney
	 * @param addCert
	 * @param updateCert
	 * @param errorReplaceCert
	 * @param lostReplaceCert
	 * @param infoChange
	 * @param electron
	 * @param methodMoney
	 * @param methodPos
	 * @param methodBank
	 * @param methodAlipay
	 * @param methodGov
	 * @param methodContract
	 * @param money
	 * @param posMoney
	 * @param bankMoney
	 * @param alipayMoney
	 * @param workTotalMoney
	 * @param workPayedMoney
	 * @param workReceivaMoney
	 * @param userReceipt
	 * @param receiptAmount
	 * @param financePayInfoId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="saveWorkPayInfo")
	public String saveWorkPayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, 
			Double updateCert, Double errorReplaceCert,Double lostReplaceCert, Double infoChange,
			Double electron, Integer methodMoney, Integer methodPos, Integer methodBank,
			Integer methodAlipay, Integer methodGov, Integer methodContract, Double money, 
			Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney, 
			Double workPayedMoney , Double workReceivaMoney, Boolean userReceipt, 
			Double receiptAmount, Long[] financePayInfoId,
			RedirectAttributes redirectAttributes,Long applyId){
		WorkCertTrustApply apply = workCertTrustApplyService.get(applyId);
		WorkCertInfo certInfo = apply.getWorkCertInfo();
		WorkDealInfo workDealInfo = dealInfoService.findByKeySnWithOutDel(apply.getKeySn(), apply.getCertSn()).get(0);
		//首先检查可信移动设备数量是否够用
		if (methodGov!=null&&methodGov==1) {
			ConfigApp app = workDealInfo.getConfigApp();
			if (app.getGovDeviceAmount()!=null) {//数量限制不是null
				Long appId = workDealInfo.getConfigApp().getId();
				Integer curValidDevice = dealInfoService.getValidDeviceTotal(appId);
				if (curValidDevice+Integer.valueOf(apply.getApplyCount())>app.getGovDeviceAmount()) {//数量超过...
					addMessage(redirectAttributes, "当前应用可信移动设备数量超过最大限制");
					return "redirect:"+Global.getAdminPath()+"/work/workDealInfoAudit/listTrustCountApply";
				}
			}
		}
		
		WorkPayInfo workPayInfo = new WorkPayInfo();
		workPayInfo.setOpenAccountMoney(openAccountMoney);
		workPayInfo.setAddCert(addCert);
		workPayInfo.setUpdateCert(updateCert);
		workPayInfo.setErrorReplaceCert(errorReplaceCert);
		workPayInfo.setLostReplaceCert(lostReplaceCert);
		workPayInfo.setInfoChange(infoChange);
		workPayInfo.setElectron(electron);

		double bindMoney = workTotalMoney;
		
		if (methodMoney != null && methodMoney == 1) {
			workPayInfo.setMethodMoney(true);
			bindMoney = bindMoney - money;
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			bindMoney = bindMoney - posMoney;
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			bindMoney = bindMoney-bankMoney;
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			bindMoney = bindMoney - alipayMoney;
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setPosMoney(posMoney);
		workPayInfo.setMoney(money);
		workPayInfo.setBankMoney(bankMoney);
		workPayInfo.setAlipayMoney(alipayMoney);
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);

		apply.setWorkPayInfo(workPayInfo);
		if (workPayInfo.getUserReceipt()) {
			receiptInvoiceService.receiptIncoiceIYDSBSL(workPayInfo.getReceiptAmount(),UserUtils.getUser().getOffice(),workDealInfo.getWorkCompany().getCompanyName(),workDealInfo.getId());				//key出库
		}
		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney==0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney
							- financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double)0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney()-bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				financePayInfoRelation
						.setFinancePaymentInfo(financePaymentInfo);
				financePayInfoRelation.setWorkPayInfo(workPayInfo);
				financePayInfoRelation.setSn(System.currentTimeMillis()+"");
				workFinancePayInfoRelationService.save(financePayInfoRelation);
			}
		}
		int count = 0;
		if (certInfo.getTrustDeviceCount()!=null) {
			count = certInfo.getTrustDeviceCount();
		}
		certInfo.setTrustDeviceCount(apply.getApplyCount()+count);
		certInfo.setTrustDeviceDate(certInfo.getNotafter());
		workCertInfoService.save(certInfo);
		
		addMessage(redirectAttributes, "操作成功");
		apply.setStatus(1);
		workCertTrustApplyService.save(apply);
		return "redirect:"+Global.getAdminPath()+"/work/workDealInfoAudit/listTrustCountApply";
	}
}
