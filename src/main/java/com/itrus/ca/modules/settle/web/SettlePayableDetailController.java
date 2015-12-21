package com.itrus.ca.modules.settle.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.sql.visitor.functions.If;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.vo.PayableDetailVo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

/**
 * 年限结算表
 * 
 * @author huhao
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/settlePayableDetail")
public class SettlePayableDetailController extends BaseController {
	@Autowired
	private ConfigCommercialAgentService configCommercialAgentService;
	
	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@RequiresPermissions("work:settlePayableDetail:view")
	@RequestMapping(value = "list")
	public String settlePayableDetailList(
			HttpServletRequest request,
			HttpServletResponse response, 
			Model model, 
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime
			
			){
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		if (comAgentId==null) {
			return "modules/settle/settlePayableDetail";
		}
		model.addAttribute("appId", appId);
		model.addAttribute("productIds", productIds);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
			
		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId==null) {
			List<ConfigAgentAppRelation> relation =  configAgentAppRelationService.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();
		if (productIds!=null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
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
		List<WorkDealInfo> dealInfos = 
				workDealInfoService.findDealInfo(appId,appIds,productIdList,start,end);
		
		Integer lenth = 0;
		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
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
			for (int j = 0; j < infos.size(); j++) {
				PayableDetailVo detailVo = new PayableDetailVo();
				if (infos.get(j).getDealInfoType()!=null) {
					if (infos.get(j).getDealInfoType().equals(1)||infos.get(j).getDealInfoType().equals(0)) {
						detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
						detailVo.setEndDate(infos.get(j).getNotafter());
						detailVo.setDealInfoType(WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType()));
						detailVo.setSettleYear(infos.get(j).getYear().toString());
						detailList.add(detailVo);
					}
				}
				
			}
			
			dealInfos.get(i).setDetailList(detailList);
			if (detailList.size()>lenth) {
				lenth = detailList.size();
			}
		}
		
//		List<String> = 
//		for (int i = 0; i < lenth; i++) {
//			
//		}
//		
		
		model.addAttribute("dealInfos", dealInfos);
		model.addAttribute("lenth", lenth);
		return "modules/settle/settlePayableDetail";
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
	
	@RequestMapping(value = "setProducts")
	@ResponseBody
	public String setProducts(Long appId) throws JSONException{
		List<ConfigProduct> products = configProductService.findByAppId(appId);
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < products.size(); i++) {
			json = new JSONObject();
			json.put("id", products.get(i).getId());
			System.out.println(products.get(i).getId()+"===="+ProductType.productTypeStrMap.get(products.get(i).getProductName()));
			json.put("name", ProductType.productTypeStrMap.get(products.get(i).getProductName()));
			array.put(json);
		}
		return array.toString();
	}

}
