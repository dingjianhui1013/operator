package com.itrus.ca.modules.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 提供i信服务
 * 
 * @author ZhangJingtao test url:
 * 19E84C221A697E911F157127BE37FD53F8950A00
 *        http://localhost/operator/cert/1238FB5A0E9EF57B4DAA4B51F67A60120F785F25/project
http://localhost/operator/cert/2780422128C2D23B6914AC425BF142A36E59B663/project


http://localhost/operator/key/TWD0903103100085/project?include=profile

http://localhost/operator/key/TWD0903103100086/project?include=profile


 *         http://localhost/operator/cert/2780422128C2D23B6914AC425BF142A36E59B663
 *         /device
 * 
 * 
 */
@Controller
@RequestMapping("/")
public class ItrustController {
	Logger log = Logger.getLogger(ItrustController.class);

	@Autowired
	ItrustService itrustService;

	@RequestMapping(value = "/{id}")
	@ResponseBody
	public String test(@PathVariable(value = "id") Long id) {
		log.info("service is available...");
		JSONObject json = new JSONObject();
		return json.toString();
	}

	/**
	 * 根据证书序列号查询证书的应用(project)信息
	 * 
	 * @param certsn
	 * @return 应用，产品(包含证书是否是通用)， 通用? 返回所有支持通用应用列表
	 * @throws JSONException
	 */
	@RequestMapping(value = "/cert/{sn}/project")
//	@ResponseBody
	public void getCertProjectByCert(
			@PathVariable(value = "sn") String certsn,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String include)
			throws JSONException,IOException {
		response.setStatus(response.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		JSONObject json = new JSONObject();
		try {
			json.put("status", 0); 
			certsn = certsn.toUpperCase();
			log.info("certSn:"+certsn);
			log.info("IP:"+request.getRemoteAddr());
			Enumeration e = request.getHeaderNames();
		    while(e.hasMoreElements()){
		        String k = e.nextElement().toString();
		        String v = request.getHeader(k);     
		        log.info("header - key: " + k + " | value: " + v);
		    }
			WorkDealInfo wdi = itrustService.findBySn(certsn, 0);
			if (wdi != null) {
				if (wdi.getWorkCertInfo().getNotafter().before(new Date())) {// 验证有效期
					json.put("status", 0);
					json.put("msg", "超过有效期");
				} else {
					json.put("project", appToJson(wdi.getConfigApp()));// 应用
					json.put("product", productToJson(wdi.getConfigProduct()));// 产品
					
					
					
					if (wdi.getConfigProduct().getProductLabel() != null
							&& wdi.getConfigProduct().getProductLabel()==0) {
						json.put("commonProjects",
								itrustService.listCommonApp());// 不返回po 只返回应用的id
					}
					
					
					if ("profile".equals(include)) {
					//	if (wdi.getConfigProduct().getConfigRaAccounts().size() != 0) {
							ConfigRaAccount raAccount = itrustService.findRaById(wdi.getConfigProduct().getRaAccountId());
									//itrustService.findByAppId(wdi.getConfigApp().getId());
							JSONObject ra = new JSONObject();
							ra.put("accountOrganization",
									raAccount.getAccountOrganization());
							ra.put("accountOrgUnit",
									raAccount.getAccountOrgUnit());
							ra.put("raProtocol", raAccount.getRaProtocol());
							ra.put("serviceUrl", raAccount.getMobileDeviceUrl());
							ra.put("id", raAccount.getId());
							ra.put("accountHash", raAccount.getAccountHash());
							
							
							log.debug(wdi.getWorkCertInfo().getTrustDeviceCount());
							
							
							json.put("trustDeviceCount", wdi.getWorkCertInfo()
									.getTrustDeviceCount() == null ? 0 : wdi
									.getWorkCertInfo().getTrustDeviceCount());
							if (wdi.getWorkCertInfo().getTrustDeviceDate()!=null) {
								json.put("trustDeviceDate", wdi.getWorkCertInfo().getTrustDeviceDate().getTime());
							}else {
								json.put("trustDeviceDate", 0);
							}
							json.put("ra", ra);
//						} else {
//							json.put("ra", "");
//						}
					}
					json.put("status", 1);
				}
			} else {
				json.put("msg", "未查询到相关信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
		}
		log.info("result:"+json.toString());
//		return json.toString();
		response.getWriter().println(json.toString());
		response.flushBuffer();
	}

	/**
	 * 根据key序列号查询
	 * 
	 * 
	 * I信应用导航功能使用
	 * @param keysn
	 * @return 应用，产品(包含证书是否是通用)， 通用? 返回所有支持通用应用列表
	 * @throws JSONException
	 */
	@RequestMapping(value = "/key/{sn}/project")
//	@ResponseBody
	public void getCertProjectByKey(@PathVariable(value = "sn") String keysn,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String include)
			throws JSONException,IOException {
		response.setStatus(response.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		JSONObject json = new JSONObject();
		try {
			json.put("status", 0);
		/*	log.info("keySn:"+keysn);
			
			
			log.info("IP:"+getIp(request));
			*/
				
			
			 Enumeration e = request.getHeaderNames();
			    while(e.hasMoreElements()){
			        String k = e.nextElement().toString();
			        String v = request.getHeader(k);     
			       /* log.info("header - key: " + k + " | value: " + v);*/
			    }
			WorkDealInfo wdi = itrustService.findBySn(keysn, 1);
			if (wdi != null) {
				if (wdi.getWorkCertInfo().getNotafter().before(new Date())) {
					json.put("status", 0);
					json.put("msg", "超过有效期");
				} else {
					json.put("project", appToJson(wdi.getConfigApp()));
					json.put("product", productToJson(wdi.getConfigProduct()));
					
					List<Long> ids = new ArrayList<Long>();
					ids.add(wdi.getConfigApp().getId());
					
					json.put("commonProjects", ids);
					
					
					/*if (wdi.getConfigProduct().getProductLabel() != null
							&& wdi.getConfigProduct().getProductLabel()==0) {
						json.put("commonProjects",
								itrustService.listCommonApp());
					}*/
					if ("profile".equals(include)) {
						if (wdi.getConfigProduct().getRaAccountId()!=null) {
							ConfigRaAccount raAccount = itrustService.findRaById(wdi.getConfigProduct().getRaAccountId());
//							ConfigRaAccount raAccount = itrustService.findByAppId(wdi.getConfigApp().getId());
							JSONObject ra = new JSONObject();
							ra.put("accountOrganization",
									raAccount.getAccountOrganization());
							ra.put("accountOrgUnit",
									raAccount.getAccountOrgUnit());
							ra.put("raProtocol", raAccount.getRaProtocol());
							ra.put("serviceUrl", raAccount.getMobileDeviceUrl());
							ra.put("id", raAccount.getId());
							ra.put("accountHash", raAccount.getAccountHash());
							json.put("trustDeviceCount", wdi.getWorkCertInfo()
									.getTrustDeviceCount() == null ? 0 : wdi
									.getWorkCertInfo().getTrustDeviceCount());
							if (wdi.getWorkCertInfo().getTrustDeviceDate()!=null) {
								json.put("trustDeviceDate", wdi.getWorkCertInfo().getTrustDeviceDate().getTime());
							}else {
								json.put("trustDeviceDate", 0);
							}
							json.put("ra", ra);
						} else {
							json.put("ra", "");
						}
					}

					json.put("status", 1);
				}
			} else {
				json.put("msg", "未查询到相关信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
		}
		/*log.info("result:"+json.toString());*/
//		return json.toString();
		response.getWriter().println(json.toString());
		response.flushBuffer();
	}

	/**
	 * 
	 * 根据应用id查询移动设备证书的RA配置
	 * 
	 * @return 返回 O OU 方式 服务地址 哈希值 id
	 * @throws JSONException
	 */

	@RequestMapping(value = "/profiles/{id}")
//	@ResponseBody
	public void findRaProfile(@PathVariable(value = "id") Long raId,
			HttpServletResponse response)
			throws JSONException,IOException {
		response.setStatus(response.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		JSONObject json = new JSONObject();
		try {
			json.put("status", 0);
			ConfigRaAccount raAccount = itrustService.findRaById(raId);
			log.info("raId:"+raId);
			if (raAccount != null) {
				json.put("accountOrganization",
						raAccount.getAccountOrganization());
				json.put("accountOrgUnit", raAccount.getAccountOrgUnit());
				json.put("raProtocol", raAccount.getRaProtocol());
				json.put("serviceUrl", raAccount.getServiceUrl());
				json.put("id", raAccount.getId());
				json.put("accountHash", raAccount.getAccountHash());

				json.put("status", 1);
			} else {
				json.put("msg", "未查询到相关信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
		}
//		return json.toString();
		response.getWriter().println(json.toString());
		response.flushBuffer();
	}

	/**
	 * 根据证书信息查询可信设备数量
	 * 
	 * @return 返回当前证书可信设备数量，时间，上一张证书 可信设备数量(可能为空)，时间(可能为空)
	 * @throws JSONException
	 */
	@RequestMapping(value = "/cert/{sn}/device")
//	@ResponseBody
	public void getCertTrustDevice(@PathVariable(value = "sn") String sn,
			HttpServletResponse response)
			throws JSONException,IOException {
		response.setStatus(response.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		JSONObject json = new JSONObject();
		try {
			sn = sn.toUpperCase();
			log.info("certSn:"+sn);
			json.put("status", 0);
			WorkCertInfo cert = itrustService.getTrustDeviceCount(sn);
			if (cert != null) {
				json.put(
						"trustDeviceCount",
						cert.getTrustDeviceCount() == null ? 0 : cert
								.getTrustDeviceCount());
				if (cert.getTrustDeviceDate()!=null) {
					json.put("trustDeviceDate", cert.getTrustDeviceDate().getTime());
				}else {
					json.put("trustDeviceDate", 0);
				}// json.put("lastTrustDeviceCount",
				// cert.getLastTrustDeviceCount()==null?
				// 0:cert.getLastTrustDeviceCount());
				// json.put("lastTrustDeviceDate",
				// cert.getLastTrustDeviceDate());
				json.put("status", 1);
			} else {
				json.put("msg", "未查询到相关信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
		}
		log.info("result:"+json.toString());
//		return json.toString();
		response.getWriter().println(json.toString());
		response.flushBuffer();
	}

	public JSONObject appToJson(ConfigApp app) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", app.getId());
			json.put("name", app.getAppName());
			json.put("supportCommon", app.getSupportCommon());
		} catch (Exception e) {
			//TODO: handle exception
		}
		return json;
	}

	public JSONObject productToJson(ConfigProduct product) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", product.getId());
			json.put("name", ProductType.getProductTypeName(Integer
					.valueOf(product.getProductName())));
			json.put("label", product.getProductLabel());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}
	
	
	
	
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		return ip;
	}

	
	
}
