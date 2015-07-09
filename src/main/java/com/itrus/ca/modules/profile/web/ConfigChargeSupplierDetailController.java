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
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;
import com.itrus.ca.modules.profile.service.ConfigChargeSupplierDetailService;

/**
 * 供应商计费配置Controller
 * @author ZhangShaoBo
 * @version 2014-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configChargeSupplierDetail")
public class ConfigChargeSupplierDetailController extends BaseController {

	@Autowired
	private ConfigChargeSupplierDetailService configChargeSupplierDetailService;
	
	private LogUtil logUtil = new LogUtil();
	
	@ModelAttribute
	public ConfigChargeSupplierDetail get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configChargeSupplierDetailService.get(id);
		}else{
			return new ConfigChargeSupplierDetail();
		}
	}
	
	@RequiresPermissions("profile:configChargeSupplierDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigChargeSupplierDetail configChargeSupplierDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ConfigChargeSupplierDetail> page = configChargeSupplierDetailService.find(new Page<ConfigChargeSupplierDetail>(request, response), configChargeSupplierDetail); 
        model.addAttribute("page", page);
		return "modules/profile/configChargeSupplierDetailList";
	}

	@RequiresPermissions("profile:configChargeSupplierDetail:view")
	@RequestMapping(value = "form")
	public String form(ConfigChargeSupplierDetail configChargeSupplierDetail, Model model) {
		model.addAttribute("configChargeSupplierDetail", configChargeSupplierDetail);
		return "modules/profile/configChargeSupplierDetailForm";
	}

	@RequiresPermissions("profile:configChargeSupplierDetail:edit")
	@RequestMapping(value = "save")
	public String save(ConfigChargeSupplierDetail configChargeSupplierDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configChargeSupplierDetail)){
			return form(configChargeSupplierDetail, model);
		}
		configChargeSupplierDetailService.save(configChargeSupplierDetail);
		logUtil.saveSysLog("业务配置", "保存供应商计费配置成功", null);
		addMessage(redirectAttributes, "保存供应商计费配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configChargeSupplierDetail/?repage";
	}
	
	@RequiresPermissions("profile:configChargeSupplierDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		configChargeSupplierDetailService.delete(id);
		addMessage(redirectAttributes, "删除供应商计费配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configChargeSupplierDetail/?repage";
	}

}
