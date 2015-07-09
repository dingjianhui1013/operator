package com.itrus.ca.modules.work.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WorkCompanyHis entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_company_his")
public class WorkCompanyHis implements java.io.Serializable {

	// Fields

	private Long id;
	private String companyName;
	private String comEnglishName;
	private String companyType;
	private String comCertificateType;
	private String comCertficateNumber;
	private String businessNumber;
	private String selectLv;
	private String organizationNumber;
	private Timestamp orgExpirationTime;
	private String comPhone;
	private String zipCode;
	private String tcpNumber;
	private String ageDisNumber;
	private String legalName;
	private String province;
	private String city;
	private String district;
	private String address;
	private String companyMobile;
	private String enterpriseType;
	private String marketingScope;
	private String annualInspection;
	private String industry;
	private String registeredCapital;
	private String actualCapital;
	private String companyIp;
	private String companyWeb;
	private String remarks;
	private String areaRemark;
	private Timestamp comCertficateTime;

	// Constructors

	/** default constructor */
	public WorkCompanyHis() {
	}

	/** full constructor */
	public WorkCompanyHis(String companyName, String comEnglishName,
			String companyType, String comCertificateType,
			String comCertficateNumber, String businessNumber, String selectLv,
			String organizationNumber, Timestamp orgExpirationTime,
			String comPhone, String zipCode, String tcpNumber,
			String ageDisNumber, String legalName, String province,
			String city, String district, String address, String companyMobile,
			String enterpriseType, String marketingScope,
			String annualInspection, String industry, String registeredCapital,
			String actualCapital, String companyIp, String companyWeb,
			String remarks, Timestamp comCertficateTime) {
		this.companyName = companyName;
		this.comEnglishName = comEnglishName;
		this.companyType = companyType;
		this.comCertificateType = comCertificateType;
		this.comCertficateNumber = comCertficateNumber;
		this.businessNumber = businessNumber;
		this.selectLv = selectLv;
		this.organizationNumber = organizationNumber;
		this.orgExpirationTime = orgExpirationTime;
		this.comPhone = comPhone;
		this.zipCode = zipCode;
		this.tcpNumber = tcpNumber;
		this.ageDisNumber = ageDisNumber;
		this.legalName = legalName;
		this.province = province;
		this.city = city;
		this.district = district;
		this.address = address;
		this.companyMobile = companyMobile;
		this.enterpriseType = enterpriseType;
		this.marketingScope = marketingScope;
		this.annualInspection = annualInspection;
		this.industry = industry;
		this.registeredCapital = registeredCapital;
		this.actualCapital = actualCapital;
		this.companyIp = companyIp;
		this.companyWeb = companyWeb;
		this.remarks = remarks;
		this.comCertficateTime = comCertficateTime;
	}

	// Property accessors
	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "COMMON_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "company_name", length = 100)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "com_english_name", length = 100)
	public String getComEnglishName() {
		return this.comEnglishName;
	}

	public void setComEnglishName(String comEnglishName) {
		this.comEnglishName = comEnglishName;
	}

	@Column(name = "company_type", length = 100)
	public String getCompanyType() {
		return this.companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	@Column(name = "com_certificate_type", length = 100)
	public String getComCertificateType() {
		return this.comCertificateType;
	}

	public void setComCertificateType(String comCertificateType) {
		this.comCertificateType = comCertificateType;
	}

	@Column(name = "com_certficate_number", length = 200)
	public String getComCertficateNumber() {
		return this.comCertficateNumber;
	}

	public void setComCertficateNumber(String comCertficateNumber) {
		this.comCertficateNumber = comCertficateNumber;
	}

	@Column(name = "business_number", length = 200)
	public String getBusinessNumber() {
		return this.businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	@Column(name = "select_lv", length = 100)
	public String getSelectLv() {
		return this.selectLv;
	}

	public void setSelectLv(String selectLv) {
		this.selectLv = selectLv;
	}

	@Column(name = "organization_number", length = 100)
	public String getOrganizationNumber() {
		return this.organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	@Column(name = "org_expiration_time", length = 19)
	public Timestamp getOrgExpirationTime() {
		return this.orgExpirationTime;
	}

	public void setOrgExpirationTime(Timestamp orgExpirationTime) {
		this.orgExpirationTime = orgExpirationTime;
	}

	@Column(name = "com_phone", length = 100)
	public String getComPhone() {
		return this.comPhone;
	}

	public void setComPhone(String comPhone) {
		this.comPhone = comPhone;
	}

	@Column(name = "zip_code", length = 100)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "tcp_number", length = 200)
	public String getTcpNumber() {
		return this.tcpNumber;
	}

	public void setTcpNumber(String tcpNumber) {
		this.tcpNumber = tcpNumber;
	}

	@Column(name = "age_dis_number")
	public String getAgeDisNumber() {
		return this.ageDisNumber;
	}

	public void setAgeDisNumber(String ageDisNumber) {
		this.ageDisNumber = ageDisNumber;
	}

	@Column(name = "legal_name", length = 100)
	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	@Column(name = "province", length = 100)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city", length = 100)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "district", length = 100)
	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "address", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "company_mobile", length = 100)
	public String getCompanyMobile() {
		return this.companyMobile;
	}

	public void setCompanyMobile(String companyMobile) {
		this.companyMobile = companyMobile;
	}

	@Column(name = "enterprise_type", length = 100)
	public String getEnterpriseType() {
		return this.enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	@Column(name = "marketing_scope", length = 100)
	public String getMarketingScope() {
		return this.marketingScope;
	}

	public void setMarketingScope(String marketingScope) {
		this.marketingScope = marketingScope;
	}

	@Column(name = "annual_inspection", length = 200)
	public String getAnnualInspection() {
		return this.annualInspection;
	}

	public void setAnnualInspection(String annualInspection) {
		this.annualInspection = annualInspection;
	}

	@Column(name = "industry", length = 100)
	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Column(name = "registered_capital", length = 100)
	public String getRegisteredCapital() {
		return this.registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	@Column(name = "actual_capital", length = 100)
	public String getActualCapital() {
		return this.actualCapital;
	}

	public void setActualCapital(String actualCapital) {
		this.actualCapital = actualCapital;
	}

	@Column(name = "company_ip", length = 100)
	public String getCompanyIp() {
		return this.companyIp;
	}

	public void setCompanyIp(String companyIp) {
		this.companyIp = companyIp;
	}

	@Column(name = "company_web", length = 100)
	public String getCompanyWeb() {
		return this.companyWeb;
	}

	public void setCompanyWeb(String companyWeb) {
		this.companyWeb = companyWeb;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "com_certficate_time", length = 19)
	public Timestamp getComCertficateTime() {
		return this.comCertficateTime;
	}

	public void setComCertficateTime(Timestamp comCertficateTime) {
		this.comCertficateTime = comCertficateTime;
	}

	@Column(name = "area_remark")
	public String getAreaRemark() {
		return areaRemark;
	}

	public void setAreaRemark(String areaRemark) {
		this.areaRemark = areaRemark;
	}

}