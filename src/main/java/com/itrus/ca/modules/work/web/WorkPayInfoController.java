/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;

/**
 * 绑定信息Controller
 * 
 * @author HUHAO
 * @version 2014-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workPayInfo")
public class WorkPayInfoController extends BaseController {

	@Autowired
	private WorkPayInfoService workPayInfoService;

	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;

	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private KeyUsbKeyInvoiceService keyInvoiceService;
	
	@Autowired
	ConfigRaAccountService raAccountService;
	
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public WorkPayInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return workPayInfoService.get(id);
		} else {
			return new WorkPayInfo();
		}
	}

	@RequiresPermissions("work:workPayInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(WorkPayInfo workPayInfo, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// if (!user.isAdmin()){
		// workPayInfo.setCreateBy(user);
		// }
		Page<WorkPayInfo> page = workPayInfoService.find(new Page<WorkPayInfo>(
				request, response), workPayInfo);
		model.addAttribute("page", page);
		return "modules/work/workPayInfoList";
	}

	@RequiresPermissions("work:workPayInfo:view")
	@RequestMapping(value = "form")
	public String form(WorkPayInfo workPayInfo, Model model) {
		model.addAttribute("workPayInfo", workPayInfo);
		return "modules/work/workPayInfoForm";
	}

	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "save")
	public String save(WorkPayInfo workPayInfo, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workPayInfo)) {
			return form(workPayInfo, model);
		}

		workPayInfoService.save(workPayInfo);
		// addMessage(redirectAttributes, "保存绑定信息'" + workPayInfo.getName() +
		// "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/work/workPayInfo/?repage";
	}

	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workPayInfoService.delete(id);
		addMessage(redirectAttributes, "删除绑定信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/work/workPayInfo/?repage";
	}

	/**
	 * 新建业务缴费信息保存
	 * 
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
	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "savePayInfo")
	public String savePayInfo(Long workDealInfoId, Double openAccountMoney,
			Double addCert, Double updateCert, Double errorReplaceCert,
			Double lostReplaceCert, Double infoChange, Double electron,
			Integer methodMoney, Integer methodPos, Integer methodBank,
			Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney,
			Double alipayMoney, Double workTotalMoney, Double workPayedMoney,
			Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, RedirectAttributes redirectAttributes) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
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
			if (bindMoney<money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney<posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney<bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			
			if (bindMoney<alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - alipayMoney;
				workPayInfo.setAlipayMoney(alipayMoney);
			}
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);
		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney == 0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
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
					workTotalMoney = (double) 0;
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
		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);
		workDealInfoService.save(workDealInfo);
		
		logUtil.saveSysLog("业务中心", "业务新增：编号"+workDealInfo.getId()+"缴费"+workTotalMoney+"元", "");
		addMessage(redirectAttributes, "缴费成功");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	/**
	 * 更新保存缴费信息
	 * 
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
	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "maintainSavePayInfo")
	public String maintainSavePayInfo(Long workDealInfoId,
			Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange,
			Double electron, Integer methodMoney, Integer methodPos,
			Integer methodBank, Integer methodAlipay, Integer methodGov,
			Integer methodContract, Double money, Double posMoney,
			Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney,
			Boolean userReceipt, Double receiptAmount, Long[] financePayInfoId,
			Double oldOpenAccountMoney,Double oldAddCert, Double oldUpdateCert,
			Double oldErrorReplaceCert, Double oldLostReplaceCert, 
			Double oldElectron, Double oldInfoChange,
			RedirectAttributes redirectAttributes, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
		WorkPayInfo workPayInfo = new WorkPayInfo();
		workPayInfo.setOpenAccountMoney(openAccountMoney);
		workPayInfo.setAddCert(addCert);
		workPayInfo.setUpdateCert(updateCert);
		workPayInfo.setErrorReplaceCert(errorReplaceCert);
		workPayInfo.setLostReplaceCert(lostReplaceCert);
		workPayInfo.setInfoChange(infoChange);
		workPayInfo.setElectron(electron);
		workPayInfo.setOldOpenAccountMoney(oldOpenAccountMoney);
		workPayInfo.setOldAddCert(oldAddCert);
		workPayInfo.setOldUpdateCert(oldUpdateCert);
		workPayInfo.setOldErrorReplaceCert(oldErrorReplaceCert);
		workPayInfo.setOldLostReplaceCert(oldLostReplaceCert);
		workPayInfo.setOldInfoChange(oldInfoChange);
		workPayInfo.setOldElectron(oldElectron);

		double bindMoney = workTotalMoney;


		if (methodMoney != null && methodMoney == 1) {
			workPayInfo.setMethodMoney(true);
			if (bindMoney<money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney<posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney<bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			
			if (bindMoney<alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - alipayMoney;
				workPayInfo.setAlipayMoney(alipayMoney);
			}
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);
		

		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney == 0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
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
					workTotalMoney = (double) 0;
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
		if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_ADD_CERT) {
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ADD_USER);
		} else {
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_WAIT);
		}
		
		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);
		workDealInfoService.save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("workDealInfo", workDealInfo);
		logUtil.saveSysLog("业务中心", "业务维护：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
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
				if (workDealInfo.getIsIxin()!=null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType1() != null) {
			if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType1()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin()!=null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType1().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType1().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType2() != null) {
			if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType2()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin()!=null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType2().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType2().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType3() != null) {
			if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType3()
							.equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin()!=null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType3().equals(
					WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType3().equals(
							WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		return "modules/work/workDealInfoMaintainAuditMake";
	}

	/**
	 * 变更保存缴费信息
	 * 
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
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "changeSavePayInfo")
	public String changeSavePayInfo(Long workDealInfoId,
			Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange,
			Double electron, Integer methodMoney, Integer methodPos,
			Integer methodBank, Integer methodAlipay, Integer methodGov,
			Integer methodContract, Double money, Double posMoney,
			Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney,
			Boolean userReceipt, Double receiptAmount, Long[] financePayInfoId,
			RedirectAttributes redirectAttributes, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
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
			if (bindMoney<money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney<posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney<bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			
			if (bindMoney<alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - alipayMoney;
				workPayInfo.setAlipayMoney(alipayMoney);
			}
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);


		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney == 0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
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
					workTotalMoney = (double) 0;
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
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_WAIT);
		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);
		workDealInfoService.save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		logUtil.saveSysLog("业务中心", "业务变更：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
		addMessage(redirectAttributes, "缴费成功");
		if (workDealInfo.getPrevId()!=null) {
			//获取上一张证书的签名证书序列号
			WorkDealInfo oldDealInfo = workDealInfoService.get(workDealInfo.getPrevId());
			try {
				model.addAttribute("signSerialNumber", oldDealInfo.getWorkCertInfo().getSerialnumber().toLowerCase());
			} catch (Exception e) {
			}
		}
		return "modules/work/workDealInfoChangeAuditMake";
	}

	/**
	 * 补办保存缴费信息
	 * 
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
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "reissueSavePayInfo")
	public String reissueSavePayInfo(Long workDealInfoId,
			Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange,
			Double electron, Integer methodMoney, Integer methodPos,
			Integer methodBank, Integer methodAlipay, Integer methodGov,
			Integer methodContract, Double money, Double posMoney,
			Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney,
			Boolean userReceipt, Double receiptAmount, Long[] financePayInfoId,
			RedirectAttributes redirectAttributes, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
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
			if (bindMoney<money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney<posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney<bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			
			if (bindMoney<alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - alipayMoney;
				workPayInfo.setAlipayMoney(alipayMoney);
			}
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);


		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney == 0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
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
					workTotalMoney = (double) 0;
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
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_WAIT);
		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);
		workDealInfoService.save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "缴费成功");
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		logUtil.saveSysLog("业务中心", "业务补办：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
		return "modules/work/workDealInfoReissueAuditMake";

	}

	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "auditSavePayInfo")
	public String auditSavePayInfo(Long workDealInfoId,
			Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange,
			Double electron, Integer methodMoney, Integer methodPos,
			Integer methodBank, Integer methodAlipay, Integer methodGov,
			Integer methodContract, Double money, Double posMoney,
			Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney,
			Boolean userReceipt, Double receiptAmount, Long[] financePayInfoId,
			RedirectAttributes redirectAttributes, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
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
			if (bindMoney<money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney<posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney<bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			
			if (bindMoney<alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - alipayMoney;
				workPayInfo.setAlipayMoney(alipayMoney);
			}
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);


		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney == 0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
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
					workTotalMoney = (double) 0;
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
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_OBTAINED_WAIT);
		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);
		workDealInfoService.save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		addMessage(redirectAttributes, "缴费成功");
		logUtil.saveSysLog("业务中心", "客户端更新业务：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
	}

	@RequestMapping("putStore")
	@ResponseBody
	public String putStore(String keySn) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			
			List<KeyUsbKeyInvoice> invoices =  keyInvoiceService.findByKeysn(keySn);
			if (invoices.size()<1) {
				jsonObject.put("status", 2);
			}else{
				keyInvoiceService.saveKeyUsbKey(keySn);
				jsonObject.put("status", 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("status", 0);
		}
		return jsonObject.toString();
	}

	/**
	 * 判断当前业务如果原来绑定过信息，解绑，退费
	 * 
	 * @param dealInfoId
	 */
	private void fixOldPayInfo(WorkDealInfo dealInfo) {
		try {
			if (dealInfo.getWorkPayInfo().getId() != null) {
				WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
				Set<WorkFinancePayInfoRelation> relations = payInfo
						.getWorkFinancePayInfoRelations();
				if (relations.size() != 0) {
					for (WorkFinancePayInfoRelation relation : relations) {// 退费
						FinancePaymentInfo financePaymentInfo = relation
								.getFinancePaymentInfo();
						financePaymentInfo.setBingdingTimes(financePaymentInfo
								.getBingdingTimes() - 1);
						financePaymentInfo.setResidueMoney(financePaymentInfo
								.getResidueMoney() + relation.getMoney());// 返还金额
						financePaymentInfoService.save(financePaymentInfo);
						workFinancePayInfoRelationService.delete(relation
								.getId());
					}
				}
				dealInfo.setWorkPayInfo(null);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	/**
	 * 
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
	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "errorSavePayInfo")
	public String errorSavePayInfo(Long workDealInfoId, Double openAccountMoney,
			Double addCert, Double updateCert, Double errorReplaceCert,
			Double lostReplaceCert, Double infoChange, Double electron,
			Integer methodMoney, Integer methodPos, Integer methodBank,
			Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney,
			Double alipayMoney, Double workTotalMoney, Double workPayedMoney,
			Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, RedirectAttributes redirectAttributes,
			Integer errorPayType, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
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
			if (bindMoney<money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney<posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney<bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);
			
			if (bindMoney<alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			}else {
				bindMoney = bindMoney - alipayMoney;
				workPayInfo.setAlipayMoney(alipayMoney);
			}
		}
		if (methodGov != null && methodGov == 1) {
			workPayInfo.setMethodGov(true);
		}
		if (methodContract != null && methodContract == 1) {
			workPayInfo.setMethodContract(true);
		}
		workPayInfo.setWorkTotalMoney(workTotalMoney);
		workPayInfo.setWorkPayedMoney(workPayedMoney);
		workPayInfo.setWorkReceivaMoney(workReceivaMoney);
		workPayInfo.setUserReceipt(userReceipt);
		workPayInfo.setReceiptAmount(receiptAmount);
		workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
		workPayInfoService.save(workPayInfo);

		if (financePayInfoId != null && financePayInfoId.length > 0) {
			for (int i = 0; i < financePayInfoId.length; i++) {
				if (bindMoney == 0) {
					break;
				}
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService
						.get(financePayInfoId[i]);
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
					workTotalMoney = (double) 0;
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
		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);
		workDealInfoService.save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String []> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		addMessage(redirectAttributes, "缴费成功");
		logUtil.saveSysLog("业务中心", "审核拒绝重新编辑业务：编号"+workDealInfo.getId()+"缴费"+workPayedMoney+"元", "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}
}
