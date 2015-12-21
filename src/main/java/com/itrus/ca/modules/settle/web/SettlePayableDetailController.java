package com.itrus.ca.modules.settle.web;

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

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
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
	@RequestMapping(value = "list")
	public String settlePayableDetailList(
			HttpServletRequest request,
			HttpServletResponse response, 
			Model model, 
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgent", required = false) Long comAgent,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds
			){
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		
		//Page<WorkDealInfo> dealInfos = workDealInfoService.findDealInfo(new Page<WorkDealInfo>(request, response), Long appId, Long[] productIds);
		
		
		
		
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
