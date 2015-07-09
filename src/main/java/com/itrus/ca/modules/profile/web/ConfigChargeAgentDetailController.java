/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;

/**
 * 供应商计费type配置Controller
 * @author ZhangShaoBo
 * @version 2014-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configChargeAgentDetail")
public class ConfigChargeAgentDetailController extends BaseController {

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;
	
	@Autowired
	private WorkDealInfoService dealInfoService;
	
	private LogUtil logUtil = new LogUtil();
	
	
	@ModelAttribute
	public ConfigChargeAgentDetail get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configChargeAgentDetailService.get(id);
		}else{
			return new ConfigChargeAgentDetail();
		}
	}
	
	@RequiresPermissions("profile:configChargeAgentDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigChargeAgentDetail configChargeAgentDetail, HttpServletRequest request, HttpServletResponse response, Model model) {

        Page<ConfigChargeAgentDetail> page = configChargeAgentDetailService.find(new Page<ConfigChargeAgentDetail>(request, response), configChargeAgentDetail); 
        model.addAttribute("page", page);
		return "modules/profile/configChargeAgentDetailList";
	}

	@RequiresPermissions("profile:configChargeAgentDetail:view")
	@RequestMapping(value = "form")
	public String form(ConfigChargeAgentDetail configChargeAgentDetail, Model model) {
		model.addAttribute("configChargeAgentDetail", configChargeAgentDetail);
		return "modules/profile/configChargeAgentDetailForm";
	}

	@RequiresPermissions("profile:configChargeAgentDetail:edit")
	@RequestMapping(value = "save")
	public String save(ConfigChargeAgentDetail configChargeAgentDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configChargeAgentDetail)){
			return form(configChargeAgentDetail, model);
		}
		configChargeAgentDetailService.save(configChargeAgentDetail);
//		logUtil.saveSysLog("业务配置", "保存供应商计费type配置成功", null);
		addMessage(redirectAttributes, "保存供应商计费type配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configChargeAgentDetail/?repage";
	}
	
	@RequiresPermissions("profile:configChargeAgentDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configChargeAgentDetailService.delete(id);
		addMessage(redirectAttributes, "删除供应商计费type配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configChargeAgentDetail/?repage";
	}

	
}
