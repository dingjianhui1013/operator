package com.itrus.ca.modules.settle.web;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.utils.DateUtils;
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
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String settlePayableDetailList(Model model){
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		model.addAttribute("startTime", DateUtils.firstDayOfMonth(new Date()));
		model.addAttribute("endTime", new Date());
		return "modules/settle/settlePayableDetail";
	}
	
	@RequiresPermissions("work:settlePayableDetail:view")
	@RequestMapping(value = "list",method = RequestMethod.POST)
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
			
			for (int j = 0; j < infos.size(); j++) {
				WorkDealInfo prvedDealInfo = infos.get(j);
				if (prvedDealInfo.getPayType()==null) {
					continue;
				}
				if (!prvedDealInfo.getPayType().equals(1)) {
					continue;
				}
				PayableDetailVo detailVo = new PayableDetailVo();
				if (infos.get(j).getDealInfoType()!=null) {
					if (infos.get(j).getDealInfoType().equals(1)||infos.get(j).getDealInfoType().equals(0)) {
						if (infos.get(j).getBusinessCardUserDate().getTime()>endLastDate.getTime()) {
							break;
						}else if (infos.get(j).getNotafter().getTime()<endLastDate.getTime()) {
							yjNum += infos.get(j).getYear();
							detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
							detailVo.setEndDate(infos.get(j).getNotafter());
							detailVo.setDealInfoType(WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType()));
							detailVo.setSettleYear(infos.get(j).getYear().toString());
							detailList.add(detailVo);
						}else if (infos.get(j).getBusinessCardUserDate().getTime()<endLastDate.getTime()&&infos.get(j).getNotafter().getTime()>endLastDate.getTime()) {
							
							
//							infos.get(j).getBusinessCardUserDate().setHours(23);
//							infos.get(j).getBusinessCardUserDate().setMinutes(59);
//							infos.get(j).getBusinessCardUserDate().setSeconds(59);
							long between = endLastDate.getTime()-infos.get(j).getBusinessCardUserDate().getTime();
							
							
							long a  = between/31536000000L; 
							
							
							
							
							int yy = (int) Math.ceil(a);
							
							
							
							yjNum += yy;
							detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
							detailVo.setEndDate(infos.get(j).getNotafter());
							detailVo.setDealInfoType(WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType()));
							detailVo.setSettleYear(yy+"");
							detailList.add(detailVo);
						}
						
						
					}
				}
				
			}
			dealInfos.get(i).setYyNum(yjNum);
			dealInfos.get(i).setTotalNum(totalAgentYear);
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
	
	
	 public static void main(String[] args)
	    {
	        Calendar calendar = Calendar.getInstance();
	        Date date = new Date(System.currentTimeMillis());
	        calendar.setTime(date);
//	        calendar.add(Calendar.WEEK_OF_YEAR, -1);
	        calendar.add(Calendar.YEAR, 5);
	        date = calendar.getTime();
	        System.out.println(date);
	    }
	

}
