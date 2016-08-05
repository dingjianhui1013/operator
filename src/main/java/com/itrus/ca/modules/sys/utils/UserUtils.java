/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.dao.AreaDao;
import com.itrus.ca.modules.sys.dao.MenuDao;
import com.itrus.ca.modules.sys.dao.OfficeDao;
import com.itrus.ca.modules.sys.dao.UserDao;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Menu;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.Role;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.itrus.ca.modules.sys.service.OfficeService;

/**
 * 用户工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class UserUtils extends BaseService {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
	private static ConfigAppService configAppService = SpringContextHolder.getBean(ConfigAppService.class);
	private static ConfigAppOfficeRelationService configAppOfficeRelationService = SpringContextHolder.getBean(ConfigAppOfficeRelationService.class);
	
	
	
	public static final String CACHE_USER = "user";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";

	public static User getUser() {
		User user = (User) getCache(CACHE_USER);
		if (user == null) {
			Principal principal = (Principal) SecurityUtils.getSubject()
					.getPrincipal();
			if (principal != null) {
				user = userDao.findOne(principal.getId());
				putCache(CACHE_USER, user);
			}
		}
		if (user == null) {
			user = new User();
			SecurityUtils.getSubject().logout();
		}
		return user;
	}

	public static User getUser(boolean isRefresh) {
		if (isRefresh) {
			removeCache(CACHE_USER);
		}
		return getUser();
	}

	public static List<Menu> getMenuList() {
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
//		if (menuList == null) {
			User user = getUser();
//			if (user.isAdmin()) {
//				menuList = menuDao.findAllList();
//			} else {
				menuList = menuDao.findByUserId(user.getId());
//			}
			putCache(CACHE_MENU_LIST, menuList);
//		}
		return menuList;
	}
	
	public static List<Menu> getMenuListWithRole() {
		@SuppressWarnings("unchecked")
		List<Menu> menuList = new ArrayList<Menu>();
			User user = getUser();
			if (user.isAdmin()) {
				menuList = menuDao.findAllList();
			} else {
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		return menuList;
	}

	public static List<Area> getAreaList() {
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			areaList = areaDao.findAllList();
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}

	public static List<Office> getOfficeList() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = null;
		if (officeList == null) {
			User user = getUser();
			DetachedCriteria dc = officeDao.createDetachedCriteria();
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
			dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("code"));
			officeList = officeDao.find(dc);
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	public static List<Office> getOfficeList(Integer type) {
		@SuppressWarnings("unchecked")
		List<Office> officeList = null;
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("type", type.toString()));
		dc.addOrder(Order.asc("code"));
		officeList = officeDao.find(dc);
		putCache(CACHE_OFFICE_LIST, officeList);
		return officeList;
	}
	
	
	
	/**
	 * 获取当前user所能看到的app集合
	 */
	public static List<ConfigApp> getAppList(){
		
		User user = getUser();
		
		//判断是否为管理员
		boolean isAdmin = false;
				
		List<Role> roleList = user.getRoleList();
		for(Role role: roleList){
			if(role.getId()==1){
				isAdmin = true;
				break;
			}
		}
		
		
		List<ConfigApp> configApps = Lists.newArrayList();
		
		//如果用户有管理员权限 ,则默认展示全部应用,如果没有管理员权限,则默认展示该用户下能够看到的全部应用
		if(isAdmin){
			configApps = configAppService.findAllConfigApp();
				
		}else{
			// user对应的所有网点
			List<Office> officeList = officeService.getOfficeByType(user, 2);
		    // user能够看到的全部应用
			for (int i = 0; i < officeList.size(); i++) {

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
						.findAllByOfficeId(officeList.get(i).getId());
				for (ConfigAppOfficeRelation appOffice : appOffices) {
					if (!configApps.contains(appOffice.getConfigApp())) {
						configApps.add(appOffice.getConfigApp());
					}
							
				}
			}
		}
		
		
		
		return configApps;
		
	}
	
	
	
	
	public static List<Long> getOfficeIdList(Long area,Long office,List<Office> offices){
		
		List<Long> officeids = Lists.newArrayList();
		
		if (area != null && office != null) {
			offices = officeService.findByParentId(area);
			
			officeids.add(office);
		} else if (area != null && office == null) {
			offices = officeService.findByParentId(area);
			
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else {

			
			User user = getUser();
			
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			offices = officeService.findByParentIds(areas);
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		
		
		return officeids;
	}
	
	

	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}

	public static Map<String, Object> getCacheMap() {
		Map<String, Object> map = Maps.newHashMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			return principal != null ? principal.getCacheMap() : map;
		} catch (UnavailableSecurityManagerException e) {
			return map;
		}
	}

}
