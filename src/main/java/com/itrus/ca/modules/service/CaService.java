package com.itrus.ca.modules.service;

import static cn.topca.security.bc.openssl.OpenSslUtil.decodePEMCert;
import static cn.topca.security.bc.openssl.OpenSslUtil.decodePEMPrivateKey;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.modules.connection.CaConnectionPoolProvider;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.cert.X509Certificate;
import com.itrus.raapi.RaCertManager;
import com.itrus.raapi.RaFactory;
import com.itrus.raapi.exception.RaServiceUnavailable;
import com.itrus.raapi.info.UserInfo;
import com.itrus.raapi.result.EnrollResult;
import com.itrus.raapi.result.OperationResult;
import com.itrus.raapi.result.PickupResult;
import com.itrus.raapi.result.RevokeResult;

import cn.topca.connection.ResponseException;
import cn.topca.connection.examples.SampleConnectionProvider;
import cn.topca.security.JCAJCEUtils;
import cn.topca.tca.client.CAClient;
import cn.topca.tca.client.CertificateRequest;
import cn.topca.tca.client.CertificateResponse;
import cn.topca.tca.client.RevokeRequest;
import cn.topca.tca.client.RevokeResponse;
import cn.topca.tca.ra.service.RaServiceUnavailable_Exception;
import cn.topca.tca.ra.service.UserAPIService;
import cn.topca.tca.ra.service.UserAPIServicePortType;

/**
 * 链接CA服务的service
 * 
 * @author ZhangJingtao
 * 
 */
