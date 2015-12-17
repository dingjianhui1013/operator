/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCompanyService;
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
	@Autowired
	private WorkCompanyService workCompanyService;
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

//	@RequiresPermissions("work:workLog:view")
//	@RequestMapping(value = { "rclist" })
//	public String rclist(
//			WorkLog workLog,String name,
//			@RequestParam(value = "startTime", required = false) String startTime,
//			@RequestParam(value = "endTime", required = false) String endTime,
//			@RequestParam(value ="appId" , required=false) Long appId,
//			HttpServletRequest request, HttpServletResponse response,
//			Model model) {
//		// Page<WorkLog> page = workLogService.find(new Page<WorkLog>(request,
//		// response), workLog);
//		// model.addAttribute("page", page);
//		String names = workLogService.findName(name);
//		List<Office> offices = officeService.getOfficeByType(
//				UserUtils.getUser(), 2);
//		if("true".equals(names)){
//		List<Map<String, Object>> list = workLogService.findRctj(offices,name,
//				startTime, endTime,appId);
//		model.addAttribute("logList", list);
//		}
//		model.addAttribute("apps", configAppService.findall());
//		model.addAttribute("startTime", getDateByString(startTime));
//		model.addAttribute("endTime", getDateByString(endTime));
//		model.addAttribute("name",name);
//		return "modules/customer/workRCCustomerList";
//	}
	@RequiresPermissions("work:workLog:view")
	@RequestMapping(value = { "rclist" })
	public String rclist(
			WorkLog workLog,String name,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value ="appId" , required=false) Long appId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// Page<WorkLog> page = workLogService.find(new Page<WorkLog>(request,
		// response), workLog);
		// model.addAttribute("page", page);
		Map<String, List<Integer>> access_count=new HashMap<String, List<Integer>>();
		String names = workLogService.findName(name);
		List<Office> offices = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		if("true".equals(names)){
//		List<Map<String, Object>> list = workLogService.findRctj(offices,name,
//				startTime, endTime,appId);
//		model.addAttribute("logList", list);
			List<WorkLog> workLogs=workLogService.findworkLogs(name, startTime, endTime,appId);
			model.addAttribute("li", workLogs);
			Set<String> Access=new LinkedHashSet<String>();
			for(int i=0;i<workLogs.size();i++)
			{
				Access.add(workLogs.get(i).getAccess());
			}
			Object Accesss[]=Access.toArray();
			
			for(int a=0;a<Accesss.length;a++)
			{
				List<Integer> count=Lists.newArrayList();
				int xz=0;
				int gx=0;
				int js=0;
				int bg=0;
				int	bb=0;
				int yt=0;
				int mm=0;
				int sq=0;
				int hz=0;
				int ywxt=0;
				int ywcz=0;
				int qt=0;
				for(int w=0;w<workLogs.size();w++)
				{
					if(workLogs.get(w).getAccess().equals(Accesss[a]))
					{
						if(workLogs.get(w).getYwzx()!=null)
						{
							
							if(workLogs.get(w).getYwzx().indexOf("新增")!=-1)
							{
								xz++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("更新")!=-1)
							{
								gx++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("解锁")!=-1)
							{
								js++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("变更")!=-1)
							{
								xz++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("补办")!=-1)
							{
								xz++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("用途")!=-1)
							{
								xz++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("密码")!=-1)
							{
								xz++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("授权")!=-1)
							{
								xz++;
							}else
							if(workLogs.get(w).getYwzx().indexOf("合作")!=-1)
							{
								xz++;
							}
							if(workLogs.get(w).getProbleType()!=null)
							{
								qt++;
							}
						}
						if(workLogs.get(w).getYwxt()!=null)
						{
							ywxt+=workLogs.get(w).getYwxt().split(",").length;
						}
						if(workLogs.get(w).getYwcz()!=null)
						{
							ywcz+=workLogs.get(w).getYwcz().split(",").length;
						}
					}
				}
				count.add(xz);
				count.add(gx);
				count.add(js);
				count.add(bg);
				count.add(bb);
				count.add(yt);
				count.add(mm);
				count.add(sq);
				count.add(hz);
				count.add(ywxt);
				count.add(ywcz);
				count.add(qt);
				access_count.put((String)Accesss[a], count);
			}
		}
		int index=0;
		Iterator<Map.Entry<String, List<Integer>>> itt=access_count.entrySet().iterator();
		while(itt.hasNext())
		{
			index=itt.next().getValue().size();
		}
		List<Integer> zj=Lists.newArrayList();
		for(int c=0;c<index;c++)
		{
			int count=0;
			Iterator<Map.Entry<String, List<Integer>>> it=access_count.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, List<Integer>> entry=it.next();
				count+=entry.getValue().get(c);
			}
			zj.add(count);
		}
		model.addAttribute("zj", zj);
		model.addAttribute("access_count", access_count);
		model.addAttribute("apps", configAppService.findall());
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime",endTime);
		model.addAttribute("name",name);
		if(appId!=null)
		{
			model.addAttribute("appId", appId);
		}
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
	@RequiresPermissions("work:workLog:view")
	@RequestMapping(value = "formFiling")
	public String formFiling(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		return "modules/work/workLogFilingForm";
	}
	@RequiresPermissions("work:workLog:view")
	@RequestMapping(value = "formProject")
	public String formProject(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		return "modules/work/workLogProjectForm";
	}
	@RequestMapping("updateFrom")
	public String updateFrom(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		List<WorkCompany> list=workCompanyService.findAll();
		model.addAttribute("list", list);
		return "modules/work/workLogUpdateForm";
	}

	@RequestMapping("updateFromK")
	public String updateFromK(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		return "modules/work/workLogUpdateFormK";
	}
	@RequestMapping("updateFromF")
	public String updateFromF(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		List<ConfigApp> configApp= configAppService.findAllConfigApp();
		model.addAttribute("configApp", configApp);
		return "modules/work/workLogUpdateFormF";
	}
	@RequestMapping("updateFromP")
	public String updateFromP(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		List<ConfigApp> configApp= configAppService.findAllConfigApp();
		model.addAttribute("configApp", configApp);
		return "modules/work/workLogUpdateFormP";
	}
	@RequestMapping("updateFromFi")
	public String updateFromFi(WorkLog workLog, Model model) {
		model.addAttribute("workLog", workLog);
		model.addAttribute("userName", UserUtils.getUser().getName());
		List<WorkCompany> list=workCompanyService.findAll();
		model.addAttribute("list", list);
		return "modules/work/workLogUpdateFormFi";
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
	@RequestMapping(value = "saveP")
	public String saveP(Long dealInfoId, WorkLog workLog, Model model,
			RedirectAttributes redirectAttributes,Long appid) {
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
		ConfigApp config=configAppService.findByAppId(appid);
		workLog.setConfigApp(config);
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		addMessage(redirectAttributes, "保存工作记录成功");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoFiling/plist?distinguish=3";
	}
	@RequestMapping(value = "saveF")
	public String saveF(Long dealInfoId, WorkLog workLog, Model model,
			RedirectAttributes redirectAttributes,Long appid) {
		
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
		ConfigApp config=configAppService.findByAppId(appid);
		workLog.setConfigApp(config);
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		addMessage(redirectAttributes, "保存工作记录成功");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoFiling/flist?distinguish=2";
	}
	@RequestMapping(value = "saveFi")
	public String saveFi(Long dealInfoId, WorkLog workLog, Model model,
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
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		addMessage(redirectAttributes, "保存工作记录成功");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoFiling/ulist?distinguish=1";
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
