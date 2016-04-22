/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.SingleCvm;
import com.itrus.ca.modules.sys.service.SystemService;

import cn.topca.sp.cvm.CVM;
import cn.topca.sp.svm.SVM;

import cn.topca.sp.x509.X509Certificate;

/**
 * 证书登录Controller
 * @author CYC
 * @version 2016-4-22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys")
public class CertLoginController extends BaseController{
	Logger log = Logger.getLogger(LoginController.class);
	@Autowired
	SystemService systemService;
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "/certLogin", method = RequestMethod.POST)
	public String certLogin(HttpServletRequest request, HttpServletResponse response,String signedData, Model model) {
	
	/*	HttpSession session = request.getSession();
		try {
			String randomString = (String)session.getAttribute("randomString");
			if(randomString==null){
				request.setAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, "session过期！,请重新登录！");
				return  "/modules/sys/sysLogin";
			}
			
			String toSign = "LOGONDATA:"+randomString;
			byte[] signedDate = Base64.decodeBase64(signedData);
			//X509Certificate certificate = SVM.verifyPKCS7SignedData(signedDate, toSign.getBytes());
		
			
			
			CVM cvm = SingleCvm.getCvm();
			//int status = cvm.verifyCertificate(certificate);
			int status = 0;
			if(status!=0){
				return "redirect:"+Global.getAdminPath()+"/login";
			}
			
			
			
			//String userName = certificate.getCertSubjectNames().getItem("CN");
			String userName = "admin";
			
			String url = Global.getAdminPath()+"/login?username="+userName+"&password=certLogin";
			
			return "redirect:"+url;*/
			
		/*} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		
		
				
		return "modules/sys/sysLogin";
	}
}

