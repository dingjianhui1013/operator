/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
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
import com.itrus.ca.modules.profile.service.ConfigAppService;
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

		if(startDate==null&&endDate==null)//首次进入将产品设置为全部产品
		{
			productType="-1";
//			officeId=UserUtils.getUser().getId();
		}
		if(officeId==null)
		{
			officeId=UserUtils.getUser().getOffice().getId();
		}
		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}else{
			List<Office> offices = officeService.getOfficeByType(UserUtils.getUser(), 2);
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

		
		if (startDate == null && endDate == null) {
//			return "modules/statistic/statisticCertDataList";
			startDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			endDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
		}
		model.addAttribute("productId", productType);
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("officeId", officeId);
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
					Date endT = end;
					
					endT.setHours(23);
					endT.setSeconds(59);
					endT.setMinutes(59);
					// 自费企业
					Integer oneAdd1 = 0;
					Integer oneAdd2 = 0;
					
					Integer oneAdd3 = 0;
					
					Integer oneAdd4 = 0;
					Integer oneAdd5 = 0;

					Integer oneRenew1 = 0;
					Integer oneRenew2 = 0;
					
					Integer oneRenew3 = 0;
					
					Integer oneRenew4 = 0;
					Integer oneRenew5 = 0;
					// List<StatisticCertData> zList =
					// statisticCertDataService.getSum(configApp, office, start,
					// end, 0);
					List<StatisticCertData> zList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, endT, 0);
					for (StatisticCertData statisticCertData2 : zList) {
						oneAdd1 += statisticCertData2.getAdd1()==null?0:statisticCertData2.getAdd1();
						oneAdd2 += statisticCertData2.getAdd2()==null?0:statisticCertData2.getAdd2();
						
						oneAdd3 += statisticCertData2.getAdd3()==null?0:statisticCertData2.getAdd3();
						
						oneAdd4 += statisticCertData2.getAdd4()==null?0:statisticCertData2.getAdd4();
						oneAdd5 += statisticCertData2.getAdd5()==null?0:statisticCertData2.getAdd5();
						oneRenew1 += statisticCertData2.getRenew1()==null?0:statisticCertData2.getRenew1();
						oneRenew2 += statisticCertData2.getRenew2()==null?0:statisticCertData2.getRenew2();
						
						oneRenew3 += statisticCertData2.getRenew3()==null?0:statisticCertData2.getRenew3();
						
						oneRenew4 += statisticCertData2.getRenew4()==null?0:statisticCertData2.getRenew4();
						oneRenew5 += statisticCertData2.getRenew5()==null?0:statisticCertData2.getRenew5();
					}
					scm.setOneAdd1(oneAdd1);
					scm.setOneAdd2(oneAdd2);
					
					scm.setOneAdd3(oneAdd3);
					
					scm.setOneAdd4(oneAdd4);
					scm.setOneAdd5(oneAdd5);
					scm.setOneRenew1(oneRenew1);
					scm.setOneRenew2(oneRenew2);
					
					scm.setOneRenew3(oneRenew3);
					
					scm.setOneRenew4(oneRenew4);
					scm.setOneRenew5(oneRenew5);
					// 合同企业
					Integer twoAdd1 = 0;
					Integer twoAdd2 = 0;
					
					Integer twoAdd3 = 0;
					
					Integer twoAdd4 = 0;
					Integer twoAdd5 = 0;
					Integer twoRenew1 = 0;
					Integer twoRenew2 = 0;
					
					Integer twoRenew3 = 0;
					
					Integer twoRenew4 = 0;
					Integer twoRenew5 = 0;
					// List<StatisticCertData> hList =
					// statisticCertDataService.getSum(configApp, office, start,
					// end, 2);
					List<StatisticCertData> hList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 2);
					for (StatisticCertData scd : hList) {
						twoAdd1 += scd.getAdd1()==null?0:scd.getAdd1();
						twoAdd2 += scd.getAdd2()==null?0:scd.getAdd2();
						
						twoAdd3 += scd.getAdd3()==null?0:scd.getAdd3();
						
						twoAdd4 += scd.getAdd4()==null?0:scd.getAdd4();
						twoAdd5 += scd.getAdd5()==null?0:scd.getAdd5();
						twoRenew1 += scd.getRenew1()==null?0:scd.getRenew1();
						twoRenew2 += scd.getRenew2()==null?0:scd.getRenew2();
						
						twoRenew3 += scd.getRenew3()==null?0:scd.getRenew3();
						
						twoRenew4 += scd.getRenew4()==null?0:scd.getRenew4();
						twoRenew5 += scd.getRenew5()==null?0:scd.getRenew5();
					}
					scm.setTwoAdd1(twoAdd1);
					scm.setTwoAdd2(twoAdd2);
					
					scm.setTwoAdd3(twoAdd3);
					
					scm.setTwoAdd4(twoAdd4);
					scm.setTwoAdd5(twoAdd5);
					scm.setTwoRenew1(twoRenew1);
					scm.setTwoRenew2(twoRenew2);
					
					scm.setTwoRenew3(twoRenew3);
					
					scm.setTwoRenew4(twoRenew4);
					scm.setTwoRenew5(twoRenew5);
					// 政府统一采购
					Integer fourAdd1 = 0;
					Integer fourAdd2 = 0;
					
					Integer fourAdd3 = 0;
					
					Integer fourAdd4 = 0;
					Integer fourAdd5 = 0;
					Integer fourRenew1 = 0;
					Integer fourRenew2 = 0;
					
					Integer fourRenew3 = 0;
					
					Integer fourRenew4 = 0;
					Integer fourRenew5 = 0;
					// List<StatisticCertData> zfList =
					// statisticCertDataService.getSum(configApp, office, start,
					// end, 1);
					List<StatisticCertData> zfList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productType),
									office, start, end, 1);
					for (StatisticCertData scd1 : zfList) {
						fourAdd1 += scd1.getAdd1()==null?0:scd1.getAdd1();
						fourAdd2 += scd1.getAdd2()==null?0:scd1.getAdd2();
						
						fourAdd3 += scd1.getAdd3()==null?0:scd1.getAdd3();
						
						fourAdd4 += scd1.getAdd4()==null?0:scd1.getAdd4();
						fourAdd5 += scd1.getAdd5()==null?0:scd1.getAdd5();
						fourRenew1 += scd1.getRenew1()==null?0:scd1.getRenew1();
						fourRenew2 += scd1.getRenew2()==null?0:scd1.getRenew2();
						
						fourRenew3 += scd1.getRenew3()==null?0:scd1.getRenew3();
						
						fourRenew4 += scd1.getRenew4()==null?0:scd1.getRenew4();
						fourRenew5 += scd1.getRenew5()==null?0:scd1.getRenew5();
					}
					scm.setFourAdd1(fourAdd1);
					scm.setFourAdd2(fourAdd2);
					
					scm.setFourAdd3(fourAdd3);
					
					scm.setFourAdd4(fourAdd4);
					scm.setFourAdd5(fourAdd5);
					scm.setFourRenew1(fourRenew1);
					scm.setFourRenew2(fourRenew2);
					
					scm.setFourRenew3(fourRenew3);
					
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
				 
				 Integer totaladd = certMonth.getOneAdd1()==null?0:certMonth.getOneAdd1()
						 		   +(certMonth.getOneAdd2()==null?0:certMonth.getOneAdd2())
						 		   +(certMonth.getOneAdd3()==null?0:certMonth.getOneAdd3())
								   +(certMonth.getOneAdd4()==null?0:certMonth.getOneAdd4())
								   +(certMonth.getOneAdd5()==null?0:certMonth.getOneAdd5())
						 		   +(certMonth.getTwoAdd1()==null?0:certMonth.getTwoAdd1())
						 		   +(certMonth.getTwoAdd2()==null?0:certMonth.getTwoAdd2())
						 		   +(certMonth.getTwoAdd3()==null?0:certMonth.getTwoAdd3())
						 		   +(certMonth.getTwoAdd4()==null?0:certMonth.getTwoAdd4())
						 		   +(certMonth.getTwoAdd5()==null?0:certMonth.getTwoAdd5())
						           +(certMonth.getFourAdd1()==null?0:certMonth.getFourAdd1())
						           +(certMonth.getFourAdd2()==null?0:certMonth.getFourAdd2())
						           +(certMonth.getFourAdd3()==null?0:certMonth.getFourAdd3())
						           +(certMonth.getFourAdd4()==null?0:certMonth.getFourAdd4())
						           +(certMonth.getFourAdd5()==null?0:certMonth.getFourAdd5());
		
				 if (totalListNum.size()>totalLocation) {
					 totalListNum.set(totalLocation, totalListNum.get(totalLocation)+totaladd);
				 }else{
					 totalListNum.add(totalLocation,totaladd);
				 }
				 totalLocation++;
				 
				 Integer totalupdate =  (certMonth.getOneRenew1()==null?0:certMonth.getOneRenew1())
						 				+(certMonth.getOneRenew2()==null?0:certMonth.getOneRenew2())
						 				+(certMonth.getOneRenew3()==null?0:certMonth.getOneRenew3())
						 				+(certMonth.getOneRenew4()==null?0:certMonth.getOneRenew4())
						 				+(certMonth.getOneRenew5()==null?0:certMonth.getOneRenew5())
						 				+(certMonth.getTwoRenew1()==null?0:certMonth.getTwoRenew1())
						 				+(certMonth.getTwoRenew2()==null?0:certMonth.getTwoRenew2())
						 				+(certMonth.getTwoRenew3()==null?0:certMonth.getTwoRenew3())
						 				+(certMonth.getTwoRenew4()==null?0:certMonth.getTwoRenew4())
						 				+(certMonth.getTwoRenew5()==null?0:certMonth.getTwoRenew5())
						 				+(certMonth.getFourRenew1()==null?0:certMonth.getFourRenew1())
						 				+(certMonth.getFourRenew2()==null?0:certMonth.getFourRenew2())
						 				+(certMonth.getFourRenew3()==null?0:certMonth.getFourRenew3())
						 				+(certMonth.getFourRenew4()==null?0:certMonth.getFourRenew4())
						 				+(certMonth.getFourRenew5()==null?0:certMonth.getFourRenew5());
			
				 if (totalListNum.size()>totalLocation) {
					 totalListNum.set(totalLocation, totalListNum.get(totalLocation)+totalupdate);
				 }else{
					 totalListNum.add(totalLocation, totalupdate);
				 }				
				 totalLocation++;
			}
		}
		model.addAttribute("totalListNum", totalListNum);
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
	@RequestMapping(value = "exportZS")
	public void exportZS(
			Long areaId,
			Long officeId,
			String startDate,
			String endDate,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) String productType)
	{
		Office office = officeService.get(officeId);
		List<String> monthList = getMonthList(startDate, endDate);
		List<String > monthlist1=getMoList(startDate, endDate);
		List<StaticCertDateMonth> sumList = new ArrayList<StaticCertDateMonth>();
		List<ConfigApp> configApps = null;
		if (applyId != null
				&& "-1".equals(productType.toString())
				|| (applyId != null && (productType == null || !"-1"
						.equals(productType.toString())))) {
			configApps = configAppService.findById(applyId);
		}
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
					
					Integer oneAdd3 = 0;
					
					Integer oneAdd4 = 0;
					Integer oneAdd5 = 0;

					Integer oneRenew1 = 0;
					Integer oneRenew2 = 0;
					
					Integer oneRenew3 = 0;
					
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
						
						oneAdd3 += statisticCertData2.getAdd3();
						
						oneAdd4 += statisticCertData2.getAdd4();
						oneAdd5 += statisticCertData2.getAdd5();
						oneRenew1 += statisticCertData2.getRenew1();
						oneRenew2 += statisticCertData2.getRenew2();
						
						oneRenew3 += statisticCertData2.getRenew3();
						
						oneRenew4 += statisticCertData2.getRenew4();
						oneRenew5 += statisticCertData2.getRenew5();
					}
					scm.setOneAdd1(oneAdd1);
					scm.setOneAdd2(oneAdd2);
					
					scm.setOneAdd3(oneAdd3);
					
					scm.setOneAdd4(oneAdd4);
					scm.setOneAdd5(oneAdd5);
					scm.setOneRenew1(oneRenew1);
					scm.setOneRenew2(oneRenew2);
					
					scm.setOneRenew3(oneRenew3);
					
					scm.setOneRenew4(oneRenew4);
					scm.setOneRenew5(oneRenew5);
					// 合同企业
					Integer twoAdd1 = 0;
					Integer twoAdd2 = 0;
					
					Integer twoAdd3 = 0;
					
					Integer twoAdd4 = 0;
					Integer twoAdd5 = 0;
					Integer twoRenew1 = 0;
					Integer twoRenew2 = 0;
					
					Integer twoRenew3 = 0;
					
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
						
						twoAdd3 += scd.getAdd3();
						
						twoAdd4 += scd.getAdd4();
						twoAdd5 += scd.getAdd5();
						twoRenew1 += scd.getRenew1();
						twoRenew2 += scd.getRenew2();
						
						twoRenew3 += scd.getRenew3();
						
						twoRenew4 += scd.getRenew4();
						twoRenew5 += scd.getRenew5();
					}
					scm.setTwoAdd1(twoAdd1);
					scm.setTwoAdd2(twoAdd2);
					
					scm.setTwoAdd3(twoAdd3);
					
					scm.setTwoAdd4(twoAdd4);
					scm.setTwoAdd5(twoAdd5);
					scm.setTwoRenew1(twoRenew1);
					scm.setTwoRenew2(twoRenew2);
					
					scm.setTwoRenew3(twoRenew3);
					
					scm.setTwoRenew4(twoRenew4);
					scm.setTwoRenew5(twoRenew5);
					// 政府统一采购
					Integer fourAdd1 = 0;
					Integer fourAdd2 = 0;
					
					Integer fourAdd3 = 0;
					
					Integer fourAdd4 = 0;
					Integer fourAdd5 = 0;
					Integer fourRenew1 = 0;
					Integer fourRenew2 = 0;
					
					Integer fourRenew3 = 0;
					
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
						
						fourAdd3 += scd1.getAdd3();
						
						fourAdd4 += scd1.getAdd4();
						fourAdd5 += scd1.getAdd5();
						fourRenew1 += scd1.getRenew1();
						fourRenew2 += scd1.getRenew2();
						
						fourRenew3 += scd1.getRenew3();
						
						fourRenew4 += scd1.getRenew4();
						fourRenew5 += scd1.getRenew5();
					}
					scm.setFourAdd1(fourAdd1);
					scm.setFourAdd2(fourAdd2);
					
					scm.setFourAdd3(fourAdd3);
					
					scm.setFourAdd4(fourAdd4);
					scm.setFourAdd5(fourAdd5);
					scm.setFourRenew1(fourRenew1);
					scm.setFourRenew2(fourRenew2);
					
					scm.setFourRenew3(fourRenew3);
					
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
		List<Integer> totalListNum = new ArrayList<Integer>();
		for (int i = 0; i < sumList.size(); i++) {
			int totalLocation = 0;
			for (int j = 0; j < sumList.get(i).getCertMonths().size(); j++) {
				 StaticCertMonth certMonth = sumList.get(i).getCertMonths().get(j);
				 
				 Integer totaladd = certMonth.getOneAdd1()+certMonth.getOneAdd2()+certMonth.getOneAdd3()+certMonth.getOneAdd4()+certMonth.getOneAdd5()
				 				   +certMonth.getTwoAdd1()+certMonth.getTwoAdd2()+certMonth.getTwoAdd3()+certMonth.getTwoAdd4()+certMonth.getTwoAdd5()
				 				   +certMonth.getFourAdd1()+certMonth.getFourAdd2()+certMonth.getFourAdd3()+certMonth.getFourAdd4()+certMonth.getFourAdd5();
		
				 if (totalListNum.size()>totalLocation) {
					 totalListNum.set(totalLocation, totalListNum.get(totalLocation)+totaladd);
					 
				 }else{
					 totalListNum.add(totalLocation,totaladd);
				 }
				 totalLocation++;
				 
				 Integer totalupdate = certMonth.getOneRenew1()+certMonth.getOneRenew2()+certMonth.getOneRenew3()+certMonth.getOneRenew4()+certMonth.getOneRenew5()
						 			  +certMonth.getTwoRenew1()+certMonth.getTwoRenew2()+certMonth.getTwoRenew3()+certMonth.getTwoRenew4()+certMonth.getTwoRenew5()
						 			  +certMonth.getFourRenew1()+certMonth.getFourRenew2()+certMonth.getFourRenew3()+certMonth.getFourRenew4()+certMonth.getFourRenew5();
			
				 if (totalListNum.size()>totalLocation) {
					 totalListNum.set(totalLocation, totalListNum.get(totalLocation)+totalupdate);
				 }else{
					 totalListNum.add(totalLocation, totalupdate);
				 }				
				 totalLocation++;
			}
		}
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("项目发放证书统计");
		HSSFCellStyle style=wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font=wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)20);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0,(short)0, 0,(short) (2*monthlist1.size()+3)));
		HSSFRow row0=sheet.createRow(0);
		HSSFCell cell0=row0.createCell(0);
		cell0.setCellValue("项目发放证书统计");
		row0.setHeightInPoints(40);
		cell0.setCellStyle(style);
		HSSFRow row=sheet.createRow(1);
		row.createCell(0).setCellValue("应用");
		row.createCell(1).setCellValue("费用归属");
		row.createCell(2).setCellValue("证书年限");
		HSSFRow row1=sheet.createRow(2);
		HSSFRow rowz=sheet.createRow(3+(3*5*(sumList.size())));
		rowz.createCell(0).setCellValue("总计");
		int z=0;
		for(int i=0;i<totalListNum.size();i++)
		{
			z+=totalListNum.get(i);
			rowz.createCell(3+i).setCellValue(totalListNum.get(i));
		}
		rowz.createCell(3+2*monthlist1.size()).setCellValue(z);
		for(int i=1;i<monthlist1.size()+1;i++)
		{
			sheet.addMergedRegion(new Region(1, (short)(2*i+1), 1, (short)(2*i+2)));
		}
		for(int i=1;i<=monthlist1.size();i++)
		{
			row.createCell(2*i+1).setCellValue(monthlist1.get(i-1));
		}
		row1.createCell(3+2*monthlist1.size()).setCellValue("小计");
		
		for(int i=1;i<=2*monthlist1.size();i++)
		{
			if(i%2==0)
			{
				row1.createCell(i+2).setCellValue("更新");
			}else
			{
				row1.createCell(i+2).setCellValue("新增");
			}
			
		}

		for(int a=0;a<sumList.size();a++)
		{
			sheet.addMergedRegion(new Region(a*15+3, (short)0, a*15+14, (short)0));
			for(int i=0;i<3*(a+1);i++)
			{
				sheet.addMergedRegion(new Region(i*5+3, (short)1, i*5+2+4, (short)1));
				HSSFRow rown1= sheet.createRow(i*5+3);
				rown1.createCell(2).setCellValue("一年期限");
				HSSFRow rown2=sheet.createRow(i*5+4);
				rown2.createCell(2).setCellValue("二年期限");
				HSSFRow rown3=sheet.createRow(i*5+5);
				rown3.createCell(2).setCellValue("三年期限");
				HSSFRow rown4=sheet.createRow(i*5+6);
				rown4.createCell(2).setCellValue("四年期限");
				HSSFRow rown5=sheet.createRow(i*5+7);
				rown5.createCell(2).setCellValue("五年期限");
			}
			
		}
		for(int a=0;a<sumList.size();a++)
		{
			HSSFRow rown11=sheet.createRow(a*15+3);
			HSSFRow rown12=sheet.createRow(a*15+4);
			HSSFRow rown13=sheet.createRow(a*15+5);
			HSSFRow rown14=sheet.createRow(a*15+6);
			HSSFRow rown15=sheet.createRow(a*15+7);
			HSSFRow rown22=sheet.createRow(a*15+9);
			HSSFRow rown23=sheet.createRow(a*15+10);
			HSSFRow rown24=sheet.createRow(a*15+11);
			HSSFRow rown25=sheet.createRow(a*15+12);
			HSSFRow rown32=sheet.createRow(a*15+14);
			HSSFRow rown33=sheet.createRow(a*15+15);
			HSSFRow rown34=sheet.createRow(a*15+16);
			HSSFRow rown35=sheet.createRow(a*15+17);
			rown11.createCell(0).setCellValue(sumList.get(a).getConfigApp().getAppName());
			rown11.createCell(1).setCellValue("自费企业");
			HSSFRow rown21=sheet.createRow(a*15+7+1);
			rown21.createCell(1).setCellValue("合同企业");
			HSSFRow rown31=sheet.createRow(a*15+12+1);
			rown31.createCell(1).setCellValue("政府统一采购");
			int onea1=0;
			int oner1=0;
			int onea2=0;
			int oner2=0;
			int onea3=0;
			int oner3=0;
			int onea4=0;
			int oner4=0;
			int onea5=0;
			int oner5=0;
			int twoa1=0;
			int twor1=0;
			int twoa2=0;
			int twor2=0;
			int twoa3=0;
			int twor3=0;
			int twoa4=0;
			int twor4=0;
			int twoa5=0;
			int twor5=0;
			int fourr1=0;
			int foura1=0;
			int foura2=0;
			int fourr2=0;
			int foura3=0;
			int fourr3=0;
			int foura4=0;
			int fourr4=0;
			int foura5=0;
			int fourr5=0;
			for(int j=1;j<monthlist1.size()+1;j++)
			{
				onea1+=sumList.get(a).getCertMonths().get(j-1).getOneAdd1();
				oner1+=sumList.get(a).getCertMonths().get(j-1).getOneRenew1();
				onea2+=sumList.get(a).getCertMonths().get(j-1).getOneAdd2();
				oner2+=sumList.get(a).getCertMonths().get(j-1).getOneRenew2();
				onea3+=sumList.get(a).getCertMonths().get(j-1).getOneAdd3();
				oner3+=sumList.get(a).getCertMonths().get(j-1).getOneRenew3();
				onea4+=sumList.get(a).getCertMonths().get(j-1).getOneAdd4();
				oner4+=sumList.get(a).getCertMonths().get(j-1).getOneRenew4();
				onea5+=sumList.get(a).getCertMonths().get(j-1).getOneAdd5();
				oner5+=sumList.get(a).getCertMonths().get(j-1).getOneRenew5();
				twoa1+=sumList.get(a).getCertMonths().get(j-1).getTwoAdd1();
				twor1+=sumList.get(a).getCertMonths().get(j-1).getTwoRenew1();
				twoa2+=sumList.get(a).getCertMonths().get(j-1).getTwoAdd2();
				twor2+=sumList.get(a).getCertMonths().get(j-1).getTwoRenew2();
				twoa3+=sumList.get(a).getCertMonths().get(j-1).getTwoAdd3();
				twor3+=sumList.get(a).getCertMonths().get(j-1).getTwoRenew3();
				twoa4+=sumList.get(a).getCertMonths().get(j-1).getTwoAdd4();
				twor4+=sumList.get(a).getCertMonths().get(j-1).getTwoRenew4();
				twoa5+=sumList.get(a).getCertMonths().get(j-1).getTwoAdd5();
				twor5+=sumList.get(a).getCertMonths().get(j-1).getTwoRenew5();
				foura1+=sumList.get(a).getCertMonths().get(j-1).getFourAdd1();
				fourr1+=sumList.get(a).getCertMonths().get(j-1).getFourRenew1();
				foura2+=sumList.get(a).getCertMonths().get(j-1).getFourAdd2();
				fourr2+=sumList.get(a).getCertMonths().get(j-1).getFourRenew2();
				foura3+=sumList.get(a).getCertMonths().get(j-1).getFourAdd3();
				fourr3+=sumList.get(a).getCertMonths().get(j-1).getFourRenew3();
				foura4+=sumList.get(a).getCertMonths().get(j-1).getFourAdd4();
				fourr4+=sumList.get(a).getCertMonths().get(j-1).getFourRenew4();
				foura5+=sumList.get(a).getCertMonths().get(j-1).getFourAdd5();
				fourr5+=sumList.get(a).getCertMonths().get(j-1).getFourRenew5();
			}
			for(int j=1;j<monthlist1.size()+1;j++)
			{
				rown11.createCell(2).setCellValue("一年期限");
				rown11.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneAdd1());
				rown11.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneRenew1());
				rown11.createCell(3+2*monthlist1.size()).setCellValue(onea1+oner1);
				rown12.createCell(2).setCellValue("二年期限");
				rown12.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneAdd2());
				rown12.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneRenew2());
				rown12.createCell(3+2*monthlist1.size()).setCellValue(onea2+oner2);
				rown13.createCell(2).setCellValue("三年期限");
				rown13.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneAdd3());
				rown13.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneRenew3());
				rown13.createCell(3+2*monthlist1.size()).setCellValue(onea3+oner3);
				rown14.createCell(2).setCellValue("四年期限");
				rown14.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneAdd4());
				rown14.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneRenew4());
				rown14.createCell(3+2*monthlist1.size()).setCellValue(onea4+oner4);
				rown15.createCell(2).setCellValue("五年期限");
				rown15.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneAdd5());
				rown15.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getOneRenew5());
				rown15.createCell(3+2*monthlist1.size()).setCellValue(onea5+oner5);
				
				
				
				rown21.createCell(2).setCellValue("一年期限");
				rown21.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoAdd1());
				rown21.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoRenew1());
				rown21.createCell(3+2*monthlist1.size()).setCellValue(twoa1+twor1);
				rown22.createCell(2).setCellValue("二年期限");
				rown22.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoAdd2());
				rown22.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoRenew2());
				rown22.createCell(3+2*monthlist1.size()).setCellValue(twoa2+twor2);
				rown23.createCell(2).setCellValue("三年期限");
				rown23.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoAdd3());
				rown23.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoRenew3());
				rown23.createCell(3+2*monthlist1.size()).setCellValue(twoa3+twor3);
				rown24.createCell(2).setCellValue("四年期限");
				rown24.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoAdd4());
				rown24.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoRenew4());
				rown24.createCell(3+2*monthlist1.size()).setCellValue(twoa4+twor4);
				rown25.createCell(2).setCellValue("五年期限");
				rown25.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoAdd5());
				rown25.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getTwoRenew5());
				rown25.createCell(3+2*monthlist1.size()).setCellValue(twoa5+twor5);
				
				rown31.createCell(2).setCellValue("一年期限");
				rown31.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourAdd1());
				rown31.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourRenew1());
				rown31.createCell(3+2*monthlist1.size()).setCellValue(foura1+fourr1);
				rown32.createCell(2).setCellValue("二年期限");
				rown32.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourAdd2());
				rown32.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourRenew2());
				rown32.createCell(3+2*monthlist1.size()).setCellValue(foura2+fourr2);
				rown33.createCell(2).setCellValue("三年期限");
				rown33.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourAdd3());
				rown33.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourRenew3());
				rown33.createCell(3+2*monthlist1.size()).setCellValue(foura3+fourr3);
				rown34.createCell(2).setCellValue("四年期限");
				rown34.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourAdd4());
				rown34.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourRenew4());
				rown34.createCell(3+2*monthlist1.size()).setCellValue(foura4+fourr4);
				rown35.createCell(2).setCellValue("五年期限");
				rown35.createCell(2*j+1).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourAdd5());
				rown35.createCell(2*j+2).setCellValue(sumList.get(a).getCertMonths().get(j-1).getFourRenew5());
				rown35.createCell(3+2*monthlist1.size()).setCellValue(foura5+fourr5);

				
			}
			
		}
		
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=StatisticCertData.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
					Integer oneAdd3 = 0;
					Integer oneAdd4 = 0;
					Integer oneAdd5 = 0;
					Integer oneRenew1 = 0;
					Integer oneRenew2 = 0;
					Integer oneRenew3 = 0;
					Integer oneRenew4 = 0;
					Integer oneRenew5 = 0;
					List<StatisticCertData> zList = statisticCertDataService
							.getSum1(configApp, Integer.parseInt(productId),
									office, start, end, 0);
					for (StatisticCertData statisticCertData2 : zList) {
						oneAdd1 += statisticCertData2.getAdd1();
						oneAdd2 += statisticCertData2.getAdd2();
						oneAdd3 += statisticCertData2.getAdd3();
						oneAdd4 += statisticCertData2.getAdd4();
						oneAdd5 += statisticCertData2.getAdd5();
						oneRenew1 += statisticCertData2.getRenew1();
						oneRenew2 += statisticCertData2.getRenew2();
						oneRenew3 += statisticCertData2.getRenew3();
						oneRenew4 += statisticCertData2.getRenew4();
						oneRenew5 += statisticCertData2.getRenew5();
					}
					scd.setOneAdd1(oneAdd1);
					scd.setOneAdd2(oneAdd2);
					scd.setOneAdd3(oneAdd3);
					scd.setOneAdd4(oneAdd4);
					scd.setOneAdd5(oneAdd5);
					scd.setOneRenew1(oneRenew1);
					scd.setOneRenew2(oneRenew2);
					scd.setOneRenew3(oneRenew3);
					scd.setOneRenew4(oneRenew4);
					scd.setOneRenew5(oneRenew5);
					// 合同企业
					Integer twoAdd1 = 0;
					Integer twoAdd2 = 0;
					Integer twoAdd3 = 0;
					Integer twoAdd4 = 0;
					Integer twoAdd5 = 0;
					Integer twoRenew1 = 0;
					Integer twoRenew2 = 0;
					Integer twoRenew3 = 0;
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
						twoAdd3 += scds.getAdd3();
						twoAdd4 += scds.getAdd4();
						twoAdd5 += scds.getAdd5();
						twoRenew1 += scds.getRenew1();
						twoRenew2 += scds.getRenew2();
						twoRenew3 += scds.getRenew3();
						twoRenew4 += scds.getRenew4();
						twoRenew5 += scds.getRenew5();
					}
					scd.setTwoAdd1(twoAdd1);
					scd.setTwoAdd2(twoAdd2);
					scd.setTwoAdd3(twoAdd3);
					scd.setTwoAdd4(twoAdd4);
					scd.setTwoAdd5(twoAdd5);
					scd.setTwoRenew1(twoRenew1);
					scd.setTwoRenew2(twoRenew2);
					scd.setTwoRenew3(twoRenew3);
					scd.setTwoRenew4(twoRenew4);
					scd.setTwoRenew5(twoRenew5);
					// 政府统一采购
					Integer fourAdd1 = 0;
					Integer fourAdd2 = 0;
					Integer fourAdd3 = 0;
					Integer fourAdd4 = 0;
					Integer fourAdd5 = 0;
					Integer fourRenew1 = 0;
					Integer fourRenew2 = 0;
					Integer fourRenew3 = 0;
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
						fourAdd3 += scd1.getAdd3();
						fourAdd4 += scd1.getAdd4();
						fourAdd5 += scd1.getAdd5();
						fourRenew1 += scd1.getRenew1();
						fourRenew2 += scd1.getRenew2();
						fourRenew3 += scd1.getRenew3();
						fourRenew4 += scd1.getRenew4();
						fourRenew5 += scd1.getRenew5();
					}
					scd.setFourAdd1(fourAdd1);
					scd.setFourAdd2(fourAdd2);
					scd.setFourAdd3(fourAdd3);
					scd.setFourAdd4(fourAdd4);
					scd.setFourAdd5(fourAdd5);
					scd.setFourRenew1(fourRenew1);
					scd.setFourRenew2(fourRenew2);
					scd.setFourRenew3(fourRenew3);
					scd.setFourRenew4(fourRenew4);
					scd.setFourRenew5(fourRenew5);
				} catch (Exception e) {
					e.printStackTrace();
				}
				scdList.add(scd);
			}
			scdd.setCertDays((scdList));
			sumList.add(scdd);

			
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
		new SimpleDateFormat("yyyy-MM-dd");
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
