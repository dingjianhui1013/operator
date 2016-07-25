package com.itrus.ca.modules.work.vo;

import com.itrus.ca.common.utils.excel.annotation.ExcelField;

public class WorkDealInfoVo {
	private String svn;
	private String alias;
	private String companyName;
	private String contactName;
	private String certApplyInfoName;
	private String productName;
	private String dealInfoType;
	private String keySn;
	private String signDateString;
	private String certDays;
	private String notAfter;
	private String dealInfoStatus;
	private String attestAtionUserName;
	private String certApplyInfoPhone;
	private String inputUserName;
	private String appName;
	private String areaName;
	private String tempStyle;
	private String totalMoney;
	private String payUserName;
	private String inputDate;
	private String attestAtionUserDate;
	private String tempName;
	private String businessCardUserName;
	private String payDate;
	private String certApplyInfoEmail;
	private String productLabel;
	private String payType;

	@ExcelField(title = "付费方式", align = 2)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@ExcelField(title = "产品标识", align = 2)
	public String getProductLabel() {
		return productLabel;
	}

	public void setProductLabel(String productLabel) {
		this.productLabel = productLabel;
	}

	@ExcelField(title = "经办人邮箱", align = 2)
	public String getCertApplyInfoEmail() {
		return certApplyInfoEmail;
	}

	public void setCertApplyInfoEmail(String certApplyInfoEmail) {
		this.certApplyInfoEmail = certApplyInfoEmail;
	}

	@ExcelField(title = "收费日期", align = 2)
	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	@ExcelField(title = "制证人", align = 2)
	public String getBusinessCardUserName() {
		return businessCardUserName;
	}

	public void setBusinessCardUserName(String businessCardUserName) {
		this.businessCardUserName = businessCardUserName;
	}

	@ExcelField(title = "计费策略模板", align = 2)
	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	@ExcelField(title = "鉴定日期", align = 2)
	public String getAttestAtionUserDate() {
		return attestAtionUserDate;
	}

	public void setAttestAtionUserDate(String attestAtionUserDate) {
		this.attestAtionUserDate = attestAtionUserDate;
	}

	@ExcelField(title = "录入日期", align = 2)
	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	@ExcelField(title = "收费人", align = 2)
	public String getPayUserName() {
		return payUserName;
	}

	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}

	@ExcelField(title = "收费金额", align = 2)
	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	@ExcelField(title = "计费策略类型", align = 2)
	public String getTempStyle() {
		return tempStyle;
	}

	public void setTempStyle(String tempStyle) {
		this.tempStyle = tempStyle;
	}

	@ExcelField(title = "应用名称", align = 2)
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@ExcelField(title = "录入人", align = 2)
	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	@ExcelField(title = "经办人手机号", align = 2)
	public String getCertApplyInfoPhone() {
		return certApplyInfoPhone;
	}

	public void setCertApplyInfoPhone(String certApplyInfoPhone) {
		this.certApplyInfoPhone = certApplyInfoPhone;
	}

	@ExcelField(title = "鉴证人", align = 2)
	public String getAttestAtionUserName() {
		return attestAtionUserName;
	}

	public void setAttestAtionUserName(String attestAtionUserName) {
		this.attestAtionUserName = attestAtionUserName;
	}

	@ExcelField(title = "业务编号", align = 2)
	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	@ExcelField(title = "别名", align = 2)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@ExcelField(title = "单位名称", align = 2)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@ExcelField(title = "证书持有人", align = 2)
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@ExcelField(title = "经办人姓名", align = 2)
	public String getCertApplyInfoName() {
		return certApplyInfoName;
	}

	public void setCertApplyInfoName(String certApplyInfoName) {
		this.certApplyInfoName = certApplyInfoName;
	}

	@ExcelField(title = "产品名称", align = 2)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@ExcelField(title = "业务类型", align = 2)
	public String getDealInfoType() {
		return dealInfoType;
	}

	public void setDealInfoType(String dealInfoType) {
		this.dealInfoType = dealInfoType;
	}

	@ExcelField(title = "key编码", align = 2)
	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@ExcelField(title = "制证日期", align = 2)
	public String getSignDateString() {
		return signDateString;
	}

	public void setSignDateString(String signDateString) {
		this.signDateString = signDateString;
	}

	@ExcelField(title = "有效期", align = 2)
	public String getCertDays() {
		return certDays;
	}

	public void setCertDays(String certDays) {
		this.certDays = certDays;
	}

	@ExcelField(title = "到期日期", align = 2)
	public String getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(String notAfter) {
		this.notAfter = notAfter;
	}

	@ExcelField(title = "业务状态", align = 2)
	public String getDealInfoStatus() {
		return dealInfoStatus;
	}

	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}

	@ExcelField(title = "行政所属区", align = 2)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
