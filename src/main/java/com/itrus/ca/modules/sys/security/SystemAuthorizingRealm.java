/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.itrus.ca.common.servlet.ValidateCodeServlet;
import com.itrus.ca.common.utils.CacheUtils;
import com.itrus.ca.common.utils.Encodes;
import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.sys.entity.Menu;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.SingleCvm;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.sys.web.LoginController;

import cn.topca.sp.cvm.CVM;
import cn.topca.sp.svm.SVM;
import cn.topca.sp.x509.X509Certificate;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
@DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {

	//@Autowired
	//private SysCrlContextService sysCrlContextService;

	private SystemService systemService;

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		if(token.getLoginType()!=null&&token.getLoginType().equals("1")){
			/*HttpSession session = token.getSession();
			 if(session==null){
				 throw new CaptchaException("session过期,请重新登录");
			 }*/
			
			try {
				
				String randomString = (String)CacheUtils.get("randomString");
				//String randomString = (String)session.getAttribute("randomString");
				if(randomString==null){
					 throw new CaptchaException("session过期,请重新登录");
				}
				
				String toSign = "LOGONDATA:"+randomString;
				
				byte[] signedDate = Base64.decodeBase64(token.getSignedData());
				X509Certificate certificate = SVM.verifyPKCS7SignedData(signedDate, toSign.getBytes());
			
				User user = getSystemService().getUserByLoginName(certificate.getCertSubjectNames().getItem("CN"));
				if (user==null) {
					throw new CaptchaException("用户不存在！");
				}
				if(user.getLoginType()==null||!user.getLoginType().equals("1")){
					throw new CaptchaException("用户无法进行证书登录，请检查登录类型！");
					//return null;
				}	
				if (user.getOffice().getDelFlag().equals(Office.DEL_FLAG_DELETE)) {
					throw new CaptchaException("用户所在网点已删除！");
					//return null;
				}
				
				CVM cvm = SingleCvm.getInstance().getCVM();
				int status = cvm.verifyCertificate(certificate);
			
				if(status!=0){
					if(status==1){
						throw new CaptchaException("证书已过期！");
					}
					if(status==-1){
						throw new CaptchaException("严重系统错误,CVM初始化失败,请检查配置文件和日志！");
					}
					if(status==2){
						throw new CaptchaException("证书已吊销！");
					}
					
					if(status==3){
						throw new CaptchaException("不支持的颁发者=["+certificate.getCertSubjectNames()+"]");
					}
					if(status==4){
						throw new CaptchaException("验证CA签名失败,疑是伪造证书！");
					}
					if(status==5){
						throw new CaptchaException("无法获取CRL,请检查配置文件和网络！");
					}
					
					throw new CaptchaException("认证证书失败！");
				}

				
				String password = SystemService.entryptPassword("certLogin");
				
				byte[] salt = Encodes.decodeHex(password.substring(0,16));
				return new SimpleAuthenticationInfo(new Principal(user), 
						password.substring(16), ByteSource.Util.bytes(salt), getName());
				
			} catch (Exception e) {
				throw new CaptchaException("证书登录错误！:" + e.getMessage());
			}
			
			
		}
		else{
			if (token.getUsername().length()<30&&LoginController.isValidateCodeLogin(token.getUsername(), false, false)){
				// 判断验证码
				Session session = SecurityUtils.getSubject().getSession();
				String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
				if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
					throw new CaptchaException("验证码错误.");
				}
			}
			
			
	
			User user = getSystemService().getUserByLoginName(token.getUsername());
			if (user==null){
				return null;
			} 
			if(user.getLoginType()==null||!user.getLoginType().equals("0")){
				throw new LoginTypeException("登录类型错误.");
			}	
			if (user.getOffice().getDelFlag().equals(Office.DEL_FLAG_DELETE)) {
				return null;
			}
			
			
			byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user), 
					user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
		}
		
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list){
				if (StringUtils.isNotBlank(menu.getPermission())){
					// 添加基于Permission的权限信息
					info.addStringPermission(menu.getPermission());
				}
			}			
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private Long id;
		private String loginName;
		private String name;
		private Map<String, Object> cacheMap;

		public Principal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
		}

		public Long getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

	}
}
