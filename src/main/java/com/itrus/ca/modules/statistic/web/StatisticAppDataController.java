/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.statistic.entity.StatisticAppData;
import com.itrus.ca.modules.statistic.service.StatisticAppDataService;

/**
 * 日经营统计Controller
 * @author ZhangJingtao
 * @version 2014-07-15
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/statisticAppData")
public class StatisticAppDataController extends BaseController {

	@Autowired
	private StatisticAppDataService statisticAppDataService;
	
	@ModelAttribute
	public StatisticAppData get(@RequestParam(required=false) Long id) {
		if (id != null){
			return statisticAppDataService.get(id);
		}else{
			return new StatisticAppData();
		}
	}
	

	@RequiresPermissions("statistic:statisticAppData:view")
	@RequestMapping(value = "form")
	public String form(StatisticAppData statisticAppData, Model model) {
		model.addAttribute("statisticAppData", statisticAppData);
		return "modules/statistic/statisticAppDataForm";
	}

	@RequiresPermissions("statistic:statisticAppData:edit")
	@RequestMapping(value = "save")
	public String save(StatisticAppData statisticAppData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, statisticAppData)){
			return form(statisticAppData, model);
		}
		statisticAppDataService.save(statisticAppData);
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticAppData/?repage";
	}
	
	@RequiresPermissions("statistic:statisticAppData:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		statisticAppDataService.delete(id);
		addMessage(redirectAttributes, "删除日经营统计成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticAppData/?repage";
	}

}
