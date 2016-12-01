/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.entity.WorkDealInfoTypeObj;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.CommonAttach;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.CommonAttachService;
import com.itrus.ca.modules.sys.service.OfficeService;
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
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private CommonAttachService attachService;
	
	@Autowired
	private ConfigAppService configAppService;
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
	public String list(WorkDealInfo workDealInfo, 
			HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "area", required = false) Long area,//受理区域
			@RequestParam(value = "officeId", required = false) Long officeId,//受理网点
			@RequestParam(value = "certType", required = false) String certType,//产品名称
			@RequestParam(value = "workType", required = false) Integer workType,//业务类型
			@RequestParam(value = "payMethod", required = false) Integer payMethod//付款方式
			) {
		
		//默认条件 当前网点
		if(officeId==null){
			if(UserUtils.getUser().getOffice()!=null){
				officeId = UserUtils.getUser().getOffice().getId();
			}
		}
		
		Map<String, String> queryStr = new HashMap<String, String>();
		queryStr.put("area", area == null ? "" : area.toString());
		queryStr.put("officeId", officeId == null ? "" : officeId.toString());
		queryStr.put("certType", certType == null ? "" : certType.toString());
		queryStr.put("workType", workType == null ? "" : workType.toString());
		queryStr.put("payMethod", payMethod == null ? "" : payMethod.toString());
		
		if (payMethod != null) {
			WorkPayInfo workPayInfo = new WorkPayInfo();
			if (payMethod==1) {
				workPayInfo.setMethodMoney(true);
			}
			if (payMethod==2) {
				workPayInfo.setMethodPos(true);
			}
			if (payMethod==3) {
				workPayInfo.setMethodBank(true);
			}
			if (payMethod==4) {
				workPayInfo.setMethodAlipay(true);
			}
			if (payMethod==5) {
				workPayInfo.setMethodGov(true);
			}
			if (payMethod==6) {
				workPayInfo.setMethodContract(true);
			}
			workDealInfo.setWorkPayInfo(workPayInfo);
		}
		
		//受理信息
		List<Office> offsList = officeService.getOfficeByType(
				UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		model.addAttribute("offsList", offsList);
		if (area != null) {
			List<Office> offices = officeService.findByParentId(area);
			model.addAttribute("offices", offices);
		}
		ProductType productType = new ProductType();
		model.addAttribute("certTypes", productType.getProductTypeList());
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		model.addAttribute("workTypes", workDealInfoType.getProductTypeList());
		
		String url = "modules/work/workDealInfoFilingListF";
		
		Page<WorkDealInfo> page = workDealInfoService.findByGuiDang(
				request, response, workDealInfo,queryStr,false);
		
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
		model.addAttribute("area", area);
		model.addAttribute("officeId", officeId);
		model.addAttribute("certType", certType);
		model.addAttribute("workType", workType);
		model.addAttribute("payMethod", payMethod);
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
	public String ulist(WorkLog workLog,Date createStart,Date createEnd,HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<WorkLog> page = workLogService.findKpList(new Page<WorkLog>(
				request, response), workLog,createStart,createEnd);
		model.addAttribute("page", page);
		List<ConfigApp> appNames=configAppService.findAllConfigApp();
		model.addAttribute("configApp", appNames);
		model.addAttribute("createStart", createStart);
		model.addAttribute("createEnd", createEnd);
		return "modules/work/workDealInfoFilingListT";
	}
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = {"flist"})
	public String flist(WorkLog workLog, Date createStart, Date createEnd, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<WorkLog> page = workLogService.findKpList(new Page<WorkLog>(
				request, response), workLog,null,null);
		model.addAttribute("createStart", createStart);
		model.addAttribute("createEnd", createEnd);
		model.addAttribute("page", page);
		List<ConfigApp> configApp= configAppService.findAllConfigApp();
		model.addAttribute("configApp", configApp);
		return "modules/work/workDealInfoFilingListFuzzy";
	}
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "plist" })
	public String plist(WorkLog workLog, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<WorkLog> page = workLogService.findKpList(new Page<WorkLog>(
				request, response), workLog,null,null);
		model.addAttribute("page", page);
		List<ConfigApp> configApp= configAppService.findAllConfigApp();
		model.addAttribute("configApp", configApp);
		return "modules/work/workDealInfoFilingListProject";
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
		//显示图片
		List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
		String url = Global.getConfig("images.path");
		if(attachs!=null&&attachs.size()>0){
			String imgNames = "";
			for(int i =0;i<attachs.size();i++){
				if(i==0){
					imgNames+=url+"/"+attachs.get(0).getAttachName();
				}else{
					imgNames+=","+url+"/"+attachs.get(i).getAttachName();	
				}
			}
			model.addAttribute("imgNames", imgNames);
		}
		return "modules/work/workDealInfoFilingFormF";
	}

	@RequestMapping("fromApp")
	public String fromApp(WorkDealInfo workDealInfo, Model model) {
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("workDealInfo", workDealInfo);
		return "modules/work/workDealInfoFilingFormA";
	}
	
	@RequestMapping(value = "exportSerial")
	public void exportSerial( WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "area", required = false) Long area,//受理区域
			@RequestParam(value = "officeId", required = false) Long officeId,//受理网点
			@RequestParam(value = "certType", required = false) String certType,//产品名称
			@RequestParam(value = "workType", required = false) Integer workType,//业务类型
			@RequestParam(value = "payMethod", required = false) Integer payMethod,//付款方式
			@RequestParam(value = "payType", required = false) String payType,//计费策略
			@RequestParam(value = "province", required = false) String province,//省
			@RequestParam(value = "city", required = false) String city,//市
			@RequestParam(value = "district", required = false) String district,//县
			@RequestParam(value = "keySn", required = false) String keySn,//Key序列号
			@RequestParam(value = "companyName", required = false) String companyName,//单位名称
			@RequestParam(value = "contactName", required = false) String contactName,//联系人姓名
			@RequestParam(value = "contactTel", required = false) String contactTel,//固定电话
			@RequestParam(value = "contactPhone", required = false) String contactPhone,//移动电话
			@RequestParam(value = "status", required = false) String status//归档状态
			
			) throws Exception {
		//初始化workDealInfo
		WorkCompany workCompany = new WorkCompany();
		workCompany.setCompanyName(companyName);
		workCompany.setProvince(province);
		workCompany.setCity(city);
		workCompany.setDistrict(district);
		workDealInfo.setWorkCompany(workCompany);
		
		//联系人姓名 联系人电话 固定电话
		WorkUser workUser = new WorkUser();
		workUser.setContactName(contactName);
		workUser.setContactTel(contactTel);
		workUser.setContactPhone(contactPhone);
		workDealInfo.setWorkUser(workUser);
		
		workDealInfo.setStatus(status==null?null:Integer.parseInt(status));
		workDealInfo.setKeySn(keySn);

		//默认条件 当前网点
		if(officeId==null){
			if(UserUtils.getUser().getOffice()!=null){
				officeId = UserUtils.getUser().getOffice().getId();
			}
		}
		
		//查询条件处理
		Map<String, String> queryStr = new HashMap<String, String>();
		queryStr.put("area", area == null ? "" : area.toString());
		queryStr.put("officeId", officeId == null ? "" : officeId.toString());
		queryStr.put("certType", certType == null ? "" : certType.toString());
		queryStr.put("workType", workType == null ? "" : workType.toString());
		queryStr.put("payMethod", payMethod == null ? "" : payMethod.toString());
		
		if (payMethod != null) {
			WorkPayInfo workPayInfo = new WorkPayInfo();
			if (payMethod==1) {
				workPayInfo.setMethodMoney(true);
			}
			if (payMethod==2) {
				workPayInfo.setMethodPos(true);
			}
			if (payMethod==3) {
				workPayInfo.setMethodBank(true);
			}
			if (payMethod==4) {
				workPayInfo.setMethodAlipay(true);
			}
			if (payMethod==5) {
				workPayInfo.setMethodGov(true);
			}
			if (payMethod==6) {
				workPayInfo.setMethodContract(true);
			}
			workDealInfo.setWorkPayInfo(workPayInfo);
		}
		
		//受理信息
		List<Office> offsList = officeService.getOfficeByType(
				UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		List<Office> offices = null;
		if (area != null) {
			offices = officeService.findByParentId(area);
		}
		
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		
		Page<WorkDealInfo> page = workDealInfoService.findByGuiDang(
				request, response, workDealInfo,queryStr,true);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		HSSFWorkbook wb = new HSSFWorkbook();// 定义工作簿
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		Cell cell = null;
		HSSFSheet sheet = wb.createSheet("归档信息");

		HSSFRow row = sheet.createRow(0);
		sheet.setDefaultColumnWidth(11);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));// 合并单元格
		HSSFFont font = wb.createFont();
		// 第一行数据
		HSSFRow row0 = sheet.createRow(0);
		cell = row0.createCell(0);
		HSSFFont font0 = wb.createFont();
		HSSFCellStyle style0 = wb.createCellStyle();
		font0.setFontHeightInPoints((short) 18);
		style0.setFont(font0);
		style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellStyle(style0);
		cell.setCellValue("归档信息明细");
		// 第二行数据
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 11));
		HSSFRow row1 = sheet.createRow(1);
		cell = row1.createCell(1);
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellValue("统计时间：" + DateUtils.formatDate(new Date(), "yyyy-MM-dd") + "—"
				+ DateUtils.formatDate(new Date(), "yyyy-MM-dd"));

		/*Key序列号： 单位名称： 联系人姓名：
		      固定电话： 移动电话：       
		      受理区域：  受理网点：  企业证书 
		      业务类型：  计费策略类型：  行政所属区： 省份 地级市   市、县级市 	
		      付款方式 ： 
	    */
		//显示查询条件
		HSSFRow row2 = sheet.createRow(2);
		row2.createCell(0).setCellValue("Key序列号：");
		row2.createCell(1).setCellValue(keySn);
		row2.createCell(2).setCellValue("单位名称：");
		row2.createCell(3).setCellValue(companyName);
		row2.createCell(4).setCellValue("联系人姓名：");
		row2.createCell(5).setCellValue(contactName);
		
		HSSFRow row3 = sheet.createRow(3);
		row3.createCell(0).setCellValue("固定电话：");
		row3.createCell(1).setCellValue(contactTel);
		row3.createCell(2).setCellValue("移动电话：");
		row3.createCell(3).setCellValue(contactPhone);

		HSSFRow row4 = sheet.createRow(4);
		row4.createCell(0).setCellValue("受理区域：");
		if(area!=null){
			for(Office o: offsList){
				if(o.getId().equals(area)){
					row4.createCell(1).setCellValue(o.getName());
				}
			}
		}
		row4.createCell(2).setCellValue("受理网点：");
		if(officeId!=null&&offices!=null){
			for(Office o: offices){
				if(o.getId().equals(officeId)){
					row4.createCell(3).setCellValue(o.getName());
				}
			}
		}
		row4.createCell(4).setCellValue("产品名称：");
		List<ProductTypeObj> productObj = productType.getProductTypeList();
		if(certType!=null){
			for(ProductTypeObj o: productObj){
				if((o.getId()+"").equals(certType)){
					row4.createCell(5).setCellValue(o.getName());
				}
			}
		}
		
		HSSFRow row5 = sheet.createRow(5);
		row5.createCell(0).setCellValue("业务类型：");
		List<WorkDealInfoTypeObj> workObj = workDealInfoType.getProductTypeList();
		if(workType!=null){
			for(WorkDealInfoTypeObj o: workObj){
				if(o.getId().equals(workType)){
					row5.createCell(1).setCellValue(o.getName());
				}
			}
		}
		row5.createCell(2).setCellValue("计费策略类型：");
		if(payType!=null){
			if(payType.equals("1")){
				row5.createCell(3).setCellValue("标准");
			}else if(payType.equals("2")){
				row5.createCell(3).setCellValue("政府统一采购");
			}else if(payType.equals("3")){
				row5.createCell(3).setCellValue("合同采购");
			}
		}
		row5.createCell(4).setCellValue("行政所属区：");
		row5.createCell(5).setCellValue("省份");
		row5.createCell(6).setCellValue(province);
		row5.createCell(7).setCellValue("地级市");
		row5.createCell(8).setCellValue(city);
		row5.createCell(9).setCellValue("市、县级市");
		row5.createCell(10).setCellValue(district);
		
		HSSFRow row6 = sheet.createRow(6);
		row6.createCell(0).setCellValue("付款方式：");
		if(payMethod!=null){
			if(payMethod==0l){
				row6.createCell(1).setCellValue("全部");
			}else if(payMethod==1l){
				row6.createCell(1).setCellValue("现金");
			}else if(payMethod==2l){
				row6.createCell(1).setCellValue("POS收款");
			}else if(payMethod==3l){
				row6.createCell(1).setCellValue("银行转帐");
			}else if(payMethod==4l){
				row6.createCell(1).setCellValue("支付宝转账");
			}else if(payMethod==5l){
				row6.createCell(1).setCellValue("政府统一采购");
			}else if(payMethod==6l){
				row6.createCell(1).setCellValue("合同采购");
			}
		}
		
		HSSFRow row7 = sheet.createRow(7);
		row7.createCell(0).setCellValue("编号");
		row7.createCell(1).setCellValue("单位名称");
		row7.createCell(2).setCellValue("应用名称");
		row7.createCell(3).setCellValue("业务类型");
		row7.createCell(4).setCellValue("证书类型");
		row7.createCell(5).setCellValue("归档人");
		row7.createCell(6).setCellValue("归档日期");
		row7.createCell(7).setCellValue("归档编号");

		List<WorkDealInfo> list = page.getList();
		for (int i = 0; i < list.size(); i++) {
			HSSFRow rown = sheet.createRow(i + 8);
			rown.createCell(0).setCellValue(i + 1);
			WorkDealInfo dealInfo = list.get(i);
			if(dealInfo!=null){
				if(dealInfo.getWorkCompany().getCompanyName()==null){
					rown.createCell(1).setCellValue("");
				}else{
					rown.createCell(1).setCellValue(dealInfo.getWorkCompany().getCompanyName());
				}
				
				if(dealInfo.getConfigApp().getAppName()==null){
					rown.createCell(2).setCellValue("");
				}else{
					rown.createCell(2).setCellValue(dealInfo.getConfigApp().getAppName());
				}
				//业务类型
				String infoType = "";
				if(dealInfo.getDealInfoType()!=null){
					infoType += WorkDealInfoType.WorkDealInfoTypeMap.get(dealInfo.getDealInfoType())+" ";
				}
				if(dealInfo.getDealInfoType1()!=null){
					infoType += WorkDealInfoType.WorkDealInfoTypeMap.get(dealInfo.getDealInfoType1())+" ";
				}
				if(dealInfo.getDealInfoType2()!=null){
					infoType += WorkDealInfoType.WorkDealInfoTypeMap.get(dealInfo.getDealInfoType2())+" ";
				}
				if(dealInfo.getDealInfoType3()!=null){
					infoType += WorkDealInfoType.WorkDealInfoTypeMap.get(dealInfo.getDealInfoType3())+" ";
				}
				rown.createCell(3).setCellValue(infoType);
				//证书类型
				if(dealInfo.getConfigProduct().getProductName()==null){
					rown.createCell(4).setCellValue("");
				}else{
					String dealType = dealInfo.getConfigProduct().getProductName();
					if(dealType!=null){
						if(dealType.equals("1")){
							rown.createCell(4).setCellValue("企业证书");
						}else if(dealType.equals("2")){
							rown.createCell(4).setCellValue("个人证书(企业)");
						}else if(dealType.equals("3")){
							rown.createCell(4).setCellValue("机构证书");
						}else if(dealType.equals("4")){
							rown.createCell(4).setCellValue("可信移动设备");
						}else if(dealType.equals("5")){
							rown.createCell(4).setCellValue("个人证书(机构)");
						}
					}
				}
				//归档人 归档日期 归档编号
				if(dealInfo.getUpdateBy().getName()==null){
					rown.createCell(5).setCellValue("");
				}else{
					rown.createCell(5).setCellValue(dealInfo.getUpdateBy().getName());
				}
				if(dealInfo.getArchiveDate()==null){
					rown.createCell(6).setCellValue("");
				}else{
					rown.createCell(6).setCellValue(dealInfo.getArchiveDate());
				}
				if(dealInfo.getUserSn()==null){
					rown.createCell(7).setCellValue("");
				}else{
					rown.createCell(7).setCellValue(dealInfo.getUserSn());
				}
			}

		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=pigeonhole.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

}
