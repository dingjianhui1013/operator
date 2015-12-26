/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.message.entity.MessageSending;
import com.itrus.ca.modules.message.entity.SmsConfiguration;
import com.itrus.ca.modules.message.entity.readFileByChars;
import com.itrus.ca.modules.message.service.MessageSendingService;
import com.itrus.ca.modules.message.service.SmsConfigurationService;
import com.itrus.ca.modules.message.service.SmsService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 消息发送Controller
 * 
 * @author qt
 * @version 2015-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/message/messageSending")
public class MessageSendingController extends BaseController {

	@Autowired
	private MessageSendingService messageSendingService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private SmsConfigurationService smsConfigurationService;

	@Autowired
	private SmsService smsService;

	@ModelAttribute
	public MessageSending get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return messageSendingService.get(id);
		} else {
			return new MessageSending();
		}
	}

	@RequiresPermissions("message:messageSending:view")
	@RequestMapping(value = { "list", "" })
	public String list(MessageSending messageSending, WorkDealInfo workDealInfo,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "smsId", required = false) Long smsId, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<SmsConfiguration> smsConfigurationList = smsConfigurationService.findAll();
		model.addAttribute("smsConfigurationList", smsConfigurationList);
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<Office> offsList = officeService.getOfficeByType(UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}

		if (areaId != null) {
			model.addAttribute("areaId", areaId);
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
			if (officeId != null) {
				model.addAttribute("officeId", officeId);
			}
		}
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			messageSending.setCreateBy(user);
		}

		Page<WorkDealInfo> page = workDealInfoService.find14(new Page<WorkDealInfo>(request, response), workDealInfo,
				areaId, officeId, apply, workType, certInfoList);
		if (checkIds != null) {
			String[] ids = checkIds.split(",");
			model.addAttribute("ids", ids);
		}
		model.addAttribute("checkIds", checkIds);
		// List<WorkDealInfo> noIxinInfos = page.getList();
		// List<WorkDealInfo> isIxinInfos =
		// workDealInfoService.find14A(workDealInfo, areaId, officeId, apply,
		// workType, certInfoList);
		// noIxinInfos.addAll(isIxinInfos);
		//
		// page.setList(noIxinInfos);
		model.addAttribute("dealInfoStatus", workDealInfo.getDealInfoStatus());
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListNew());
		model.addAttribute("page", page);

		model.addAttribute("offsList", offsList);
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("apply", apply);
		model.addAttribute("workType", workType);
		model.addAttribute("smsId", smsId);

		return "modules/message/messageSendingList";
	}

	@RequiresPermissions("message:messageSending:view")
	@RequestMapping(value = "send")
	@ResponseBody
	public String form(Model model, WorkDealInfo workDealInfo,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "smsId", required = false) Long smsId, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		JSONObject json = new JSONObject();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		// String
		// messageAddress=smsConfigurationService.get(smsId).getMessageAddress();
		if(smsId==null){
			json.put("status", -1);
			json.put("msg", "请选择模板");
			return json.toString();
		}
		SmsConfiguration smsConfiguration = smsConfigurationService.get(smsId);
		String messageName = smsConfiguration.getMessageName();
		// System.out.println(messageAddress);
		String messageAddress = SmsConfigurationController.class.getResource("/").toString().replace("file:", "")
				.replace("%20", " ");
		String messageContent =smsConfiguration.getMessageContent();
		if (StringUtils.contains(messageAddress, "/WEB-INF")) {
			messageAddress = messageAddress.substring(0, StringUtils.indexOf(messageAddress, "/WEB-INF"));
		}
		;
		messageAddress += "/WEB-INF/template/";
		String newMessageAddress = messageAddress + messageName;
		System.out.println(messageAddress);
		if(checkIds==""||checkIds.equals("")||checkIds==null){
			json.put("status", -1);
			json.put("msg", "请选择需要发送的公司");
			return json.toString();
		}
		String[] dealInfos = checkIds.split(",");
		short s[] = new short[dealInfos.length];
		for (int i = 0; i < dealInfos.length; i++) {
			//System.out.println(dealInfos[i]);

			// s[i]=Short.parseShort(dealInfos[i]);
			// System.out.println(s[i]);

			long dealInfoId = Long.parseLong(dealInfos[i]);
			WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
			if(dealInfo!=null){
			WorkCompany company = dealInfo.getWorkCompany();
			WorkUser workUser = dealInfo.getWorkUser();
			ConfigApp configApp = dealInfo.getConfigApp();
			WorkCertInfo workCertInfo = dealInfo.getWorkCertInfo();
			// 组织机构代码
			String companyCode = dealInfo.getWorkCompany().getOrganizationNumber();
			// 机构名称
			String companyName = dealInfo.getWorkCompany().getCompanyName();
			//System.out.println(companyName);
			// 法人姓名
			String legalName = dealInfo.getWorkCompany().getLegalName();
			// key编码
			String keySn = dealInfo.getKeySn();
			// 机构地址
			String organizationAddress = dealInfo.getWorkCompany().getProvince() + dealInfo.getWorkCompany().getCity()
					+ dealInfo.getWorkCompany().getDistrict() + dealInfo.getWorkCompany().getAddress();
			// 经办人姓名
			String consigner = dealInfo.getWorkCertInfo().getWorkCertApplyInfo().getName();
			//System.out.println(consigner);
			// 业务状态
			WorkDealInfoStatus workDealInfoStatus = new WorkDealInfoStatus();
			String businessStatus = workDealInfoStatus.WorkDealInfoStatusMap.get(dealInfo.getDealInfoStatus());
			// 项目名称
			String alias = dealInfo.getConfigApp().getAlias();
			//System.out.println(alias);
			// 证书到期时间
			Date endDate = dealInfo.getNotafter();
			//System.out.println(endDate);
			// 证书持有人电话
			String phone = dealInfo.getWorkUser().getContactPhone();
			//System.out.println(phone);
//			Properties p = new Properties();
//			p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
//			p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
//			p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, messageAddress);
//			System.out.println(p);
//			VelocityEngine velocityEngine = new VelocityEngine();
//			velocityEngine.init(p);
//			Map<String, Object> orange = new HashMap<>();
//			orange.put("companyCode", companyCode);
//			orange.put("companyName", companyName);
//			orange.put("legalName", legalName);
//			orange.put("keySn", keySn);
//			orange.put("organizationAddress", organizationAddress);
//			orange.put("consigner", consigner);
//			orange.put("businessStatus", businessStatus);
//			orange.put("alias", alias);
//			orange.put("endDate", endDate);
//			orange.put("date", new Date());
//			String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, messageName, "UTF-8", orange);
//			System.out.println(content);
//			long mess = System.currentTimeMillis();
//			String messId = "" + mess;
//			System.out.println(messId);
//			String smsSendDate1 = format.format(new Date());
//
//			Date smsSendDate = null;
//			try {
//				smsSendDate = format.parse(smsSendDate1);
//			} catch (ParseException e) {
//				
//				e.printStackTrace();
//			}
//			System.out.println(smsSendDate);
			// SmsService smsService=new SmsService();
			VelocityEngine ve = new VelocityEngine();
			   ve.init();
			   String content = messageContent;
			   VelocityContext context = new VelocityContext();
			   context.put("companyCode", companyCode);
			   context.put("companyName", companyName);
			   context.put("legalName", legalName);
			   context.put("keySn", keySn);
			   context.put("organizationAddress", organizationAddress);
			   context.put("consigner", consigner);
			   context.put("businessStatus", businessStatus);
			   context.put("alias", alias);
			   context.put("endDate", endDate);
			   context.put("date", new Date());
			   StringWriter writer = new StringWriter();
			   ve.evaluate(context, writer, "", content); 
			 
			   long mess = System.currentTimeMillis();
			String smsSendDate1 = format.format(new Date());
			String messId = "" + mess;
			Date smsSendDate = null;
				try {
					smsSendDate = format.parse(smsSendDate1);
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				System.out.println(smsSendDate);
				// SmsService smsService=new SmsService();
			String returnStatus = null;
			if (phone != null) {

				returnStatus = smsService.sendSms(messId, phone, writer.toString());
				if (returnStatus.equals("0")) {
					json.put("status", -1);
					json.put("msg", "发送失败");
					return json.toString();
				} 
					json.put("status", 1);
					json.put("msg", "发送完成");
					MessageSending messageSending = new MessageSending();
					messageSending.setMessId(messId);
					messageSending.setpId(4);
					messageSending.setPhone(phone);
					messageSending.setMessageContext(writer.toString());
					messageSending.setSmsSendDate(smsSendDate);
					messageSending.setReturnStatus(returnStatus);
					messageSending.setWorkDealInfo(dealInfo);
					messageSending.setSmsConfiguration(smsConfiguration);
					messageSending.setWorkCompany(company);
					messageSending.setWorkUser(workUser);
					messageSending.setConfigApp(configApp);
					messageSending.setWorkCertInfo(workCertInfo);
					messageSendingService.save(messageSending);
					
				
			}
		}
		}
		return json.toString();
		// return "redirect:" + Global.getAdminPath() +
		// "/modules/message/messageSending/search";
	}

	@RequiresPermissions("message:messageSending:edit")
	@RequestMapping(value = "save")
	public String save(MessageSending messageSending, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, messageSending)) {

		}
		messageSendingService.save(messageSending);
		addMessage(redirectAttributes, "保存消息发送'" + messageSending.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/modules/message/messageSending/?repage";
	}

	@RequiresPermissions("message:messageSending:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		messageSendingService.delete(id);
		addMessage(redirectAttributes, "删除消息发送成功");
		return "redirect:" + Global.getAdminPath() + "/modules/message/messageSending/?repage";
	}

	@RequiresPermissions("message:messageSending:view")
	@RequestMapping(value = "search")
	public String search(MessageSending messageSending, @RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			messageSending.setCreateBy(user);
		}
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		Page<MessageSending> page = messageSendingService.find(new Page<MessageSending>(request, response),
				messageSending, apply, startTime, endTime);

		// Page<MessageSending> page=messageSendingService.find(new
		// Page<MessageSending>(request, response), messageSending, apply,
		// StartDate, endDate);
		// Page<CheckMessage> page = checkMessageService.find(new
		// Page<CheckMessage>(request, response), checkMessage);
		model.addAttribute("page", page);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("apply", apply);

		return "modules/message/checkMessageList";
	}
	@RequestMapping(value = "showWorkDeal")
	public String showWorkDeal(Long checkMessageId, Model model, HttpServletRequest request, HttpServletResponse response) {

		List<MessageSending> page = messageSendingService.findByDepotId(checkMessageId);
		
		model.addAttribute("dealInfo", page.get(0));
		return "modules/message/checkMessageListF";
	}
}
