/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.web;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avro.data.Json;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.alibaba.fastjson.JSON;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.SignatureInfoType;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.SignatureInfoStatus;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgentDetail;
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.entity.SignaturePayInfo;
import com.itrus.ca.modules.signature.service.SignatureAgentBoundConfigAppService;
import com.itrus.ca.modules.signature.service.SignatureConfigChargeAgentDetailService;
import com.itrus.ca.modules.signature.service.SignatureConfigChargeAgentService;
import com.itrus.ca.modules.signature.service.SignatureInfoService;
import com.itrus.ca.modules.signature.service.SignaturePayInfoService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.sun.jmx.snmp.Timestamp;
import com.sun.org.apache.bcel.internal.generic.IFGE;
import com.sun.org.apache.bcel.internal.generic.NEW;


/**
 * 印章Controller
 * @author CYC
 * @version 2016-02-21
 */
@Controller
@RequestMapping(value = "${adminPath}/signature/signatureInfo")
public class SignatureInfoController extends BaseController {

	@Autowired
	private SignatureInfoService signatureInfoService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private SignatureAgentBoundConfigAppService SignatureAgentBoundConfigAppService;
	
	@Autowired
	private SignatureConfigChargeAgentDetailService signatureConfigChargeAgentDetailService;
	

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	@Autowired
	private SignatureConfigChargeAgentDetailService signatureConfigChargeDetailService;
	
	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private SignatureConfigChargeAgentService signatureConfigChargeAgentService;
	
