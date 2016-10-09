package com.itrus.ca.modules.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.service.SendMsgService;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptLogService;
import com.itrus.ca.modules.statistic.service.StatisticAppDataService;
import com.itrus.ca.modules.statistic.service.StatisticCertDataProductService;
import com.itrus.ca.modules.statistic.service.StatisticCertDataService;
import com.itrus.ca.modules.statistic.service.StatisticDayDataService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.task.entity.BasicInfoScca;
import com.itrus.ca.modules.task.service.BasicInfoSccaService;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * *************************************************************
 **
 ** @author 张京涛
 **
 ** @date:2014年8月18日 下午1:35:44
 ** 
 ** @describe:8月23日上线准备的方法
 **
 **************************************************************
 */
@Controller
@RequestMapping(value = "/test")
public class ClientController {

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
	@Autowired
	private BasicInfoSccaService basicInfoSccaService;
	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;
	@Autowired
	private UpdateFirstCertSNThread updateFirstCertSNThread;

	private LogUtil logUtil = new LogUtil();

	private Log exLog = LogFactory.getLog("ex");

	static Long MILL = 86400000L;

	Logger log = Logger.getLogger(ClientController.class);

	Boolean isRunning;
	boolean previdRunning;
	int count;

	public ClientController() {
		isRunning = false;
		count = 0;
	}

