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
import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.dao.SettlementCollectDataDao;
import com.itrus.ca.modules.settle.entity.SettlementCollectData;
import com.itrus.ca.modules.settle.entity.SettlementLog;
import com.itrus.ca.modules.settle.entity.WorkDealInfo_settlementLog;
import com.itrus.ca.modules.settle.service.SettlementCollectDataService;
import com.itrus.ca.modules.settle.service.SettlementLogService;
import com.itrus.ca.modules.settle.service.WorkDealInfo_settlementLogService;
import com.itrus.ca.modules.settle.vo.PayableDetailVo;
import com.itrus.ca.modules.settle.vo.SettleCollectVO;
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
	private SettlementCollectDataService settlementCollectDataService;
	
	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	
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
		Date end = null;
		if(endTime!=null)
		{
			end = endTime;
			end.setHours(23);
			end.setSeconds(59);
			end.setMinutes(59);
		}
		
		List<SettlementLog> list = settlementLogService.findAll(settlementLog,end,startTime);
		
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
		
		
		List<SettlementCollectData> collect = settlementCollectDataService.findByLogId(settlementLog.getId());

		
		

		model.addAttribute("settlementLog", settlementLog);
		model.addAttribute("collect", collect);
	
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
		Date endT = endTime;
		endT.setHours(23);
		endT.setMinutes(59);
		endT.setSeconds(59);
		settlementLog.setEndTime(endT);
		List<SettlementLog> list = settlementLogService.find(settlementLog);
		JSONObject json = new JSONObject();
		if(list.size()>0)
		{
			json.put("status", 1);
			json.put("remarks",remarks);
			json.put("msg","此查询条件已保存是否重新保存");
		}else
		{
			json.put("status", 0);
		}
		return json.toString();
	}
	
	
	//方法没用
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
			String  validity = new SimpleDateFormat("yyyy-MM-dd").format(start)+" 至  "+new SimpleDateFormat("yyyy-MM-dd").format(end);
			settlementLog.setComagentValidity(validity);
			settlementLog.setComagentName(comAgentName);		
		}
		if (appId!=null) {
			String appName = configAgentAppRelationService.findByAppName(appId).get(0).getConfigApp().getAppName();
			settlementLog.setAppName(appName);
		}
		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId == null) {
			List<ConfigAgentAppRelation> relation = configAgentAppRelationService.findByComAgentId(comAgentId);
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
		Date endT = endTime;
		endT.setHours(23);
		endT.setMinutes(59);
		endT.setSeconds(59);
		settlementLog.setEndTime(endT);
		
		List<SettlementLog> list = settlementLogService.find(settlementLog);
		if(list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				settlementLogService.delete(list.get(i).getId());
			}
		}
		
		

		

		JSONObject json = new JSONObject();
	
	
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
			String  validity = new SimpleDateFormat("yyyy-MM-dd").format(start)+" 至  "+new SimpleDateFormat("yyyy-MM-dd").format(end);
			settlementLog.setComagentValidity(validity);
			settlementLog.setComagentName(comAgentName);		
		}
		if (appId!=null) {
			String appName = configAgentAppRelationService.findByAppName(appId).get(0).getConfigApp().getAppName();
			settlementLog.setAppName(appName);
		}
		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId == null) {
			List<ConfigAgentAppRelation> relation = configAgentAppRelationService.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();
		StringBuffer productNameList = new StringBuffer();
		if (productIds != null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
				String productName = configProductService.get(Long.parseLong(products[i].toString())).getProductName();
				productNameList.append(" "+productName);
			}
		}
		settlementLog.setProductName(productNameList.toString());
		settlementLog.setStartTime(startTime);
		Date endT = endTime;
		endT.setHours(23);
		endT.setMinutes(59);
		endT.setSeconds(59);
		settlementLog.setEndTime(endT);

		List<SettlementCollectData> collect = Lists.newArrayList();
		for(Long productId:productIdList){
			ConfigProduct product = configProductService.get(productId);
			SettlementCollectData data = new SettlementCollectData(product.getProductName(), product.getProductLabel());
			data.setProductName( ProductType.productTypeStrMap.get(product.getProductName())+"["+(product.getProductLabel()==0?"通用":"专用")+"]");
			collect.add(data);
		}
		
		// 先得到业务办理时间范围内所有的新增和更新 然后找到每个业务链的头部，和代理商合同有效期比较，符合条件放入list
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfoByAdd(appId, appIds, productIdList, startTime,
				endTime,new Date(comAgent.getAgentContractStart().getTime()),
				new Date(comAgent.getAgentContractEnd().getTime()));
		

		
		//得到此时间段内更新的业务
		List<WorkDealInfo> dealInfoUpdates = workDealInfoService.findDealInfoByUpdate(appId, appIds, productIdList,
				startTime, endTime);

			
		//循环更新业务,如果更新业务所在业务链的首条业务在合同有效期范围之内，则放入set
		for (WorkDealInfo info : dealInfoUpdates) {
			
			WorkDealInfo first = workDealInfoService.findFirstByFirstCertSN(info.getFirstCertSN());
			
				if (first.getBusinessCardUserDate().after(new Date(comAgent.getAgentContractStart().getTime()))
					&& first.getBusinessCardUserDate().before(new Date(comAgent.getAgentContractEnd().getTime()))) {
					dealInfos.add(first);
				}
		}
		
	
		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
			//结算年限
			int totalAgentYear = comAgent.getSettlementPeriod();
			
			Calendar firstCalendar = Calendar.getInstance();
			firstCalendar.setTime(dealInfo.getBusinessCardUserDate());
			firstCalendar.set(Calendar.HOUR_OF_DAY, 0);
			firstCalendar.set(Calendar.SECOND, 0);
			firstCalendar.set(Calendar.MINUTE, 0);
			
			Date firstDealBusiness = firstCalendar.getTime();
			
			// endLastDate 最终截止时间:业务链首条的办理时间+代理商的结算年限
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(firstDealBusiness);
			calendar.add(Calendar.YEAR, totalAgentYear);
			Date endLastDate = calendar.getTime();
			int lastNum = 0;   //本次结算
			
			//找到整个业务链
			List<WorkDealInfo> infos = workDealInfoService.findChainByFirstCertSN(dealInfo.getCertSn(),endTime,endLastDate);
			
			

			//本期需要结算的 首先肯定是新增或者更新的
			WorkDealInfo currentDealInfo = infos.get(infos.size()-1);
			ConfigChargeAgent agent = configChargeAgentService.get(currentDealInfo.getConfigChargeAgentId());
			
			
			Calendar currentBussinessCalendar = Calendar.getInstance();
			currentBussinessCalendar.setTime(currentDealInfo.getBusinessCardUserDate());
			currentBussinessCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentBussinessCalendar.set(Calendar.SECOND, 0);
			currentBussinessCalendar.set(Calendar.MINUTE, 0);
			
			Date currentDealBusiness = currentBussinessCalendar.getTime();
			
			Calendar currentNotAfterCalendar = Calendar.getInstance();
			currentNotAfterCalendar.setTime(currentDealInfo.getNotafter());
			currentNotAfterCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentNotAfterCalendar.set(Calendar.SECOND, 0);
			currentNotAfterCalendar.set(Calendar.MINUTE, 0);
			
			Date currentDealNotAfter = currentNotAfterCalendar.getTime();
			
			//如果模板类型不是标准, 则结算年限为0
			if (!agent.getTempStyle().equals("1")) {
					continue;
			}
			//如果业务到期时间在最终截止日期之内,则参与结算  结算年限为证书办理年限	
			else if (currentDealNotAfter.getTime() < endLastDate.getTime()) {
				
					lastNum += currentDealInfo.getYear();
					
			} 
			
			//如果业务制证时间在最终截止日期之内而业务到期时间在最终截止日期之外,结算年限为
			else if (currentDealBusiness.getTime() < endLastDate.getTime()
					&& currentDealNotAfter.getTime() > endLastDate.getTime()) {
				    long between = endLastDate.getTime() - currentDealBusiness.getTime();
					long a = between / 31536000000L;
					int yy = (int) Math.ceil(a);
					lastNum += yy;
					
			}
			
			
			//汇总
			for(SettlementCollectData data:collect){
				if(currentDealInfo.getConfigProduct().getProductName().equals(data.getProductId())&&currentDealInfo.getConfigProduct().getProductLabel()==data.getProductLabel()){
				
				if(currentDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)){
					if(lastNum== 1){
						data.setAdd1(data.getAdd1()+1);
					}
					if(lastNum== 2){
						data.setAdd2(data.getAdd2()+1);
					}
					if(lastNum== 3){
						data.setAdd3(data.getAdd3()+1);
					}
					if(lastNum== 4){
						data.setAdd4(data.getAdd4()+1);
					}
					if(lastNum== 5){
						data.setAdd5(data.getAdd5()+1);
					}
				}else if(currentDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)){
					if(lastNum== 1){
						data.setUpdate1(data.getUpdate1()+1);
					}
					if(lastNum== 2){
						data.setUpdate2(data.getUpdate2()+1);
					}
					if(lastNum== 3){
						data.setUpdate3(data.getUpdate3()+1);
					}
					if(lastNum== 4){
						data.setUpdate4(data.getUpdate4()+1);
					}
					if(lastNum== 5){
						data.setUpdate5(data.getUpdate5()+1);
					}
				}
					
					
					
				}
			}
			
			
			
		}
		
		
		JSONObject json = new JSONObject();
		try {
			settlementLogService.save(settlementLog);
			
			for(SettlementCollectData data:collect){
				data.setSettlementLogId(settlementLog.getId());
				settlementCollectDataService.save(data);
			}
			
			
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
