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
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.service.ConfigChargeSupplierDetailService;
import com.itrus.ca.modules.profile.service.ConfigSupplierProductRelationService;

/**
 * 供应商计费配置Controller
 * @author ZhangShaoBo
 * @version 2014-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configSupplierProductRelation")
public class ConfigSupplierProductRelationController extends BaseController {

	@Autowired
	private ConfigSupplierProductRelationService configSupplierProductRelationService;
	
	@Autowired
	private ConfigChargeSupplierDetailService configChargeSupplierDetailService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	
	private LogUtil logUtil = new LogUtil();
	
	@ModelAttribute
	public ConfigSupplierProductRelation get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configSupplierProductRelationService.get(id);
		}else{
			return new ConfigSupplierProductRelation();
		}
	}
	
	@RequiresPermissions("profile:configSupplierProductRelation:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigSupplierProductRelation configSupplierProductRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();

        Page<ConfigSupplierProductRelation> page = configSupplierProductRelationService.find(new Page<ConfigSupplierProductRelation>(request, response), configSupplierProductRelation);
        List<KeyGeneralInfo> geneInfos = keyGeneralInfoService.findAll();
        List<ConfigSupplierProductRelation> bound =  configSupplierProductRelationService.findByNotNullGeneInfo();
        if (geneInfos.size()>bound.size()) {
        	 model.addAttribute("showHistory", 1);
		}
        model.addAttribute("page", page);
        model.addAttribute("productType", ProductType.proTypeMap);
        model.addAttribute("proList", ProductType.getProTypeList());
		return "modules/profile/configSupplierProductRelationList";
	}

	@RequiresPermissions("profile:configSupplierProductRelation:view")
	@RequestMapping(value = "form")
	public String form(ConfigSupplierProductRelation configSupplierProductRelation, Model model) {	
		
		List<ConfigChargeSupplierDetail> list = configChargeSupplierDetailService.findByChargeSupplierId(configSupplierProductRelation);
		model.addAttribute("list", list);
		for (ConfigChargeSupplierDetail detail : list) {
			model.addAttribute("money"+detail.getYear(), detail.getMoney());
		}
		model.addAttribute("configSupplierProductRelation", configSupplierProductRelation);
		model.addAttribute("productTypeMap", ProductType.productTypeMap);
		return "modules/profile/configSupplierProductRelationForm";
	}

	@RequiresPermissions("profile:configSupplierProductRelation:edit")
	@RequestMapping(value = "save")
	public String save(ConfigSupplierProductRelation configSupplierProductRelation, Double[] money, Model model, RedirectAttributes redirectAttributes) {
		List<ConfigChargeSupplierDetail> list = configChargeSupplierDetailService.findByChargeSupplierId(configSupplierProductRelation);
		configChargeSupplierDetailService.deleteList(list);
		Integer year = 1;
		for (Double double1 : money) {
			if (double1!=null) {
				ConfigChargeSupplierDetail configChargeSupplierDetail = new ConfigChargeSupplierDetail();
				configChargeSupplierDetail.setConfigSupplierProductRelation(configSupplierProductRelation);
				configChargeSupplierDetail.setMoneyType(configSupplierProductRelation.getProductType());
				configChargeSupplierDetail.setMoney(double1);
				configChargeSupplierDetail.setYear(year);

				
				//configChargeSupplierDetail.setYear(year==3? 4:year);
				configChargeSupplierDetailService.save(configChargeSupplierDetail);
			}
			year++;
		}
		logUtil.saveSysLog("业务配置", "保存" + configSupplierProductRelation.getConfigSupplier().getSupplierName() + "计费配置成功", null);
		addMessage(redirectAttributes, "保存'" + configSupplierProductRelation.getConfigSupplier().getSupplierName() + "'计费配置成功");
		
		return "redirect:"+Global.getAdminPath()+"/profile/configSupplierProductRelation";
	}
	
	
	@RequestMapping(value = "replaceHistoryKey")
	@ResponseBody
	public String replaceHistoryKey() throws JSONException{
		JSONObject json = new JSONObject();
		
		try {
			 List<KeyGeneralInfo> geneInfos = keyGeneralInfoService.findAll();
			 
			 for (int i = 0; i < geneInfos.size(); i++) {
				 ConfigSupplierProductRelation bound =  configSupplierProductRelationService.findBySupplierType(geneInfos.get(i).getConfigSupplier(),Integer.parseInt(geneInfos.get(i).getId().toString()) );
				 if (bound==null) {
					ConfigSupplierProductRelation configSupplierProductRelation = new ConfigSupplierProductRelation();
					configSupplierProductRelation.setConfigSupplier(geneInfos.get(i).getConfigSupplier());
					KeyGeneralInfo keyGeneralInfo = geneInfos.get(i);
					configSupplierProductRelation.setKeyGeneralInfo(keyGeneralInfo);
					configSupplierProductRelationService.save(configSupplierProductRelation);
				}
			 }
			 json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 0);
		}
		return json.toString();
	}
	
	
	
	@RequiresPermissions("profile:configSupplierProductRelation:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configSupplierProductRelationService.delete(id);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"的供应商产品配置成功", null);
		addMessage(redirectAttributes, "删除供应商产品配置成功");
		return "redirect:"+Global.getAdminPath()+"/modules/profile/configSupplierProductRelation/?repage";
	}

}
