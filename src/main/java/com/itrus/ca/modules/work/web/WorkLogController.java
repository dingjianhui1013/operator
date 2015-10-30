/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;

/**
 * 工作记录Controller
 * 
 * @author WangHongwei
 * @version 2014-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workLog")
public class WorkLogController extends BaseController {

	@Autowired
	private WorkLogService workLogService;
	
	
	private SystemService systemService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ConfigAppService configAppService;

	@ModelAttribute
	public WorkLog get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return workLogService.get(id);
		} else {
			return new WorkLog();
		}
	}

	@RequiresPermissions("work:workLog:view")
	@RequestMapping(value = { "list", "" })
	public String list(
			WorkLog workLog,String name,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// Page<WorkLog> page = workLogService.find(new Page<WorkLog>(request,
		// response), workLog);
		// model.addAttribute("page", page);
		
		List<Office> offices = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		String names = workLogService.findName(name);
		if("true".equals(names)){
			List<Map<String, Object>> list = workLogService.findtj(offices,name,
					startTime, endTime);
			model.addAttribute("logList", list);
		}
		model.addAttribute("name",name);
		model.addAttribute("startTime", getDateByString(startTime));
		model.addAttribute("endTime", getDateByString(endTime));
		return "modules/customer/workLogCustomerList";
	}

	@RequiresPermissions("work:workLog:view")
	@RequestMapping(value = { "rclist" })
	public String rclist(
			WorkLog workLog,String name,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value ="appId" , required=false) Long appId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// Page<WorkLog> page = workLogService.find(new Page<WorkLog>(request,
		// response), workLog);
		// model.addAttribute("page", page);
		String names = workLogService.findName(name);
		List<Office> offices = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		if("true".equals(names)){
		List<Map<String, Object>> list = workLogService.findRctj(offices,name,
				startTime, endTime,appId);
		model.addAttribute("logList", list);
		}
		model.addAttribute("apps", configAppService.findall());
		model.addAttribute("startTime", getDateByString(startTime));
		model.addAttribute("endTime", getDateByString(endTime));
		model.addAttribute("name",name);
		return "modules/customer/workRCCustomerList";
	}

	@RequiresPermissions("work:workLog:view")
	@RequestMapping("kflist")
	public String kflist(WorkLog workLog, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		
		Page<WorkLog> page = workLogService.findKfList(new Page<WorkLog>(
				request, response), workLog);
		model.addAttribute("page", page);
		return "modules/customer/workLogKfList";
	}

	@RequiresPermissions("work:workLog:view")
	@RequestMapping(value = "form")
	public String form(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		return "modules/work/workLogForm";
	}

	@RequestMapping("updateFrom")
	public String updateFrom(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		return "modules/work/workLogUpdateForm";
	}

	@RequestMapping("updateFromK")
	public String updateFromK(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		return "modules/work/workLogUpdateFormK";
	}

	@RequestMapping(value = "saveK")
	public String saveK(Long dealInfoId, WorkLog workLog, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workLog)) {
			return form(workLog, model);
		}
		if (dealInfoId != null && dealInfoId != 0) {
			WorkDealInfo workDealInfo = workDealInfoService.get(dealInfoId);
			workLog.setWorkDealInfo(workDealInfo);
			workLog.setWorkCompany(workDealInfo.getWorkCompany());
			workLog.setConfigApp(workDealInfo.getConfigApp());
		}
		if (workLog.getSerType().equals("日常客服")) {
			workLog.setProbleType(null);
			workLog.setStatus(1);
		} else {
			workLog.setStatus(0);
		}
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		addMessage(redirectAttributes, "保存工作记录成功");
		return "redirect:" + Global.getAdminPath() + "/work/workLog/kflist";
	}

	@RequestMapping(value = "save")
	public String save(Long dealInfoId, WorkLog workLog, Model model,
			RedirectAttributes redirectAttributes,String ywzx,String ywcz,String ywxt) {
		if(ywzx!=null)
		{
			String ywzxs=ywzx.replace(","," ");
			workLog.setYwzx(ywzxs);
		}
		if(ywcz!=null)
		{
			String ywczs=ywcz.replace(","," ");
			workLog.setYwcz(ywczs);
		}
		if(ywxt!=null)
		{
			String ywxts=ywxt.replace(","," ");
			workLog.setYwxt(ywxts);
		}
		if (!beanValidator(model, workLog)) {
			return form(workLog, model);
		}
		if (dealInfoId != null && dealInfoId != 0) {
			WorkDealInfo workDealInfo = workDealInfoService.get(dealInfoId);
			workLog.setWorkDealInfo(workDealInfo);
			workLog.setWorkCompany(workDealInfo.getWorkCompany());
			workLog.setConfigApp(workDealInfo.getConfigApp());
		}
		if (workLog.getSerType().equals("日常客服")) {
			workLog.setProbleType(null);
			workLog.setStatus(1);
		} else {
			workLog.setStatus(0);
		}
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		addMessage(redirectAttributes, "保存工作记录成功");
		return "redirect:" + Global.getAdminPath() + "/work/customer/list";
	}

	@RequiresPermissions("work:workLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		// workLogService.delete(id);
		addMessage(redirectAttributes, "删除工作记录成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/work/workLog/?repage";
	}

	private Date getDateByString(String date) {
		if (date != null && !date.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		return null;
	}
}
