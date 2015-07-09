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
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.service.WorkCompanyService;

/**
 * 单位名称Controller
 * @author HUHAO
 * @version 2014-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workCompany")
public class WorkCompanyController extends BaseController {

	@Autowired
	private WorkCompanyService workCompanyService;
	
	@ModelAttribute
	public WorkCompany get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workCompanyService.get(id);
		}else{
			return new WorkCompany();
		}
	}
	
	@RequiresPermissions("work:workCompany:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkCompany workCompany, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			//workCompany.setCreateBy(user);
		}
        Page<WorkCompany> page = workCompanyService.find(new Page<WorkCompany>(request, response), workCompany); 
        model.addAttribute("page", page);
		return "modules/work/workCompanyList";
	}

	@RequiresPermissions("work:workCompany:view")
	@RequestMapping(value = "form")
	public String form(WorkCompany workCompany, Model model) {
		model.addAttribute("workCompany", workCompany);
		return "modules/work/workCompanyForm";
	}

	@RequiresPermissions("work:workCompany:edit")
	@RequestMapping(value = "save")
	public String save(WorkCompany workCompany, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workCompany)){
			return form(workCompany, model);
		}
		workCompanyService.save(workCompany);
		addMessage(redirectAttributes, "保存单位名称'" + workCompany.getCompanyName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCompany/?repage";
	}
	
	@RequiresPermissions("work:workCompany:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workCompanyService.delete(id);
		addMessage(redirectAttributes, "删除单位名称成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCompany/?repage";
	}

}
