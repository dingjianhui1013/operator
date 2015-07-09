/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.profile.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/office")
public class ConfigOfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	private LogUtil logUtil = new LogUtil();
	
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) Long id) {
		if (id != null){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"list", ""})
	public String list(Office office, Model model,HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		if(user.isAdmin()){
			office.setId(1L);
		}else{
			office.setId(user.getOffice().getId());
		}
		Page<Office> page = officeService.find(new Page<Office>(request, response),office);
	        model.addAttribute("page", page);
		 
	//	Office.sortList(list, sourcelist, office.getId());
        model.addAttribute("page", page);
		return "modules/profile/configOfficeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"officeView"})
	public String list(Office office, Model model,Long appId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		User user = UserUtils.getUser();
//		if(user.isAdmin()){
			office.setId(1L);
//		}else{
//			office.setId(user.getOffice().getId());
//		}
	   List<ConfigAppOfficeRelation> configAppOfficeRelations =  configAppOfficeRelationService.findAllByAppId(appId);
			
		
		Page<Office> page = officeService.findByDel(new Page<Office>(request, response),office);
	        model.addAttribute("page", page);
		 
	//	Office.sortList(list, sourcelist, office.getId());
		 for (int i = 0; i < configAppOfficeRelations.size(); i++) {
			for (int j = 0; j < page.getList().size(); j++) {
				if (page.getList().get(j).getId().equals(configAppOfficeRelations.get(i).getOffice().getId())) {
					page.getList().get(j).setOnfous(true);
					break;
				}
			}
		}
		model.addAttribute("appId", appId);
        model.addAttribute("page", page);
		return "modules/profile/configOfficeList";
	}

	@RequestMapping(value = "saveAppOffice")
	public String saveAppOffice(Office office, Model model, RedirectAttributes redirectAttributes,Long appId) {
		try {
			ConfigApp  configApp = new ConfigApp();
			configApp.setId(appId);
			ConfigAppOfficeRelation configAppOfficeRelation = new ConfigAppOfficeRelation();
			configAppOfficeRelation.setConfigApp(configApp);
			configAppOfficeRelation.setOffice(office);
			configAppOfficeRelation.setCreatDate(new Date());
			configAppOfficeRelationService.save(configAppOfficeRelation);
		} catch (Exception e) {
			// TODO: handle exception
		}
		logUtil.saveSysLog("业务配置", "授权区域"+office.getName()+"成功", null);
		addMessage(redirectAttributes, "授权区域" + office.getName() + "成功");
		return "redirect:"+Global.getAdminPath()+"/profile/office/officeView?appId="+appId;
	}
	
	
	@RequestMapping(value = "deleteAppOffice")
	public String deleteAppOffice(Office office, Model model, RedirectAttributes redirectAttributes,Long appId) {
		List<ConfigAppOfficeRelation> configAppOfficeRelations  = configAppOfficeRelationService.findAllByOfficeId(office.getId());
		try {
			Long id = configAppOfficeRelations.get(0).getId();
			configAppOfficeRelationService.deleteByofficeId(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		logUtil.saveSysLog("业务配置", "取消授权"+office.getName()+"网点成功", null);
		addMessage(redirectAttributes, office.getName()+"取消授权网点成功");
		return "redirect:"+Global.getAdminPath()+"/profile/office/officeView?appId="+appId;
	}
	
	


	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) Long type,
			@RequestParam(required=false) Long grade, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Office> list = officeService.findAll();
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
