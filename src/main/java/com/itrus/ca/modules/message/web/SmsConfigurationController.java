/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.message.entity.SmsConfiguration;
import com.itrus.ca.modules.message.service.SmsConfigurationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkUserService;
import org.apache.commons.lang3.StringUtils;

/**
 * 短信配置Controller
 * 
 * @author qt
 * @version 2015-11-27
 */
@Controller
@RequestMapping(value = "${adminPath}/message/smsConfiguration")
public class SmsConfigurationController extends BaseController {

	@Autowired
	private SmsConfigurationService smsConfigurationService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	private String realPath;
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public SmsConfiguration get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return smsConfigurationService.get(id);
		} else {
			return new SmsConfiguration();
		}
	}

	@RequiresPermissions("message:smsConfiguration:view")
	@RequestMapping(value = { "list", "" })
	public String list(SmsConfiguration smsConfiguration, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		Page<SmsConfiguration> page = smsConfigurationService.find(new Page<SmsConfiguration>(request, response),
				smsConfiguration);
		model.addAttribute("page", page);
		return "modules/message/smsConfigurationList";
	}

	@RequiresPermissions("message:smsConfiguration:view")
	@RequestMapping(value = "form")
	public String form(SmsConfiguration smsConfiguration, Model model) {
		model.addAttribute("smsConfiguration", smsConfiguration);

		return "modules/message/smsConfigurationForm";
	}
	@RequiresPermissions("message:smsConfiguration:view")
	@RequestMapping(value = "form1")
	public String form1(SmsConfiguration smsConfiguration, Model model) {
		model.addAttribute("smsConfiguration", smsConfiguration);

		return "modules/message/smsConfigurationFormC";
	}
	@RequiresPermissions("message:smsConfiguration:edit")
	@RequestMapping(value = "save")
	public String save(SmsConfiguration smsConfiguration, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) throws JSONException {
		if (!beanValidator(model, smsConfiguration)) {
			return form(smsConfiguration, model);
		}
		JSONObject json = new JSONObject();
		smsConfigurationService.save(smsConfiguration);
		try {

			List<SmsConfiguration> raAccounts = smsConfigurationService.findAll();
			if (raAccounts.size() != 0) {
				// if(raAccounts.get(0).getConfigRaAccountExtendInfo()!=null){
				// configRaAccount.setConfigRaAccountExtendInfo(raAccounts.get(0).getConfigRaAccountExtendInfo());
				// }
			}
			smsConfigurationService.save(smsConfiguration);
			// ConfigProduct product =
			// productService.get(configRaAccount.getConfigProduct().getId());
			//
			// product.setRaAccountId(configRaAccount.getId());
			// productService.save(product);
			logUtil.saveSysLog("短信模板配置", "保存短信模板配置" + smsConfiguration.getMessageName() + "成功", null);
			addMessage(redirectAttributes, "保存短信模板配置成功");
			// json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.saveSysLog("短信模板配置", "保存短信模板配置" + smsConfiguration.getMessageName() + "失败，配置异常", null);
			addMessage(redirectAttributes, "保存短信模板配置失败，配置异常");
			// json.put("status", "0");
		}

		// addMessage(redirectAttributes, "保存短信配置'" + smsConfiguration.getName()
		// + "'成功");
		return "redirect:" + Global.getAdminPath() + "/message/smsConfiguration/?repage";
	}

	@RequiresPermissions("message:smsConfiguration:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id,String messageName, RedirectAttributes redirectAttributes) {
		smsConfigurationService.delete(id);
		logUtil.saveSysLog("短信模板配置", "删除id为" + id + "短信模板配置成功", null);
		addMessage(redirectAttributes, "删除模板名称为'"+messageName+"'模版成功");
		return "redirect:" + Global.getAdminPath() + "/message/smsConfiguration";
	}			

	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model)
			throws IllegalStateException, IOException, JSONException {
		String newFileName = null;
		if (file != null) {

			String path = SmsConfigurationController.class.getResource("/").toString().replace("file:", "")
					.replace("%20", " ");
			if (StringUtils.contains(path, "/WEB-INF")) {
				path = path.substring(0, StringUtils.indexOf(path, "/WEB-INF"));
			}
			;
			path += "/WEB-INF/template/";// 文件保存目录，也可自定为绝对路径
			String fileName = file.getOriginalFilename();// getOriginalFilename和getName是不一样的哦
			// String extensionName = fileName
			// .substring(fileName.lastIndexOf(".") + 1); 获取文件本质
			//newFileName = String.valueOf(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
			newFileName=fileName.substring(0,fileName.lastIndexOf("."));
			System.out.println(newFileName);
			File targetFile = new File(path,newFileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SmsConfiguration smsConfiguration = new SmsConfiguration();
		smsConfiguration.setMessageName(newFileName);

		smsConfigurationService.save(smsConfiguration);
		return "redirect:" + Global.getAdminPath() + "/message/smsConfiguration/?repage";
	}
	@RequestMapping("test")
	@ResponseBody
	public String importFile(@RequestParam(value = "fileName", required = true) MultipartFile file)
			throws IllegalStateException, IOException, JSONException {
		String newFileName=null;
		JSONObject json = new JSONObject();
		String path=null;
		String fileName=null;
		try {
		
		 path = SmsConfigurationController.class.getResource("/").toString().replace("file:", "")
				.replace("%20", " ");
		if (StringUtils.contains(path, "/WEB-INF")) {
			path = path.substring(0, StringUtils.indexOf(path, "/WEB-INF"));
		}
		;
		path += "/WEB-INF/template/";// 文件保存目录，也可自定为绝对路径
		 fileName = file.getOriginalFilename();// getOriginalFilename和getName是不一样的哦
		// String extensionName = fileName
		// .substring(fileName.lastIndexOf(".") + 1); 获取文件本质
		//newFileName = String.valueOf(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		 newFileName=fileName.substring(0,fileName.lastIndexOf("."));
		List<SmsConfiguration> list=smsConfigurationService.findAll();
		for(int i=0;i<list.size();i++){
		if(newFileName.equals(list.get(i).getMessageName())){
			json.put("status", -1);
			json.put("msg", "模板名称已存在，请改正");
			return json.toString();
		}}
		File targetFile = new File(path,newFileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		
			file.transferTo(targetFile);
			json.put("status", 1);
			json.put("msg", "上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			
		}
//		Read read=new Read();
//		String messageContent=read.readFile(path);
//		System.out.println(messageContent);
		Properties p = new Properties();
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		System.out.println(p);
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init(p);
		Map<String, Object>  orange = new HashMap<>();
		String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, newFileName, "UTF-8", orange);
		System.out.println(content);
		SmsConfiguration smsConfiguration = new SmsConfiguration();
		smsConfiguration.setMessageName(newFileName);
		smsConfiguration.setMessageContent(content);
		smsConfigurationService.save(smsConfiguration);
		return json.toString();
		
	}
}
