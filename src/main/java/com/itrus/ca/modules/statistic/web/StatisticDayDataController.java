package com.itrus.ca.modules.statistic.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.bean.StatisticAppMonthData;
import com.itrus.ca.modules.bean.StatisticMonthData;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptLogService;
import com.itrus.ca.modules.statistic.entity.StatisticAppData;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;
import com.itrus.ca.modules.statistic.service.StatisticAppDataService;
import com.itrus.ca.modules.statistic.service.StatisticDayDataService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.task.AutoTask;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * Controller
 * 
 * @author CaoYiChao
 * @version 2014-07-08
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/StatisticDayData")
public class StatisticDayDataController extends BaseController {

	@Autowired
	private StatisticDayDataService statisticDayDataService;

	@Autowired
	private StatisticAppDataService statisticAppDataService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	KeyUsbKeyService keyUsbKeyService;

	@Autowired
	KeyUsbKeyInvoiceService keyUsbKeyInvoiceService;

	@Autowired
	ReceiptEnterInfoService receiptEnterInfoService;

	@Autowired
	ReceiptInvoiceService receiptInvoiceService;

	@Autowired
	WorkDealInfoService workDealInfoService;

	@Autowired
	ReceiptDepotInfoService receiptDepotInfoService;

	@Autowired
	ReceiptLogService receiptLogService;

