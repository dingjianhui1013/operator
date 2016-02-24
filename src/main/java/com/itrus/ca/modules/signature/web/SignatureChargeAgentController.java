/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigApp;

import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;
import com.itrus.ca.modules.signature.service.SignatureConfigChargeAgentService;


/**
 * 签章计费策略Controller
 * @author CYC
 * @version 2016-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/signature/signatureChargeAgent")
public class SignatureChargeAgentController extends BaseController {

	@Autowired
	private SignatureConfigChargeAgentService signatureConfigChargeAgentService;
	
	@Autowired
	private ConfigAppService configAppService;
	
	@Autowired
	private ConfigChargeAgentBoundConfigAppService configChargeAgentBoundConfigAppService; 
	



	
	@RequiresPermissions("signature:signatureChargeAgent:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigApp configApp, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ConfigApp> page = configAppService.find(new Page<ConfigApp>(request, response), configApp, new ArrayList<Long>(), null);
        model.addAttribute("page", page);
    
		return "modules/profile/signatureChargeAgentList";
	}
	
	
	
	
	/**
	 * 显示绑定模版的数据
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("signature:signatureChargeAgent:view")
	@RequestMapping(value = "bindListNew")
	public String bindListNew(SignatureConfigChargeAgent signatureConfigChargeAgent,Long appId,
						   HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SignatureConfigChargeAgent> page = signatureConfigChargeAgentService.find(new Page<SignatureConfigChargeAgent>(request, response), signatureConfigChargeAgent);
		for (int i = 0; i < page.getList().size(); i++) {
			ConfigChargeAgentBoundConfigApp bound =	configChargeAgentBoundConfigAppService.findByAgentIdAppId(page.getList().get(i).getId(),appId);
			if (bound==null) {
				page.getList().get(i).setIsBind("1");
			}else{
				page.getList().get(i).setIsBind("2");
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("appId", appId);
		return "modules/signature/signatureConfigChargeAgentTemplateBindNewList";
	}
	
	
	
	
	/**
	 * 点击“签章计费策略模板” 跳转到签章计费策略模板 界面
	 * @param signatureConfigChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getSignatureChargeAgentList")
	public String signatureChargeAgentList(SignatureConfigChargeAgent signatureConfigChargeAgent,HttpServletRequest request ,
										 HttpServletResponse response, Model model){
		Page<SignatureConfigChargeAgent> page = signatureConfigChargeAgentService.find(new Page<SignatureConfigChargeAgent>(request,response),signatureConfigChargeAgent);
		/*List<SignatureConfigChargeAgent> agents =  signatureConfigChargeAgentService.findByNullUpdateNum();
		model.addAttribute("isHaveOld", agents.size());*/
		model.addAttribute("page",page);
		return "modules/signature/signatureConfigChargeAgentTemplateList";
	}

	
	

}
