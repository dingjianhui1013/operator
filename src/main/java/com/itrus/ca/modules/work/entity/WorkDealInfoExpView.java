/**
 *2016年7月18日 下午7:34:41
 */
package com.itrus.ca.modules.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * @author: liubin
 *
 */

// @Embeddable
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Entity
@Table(name = "WORKDEALINFO_EXP_VIEW")
public class WorkDealInfoExpView implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WORK_DEAL_INFO_ID", length = 36)
	private Long id;
	@Column(name = "WORK_DEAL_INFO_SVN", columnDefinition = "NVARCHAR2(255)")
	private String svn;
	@Column(name = "WORK_DEAL_INFO_YEAR")
	private Integer year;
	@Column(name = "WORK_DEAL_INFO_LAST_DAYS")
	private Integer lastDays;
	@Column(name = "WORK_DEAL_INFO_NOTAFTER")
	private Date notAfter;
	@Column(name = "WORK_DEAL_INFO_KEY_SN", columnDefinition = "NVARCHAR2(255)")
	private String keySN;
	@Column(name = "WORK_DEAL_INFO_DEAL_INFO_TYPE")
	private Integer dealInfoType;
	@Column(name = "WORK_DEAL_INFO_DEAL_INFO_TYPE1")
	private Integer dealInfoType1;
	@Column(name = "WORK_DEAL_INFO_DEAL_INFO_TYPE2")
	private Integer dealInfoType2;
	@Column(name = "WORK_DEAL_INFO_DEAL_INFO_TYPE3")
	private Integer dealInfoType3;
	@Column(name = "WORK_DEAL_INFO_WORK_COMPANY_ID")
	private Long workCompanyId;
	@Column(name = "WORK_DEAL_INFO_WORK_USER_ID")
	private Long workUserId;
	@Column(name = "WORK_DEAL_INFO_PRODUCT_ID")
	private Long productId;
	@Column(name = "WORK_DEAL_INFO_APP_ID")
	private Long appId;
	@Column(name = "WORK_DEAL_INFO_CERT_ID")
	private Long certId;
	@Column(name = "WORK_COMPANY_COMPANY_NAME", columnDefinition = "NVARCHAR2(255)")
	private String companyName;
	@Column(name = "WORK_USER_CONTACT_NAME", columnDefinition = "NVARCHAR2(255)")
	private String contactName;
	@Column(name = "CONFIG_PRODUCT_PRODUCT_NAME", columnDefinition = "NVARCHAR2(255)")
	private String productName;
	@Column(name = "CONFIG_APP_ALIAS", columnDefinition = "NVARCHAR2(255)")
	private String appAlias;
	@Column(name = "WORK_CERT_APPLY_INFO_NAME", columnDefinition = "NVARCHAR2(255)")
	private String certApplyInfoName;
	@Column(name = "WORK_DEAL_INFO_OFFICE_ID")
	private Long officeId;
	@Column(name = "SYS_OFFICE_NAME", columnDefinition = "NVARCHAR2(255)")
	private String officeName;
	@Column(name = "WORK_DEAL_INFO_PAY_USER")
	private Long payUserId;
	@Column(name = "PAY_USER_NAME", columnDefinition = "NVARCHAR2(255)")
	private String payUserName;
	@Column(name = "WORK_DEAL_INFO_INPUT_USER")
	private Long inputUserId;
	@Column(name = "INPUT_USER_NAME", columnDefinition = "NVARCHAR2(255)")
	private String inputUserName;
	@Column(name = "AREA_ID")
	private Long areaId;
	@Column(name = "BUSINESS_CARD_USER_NAME", columnDefinition = "NVARCHAR2(255)")
	private String businessCardUserName;
	@Column(name = "CONFIG_CHARGE_AGENT_TEMP_STYLE")
	private String tempStyle;
	@Column(name = "METHOD_ALIPAY")
	private Boolean methodAlipay;
	@Column(name = "METHOD_BANK")
	private Boolean methodBank;
	@Column(name = "METHOD_CONTRACT")
	private Boolean methodContract;
	@Column(name = "METHOD_GOV")
	private Boolean methodGov;
	@Column(name = "METHOD_MONEY")
	private Boolean methodMoney;
	@Column(name = "METHOD_POS")
	private Boolean methodPos;
	@Column(name = "INPUT_USER_DATE")
	private Date inputUserDate;
	@Column(name = "BUSINESS_CARD_USER_DATE")
	private Date businessCardUserDate;
	@Column(name = "ORGANIZATION_NUMBER", length = 100, columnDefinition = "NVARCHAR2(100)")
	private String organizationNumber;
	@Column(name = "CON_CERT_NUMBER", length = 200, columnDefinition = "NVARCHAR2(200)")
	private String conCertNumber;
	@Column(name = "SIGN_DATE")
	private Date signDate;
	@Column(name = "ADD_CERT_DAYS")
	private Integer addCertDays;
	@Column(name = "DEAL_INFO_STATUS", nullable = false, length = 100, columnDefinition = "NVARCHAR2(2)")
	private String dealInfoStatus;
	@Column(name = "BUSINESS_CARD_USER")
	private Long businessCardUser;
	@Column(name = "PAY_TYPE")
	private Integer payType;
	@Column(name = "ATTESTATION_USER")
	private Long attestationId;
	@Column(name = "ATTESTATION_USER_NAME", columnDefinition = "NVARCHAR2(255)")
	private String attestationUserName;
	@Column(name = "PROVINCE", length = 100, columnDefinition = "NVARCHAR2(100)")
	private String province;
	@Column(name = "CITY", length = 100, columnDefinition = "NVARCHAR2(100)")
	private String city; // 地级市
	@Column(name = "DISTRICT", length = 100, columnDefinition = "NVARCHAR2(100)")
	private String district; // 市、县级市
	@Column(name = "RELATION_METHOD")
	private Integer relationMethod;
	@Column(name = "PAY_USER_DATE")
	private Date payUserDate;

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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getLastDays() {
		return lastDays;
	}

	public void setLastDays(Integer lastDays) {
		this.lastDays = lastDays;
	}

	public Date getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(Date notAfter) {
		this.notAfter = notAfter;
	}

	public String getKeySN() {
		return keySN;
	}

	public void setKeySN(String keySN) {
		this.keySN = keySN;
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

	public Long getWorkCompanyId() {
		return workCompanyId;
	}

	public void setWorkCompanyId(Long workCompanyId) {
		this.workCompanyId = workCompanyId;
	}

	public Long getWorkUserId() {
		return workUserId;
	}

	public void setWorkUserId(Long workUserId) {
		this.workUserId = workUserId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCertId() {
		return certId;
	}

	public void setCertId(Long certId) {
		this.certId = certId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAppAlias() {
		return appAlias;
	}

	public void setAppAlias(String appAlias) {
		this.appAlias = appAlias;
	}

	public String getCertApplyInfoName() {
		return certApplyInfoName;
	}

	public void setCertApplyInfoName(String certApplyInfoName) {
		this.certApplyInfoName = certApplyInfoName;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getPayUserId() {
		return payUserId;
	}

	public void setPayUserId(Long payUserId) {
		this.payUserId = payUserId;
	}

	public String getPayUserName() {
		return payUserName;
	}

	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}

	public Long getInputUserId() {
		return inputUserId;
	}

	public void setInputUserId(Long inputUserId) {
		this.inputUserId = inputUserId;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getBusinessCardUserName() {
		return businessCardUserName;
	}

	public void setBusinessCardUserName(String businessCardUserName) {
		this.businessCardUserName = businessCardUserName;
	}

	public String getTempStyle() {
		return tempStyle;
	}

	public void setTempStyle(String tempStyle) {
		this.tempStyle = tempStyle;
	}

	public Boolean getMethodAlipay() {
		return methodAlipay;
	}

	public void setMethodAlipay(Boolean methodAlipay) {
		this.methodAlipay = methodAlipay;
	}

	public Boolean getMethodBank() {
		return methodBank;
	}

	public void setMethodBank(Boolean methodBank) {
		this.methodBank = methodBank;
	}

	public Boolean getMethodContract() {
		return methodContract;
	}

	public void setMethodContract(Boolean methodContract) {
		this.methodContract = methodContract;
	}

	public Boolean getMethodGov() {
		return methodGov;
	}

	public void setMethodGov(Boolean methodGov) {
		this.methodGov = methodGov;
	}

	public Boolean getMethodMoney() {
		return methodMoney;
	}

	public void setMethodMoney(Boolean methodMoney) {
		this.methodMoney = methodMoney;
	}

	public Boolean getMethodPos() {
		return methodPos;
	}

	public void setMethodPos(Boolean methodPos) {
		this.methodPos = methodPos;
	}

	public Date getInputUserDate() {
		return inputUserDate;
	}

	public void setInputUserDate(Date inputUserDate) {
		this.inputUserDate = inputUserDate;
	}

	public Date getBusinessCardUserDate() {
		return businessCardUserDate;
	}

	public void setBusinessCardUserDate(Date businessCardUserDate) {
		this.businessCardUserDate = businessCardUserDate;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public String getConCertNumber() {
		return conCertNumber;
	}

	public void setConCertNumber(String conCertNumber) {
		this.conCertNumber = conCertNumber;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Integer getAddCertDays() {
		return addCertDays;
	}

	public void setAddCertDays(Integer addCertDays) {
		this.addCertDays = addCertDays;
	}

	public String getDealInfoStatus() {
		return dealInfoStatus;
	}

	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}

	public Long getBusinessCardUser() {
		return businessCardUser;
	}

	public void setBusinessCardUser(Long businessCardUser) {
		this.businessCardUser = businessCardUser;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Long getAttestationId() {
		return attestationId;
	}

	public void setAttestationId(Long attestationId) {
		this.attestationId = attestationId;
	}

	public String getAttestationUserName() {
		return attestationUserName;
	}

	public void setAttestationUserName(String attestationUserName) {
		this.attestationUserName = attestationUserName;
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

	public Integer getRelationMethod() {
		return relationMethod;
	}

	public void setRelationMethod(Integer relationMethod) {
		this.relationMethod = relationMethod;
	}

	public Date getPayUserDate() {
		return payUserDate;
	}

	public void setPayUserDate(Date payUserDate) {
		this.payUserDate = payUserDate;
	}

}
