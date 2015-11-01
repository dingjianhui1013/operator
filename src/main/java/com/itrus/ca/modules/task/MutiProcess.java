package com.itrus.ca.modules.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.task.entity.BasicInfoScca;
import com.itrus.ca.modules.task.service.BasicInfoSccaService;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

public class MutiProcess implements Runnable {

	WorkDealInfoService workDealInfoService = SpringContextHolder.getBean(WorkDealInfoService.class);
	ConfigAgentOfficeRelationService configAgentOfficeRelationService = SpringContextHolder.getBean(ConfigAgentOfficeRelationService.class);
	ConfigProductService configProductService = SpringContextHolder.getBean(ConfigProductService.class);
	ConfigAgentAppRelationService configAgentAppRelationService = SpringContextHolder.getBean(ConfigAgentAppRelationService.class);
	WorkCompanyService workCompanyService = SpringContextHolder.getBean(WorkCompanyService.class);
	WorkCompanyHisService workCompanyHisService = SpringContextHolder.getBean(WorkCompanyHisService.class);
	WorkUserService workUserService = SpringContextHolder.getBean(WorkUserService.class);
	
	WorkUserHisService workUserHisService = SpringContextHolder.getBean(WorkUserHisService.class);
	WorkCertApplyInfoService workCertApplyInfoService = SpringContextHolder.getBean(WorkCertApplyInfoService.class);
	WorkCertInfoService workCertInfoService = SpringContextHolder.getBean(WorkCertInfoService.class);
	WorkPayInfoService workPayInfoService = SpringContextHolder.getBean(WorkPayInfoService.class);
	BasicInfoSccaService basicInfoSccaService = SpringContextHolder.getBean(BasicInfoSccaService.class);
	
	ConfigChargeAgentService configChargeAgentService = SpringContextHolder.getBean(ConfigChargeAgentService.class);
	ConfigChargeAgentDetailService configChargeAgentDetailService = SpringContextHolder.getBean(ConfigChargeAgentDetailService.class);
	
	
	Logger log = Logger.getLogger(MutiProcess.class);
	private List<BasicInfoScca> sccaList;
	private Long officeId;
	private User createBy;
	private Integer number;
	
	public MutiProcess(){
	}
	
//	public MutiProcess(List<BasicInfoScca> sccaList){
//		this.sccaList = sccaList;
//	}
	
