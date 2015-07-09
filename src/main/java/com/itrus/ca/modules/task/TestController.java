package com.itrus.ca.modules.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.itrus.ca.common.service.SendMsgService;
import com.itrus.ca.common.utils.MailSend;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptLog;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptLogService;
import com.itrus.ca.modules.statistic.entity.StatisticAppData;
import com.itrus.ca.modules.statistic.entity.StatisticCertData;
import com.itrus.ca.modules.statistic.entity.StatisticCertDataProduct;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;
import com.itrus.ca.modules.statistic.service.StatisticAppDataService;
import com.itrus.ca.modules.statistic.service.StatisticCertDataProductService;
import com.itrus.ca.modules.statistic.service.StatisticCertDataService;
import com.itrus.ca.modules.statistic.service.StatisticDayDataService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;

@Controller
@RequestMapping(value = "/test")
public class TestController {

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
	private ConfigAppService configAppService;

	@Autowired
	ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	StatisticCertDataService statisticCertDataService;

	@Autowired
	StatisticAppDataService statisticAppDataService;

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;

	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;

	@Autowired
	private StatisticCertDataProductService statisticCertDataProductService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private StatisticDayDataService statisticDayDataService;

	@Autowired
	private SendMsgService msgService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private WorkLogService workLogService;

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;
	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private WorkPayInfoService workPayInfoService;

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private ConfigProductService configProductService;

	@Autowired
	private WorkCompanyHisService workCompanyHisService;

	@Autowired
	private WorkUserHisService workUserHisService;

	private LogUtil logUtil = new LogUtil();

