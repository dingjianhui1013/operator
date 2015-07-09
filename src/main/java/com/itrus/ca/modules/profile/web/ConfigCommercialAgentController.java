/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;

/**
 * 代理商Controller
 * 
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configCommercialAgent")
public class ConfigCommercialAgentController extends BaseController {

	private LogUtil logUtil = new LogUtil();

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigCommercialAgentService configCommercialAgentService;

	@ModelAttribute
	public ConfigCommercialAgent get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return configCommercialAgentService.get(id);
		} else {
			return new ConfigCommercialAgent();
		}
	}

	/*
	 * 列表显示
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = { "list", "" })
	public String list(
			ConfigCommercialAgent configCommercialAgent,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime) {
		User user = UserUtils.getUser();
		Page<ConfigCommercialAgent> page = configCommercialAgentService.find(
				new Page<ConfigCommercialAgent>(request, response),
				configCommercialAgent);
		model.addAttribute("page", page);
		return "modules/profile/configCommercialAgentList";
	}

	/**
	 * 查看页面
	 * 
	 * @param configCommercialAgent
	 * @param model
	 * @return
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = "form")
	public String form(ConfigCommercialAgent configCommercialAgent, Model model) {
		model.addAttribute("configCommercialAgent", configCommercialAgent);
		List<ConfigApp> configApps = configAppService
				.findByAgentId(configCommercialAgent.getId());
		List<Office> offices = officeService
				.findByAgentId(configCommercialAgent.getId());
		model.addAttribute("configApps", configApps);
		model.addAttribute("offices", offices);
		return "modules/profile/configCommercialAgentForm";
	}

	/**
	 * 添加处理
	 * 
	 * @param timeStart
	 * @param timeEnd
	 * @param officeIds
	 * @param appIds
	 * @param configCommercialAgent
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = "save")
	public String save(String timeStart, String timeEnd, String officeIds,
			String appIds, ConfigCommercialAgent configCommercialAgent,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, configCommercialAgent)) {
			return form(configCommercialAgent, model);
		}
		try {
			configCommercialAgent.setAgentContractStart(Timestamp
					.valueOf(timeStart + " 00:00:00"));
			configCommercialAgent.setAgentContractEnd(Timestamp.valueOf(timeEnd
					+ " 00:00:00"));
			configCommercialAgent.setDel_flag(0);
			configCommercialAgentService.save(configCommercialAgent);
			// 添加代理商与网点的关联
			if (configCommercialAgent.getAgentType2()) {
				if (officeIds.length() == 0) {
					configCommercialAgent.setAgentType2(false);
				} else {
					for (String oid : officeIds.split(",")) {
						Office office = new Office();
						office.setId(new Long(oid));
						ConfigAgentOfficeRelation caor = new ConfigAgentOfficeRelation(
								office, configCommercialAgent);
						configAgentOfficeRelationService.save(caor);
					}
				}
			}
			// 添加代理商与业务的关联
			if (configCommercialAgent.getAgentType1()) {
				if (appIds.length() == 0) {
					configCommercialAgent.setAgentType1(false);
				} else {
					for (String aid : appIds.split(",")) {
						ConfigApp app = new ConfigApp();
						app.setId(new Long(aid));
						ConfigAgentAppRelation caar = new ConfigAgentAppRelation(
								app, configCommercialAgent);
						configAgentAppRelationService.save(caar);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			logUtil
					.saveSysLog("业务中心",
							"添加代理商'" + configCommercialAgent.getAgentName()
									+ "'失败", "");

			addMessage(redirectAttributes,
					"保存代理商'" + configCommercialAgent.getAgentName() + "'失败");
			return "redirect:" + Global.getAdminPath()
					+ "/profile/configCommercialAgent/list";
		}

		logUtil.saveSysLog("业务中心",
				"添加代理商'" + configCommercialAgent.getAgentName() + "'成功", "");

		addMessage(redirectAttributes,
				"保存代理商'" + configCommercialAgent.getAgentName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configCommercialAgent/list";
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		List<ConfigApp> configApps = configAppService
				.findByAgentId(id);
		if (configApps.size()>0) {
			List<WorkDealInfo> workDealInfos = workDealInfoService.selectByApp(configApps);
			for (int i = 0; i < workDealInfos.size(); i++) {
				if (workDealInfos.get(i).getWorkCertInfo().getNotafter()!=null) {
					if (workDealInfos.get(i).getWorkCertInfo().getNotafter().after( new Date())) {
						addMessage(redirectAttributes, "代理商下有正在使用的应用，代理商不能被删除");
						return "redirect:" + Global.getAdminPath()
								+ "/profile/configCommercialAgent/?repage";
					}
				}
			}
		}
		configAgentAppRelationService.delete(id);
		configAgentOfficeRelationService.delete(id);
		configCommercialAgentService.delete(id);

		/* logUtil.saveSysLog("业务配置", "删除id为"+id+"的代理商成功", null); */

		logUtil.saveSysLog("业务中心", "删除代理商成功", "");

		addMessage(redirectAttributes, "删除代理商成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configCommercialAgent/?repage";
	}

	/**
	 * 跳转添加页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping("insertFrom")
	public String insertFrom(Model model) {
		List<ConfigApp> apps = configAppService.findallAppsT();
		
		
		
		model.addAttribute("menuList",apps);
		List<Office> offices = officeService.findAllByidNotInRaletion();
		model.addAttribute("officeList", offices);
		return "modules/profile/configCommercialAgentInsert";
	}

	/**
	 * 跳转修改页面
	 * 
	 * @param configCommercialAgent
	 * @param model
	 * @return
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping("updateFrom")
	public String updateFrom(ConfigCommercialAgent configCommercialAgent,
			Model model) {
		model.addAttribute("configCommercialAgent", configCommercialAgent);
		List<ConfigApp> configApps = configAppService
				.findByAgentId(configCommercialAgent.getId());
		List<Office> offices = officeService
				.findByAgentId(configCommercialAgent.getId());
		model.addAttribute("menuList",
				configAppService.findAllByid(configCommercialAgent.getId()));
		List<Office> offices1 = officeService.findAllByid(configCommercialAgent
				.getId());
		model.addAttribute("officeList", offices1);
		String appids = "";
		String ofids = "";
		for (ConfigApp a : configApps) {
			appids += a.getId() + ",";
		}
		for (Office o : offices) {
			ofids += o.getId() + ",";
		}
		model.addAttribute("configApps", appids);
		model.addAttribute("offices", ofids);
		return "modules/profile/configCommercialAgentUpdate";
	}

	/**
	 * 修改处理
	 * 
	 * @param timeStart
	 * @param timeEnd
	 * @param officeIds
	 * @param appIds
	 * @param configCommercialAgent
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("update")
	public String update(String timeStart, String timeEnd, String officeIds,
			String appIds, ConfigCommercialAgent configCommercialAgent,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			configAgentOfficeRelationService.delete(configCommercialAgent
					.getId());
			configAgentAppRelationService.delete(configCommercialAgent.getId());
			configCommercialAgent.setAgentContractStart(Timestamp
					.valueOf(timeStart + " 00:00:00"));
			configCommercialAgent.setAgentContractEnd(Timestamp.valueOf(timeEnd
					+ " 00:00:00"));
			configCommercialAgentService.save(configCommercialAgent);
			// 添加代理商与网点的关联
			if (configCommercialAgent.getAgentType2()) {
				if (officeIds.length() == 0) {
					configCommercialAgent.setAgentType2(false);
				} else {
					for (String oid : officeIds.split(",")) {
						Office office = new Office();
						office.setId(new Long(oid));
						ConfigAgentOfficeRelation caor = new ConfigAgentOfficeRelation(
								office, configCommercialAgent);
						configAgentOfficeRelationService.save(caor);
					}
				}
			}
			// 添加代理商与业务的关联
			if (configCommercialAgent.getAgentType1()) {
				if (appIds.length() == 0) {
					configCommercialAgent.setAgentType1(false);
				} else {
					for (String aid : appIds.split(",")) {
						ConfigApp app = new ConfigApp();
						app.setId(new Long(aid));
						ConfigAgentAppRelation caar = new ConfigAgentAppRelation(
								app, configCommercialAgent);
						configAgentAppRelationService.save(caar);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logUtil
					.saveSysLog("业务中心",
							"修改代理商'" + configCommercialAgent.getAgentName()
									+ "'失败", "");
			return "redirect:" + Global.getAdminPath()
					+ "/profile/configCommercialAgent/list";
		}
		logUtil.saveSysLog("业务中心",
				"修改代理商'" + configCommercialAgent.getAgentName() + "'成功", "");
		addMessage(redirectAttributes,
				"修改代理商'" + configCommercialAgent.getAgentName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/profile/configCommercialAgent/list";
	}

	/**
	 * 将字符串转换成date时间类型
	 * 
	 * @return
	 */
	private Date getDateByString(String timeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