	@Autowired
	private SignaturePayInfoService signaturePayInfoService;
	
	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@Autowired
	private OfficeService officeService;

	
	@ModelAttribute
	public SignatureInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return signatureInfoService.get(id);
		} else {
			return new SignatureInfo();
		}
	}
	
	
	
	@RequiresPermissions("signature:signatureInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(SignatureInfo signatureInfo,HttpServletRequest request,
			HttpServletResponse response, Model model,String signatureType,Date startTime,Date endTime
			) throws ParseException {

		User user = UserUtils.getUser();
		
		List<Office> offices =  officeService.getOfficeByType(user, 2);
		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < offices.size(); i++) {
			officeIds.add(offices.get(i).getId());
		}
		Page<SignatureInfo> page = signatureInfoService.findByCondition(new Page<SignatureInfo>(request, response),
				signatureInfo,signatureType,startTime,endTime,officeIds);
		model.addAttribute("page", page);
		model.addAttribute("signatureType",signatureType);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		
		model.addAttribute("status", SignatureInfoStatus.statusMap);
		model.addAttribute("infoStatus", SignatureInfoStatus.signatureInfoMap);
		
		model.addAttribute("signatureTypeMap",SignatureInfoType.SignatureInfoTypeMap);
		model.addAttribute("date",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return "modules/signature/signatureInfoList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "form")
	public String form(Model model,Long workDealInfoId, Long signatureInfoId, RedirectAttributes redirectAttributes) {
		
		ConfigApp configApp = null;
		
		//新增
		if(workDealInfoId!=null&&signatureInfoId==null){
			WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
			boolean inOffice = false;
			List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
					.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
			for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
				if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
					inOffice = true;
				}
			}
			if (!inOffice) {
				redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
				addMessage(redirectAttributes, "请到业务网点办理新增！");
				return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
			}
			
			configApp = workDealInfo.getConfigApp();
			
			model.addAttribute("workDealInfo", workDealInfo);
		}
		
		//新增修改
		if(workDealInfoId==null&&signatureInfoId!=null){
			SignatureInfo signatureInfo = signatureInfoService.get(signatureInfoId);
			configApp = signatureInfo.getConfigApp();
			
			model.addAttribute("signatureInfo", signatureInfo);
			
			//支付信息
			SignaturePayInfo payInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
			model.addAttribute("payInfo", payInfo);
			
			//发票信息
			if(payInfo.getIsReceipt()==true){
				ReceiptInvoice receiptInvoice = payInfo.getReceiptInvoice();
				if(receiptInvoice!=null){
					model.addAttribute("receiptInvoice", receiptInvoice);
				}
			}
			
			SignatureConfigChargeAgentDetail detail = signatureConfigChargeAgentDetailService.findByAgentId(signatureInfo.getSignatureAgent().getId(),WorkType.TYPE_ADD,Integer.parseInt(signatureInfo.getYear()));
			if(detail!=null){
				model.addAttribute("detail", detail);
			}
			
			Map<Integer,Double> years = signatureConfigChargeDetailService.getChargeAgentYears(signatureInfo.getSignatureAgent().getId(),
					WorkType.TYPE_ADD);
			
			for (Integer year:years.keySet()) {
				if(year==1){
					model.addAttribute("year1", true);
					model.addAttribute("money1", years.get(year));
					continue;
				}else if(year==2){
					model.addAttribute("year2", true);
					model.addAttribute("money2", years.get(year));
					continue;
				}else if(year==3){
					model.addAttribute("year3", true);
					model.addAttribute("money3", years.get(year));
					continue;
				}else if(year==4){
					model.addAttribute("year4", true);
					model.addAttribute("money4", years.get(year));
					continue;
				}else if(year==5){
					model.addAttribute("year5", true);
					model.addAttribute("money5", years.get(year));
					continue;
				}
			}
		}
		

		
		model.addAttribute("configApp", configApp);

		
		List<SignatureAgentBoundConfigApp> bounds = SignatureAgentBoundConfigAppService.findByApp(configApp.getId());
		List<SignatureConfigChargeAgent> signatureAgents = new ArrayList<SignatureConfigChargeAgent>();
		if (bounds.size()>0) {
			for (int i = 0; i < bounds.size(); i++) {
				signatureAgents.add(bounds.get(i).getAgent());
			}
		}
		model.addAttribute("signatureAgents",signatureAgents);

		return "modules/signature/signatureInfoForm";
	}

	
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "modifyForm")
	public String modifyForm(Long signatureInfoId ,Model model, RedirectAttributes redirectAttributes) {
		
		SignatureInfo signatureInfo = signatureInfoService.get(signatureInfoId);
		
		if(signatureInfo.getSignatureInfoType().equals(SignatureInfoType.TYPE_UPDATE_SIGNATURE)){
			Map<Integer,Double> years = signatureConfigChargeDetailService.getChargeAgentYears(signatureInfo.getSignatureAgent().getId(),
					WorkType.TYPE_UPDATE);
			
			for (Integer year:years.keySet()) {
				if(year==1){
					model.addAttribute("year1", true);
					model.addAttribute("money1", years.get(year));
					continue;
				}else if(year==2){
					model.addAttribute("year2", true);
					model.addAttribute("money2", years.get(year));
					continue;
				}else if(year==3){
					model.addAttribute("year3", true);
					model.addAttribute("money3", years.get(year));
					continue;
				}else if(year==4){
					model.addAttribute("year4", true);
					model.addAttribute("money4", years.get(year));
					continue;
				}else if(year==5){
					model.addAttribute("year5", true);
					model.addAttribute("money5", years.get(year));
					continue;
				}
			}
			SignaturePayInfo signaturePayInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
			model.addAttribute("signaturePayInfo", signaturePayInfo);
			model.addAttribute("years", signatureInfo.getYear());
			model.addAttribute("signatureInfo", signatureInfo);
			model.addAttribute("update", 2);
			model.addAttribute("types", 2);
			model.addAttribute("difference", 2);
			return "modules/signature/maintain/signatureInfoMaintainUpdate";
		}else if(signatureInfo.getSignatureInfoType().equals(SignatureInfoType.TYPE_CHANGE_SIGNATURE)){
			SignaturePayInfo signaturePayInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
			model.addAttribute("signaturePayInfo", signaturePayInfo);
			model.addAttribute("signatureInfo", signatureInfo);
			model.addAttribute("change", 1);
			model.addAttribute("types", 1);
			Double moneyChange = signatureConfigChargeDetailService.getMoney(signatureInfo.getSignatureAgent().getId(),WorkType.TYPE_CHANGE);
			model.addAttribute("moneyChange", moneyChange);
			model.addAttribute("difference", 2);
			return "modules/signature/maintain/signatureInfoMaintainUpdate";
		}
		
		return "modules/signature/signatureInfoList";
	}
	
	
	@RequestMapping(value = "showYearNew")
	@ResponseBody
	public String showYearNew(Long agentId,Integer workType) {
		JSONObject json = new JSONObject();
		try {
			SignatureConfigChargeAgent agent =  signatureConfigChargeAgentService.get(agentId);
			if (agent.getChargeMethodPos()) {
				json.put("methodPos", true);
			}else{
				json.put("methodPos", false);
			}
			if (agent.getChargeMethodMoney()) {
				json.put("methodMoney", true);
			}else{
				json.put("methodMoney", false);
			}
			List<SignatureConfigChargeAgentDetail> details = signatureConfigChargeAgentDetailService.findByAgentId(agentId,workType);
			String[] years = new String[details.size()];
			for(int i = 0 ; i < details.size(); i++){
				SignatureConfigChargeAgentDetail detail = details.get(i);
				years[i] = detail.getChargeYear().toString();
			}
			for (int i = 0; i < years.length; i++) {
				switch (years[i]) {
				case "1":
					json.put("year1", true);
					json.put("money1", details.get(i).getMoney());
					break;
				case "2":
					json.put("year2", true);
					json.put("money2", details.get(i).getMoney());
					break;
				case "3":
					json.put("year3", true);
					json.put("money3", details.get(i).getMoney());
					break;	
				case "4":
					json.put("year4", true);
					json.put("money4", details.get(i).getMoney());
					break;
				case "5":
					json.put("year5", true);
					json.put("money5", details.get(i).getMoney());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	

	@RequestMapping(value = "save")
	public String save(HttpServletRequest request,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes,
			Long dealInfoId,Long signatureInfoId, String signatureType,Long agentId,Integer year,Boolean methodPos,
			Boolean methodMoney,Double posMoney , Double cashMoney ,Double workTotalMoney,
			Boolean userReceipt , Double receiptAmount 
			) {
		
		//新增保存
		if(dealInfoId!=null&&signatureInfoId==null){
			WorkDealInfo dealInfo =  workDealInfoService.get(dealInfoId);
			SignatureConfigChargeAgent agent = signatureConfigChargeAgentService.get(agentId);
			
			try {
				System.out.println(new Timestamp().getDateTime());
				
				SignatureInfo signature = new SignatureInfo();
				signature.setWorkDealInfo(dealInfo);
				signature.setSignatureAgent(agent);
				signature.setSignatureInfoType(SignatureInfoType.TYPE_ADD_SIGNATURE.toString());
				signature.setSignatureType(signatureType);
				signature.setSignatureAppName("印章业务");
				signature.setYear(year.toString());
				signature.setSvn(signatureInfoService.getSVN());
				signature.setWorkCertInfo(dealInfo.getWorkCertInfo());
				signature.setConfigApp(dealInfo.getConfigApp());
				signature.setSignatureInfoStatus(SignatureInfoStatus.STATUS_ADD_Info);
				signature.setStatus(SignatureInfoStatus.STATUS_UNSTART);
				signature.setStartDate(new Date());
				signature.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
				signature.setOfficeId(UserUtils.getUser().getOffice().getId());
				Calendar cd = Calendar.getInstance(); 
				cd.add(Calendar.DATE, year*365);
				Date end=cd.getTime(); 
				signature.setEndDate(end);
				signature.setEnterDate(new Date());
				signature.setEnterUser(UserUtils.getUser());
				signatureInfoService.save(signature);
				SignaturePayInfo pay = new SignaturePayInfo();
				pay.setSignatureInfo(signature);
				pay.setYear(year);
				pay.setMethodPos(methodPos);
				pay.setMethodMoney(methodMoney);
				pay.setAddSignature(workTotalMoney);
				pay.setChangeSignature(0d);
				pay.setUpdateSignature(0d);
				pay.setPosMoney(posMoney);
				pay.setCashMoney(cashMoney);
				pay.setTotalMoney(workTotalMoney);
				pay.setIsReceipt(userReceipt);
				signaturePayInfoService.save(pay);
				signatureInfoService.save(signature);
				
				if(pay.getIsReceipt()){
					ReceiptInvoice receiptInvoice = new ReceiptInvoice();
					Office office = signature.getEnterUser().getOffice();
					List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
					receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
					receiptInvoice.setCompanyName(signature.getWorkDealInfo().getWorkCompany().getCompanyName());
					receiptInvoice.setReceiptMoney(receiptAmount);
					receiptInvoice.setReceiptType(3);//印章出库
					receiptInvoice.setDealInfoId(signature.getWorkDealInfo().getId());
					receiptInvoice.setCreateDate(new Date());
					receiptInvoice.setUpdateDate(new Date());
					receiptInvoice.setCreateBy(signature.getCreateBy());
					receiptInvoice.setUpdateBy(signature.getCreateBy());
					receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
					receiptInvoiceService.save(receiptInvoice);
					pay.setReceiptInvoice(receiptInvoice);
					signaturePayInfoService.save(pay);	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		//修改保存	
		}else if(dealInfoId==null&&signatureInfoId!=null){
			SignatureInfo signatureInfo = signatureInfoService.get(signatureInfoId);
			SignatureConfigChargeAgent agent = signatureConfigChargeAgentService.get(agentId);
			SignaturePayInfo payInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
			
			try {
				signatureInfo.setSignatureType(signatureType);
				signatureInfo.setSignatureAgent(agent);
				signatureInfo.setYear(year.toString());
				
				signatureInfoService.save(signatureInfo);
				
				
				if(userReceipt!=payInfo.getIsReceipt()){
					if(userReceipt==true){
						ReceiptInvoice receiptInvoice = new ReceiptInvoice();
						Office office = signatureInfo.getEnterUser().getOffice();
						List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
						receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
						receiptInvoice.setCompanyName(signatureInfo.getWorkDealInfo().getWorkCompany().getCompanyName());
						receiptInvoice.setReceiptMoney(receiptAmount);
						receiptInvoice.setReceiptType(3);//印章出库
						receiptInvoice.setDealInfoId(signatureInfo.getWorkDealInfo().getId());
						receiptInvoice.setCreateDate(new Date());
						receiptInvoice.setUpdateDate(new Date());
						receiptInvoice.setCreateBy(signatureInfo.getCreateBy());
						receiptInvoice.setUpdateBy(signatureInfo.getCreateBy());
						receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
						receiptInvoiceService.save(receiptInvoice);
				
						payInfo.setReceiptInvoice(receiptInvoice);
					}else{
						ReceiptInvoice receiptInvoice = payInfo.getReceiptInvoice();
						if(receiptInvoice!=null){
							receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
							receiptInvoiceService.save(receiptInvoice);
						}
					}
				}else{
					if(userReceipt==true){
						ReceiptInvoice receiptInvoice = payInfo.getReceiptInvoice();
						if(receiptInvoice!=null){
							//发票金额更改
							receiptInvoice.setReceiptMoney(receiptAmount);
							receiptInvoiceService.save(receiptInvoice);
							

						}
					}
				}
				
				
				payInfo.setYear(year);
				payInfo.setMethodMoney(methodMoney);
				payInfo.setMethodPos(methodPos);
				payInfo.setCashMoney(cashMoney);
				payInfo.setPosMoney(posMoney);
				payInfo.setIsReceipt(userReceipt);
				
				signaturePayInfoService.save(payInfo);

				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "redirect:" + Global.getAdminPath() + "/signature/signatureInfo/";
	}
	
	
	
	
	
	@RequestMapping(value = "typeForm")
	public String typeForm(String dealType, Long id, Model model, 
			RedirectAttributes redirectAttributes) {
		
		SignatureInfo signatureInfo = signatureInfoService.get(id);
		
		
		Date end = signatureInfo.getEndDate();
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		if(end.before(new Date())){
			addMessage(redirectAttributes, "此业务已过期,无法办理"+(dealType.equals("1")?"变更":"续期")+"业务!");
			return "redirect:" + Global.getAdminPath() + "/signature/signatureInfo/?repage";
		}
		
		
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(signatureInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}

	
			if (dealType.equals("1")) {
				
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务网点办理变更！");
					return "redirect:" + Global.getAdminPath() + "/signature/signatureInfo/?repage";
				}
				
				model.addAttribute("change", "1");
				model.addAttribute("type", 1);
				Double moneyChange = signatureConfigChargeDetailService.getMoney(signatureInfo.getSignatureAgent().getId(),WorkType.TYPE_CHANGE);
				model.addAttribute("moneyChange", moneyChange);
				
				
			}  else if (dealType.equals("2")) {
				
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务网点办理续期！");
					return "redirect:" + Global.getAdminPath() + "/signature/signatureInfo/?repage";
				}
				
				model.addAttribute("update", "2");
				Map<Integer,Double> years = signatureConfigChargeDetailService.getChargeAgentYears(signatureInfo.getSignatureAgent().getId(),
						WorkType.TYPE_UPDATE);
				
				for (Integer year:years.keySet()) {
					if(year==1){
						model.addAttribute("year1", true);
						model.addAttribute("money1", years.get(year));
						continue;
					}else if(year==2){
						model.addAttribute("year2", true);
						model.addAttribute("money2", years.get(year));
						continue;
					}else if(year==3){
						model.addAttribute("year3", true);
						model.addAttribute("money3", years.get(year));
						continue;
					}else if(year==4){
						model.addAttribute("year4", true);
						model.addAttribute("money4", years.get(year));
						continue;
					}else if(year==5){
						model.addAttribute("year5", true);
						model.addAttribute("money5", years.get(year));
						continue;
					}
				}

				
			} 
		

		model.addAttribute("dealType", dealType);
		model.addAttribute("difference", 1);
		model.addAttribute("signatureInfo", signatureInfo);
		
		return "modules/signature/maintain/signatureInfoMaintainUpdate";
		
			

		
	}
	

	//更新印章
	@RequestMapping(value = "maintainSaveUpdate")
	public String maintainSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes,
			Long signatureInfoId,String signatureType,Long agentId,Integer year,Boolean methodPos,
			Boolean methodMoney,Double posMoney , Double cashMoney ,Double workTotalMoney,
			Boolean userReceipt , Double receiptAmount ,Integer difference,Integer types
			) {
		SignatureConfigChargeAgent agent = signatureConfigChargeAgentService.get(agentId);
		
		try {
			if(difference.equals(1))
			{
				SignatureInfo signatureInfoOld =  signatureInfoService.get(signatureInfoId);
				SignatureInfo signature = new SignatureInfo();
				
				signature.setSignatureInfoStatus(SignatureInfoStatus.STATUS_ADD_Info);  //业务状态为缴费完成
				signature.setStatus(SignatureInfoStatus.STATUS_UNSTART);                //状态为不可用

				if(signature.getId()==null){
					signature.setSvn(signatureInfoService.getSVN());
				}
				
				signature.setWorkDealInfo(signatureInfoOld.getWorkDealInfo());
				signature.setSignatureAgent(agent);
				signature.setSignatureType(signatureType);
				signature.setSignatureAppName("印章业务");
				
				signature.setConfigApp(signatureInfoOld.getConfigApp());
				signature.setEnterDate(new Date());
				signature.setEnterUser(UserUtils.getUser());
				signature.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
				signature.setOfficeId(UserUtils.getUser().getOffice().getId());
				signature.setPrevId(signatureInfoId);
				signature.setFirstId(signatureInfoOld.getFirstId()==null?signatureInfoId:signatureInfoOld.getFirstId());
				
				//year为空代表是变更
				if(year==null){
					signature.setSignatureInfoType(SignatureInfoType.TYPE_CHANGE_SIGNATURE);
					signature.setYear(signatureInfoOld.getYear());
					signature.setStartDate(signatureInfoOld.getStartDate());
					signature.setEndDate(signatureInfoOld.getEndDate());
					
					
					//如果上一条印章业务已经改过证书外键
					if(signatureInfoOld.getOldWorkCertInfo()!=null){
						signature.setWorkCertInfo(signatureInfoOld.getWorkCertInfo());
						signature.setOldWorkCertInfo(signatureInfoOld.getOldWorkCertInfo());
					}else{
						signature.setWorkCertInfo(signatureInfoOld.getWorkCertInfo());
					}
				//year不为空代表是续期
				}else if(year!=null){
					signature.setSignatureInfoType(SignatureInfoType.TYPE_UPDATE_SIGNATURE);
					signature.setYear(year.toString());
					signature.setStartDate(signatureInfoOld.getStartDate());
					Calendar calendar = Calendar.getInstance();          //更新的起始时间为上一条印章业务的起始时间，结束时间为上一条业务的结束时间+365*year
					calendar.setTime(signatureInfoOld.getEndDate());
					
					calendar.add(Calendar.DATE, year*365);
					signature.setEndDate(calendar.getTime());
					
					signature.setWorkCertInfo(signatureInfoOld.getWorkCertInfo());
				}

				signatureInfoService.save(signature);
				
				signatureInfoOld.setDelFlag("1");
				signatureInfoService.save(signatureInfoOld);
				
				
				
				SignaturePayInfo pay = new SignaturePayInfo();
				
				//year为空代表是变更
				if(year==null){
					pay.setYear(Integer.parseInt(signatureInfoOld.getYear()));
					pay.setChangeSignature(workTotalMoney);
					pay.setUpdateSignature(0d);
				//year不为空代表是续期	
				}else if(year!=null){
					pay.setYear(year);
					pay.setChangeSignature(0d);
					pay.setUpdateSignature(workTotalMoney);
				}
				
				pay.setSignatureInfo(signature);
				pay.setMethodPos(methodPos);
				pay.setMethodMoney(methodMoney);
				pay.setAddSignature(0d);
				pay.setPosMoney(posMoney);
				pay.setCashMoney(cashMoney);
				pay.setTotalMoney(workTotalMoney);
				pay.setIsReceipt(userReceipt);
				signaturePayInfoService.save(pay);
				signatureInfoService.save(signature);
				if(pay.getIsReceipt()){
					this.receptOut(signature, pay);
				}
			}else if(difference.equals(2)) {
				
				SignatureInfo signature = signatureInfoService.get(signatureInfoId);
				signature.setSignatureAgent(agent);
				signature.setEnterDate(new Date());
				signature.setEnterUser(UserUtils.getUser());
				signature.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
				signature.setOfficeId(UserUtils.getUser().getOffice().getId());
				signature.setSignatureType(signatureType);
				if(year!=null)
				{
					signature.setYear(year.toString());
					Calendar cd = Calendar.getInstance(); 
					cd.add(Calendar.DATE, year*365);
					Date end=cd.getTime(); 
					signature.setEndDate(end);
				}
					signatureInfoService.save(signature);
					SignaturePayInfo pay = signaturePayInfoService.findBySignatureInfo(signature);
				
				
				if(userReceipt!=pay.getIsReceipt()){
					if(userReceipt==true){
						ReceiptInvoice receiptInvoice = new ReceiptInvoice();
						Office office = signature.getEnterUser().getOffice();
						List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
						receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
						receiptInvoice.setCompanyName(signature.getWorkDealInfo().getWorkCompany().getCompanyName());
						receiptInvoice.setReceiptMoney(receiptAmount);
						receiptInvoice.setReceiptType(3);//印章出库
						receiptInvoice.setDealInfoId(signature.getWorkDealInfo().getId());
						receiptInvoice.setCreateDate(new Date());
						receiptInvoice.setUpdateDate(new Date());
						receiptInvoice.setCreateBy(signature.getCreateBy());
						receiptInvoice.setUpdateBy(signature.getCreateBy());
						receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
						receiptInvoiceService.save(receiptInvoice);
						pay.setReceiptInvoice(receiptInvoice);
					}else{
						ReceiptInvoice receiptInvoice = pay.getReceiptInvoice();
						if(receiptInvoice!=null){
							receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
							receiptInvoiceService.save(receiptInvoice);
						}
					}
				}else{
					if(userReceipt==true){
						ReceiptInvoice receiptInvoice = pay.getReceiptInvoice();
						if(receiptInvoice!=null){
							//发票金额更改
							receiptInvoice.setReceiptMoney(receiptAmount);
							receiptInvoiceService.save(receiptInvoice);
						}
					}
				}
				
				
				
				
				if(year!=null)
				{
					pay.setYear(year);
				}
				pay.setMethodPos(methodPos);
				pay.setMethodMoney(methodMoney);
				if (types.equals("1")) {
					pay.setChangeSignature(workTotalMoney);
				}else if (types.equals("2")) {
					pay.setUpdateSignature(workTotalMoney);
				}
				
				pay.setPosMoney(posMoney);
				pay.setCashMoney(cashMoney);
				pay.setTotalMoney(workTotalMoney);
				pay.setIsReceipt(userReceipt);
				signaturePayInfoService.save(pay);


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + Global.getAdminPath() + "/signature/signatureInfo/?repage";
	}
	
	
	private void receptOut(SignatureInfo signature,SignaturePayInfo pay){
			ReceiptInvoice receiptInvoice = new ReceiptInvoice();
			Office office = signature.getEnterUser().getOffice();
			List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
			receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
			receiptInvoice.setCompanyName(signature.getWorkDealInfo().getWorkCompany().getCompanyName());
			receiptInvoice.setReceiptMoney(pay.getTotalMoney());
			receiptInvoice.setReceiptType(3);//印章出库
			receiptInvoice.setDealInfoId(signature.getWorkDealInfo().getId());
			receiptInvoice.setCreateDate(new Date());
			receiptInvoice.setUpdateDate(new Date());
			receiptInvoice.setCreateBy(signature.getCreateBy());
			receiptInvoice.setUpdateBy(signature.getCreateBy());
			receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			receiptInvoiceService.save(receiptInvoice);
			
			pay.setReceiptInvoice(receiptInvoice);
			signaturePayInfoService.save(pay);	
	}
	
	
	
	@RequestMapping(value ="check")
	@ResponseBody
	public String checkAdd(Long signatureId) throws JSONException
	{
		JSONObject json = new JSONObject();
		WorkDealInfo workDealInfo =  workDealInfoService.get(signatureId);
		String  string=	workDealInfo.getWorkCertInfo().getSerialnumber();
		System.out.println(string);
		SignatureInfo signature = signatureInfoService.getCanDoByCertSn(workDealInfo.getWorkCertInfo().getSerialnumber());
		try {
			if(signature==null)
			{
				json.put("status","0");
			}else {
				json.put("status","1");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			json.put("status","0");
			e.printStackTrace();
		}
		return json.toString();
	}
	
	
	
	
	/*
	 * 业务生成新证书之后,原来证书上的印章要挪到新证书上
	 * */
	@RequestMapping(value = "changeCertVal")
	@ResponseBody
	public String changeCertVal(String certSn,String keySn) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			WorkCertInfo certInfo = workCertInfoService.getByCertSn(certSn);
			WorkDealInfo dealInfo = workDealInfoService.findByCertSnAndKeySn(certSn, keySn);
			if(certInfo!=null&&dealInfo!=null){
				WorkDealInfo preDealInfo = workDealInfoService.get(dealInfo.getPrevId());
				if(preDealInfo!=null){
					List<SignatureInfo> signatureInfos = signatureInfoService.findBycertSn(preDealInfo.getWorkCertInfo().getSerialnumber());
					if(!signatureInfos.isEmpty()){
						for(SignatureInfo info:signatureInfos){
							info.setOldWorkCertInfo(info.getWorkCertInfo());
							info.setWorkCertInfo(certInfo);
							info.setWorkDealInfo(dealInfo);
							
							signatureInfoService.save(info);
						}
					}
				}			
			json.put("status", 1);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("msg", e.getMessage());
		}
		
		
		return json.toString();
		
	}
	@RequiresPermissions("signature:signatureInfo:view")
	@RequestMapping(value = "sealLog")
	public String sealLog(SignatureInfo signatureInfo,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SignatureInfo> page = signatureInfoService.findAll(new Page<SignatureInfo>(request, response), signatureInfo); 
        List<Office> offices = officeService.findAll();
        model.addAttribute("page", page);
        model.addAttribute("signatureTypeMap", SignatureInfoType.SignatureInfoTypeMap);
        model.addAttribute("offices", offices);
		return "modules/log/sealLogList";
	}

	@RequestMapping(value="delete")
	public String delete(Long signarureId)
	{
		SignatureInfo signatureInfo =signatureInfoService.get(signarureId);
		SignaturePayInfo signaturePayInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
		/*if(signaturePayInfo.getIsReceipt())
		{
			Office office = signatureInfo.getEnterUser().getOffice();
			List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
			ReceiptDepotInfo receiptDepotInfo =depotInfos.get(0);
			receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()+signaturePayInfo.getTotalMoney());
			receiptDepotInfoService.save(receiptDepotInfo);
			ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
			receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
			receiptEnterInfo.setCreateDate(new Date());
			receiptEnterInfo.setNow_Money(signaturePayInfo.getTotalMoney());
			receiptEnterInfo.setReceiptType(5);
			User user = UserUtils.getUser();
			receiptEnterInfo.setCreateBy(user);
			receiptEnterInfoService.save(receiptEnterInfo);
		}*/
		if(signatureInfo.getPrevId()!=null)
		{
			SignatureInfo signatureInfoUp =signatureInfoService.get(signatureInfo.getPrevId());
			signatureInfoUp.setDelFlag(WorkDealInfo.DEL_FLAG_NORMAL);
		}
		signatureInfoService.delete(signarureId);
		return "redirect:" + Global.getAdminPath() + "/signature/signatureInfo/";
	}
}
