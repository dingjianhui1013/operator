/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

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

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyManufacturerService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;

/**
 * key厂家信息Controller
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyManufacturer")
public class KeyManufacturerController extends BaseController {

	@Autowired
	private KeyManufacturerService keyManufacturerService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	@Autowired
	private KeyUsbKeyService keyUsbKeyService;
	
	private LogUtil logUtil = new LogUtil();
	
	@ModelAttribute
	public KeyManufacturer get(@RequestParam(required=false) Long id) {
		if (id != null){
			return keyManufacturerService.get(id);
		}else{
			return new KeyManufacturer();
		}
	}
	
	@RequiresPermissions("key:keyManufacturer:view")
	@RequestMapping(value = {"list", ""})
	public String list(KeyManufacturer keyManufacturer, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<KeyManufacturer> page = keyManufacturerService.find(new Page<KeyManufacturer>(request, response), keyManufacturer); 
        model.addAttribute("page", page);
		return "modules/key/keyManufacturerList";
	}

	@RequiresPermissions("key:keyManufacturer:view")
	@RequestMapping(value = "form")
	public String form(KeyManufacturer keyManufacturer, Model model) {
		model.addAttribute("keyManufacturer", keyManufacturer);
		return "modules/key/keyManufacturerForm";
	}

	@RequiresPermissions("key:keyManufacturer:edit")
	@RequestMapping(value = "save")
	public String save(KeyManufacturer keyManufacturer, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, keyManufacturer)){
			return form(keyManufacturer, model);
		}
		
		if (keyManufacturer.getId()==null) {
			List<KeyManufacturer> manns =keyManufacturerService.findByName(keyManufacturer.getName());
			if (manns.size()>0) {
				model.addAttribute("keyManufacturer", keyManufacturer);
				model.addAttribute("message", "厂商名称已存在，请重新输入！");
				return "modules/key/keyManufacturerForm";
			}
		}
		String detail = "";
		if (keyManufacturer.getId()==null) {
			detail = "新增厂商"+keyManufacturer.getName()+"，编号：";
		} else {
			detail = "修改厂商"+keyManufacturer.getName()+"，编号：";
		}
		keyManufacturerService.save(keyManufacturer);
		addMessage(redirectAttributes, "保存厂商名称信息'" + keyManufacturer.getName() + "'成功");
		logUtil.saveSysLog("库存管理", detail+keyManufacturer.getId(), "");
		return "redirect:"+Global.getAdminPath()+"/key/keyManufacturer/?repage";
	}
	
	/*@RequiresPermissions("key:keyManufacturer:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		 List<KeyGeneralInfo> generalInfos =  keyGeneralInfoService.findByManuId(id);
		if (generalInfos.size()>0) {
			List<Long> idList =Lists.newArrayList();
			for (int i = 0; i < generalInfos.size(); i++) {
				Long gene = generalInfos.get(i).getId();
				idList.add(gene);
			}
			
			List<KeyUsbKey> kuk =  keyUsbKeyService.findByGeneralInfoIds(idList);
			if (kuk.size()>0) {
				addMessage(redirectAttributes, "厂商下Key类型正在使用，不能删除！");	
			}else{
				for (int i = 0; i < idList.size(); i++) {
					keyGeneralInfoService.delete(idList.get(i));
				}
				keyManufacturerService.delete(id);
				addMessage(redirectAttributes, "删除厂商及其Key类型信息成功!");
				logUtil.saveSysLog("库存信息", "删除厂商：编号"+id+"及其key类型信息成功", "");
			}
			
			
		}else{
			keyManufacturerService.delete(id);
			addMessage(redirectAttributes, "删除厂商信息成功");
			logUtil.saveSysLog("库存信息", "删除厂商：编号"+id, "");
		}
		return "redirect:"+Global.getAdminPath()+"/key/keyManufacturer/?repage";
	}*/
	
	
	// 通过AJAX判断
	@RequestMapping(value = "checkName")
	@ResponseBody
	public String bbfw2Submit(String name,int id) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			List<KeyManufacturer> manns =keyManufacturerService.findByName(name);
			if (id!=0) {
				if (manns.size()==1) {
					
				}		
			}else{
				if(manns.size()>0){
					json.put("status", 0);
					return json.toString();
				}
			}
			
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}
	
}