	/**
	 * 
	 * 日经营统计定时任务 总量日结
	 * 
	 * @see{StatisticDayData
	 * @see{StatisticAppData
	 */
	@RequestMapping(value = "day")
	@ResponseBody
	public String staticDayCertData(String curDate) {
		JSONObject json = new JSONObject();
		try {
			json.put("status", -1);
			List<Office> offices = officeService.getOfficeList(2);// 得到所有网点
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date cur = sdf.parse(curDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(cur);
			calendar.add(Calendar.DATE, -1); // 得到前1天只有在这个服务中是这样的，定时任务中以实际时间为准
			Date yesterDay = calendar.getTime();
			
			calendar.add(Calendar.DATE, -1); // 得到前1天只有在这个服务中是这样的，定时任务中以实际时间为准
			
			Date staticYestDay = calendar.getTime();//获得前一天的统计日期

			for (Office office : offices) {
				List<StatisticDayData> yesterDayDatas = statisticDayDataService
						.getYesterDayDatas(staticYestDay, office);

				// 需要统计key、发票当天的出库单和入库单
				// 统计业务办理部分证书当天新增、更新、变更、补办成功的数量(查work_deal_info status是已获得证书即可)

				StatisticDayData statisticDayData = new StatisticDayData();

				int keyIn = 0; // key入库量
				Double receiptIn = 0d; // 发票入库量
				int keyYest = 0; // key总量 -- 前一天数量
				Double receiptYest = 0d; // 发票数量 --前一天数量
				int keyLast = 0; // key余量
				Double receiptLast = 0d; // 发票余量
				int certTotal = 0; // 证书发放总量
				Double paymentTotal = 0d; // 支付信息总量
				int keyOver = 0; // key出库总量

				if (yesterDayDatas != null && yesterDayDatas.size() > 0) {
					StatisticDayData data = yesterDayDatas.get(0);
					keyYest = data.getKeyStoreTotal();
					receiptYest = data.getReceiptStoreTotal();
//					yesterDay = data.getCreateDate();
				}
				// 入库单
				List<KeyUsbKey> keyUsbkeys = keyUsbKeyService.findByCreateDate(
						yesterDay, office.getId(), cur);// >=昨天到现在的时间
				for (int i = 0; i < keyUsbkeys.size(); i++) {
					keyIn += keyUsbkeys.get(i).getCount();
				}
				/**
				 * key出库
				 */
				List<KeyUsbKeyInvoice> keyUsbKeyInvoices = keyUsbKeyInvoiceService
						.findByCreateDate(yesterDay, office.getId(), cur);
				for (int i = 0; i < keyUsbKeyInvoices.size(); i++) {
					keyOver += keyUsbKeyInvoices.get(i).getDeliveryNum();
				}
				/**
				 * 发票入库
				 */
				List<ReceiptEnterInfo> receiptEnterInfos = receiptEnterInfoService
						.findByCreateDate(yesterDay, office.getId(), cur);
				for (int i = 0; i < receiptEnterInfos.size(); i++) {
					receiptIn += receiptEnterInfos.get(i).getReceiptMoney();
				}
				// 证书总量
				certTotal = workDealInfoService.getCertPublishTimes(yesterDay,
						office.getId(), cur);
				// 金额总量
				paymentTotal = workDealInfoService.getWorkPayMoney(yesterDay,
						office.getId(), cur);
				// key余量
				keyLast = keyUsbKeyDepotService
						.findDepotTotalNumByOffice(office.getId());
				receiptLast = receiptDepotInfoService.getCurLastReceipt(office
						.getId());

				statisticDayData.setCertMoneyTotal(paymentTotal);
				statisticDayData.setCertTotal(certTotal);
				statisticDayData.setCreateDate(new Date());
				statisticDayData.setKeyIn(keyIn);
				statisticDayData.setKeyStoreTotal(keyLast);// key余量
				statisticDayData.setKeyOver(keyOver);
				statisticDayData.setKeyTotal(keyYest);// key总量(昨天的量)
				statisticDayData.setReceiptIn(receiptIn);
				statisticDayData.setReceiptTotal(receiptYest);
				statisticDayData.setReceiptStoreTotal(receiptLast);// 余量
				statisticDayData.setOffice(office);
				statisticDayData.setStatisticDate(yesterDay);
				statisticDayData.setCreateDate(cur);
				statisticDayDataService.save(statisticDayData);
				// ======================日经营汇总表结束========================
				// ======================日经营详情表===========================
				List<ConfigAppOfficeRelation> apps = configAppOfficeRelationService
						.findAllByOfficeId(office.getId());
				for (ConfigAppOfficeRelation relation : apps) {
					if (relation.getConfigApp() == null) {
						continue;
					}
					ConfigApp app = relation.getConfigApp();
					StatisticAppData certData = new StatisticAppData();
					/**
					 * 根据应用、网点、时间、年限生成数据
					 */
					certData.setAdd1(workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 1, app.getId(),
							WorkDealInfoType.TYPE_ADD_CERT, cur));
					certData.setAdd2(workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 2, app.getId(),
							WorkDealInfoType.TYPE_ADD_CERT, cur));
					certData.setAdd4(workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 4, app.getId(),
							WorkDealInfoType.TYPE_ADD_CERT, cur));
					certData.setRenew1(workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 1, app.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, cur));
					certData.setRenew2(workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 2, app.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, cur));
					certData.setRenew4(workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 4, app.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, cur));

					certData.setModifyNum(workDealInfoService
							.getCertAppYearInfo(yesterDay, office.getId(), 0,
									app.getId(),
									WorkDealInfoType.TYPE_INFORMATION_REROUTE,
									cur));
					certData.setReissueNum(workDealInfoService
							.getCertAppYearInfo(yesterDay, office.getId(), 0,
									app.getId(),
									WorkDealInfoType.TYPE_LOST_CHILD, cur));
					certData.setCertTotal(certData.getAdd1()
							+ certData.getAdd2() + certData.getAdd4()
							+ certData.getRenew1() + certData.getRenew2()
							+ certData.getRenew4() + certData.getModifyNum()
							+ certData.getReissueNum());
					certData.setReceiptTotal(workDealInfoService
							.getWorkPayReceipt(yesterDay, office.getId(),
									app.getId(), cur));
					certData.setKeyTotal(workDealInfoService
							.getKeyPublishTimes(yesterDay, office.getId(),
									app.getId(), cur));
					certData.setOffice(office);
					certData.setApp(app);
					certData.setStatisticDate(yesterDay);
					statisticAppDataService.save(certData);
				}
			}
			json.put("status", 1);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			json.put("msg",
					"计算完毕:" + sdf1.format(yesterDay) + "到" + sdf1.format(cur)
							+ "之间所有数据");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 证书发放量统计定时任务
	 * 
	 * @throws ParseException
	 * @see{StatisticCertData //企业证书or个人证书统计
	 * @see{StatisticCertDataProduct //汇总统计
	 * 
	 */
	@RequestMapping(value = "cert")
	@ResponseBody
	public String workDayStatic(String curDate) throws ParseException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", -1);
			List<Office> offices = officeService.getOfficeList(2);// 得到所有网点
			List<String> productType = new ArrayList<String>();
			HashMap<String, Integer> idMap = ProductType.productTypeIdMap;
			productType.add(idMap.get(ProductType.PRODUCT_TYPE_ENT).toString());
			productType
					.add(idMap.get(ProductType.PRODUCT_TYPE_PERE).toString());
			productType
					.add(idMap.get(ProductType.PRODUCT_TYPE_PERO).toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date cur = sdf.parse(curDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(cur);
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			Date yesterDay = calendar.getTime();

			for (Office office : offices) {
				// 查询所有的应用
				List<ConfigAppOfficeRelation> configApps = configAppOfficeRelationService
						.findAllByOfficeId(office.getId());
				// 遍历每个应用，查询当天签发的所有证书--查deal_info表
				for (ConfigAppOfficeRelation appOfficeRelation : configApps) {
					List<Integer> types = new ArrayList<Integer>();
					types.add(0);
					types.add(1);
					types.add(2);
					for (Integer type : types) {
						StatisticCertData data = new StatisticCertData();
						ConfigApp app = appOfficeRelation.getConfigApp();
						data.setApp(app);
						data.setOffice(office);
						data.setCountDate(sdf.parse(curDate));

						Integer add1 = workDealInfoService.getCertAppYearInfo(
								yesterDay, office.getId(), 1, app.getId(),
								WorkDealInfoType.TYPE_ADD_CERT, productType,
								type, cur);
						Integer add2 = workDealInfoService.getCertAppYearInfo(
								yesterDay, office.getId(), 2, app.getId(),
								WorkDealInfoType.TYPE_ADD_CERT, productType,
								type, cur);
						Integer add4 = workDealInfoService.getCertAppYearInfo(
								yesterDay, office.getId(), 4, app.getId(),
								WorkDealInfoType.TYPE_ADD_CERT, productType,
								type, cur);
						Integer renew1 = workDealInfoService
								.getCertAppYearInfo(yesterDay, office.getId(),
										1, app.getId(),
										WorkDealInfoType.TYPE_UPDATE_CERT,
										productType, type, cur);
						Integer renew2 = workDealInfoService
								.getCertAppYearInfo(yesterDay, office.getId(),
										2, app.getId(),
										WorkDealInfoType.TYPE_UPDATE_CERT,
										productType, type, cur);
						Integer renew4 = workDealInfoService
								.getCertAppYearInfo(yesterDay, office.getId(),
										4, app.getId(),
										WorkDealInfoType.TYPE_UPDATE_CERT,
										productType, type, cur);
						data.setAdd1(add1);
						data.setAdd2(add2);
						data.setAdd4(add4);
						data.setRenew1(renew1);
						data.setRenew2(renew2);
						data.setRenew4(renew4);
						data.setPayType(type);
						// 每一个po存的是 某个应用下某种证书（企业or个人）某种付款方式 新增、更新了多少
						statisticCertDataService.save(data);
					}
				}
				// ==================================
				// ============证书发放量汇总==========
				// ==================================
				for (String certType : ProductType.productTypeStrMap.keySet()) {
					List<String> type = new ArrayList<String>();
					type.add(certType);
					StatisticCertDataProduct product = new StatisticCertDataProduct();
					Integer year1 = workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 1, null, null, type,
							null, cur);
					Integer year2 = workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 2, null, null, type,
							null, cur);
					Integer year4 = workDealInfoService.getCertAppYearInfo(
							yesterDay, office.getId(), 4, null, null, type,
							null, cur);
					product.setCountDate(cur);
					product.setOffice(office);
					product.setProductType(Integer.valueOf(certType));
					product.setYear1(year1);
					product.setYear2(year2);
					product.setYear4(year4);
					// 某个网点 某种类型证书 发放数量
					statisticCertDataProductService.save(product);
				}
			}
			json.put("status", 1);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			json.put("msg",
					"计算完毕:" + sdf1.format(yesterDay) + "到" + sdf1.format(cur)
							+ "之间所有数据");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json.toString();
	}

	/**
	 * 清除定时任务数据
	 * 
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "clear")
	@ResponseBody
	public String clear(Integer type, String day) throws ParseException {
		JSONObject json = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cur = sdf.parse(day);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cur);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		try {
			json.put("status", -1);
			if (type.equals(0)) {// 证书发放量统计
				statisticCertDataService.deleteDataByDay(calendar.getTime());
				statisticCertDataProductService.deleteDayData(calendar.getTime());
			} else {// 日经营统计
				statisticDayDataService.deleteDayData(calendar.getTime());
				statisticAppDataService.deleteDayData(calendar.getTime());
			}
			json.put("status", 1);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			json.put("msg", "删除完毕:" + sdf1.format(calendar.getTime()) + "到"
					+ sdf1.format(cur) + "之间所有数据");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json.toString();
	}

	public void test() {
		System.out.println("auto task thread is valid!");
	}

	/**
	 * 发票统计
	 */
	public void receiptTask() {
		List<ReceiptDepotInfo> receiptDepotInfos = receiptDepotInfoService
				.findAllDepot();
		for (ReceiptDepotInfo receiptDepotInfo : receiptDepotInfos) {
			ReceiptLog rl = new ReceiptLog();
			rl.setReceiptDepotInfo(receiptDepotInfo);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dayTime = sdf.format(date);
			try {
				rl.setCreateDate(new Timestamp(sdf.parse(dayTime).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			rl.setReceiptTotal(receiptDepotInfo.getReceiptTotal());
			rl.setReceiptOut(receiptDepotInfo.getReceiptOut());
			rl.setReceiptResidue(receiptDepotInfo.getReceiptResidue());
			receiptLogService.save(rl);
		}
	}

	/**
	 * 发票和库存发送预警邮件
	 */
	@RequestMapping(value = "/sendMail")
	@ResponseBody
	public String sendMail() {
		JSONObject json = new JSONObject();
		try {
			json.put("status", -1);
			System.out.println("发送邮件");
			List<ReceiptDepotInfo> receiptDepotInfos = receiptDepotInfoService
					.findAllDepot();
			MailSend mailSend = new MailSend();
			for (ReceiptDepotInfo receiptDepotInfo : receiptDepotInfos) {
				if (receiptDepotInfo.getReceiptResidue() != null
						&& receiptDepotInfo.getPrewarning() != null) {
					if (receiptDepotInfo.getReceiptResidue() < receiptDepotInfo
							.getPrewarning()) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("USER_NAME",
								receiptDepotInfo.getReceiptCommUser());
						map.put("CONTENT", receiptDepotInfo.getReceiptName()
								+ "该网点发票库存不足请及时申请调拨");
						msgService.sendMail(receiptDepotInfo.getEmail(),
								"发票库存不足", map, 1);
					}
				}
			}
			// 库存发送邮件

			List<KeyDepotGeneralStatistics> statistics = keyDepotGeneralStatisticsService
					.findByAlarm();
			List<Long> idList = Lists.newArrayList();
			for (int i = 0; i < statistics.size(); i++) {
				idList.add(statistics.get(i).getKeyUsbKeyDepot().getId());
			}
			List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService
					.findByDepotIds(idList);
			for (int i = 0; i < depots.size(); i++) {
				List<KeyDepotGeneralStatistics> statis = Lists.newArrayList();
				for (int j = 0; j < statistics.size(); j++) {
					if (depots
							.get(i)
							.getId()
							.equals(statistics.get(j).getKeyUsbKeyDepot()
									.getId())) {
						statis.add(statistics.get(j));
					}
				}
				if (statis.size() > 0) {
					String content = "";
					for (int j = 0; j < statis.size(); j++) {
						content += statis.get(j).getKeyGeneralInfo().getName();
						if (j != statis.size() - 1) {
							content += "、";
						}
					}
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("USER_NAME", depots.get(i).getLinkmanName());
					map.put("CONTENT", depots.get(i).getDepotName() + "仓库中：'"
							+ content + "'库存不足请及时申请调拨");
					msgService.sendMail(depots.get(i).getLinkmanEmail(),
							"key库存不足", map, 0);
				}
			}
			System.out.println("发送邮件结束");
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json.toString();
	}

	@RequestMapping(value = "showAllDealInfo")
	@ResponseBody
	public String showAllDealInfo() throws JSONException {
		List<WorkDealInfo> dealInfos = new ArrayList<WorkDealInfo>();
		dealInfos = workDealInfoService.getAllObtainedDealInfo();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		json.put("status", -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (WorkDealInfo dealInfo : dealInfos) {
			JSONObject a = new JSONObject();
			try {
				a.put("id", dealInfo.getId());
				a.put("sn", dealInfo.getSvn());
				a.put("year", dealInfo.getYear());
				a.put("keysn", dealInfo.getKeySn());
				a.put("certsn", dealInfo.getCertSn());
				a.put("obtain", sdf.format(dealInfo.getObtainedDate()));
				a.put("isSettle", dealInfo.getCanSettle() ? "是" : "否");
				a.put("settleYear", dealInfo.getPhysicalLife());
				a.put("payMethod", getPayMethodStr(dealInfo.getWorkPayInfo()));
				a.put("dealType1", WorkDealInfoType
						.getDealInfoTypeName(dealInfo.getDealInfoType()));
				a.put("dealType2", WorkDealInfoType
						.getDealInfoTypeName(dealInfo.getDealInfoType1()));
				a.put("dealTotal", dealInfo.getWorkPayInfo()
						.getWorkTotalMoney());
				a.put("payTotal", dealInfo.getWorkPayInfo().getWorkPayedMoney());
				a.put("receiptAmount", dealInfo.getWorkPayInfo()
						.getUserReceipt() ? dealInfo.getWorkPayInfo()
						.getReceiptAmount() : "0");
				a.put("dealinfostatus",
						WorkDealInfoStatus.WorkDealInfoStatusMap.get(dealInfo
								.getDealInfoStatus()));
				a.put("office", dealInfo.getCreateBy().getOffice().getName());
				a.put("user", dealInfo.getCreateBy().getName());
				a.put("certDate",
						sdf.format(dealInfo.getWorkCertInfo().getNotbefore())
								+ "---"
								+ sdf.format(dealInfo.getWorkCertInfo()
										.getNotafter()));
				ConfigRaAccount raAccount = dealInfo.getConfigProduct()
						.getConfigRaAccounts().iterator().next();
				a.put("ou", raAccount.getAccountOrgUnit());
				a.put("isTest", raAccount.getIsTest() ? "是" : "否");
				a.put("product", ProductType.getProductTypeName(Integer
						.valueOf(dealInfo.getConfigProduct().getProductName())));
			} catch (Exception e) {
				// TODO: handle exception
			}
			array.put(a);
		}
		json.put("result", array);
		json.put("status", 1);
		return json.toString();
	}

	public String getPayMethodStr(WorkPayInfo payInfo) {
		String result = new String();
		if (payInfo.getMethodAlipay()) {
			result += "|支付宝";
		}
		if (payInfo.getMethodBank()) {
			result += "|银行转账";
		}
		if (payInfo.getMethodContract()) {
			result += "|合同采购";
		}
		if (payInfo.getMethodGov()) {
			result += "|政府统一采购";
		}
		if (payInfo.getMethodMoney()) {
			result += "|现金";
		}
		if (payInfo.getMethodPos()) {
			result += "|pos转账";
		}
		/**
		 * 1 现金 2pos 3银行 4支付宝
		 */
		if (payInfo.getRelationMethod() != null) {
			Integer relation = payInfo.getRelationMethod();
			if (relation == 1) {
				result += "|现金(绑定)";
			}
			if (relation == 2) {
				result += "|pos(绑定)";
			}
			if (relation == 3) {
				result += "|银行(绑定)";
			}
			if (relation == 4) {
				result += "|支付宝(绑定)";
			}
		}
		return result;
	}

	/**
	 * 获取网点、产品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAddPrepareData")
	@ResponseBody
	public String getAddPrepareData() {
		JSONObject json = new JSONObject();
		try {
			List<Office> offices = officeService.getOfficeList(2);
			List<ConfigProduct> products = configProductService.findAll();
			JSONArray array1 = new JSONArray();
			for (Office o : offices) {
				JSONObject off = new JSONObject();
				off.put("id", o.getId());
				off.put("name", o.getName());
				array1.put(off);
			}
			JSONArray array2 = new JSONArray();
			for (ConfigProduct p : products) {
				JSONObject pro = new JSONObject();
				pro.put("id", p.getId());
				pro.put("name",
						p.getConfigApp().getAppName()
								+ "---"
								+ ProductType.getProductTypeName(Integer
										.valueOf(p.getProductName())));
				array2.put(pro);
			}
			json.put("office", array1.toString());
			json.put("product", array2.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json.toString();
	}

	static Long MILL = 86400000L;

	private int getLastCertDay(Date notAfter) {
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong<nowLong) {
			return 0;
		}
		return (int) ((notAfterLong - nowLong) / MILL);
	}
	@RequestMapping(value="testIp")
	public void test1(){
		try {
			String regIp  = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,4}";//ip
			String regDomainName = "(?<=//|)((\\w)+\\.)+\\w+";//域名
			String str = "http://192.168.103.42:909/dfd";
			Pattern pattern = Pattern.compile(regIp);
			Matcher matcher = pattern.matcher(str);
			String address = null;
			if(matcher.find()){
				System.out.println(matcher.group());
				address = matcher.group();
			}else{
				pattern = Pattern.compile(regDomainName);
				matcher = pattern.matcher(str);
				if(matcher.find()){
					System.out.println(matcher.group());
					address = matcher.group();
				}
			}

//			Runtime runtime = Runtime.getRuntime();
//			Process process = runtime.exec("ping " + address);
//			InputStream inputStream = process.getInputStream();
//			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//			String line = bufferedReader.readLine();
//			Boolean flag = false;
//			while (line != null){
//				System.out.println(line);
//				if(line.contains("ttl")){
//					flag = true;
//					break;
//				}
//			}
//			inputStream.close();
//			inputStreamReader.close();
//			bufferedReader.close();
//			runtime.exit(1);
//			if(flag){
//				System.out.println(address+"连通");
//			}else{
//				System.out.println(address+"连不通");
//			}

			HttpURLConnection connection = (HttpURLConnection) new URL("http://192.168.103.42:8081")
					.openConnection();
			connection.setConnectTimeout(30000);
			int state = connection.getResponseCode();
			if (state == 200) {
				System.out.println(
						"dddd"
				);
				// String realurl = connection.getURL().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public Status httpConnect(String url, int timeout) {
//		if (url == null || url.length() <= 0) {
//			return Status.FAIl;
//		}
//		try {
//			HttpURLConnection connection = (HttpURLConnection) new URL(url)
//					.openConnection();
//			connection.setConnectTimeout(timeout);
//			int state = connection.getResponseCode();
//			if (state == 200) {
//				// String realurl = connection.getURL().toString();
//				return Status.VALID;



//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return Status.FAIl;
//	}
}
