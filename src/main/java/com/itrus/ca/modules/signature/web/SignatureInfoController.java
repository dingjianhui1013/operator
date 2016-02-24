/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.service.SignatureInfoService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.vo.WorkDealInfoListVo;

/**
 * 印章Controller
 * @author CYC
 * @version 2016-02-21
 */
@Controller
@RequestMapping(value = "${adminPath}/signature/signatureInfo")
public class SignatureInfoController extends BaseController {

	@Autowired
	private SignatureInfoService signatureInfoService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	
	
	
	

	
	@RequiresPermissions("signature:signatureInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(WorkDealInfo workDealInfo, Date startTime, Date endTime,Date makeCertStartTime,Date makeCertEndTime,HttpServletRequest request,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "productName", required = false) String productName
			) throws ParseException {
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());

		model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		
		model.addAttribute("proList", ProductType.getProductTypeList());

		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
	
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("productName", productName);
		model.addAttribute("makeCertStartTime", makeCertStartTime);
		model.addAttribute("makeCertEndTime", makeCertEndTime);
		return "modules/signature/signatureInfoList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "form")
	public String form(SignatureInfo signatureInfo, Model model,Long workDealInfoId) {
		model.addAttribute("signatureInfo", signatureInfo);
		WorkDealInfo workDealInfo = workDealInfoService.get(workDealInfoId);
		ConfigApp configApp = workDealInfo.getConfigApp();
		model.addAttribute("workDealInfo", workDealInfo);
		
		model.addAttribute("configApp", configApp);
			
		
		return "modules/signature/signatureInfoForm";
	}

	
	
	
	

}
