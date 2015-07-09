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
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkUserHisService;

/**
 * 原始经办人信息Controller
 * @author ZhangShaoBo
 * @version 2014-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workUserHis")
public class WorkUserHisController extends BaseController {

	@Autowired
	private WorkUserHisService workUserHisService;
	
	@ModelAttribute
	public WorkUserHis get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workUserHisService.get(id);
		}else{
			return new WorkUserHis();
		}
	}
	
	@RequiresPermissions("work:workUserHis:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkUserHis workUserHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
//			workUserHis.setCreateBy(user);
		}
        Page<WorkUserHis> page = workUserHisService.find(new Page<WorkUserHis>(request, response), workUserHis); 
        model.addAttribute("page", page);
		return "modules/work/workUserHisList";
	}

	@RequiresPermissions("work:workUserHis:view")
	@RequestMapping(value = "form")
	public String form(WorkUserHis workUserHis, Model model) {
		model.addAttribute("workUserHis", workUserHis);
		return "modules/work/workUserHisForm";
	}

	@RequiresPermissions("work:workUserHis:edit")
	@RequestMapping(value = "save")
	public String save(WorkUserHis workUserHis, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workUserHis)){
			return form(workUserHis, model);
		}
		workUserHisService.save(workUserHis);
//		addMessage(redirectAttributes, "保存原始经办人信息'" + workUserHis.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workUserHis/?repage";
	}
	
	@RequiresPermissions("work:workUserHis:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workUserHisService.delete(id);
		addMessage(redirectAttributes, "删除原始经办人信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workUserHis/?repage";
	}

}
