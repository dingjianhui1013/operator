/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.bean.StaticCertDateMonth;
import com.itrus.ca.modules.bean.StaticCertMonth;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.entity.CertificateSettlementStatistics;
import com.itrus.ca.modules.settle.service.CertificateSettlementStatisticsService;
import com.itrus.ca.modules.statistic.entity.StatisticCertData;

/**
 * 证书结算统计表Controller
 * @author qt
 * @version 2015-11-22
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/certificateSettlementStatistics")
public class CertificateSettlementStatisticsController extends BaseController {

	@Autowired
	private CertificateSettlementStatisticsService certificateSettlementStatisticsService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ConfigAppService configAppService;
	@Autowired
	private ConfigProductService configProductService;

	private ConfigAppOfficeRelationService configAppOfficeRelationService;


	
	@ModelAttribute
	public CertificateSettlementStatistics get(@RequestParam(required=false) Long id) {
		if (id != null){
			return certificateSettlementStatisticsService.get(id);
		}else{
			return new CertificateSettlementStatistics();
		}
	}
	
	@RequiresPermissions("settle:certificateSettlementStatistics:view")
	@RequestMapping(value = {"list", ""})
	public String list(
			CertificateSettlementStatistics certificateSettlementStatistics,
			WorkDealInfo workDealInfo,
			Long areaId,
			Long officeId,
			String startDate,
			String endDate,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) String productType,
			@RequestParam(value = "workTypes", required = false) Integer workType,
			
			Model model) {
		
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
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

		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}

		/* 应用查询列表显示 */
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("proList", ProductType.getProductTypeAutoTask());

		String product = "";
		if (productType != null && !"-1".equals(productType.toString())) {
			product = ProductType.getProductTypeName(Integer
					.parseInt(productType));
		}
		// 如果应用为空并且产品为空，则显示全部应用全部产品
		if (applyId == null
				&& (productType == null || "-1".equals(productType.toString()))) {
			String yingyong = "全部应用全部产品";
			model.addAttribute("yingyong", yingyong);
		}
		// 如果应用不为空，产品也不为空，则显示XX应用XX产品
		if (applyId != null
				&& (productType == null || !"-1".equals(productType.toString()))) {
			ConfigApp appName = configAppService.get(applyId);// 获取应用名称
			model.addAttribute("applyId", appName.getId());
			String yingyong = appName.getAppName() + "应用" + product + "产品";
			model.addAttribute("yingyong", yingyong);
		}
		// 如果应用不为空，产品为空 则显示 XX应用全部产品
		if (applyId != null
				&& (productType == null || "-1".equals(productType.toString()))) {
			ConfigApp appName = configAppService.get(applyId);// 获取应用名称
			model.addAttribute("applyId", appName.getId());
			String yingyong = appName.getAppName() + "全部产品";
			model.addAttribute("yingyong", yingyong);
		}
		// 如果应用为空，产品不为空，则显示全部应用XX产品
		if (applyId == null
				&& (productType != null && !"-1".equals(productType.toString()))) {
			String yingyong = "全部应用" + product + "产品";
			model.addAttribute("yingyong", yingyong);
		}
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListNew());
		model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		model.addAttribute("productId", productType);
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("officeId", officeId);

		if (startDate == null && endDate == null) {
			return "modules/settle/certificateSettlementStatisticsList";
		}
		Office office = officeService.get(officeId);
		List<String> monthList = getMonthList(startDate, endDate);
		model.addAttribute("monthList", getMoList(startDate, endDate));
		List<StaticCertDateMonth> sumList = new ArrayList<StaticCertDateMonth>();

		// 如果应用不为空，产品为空则根据应用+查询条件查询
		// 判断这个应用是否属于这个网点
		List<ConfigApp> configApps = null;
		if (applyId != null
				&& "-1".equals(productType.toString())
				|| (applyId != null && (productType == null || !"-1"
						.equals(productType.toString())))) {
			configApps = configAppService.findById(applyId);
		}
		// 如果应用和产品都为空，则查询所有
		if (applyId == null
				&& (productType == null || "-1".equals(productType.toString()))
				|| (applyId == null && (productType == null || !"-1"
						.equals(productType.toString())))) {
			configApps = configAppService.selectAll();
		}

		for (ConfigApp configApp : configApps) {
			StaticCertDateMonth scdm = new StaticCertDateMonth();
			scdm.setConfigApp(configApp);
			List<StaticCertMonth> scmList = new ArrayList<StaticCertMonth>();
			// for (String s : monthList) {
			for (int i = 0; i < monthList.size() - 1; i++) {
				StaticCertMonth scm = new StaticCertMonth();
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date start = sdf.parse(monthList.get(i));
					// Calendar rightNow = Calendar.getInstance();
					// rightNow.setTime(start);
					// rightNow.add(Calendar.MONTH, 1);//从当前月日到下一个月的日
					Date end = sdf.parse(monthList.get(i + 1));// rightNow.getTime();
					// 自费企业
					Integer oneAdd1 = 0;
					Integer oneAdd2 = 0;
					Integer oneAdd4 = 0;
					Integer oneAdd5 = 0;

					Integer oneRenew1 = 0;
					Integer oneRenew2 = 0;
					Integer oneRenew4 = 0;
					Integer oneRenew5 = 0;
					// List<StatisticCertData> zList =
					// statisticCertDataService.getSum(configApp, office, start,
					// end, 0);
					List<CertificateSettlementStatistics> zList = certificateSettlementStatisticsService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 0);
					for (CertificateSettlementStatistics CertificateSettlementStatistics2 : zList) {
						oneAdd1 += CertificateSettlementStatistics2.getAdd1();
						oneAdd2 += CertificateSettlementStatistics2.getAdd2();
						oneAdd4 += CertificateSettlementStatistics2.getAdd4();
						oneAdd5 += CertificateSettlementStatistics2.getAdd5();
						oneRenew1 += CertificateSettlementStatistics2.getRenew1();
						oneRenew2 += CertificateSettlementStatistics2.getRenew2();
						oneRenew4 += CertificateSettlementStatistics2.getRenew4();
						oneRenew5 += CertificateSettlementStatistics2.getRenew5();
					}
					scm.setOneAdd1(oneAdd1);
					scm.setOneAdd2(oneAdd2);
					scm.setOneAdd4(oneAdd4);
					scm.setOneAdd5(oneAdd5);
					scm.setOneRenew1(oneRenew1);
					scm.setOneRenew2(oneRenew2);
					scm.setOneRenew4(oneRenew4);
					scm.setOneRenew5(oneRenew5);
					// 合同企业
					Integer twoAdd1 = 0;
					Integer twoAdd2 = 0;
					Integer twoAdd4 = 0;
					Integer twoAdd5 = 0;
					Integer twoRenew1 = 0;
					Integer twoRenew2 = 0;
					Integer twoRenew4 = 0;
					Integer twoRenew5 = 0;
					// List<StatisticCertData> hList =
					// statisticCertDataService.getSum(configApp, office, start,
					// end, 2);
					List<CertificateSettlementStatistics> hList = certificateSettlementStatisticsService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 2);
					for (CertificateSettlementStatistics scd : hList) {
						twoAdd1 += scd.getAdd1();
						twoAdd2 += scd.getAdd2();
						twoAdd4 += scd.getAdd4();
						twoAdd5 += scd.getAdd5();
						twoRenew1 += scd.getRenew1();
						twoRenew2 += scd.getRenew2();
						twoRenew4 += scd.getRenew4();
						twoRenew5 += scd.getRenew5();
					}
					scm.setTwoAdd1(twoAdd1);
					scm.setTwoAdd2(twoAdd2);
					scm.setTwoAdd4(twoAdd4);
					scm.setTwoAdd5(twoAdd5);
					scm.setTwoRenew1(twoRenew1);
					scm.setTwoRenew2(twoRenew2);
					scm.setTwoRenew4(twoRenew4);
					scm.setTwoRenew5(twoRenew5);
					// 政府统一采购
					Integer fourAdd1 = 0;
					Integer fourAdd2 = 0;
					Integer fourAdd4 = 0;
					Integer fourAdd5 = 0;
					Integer fourRenew1 = 0;
					Integer fourRenew2 = 0;
					Integer fourRenew4 = 0;
					Integer fourRenew5 = 0;
					// List<StatisticCertData> zfList =
					// statisticCertDataService.getSum(configApp, office, start,
					// end, 1);
					List<CertificateSettlementStatistics> zfList = certificateSettlementStatisticsService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 1);
					for (CertificateSettlementStatistics scd1 : zfList) {
						fourAdd1 += scd1.getAdd1();
						fourAdd2 += scd1.getAdd2();
						fourAdd4 += scd1.getAdd4();
						fourAdd5 += scd1.getAdd5();
						fourRenew1 += scd1.getRenew1();
						fourRenew2 += scd1.getRenew2();
						fourRenew4 += scd1.getRenew4();
						fourRenew5 += scd1.getRenew5();
					}
					scm.setFourAdd1(fourAdd1);
					scm.setFourAdd2(fourAdd2);
					scm.setFourAdd4(fourAdd4);
					scm.setFourAdd5(fourAdd5);
					scm.setFourRenew1(fourRenew1);
					scm.setFourRenew2(fourRenew2);
					scm.setFourRenew4(fourRenew4);
					scm.setFourRenew5(fourRenew5);
				} catch (Exception e) {
					e.printStackTrace();
				}
				scmList.add(scm);
			}
			scdm.setCertMonths(scmList);
			sumList.add(scdm);
		}
		model.addAttribute("sumList", sumList);
		List<Integer> totalListNum = new ArrayList<Integer>();
		for (int i = 0; i < sumList.size(); i++) {
			int totalLocation = 0;
			for (int j = 0; j < sumList.get(i).getCertMonths().size(); j++) {
				 StaticCertMonth certMonth = sumList.get(i).getCertMonths().get(j);
				 
				 Integer totaladd = certMonth.getOneAdd1()+certMonth.getOneAdd2()+certMonth.getOneAdd4()+certMonth.getOneAdd5()
						 +certMonth.getTwoAdd1()+certMonth.getTwoAdd2()+certMonth.getTwoAdd4()+certMonth.getTwoAdd5()
						 +certMonth.getFourAdd1()+certMonth.getFourAdd2()+certMonth.getFourAdd4()+certMonth.getFourAdd4();
		
				 if (totalListNum.size()>totalLocation) {
					 totalListNum.set(totalLocation, totalListNum.get(totalLocation)+totaladd);
					 
				 }else{
					 totalListNum.add(totalLocation,totaladd);
				 }
				 totalLocation++;
				 
				 Integer totalupdate = certMonth.getOneRenew1()+certMonth.getOneRenew2()+certMonth.getOneRenew4()+certMonth.getOneRenew5()
						 +certMonth.getTwoRenew1()+certMonth.getTwoRenew2()+certMonth.getTwoRenew4()+certMonth.getTwoRenew5()
						 +certMonth.getFourRenew1()+certMonth.getFourRenew2()+certMonth.getFourRenew4()+certMonth.getFourRenew5();
			
				 if (totalListNum.size()>totalLocation) {
					 totalListNum.set(totalLocation, totalListNum.get(totalLocation)+totalupdate);
				 }else{
					 totalListNum.add(totalLocation, totalupdate);
				 }				
				 totalLocation++;
			}
		}
		model.addAttribute("totalListNum", totalListNum);
		return "modules/settle/certificateSettlementStatisticsList";
	}
	
	public List<String> getMonthList(String beginTime, String endTime) {
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = monthFormat.parse(beginTime);
			Date end = monthFormat.parse(endTime);
			int months = (end.getYear() - begin.getYear()) * 12
					+ (end.getMonth() - begin.getMonth());
			Calendar calen = Calendar.getInstance();
			calen.setTime(end);
			calen.add(Calendar.DATE, 1);
			for (int i = 0; i <= months; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(begin);
				calendar.add(Calendar.MONTH, i);
				if (i == 0) {
					monthList.add(beginTime);
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

	public List<String> getMoList(String beginTime, String endTime) {
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		try {
			Date begin = monthFormat.parse(beginTime);
			Date end = monthFormat.parse(endTime);
			int months = (end.getYear() - begin.getYear()) * 12
					+ (end.getMonth() - begin.getMonth());
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
	
	

	@RequiresPermissions("settle:certificateSettlementStatistics:edit")
	@RequestMapping(value = "save")
	public String save(CertificateSettlementStatistics certificateSettlementStatistics, Model model, RedirectAttributes redirectAttributes) {
		/*if (!beanValidator(model, certificateSettlementStatistics)){
			return form(certificateSettlementStatistics, model);
		}*/
		certificateSettlementStatisticsService.save(certificateSettlementStatistics);
		//addMessage(redirectAttributes, "保存证书结算统计表'" + certificateSettlementStatistics.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/certificateSettlementStatistics/?repage";
	}
	
	@RequiresPermissions("settle:certificateSettlementStatistics:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		certificateSettlementStatisticsService.delete(id);
		addMessage(redirectAttributes, "删除证书结算统计表成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/certificateSettlementStatistics/?repage";
	}

}
