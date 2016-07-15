package com.itrus.ca.modules.work.web;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.HttpHostConnectException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.topca.tca.client.CertificateRequest;
import cn.topca.tca.client.CertificateResponse;

import com.itrus.ca.common.utils.HttpClientUtil;
import com.itrus.ca.common.utils.PropertiesUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.dao.ConfigSupplierProductRelationDao;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.self.entity.SelfApplication;
import com.itrus.ca.modules.self.service.SelfApplicationService;
import com.itrus.ca.modules.self.utils.SelfApplicationStatus;
import com.itrus.ca.modules.service.CaService;
import com.itrus.ca.modules.settle.web.UpdateQuantityStatistics;
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.service.SignatureInfoService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;

@Controller
@RequestMapping(value = "/ca")
public class CertController extends BaseController {
	@Autowired
	SystemService systemService;
	
	@Autowired
	private SelfApplicationService selfApplicationService;

	@Autowired
	WorkDealInfoService workDealInfoService;

	@Autowired
	ConfigRaAccountService raAccountService;

	@Autowired
	ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;

	@Autowired
	CaService caService;

	@Autowired
	private WorkCertInfoService workCertInfoService;
	@Autowired
	ReceiptInvoiceService receiptInvoiceService;

	@Autowired
	KeyUsbKeyInvoiceService keyInvoiceService;

	@Autowired
	UpdateQuantityStatistics updateQuantityStatistics;
	
	
	@Autowired
	private SignatureInfoService  signatureInfoService;

	private LogUtil logUtil = new LogUtil();

	@Autowired
	ConfigSupplierProductRelationDao configSupplierProductRelationDao;

	@Autowired
	WorkLogService workLogService;

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	private static String dzsBH = new PropertiesUtil("application.properties").readValue("dzsUrl");

