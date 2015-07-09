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
import com.itrus.ca.modules.profile.entity.ConfigMsg;
import com.itrus.ca.modules.profile.service.ConfigMsgService;

/**
 * 消息Controller
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configMsg")
public class ConfigMsgController extends BaseController {

	@Autowired
	private ConfigMsgService configMsgService;
	
	private LogUtil logUtil = new LogUtil();
	
	@ModelAttribute
	public ConfigMsg get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configMsgService.get(id);
		}else{
			return new ConfigMsg();
		}
	}
	
	@RequiresPermissions("profile:configMsg:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigMsg configMsg, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			configMsg.setCreateBy(user);
		}
        Page<ConfigMsg> page = configMsgService.find(new Page<ConfigMsg>(request, response), configMsg); 
        model.addAttribute("page", page);
		return "modules/profile/configMsgList";
	}

	@RequiresPermissions("profile:configMsg:view")
	@RequestMapping(value = "form")
	public String form(ConfigMsg configMsg, Model model) {
		model.addAttribute("configMsg", configMsg);
		return "modules/profile/configMsgForm";
	}

	@RequiresPermissions("profile:configMsg:edit")
	@RequestMapping(value = "save")
	public String save(ConfigMsg configMsg, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configMsg)){
			return form(configMsg, model);
		}
		
		String detail = "";
		if(configMsg.getId()==null){
			detail = "添加消息"+configMsg.getMsgName()+"成功";
		}else{
			detail = "更新消息"+configMsg.getMsgName()+"成功";
		}
		configMsgService.save(configMsg);
		logUtil.saveSysLog("业务配置", "保存消息'" + configMsg.getMsgTitle() + "'成功", null);
		addMessage(redirectAttributes, "保存消息'" + configMsg.getMsgTitle() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configMsg";
	}
	
	@RequiresPermissions("profile:configMsg:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		ConfigMsg configMsg = get(id);
		if (configMsg.getDelFlag().equals("0")) {
			configMsg.setDelFlag("1");
		} else {
			configMsg.setDelFlag("0");
		}
		
		configMsgService.save(configMsg);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"的消息成功", null);
		addMessage(redirectAttributes, "设置消息状态成功");
		if (configMsg.getDelFlag().equals("1")) {
			logUtil.saveSysLog("业务配置", "启用消息"+configMsg.getMsgTitle(), "");
		} else {
			logUtil.saveSysLog("业务配置", "停用消息"+configMsg.getMsgTitle(), "");
		}
		
		return "redirect:"+Global.getAdminPath()+"/profile/configMsg";
	}

}
