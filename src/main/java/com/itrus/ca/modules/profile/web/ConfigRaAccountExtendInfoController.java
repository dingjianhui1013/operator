/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;


/**
 * RA配置详情Controller
 * @author ZhangJingtao
 * @version 2014-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configRaAccountExtendInfo")
public class ConfigRaAccountExtendInfoController extends BaseController {

	@Autowired
	private ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;
	private LogUtil logUtil = new LogUtil();
	@Autowired
	private ConfigRaAccountService configRaAccountService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@ModelAttribute
	public ConfigRaAccountExtendInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configRaAccountExtendInfoService.get(id);
		}else{
			return new ConfigRaAccountExtendInfo();
		}
	}
	
	@RequiresPermissions("profile:configRaAccountExtendInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigRaAccountExtendInfo configRaAccountExtendInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<ConfigRaAccountExtendInfo> page = configRaAccountExtendInfoService.find(new Page<ConfigRaAccountExtendInfo>(request, response), configRaAccountExtendInfo); 
        model.addAttribute("page", page);
		return "modules/profile/configRaAccountExtendInfoList";
	}
	@RequiresPermissions("profile:configRaAccountExtendInfo:view")
	@RequestMapping(value = "bindList")
	public String bindList(ConfigRaAccountExtendInfo configRaAccountExtendInfo, HttpServletRequest request, HttpServletResponse response, Model model,Long configProductId) {
		User user = UserUtils.getUser();
        Page<ConfigRaAccountExtendInfo> page = configRaAccountExtendInfoService.find(new Page<ConfigRaAccountExtendInfo>(request, response), configRaAccountExtendInfo); 
        model.addAttribute("page", page);
        model.addAttribute("configProductId", configProductId);
		return "modules/profile/configRaAccountExtendInfoBindList";
	}
	

	@RequiresPermissions("profile:configRaAccountExtendInfo:view")
	@RequestMapping(value = "form")
	public String form(ConfigRaAccountExtendInfo configRaAccountExtendInfo, Model model) {
		/*model.addAttribute("raAccountId", raAccountId);
		List<ConfigRaAccount> list = configRaAccountService.findAll();
		if(list.size()!=0){
			if(list.get(0).getConfigRaAccountExtendInfo()!=null){
				model.addAttribute("configRaAccountExtendInfo", list.get(0).getConfigRaAccountExtendInfo());
			}else{
				model.addAttribute("configRaAccountExtendInfo", configRaAccountExtendInfo);
			}
		}else {*/
		model.addAttribute("configRaAccountExtendInfo", configRaAccountExtendInfo);
		
		return "modules/profile/configRaAccountExtendInfoForm";
	}
	@RequiresPermissions("profile:configRaAccountExtendInfo:edit")
	@RequestMapping(value = "save")
	public String save(ConfigRaAccountExtendInfo configRaAccountExtendInfo, Model model, RedirectAttributes redirectAttributes) throws JSONException {
		if (!beanValidator(model, configRaAccountExtendInfo)){
			return form(configRaAccountExtendInfo, model);
		}
	
		try {
//			String detail = "";
//			if(configRaAccountExtendInfo.getId()==null){
//				detail = "添加RA配置详情成功成功";
//			}else{
//				detail = "更新RA配置详情成功成功";
//			}
			configRaAccountExtendInfoService.save(configRaAccountExtendInfo);
//			logUtil.saveSysLog("业务配置", detail, null);
//			addMessage(redirectAttributes, "保存RA配置详情成功");
//			ConfigRaAccount configRaAccount =  configRaAccountService.get(raAccountId);
//			configRaAccount.setConfigRaAccountExtendInfo(configRaAccountExtendInfo);
//			configRaAccountService.save(configRaAccount);
		
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "保存证书模版配置失败");
		}
		addMessage(redirectAttributes, "保存证书模版配置成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configRaAccountExtendInfo/";
	}
	
	@RequiresPermissions("profile:configRaAccountExtendInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id,String certName, RedirectAttributes redirectAttributes) {
		configRaAccountExtendInfoService.delete(id);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"的证书模版成功", null);
		addMessage(redirectAttributes, "删除证书名称为'"+certName+"'模版成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configRaAccountExtendInfo/";
	}
	
	@RequestMapping(value="checkRepeat")
	@ResponseBody
	public String checkRepeat(String certName,Long extendId) throws JSONException{
		JSONObject json = new JSONObject();
		try {
			ConfigRaAccountExtendInfo extendInfo =  configRaAccountExtendInfoService.findByCertName(certName,extendId);
			if (extendInfo!=null) {
				json.put("type", 1);
			}else {
				json.put("type", 0);
			}
			json.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 1);
		}
		
		return json.toString();
	};
	
	
	@RequestMapping(value="used")
	@ResponseBody
	public String used(Long extendId) throws JSONException{
		JSONObject json = new JSONObject();
		try {
			ConfigRaAccountExtendInfo extendInfo = configRaAccountExtendInfoService.get(extendId);
			ConfigProduct configProduct = configProductService.findByExtedId(extendId);
			if (configProduct!=null) {
				json.put("type", 1);
			}else {
				json.put("type", 0);
			}
			json.put("certName", extendInfo.getCertName());
			json.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 1);
		}
		
		return json.toString();
	};
	
	

}