	@Autowired
	ConfigAppOfficeRelationService configAppOfficeRelationService;

	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public StatisticDayData get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return statisticDayDataService.get(id);
		} else {
			return new StatisticDayData();
		}
	}

	/**
	 * 列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("statistic:StatisticDayData:view")
	@RequestMapping(value = "list")
	public String list(
			@RequestParam(required = false) StatisticDayData statisticDayData,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			// statisticDayData.setCreateBy(user);
		}

		List<Office> offices = officeService.getOfficeByType(user, 2);
		model.addAttribute("offices", offices);
		model.addAttribute("office", office);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		if (startTime == null || endTime == null || office == null) {
			return "modules/work/operateCountOfDay";
		}

		Page<StatisticDayData> page = statisticDayDataService.findByDay(
				new Page<StatisticDayData>(request, response),
				statisticDayData, office, startTime, endTime);
		List<StatisticAppData> certDatas = statisticAppDataService
				.findByCreateDateAndOffice(office, startTime, endTime);
		List<List<StatisticAppData>> appDatas = new ArrayList<List<StatisticAppData>>();
		List<ConfigApp> apps = configAppService.selectAll();
		for (int i = 0; i < apps.size(); i++) {
			List<StatisticAppData> appData = new ArrayList<StatisticAppData>();
			for (int j = 0; j < certDatas.size(); j++) {
				if (apps.get(i).getAppName()
						.equals(certDatas.get(j).getApp().getAppName())) {
					appData.add(certDatas.get(j));
				}
			}

			if (appData.size() > 0) {
				appDatas.add(appData);
			}

		}

		model.addAttribute("page", page);
		model.addAttribute("appDatas", appDatas);

		return "modules/work/operateCountOfDay";
	}

	@RequiresPermissions("statistic:StatisticDayData:view")
	@RequestMapping(value = "listMonth")
	public String listMonth(
			@RequestParam(required = false) StatisticDayData statisticDayData,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			// statisticDayData.setCreateBy(user);
		}

		List<Office> offices = officeService.getOfficeByType(user, 2);
		model.addAttribute("offices", offices);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		try {
			if (startTime != null && !startTime.equals("")) {
				Date startDate = dateFormat.parse(startTime);
				model.addAttribute("startTime", startDate);
			}
			if (endTime != null && !endTime.equals("")) {
				Date endDate = dateFormat.parse(endTime);
				model.addAttribute("endTime", endDate);

			}

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		model.addAttribute("office", office);
		if (startTime == null || endTime == null || office == null
				|| startTime.equals("") || endTime.equals("")) {
			return "modules/work/operateCountOfMonth";
		}

		List<String> monthList = getMonthList(startTime + "-01", endTime
				+ "-01");

		try {

			List<StatisticMonthData> sumList = new ArrayList<StatisticMonthData>();
			for (String s : monthList) {
				StatisticMonthData smd = new StatisticMonthData();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date start = sdf.parse(s);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(start);
				rightNow.add(Calendar.MONTH, 1);
				rightNow.add(Calendar.DATE, -1);
				
				Date end = rightNow.getTime();
				end.setHours(23);
				end.setSeconds(59);
				end.setMinutes(59);

				Integer keyIn = 0;
				Integer keyOver = 0;
				Integer keyStoreTotal = 0;
				Double receiptIn = 0d;
				Double receiptOver = 0d;
				Double receiptStoreTotal = 0d;
				Integer certTotal = 0;
				Integer keyTotal = 0;
				Double receiptTotal = 0d;
				Double certMoneyTotal = 0d;

				List<StatisticDayData> ListMonth = statisticDayDataService
						.findByMonth(office, start, end);
				for (StatisticDayData sdd : ListMonth) {
					keyIn += sdd.getKeyIn();
					keyOver += sdd.getKeyOver();
					keyStoreTotal += sdd.getKeyStoreTotal();
					receiptIn += sdd.getReceiptIn();
					// receiptOver += sdd.getReceiptOver();
					receiptStoreTotal += sdd.getReceiptStoreTotal();
					certTotal += sdd.getCertTotal();
					keyTotal += sdd.getKeyTotal();
					receiptTotal += sdd.getReceiptTotal();
					certMoneyTotal += sdd.getCertMoneyTotal();
				}
				smd.setKeyIn(keyIn);
				smd.setKeyOver(keyOver);
				smd.setKeyStoreTotal(keyStoreTotal);
				smd.setReceiptIn(receiptIn);
				// smd.setReceiptOver(receiptOver);
				smd.setReceiptStoreTotal(receiptStoreTotal);
				smd.setCertTotal(certTotal);
				smd.setKeyTotal(keyTotal);
				smd.setReceiptTotal(receiptTotal);
				smd.setCertMoneyTotal(certMoneyTotal);
				smd.setCreateDate(start);
				sumList.add(smd);
			}

			model.addAttribute("sumList", sumList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			List<List<StatisticAppMonthData>> appSumList = new ArrayList<List<StatisticAppMonthData>>();
			List<ConfigApp> configApps = configAppService.selectAll();
			for (ConfigApp configApp : configApps) {
				List<StatisticAppMonthData> statisticAppMonthDatas = new ArrayList<StatisticAppMonthData>();
				for (String s : monthList) {

					StatisticAppMonthData samd = new StatisticAppMonthData();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date start = sdf.parse(s);
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(start);
					rightNow.add(Calendar.MONTH, 1);
					rightNow.add(Calendar.DATE, -1);
					
					Date end = rightNow.getTime();
					end.setHours(23);
					end.setSeconds(59);
					end.setMinutes(59);

					Integer add1 = 0;
					Integer add2 = 0;
					Integer add4 = 0;
					Integer add5 = 0;
					Integer renew1 = 0;
					Integer renew2 = 0;
					Integer renew4 = 0;
					Integer renew5 = 0;
					Integer modifyNum = 0;
					Integer reissueNum = 0;
					Integer certTotal = 0;
					Integer keyTotal = 0;
					Double receiptTotal = 0d;
					Integer lostReplaceNum = 0;
					Integer updateChangeNum = 0;
					Integer updateChangeNum2 = 0;
					Integer updateChangeNum4 = 0;
					Integer updateChangeNum5 = 0;
					Integer updateLostNum = 0;
					Integer updateLostNum2 = 0;
					Integer updateLostNum4 = 0;
					Integer updateLostNum5 = 0;
					Integer updateReplaceNum = 0;
					Integer updateReplaceNum2 = 0;
					Integer updateReplaceNum4 = 0;
					Integer updateReplaceNum5 = 0;
					Integer changeLostNum = 0;
					Integer changeReplaceNum = 0;
					Integer changeUpdateLostNum = 0;
					Integer changeUpdateLostNum2 = 0;
					Integer changeUpdateLostNum4 = 0;
					Integer changeUpdateLostNum5 = 0;
					Integer changeUpdateReplaceNum = 0;
					Integer changeUpdateReplaceNum2 = 0;
					Integer changeUpdateReplaceNum4 = 0;
					Integer changeUpdateReplaceNum5 = 0;
					
					
					
					

					List<StatisticAppData> listMonth = statisticAppDataService
							.findByMonth(configApp, office, start, end);
					for (StatisticAppData sad : listMonth) {
						add1 += sad.getAdd1();
						add2 += sad.getAdd2();
						add4 += sad.getAdd4();
						add5 += sad.getAdd5();
						renew1 += sad.getRenew1();
						renew2 += sad.getRenew2();
						renew4 += sad.getRenew4();
						renew5 += sad.getRenew5();
						modifyNum += sad.getModifyNum();
						reissueNum += sad.getReissueNum();
						certTotal += sad.getCertTotal();
						keyTotal += sad.getKeyTotal();
						receiptTotal += sad.getReceiptTotal();
						lostReplaceNum += sad.getLostReplaceNum();
						updateChangeNum += sad.getUpdateChangeNum();
						updateChangeNum2 += sad.getUpdateChangeNum2();
						updateChangeNum4 += sad.getUpdateChangeNum4();
						updateChangeNum5 += sad.getUpdateChangeNum5();
						updateLostNum += sad.getUpdateLostNum();
						updateLostNum2 += sad.getUpdateLostNum2();
						updateLostNum4 += sad.getUpdateLostNum4();
						updateLostNum5 += sad.getUpdateLostNum5();
						updateReplaceNum += sad.getUpdateReplaceNum();
						updateReplaceNum2 += sad.getUpdateReplaceNum2();
						updateReplaceNum4 += sad.getUpdateReplaceNum4();
						updateReplaceNum5 += sad.getUpdateReplaceNum5();
						changeLostNum += sad.getChangeLostNum();
						changeReplaceNum += sad.getChangeReplaceNum();
						changeUpdateLostNum += sad.getChangeUpdateLostNum();
						changeUpdateLostNum2 += sad.getChangeUpdateLostNum2();
						changeUpdateLostNum4 += sad.getChangeUpdateLostNum4();
						changeUpdateLostNum5 += sad.getChangeUpdateLostNum5();
						changeUpdateReplaceNum += sad.getChangeUpdateReplaceNum();
						changeUpdateReplaceNum2 += sad.getChangeUpdateReplaceNum2();
						changeUpdateReplaceNum4 += sad.getChangeUpdateReplaceNum4();
						changeUpdateReplaceNum5 += sad.getChangeUpdateReplaceNum5();
						
						
						
						

					}
					samd.setAdd1(add1);
					samd.setAdd2(add2);
					samd.setAdd4(add4);
					samd.setAdd5(add5);
					samd.setRenew1(renew1);
					samd.setRenew2(renew2);
					samd.setRenew4(renew4);
					samd.setRenew5(renew5);
					samd.setModifyNum(modifyNum);
					samd.setReissueNum(reissueNum);
					samd.setLostReplaceNum(lostReplaceNum);
					samd.setUpdateChangeNum(updateChangeNum);
					samd.setUpdateChangeNum2(updateChangeNum2);
					samd.setUpdateChangeNum4(updateChangeNum4);
					samd.setUpdateChangeNum5(updateChangeNum5);
					samd.setUpdateLostNum(changeUpdateLostNum);
					samd.setUpdateLostNum2(changeUpdateLostNum2);
					samd.setUpdateLostNum4(changeUpdateLostNum4);
					samd.setUpdateLostNum5(changeUpdateLostNum5);
					samd.setUpdateReplaceNum(changeUpdateReplaceNum);
					samd.setUpdateReplaceNum2(changeUpdateReplaceNum2);
					samd.setUpdateReplaceNum4(changeUpdateReplaceNum4);
					samd.setUpdateReplaceNum5(changeUpdateReplaceNum5);
					samd.setChangeLostNum(changeLostNum);
					samd.setChangeReplaceNum(changeReplaceNum);
					samd.setChangeUpdateLostNum(changeUpdateLostNum);
					samd.setChangeUpdateLostNum2(changeUpdateLostNum2);
					samd.setChangeUpdateLostNum4(changeUpdateLostNum4);
					samd.setChangeUpdateLostNum5(changeUpdateLostNum5);
					samd.setChangeUpdateReplaceNum(changeUpdateReplaceNum);
					samd.setChangeUpdateReplaceNum2(changeUpdateReplaceNum2);
					samd.setChangeUpdateReplaceNum4(changeUpdateReplaceNum4);
					samd.setChangeUpdateReplaceNum5(changeUpdateReplaceNum5);
					
					samd.setCertTotal(certTotal);
					samd.setKeyTotal(keyTotal);
					samd.setReceiptTotal(receiptTotal);

					samd.setApp(configApp);
					samd.setStatisticDate(start);
					statisticAppMonthDatas.add(samd);

				}

				appSumList.add(statisticAppMonthDatas);

			}

			model.addAttribute("appSumList", appSumList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "modules/work/operateCountOfMonth";
	}

	@RequestMapping(value = "exportCountDay")
	public void exportCountDay(
			@RequestParam(required = false) StatisticDayData statisticDayData,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime)
	{
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("日经营统计表");
		HSSFCellStyle style=wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font=wb.createFont();
		font.setFontHeightInPoints((short)20);
		font.setCharSet(20);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)9));
		sheet.addMergedRegion(new Region(1, (short)0, 2, (short)0));
		sheet.addMergedRegion(new Region(1, (short)1, 1, (short)2));
		sheet.addMergedRegion(new Region(1, (short)3, 1, (short)4));
		sheet.addMergedRegion(new Region(1, (short)5, 1, (short)7));
		sheet.addMergedRegion(new Region(1, (short)8, 1, (short)9));
		HSSFRow row0=sheet.createRow(0);
		HSSFCell cell0=row0.createCell(0);
		cell0.setCellValue("日经营统计");
		cell0.setCellStyle(style);
		HSSFRow row1=sheet.createRow(1);
		row1.createCell(0).setCellValue("日期");
		row1.createCell(1).setCellValue("入库");
		row1.createCell(3).setCellValue("总量");
		row1.createCell(5).setCellValue("日结");
		row1.createCell(8).setCellValue("余量");
		HSSFRow row2=sheet.createRow(2);
		row2.createCell(1).setCellValue("KEY");
		row2.createCell(2).setCellValue("发票");
		row2.createCell(3).setCellValue("KEY");
		row2.createCell(4).setCellValue("发票");
		row2.createCell(5).setCellValue("证书");
		row2.createCell(6).setCellValue("费用");
		row2.createCell(7).setCellValue("KEY");
		row2.createCell(8).setCellValue("kEY");
		row2.createCell(9).setCellValue("发票");

		Page<StatisticDayData> page = statisticDayDataService.findByDay(
				new Page<StatisticDayData>(request, response),
				statisticDayData, office, startTime, endTime);
		List<StatisticDayData> list=page.getList();
		
		List<StatisticAppData> certDatas = statisticAppDataService
				.findByCreateDateAndOffice(office, startTime, endTime);
		List<List<StatisticAppData>> appDatas = new ArrayList<List<StatisticAppData>>();
		List<ConfigApp> apps = configAppService.selectAll();
		for (int i = 0; i < apps.size(); i++) {
			List<StatisticAppData> appData = new ArrayList<StatisticAppData>();
			for (int j = 0; j < certDatas.size(); j++) {
				if (apps.get(i).getAppName()
						.equals(certDatas.get(j).getApp().getAppName())) {
					appData.add(certDatas.get(j));
				}
			}

			if (appData.size() > 0) {
				appDatas.add(appData);
			}

		}
		
		
		for(int i=0;i<list.size();i++)
		{
			HSSFRow rown=sheet.createRow(i+3);
			rown.createCell(0).setCellValue((list.get(i).getStatisticDate()).toString());
			rown.createCell(1).setCellValue(list.get(i).getKeyIn());
			rown.createCell(2).setCellValue(list.get(i).getReceiptIn());
			rown.createCell(3).setCellValue(list.get(i).getKeyTotal());
			rown.createCell(4).setCellValue(list.get(i).getReceiptTotal());
			rown.createCell(5).setCellValue(list.get(i).getCertTotal());
			rown.createCell(6).setCellValue(list.get(i).getCertMoneyTotal());
			rown.createCell(7).setCellValue(list.get(i).getKeyOver());
			rown.createCell(8).setCellValue(list.get(i).getKeyStoreTotal());
			rown.createCell(9).setCellValue(list.get(i).getReceiptStoreTotal());
		}
		
		for(int j=0;j<appDatas.size();j++)
		{
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size()),(short)0,(j+1)*(5+appDatas.size())+3,(short)0));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size()),(short)1,(j+1)*(5+appDatas.size()),(short)37));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+1, (short)1, (j+1)*(5+appDatas.size())+1, (short)33));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+1, (short)34, (j+1)*(5+appDatas.size())+1, (short)36));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)1, (j+1)*(5+appDatas.size())+2, (short)4));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)5, (j+1)*(5+appDatas.size())+2, (short)8));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)9, (j+1)*(5+appDatas.size())+2+1, (short)9));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)10, (j+1)*(5+appDatas.size())+2+1, (short)10));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)11, (j+1)*(5+appDatas.size())+2+1, (short)11));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)12, (j+1)*(5+appDatas.size())+2, (short)15));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)16, (j+1)*(5+appDatas.size())+2, (short)19));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)20, (j+1)*(5+appDatas.size())+2, (short)23));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)24, (j+1)*(5+appDatas.size())+2+1, (short)24));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)25,(j+1)*(5+appDatas.size())+2+1, (short)25));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)26, (j+1)*(5+appDatas.size())+2, (short)29));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)30, (j+1)*(5+appDatas.size())+2, (short)33));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)34, (j+1)*(5+appDatas.size())+2+1, (short)34));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)35, (j+1)*(5+appDatas.size())+2+1, (short)35));
			sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+2, (short)36, (j+1)*(5+appDatas.size())+2+1, (short)36));
			
			HSSFCellStyle stylese=wb.createCellStyle();
			stylese.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			stylese.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont fontse=wb.createFont();
			fontse.setFontHeightInPoints((short)10);
//			fontse.setCharSet(20);
			fontse.setFontName("宋体");
			fontse.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			stylese.setFont(fontse);
			
			HSSFRow serow0=sheet.createRow((j+1)*(5+appDatas.size()));
			HSSFCell cellse0=serow0.createCell(0);
			cellse0.setCellStyle(stylese);
			cellse0.setCellValue("日期");
			serow0.setHeightInPoints((short)20);
			HSSFCell cellse1=serow0.createCell(1);
			cellse1.setCellStyle(style);
			cellse1.setCellValue(appDatas.get(j).get(j).getApp().getAppName());
			HSSFRow serow1=sheet.createRow((j+1)*(5+appDatas.size())+1);
			serow1.createCell(1).setCellValue("业务办理");
			serow1.createCell(34).setCellValue("小计");
			HSSFRow serow2=sheet.createRow((j+1)*(5+appDatas.size())+2);
			serow2.createCell(1).setCellValue("新增");
			serow2.createCell(5).setCellValue("更新");
			serow2.createCell(9).setCellValue("变更");
			serow2.createCell(10).setCellValue("损坏更换");
			serow2.createCell(11).setCellValue("遗失补办");
			serow2.createCell(12).setCellValue("更新+变更");
			serow2.createCell(16).setCellValue("更新+遗失补办");
			serow2.createCell(20).setCellValue("更新+遗损坏更换");
			serow2.createCell(24).setCellValue("变更+遗失补办");
			serow2.createCell(25).setCellValue("变更+遗损坏更换");
			serow2.createCell(26).setCellValue("变更+遗失补办");
			serow2.createCell(30).setCellValue("变更+遗损坏更换");
			serow2.createCell(34).setCellValue("证书");
			serow2.createCell(35).setCellValue("KEY");
			serow2.createCell(36).setCellValue("发票");
			HSSFRow serow3=sheet.createRow((j+1)*(5+appDatas.size())+3);
			serow3.createCell(1).setCellValue("1年");
			serow3.createCell(2).setCellValue("2年");
			serow3.createCell(3).setCellValue("4年");
			serow3.createCell(4).setCellValue("5年");
			serow3.createCell(5).setCellValue("1年");
			serow3.createCell(6).setCellValue("2年");
			serow3.createCell(7).setCellValue("4年");
			serow3.createCell(8).setCellValue("5年");
			
			serow3.createCell(12).setCellValue("1年");
			serow3.createCell(13).setCellValue("2年");
			serow3.createCell(14).setCellValue("4年");
			serow3.createCell(15).setCellValue("5年");
			serow3.createCell(16).setCellValue("1年");
			serow3.createCell(17).setCellValue("2年");
			serow3.createCell(18).setCellValue("4年");
			serow3.createCell(19).setCellValue("5年");
			serow3.createCell(20).setCellValue("1年");
			serow3.createCell(21).setCellValue("2年");
			serow3.createCell(22).setCellValue("4年");
			serow3.createCell(23).setCellValue("5年");
			serow3.createCell(26).setCellValue("1年");
			serow3.createCell(27).setCellValue("2年");
			serow3.createCell(28).setCellValue("4年");
			serow3.createCell(29).setCellValue("5年");
			serow3.createCell(30).setCellValue("1年");
			serow3.createCell(31).setCellValue("2年");
			serow3.createCell(32).setCellValue("4年");
			serow3.createCell(33).setCellValue("5年");
			
			for(int i=0;i<appDatas.size();i++)
			{
				HSSFRow serow4=sheet.createRow((j+1)*(5+appDatas.size())+(4+i));
				serow4.createCell(0).setCellValue((appDatas.get(j).get(i).getStatisticDate()).toString());
				if(appDatas.get(j).get(i).getCertTotal()==0)
				{
					sheet.addMergedRegion(new Region((j+1)*(5+appDatas.size())+(4+i), (short)1, (j+1)*(5+appDatas.size())+(4+i), (short)36));
					serow4.createCell(1).setCellValue(appDatas.get(j).get(j).getApp().getAppName()+"应用当天没办理数据");
				}else if(appDatas.get(j).get(i).getCertTotal()!=0)
				{
					serow4.createCell(1).setCellValue(appDatas.get(j).get(i).getAdd1());
					serow4.createCell(2).setCellValue(appDatas.get(j).get(i).getAdd2());
					serow4.createCell(3).setCellValue(appDatas.get(j).get(i).getAdd4());
					serow4.createCell(4).setCellValue(appDatas.get(j).get(i).getAdd5());
					serow4.createCell(5).setCellValue(appDatas.get(j).get(i).getRenew1());
					serow4.createCell(6).setCellValue(appDatas.get(j).get(i).getRenew2());
					serow4.createCell(7).setCellValue(appDatas.get(j).get(i).getRenew4());
					serow4.createCell(8).setCellValue(appDatas.get(j).get(i).getRenew5());
					serow4.createCell(9).setCellValue(appDatas.get(j).get(i).getModifyNum());
					serow4.createCell(10).setCellValue(appDatas.get(j).get(i).getReissueNum());
					
					serow4.createCell(11).setCellValue(appDatas.get(j).get(i).getLostReplaceNum());
					
					serow4.createCell(12).setCellValue(appDatas.get(j).get(i).getUpdateChangeNum());
					serow4.createCell(13).setCellValue(appDatas.get(j).get(i).getUpdateChangeNum2());
					serow4.createCell(14).setCellValue(appDatas.get(j).get(i).getUpdateChangeNum4());
					serow4.createCell(15).setCellValue(appDatas.get(j).get(i).getUpdateChangeNum5());
					
					serow4.createCell(16).setCellValue(appDatas.get(j).get(i).getUpdateLostNum());
					serow4.createCell(17).setCellValue(appDatas.get(j).get(i).getUpdateLostNum2());
					serow4.createCell(18).setCellValue(appDatas.get(j).get(i).getUpdateLostNum4());
					serow4.createCell(19).setCellValue(appDatas.get(j).get(i).getUpdateLostNum5());
					
					serow4.createCell(20).setCellValue(appDatas.get(j).get(i).getUpdateReplaceNum());
					serow4.createCell(21).setCellValue(appDatas.get(j).get(i).getUpdateReplaceNum2());
					serow4.createCell(22).setCellValue(appDatas.get(j).get(i).getUpdateReplaceNum4());
					serow4.createCell(23).setCellValue(appDatas.get(j).get(i).getUpdateReplaceNum5());
					
					serow4.createCell(24).setCellValue(appDatas.get(j).get(i).getChangeLostNum());
					serow4.createCell(25).setCellValue(appDatas.get(j).get(i).getChangeReplaceNum());
					serow4.createCell(26).setCellValue(appDatas.get(j).get(i).getChangeUpdateLostNum());
					serow4.createCell(27).setCellValue(appDatas.get(j).get(i).getChangeUpdateLostNum2());
					serow4.createCell(28).setCellValue(appDatas.get(j).get(i).getChangeUpdateLostNum4());
					serow4.createCell(29).setCellValue(appDatas.get(j).get(i).getChangeUpdateLostNum5());
					serow4.createCell(30).setCellValue(appDatas.get(j).get(i).getChangeUpdateReplaceNum());
					serow4.createCell(31).setCellValue(appDatas.get(j).get(i).getChangeUpdateReplaceNum2());
					serow4.createCell(32).setCellValue(appDatas.get(j).get(i).getChangeUpdateReplaceNum4());
					serow4.createCell(33).setCellValue(appDatas.get(j).get(i).getChangeUpdateReplaceNum5());
					
					serow4.createCell(34).setCellValue(appDatas.get(j).get(i).getCertTotal());
					serow4.createCell(35).setCellValue(appDatas.get(j).get(i).getKeyTotal());
					serow4.createCell(36).setCellValue(appDatas.get(j).get(i).getReceiptTotal());
				}
				

				
			}
			
		}
		
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=operateCountDay.xls");
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
	@RequestMapping(value = "exportCountMonth")
	public void exportCountMonth(@RequestParam(required = false) StatisticDayData statisticDayData,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime
			)
	{
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("月经营统计表");
		HSSFCellStyle style=wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font=wb.createFont();
		font.setFontHeightInPoints((short)20);
		font.setCharSet(20);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)9));
		sheet.addMergedRegion(new Region(1, (short)0, 2, (short)0));
		sheet.addMergedRegion(new Region(1, (short)1, 1, (short)2));
		sheet.addMergedRegion(new Region(1, (short)3, 1, (short)4));
		sheet.addMergedRegion(new Region(1, (short)5, 1, (short)7));
		sheet.addMergedRegion(new Region(1, (short)8, 1, (short)9));
		HSSFRow row0=sheet.createRow(0);
		HSSFCell cell0=row0.createCell(0);
		cell0.setCellValue("月经营统计");
		cell0.setCellStyle(style);
		HSSFRow row1=sheet.createRow(1);
		row1.createCell(0).setCellValue("日期");
		row1.createCell(1).setCellValue("入库");
		row1.createCell(3).setCellValue("总量");
		row1.createCell(5).setCellValue("月结");
		row1.createCell(8).setCellValue("余量");
		HSSFRow row2=sheet.createRow(2);
		row2.createCell(1).setCellValue("KEY");
		row2.createCell(2).setCellValue("发票");
		row2.createCell(3).setCellValue("KEY");
		row2.createCell(4).setCellValue("发票");
		row2.createCell(5).setCellValue("证书");
		row2.createCell(6).setCellValue("费用");
		row2.createCell(7).setCellValue("KEY");
		row2.createCell(8).setCellValue("kEY");
		row2.createCell(9).setCellValue("发票");

		
		List<String> monthList = getMonthList(startTime + "-01", endTime
				+ "-01");
		List<StatisticMonthData> sumList = new ArrayList<StatisticMonthData>();
		try {
			for (String s : monthList) {
				StatisticMonthData smd = new StatisticMonthData();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date start = sdf.parse(s);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(start);
				rightNow.add(Calendar.MONTH, 1);
				Date end = rightNow.getTime();

				Integer keyIn = 0;
				Integer keyOver = 0;
				Integer keyStoreTotal = 0;
				Double receiptIn = 0d;
				Double receiptOver = 0d;
				Double receiptStoreTotal = 0d;
				Integer certTotal = 0;
				Integer keyTotal = 0;
				Double receiptTotal = 0d;
				Double certMoneyTotal = 0d;

				List<StatisticDayData> ListMonth = statisticDayDataService
						.findByMonth(office, start, end);
				for (StatisticDayData sdd : ListMonth) {
					keyIn += sdd.getKeyIn();
					keyOver += sdd.getKeyOver();
					keyStoreTotal += sdd.getKeyStoreTotal();
					receiptIn += sdd.getReceiptIn();
					// receiptOver += sdd.getReceiptOver();
					receiptStoreTotal += sdd.getReceiptStoreTotal();
					certTotal += sdd.getCertTotal();
					keyTotal += sdd.getKeyTotal();
					receiptTotal += sdd.getReceiptTotal();
					certMoneyTotal += sdd.getCertMoneyTotal();
				}
				smd.setKeyIn(keyIn);
				smd.setKeyOver(keyOver);
				smd.setKeyStoreTotal(keyStoreTotal);
				smd.setReceiptIn(receiptIn);
				// smd.setReceiptOver(receiptOver);
				smd.setReceiptStoreTotal(receiptStoreTotal);
				smd.setCertTotal(certTotal);
				smd.setKeyTotal(keyTotal);
				smd.setReceiptTotal(receiptTotal);
				smd.setCertMoneyTotal(certMoneyTotal);
				smd.setCreateDate(start);
				sumList.add(smd);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<sumList.size();i++)
		{
			HSSFRow rown=sheet.createRow(i+3);
			rown.createCell(0).setCellValue((sumList.get(i).getCreateDate()).toString());
			rown.createCell(1).setCellValue(sumList.get(i).getKeyIn());
			rown.createCell(2).setCellValue(sumList.get(i).getReceiptIn());
			rown.createCell(3).setCellValue(sumList.get(i).getKeyTotal());
			rown.createCell(4).setCellValue(sumList.get(i).getReceiptTotal());
			rown.createCell(5).setCellValue(sumList.get(i).getCertTotal());
			rown.createCell(6).setCellValue(sumList.get(i).getCertMoneyTotal());
			rown.createCell(7).setCellValue(sumList.get(i).getKeyOver());
			rown.createCell(8).setCellValue(sumList.get(i).getKeyStoreTotal());
			rown.createCell(9).setCellValue(sumList.get(i).getReceiptStoreTotal());
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=operateCountMonth.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	/**
	 * 日经营统计手动触发跳转界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "dayDateCount")
	public String dayDateCount(HttpSession httpSession,
			HttpServletRequest request, HttpServletResponse response,
			Date countDate) {
		User user = UserUtils.getUser();
		Long officeId = user.getOffice().getId();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (countDate == null) {
				httpSession.setAttribute("countDate", simpleDateFormat
						.parse(simpleDateFormat.format(new Date())));
				httpSession.setAttribute("officeId", officeId);
				httpSession.removeAttribute("statisticDayData");
				httpSession.removeAttribute("appDatas");
			} else {
				httpSession.setAttribute("countDate", countDate);
				dayStatic(countDate, officeId, httpSession);

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
		return "modules/work/CountOfDayNew";
		//return "modules/work/CountOfDay";
	}

	/**
	 * 校验当前日期是否进行过统计
	 * 
	 * @param countDate
	 * @return
	 */
	@RequestMapping(value = "checkDate")
	@ResponseBody
	public String checkDate(String countDate, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = UserUtils.getUser();
		Long officeId = user.getOffice().getId();
		List<String> dates = statisticDayDataService.getDateNoStatic(countDate,
				30, officeId);
		if (dates.size() != 0) {// 有未进行的统计
			jsonObject.put("status", "2");
			StringBuffer bDates = new StringBuffer();
			for (String s : dates) {
				bDates.append("[" + s + "]");
			}
			jsonObject.put("msg", "日期：" + bDates + " 有业务办理，但尚未生成每日统计，请先生成！");
			return jsonObject.toJSONString(); // 直接返回，让用户先处理前面的信息
		}

		List<StatisticDayData> statisticDayDataList = statisticDayDataService
				.getStatisticDayDataList(countDate, officeId);
		if (statisticDayDataList.size() != 0) {// 已进行过统计
			jsonObject.put("status", "0");
			jsonObject.put("msg", "当前日期已经统计，是否重新统计？");
		} else {// 未进行统计，直接调用统计方法
			dayStatic(DateUtils.parseDate(countDate), officeId, session);
			jsonObject.put("status", "1");
			jsonObject.put("msg", countDate + "日经营统计完成");
		}
		return jsonObject.toJSONString();
	}

	/**
	 * 再次统计
	 * 
	 * @param countDate
	 * @return
	 */
	@RequestMapping(value = "statisticAgain")
	@ResponseBody
	public String statisticAgain(String countDate, HttpSession session) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject jsonObject = new JSONObject();
		
		User user = UserUtils.getUser();
		Long officeId = user.getOffice().getId();
		try {
			Date date = simpleDateFormat.parse(countDate);
			statisticDayDataService.deleteDayData(date,officeId);// 删除历史已统计数据
			logUtil.saveSysLog("日经营统计重新统计", "删除STATISTIC_DAY_DATA" + countDate
					+ "成功", null);
			statisticAppDataService.deleteDayData(date,officeId);// 删除明细数据
			logUtil.saveSysLog("日经营统计重新统计", "删除STATISTIC_APP_DATA" + countDate
					+ "成功", null);
			
			dayStatic(DateUtils.parseDate(countDate), officeId, session);
			jsonObject.put("status", "1");
			jsonObject.put("msg", countDate + "日经营统计完成");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", "0");
			jsonObject.put("msg", "重新统计异常。");
			return jsonObject.toJSONString();
		}

		return jsonObject.toJSONString();
	}

	/**
	 * 保存统计
	 * 
	 * @return
	 */
	@RequestMapping(value = "statisticSave")
	@ResponseBody
	public String statisticSave(HttpSession session) {

		JSONObject jsonObject = new JSONObject();
		try {
			StatisticDayData statisticDayData = (StatisticDayData) session
					.getAttribute("statisticDayData");
			if (statisticDayData == null) {
				jsonObject.put("status", "0");
				jsonObject.put("msg", "数据不存在。");
			} else {
				statisticDayDataService.deleteDayData(statisticDayData.getStatisticDate(),statisticDayData.getOffice().getId()); // 删除历史已统计数据
				statisticAppDataService.deleteDayData(statisticDayData.getStatisticDate(),statisticDayData.getOffice().getId());// 删除明细数据				
				statisticDayDataService.save(statisticDayData);
				
				List<StatisticAppData> appDatas = (List<StatisticAppData>) session
						.getAttribute("appDatas");
				if (appDatas != null) {
					for (StatisticAppData certData : appDatas) {
						statisticAppDataService.save(certData);
					}
				}
				jsonObject.put("status", "1");
				jsonObject.put("msg", "日经营统计保存完成");
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", "0");
			jsonObject.put("msg", "重新统计异常。");
			return jsonObject.toJSONString();
		}

		return jsonObject.toJSONString();
	}

	// 根据date，officeId 生成每日统计数据，并放到Session中，以便后面保存使用
	private void dayStatic(Date date, Long officeId, HttpSession session) {

		Date countDate = date;// 统计日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, +1); // 得到下一天
		Date nextDay = calendar.getTime();

		List<StatisticDayData> prevDayDatas = statisticDayDataService
				.getPrevDayDatas(countDate, officeId);
		StatisticDayData oldDayData = new StatisticDayData();// 初始化空值
		if (prevDayDatas.size() > 0) { // 如果之前做过，oldDayData 用数据库中的值赋值
			oldDayData = prevDayDatas.get(0);
		}

		// 需要统计key、发票当天的出库单和入库单
		// 统计业务办理部分证书当天新增、更新、变更、补办成功的数量(查work_deal_info status是已获得证书即可)

		StatisticDayData statisticDayData = new StatisticDayData();
		Double receiptIn = 0d; // 发票入库量
		int certTotal = 0; // 证书发放总量
		Double paymentTotal = 0d; // 支付信息总量
		int keyOver = 0; // key出库总量

		// 上个统计周期的库存总量，为今天的keyTotal 昨日库存
		statisticDayData.setKeyTotal(oldDayData.getKeyStoreTotal() == null ? 0
				: oldDayData.getKeyStoreTotal());

		// 获得统计日期当天Key所有的入库信息:大于等于当天0点00 到 小于第二天0：00
		List<KeyUsbKey> keyUsbkeys = keyUsbKeyService.findByCreateDate(
				countDate, officeId, nextDay);
		int keyIn = 0; // key入库量
		for (KeyUsbKey keyUsbKey : keyUsbkeys) {
			keyIn += keyUsbKey.getCount();
		}
		statisticDayData.setKeyIn(keyIn);

		// 获得统计日期之前Key所有的出库信息
		List<KeyUsbKeyInvoice> keyUsbKeyInvoices = keyUsbKeyInvoiceService
				.findByCreateDate(countDate, officeId, nextDay);

		for (KeyUsbKeyInvoice keyUsbKeyInvoice : keyUsbKeyInvoices) {
			keyOver += keyUsbKeyInvoice.getDeliveryNum();
		}
		statisticDayData.setKeyStoreTotal(statisticDayData.getKeyTotal()
				+ keyIn - keyOver);// key余量
		statisticDayData.setKeyOver(keyOver);

		// 获得统计日期之前发票所有的入库信息
		List<ReceiptEnterInfo> receiptEnterInfos = receiptEnterInfoService
				.findByDate(countDate, nextDay, officeId);
//getReceiptMoney 如果有值，这 receiptMoney是正确的，如果没有，则nowMoney是正确的，产生的原因待查
		for (ReceiptEnterInfo receiptEnterInfo : receiptEnterInfos) {
			if (receiptEnterInfo.getReceiptMoney()!=null && receiptEnterInfo.getReceiptMoney()>0) {
				receiptIn += receiptEnterInfo.getReceiptMoney();
			}else{
				
				receiptIn += receiptEnterInfo.getNow_Money();
			}
			

		}

		List<ReceiptInvoice> receiptInvoices = receiptInvoiceService
				.findByDate(countDate, nextDay, officeId);

		Double receiptOut = 0d;
		for (ReceiptInvoice receiptInvoice : receiptInvoices) {
			receiptOut += receiptInvoice.getReceiptMoney();
		}

		statisticDayData.setReceiptOver(receiptOut);
		statisticDayData.setReceiptIn(receiptIn);
		statisticDayData
				.setReceiptTotal(oldDayData.getReceiptStoreTotal() == null ? 0d
						: oldDayData.getReceiptStoreTotal());
		statisticDayData.setReceiptStoreTotal(statisticDayData
				.getReceiptTotal() + receiptIn - receiptOut);// 余量

		// 证书总量
		certTotal = workDealInfoService
				.getCertPublishCount(countDate, officeId);
		// 金额总量
		paymentTotal = workDealInfoService.getWorkPayMoneyCount(countDate,
				countDate, officeId);
		// key余量

		statisticDayData.setCertMoneyTotal(paymentTotal);
		statisticDayData.setCertTotal(certTotal);
		statisticDayData.setCreateDate(new Date());

		statisticDayData.setOffice(officeService.get(officeId));
		statisticDayData.setStatisticDate(countDate);
		session.setAttribute("statisticDayData", statisticDayData);
	//	statisticDayDataService.save(statisticDayData);

		// ======================日经营汇总表结束========================
		// ======================日经营详情表===========================
		List<ConfigAppOfficeRelation> apps = configAppOfficeRelationService
				.findAllByOfficeId(officeId);
		List<StatisticAppData> appDatas = new ArrayList<StatisticAppData>();
		for (ConfigAppOfficeRelation relation : apps) {
			if (relation.getConfigApp() == null) {
				continue;
			}
			ConfigApp app = relation.getConfigApp();
			StatisticAppData certData = new StatisticAppData();

			/**
			 * 根据应用、网点、时间、年限生成数据
			 */
			// 新增
			certData.setAdd1(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_ADD_CERT));
			certData.setAdd2(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_ADD_CERT));
			certData.setAdd4(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_ADD_CERT));
			certData.setAdd5(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_ADD_CERT));
			// 更新
