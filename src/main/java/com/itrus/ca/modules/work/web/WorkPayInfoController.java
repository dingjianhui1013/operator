/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
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
import com.itrus.ca.modules.self.entity.SelfApplication;
import com.itrus.ca.modules.self.service.SelfApplicationService;
import com.itrus.ca.modules.self.service.SelfAreaService;
import com.itrus.ca.modules.self.utils.SelfApplicationStatus;
import com.itrus.ca.modules.sys.entity.CommonAttach;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.CommonAttachService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
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
	private CommonAttachService attachService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private KeyUsbKeyInvoiceService keyInvoiceService;

	@Autowired
	private ConfigRaAccountService raAccountService;

	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;
	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	private LogUtil logUtil = new LogUtil();

	@Autowired
	private ConfigChargeAgentService chargeAgentService;

	@Autowired
	private WorkLogService workLogService;
	
	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;

	@Autowired
	private SelfApplicationService selfApplicationService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private SelfAreaService selfAreaService;
	
	
	
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
	public String list(WorkPayInfo workPayInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkPayInfo> page = workPayInfoService.find(new Page<WorkPayInfo>(request, response), workPayInfo);
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
	public String save(WorkPayInfo workPayInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workPayInfo)) {
			return form(workPayInfo, model);
		}

		workPayInfoService.save(workPayInfo);
		// addMessage(redirectAttributes, "保存绑定信息'" + workPayInfo.getName() +
		// "'成功");
		return "redirect:" + Global.getAdminPath() + "/modules/work/workPayInfo/?repage";
	}

	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workPayInfoService.delete(id);
		addMessage(redirectAttributes, "删除绑定信息成功");
		return "redirect:" + Global.getAdminPath() + "/modules/work/workPayInfo/?repage";
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
	public String savePayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange, Double electron, Integer methodMoney,
			Integer methodPos, Integer methodBank, Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
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
			if (bindMoney < money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney < posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney < bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);

			if (bindMoney < alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			} else {
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
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney - financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double) 0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() - bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				if (financePaymentInfo.getBingdingTimes() == null) {
					financePaymentInfo.setBingdingTimes(1);
				} else {
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() + 1);
				}
				if (i == 0) {
					workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
				}

				financePaymentInfoService.save(financePaymentInfo);
				financePayInfoRelation.setFinancePaymentInfo(financePaymentInfo);
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

		logUtil.saveSysLog("业务中心", "业务新增：编号" + workDealInfo.getId() + "缴费" + workTotalMoney + "元", "");
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
	public String maintainSavePayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange, Double electron, Integer methodMoney,
			Integer methodPos, Integer methodBank, Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, Double oldOpenAccountMoney, Double oldAddCert, Double oldUpdateCert,
			Double oldErrorReplaceCert, Double oldLostReplaceCert, Double oldElectron, Double oldInfoChange,
			RedirectAttributes redirectAttributes, Model model) {
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		fixOldPayInfo(workDealInfo);
		if(workDealInfo.getSelfApplyId()!=null){
			SelfApplication selfApplication=selfApplicationService.get(workDealInfo.getSelfApplyId());
			if(selfApplication.getStatus().equalsIgnoreCase(SelfApplicationStatus.payApply)||selfApplication.getStatus().equalsIgnoreCase(SelfApplicationStatus.downApply)){
				selfApplication.setStatus(SelfApplicationStatus.handApply);
				selfApplicationService.save(selfApplication);
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
			if (bindMoney < money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney < posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney < bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);

			if (bindMoney < alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			} else {
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
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney - financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double) 0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() - bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				if (financePaymentInfo.getBingdingTimes() == null) {
					financePaymentInfo.setBingdingTimes(1);
				} else {
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() + 1);
				}
				if (i == 0) {
					workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
				}
				financePaymentInfoService.save(financePaymentInfo);

				financePayInfoRelation.setFinancePaymentInfo(financePaymentInfo);
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
			if(workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_UPDATE_CERT){
				if(workDealInfo.getIsIxin()!=null&&workDealInfo.getIsIxin()){
					workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_VERIFICATE_WAIT);
				}else{
					workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ADD_USER);
				}
			}else{
				//遗失变更等 均需要进行鉴证
				workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ADD_USER);
			}
			if(workDealInfo.getSelfApplyId()!=null){
				SelfApplication selfApplication=selfApplicationService.get(workDealInfo.getSelfApplyId());
				selfApplication.setStatus(SelfApplicationStatus.makeApply);
				selfApplicationService.save(selfApplication);
			}
		}

		workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);

		if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
			if (workDealInfo.getDealInfoType() != null && workDealInfo.getDealInfoType().equals(1)) {

				if (workDealInfo.getDealInfoType().equals(1)) {
					ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
					if (!agent.getTempStyle().equals("1")) {
						agent.setAvailableUpdateNum(agent.getAvailableUpdateNum() + 1);// 已用数量
						agent.setSurplusUpdateNum(agent.getSurplusUpdateNum() - 1);// 剩余数量
						configChargeAgentService.save(agent);
						logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
					}

					ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
					dealInfoBound.setDealInfo(workDealInfo);
					dealInfoBound.setAgent(agent);
					configAgentBoundDealInfoService.save(dealInfoBound);
					logUtil.saveSysLog("计费策略模版",
							"计费策略模版：" + agent.getId() + "--业务编号：" + workDealInfo.getId() + "--关联成功!", "");

				}
			}

		} else {
			if (workDealInfo.getDealInfoType() != null && workDealInfo.getDealInfoType().equals(1)) {
				if (workDealInfo.getDealInfoType().equals(1)) {
					ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
					Integer avaiUpdateNum = agent.getAvailableUpdateNum();// 已用数量
					Integer reseUpdateNum = agent.getReserveUpdateNum();// 预留数量

					agent.setAvailableUpdateNum(avaiUpdateNum + 1);// 已用数量
					agent.setReserveUpdateNum(reseUpdateNum - 1);// 预留数量
					configChargeAgentService.save(agent);
					logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
				}
			}
		}

		workDealInfo.setPayUser(UserUtils.getUser());
		workDealInfo.setPayUserDate(new Date());
		if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
			workDealInfo.setCreateBy(UserUtils.getUser());
			workDealInfo.setUpdateBy(UserUtils.getUser());
		}
		workDealInfoService.save(workDealInfo);
		ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("workDealInfo", workDealInfo);
		logUtil.saveSysLog("业务中心", "业务维护：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
		if (workDealInfo.getPrevId() != null) {
			// 获取上一张证书的签名证书序列号
			WorkDealInfo oldDealInfo = workDealInfoService.get(workDealInfo.getPrevId());
			try {
				model.addAttribute("signSerialNumber", oldDealInfo.getWorkCertInfo().getSerialnumber().toLowerCase());
			} catch (Exception e) {
			}
		}
		ArrayList<Integer> dealInfoTypes = new ArrayList<Integer>();
		if (workDealInfo.getDealInfoType() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType());
		}
		if (workDealInfo.getDealInfoType1() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType1());
		}
		if (workDealInfo.getDealInfoType2() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType2());
		}
		if (workDealInfo.getDealInfoType3() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType3());
		}
		boolean invBoolean = false;
		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals(1) || dealInfoTypes.get(0).equals(4)) {
				invBoolean = true;

			}
		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals(1) || dealInfoTypes.get(0).equals(4)) {
				if (dealInfoTypes.get(1).equals(1) || dealInfoTypes.get(1).equals(4)) {
					invBoolean = true;
				}
			}
		}

		if (invBoolean) {

			if (workDealInfo.getWorkPayInfo().getUserReceipt()) {

				ReceiptInvoice receiptInvoice = new ReceiptInvoice();
				Office office = workDealInfo.getCreateBy().getOffice();
				List<ReceiptDepotInfo> depotInfos = receiptDepotInfoService.findDepotByOffice(office);
				receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
				receiptInvoice.setCompanyName(workDealInfo.getWorkCompany().getCompanyName());
				receiptInvoice.setReceiptMoney(workDealInfo.getWorkPayInfo().getReceiptAmount());
				receiptInvoice.setReceiptType(0);// 销售出库
				receiptInvoice.setDealInfoId(workDealInfo.getId());
				receiptInvoiceService.save(receiptInvoice);
				ReceiptDepotInfo receiptDepotInfo = depotInfos.get(0);
				receiptDepotInfo.setReceiptResidue(
						receiptDepotInfo.getReceiptResidue() - workDealInfo.getWorkPayInfo().getReceiptAmount());
				receiptDepotInfo.setReceiptOut(
						receiptDepotInfo.getReceiptOut() + workDealInfo.getWorkPayInfo().getReceiptAmount());
				receiptDepotInfoService.save(receiptDepotInfo);

			}

		}
		
		
		//经信委
				if(workDealInfo.getExpirationDate()!=null){
					model.addAttribute("expirationDate", workDealInfo.getExpirationDate());
					model.addAttribute("addCertDays",StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate())-workDealInfo.getYear()*365-workDealInfo.getLastDays());
					model.addAttribute("validiteDays",StringHelper.getDvalueDay(new Date(), workDealInfo.getExpirationDate()));
				}
		
		
		if (workDealInfo.getDealInfoType() != null) {
			if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				if (dealInfoTypes.size() == 1) {
					if (dealInfoTypes.get(0).equals(1)) {
						model.addAttribute("onlyUpdate", "onlyUpdate");
					}
				}
				
				//更新 
				if(workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)){
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
				}else{//信息变更
					return "modules/work/workDealInfoMaintainAuditMake";
				}
			} else if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType1() != null) {
			if (workDealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType1().equals(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				//遗失补办和损坏变更返回列表
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
				//return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType2() != null) {
			if (workDealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				
				if(workDealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE)){
					model.addAttribute("isChangeBusiness",true);
				}
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
				//return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType2().equals(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
				return "modules/work/workDealInfoReissueAuditMake";
			}
		}
		if (workDealInfo.getDealInfoType3() != null) {
			if (workDealInfo.getDealInfoType3().equals(WorkDealInfoType.TYPE_UPDATE_CERT)
					|| (workDealInfo.getDealInfoType3().equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE))) {
				if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
					addMessage(redirectAttributes, "缴费成功");
					logUtil.saveSysLog("业务中心", "客户端更新业务：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
				}
				return "modules/work/workDealInfoMaintainAuditMake";
			} else if (workDealInfo.getDealInfoType3().equals(WorkDealInfoType.TYPE_ADD_CERT)) {
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
			} else if (workDealInfo.getDealInfoType3().equals(WorkDealInfoType.TYPE_LOST_CHILD)
					|| workDealInfo.getDealInfoType3().equals(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
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
	public String changeSavePayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange, Double electron, Integer methodMoney,
			Integer methodPos, Integer methodBank, Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, RedirectAttributes redirectAttributes, Model model) {
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
			if (bindMoney < money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney < posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney < bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);

			if (bindMoney < alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			} else {
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
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney - financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double) 0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() - bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				if (financePaymentInfo.getBingdingTimes() == null) {
					financePaymentInfo.setBingdingTimes(1);
				} else {
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() + 1);
				}
				if (i == 0) {
					workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
				}
				financePaymentInfoService.save(financePaymentInfo);

				financePayInfoRelation.setFinancePaymentInfo(financePaymentInfo);
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
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		logUtil.saveSysLog("业务中心", "业务变更：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
		addMessage(redirectAttributes, "缴费成功");
		if (workDealInfo.getPrevId() != null) {
			// 获取上一张证书的签名证书序列号
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
	public String reissueSavePayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange, Double electron, Integer methodMoney,
			Integer methodPos, Integer methodBank, Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, RedirectAttributes redirectAttributes, Model model) {
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
			if (bindMoney < money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney < posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney < bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);

			if (bindMoney < alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			} else {
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
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney - financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double) 0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() - bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				if (financePaymentInfo.getBingdingTimes() == null) {
					financePaymentInfo.setBingdingTimes(1);
				} else {
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() + 1);
				}
				if (i == 0) {
					workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
				}
				financePaymentInfoService.save(financePaymentInfo);

				financePayInfoRelation.setFinancePaymentInfo(financePaymentInfo);
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
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "缴费成功");
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		logUtil.saveSysLog("业务中心", "业务补办：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
		return "modules/work/workDealInfoReissueAuditMake";

	}

	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "auditSavePayInfo")
	public String auditSavePayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange, Double electron, Integer methodMoney,
			Integer methodPos, Integer methodBank, Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, RedirectAttributes redirectAttributes, Model model) {
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
			if (bindMoney < money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney < posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney < bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);

			if (bindMoney < alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			} else {
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
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney - financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double) 0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() - bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				if (financePaymentInfo.getBingdingTimes() == null) {
					financePaymentInfo.setBingdingTimes(1);
				} else {
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() + 1);
				}
				if (i == 0) {
					workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
				}
				financePaymentInfoService.save(financePaymentInfo);

				financePayInfoRelation.setFinancePaymentInfo(financePaymentInfo);
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
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("pt", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		addMessage(redirectAttributes, "缴费成功");
		logUtil.saveSysLog("业务中心", "客户端更新业务：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
	}

	@RequestMapping("putStore")
	@ResponseBody
	public String putStore(String keySn) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {

			List<KeyUsbKeyInvoice> invoices = keyInvoiceService.findByKeysn(keySn);
			if (invoices.size() < 1) {
				jsonObject.put("status", 2);
			} else {
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
				Set<WorkFinancePayInfoRelation> relations = payInfo.getWorkFinancePayInfoRelations();
				if (relations.size() != 0) {
					for (WorkFinancePayInfoRelation relation : relations) {// 退费
						FinancePaymentInfo financePaymentInfo = relation.getFinancePaymentInfo();
						financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() - 1);
						financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() + relation.getMoney());// 返还金额
						financePaymentInfoService.save(financePaymentInfo);
						workFinancePayInfoRelationService.delete(relation.getId());
					}
				}
				dealInfo.setWorkPayInfo(null);
			}
		} catch (Exception e) {
			// e.printStackTrace();
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
	public String errorSavePayInfo(Long workDealInfoId, Double openAccountMoney, Double addCert, Double updateCert,
			Double errorReplaceCert, Double lostReplaceCert, Double infoChange, Double electron, Integer methodMoney,
			Integer methodPos, Integer methodBank, Integer methodAlipay, Integer methodGov, Integer methodContract,
			Double money, Double posMoney, Double bankMoney, Double alipayMoney, Double workTotalMoney,
			Double workPayedMoney, Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Long[] financePayInfoId, RedirectAttributes redirectAttributes, Integer errorPayType, Model model) {
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
			if (bindMoney < money) {
				workPayInfo.setMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - money;
				workPayInfo.setMoney(money);
			}
		}
		if (methodPos != null && methodPos == 1) {
			workPayInfo.setMethodPos(true);
			if (bindMoney < posMoney) {
				workPayInfo.setPosMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - posMoney;
				workPayInfo.setPosMoney(posMoney);
			}
		}
		if (methodBank != null && methodBank == 1) {
			workPayInfo.setMethodBank(true);
			if (bindMoney < bankMoney) {
				workPayInfo.setBankMoney(bindMoney);
				bindMoney = 0d;
			} else {
				bindMoney = bindMoney - bankMoney;
				workPayInfo.setBankMoney(bankMoney);
			}
		}
		if (methodAlipay != null && methodAlipay == 1) {
			workPayInfo.setMethodAlipay(true);

			if (bindMoney < alipayMoney) {
				workPayInfo.setAlipayMoney(bindMoney);
				bindMoney = 0d;
			} else {
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
				FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(financePayInfoId[i]);
				WorkFinancePayInfoRelation financePayInfoRelation = new WorkFinancePayInfoRelation();
				if (bindMoney > financePaymentInfo.getResidueMoney()) {
					financePayInfoRelation.setMoney(financePaymentInfo.getResidueMoney());
					bindMoney = bindMoney - financePaymentInfo.getResidueMoney();
					financePaymentInfo.setResidueMoney((double) 0);
				} else {
					financePayInfoRelation.setMoney(bindMoney);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() - bindMoney);
					workTotalMoney = (double) 0;
					bindMoney = 0d;
				}
				if (financePaymentInfo.getBingdingTimes() == null) {
					financePaymentInfo.setBingdingTimes(1);
				} else {
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() + 1);
				}
				if (i == 0) {
					workPayInfo.setRelationMethod(financePaymentInfo.getPaymentMethod());
				}
				financePaymentInfoService.save(financePaymentInfo);
				financePayInfoRelation.setFinancePaymentInfo(financePaymentInfo);
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
		List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());
		model.addAttribute("list", list);
		model.addAttribute("workDealInfo", workDealInfo);
		addMessage(redirectAttributes, "缴费成功");
		logUtil.saveSysLog("业务中心", "审核拒绝重新编辑业务：编号" + workDealInfo.getId() + "缴费" + workPayedMoney + "元", "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "returnPayment")
	public String returnPayment(Long workDealInfoId, RedirectAttributes redirectAttributes, Model model) {

		WorkDealInfo dealInfo = workDealInfoService.get(workDealInfoId);
		if (dealInfo.getDealInfoType()!=null && dealInfo.getDealInfoType().equals(1)) {
			ConfigChargeAgent agentOri = configChargeAgentService.get(dealInfo.getConfigChargeAgentId());
			agentOri.setReserveUpdateNum(agentOri.getReserveUpdateNum() + 1);
			agentOri.setAvailableUpdateNum(agentOri.getAvailableUpdateNum() - 1);
			configChargeAgentService.save(agentOri);
		}

		List<Integer> dealInfoList = new ArrayList<Integer>();
		if (dealInfo.getDealInfoType() != null) {
			dealInfoList.add(dealInfo.getDealInfoType());
		}
		if (dealInfo.getDealInfoType1() != null) {
			dealInfoList.add(dealInfo.getDealInfoType1());
		}
		if (dealInfo.getDealInfoType2() != null) {
			dealInfoList.add(dealInfo.getDealInfoType2());
		}
		boolean isOut = false;
		if (dealInfoList.size() == 1) {
			if (dealInfoList.get(0).equals(1)) {
				isOut = true;
			} else if (dealInfoList.get(0).equals(4)) {
				isOut = true;
			}
		} else if (dealInfoList.size() == 2) {
			if (dealInfoList.get(0).equals(1) && dealInfoList.get(1).equals(4)) {
				isOut = true;
			}
		}

		if (isOut) {
			WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
			Set<WorkFinancePayInfoRelation> relations = payInfo.getWorkFinancePayInfoRelations();
			if (relations.size() != 0) {
				for (WorkFinancePayInfoRelation relation : relations) {// 退费
					FinancePaymentInfo financePaymentInfo = relation.getFinancePaymentInfo();
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() - 1);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() + relation.getMoney());// 返还金额
					financePaymentInfoService.save(financePaymentInfo);
					workFinancePayInfoRelationService.delete(relation.getId());
				}
			}

			Double money = dealInfo.getWorkPayInfo().getReceiptAmount();

			if (money > 0d) {
				ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService
						.findDepotByOffice(dealInfo.getCreateBy().getOffice()).get(0);
				// 修改余额
				receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue() + money);
				receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal() + money);

				// 创建入库信息
				ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
				receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
				receiptEnterInfo.setNow_Money(Double.valueOf(money));
				receiptEnterInfo.setBeforMoney(
						receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue() - Double.valueOf(money));
				receiptEnterInfo.setReceiptMoney(receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue());
				receiptEnterInfo.setReceiptType(4);// 退费入库
				receiptEnterInfoService.save(receiptEnterInfo);

				logUtil.saveSysLog("更新业务办理重新缴费", "库房" + receiptDepotInfo.getReceiptName() + "添加入库信息成功", "");
				receiptDepotInfoService.save(receiptDepotInfo);
			}
		}

		workPayInfoService.delete(dealInfo.getWorkPayInfo().getId());
		dealInfo.setWorkPayInfo(null);
		dealInfo.setDealInfoStatus("5");
		workDealInfoService.save(dealInfo);
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/pay?id=" + dealInfo.getId();
	}
	
	
	
	
	@RequiresPermissions("work:workPayInfo:edit")
	@RequestMapping(value = "errorReturnPayment")
	public String errorReturnPayment(Long workDealInfoId, RedirectAttributes redirectAttributes, Model model) {

		WorkDealInfo dealInfo = workDealInfoService.get(workDealInfoId);
		if (dealInfo.getDealInfoType()!=null && dealInfo.getDealInfoType().equals(1)) {
			ConfigChargeAgent agentOri = configChargeAgentService.get(dealInfo.getConfigChargeAgentId());
			agentOri.setReserveUpdateNum(agentOri.getReserveUpdateNum() + 1);
			agentOri.setAvailableUpdateNum(agentOri.getAvailableUpdateNum() - 1);
			configChargeAgentService.save(agentOri);
		}

		List<Integer> dealInfoList = new ArrayList<Integer>();
		if (dealInfo.getDealInfoType() != null) {
			dealInfoList.add(dealInfo.getDealInfoType());
		}
		if (dealInfo.getDealInfoType1() != null) {
			dealInfoList.add(dealInfo.getDealInfoType1());
		}
		if (dealInfo.getDealInfoType2() != null) {
			dealInfoList.add(dealInfo.getDealInfoType2());
		}
		boolean isOut = false;
		if (dealInfoList.size() == 1) {
			if (dealInfoList.get(0).equals(1)) {
				isOut = true;
			} else if (dealInfoList.get(0).equals(4)) {
				isOut = true;
			}
		} else if (dealInfoList.size() == 2) {
			if (dealInfoList.get(0).equals(1) && dealInfoList.get(1).equals(4)) {
				isOut = true;
			}
		}

		if (isOut) {
			WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
			Set<WorkFinancePayInfoRelation> relations = payInfo.getWorkFinancePayInfoRelations();
			if (relations.size() != 0) {
				for (WorkFinancePayInfoRelation relation : relations) {// 退费
					FinancePaymentInfo financePaymentInfo = relation.getFinancePaymentInfo();
					financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() - 1);
					financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() + relation.getMoney());// 返还金额
					financePaymentInfoService.save(financePaymentInfo);
					workFinancePayInfoRelationService.delete(relation.getId());
				}
			}

			Double money = dealInfo.getWorkPayInfo().getReceiptAmount();

			if (money > 0d) {
				ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService
						.findDepotByOffice(dealInfo.getCreateBy().getOffice()).get(0);
				// 修改余额
				receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue() + money);
				receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal() + money);

				// 创建入库信息
				ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
				receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
				receiptEnterInfo.setNow_Money(Double.valueOf(money));
				receiptEnterInfo.setBeforMoney(
						receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue() - Double.valueOf(money));
				receiptEnterInfo.setReceiptMoney(receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue());
				receiptEnterInfo.setReceiptType(4);// 退费入库
				receiptEnterInfoService.save(receiptEnterInfo);

				logUtil.saveSysLog("更新业务办理重新缴费", "库房" + receiptDepotInfo.getReceiptName() + "添加入库信息成功", "");
				receiptDepotInfoService.save(receiptDepotInfo);
			}
		}

		workPayInfoService.delete(dealInfo.getWorkPayInfo().getId());
		dealInfo.setWorkPayInfo(null);
		boolean inOffice = false;
		dealInfo.setDealInfoStatus("15");
		workDealInfoService.save(dealInfo);

		String url = Global.getConfig("images.path");
		List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
		
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
		
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(dealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点办理！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}

		if (dealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", dealInfo.getWorkCertInfo().getWorkCertApplyInfo());
		}

		ConfigChargeAgent chargeAgent = chargeAgentService.get(dealInfo.getConfigChargeAgentId());
		model.addAttribute("tempStyle", chargeAgent.getTempStyle());
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		model.addAttribute("workDealInfo", dealInfo);

		ConfigProduct configProduct = dealInfo.getConfigProduct();
		List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
				.findByProIdAll(configProduct.getId());
		Set<Integer> nameSet = new HashSet<Integer>();
		for (int i = 0; i < boundList.size(); i++) {
			nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
		}
		model.addAttribute("boundLabelList", nameSet);
		List<WorkLog> list = workLogService.findByDealInfo(dealInfo);
		model.addAttribute("workLog", list);
		model.addAttribute("tempStyle", chargeAgent.getTempStyle());

		ArrayList<Integer> dealInfoTypes = new ArrayList<Integer>();
		if (dealInfo.getDealInfoType() != null) {
			dealInfoTypes.add(dealInfo.getDealInfoType());
		}
		if (dealInfo.getDealInfoType1() != null) {
			dealInfoTypes.add(dealInfo.getDealInfoType1());
		}
		if (dealInfo.getDealInfoType2() != null) {
			dealInfoTypes.add(dealInfo.getDealInfoType2());
		}
		if (dealInfo.getDealInfoType3() != null) {
			dealInfoTypes.add(dealInfo.getDealInfoType3());
		}
		
		//获得应用下的产品
		List<ConfigProduct> products = configProductService.findByAppAndProName(dealInfo.getConfigApp().getId(), dealInfo.getConfigProduct().getProductName());
		List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
		for (int i = 0; i < products.size(); i++) {
			String ssssi = ProductType.productTypeStrMap.get(products.get(i).getProductName())+"["+(products.get(i).getProductLabel()==0?"通用":"专用")+"]";
			ProductTypeObj obj = new ProductTypeObj(products.get(i).getId().intValue(), ssssi);
			listProductTypeObjs.add(obj);
		}
		model.addAttribute("proList", listProductTypeObjs);
		
		//获得省和市对应self_area表中的id
		if(dealInfo.getWorkCompany().getProvince()!=null&&!dealInfo.getWorkCompany().getProvince().isEmpty()){
			String provinceId = selfAreaService.findByAreaName(dealInfo.getWorkCompany().getProvince()).getAreaId();
			model.addAttribute("provinceId", provinceId);
			if(dealInfo.getWorkCompany().getCity()!=null&&!dealInfo.getWorkCompany().getCity().isEmpty()){
						
				String cityId = selfAreaService.findByProvinceName(dealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
				model.addAttribute("cityId", cityId);
			}
				
		}
	

		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals(4)) {// 变更
				return "modules/work/maintain/workDealInfoMaintainReturnChange";
			} else if (dealInfoTypes.get(0).equals(1)) {
				model.addAttribute("update", "3");
				ConfigProduct configProductOld = dealInfo.getConfigProduct();
				String[] years = configChargeAgentDetailService.getChargeAgentYears(configProductOld.getChargeAgentId(),
						WorkDealInfoType.TYPE_UPDATE_CERT);
				for (int j = 0; j < years.length; j++) {
					switch (years[j]) {
					case "1":
						model.addAttribute("year1", true);
						break;
					case "2":
						model.addAttribute("year2", true);
						break;
					case "3":
						model.addAttribute("year3", true);
						break;
					case "4":
						model.addAttribute("year4", true);
						break;
					case "5":
						model.addAttribute("year5", true);
						break;
					}
				}
				model.addAttribute("dealType", dealInfoTypes.toString());
				return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
			} else if (dealInfoTypes.get(0).equals(2) || dealInfoTypes.get(0).equals(3)) {
				model.addAttribute("reissue", dealInfoTypes.get(0));
				return "modules/work/maintain/workDealInfoMaintainReturnLost";
			}

		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals(2) || dealInfoTypes.get(0).equals(3)) {
				if (dealInfoTypes.get(1).equals(4)) {
					model.addAttribute("reissue", dealInfoTypes.get(0));
					return "modules/work/maintain/workDealInfoMaintainReturnChange";
				}
			} else if (dealInfoTypes.get(0).equals(1)) {
				if (dealInfoTypes.get(1).equals(2) || dealInfoTypes.get(1).equals(3)) {
					model.addAttribute("update", "3");
					model.addAttribute("reissue", dealInfoTypes.get(1));
					ConfigProduct configProductOld = dealInfo.getConfigProduct();
					String[] years = configChargeAgentDetailService.getChargeAgentYears(
							configProductOld.getChargeAgentId(), WorkDealInfoType.TYPE_UPDATE_CERT);
					for (int j = 0; j < years.length; j++) {
						switch (years[j]) {
						case "1":
							model.addAttribute("year1", true);
							break;
						case "2":
							model.addAttribute("year2", true);
							break;
						case "3":
							model.addAttribute("year3", true);
							break;
						case "4":
							model.addAttribute("year4", true);
							break;
						case "5":
							model.addAttribute("year5", true);
							break;
						}
					}
					model.addAttribute("dealType", dealInfoTypes.toString());
					return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
				} else if (dealInfoTypes.get(1).equals(4)) {
					model.addAttribute("update", "3");
					ConfigProduct configProductOld = dealInfo.getConfigProduct();
					String[] years = configChargeAgentDetailService.getChargeAgentYears(
							configProductOld.getChargeAgentId(), WorkDealInfoType.TYPE_UPDATE_CERT);
					for (int j = 0; j < years.length; j++) {
						switch (years[j]) {
						case "1":
							model.addAttribute("year1", true);
							break;
						case "2":
							model.addAttribute("year2", true);
							break;
						case "3":
							model.addAttribute("year3", true);
							break;
						case "4":
							model.addAttribute("year4", true);
							break;
						case "5":
							model.addAttribute("year5", true);
							break;
						}
					}
					model.addAttribute("dealType", dealInfoTypes.toString());
					return "modules/work/maintain/workDealInfoMaintainReturnUpdateChange";
				}
			}
		} else if (dealInfoTypes.size() == 3) {
			model.addAttribute("update", "3");
			model.addAttribute("reissue", dealInfoTypes.get(1));
			ConfigProduct configProductOld = dealInfo.getConfigProduct();
			String[] years = configChargeAgentDetailService.getChargeAgentYears(configProductOld.getChargeAgentId(),
					WorkDealInfoType.TYPE_UPDATE_CERT);
			for (int j = 0; j < years.length; j++) {
				switch (years[j]) {
				case "1":
					model.addAttribute("year1", true);
					break;
				case "2":
					model.addAttribute("year2", true);
					break;
				case "3":
					model.addAttribute("year3", true);
					break;
				case "4":
					model.addAttribute("year4", true);
					break;
				case "5":
					model.addAttribute("year5", true);
					break;
				}
			}
			model.addAttribute("dealType", dealInfoTypes.toString());
			return "modules/work/maintain/workDealInfoMaintainReturnUpdateChange";
		}
		return null;
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
