package cn.topca.tca.client;

import cn.topca.connection.Message;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 证书颁发响应数据
 * 
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 12:53
 */
public class CertificateResponse implements Message {

	private String certType;
	private String certSignBuf;
	private String certStatus;
	private String certSerialNumber;
	private String certIssuerDn;
	private String certIssuerHashMd5;
	private String certNotAfter;
	private String certNotBefore;
	private String certReqBuf;
	private String certReqBufType;
	private String certReqDate;
	private String certReqOverrideValidity;
	private String certSignDate;
	private String certApproveDate;
	private String certSubjectDn;
	private String certSubjectHashMd5;
	private String certSignBufP7;
	private String certReqPublickey;
	private String certReqPublickeyHashMd5;

	// 双证书
	private String certKmcRep1;
	private String certKmcRep2;
	private String certKmcRep3;
	private String certSignBufKmc;
	private String certTypeKmc;
	private String certReqBufKmc;
	private String certReqBufTypeKmc;
	private String certSerialnumberKmc;
	private String installMode;

	// status

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertSignBuf() {
		return certSignBuf;
	}

	public void setCertSignBuf(String certSignBuf) {
		this.certSignBuf = certSignBuf;
	}

	public String getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(String certStatus) {
		this.certStatus = certStatus;
	}

	public String getCertSerialNumber() {
		return certSerialNumber;
	}

	public void setCertSerialNumber(String certSerialNumber) {
		this.certSerialNumber = certSerialNumber;
	}

	public String getCertIssuerDn() {
		return certIssuerDn;
	}

	public void setCertIssuerDn(String certIssuerDn) {
		this.certIssuerDn = certIssuerDn;
	}

	public String getCertIssuerHashMd5() {
		return certIssuerHashMd5;
	}

	public void setCertIssuerHashMd5(String certIssuerHashMd5) {
		this.certIssuerHashMd5 = certIssuerHashMd5;
	}

	public String getCertNotAfter() {
		return certNotAfter;
	}

	public void setCertNotAfter(String certNotAfter) {
		this.certNotAfter = certNotAfter;
	}

	public String getCertNotBefore() {
		return certNotBefore;
	}

	public void setCertNotBefore(String certNotBefore) {
		this.certNotBefore = certNotBefore;
	}

	public String getCertReqBuf() {
		return certReqBuf;
	}

	public void setCertReqBuf(String certReqBuf) {
		this.certReqBuf = certReqBuf;
	}

	public String getCertReqBufType() {
		return certReqBufType;
	}

	public void setCertReqBufType(String certReqBufType) {
		this.certReqBufType = certReqBufType;
	}

	public String getCertReqDate() {
		return certReqDate;
	}

	public void setCertReqDate(String certReqDate) {
		this.certReqDate = certReqDate;
	}

	public String getCertReqOverrideValidity() {
		return certReqOverrideValidity;
	}

	public void setCertReqOverrideValidity(String certReqOverrideValidity) {
		this.certReqOverrideValidity = certReqOverrideValidity;
	}

	public String getCertSignDate() {
		return certSignDate;
	}

	public void setCertSignDate(String certSignDate) {
		this.certSignDate = certSignDate;
	}

	public String getCertApproveDate() {
		return certApproveDate;
	}

	public void setCertApproveDate(String certApproveDate) {
		this.certApproveDate = certApproveDate;
	}

	public String getCertSubjectDn() {
		return certSubjectDn;
	}

	public void setCertSubjectDn(String certSubjectDn) {
		this.certSubjectDn = certSubjectDn;
	}

	public String getCertSubjectHashMd5() {
		return certSubjectHashMd5;
	}

	public void setCertSubjectHashMd5(String certSubjectHashMd5) {
		this.certSubjectHashMd5 = certSubjectHashMd5;
	}

	public String getCertSignBufP7() {
		return certSignBufP7;
	}

	public void setCertSignBufP7(String certSignBufP7) {
		this.certSignBufP7 = certSignBufP7;
	}

	public String getCertReqPublickey() {
		return certReqPublickey;
	}

	public void setCertReqPublickey(String certReqPublickey) {
		this.certReqPublickey = certReqPublickey;
	}

	public String getCertReqPublickeyHashMd5() {
		return certReqPublickeyHashMd5;
	}

	public void setCertReqPublickeyHashMd5(String certReqPublickeyHashMd5) {
		this.certReqPublickeyHashMd5 = certReqPublickeyHashMd5;
	}

	public String getCertKmcRep1() {
		return certKmcRep1;
	}

	public void setCertKmcRep1(String certKmcRep1) {
		this.certKmcRep1 = certKmcRep1;
	}

	public String getCertKmcRep2() {
		return certKmcRep2;
	}

	public void setCertKmcRep2(String certKmcRep2) {
		this.certKmcRep2 = certKmcRep2;
	}

	public String getCertKmcRep3() {
		return certKmcRep3;
	}

	public void setCertKmcRep3(String certKmcRep3) {
		this.certKmcRep3 = certKmcRep3;
	}

	public String getCertSignBufKmc() {
		return certSignBufKmc;
	}

	public void setCertSignBufKmc(String certSignBufKmc) {
		this.certSignBufKmc = certSignBufKmc;
	}

	public String getCertTypeKmc() {
		return certTypeKmc;
	}

	public void setCertTypeKmc(String certTypeKmc) {
		this.certTypeKmc = certTypeKmc;
	}

	public String getCertReqBufKmc() {
		return certReqBufKmc;
	}

	public void setCertReqBufKmc(String certReqBufKmc) {
		this.certReqBufKmc = certReqBufKmc;
	}

	public String getCertReqBufTypeKmc() {
		return certReqBufTypeKmc;
	}

	public void setCertReqBufTypeKmc(String certReqBufTypeKmc) {
		this.certReqBufTypeKmc = certReqBufTypeKmc;
	}

	public String getCertSerialnumberKmc() {
		return certSerialnumberKmc;
	}

	public void setCertSerialnumberKmc(String certSerialnumberKmc) {
		this.certSerialnumberKmc = certSerialnumberKmc;
	}

	public String getInstallMode() {
		return installMode;
	}

	public void setInstallMode(String installMode) {
		this.installMode = installMode;
	}
}