	public MutiProcess(List<BasicInfoScca> all , Long officeId , User createBy , Integer number){
		this.sccaList = all ;
		this.officeId = officeId;
		this.createBy = createBy;
		this.number = number;
	}
	
	
	@Override
	@Transactional
	public void run() {
		sccaList.size();
		System.out.println(sccaList.size());
		
		try {
			HashMap<String, WorkCompany> companyHash = new HashMap<String, WorkCompany>();
			// 保存经办人信息，以concert_num（经办人证件号）为准
			HashMap<String, WorkUser> userHash = new HashMap<String, WorkUser>();
			//存储开户费  产品id为key
			HashMap<Long, Double> openAccountHash = new HashMap<Long, Double>();
			//存储 新增证书费用
			HashMap<Long, Double> addMoneyHash = new HashMap<Long, Double>();
			//存储 代理商  appId --key
			HashMap<Long, ConfigCommercialAgent> commercialAgentHash = new HashMap<Long, ConfigCommercialAgent>();
			
			Office  office  = createBy.getOffice();
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(office);
			
			//批量  存储  basicInfoScca和业务
			List<BasicInfoScca> usedBasicInfo = new  ArrayList<BasicInfoScca>();
			List<WorkDealInfo> unSavedDealInfos = new ArrayList<WorkDealInfo>();
			List<WorkPayInfo>  payInfos =  new ArrayList<WorkPayInfo>();
			List<WorkCertApplyInfo> certApplyInfos = new  ArrayList<WorkCertApplyInfo>();
			List<WorkCertInfo>  certInfos = new  ArrayList<WorkCertInfo>();
			List<WorkCompanyHis>  companyHisList = new ArrayList<WorkCompanyHis>();
			List<WorkUserHis> userHisList = new ArrayList<WorkUserHis>();
			List<WorkUser> userList = new ArrayList<WorkUser>();
			List<WorkCompany>  companyList = new  ArrayList<WorkCompany>();
			SimpleDateFormat dnf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			Integer sjNum =1;
			for (BasicInfoScca s1 : sccaList) {
				log.debug("开始创建第"+number+"个线程,第" + sjNum + "条数据,中间表id："+s1.getId());
				WorkCompany company = new WorkCompany();
				WorkUser user = new WorkUser();
				WorkCertInfo certInfo = new WorkCertInfo();
				WorkCertApplyInfo certApplyInfo = new WorkCertApplyInfo();
				WorkCompanyHis companyHis = new WorkCompanyHis();
				WorkUserHis userHis = new WorkUserHis();
				
				String productName = s1.getCertType();
				String appName = s1.getAppName();
				
				productName = ProductType.productTypeIdMap.get(productName).toString();
				
				ConfigProduct product = configProductService.findByNamesLabel(s1.getProductLabel(), appName, productName);
				if (product==null) {
					log.error(appName+"下产品名称'"+s1.getCertType()+"'对应的产品不存在...跳过，中间表id:"+s1.getId());
					continue;
				}

				if (companyHash.get(s1.getCompanyName()) != null) {// 存在企业信息
					log.debug("找到已存在的企业:" + s1.getCompanyName());
					company = companyHash.get(s1
							.getCompanyName());
					// his信息不再查询。每次自动生成新的his--实际业务中也是这样处理的
					companyHis = change(company);
				} else {
					log.debug("未找到企业Id,创建新的:" + s1.getCompanyName());
					company.setCompanyName(s1.getCompanyName());
					company.setCompanyType(String.valueOf(s1.getCompanyType()));
					company.setOrganizationNumber(s1.getOrganizationNumber());
					
					if (s1.getOrgExpirationTime()!=null&&!s1.getOrgExpirationTime().equals("")) {
						company.setOrgExpirationTime(new Timestamp(dnf.parse(s1.getOrgExpirationTime()).getTime()));
					}
					
					
					
					company.setSelectLv(String.valueOf(s1.getSelectLv()));
					company.setComCertficateNumber(s1.getComCertficateNumber());
					
					if (s1.getComCertificateTime()!=null&&!s1.getComCertificateTime().equals("")) {
					company.setComCertficateTime(new Timestamp(dnf.parse(s1.getComCertificateTime()).getTime()));
					}
					
					//company.setComCertficateTime(s1.getComCertificateTime());
					company.setComCertificateType(String.valueOf(s1
							.getComCertificateType()));
					company.setLegalName(s1.getLegalName());
					company.setProvince(s1.getSProvince());
					company.setCity(s1.getSCity());
					company.setDistrict(s1.getSCounty());
					company.setAddress(s1.getAddress());
					company.setCompanyIp(s1.getCompanyMobile());
					if (company.getCompanyType()==null||company.getCompanyType().equals("null")) {
						company.setCompanyType("-1");
					}
					if (company.getSelectLv()==null||company.getSelectLv().equals("null")) {
						company.setSelectLv(null);
					}
//					workCompanyService.save(company);
					companyList.add(company);
					
					companyHash.put(company.getCompanyName(), company);
					companyHis = change(company);
				}
//				log.debug("保存company_his信息");
//				workCompanyHisService.save(companyHis);
				companyHisList.add(companyHis);
				
				String conCerNum = s1.getConCertNumber();// 经办人证件号
				if (userHash.get(conCerNum) != null) {
					log.debug("找到 workUser ");
					user = userHash.get(conCerNum);
					userHis = change(user, companyHis);
				} else {
					log.debug("创建新的 workUser ");
					user = new WorkUser();
					user.setContactName(s1.getContactName());
					user.setConCertType(String.valueOf(s1.getConCertType()));
					user.setConCertNumber(s1.getConCertNumber());
					user.setContactEmail(s1.getContactEmail());
					user.setContactPhone(s1.getContactPhone());
					user.setContactTel(s1.getContactTel());
					user.setContactSex(s1.getContactSex());
					user.setWorkCompany(company);
//					workUserService.save(user);
					userList.add(user);
					userHash.put(user.getContactName(), user);
					log.debug("创建新的 workUserHis ");
					userHis = change(user, companyHis);
				}
//				workUserHisService.save(userHis);
				userHisList.add(userHis);
				
//				log.error("保存 workUserHis ");
				certApplyInfo.setName(s1.getName());
				certApplyInfo.setIdCard(s1.getIdCard());
				certApplyInfo.setEmail(s1.getEmail());
//				workCertApplyInfoService.save(certApplyInfo);
				certApplyInfos.add(certApplyInfo);
				certInfo.setIssuerDn(s1.getIssuerDn());
				certInfo.setSubjectDn(s1.getSubjectDn());
				
				//String notafter = s1.getNotafter();
				
				
				certInfo.setNotafter(dnf.parse(s1.getNotafter()));
				certInfo.setNotbefore(dnf.parse(s1.getNotbefore()));
				certInfo.setKeySn("");// 这次不需要keySn即使有也不记录
				certInfo.setTrustDeviceCount(1);//默认送1个
				certInfo.setTrustDeviceDate(dnf.parse(s1.getNotafter()));//可信设备时间
				String serNum = s1.getSerialnumber();
				if (serNum!=null) {
					serNum = serNum.trim();
				}
				certInfo.setSerialnumber(serNum);
				
				certInfo.setWorkCertApplyInfo(certApplyInfo);
//				workCertInfoService.save(certInfo);
				certInfos.add(certInfo);
				
				log.debug("证书信息、企业信息、个人信息生成完毕，开始生成业务信息...");
				try {
					
//					Date start = certInfo.getNotbefore();
//					Date end = certInfo.getNotafter();
					//Integer year = (int) ((s1.get证书天数()) / 365);// 新增业务的年限
					Integer year = s1.getYear();				
					WorkDealInfo workDealInfo = new WorkDealInfo();
					WorkPayInfo workPayInfo = new WorkPayInfo();
					
					ConfigApp app = product.getConfigApp();
					//C-四川CA网点-1408-0826
					workDealInfo.setSvn(s1.getSvnNum());					
					log.debug("证书:"+certInfo.getSerialnumber()+"\t业务年限："+year+ "\t业务编号:"+workDealInfo.getSvn());					
					if (commercialAgentHash.get(app.getId())==null) {
						ConfigCommercialAgent commercialAgent = configAgentAppRelationService
								.findAgentByApp(app);
						workDealInfo.setConfigCommercialAgent(commercialAgent);
					}else {
						workDealInfo.setConfigCommercialAgent(commercialAgentHash.get(app.getId()));
					}
					
					if (configAgentOfficeRelations.size() > 0) {
						workDealInfo
								.setCommercialAgent(configAgentOfficeRelations
										.get(0).getConfigCommercialAgent());// 劳务关系外键
					}else {
						log.debug("证书:"+certInfo.getSerialnumber()+"\t无劳务关系代理商，未设置劳务关系...");
					}
					workDealInfo.setConfigApp(app);
					workDealInfo.setWorkUser(user);
					workDealInfo.setWorkCompany(company);
					workDealInfo.setConfigProduct(product);
					workDealInfo
							.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
					workDealInfo.setYear(year);
					
					if(year*365>s1.getCertValidDays()){
						
						Integer certDayNum = s1.getCertValidDays()-year*365;
						workDealInfo.setAddCertDays(certDayNum);
					}else if(year*365==s1.getCertValidDays()){
						workDealInfo.setAddCertDays(0);
					}else {
						Integer certDayNum = s1.getCertValidDays()-year*365;
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
					workDealInfo.setUpdateDate(new Date());
					workDealInfo.setWorkCertInfo(certInfo);
					workDealInfo.setObtainedDate(new Date());
					workDealInfo.setStatus(0);
					workDealInfo.setIsSJQY(1);
					
					workDealInfo.setNotafter(dnf.parse(s1.getNotafter()));
					
					workDealInfo.setKeySn(s1.getKeyAndUsbSn());
					Double openAccountMoney = 0d;
					Double addCert = 0d;
					if (openAccountHash.get(product.getId())==null) {
//						ConfigChargeAgent chargeAgent = configChargeAgentService
//								.get(workDealInfo.getConfigProduct().getChargeAgentId());
						ConfigChargeAgent chargeAgent = configChargeAgentService
								.get(s1.getAgentId());
						
						openAccountMoney = configChargeAgentDetailService
								.selectMoney(chargeAgent,
										WorkDealInfoType.TYPE_OPEN_ACCOUNT, null,
										workDealInfo.getConfigProduct()
												.getProductLabel());

						workPayInfo.setOpenAccountMoney(openAccountMoney);
						addCert = configChargeAgentDetailService
								.selectMoney(chargeAgent, WorkType.TYPE_ADD,
										workDealInfo.getYear(), workDealInfo
												.getConfigProduct()
												.getProductLabel());
						workPayInfo.setAddCert(addCert);
						if (openAccountMoney == null) {
							openAccountMoney = 0d;
						}
						if (addCert == null) {
							addCert = 0d;
						}
						openAccountHash.put(product.getId(), openAccountMoney);
						addMoneyHash.put(product.getId(), addCert);
					}else {
						log.debug("从map中得到费用信息 ");
						openAccountMoney = openAccountHash.get(product.getId());
						addCert = addMoneyHash.get(product.getId());
						workPayInfo.setOpenAccountMoney(openAccountMoney);
						workPayInfo.setAddCert(addCert);
					}
					
					workPayInfo.setMethodGov(true);
					
					log.debug("证书:"+certInfo.getSerialnumber()+"开户费:"+openAccountMoney+"\t新增证书费用:"+addCert);
					// 证书序列号
					workDealInfo.setCertSort(s1.getMultiCertSns());
					
					
					ConfigChargeAgent agent = configChargeAgentService.get(s1.getAgentId());
					
					workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));
					
					workDealInfo.setConfigChargeAgentId(s1.getAgentId());

					workPayInfo.setWorkTotalMoney(openAccountMoney + addCert);
					workPayInfo.setWorkPayedMoney(0d);
					workPayInfo.setWorkReceivaMoney(0d);
					workPayInfo.setUserReceipt(false);
					workPayInfo.setReceiptAmount(0d);
					workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
					workPayInfo.setCreateBy(createBy);
					workPayInfo.setUpdateBy(createBy);
					log.debug("证书:"+certInfo.getSerialnumber()+"付款流水:"+workPayInfo.getSn());
					payInfos.add(workPayInfo);
					workDealInfo.setWorkPayInfo(workPayInfo);
					workDealInfo
							.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
					workDealInfo.setCanSettle(false);
					s1.setUsed(true);
					
					usedBasicInfo.add(s1);
					unSavedDealInfos.add(workDealInfo);
					
					
					
					//log.error("新增业务数据生成完毕...当前第"+count+"条...");
					log.debug("开始创建第"+number+"个线程,第" + sjNum + "条数据,中间表id："+s1.getId());
					sjNum++;
					
				} catch (Exception e) {
					log.error("Exception at 线程 ："+ number +"\t数据id:"+ s1.getId()+"\t" + e.getMessage());
				}
			}
				log.debug("保存 企业历史 数据+实时数据");
				workCompanyService.save(companyList);
				workCompanyHisService.save(companyHisList);
			
				log.debug("保存 用户历史 数据+实时数据 ");
				workUserService.save(userList);
				workUserHisService.save(userHisList);
			
			
				log.info("保存申请人信息 ");
				workCertApplyInfoService.save(certApplyInfos);
			
				log.info("保存证书信息");
				workCertInfoService.save(certInfos);
			
				log.info("批量保存支付信息");
				workPayInfoService.save(payInfos);
			
				workDealInfoService.save(unSavedDealInfos);
				log.info("批量存储workDealinfo");
				
				basicInfoSccaService.deleteList(usedBasicInfo);
				
			
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("Excepton at num:"+ this.number+"\t" +e.getMessage());
		}
		
		
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

	
	
	public static String getSvn(String head,Integer num){
		String  numStr = "000"+num;
		return  head + "-"+ numStr.substring(numStr.length()-4, numStr.length());
	}
	
	

}
