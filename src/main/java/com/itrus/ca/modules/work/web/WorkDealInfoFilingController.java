/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 业务办理Controller
 * 
 * @author ZhangShaoBo
 * @version 2014-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workDealInfoFiling")
public class WorkDealInfoFilingController extends BaseController {

	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private WorkLogService workLogService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private WorkPayInfoService workPayInfoService;

	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public WorkDealInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return workDealInfoService.get(id);
		} else {
			return new WorkDealInfo();
		}
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam(value = "checkIds", required = false) String checkIds) {
		String url = "modules/work/workDealInfoFilingListF";
		Page<WorkDealInfo> page = workDealInfoService.find(
				new Page<WorkDealInfo>(request, response), workDealInfo);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("page", page);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		if (checkIds != null) {
			String[] ids = checkIds.split(",");
			model.addAttribute("ids", ids);
			int index=0;
			for(int i=0;i<ids.length;i++)
			{
				if("".equals(ids[i]))
				{
					index+=1;
				}
			}
			model.addAttribute("count", ids.length-index);
		}
		model.addAttribute("checkIds", checkIds);
		model.addAttribute("status", workDealInfo.getStatus());
		return url;
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "quangui")
	public String quangui() {
		List<WorkDealInfo> list = workDealInfoService.findList();
		WorkDealInfo workDealInfo = new WorkDealInfo();
		for (int i = 0; i < list.size(); i++) {
			workDealInfo = workDealInfoService.get(list.get(i).getId());
			workDealInfo.setArchiveDate(new Timestamp(new Date().getTime()));
			workDealInfo.setStatus(1);
			workDealInfoService.save(workDealInfo);
			logUtil.saveSysLog("业务中心", "业务信息归档：业务编号" + workDealInfo.getId(), "");
		}
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfoFiling/list?status=0";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "gui")
	public String gui(String[] ids, HttpServletRequest request,
			HttpServletResponse response) {
		WorkDealInfo workDealInfo = new WorkDealInfo();	
		for (int i = 0; i < ids.length; i++) {
			workDealInfo = workDealInfoService.get(Long.parseLong(ids[i]));
			workDealInfo.setArchiveDate(new Timestamp(new Date().getTime()));
			workDealInfo.setStatus(1);
			workDealInfoService.save(workDealInfo);
			logUtil.saveSysLog("业务中心", "业务信息归档：业务编号" + workDealInfo.getId(), "");
		}
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfoFiling/list?status=0";
	}

	/**
	 * 咨询类用户列表
	 * 
	 * @param workUser
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	// @RequiresPermissions("work:workDealInfo:view")
	// @RequestMapping(value = { "ulist" })
	// public String ulist(WorkDealInfo workDealInfo, WorkUser workUser,
	// HttpServletRequest request, HttpServletResponse response,
	// Model model) {
	// User user = UserUtils.getUser();
	// model.addAttribute("proType", ProductType.productTypeStrMap);
	// model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
	// model.addAttribute("wdiStatus",
	// WorkDealInfoStatus.WorkDealInfoStatusMap);
	// if (workDealInfo.getWorkUser() != null) {
	// if (workDealInfo.getWorkUser().getContactName() != null
	// && !workDealInfo.getWorkUser().getContactName().equals(""))
	// workUser.setContactName(workDealInfo.getWorkUser()
	// .getContactName());
	// if (workDealInfo.getWorkUser().getContactPhone() != null
	// && !workDealInfo.getWorkUser().getContactPhone().equals(""))
	// workUser.setContactPhone(workDealInfo.getWorkUser()
	// .getContactPhone());
	// if (workDealInfo.getWorkUser().getContactTel() != null
	// && !workDealInfo.getWorkUser().getContactTel().equals(""))
	// workUser.setContactTel(workDealInfo.getWorkUser()
	// .getContactTel());
	// }
	// model.addAttribute("proType", ProductType.productTypeStrMap);
	// workUser.setStatus(2);
	// Page<WorkUser> page = workUserService.find(new Page<WorkUser>(request,
	// response), workUser);
	// model.addAttribute("page", page);
	// return "modules/work/workDealInfoFilingListT";
	// }
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "ulist" })
	public String ulist(WorkLog workLog, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<WorkLog> page = workLogService.findKfList(new Page<WorkLog>(
				request, response), workLog);
		model.addAttribute("page", page);
		return "modules/work/workDealInfoFilingListT";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "formF")
	public String form(Long uid, WorkDealInfo workDealInfo, Model model) {
		try {
			if (uid != null) {
				WorkUser workUser = workUserService.get(uid);
				model.addAttribute("workDealInfo", workUser);
				if (workUser.getWorkCompany() == null) {
					model.addAttribute("key", 0);
				}
				return "modules/work/workDealInfoFilingComFormU";
			} else {
				model.addAttribute("workDealInfo", workDealInfo);
			}
		} catch (Exception e) {
		}
		return "modules/work/workDealInfoFilingComForm";
	}

	/**
	 * 联系人列表
	 * 
	 * @param workUser
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "userlist" })
	public String userlist(Long uid, Long comId, WorkUser workUser,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			if (uid != null) {
				WorkUser user = workUserService.get(uid);
				model.addAttribute("user", user);
				workUser.setWorkCompany(user.getWorkCompany());
				Page<WorkUser> page = workUserService.find(new Page<WorkUser>(
						request, response), workUser);
				model.addAttribute("page", page);
				return "modules/work/workUserListFilUserU";
			}
			User user = UserUtils.getUser();
			WorkCompany workCompany = workCompanyService.get(comId);
			workUser.setWorkCompany(workCompany);
			Page<WorkUser> page = workUserService.find(new Page<WorkUser>(
					request, response), workUser);
			model.addAttribute("page", page);
		} catch (Exception e) {
		}
		return "modules/work/workUserListFilUser";
	}

	/**
	 * 业务办理列表
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "applist" })
	public String applist(Long comId, WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			WorkCompany workCompany = workCompanyService.get(comId);

			ArrayList<Long> dealInfoIds = new ArrayList<Long>();
			WorkDealInfo dealInfo = new WorkDealInfo();
			dealInfo = workDealInfo;
			for (int i = 0;; i++) {
				dealInfoIds.add(dealInfo.getId());
				if (dealInfo.getPrevId() != null) {
					WorkDealInfo inDealInfo = workDealInfoService.get(dealInfo
							.getPrevId());
					dealInfo = inDealInfo;
				} else {

					break;
				}
			}
			workDealInfo.setWorkCompany(workCompany);
			model.addAttribute("proType", ProductType.productTypeStrMap);
			model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
			model.addAttribute("wdiStatus",
					WorkDealInfoStatus.WorkDealInfoStatusMap);
			// Page<WorkDealInfo> page = workDealInfoService.findAppList(
			// new Page<WorkDealInfo>(request, response), workDealInfo,
			// dealInfoIds);
			Page<WorkDealInfo> page = workDealInfoService.findByIds(
					new Page<WorkDealInfo>(request, response), workDealInfo,
					dealInfoIds);
			model.addAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/work/workDealInfoListFilApp";
	}

	/**
	 * 产品业务列表
	 * 
	 * @param comId
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "proApplist", "" })
	public String proApplist(Long comId, WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			WorkCompany workCompany = workCompanyService.get(comId);
			workDealInfo.setWorkCompany(workCompany);
			model.addAttribute("now_date", new Date());
			model.addAttribute("proType", ProductType.productTypeStrMap);
			Page<WorkDealInfo> page = workDealInfoService.find(
					new Page<WorkDealInfo>(request, response), workDealInfo);
			model.addAttribute("page", page);
		} catch (Exception e) {
		}
		return "modules/work/workDealInfoListFilProApp";
	}

	/**
	 * 客服记录
	 * 
	 * @param workLog
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "loglist" })
	public String loglist(Long uid, Long comId, WorkLog workLog,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			if (uid != null) {
				WorkUser user = workUserService.get(uid);
				workLog.setWorkCompany(user.getWorkCompany());
				Page<WorkLog> page = workLogService.find(new Page<WorkLog>(
						request, response), workLog);
				model.addAttribute("page", page);
				model.addAttribute("user", user);
				return "modules/work/workLogListFilLogU";
			}
			WorkCompany workCompany = workCompanyService.get(comId);
			workLog.setWorkCompany(workCompany);
			Page<WorkLog> page = workLogService.find(new Page<WorkLog>(request,
					response), workLog);
			model.addAttribute("page", page);
		} catch (Exception e) {
		}
		return "modules/work/workLogListFilLog";
	}

	/**
	 * 归档处理
	 * 
	 * @return
	 */
	@RequestMapping("updateStatus")
	public String updateStatus(WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		workDealInfo.setArchiveDate(new Timestamp(new Date().getTime()));
		workDealInfo.setStatus(1);
		workDealInfoService.save(workDealInfo);
		logUtil.saveSysLog("业务中心", "业务信息归档：业务编号" + workDealInfo.getId(), "");
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfoFiling/list?status=0";
	}

	/**
	 * 编辑联系人跳转页面
	 */
	@RequestMapping("updateUserFrom")
	public String updateUserFrom(Long workDealInfoId, Long comId,
			WorkUser workUser, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		WorkUser user = workUserService.get(workUser.getId());
		if (workDealInfoId != null) {
			model.addAttribute("workDealInfoId", workDealInfoId);
		}
		model.addAttribute("workUser", user);
		return "modules/work/workUserUpdateFilingForm";
	}

	/**
	 * 编辑联系人
	 * 
	 * @return
	 */
	@RequestMapping("updateUser")
	public String updateUser(Long workDealInfoId, Long comId,
			WorkUser workUser, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		WorkCompany workCompany = workCompanyService.get(comId);
		workUser.setWorkCompany(workCompany);
		String detail = "";
		if (workUser.getId() == null) {
			detail = workCompany.getCompanyName() + "新增联系人"
					+ workUser.getContactName();
		} else {
			detail = workCompany.getCompanyName() + "修改联系人"
					+ workUser.getContactName();
		}
		workUserService.save(workUser);
		if (workDealInfoId != null) {
			return "redirect:" + Global.getAdminPath()
					+ "/work/workDealInfoFiling/userlist?id=" + workDealInfoId
					+ "&comId=" + workUser.getWorkCompany().getId();
		}
		logUtil.saveSysLog("业务中心", detail, "");
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfoFiling/userlist?uid=" + workUser.getId();
	}

	@RequestMapping("fromProApp")
	public String fromProApp(WorkDealInfo workDealInfo, Model model) {
		WorkPayInfo workPayInfo = workPayInfoService.get(workDealInfo
				.getWorkPayInfo().getId());
		List<WorkFinancePayInfoRelation> finance = workFinancePayInfoRelationService
				.findByPayInfo(workPayInfo);
		if (finance.size() > 0) {
			model.addAttribute("bangD", "绑定");
		}
		List<WorkLog> workLogs = workLogService.findByDealInfo(workDealInfo);
		model.addAttribute("workLogs", workLogs);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("workDealInfo", workDealInfo);
		return "modules/work/workDealInfoFilingFormF";
	}

	@RequestMapping("fromApp")
	public String fromApp(WorkDealInfo workDealInfo, Model model) {
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("workDealInfo", workDealInfo);
		return "modules/work/workDealInfoFilingFormA";
	}
}
