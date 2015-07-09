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
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 联系人Controller
 * @author WangHongwei
 * @version 2014-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workUser")
public class WorkUserController extends BaseController {

	@Autowired
	private WorkUserService workUserService;
	
	@Autowired
	private WorkCompanyService workCompanyService;
	
	@ModelAttribute
	public WorkUser get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workUserService.get(id);
		}else{
			return new WorkUser();
		}
	}
	
	@RequiresPermissions("work:workUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkUser workUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<WorkUser> page = workUserService.find(new Page<WorkUser>(request, response), workUser); 
        model.addAttribute("page", page);
		return "modules/work/workUserList";
	}

	@RequiresPermissions("work:workUser:view")
	@RequestMapping(value = "form")
	public String form(WorkUser workUser, Model model) {
		model.addAttribute("workUser", workUser);
		return "modules/work/workUserForm";
	}

	@RequiresPermissions("work:workUser:edit")
	@RequestMapping(value = "save")
	public String save(WorkUser workUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workUser)){
			return form(workUser, model);
		}
		workUserService.save(workUser);
//		addMessage(redirectAttributes, "保存联系人'" + workUser.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workUser/?repage";
	}

	@RequestMapping(value = "insertWorkUserFrom")
	public String insertWorkUserFrom(Long comId,Long workDealInfoId,WorkUser workUser, Model model) {
		model.addAttribute("comId", comId);
		model.addAttribute("workUser", workUser);
		model.addAttribute("workDealInfoId", workDealInfoId);
		return "modules/work/workUserInsertForm";
	}
	
	@RequestMapping(value = "insertWorkUserFromU")
	public String insertWorkUserFromU(Long comId,WorkUser workUser, Model model) {
		model.addAttribute("comId", comId);
		model.addAttribute("workUser", workUser);
		return "modules/work/workUserInsertFormU";
	}
	
	@RequestMapping(value = "insertWorkUser")
	public String insertWorkUser(WorkUser workUser,Long workDealInfoId, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workUser)){
			return form(workUser, model);
		}
		workUserService.save(workUser);
		
		addMessage(redirectAttributes, "保存联系人'" + workUser.getContactName()+"'成功");
		return "redirect:"+Global.getAdminPath()+"/work/workDealInfoFiling/userlist?comId="+workUser.getWorkCompany().getId()+"&id="+workDealInfoId;
	}
	
	@RequestMapping(value = "insertWorkUserU")
	public String insertWorkUserU(WorkUser workUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workUser)){
			return form(workUser, model);
		}
		workUserService.save(workUser);
		
		addMessage(redirectAttributes, "保存联系人'" + workUser.getContactName()+"'成功");
		return "redirect:"+Global.getAdminPath()+"/work/workDealInfoFiling/userlist?uid="+workUser.getId();
	}
	
	@RequiresPermissions("work:workUser:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		workUserService.delete(id);
		addMessage(redirectAttributes, "删除联系人成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workUser/?repage";
	}
}
