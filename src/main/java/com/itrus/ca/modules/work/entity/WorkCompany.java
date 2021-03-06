package com.itrus.ca.modules.work.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.BaseEntity;

/**
 * WorkCompany entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_company")
public class WorkCompany extends BaseEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String companyName;          // 单位名称
	
	private String twoLevelCompanyName;  // 二级单位名称
	
	private String comEnglishName;
	private String companyType;          // 单位类型
	private String comCertificateType;   // 单位证照
	private String comCertficateNumber;  // 证件号
	private Timestamp comCertficateTime; // 单位证照有效期
	private String businessNumber;
	private String selectLv;             // 服务级别
	private String organizationNumber;   // 组织机构代码
	private Timestamp orgExpirationTime; // 组织机构代码有效期
	private String comPhone;
	private String zipCode;
	private String tcpNumber;
	private String ageDisNumber;
	private String legalName;  		// 法人姓名
	private String province;   		// 省份
	private String city;	   		// 地级市
	private String district;   		// 市、县级市
	private String address;    		// 街道地址
	private String companyMobile;	// 单位联系电话
	private String enterpriseType;
	private String marketingScope;
	private String annualInspection;
	private String industry;
	private String registeredCapital;
	private String actualCapital;
	private String companyIp;
	private String companyWeb;
	private String remarks;			// 备注信息
	private String areaRemark; 		// 区域备注
	private Set<WorkDealInfo> workDealInfos = new HashSet<WorkDealInfo>(0);
	private Set<WorkUser> workUsers = new HashSet<WorkUser>(0);
	private Set<WorkLog> workLogs = new HashSet<WorkLog>(0);

	// Constructors

	/** default constructor */
	public WorkCompany() {
	}

	/** full constructor */
	public WorkCompany(String companyName, String comEnglishName,
			String companyType, String comCertificateType,
			String comCertficateNumber, String businessNumber, String selectLv,
			String organizationNumber, Timestamp orgExpirationTime,
			String comPhone, String zipCode, String tcpNumber,
			String ageDisNumber, String legalName, String province,
			String city, String district, String address, String companyMobile,
			String enterpriseType, String marketingScope,
			String annualInspection, String industry, String registeredCapital,
			String actualCapital, String companyIp, String companyWeb,
			String remarks, Set<WorkDealInfo> workDealInfos,
			Set<WorkUser> workUsers, Set<WorkLog> workLogs) {
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
		this.workDealInfos = workDealInfos;
		this.workUsers = workUsers;
		this.workLogs = workLogs;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="WORK_COMPANY_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="WORK_COMPANY_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "WORK_COMPANY_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "company_name", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "two_level_company_name", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getTwoLevelCompanyName() {
		return twoLevelCompanyName;
	}

	public void setTwoLevelCompanyName(String twoLevelCompanyName) {
		this.twoLevelCompanyName = twoLevelCompanyName;
	}

	@Column(name = "com_english_name", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getComEnglishName() {
		return this.comEnglishName;
	}

	public void setComEnglishName(String comEnglishName) {
		this.comEnglishName = comEnglishName;
	}

	@Column(name = "company_type", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getCompanyType() {
		return this.companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	@Column(name = "com_certificate_type", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getComCertificateType() {
		return this.comCertificateType;
	}

	public void setComCertificateType(String comCertificateType) {
		this.comCertificateType = comCertificateType;
	}

	@Column(name = "com_certficate_number", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getComCertficateNumber() {
		return this.comCertficateNumber;
	}

	public void setComCertficateNumber(String comCertficateNumber) {
		this.comCertficateNumber = comCertficateNumber;
	}

	@Column(name = "business_number", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getBusinessNumber() {
		return this.businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	@Column(name = "select_lv", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getSelectLv() {
		return this.selectLv;
	}

	public void setSelectLv(String selectLv) {
		this.selectLv = selectLv;
	}

	@Column(name = "organization_number", length = 100,columnDefinition = "NVARCHAR2(100)")
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

	@Column(name = "com_phone", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getComPhone() {
		return this.comPhone;
	}

	public void setComPhone(String comPhone) {
		this.comPhone = comPhone;
	}

	@Column(name = "zip_code", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "tcp_number", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getTcpNumber() {
		return this.tcpNumber;
	}

	public void setTcpNumber(String tcpNumber) {
		this.tcpNumber = tcpNumber;
	}

	@Column(name = "age_dis_number",columnDefinition = "NVARCHAR2(255)")
	public String getAgeDisNumber() {
		return this.ageDisNumber;
	}

	public void setAgeDisNumber(String ageDisNumber) {
		this.ageDisNumber = ageDisNumber;
	}

	@Column(name = "legal_name", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	@Column(name = "province", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "district", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "address", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "company_mobile", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getCompanyMobile() {
		return this.companyMobile;
	}

	public void setCompanyMobile(String companyMobile) {
		this.companyMobile = companyMobile;
	}

	@Column(name = "enterprise_type", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getEnterpriseType() {
		return this.enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	@Column(name = "marketing_scope", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getMarketingScope() {
		return this.marketingScope;
	}

	public void setMarketingScope(String marketingScope) {
		this.marketingScope = marketingScope;
	}

	@Column(name = "annual_inspection", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getAnnualInspection() {
		return this.annualInspection;
	}

	public void setAnnualInspection(String annualInspection) {
		this.annualInspection = annualInspection;
	}

	@Column(name = "industry", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Column(name = "registered_capital", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getRegisteredCapital() {
		return this.registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	@Column(name = "actual_capital", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getActualCapital() {
		return this.actualCapital;
	}

	public void setActualCapital(String actualCapital) {
		this.actualCapital = actualCapital;
	}

	@Column(name = "company_ip", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getCompanyIp() {
		return this.companyIp;
	}

	public void setCompanyIp(String companyIp) {
		this.companyIp = companyIp;
	}

	@Column(name = "company_web", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getCompanyWeb() {
		return this.companyWeb;
	}

	public void setCompanyWeb(String companyWeb) {
		this.companyWeb = companyWeb;
	}

	@Column(name = "remarks",columnDefinition = "NVARCHAR2(255)")
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workCompany")
	public Set<WorkDealInfo> getWorkDealInfos() {
		return this.workDealInfos;
	}

	public void setWorkDealInfos(Set<WorkDealInfo> workDealInfos) {
		this.workDealInfos = workDealInfos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workCompany")
	public Set<WorkUser> getWorkUsers() {
		return this.workUsers;
	}

	public void setWorkUsers(Set<WorkUser> workUsers) {
		this.workUsers = workUsers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workCompany")
	public Set<WorkLog> getWorkLogs() {
		return this.workLogs;
	}

	public void setWorkLogs(Set<WorkLog> workLogs) {
		this.workLogs = workLogs;
	}
	@Column(name = "com_certficate_time")
	public Timestamp getComCertficateTime() {
		return comCertficateTime;
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