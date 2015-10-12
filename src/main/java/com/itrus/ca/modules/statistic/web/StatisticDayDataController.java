package com.itrus.ca.modules.statistic.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
					Date end = rightNow.getTime();

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
