/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.settle.entity.PaymethodCertificateSettle;
import com.itrus.ca.modules.settle.service.PaymethodCertificateSettleService;
import com.itrus.ca.modules.settle.vo.Certificate;
import com.itrus.ca.modules.settle.vo.CertificateF;
import com.itrus.ca.modules.settle.vo.CertificateSettlementStatisticsVO;
import com.itrus.ca.modules.settle.vo.PaymethodCertificateSettleVo;

/**
 * 支付方式证书结算Controller
 * @author qt
 * @version 2016-01-31
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/paymethodCertificateSettle")
public class PaymethodCertificateSettleController extends BaseController {

	@Autowired
	private PaymethodCertificateSettleService paymethodCertificateSettleService;
	
	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;
	
	@ModelAttribute
	public PaymethodCertificateSettle get(@RequestParam(required=false) Long id) {
		if (id != null){
			return paymethodCertificateSettleService.get(id);
		}else{
			return new PaymethodCertificateSettle();
		}
	}
	
	@RequiresPermissions("settle:paymethodCertificateSettle:view")
	
	@RequestMapping(value = { "list", "" })
	public String list(Long areaId, Long officeId, Date startDate, Date endDate, String tempStyle,String agentId,
			boolean multiType,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) List<String> productType,
			@RequestParam(value = "workTypes", required = false) List<String> workType, Model model) {

		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<String> agentids = Lists.newArrayList();
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
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}
		if(tempStyle !=null){
			List<ConfigChargeAgent> agentList=configChargeAgentService.findByStyle(tempStyle.toString());
			model.addAttribute("agentList", agentList);
			model.addAttribute("tempStyle", tempStyle);
			model.addAttribute("agentId", agentId);
		}
		/* 应用查询列表显示 */
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("proList", ProductType.getProductTypeAutoTask());
		
		
		String product = "";
		if (productType != null && productType.size() > 0) {
			for (int i = 0; i < productType.size(); i++) {
				product += ProductType.getProductTypeName(Integer.parseInt(productType.get(i))) + ",";
			}
			StringUtils.removeEnd(product, ",");
		}

		// 如果应用不为空，产品也不为空，则显示XX应用XX产品
		if (applyId != null && (productType != null && productType.size() > 0)) {
			ConfigApp appName = configAppService.get(applyId);// 获取应用名称
			model.addAttribute("applyId", appName.getId());
			String yingyong = appName.getAppName() + "应用[" + product + "]产品";
			model.addAttribute("yingyong", yingyong);
		}
		// 如果应用不为空，产品为空 则显示 XX应用全部产品
		if (applyId != null && (productType == null || productType.size() == 0)) {
			ConfigApp appName = configAppService.get(applyId);// 获取应用名称
			model.addAttribute("applyId", appName.getId());
			String yingyong = appName.getAppName() + "全部产品";
			model.addAttribute("yingyong", yingyong);
		}
		// // 如果应用为空，产品不为空，则显示全部应用XX产品
		// if (applyId == null && (productType != null && productType.length >
		// 0)) {
		// String yingyong = "全部应用" + product + "产品";
		// model.addAttribute("yingyong", yingyong);
		// }
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListLess());
		// model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		model.addAttribute("productId", productType);
		model.addAttribute("multiType",multiType);
		model.addAttribute("workType", workType);
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		

		model.addAttribute("officeId", officeId);
		// 开始日期，结束日期，应用必须选择
		if (startDate == null || endDate == null || applyId == null) {
			model.addAttribute("startDate", DateUtils.firstDayOfMonth(new Date()));
			model.addAttribute("endDate", new Date());
 			return "modules/settle/paymethodCertificateSettleList";
		} else {
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
		}
		List<Long> officeIdList = new ArrayList<Long>();

		if (officeId != null) {
			officeIdList.add(officeId);
		} else if (areaId != null) {
			officeIdList = officeService.findOfficeIdsByParentId(areaId);
		}
		List<PaymethodCertificateSettleVo> findWorkList1 = paymethodCertificateSettleService.findMulitWorkList1(
				applyId, org.springframework.util.StringUtils.collectionToCommaDelimitedString(productType),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(workType),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(officeIdList), agentId, startDate,
				endDate);

		HashMap<String, Certificate> monthMap1 = paymethodCertificateSettleService.getStaticMap1(findWorkList1);
		
		
		model.addAttribute("list", findWorkList1);
		model.addAttribute("monthList", new ArrayList<String>(monthMap1.keySet()));
		model.addAttribute("sumList", monthMap1);	
		
		return "modules/settle/paymethodCertificateSettleList";
	}
	
	public List<String> getMonthList(Date begin, Date end) {
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// Date begin = monthFormat.parse(beginTime);
			// Date end = monthFormat.parse(endTime);
			int months = (end.getYear() - begin.getYear()) * 12 + (end.getMonth() - begin.getMonth());
			Calendar calen = Calendar.getInstance();
			calen.setTime(end);
			calen.add(Calendar.DATE, 1);
			for (int i = 0; i <= months; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(begin);
				calendar.add(Calendar.MONTH, i);
				if (i == 0) {
					monthList.add(monthFormat.format(begin));
				} else {
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					monthList.add(monthFormat.format(calendar.getTime()));
				}

			}
			monthList.add(monthFormat.format(calen.getTime()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return monthList;
	}

	public List<String> getMoList(Date begin, Date end) {
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		try {
			// Date begin = monthFormat.parse(beginTime);
			// Date end = monthFormat.parse(endTime);
			int months = (end.getYear() - begin.getYear()) * 12 + (end.getMonth() - begin.getMonth());
			for (int i = 0; i <= months; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(begin);
				calendar.add(Calendar.MONTH, i);
				monthList.add(monthFormat.format(calendar.getTime()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return monthList;
	}
	@RequiresPermissions("settle:paymethodCertificateSettle:view")
	@RequestMapping(value = "form")
	public String form(PaymethodCertificateSettle paymethodCertificateSettle, Model model) {
		model.addAttribute("paymethodCertificateSettle", paymethodCertificateSettle);
		return "modules/settle/paymethodCertificateSettleForm";
	}

	@RequiresPermissions("settle:paymethodCertificateSettle:edit")
	@RequestMapping(value = "save")
	public String save(PaymethodCertificateSettle paymethodCertificateSettle, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, paymethodCertificateSettle)){
			return form(paymethodCertificateSettle, model);
		}
		paymethodCertificateSettleService.save(paymethodCertificateSettle);
		addMessage(redirectAttributes, "保存支付方式证书结算'" + paymethodCertificateSettle.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/paymethodCertificateSettle/?repage";
	}
	
	@RequiresPermissions("settle:paymethodCertificateSettle:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		paymethodCertificateSettleService.delete(id);
		addMessage(redirectAttributes, "删除支付方式证书结算成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/paymethodCertificateSettle/?repage";
	}

}
