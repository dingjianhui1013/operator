package com.itrus.ca.modules.work.vo;

import java.util.Date;

public class WorkDealInfoListVo {

	private Long id;
	private String svn;
	private String configAppName;
	private String companyName;
	private String certApplyInfoName;
	private String productName;
	private String keySn;
	private Date signDate;
	private Date notafter;
	private Date notbefore;
	private String dealInfoStatus;
	private Integer addCertDays;
	private Integer dealInfoType;
	private Integer dealInfoType1;
	private Integer dealInfoType2;
	private Integer dealInfoType3;
	private Integer lastDays;
	private Integer year;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSvn() {
		return svn;
	}
	public void setSvn(String svn) {
		this.svn = svn;
	}
	public String getConfigAppName() {
		return configAppName;
	}
	public void setConfigAppName(String configAppName) {
		this.configAppName = configAppName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getKeySn() {
		return keySn;
	}
	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	public Date getNotafter() {
		return notafter;
	}
	public void setNotafter(Date notafter) {
		this.notafter = notafter;
	}
	public Date getNotbefore() {
		return notbefore;
	}
	public void setNotbefore(Date notbefore) {
		this.notbefore = notbefore;
	}
	public String getDealInfoStatus() {
		return dealInfoStatus;
	}
	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}
	public Integer getAddCertDays() {
		return addCertDays;
	}
	public void setAddCertDays(Integer addCertDays) {
		this.addCertDays = addCertDays;
	}
	public Integer getDealInfoType() {
		return dealInfoType;
	}
	public void setDealInfoType(Integer dealInfoType) {
		this.dealInfoType = dealInfoType;
	}
	public Integer getDealInfoType1() {
		return dealInfoType1;
	}
	public void setDealInfoType1(Integer dealInfoType1) {
		this.dealInfoType1 = dealInfoType1;
	}
	public Integer getDealInfoType2() {
		return dealInfoType2;
	}
	public void setDealInfoType2(Integer dealInfoType2) {
		this.dealInfoType2 = dealInfoType2;
	}
	public Integer getDealInfoType3() {
		return dealInfoType3;
	}
	public void setDealInfoType3(Integer dealInfoType3) {
		this.dealInfoType3 = dealInfoType3;
	}
	public Integer getLastDays() {
		return lastDays;
	}
	public void setLastDays(Integer lastDays) {
		this.lastDays = lastDays;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	
}
