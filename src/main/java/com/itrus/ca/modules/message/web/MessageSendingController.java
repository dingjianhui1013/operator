/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.web;

import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.message.entity.MessageSending;
import com.itrus.ca.modules.message.service.MessageSendingService;
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
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "contactName", required = false) String contactName,
			@RequestParam(value = "dealInfoStatus", required = false) String dealInfoStatus, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		WorkCompany company = new WorkCompany();
		company.setCompanyName(companyName);
		WorkUser workUser = new WorkUser();
		workUser.setContactName(contactName);
		workDealInfo.setWorkCompany(company);
		workDealInfo.setWorkUser(workUser);

		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			messageSending.setCreateBy(user);
		}
		Page<WorkDealInfo> page = workDealInfoService.find14(new Page<WorkDealInfo>(request, response), workDealInfo,
				area, officeId, apply, workType, certInfoList);

		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListNew());
		model.addAttribute("page", page);
		List<Office> offsList = officeService.getOfficeByType(UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		if (area != null) {
			List<Office> offices = officeService.findByParentId(area);
			model.addAttribute("offices", offices);
		}

		model.addAttribute("offsList", offsList);

		model.addAttribute("offsList", offsList);
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("apply", apply);
		model.addAttribute("dealInfoStatus", dealInfoStatus);
		model.addAttribute("workType", workType);
		model.addAttribute("officeId", officeId);
		model.addAttribute("area", area);
		return "modules/message/messageSendingList";
	}

	@RequiresPermissions("message:messageSending:view")
	@RequestMapping(value = "form")
	public String form(MessageSending messageSending, Model model) {
		model.addAttribute("messageSending", messageSending);
		return "modules/message/messageSendingForm";
	}

	@RequiresPermissions("message:messageSending:edit")
	@RequestMapping(value = "save")
	public String save(MessageSending messageSending, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, messageSending)) {
			return form(messageSending, model);
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

}
