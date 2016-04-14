/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.log.entity.SysOperateLog;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.log.service.SysLogService;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.AreaService;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * @author ThinkGem   
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) Long id) {
		if (id != null){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}
	
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model) {
		area.setId(1L);
		model.addAttribute("area", area);
		List<Area> list = Lists.newArrayList();
		List<Area> sourcelist = areaService.findAll();
		Area.sortList(list, sourcelist, area.getId());
        model.addAttribute("list", list);
		return "modules/sys/areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent()==null||area.getParent().getId()==null){
			area.setParent(UserUtils.getUser().getOffice().getArea());
		}
		area.setParent(areaService.get(area.getParent().getId()));
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area,String officeName, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, area)){
			return form(area, model);
		}
		String detail = "";
		if(area.getId()==null){
			detail = "添加"+area.getName()+"区域成功";
		}else{
			detail = "更新"+area.getName()+"区域成功";
		}
		areaService.save(area);
		
		logUtil.saveSysLog("业务配置", detail, null);
		
		if(officeName!=null&&!officeName.equals("")){
			Office parent = new Office();
			parent.setId(1L);
			Office office = new Office();
			office.setParent(parent);
			office.setName(officeName);
			office.setParentIds("1");
			office.setCode("100000");
			office.setType("2");
			office.setGrade("1");
			office.setAddress("五马路");
			office.setZipCode("1");
			office.setMaster("1");
			office.setPhone("1");
			office.setFax("1");
			office.setEmail("1");
			office.setRemarks("1");
			office.setArea(area);
			
			
			officeService.save(office);
			logUtil.saveSysLog("业务配置", "添加"+office.getName()+"网点成功", null);
			
		}
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/office/";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		if (Area.isAdmin(id)){
			addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
		}else{
			areaService.delete(id);
			logUtil.saveSysLog("业务配置", "删除id为"+id+"的区域成功", null);
			addMessage(redirectAttributes, "删除区域成功");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/area/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	//通过AJAX查询区域下的所有网店
		@RequestMapping(value = "addOffices")
		@ResponseBody
		public String delete(Long areaId) throws JSONException {
			JSONObject  json = new org.json.JSONObject();
			JSONArray array = new JSONArray();		
			try {
				Area area = areaService.get(areaId);
				List<Office> offices = area.getOfficeList();
				for (Office office : offices) {
					json = new JSONObject();
					json.put("id", office.getId());
					json.put("name", office.getName());
					array.put(json);
				}				
				// 检查是否有权限操作
			} catch (Exception e) {
				e.printStackTrace();
				json.put("status", "0");
			}
			return array.toString();
		}

	
	
}