	/**
	 * 制证前判断是否入库
	 * 
	 * @param csp
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "validateCspIsValid")
	@ResponseBody
	public String validateCspValid(String csp) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", -1);
			csp = URLDecoder.decode(csp, "UTF-8");
			boolean result = keyInvoiceService.validateCSPvalid(csp);
			json.put("status", result ? 1 : -1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	@RequestMapping(value = "validateCspIsValidNew")
	@ResponseBody
	public String validateCspIsValidNew(String csp, Long dealId, String keySn) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", -1);
			csp = URLDecoder.decode(csp, "UTF-8");
			boolean result = keyInvoiceService.validateCSPvalid(csp);
			json.put("status", result ? 1 : -1);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	
	@RequestMapping(value = "makeCert")
	@ResponseBody
	public String makeCert(Long dealInfoId, String reqOverrideValidity, String addCertDays, String certProvider,
			String csr, String keySn) throws JSONException {
		JSONObject json = new JSONObject();
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		try {
			certProvider = URLDecoder.decode(certProvider, "UTF-8");
			if (certProvider.length() != 0) {
				boolean inStore = keyInvoiceService.validateCSPvalid(certProvider);
				if (!inStore) {
					json.put("status", -1);
					json.put("msg", "当前网点无" + certProvider + "库存，申请证书失败！");
					return json.toString();
				}
			}
			WorkCertInfo caCert = dealInfo.getWorkCertInfo();
		
			if (reqOverrideValidity != null) {
				reqOverrideValidity = reqOverrideValidity.replace(",","");
				if(!addCertDays.equals("")){
					caCert.setReqOverrideValidity(Integer.valueOf(reqOverrideValidity) + Integer.valueOf(addCertDays));
				}else{
					caCert.setReqOverrideValidity(Integer.valueOf(reqOverrideValidity));
				}
				
				
				
			} else {
				caCert.setReqOverrideValidity(365);
			}
			caCert.setProvider(certProvider);
			json.put("status", -1);
		/*	if (caCert.getReqBuf() == null) {
		
				caCert.setReqBuf(csr);
			}*/
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Integer sort = 1;
			Integer amount = 1;
			if (caCert.getSignBuf() == null || caCert.getSignBuf().length() == 0) {// 异常业务安装证书可能不需要再次申请
				try {
					if (dealInfo.getPrevId() == null) {
						// 个人证书
						if (dealInfo.getConfigProduct().getProductName().equals("1")) {

							String orgNum = dealInfo.getWorkCompany().getOrganizationNumber();

							Integer certSortInteger = workDealInfoService.getCertSortByOrganizationNumber(orgNum, 1);
						
							Integer certSortIntegerSC = 0;
						
							if (certSortInteger > certSortIntegerSC) {
								dealInfo.setCertSort(certSortInteger + 1);
							} else {
								dealInfo.setCertSort(certSortIntegerSC + 1);
							}

						} else if (dealInfo.getConfigProduct().getProductName().equals("2")
								|| dealInfo.getConfigProduct().getProductName().equals("6")) {

							String conName = dealInfo.getWorkUser().getContactName();
							String conCertNumber = dealInfo.getWorkUser().getConCertNumber();
							Integer certSortIntegerSC = 0;
							Integer certSortInteger = 0;
							if (conCertNumber != null && !conCertNumber.equals("")) {
								certSortInteger = workDealInfoService.getCertSortByConCertNumber(conCertNumber);
								String url = dzsBH;
								Map<String, String> params = new HashMap<String, String>();
								params.put("param1", "按个人证件号(含企业和机构个人)查个人证书");
								params.put("param2", conCertNumber);
								String res = HttpClientUtil.post(url, params);
								if (!res.equals("")) {
									JSONObject jsonReturn = new JSONObject(res);
									String status = jsonReturn.getString("status");
									if (status.equals("success")) {
										certSortIntegerSC = jsonReturn.getInt("certsInSccA");
									}
								}
							} else if (conName != null && !conName.equals("")) {
								certSortInteger = workDealInfoService.getCertSortByContactName(conName);
								String url = dzsBH;
								Map<String, String> params = new HashMap<String, String>();
								params.put("param1", "按个人姓名(含企业和机构个人)号查个人证书");
								params.put("param2", conName);
								String res = HttpClientUtil.post(url, params);
								if (!res.equals("")) {
									JSONObject jsonReturn = new JSONObject(res);
									String status = jsonReturn.getString("status");
									if (status.equals("success")) {
										certSortIntegerSC = jsonReturn.getInt("certsInSccA");
									}
								}
							}

							if (certSortInteger > certSortIntegerSC) {
								dealInfo.setCertSort(certSortInteger + 1);
							} else {
								dealInfo.setCertSort(certSortIntegerSC + 1);
							}

						} else if (dealInfo.getConfigProduct().getProductName().equals("3")) {

							String orgNum = dealInfo.getWorkCompany().getOrganizationNumber();
							String comPanyName = dealInfo.getWorkCompany().getCompanyName();

							Integer certSortIntegerSC = 0;
							Integer certSortInteger = 0;

							if (orgNum != null && !orgNum.equals("")) {
								certSortInteger = workDealInfoService.getCertSortByOrganizationNumber(orgNum, 3);

								String url = dzsBH;
								Map<String, String> params = new HashMap<String, String>();
								params.put("param1", "按单位组织机构代码(含企业和机构证书)查单位证书");
								params.put("param2", orgNum);
								String res = HttpClientUtil.post(url, params);
								if (!res.equals("")) {
									JSONObject jsonReturn = new JSONObject(res);
									String status = jsonReturn.getString("status");
									if (status.equals("success")) {
										certSortIntegerSC = jsonReturn.getInt("certsInSccA");
									}
								}
							} else if (comPanyName != null && !comPanyName.equals("")) {
								certSortInteger = workDealInfoService.getCertSortByCompanyName(comPanyName, 3);
								String url = dzsBH;
								Map<String, String> params = new HashMap<String, String>();
								params.put("param1", "按单位名称(含企业和机构证书)查单位证书 ");
								params.put("param2", comPanyName);
								String res = HttpClientUtil.post(url, params);
								if (!res.equals("")) {
									JSONObject jsonReturn = new JSONObject(res);
									String status = jsonReturn.getString("status");
									if (status.equals("success")) {
										certSortIntegerSC = jsonReturn.getInt("certsInSccA");
									}
								}
							}
							if (certSortInteger > certSortIntegerSC) {
								dealInfo.setCertSort(certSortInteger + 1);
							} else {
								dealInfo.setCertSort(certSortIntegerSC + 1);
							}
						} else {
							dealInfo.setCertSort(sort);
						}
					} else {
						dealInfo.setCertSort(workDealInfoService.get(dealInfo.getPrevId()).getCertSort());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 取RA配置
				ConfigRaAccount raAccount = raAccountService.get(dealInfo.getConfigProduct().getRaAccountId());
				// 取RA配置模版项
				ConfigRaAccountExtendInfo extendInfo = configRaAccountExtendInfoService
						.get(dealInfo.getConfigProduct().getRaAccountExtedId());
				if (raAccount == null || extendInfo == null) {
					json.put("status", -1);
					json.put("msg", "CA配置错误：无证书注册项模板");
					return json.toString();
				}
			

				CertificateRequest request = RaAccountUtil.outRequest(dealInfo, extendInfo);
				System.out.println("==========");
				System.out.println(caCert.getReqBuf());
				
				
				/*String reqBuf = caCert.getReqBuf();
				caCert.setReqBuf(null);*/

				CertificateResponse certificateResponse = caService.getCaCert(request, csr,
						Integer.valueOf(reqOverrideValidity) + Integer.valueOf(addCertDays), raAccount);

				caCert.setType(certificateResponse.getCertType());
				caCert.setSignBuf(certificateResponse.getCertSignBuf());
				caCert.setSerialnumber(certificateResponse.getCertSerialNumber());
				caCert.setIssuerDn(certificateResponse.getCertIssuerDn());
				caCert.setIssuerHashMd5(certificateResponse.getCertIssuerHashMd5());
				caCert.setNotafter(sdf.parse(certificateResponse.getCertNotAfter()));
				caCert.setNotbefore(sdf.parse(certificateResponse.getCertNotBefore()));
				caCert.setReqBuf(certificateResponse.getCertReqBuf());
				caCert.setReqBufType(certificateResponse.getCertReqBufType());
				caCert.setReqOverrideValidity(Integer.valueOf(certificateResponse.getCertReqOverrideValidity()));
				caCert.setSignDate(new Date());
				caCert.setSubjectDn(certificateResponse.getCertSubjectDn());
				caCert.setSubjectHashMd5(certificateResponse.getCertSubjectHashMd5());
				caCert.setSignBufP7(certificateResponse.getCertSignBufP7());
				caCert.setObtained(false);
				caCert.setTrustDeviceCount(1);
				caCert.setTrustDeviceDate(caCert.getNotafter());
				// 支持双证
				caCert.setCertKmcRep1(certificateResponse.getCertKmcRep1());
				caCert.setCertKmcRep2(certificateResponse.getCertKmcRep2());
				caCert.setCertKmcRep3(certificateResponse.getCertKmcRep3());
				caCert.setCertSignBufKmc(certificateResponse.getCertSignBufKmc());
				caCert.setCertTypeKmc(certificateResponse.getCertTypeKmc());
				caCert.setCertReqBufKmc(certificateResponse.getCertReqBufKmc());
				caCert.setCertReqBufTypeKmc(certificateResponse.getCertReqBufTypeKmc());
				caCert.setCertSerialnumberKmc(certificateResponse.getCertSerialnumberKmc());
				caCert.setInstallMode(certificateResponse.getInstallMode());
				if (caCert.getCertSignBufKmc() != null) {
					amount = 2;
					json.put("kmcvalid", 1);
				} else {
					json.put("kmcvalid", 0);
				}
				workCertInfoService.save(caCert);
				caCert.setCreateDate(workCertInfoService.getCreateDate(caCert.getId()));
				workCertInfoService.save(caCert);
				dealInfo.setCertSn(caCert.getSerialnumber());
				dealInfo.setKeySn(keySn);
				dealInfo.setBusinessCardUser(UserUtils.getUser());
				dealInfo.setBusinessCardUserDate(new Date());
				dealInfo.setNotafter(caCert.getNotafter());
				dealInfo.setAddCertDays(Integer.parseInt(addCertDays));
				if (!WorkDealInfoType.TYPE_RETURN_MONEY.equals(dealInfo.getDealInfoType())) {
					Integer type = getDealInfoAddType(dealInfo);
					if (raAccount.getIsTest()) {
						type = 3;
					}
					ConfigSupplier supplier = getSupplier(
							Integer.valueOf(dealInfo.getConfigProduct().getProductName()));
					if (supplier != null) {
						updateQuantityStatistics.updateOUSettleInfo(raAccount.getAccountOrgUnit(),
								Integer.valueOf(dealInfo.getConfigProduct().getProductName()), type, amount,
								dealInfo.getYear(), supplier);
					}
				} else {
					dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
				}
				if(dealInfo.getSelfApplyId()!=null){
					SelfApplication selfApplication=selfApplicationService.get(dealInfo.getSelfApplyId());
					selfApplication.setKeySn(keySn);
					selfApplication.setStatus(SelfApplicationStatus.finishApply);
					selfApplicationService.save(selfApplication);
				}
				workDealInfoService.save(dealInfo);
			}

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			json.put("status", 1);
			json.put("signBufP7", caCert.getSignBufP7());
			json.put("signBuf", caCert.getSignBuf());
			json.put("msg", "申请成功");
			json.put("sort", dealInfo.getCertSort());
			json.put("sn", caCert.getSerialnumber());// 序列号
			json.put("notbefore", sdf1.format(caCert.getNotbefore()));
			json.put("notafter", sdf1.format(caCert.getNotafter()));// 有效期
			json.put("issuer", caCert.getIssuerDn());// 颁发者
			json.put("subject", caCert.getSubjectDn());// 主题
			json.put("certKmcBuf", caCert.getCertSignBufKmc());
			json.put("certKmcRep1", caCert.getCertKmcRep1());
			json.put("certKmcRep2", caCert.getCertKmcRep2());
			json.put("installMode", caCert.getInstallMode());

			if (dealInfo.getDealInfoType() == null) {

				ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
				dealInfoBound.setDealInfo(dealInfo);
				ConfigChargeAgent agentBound = configChargeAgentService.get(dealInfo.getConfigChargeAgentId());
				dealInfoBound.setAgent(agentBound);
				configAgentBoundDealInfoService.save(dealInfoBound);
				logUtil.saveSysLog("计费策略模版", "计费策略模版：" + dealInfo.getId() + "--业务编号：" + dealInfo.getId() + "--关联成功!",
						"");

			}

			if (dealInfo.getDealInfoType() != null && dealInfo.getDealInfoType().equals(0)) {

				ConfigChargeAgent agent = configChargeAgentService.get(dealInfo.getConfigChargeAgentId());
				Integer avaiNum = agent.getAvailableNum();// 已用数量
				Integer reseNum = agent.getReserveNum();// 预留数量
				agent.setAvailableNum(avaiNum + 1);// 已用数量
				agent.setReserveNum(reseNum - 1);// 预留数量
				configChargeAgentService.save(agent);
				logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
			}

		}
		
		
		
		catch (HttpHostConnectException e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("msg", "申请证书失败,可能原因:制证服务器未连接,制证服务器未开启！");
				// 异常业务
			dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
			workDealInfoService.save(dealInfo);
			logUtil.saveSysLog("业务中心", "制证失败", e.getMessage());	
			return json.toString();
		}
		
		
		catch (ConnectException e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("msg", "申请证书失败,可能原因:制证服务器未连接,CA模板配置错误！");
				// 异常业务
			dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
			workDealInfoService.save(dealInfo);
			logUtil.saveSysLog("业务中心", "制证失败", e.getMessage());	
			return json.toString();
			
		}
		
		catch (NullPointerException e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("msg", "申请证书失败,可能原因:制证服务器未连接！");
				// 异常业务
			dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
			workDealInfoService.save(dealInfo);
			logUtil.saveSysLog("业务中心", "制证失败", e.getMessage());	
			return json.toString();
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			json.put("status", -1);
			json.put("msg", "申请证书失败,可能原因:制证服务器未连接！");
				// 异常业务
			dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
			workDealInfoService.save(dealInfo);
			logUtil.saveSysLog("业务中心", "制证失败", e.getMessage());	
			return json.toString();
		}
		
		System.out.println("json:"+json.toString());
		logUtil.saveSysLog("业务中心", "制证：编号" + dealInfoId, "");
		return json.toString();
	}
	
	
	
