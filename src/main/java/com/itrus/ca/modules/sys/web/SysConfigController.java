/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.sys.entity.SysConfig;
import com.itrus.ca.modules.sys.service.SysConfigService;
import com.itrus.ca.modules.sys.utils.ConfigConstant;
import com.itrus.ca.modules.sys.utils.JsonUtils;
import com.itrus.ca.modules.sys.utils.MailSendUtil;
import com.itrus.ca.modules.sys.utils.MailSenderInfo;

/**
 * 邮件配置Controller
 * @author ZhangShaoBo
 * @version 2014-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysConfig")
public class SysConfigController extends BaseController {

	@Autowired
	private SysConfigService sysConfigService;
	
	private LogUtil logUtil = new LogUtil();
	
	@ModelAttribute
	public SysConfig get(@RequestParam(required=false) Long id) {
		if (id != null){
			return sysConfigService.get(id);
		}else{
			return new SysConfig();
		}
	}
	
	@RequiresPermissions("sys:sysConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysConfig sysConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			sysConfig.setCreateBy(user);
		}
        Page<SysConfig> page = sysConfigService.find(new Page<SysConfig>(request, response), sysConfig); 
        model.addAttribute("page", page);
		return "modules/sys/sysConfigList";
	}

	@RequiresPermissions("sys:sysConfig:view")
	@RequestMapping(value = "form")
	public String form(SysConfig sysConfig, Model model) {
		List<String> types = new ArrayList<String>();
		types.add(ConfigConstant.EMAIL_URL);
		types.add(ConfigConstant.PORT);
		types.add(ConfigConstant.IS_NEED_SSL);
		types.add(ConfigConstant.EMAIL_USER_NAME);
		types.add(ConfigConstant.EMAIL_USER_PASS);
		List<SysConfig> list = sysConfigService.findByType(types);
		if (list != null && list.size() != 0) {
			for (SysConfig sys : list) {
				if (sys.getType().equals(ConfigConstant.EMAIL_URL)) {
					model.addAttribute(ConfigConstant.EMAIL_URL,
							sys.getConfig());
					model.addAttribute("urlId", sys.getId());
					continue;
				}
				if (sys.getType().equals(ConfigConstant.PORT)) {
					model.addAttribute(ConfigConstant.PORT,
							sys.getConfig());
					model.addAttribute("portId", sys.getId());
					continue;
				}
				if (sys.getType().equals(ConfigConstant.IS_NEED_SSL)) {
					model.addAttribute(ConfigConstant.IS_NEED_SSL,
							sys.getConfig());
					model.addAttribute("sslId", sys.getId());
					continue;
				}
				if (sys.getType().equals(ConfigConstant.EMAIL_USER_NAME)) {
					model.addAttribute(ConfigConstant.EMAIL_USER_NAME,
							sys.getConfig());
					model.addAttribute("nameId", sys.getId());
					continue;
				}
				if (sys.getType().equals(ConfigConstant.EMAIL_USER_PASS)) {
					model.addAttribute(ConfigConstant.EMAIL_USER_PASS,
							sys.getConfig());
					model.addAttribute("passId", sys.getId());
					continue;
				}
			}
		} else {
			model.addAttribute(ConfigConstant.SMS_URL, "");
			model.addAttribute(ConfigConstant.PORT, "");
			model.addAttribute(ConfigConstant.IS_NEED_SSL, "");
			model.addAttribute(ConfigConstant.SMS_USER_NAME, "");
			model.addAttribute(ConfigConstant.SMS_USER_PASS, "");
		}
		return "modules/sys/sysConfigForm";
	}

	@RequiresPermissions("sys:sysConfig:edit")
	@RequestMapping(value = "save")
	public String save(String url, String port, String isSSL, String name,
			String pass, String userEmail, String toEmail,
			Long urlId, Long portId, Long sslId, Long nameId,
			Long passId, Long adminId, Long receiverId, 
			RedirectAttributes redirectAttributes) {
		//修改url
		SysConfig sysConfig = sysConfigService.get(urlId);
		if (!url.equals(sysConfig.getConfig())) {
			sysConfig.setConfig(url);
			sysConfig.setUpdateDate(new Date());
			sysConfigService.save(sysConfig);
			logUtil.saveSysLog("系统管理", "修改邮件服务器地址成功", null);

		}
		
		//修改端口
		SysConfig sysConfig1 = sysConfigService.get(portId);
		if (!port.equals(sysConfig1.getConfig())) {
			sysConfig1.setConfig(port);
			sysConfig1.setUpdateDate(new Date());
			sysConfigService.save(sysConfig1);
			logUtil.saveSysLog("系统管理", "修改邮件服务器端口成功", null);
		}
		
		//修改SSL
		SysConfig sysConfig2 = sysConfigService.get(sslId);
		if (!isSSL.equals(sysConfig2.getConfig())) {
			sysConfig2.setConfig(isSSL);
			sysConfig2.setUpdateDate(new Date());
			sysConfigService.save(sysConfig2);
			logUtil.saveSysLog("系统管理", "修改邮件服务器SSL成功", null);
		}
		
		//修改用户名
		SysConfig sysConfig3 = sysConfigService.get(nameId);
		if (!name.equals(sysConfig3.getConfig())) {
			sysConfig3.setConfig(name);
			sysConfig3.setUpdateDate(new Date());
			sysConfigService.save(sysConfig3);
			logUtil.saveSysLog("系统管理", "修改邮件服务器用户名成功", null);
		}
		
		//修改密码
		SysConfig sysConfig4 = sysConfigService.get(passId);
		if (!pass.equals(sysConfig4.getConfig())) {
			sysConfig4.setConfig(pass);
			sysConfig4.setUpdateDate(new Date());
			sysConfigService.save(sysConfig4);
			logUtil.saveSysLog("系统管理", "修改邮件服务器密码成功", null);
		}
		
		//修改管理员邮箱
		SysConfig sysConfig5 = new SysConfig();
		if (adminId!=null) {
			sysConfig5 = sysConfigService.get(adminId);
		}
		// 修改管理员邮箱
		SysConfig sysConfig6 = new SysConfig();
		if (receiverId!=null) {
			sysConfig6 = sysConfigService.get(receiverId);
		}
		addMessage(redirectAttributes, "更新邮件配置成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysConfig/form";
	}
	
	@RequiresPermissions("sys:sysConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		sysConfigService.delete(id);
		logUtil.saveSysLog("系统管理", "删除id为"+id+"的邮件配置成功", null);
		addMessage(redirectAttributes, "删除邮件配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/sys/sysConfig/form";
	}
	// 测试接口
	@RequestMapping(value = "testSendMail")
	public void testSendMail(HttpServletRequest request,
				HttpServletResponse response, String toEmail, String smtp, String user,
				String pass, String isSSL, String port) throws Exception {
				JSONObject object = new JSONObject();

			try {
				MailSendUtil mSendUtil = new MailSendUtil();
				boolean ssl;
				if (isSSL.equals("true")) {
					ssl = true;
				} else {
					ssl = false;
				}
				boolean test = mSendUtil.testMailHostIsValid(toEmail,ssl, smtp, user, pass,
						port);
				if (test == true) {
					object.put("status", 1);
					object.put("test", test);
				} else {
					object.put("status", 0);
					object.put("test", test);
				}

			} catch (Exception e) {
				e.printStackTrace();
				object.put("status", -1);
				object.put("test", "false");
			}

			JsonUtils.writeJson(response, object.toString());

		}
		
	/**
	 * 
	 * @param sysConfig
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:sysConfig:view")
	@RequestMapping(value = "smsForm")
	public String smsForm(Model model) {
		Map<String,SysConfig> map = sysConfigService.selectConfigByType();
		model.addAttribute("sms_url",map.get("sms_url").getConfig());
		model.addAttribute("sms_name",map.get("sms_user_name").getConfig());
		model.addAttribute("sms_password",map.get("sms_user_pass").getConfig());
		return "modules/sys/sysConfigSmsForm";
	}
	
	@RequiresPermissions("sys:sysConfig:view")
	@RequestMapping("smsSave")
	public String smsSave(String smsUrl,String smsName,String smsPassword, Model model,RedirectAttributes redirectAttributes){
		Map<String,SysConfig> map = sysConfigService.selectConfigByType();
		SysConfig config = null;
		if(smsUrl!=null&&!smsUrl.equals("")){
			config = map.get("sms_url");
			config.setConfig(smsUrl);
			sysConfigService.save(config);
			logUtil.saveSysLog("系统管理", "修改短信服务配置地址成功", null);
		}
		if(smsName!=null&&!smsName.equals("")){
			config = map.get("sms_user_name");
			config.setConfig(smsName);
			sysConfigService.save(config);
			logUtil.saveSysLog("系统管理", "修改短信服务配置用户名成功", null);
		}
		if(smsPassword!=null&&!smsPassword.equals("")){
			config = map.get("sms_user_pass");
			config.setConfig(smsPassword);
			sysConfigService.save(config);
			logUtil.saveSysLog("系统管理", "修改短信服务配置密码成功", null);
		}
		addMessage(redirectAttributes, "修改短信配置成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysConfig/smsForm";
	}
}
