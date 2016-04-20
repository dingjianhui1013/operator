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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.bean.StatisticAppMonthData;
import com.itrus.ca.modules.bean.StatisticMonthData;
import com.itrus.ca.modules.bean.StatisticSealAppMonth;
import com.itrus.ca.modules.bean.StatisticSealMonthData;
import com.itrus.ca.modules.constant.SignatureInfoType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.entity.SignaturePayInfo;
import com.itrus.ca.modules.signature.service.SignatureInfoService;
import com.itrus.ca.modules.signature.service.SignaturePayInfoService;
import com.itrus.ca.modules.statistic.entity.StatisticAppData;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;
import com.itrus.ca.modules.statistic.entity.StatisticSealAppData;
import com.itrus.ca.modules.statistic.entity.StatisticSealDayData;
import com.itrus.ca.modules.statistic.service.StatisticAppDataService;
import com.itrus.ca.modules.statistic.service.StatisticSealAppDataService;
import com.itrus.ca.modules.statistic.service.StatisticSealDayDataService;
import com.itrus.cert.SignatureType;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 印章日经营Controller
 * @author DingJianHui
 * @version 2016-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/statisticSealDayData")
public class StatisticSealDayDataController extends BaseController {

	@Autowired
	private StatisticSealDayDataService statisticSealDayDataService;
	@Autowired
	private StatisticSealAppDataService statisticSealAppDataService;
	@Autowired
	private SignatureInfoService signatureInfoService;
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;
	@Autowired
	private ReceiptInvoiceService	receiptInvoiceService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	@Autowired
	private ConfigAppService configAppService;
	@Autowired
	private SignaturePayInfoService signaturePayInfoService;
	LogUtil logUtil=new LogUtil();
	@ModelAttribute
	public StatisticSealDayData get(@RequestParam(required=false) Long id) {
		if (id != null){
			return statisticSealDayDataService.get(id);
		}else{
			return new StatisticSealDayData();
		}
	}
	
	@RequiresPermissions("statistic:statisticSealDayData:view")
	@RequestMapping(value = {"list", ""})
	public String list(
			@RequestParam(required = false) StatisticSealDayData statisticSealDayData, 
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			Model model) {
		User user = UserUtils.getUser();
		List<Office> offices = officeService.getOfficeByType(user, 2);
		model.addAttribute("offices", offices);
		model.addAttribute("office", office);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		if (startTime == null || endTime == null || office == null) {
			return "modules/statistic/statisticSealDayDataList";
		}
        Page<StatisticSealDayData> page = statisticSealDayDataService.findByDay(
        		new Page<StatisticSealDayData>(request, response),statisticSealDayData,startTime,endTime,office); 
		List<ConfigAppOfficeRelation> configApps=configAppOfficeRelationService.findAllByOfficeId(office);
		List<Long> appIds=Lists.newArrayList();
		if(configApps.size()>0)
		{
			for(int i=0;i<configApps.size();i++)
			{
				appIds.add(configApps.get(i).getConfigApp().getId());
			}
		}else
		{
			appIds.clear();
		}
		List<StatisticSealAppData> sealDatas = statisticSealAppDataService.findByDateAndOffice(office, appIds, startTime, endTime);
		List<List<StatisticSealAppData>>  appDatas = new ArrayList<List<StatisticSealAppData>>();
		List<ConfigApp> apps = configAppService.selectAll(); 
		for (int i = 0; i < apps.size(); i++) {
			List<StatisticSealAppData> appData = new ArrayList<StatisticSealAppData>();
			for (int j = 0; j < sealDatas.size(); j++) {
				if (apps.get(i).getAppName()
						.equals(sealDatas.get(j).getApp().getAppName())) {
					appData.add(sealDatas.get(j));
				}
			}

			if (appData.size() > 0) {
				appDatas.add(appData);
			}

		}
        model.addAttribute("page", page);
        model.addAttribute("appDatas",appDatas);
		return "modules/statistic/statisticSealDayDataList";
	}
	