	/**
	 * 
	 * @param productId
	 * @param officeId
	 * @throws JSONException
	 * @date:2014年8月18日
	 * @user:Zhang Jingtao
	 * @return_type:String
	 */
	@RequestMapping(value = "makeCertDealInfo")
	@ResponseBody
	public String makeAddCertDealInfo(
			Long officeId,
			@RequestParam(defaultValue = "yyyy-MM-dd HH:mm:ss", required = false) String pattern)
			throws JSONException {
		Date startDATE = new Date();
		SimpleDateFormat certTimeFormat = new SimpleDateFormat(pattern);
		JSONObject json = new JSONObject();
		if (isRunning) {
			json.put("statu", "0");
			json.put("msg", "有一个任务进行中，请勿重复操作" + count);
			return json.toString();
		}
		isRunning = true;

		try {
			certTimeFormat.format(new Date());
		} catch (Exception e) {
			json.put("statu", "0");
			json.put("msg", "日期格式错误:" + pattern);
			return json.toString();
		}
		try {
			count = 0;
			json.put("status", -1);
			User createBy = new User(1L);
			// User createBy = UserUtils.getUser();
			if (officeId != 1L) {
				Office office = officeService.get(officeId);
				List<User> users = office.getUserList();
				if (users.size() == 0) {
					json.put("statu", "0");
					json.put("msg", "当前网点下无用户，无法创建");
					return json.toString();
				} else {
					createBy = users.get(0);
				}
			}
			List<BasicInfoScca> all = basicInfoSccaService.findAll();
			// 保存企业信息work_company--以name为准,Long 保存company的Id
			HashMap<String, WorkCompany> companyHash = new HashMap<String, WorkCompany>();
			// 保存经办人信息，以concert_num（经办人证件号）为准
			HashMap<String, WorkUser> userHash = new HashMap<String, WorkUser>();
			// 存储开户费 产品id为key
			HashMap<Long, Double> openAccountHash = new HashMap<Long, Double>();
			// 存储 新增证书费用
			HashMap<Long, Double> addMoneyHash = new HashMap<Long, Double>();
			// 存储 代理商 appId --key
			HashMap<Long, ConfigCommercialAgent> commercialAgentHash = new HashMap<Long, ConfigCommercialAgent>();

			Office office = createBy.getOffice();
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(office);

			String firstSvn = workDealInfoService.getSVN(office.getName());
			// C-四川CA网点-1408-0826

			Integer num = Integer.valueOf(firstSvn.split("-")[3]);
			String head = firstSvn.replace("-" + firstSvn.split("-")[3], "");
			// 批量 存储 basicInfoScca和业务
			List<BasicInfoScca> usedBasicInfo = new ArrayList<BasicInfoScca>();
			List<WorkDealInfo> unSavedDealInfos = new ArrayList<WorkDealInfo>();
			List<WorkPayInfo> payInfos = new ArrayList<WorkPayInfo>();
			List<WorkCertApplyInfo> certApplyInfos = new ArrayList<WorkCertApplyInfo>();
			List<WorkCertInfo> certInfos = new ArrayList<WorkCertInfo>();
			List<WorkCompanyHis> companyHisList = new ArrayList<WorkCompanyHis>();
			List<WorkUserHis> userHisList = new ArrayList<WorkUserHis>();
			List<WorkUser> userList = new ArrayList<WorkUser>();
			List<WorkCompany> companyList = new ArrayList<WorkCompany>();
			SimpleDateFormat dnf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			for (BasicInfoScca s1 : all) {
				log.error("开始创建第" + (count + 1) + "条数据,中间表id：" + s1.getId());
				WorkCompany company = new WorkCompany();
				WorkUser user = new WorkUser();
				WorkCertInfo certInfo = new WorkCertInfo();
				WorkCertApplyInfo certApplyInfo = new WorkCertApplyInfo();
				WorkCompanyHis companyHis = new WorkCompanyHis();
				WorkUserHis userHis = new WorkUserHis();

				String productName = s1.getCertType();
				String appName = s1.getAppName();

				productName = ProductType.productTypeIdMap.get(productName)
						.toString();

				ConfigProduct product = configProductService.findByNamesLabel(
						s1.getProductLabel(), appName, productName);
				if (product == null) {
					log.error(appName + "下产品名称'" + s1.getCertType()
							+ "'对应的产品不存在...跳过，中间表id:" + s1.getId());
					continue;
				}

				if (companyHash.get(s1.getCompanyName()) != null) {// 存在企业信息
					log.error("找到已存在的企业:" + s1.getCompanyName());
					company = companyHash.get(s1.getCompanyName());
					// his信息不再查询。每次自动生成新的his--实际业务中也是这样处理的
					companyHis = change(company);
				} else {
					log.error("未找到企业Id,创建新的:" + s1.getCompanyName());
					company.setCompanyName(s1.getCompanyName());
					company.setCompanyType(String.valueOf(s1.getCompanyType()));
					company.setOrganizationNumber(s1.getOrganizationNumber());

					if (s1.getOrgExpirationTime() != null
							&& !s1.getOrgExpirationTime().equals("")) {
						company.setOrgExpirationTime(new Timestamp(dnf.parse(
								s1.getOrgExpirationTime()).getTime()));
					}

					company.setSelectLv(String.valueOf(s1.getSelectLv()));
					company.setComCertficateNumber(s1.getComCertficateNumber());

					if (s1.getComCertificateTime() != null
							&& !s1.getComCertificateTime().equals("")) {
						company.setComCertficateTime(new Timestamp(dnf.parse(
								s1.getComCertificateTime()).getTime()));
					}

					// company.setComCertficateTime(s1.getComCertificateTime());
					company.setComCertificateType(String.valueOf(s1
							.getComCertificateType()));
					company.setLegalName(s1.getLegalName());
					company.setProvince(s1.getSProvince());
					company.setCity(s1.getSCity());
					company.setDistrict(s1.getSCounty());
					company.setAddress(s1.getAddress());
					company.setCompanyIp(s1.getCompanyMobile());
					if (company.getCompanyType() == null
							|| company.getCompanyType().equals("null")) {
						company.setCompanyType("-1");
					}
					if (company.getSelectLv() == null
							|| company.getSelectLv().equals("null")) {
						company.setSelectLv(null);
					}
					// workCompanyService.save(company);
					companyList.add(company);

					companyHash.put(company.getCompanyName(), company);
					companyHis = change(company);
				}
				log.error("保存company_his信息");
				// workCompanyHisService.save(companyHis);
				companyHisList.add(companyHis);

				String conCerNum = s1.getConCertNumber();// 经办人证件号
				if (userHash.get(conCerNum) != null) {
					user = userHash.get(conCerNum);
					userHis = change(user, companyHis);
				} else {
					log.error("创建新的 workUser ");
					user = new WorkUser();
					user.setContactName(s1.getContactName());
					user.setConCertType(String.valueOf(s1.getConCertType()));
					user.setConCertNumber(s1.getConCertNumber());
					user.setContactEmail(s1.getContactEmail());
					user.setContactPhone(s1.getContactPhone());
					user.setContactTel(s1.getContactTel());
					user.setContactSex(s1.getContactSex());
					user.setWorkCompany(company);
					// workUserService.save(user);
					userList.add(user);
					userHash.put(user.getContactName(), user);
					log.error("创建新的 workUserHis ");
					userHis = change(user, companyHis);
				}
				// workUserHisService.save(userHis);
				userHisList.add(userHis);

				// log.error("保存 workUserHis ");
				certApplyInfo.setName(s1.getName());
				certApplyInfo.setIdCard(s1.getIdCard());
				certApplyInfo.setEmail(s1.getEmail());
				// workCertApplyInfoService.save(certApplyInfo);
				certApplyInfos.add(certApplyInfo);
				certInfo.setIssuerDn(s1.getIssuerDn());
				certInfo.setSubjectDn(s1.getSubjectDn());

				// String notafter = s1.getNotafter();

				certInfo.setNotafter(dnf.parse(s1.getNotafter()));
				certInfo.setNotbefore(dnf.parse(s1.getNotbefore()));
				certInfo.setKeySn("");// 这次不需要keySn即使有也不记录
				certInfo.setTrustDeviceCount(1);// 默认送1个
				certInfo.setTrustDeviceDate(dnf.parse(s1.getNotafter()));// 可信设备时间
				String serNum = s1.getSerialnumber();
				if (serNum != null) {
					serNum = serNum.trim();
				}
				certInfo.setSerialnumber(serNum);

				certInfo.setWorkCertApplyInfo(certApplyInfo);
				// workCertInfoService.save(certInfo);
				certInfos.add(certInfo);

				log.error("证书信息、企业信息、个人信息生成完毕，开始生成业务信息...");
				try {
					log.error("证书:" + certInfo.getSerialnumber());
					Date start = certInfo.getNotbefore();
					Date end = certInfo.getNotafter();
					// Integer year = (int) ((s1.get证书天数()) / 365);// 新增业务的年限
					Integer year = s1.getYear();

					log.error("业务年限：" + year);
					WorkDealInfo workDealInfo = new WorkDealInfo();
					WorkPayInfo workPayInfo = new WorkPayInfo();

					ConfigApp app = product.getConfigApp();
					workDealInfo.setSvn(getSvn(head, num));
					num++;
					log.error("业务编号:" + workDealInfo.getSvn());

					if (commercialAgentHash.get(app.getId()) == null) {
						ConfigCommercialAgent commercialAgent = configAgentAppRelationService
								.findAgentByApp(app);
						workDealInfo.setConfigCommercialAgent(commercialAgent);
					} else {
						workDealInfo
								.setConfigCommercialAgent(commercialAgentHash
										.get(app.getId()));
					}

					if (configAgentOfficeRelations.size() > 0) {
						workDealInfo
								.setCommercialAgent(configAgentOfficeRelations
										.get(0).getConfigCommercialAgent());// 劳务关系外键
					} else {
						log.error("无劳务关系代理商，未设置劳务关系...");
					}
					workDealInfo.setConfigApp(app);
					workDealInfo.setWorkUser(user);
					workDealInfo.setWorkCompany(company);
					workDealInfo.setConfigProduct(product);
					workDealInfo
							.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
					workDealInfo.setYear(year);

					if (year * 365 > s1.getCertValidDays()) {

						Integer certDayNum = s1.getCertValidDays() - year * 365;
						workDealInfo.setAddCertDays(certDayNum);
					} else if (year * 365 == s1.getCertValidDays()) {
						workDealInfo.setAddCertDays(0);
					} else {
						Integer certDayNum = s1.getCertValidDays() - year * 365;
						workDealInfo.setAddCertDays(certDayNum);
					}

					workDealInfo
							.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
					workDealInfo.setCreateBy(createBy);
					workDealInfo.setCreateDate(new Date());
					workDealInfo.setCertSn(certInfo.getSerialnumber());

					workDealInfo.setWorkCompanyHis(companyHis);
					workDealInfo.setWorkUserHis(userHis);
					workDealInfo.setUpdateBy(createBy);
					workDealInfo.setCreateDate(new Date());
					workDealInfo.setWorkCertInfo(certInfo);
					workDealInfo.setObtainedDate(new Date());
					workDealInfo.setStatus(0);

					workDealInfo.setNotafter(dnf.parse(s1.getNotafter()));

					workDealInfo.setKeySn(s1.getKeyAndUsbSn());
					Double openAccountMoney = 0d;
					Double addCert = 0d;
					if (openAccountHash.get(product.getId()) == null) {
						// ConfigChargeAgent chargeAgent =
						// configChargeAgentService
						// .get(workDealInfo.getConfigProduct().getChargeAgentId());
						ConfigChargeAgent chargeAgent = configChargeAgentService
								.get(s1.getAgentId());

						openAccountMoney = configChargeAgentDetailService
								.selectMoney(chargeAgent,
										WorkDealInfoType.TYPE_OPEN_ACCOUNT,
										null, workDealInfo.getConfigProduct()
												.getProductLabel());

						workPayInfo.setOpenAccountMoney(openAccountMoney);
						addCert = configChargeAgentDetailService.selectMoney(
								chargeAgent, WorkType.TYPE_ADD, workDealInfo
										.getYear(), workDealInfo
										.getConfigProduct().getProductLabel());
						workPayInfo.setAddCert(addCert);
						if (openAccountMoney == null) {
							openAccountMoney = 0d;
						}
						if (addCert == null) {
							addCert = 0d;
						}
						openAccountHash.put(product.getId(), openAccountMoney);
						addMoneyHash.put(product.getId(), addCert);
					} else {
						log.error("从map中得到费用信息 ");
						openAccountMoney = openAccountHash.get(product.getId());
						addCert = addMoneyHash.get(product.getId());
						workPayInfo.setOpenAccountMoney(openAccountMoney);
						workPayInfo.setAddCert(addCert);
					}

					workPayInfo.setMethodGov(true);
					log.error("开户费:" + openAccountMoney);
					log.error("新增证书费用:" + addCert);
					// 证书序列号
					workDealInfo.setCertSort(s1.getMultiCertSns());

					workPayInfo.setWorkTotalMoney(openAccountMoney + addCert);
					workPayInfo.setWorkPayedMoney(0d);
					workPayInfo.setWorkReceivaMoney(0d);
					workPayInfo.setUserReceipt(false);
					workPayInfo.setReceiptAmount(0d);
					workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
					workPayInfo.setCreateBy(createBy);
					workPayInfo.setUpdateBy(createBy);
					log.error("付款流水:" + workPayInfo.getSn());
					payInfos.add(workPayInfo);
					workDealInfo.setWorkPayInfo(workPayInfo);
					workDealInfo
							.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
					workDealInfo.setCanSettle(false);
					count++;
					s1.setUsed(true);

					usedBasicInfo.add(s1);
					unSavedDealInfos.add(workDealInfo);

					if (companyHisList.size() > 3000) {
						log.error("保存 企业历史 数据+实时数据");
						workCompanyService.save(companyList);
						companyList = new ArrayList<WorkCompany>();
						workCompanyHisService.save(companyHisList);
						companyHisList = new ArrayList<WorkCompanyHis>();
					}

					if (userHisList.size() > 3000) {
						log.error("保存 用户历史 数据+实时数据 ");
						workUserService.save(userList);
						userList = new ArrayList<WorkUser>();
						workUserHisService.save(userHisList);
						userHisList = new ArrayList<WorkUserHis>();
					}

					if (certApplyInfos.size() > 3000) {
						log.info("保存申请人信息 ");
						workCertApplyInfoService.save(certApplyInfos);
						certApplyInfos = new ArrayList<WorkCertApplyInfo>();
					}

					if (certInfos.size() > 3000) {
						log.info("保存证书信息");
						workCertInfoService.save(certInfos);
						certInfos = new ArrayList<WorkCertInfo>();
					}

					if (payInfos.size() > 3000) {
						log.info("批量保存支付信息");
						workPayInfoService.save(payInfos);
						payInfos = new ArrayList<WorkPayInfo>();
					}

					if (unSavedDealInfos.size() > 3000) {
						workDealInfoService.save(unSavedDealInfos);
						unSavedDealInfos = new ArrayList<WorkDealInfo>();
						log.info("批量存储workDealinfo");
					}
					if (usedBasicInfo.size() > 3000) {
						basicInfoSccaService.saveList(usedBasicInfo);
						usedBasicInfo = new ArrayList<BasicInfoScca>();
					}

					log.error("新增业务数据生成完毕...当前第" + count + "条...");
					log.error("------------------------------------------------");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (companyHisList.size() != 0) {
				log.error("最后一次保存 企业历史 数据+实时数据 ");
				workCompanyService.save(companyList);
				companyList = new ArrayList<WorkCompany>();
				workCompanyHisService.save(companyHisList);
				companyHisList = new ArrayList<WorkCompanyHis>();
			}

			if (userHisList.size() != 0) {
				log.error("最后一次保存 用户历史 数据+实时数据");
				workUserService.save(userList);
				userList = new ArrayList<WorkUser>();
				workUserHisService.save(userHisList);
				userHisList = new ArrayList<WorkUserHis>();
			}

			if (certApplyInfos.size() != 0) {
				log.info("最后一次保存申请人信息 ");
				workCertApplyInfoService.save(certApplyInfos);
				certApplyInfos = new ArrayList<WorkCertApplyInfo>();
			}

			if (certInfos.size() != 0) {
				log.info("最后一次保存证书信息");
				workCertInfoService.save(certInfos);
				certInfos = new ArrayList<WorkCertInfo>();
			}

			if (payInfos.size() != 0) {
				log.info("最后一次批量保存支付信息");
				workPayInfoService.save(payInfos);
				payInfos = new ArrayList<WorkPayInfo>();
			}

			if (unSavedDealInfos.size() != 0) {
				workDealInfoService.save(unSavedDealInfos);
			}
			if (usedBasicInfo.size() != 0) {
				basicInfoSccaService.saveList(usedBasicInfo);
			}

			basicInfoSccaService.saveList(usedBasicInfo);
			workDealInfoService.save(unSavedDealInfos);

			json.put("status", 1);
			json.put("msg", "操作成功,生成：" + count);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", e.getMessage());
		}
		isRunning = false;
		System.out.println("使用时间 :"
				+ (System.currentTimeMillis() - startDATE.getTime()));
		return json.toString();
	}

	/**
	 * 
	 * @date:2014年8月18日
	 * @user:京涛
	 * @return_type:WorkUserHis
	 */
	public WorkUserHis change(WorkUser workUser, WorkCompanyHis workCompanyHis) {
		WorkUserHis workUserHis = new WorkUserHis();
		workUserHis.setAddress(workUser.getAddress());
		workUserHis.setConCertNumber(workUser.getConCertNumber());
		workUserHis.setConCertType(workUser.getConCertType());
		workUserHis.setContactEmail(workUser.getContactEmail());
		workUserHis.setContactName(workUser.getContactName());
		workUserHis.setContactPhone(workUser.getContactPhone());
		workUserHis.setContactSex(workUser.getContactSex());
		workUserHis.setContactTel(workUser.getContactTel());
		workUserHis.setDepartment(workUser.getDepartment());
		workUserHis.setSource(workUser.getSource());
		workUserHis.setStatus(workUser.getStatus());
		workUserHis.setUserSn(workUser.getUserSn());
		workUserHis.setWorkCompanyHis(workCompanyHis);
		workUserHis.setWorkType(workUser.getWorkType());
		return workUserHis;
	}

	public WorkCompanyHis change(WorkCompany workCompany) {
		WorkCompanyHis workCompanyHis = new WorkCompanyHis();
		workCompanyHis.setActualCapital(workCompany.getActualCapital());
		workCompanyHis.setAddress(workCompany.getAddress());
		workCompanyHis.setAgeDisNumber(workCompany.getAgeDisNumber());
		workCompanyHis.setAnnualInspection(workCompany.getAnnualInspection());
		workCompanyHis.setBusinessNumber(workCompany.getBusinessNumber());
		workCompanyHis.setCity(workCompany.getCity());
		workCompanyHis.setComCertficateNumber(workCompany
				.getComCertficateNumber());
		workCompanyHis.setComCertficateTime(workCompany.getComCertficateTime());
		workCompanyHis.setComCertificateType(workCompany
				.getComCertificateType());
		workCompanyHis.setComEnglishName(workCompany.getComEnglishName());
		workCompanyHis.setCompanyIp(workCompany.getCompanyIp());
		workCompanyHis.setCompanyMobile(workCompany.getCompanyMobile());
		workCompanyHis.setCompanyName(workCompany.getCompanyName());
		workCompanyHis.setCompanyType(workCompany.getCompanyType());
		workCompanyHis.setCompanyWeb(workCompany.getCompanyWeb());
		workCompanyHis.setComPhone(workCompany.getComPhone());
		workCompanyHis.setDistrict(workCompany.getDistrict());
		workCompanyHis.setEnterpriseType(workCompany.getEnterpriseType());
		workCompanyHis.setIndustry(workCompany.getIndustry());
		workCompanyHis.setLegalName(workCompany.getLegalName());
		workCompanyHis.setMarketingScope(workCompany.getMarketingScope());
		workCompanyHis.setOrganizationNumber(workCompany
				.getOrganizationNumber());
		workCompanyHis.setOrgExpirationTime(workCompany.getOrgExpirationTime());
		workCompanyHis.setProvince(workCompany.getProvince());
		workCompanyHis.setRegisteredCapital(workCompany.getRegisteredCapital());
		workCompanyHis.setRemarks(workCompany.getRemarks());
		workCompanyHis.setSelectLv(workCompany.getSelectLv());
		workCompanyHis.setTcpNumber(workCompany.getTcpNumber());
		workCompanyHis.setZipCode(workCompany.getZipCode());
		return workCompanyHis;
	}

	public static String getSvn(String head, Integer num) {
		String numStr = "00000" + num;
		return head + "-"
				+ numStr.substring(numStr.length() - 6, numStr.length());
	}

	/**
	 * 
	 * @param year
	 * @param autoPass
	 * @return
	 * @throws JSONException
	 * @date:2014年8月18日
	 * @user:Zhang Jingtao
	 * @return_type:String
	 */
	@RequestMapping(value = "autoWriteUpdate")
	@ResponseBody
	public String autoWriteUpdateDealInfo() throws JSONException {
		Date startDATE = new Date();
		JSONObject json = new JSONObject();
		if (isRunning) {
			json.put("statu", "0");
			json.put("msg", "有一个任务进行中，请勿重复操作:" + count);
			return json.toString();
		}
		isRunning = true;
		count = 0;
		try {
			json.put("status", -1);
			List<WorkDealInfo> dealInfos = workDealInfoService
					.getCurValidAddDealInfo();
			String status = WorkDealInfoStatus.STATUS_UPDATE_USER;
			log.debug("共获取到:" + dealInfos.size() + "笔可生成更新服务的证书");

			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(UserUtils.getUser().getOffice());
			User createBy = UserUtils.getUser();
			Office curOffice = createBy.getOffice();

			List<WorkDealInfo> unSaveDealInfos = new ArrayList<WorkDealInfo>();
			List<WorkDealInfo> unDeleteDealInfos = new ArrayList<WorkDealInfo>();
			List<WorkCertInfo> unSaveCertInfos = new ArrayList<WorkCertInfo>();
			List<WorkCertInfo> unSaveOldCertInfos = new ArrayList<WorkCertInfo>();
			List<WorkLog> unSaveWorkLogs = new ArrayList<WorkLog>();
			String first = workDealInfoService.getSVN(1);
			Integer num = Integer.valueOf(first.split("-")[3]);
			String head = first.replace("-" + first.split("-")[3], "");

			for (WorkDealInfo oldDealInfo : dealInfos) {
				log.debug("开始第" + (count + 1) + "笔");
				// 新建业务信息
				WorkDealInfo workDealInfo = new WorkDealInfo();
				workDealInfo.setKeySn(oldDealInfo.getKeySn());
				workDealInfo.setConfigApp(oldDealInfo.getConfigApp());
				ConfigCommercialAgent commercialAgent = configAgentAppRelationService
						.findAgentByApp(workDealInfo.getConfigApp());
				workDealInfo.setConfigCommercialAgent(commercialAgent);

				if (configAgentOfficeRelations.size() > 0) {
					workDealInfo.setCommercialAgent(configAgentOfficeRelations
							.get(0).getConfigCommercialAgent());// 劳务关系外键
				} else {
					log.error("无劳务关系,ID:" + oldDealInfo.getId());
				}
				workDealInfo.setWorkUser(oldDealInfo.getWorkUser());
				workDealInfo.setWorkCompany(oldDealInfo.getWorkCompany());
				workDealInfo.setWorkCompanyHis(oldDealInfo.getWorkCompanyHis());
				workDealInfo.setWorkUserHis(oldDealInfo.getWorkUserHis());
				workDealInfo.setConfigProduct(oldDealInfo.getConfigProduct());
				workDealInfo.setCreateBy(new User(1L));
				workDealInfo.setDealInfoStatus(status);
				workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
				workDealInfo.setCreateBy(createBy);
				workDealInfo.setCreateDate(new Date());
				workDealInfo.setClassifying(oldDealInfo.getClassifying());
				workDealInfo.setSvn(getSvn("O-客户端 ", num));
				num++;
				workDealInfo.setPrevId(oldDealInfo.getId());
				if (oldDealInfo.getWorkCertInfo().getNotafter()
						.after(new Date())) {
					int day = getLastCertDay(oldDealInfo.getWorkCertInfo()
							.getNotafter());
					workDealInfo.setLastDays(day);
					log.error("原有证书剩余天数:" + day);
				} else {
					workDealInfo.setLastDays(0);
					log.error("原有证书剩余天数:" + 0);
				}

				WorkCertInfo oldCertInfo = oldDealInfo.getWorkCertInfo();
				WorkCertApplyInfo workCertApplyInfo = oldDealInfo
						.getWorkCertInfo().getWorkCertApplyInfo();
				WorkCertInfo workCertInfo = new WorkCertInfo();
				workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
				workCertInfo.setRenewalPrevId(oldCertInfo.getId());
				workCertInfo.setCreateDate(workCertInfoService
						.getCreateDate(oldCertInfo.getId()));
				// workCertInfoService.save(workCertInfo);
				unSaveCertInfos.add(workCertInfo);

				// 给上张证书存nextId
				// oldCertInfo.setRenewalNextId(workCertInfo.getId());
				// workCertInfoService.save(oldCertInfo);
				unSaveOldCertInfos.add(oldCertInfo);
				workDealInfo.setWorkCertInfo(workCertInfo);
				oldDealInfo.setDelFlag(WorkDealInfo.DEL_FLAG_DELETE);
				// workDealInfoService.delete(oldDealInfo.getId());
				oldDealInfo.setUpdateBy(createBy);
				unDeleteDealInfos.add(oldDealInfo);

				// workDealInfoService.save(workDealInfo);
				// 保存日志信息
				WorkLog workLog = new WorkLog();
				workLog.setRecordContent("自动创建成功");
				workLog.setWorkDealInfo(workDealInfo);
				workLog.setCreateDate(new Date());
				workLog.setCreateBy(createBy);
				workLog.setUpdateBy(createBy);
				workLog.setConfigApp(workDealInfo.getConfigApp());
				workLog.setWorkCompany(workDealInfo.getWorkCompany());
				workLog.setOffice(curOffice);
				unSaveWorkLogs.add(workLog);
				// workLogService.save(workLog);

				// logUtil.saveSysLog("业务中心", "业务更新：编号" + workDealInfo.getId()
				// + "单位名称："
				// + workDealInfo.getWorkCompany().getCompanyName(), "");

				workDealInfo.setCanSettle(false);
				workDealInfo.setCreateBy(createBy);
				workDealInfo.setUpdateBy(createBy);
				// workDealInfoService.save(workDealInfo);
				unSaveDealInfos.add(workDealInfo);

				// List<WorkDealInfo> unSaveDealInfos = new
				// ArrayList<WorkDealInfo>();
				// List<WorkDealInfo> unDeleteDealInfos = new
				// ArrayList<WorkDealInfo>();
				// List<WorkCertInfo> unSaveCertInfos = new
				// ArrayList<WorkCertInfo>();
				// List<WorkCertInfo> unSaveOldCertInfos = new
				// ArrayList<WorkCertInfo>();
				// List<WorkLog> unSaveWorkLogs = new ArrayList<WorkLog>();

				if (unSaveDealInfos.size() > 10000) {
					log.info("批量保存信息...");
					workCertInfoService.save(unSaveCertInfos);
					for (int i = 0; i < unSaveCertInfos.size(); i++) {
						unSaveOldCertInfos.get(i).setRenewalNextId(
								unSaveCertInfos.get(i).getId());
					}
					workCertInfoService.save(unSaveOldCertInfos);
					workDealInfoService.save(unSaveDealInfos);
					workDealInfoService.save(unDeleteDealInfos);
					workLogService.save(unSaveWorkLogs);

					unSaveDealInfos = new ArrayList<WorkDealInfo>();
					unDeleteDealInfos = new ArrayList<WorkDealInfo>();
					unSaveCertInfos = new ArrayList<WorkCertInfo>();
					unSaveOldCertInfos = new ArrayList<WorkCertInfo>();
					unSaveWorkLogs = new ArrayList<WorkLog>();
					log.info("批量 保存完毕");
				}
				log.error("创建更新业务完成");
				log.error("-------------------------------------------");
				count++;
			}

			if (unSaveDealInfos.size() != 0) {
				log.info("最后一次 批量保存信息");
				workCertInfoService.save(unSaveCertInfos);
				for (int i = 0; i < unSaveCertInfos.size(); i++) {
					unSaveOldCertInfos.get(i).setRenewalNextId(
							unSaveCertInfos.get(i).getId());
				}
				workCertInfoService.save(unSaveOldCertInfos);
				workDealInfoService.save(unSaveDealInfos);
				workDealInfoService.save(unDeleteDealInfos);
				workLogService.save(unSaveWorkLogs);
			}

			json.put("status", 1);
			json.put("msg", "操作成功,生成更新数据:" + count);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", e.getMessage());
		}

		isRunning = false;
		log.debug("使用时间 :" + (System.currentTimeMillis() - startDATE.getTime()));
		return json.toString();
	}

	private int getLastCertDay(Date notAfter) {
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong < nowLong) {
			return 0;
		}
		long d = (notAfterLong - nowLong) / MILL;
		long hour1 = (notAfterLong - nowLong) % MILL;
		if (hour1 > 0) {
			d += 1;
		}
		return (int) d;
	}

	public static void main(String[] args) {
		System.out.println(getSvn("O-四川", 11));
		List<Thread> allThread = new ArrayList<Thread>();
		List<Integer> in = new ArrayList<Integer>();
		in.add(1);
		in.add(2);
		for (int i = 0; i < 10; i++) {
			MutiProcess mp = new MutiProcess();
			Thread thread = new Thread(mp);
			thread.start();
			allThread.add(thread);
		}
		for (Thread thread1 : allThread) {
			try {
				thread1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("555555555555");
	}

	@RequestMapping(value = "updateFirstCertSN")
	@ResponseBody
	public String updateFirstCertSN(Integer updateCount, Integer appid)
			throws JSONException {
		JSONObject json = new JSONObject();
		// 修复所有现有数据里，没有firstCertSn字段的记录
		// workDealInfoService.fixAllDataFirstCertSN(updateCount);

		List<Long> lst = workDealInfoService.findNullFirstCertSNByCount(
				updateCount, appid == null ? appid : 0);
		updateFirstCertSNThread.process(lst);
		json.put("statu", "0");
		json.put("msg", "完成,待处理条数:" + lst.size());

		return json.toString();
	}

	@RequestMapping(value = "fixSVN")
	@ResponseBody
	public String fixSVN(Long svnAppid, Integer svnCount) throws JSONException {
		JSONObject json = new JSONObject();
		if (svnAppid == null || svnAppid.intValue() <= 0) {
			json.put("statu", "-1");
			json.put("msg", "APPID不能为空");
			return json.toString();
		}
		new Thread(new FixSVNThread(svnAppid, svnCount)).start();

		log.error("异步提交处理完毕,app_id:");

		json.put("statu", "0");
		json.put("msg", "请求提交完成");
		return json.toString();
	}

	@RequestMapping(value = "fixFirstCertSN")
	@ResponseBody
	public String fixFirstCertSN(String fixFirstCertSnAppid)
			throws JSONException {
		JSONObject json = new JSONObject();
		if (StringHelper.isNull(fixFirstCertSnAppid)) {
			json.put("statu", "-1");
			json.put("msg", "APPID不能为空");
			return json.toString();
		}
		// 第一种情况
		List<String> lst = workDealInfoService
				.getNeedFixFirstCertSNLst(fixFirstCertSnAppid);
		new Thread(new FixFirstCertSNThread(lst)).start();
		// 第二种情况
		List<String> lst2 = workDealInfoService
				.getNeedFixFirstCertSNLst2(fixFirstCertSnAppid);

		new Thread(new FixFirstSNThread2(lst2)).start();

		Integer c1 = lst == null ? 0 : lst.size();
		Integer c2 = lst2 == null ? 0 : lst2.size();
		json.put("statu", "0");
		json.put("msg", "开始处理,app_id:" + fixFirstCertSnAppid + "数据总数:"
				+ (c1 + c2));
		return json.toString();
	}

	@RequestMapping(value = "fixPreId")
	@ResponseBody
	public String fixPreId(Integer prevIdCount, Integer prevIdAppid,
			String prevFirstCertSN) throws JSONException {
		JSONObject json = new JSONObject();
		if (prevIdAppid == null || prevIdAppid.intValue() <= 0) {
			json.put("statu", "-1");
			json.put("msg", "APPID不能为空");
			return json.toString();
		}
		if (previdRunning || isRunning) {
			json.put("statu", "-2");
			json.put("msg", "另一个修复prev_id任务或导入任务正在进行");
			return json.toString();
		}

		previdRunning = true;

		// 业务数据的prev_id置空
		if (prevIdCount != null && prevIdCount.intValue() == 0) {
			try {
				workDealInfoService.setPrevIdToNull(prevIdAppid);
			} catch (Exception e) {
			}
		}

		// 按需要的查出对应数据
		Integer isNullCount = workDealInfoService.findPrevIdIsNull(prevIdAppid);
		Integer needFixCount = workDealInfoService
				.getNeedFixPrevIdCount(prevIdAppid.toString());
		Integer c = isNullCount + needFixCount;
		List<String> lst = null;

		if (StringHelper.isNull(prevFirstCertSN)) {
			lst = workDealInfoService.findPreIdIsNullFirstCertSnByAppId(
					prevIdAppid, prevIdCount);
		} else {
			lst = new ArrayList<String>();
			String[] flst = StringHelper.splitStr(prevFirstCertSN, ",");
			for (String e : flst) {
				lst.add(e);
			}
		}

		List<String> needFixLst = workDealInfoService.getNeedFixPrevId(
				prevIdAppid.toString(), prevIdCount);
		if (needFixLst != null && needFixLst.size() > 0) {
			lst.addAll(needFixLst);
		}

		log.error("开始处理,app_id:" + prevIdAppid + ",prev_id为空记录:" + c
				+ "条,本次需要处理总数:" + lst.size() + "条");
		if (lst != null && lst.size() > 0) {
			try {
				new Thread(new ModifyPreidThread(lst)).start();
			} catch (Exception e) {
				e.printStackTrace();
				exLog.error(StringHelper.getStackInfo(e));
				json.put("msg", "更新preid出现异常:" + StringHelper.getStackInfo(e));
				isRunning = false;
				return json.toString();
			}
		}

		json.put("statu", "0");
		json.put("msg", "请求提交完成,更新:" + lst.size()
				+ "条firstCertSN数据，后台异步处理完后会自动停止,处理前总数据量为:" + c + ",还余"
				+ (c - lst.size()) + "条firstCertSN数据未处理");

		previdRunning = false;

		log.error("异步提交处理完毕,app_id:");
		return json.toString();
	}

	@RequestMapping(value = "fixFirstDealInfoType")
	@ResponseBody
	public String fixFirstDealInfoType(Integer fixAppId) throws JSONException {
		JSONObject json = new JSONObject();
		if (fixAppId == null || fixAppId.intValue() <= 0) {
			json.put("statu", "-1");
			json.put("msg", "APPID不能为空");
			return json.toString();
		}
		List<String> lst = workDealInfoService.findFirstCertSnByAppId(fixAppId,
				0);
		new Thread(new FixFirstDealInfoTypeThread(lst)).start();
		json.put("statu", "0");
		json.put("msg", "请求提交完成,更新:" + lst.size()
				+ "条firstCertSN数据，后台异步处理完后会自动停止,处理前总数据量为:" + lst.size());
		log.error("异步提交处理完毕,app_id:" + fixAppId);
		return json.toString();
	}

	/**
	 * 
	 * @param productId
	 * @param officeId
	 * @throws JSONException
	 * @date:2014年8月18日
	 * @user:Zhang Jingtao
	 * @return_type:String
	 */
	@RequestMapping(value = "importNewDealInfo")
	@ResponseBody
	public String importNewDealInfo(
			Long officeId,
			@RequestParam(defaultValue = "yyyy-MM-dd HH:mm:ss", required = false) String pattern)
			throws JSONException {
		JSONObject json = new JSONObject();
		Date startDATE = new Date();

		Long dealInfoId = workDealInfoService.findDealInfoMax();

		SimpleDateFormat certTimeFormat = new SimpleDateFormat(pattern);
		if (isRunning) {
			json.put("statu", "0");
			json.put("msg", "有一个任务进行中，请勿重复操作!");
			return json.toString();
		}
		isRunning = true;

		try {
			certTimeFormat.format(new Date());
		} catch (Exception e) {
			json.put("statu", "0");
			json.put("msg", "日期格式错误:" + pattern);
			isRunning = false;
			return json.toString();
		}
		count = 0;
		json.put("status", -1);

		User createBy = new User(1L);
		// User createBy = UserUtils.getUser();
		String firstSvn = "";
		if (officeId != 1L) {
			Office office = officeService.get(officeId);
			List<User> users = office.getUserList();
			if (users.size() == 0) {
				json.put("statu", "0");
				json.put("msg", "当前网点下无用户，无法创建");
				isRunning = false;
				return json.toString();
			} else {
				createBy = users.get(0);
			}
			firstSvn = workDealInfoService.getSVN(office.getName());
		}
		List<BasicInfoScca> all = basicInfoSccaService.findAll();
		if (all.size() == 0) {
			json.put("statu", "0");
			json.put("msg", "要导入的数据为空，请检查");
			isRunning = false;
			return json.toString();
		}
		Integer num = Integer.valueOf(firstSvn.split("-")[3]);
		String head = firstSvn.replace("-" + firstSvn.split("-")[3], "");
		// 最后处理preId用，暂存firstCertSN，排重
		List<String> firstSnAll = new ArrayList<String>();
		for (int i = 0; i < all.size(); i++) {
			String svn = getSvn(head, num);
			num++;
			all.get(i).setSvnNum(svn);

			// 最后处理preId用
			String firstCertSn = all.get(i).getFirstCertSN();
			if (!StringHelper.isNull(firstCertSn))
				firstSnAll.add(firstCertSn.trim());
		}

		List<Thread> allThread = new ArrayList<Thread>();

		int loopTime = 100;
		int tempSize = all.size();
		if (tempSize <= loopTime) {
			List<BasicInfoScca> transToTrheads = new ArrayList<BasicInfoScca>();
			for (int i = 0; i < tempSize; i++) {
				transToTrheads.add(all.get(i));
			}
			MutiProcess mp = new MutiProcess(transToTrheads, officeId,
					createBy, 1);
			Thread thread = new Thread(mp);
			thread.start();
			allThread.add(thread);
		} else {
			int divisor = tempSize / loopTime;
			int remainder = tempSize % loopTime;

			for (int i = 0; i < loopTime; i++) {

				int newSize = divisor;
				if (i < remainder) {
					newSize = divisor + 1;
				}
				List<BasicInfoScca> transToTrheads = new ArrayList<BasicInfoScca>();
				for (int j = newSize - 1; j >= 0; j--) {
					transToTrheads.add(all.get(j));
					all.remove(j);
				}
				MutiProcess mp = new MutiProcess(transToTrheads, officeId,
						createBy, (i + 1));
				Thread thread = new Thread(mp);
				thread.start();
				allThread.add(thread);

			}

		}

		for (Thread thread1 : allThread) {
			try {
				thread1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Integer dealInfoCount = workDealInfoService.afterDealInfoId(dealInfoId);

		// 业务链中，只有最后一条是0，前面的都是1
		try {
			new Thread(new ModifyPreidThread(firstSnAll)).start();
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "更新preid出现异常:" + StringHelper.getStackInfo(e));
			isRunning = false;
			return json.toString();
		}
		json.put("msg", "本次成功提交数据：" + dealInfoCount + "条！");

		System.out.println("使用时间 :"
				+ (System.currentTimeMillis() - startDATE.getTime()));
		isRunning = false;
		return json.toString();
	}

}
