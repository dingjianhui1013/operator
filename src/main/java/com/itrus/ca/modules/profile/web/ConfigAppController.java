/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigProjectType;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigProjectTypeService;
import com.itrus.ca.modules.sys.entity.CommonAttach;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.CommonAttachService;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 应用Controller
 * 
 * @author HuHao
 * @version 2014-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configApp")
public class ConfigAppController extends BaseController {

	@Autowired
	private ConfigAppService configAppService;
	@Autowired
	private CommonAttachService attachService;
	@Autowired
	private ConfigProductService configProductService;
	
	
	@Autowired
	private ConfigProjectTypeService configProjectTypeService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	@Autowired
	private OfficeService officeService;

	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public ConfigApp get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return configAppService.get(id);
		} else {
			return new ConfigApp();
		}
	}

	private String realPath;
	private String uploadRoot;
	private String userPath;

	@RequiresPermissions("profile:configApp:view")
	@RequestMapping(value = { "list", "" })
	public String list(ConfigApp configApp, HttpServletRequest request,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "office", required = false) Long office,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			configApp.setCreateBy(user);
		}
		
		List<ConfigAppOfficeRelation> page2 =Lists.newArrayList();
		if (area!=null) {
			 page2 = configAppOfficeRelationService.findByArea(area,office); // 获取应用信息
		}
		
		List<Long> appIdList =Lists.newArrayList();
		for (int i = 0; i < page2.size(); i++) {
			appIdList.add(page2.get(i).getConfigApp().getId());
		}

		
		
		Page<ConfigApp> page = configAppService.find(new Page<ConfigApp>(
				request, response), configApp,appIdList,area); // 获取应用信息
		
		for (int i = 0; i < page.getList().size(); i++) {
			Long appId = page.getList().get(i).getId();

			ConfigProduct configProduct = new ConfigProduct();
			configProduct.setConfigApp(page.getList().get(i));
			List<ConfigProduct> pros = configProductService
					.findByAppId(configProduct);// 获取应用下的产品信息
			if (pros.size() > 0) {
				String proName = "";
				for (int j = 0; j < pros.size(); j++) {
					ProductType productType = new ProductType();
					proName += productType.getProductTypeName(Integer
							.valueOf(pros.get(j).getProductName()));

					String proLabel = "";
					if (pros.get(j).getProductLabel() == 1) {
						proLabel = "(专用)";
						proName += proLabel;
					} else {
						proLabel = "(通用)";
						proName += proLabel;
					}

					if (j != pros.size() - 1) {
						proName += ";";
					}
				}
				page.getList().get(i).setProductName(proName);
			}
		}
		List<Office> offsList = officeService.getOfficeByType(user, 1);
		
		if (area!=null) {
    		model.addAttribute("areaId",area);
    		
    		List<Office> offs = officeService.findByParentId(area);
    		model.addAttribute("offices",offs);
    		if (office!=null) {
    			model.addAttribute("office",office);
    		}
    	}
		model.addAttribute("offsList", offsList);
		model.addAttribute("page", page);
		return "modules/profile/configAppList";
	}

	@RequiresPermissions("profile:configApp:view")
	@RequestMapping(value = "form")
	public String form(ConfigApp configApp, Model model) {
		model.addAttribute("configApp", configApp);
		
		List<ConfigProjectType> proJectTypes = configProjectTypeService.findProjectTypeList();
		
		
		model.addAttribute("proJectTypes",proJectTypes);
		
		
		return "modules/profile/configAppForm";
	}

	@RequiresPermissions("profile:configApp:edit")
	@RequestMapping(value = "save")
	public String save(ConfigApp configApp, Model model,
			RedirectAttributes redirectAttributes , Long configProjectTypeId ) {
		if (!beanValidator(model, configApp)) {
			return form(configApp, model);
		}
		if (configApp.getId() == null) {
			if (configAppService.findAppByName(configApp.getAppName()) == false) {
				logUtil.saveSysLog("业务配置", "保存应用" + configApp.getAppName()
						+ "失败", null);
				addMessage(redirectAttributes, "保存应用失败,应用名称重复");
				return "redirect:" + Global.getAdminPath()
						+ "/profile/configApp/?repage";
			}
		}
		String detail = "";
		if (configApp.getId() == null) {
			detail = "添加应用" + configApp.getAppName() + "成功";
		} else {
			detail = "更新应用" + configApp.getAppName() + "成功";
		}
		
	
		ConfigProjectType proType =  configProjectTypeService.get(configProjectTypeId);
		configApp.setConfigProjectType(proType);
	
		
		
		
		configAppService.save(configApp);
		logUtil.saveSysLog("业务配置", detail, null);
		addMessage(redirectAttributes, "保存应用'" + configApp.getAppName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configApp/?repage";
	}

	@RequiresPermissions("profile:configApp:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configAppService.delete(id);
		logUtil.saveSysLog("业务配置", "删除id为" + id + "的应用成功", null);
		addMessage(redirectAttributes, "删除应用成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configApp/?repage";
	}

	@RequestMapping("addAttach")
	@ResponseBody
	public String importFile(
			@RequestParam(value = "fileName", required = true) MultipartFile file,
			@RequestParam(value = "uploadFileId", required = false) String uploadFileId)
			throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		List<String> fileName = new ArrayList<String>();
		try {
			if (uploadFileId == null || uploadFileId.equals("")) {
				uploadFileId = UUID.randomUUID().toString();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			User user = UserUtils.getUser();
			String attachName = UUID.randomUUID().toString().toLowerCase();
			String path = sdf.format(new Date());
			String originalFileName = file.getOriginalFilename();
			String suffix = (String) originalFileName.subSequence(
					originalFileName.lastIndexOf("."),
					originalFileName.length());
			if (new File(realPath + "/" + path).exists()) {
				new File(realPath + "/" + path).mkdirs();
			}
			CommonAttach attach = new CommonAttach(attachName, null,
					file.getOriginalFilename(), uploadRoot + "/" + path,
					realPath + "/" + path, "", 1, suffix, "1", 1,
					user.getName(), user.getOffice().getName(),
					Integer.valueOf(user.getOffice().getId().toString()),
					Integer.valueOf(user.getId().toString()), new Timestamp(
							System.currentTimeMillis()), uploadFileId);
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
					realPath + "/" + path, attach.getAttachName() + suffix));
			attachService.saveAttach(attach);
			fileName.add(originalFileName);

			json.put("attach", attach.getId());
			json.put("uploadFileId", uploadFileId);
			json.put("status", "1");
			json.put("fileName", fileName.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
			json.put("errorMsg", "系统错误");
		}
		return json.toString();
	}
	
//	@RequestMapping("addAttach")
//	@ResponseBody
//	public String findAreaByAppId(Long appId) {
//		
//		JSONObject json = new org.json.JSONObject();
//		JSONArray array = new JSONArray();
//		try {
//			List<Area> offids = officeService.findByParentId(areaId);
//			for (Office office : offids) {
//				if (office.getType().equals("1")) {
//					continue;
//				}
//				json = new JSONObject();
//				json.put("id", office.getId());
//				json.put("name", office.getName());
//				array.put(json);
//			}
//			// 检查是否有权限操作
//		} catch (Exception e) {
//			e.printStackTrace();
//			json.put("status", "0");
//		}
//		return array.toString();
//		
//	}

	public String getRealPath() {
		return realPath;
	}

	@Value(value = "${uploadFile.path}")
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getUploadRoot() {
		return uploadRoot;
	}

	@Value(value = "${uploadFile.webroot}")
	public void setUploadRoot(String uploadRoot) {
		this.uploadRoot = uploadRoot;
	}

	public String getUserPath() {
		return userPath;
	}

	@Value(value = "${uploadFile.userPath}")
	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}
	
	@RequiresPermissions("profile:configApp:view")
	@RequestMapping(value = "findAllConfigApp")
	public  String findAllConfigApp(Model model){
		 List<ConfigApp> list = configAppService.findAllConfigApp();
		 model.addAttribute("list", list);
		 return "modules/profile/configAppSet";
	}
	
	@RequestMapping(value = "saveApplyFlag")
	@ResponseBody
	public String saveApplyFlag(String selectApplyFlag1,String selectApplyFlag2, Model model) {
		JSONObject json = new JSONObject();
		try {
			List<ConfigApp> list = configAppService.findAllConfigApp();
			for (int i = 0; i < list.size(); i++) {
				ConfigApp configApp = list.get(i);
				configApp.setApplyFlag1(false);
				configApp.setApplyFlag2(false);
				configAppService.save(configApp);
			}
			if (StringUtils.isNotEmpty(selectApplyFlag1)) {
				String[] ApplyFlag1 = selectApplyFlag1.split(",");
				for (int i = 0; i < ApplyFlag1.length; i++) {
					ConfigApp configApp = configAppService.get(Long
							.parseLong(ApplyFlag1[i]));
					configApp.setApplyFlag1(true);
					configAppService.save(configApp);
				}
			}
			if (StringUtils.isNotEmpty(selectApplyFlag2)) {
				String[] ApplyFlag2 = selectApplyFlag2.split(",");
				for (int i = 0; i < ApplyFlag2.length; i++) {
					ConfigApp configApp = configAppService.get(Long
							.parseLong(ApplyFlag2[i]));
					configApp.setApplyFlag2(true);
					configAppService.save(configApp);
				}
			}
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				json.put("status", "0");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();

	}

}