	@RequestMapping(value = "makeCertInstallFail")
	@ResponseBody
	public String makeCertInstallFail(Long dealInfoId,  String certProvider,
			String csr, String keySn) throws Exception {
		JSONObject json = new JSONObject();
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		WorkCertInfo caCert = dealInfo.getWorkCertInfo();
		certProvider = URLDecoder.decode(certProvider, "UTF-8");
		if (certProvider.length() != 0) {
			boolean inStore = keyInvoiceService.validateCSPvalid(certProvider);
			if (!inStore) {
				json.put("status", -1);
				json.put("msg", "当前网点无" + certProvider + "库存，申请证书失败！");
				return json.toString();
			}
		}
		caCert.setProvider(certProvider);
		caCert.setReqBuf(csr);
		workCertInfoService.save(caCert);
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		json.put("status", 1);
		json.put("signBufP7", caCert.getSignBufP7());
		json.put("signBuf", caCert.getSignBuf());
		json.put("issuer", caCert.getIssuerDn());// 颁发者
		json.put("sort", dealInfo.getCertSort());
		json.put("sn", caCert.getSerialnumber());// 序列号
		json.put("notbefore", sdf1.format(caCert.getNotbefore()));
		json.put("notafter", sdf1.format(caCert.getNotafter()));// 有效期
		json.put("subject", caCert.getSubjectDn());// 主题
		if (caCert.getCertSignBufKmc() != null) {
			json.put("kmcvalid", 1);
		} else {
			json.put("kmcvalid", 0);
		}
		return json.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@RequestMapping(value = "enrollMakeCert")
	@ResponseBody
	public String enrollMakeCert(Long dealInfoId, String reqOverrideValidity, String certProvider, String csr,
			String keySn) throws JSONException {
		JSONObject json = new JSONObject();
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		try {
			certProvider = URLDecoder.decode(certProvider, "UTF-8");
			WorkCertInfo caCert = dealInfo.getWorkCertInfo();
			WorkCertApplyInfo applyInfo = caCert.getWorkCertApplyInfo();
			if (reqOverrideValidity != null) {
				reqOverrideValidity = reqOverrideValidity.replace(",", "");
				caCert.setReqOverrideValidity(Integer.valueOf(reqOverrideValidity));
			} else {
				caCert.setReqOverrideValidity(365);
			}
			caCert.setProvider(certProvider);
			json.put("status", -1);
			if (caCert.getReqBuf() == null) {
				String result = java.net.URLDecoder.decode(csr, "UTF-8");
				caCert.setReqBuf(csr);
			}
			Calendar cal = Calendar.getInstance();
			// cal.add(Calendar.DAY_OF_MONTH, caCert.getReqOverrideValidity());
			Date da = cal.getTime();
			// caCert.setNotafter(da);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Integer sort = 1;
			Integer amount = 1;
			if (caCert.getSignBuf() == null || caCert.getSignBuf().length() == 0) {// 异常业务安装证书可能不需要再次申请
				try {
					if (dealInfo.getCertSort() == null) {
						// 个人证书
						if (dealInfo.getConfigProduct().getProductName().equals("2")
								|| dealInfo.getConfigProduct().getProductName().equals("6")) {
							sort = workDealInfoService.getMultiNumByWorkUser(dealInfo.getWorkUser().getId());
						} else {
							sort = workDealInfoService.getMultiNumByWorkCompany(dealInfo.getWorkCompany().getId());
						}
						dealInfo.setCertSort(sort);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 取RA配置
				ConfigRaAccount raAccount = raAccountService.get(dealInfo.getConfigProduct().getRaAccountId());
				// 取RA配置模版项
				ConfigRaAccountExtendInfo extendInfo = configRaAccountExtendInfoService
						.get(dealInfo.getConfigProduct().getRaAccountExtedId());
				if (raAccount == null || extendInfo == null) {
					json.put("status", -1);
					json.put("msg", "CA配置错误：无证书注册项模板");
					return json.toString();
				}
				// if
				// (raAccount==null||raAccount.getConfigRaAccountExtendInfo()==null||raAccount.getConfigRaAccountExtendInfo().getId()==null)
				// {
				// json.put("status", -1);
				// json.put("msg", "CA配置错误：无证书注册项模板");
				// return json.toJSONString();
				// }

				CertificateRequest request = RaAccountUtil.outRequest(dealInfo, extendInfo);
				CertificateResponse certificateResponse = caService.getCaCert(request, caCert.getReqBuf(),
						Integer.valueOf(reqOverrideValidity), raAccount);

				caCert.setType(certificateResponse.getCertType());
				caCert.setSignBuf(certificateResponse.getCertSignBuf());
				caCert.setSerialnumber(certificateResponse.getCertSerialNumber());
				caCert.setIssuerDn(certificateResponse.getCertIssuerDn());
				caCert.setIssuerHashMd5(certificateResponse.getCertIssuerHashMd5());
				caCert.setNotafter(sdf.parse(certificateResponse.getCertNotAfter()));
				caCert.setNotbefore(sdf.parse(certificateResponse.getCertNotBefore()));
				caCert.setReqBuf(certificateResponse.getCertReqBuf());
				caCert.setReqBufType(certificateResponse.getCertReqBufType());
				caCert.setReqOverrideValidity(Integer.valueOf(certificateResponse.getCertReqOverrideValidity()));
				caCert.setSignDate(new Date());
				caCert.setSubjectDn(certificateResponse.getCertSubjectDn());
				caCert.setSubjectHashMd5(certificateResponse.getCertSubjectHashMd5());
				caCert.setSignBufP7(certificateResponse.getCertSignBufP7());
				caCert.setObtained(false);
				caCert.setTrustDeviceCount(1);
				caCert.setTrustDeviceDate(caCert.getNotafter());
				// 支持双证
				caCert.setCertKmcRep1(certificateResponse.getCertKmcRep1());
				caCert.setCertKmcRep2(certificateResponse.getCertKmcRep2());
				caCert.setCertKmcRep3(certificateResponse.getCertKmcRep3());
				caCert.setCertSignBufKmc(certificateResponse.getCertSignBufKmc());
				caCert.setCertTypeKmc(certificateResponse.getCertTypeKmc());
				caCert.setCertReqBufKmc(certificateResponse.getCertReqBufKmc());
				caCert.setCertReqBufTypeKmc(certificateResponse.getCertReqBufTypeKmc());
				caCert.setCertSerialnumberKmc(certificateResponse.getCertSerialnumberKmc());
				caCert.setInstallMode(certificateResponse.getInstallMode());
				if (caCert.getCertSignBufKmc() != null) {
					amount = 2;
					json.put("kmcvalid", 1);
				} else {
					json.put("kmcvalid", 0);
				}
				workCertInfoService.save(caCert);
				caCert.setCreateDate(workCertInfoService.getCreateDate(caCert.getId()));
				workCertInfoService.save(caCert);
				SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = new String(sss.format(caCert.getNotbefore()) + "至" + sss.format(caCert.getNotafter()));
				dealInfo.setCertSn(caCert.getSerialnumber());
				dealInfo.setKeySn(keySn);
				dealInfo.setNotafter(caCert.getNotafter());
				
				//新增制证时间
				dealInfo.setBusinessCardUserDate(new Date());
				workDealInfoService.save(dealInfo);
				Integer type = getDealInfoAddType(dealInfo);
				if (raAccount.getIsTest()) {
					type = 3;
				}
				ConfigSupplier supplier = getSupplier(Integer.valueOf(dealInfo.getConfigProduct().getProductName()));

				updateQuantityStatistics.updateOUSettleInfo(raAccount.getAccountOrgUnit(),
						Integer.valueOf(dealInfo.getConfigProduct().getProductName()), type, amount, dealInfo.getYear(),
						supplier);
			}

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			json.put("status", 1);
			json.put("signBufP7", caCert.getSignBufP7());
			json.put("signBuf", caCert.getSignBuf());
			json.put("msg", "申请成功");
			json.put("sort", dealInfo.getCertSort());
			json.put("sn", caCert.getSerialnumber());// 序列号
			json.put("notbefore", sdf1.format(caCert.getNotbefore()));
			json.put("notafter", sdf1.format(caCert.getNotafter()));// 有效期
			json.put("issuer", caCert.getIssuerDn());// 颁发者
			json.put("subject", caCert.getSubjectDn());// 主题
			json.put("certKmcBuf", caCert.getCertSignBufKmc());
			json.put("certKmcRep1", caCert.getCertKmcRep1());
			json.put("certKmcRep2", caCert.getCertKmcRep2());
			json.put("installMode", caCert.getInstallMode());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("msg", "申请证书失败");
			// 异常业务
			dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
			workDealInfoService.save(dealInfo);
		}
		// try {
		// //发票出库
		// //前台服务发票出库
		// if (dealInfo.getWorkPayInfo().getUserReceipt()) {
		// boolean r =
		// receiptInvoiceService.receiptIncoiceI(dealInfo.getWorkPayInfo().getReceiptAmount(),UserUtils.getUser().getOffice(),dealInfo.getWorkCompany().getCompanyName());
		// //key出库
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		return json.toString();
	}

	/**
	 * 证书安装完后调用
	 * 
	 * @param dealInfoId
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "installResult")
	@ResponseBody
	public String installCertResult(Long dealInfoId, Integer result) throws JSONException {
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		JSONObject json = new JSONObject();
		try {
			if (result == 1) {// 安装成功
				
				json.put("isChange", false);
				
				
				WorkCertInfo workCertInfo = dealInfo.getWorkCertInfo();
				workCertInfo.setObtained(true);
				workCertInfoService.save(workCertInfo);
				dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);// 审核通过
				dealInfo.setObtainedDate(new Date());
				if (dealInfo.getWorkPayInfo().getMethodGov() || dealInfo.getWorkPayInfo().getMethodContract()) {
					dealInfo.setUserSn(getHTSn(dealInfo));
				} else {
					dealInfo.setUserSn(getZFSn(dealInfo));
				}
				dealInfo.setStatus(0);// 待归档
				workDealInfoService.save(dealInfo);
				// 发票出库
				if (dealInfo.getWorkPayInfo().getUserReceipt()) {
					receiptInvoiceService.receiptIncoiceI(dealInfo.getWorkPayInfo().getReceiptAmount(),
							UserUtils.getUser().getOffice(), dealInfo.getWorkCompany().getCompanyName(), dealInfoId); // key出库
				}
				if (dealInfo.getDealInfoType() != null && dealInfo.getDealInfoType().equals(0)) {
					// 新增key出库
					keyInvoiceService.saveInvoice(dealInfo.getWorkCertInfo().getProvider(), dealInfo.getKeySn(),
							dealInfo.getWorkCompany().getCompanyName());
				} else if (dealInfo.getDealInfoType1() != null) {
					if (dealInfo.getDealInfoType1().equals(2) || dealInfo.getDealInfoType1().equals(3)) {
						// 遗失补办损坏更换key出库
						keyInvoiceService.saveInvoice(dealInfo.getWorkCertInfo().getProvider(), dealInfo.getKeySn(),
								dealInfo.getWorkCompany().getCompanyName());

					}

				}
				
				
				List<SignatureInfo> signatureInfos = signatureInfoService.findByDealInfo(dealInfo);
				if(!signatureInfos.isEmpty()){
					if(dealInfo.getDealInfoType2()!=null){
						if(dealInfo.getDealInfoType2()==4){
							json.put("isChange", true);
						}	
					}	
				}
				
				
				//只有遗失补办吊销,其余情况都不吊销
				List<Integer> revokeList = new ArrayList<Integer>();
				revokeList.add(WorkDealInfoType.TYPE_LOST_CHILD);
			
				// 吊销证书
				if (dealInfo.getPrevId() != null && revokeList.contains(dealInfo.getDealInfoType1())) {
					WorkDealInfo old = workDealInfoService.get(dealInfo.getPrevId());
					
					if(old.getNotafter().after(new Date())){
						revokeOldCert(old.getId());
						old.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_REVOKE);
						workDealInfoService.save(old);	
					}
				}
				json.put("status", 1);
			} else {
				System.out.println("installCertResult : cert install failed. dealInfo id = "+ dealInfoId + ",result = " + result);
				dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
				workDealInfoService.save(dealInfo);
				json.put("status", -1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
		}
		return json.toString();
	}

	/**
	 * 证书安装完后调用
	 * 
	 * @param dealInfoId
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "installResult4Enroll")
	@ResponseBody
	public String installResult4Enroll(Long dealInfoId, Integer result) throws JSONException {
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		JSONObject json = new JSONObject();
		try {
			if (result == 1) {// 安装成功
				WorkCertInfo workCertInfo = dealInfo.getWorkCertInfo();
				workCertInfo.setObtained(true);
				workCertInfoService.save(workCertInfo);
				dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);// 审核通过
				dealInfo.setObtainedDate(new Date());
				if (dealInfo.getWorkPayInfo().getMethodGov() || dealInfo.getWorkPayInfo().getMethodContract()) {
					dealInfo.setUserSn(getHSn(dealInfo));
				} else {
					dealInfo.setUserSn(getZSn(dealInfo));
				}
				dealInfo.setStatus(0);// 待归档
				workDealInfoService.save(dealInfo);
				// //key出库
				// keyInvoiceService.saveInvoice(dealInfo.getWorkCertInfo().getProvider(),
				// dealInfo.getKeySn());
				// List<Integer> revokeList = new ArrayList<Integer>();
				// revokeList.add(WorkDealInfoType.TYPE_LOST_CHILD);
				// revokeList.add(WorkDealInfoType.TYPE_DAMAGED_REPLACED);
				// revokeList.add(WorkDealInfoType.TYPE_INFORMATION_REROUTE);
				// //吊销证书
				// if
				// (dealInfo.getPrevId()!=null&&revokeList.contains(dealInfo.getDealInfoType()))
				// {
				// WorkDealInfo old =
				// workDealInfoService.get(dealInfo.getPrevId());
				// revokeOldCert(old.getId());
				// old.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_UNABLE);
				// workDealInfoService.save(old);
				// }
				json.put("status", 1);
			} else {
				System.out.println("installResult4Enroll : cert install failed. dealInfo id = "+ dealInfoId + ",result = " + result);
				dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ABNORMAL_USER);// 异常业务
				workDealInfoService.save(dealInfo);
				json.put("status", -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
		}
		return json.toString();
	}

	/**
	 * 吊销业务对应的证书
	 * 
	 * @param dealInfoId
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "revokeCert")
	@ResponseBody
	public String revokeCert(Long dealInfoId) throws JSONException {
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		JSONObject json = new JSONObject();
		try {
			ConfigRaAccount raAccount = raAccountService.get(dealInfo.getConfigProduct().getRaAccountId());
			caService.revokeCaCert(dealInfo.getCertSn(), "", raAccount);

			dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_REVOKE);
			WorkCertInfo certInfo = dealInfo.getWorkCertInfo();
			certInfo.setStatus(3);
			certInfo.setRevokeDate(new Date());
			workCertInfoService.save(certInfo);
			workDealInfoService.save(dealInfo);
			// 保存日志信息
			WorkLog workLog = new WorkLog();
			workLog.setRecordContent("吊销成功");
			workLog.setWorkDealInfo(dealInfo);
			workLog.setCreateDate(new Date());
			workLog.setCreateBy(UserUtils.getUser());
			workLog.setOffice(UserUtils.getUser().getOffice());
			workLog.setConfigApp(dealInfo.getConfigApp());
			workLog.setWorkCompany(dealInfo.getWorkCompany());
			workLogService.save(workLog);

			json.put("status", 1);

			if (!raAccount.getIsTest()) {
				ConfigSupplier supplier = getSupplier(Integer.valueOf(dealInfo.getConfigProduct().getProductName()));
				updateQuantityStatistics.updateOUSettleInfo(raAccount.getAccountOrgUnit(),
						Integer.valueOf(dealInfo.getConfigProduct().getProductName()), 2, 1, dealInfo.getYear(),
						supplier);
			}
		} catch (Exception e) {
			json.put("status", -1);
		}
		logUtil.saveSysLog("业务中心", "吊销证书：编号" + dealInfoId, "");
		return json.toString();
	}

	/**
	 * 只吊销证书 不改变业务状态
	 * 
	 * @param dealInfoId
	 */
	private void revokeOldCert(Long dealInfoId) {
		WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
		try {
			ConfigRaAccount raAccount = raAccountService.get(dealInfo.getConfigProduct().getRaAccountId());
			caService.revokeCaCert(dealInfo.getCertSn(), "", raAccount);
			WorkCertInfo certInfo = dealInfo.getWorkCertInfo();
			certInfo.setStatus(3);
			certInfo.setRevokeDate(new Date());
			workCertInfoService.save(certInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private CertificateRequest transCaUserToRequest(WorkCertApplyInfo
	// applyInfo){
	// CertificateRequest re = new CertificateRequest();
	// re.setUserAdditionalField1(applyInfo.getAddtionalField1());
	// re.setUserAdditionalField2(applyInfo.getAddtionalField2());
	// re.setUserAdditionalField3(applyInfo.getAddtionalField3());
	// re.setUserAdditionalField4(applyInfo.getAddtionalField4());
	// re.setUserAdditionalField5(applyInfo.getAddtionalField5());
	// re.setUserAdditionalField6(applyInfo.getAddtionalField6());
	// re.setUserAdditionalField7(applyInfo.getAddtionalField7());
	// re.setUserAdditionalField8(applyInfo.getAddtionalField8());
	// re.setUserAdditionalField9(applyInfo.getAddtionalField9());
	// re.setUserAdditionalField10(applyInfo.getAddtionalField10());
	// re.setUserCountry(applyInfo.getCountry());
	// re.setUserDescription(applyInfo.getDescription());
	// re.setUserDns(applyInfo.getDns());
	// re.setUserEmail(applyInfo.getEmail());
	// re.setUserIp(applyInfo.getIp());
	// re.setUserLocality(applyInfo.getLocality());
	// re.setUserMobilePhone(applyInfo.getMobilePhone());
	// re.setUserName(applyInfo.getName());
	// re.setUserOrganization(applyInfo.getOrganization());
	// re.setUserOrgunit(applyInfo.getOrgunit());
	// re.setUserStreet(applyInfo.getStreet());
	// re.setUserSurname(applyInfo.getSurname());
	// re.setUserTitle(applyInfo.getTitle());
	// return re;
	// }

	private Integer getDealInfoAddType(WorkDealInfo dealInfo) {
		List<Integer> allTypes = new ArrayList<Integer>();
		allTypes.add(dealInfo.getDealInfoType());
		allTypes.add(dealInfo.getDealInfoType1());
		allTypes.add(dealInfo.getDealInfoType2());
		allTypes.add(dealInfo.getDealInfoType3());

		if (allTypes.contains(WorkDealInfoType.TYPE_UPDATE_CERT)) {
			return 1;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
			return dealInfo.getManMadeDamage() ? 5 : 4;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_LOST_CHILD)) {
			return 5;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_INFORMATION_REROUTE)) {
			return 6;
		}
		if (allTypes.contains(WorkDealInfoType.TYPE_ADD_CERT)) {
			return -1;
		}
		return -1;
	}

	/**
	 * 根据类型查询供应商
	 * 
	 * @param productType
	 * @return
	 */
	private ConfigSupplier getSupplier(Integer productType) {
		try {
			DetachedCriteria dc = configSupplierProductRelationDao.createDetachedCriteria();
			dc.createAlias("configSupplier", "configSupplier");
			dc.add(Restrictions.eq("productType", productType));
			dc.add(Restrictions.eq("configSupplier.delFlag", ConfigSupplier.DEL_FLAG_NORMAL));
			List<ConfigSupplierProductRelation> relations = configSupplierProductRelationDao.find(dc);
			return relations.get(0).getConfigSupplier();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 按照合同生成归档编码
	 * 
	 * @param workDealInfo
	 * @return
	 */
	private String getHTSn(WorkDealInfo workDealInfo) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		workDealInfo.setStatus(0);
		User user = UserUtils.getUser();
		user.getOffice().getName();
		String sn = sdf.format(date).toString().substring(2);
		List<WorkDealInfo> list = workDealInfoService.findNum(workDealInfo, "%" + sn + "%");
		int num = list.size() + 1;
		String numstr = "";
		if (num > 0 && num < 10) {
			numstr = "00" + num;
		} else if (num > 9 && num < 100) {
			numstr = "0" + num;
		} else {
			numstr = "" + num;
		}
		String archiveSn = "SCCA-JZ-" + user.getOffice().getName() + "-" + workDealInfo.getConfigApp().getAppName()
				+ "-" + sn + numstr;
		return archiveSn;
	}

	/**
	 * 按照自费生成归档编码
	 * 
	 * @param workDealInfo
	 * @return
	 */
	private String getZFSn(WorkDealInfo workDealInfo) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-");
		workDealInfo.setStatus(0);
		User user = UserUtils.getUser();
		user.getOffice().getName();
		String sn = sdf.format(date).toString().substring(2);
		List<WorkDealInfo> list = workDealInfoService.findNum(workDealInfo, "%" + sn + "%");
		int num = list.size() + 1;
		String numstr = "";
		if (num > 0 && num < 10) {
			numstr = "00" + num;
		} else if (num > 9 && num < 100) {
			numstr = "0" + num;
		} else {
			numstr = "" + num;
		}
		String archiveSn = "SCCA-JZ-" + user.getOffice().getName() + "-" + sn + numstr;
		return archiveSn;
	}

	/**
	 * 按照合同生成归档编码
	 * 
	 * @param workDealInfo
	 * @return
	 */
	private String getHSn(WorkDealInfo workDealInfo) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
		workDealInfo.setStatus(0);
		User user = systemService.getUser(1L);
		String sn = sdf.format(date).toString().substring(2);
		List<WorkDealInfo> list = workDealInfoService.findNum(workDealInfo, "%" + sn + "%");
		int num = list.size() + 1;
		String numstr = "";
		if (num > 0 && num < 10) {
			numstr = "00" + num;
		} else if (num > 9 && num < 100) {
			numstr = "0" + num;
		} else {
			numstr = "" + num;
		}
		String archiveSn = "SCCA-JZ-" + user.getOffice().getName() + "-" + workDealInfo.getConfigApp().getAppName()
				+ "-" + sn + numstr;
		return archiveSn;
	}

	/**
	 * 按照自费生成归档编码
	 * 
	 * @param workDealInfo
	 * @return
	 */
	private String getZSn(WorkDealInfo workDealInfo) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-");
		workDealInfo.setStatus(0);
		User user = systemService.getUser(1L);
		String sn = sdf.format(date).toString().substring(2);
		List<WorkDealInfo> list = workDealInfoService.findNum(workDealInfo, "%" + sn + "%");
		int num = list.size() + 1;
		String numstr = "";
		if (num > 0 && num < 10) {
			numstr = "00" + num;
		} else if (num > 9 && num < 100) {
			numstr = "0" + num;
		} else {
			numstr = "" + num;
		}
		String archiveSn = "SCCA-JZ-" + user.getOffice().getName() + "-" + sn + numstr;
		return archiveSn;
	}
}
