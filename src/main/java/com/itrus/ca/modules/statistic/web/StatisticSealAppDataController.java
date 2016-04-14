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
import com.itrus.ca.modules.statistic.entity.StatisticSealAppData;
import com.itrus.ca.modules.statistic.service.StatisticSealAppDataService;

/**
 * 印章日经营详细表Controller
 * @author DingJianHui
 * @version 2016-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/statisticSealAppData")
public class StatisticSealAppDataController extends BaseController {

	@Autowired
	private StatisticSealAppDataService statisticSealAppDataService;
	
	@ModelAttribute
	public StatisticSealAppData get(@RequestParam(required=false) Long id) {
		if (id != null){
			return statisticSealAppDataService.get(id);
		}else{
			return new StatisticSealAppData();
		}
	}
	
	@RequiresPermissions("statistic:statisticSealAppData:view")
	@RequestMapping(value = {"list", ""})
	public String list(StatisticSealAppData statisticSealAppData, HttpServletRequest request, HttpServletResponse response, Model model) {
//		User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			statisticSealAppData.setCreateBy(user);
//		}
        Page<StatisticSealAppData> page = statisticSealAppDataService.find(new Page<StatisticSealAppData>(request, response), statisticSealAppData); 
        model.addAttribute("page", page);
		return "modules/statistic/statisticSealAppDataList";
	}

	@RequiresPermissions("statistic:statisticSealAppData:view")
	@RequestMapping(value = "form")
	public String form(StatisticSealAppData statisticSealAppData, Model model) {
		model.addAttribute("statisticSealAppData", statisticSealAppData);
		return "modules/statistic/statisticSealAppDataForm";
	}

	@RequiresPermissions("statistic:statisticSealAppData:edit")
	@RequestMapping(value = "save")
	public String save(StatisticSealAppData statisticSealAppData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, statisticSealAppData)){
			return form(statisticSealAppData, model);
		}
		statisticSealAppDataService.save(statisticSealAppData);
//		addMessage(redirectAttributes, "保存印章日经营详细表'" + statisticSealAppData.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticSealAppData/?repage";
	}
	
	@RequiresPermissions("statistic:statisticSealAppData:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		statisticSealAppDataService.delete(id);
		addMessage(redirectAttributes, "删除印章日经营详细表成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticSealAppData/?repage";
	}

}
