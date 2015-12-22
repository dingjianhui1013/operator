/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.AreaService;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 机构Controller
 * 
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	private OfficeService officeService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private KeyUsbKeyDepotService keyDepotService;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "list", "" })
	public String list(Office office, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		User user = UserUtils.getUser();
		if (user.isAdmin()) {
			office.setId(1L);
		} else {
			office.setId(user.getOffice().getId());
		}
		Page<Office> page = officeService.find(new Page<Office>(request,
				response), office);
		List<Office> offices = UserUtils.getOfficeList();
		List<Office> escapeOffice = new ArrayList<Office>();
		
		for (Office office2 : offices) {
			office2.setName(EscapeUtil.escapeInput(office2.getName()));
			escapeOffice.add(office2);
		}
		model.addAttribute("page", page);
		model.addAttribute("ooo", escapeOffice);
		return "modules/sys/officeList";
	}
	

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "officeView" })
	public String list(Office office, Model model, Long appId,
			HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		office.setId(1L);
		Page<Office> page = officeService.find(new Page<Office>(request,
				response), office);
		model.addAttribute("page", page);
		List<Office> offices = UserUtils.getOfficeList();

		model.addAttribute("ooo", offices);

		return "modules/sys/officeList";
	}

	/**
	 * 查看所有网点
	 */
	@RequestMapping("addAreaFrom")
	public String addAreaFrom(Model uiModel) {
		return "modules/sys/areaForm";
	}

	/**
	 * 添加网点
	 * 
	 * @param areaName
	 * @param office
	 * @param model
	 * @param appId
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "saveArea")
	public String saveArea(String areaName, Office office, Model model,
			Long appId, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		List<Office> offices = officeService.selectArea(areaName);
		if (offices.size() > 0) {
			addMessage(redirectAttributes, "保存区域'" + areaName + "'失败，区域名称重复");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		Office zhuOffice = new Office();
		zhuOffice.setId(1L);
		Office areaOffice = new Office();
		areaOffice.setName(EscapeUtil.escapeInput(areaName));
		areaOffice.setParent(zhuOffice);
		areaOffice.setParentIds("0,1");
		areaOffice.setCode("100000");
		areaOffice.setType("1");
		areaOffice.setGrade("1");
		areaOffice.setAddress("");
		areaOffice.setZipCode("1");
		areaOffice.setMaster("1");
		areaOffice.setPhone("");
		areaOffice.setFax("1");
		areaOffice.setEmail("1");
		areaOffice.setRemarks("");
		officeService.save(areaOffice);
		logUtil.saveSysLog("业务配置", "网点配置添加区域："+areaName+" 成功", "");
		if (!(office.getName() != null && !office.getName().equals(""))) {
			office.setName(EscapeUtil.escapeInput(areaName) + "(网点)");
		}else {
			office.setName(EscapeUtil.escapeInput(office.getName()));
		}
		office.setParent(areaOffice);
		office.setParentIds("0," + areaOffice.getId());
		office.setCode("100000");
		office.setType("2");
		office.setGrade("1");
		office.setAddress("");
		office.setZipCode("1");
		office.setMaster("1");
		office.setPhone("");
		office.setFax("1");
		office.setEmail("1");
		office.setRemarks("");
		officeService.save(office);
		logUtil.saveSysLog("业务配置", "网点配置添加区域"+areaName+"下级网点："+office.getName()+" 成功", "");
		addMessage(redirectAttributes, "保存区域'" + areaName + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent() == null || office.getParent().getId() == null) {
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		// if (office.getArea()==null){
		// office.setArea(user.getOffice().getArea());
		// }
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}

	/**
	 * 跳转添加网点页面
	 * 
	 * @return
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping("isnertFrom")
	public String insertFrom(Model uiModel) {
		List<Office> offices = UserUtils.getOfficeList(1);// 1区域 2 网点
		for (Office office : offices) {
			if (office.getId().equals(1L)) {
				offices.remove(office);
				break;
			}
		}
		uiModel.addAttribute("areas", offices);
		return "modules/sys/officeInsert";
	}

	// 通过AJAX查询区域下的所有网店
	@RequestMapping(value = "addOffices")
	@ResponseBody
	public String getOffices(Long areaId) throws JSONException {
		JSONObject json = new org.json.JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Office> offids = officeService.findByParentId(areaId);
			for (Office office : offids) {
				if (office.getType().equals("1")) {
					continue;
				}
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

	// 通过AJAX查询区域下的所有网店
	@RequestMapping(value = "getDepotName")
	@ResponseBody
	public String getDepotName(Long offId) throws JSONException {
		JSONObject json = new org.json.JSONObject();
		try {
			Office off = officeService.get(offId);
			json.put("status", "1");
			json.put("name", off.getName());
			// 检查是否有权限操作
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Long areaId, Office office, Model model,
			RedirectAttributes redirectAttributes) {
		List<Office> offices = officeService.selectArea(office.getName());
		if (offices.size() > 0) {
			addMessage(redirectAttributes, "保存网点'" + EscapeUtil.escapeInput(office.getName()) + "'失败，网点名称重复");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		Office off = officeService.get(areaId);
		office.setParent(off);
		office.setParentIds("0," + areaId);
		office.setCode("100000");
		office.setType("2");
		office.setGrade("1");
		office.setZipCode("1");
		office.setMaster("1");
		office.setFax("1");
		office.setEmail("1");
		office.setRemarks("");
		office.setAreaName(off.getName());
		officeService.save(office);
		logUtil.saveSysLog("业务配置", "网点配置,添加区域"+off.getName()+"下级网点："+office.getName()+" 成功", "");
		addMessage(redirectAttributes, "保存网点'" + office.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}

	@RequestMapping("updateOffice")
	public String updateOffice(Long areaId, Office office, Model model,
			RedirectAttributes redirectAttributes) {
		Office off = officeService.get(areaId);
		officeService.updateOffice(off, office.getId(), office.getName(),
				off.getName(),office.getPhone(),office.getAddress());
		addMessage(redirectAttributes, "修改网点'" + office.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}

	@RequestMapping("updateFrom")
	public String updateFrom(Office office, Model model) {
		List<Office> offices = UserUtils.getOfficeList(1);// 1区域 2 网点
		for (Office o : offices) {
			if (o.getId().equals(1L)) {
				offices.remove(o);
				break;
			}
		}
		model.addAttribute("areas", offices);
		model.addAttribute("Office", office);
		return "modules/sys/officeUpdate";
	}

	@RequestMapping(value = "saveAppOffice")
	public String saveAppOffice(Office office, Model model,
			RedirectAttributes redirectAttributes, Long appId) {
		ConfigApp configApp = new ConfigApp();
		configApp.setId(appId);
		ConfigAppOfficeRelation configAppOfficeRelation = new ConfigAppOfficeRelation();
		configAppOfficeRelation.setConfigApp(configApp);
		configAppOfficeRelation.setOffice(office);
		configAppOfficeRelation.setCreatDate(new Date());
		configAppOfficeRelationService.save(configAppOfficeRelation);
		logUtil.saveSysLog("业务配置", "网点配置，授权区域"+ office.getName() + "成功", "");
		addMessage(redirectAttributes, "授权区域" + office.getName() + "成功");
		return "redirect:" + Global.getAdminPath()
				+ "/sys/office/officeView?appId=" + appId;
	}

	@RequestMapping(value = "deleteAppOffice")
	public String deleteAppOffice(Office office, Model model,
			RedirectAttributes redirectAttributes, Long appId) {
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(office.getId());
		Long id = configAppOfficeRelations.get(0).getId();
		configAppOfficeRelationService.deleteByofficeId(id);
		logUtil.saveSysLog("业务配置",office.getName() + "取消授权网点成功", "");
		addMessage(redirectAttributes, office.getName() + "取消授权网点成功");
		return "redirect:" + Global.getAdminPath()
				+ "/sys/office/officeView?appId=" + appId;
	}

	//@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		List<Long> forbidIds = new ArrayList<Long>();
		forbidIds.add(1L);
		forbidIds.add(34L);
		
		if (forbidIds.contains(id)) {
			addMessage(redirectAttributes, "禁用网点失败, 不允许禁用默认网点");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		Office office = officeService.get(id);
		//一并禁用区域下所有网点
		List<Office> offices = officeService.findOfficesByParentId(id);
		if (Office.isRoot(id)) {
			logUtil.saveSysLog("业务配置","禁用网点:"+office.getName()+"失败, 不允许禁用顶级网点或编号空", "");
			addMessage(redirectAttributes, "禁用网点失败, 不允许禁用顶级网点或编号空");
		} else {
			officeService.delete(id);
			for (Office off : offices) {
				officeService.delete(off.getId());
			}
			logUtil.saveSysLog("业务配置","禁用网点:"+office.getName()+"成功", "");
			addMessage(redirectAttributes, "禁用网点成功");
		}
		
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(
			@RequestParam(required = false) Long extId,
			@RequestParam(required = false) Long type,
			@RequestParam(required = false) Long grade,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// User user = UserUtils.getUser();
		List<Office> list = officeService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((extId == null || (extId != null && !extId.equals(e.getId()) && e
					.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && Integer.parseInt(e
							.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e
							.getGrade()) <= grade.intValue()))) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", !user.isAdmin() &&
				// e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent() != null ? e.getParent().getId()
						: 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 禁用是调用，查询是否关联业务（网点跟区域均可调用）
	* @Title: deleteOffice
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param id
	* @param @return
	* @param @throws JSONException    设定文件
	* @return String    返回类型
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "deleteOffice")
	public String deleteOffice(Long id) throws JSONException{
		JSONObject json=new JSONObject();
		Office office = officeService.get(id);
		List<Office> offices = officeService.findOfficesByParentId(id);
		offices.add(office);
		boolean findByoffice = workDealInfoService.findByOffice(offices);
		try {
			if (findByoffice) {
				json.put("status", 1);
			}else {
				json.put("status", -1);
			}
		} catch (Exception e) {
				json.put("status", -2);
		}
		
		return json.toString();
	}
	@RequestMapping(value="enable")
	public String enable(Long id, RedirectAttributes redirectAttributes){
		Office office = officeService.get(id);
		office.setDelFlag("0");
		officeService.save(office);
		logUtil.saveSysLog("业务配置","启用网点:"+office.getName()+"成功", "");
		addMessage(redirectAttributes, "启用网点成功");
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "listL")
	public String listL(Office office, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Page<Office> page = officeService.findOffice(new Page<Office>(request,
				response), office);
		List<Office> list = page.getList();
		model.addAttribute("page", page);
		model.addAttribute("officeId", office.getId());
		return "modules/sys/nextOfficeList";
	}
	
	
	@RequestMapping(value="deleteL")
	public String deletrL(Office office,RedirectAttributes redirectAttributes){
		List<Office> offices = officeService.findOfficesByParentId(office.getId());
		offices.add(office);
		boolean userRe = officeService.findUserByOffice(offices);
		if (userRe) {
			logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"失败", "");
			addMessage(redirectAttributes, "不允许删除，该网点已创建管理员");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		boolean roleRe = officeService.findRoleByOffice(offices);
		if (roleRe) {
			logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"失败", "");
			addMessage(redirectAttributes, "不允许删除，该网点已创建角色");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		boolean appRe = configAppOfficeRelationService.findAllByOffices(offices);
		if (appRe) {
			logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"失败", "");
			addMessage(redirectAttributes, "不允许删除，该网点已被授权应用");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		if (keyDepotService.findByOfficeIds(offices).size()>0) {
			logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"失败", "");
			addMessage(redirectAttributes, "不允许删除，该网点已创建key仓库");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		if (receiptDepotInfoService.findByOfficeIds(offices).size()>0) {
			logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"失败", "");
			addMessage(redirectAttributes, "不允许删除，该网点已创建发票仓库");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		try {
			for (Office off : offices) {
				officeService.deleteL(off);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"失败", "");
			addMessage(redirectAttributes, "不允许删除，该网点已创建关联");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		
		logUtil.saveSysLog("业务配置","删除网点:"+office.getName()+"成功", "");
		addMessage(redirectAttributes, "删除网点成功");
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}
}