//			certData.setRenew1(workDealInfoService.getCertAppYearInfoCount(
//					countDate, officeId, 1, app.getId(),
//					WorkDealInfoType.TYPE_UPDATE_CERT));
			
			certData.setRenew1(workDealInfoService.getCertAppYearInfoCountOneDeal(countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));
			
			
			
			certData.setRenew2(workDealInfoService.getCertAppYearInfoCountOneDeal(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setRenew4(workDealInfoService.getCertAppYearInfoCountOneDeal(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setRenew5(workDealInfoService.getCertAppYearInfoCountOneDeal(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));

			certData.setModifyNum(workDealInfoService.getCertAppYearInfoCountOneDeal(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			certData.setReissueNum(workDealInfoService.getCertAppYearInfoCountOneDeal(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_LOST_CHILD)
					);
			certData.setLostReplaceNum(workDealInfoService.getCertAppYearInfoCountOneDeal(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			
			
			certData.setUpdateChangeNum(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			certData.setUpdateChangeNum2(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			certData.setUpdateChangeNum4(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			certData.setUpdateChangeNum5(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			
			certData.setUpdateLostNum(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			certData.setUpdateLostNum2(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			certData.setUpdateLostNum4(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			certData.setUpdateLostNum5(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			
			
			certData.setUpdateReplaceNum(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			certData.setUpdateReplaceNum2(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			certData.setUpdateReplaceNum4(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			certData.setUpdateReplaceNum5(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			
			certData.setChangeLostNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceNum(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD)
					
					);
			certData.setChangeReplaceNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceNum(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED)
					
					);
			
			certData.setChangeUpdateLostNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setChangeUpdateLostNum2(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setChangeUpdateLostNum4(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setChangeUpdateLostNum5(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			
			
			certData.setChangeUpdateReplaceNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setChangeUpdateReplaceNum2(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setChangeUpdateReplaceNum4(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setChangeUpdateReplaceNum5(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			
			certData.setCertTotal(certData.getAdd1() + certData.getAdd2()
					+ certData.getAdd4() + certData.getRenew1()
					+ certData.getAdd5() + certData.getRenew2()
					+ certData.getRenew4() + certData.getRenew5()
					+ certData.getModifyNum() + certData.getReissueNum()
					+ certData.getLostReplaceNum() 
					+ certData.getUpdateChangeNum() + certData.getUpdateChangeNum2() + certData.getUpdateChangeNum4() + certData.getUpdateChangeNum5()
					+ certData.getUpdateLostNum() + certData.getUpdateLostNum2() + certData.getUpdateLostNum4() + certData.getUpdateLostNum5() 
					+ certData.getUpdateReplaceNum() + certData.getUpdateReplaceNum2() + certData.getUpdateReplaceNum4() + certData.getUpdateReplaceNum5()
					+ certData.getChangeLostNum() 
					+ certData.getChangeReplaceNum()
					+ certData.getChangeUpdateLostNum() + certData.getChangeUpdateLostNum2() + certData.getChangeUpdateLostNum4() + certData.getChangeUpdateLostNum5() 
					+ certData.getChangeUpdateReplaceNum() + certData.getChangeUpdateReplaceNum2() + certData.getChangeUpdateReplaceNum4() + certData.getChangeUpdateReplaceNum5()
					);
			certData.setReceiptTotal(workDealInfoService
					.getWorkPayReceiptCount(countDate, officeId, app.getId()));
			certData.setKeyTotal(workDealInfoService.getKeyPublishTimesCount(
					countDate, officeId, app.getId()));
			certData.setOffice(officeService.get(officeId));
			certData.setApp(app);
			certData.setStatisticDate(countDate);
			appDatas.add(certData);
			// statisticAppDataService.save(certData);
		}
		session.setAttribute("appDatas", appDatas);
	}

}