	@RequestMapping(value="listMonth")
	public String listMonth(@RequestParam(required = false) StatisticSealDayData statisticSealDayData, 
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			Model model)
	{
		User user = UserUtils.getUser();
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
			return "modules/statistic/operateCountOfMonth";
		}
		List<String> monthList = getMonthList(startTime + "-01", endTime
				+ "-01");
		try {
			List<StatisticSealMonthData> sumList = new ArrayList<StatisticSealMonthData>();
			for (String s : monthList) {
				StatisticSealMonthData smd = new StatisticSealMonthData();
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
				Double receiptIn=0d; //发票入库
				Double receiptTotal=0d;//发票总量
				Integer sealDay=0;   //印章
				Double sealMoney=0d;  //费用
				Double receiptDay=0d; //今日发票
				Double receiptSurplus=0d;//发票剩余
				List<StatisticSealDayData> ListMonth = statisticSealDayDataService
						.findByMonth(office, start, end);
				for (StatisticSealDayData sdd : ListMonth) {
					receiptIn+=sdd.getReceiptIn();
					sealDay+=sdd.getSealDay();
					sealMoney+=sdd.getSealMoney();
					receiptDay+=sdd.getReceiptDay();
				}
				if (ListMonth.size()>0) {
					receiptTotal = ListMonth.get(0).getReceiptTotal();
				}
				receiptSurplus = receiptIn - receiptDay ;
				smd.setReceiptIn(receiptIn);
				smd.setReceiptTotal(receiptTotal);
				smd.setReceiptSurplus(receiptSurplus);
				smd.setReceiptDay(receiptDay);
				smd.setSealDay(sealDay);
				smd.setSealMoney(sealMoney);
				smd.setCreateDate(new Date());
				smd.setStatisticDate(start);
				sumList.add(smd);
			}
			model.addAttribute("sumList", sumList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<List<StatisticSealAppMonth>> appSumList = new ArrayList<List<StatisticSealAppMonth>>();
			Set<ConfigApp> configApps= new LinkedHashSet<ConfigApp>();
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
				List<ConfigAppOfficeRelation> configAppOfficeRelations=configAppOfficeRelationService.findAllByOfficeId(office);
				List<Long> appIds=Lists.newArrayList();
				if(configAppOfficeRelations.size()>0)
				{
					for(int i=0;i<configAppOfficeRelations.size();i++)
					{
						appIds.add(configAppOfficeRelations.get(i).getConfigApp().getId());
					}
				}else
				{
					appIds.clear();
				}
				List<StatisticSealAppData> listMonth = statisticSealAppDataService.findByMonth(office,appIds);
				if(listMonth.size()>0)
				{
					for (StatisticSealAppData configApp : listMonth) {
						configApps.add(configApp.getApp());
					}
				}
			}
			for (ConfigApp configApp : configApps) {
				List<StatisticSealAppMonth> statisticSealAppMonth = new ArrayList<StatisticSealAppMonth>();
				for (String s : monthList) {
					StatisticSealAppMonth samd = new StatisticSealAppMonth();
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

					Integer sealTotal=0;
					Double receiptTotal=0d;
					Double sealMoney=0d;
					Integer addOne=0; 	//新增财务章
					Integer addTwo=0;	//新增合同章
					Integer addThree=0;	//新增个人章
					Integer addFour=0;	//新增公章
					Integer addFive=0;
					Integer renewOne=0; 	//更新财务章
					Integer renewTwo=0;	//更新合同章
					Integer renewThree=0;	//更新个人章
					Integer renewFour=0;	//更新公章
					Integer renewFive=0;
					Integer changeOne=0; 	//变更财务章
					Integer changeTwo=0;	//变更合同章
					Integer changeThree=0;	//变更个人章
					Integer changeFour=0;	//变更公章
					Integer changeFive=0;
					List<StatisticSealAppData> listMonth = statisticSealAppDataService
							.findByMonth(configApp, office, start, end);
					for (StatisticSealAppData sad : listMonth) {
						sealTotal+=sad.getSealTotal();
						receiptTotal+=sad.getReceiptTotal();
						sealMoney+=sad.getSealMoney();
						addOne+=sad.getAddOne();
						addTwo+=sad.getAddTwo();
						addThree+=sad.getAddThree();
						addFour+=sad.getAddFour();
						addFive+=sad.getAddFive();
						renewOne+=sad.getRenewOne();
						renewTwo+=sad.getRenewTwo();
						renewThree+=sad.getRenewThree();
						renewFour+=sad.getRenewFour();
						renewFive+=sad.getRenewFive();
						changeOne+=sad.getChangeOne();
						changeTwo+=sad.getChangeTwo();
						changeThree+=sad.getChangeThree();
						changeFour+=sad.getChangeFour();
						changeFive+=sad.getChangeFive();
					}
					samd.setSealTotal(sealTotal);
					samd.setReceiptTotal(receiptTotal);
					samd.setSealMoney(sealMoney);
					samd.setAddOne(addOne);
					samd.setAddTwo(addTwo);
					samd.setAddThree(addThree);
					samd.setAddThree(addThree);
					samd.setAddFour(addFour);
					samd.setAddFive(addFive);
					samd.setRenewOne(renewOne);
					samd.setRenewTwo(renewTwo);
					samd.setRenewThree(renewThree);
					samd.setRenewFour(renewFour);
					samd.setRenewFive(renewFive);
					samd.setChangeOne(changeOne);
					samd.setChangeTwo(changeTwo);
					samd.setChangeThree(changeThree);
					samd.setChangeFour(changeFour);
					samd.setChangeFive(changeFive);
					samd.setApp(configApp);
					samd.setStatisticDate(start);
					statisticSealAppMonth.add(samd);

				}

				appSumList.add(statisticSealAppMonth);

			}

			model.addAttribute("appSumList", appSumList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/statistic/operateCountOfMonth";
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
	
	
	@RequiresPermissions("statistic:statisticSealDayData:view")
	@RequestMapping(value = "form")
	public String form(StatisticSealDayData statisticSealDayData, Model model) {
		model.addAttribute("statisticSealDayData", statisticSealDayData);
		return "modules/statistic/statisticSealDayDataForm";
	}

	@RequiresPermissions("statistic:statisticSealDayData:edit")
	@RequestMapping(value = "save")
	public String save(StatisticSealDayData statisticSealDayData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, statisticSealDayData)){
			return form(statisticSealDayData, model);
		}
		statisticSealDayDataService.save(statisticSealDayData);
//		addMessage(redirectAttributes, "保存印章日经营'" + statisticSealDayData.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticSealDayData/?repage";
	}
	
