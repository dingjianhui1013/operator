package com.itrus.ca.modules.profile.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.key.web.KeyUsbKeyController;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.JsonUtils;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.cert.X509Certificate;
import com.itrus.util.Base64;
import com.itrus.util.CipherUtils;

/**
 * RA配置Controller
 * 
 * @author ZhangJingtao
 * @version 2014-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configRaAccount")
public class ConfigRaAccountController extends BaseController {

	static Log log = LogFactory.getLog(ConfigRaAccountController.class);
	@Autowired
	private ConfigRaAccountService configRaAccountService;

	@Autowired
	private ConfigProductService productService;
	
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public ConfigRaAccount get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return configRaAccountService.get(id);
		} else {
			return new ConfigRaAccount();
		}
	}

	@RequiresPermissions("profile:configRaAccount:view")
	@RequestMapping(value = { "list", "" })
	public String list(ConfigRaAccount configRaAccount,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		Page<ConfigRaAccount> page = configRaAccountService.find(
				new Page<ConfigRaAccount>(request, response), configRaAccount);
		model.addAttribute("page", page);
		return "modules/profile/configRaAccountList";
	}
	
	@RequiresPermissions("profile:configRaAccount:view")
	@RequestMapping(value = "bindList")
	public String bindList(ConfigRaAccount configRaAccount,Long configProductId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		Page<ConfigRaAccount> page = configRaAccountService.find(
				new Page<ConfigRaAccount>(request, response), configRaAccount);
		model.addAttribute("page", page);
		model.addAttribute("configProductId",configProductId);
		return "modules/profile/configRaAccountBindList";
	}

	@RequiresPermissions("profile:configRaAccount:view")
	@RequestMapping(value = "form")
	public String form(ConfigRaAccount configRaAccount, Model model) {
		model.addAttribute("configRaAccount", configRaAccount);
		
		/*if(configRaAccount.getId()!=null&&configRaAccount.getKeyLen()!=null){
			model.addAttribute("canModify", configRaAccountService.checkUsedByDeal(configRaAccount.getId()));
		}*/
		
		return "modules/profile/configRaAccountForm";
	}

	@RequestMapping(value = "alistModel")
	public ModelAndView alistConsumerModel(
			@RequestParam(required = false) Long id, Long productId,
			Long appId, Model model) {
		ModelAndView mav = new ModelAndView(
				"modules/profile/configRaAccountForm");
		model.addAttribute("productId", productId);
		model.addAttribute("appId", appId);
		return mav;
	}

	@RequestMapping(value = "listModel")
	public String listConsumerModel(ConfigRaAccount configRaAccount,
			@RequestParam(required = false) Long id, Long productId, Model model) {
		configRaAccount.setId(id);
		model.addAttribute("productId", productId);
		model.addAttribute("configRaAccount", configRaAccount);
		return "modules/profile/configRaAccountForm";
	}

	// 添加device证书
	@RequestMapping("addDeviceCert")
	@ResponseBody
	public String importDeviceCertFile(
			@RequestParam(value = "deviceCert1File", required = true) MultipartFile deviceCertFile,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		try {
			if (deviceCertFile != null) {
				if (deviceCertFile.getSize() > 0) {
					byte[] deviceCert = deviceCertFile.getBytes();
					String s3 = new String(deviceCert);
					try {
						new Base64().decode(s3.getBytes());
						X509Certificate cert = X509Certificate.getInstance(s3
								.getBytes());
					} catch (Exception e) {
						// TODO: handle exception
						json.put("caEception", "ca");
						JsonUtils.writeJson4AjaxSubmit(response,
								json.toString());
						return json.toString();
					}
					json.put("deviceCert", s3);
					json.put("status", "1");
					return json.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
			json.put("errorMsg", "系统错误");
		}
		return json.toString();
	}

	// 添加caDevice证书
	@RequestMapping("addCaCert")
	@ResponseBody
	public String importCaDeviceCertFile(
			@RequestParam(value = "caCert1File", required = true) MultipartFile caCertFile,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		try {
			if (caCertFile != null) {
				if (caCertFile.getSize() > 0) {
					byte[] raDeviceCert = caCertFile.getBytes();
					String s3 = new String(raDeviceCert);
					try {
						new Base64().decode(s3.getBytes());
						X509Certificate cert = X509Certificate.getInstance(s3
								.getBytes());
					} catch (Exception e) {
						// TODO: handle exception
						json.put("caCert", "caCert");
						JsonUtils.writeJson4AjaxSubmit(response,
								json.toString());
						return json.toString();
					}
					json.put("caCertFile", s3);
					json.put("status", "1");
					return json.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
			json.put("errorMsg", "系统错误");
		}
		return json.toString();
	}

	// 添加raDeviceCert证书
	@RequestMapping("addRaDeviceCert")
	@ResponseBody
	public String importRaDeviceCertFile(
			@RequestParam(value = "raDeviceCert1File", required = true) MultipartFile raDeviceCertFile,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		try {
			if (raDeviceCertFile != null) {
				if (raDeviceCertFile.getSize() > 0) {
					byte[] raDeviceCert = raDeviceCertFile.getBytes();
					String s3 = new String(raDeviceCert);
					try {
						new Base64().decode(s3.getBytes());
						X509Certificate cert = X509Certificate.getInstance(s3
								.getBytes());
						String org = cert.getSubjectNames().getItem("O");
						String orgUnit = cert.getSubjectNames().getItem("OU");

						String accountHash = CipherUtils.md5((orgUnit + org
								.toUpperCase().getBytes("GBK")));

						// CN=管理员CA,OU=测试部试用,O=天威诚信试用
						json.put("accountHash", "accountHash");
						json.put("OU", org);
						json.put("O", orgUnit);
					} catch (Exception e) {
						// TODO: handle exception
						json.put("raDevice", "raDevice");
						JsonUtils.writeJson4AjaxSubmit(response,
								json.toString());
						return json.toString();
					}
					json.put("raDeviceCert", s3);
					json.put("status", "1");
					return json.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
			json.put("errorMsg", "系统错误");
		}
		return json.toString();
	}

	@RequiresPermissions("profile:configRaAccount:edit")
	@RequestMapping(value = "save")
	//@ResponseBody
	public String save(ConfigRaAccount configRaAccount, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		if (!beanValidator(model, configRaAccount)) {
			return form(configRaAccount, model);
		}
		JSONObject json = new JSONObject();
		// Page<ConfigRaAccount> page = configRaAccountService.findByRaName(
		// new Page<ConfigRaAccount>(request, response), configRaAccount);
		// if (page.getList().size() > 0) {
		// json.put("status", "2");
		// addMessage(redirectAttributes, "RA名称重复，添加失败！");
		// return json.toString();
		// }
		try {
			String accountHash = CipherUtils.md5(
					(configRaAccount.getAccountOrganization() + configRaAccount
							.getAccountOrgUnit()).getBytes("GBK"))
					.toUpperCase();
			configRaAccount.setAccountHash(accountHash);
			List<ConfigRaAccount> raAccounts = configRaAccountService.findAll();
			if(raAccounts.size()!=0){
//				if(raAccounts.get(0).getConfigRaAccountExtendInfo()!=null){
//					configRaAccount.setConfigRaAccountExtendInfo(raAccounts.get(0).getConfigRaAccountExtendInfo());
//				}
			}
			configRaAccountService.save(configRaAccount);
//			ConfigProduct product = productService.get(configRaAccount.getConfigProduct().getId());
//
//			product.setRaAccountId(configRaAccount.getId());
//			productService.save(product);
			logUtil.saveSysLog("业务配置", "保存RA模板配置"+configRaAccount.getRaName()+"成功", null);
			addMessage(redirectAttributes, "保存RA模板配置成功");
			//json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.saveSysLog("业务配置", "保存RA模板配置"+configRaAccount.getRaName()+"失败，配置异常", null);
			addMessage(redirectAttributes, "保存RA模板配置失败，配置异常");
			//json.put("status", "0");
		}
		//return json.toString();
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configRaAccount/?repage";
	}

	@RequiresPermissions("profile:configRaAccount:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configRaAccountService.delete(id);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"RA配置成功", null);
		addMessage(redirectAttributes, "删除RA配置成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configRaAccount";
	}

	/**
	 *  校验RA模板
	 * @param raId
	 * @param
	 * @return
	 */
	@RequestMapping(value="checkRa")
	@ResponseBody
	public String checkRa(Long raId ,HttpServletRequest request, HttpServletResponse response ){
		JSONObject jsonObject = new JSONObject();
		try{
			String raName = request.getParameter("raName");
			List<ConfigRaAccount> list = configRaAccountService.findByRaName(raName);
			if(list.size() != 0 ){
				if(raId == null ){
					jsonObject.put("status","0");
					jsonObject.put("msg","RA模板名称已存在,添加失败");
					return jsonObject.toString();
				}else{
					ConfigRaAccount configRaAccount = list.get(0);
					if(raId.longValue() != configRaAccount.getId().longValue()){//更新重名
						jsonObject.put("status","0");
						jsonObject.put("msg","RA模板名称已存在,更新失败");
						return jsonObject.toString();
					}else{
						jsonObject.put("status","2");
						return jsonObject.toString();

					}
//					if(configRaAccountService.checkUsed(raId)){
//						jsonObject.put("status","1");
//						jsonObject.put("msg","该RA模版已被使用，是否继续进行修改？");
//						return jsonObject.toString();
//					}
				}
			}else{
				jsonObject.put("status","2");
				return jsonObject.toString();
			}

		}catch(Exception e){

			e.printStackTrace();
		}

		return null;
	}

	/**
	 *  检测RA模板是否已绑定
	 * @param raId
	 * @return
	 */
	@RequestMapping(value = "checkUsed")
	@ResponseBody
	public String checkUsed(Long raId){
		JSONObject jsonObject = new JSONObject();
		try{
			if(configRaAccountService.checkUsed(raId)){
				jsonObject.put("status","0");
				jsonObject.put("msg","该RA模版已被使用，请取消绑定使用后再进行删除。");
			}else{
				jsonObject.put("status","1");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	/**
	 *
	 * @param raId
	 * @return
	 */
	@RequestMapping(value = "checkUsedByModify")
	@ResponseBody
	public String checkByModify(Long raId ){
		JSONObject jsonObject = new JSONObject();
		try{
			if(configRaAccountService.checkUsed(raId)){
				jsonObject.put("status","0");
				jsonObject.put("msg","该RA模版已被使用，是否继续进行修改？");
			}else{
				jsonObject.put("status","1");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	/**
	 * 测试RA是否可用
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "testPing")
	@ResponseBody
	public String testPing(Long id) throws Exception {
		JSONObject jsonObject = new JSONObject();
		ConfigRaAccount configRaAccount = configRaAccountService.get(id);
		String url = configRaAccount.getServiceUrl();
		String address = null;

		{
			try {
				String regIp  = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";//ip+端口
				String regDomainName = "(?<=//|)((\\w)+\\.)+\\w+";//域名
				Pattern pattern = Pattern.compile(regIp);
				Matcher matcher = pattern.matcher(url);
				if(matcher.find()){
					address = matcher.group();
				}else{
					pattern = Pattern.compile(regDomainName);
					matcher = pattern.matcher(url);
					if(matcher.find()){
						address = matcher.group();
					}else{
						jsonObject.put("status","0");
						jsonObject.put("msg","服务地址有误！");
						return jsonObject.toString();
					}
				}



				HttpURLConnection connection = (HttpURLConnection) new URL("http://"+address)
						.openConnection();
				connection.setConnectTimeout(3000);
				int state = connection.getResponseCode();
				if (state == 200) {
					jsonObject.put("status", "0");
					jsonObject.put("msg","服务地址可用！");
					return jsonObject.toString();
				}else{
					jsonObject.put("status","0");
					jsonObject.put("msg","服务地址不可用！");
					return jsonObject.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Runtime runtime = Runtime.getRuntime();
				Process process = runtime.exec("ping " + address);
				InputStream inputStream = process.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String line = null;
				Boolean flag = false;

				while ((line = bufferedReader.readLine()) != null){
					log.debug(line);
					if(line.contains("ttl") || line.contains("TTL")){
						flag = true;
						break;
					}
				}
				inputStream.close();
				inputStreamReader.close();
				bufferedReader.close();
				process.destroy();
				if(flag){
					log.debug(address + "连通");
					jsonObject.put("status","0");
					jsonObject.put("msg","服务地址可用！");
					return jsonObject.toString();
				}else{
					log.debug(address+"连不通");
					jsonObject.put("status","0");
					jsonObject.put("msg","服务地址不可用！");
					return jsonObject.toString();
				}

			}
		}
	}
}