@Service
public class CaService {
	Logger log = Logger.getLogger(CaService.class);
	static{
		// 注册连接服务提供者--static 启动时注册一下即可
		CaConnectionPoolProvider.getInstance().register();
		JCAJCEUtils.register(new BouncyCastleProvider());
	}
	
//	public static String CA_URL = "http://localhost:8085/TopCA/crs/crs";//TopCA
//	public static String RA_HASH ="059A1782E816F71212AE704AA2F1EFE6";//ra_hash
	
	
	/**
	 * 
	 * @param certificateRequest 请求数据，apply_info
	 * @param reqBuf csr签名
	 * @param validityDays  申请时间
	 * @param raAccount  configRaAccount
	 * @return
	 */
	public CertificateResponse getCaCert(CertificateRequest certificateRequest,String reqBuf,Integer validityDays,ConfigRaAccount raAccount)  throws Exception{
		CertificateResponse certificateResponse = null;//证书数据
		if (validityDays==null) {//更新证书时，如果不配ra的默认更新天数，并且未指定，则时间是原来证书的天数
			validityDays = 365;
		}
		if (certificateRequest.getUserOrganization()==null) {
			certificateRequest.setUserOrganization(raAccount.getAccountOrganization());
		}
		if (certificateRequest.getUserOrgunit()==null) {
			certificateRequest.setUserOrgunit(raAccount.getAccountOrgUnit());
		}
		
		if (raAccount.getRaProtocol().equals("TCA")) {//raAccount.getRaProtocol().equals("TCA")
			reqBuf = reqBuf.replace("%2B", "+");
			reqBuf = reqBuf.replace("%26", "&");
			log.info("请求CA申请证书");
			/* init system */
			/* init properties */
			String connection = "ConnectionPool"; // 按实际情况实现连接服务
			Certificate recipientCertificate = decodePEMCert(CA_RECIPIENT_CERT);
			PrivateKey nativePrivateKey = decodePEMPrivateKey(RA_NATIVE_PKEY,
					"password".toCharArray());
			Certificate nativeCertificate = decodePEMCert(RA_NATIVE_CERT);

			/* setup ca client */
			CAClient client = null;
			try {
				client = CAClient.getInstance(connection, raAccount.getServiceUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 申请的证书序列号
			String certSerialnumber = null;
			/* request certificate action */
			try {
				String csr = reqBuf;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				certificateRequest.setAccountHash(raAccount.getAccountHash());
				certificateRequest.setCertReqBuf(csr);
				certificateRequest.setCertReqChallenge(USER_PIN);
				Date now = new Date();
				certificateRequest.setCertNotBefore(sdf.format(now));
				certificateRequest.setCertNotAfter(sdf.format(DateUtils.addDays(now, validityDays)));
				certificateResponse = client
						.requestCertificate(certificateRequest);
				certSerialnumber = certificateResponse.getCertSerialNumber();
				String base64Cert = certificateResponse.getCertSignBuf();
				try {
					CertificateFactory cf = CertificateFactory.getInstance("X.509");
					Certificate cert = cf
							.generateCertificate(new ByteArrayInputStream(Base64
									.decodeBase64(base64Cert)));
					System.out.println(cert);
				} catch (CertificateException e) {
					e.printStackTrace();
				}
			} catch (ResponseException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
				
			}
		}else if (raAccount.getRaProtocol().equals("ICA")){
			String certSignBufP7 = null; //自动获取模式
			UserInfo userInfo = new UserInfo();
			userInfo.setUserName(certificateRequest.getUserName());
			userInfo.setUserEmail(certificateRequest.getUserEmail());
			userInfo.setUserCountry("CN");
			userInfo.setCertReqBuffer(reqBuf);
			userInfo.setUserOrganization(certificateRequest.getUserOrganization());
			userInfo.setUserOrgUnit(certificateRequest.getUserOrgunit());
			userInfo.setUserTitle(certificateRequest.getUserTitle());
			userInfo.setUserIp(certificateRequest.getUserIp());
			userInfo.setUserDns(certificateRequest.getUserDns());
			userInfo.setUserAdditionalField1(certificateRequest.getUserAdditionalField1());
			userInfo.setUserAdditionalField2(certificateRequest.getUserAdditionalField2());
			userInfo.setUserAdditionalField3(certificateRequest.getUserAdditionalField3());
			userInfo.setUserAdditionalField4(certificateRequest.getUserAdditionalField4());
			userInfo.setUserAdditionalField5(certificateRequest.getUserAdditionalField5());
			userInfo.setUserAdditionalField6(certificateRequest.getUserAdditionalField6());
			userInfo.setUserAdditionalField7(certificateRequest.getUserAdditionalField7());
			userInfo.setUserAdditionalField8(certificateRequest.getUserAdditionalField8());
			userInfo.setUserAdditionalField9(certificateRequest.getUserAdditionalField9());
			userInfo.setUserAdditionalField10(certificateRequest.getUserAdditionalField10());

			userInfo.setUserSurName(certificateRequest.getUserSurname());

			userInfo.setCertNotBeforeDate(new Date());
			/*System.out.println("CN:"+certificateRequest.getUserName());
			System.out.println("O:"+certificateRequest.getUserOrganization());
			System.out.println("OU:"+certificateRequest.getUserOrgunit());*/
			
			log.debug("CN:"+certificateRequest.getUserName());
			log.debug("O:"+certificateRequest.getUserOrganization());
			log.debug("OU:"+certificateRequest.getUserOrgunit());
			
			
			if (raAccount.getIsSingleCert()!=null&&!raAccount.getIsSingleCert()) {//双证
				userInfo.setKeyMode("ENROLL"); 
				userInfo.setCertKmcReq2("kmcClientVersion=20110501");	// 加密证书申请通道。
			}
			userInfo.setCertReqChallenge(raAccount.getAaPassword()==null||raAccount.getAaPassword().equals("")? PASSWORD:raAccount.getAaPassword());//用户口令
			userInfo.setPasscode(null);//通行码？ 不需要..
			userInfo.setCertReqOverrideValidity(validityDays+""); //设置证书有效期，如果注释，则取证书模板默认有效期

//			RaCertManager raCertManager = RaFactory.getRaCertManager("iTruschina Co., Ltd.",  "Test Service");//(O,OU)
			/*System.out.println("O:"+raAccount.getAccountOrganization());
			System.out.println("OU"+raAccount.getAccountOrgUnit());
			System.out.println("AA密码:"+raAccount.getAaPassword());
			System.out.println("申请天数:"+userInfo.getCertReqOverrideValidity());*/
			
			log.debug("O:"+raAccount.getAccountOrganization());
			log.debug("OU"+raAccount.getAccountOrgUnit());
			log.debug("AA密码:"+raAccount.getAaPassword());
			log.debug("申请天数:"+userInfo.getCertReqOverrideValidity());
			
			
			
			
			RaCertManager raCertManager = RaFactory.getRaCertManager(raAccount.getAccountOrganization(),
					raAccount.getAccountOrgUnit());
			raCertManager.addRaService(raAccount.getServiceUrl());

			OperationResult operationResult = null;
			try {
				operationResult = raCertManager.enrollCert(userInfo);
				if (operationResult instanceof EnrollResult) {
					log.info("申请..ICA未返回证书..");
					EnrollResult enrollResult = (EnrollResult) operationResult;
					System.out.println(enrollResult.getBaseMessage());
					System.out.println(enrollResult.getExtMessage());
					if (enrollResult.isSuccess()) {
						enrollResult.getMessage();
					}else {
					}
				} else if (operationResult instanceof PickupResult) {
					PickupResult pickupResult = (PickupResult) operationResult;
					System.out.println(pickupResult.getBaseMessage());
					if (pickupResult.isSuccess()) {
						//System.out.println("申请证书成功...");
						log.debug("申请证书成功...");
						certificateResponse = transPickupResultToResp(pickupResult, reqBuf, validityDays.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			cn.topca.tca.ra.service.UserInfo userInfo = new cn.topca.tca.ra.service.UserInfo();
			userInfo.setUserName(certificateRequest.getUserName());
			userInfo.setUserEmail(certificateRequest.getUserEmail());
			userInfo.setUserSurname(certificateRequest.getUserSurname());
			userInfo.setUserCountry("CN");
			userInfo.setUserOrganization(certificateRequest.getUserOrganization());
			userInfo.setUserOrgunit(certificateRequest.getUserOrgunit());
			userInfo.setUserTitle(certificateRequest.getUserTitle());
			userInfo.setUserIp(certificateRequest.getUserIp());
			userInfo.setUserDns(certificateRequest.getUserDns());
			userInfo.setUserAdditionalField1(certificateRequest.getUserAdditionalField1());
			userInfo.setUserAdditionalField2(certificateRequest.getUserAdditionalField2());
			userInfo.setUserAdditionalField3(certificateRequest.getUserAdditionalField3());
			userInfo.setUserAdditionalField4(certificateRequest.getUserAdditionalField4());
			userInfo.setUserAdditionalField5(certificateRequest.getUserAdditionalField5());
			userInfo.setUserAdditionalField6(certificateRequest.getUserAdditionalField6());
			userInfo.setUserAdditionalField7(certificateRequest.getUserAdditionalField7());
			userInfo.setUserAdditionalField8(certificateRequest.getUserAdditionalField8());
			userInfo.setUserAdditionalField9(certificateRequest.getUserAdditionalField9());
			userInfo.setUserAdditionalField10(certificateRequest.getUserAdditionalField10());
			
			try {
				JSONObject json = new JSONObject();
				json.put("certValidity",validityDays+"");
				
				UserAPIService ss = new UserAPIService(new URL(raAccount.getServiceUrl()));
				cn.topca.tca.ra.service.CertInfo certInfo = ss.getUserAPIServicePort().enrollCertAA(userInfo, reqBuf, raAccount.getAccountHash(), raAccount.getAaPassword(), null, json.toString());
				certificateResponse = transCertInfoToResp(certInfo, reqBuf, validityDays.toString());
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RaServiceUnavailable_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* revoke certificate action */
		return certificateResponse;
	}

//	public static String getCertSn4ICA(String sn) {
//		sn = sn.toUpperCase();
//		int len = sn.length();
//		if (len%2==0) {//偶数长度
//			String first = sn.substring(0, 1);
//			if (first.compareTo("7") > 0) {
//				return "00" + sn;
//			} else {
//				return sn;
//			}
//		}else {//奇数长度
//			return "0"+sn;
//		}
//	}
//	
//	public static String getOldCertSn4ICA(String sn) {
//		sn = sn.toUpperCase();
//		String second = sn.substring(0, 2);
//		String first = sn.substring(0,1);
//		if (second.equals("00")) {
//			return sn.substring(2, sn.length());
//		} else {
//			return sn;
//		}
//	}
	
	/**
	 * ICA方式返回的结果转换成TCA方式的结果
	 * 注意 ICA的日期格式是 yyyy-MM-dd HH:mm:ss
	 * TCA的格式是 yyyyMMddHHmmss
	 * @param result
	 * @param csr
	 * @param day
	 * @return
	 */
	public CertificateResponse transPickupResultToResp(PickupResult result,String csr,String day){
		String serialNum = result.getCertSerialNumber();
		String serialNumKmc = result.getCertSerialNumberKmc();
		if (serialNum!=null) {
			serialNum = getIEValidSerialNumber(serialNum).toUpperCase();
		}
		if (serialNumKmc!=null) {
			serialNumKmc = getIEValidSerialNumber(serialNumKmc).toUpperCase();
		}
		result.setCertSerialNumber(serialNum);
		result.setCertSerialNumberKmc(serialNumKmc);
		
		CertificateResponse response = new CertificateResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		response.setCertApproveDate(sdf.format(new Date()));
		response.setCertIssuerDn(result.getCertIssuerDN());
		response.setCertIssuerHashMd5(result.getCertIssuerHashMD5());
		response.setCertNotAfter(sdf.format(result.getCertNotAfter()));
		response.setCertNotBefore(sdf.format(result.getCertNotBefore()));
		response.setCertReqBuf(csr);
		response.setCertReqDate(sdf.format(new Date()));
		response.setCertReqOverrideValidity(day);
		response.setCertSerialNumber(result.getCertSerialNumber());
		response.setCertSignBuf(result.getCertSignBuf());
		response.setCertSignBufP7(result.getCertSignBufP7());
		response.setCertSignDate(sdf.format(result.getCertSignDate()));
		response.setCertSubjectDn(result.getCertSubjectDN());
		response.setCertSubjectHashMd5(result.getCertSubjectHashMD5());
		//双证
		response.setCertKmcRep1(result.getCertKmcRep1());
		response.setCertKmcRep2(result.getCertKmcRep2());
		response.setCertKmcRep3(result.getCertKmcRep3());
		response.setCertSignBufKmc(result.getCertKmcRep2());
		response.setInstallMode("");//ICA使用installCertKmc 第二种方式
		response.setCertSerialnumberKmc(result.getCertSerialNumberKmc());
		
		System.out.println("证书序列号:"+response.getCertSerialNumber());
		System.out.println("证书有效期:"+response.getCertNotBefore()+"===="+response.getCertNotAfter());
		System.out.println("subjectdn"+response.getCertSubjectDn());
		System.out.println("issuerDn"+response.getCertIssuerDn());
		System.out.println("signBuf:"+response.getCertSignBuf());
		System.out.println("加密证书序列号（双证时存在）:"+result.getCertSerialNumberKmc());
//		response.setInstallMode(result.getcert);
		
		return response;
	}
	
	public CertificateResponse transCertInfoToResp(cn.topca.tca.ra.service.CertInfo certInfo,String csr,String day){
		String serialNum = certInfo.getCertSerialNumber();
		String serialNumKmc = certInfo.getCertSerialnumberKmc();
		if (serialNum!=null) {
			serialNum = getIEValidSerialNumber(serialNum).toUpperCase();
		}
		if (serialNumKmc!=null) {
			serialNumKmc = getIEValidSerialNumber(serialNumKmc).toUpperCase();
		}
		certInfo.setCertSerialNumber(serialNum);
		certInfo.setCertSerialnumberKmc(serialNumKmc);
		
		CertificateResponse response = new CertificateResponse();
		response.setCertApproveDate(certInfo.getCertApproveDate());
		response.setCertIssuerDn(certInfo.getCertIssuerDn());
		response.setCertIssuerHashMd5(certInfo.getCertIssuerHashMd5());
		response.setCertNotAfter(certInfo.getCertNotAfter());
		response.setCertNotBefore(certInfo.getCertNotBefore());
		response.setCertReqBuf(csr);
		response.setCertReqDate(certInfo.getCertReqDate());
		response.setCertReqOverrideValidity(day);
		response.setCertSerialNumber(certInfo.getCertSerialNumber());
		response.setCertSignBuf(certInfo.getCertSignBuf());
		response.setCertSignBufP7(certInfo.getCertSignBufP7());
		response.setCertSignDate(certInfo.getCertSignDate());
		response.setCertSubjectDn(certInfo.getCertSubjectDn());
		response.setCertSubjectHashMd5(certInfo.getCertSubjectHashMd5());
		//双证
		response.setCertKmcRep1(certInfo.getCertKmcRep1());
		response.setCertKmcRep2(certInfo.getCertKmcRep2());
		response.setCertKmcRep3(certInfo.getCertKmcRep3());
		response.setCertSignBufKmc(certInfo.getCertKmcRep2());
		response.setInstallMode("");//ICA使用installCertKmc 第二种方式
		response.setCertSerialnumberKmc(certInfo.getCertSerialnumberKmc());
		
		System.out.println("证书序列号:"+response.getCertSerialNumber());
		System.out.println("证书有效期:"+response.getCertNotBefore()+"===="+response.getCertNotAfter());
		System.out.println("subjectdn"+response.getCertSubjectDn());
		System.out.println("issuerDn"+response.getCertIssuerDn());
		System.out.println("signBuf:"+response.getCertSignBuf());
		System.out.println("加密证书序列号（双证时存在）:"+certInfo.getCertSerialnumberKmc());
		
		return response;
	}
	
	/**
	 * 吊销证书
	 * 
	 * @return
	 */
	public boolean revokeCaCert(String certSn,String reason,ConfigRaAccount raAccount) throws Exception{
		log.info("请求CA吊销证书");
		
		if (raAccount.getRaProtocol().equals("TCA")) {
			/* init properties */
			String connection = "ConnectionPool"; // 按实际情况实现连接服务
			try {
				/* setup ca client */
				CAClient client = CAClient.getInstance(connection, raAccount.getServiceUrl());
				RevokeRequest revokeRequest = new RevokeRequest();
				revokeRequest.setAccountHash(raAccount.getAccountHash());
				revokeRequest.setCertSerialNumber(certSn);
				revokeRequest.setCertRevokeReason(reason==null? RevokeRequest.certificateHold:reason);
				revokeRequest.setCertReqChallenge(USER_PIN);
				RevokeResponse revokeResponse = client.revokeCertificate(revokeRequest);
				System.out.println("Revoke Cert:");
				System.out.println(revokeResponse.getCertSerialNumber());
				System.out.println(revokeResponse.getCertRevokeReason());
				System.out.println(revokeResponse.getCertRevokeDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if(raAccount.getRaProtocol().equals("ICA")) {
			RevokeResult revokeResult = null;
			
			RaCertManager raCertManager = RaFactory.getRaCertManager(raAccount.getAccountOrganization(),
					raAccount.getAccountOrgUnit());
			raCertManager.addRaService(raAccount.getServiceUrl());
			certSn = getICAValidSerialNumber(certSn).toUpperCase();
			try {
				String challenge = raAccount.getAaPassword();
				
				revokeResult = raCertManager.revokeCert(certSn,
				(challenge!=null && !challenge.isEmpty())? challenge:PASSWORD, 
				(reason!=null && !reason.isEmpty())? reason:RevokeRequest.certificateHold);
				System.out.println(revokeResult.getBaseMessage());
				System.out.println(revokeResult.getExtMessage());
			} catch (RaServiceUnavailable e) {
				return false;
			}

			if (revokeResult != null && revokeResult.isSuccess()) {
				if (revokeResult.isSuccess()) {
					return true;
				} else {
					return false;
				}
			}
		}else{
			
			try {
				UserAPIService ss = new UserAPIService(new URL(raAccount.getServiceUrl()));
				ss.getUserAPIServicePort().revokeCert(certSn, raAccount.getAaPassword(), (reason!=null && !reason.isEmpty())? reason:RevokeRequest.certificateHold, raAccount.getAccountHash(), null);

			} catch (RaServiceUnavailable_Exception e) {
				e.printStackTrace();
			}
			
		}
		return true;
	}

//	static final String CA_URL = "http://localhost:8085/TopCA/crs/crs";
//	static final String RA_ACCOUNT = "059A1782E816F71212AE704AA2F1EFE6"; // ra账户
	
	static final String RA_NATIVE_CERT = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDZjCCAs+gAwIBAgIUZ9WqTGmrrnpZwLdA+CNhnyyYcbQwDQYJKoZIhvcNAQEF\n"
			+ "BQAwUzEaMBgGA1UEAwwR6K6+5aSH6K+B5Lmm5a2QQ0ExGDAWBgNVBAsMD+a1i+iv\n"
			+ "lemDqOivleeUqDEbMBkGA1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMB4XDTE0MDQy\n"
			+ "NjAxMzcwMVoXDTE1MDQyNjAxMzcwMVowajEXMBUGA1UEAwwOUkHorr7lpIfor4Hk\n"
			+ "uaYxFTATBgkqhkiG9w0BCQEWBnRAdC5jbjELMAkGA1UECwwCUkExDTALBgNVBAoM\n"
			+ "BFRlc3QxCzAJBgNVBAYTAkNOMQ8wDQYHYIEcAQYKAQwCQUEwgZ8wDQYJKoZIhvcN\n"
			+ "AQEBBQADgY0AMIGJAoGBAJtIel0eGY/FHTRmt9CNvL9sQDl4njSH26qjMTNVQjyQ\n"
			+ "SnQjSaBBNwHEyuAdWE3JzVBGE2l0tzbpAkXo5H04BGMFTuv5q5PcbD5WzGM5gvk0\n"
			+ "Cq/vit3ooNwpJFTVYNxmYn0TMVFitrNey94OI93rS4yy78xI80mJIRXaz5CsVeFZ\n"
			+ "AgMBAAGjggEeMIIBGjAdBgNVHQ4EFgQUAmjm6pAW+w2650fwE7tmawZf6lowHwYD\n"
			+ "VR0jBBgwFoAUIRruKrfLhkggPVbBr2P3OZ7FF8MwCQYDVR0TBAIwADAOBgNVHQ8B\n"
			+ "Af8EBAMCBsAwSgYIKwYBBQUHAQEEPjA8MDoGCCsGAQUFBzABhi5odHRwOi8vWW91\n"
			+ "cl9TZXJ2ZXJfTmFtZTpQb3J0L1RvcENBL2xvZHBfQmFzZUROMHEGA1UdHwEB/wRn\n"
			+ "MGUwY6BhoF+GXWh0dHA6Ly8xOTIuMTY4LjE0NS4xNzY6ODA4MC9Ub3BDQS9wdWJs\n"
			+ "aWMvaXRydXNjcmw/Q0E9NjExQ0UyNTQ3MTBEMzFFNDUzM0Y0QkMyRTVCMjVBQjkx\n"
			+ "NDBFNjFGNDANBgkqhkiG9w0BAQUFAAOBgQAG5SEbmtxKXNt8FZ+GUB88t5BxFEll\n"
			+ "vryXpP8wLpy8NuQk/f67NQfLYRpoGQMepWehB48T/+pOGkoHBAxy9ndjTpJesY7H\n"
			+ "6txqY5+l+G73Lp5eZqP8CdjDwa0elRMpmsPnV1c0qOnxyquE0zzDfuOblBdoT3bg\n"
			+ "jJboQzJUST7WOw==\n" + "-----END CERTIFICATE-----";

	static final String CA_RECIPIENT_CERT = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDbDCCAtWgAwIBAgIULkIGrHPdqKXHAIx6bDFhSBQsdtUwDQYJKoZIhvcNAQEF\n"
			+ "BQAwUzEaMBgGA1UEAwwR6K6+5aSH6K+B5Lmm5a2QQ0ExGDAWBgNVBAsMD+a1i+iv\n"
			+ "lemDqOivleeUqDEbMBkGA1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMB4XDTE0MDQy\n"
			+ "NTA2MDcwM1oXDTE1MDQyNTA2MDcwM1owcDEPMA0GA1UEAwwGVGVzdEFBMRUwEwYJ\n"
			+ "KoZIhvcNAQkBFgZ0QHQuY24xGDAWBgNVBAsMD+a1i+ivlemDqOivleeUqDEbMBkG\n"
			+ "A1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMQ8wDQYHYIEcAQYKAQwCQUEwgZ8wDQYJ\n"
			+ "KoZIhvcNAQEBBQADgY0AMIGJAoGBAIFikIsr5D9zgNiI4xWHRPjf1UdQwrV98R9t\n"
			+ "T0q6DGKydgjDW2E3xjY8Qaa9vOobS+gKjD8WD7Bv4pFwBBmqPy6M3sRE7N4kalvw\n"
			+ "mEeNaqYxCYoLACrHawxXI8PV6DIgAsSUYD9B2rM3zuX/UHBN+2ePo4JrrhNC/QGr\n"
			+ "e1AWji4LAgMBAAGjggEeMIIBGjAdBgNVHQ4EFgQUA38QzECFabEub9Hhx+H9grE1\n"
			+ "eaMwHwYDVR0jBBgwFoAUIRruKrfLhkggPVbBr2P3OZ7FF8MwCQYDVR0TBAIwADAO\n"
			+ "BgNVHQ8BAf8EBAMCBsAwSgYIKwYBBQUHAQEEPjA8MDoGCCsGAQUFBzABhi5odHRw\n"
			+ "Oi8vWW91cl9TZXJ2ZXJfTmFtZTpQb3J0L1RvcENBL2xvZHBfQmFzZUROMHEGA1Ud\n"
			+ "HwEB/wRnMGUwY6BhoF+GXWh0dHA6Ly8xOTIuMTY4LjE0NS4xNzY6ODA4MC9Ub3BD\n"
			+ "QS9wdWJsaWMvaXRydXNjcmw/Q0E9NjExQ0UyNTQ3MTBEMzFFNDUzM0Y0QkMyRTVC\n"
			+ "MjVBQjkxNDBFNjFGNDANBgkqhkiG9w0BAQUFAAOBgQCYw+bCxjCm5cWnYxYCHo+w\n"
			+ "3fz4JZeUlpi7UmMcL/T+RmZwc8yjIhPR8nugc3ANhu12ks4wacq86yzN6FZ4nF8a\n"
			+ "eJtZEDDRwXNih/FtSDcg5yS+wo4aEWLsxqus99MrdQ/WQSeIz21Zp7hlnb5xcxYY\n"
			+ "CA7m6psc2kZC4No099HErw==\n" + "-----END CERTIFICATE-----";

	static final String RA_NATIVE_PKEY = "-----BEGIN RSA PRIVATE KEY-----\n"
			+ "Proc-Type: 4,ENCRYPTED\n"
			+ "DEK-Info: AES-128-CBC,b1e4a0e820352452d5ecbebfae16a59a\n"
			+ "\n"
			+ "Xx9eGvCWD93Dpa8hQ6rFk0AmjAb8a+0kq0T0uLE+7AwiQrhcjUKA8vxlc3o8doUZ\n"
			+ "eXMAoKSmTg97bGzHS+UnHK5USZHbVlmGTN8dGb9B/d4aJkIwz8CcHpyqOlCENJQv\n"
			+ "6Vldn9MqkZ0ju0KLBotzGaV8S0qmt52i4nV2JEGzSuDDjL5WYDUDLdVVOE0LHwQI\n"
			+ "Xc5a+sTkBFJuYjj/N+y+q58xHLD1Zrldh+jIQqu3l9ADjJRgQSKlqOFOCZpnV4bZ\n"
			+ "JotISqsMMG2/HJ9PyI6fMgFFbsKOJVPKuMTIN8BUdHBIJTbbDSiRYL0dGRypTfbe\n"
			+ "voSxczIiF+WXHMnfgUfUP1cO3p8rm/s4B37E12n0OIKRZwA2S9Ye8L2R/Jefwd/k\n"
			+ "6LTXLJxldKgLpsvCiCqq6okvRmmy/sUgkDmOsmjLwVLvx1NOW3v1q5356zy7xofk\n"
			+ "GlkqlpkuI25P/3jVVRvIoZQV/rkbHD0/TWHHUBefcH8NQ40jwIPTequhSSflLWal\n"
			+ "1meVB08//skS7wjXE7wKHVIIfokt6Y2SOTt6wM2Cj4ThFh1TJLji+ZhXmnQSUlyn\n"
			+ "OTC/ZCTnOvr/qEy3rqjWNluMFCbC3mqRzHMkDkHW4l3hTfWR2xiGPYKw4AeXuY6R\n"
			+ "x5ziJk5dTHX+66IfgTcAnx462Z7FqFz7dxaRKw7kdtMLDcYnx69PtpLtCbU1J2+z\n"
			+ "0rs/tGxWbpzFnxVLk2XPbxtlnMeMKv+mIhO/PbMCBeUx2wMICXihxdIjMXCsH8ub\n"
			+ "OGB2kkoerKJ5sH1q1UKEulg1B1mTdKVnnymtMtMXpKgsYzCFZqjDBQ5dfklT6vTj\n"
			+ "-----END RSA PRIVATE KEY-----";

	static final String USER_PIN = "password";//ra私钥口令
	static final String PASSWORD = "itrusyes";//向ICA ra服务提交申请时
	
	
	static final String testReq = new String("MIIBWTCBwwIBADAaMRgwFgYDVQQDEw9DTj1pdHJ1c19lbnJvbGwwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAOlRtl2Poun2NNhuN1rwZQjn/zLIQuNkwETbgTwlL4TuJ5briZOD27wVKiDxQVou0bn6yeoheR6kualz+fBsRpgSocF68GgukMlYF7Leo7rGmfrN4JEtWZcxWdqhSe+UKp8fmTO3T2Ih3O0UJPRC1MjRlJgtMl3HqYmz1+6VjvOpAgMBAAGgADANBgkqhkiG9w0BAQUFAAOBgQBz9RJE8UPHKlK9Yd4U+fIxfNPZpzOo7MGdjuyYx3JMs+zF4LBRRdy5WxVl9VKhoGGQc+cocxXvnC4rt8tz7UFzyMc5pvMMywlUNYbc1jF2HZp+ZppatQQzFbknfadim9WcxFoJQ3VI2y+4n6O8kyMHsWr4wdHYcgE4z2d8sV+SYA== ");
//	public static void main(String[] args) {
////		CertificateResponse res = new CaService().getCaCert("测试", "610039879@qq.com", testReq,365,new RaAccount());
////		System.out.println(res.getCertSerialNumber());
//	}
	
	
	/**
	 * 转换为Windows格式序列号
	 * 追加00等
	 * @param serialNumber
	 * @return String 16进制字符串
	 */
	public static String getIEValidSerialNumber(String serialNumber) {
		if (serialNumber == null)
			return null;
		if (serialNumber.length() == 0) {
			return serialNumber;
		} else {
			serialNumber = serialNumber.toLowerCase();
			if (serialNumber.length() % 2 == 1)// java有可能把第一个0去掉，不可能为单数
				serialNumber = "0" + serialNumber;
			String firstWord = serialNumber.substring(0, 1);
			int firstNumber = 0;
			try {
				firstNumber = Integer.parseInt(firstWord, 16);
			} catch (Exception e) {
				return serialNumber;
			}
			if (firstNumber >= 8) {
				return "00" + serialNumber;
			} else {
				return serialNumber;
			}
		}
	}

	/**
	 * 转换为iTrusCA识别的序列号
	 * 去掉00等
	 * @param serialNumber
	 * @return String 16进制字符串
	 */
	public static String getICAValidSerialNumber(String serialNumber) {
		if (serialNumber == null)
			return null;
		if (serialNumber.length() % 2 == 1)// java有可能把第一个0去掉，不可能为单数
			serialNumber = "0" + serialNumber;
		if (serialNumber.startsWith("00")) {
			return serialNumber.substring(2).toUpperCase();
		} else {
			return serialNumber.toUpperCase();
		}
	}
	/**
	 * GBK转UTF-8
	 * @date:2014年9月4日
	 * @user:京涛
	 * @return_type:String
	 */
	public static String transGBK2UTF8(String base){
		if (base==null) {
			return null;
		}
		try {
			return new String(base.getBytes("utf-8"), "gbk");
		} catch (Exception e) {
			return base;
		}
	}
	
	public static String transUTF82GBK(String base){
		if (base==null) {
			return null;
		}
		try {
			return new String(base.getBytes("gbk"), "utf-8");
		} catch (Exception e) {
			return base;
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(transGBK2UTF8("GBK缂栫爜"));
			System.out.println(transUTF82GBK("UTF-8Ҡë"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
