/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

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
import com.itrus.ca.modules.settle.entity.WorkDealInfo_settlementLog;
import com.itrus.ca.modules.settle.service.WorkDealInfo_settlementLogService;

/**
 * 年费结算保存Controller
 * @author DingJianHui
 * @version 2016-03-09
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/workDealInfo_settlementLog")
public class WorkDealInfo_settlementLogController extends BaseController {

	@Autowired
	private WorkDealInfo_settlementLogService workDealInfo_settlementLogService;
	
	@ModelAttribute
	public WorkDealInfo_settlementLog get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workDealInfo_settlementLogService.get(id);
		}else{
			return new WorkDealInfo_settlementLog();
		}
	}
	
	@RequiresPermissions("settle:workDealInfo_settlementLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkDealInfo_settlementLog workDealInfo_settlementLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			workDealInfo_settlementLog.setCreateBy(user);
		}
        Page<WorkDealInfo_settlementLog> page = workDealInfo_settlementLogService.find(new Page<WorkDealInfo_settlementLog>(request, response), workDealInfo_settlementLog); 
        model.addAttribute("page", page);
		return "modules/settle/workDealInfo_settlementLogList";
	}

	@RequiresPermissions("settle:workDealInfo_settlementLog:view")
	@RequestMapping(value = "form")
	public String form(WorkDealInfo_settlementLog workDealInfo_settlementLog, Model model) {
		model.addAttribute("workDealInfo_settlementLog", workDealInfo_settlementLog);
		return "modules/settle/workDealInfo_settlementLogForm";
	}

	@RequiresPermissions("settle:workDealInfo_settlementLog:edit")
	@RequestMapping(value = "save")
	public String save(WorkDealInfo_settlementLog workDealInfo_settlementLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workDealInfo_settlementLog)){
			return form(workDealInfo_settlementLog, model);
		}
		workDealInfo_settlementLogService.save(workDealInfo_settlementLog);
//		addMessage(redirectAttributes, "保存年费结算保存'" + workDealInfo_settlementLog.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/workDealInfo_settlementLog/?repage";
	}
	
	@RequiresPermissions("settle:workDealInfo_settlementLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workDealInfo_settlementLogService.delete(id);
		addMessage(redirectAttributes, "删除年费结算保存成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/workDealInfo_settlementLog/?repage";
	}

}
