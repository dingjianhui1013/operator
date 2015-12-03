/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.web;

import java.util.Date;
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
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkUserService;
import com.itrus.util.CipherUtils;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.message.entity.SmsConfiguration;
import com.itrus.ca.modules.message.service.SmsConfigurationService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.service.ConfigAppService;

/**
 * 短信配置Controller
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
	
	private LogUtil logUtil = new LogUtil();
	@ModelAttribute
	public SmsConfiguration get(@RequestParam(required=false) Long id) {
		if (id != null){
			return smsConfigurationService.get(id);
		}else{
			return new SmsConfiguration();
		}
	}
	
	@RequiresPermissions("message:smsConfiguration:view")
	@RequestMapping(value = {"list", ""})
	public String list(SmsConfiguration smsConfiguration, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		Page<SmsConfiguration> page = smsConfigurationService.find(
				new Page<SmsConfiguration>(request, response), smsConfiguration);
		model.addAttribute("page", page);
		return "modules/message/smsConfigurationList";
	}

	@RequiresPermissions("message:smsConfiguration:view")
	@RequestMapping(value = "form")
	public String form(SmsConfiguration smsConfiguration,
			Model model) {
		model.addAttribute("smsConfiguration", smsConfiguration);
		
		return "modules/message/smsConfigurationForm";
	}

	@RequiresPermissions("message:smsConfiguration:edit")
	@RequestMapping(value = "save")
	public String save(SmsConfiguration smsConfiguration,Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response)throws JSONException {
		if (!beanValidator(model, smsConfiguration)) {
			return form(smsConfiguration, model);
		}
		JSONObject json = new JSONObject();
		smsConfigurationService.save(smsConfiguration);
		try {
			
			List<SmsConfiguration> raAccounts = smsConfigurationService.findAll();
			if(raAccounts.size()!=0){
//				if(raAccounts.get(0).getConfigRaAccountExtendInfo()!=null){
//					configRaAccount.setConfigRaAccountExtendInfo(raAccounts.get(0).getConfigRaAccountExtendInfo());
//				}
			}
			smsConfigurationService.save(smsConfiguration);
//			ConfigProduct product = productService.get(configRaAccount.getConfigProduct().getId());
//
//			product.setRaAccountId(configRaAccount.getId());
//			productService.save(product);
			logUtil.saveSysLog("短信模板配置", "保存短信模板配置"+smsConfiguration.getMessageName()+"成功", null);
			addMessage(redirectAttributes, "保存短信模板配置成功");
			//json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.saveSysLog("短信模板配置", "保存短信模板配置"+smsConfiguration.getMessageName()+"失败，配置异常", null);
			addMessage(redirectAttributes, "保存短信模板配置失败，配置异常");
			//json.put("status", "0");
		}
		
		//addMessage(redirectAttributes, "保存短信配置'" + smsConfiguration.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/message/smsConfiguration/?repage";
	}
	
	@RequiresPermissions("message:smsConfiguration:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		smsConfigurationService.delete(id);
		logUtil.saveSysLog("短信模板配置", "删除id为"+id+"短信模板配置成功", null);
		addMessage(redirectAttributes, "删除短信配置成功");
		return "redirect:" + Global.getAdminPath()
				+ "/message/smsConfiguration";}
	
	@RequestMapping(value="checkMessage")
	@ResponseBody
	public String checkMessage(Long id ,HttpServletRequest request, HttpServletResponse response ){
		JSONObject jsonObject = new JSONObject();
		try{
			String messageName = request.getParameter("messageName");
			List<SmsConfiguration> list = smsConfigurationService.findByRaName(messageName);
			if(list.size() != 0 ){
				if(id == null ){
					jsonObject.put("status","0");
					jsonObject.put("msg","RA模板名称已存在,添加失败");
					return jsonObject.toString();
				}else{
					SmsConfiguration smsConfiguration = list.get(0);
					if(id.longValue() != smsConfiguration.getId().longValue()){//更新重名
						jsonObject.put("status","0");
						jsonObject.put("msg","RA模板名称已存在,更新失败");
						return jsonObject.toString();
					}else{
						jsonObject.put("status","2");
						return jsonObject.toString();

					}
//					if(configRaAccountService.checkUsed(raId)){
//						jsonObject.put("status","1");
//						jsonObject.put("msg","该RA模版已被使用，是否继续进行修改？");
//						return jsonObject.toString();
//					}
				}
			}else{
				jsonObject.put("status","2");
				return jsonObject.toString();
			}

		}catch(Exception e){

			e.printStackTrace();
		}

		return null;
	}

}
