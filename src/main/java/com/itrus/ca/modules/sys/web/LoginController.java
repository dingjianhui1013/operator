/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.web;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.utils.CacheUtils;
import com.itrus.ca.common.utils.CookieUtils;
import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.SingleCvm;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;

import cn.topca.sp.cvm.CVM;
import cn.topca.sp.svm.SVM;
import cn.topca.sp.x509.X509CRL;
import cn.topca.sp.x509.X509Certificate;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	Logger log = Logger.getLogger(LoginController.class);
	@Autowired
	SystemService systemService;
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			systemService.updateUserLoginInfo(user.getId(),StringUtils.getRemoteAddr(request));
			return "redirect:"+Global.getAdminPath();
		}
		String randomString = UUID.randomUUID().toString();
		
		HttpSession session = request.getSession();
		session.setAttribute("randomString", randomString);
		
		model.addAttribute("randomString", randomString);
		
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String login(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			systemService.updateUserLoginInfo(user.getId(),StringUtils.getRemoteAddr(request));
			return "redirect:"+Global.getAdminPath();
		}
		String type = request.getParameter("type");
		if(StringUtils.isNotEmpty(type)&&"1".equals(type)){
			model.addAttribute("sccaError", "error");
		}else{
			model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}
		return "modules/sys/sysLogin";
	}
	
	public String validate(String signData,HttpServletRequest request, HttpServletResponse response){
		
		
		
		HttpSession session = request.getSession();
		try {
			
			
			String randomString = (String)session.getAttribute("randomString");
			
			if(randomString==null){
				return "redirect:"+Global.getAdminPath()+"/login";
			}
			
			String toSign = "LOGONDATA:"+randomString;
			byte[] signedDate = Base64.decodeBase64(signData);
			X509Certificate certificate = SVM.verifyPKCS7SignedData(signedDate, toSign.getBytes());
		
			
			
			CVM cvm = SingleCvm.getCvm();
			int status = cvm.verifyCertificate(certificate);
			if(status!=0){
				return "redirect:"+Global.getAdminPath()+"/login";
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
		
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresUser
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:"+Global.getAdminPath()+"/login";
		}
		
		// 更新登录IP和时间
		systemService.updateUserLoginInfo(user.getId(),StringUtils.getRemoteAddr(request));
		log.info(user.getId()+"登陆成功..");
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true);
		// 登录成功后，获取上次登录的当前站点ID
		UserUtils.putCache("siteId", StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));
		CookieUtils.setCookie(response, "theme", "flat");
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
	

}
