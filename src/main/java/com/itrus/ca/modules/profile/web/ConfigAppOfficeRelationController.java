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
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;

/**
 * 供应商产品配置Controller
 * @author HUHAO
 * @version 2014-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configAppOfficeRelation")
public class ConfigAppOfficeRelationController extends BaseController {

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	private LogUtil logUtil = new LogUtil();
	
	@ModelAttribute
	public ConfigAppOfficeRelation get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configAppOfficeRelationService.get(id);
		}else{
			return new ConfigAppOfficeRelation();
		}
	}
	
	@RequiresPermissions("profile:configAppOfficeRelation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigAppOfficeRelation configAppOfficeRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
		//	configAppOfficeRelation.setCreateBy(user);
		}
      //  Page<ConfigAppOfficeRelation> page = configAppOfficeRelationService.find(new Page<ConfigAppOfficeRelation>(request, response), configAppOfficeRelation); 
      //  model.addAttribute("page", page);
		return "modules/profile/configAppOfficeRelationList";
	}

	@RequiresPermissions("profile:configAppOfficeRelation:view")
	@RequestMapping(value = "form")
	public String form(ConfigAppOfficeRelation configAppOfficeRelation, Model model) {
		model.addAttribute("configAppOfficeRelation", configAppOfficeRelation);
		return "modules/profile/configAppOfficeRelationForm";
	}

	@RequiresPermissions("profile:configAppOfficeRelation:edit")
	@RequestMapping(value = "save")
	public String save(ConfigAppOfficeRelation configAppOfficeRelation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configAppOfficeRelation)){
			return form(configAppOfficeRelation, model);
		}
		String detail = "";
		if(configAppOfficeRelation.getId()==null){
			detail = "添加id为"+configAppOfficeRelation.getId()+"供应商产品配置成功";
		}else{
			detail = "更新id为"+configAppOfficeRelation.getId()+"供应商产品配置成功";
		}
		configAppOfficeRelationService.save(configAppOfficeRelation);
		logUtil.saveSysLog("业务配置", detail, null);
		//addMessage(redirectAttributes, "保存供应商产品配置'" + configAppOfficeRelation.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configAppOfficeRelation/?repage";
	}
	
	@RequiresPermissions("profile:configAppOfficeRelation:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configAppOfficeRelationService.delete(id);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"的供应商产品配置成功", null);
		addMessage(redirectAttributes, "删除供应商产品配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configAppOfficeRelation/?repage";
	}

}
