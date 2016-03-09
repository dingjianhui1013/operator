/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.entity.SettlementLog;
import com.itrus.ca.modules.settle.entity.WorkDealInfo_settlementLog;
import com.itrus.ca.modules.settle.service.SettlementLogService;
import com.itrus.ca.modules.settle.service.WorkDealInfo_settlementLogService;
import com.itrus.ca.modules.settle.vo.PayableDetailVo;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;

import freemarker.template.SimpleDate;

/**
 * 年限结算保存Controller
 * @author DingJianHui
 * @version 2016-03-07
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/settlementLog")
public class SettlementLogController extends BaseController {

	@Autowired
	private SettlementLogService settlementLogService;
	
	@Autowired
	private ConfigCommercialAgentService configCommercialAgentService;
	
	
	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	@Autowired
	private  WorkFinancePayInfoRelationService workFinancePayInfoRelationService;
	
	@Autowired
	private WorkDealInfo_settlementLogService WorkDealInfo_settlementLogService;
	
	private LogUtil logUtil = new LogUtil();
	
	
	@ModelAttribute
	public SettlementLog get(@RequestParam(required=false) Long id) {
		if (id != null){
			return settlementLogService.get(id);
		}else{
			return new SettlementLog();
		}
	}
	
	@RequiresPermissions("settle:settlementLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(SettlementLog settlementLog, HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime
			) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			settlementLog.setCreateBy(user);
		}
		if(comAgentId!=null)
		{
			String comAgentName = configCommercialAgentService.findById(comAgentId).get(0).getAgentName();
			settlementLog.setComagentName(comAgentName);		
		}
		if (appId!=null) {
			String appName = configAgentAppRelationService.findByAppName(appId).get(0).getConfigApp().getAppName();
			settlementLog.setAppName(appName);
		}
		if(startTime!=null)
		{
			settlementLog.setStartTime(startTime);
		}
		if(endTime!=null)
		{
			settlementLog.setEndTime(endTime);
		}
		List<SettlementLog> list = settlementLogService.findAll(settlementLog);
		
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		
		List<ConfigAgentAppRelation> relationByComAgentId =  configAgentAppRelationService.findByComAgentId(comAgentId);
		model.addAttribute("relationByComAgentId", relationByComAgentId);
        model.addAttribute("page", list);
        model.addAttribute("comAgentId", comAgentId);
        model.addAttribute("appId", appId);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("proType", ProductType.productTypeStrMap);
		return "modules/settle/settlementLogList";
	}
	@RequiresPermissions("settle:settlementLog:view")
	@RequestMapping(value = "see")
	public String see(SettlementLog settlementLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			settlementLog.setCreateBy(user);
		}
		SettlementLog  settlementLogs= settlementLogService.get(settlementLog.getId());
		ConfigCommercialAgent comAgent = configCommercialAgentService.findByName(settlementLogs.getComagentName()).get(0);
		Set<WorkDealInfo_settlementLog> workDealInfos = settlementLogs.getWorkDealInfos();
		
		List<WorkDealInfo> dealInfos = new ArrayList<WorkDealInfo>();
		for (WorkDealInfo_settlementLog ws : workDealInfos) { 
			WorkDealInfo_settlementLog wsl = WorkDealInfo_settlementLogService.get(ws.getId());
		    dealInfos.add(workDealInfoService.get(wsl.getWorkDealInfoId()));
		} 
		Integer lenth = 0;
		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
			int totalAgentYear = comAgent.getSettlementPeriod();
			List<WorkDealInfo> infos = new ArrayList<WorkDealInfo>();
			infos.add(dealInfo);
			WorkDealInfo info = workDealInfoService.findDealInfo(dealInfo.getId());
			if (info!=null) {
				infos.add(info);
			}
			while(info!=null){
				info = workDealInfoService.findDealInfo(info.getId());
				if (info!=null) {
					infos.add(info);
				}
			}
			List<PayableDetailVo> detailList = new ArrayList<PayableDetailVo>();
			
			Date endLastDate = new Date();
			Calendar calendar = Calendar.getInstance();
	        
	        calendar.setTime(dealInfo.getBusinessCardUserDate());
	        calendar.add(Calendar.YEAR, totalAgentYear);
	        endLastDate = calendar.getTime();
			
			int yjNum = 0;
			int lastNum = 0;
			
			for (int j = 0; j < infos.size(); j++) {
				WorkDealInfo prvedDealInfo = infos.get(j);
				PayableDetailVo detailVo = new PayableDetailVo();
				String dealInfoType ="";
				if (infos.get(j).getDealInfoType()!=null) {
					dealInfoType= WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType()) +" ";
				}
				if (infos.get(j).getDealInfoType1()!=null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType1()) +" ";
				}
				if (infos.get(j).getDealInfoType2()!=null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType2()) +" ";				
				}
				if (infos.get(j).getDealInfoType3()!=null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType3());	
				}
						
				if (prvedDealInfo.getPayType()==null) {
					Set<String> payMethods=new LinkedHashSet<String>();
					if(infos.get(j).getWorkPayInfo()!=null)
					{
						if(infos.get(j).getWorkPayInfo().getRelationMethod()==null)
						{
							if(infos.get(j).getWorkPayInfo().getMethodPos()==true)
							{
								payMethods.add("Pos付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodMoney()==true)
							{
								payMethods.add("现金付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodAlipay()==true)
							{
								payMethods.add("支付宝付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodBank()==true)
							{
								payMethods.add("银行付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodGov()==true)
							{
								payMethods.add("政府统一采购");
							}
							if(infos.get(j).getWorkPayInfo().getMethodContract()==true)
							{
								payMethods.add("合同采购");
							}
						}else
						{
							List<WorkFinancePayInfoRelation> workfinancePayinfos=workFinancePayInfoRelationService.findByPayInfoId(infos.get(j).getWorkPayInfo().getId());
							for(int w=0;w<workfinancePayinfos.size();w++)
							{
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==1)
									{
										payMethods.add("现金付款");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==2)
									{
										payMethods.add("POS付款");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==3)
									{
										payMethods.add("银行转账");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==4)
									{
										payMethods.add("支付宝付款");
									}
								}
							
						}
						detailVo.setMethod(payMethods);
					}
					detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
					detailVo.setEndDate(infos.get(j).getNotafter());
					detailVo.setDealInfoType(dealInfoType);
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}
				if (!prvedDealInfo.getPayType().equals(1)) {
					Set<String> payMethods=new LinkedHashSet<String>();
					if(infos.get(j).getWorkPayInfo()!=null)
					{
						if(infos.get(j).getWorkPayInfo().getRelationMethod()==null)
						{
							if(infos.get(j).getWorkPayInfo().getMethodPos()==true)
							{
								payMethods.add("Pos付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodMoney()==true)
							{
								payMethods.add("现金付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodAlipay()==true)
							{
								payMethods.add("支付宝付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodBank()==true)
							{
								payMethods.add("银行付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodGov()==true)
							{
								payMethods.add("政府统一采购");
							}
							if(infos.get(j).getWorkPayInfo().getMethodContract()==true)
							{
								payMethods.add("合同采购");
							}
						}else
						{
							List<WorkFinancePayInfoRelation> workfinancePayinfos=workFinancePayInfoRelationService.findByPayInfoId(infos.get(j).getWorkPayInfo().getId());
							for(int w=0;w<workfinancePayinfos.size();w++)
							{
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==1)
									{
										payMethods.add("现金付款");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==2)
									{
										payMethods.add("POS付款");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==3)
									{
										payMethods.add("银行转账");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==4)
									{
										payMethods.add("支付宝付款");
									}
								}
							
						}
						detailVo.setMethod(payMethods);
					}
					
					detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
					detailVo.setEndDate(infos.get(j).getNotafter());
					detailVo.setDealInfoType(dealInfoType);
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}
				
				if (infos.get(j).getDealInfoType()!=null) {
					Set<String> payMethods=new LinkedHashSet<String>();
					if(infos.get(j).getWorkPayInfo()!=null)
					{
						if(infos.get(j).getWorkPayInfo().getRelationMethod()==null)
						{
							if(infos.get(j).getWorkPayInfo().getMethodPos()==true)
							{
								payMethods.add("Pos付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodMoney()==true)
							{
								payMethods.add("现金付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodAlipay()==true)
							{
								payMethods.add("支付宝付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodBank()==true)
							{
								payMethods.add("银行付款");
							}
							if(infos.get(j).getWorkPayInfo().getMethodGov()==true)
							{
								payMethods.add("政府统一采购");
							}
							if(infos.get(j).getWorkPayInfo().getMethodContract()==true)
							{
								payMethods.add("合同采购");
							}
						}else
						{
							List<WorkFinancePayInfoRelation> workfinancePayinfos=workFinancePayInfoRelationService.findByPayInfoId(infos.get(j).getWorkPayInfo().getId());
							for(int w=0;w<workfinancePayinfos.size();w++)
							{
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==1)
									{
										payMethods.add("现金付款");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==2)
									{
										payMethods.add("POS付款");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==3)
									{
										payMethods.add("银行转账");
									}
									if(workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod()==4)
									{
										payMethods.add("支付宝付款");
									}
								}
							
						}
						detailVo.setMethod(payMethods);
					}
					if (infos.get(j).getDealInfoType().equals(1)||infos.get(j).getDealInfoType().equals(0)) {
						if(infos.get(j).getBusinessCardUserDate()!=null)
						{
							if (infos.get(j).getBusinessCardUserDate().getTime()>endLastDate.getTime()) {
								detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
								detailVo.setEndDate(infos.get(j).getNotafter());
								detailVo.setDealInfoType(dealInfoType);
								detailVo.setSettleYear("0");
								detailList.add(detailVo);
							}else if (infos.get(j).getNotafter().getTime()<endLastDate.getTime()) {
								yjNum += infos.get(j).getYear();
								lastNum = infos.get(j).getYear();
								detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
								detailVo.setEndDate(infos.get(j).getNotafter());
								detailVo.setDealInfoType(dealInfoType);
								detailVo.setSettleYear(infos.get(j).getYear().toString());
								detailList.add(detailVo);
							}else if (infos.get(j).getBusinessCardUserDate().getTime()<endLastDate.getTime()&&infos.get(j).getNotafter().getTime()>endLastDate.getTime()) {
								long between = endLastDate.getTime()-infos.get(j).getBusinessCardUserDate().getTime();
								long a  = between/31536000000L; 
								int yy = (int) Math.ceil(a);
								yjNum += yy;
								lastNum = yy;
								detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
								detailVo.setEndDate(infos.get(j).getNotafter());
								detailVo.setDealInfoType(dealInfoType);
								detailVo.setSettleYear(yy+"");
								detailList.add(detailVo);
							}
						}
					}
				}
			}
			dealInfos.get(i).setYyNum(yjNum);
			dealInfos.get(i).setTotalNum(totalAgentYear);
			dealInfos.get(i).setDetailList(detailList);
			dealInfos.get(i).setLastNum(lastNum);
			if (detailList.size()>lenth) {
				lenth = detailList.size();
			}
		}
		model.addAttribute("settlementLogs", settlementLogs);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("dealInfos", dealInfos);
		model.addAttribute("lenth", lenth);
		return "modules/settle/settlementLogForm";
	}

	@RequiresPermissions("settle:settlementLog:view")
	@RequestMapping(value = "form")
	public String form(SettlementLog settlementLog, Model model) {
		model.addAttribute("settlementLog", settlementLog);
		return "modules/settle/settlementLogForm";
	}

	@RequiresPermissions("settle:settlementLog:edit")
	@RequestMapping(value = "save")
	public String save(SettlementLog settlementLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, settlementLog)){
			return form(settlementLog, model);
		}
		settlementLogService.save(settlementLog);
//		addMessage(redirectAttributes, "保存年限结算保存'" + settlementLog.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/settlementLog/?repage";
	}
	
	@RequestMapping(value = "isExist")
	@ResponseBody
	public String isExist(
			HttpServletRequest request,
			HttpServletResponse response, 
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "remarks", required = false) String remarks)
	{
		SettlementLog settlementLog = new SettlementLog();
		if(comAgentId!=null&&comAgentId!=0)
		{
			String comAgentName = configCommercialAgentService.findById(comAgentId).get(0).getAgentName();
			settlementLog.setComagentName(comAgentName);		
		}
		if (appId!=null&&appId!=0) {
			String appName = configAgentAppRelationService.findByAppName(appId).get(0).getConfigApp().getAppName();
			settlementLog.setAppName(appName);
		}
		StringBuffer productNameList = new StringBuffer(0);
		
		if (productIds!=null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				String productName = configProductService.get(Long.parseLong(products[i].toString())).getProductName();
				productNameList.append(" "+productName);
			}
		}
		settlementLog.setProductName(productNameList.toString());
		settlementLog.setStartTime(startTime);
		settlementLog.setEndTime(endTime);
		List<SettlementLog> list = settlementLogService.find(settlementLog);
		JSONObject json = new JSONObject();
		if(list.size()>0)
		{
			json.put("status", 1);
			json.put("msg","此查询条件已保存是否重新保存");
		}else
		{
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@RequestMapping(value = "deleteAndSave")
	@ResponseBody
	public String deleteAndSave(
			HttpServletRequest request,
			HttpServletResponse response, 
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "remarks", required = false) String remarks)
	{
		SettlementLog settlementLog = new SettlementLog();
		settlementLog.setRemarks(remarks);
		String userName = UserUtils.getUser().getName();
		settlementLog.setUserName(userName);
		if(comAgentId!=null)
		{
			List<ConfigCommercialAgent> configCommercialAgent = configCommercialAgentService.findById(comAgentId);
			String comAgentName = configCommercialAgent.get(0).getAgentName();
			Date start = configCommercialAgent.get(0).getAgentContractStart();
			Date end = configCommercialAgent.get(0).getAgentContractEnd();
			String  validity = new SimpleDateFormat("yyyy-mm-dd").format(start)+" 至  "+new SimpleDateFormat("yyyy-mm-dd").format(end);
			settlementLog.setComagentValidity(validity);
			settlementLog.setComagentName(comAgentName);			
		}
		if (appId!=null) {
			String appName = configAgentAppRelationService.findByAppName(appId).get(0).getConfigApp().getAppName();
			settlementLog.setAppName(appName);
			List<ConfigProduct> products = configProductService.findByAppId(appId);
		}
	
		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId==null) {
			List<ConfigAgentAppRelation> relation =  configAgentAppRelationService.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();
		StringBuffer productNameList = new StringBuffer();
		if (productIds!=null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
				String productName = configProductService.get(Long.parseLong(products[i].toString())).getProductName();
				productNameList.append(" "+productName);
			}
		}
		settlementLog.setProductName(productNameList.toString());
		settlementLog.setStartTime(startTime);
		settlementLog.setEndTime(endTime);
		
		List<SettlementLog> list = settlementLogService.find(settlementLog);
		if(list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				settlementLogService.delete(list.get(i).getId());
			}
		}
		
		
		Date start = new Date();
		Date end = new Date();
		if (startTime!=null && !startTime.equals("")) {
			if (comAgent.getAgentContractStart().getTime()>startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			}else{
				start = startTime;
			}
		}else{
			start = new Date(comAgent.getAgentContractStart().getTime());
		}
		if (endTime!=null && !endTime.equals("")) {
			if (comAgent.getAgentContractEnd().getTime()<startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			}else{
				start = startTime;
			}
		}else{
			
			end = new Date(comAgent.getAgentContractEnd().getTime());
		}
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfo(appId,appIds,productIdList,start,end);
		Set<WorkDealInfo_settlementLog> workDealInfos = new HashSet<WorkDealInfo_settlementLog>();
		JSONObject json = new JSONObject();
		for(int i=0;i<dealInfos.size();i++)
		{
			try {
				WorkDealInfo_settlementLog work_sett = new WorkDealInfo_settlementLog();
				work_sett.setWorkDealInfoId(dealInfos.get(i).getId());
				workDealInfos.add(work_sett);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				json.put("status",0);
			}
		}
		settlementLog.setWorkDealInfos(workDealInfos);
		try {
			settlementLogService.save(settlementLog);
			logUtil.saveSysLog("年限结算保存", "查询项为  代理商："+settlementLog.getComagentName()+
								settlementLog.getAppName()==null?"":"应用名称："+settlementLog.getAppName()+
								settlementLog.getProductName()==null?"":"产品名称："+settlementLog.getAppName()+
								settlementLog.getStartTime()==null?"":"业务办理起始时间："+settlementLog.getStartTime()+
								settlementLog.getEndTime()==null?"":"业务办理结束时间："+settlementLog.getEndTime()+"删除并保存成功", "");
			json.put("status", 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.saveSysLog("年限结算保存", "查询项为  代理商："+settlementLog.getComagentName()+
					settlementLog.getAppName()==null?"":"应用名称："+settlementLog.getAppName()+
					settlementLog.getProductName()==null?"":"产品名称："+settlementLog.getAppName()+
					settlementLog.getStartTime()==null?"":"业务办理起始时间："+settlementLog.getStartTime()+
					settlementLog.getEndTime()==null?"":"业务办理结束时间："+settlementLog.getEndTime()+"删除并保存失败", "");
			json.put("status", 0);
			e.printStackTrace();
			
		}
		return json.toString();
	}
	
	@RequestMapping(value = "saveSettlementLog")
	@ResponseBody
	public String saveSettlementLog(
			HttpServletRequest request,
			HttpServletResponse response, 
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "remarks", required = false) String remarks)
	{
		SettlementLog settlementLog = new SettlementLog();
		settlementLog.setRemarks(remarks);
		String userName = UserUtils.getUser().getName();
		settlementLog.setUserName(userName);
		if(comAgentId!=null)
		{
			List<ConfigCommercialAgent> configCommercialAgent = configCommercialAgentService.findById(comAgentId);
			String comAgentName = configCommercialAgent.get(0).getAgentName();
			Date start = configCommercialAgent.get(0).getAgentContractStart();
			Date end = configCommercialAgent.get(0).getAgentContractEnd();
			String  validity = new SimpleDateFormat("yyyy-mm-dd").format(start)+" 至  "+new SimpleDateFormat("yyyy-mm-dd").format(end);
			settlementLog.setComagentValidity(validity);
			settlementLog.setComagentName(comAgentName);		
		}
		if (appId!=null) {
			String appName = configAgentAppRelationService.findByAppName(appId).get(0).getConfigApp().getAppName();
			settlementLog.setAppName(appName);
//			List<ConfigProduct> products = configProductService.findByAppId(appId);
		}
	
		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId==null) {
			List<ConfigAgentAppRelation> relation =  configAgentAppRelationService.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();
		StringBuffer productNameList = new StringBuffer();
		if (productIds!=null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
				String productName = configProductService.get(Long.parseLong(products[i].toString())).getProductName();
				productNameList.append(" "+productName);
			}
		}
		settlementLog.setProductName(productNameList.toString());
		settlementLog.setStartTime(startTime);
		settlementLog.setEndTime(endTime);
		
		Date start = new Date();
		Date end = new Date();
		if (startTime!=null && !startTime.equals("")) {
			if (comAgent.getAgentContractStart().getTime()>startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			}else{
				start = startTime;
			}
		}else{
			start = new Date(comAgent.getAgentContractStart().getTime());
		}
		if (endTime!=null && !endTime.equals("")) {
			if (comAgent.getAgentContractEnd().getTime()<startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			}else{
				start = startTime;
			}
		}else{
			
			end = new Date(comAgent.getAgentContractEnd().getTime());
		}
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfo(appId,appIds,productIdList,start,end);
		Set<WorkDealInfo_settlementLog> workDealInfos = new HashSet<WorkDealInfo_settlementLog>();
		for(int i=0;i<dealInfos.size();i++)
		{
			WorkDealInfo_settlementLog work_sett = new WorkDealInfo_settlementLog();
			work_sett.setWorkDealInfoId(dealInfos.get(i).getId());
			workDealInfos.add(work_sett);
			
		}
		settlementLog.setWorkDealInfos(workDealInfos);
		JSONObject json = new JSONObject();
		try {
			settlementLogService.save(settlementLog);
			logUtil.saveSysLog("年限结算保存", "查询项为  代理商："+settlementLog.getComagentName()+
								settlementLog.getAppName()==null?"":"应用名称："+settlementLog.getAppName()+
								settlementLog.getProductName()==null?"":"产品名称："+settlementLog.getAppName()+
								settlementLog.getStartTime()==null?"":"业务办理起始时间："+settlementLog.getStartTime()+
								settlementLog.getEndTime()==null?"":"业务办理结束时间："+settlementLog.getEndTime()+"保存成功", "");
			json.put("status", 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.saveSysLog("年限结算保存", "查询项为  代理商："+settlementLog.getComagentName()+
					settlementLog.getAppName()==null?"":"应用名称："+settlementLog.getAppName()+
					settlementLog.getProductName()==null?"":"产品名称："+settlementLog.getAppName()+
					settlementLog.getStartTime()==null?"":"业务办理起始时间："+settlementLog.getStartTime()+
					settlementLog.getEndTime()==null?"":"业务办理结束时间："+settlementLog.getEndTime()+"保存失败", "");
			json.put("status", 0);
			e.printStackTrace();
			
		}
		return json.toString();
	}
	
	@RequiresPermissions("settle:settlementLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		settlementLogService.delete(id);
		addMessage(redirectAttributes, "删除年限结算保存成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/settlementLog/?repage";
	}

	@RequestMapping(value = "setApps")
	@ResponseBody
	public String setApps(Long comAgentId) throws JSONException{
		List<ConfigAgentAppRelation> relation =  configAgentAppRelationService.findByComAgentId(comAgentId);
		
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < relation.size(); i++) {
			json = new JSONObject();
			json.put("id", relation.get(i).getConfigApp().getId());
			json.put("name", relation.get(i).getConfigApp().getAppName());
			array.put(json);
		}
		return array.toString();
	}
	
}
