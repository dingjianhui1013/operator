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
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 联系人Controller
 * @author WangHongwei
 * @version 2014-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workUserFiling")
public class WorkUserFilingController extends BaseController {

	@Autowired
	private WorkUserService workUserService;
	
	@Autowired
	private WorkCompanyService workCompanyService;
	
	@Autowired
	private WorkLogService workLogService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
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
		String url="modules/work/workUserFilingList";
		if(workUser.getStatus()==0){
			url = "modules/work/workUserFilingListZ";
		}else if(workUser.getStatus()==2){
			url = "modules/work/workUserFilingListT";
		}
		User user = UserUtils.getUser();
        Page<WorkUser> page = workUserService.find(new Page<WorkUser>(request, response), workUser); 
        model.addAttribute("page", page);
		return url;
	}
	
	/**
	 * 联系人列表
	 * @param workUser
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workUser:view")
	@RequestMapping(value = {"userlist"})
	public String userlist(WorkUser workUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<WorkUser> page = workUserService.find(new Page<WorkUser>(request, response), workUser); 
        model.addAttribute("page", page);
		return "modules/work/workUserListFilUser";
	}
	
	/**
	 * 业务办理列表
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = {"applist", ""})
	public String applist(Long comId,WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		WorkCompany workCompany = workCompanyService.get(comId);
        workDealInfo.setWorkCompany(workCompany);
		Page<WorkDealInfo> page = workDealInfoService.find(new Page<WorkDealInfo>(request, response), workDealInfo); 
        model.addAttribute("page", page);
		return "modules/work/workDealInfoListFilApp";
	}
	
	/**
	 * 产品业务列表
	 * @param comId
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = {"proApplist", ""})
	public String proApplist(Long comId,WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		WorkCompany workCompany = workCompanyService.get(comId);
		workDealInfo.setWorkCompany(workCompany);
		Page<WorkDealInfo> page = workDealInfoService.find(new Page<WorkDealInfo>(request, response), workDealInfo); 
		model.addAttribute("page", page);
		return "modules/work/workDealInfoListFilProApp";
	}

	/**
	 * 客服记录
	 * @param workLog
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"loglist", ""})
	public String loglist(Long comId,WorkLog workLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		WorkCompany workCompany = workCompanyService.get(comId);
		workLog.setWorkCompany(workCompany);
		Page<WorkLog> page = workLogService.find(new Page<WorkLog>(request, response), workLog); 
        model.addAttribute("page", page);
		return "modules/work/workLogListFilLog";
	}

	
	@RequiresPermissions("work:workUser:view")
	@RequestMapping(value = "alreadyform")
	public String form(WorkUser workUser, Model model) {
		model.addAttribute("workUser", workUser);
		return "modules/work/workUserFilAlreadyForm";
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
	
	@RequiresPermissions("work:workUser:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		workUserService.delete(id);
		addMessage(redirectAttributes, "删除联系人成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workUser/?repage";
	}

}
