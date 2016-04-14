package com.itrus.ca.modules.work.web;

import java.text.ParseException;
import java.util.Date;

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

import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

@Controller
@RequestMapping(value = "${adminPath}/work/archives")
public class WorkArchivesController extends BaseController {
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
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
	public String list(WorkDealInfo workDealInfo, Date startTime, Date endTime,Date makeCertStartTime,Date makeCertEndTime,HttpServletRequest request,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "productName", required = false) String productName
			) throws ParseException {
		
		return "modules/work/workDealInfoList";
	}
	
	
	
	
	
	
	
	
	
}
