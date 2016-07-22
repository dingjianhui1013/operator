/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.web;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.itrus.ca.modules.message.service.MessageSendingService;
import com.itrus.ca.modules.message.service.SmsConfigurationService;
import com.itrus.ca.modules.message.service.SmsService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
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
			@RequestParam(value = "smsId", required = false) Long smsId,
			@RequestParam(value = "makeCertStart", required = false) Date makeCertStart,
			@RequestParam(value = "makeCertEnd", required = false) Date makeCertEnd,
			@RequestParam(value = "expiredStart", required = false) Date expiredStart,
			@RequestParam(value = "expiredEnd", required = false) Date expiredEnd,HttpServletRequest request,
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

		model.addAttribute("dealInfoStatus", workDealInfo.getDealInfoStatus());
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListNew());

		model.addAttribute("offsList", offsList);
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("apply", apply);
		model.addAttribute("workType", workType);
		model.addAttribute("smsId", smsId);
		model.addAttribute("checkIds", checkIds);
		model.addAttribute("makeCertStart", makeCertStart);
		model.addAttribute("makeCertEnd", makeCertEnd);
		model.addAttribute("expiredStart", expiredStart);
		model.addAttribute("expiredEnd", expiredEnd);
		
		/*if(areaId==null&&officeId==null&&apply==null&&workType==null&&workDealInfo.getWorkCompany()==null){
			return "modules/message/messageSendingList";
		}
		if(workDealInfo.getWorkCompany()!=null)
		{
			if(areaId==null&&officeId==null&&apply==null&&workType==null&&workDealInfo.getWorkCompany().getDistrict()==""){
				return "modules/message/messageSendingList";
			}
		}*/
				
		if(workType ==null&&areaId==null&&officeId==null&&apply==null&&certInfoList.size()==0
		        &&makeCertStart==null&&makeCertEnd==null&&workDealInfo.getStatus()==null&&StringUtils.isEmpty(workDealInfo.getDealInfoStatus())
		        &&(workDealInfo.getWorkCompany() == null|| (workDealInfo.getWorkCompany() != null&& StringUtils.isEmpty(workDealInfo.getWorkCompany().getCompanyName())&&
		                StringUtils.isEmpty(workDealInfo.getWorkCompany().getOrganizationNumber())&&StringUtils.isEmpty(workDealInfo.getWorkCompany().getProvince())
                   && StringUtils.isEmpty(workDealInfo.getWorkCompany().getCity())
                           && StringUtils.isEmpty(workDealInfo.getWorkCompany().getDistrict())))
                           
		        &&(workDealInfo.getWorkUser() == null||(workDealInfo.getWorkUser() != null
                && StringUtils.isEmpty(workDealInfo.getWorkUser().getContactName())&& StringUtils.isEmpty(workDealInfo.getWorkUser()
                        .getConCertNumber())))
		        &&makeCertStart==null &&makeCertStart==null
		        ){
		    workDealInfo.setDealInfoStatus("5");
		}
		Page<WorkDealInfo> page = workDealInfoService.find14(new Page<WorkDealInfo>(request, response), workDealInfo,
				areaId, officeId, apply, workType, certInfoList,makeCertStart,makeCertEnd,expiredStart,expiredEnd);
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
		model.addAttribute("page", page);
		return "modules/message/messageSendingList";
	}

	@RequestMapping(value = "selectData")
	@ResponseBody
	public String selectData(
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "contactName", required = false) String contactName,
			@RequestParam(value = "dealInfoStatus", required = false) String dealInfoStatus,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "district", required = false) String district,
			Model model
			)
	{
		WorkCompany workCompany = new WorkCompany();
		workCompany.setCompanyName(companyName);
		workCompany.setProvince(province);
		workCompany.setCity(city);
		workCompany.setDistrict(district);
		WorkUser workUser = new WorkUser();
		workUser.setContactName(contactName);
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setDealInfoStatus(dealInfoStatus);
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		
		if(workType ==null&&areaId==null&&officeId==null&&apply==null&&StringUtils.isEmpty(companyName)
               &&StringUtils.isEmpty(contactName)&&StringUtils.isEmpty(dealInfoStatus)
               &&StringUtils.isEmpty(province)&&StringUtils.isEmpty(city) &&StringUtils.isEmpty(district) 
                ){
            workDealInfo.setDealInfoStatus("5");
        }
		
		
		
		List<WorkDealInfo> list = workDealInfoService.find14(workDealInfo,
				areaId, officeId, apply, workType, certInfoList);
		StringBuffer sb = new StringBuffer();
		for (WorkDealInfo workDealInfos : list) {
			if(workDealInfos.getWorkUser().getContactPhone()!=null)
			{
				sb.append(workDealInfos.getId()+",");
			}
		}
		sb=sb.replace(sb.length()-1, sb.length(), "");
		return sb.toString();
		
	}
	
	@RequiresPermissions("message:messageSending:view")
	@RequestMapping(value = "checkone")
	@ResponseBody
	public String checkone(Model model, WorkDealInfo workDealInfo,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "smsId", required = false) Long smsId, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
			JSONObject json = new JSONObject();
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		
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
		System.out.println(messageAddress);
		if(checkIds==""||checkIds.equals("")||checkIds==null){
			json.put("status", -1);
			json.put("msg", "请选择需要发送的公司");
			return json.toString();
		}
		String[] dealInfos = checkIds.split(",");
		 StringWriter writer = new StringWriter();
		 long size= dealInfos.length;
		 List<Object> errorList = new ArrayList<Object>();
		for (int i = 0; i < dealInfos.length; i++) {
			//System.out.println(dealInfos[i]);

			// s[i]=Short.parseShort(dealInfos[i]);
			// System.out.println(s[i]);
			
			long dealInfoId = Long.parseLong(dealInfos[i]);
			WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
			if(dealInfo!=null){
			 // 经办人姓名
	            String consigner = dealInfo.getWorkCertInfo().getWorkCertApplyInfo().getName();
	            if(messageContent.contains("consigner")){
	                if(StringUtils.isEmpty(consigner)){
	                    Map<String,Object> map = new HashMap<String,Object>();
	                    map.put("status", -1);
	                    map.put("meg", dealInfo.getSvn()+"的经办人姓名为空");
	                    errorList.add(map);
	                    continue;
	                }
	            }
	           
	            
			// 组织机构代码
			String companyCode = dealInfo.getWorkCompany().getOrganizationNumber();
			if(messageContent.contains("companyCode")){
			    if(StringUtils.isEmpty(companyCode)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的组织代码为空");
	                errorList.add(map);
	                continue;
	            }
			}
			
			// 机构名称
			String companyName = dealInfo.getWorkCompany().getCompanyName();
			if(messageContent.contains("companyName")){
                if(StringUtils.isEmpty(companyName)){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("status", -1);
                    map.put("meg", dealInfo.getSvn()+"的机构名称为空");
                    errorList.add(map);
                    continue;
                }
            }
			//System.out.println(companyName);
			// 法人姓名
			String legalName = dealInfo.getWorkCompany().getLegalName();
			if(messageContent.contains("legalName")){
			    if(StringUtils.isEmpty(legalName)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的法人姓名为空");
	                errorList.add(map);
	                continue;
	            }
			}
			
			// key编码
			String keySn = dealInfo.getKeySn();
			if(messageContent.contains("keySn")){
			    if(StringUtils.isEmpty(keySn)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的key序列号为空");
	                errorList.add(map);
	                continue;
	            }
			}
			
			// 机构地址
			String organizationAddress = dealInfo.getWorkCompany().getProvince() + dealInfo.getWorkCompany().getCity()
					+ dealInfo.getWorkCompany().getDistrict() + dealInfo.getWorkCompany().getAddress();
			if(messageContent.contains("organizationAddress")){
			    if(StringUtils.isEmpty(organizationAddress)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的机构地址为空");
	                errorList.add(map);
	                continue;
	            }
			}
			
			//System.out.println(consigner);
			// 业务状态
			WorkDealInfoStatus workDealInfoStatus = new WorkDealInfoStatus();
			String businessStatus = workDealInfoStatus.WorkDealInfoStatusMap.get(dealInfo.getDealInfoStatus());
			// 项目名称
			String alias = dealInfo.getConfigApp().getAlias();
			if(messageContent.contains("alias")){
                if(StringUtils.isEmpty(alias)){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("status", -1);
                    map.put("meg", dealInfo.getSvn()+"的项目名称为空");
                    errorList.add(map);
                    continue;
                }
            }
			//System.out.println(alias);
			// 证书到期时间
			Date endDate = dealInfo.getNotafter();
			if(messageContent.contains("endDate")){
			    if(endDate==null){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的证书到期时间为空");
	                errorList.add(map);
	                continue;
	            }
			}
			
			//System.out.println(endDate);
			// 证书持有人电话
			VelocityEngine ve = new VelocityEngine();
			   ve.init();
			   String content = messageContent;
			   writer = new StringWriter();
			   VelocityContext context = new VelocityContext();
			   context.put("companyCode", companyCode);
			   context.put("companyName", companyName);
			   context.put("legalName", legalName);
			   context.put("keySn", keySn);
			   context.put("organizationAddress", organizationAddress);
			   context.put("consigner", consigner);
			   context.put("businessStatus", businessStatus);
			   context.put("alias", alias);
			   context.put("endDate", endDate ==null?"":format1.format(endDate));
			   context.put("date", format1.format(new Date()));
			   ve.evaluate(context, writer, "", content); 
			   }
		}
		 json.put("status", 1);
         json.put("size", size);
         json.put("courentSize", size-errorList.size());
         json.put("content", writer.toString());
         json.put("errorList", errorList);
         json.put("errorSize", errorList.size());
		return json.toString();
		// return "redirect:" + Global.getAdminPath() +
		// "/modules/message/messageSending/search";
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
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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
		 long size= dealInfos.length;
		short s[] = new short[dealInfos.length];
		List<Object> errorList = new ArrayList<Object>();
		for (int i = 0; i < dealInfos.length; i++) {
			//System.out.println(dealInfos[i]);

			// s[i]=Short.parseShort(dealInfos[i]);
			// System.out.println(s[i]);

			long dealInfoId = Long.parseLong(dealInfos[i]);
			WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
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
			if(dealInfo!=null){
			WorkCompany company = dealInfo.getWorkCompany();
			WorkUser workUser = dealInfo.getWorkUser();
			ConfigApp configApp = dealInfo.getConfigApp();
			WorkCertInfo workCertInfo = dealInfo.getWorkCertInfo();
			// 证书持有人电话
            String phone = dealInfo.getWorkUser().getContactPhone();
            
			// 组织机构代码
			String companyCode = dealInfo.getWorkCompany().getOrganizationNumber();
			if(messageContent.contains("companyCode")){
			    if(StringUtils.isEmpty(companyCode)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的经办人姓名为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("经办人姓名为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			// 机构名称
			String companyName = dealInfo.getWorkCompany().getCompanyName();
			if(messageContent.contains("companyName")){
			    if(StringUtils.isEmpty(companyName)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的机构名称为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("机构名称为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			//System.out.println(companyName);
			// 法人姓名
			String legalName = dealInfo.getWorkCompany().getLegalName();
			if(messageContent.contains("legalName")){
			    if(StringUtils.isEmpty(legalName)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的法人姓名为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("法人姓名为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			// key编码
			String keySn = dealInfo.getKeySn();
			if(messageContent.contains("legalName")){
			    if(StringUtils.isEmpty(legalName)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的key为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("key为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			// 机构地址
			String organizationAddress = dealInfo.getWorkCompany().getProvince() + dealInfo.getWorkCompany().getCity()
					+ dealInfo.getWorkCompany().getDistrict() + dealInfo.getWorkCompany().getAddress();
			if(messageContent.contains("organizationAddress")){
			    if(StringUtils.isEmpty(organizationAddress)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的机构地址为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("机构地址为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			// 经办人姓名
			String consigner = dealInfo.getWorkCertInfo().getWorkCertApplyInfo().getName();
			if(messageContent.contains("consigner")){
			    if(StringUtils.isEmpty(consigner)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的经办人姓名为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("经办人姓名为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			//System.out.println(consigner);
			// 业务状态
			WorkDealInfoStatus workDealInfoStatus = new WorkDealInfoStatus();
			String businessStatus = workDealInfoStatus.WorkDealInfoStatusMap.get(dealInfo.getDealInfoStatus());
			// 项目名称
			String alias = dealInfo.getConfigApp().getAlias();
			if(messageContent.contains("alias")){
			    if(StringUtils.isEmpty(alias)){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的项目名称为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("项目名称为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			
			//System.out.println(alias);
			// 证书到期时间
			Date endDate = dealInfo.getNotafter();
			if(messageContent.contains("endDate")){
			    if(endDate==null){
	                Map<String,Object> map = new HashMap<String,Object>();
	                map.put("status", -1);
	                map.put("meg", dealInfo.getSvn()+"的证书到期时间为空");
	                errorList.add(map);
	                MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext("证书到期时间为空");
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus("0");
                    messageSending.setWorkDealInfo(dealInfo);
                    messageSending.setSmsConfiguration(smsConfiguration);
                    messageSending.setWorkCompany(company);
                    messageSending.setWorkUser(workUser);
                    messageSending.setConfigApp(configApp);
                    messageSending.setWorkCertInfo(workCertInfo);
                    messageSendingService.save(messageSending);
	                continue;
	            }
			}
			//System.out.println(endDate);
			
			//System.out.println(phone);
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
			   context.put("endDate", endDate==null?"":format1.format(endDate));
			   context.put("date", format1.format(new Date()));
			   StringWriter writer = new StringWriter();
			   ve.evaluate(context, writer, "", content); 
				// SmsService smsService=new SmsService();
			String returnStatus = null;
			if (phone != null) {
				returnStatus = smsService.sendSms(messId, phone, writer.toString());
				if (returnStatus.equals("0")) {
					 Map<String,Object> map = new HashMap<String,Object>();
		                map.put("status", -1);
		                map.put("msg", dealInfo.getSvn()+"发送失败");
		                errorList.add(map);
		                MessageSending messageSending = new MessageSending();
	                    messageSending.setMessId(messId);
	                    messageSending.setpId(4);
	                    messageSending.setPhone(phone);
	                    messageSending.setMessageContext(writer.toString());
	                    messageSending.setSmsSendDate(smsSendDate);
	                    messageSending.setReturnStatus(returnStatus.equals("1")?returnStatus:returnStatus.substring(1));
	                    messageSending.setWorkDealInfo(dealInfo);
	                    messageSending.setSmsConfiguration(smsConfiguration);
	                    messageSending.setWorkCompany(company);
	                    messageSending.setWorkUser(workUser);
	                    messageSending.setConfigApp(configApp);
	                    messageSending.setWorkCertInfo(workCertInfo);
	                    messageSendingService.save(messageSending);
		                
		                continue;
				} else{
				    json.put("status", 1);
                    json.put("msg", "发送完成");
                    MessageSending messageSending = new MessageSending();
                    messageSending.setMessId(messId);
                    messageSending.setpId(4);
                    messageSending.setPhone(phone);
                    messageSending.setMessageContext(writer.toString());
                    messageSending.setSmsSendDate(smsSendDate);
                    messageSending.setReturnStatus(returnStatus.equals("1")?returnStatus:returnStatus.substring(1));
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
		}
		 json.put("size", size);
         json.put("courentSize", size-errorList.size());
         json.put("errorList", errorList);
         json.put("errorSize", errorList.size());
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
	
	@ResponseBody
	@RequestMapping(value = "showWorkDeal")
	public String showWorkDeal(Long checkMessageId, Model model, HttpServletRequest request, HttpServletResponse response) {
	    JSONObject json = new JSONObject();
		List<MessageSending> page = messageSendingService.findByDepotId(checkMessageId);
		MessageSending messageSending= page.get(0);
		json.put("status", messageSending.getReturnStatus());
		json.put("content", messageSending.getMessageContext());
		return json.toString();
	}
}
