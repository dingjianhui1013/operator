/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.weaver.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.bean.StaticUserLog;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * 工作记录
 * 
 * @author WHW
 * @version 2014年6月27日15:08:21
 */
@Controller
@RequestMapping(value = "${adminPath}/work/customer")
public class WorkCustomerController extends BaseController {

	private LogUtil logUtil = new LogUtil();

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

	@Autowired
	private ConfigAppService configAppService;

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
			HttpServletResponse response, Model model,Date startTime , Date endTime ) {
		if (endTime!=null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
		}
	
		String url = "modules/customer/workDealInfoFilingListF";
		Page<WorkDealInfo> page = workDealInfoService.findCustomer(
				new Page<WorkDealInfo>(request, response), workDealInfo , startTime , endTime );
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("apps", configAppService.selectAll());
		model.addAttribute("page", page);
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);
		return url;
	}

	/**
	 * 新增记录
	 * 
	 * @param workDealInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("insertCustomerFrom")
	public String insertCustomerFrom(WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("nowDate", getDateString());
		model.addAttribute("userName", UserUtils.getUser().getName());
		List<ConfigApp> configApp= configAppService.findall();
		model.addAttribute("configApp", configApp);
		return "modules/customer/workCustomerInsert";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("insertCustomerFromF")
	public String insertCustomerFromF(Long id, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		WorkUser workUser = workUserService.get(id);
		model.addAttribute("workUser", workUser);
		model.addAttribute("nowDate", getDateString());
		model.addAttribute("userName", UserUtils.getUser().getName());
		return "modules/customer/workCustomerComInsertTR";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("insertCustomerFromT")
	public String insertCustomerFromT(WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("nowDate", getDateString());
		model.addAttribute("userName", UserUtils.getUser().getName());
		return "modules/customer/workCustomerInsert";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("insertCustomer")
	public String insertCustomer(Long dealInfoId, WorkLog workLog,
			HttpServletRequest request, HttpServletResponse response,
			Model model,String ywzx,String ywcz,String ywxt) {
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
		WorkDealInfo workDealInfo = workDealInfoService.get(dealInfoId);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setCreatTime(new Date());
		workLog.setState(1);
		User user = UserUtils.getUser();
		workLog.setOffice(UserUtils.getUser().getOffice());
		if (!workLog.getSerType().equals("日常客服")) {
			workLog.setProbleType(null);
			workLog.setStatus(1);
		} else {
			workLog.setStatus(0);
		}
		workLog.setCreateBy(UserUtils.getUser());
		String detail = "";
		if (workLog.getId() != null) {
			detail = "修改工作记录成功,id为";
		} else {
			detail = "添加工作记录成功,id为";
		}
		workLogService.save(workLog);
		logUtil.saveSysLog("客服管理", detail + workLog.getId(), "");
		return "redirect:" + Global.getAdminPath() + "/work/customer/list";
	}

	@RequestMapping("insertUserFrom")
	public String insertUserFrom(WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return "modules/customer/workCompanyForm";
	}

	@RequestMapping("insertUser")
	public String insertUser(String endTime,WorkUser workUser, WorkCompany workCompany,
			HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,
			Model model) {
	
		//List<WorkCompany> companys = workCompanyService.selectByNameAndNumber(workCompany.getCompanyName(),workCompany.getOrganizationNumber());
//		if(companys.size()>0){
//			addMessage(redirectAttributes, "该公司已经存在！");
//			return "redirect:" + Global.getAdminPath() + "/work/customer/list";
//		}
		// 判断公司是否存在
		/*
		 * List<WorkCompany> workCompanies =
		 * workCompanyService.selectByNameAndNumber
		 * (workCompany.getOrganizationNumber(),workCompany.getCompanyName());
		 * if(workCompanies.size()>0){ WorkCompany workCom =
		 * workCompanies.get(0);
		 * workCom.setCompanyType(workCompany.getCompanyType());
		 * workCom.setOrgExpirationTime(workCompany.getOrgExpirationTime());
		 * workCom.setProvince(workCompany.getProvince());
		 * workCom.setCity(workCompany.getCity());
		 * workCom.setDistrict(workCompany.getDistrict());
		 * workCom.setLegalName(workCompany.getLegalName());
		 * workCom.setAddress(workCompany.getAddress());
		 * workCom.setRemarks(workCompany.getRemarks()); workCompany = workCom;
		 * //判断公司是否存在业务 workDealInfoService.findByCompany(workCompany.getId());
		 * }
		 */
		Date oetDate = null;
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		if(endTime!=null&&!endTime.equals("")){
			try {
				oetDate = sim.parse(endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(oetDate!=null){
			workCompany.setOrgExpirationTime(new Timestamp(oetDate.getTime()));
		}
		workCompanyService.save(workCompany);
		workUser.setWorkCompany(workCompany);
		workUser.setStatus(2);
		workUserService.save(workUser);
		model.addAttribute("workCompany", workCompany);
		model.addAttribute("nowDate", getDateString());
		model.addAttribute("user", UserUtils.getUser());
		logUtil.saveSysLog("客服管理", "添加单位信息 " + workCompany.getCompanyName()
				+ " 成功", "");
		return "modules/customer/workCustomerComInsertT";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("insertComCustomer")
	public String insertComCustomer(Long workCompanyId, WorkLog workLog,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		workLog.setWorkCompany(workCompanyService.get(workCompanyId));
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		String detail = "";
		if (workLog.getId() != null) {
			detail = "修改工作记录成功,id为";
		} else {
			detail = "添加工作记录成功,id为";
		}
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		logUtil.saveSysLog("客服管理", detail + workLog.getId(), "");
		return "redirect:" + Global.getAdminPath() + "/work/customer/list";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping("insertComCustomerT")
	public String insertComCustomerT(Long workCompanyId, WorkLog workLog,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		workLog.setWorkCompany(workCompanyService.get(workCompanyId));
		workLog.setCreatTime(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		String detail = "";
		if (workLog.getId() != null) {
			detail = "修改工作记录成功,id为";
		} else {
			detail = "添加工作记录成功,id为";
		}
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		logUtil.saveSysLog("客服管理", detail + workLog.getId(), "");
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfoFiling/ulist";
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
		Page<WorkUser> page = workUserService.find(new Page<WorkUser>(request,
				response), workUser);
		model.addAttribute("page", page);
		return "modules/work/workUserListFilUser";
	}

	/**
	 * 系统当前时间
	 * 
	 * @return
	 */
	private String getDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date()).toString();
	}
	
	/**
	 * 咨询类用户统计
	* @Title: userStatic
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("userStatic")
	public String userStatic(Date startDate, Date endDate, Long userId,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		//客服接入方式
		String[] accessList = {"电话","QQ","QQ远程","在线工具","邮件","短信","其他"};
		//业务类型
		String[] probleTypeList = {"业务咨询","环境","驱动","key","网络","解锁","更新操作","业务系统","业务操作","其他"};
		
		model.addAttribute("users", workLogService.findAllUser());
		List<User> list = workLogService.findAllUser();
		List<StaticUserLog> logList = new ArrayList<StaticUserLog>();
		for (int i = 0; i < accessList.length; i++) {
			StaticUserLog staticUserLog = new StaticUserLog();
			staticUserLog.setAccess(accessList[i]);
			for (int j = 0; j < probleTypeList.length; j++) {
				int sum = workLogService.getSumByAccess(accessList[i], startDate, endDate, userId, probleTypeList[j]);
				switch (j) {
				case 0:
					staticUserLog.setYWZXcount(sum);
					break;
				case 1:
					staticUserLog.setHJcount(sum);
					break;
				case 2:
					staticUserLog.setQDcount(sum);
					break;
				case 3:
					staticUserLog.setKEYcount(sum);
					break;
				case 4:
					staticUserLog.setWLcount(sum);
					break;
				case 5:
					staticUserLog.setJScount(sum);
					break;
				case 6:
					staticUserLog.setGXCZcount(sum);
					break;
				case 7:
					staticUserLog.setYWXTcount(sum);
					break;
				case 8:
					staticUserLog.setYWCZcount(sum);
					break;
				case 9:
					staticUserLog.setQTcount(sum);
					break;
				}
			}
			logList.add(staticUserLog);
		}
		model.addAttribute("logList",logList);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("userId", userId);
		return "modules/customer/workCustomerUserStatic";
	}
}
