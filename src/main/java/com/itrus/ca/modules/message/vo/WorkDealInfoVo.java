package com.itrus.ca.modules.message.vo;

import java.util.Date;

public class WorkDealInfoVo {

	//WorkDealInfo
	private String svn;        
	private String keySn;     
	private String dealInfoStatus;   
	private Date notafter;
	
	//WorkCertApplyInfo
	private String name;
	
	//WorkCompany
	private String organizationNumber;
	private String companyName;
	private String legalName;
	private String province;
	private String city;
	private String district;
	private String address;
	
	//ConfigApp
	private String alias;
	
	//WorkUser
	private String contactPhone;
	
	
	private Long dealInfoId;
	private Long certInfoId;
	private Long companyId;
	private Long appId;
	private Long userId;
	
	
	
	public WorkDealInfoVo() {
		super();
	}
	
	
	
	
	public WorkDealInfoVo(String svn, String keySn, String dealInfoStatus, Date notafter, String name,
			String organizationNumber, String companyName, String legalName, String province, String city,
			String district, String address, String alias) {
		super();
		this.svn = svn;
		this.keySn = keySn;
		this.dealInfoStatus = dealInfoStatus;
		this.notafter = notafter;
		this.name = name;
		this.organizationNumber = organizationNumber;
		this.companyName = companyName;
		this.legalName = legalName;
		this.province = province;
		this.city = city;
		this.district = district;
		this.address = address;
		this.alias = alias;
	}






	public WorkDealInfoVo(String svn, String keySn, String dealInfoStatus, Date notafter, String name,
			String organizationNumber, String companyName, String legalName, String province, String city,
			String district, String address, String alias, String contactPhone, Long dealInfoId, Long certInfoId,
			Long companyId, Long appId, Long userId) {
		super();
		this.svn = svn;
		this.keySn = keySn;
		this.dealInfoStatus = dealInfoStatus;
		this.notafter = notafter;
		this.name = name;
		this.organizationNumber = organizationNumber;
		this.companyName = companyName;
		this.legalName = legalName;
		this.province = province;
		this.city = city;
		this.district = district;
		this.address = address;
		this.alias = alias;
		this.contactPhone = contactPhone;
		this.dealInfoId = dealInfoId;
		this.certInfoId = certInfoId;
		this.companyId = companyId;
		this.appId = appId;
		this.userId = userId;
	}




	public String getSvn() {
		return svn;
	}
	public void setSvn(String svn) {
		this.svn = svn;
	}
	public String getKeySn() {
		return keySn;
	}
	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}
	public String getDealInfoStatus() {
		return dealInfoStatus;
	}
	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}
	public Date getNotafter() {
		return notafter;
	}
	public void setNotafter(Date notafter) {
		this.notafter = notafter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganizationNumber() {
		return organizationNumber;
	}
	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
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
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}




	public String getContactPhone() {
		return contactPhone;
	}




	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}




	public Long getDealInfoId() {
		return dealInfoId;
	}




	public void setDealInfoId(Long dealInfoId) {
		this.dealInfoId = dealInfoId;
	}




	public Long getCertInfoId() {
		return certInfoId;
	}




	public void setCertInfoId(Long certInfoId) {
		this.certInfoId = certInfoId;
	}




	public Long getCompanyId() {
		return companyId;
	}




	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}




	public Long getAppId() {
		return appId;
	}




	public void setAppId(Long appId) {
		this.appId = appId;
	}




	public Long getUserId() {
		return userId;
	}




	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
	
}
