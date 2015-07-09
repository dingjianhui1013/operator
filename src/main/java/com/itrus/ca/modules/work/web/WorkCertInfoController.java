/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

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
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.service.WorkCertInfoService;

/**
 * 证书信息Controller
 * @author ZhangJingtao
 * @version 2014-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workCertInfo")
public class WorkCertInfoController extends BaseController {

	@Autowired
	private WorkCertInfoService workCertInfoService;
	
	@ModelAttribute
	public WorkCertInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workCertInfoService.get(id);
		}else{
			return new WorkCertInfo();
		}
	}
	
	@RequiresPermissions("work:workCertInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkCertInfo workCertInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<WorkCertInfo> page = workCertInfoService.find(new Page<WorkCertInfo>(request, response), workCertInfo); 
        model.addAttribute("page", page);
		return "modules/work/workCertInfoList";
	}

	@RequiresPermissions("work:workCertInfo:view")
	@RequestMapping(value = "form")
	public String form(WorkCertInfo workCertInfo, Model model) {
		model.addAttribute("workCertInfo", workCertInfo);
		return "modules/work/workCertInfoForm";
	}

	@RequiresPermissions("work:workCertInfo:edit")
	@RequestMapping(value = "save")
	public String save(WorkCertInfo workCertInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workCertInfo)){
			return form(workCertInfo, model);
		}
		workCertInfoService.save(workCertInfo);
		addMessage(redirectAttributes, "保存证书信息'" + workCertInfo.getSerialnumber() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCertInfo/?repage";
	}
	
	@RequiresPermissions("work:workCertInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workCertInfoService.delete(id);
		addMessage(redirectAttributes, "删除证书信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCertInfo/?repage";
	}

}
