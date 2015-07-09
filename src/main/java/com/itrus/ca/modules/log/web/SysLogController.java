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
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.log.entity.SysOperateLog;
import com.itrus.ca.modules.log.service.SysLogService;

/**
 * 后台日志Controller
 * @author ZhangJingtao
 * @version 2014-07-07
 */
@Controller
@RequestMapping(value = "${adminPath}/log/sysLog")
public class SysLogController extends BaseController {

	@Autowired
	private SysLogService sysLogService;
	
	@ModelAttribute
	public SysOperateLog get(@RequestParam(required=false) Long id) {
		if (id != null){
			return sysLogService.get(id);
		}else{
			return new SysOperateLog();
		}
	}
	
	@RequiresPermissions("log:sysLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysOperateLog sysOperateLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<SysOperateLog> page = sysLogService.find(new Page<SysOperateLog>(request, response), sysOperateLog); 
        model.addAttribute("page", page);
		return "modules/log/sysLogList";
	}

	@RequiresPermissions("log:sysLog:view")
	@RequestMapping(value = "form")
	public String form(SysOperateLog sysOperateLog, Model model) {
		model.addAttribute("sysLog", sysOperateLog);
		return "modules/log/sysLogForm";
	}

	@RequiresPermissions("log:sysLog:edit")
	@RequestMapping(value = "save")
	public String save(SysOperateLog sysOperateLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysOperateLog)){
			return form(sysOperateLog, model);
		}
		sysLogService.save(sysOperateLog);
		addMessage(redirectAttributes, "保存后台日志'" + sysOperateLog.getType() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/log/sysLog/?repage";
	}
	
	@RequiresPermissions("log:sysLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		sysLogService.delete(id);
		addMessage(redirectAttributes, "删除后台日志成功");
		return "redirect:"+Global.getAdminPath()+"/modules/log/sysLog/?repage";
	}

}
