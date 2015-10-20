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
	
	@ExcelField(title="业务编号", align=2)
	public String getSvn() {
		return svn;
	}
	public void setSvn(String svn) {
		this.svn = svn;
	}
	
	@ExcelField(title="别名", align=2)
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@ExcelField(title="单位名称", align=2)
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@ExcelField(title="证书持有人", align=2)
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@ExcelField(title="经办人", align=2)
	public String getCertApplyInfoName() {
		return certApplyInfoName;
	}
	public void setCertApplyInfoName(String certApplyInfoName) {
		this.certApplyInfoName = certApplyInfoName;
	}
	
	@ExcelField(title="产品名称", align=2)
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@ExcelField(title="业务类型", align=2)
	public String getDealInfoType() {
		return dealInfoType;
	}
	public void setDealInfoType(String dealInfoType) {
		this.dealInfoType = dealInfoType;
	}
	
	@ExcelField(title="key编码", align=2)
	public String getKeySn() {
		return keySn;
	}
	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}
	
	@ExcelField(title="制证日期", align=2)
	public String getSignDateString() {
		return signDateString;
	}
	public void setSignDateString(String signDateString) {
		this.signDateString = signDateString;
	}
	
	@ExcelField(title="有效期", align=2)
	public String getCertDays() {
		return certDays;
	}
	public void setCertDays(String certDays) {
		this.certDays = certDays;
	}
	
	@ExcelField(title="到期日期", align=2)
	public String getNotAfter() {
		return notAfter;
	}
	public void setNotAfter(String notAfter) {
		this.notAfter = notAfter;
	}
	
	@ExcelField(title="业务状态", align=2)
	public String getDealInfoStatus() {
		return dealInfoStatus;
	}
	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}
	
	
	

}
