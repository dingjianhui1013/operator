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
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;

/**
 * 原始单位信息Controller
 * @author ZhangShaoBo
 * @version 2014-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workCompanyHis")
public class WorkCompanyHisController extends BaseController {

	@Autowired
	private WorkCompanyHisService workCompanyHisService;
	
	@ModelAttribute
	public WorkCompanyHis get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workCompanyHisService.get(id);
		}else{
			return new WorkCompanyHis();
		}
	}
	
	@RequiresPermissions("work:workCompanyHis:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkCompanyHis workCompanyHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			workCompanyHis.setCreateBy(user);
//		}
        Page<WorkCompanyHis> page = workCompanyHisService.find(new Page<WorkCompanyHis>(request, response), workCompanyHis); 
        model.addAttribute("page", page);
		return "modules/work/workCompanyHisList";
	}

	@RequiresPermissions("work:workCompanyHis:view")
	@RequestMapping(value = "form")
	public String form(WorkCompanyHis workCompanyHis, Model model) {
		model.addAttribute("workCompanyHis", workCompanyHis);
		return "modules/work/workCompanyHisForm";
	}

	@RequiresPermissions("work:workCompanyHis:edit")
	@RequestMapping(value = "save")
	public String save(WorkCompanyHis workCompanyHis, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workCompanyHis)){
			return form(workCompanyHis, model);
		}
		workCompanyHisService.save(workCompanyHis);
//		addMessage(redirectAttributes, "保存原始单位信息'" + workCompanyHis.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCompanyHis/?repage";
	}
	
	@RequiresPermissions("work:workCompanyHis:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workCompanyHisService.delete(id);
		addMessage(redirectAttributes, "删除原始单位信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCompanyHis/?repage";
	}

}
