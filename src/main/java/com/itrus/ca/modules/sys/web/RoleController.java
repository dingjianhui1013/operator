/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.web;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.Role;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 角色Controller
 * 
 * @author ThinkGem
 * @version 2013-5-15 update 2013-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;

	private LogUtil logUtil = new LogUtil();

	// @Autowired
	// private CategoryService categoryService;

	@Autowired
	private OfficeService officeService;

	@ModelAttribute("role")
	public Role get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return systemService.getRole(id);
		} else {
			return new Role();
		}
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = { "list", "" })
	public String list(Role role, Model model, HttpServletRequest request) {
		System.out.println(request.getHeader("Accept"));
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		return "modules/sys/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		if (role.getOffice() == null) {
			role.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		// model.addAttribute("categoryList", categoryService.findByUser(false,
		// null));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/roleForm";
	}

	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Model model, String oldName, RedirectAttributes redirectAttributes) {
		role.setEnname(UUID.randomUUID().toString());
		role.setRoleType(UUID.randomUUID().toString());

		if (!beanValidator(model, role)) {
			return form(role, model);
		}
		if (!"true".equals(checkName(oldName, role.getName()))) {
			logUtil.saveSysLog("系统设置", "保存" + role.getName() + "角色失败", null);
			addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
			return form(role, model);
		}

		if (role.getDataScope().equals(Role.DATA_SCOPE_CUSTOM)) {
			if ((role.getMenuIds() == null || role.getMenuIds().equals(""))
					|| (role.getOfficeIds() == null || role.getOfficeIds().equals(""))) {
				logUtil.saveSysLog("系统设置", "保存" + role.getName() + "角色失败", null);
				addMessage(model, "保存角色'" + role.getName() + "'失败, 请至少选择一个网点及一个角色权限");
				return form(role, model);
			}
		}
		if (role.getStartTime() != null && role.getEndTime() != null) {
			try {
				Date startTime = role.getStartTime();
				Date endTime = role.getEndTime();
				long diff = endTime.getTime() - new Date().getTime();
				System.out.println(diff);
				long days = diff / (1000 * 60 * 60 * 24);
				role.setDays(days);
				
			} catch (Exception e) {

			}
		}
		/*
		 * if (role.getDataScope().equals(Role.DATA_SCOPE_CUSTOM)) {//按明细设置 if
		 * (role.getOfficeIds()==null||role.getOfficeIds().length()==0) {
		 * addMessage(model, "保存角色'" + role.getName() + "'失败, 请选择网点"); return
		 * form(role, model); } }
		 */

		String detail = "";
		if (role.getId() == null) {
			detail = "添加" + role.getName() + "角色成功";
		} else {
			detail = "更新" + role.getName() + "角色成功";
		}

		systemService.saveRole(role);
		logUtil.saveSysLog("系统管理", detail, null);
		addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/role/?repage";
	}

	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		if (Role.isAdmin(id)) {
			logUtil.saveSysLog("系统管理", "删除id为" + id + "的角色失败", null);
			addMessage(redirectAttributes, "删除角色失败, 不允许内置角色或编号空");
		} else {
			Role role = systemService.getRole(id);
			List<User> user = role.getUserList();
			if (user != null && user.size() != 0) {
				logUtil.saveSysLog("系统管理", "删除id为" + id + "的角色失败", null);
				addMessage(redirectAttributes, "删除角色失败, 当前角色下有管理员不允许删除");
			} else {
				systemService.deleteRole(id);
				logUtil.saveSysLog("系统管理", "删除id为" + id + "的角色成功", null);
				addMessage(redirectAttributes, "删除角色成功");
			}
		}
		return "redirect:" + Global.getAdminPath() + "/sys/role/?repage";
	}

	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> users = role.getUserList();
		model.addAttribute("users", users);
		return "modules/sys/roleAssign";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("selectIds", role.getUserIds());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUserToRole";
	}

	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(Long officeId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Office office = officeService.get(officeId);
		List<User> userList = office.getUserList();
		for (User user : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", user.getId());
			map.put("pId", 0);
			map.put("name", user.getName());
			mapList.add(map);
		}
		return mapList;
	}

	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(Long userId, Long roleId, RedirectAttributes redirectAttributes) {
		Role role = systemService.getRole(roleId);
		User user = systemService.getUser(userId);
		if (user.equals(UserUtils.getUser())) {
			addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
		} else {
			Boolean flag = systemService.outUserInRole(role, userId);
			if (!flag) {
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
			} else {
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
			}
		}
		return "redirect:" + Global.getAdminPath() + "/sys/role/assign?id=" + role.getId();
	}

	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, Long[] idsArr, RedirectAttributes redirectAttributes) {
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = systemService.assignUserToRole(role, idsArr[i]);
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功分配 " + newNum + " 个用户" + msg);
		return "redirect:" + Global.getAdminPath() + "/sys/role/assign?id=" + role.getId();
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name != null && name.equals(oldName)) {
			return "true";
		} else if (name != null && systemService.findRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

}
