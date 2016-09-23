package com.itrus.ca.modules.task;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.itrus.ca.common.service.SendMsgService;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.MailSend;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
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
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

@Repository(value = "controlTask")
public class AutoTask {

	private Log log = LogFactory.getLog(AutoTask.class);

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

	private LogUtil logUtil = new LogUtil();

	/**
	 * 
	 * 日经营统计定时任务 总量日结
	 * 
	 * @throws ParseException
	 * @see{StatisticDayData
	 * @see{StatisticAppData
	 */
	public void staticDayCertData() throws ParseException {
		System.out.println("=======================================");
		System.out.println("==============日经营统计开始================");
		List<Office> offices = officeService.getOfficeList(2);// 得到所有网点
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Office office : offices) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); // 得到前一天
			Date yesterDay = calendar.getTime();

			List<StatisticDayData> yesterDayDatas = statisticDayDataService
					.getYesterDayDatas(yesterDay, office);

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
				receiptYest = data.getReceiptTotal();
				// yesterDay = data.getCreateDate();
			}
			// 入库单
			List<KeyUsbKey> keyUsbkeys = keyUsbKeyService.findByCreateDate(
					yesterDay, office.getId());// >=昨天到现在的时间
			for (int i = 0; i < keyUsbkeys.size(); i++) {
				keyIn += keyUsbkeys.get(i).getCount();
			}
			/**
			 * key出库
			 */
			List<KeyUsbKeyInvoice> keyUsbKeyInvoices = keyUsbKeyInvoiceService
					.findByCreateDate(yesterDay, office.getId());
			for (int i = 0; i < keyUsbKeyInvoices.size(); i++) {
				keyOver += keyUsbKeyInvoices.get(i).getDeliveryNum();
			}
			/**
			 * 发票入库
			 */
			List<ReceiptEnterInfo> receiptEnterInfos = receiptEnterInfoService
					.findByCreateDate(yesterDay, office.getId());
			for (int i = 0; i < receiptEnterInfos.size(); i++) {
				receiptIn += receiptEnterInfos.get(i).getReceiptMoney();
			}
			// 证书总量
			certTotal = workDealInfoService.getCertPublishTimes(yesterDay,
					office.getId());
			// 金额总量
			paymentTotal = workDealInfoService.getWorkPayMoney(yesterDay,
					office.getId());
			// key余量
			keyLast = keyUsbKeyDepotService.findDepotTotalNumByOffice(office
					.getId());
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
			statisticDayData
					.setStatisticDate(sdf.parse(sdf.format(new Date())));
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
						WorkDealInfoType.TYPE_ADD_CERT));
				certData.setAdd2(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 2, app.getId(),
						WorkDealInfoType.TYPE_ADD_CERT));
				certData.setAdd3(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 3, app.getId(),
						WorkDealInfoType.TYPE_ADD_CERT));
				certData.setAdd4(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 4, app.getId(),
						WorkDealInfoType.TYPE_ADD_CERT));
				certData.setAdd5(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 5, app.getId(),
						WorkDealInfoType.TYPE_ADD_CERT));
				certData.setRenew1(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 1, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT));
				certData.setRenew2(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 2, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT));
				certData.setRenew3(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 3, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT));
				certData.setRenew4(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 4, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT));
				certData.setRenew5(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 5, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT));

				certData.setModifyNum(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 0, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE));
				certData.setReissueNum(workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 0, app.getId(),
						WorkDealInfoType.TYPE_LOST_CHILD));
				certData.setCertTotal(certData.getAdd1() + certData.getAdd2()+ certData.getAdd3()
						+ certData.getAdd4() +certData.getAdd5()+ certData.getRenew1()
						+ certData.getRenew2() + certData.getRenew3() +certData.getRenew4()
						+ certData.getModifyNum() + certData.getReissueNum());
				certData.setReceiptTotal(workDealInfoService.getWorkPayReceipt(
						yesterDay, office.getId(), app.getId()));
				certData.setKeyTotal(workDealInfoService.getKeyPublishTimes(
						yesterDay, office.getId(), app.getId()));
				certData.setOffice(office);
				certData.setApp(app);
				certData.setStatisticDate(new Date());
				statisticAppDataService.save(certData);
			}
		}

		System.out.println("=======================================");
		System.out.println("==============日经营统计结束================");
	}

	/**
	 * 证书发放量统计定时任务
	 * 
	 * @throws ParseException
	 * @see{StatisticCertData //5种证书
	 * @see{StatisticCertDataProduct //汇总统计、
	 * 当天业务第二天进行定时任务统计
	 */
	public void workDayStatic() throws ParseException {
		System.out.println("=======================================");
		System.out.println("==============证书发放量统计开始=============");
		List<Office> offices = officeService.getOfficeList(2);// 得到所有网点
																// 机构类型（1：公司；2：部门；3：小组）
																// 符合的赋值给offices
		List<String> productType = new ArrayList<String>();// 定义一个productType的数组，存放productType类型
		HashMap<String, String> idMap = ProductType.productTypeStrMap;// 得到所有产品类型
		for (Entry<Integer, String> entry : ProductType.productTypeMap.entrySet()) {    
			productType.add(entry.getKey().toString());
			
		}
		
//		productType.add(idMap.get(ProductType.PRODUCT_TYPE_ENT).toString());// 企业证书 1
//		productType.add(idMap.get(ProductType.PRODUCT_TYPE_PERE).toString());// 个人证书(企业) 2
//		productType.add(idMap.get(ProductType.PRODUCT_TYPE_PERO).toString());// 个人证书(机构) 6
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterDay = calendar.getTime();
		for (Office office : offices) { // 按照符合的网点遍历
			// 查询所有的应用
			List<ConfigAppOfficeRelation> configApps = configAppOfficeRelationService
					.findAllByOfficeId(office.getId());// 查询网点下的所有应用
			// 遍历每个应用，查询当天签发的所有证书--查deal_info表
			List<Integer> productTypes = new ArrayList<Integer>();
			for (ConfigAppOfficeRelation appOfficeRelation : configApps) {
				List<Integer> types = new ArrayList<Integer>();
				types.add(0);
				types.add(1);
				types.add(2);
				for (Integer type : types) {// 0自费1合同2政府统一采购
					for (String product : productType) {
						StatisticCertData data = new StatisticCertData();
						ConfigApp app = appOfficeRelation.getConfigApp();
						data.setApp(app);// 将这个应用信息set到staticCertData表里
						data.setOffice(office);// 网点信息
						data.setCountDate(sdf.parse(sdf.format(new Date())));// 统计时间
						System.out.println(sdf.parse(sdf.format(new Date())));
						System.out.println(yesterDay);
						Integer add1 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(), 1, app.getId(),WorkDealInfoType.TYPE_ADD_CERT, product, type);
						Integer add2 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(), 2, app.getId(),WorkDealInfoType.TYPE_ADD_CERT, product, type);	
						Integer add3 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(), 3, app.getId(),WorkDealInfoType.TYPE_ADD_CERT, product, type);
						Integer add4 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(), 4, app.getId(),WorkDealInfoType.TYPE_ADD_CERT, product, type);
						Integer add5 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(), 5, app.getId(),WorkDealInfoType.TYPE_ADD_CERT, product, type);
						Integer renew1 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(),1, app.getId(),WorkDealInfoType.TYPE_UPDATE_CERT,product, type);
						Integer renew2 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(),2, app.getId(),WorkDealInfoType.TYPE_UPDATE_CERT,product, type);
						Integer renew3 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(),3, app.getId(),WorkDealInfoType.TYPE_UPDATE_CERT,product, type);
						Integer renew4 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(),4, app.getId(),WorkDealInfoType.TYPE_UPDATE_CERT,product, type);
						Integer renew5 = workDealInfoService.getCertAppYearInfo(yesterDay, office.getId(),5, app.getId(),WorkDealInfoType.TYPE_UPDATE_CERT,product, type);
						data.setAdd1(add1);
						data.setAdd2(add2);
						data.setAdd3(add3);
						data.setAdd4(add4);
						data.setAdd5(add5);
						data.setRenew1(renew1);
						data.setRenew2(renew2);
						data.setRenew3(renew3);
						data.setRenew4(renew4);
						data.setRenew5(renew5);
						data.setProductType(Integer.valueOf(product));
						data.setPayType(type);
						statisticCertDataService.save(data);
					}
					// 每一个po存的是 某个应用下某种证书（企业or个人）某种付款方式 新增、更新了多少
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
						yesterDay, office.getId(), 1, null, null, type, null);
				Integer year2 = workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 2, null, null, type, null);
				
				Integer year3 = workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 3, null, null, type, null);
				
				
				Integer year4 = workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 4, null, null, type, null);
				Integer year5 = workDealInfoService.getCertAppYearInfo(
						yesterDay, office.getId(), 5, null, null, type, null);
				product.setCountDate(sdf.parse(sdf.format(new Date())));
				product.setOffice(office);
				product.setProductType(Integer.valueOf(certType));
				product.setYear1(year1);
				product.setYear2(year2);
				
				product.setYear3(year3);
				
				product.setYear4(year4);
				product.setYear5(year5);
				// 某个网点 某种类型证书 发放数量
				statisticCertDataProductService.save(product);
			}
		}
		System.out.println("=======================================");
		System.out.println("==============证书发放量统计结束=============");
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
	public void sendMail() {
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
//					map.put("USER_NAME", receiptDepotInfo.getReceiptCommUser());
					map.put("CONTENT", receiptDepotInfo.getReceiptName()
							+ "该网点发票库存不足请及时申请调拨");
					msgService.sendMail(receiptDepotInfo.getWarningEmail(), "发票库存不足",
							map, 1);
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
		if (idList.size() == 0) {
			System.out.println("无内存不足的仓库，发送邮件结束");
			return;
		}
		List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService
				.findByDepotIds(idList);
		for (int i = 0; i < depots.size(); i++) {
			List<KeyDepotGeneralStatistics> statis = Lists.newArrayList();
			for (int j = 0; j < statistics.size(); j++) {
				if (depots.get(i).getId()
						.equals(statistics.get(j).getKeyUsbKeyDepot().getId())) {
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
//				map.put("USER_NAME", depots.get(i).getLinkmanName());
				map.put("CONTENT", depots.get(i).getDepotName() + "仓库中：'"
						+ content + "'库存不足请及时申请调拨");
				msgService.sendMail(depots.get(i).getWarningEmail(), "key库存不足",
						map, 0);
			}
		}
		System.out.println("发送邮件结束");
	}

	/**
	 * 手动日经营统计 经过日期 &历史日期的统计
	 * 
	 * @param date
	 *            要统计的日期
	 * @throws ParseException
	 */
	public void staticDayCertDataByDate(String date) throws ParseException {
		List<Office> offices = officeService.getOfficeList(2);// 得到所有网点
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date countDate = sdf.parse(date);// 统计日期
		for (Office office : offices) {
			staticDayCertDataByDate(date,office.getId());
		}
	}

	/**
	 * 生成指定日期、网点的日经营统计数据
	 * 
	 * @param date
	 *            要统计的日期
	 * @param officeId
	 *            要统计的网点
	 * @throws ParseException
	 */
	public void staticDayCertDataByDate(String date, Long officeId) {

		log.debug("===日经营统计" + date + ":" + officeId + "\t开始===");
		Date countDate = DateUtils.parseDate(date);// 统计日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, +1); // 得到前一天
		Date nextDay = calendar.getTime();

		List<StatisticDayData> prevDayDatas = statisticDayDataService
				.getPrevDayDatas(countDate, officeId);
		StatisticDayData oldDayData = new StatisticDayData();// 初始化空值
		if(prevDayDatas.size()>0){ //如果之前做过，oldDayData 用数据库中的值赋值
			oldDayData = prevDayDatas.get(0);
		}

		// 需要统计key、发票当天的出库单和入库单
		// 统计业务办理部分证书当天新增、更新、变更、补办成功的数量(查work_deal_info status是已获得证书即可)

		StatisticDayData statisticDayData = new StatisticDayData();

		
		Double receiptIn = 0d; // 发票入库量
		int certTotal = 0; // 证书发放总量
		Double paymentTotal = 0d; // 支付信息总量
		int keyOver = 0; // key出库总量

		// if (yesterDayDatas != null && yesterDayDatas.size() > 0)
		// {//前一日已进行统计，提取统计表数据
		// StatisticDayData data = yesterDayDatas.get(0);
		// keyYest = data.getKeyStoreTotal();
		// receiptYest = data.getReceiptTotal();
		// // yesterDay = data.getCreateDate();
		// }
		//上个统计周期的库存总量，为今天的keyTotal 昨日库存
		statisticDayData.setKeyTotal(oldDayData.getKeyStoreTotal()==null?0:oldDayData.getKeyStoreTotal());

		// 获得统计日期当天Key所有的入库信息:大于等于当天0点00 到 小于第二天0：00
		List<KeyUsbKey> keyUsbkeys = keyUsbKeyService.findByCreateDate(countDate,
				officeId,nextDay);
		int keyIn = 0; // key入库量
		for (KeyUsbKey keyUsbKey : keyUsbkeys) {
				keyIn += keyUsbKey.getCount();				
		}
		statisticDayData.setKeyIn(keyIn);
		

		// 获得统计日期之前Key所有的出库信息
		List<KeyUsbKeyInvoice> keyUsbKeyInvoices = keyUsbKeyInvoiceService
				.findByCreateDate(countDate, officeId,nextDay);
		
		for (KeyUsbKeyInvoice keyUsbKeyInvoice : keyUsbKeyInvoices) {			
				keyOver += keyUsbKeyInvoice.getDeliveryNum();			
		}
		statisticDayData.setKeyStoreTotal(statisticDayData.getKeyTotal()+ keyIn -keyOver);// key余量
		statisticDayData.setKeyOver(keyOver);
		
		// /**
		// * 发票入库
		// */
		// List<ReceiptEnterInfo> receiptEnterInfos = receiptEnterInfoService
		// .findByDate(yesterDay, countDate, office.getId());
		// for (int i = 0; i < receiptEnterInfos.size(); i++) {
		// receiptIn += receiptEnterInfos.get(i).getReceiptMoney();
		// }
		// 获得统计日期之前发票所有的出库信息
		List<ReceiptEnterInfo> receiptEnterInfos = receiptEnterInfoService
				.findByDate(countDate,nextDay, officeId);
		
		for (ReceiptEnterInfo receiptEnterInfo : receiptEnterInfos) {			
				receiptIn += receiptEnterInfo.getNow_Money();
			
		}
		
		List<ReceiptInvoice> receiptInvoices = receiptInvoiceService
				.findByDate(countDate,nextDay, officeId);
		
		Double receiptOut = 0d;
		for (ReceiptInvoice receiptInvoice : receiptInvoices) {			
				receiptOut += receiptInvoice.getReceiptMoney();		
		}
		
		
		statisticDayData.setReceiptOver(receiptOut);
		statisticDayData.setReceiptIn(receiptIn);
		statisticDayData.setReceiptTotal(oldDayData.getReceiptStoreTotal()==null?0d:oldDayData.getReceiptStoreTotal());
		statisticDayData.setReceiptStoreTotal(statisticDayData.getReceiptTotal()+receiptIn - receiptOut);// 余量

		// 证书总量
		certTotal = workDealInfoService.getCertPublishCount(countDate,
				officeId);
		// 金额总量
		paymentTotal = workDealInfoService.getWorkPayMoneyCount(countDate,
				 officeId);
		// key余量
	

		statisticDayData.setCertMoneyTotal(paymentTotal);
		statisticDayData.setCertTotal(certTotal);
		statisticDayData.setCreateDate(new Date());
		
		
		
		
		statisticDayData.setOffice(officeService.get(officeId));
		statisticDayData.setStatisticDate(countDate);
		statisticDayDataService.save(statisticDayData);
		logUtil.saveSysLog("日经营统计", "保存 STATISTIC_DAY_DATA" + countDate + "成功",
				null);
		// ======================日经营汇总表结束========================
		// ======================日经营详情表===========================
		List<ConfigAppOfficeRelation> apps = configAppOfficeRelationService
				.findAllByOfficeId(officeId);
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
			certData.setRenew1(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 1, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setRenew2(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 2, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setRenew4(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 4, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));
			certData.setRenew5(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 5, app.getId(),
					WorkDealInfoType.TYPE_UPDATE_CERT));

			certData.setModifyNum(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			certData.setReissueNum(workDealInfoService.getCertAppYearInfoCount(
					countDate, officeId, 0, app.getId(),
					WorkDealInfoType.TYPE_LOST_CHILD));
			certData.setCertTotal(certData.getAdd1() + certData.getAdd2()
					+ certData.getAdd4() + certData.getRenew1()
					+ certData.getAdd5() + certData.getRenew2()
					+ certData.getRenew4() + certData.getRenew5()
					+ certData.getModifyNum() + certData.getReissueNum());
			certData.setReceiptTotal(workDealInfoService
					.getWorkPayReceiptCount(countDate, officeId,
							app.getId()));
			certData.setKeyTotal(workDealInfoService.getKeyPublishTimesCount(
					countDate, officeId, app.getId()));
			certData.setOffice(officeService.get(officeId));
			certData.setApp(app);
			certData.setStatisticDate(countDate);
			statisticAppDataService.save(certData);
			logUtil.saveSysLog("日经营统计", "保存 STATISTIC_APP_DATA" + countDate
					+ "成功", null);

		}

		log.debug("===日经营统计" + date + ":" + officeId + "\t结束===");
	}

}
