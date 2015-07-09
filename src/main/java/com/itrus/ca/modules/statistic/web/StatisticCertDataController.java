/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.bean.StaticCertDateDay;
import com.itrus.ca.modules.bean.StaticCertDateMonth;
import com.itrus.ca.modules.bean.StaticCertDay;
import com.itrus.ca.modules.bean.StaticCertMonth;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.statistic.entity.StatisticCertData;
import com.itrus.ca.modules.statistic.service.StatisticCertDataService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;


/**
 * 证书发放统计Controller
 * 
 * @author HUHAO
 * @version 2014-07-08
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/statisticCertData")
public class StatisticCertDataController extends BaseController {

	@Autowired
	private StatisticCertDataService statisticCertDataService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigAppService configAppService;
	@Autowired
	private ConfigProductService configProductService;

	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@ModelAttribute
	public StatisticCertData get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return statisticCertDataService.get(id);
		} else {
			return new StatisticCertData();
		}
	}

	@RequiresPermissions("statistic:statisticCertData:view")
	@RequestMapping(value = { "list", "" })
	public String list(
			StatisticCertData statisticCertData,
			Long areaId,
			Long officeId,
			String startDate,
			String endDate,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) String productType,
			Model model) {
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

		model.addAttribute("productId", productType);
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("officeId", officeId);

		if (startDate == null && endDate == null) {
			return "modules/statistic/statisticCertDataList";
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
					List<StatisticCertData> zList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 0);
					for (StatisticCertData statisticCertData2 : zList) {
						oneAdd1 += statisticCertData2.getAdd1();
						oneAdd2 += statisticCertData2.getAdd2();
						oneAdd4 += statisticCertData2.getAdd4();
						oneAdd5 += statisticCertData2.getAdd5();
						oneRenew1 += statisticCertData2.getRenew1();
						oneRenew2 += statisticCertData2.getRenew2();
						oneRenew4 += statisticCertData2.getRenew4();
						oneRenew5 += statisticCertData2.getRenew5();
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
					List<StatisticCertData> hList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 2);
					for (StatisticCertData scd : hList) {
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
					List<StatisticCertData> zfList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 1);
					for (StatisticCertData scd1 : zfList) {
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
		return "modules/statistic/statisticCertDataList";
	}

	@RequiresPermissions("statistic:statisticCertData:view")
	@RequestMapping(value = "form")
	public String form(StatisticCertData statisticCertData, Model model) {
		model.addAttribute("statisticCertData", statisticCertData);
		return "modules/statistic/statisticCertDataForm";
	}

	@RequiresPermissions("statistic:statisticCertData:edit")
	@RequestMapping(value = "save")
	public String save(StatisticCertData statisticCertData, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, statisticCertData)) {
			return form(statisticCertData, model);
		}
		statisticCertDataService.save(statisticCertData);
		// addMessage(redirectAttributes, "保存证书发放统计'" +
		// statisticCertData.getName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/statistic/statisticCertData/?repage";
	}

	@RequiresPermissions("statistic:statisticCertData:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		statisticCertDataService.delete(id);
		addMessage(redirectAttributes, "删除证书发放统计成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/statistic/statisticCertData/?repage";
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

	@RequiresPermissions("statistic:statisticCertData:view")
	@RequestMapping(value = { "showDays" })
	public String showDays(Long applyId, String productId, String startDate,
			String endDate, String month, Long officeId, Model model) {
		Office office = officeService.get(officeId);
		List<String> dayLists = getDayLists(startDate, endDate, month);
		List<String> dayList = getDayList(startDate, endDate, month);// 遍历出来每天
		List<StaticCertDateDay> sumList = new ArrayList<StaticCertDateDay>();
		List<ConfigApp> configApps = null;
		if (applyId != null
				&& "-1".equals(productId.toString())
				|| (applyId != null && (productId == null || !"-1"
						.equals(productId.toString())))) {
			configApps = configAppService.findById(applyId);
		}
		// 如果应用和产品都为空，则查询所有
		if (applyId == null
				&& (productId == null || "-1".equals(productId.toString()))
				|| (applyId == null && (productId == null || !"-1"
						.equals(productId.toString())))) {
			configApps = configAppService.selectAll();
		}
		// List<ConfigApp> configApps = configAppService.selectAll();
		for (ConfigApp configApp : configApps) {
			StaticCertDateDay scdd = new StaticCertDateDay();
			scdd.setConfigApp(configApp);
			List<StaticCertDay> scdList = new ArrayList<StaticCertDay>();
			for (int i = 0; i < dayList.size() - 1; i++) {
				StaticCertDay scd = new StaticCertDay();
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// Calendar rightNow = Calendar.getInstance();
					// rightNow.setTime(months);
					// rightNow.add(Calendar.MONTH, 1);
					// Date end = rightNow.getTime();
					Date start = sdf.parse(dayList.get(i));
					Date end = sdf.parse(dayList.get(i + 1));
					// 自费企业
					Integer oneAdd1 = 0;
					Integer oneAdd2 = 0;
					Integer oneAdd4 = 0;
					Integer oneAdd5 = 0;
					Integer oneRenew1 = 0;
					Integer oneRenew2 = 0;
					Integer oneRenew4 = 0;
					Integer oneRenew5 = 0;
					List<StatisticCertData> zList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productId),
									office, start, end, 0);
					for (StatisticCertData statisticCertData2 : zList) {
						oneAdd1 += statisticCertData2.getAdd1();
						oneAdd2 += statisticCertData2.getAdd2();
						oneAdd4 += statisticCertData2.getAdd4();
						oneAdd5 += statisticCertData2.getAdd5();
						oneRenew1 += statisticCertData2.getRenew1();
						oneRenew2 += statisticCertData2.getRenew2();
						oneRenew4 += statisticCertData2.getRenew4();
						oneRenew5 += statisticCertData2.getRenew5();
					}
					scd.setOneAdd1(oneAdd1);
					scd.setOneAdd2(oneAdd2);
					scd.setOneAdd4(oneAdd4);
					scd.setOneAdd5(oneAdd5);
					scd.setOneRenew1(oneRenew1);
					scd.setOneRenew2(oneRenew2);
					scd.setOneRenew4(oneRenew4);
					scd.setOneRenew5(oneRenew5);
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
					List<StatisticCertData> hList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productId),
									office, start, end, 2);
					for (StatisticCertData scds : hList) {
						twoAdd1 += scds.getAdd1();
						twoAdd2 += scds.getAdd2();
						twoAdd4 += scds.getAdd4();
						twoAdd5 += scds.getAdd5();
						twoRenew1 += scds.getRenew1();
						twoRenew2 += scds.getRenew2();
						twoRenew4 += scds.getRenew4();
						twoRenew5 += scds.getRenew5();
					}
					scd.setTwoAdd1(twoAdd1);
					scd.setTwoAdd2(twoAdd2);
					scd.setTwoAdd4(twoAdd4);
					scd.setTwoAdd5(twoAdd5);
					scd.setTwoRenew1(twoRenew1);
					scd.setTwoRenew2(twoRenew2);
					scd.setTwoRenew4(twoRenew4);
					scd.setTwoRenew5(twoRenew5);
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
					List<StatisticCertData> zfList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productId),
									office, start, end, 1);
					for (StatisticCertData scd1 : zfList) {
						fourAdd1 += scd1.getAdd1();
						fourAdd2 += scd1.getAdd2();
						fourAdd4 += scd1.getAdd4();
						fourAdd5 += scd1.getAdd5();
						fourRenew1 += scd1.getRenew1();
						fourRenew2 += scd1.getRenew2();
						fourRenew4 += scd1.getRenew4();
						fourRenew5 += scd1.getRenew5();
					}
					scd.setFourAdd1(fourAdd1);
					scd.setFourAdd2(fourAdd2);
					scd.setFourAdd4(fourAdd4);
					scd.setFourAdd5(fourAdd5);
					scd.setFourRenew1(fourRenew1);
					scd.setFourRenew2(fourRenew2);
					scd.setFourRenew4(fourRenew4);
					scd.setFourRenew5(fourRenew5);
				} catch (Exception e) {
					e.printStackTrace();
				}
				scdList.add(scd);
			}
			scdd.setCertDays((scdList));
			sumList.add(scdd);

			// List<StaticCertDay> zCertDays = new ArrayList<StaticCertDay>();
			// List<StatisticCertData> zList =
			// statisticCertDataService.getSum1(configApp,
			// Integer.parseInt(productId), office, start, end, 2);
			// List<StatisticCertData> zList =
			// statisticCertDataService.getSum(configApp, office, months, end,
			// 0);
			// StaticCertDay scd3 = new StaticCertDay();
			// Integer add1 = 0;
			// Integer add2 = 0;
			// Integer add4 = 0;
			// Integer add5 = 0;
			// Integer renew1 = 0;
			// Integer renew2 = 0;
			// Integer renew4 = 0;
			// Integer renew5 = 0;
			// if(zList.size()!=0){
			// for (StatisticCertData statisticCertData2 : zList) {
			// StaticCertDay scd = new StaticCertDay();
			// scd.setAdd1(statisticCertData2.getAdd1());
			// scd.setAdd2(statisticCertData2.getAdd2());
			// scd.setAdd4(statisticCertData2.getAdd4());
			// scd.setAdd5(statisticCertData2.getAdd5());
			// scd.setRenew1(statisticCertData2.getRenew1());
			// scd.setRenew2(statisticCertData2.getRenew2());
			// scd.setRenew4(statisticCertData2.getRenew4());
			// scd.setRenew5(statisticCertData2.getRenew5());
			// scd.setDay(statisticCertData2.getCountDate());
			// zCertDays.add(scd);
			// }
			// }else{
			// scd3.setAdd1(add1);
			// scd3.setAdd2(add2);
			// scd3.setAdd4(add4);
			// scd3.setAdd5(add5);
			// scd3.setRenew1(renew1);
			// scd3.setRenew2(renew2);
			// scd3.setRenew4(renew4);
			// scd3.setRenew5(renew5);
			// zCertDays.add(scd3);
			// }
			// //合同企业
			// List<StaticCertDay> hCertDays = new ArrayList<StaticCertDay>();
			// // List<StatisticCertData> hList =
			// statisticCertDataService.getSum(configApp, office, months, end,
			// 1);
			// List<StatisticCertData> hList =
			// statisticCertDataService.getSum1(configApp,
			// Integer.parseInt(productId), office, start, end, 2);
			// if(hList.size()!=0){
			// for(StatisticCertData scd : hList) {
			// StaticCertDay scd1 = new StaticCertDay();
			// scd1.setAdd1(scd.getAdd1());
			// scd1.setAdd2(scd.getAdd2());
			// scd1.setAdd4(scd.getAdd4());
			// scd1.setAdd5(scd.getAdd5());
			// scd1.setRenew1(scd.getRenew1());
			// scd1.setRenew2(scd.getRenew2());
			// scd1.setRenew4(scd.getRenew4());
			// scd1.setRenew5(scd.getRenew5());
			// hCertDays.add(scd1);
			// }
			// }else{
			// scd3.setAdd1(add1);
			// scd3.setAdd2(add2);
			// scd3.setAdd4(add4);
			// scd3.setAdd5(add5);
			// scd3.setRenew1(renew1);
			// scd3.setRenew2(renew2);
			// scd3.setRenew4(renew4);
			// scd3.setRenew5(renew5);
			// hCertDays.add(scd3);
			// }
			//
			// //政府统一采购
			// List<StaticCertDay> zfCertDays = new ArrayList<StaticCertDay>();
			// // List<StatisticCertData> zfList =
			// statisticCertDataService.getSum(configApp, office, months, end,
			// 2);
			// List<StatisticCertData> zfList =
			// statisticCertDataService.getSum1(configApp,
			// Integer.parseInt(productId), office, start, end, 2);
			// if(zfList.size()!=0){
			// for (StatisticCertData scd1 : zfList) {
			// StaticCertDay scd2 = new StaticCertDay();
			// scd2.setAdd1(scd1.getAdd1());
			// scd2.setAdd2(scd1.getAdd2());
			// scd2.setAdd4(scd1.getAdd4());
			// scd2.setAdd5(scd1.getAdd5());
			// scd2.setRenew1(scd1.getRenew1());
			// scd2.setRenew2(scd1.getRenew2());
			// scd2.setRenew4(scd1.getRenew4());
			// scd2.setRenew5(scd1.getRenew5());
			// zfCertDays.add(scd2);
			// }
			// }else{
			// scd3.setAdd1(add1);
			// scd3.setAdd2(add2);
			// scd3.setAdd4(add4);
			// scd3.setAdd5(add5);
			// scd3.setRenew1(renew1);
			// scd3.setRenew2(renew2);
			// scd3.setRenew4(renew4);
			// scd3.setRenew5(renew5);
			// zfCertDays.add(scd3);
			// }
			// scdd.setzCertDays(zCertDays);
			// scdd.sethCertDays(hCertDays);
			// scdd.setZfCertDays(zfCertDays);
			// sumList.add(scdd);
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			// }
		}
		model.addAttribute("dayList", dayLists);
		model.addAttribute("sumList", sumList);
		return "modules/statistic/statisticCertDataDayShow";
	}

	public List<String> getDayLists(String beginTime, String endTime,
			String month) {
		List<String> dayLists = new ArrayList<String>();
		List<String> dayList = new ArrayList<String>();
		try {
			Integer day = daysBetween(beginTime, endTime);// 获取相差的天数
			for (int i = 0; i <= day + 1; i++) {
				if (i == 0) {
					dayLists.add(beginTime);
				} else {
					dayLists.add(getSpecifiedDayAfter(beginTime, i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < dayLists.size() - 1; i++) {
//			try {
				Date months = DateUtils.parseDate(dayLists.get(i));// 获取月

				if (DateUtils.formatDate(months, "yyyy-MM").equals(month)) {// 判断这个月是否在这个月份里
					dayList.add(dayLists.get(i));
					continue;
				} else {
					continue;
				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		return dayList;
	}

	// 获取天集合
	public List<String> getDayList(String beginTime, String endTime,
			String month) {
		List<String> dayLists = new ArrayList<String>();
		List<String> dayList = new ArrayList<String>();
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Integer day = daysBetween(beginTime, endTime);// 获取相差的天数
			for (int i = 0; i <= day + 1; i++) {
				if (i == 0) {
					dayLists.add(beginTime);
				} else {
					dayLists.add(getSpecifiedDayAfter(beginTime, i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int j = 0;
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		for (int i = 0; i < dayLists.size() - 1; i++) {
			try {
				Date months = monthFormat.parse(dayLists.get(i));// 获取月
				if (monthFormat.format(months).equals(month)) {// 判断这个月是否在这个月份里
					dayList.add(dayLists.get(i));
					j = i;
					continue;
				} else {
					continue;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		dayList.add(dayLists.get(j + 1));
		return dayList;
	}

	// 获取两个日期之间相差的天数
	public int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		Integer day = Integer.parseInt(String.valueOf(between_days));
		for (int i = 0; i <= day; i++) {

		}
		return Integer.parseInt(String.valueOf(between_days));
	}

	// 获取指定日期的天数
	public String getSpecifiedDayAfter(String specifiedDay, int day) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day1 = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day1 + day);
		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

}
