/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;

/**
 * 计费策略模版关联业务表Controller
 * @author HuHao
 * @version 2015-07-27
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configAgentBoundDealInfo")
public class ConfigAgentBoundDealInfoController extends BaseController {

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;
	
	@ModelAttribute
	public ConfigAgentBoundDealInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configAgentBoundDealInfoService.get(id);
		}else{
			return new ConfigAgentBoundDealInfo();
		}
	}
	
	@RequiresPermissions("profile:configAgentBoundDealInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigAgentBoundDealInfo configAgentBoundDealInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			configAgentBoundDealInfo.setCreateBy(user);
		}
        Page<ConfigAgentBoundDealInfo> page = configAgentBoundDealInfoService.find(new Page<ConfigAgentBoundDealInfo>(request, response), configAgentBoundDealInfo); 
        model.addAttribute("page", page);
		return "modules/profile/configAgentBoundDealInfoList";
	}

	@RequiresPermissions("profile:configAgentBoundDealInfo:view")
	@RequestMapping(value = "form")
	public String form(ConfigAgentBoundDealInfo configAgentBoundDealInfo, Model model) {
		model.addAttribute("configAgentBoundDealInfo", configAgentBoundDealInfo);
		return "modules/profile/configAgentBoundDealInfoForm";
	}

	@RequiresPermissions("profile:configAgentBoundDealInfo:edit")
	@RequestMapping(value = "save")
	public String save(ConfigAgentBoundDealInfo configAgentBoundDealInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configAgentBoundDealInfo)){
			return form(configAgentBoundDealInfo, model);
		}
		configAgentBoundDealInfoService.save(configAgentBoundDealInfo);
	//	addMessage(redirectAttributes, "保存计费策略模版关联业务表'" + configAgentBoundDealInfo.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configAgentBoundDealInfo/?repage";
	}
	
	@RequiresPermissions("profile:configAgentBoundDealInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configAgentBoundDealInfoService.delete(id);
		addMessage(redirectAttributes, "删除计费策略模版关联业务表成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configAgentBoundDealInfo/?repage";
	}

}
