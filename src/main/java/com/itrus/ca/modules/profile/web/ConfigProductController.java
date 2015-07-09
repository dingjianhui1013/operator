/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 应用Controller
 * @author HuHao
 * @version 2014-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configProduct")
public class ConfigProductController extends BaseController {

	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private ConfigAppService configAppService;
	
	@Autowired
	private ConfigRaAccountService configRaAccountService;
	
	@Autowired
	ConfigRaAccountService raAccountService;
	
	@Autowired
	ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;
	
	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	ConfigRaAccountExtendInfoService extendInfoService;
	@ModelAttribute
	public ConfigProduct get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configProductService.get(id);
		}else{
			return new ConfigProduct();
		}
	}
	
	@RequiresPermissions("profile:configProduct:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigProduct configProduct, HttpServletRequest request, HttpServletResponse response, Model model,Long appId) {
		User user = UserUtils.getUser();
/*		ConfigApp app = new ConfigApp();
		app.setId(appId);*/
		ConfigApp configApp=configAppService.get(appId);
		configProduct.setConfigApp(configApp);
		if (configProduct.getProductName()!=null) {
			model.addAttribute("productName", configProduct.getProductName());
		}
		if (!user.isAdmin()){
			configProduct.setCreateBy(user);
		}
		Long test=configProduct.getRaAccountExtedId();
		List<ConfigProduct> page = configProductService.findByStart(configProduct); 
        /*for (int i = 0; i < page.size(); i++) {
        		List<ConfigRaAccount> configRaAccount = configRaAccountService.findByProductId(  page.get(i).getId());
        		//通过Product获取raAccount对象
        		if (configRaAccount.size()>0) {
        			page.get(i).setRaAccountId(configRaAccount.get(0).getId());
        			
        			if (configRaAccount.get(0).getConfigRaAccountExtendInfo()!=null) {
        				page.get(i).setRaAccountExtedId(configRaAccount.get(0).getConfigRaAccountExtendInfo().getId());
					}else{
						page.get(i).setRaAccountExtedId((long) 0);
					}
				}
        		page.get(i).setProductName(ProductType.getProductTypeName(Integer.valueOf(page.get(i).getProductName())));
		}*/
		 for (int i = 0; i < page.size(); i++) {
		  page.get(i).setProductName(ProductType.getProductTypeName(Integer.valueOf(page.get(i).getProductName())));
		  }
        model.addAttribute("appId", appId);
        model.addAttribute("page", page);
        model.addAttribute("proList", ProductType.getProductTypeList());
        model.addAttribute("title", "【"+configApp.getAppName()+"】应用下的产品列表:");
		return "modules/profile/configProductList";
	}

	@RequiresPermissions("profile:configProduct:view")
	@RequestMapping(value = "form")
	public String form(ConfigProduct configProduct, Model model,Long appId) {
		String proName="";
		ProductType pro = new ProductType();
		List<ProductTypeObj> pros =  pro.getProductTypeList();
		if (configProduct.getId()!=null) {
			proName = pro.getProductTypeName(Integer.parseInt(configProduct.getProductName()));
		}
		
		ConfigApp app = new ConfigApp();
		app.setId(appId);
		
		ConfigApp ap = configAppService.findByAppId(appId);
		List<ConfigRaAccount> configRaAccounts= configRaAccountService.findAll();
		
		List<ConfigRaAccountExtendInfo> raAccountExtendInfos=configRaAccountExtendInfoService.findAll();
		
		model.addAttribute("ap", ap);
		configProduct.setConfigApp(app);
		model.addAttribute("pros", pros);
		model.addAttribute("appId", appId);
		model.addAttribute("configProduct", configProduct);
		model.addAttribute("proName",proName);
		model.addAttribute("configRaAccounts", configRaAccounts);
		model.addAttribute("productRaAccount", configProduct.getRaAccountId());
		model.addAttribute("raAccountExtendInfos", raAccountExtendInfos);
		model.addAttribute("productCertAccount", configProduct.getRaAccountExtedId());
		return "modules/profile/configProductForm";
	}

	@RequiresPermissions("profile:configProduct:edit")
	@RequestMapping(value = "save")
	public String save(ConfigProduct configProduct, Model model, RedirectAttributes redirectAttributes,Long appId/*, Long raAccountId,Long raAccountExtedId*/) {
		if (!beanValidator(model, configProduct)){
			return form(configProduct, model,configProduct.getConfigApp().getId());
		}
		ConfigApp configApp = new ConfigApp();
		configApp.setId(appId);
		configProduct.setConfigApp(configApp);
		/*configProduct.setRaAccountId(raAccountId);
		configProduct.setRaAccountExtedId(raAccountExtedId);*/
		
//		if (configProduct.getRaAccountId()==null) {
//			ConfigRaAccount raAccount = new ConfigRaAccount();
//			ConfigRaAccountExtendInfo extendInfo = new ConfigRaAccountExtendInfo();
//			extendInfoService.save(extendInfo);
//			raAccount.setConfigRaAccountExtendInfo(extendInfo);
//			raAccountService.save(raAccount);
//			configProduct.setRaAccountId(raAccount.getId());
//			configProduct.setRaAccountExtedId(extendInfo.getId());
//		}
		
		String proName = ProductType.getProductTypeName(Integer.parseInt(configProduct.getProductName()));
		boolean valid = configProductService.validateProductValid(configProduct);
		if (valid) {
			configProductService.save(configProduct);

			logUtil.saveSysLog("业务配置", "保存产品"+ proName +"成功", null);
			addMessage(redirectAttributes, "保存产品'" + proName + "'成功");
			
		}else {

			logUtil.saveSysLog("业务配置", "保存产品"+ proName +"失败", null);

			

			addMessage(redirectAttributes, "保存产品失败:不允许存在重复的产品");
		}
		
		return "redirect:"+Global.getAdminPath()+"/profile/configProduct?appId="+appId;
	}
	
	@RequiresPermissions("profile:configProduct:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configProductService.delete(id);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"的应用成功", null);
		addMessage(redirectAttributes, "删除应用成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configProduct/?repage";
	}
	
	
	//通过AJAX查询应用下所有产品
	@RequestMapping(value = "addProduct")
	@ResponseBody
	public String productName(long applyId) throws JSONException{
		JSONObject json = new org.json.JSONObject();
		JSONArray array = new JSONArray();
		try{
			List<ConfigProduct> products = configProductService.findByApp(applyId);
			ProductType productType = new ProductType();
			for(ConfigProduct product:products){
			json = new JSONObject();
			json.put("status", "1");
			json.put("id",product.getId());
			json.put("name",ProductType.getProductTypeName(Integer.parseInt(product.getProductName())));
			array.put(json);
			}
		}catch(Exception e){
			e.printStackTrace();
			json.put("status", "0");
		}
		return array.toString();
	}
	//绑定Ra模板
	@RequiresPermissions("profile:configProduct:edit")
	@RequestMapping(value="bindSave")
	public String bindSave(Long productId, Long raAccountId,RedirectAttributes redirectAttributes)
	{
		ConfigProduct configProduct=configProductService.get(productId);
		ConfigRaAccount configRaAccount=configRaAccountService.get(raAccountId);
		configProduct.setRaAccountId(configRaAccount.getId());
		configProductService.save(configProduct);
		logUtil.saveSysLog("RA模板绑定","绑定id为"+raAccountId+"RA模板成功",null);
		addMessage(redirectAttributes, "绑定RA模板成功");
		//return "redirect:"+Global.getAdminPath()+"/profile/configRaAccount/bindList?repage";
		//return "redirect:" + Global.getAdminPath() + "/profile/configProduct?appId="+configProduct.getConfigApp().getId();
		return "redirect:" + Global.getAdminPath() + "/profile/configRaAccount/bindList?configProductId="+configProduct.getId();
	}
	//解除RA模板绑定
	@RequiresPermissions("profile:configProduct:edit")
	@RequestMapping(value="unBind")
	public String unBind(Long productId,RedirectAttributes redirectAttributes)
	{
		ConfigProduct configProduct=configProductService.get(productId);
		configProduct.setRaAccountId(null);
		configProductService.save(configProduct);
		logUtil.saveSysLog("解除RA模板绑定","解除名称为"+configProduct.getProductName()+"的RA模板成功",null);
		addMessage(redirectAttributes, "解除绑定RA模板成功");
		return "redirect:" + Global.getAdminPath() + "/profile/configProduct?appId="+configProduct.getConfigApp().getId();
	}
	//绑定证书模板
	@RequiresPermissions("profile:configProduct:edit")
	@RequestMapping(value="bindCertSave")
	public String bindCertSave(Long productId, Long certId,RedirectAttributes redirectAttributes)
	{
		ConfigProduct configProduct=configProductService.get(productId);
		ConfigRaAccountExtendInfo configRaAccountExtendInfo=configRaAccountExtendInfoService.get(certId);
		configProduct.setRaAccountExtedId(configRaAccountExtendInfo.getId());
		configProductService.save(configProduct);
		logUtil.saveSysLog("证书模板绑定","绑定id为"+configRaAccountExtendInfo.getCertName()+"证书模板成功",null);
		addMessage(redirectAttributes, "绑定证书模板成功");
		return "redirect:" + Global.getAdminPath() + "/profile/configRaAccountExtendInfo/bindList?configProductId="+configProduct.getId();
	}
	//解除证书模板绑定
	@RequiresPermissions("profile:configProduct:edit")
	@RequestMapping(value="unCertBind")
	public String unCertBind(Long productId,RedirectAttributes redirectAttributes)
	{
		ConfigProduct configProduct=configProductService.get(productId);
		configProduct.setRaAccountExtedId(null);
		configProductService.save(configProduct);
		logUtil.saveSysLog("解除证书模板绑定","解除名称为"+configProduct.getProductName()+"的证书模板成功",null);
		addMessage(redirectAttributes, "解除绑定证书模板成功");
		return "redirect:" + Global.getAdminPath() + "/profile/configProduct?appId="+configProduct.getConfigApp().getId();
	}
	
	
	
	
	
	
	
	
	
}