	@RequiresPermissions("statistic:statisticSealDayData:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		statisticSealDayDataService.delete(id);
		addMessage(redirectAttributes, "删除印章日经营成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticSealDayData/?repage";
	}
	
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
				httpSession.setAttribute("date", simpleDateFormat
						.parse(simpleDateFormat.format(new Date())));
				httpSession.setAttribute("officeId", officeId);
				httpSession.removeAttribute("statisticSealDayData");
				httpSession.removeAttribute("appDatas");
			} else {
				httpSession.setAttribute("countDate", countDate);
				httpSession.setAttribute("date", simpleDateFormat
						.parse(simpleDateFormat.format(new Date())));
				dayStatic(countDate, officeId, httpSession);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "modules/statistic/CountOfSealDayNew";
	}

	
	@RequestMapping(value = "checkDate")
	@ResponseBody
	public String checkDate(String countDate, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = UserUtils.getUser();
		Long officeId = user.getOffice().getId();
		List<String> dates = statisticSealDayDataService.getDateNoStatic(countDate,
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

		List<StatisticSealDayData> statisticSealDayDataList = statisticSealDayDataService
				.getStatisticDayDataList(countDate, officeId);
		if (statisticSealDayDataList.size() != 0) {// 已进行过统计
			jsonObject.put("status", "0");
			jsonObject.put("msg", "当前日期已经统计，是否重新统计？");
		} else {// 未进行统计，直接调用统计方法
			dayStatic(DateUtils.parseDate(countDate), officeId, session);
			jsonObject.put("status", "1");
			jsonObject.put("msg", countDate + "日经营统计完成");
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "statisticSave")
	@ResponseBody
	public String statisticSave(HttpSession session) {

		JSONObject jsonObject = new JSONObject();
		try {
			StatisticSealDayData statisticSealDayData = (StatisticSealDayData) session
					.getAttribute("statisticSealDayData");
			if (statisticSealDayData == null) {
				jsonObject.put("status", "0");
				jsonObject.put("msg", "数据不存在。");
			} else {
				statisticSealDayDataService.deleteDayData(statisticSealDayData.getStatisticDate(),statisticSealDayData.getSysOffice().getId()); // 删除历史已统计数据
				statisticSealAppDataService.deleteDayData(statisticSealDayData.getStatisticDate(),statisticSealDayData.getSysOffice().getId());// 删除明细数据				
				statisticSealDayDataService.save(statisticSealDayData);
				
				List<StatisticSealAppData> appDatas = (List<StatisticSealAppData>) session.getAttribute("appDatas");
				if (appDatas != null) {
					for (StatisticSealAppData certData : appDatas) {
						statisticSealAppDataService.save(certData);
					}
				}
				jsonObject.put("status", "1");
				jsonObject.put("msg", "印章日经营统计保存完成");
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", "0");
			jsonObject.put("msg", "系统异常请重新统计。");
			return jsonObject.toJSONString();
		}

		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "dayStatic")
	private void dayStatic(Date date, Long officeId, HttpSession session) {

		Date countDate = date;// 统计日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, +1); // 得到下一天
		Date nextDay = calendar.getTime();
		
		List<StatisticSealDayData> prevSealDaydDatas = statisticSealDayDataService.getPrevDayDataList(countDate, officeId);
		StatisticSealDayData oldSealDayData = new StatisticSealDayData();// 初始化空值
		if(prevSealDaydDatas.size()>0)
		{
			oldSealDayData = prevSealDaydDatas.get(0);
		}

		StatisticSealDayData statisticSealDayData = new StatisticSealDayData();
		Double receiptIn=0d;//发票入库量
		
		int sealTotal = 0;//印章发放总量
		sealTotal = signatureInfoService.finDaySealCount(countDate, officeId);
		statisticSealDayData.setSealDay(sealTotal);
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
				.findBySealDate(countDate, nextDay, officeId);
		Double receiptOut = 0d;
		for (ReceiptInvoice receiptInvoice : receiptInvoices) {
			receiptOut += receiptInvoice.getReceiptMoney();
		}
		statisticSealDayData.setReceiptIn(receiptIn);
		statisticSealDayData
				.setReceiptTotal(oldSealDayData.getReceiptSurplus() == null ? 0d
						: oldSealDayData.getReceiptSurplus());
		statisticSealDayData.setReceiptSurplus(statisticSealDayData
				.getReceiptTotal() + receiptIn - receiptOut);// 余量
		statisticSealDayData.setReceiptDay(receiptOut);
		
		Double money=0d;
		List<SignatureInfo> signatureInfos = signatureInfoService.finDaySeal(countDate, officeId);
		for (SignatureInfo signatureInfo : signatureInfos) {
			SignaturePayInfo signaturePayInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
			money+=signaturePayInfo.getTotalMoney();
		}
		statisticSealDayData.setSealMoney(money);
		statisticSealDayData.setSysOffice(officeService.get(officeId));
		statisticSealDayData.setStatisticDate(countDate);
		session.setAttribute("statisticSealDayData", statisticSealDayData);
//		// ======================日经营汇总表结束========================
//		// ======================日经营详情表===========================
		List<ConfigAppOfficeRelation> apps = configAppOfficeRelationService
				.findAllByOfficeId(officeId);
		List<StatisticSealAppData> appDatas = new ArrayList<StatisticSealAppData>();
		for (ConfigAppOfficeRelation relation : apps) {
			if (relation.getConfigApp() == null) {
				continue;
			}
			ConfigApp app = relation.getConfigApp();
			StatisticSealAppData certData = new StatisticSealAppData();

			/**
			 * 根据应用、网点、时间、年限生成数据
			 */
			// 新增
			certData.setAddOne(signatureInfoService.getInfocount(
					countDate,officeId,"1",app.getId(),SignatureInfoType.TYPE_ADD_SIGNATURE));
			certData.setAddTwo(signatureInfoService.getInfocount(
					countDate,officeId,"2",app.getId(),SignatureInfoType.TYPE_ADD_SIGNATURE));
			certData.setAddThree(signatureInfoService.getInfocount(
					countDate,officeId,"3",app.getId(),SignatureInfoType.TYPE_ADD_SIGNATURE));
			certData.setAddFour(signatureInfoService.getInfocount(
					countDate,officeId,"4",app.getId(),SignatureInfoType.TYPE_ADD_SIGNATURE));
			certData.setAddFive(signatureInfoService.getInfocount(
					countDate,officeId,"5",app.getId(),SignatureInfoType.TYPE_ADD_SIGNATURE));
			//变更
			certData.setChangeOne(signatureInfoService.getInfocount(
					countDate,officeId,"1",app.getId(),SignatureInfoType.TYPE_CHANGE_SIGNATURE));
			certData.setChangeTwo(signatureInfoService.getInfocount(
					countDate,officeId,"2",app.getId(),SignatureInfoType.TYPE_CHANGE_SIGNATURE));
			certData.setChangeThree(signatureInfoService.getInfocount(
					countDate,officeId,"3",app.getId(),SignatureInfoType.TYPE_CHANGE_SIGNATURE));
			certData.setChangeFour(signatureInfoService.getInfocount(
					countDate,officeId,"4",app.getId(),SignatureInfoType.TYPE_CHANGE_SIGNATURE));
			certData.setChangeFive(signatureInfoService.getInfocount(
					countDate,officeId,"5",app.getId(),SignatureInfoType.TYPE_CHANGE_SIGNATURE));
			//续期
			certData.setRenewOne(signatureInfoService.getInfocount(
					countDate,officeId,"1",app.getId(),SignatureInfoType.TYPE_UPDATE_SIGNATURE));
			certData.setRenewTwo(signatureInfoService.getInfocount(
					countDate,officeId,"2",app.getId(),SignatureInfoType.TYPE_UPDATE_SIGNATURE));
			certData.setRenewThree(signatureInfoService.getInfocount(
					countDate,officeId,"3",app.getId(),SignatureInfoType.TYPE_UPDATE_SIGNATURE));
			certData.setRenewFour(signatureInfoService.getInfocount(
					countDate,officeId,"4",app.getId(),SignatureInfoType.TYPE_UPDATE_SIGNATURE));
			certData.setRenewFive(signatureInfoService.getInfocount(
					countDate,officeId,"5",app.getId(),SignatureInfoType.TYPE_UPDATE_SIGNATURE));
			certData.setSealTotal(certData.getAddOne()+certData.getAddTwo()+
					certData.getAddThree()+certData.getAddFour()+certData.getAddFive()+certData.getChangeOne()
					+certData.getChangeTwo()+certData.getChangeThree()+certData.getChangeFour()+certData.getChangeFive()+
					certData.getRenewOne()+certData.getRenewTwo()+certData.getRenewThree()+certData.getRenewFour()+certData.getRenewFive());
			Double detailedMoney=0d;
			Double detailedReceiptOut=0d;
			List<SignatureInfo> detailedsSgnatureInfos = signatureInfoService.finDaySeal(countDate, officeId,app.getId());
			for (SignatureInfo signatureInfo : detailedsSgnatureInfos) {
				SignaturePayInfo signaturePayInfo = signaturePayInfoService.findBySignatureInfo(signatureInfo);
				if(signaturePayInfo.getIsReceipt())
				{
					detailedReceiptOut+=signaturePayInfo.getTotalMoney();
				}
				detailedMoney+=signaturePayInfo.getTotalMoney();
			}
			certData.setSealMoney(detailedMoney);
			certData.setReceiptTotal(detailedReceiptOut);
			certData.setSealTotal(signatureInfoService.finSealCount(countDate, officeId, app.getId()));
			certData.setOffice(officeService.get(officeId));
			certData.setApp(app);
			certData.setStatisticDate(countDate);
			appDatas.add(certData);
		}
		session.setAttribute("appDatas", appDatas);
	}

	@RequestMapping(value = "statisticAgain")
	@ResponseBody
	public String statisticAgain(String countDate, HttpSession session) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject jsonObject = new JSONObject();
		
		User user = UserUtils.getUser();
		Long officeId = user.getOffice().getId();
		try {
			Date date = simpleDateFormat.parse(countDate);
			statisticSealDayDataService.deleteDayData(date,officeId);// 删除历史已统计数据
			logUtil.saveSysLog("日经营统计重新统计", "删除STATISTIC_DAY_DATA" + countDate
					+ "成功", null);
			statisticSealAppDataService.deleteDayData(date,officeId);// 删除明细数据
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
		HSSFSheet sheet=wb.createSheet("印章日经营统计表");
		HSSFCellStyle style=wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font=wb.createFont();
		font.setFontHeightInPoints((short)20);
		font.setCharSet(20);
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)6));
		sheet.addMergedRegion(new Region(1, (short)0, 2, (short)0));
		sheet.addMergedRegion(new Region(1, (short)3, 1, (short)5));
		HSSFRow row0=sheet.createRow(0);
		HSSFCell cell0=row0.createCell(0);
		cell0.setCellValue("印章日经营统计");
		cell0.setCellStyle(style);
		HSSFRow row1=sheet.createRow(1);
		row1.createCell(0).setCellValue("日期");
		row1.createCell(1).setCellValue("入库");
		row1.createCell(2).setCellValue("总量");
		row1.createCell(3).setCellValue("日结");
		row1.createCell(6).setCellValue("余量");
		HSSFRow row2=sheet.createRow(2);
		row2.createCell(1).setCellValue("发票");
		row2.createCell(2).setCellValue("发票");
		row2.createCell(3).setCellValue("印章");
		row2.createCell(4).setCellValue("费用");
		row2.createCell(5).setCellValue("发票");
		row2.createCell(6).setCellValue("发票");
		
		 List<StatisticSealDayData> list = statisticSealDayDataService.findByDay(startTime,endTime,office); 
		List<ConfigAppOfficeRelation> configApps=configAppOfficeRelationService.findAllByOfficeId(office);
		List<Long> appIds=Lists.newArrayList();
		if(configApps.size()>0)
		{
			for(int i=0;i<configApps.size();i++)
			{
				appIds.add(configApps.get(i).getConfigApp().getId());
			}
		}else
		{
			appIds.clear();
		}
		List<StatisticSealAppData> sealDatas = statisticSealAppDataService.findByDateAndOffice(office, appIds, startTime, endTime);
		List<List<StatisticSealAppData>>  appDatas = new ArrayList<List<StatisticSealAppData>>();
		List<ConfigApp> apps = configAppService.selectAll(); 
		for (int i = 0; i < apps.size(); i++) {
			List<StatisticSealAppData> appData = new ArrayList<StatisticSealAppData>();
			for (int j = 0; j < sealDatas.size(); j++) {
				if (apps.get(i).getAppName()
						.equals(sealDatas.get(j).getApp().getAppName())) {
					appData.add(sealDatas.get(j));
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
			rown.createCell(1).setCellValue(list.get(i).getReceiptIn());
			rown.createCell(2).setCellValue(list.get(i).getReceiptTotal());
			rown.createCell(3).setCellValue(list.get(i).getSealDay());
			rown.createCell(4).setCellValue(list.get(i).getSealMoney());
			rown.createCell(5).setCellValue(list.get(i).getReceiptDay());
			rown.createCell(6).setCellValue(list.get(i).getReceiptSurplus());
		}
		
		int appDataIndex=0;
		for(int j=0;j<appDatas.size();j++)
		{
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex),(short)0,(j*6)+(5+list.size()+appDataIndex)+3,(short)0));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex),(short)1,(j*6)+(5+list.size()+appDataIndex),(short)18));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+1, (short)1, (j*6)+(5+list.size()+appDataIndex)+1, (short)15));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+1, (short)16, (j*6)+(5+list.size()+appDataIndex)+1, (short)18));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+2, (short)1, (j*6)+(5+list.size()+appDataIndex)+2, (short)5));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+2, (short)6, (j*6)+(5+list.size()+appDataIndex)+2, (short)10));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+2, (short)11, (j*6)+(5+list.size()+appDataIndex)+2, (short)15));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+2, (short)16, (j*6)+(5+list.size()+appDataIndex)+2+1, (short)16));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+2, (short)17, (j*6)+(5+list.size()+appDataIndex)+2+1, (short)17));
			sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+2, (short)18, (j*6)+(5+list.size()+appDataIndex)+2+1, (short)18));
			HSSFCellStyle stylese=wb.createCellStyle();
			stylese.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			stylese.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont fontse=wb.createFont();
			fontse.setFontHeightInPoints((short)10);
			fontse.setFontName("宋体");
			fontse.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			stylese.setFont(fontse);
			HSSFRow serow0=sheet.createRow((j*6)+(5+list.size()+appDataIndex));
			HSSFCell cellse0=serow0.createCell(0);
			cellse0.setCellStyle(stylese);
			cellse0.setCellValue("日期");
			serow0.setHeightInPoints((short)20);
			HSSFCell cellse1=serow0.createCell(1);
			cellse1.setCellStyle(style);
			cellse1.setCellValue(appDatas.get(j).get(0).getApp().getAppName());
			HSSFRow serow1=sheet.createRow((j*6)+(5+list.size()+appDataIndex)+1);
			serow1.createCell(1).setCellValue("业务办理");
			serow1.createCell(13).setCellValue("小计");
			HSSFRow serow2=sheet.createRow((j*6)+(5+list.size()+appDataIndex)+2);
			serow2.createCell(1).setCellValue("新增");
			serow2.createCell(6).setCellValue("更新");
			serow2.createCell(11).setCellValue("变更");
			serow2.createCell(16).setCellValue("印章");
			serow2.createCell(17).setCellValue("费用");
			serow2.createCell(18).setCellValue("发票");
			HSSFRow serow3=sheet.createRow((j*6)+(5+list.size()+appDataIndex)+3);
			serow3.createCell(1).setCellValue("1年");
			serow3.createCell(2).setCellValue("2年");
			serow3.createCell(3).setCellValue("3年");
			serow3.createCell(4).setCellValue("4年");
			serow3.createCell(5).setCellValue("5年");
			serow3.createCell(6).setCellValue("1年");
			serow3.createCell(7).setCellValue("2年");
			serow3.createCell(8).setCellValue("3年");
			serow3.createCell(9).setCellValue("4年");
			serow3.createCell(10).setCellValue("5年");
			serow3.createCell(11).setCellValue("1年");
			serow3.createCell(12).setCellValue("2年");
			serow3.createCell(13).setCellValue("3年");
			serow3.createCell(14).setCellValue("4年");
			serow3.createCell(15).setCellValue("5年");
			for(int i=0;i<appDatas.get(j).size();i++)
			{
				HSSFRow serow4=sheet.createRow((j*6)+(5+list.size()+appDataIndex)+(4+i));
				serow4.createCell(0).setCellValue((appDatas.get(j).get(i).getStatisticDate()).toString());
				if(appDatas.get(j).get(i).getSealTotal()==0)
				{
					sheet.addMergedRegion(new Region((j*6)+(5+list.size()+appDataIndex)+(4+i), (short)1, (j*6)+(5+list.size()+appDataIndex)+(4+i), (short)18));
					serow4.createCell(1).setCellValue(appDatas.get(j).get(i).getApp().getAppName()+"应用当天没办理数据");
				}else if(appDatas.get(j).get(i).getSealTotal()!=0)
					{
						serow4.createCell(1).setCellValue(appDatas.get(j).get(i).getAddOne());
						serow4.createCell(2).setCellValue(appDatas.get(j).get(i).getAddTwo());
						serow4.createCell(3).setCellValue(appDatas.get(j).get(i).getAddThree());
						serow4.createCell(4).setCellValue(appDatas.get(j).get(i).getAddFour());
						serow4.createCell(5).setCellValue(appDatas.get(j).get(i).getAddFive());
						serow4.createCell(6).setCellValue(appDatas.get(j).get(i).getRenewOne());
						serow4.createCell(7).setCellValue(appDatas.get(j).get(i).getRenewTwo());
						serow4.createCell(8).setCellValue(appDatas.get(j).get(i).getRenewThree());
						serow4.createCell(9).setCellValue(appDatas.get(j).get(i).getRenewFour());
						serow4.createCell(10).setCellValue(appDatas.get(j).get(i).getRenewFive());
						serow4.createCell(11).setCellValue(appDatas.get(j).get(i).getChangeOne());
						serow4.createCell(12).setCellValue(appDatas.get(j).get(i).getChangeTwo());
						serow4.createCell(13).setCellValue(appDatas.get(j).get(i).getChangeThree());
						serow4.createCell(14).setCellValue(appDatas.get(j).get(i).getChangeFour());
						serow4.createCell(15).setCellValue(appDatas.get(j).get(i).getChangeFive());
						serow4.createCell(16).setCellValue(appDatas.get(j).get(i).getSealTotal());
						serow4.createCell(17).setCellValue(appDatas.get(j).get(i).getSealMoney());
						serow4.createCell(18).setCellValue(appDatas.get(j).get(i).getReceiptTotal());
					}
				}
			appDataIndex+=appDatas.get(j).size();
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
	public void exportCountMonth(@RequestParam(required = false) StatisticSealDayData statisticSealDayData,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime
			){
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
			sheet.addMergedRegion(new Region(0, (short)0, 0, (short)6));
			sheet.addMergedRegion(new Region(1, (short)0, 2, (short)0));
			sheet.addMergedRegion(new Region(1, (short)3, 1, (short)5));
			HSSFRow row0=sheet.createRow(0);
			HSSFCell cell0=row0.createCell(0);
			cell0.setCellValue("印章月经营统计");
			cell0.setCellStyle(style);
			HSSFRow row1=sheet.createRow(1);
			row1.createCell(0).setCellValue("日期");
			row1.createCell(1).setCellValue("入库");
			row1.createCell(2).setCellValue("总量");
			row1.createCell(3).setCellValue("月结");
			row1.createCell(6).setCellValue("余量");
			HSSFRow row2=sheet.createRow(2);
			row2.createCell(1).setCellValue("发票");
			row2.createCell(2).setCellValue("发票");
			row2.createCell(3).setCellValue("印章");
			row2.createCell(4).setCellValue("费用");
			row2.createCell(5).setCellValue("发票");
			row2.createCell(6).setCellValue("发票");
		
		List<String> monthList = getMonthList(startTime + "-01", endTime
				+ "-01");
		List<StatisticSealMonthData> sumList = new ArrayList<StatisticSealMonthData>();
			try {
				for (String s : monthList) {
					StatisticSealMonthData smd = new StatisticSealMonthData();
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
					Double receiptIn=0d; //发票入库
					Double receiptTotal=0d;//发票总量
					Integer sealDay=0;   //印章
					Double sealMoney=0d;  //费用
					Double receiptDay=0d; //今日发票
					Double receiptSurplus=0d;//发票剩余
					List<StatisticSealDayData> ListMonth = statisticSealDayDataService
							.findByMonth(office, start, end);
					for (StatisticSealDayData sdd : ListMonth) {
						receiptIn+=sdd.getReceiptIn();
						sealDay+=sdd.getSealDay();
						sealMoney+=sdd.getSealMoney();
						receiptDay+=sdd.getReceiptDay();
					}
					if (ListMonth.size()>0) {
						receiptTotal = ListMonth.get(0).getReceiptTotal();
					}
					receiptSurplus = receiptIn - receiptDay ;
					smd.setReceiptIn(receiptIn);
					smd.setReceiptTotal(receiptTotal);
					smd.setReceiptSurplus(receiptSurplus);
					smd.setReceiptDay(receiptDay);
					smd.setSealDay(sealDay);
					smd.setSealMoney(sealMoney);
					smd.setCreateDate(start);
					sumList.add(smd);
				}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			List<List<StatisticSealAppMonth>> appSumList = new ArrayList<List<StatisticSealAppMonth>>();
			Set<ConfigApp> configApps= new LinkedHashSet<ConfigApp>();
			for (String s : monthList) {
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
				List<ConfigAppOfficeRelation> configAppOfficeRelations=configAppOfficeRelationService.findAllByOfficeId(office);
				List<Long> appIds=Lists.newArrayList();
				if(configAppOfficeRelations.size()>0)
				{
					for(int i=0;i<configAppOfficeRelations.size();i++)
					{
						appIds.add(configAppOfficeRelations.get(i).getConfigApp().getId());
					}
				}else
				{
					appIds.clear();
				}
				List<StatisticSealAppData> listMonth = statisticSealAppDataService.findByMonth(office,appIds);
				if(listMonth.size()>0)
				{
					for (StatisticSealAppData configApp : listMonth) {
						configApps.add(configApp.getApp());
					}
				}
			}
			for (ConfigApp configApp : configApps) {
				List<StatisticSealAppMonth> statisticSealAppMonth = new ArrayList<StatisticSealAppMonth>();
				for (String s : monthList) {
					StatisticSealAppMonth samd = new StatisticSealAppMonth();
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

					Integer sealTotal=0;
					Double receiptTotal=0d;
					Double sealMoney=0d;
					Integer addOne=0; 	//新增财务章
					Integer addTwo=0;	//新增合同章
					Integer addThree=0;	//新增个人章
					Integer addFour=0;	//新增公章
					Integer addFive=0;
					Integer renewOne=0; 	//更新财务章
					Integer renewTwo=0;	//更新合同章
					Integer renewThree=0;	//更新个人章
					Integer renewFour=0;	//更新公章
					Integer renewFive=0; 
					Integer changeOne=0; 	//变更财务章
					Integer changeTwo=0;	//变更合同章
					Integer changeThree=0;	//变更个人章
					Integer changeFour=0;	//变更公章
					Integer changeFive=0;
					List<StatisticSealAppData> listMonth = statisticSealAppDataService
							.findByMonth(configApp, office, start, end);
					for (StatisticSealAppData sad : listMonth) {
						sealTotal+=sad.getSealTotal();
						receiptTotal+=sad.getReceiptTotal();
						sealMoney+=sad.getSealMoney();
						addOne+=sad.getAddOne();
						addTwo+=sad.getAddTwo();
						addThree+=sad.getAddThree();
						addFour+=sad.getAddFour();
						addFive+=sad.getAddFive();
						renewOne+=sad.getRenewOne();
						renewTwo+=sad.getRenewTwo();
						renewThree+=sad.getRenewThree();
						renewFour+=sad.getRenewFour();
						renewFive+=sad.getRenewFive();
						changeOne+=sad.getChangeOne();
						changeTwo+=sad.getChangeTwo();
						changeThree+=sad.getChangeThree();
						changeFour+=sad.getChangeFour();
						changeFive+=sad.getChangeFive();
					}
					samd.setSealTotal(sealTotal);
					samd.setReceiptTotal(receiptTotal);
					samd.setSealMoney(sealMoney);
					samd.setAddOne(addOne);
					samd.setAddTwo(addTwo);
					samd.setAddThree(addThree);
					samd.setAddThree(addThree);
					samd.setAddFour(addFour);
					samd.setAddFive(addFive);
					samd.setRenewOne(renewOne);
					samd.setRenewTwo(renewTwo);
					samd.setRenewThree(renewThree);
					samd.setRenewFour(renewFour);
					samd.setRenewFive(renewFive);
					samd.setChangeOne(changeOne);
					samd.setChangeTwo(changeTwo);
					samd.setChangeThree(changeThree);
					samd.setChangeFour(changeFour);
					samd.setChangeFive(changeFive);
					samd.setApp(configApp);
					samd.setStatisticDate(start);
					statisticSealAppMonth.add(samd);

				}

				appSumList.add(statisticSealAppMonth);
			}
		
		for(int i=0;i<sumList.size();i++)
		{
			SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd");
			String date=simple.format(sumList.get(i).getCreateDate());
			HSSFRow rown=sheet.createRow(i+3);
			rown.createCell(0).setCellValue(date);
			rown.createCell(1).setCellValue(sumList.get(i).getReceiptIn());
			rown.createCell(2).setCellValue(sumList.get(i).getReceiptTotal());
			rown.createCell(3).setCellValue(sumList.get(i).getSealDay());
			rown.createCell(4).setCellValue(sumList.get(i).getSealMoney());
			rown.createCell(5).setCellValue(sumList.get(i).getReceiptDay());
			rown.createCell(6).setCellValue(sumList.get(i).getReceiptSurplus());
		}
		int appDataIndex=0;
		for(int j=0;j<appSumList.size();j++)
		{
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex),(short)0,(j*6)+(5+sumList.size()+appDataIndex)+3,(short)0));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex),(short)1,(j*6)+(5+sumList.size()+appDataIndex),(short)18));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+1, (short)1, (j*6)+(5+sumList.size()+appDataIndex)+1, (short)15));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+1, (short)16, (j*6)+(5+sumList.size()+appDataIndex)+1, (short)18));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+2, (short)1, (j*6)+(5+sumList.size()+appDataIndex)+2, (short)5));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+2, (short)6, (j*6)+(5+sumList.size()+appDataIndex)+2, (short)10));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+2, (short)11, (j*6)+(5+sumList.size()+appDataIndex)+2, (short)15));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+2, (short)16, (j*6)+(5+sumList.size()+appDataIndex)+2+1, (short)16));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+2, (short)17, (j*6)+(5+sumList.size()+appDataIndex)+2+1, (short)17));
			sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+2, (short)18, (j*6)+(5+sumList.size()+appDataIndex)+2+1, (short)18));
			HSSFCellStyle stylese=wb.createCellStyle();
			stylese.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			stylese.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont fontse=wb.createFont();
			fontse.setFontHeightInPoints((short)10);
			fontse.setFontName("宋体");
			fontse.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			stylese.setFont(fontse);
			HSSFRow serow0=sheet.createRow((j*6)+(5+sumList.size()+appDataIndex));
			HSSFCell cellse0=serow0.createCell(0);
			cellse0.setCellStyle(stylese);
			cellse0.setCellValue("日期");
			serow0.setHeightInPoints((short)20);
			HSSFCell cellse1=serow0.createCell(1);
			cellse1.setCellStyle(style);
			cellse1.setCellValue(appSumList.get(j).get(0).getApp().getAppName());
			HSSFRow serow1=sheet.createRow((j*6)+(5+sumList.size()+appDataIndex)+1);
			serow1.createCell(1).setCellValue("业务办理");
			serow1.createCell(16).setCellValue("小计");
			HSSFRow serow2=sheet.createRow((j*6)+(5+sumList.size()+appDataIndex)+2);
			serow2.createCell(1).setCellValue("新增");
			serow2.createCell(6).setCellValue("更新");
			serow2.createCell(11).setCellValue("变更");
			serow2.createCell(16).setCellValue("印章");
			serow2.createCell(17).setCellValue("费用");
			serow2.createCell(18).setCellValue("发票");
			HSSFRow serow3=sheet.createRow((j*6)+(5+sumList.size()+appDataIndex)+3);
			serow3.createCell(1).setCellValue("1年");
			serow3.createCell(2).setCellValue("2年");
			serow3.createCell(3).setCellValue("3年");
			serow3.createCell(4).setCellValue("4年");
			serow3.createCell(5).setCellValue("5年");
			serow3.createCell(6).setCellValue("1年");
			serow3.createCell(7).setCellValue("2年");
			serow3.createCell(8).setCellValue("3年");
			serow3.createCell(9).setCellValue("4年");
			serow3.createCell(10).setCellValue("5年");
			serow3.createCell(11).setCellValue("1年");
			serow3.createCell(12).setCellValue("2年");
			serow3.createCell(13).setCellValue("3年");
			serow3.createCell(14).setCellValue("4年");
			serow3.createCell(15).setCellValue("5年");
			for(int i=0;i<appSumList.get(j).size();i++)
			{
				HSSFRow serow4=sheet.createRow((j*6)+(5+sumList.size()+appDataIndex)+(4+i));
				serow4.createCell(0).setCellValue((new SimpleDateFormat("yyyy-MM-dd").format(appSumList.get(j).get(i).getStatisticDate())));
				if(appSumList.get(j).get(i).getSealTotal()==0)
				{
					sheet.addMergedRegion(new Region((j*6)+(5+sumList.size()+appDataIndex)+(4+i), (short)1, (j*6)+(5+sumList.size()+appDataIndex)+(4+i), (short)36));
					serow4.createCell(1).setCellValue(appSumList.get(j).get(i).getApp().getAppName()+"应用当月没办理数据");
				}else if(appSumList.get(j).get(i).getSealTotal()!=0)
				{
					serow4.createCell(1).setCellValue(appSumList.get(j).get(i).getAddOne());
					serow4.createCell(2).setCellValue(appSumList.get(j).get(i).getAddTwo());
					serow4.createCell(3).setCellValue(appSumList.get(j).get(i).getAddThree());
					serow4.createCell(4).setCellValue(appSumList.get(j).get(i).getAddFour());
					serow4.createCell(5).setCellValue(appSumList.get(j).get(i).getAddFive());
					serow4.createCell(6).setCellValue(appSumList.get(j).get(i).getRenewOne());
					serow4.createCell(7).setCellValue(appSumList.get(j).get(i).getRenewTwo());
					serow4.createCell(8).setCellValue(appSumList.get(j).get(i).getRenewThree());
					serow4.createCell(9).setCellValue(appSumList.get(j).get(i).getRenewFour());
					serow4.createCell(10).setCellValue(appSumList.get(j).get(i).getRenewFive());
					serow4.createCell(11).setCellValue(appSumList.get(j).get(i).getChangeOne());
					serow4.createCell(12).setCellValue(appSumList.get(j).get(i).getChangeTwo());
					serow4.createCell(13).setCellValue(appSumList.get(j).get(i).getChangeThree());
					serow4.createCell(14).setCellValue(appSumList.get(j).get(i).getChangeFour());
					serow4.createCell(15).setCellValue(appSumList.get(j).get(i).getChangeFive());
					serow4.createCell(16).setCellValue(appSumList.get(j).get(i).getSealTotal());
					serow4.createCell(17).setCellValue(appSumList.get(j).get(i).getSealMoney());
					serow4.createCell(18).setCellValue(appSumList.get(j).get(i).getReceiptTotal());
				}
			}
			appDataIndex+=appSumList.get(j).size();
		}
		
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=operateSealCountMonth.xls");
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
//		
	}
	
	
}




