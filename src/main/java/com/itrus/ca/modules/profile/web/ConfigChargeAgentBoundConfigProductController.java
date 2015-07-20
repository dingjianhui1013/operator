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
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;

/**
 * 计费策略模版详情历史Controller
 * @author HuHao
 * @version 2015-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configChargeAgentBoundConfigProduct")
public class ConfigChargeAgentBoundConfigProductController extends BaseController {

	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	
	@ModelAttribute
	public ConfigChargeAgentBoundConfigProduct get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configChargeAgentBoundConfigProductService.get(id);
		}else{
			return new ConfigChargeAgentBoundConfigProduct();
		}
	}
	
	@RequiresPermissions("profile:configChargeAgentBoundConfigProduct:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigChargeAgentBoundConfigProduct configChargeAgentBoundConfigProduct, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			configChargeAgentBoundConfigProduct.setCreateBy(user);
		}
        Page<ConfigChargeAgentBoundConfigProduct> page = configChargeAgentBoundConfigProductService.find(new Page<ConfigChargeAgentBoundConfigProduct>(request, response), configChargeAgentBoundConfigProduct); 
        model.addAttribute("page", page);
		return "modules/profile/configChargeAgentBoundConfigProductList";
	}

	@RequiresPermissions("profile:configChargeAgentBoundConfigProduct:view")
	@RequestMapping(value = "form")
	public String form(ConfigChargeAgentBoundConfigProduct configChargeAgentBoundConfigProduct, Model model) {
		model.addAttribute("configChargeAgentBoundConfigProduct", configChargeAgentBoundConfigProduct);
		return "modules/profile/configChargeAgentBoundConfigProductForm";
	}

	@RequiresPermissions("profile:configChargeAgentBoundConfigProduct:edit")
	@RequestMapping(value = "save")
	public String save(ConfigChargeAgentBoundConfigProduct configChargeAgentBoundConfigProduct, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configChargeAgentBoundConfigProduct)){
			return form(configChargeAgentBoundConfigProduct, model);
		}
		configChargeAgentBoundConfigProductService.save(configChargeAgentBoundConfigProduct);
	//	addMessage(redirectAttributes, "保存计费策略模版详情历史'" + configChargeAgentBoundConfigProduct.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configChargeAgentBoundConfigProduct/?repage";
	}
	
	@RequiresPermissions("profile:configChargeAgentBoundConfigProduct:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configChargeAgentBoundConfigProductService.delete(id);
		addMessage(redirectAttributes, "删除计费策略模版详情历史成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configChargeAgentBoundConfigProduct/?repage";
	}

}
