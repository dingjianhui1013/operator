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
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;

/**
 * 申请人信息Controller
 * @author ZhangShaoBo
 * @version 2014-07-07
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workCertApplyInfo")
public class WorkCertApplyInfoController extends BaseController {

	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;
	
	@ModelAttribute
	public WorkCertApplyInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return workCertApplyInfoService.get(id);
		}else{
			return new WorkCertApplyInfo();
		}
	}
	
	@RequiresPermissions("work:workCertApplyInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkCertApplyInfo workCertApplyInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			//workCertApplyInfo.setCreateBy(user);
		}
        Page<WorkCertApplyInfo> page = workCertApplyInfoService.find(new Page<WorkCertApplyInfo>(request, response), workCertApplyInfo); 
        model.addAttribute("page", page);
		return "modules/work/workCertApplyInfoList";
	}

	@RequiresPermissions("work:workCertApplyInfo:view")
	@RequestMapping(value = "form")
	public String form(WorkCertApplyInfo workCertApplyInfo, Model model) {
		model.addAttribute("workCertApplyInfo", workCertApplyInfo);
		return "modules/work/workCertApplyInfoForm";
	}

	@RequiresPermissions("work:workCertApplyInfo:edit")
	@RequestMapping(value = "save")
	public String save(WorkCertApplyInfo workCertApplyInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workCertApplyInfo)){
			return form(workCertApplyInfo, model);
		}
		workCertApplyInfoService.save(workCertApplyInfo);
		addMessage(redirectAttributes, "保存申请人信息'" + workCertApplyInfo.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCertApplyInfo/?repage";
	}
	
	@RequiresPermissions("work:workCertApplyInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workCertApplyInfoService.delete(id);
		addMessage(redirectAttributes, "删除申请人信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/work/workCertApplyInfo/?repage";
	}

}
