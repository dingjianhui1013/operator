package com.itrus.ca.modules.work.vo;

import com.itrus.ca.common.utils.excel.annotation.ExcelField;

public class WorkDealInfoVo {
	@ExcelField(title = "业务编号", align = 2, sort = 0)
	private String svn;
	@ExcelField(title = "应用名称", align = 2, sort = 1)
	private String appName;
	@ExcelField(title = "别名", align = 2, sort = 2)
	private String alias;
	@ExcelField(title = "单位名称", align = 2, sort = 3)
	private String companyName;
	@ExcelField(title = "证书持有人", align = 2, sort = 4)
	private String contactName;
	@ExcelField(title = "经办人姓名", align = 2, sort = 5)
	private String certApplyInfoName;
	@ExcelField(title = "经办人手机号", align = 2, sort = 6)
	private String certApplyInfoPhone;
	@ExcelField(title = "经办人邮箱", align = 2, sort = 7)
	private String certApplyInfoEmail;
	@ExcelField(title = "产品标识", align = 2, sort = 8)
	private String productLabel;
	@ExcelField(title = "产品名称", align = 2, sort = 9)
	private String productName;
	@ExcelField(title = "业务类型", align = 2, sort = 10)
	private String dealInfoType;
	@ExcelField(title = "key编码", align = 2, sort = 11)
	private String keySn;
	@ExcelField(title = "收费日期", align = 2, sort = 12)
	private String payDate;
	@ExcelField(title = "制证日期", align = 2, sort = 13)
	private String signDateString;
	@ExcelField(title = "有效期", align = 2, sort = 14)
	private String certDays;
	@ExcelField(title = "到期日期", align = 2, sort = 15)
	private String notAfter;
	@ExcelField(title = "业务状态", align = 2, sort = 16)
	private String dealInfoStatus;
	@ExcelField(title = "鉴证人", align = 2, sort = 17)
	private String attestAtionUserName;
	@ExcelField(title = "录入人", align = 2, sort = 18)
	private String inputUserName;
	@ExcelField(title = "收费人", align = 2, sort = 19)
	private String payUserName;
	@ExcelField(title = "制证人", align = 2, sort = 20)
	private String businessCardUserName;
	@ExcelField(title = "录入日期", align = 2, sort = 21)
	private String inputDate;
	@ExcelField(title = "行政所属区", align = 2, sort = 22)
	private String areaName;
	@ExcelField(title = "计费策略类型", align = 2, sort = 23)
	private String tempStyle;
	@ExcelField(title = "计费策略模板", align = 2, sort = 24)
	private String tempName;
	@ExcelField(title = "付费方式", align = 2, sort = 25)
	private String payType;
	@ExcelField(title = "收费金额", align = 2, sort = 26)
	private String totalMoney;
	@ExcelField(title = "省份", align = 2, sort = 25)
	private String province;
	@ExcelField(title = "地级市", align = 2, sort = 25)
	private String city;
	@ExcelField(title = "市、县级市", align = 2, sort = 25)
	private String county;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getProductLabel() {
		return productLabel;
	}

	public void setProductLabel(String productLabel) {
		this.productLabel = productLabel;
	}

	public String getCertApplyInfoEmail() {
		return certApplyInfoEmail;
	}

	public void setCertApplyInfoEmail(String certApplyInfoEmail) {
		this.certApplyInfoEmail = certApplyInfoEmail;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getBusinessCardUserName() {
		return businessCardUserName;
	}

	public void setBusinessCardUserName(String businessCardUserName) {
		this.businessCardUserName = businessCardUserName;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getPayUserName() {
		return payUserName;
	}

	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getTempStyle() {
		return tempStyle;
	}

	public void setTempStyle(String tempStyle) {
		this.tempStyle = tempStyle;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getCertApplyInfoPhone() {
		return certApplyInfoPhone;
	}

	public void setCertApplyInfoPhone(String certApplyInfoPhone) {
		this.certApplyInfoPhone = certApplyInfoPhone;
	}

	public String getAttestAtionUserName() {
		return attestAtionUserName;
	}

	public void setAttestAtionUserName(String attestAtionUserName) {
		this.attestAtionUserName = attestAtionUserName;
	}

	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCertApplyInfoName() {
		return certApplyInfoName;
	}

	public void setCertApplyInfoName(String certApplyInfoName) {
		this.certApplyInfoName = certApplyInfoName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDealInfoType() {
		return dealInfoType;
	}

	public void setDealInfoType(String dealInfoType) {
		this.dealInfoType = dealInfoType;
	}

	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	public String getSignDateString() {
		return signDateString;
	}

	public void setSignDateString(String signDateString) {
		this.signDateString = signDateString;
	}

	public String getCertDays() {
		return certDays;
	}

	public void setCertDays(String certDays) {
		this.certDays = certDays;
	}

	public String getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(String notAfter) {
		this.notAfter = notAfter;
	}

	public String getDealInfoStatus() {
		return dealInfoStatus;
	}

	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

}
