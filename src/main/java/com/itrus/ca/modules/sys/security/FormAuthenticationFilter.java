/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;


/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2013-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		
		String loginType =  request.getParameter("type");
		
		String signedData = request.getParameter("signedData");
		
		HttpSession session = ((HttpServletRequest)request).getSession();
		
		if(loginType!=null&&loginType.equals("1")){
			
			
			//String signedData = request.getParameter("signedData");
			
			String username = getUsername(request);
			String password = "certLogin";
		
			boolean rememberMe = isRememberMe(request);
			String host = getHost(request);
			String captcha = getCaptcha(request);
			
			return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha,session,signedData,loginType);
		}
		String username = getUsername(request);
		String password = getPassword(request);
		if (password==null){
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String captcha = getCaptcha(request);
		
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
	}

	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
            ServletRequest request, ServletResponse response) {
		request.setAttribute(getFailureKeyAttribute(), e.getMessage());
		//login failed, let request continue back to the login page:
		return true;
	}
}