/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigSupplierProductRelationService;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.profile.utils.Type;

/**
 * 供应商Controller
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configSupplier")
public class ConfigSupplierController extends BaseController {

	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private ConfigSupplierProductRelationService configSupplierProductRelationService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	private LogUtil logUtil = new LogUtil();
	
	
	@ModelAttribute
	public ConfigSupplier get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configSupplierService.get(id);
		}else{
			return new ConfigSupplier();
		}
	}
	
	@RequiresPermissions("profile:configSupplier:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigSupplier configSupplier, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ConfigSupplier> page = configSupplierService.find(new Page<ConfigSupplier>(request, response), configSupplier);
        model.addAttribute("page", page);
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "证书");
        map.put(1, "key");
        map.put(2, "签章");
        model.addAttribute("typeMap", map);
        model.addAttribute("productTypeMap", ProductType.productTypeMap);
		return "modules/profile/configSupplierList";
	}

	@RequiresPermissions("profile:configSupplier:manufacturers")
	@RequestMapping(value = "listByKey")
	public String listByKey(ConfigSupplier configSupplier, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ConfigSupplier> page = configSupplierService.findByKey(new Page<ConfigSupplier>(request, response), configSupplier);
       
        
//        HashMap<Integer, String> map = new HashMap<Integer, String>();
//        map.put(0, "证书");
//        map.put(1, "key");
//        map.put(2, "签章");
        
        model.addAttribute("page", page);
   //     model.addAttribute("typeMap", map);
  //      model.addAttribute("productTypeMap", ProductType.productTypeMap);
		return "modules/key/keyManufacturerList";
	}

	
	
	
	
	
	
	
	
	@RequiresPermissions("profile:configSupplier:view")
	@RequestMapping(value = "form")
	public String form(ConfigSupplier configSupplier, Model model) {
		
		ArrayList<Type> list = new ArrayList<Type>();
		ProductType productType = new ProductType();
		
		List<ProductTypeObj> productTypeObjs = productType.getProductTypeList();

		for (int i = 0; i < productTypeObjs.size(); i++) {
			Type map = new Type();
			map.setId(productTypeObjs.get(i).getId());
			map.setName(productType.getProductTypeName(productTypeObjs.get(i).getId()));
			if (configSupplier.getId()!=null) {
				List<ConfigSupplierProductRelation> configSupplierProductRelation = configSupplierProductRelationService.findBySupplier(configSupplier);
				for (ConfigSupplierProductRelation cs2 : configSupplierProductRelation) {
					if(cs2.getProductType()!=null){
						if (cs2.getProductType()==(i+1)) {
							map.setKebl(true);
						}
					}
				}
		}


			list.add(map);
			
		}
		model.addAttribute("configSupplier", configSupplier);
		model.addAttribute("list", list);
		return "modules/profile/configSupplierForm";
	}

	@RequiresPermissions("profile:configSupplier:edit")
	@RequestMapping(value = "save")
	public String save(ConfigSupplier configSupplier,Integer[] product, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configSupplier)){
			return form(configSupplier, model);
		}		
		//保存供应商对象
		configSupplierService.save(configSupplier);
		//保存供应商产品表对象
		List<ConfigSupplierProductRelation> list = configSupplierProductRelationService.findBySupplier(configSupplier);
		for (ConfigSupplierProductRelation config : list) {
			 configSupplierProductRelationService.delete(config.getId());
		}
		if (product!=null) {
			for (int i = 0; i < product.length; i++) {
				ConfigSupplierProductRelation configSupplierProductRelation = new ConfigSupplierProductRelation();
				configSupplierProductRelation.setProductType(product[i]);
				configSupplierProductRelation.setConfigSupplier(configSupplier);
				configSupplierProductRelationService.save(configSupplierProductRelation);
			}
		}
		logUtil.saveSysLog("业务配置", "保存供应商'" + configSupplier.getSupplierName() + "'成功", "");
		addMessage(redirectAttributes, "保存供应商'" + configSupplier.getSupplierName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configSupplier";
	}
	
	@RequiresPermissions("profile:configSupplier:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		ConfigSupplier configSupplier =  configSupplierService.get(id);
		String name = configSupplier.getSupplierName();
		
		configSupplierService.delete(id);
	//	addMessage(redirectAttributes, "删除供应商成功");
		logUtil.saveSysLog("业务配置", "删除供应商成功：编号"+id, "");
		addMessage(redirectAttributes, "删除供应商'"+name+"'成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configSupplier";
	}


	@RequestMapping(value = "checkName")
	@ResponseBody
	public String checkSupplierName(String supplierName,Long supplierId) throws JSONException{
		JSONObject json = new JSONObject();
		try {
			ConfigSupplier verify = configSupplierService.findBySupplierName(supplierName,supplierId);
			if (verify!=null) {
					json.put("type", 1);
			}else {
				json.put("type", 0);
			}
			json.put("status", 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", 1);
		
		}
		return json.toString();
	}
	
	@RequestMapping(value="used")
	@ResponseBody
	public String used(Long id,Integer type) throws JSONException{
		JSONObject json = new JSONObject();
		try {
			ConfigSupplier configSupplier =  configSupplierService.get(id);
		
				List<KeyGeneralInfo> KeyGeneralInfo = keyGeneralInfoService.findBySupplierId(id);
				if (KeyGeneralInfo.size()>0) {
					json.put("type", 1);
				}else {
					json.put("type", 0);
				}
				json.put("supplierName", configSupplier.getSupplierName());
				json.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 1);
		}
		
		return json.toString();
	};
	
	
	
	
	
	
		
	
}
