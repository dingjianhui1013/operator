package com.itrus.ca.modules.settle.vo;

import com.itrus.ca.common.utils.excel.annotation.ExcelField;

public class ProjectCertificationDetailsVo {
	
	private String svn;
	private String companyName;
	private String productName;
	private String dealInfoType;
	private String certDays;
	private String signDateString;
	
	@ExcelField(title="业务编号", align=2)
	public String getSvn() {
		return svn;
	}
	public void setSvn(String svn) {
		this.svn = svn;
	}
	@ExcelField(title="单位名称", align=2)
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@ExcelField(title="业务类型", align=2)
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@ExcelField(title="证书类型", align=2)
	public String getDealInfoType() {
		return dealInfoType;
	}
	public void setDealInfoType(String dealInfoType) {
		this.dealInfoType = dealInfoType;
	}
	@ExcelField(title="证书有效期", align=2)
	public String getCertDays() {
		return certDays;
	}
	public void setCertDays(String certDays) {
		this.certDays = certDays;
	}
	@ExcelField(title="制证时间", align=2)
	public String getSignDateString() {
		return signDateString;
	}
	public void setSignDateString(String signDateString) {
		this.signDateString = signDateString;
	}
	public void setNotAfter(String notafterString) {
		// TODO Auto-generated method stub
		
	}
	
	
}
