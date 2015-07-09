/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.log.web;

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
import com.itrus.ca.modules.sys.dao.UserDao;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.log.entity.TerminalLog;
import com.itrus.ca.modules.log.service.TerminalLogService;

/**
 * 前台日志Controller
 * @author ZhangJingtao
 * @version 2014-07-07
 */
@Controller
@RequestMapping(value = "${adminPath}/log/terminalLog")
public class TerminalLogController extends BaseController {

	@Autowired
	private TerminalLogService terminalLogService;
	
	@Autowired
	private UserDao userDao;
	
	@ModelAttribute
	public TerminalLog get(@RequestParam(required=false) Long id) {
		if (id != null){
			return terminalLogService.get(id);
		}else{
			return new TerminalLog();
		}
	}
	
	@RequiresPermissions("log:terminalLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(TerminalLog terminalLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TerminalLog> page = terminalLogService.find(new Page<TerminalLog>(request, response), terminalLog); 
        model.addAttribute("page", page);
		return "modules/log/terminalLogList";
	}

	@RequiresPermissions("log:terminalLog:view")
	@RequestMapping(value = "form")
	public String form(TerminalLog terminalLog, Model model) {
		model.addAttribute("terminalLog", terminalLog);
		return "modules/log/terminalLogForm";
	}

	@RequiresPermissions("log:terminalLog:edit")
	@RequestMapping(value = "save")
	public String save(TerminalLog terminalLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, terminalLog)){
			return form(terminalLog, model);
		}
		terminalLogService.save(terminalLog);
		addMessage(redirectAttributes, "保存前台日志'" + terminalLog.getType() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/log/terminalLog/?repage";
	}
	
	@RequiresPermissions("log:terminalLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
	//	terminalLogService.delete(id);
		addMessage(redirectAttributes, "删除前台日志成功");
		return "redirect:"+Global.getAdminPath()+"/modules/log/terminalLog/?repage";
	}

}
