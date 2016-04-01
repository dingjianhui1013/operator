/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avro.data.Json;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProjectType;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProjectTypeService;

/**
 * 项目类型管理Controller
 * @author HuHao
 * @version 2015-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configProjectType")
public class ConfigProjectTypeController extends BaseController {

	@Autowired
	private ConfigProjectTypeService configProjectTypeService;
	
	
	@Autowired
	private ConfigAppService configAppService;
	
	@ModelAttribute
	public ConfigProjectType get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configProjectTypeService.get(id);
		}else{
			return new ConfigProjectType();
		}
	}
	
	@RequiresPermissions("profile:configProjectType:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigProjectType configProjectType, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<ConfigProjectType> page = configProjectTypeService.find(new Page<ConfigProjectType>(request, response), configProjectType); 
        model.addAttribute("page", page);
		return "modules/profile/configProjectTypeList";
	}

	@RequiresPermissions("profile:configProjectType:view")
	@RequestMapping(value = "form")
	public String form(ConfigProjectType configProjectType, Model model) {
		model.addAttribute("configProjectType", configProjectType);
		return "modules/profile/configProjectTypeForm";
	}

	@RequiresPermissions("profile:configProjectType:edit")
	@RequestMapping(value = "save")
	public String save(ConfigProjectType configProjectType, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configProjectType)){
			return form(configProjectType, model);
		}
		configProjectTypeService.save(configProjectType);
		addMessage(redirectAttributes, "保存项目类型管理'" + configProjectType.getProjectName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configProjectType/?repage";
	}
	
	@RequiresPermissions("profile:configProjectType:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		
		List<ConfigApp> apps = configAppService.findByconfigProjectType(id);
		
		if(apps.size()>0){
			addMessage(redirectAttributes,"项目类型管理已被应用绑定,无法删除!");
			return "redirect:"+Global.getAdminPath()+"/profile/configProjectType/?repage";
		}
		
		configProjectTypeService.delete(id);
		addMessage(redirectAttributes, "删除项目类型管理成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configProjectType/?repage";
	}
	
	
	@RequestMapping(value = "checkProName")
	@ResponseBody
	public String checkProName(String proName , Long proId) throws JSONException{
		JSONObject json = new JSONObject();
		try {
			List<ConfigProjectType> proTypeList = configProjectTypeService.checkName(proName , proId);
			if (proTypeList.size()>0) {
				json.put("isAdd",1);
			}else{
				json.put("isAdd",0);
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@RequestMapping(value = "showAppList")
	@ResponseBody
	public String showAppList(Long proId) throws Exception{
		List<ConfigApp> configApps =  configAppService.findByconfigProjectType(proId);
		JSONObject json = new org.json.JSONObject();
		JSONArray array = new JSONArray();
		if(configApps.size()>0)
		{
			for (ConfigApp configApp : configApps) {
				try {
					json = new JSONObject();
					json.put("id", configApp.getId());
					json.put("name", configApp.getAppName());
					array.put(json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					json.put("status", "0");
					array.put(json);
				}
			}
		}else {
			json.put("status", 0);
			array.put(json);
		}
		return array.toString();
	}
}
