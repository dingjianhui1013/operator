package com.itrus.ca.modules.task.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*************************************************************
 **
 ** @author Zhang Jingtao
 **
 ** date:2014年8月18日 下午3:20:33
 ** 
 ** @describe:四川CA数据导入中间表
 **
 **************************************************************
 */

@Entity
@Table(name = "base_info_scca")
public class BasicInfoScca implements java.io.Serializable {

	// Fields

	private Long id;
	private Long appId;
	private Long productId;
	private Integer productLabel;
	private Integer year;
	private Integer classifying;
	private String companyName;
	private Integer companyType;
	private String organizationNumber;
	private String orgExpirationTime;
	private Integer selectLv;
	private Integer comCertificateType;
	private String comCertficateNumber;
	private String comCertificateTime;
	private String legalName;
	private String SProvince;
	private String SCity;
	private String SCounty;
	private String address;
	private String companyMobile;
	private String contactName;
	private Integer conCertType;
	private String conCertNumber;
	private String contactEmail;
	private String contactPhone;
	private String contactTel;
	private String contactSex;
	private String name;
	private String idCard;
	private String email;
	private String issuerDn;
	private String subjectDn;
	private String notafter;
	private String notbefore;
	private String serialnumber;
	private String keySn;
	private String 证书类型;
	private String 应用名;
	private String 单证or双证;
	private String 专用or通用;
	private Integer 多证书编号;
	private String key编码Usb口的编码;
	private Integer 证书天数;
	private Boolean used;

	// Constructors

	/** default constructor */
	public BasicInfoScca() {
	}

	/** full constructor */
	public BasicInfoScca(Long id, Long appId, Long productId,
			Integer productLabel, Integer year, Integer classifying,
			String companyName, Integer companyType, String organizationNumber,
			String orgExpirationTime, Integer selectLv,
			Integer comCertificateType, String comCertficateNumber,
			String comCertificateTime, String legalName, String SProvince,
			String SCity, String SCounty, String address, String companyMobile,
			String contactName, Integer conCertType, String conCertNumber,
			String contactEmail, String contactPhone, String contactTel,
			String contactSex, String name, String idCard, String email,
			String issuerDn, String subjectDn, String notafter,
			String notbefore, String serialnumber, String keySn,
			String 证书类型, String 应用名, String 单证or双证, String 专用or通用,
			Integer 多证书编号, String key编码Usb口的编码, Integer 证书天数) {
		this.id = id;
		this.appId = appId;
		this.productId = productId;
		this.productLabel = productLabel;
		this.year = year;
		this.classifying = classifying;
		this.companyName = companyName;
		this.companyType = companyType;
		this.organizationNumber = organizationNumber;
		this.orgExpirationTime = orgExpirationTime;
		this.selectLv = selectLv;
		this.comCertificateType = comCertificateType;
		this.comCertficateNumber = comCertficateNumber;
		this.comCertificateTime = comCertificateTime;
		this.legalName = legalName;
		this.SProvince = SProvince;
		this.SCity = SCity;
		this.SCounty = SCounty;
		this.address = address;
		this.companyMobile = companyMobile;
		this.contactName = contactName;
		this.conCertType = conCertType;
		this.conCertNumber = conCertNumber;
		this.contactEmail = contactEmail;
		this.contactPhone = contactPhone;
		this.contactTel = contactTel;
		this.contactSex = contactSex;
		this.name = name;
		this.idCard = idCard;
		this.email = email;
		this.issuerDn = issuerDn;
		this.subjectDn = subjectDn;
		this.notafter = notafter;
		this.notbefore = notbefore;
		this.serialnumber = serialnumber;
		this.keySn = keySn;
		this.证书类型 = 证书类型;
		this.应用名 = 应用名;
		this.单证or双证 = 单证or双证;
		this.专用or通用 = 专用or通用;
		this.多证书编号 = 多证书编号;
		this.key编码Usb口的编码 = key编码Usb口的编码;
		this.证书天数 = 证书天数;
	}

	// Property accessors

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMON_SEQUENCE")
	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "app_id")
	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Column(name = "product_id")
	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "product_label")
	public Integer getProductLabel() {
		return this.productLabel;
	}

	public void setProductLabel(Integer productLabel) {
		this.productLabel = productLabel;
	}

	@Column(name = "year")
	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(name = "classifying")
	public Integer getClassifying() {
		return this.classifying;
	}

	public void setClassifying(Integer classifying) {
		this.classifying = classifying;
	}

	@Column(name = "company_name")
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "company_type")
	public Integer getCompanyType() {
		return this.companyType;
	}

	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}

	@Column(name = "organization_number")
	public String getOrganizationNumber() {
		return this.organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	@Column(name = "org_expiration_time", length = 19)
	public String getOrgExpirationTime() {
		return this.orgExpirationTime;
	}

	public void setOrgExpirationTime(String orgExpirationTime) {
		this.orgExpirationTime = orgExpirationTime;
	}

	@Column(name = "select_lv")
	public Integer getSelectLv() {
		return this.selectLv;
	}

	public void setSelectLv(Integer selectLv) {
		this.selectLv = selectLv;
	}

	@Column(name = "com_certificate_type")
	public Integer getComCertificateType() {
		return this.comCertificateType;
	}

	public void setComCertificateType(Integer comCertificateType) {
		this.comCertificateType = comCertificateType;
	}

	@Column(name = "com_certficate_number")
	public String getComCertficateNumber() {
		return this.comCertficateNumber;
	}

	public void setComCertficateNumber(String comCertficateNumber) {
		this.comCertficateNumber = comCertficateNumber;
	}

	@Column(name = "com_certificate_time", length = 19)
	public String getComCertificateTime() {
		return this.comCertificateTime;
	}

	public void setComCertificateTime(String comCertificateTime) {
		this.comCertificateTime = comCertificateTime;
	}

	@Column(name = "legal_name")
	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	@Column(name = "s_province")
	public String getSProvince() {
		return this.SProvince;
	}

	public void setSProvince(String SProvince) {
		this.SProvince = SProvince;
	}

	@Column(name = "s_city")
	public String getSCity() {
		return this.SCity;
	}

	public void setSCity(String SCity) {
		this.SCity = SCity;
	}

	@Column(name = "s_county")
	public String getSCounty() {
		return this.SCounty;
	}

	public void setSCounty(String SCounty) {
		this.SCounty = SCounty;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "company_mobile")
	public String getCompanyMobile() {
		return this.companyMobile;
	}

	public void setCompanyMobile(String companyMobile) {
		this.companyMobile = companyMobile;
	}

	@Column(name = "contact_name")
	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "con_cert_type")
	public Integer getConCertType() {
		return this.conCertType;
	}

	public void setConCertType(Integer conCertType) {
		this.conCertType = conCertType;
	}

	@Column(name = "con_cert_number")
	public String getConCertNumber() {
		return this.conCertNumber;
	}

	public void setConCertNumber(String conCertNumber) {
		this.conCertNumber = conCertNumber;
	}

	@Column(name = "contact_email")
	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "contact_phone")
	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column(name = "contact_tel")
	public String getContactTel() {
		return this.contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	@Column(name = "contact_sex")
	public String getContactSex() {
		return this.contactSex;
	}

	public void setContactSex(String contactSex) {
		this.contactSex = contactSex;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "id_card")
	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "issuer_dn")
	public String getIssuerDn() {
		return this.issuerDn;
	}

	public void setIssuerDn(String issuerDn) {
		this.issuerDn = issuerDn;
	}

	@Column(name = "subject_dn")
	public String getSubjectDn() {
		return this.subjectDn;
	}

	public void setSubjectDn(String subjectDn) {
		this.subjectDn = subjectDn;
	}

	@Column(name = "notafter", length = 19)
	public String getNotafter() {
		return this.notafter;
	}

	public void setNotafter(String notafter) {
		this.notafter = notafter;
	}

	@Column(name = "notbefore", length = 19)
	public String getNotbefore() {
		return this.notbefore;
	}

	public void setNotbefore(String notbefore) {
		this.notbefore = notbefore;
	}

	@Column(name = "serialnumber")
	public String getSerialnumber() {
		return this.serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	@Column(name = "key_sn")
	public String getKeySn() {
		return this.keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name = "证书类型")
	public String get证书类型() {
		return this.证书类型;
	}

	public void set证书类型(String 证书类型) {
		this.证书类型 = 证书类型;
	}

	@Column(name = "应用名")
	public String get应用名() {
		return this.应用名;
	}

	public void set应用名(String 应用名) {
		this.应用名 = 应用名;
	}

	@Column(name = "单证or双证")
	public String get单证or双证() {
		return this.单证or双证;
	}

	public void set单证or双证(String 单证or双证) {
		this.单证or双证 = 单证or双证;
	}

	@Column(name = "专用or通用")
	public String get专用or通用() {
		return this.专用or通用;
	}

	public void set专用or通用(String 专用or通用) {
		this.专用or通用 = 专用or通用;
	}

	@Column(name = "多证书编号")
	public Integer get多证书编号() {
		return this.多证书编号;
	}

	public void set多证书编号(Integer 多证书编号) {
		this.多证书编号 = 多证书编号;
	}

	@Column(name = "key编码_USB口的编码")
	public String getKey编码Usb口的编码() {
		return this.key编码Usb口的编码;
	}

	public void setKey编码Usb口的编码(String key编码Usb口的编码) {
		this.key编码Usb口的编码 = key编码Usb口的编码;
	}

	@Column(name = "证书天数")
	public Integer get证书天数() {
		return this.证书天数;
	}

	public void set证书天数(Integer 证书天数) {
		this.证书天数 = 证书天数;
	}

	@Column(name = "used")
	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BasicInfoScca))
			return false;
		BasicInfoScca castOther = (BasicInfoScca) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getAppId() == castOther.getAppId()) || (this
						.getAppId() != null && castOther.getAppId() != null && this
						.getAppId().equals(castOther.getAppId())))
				&& ((this.getProductId() == castOther.getProductId()) || (this
						.getProductId() != null
						&& castOther.getProductId() != null && this
						.getProductId().equals(castOther.getProductId())))
				&& ((this.getProductLabel() == castOther.getProductLabel()) || (this
						.getProductLabel() != null
						&& castOther.getProductLabel() != null && this
						.getProductLabel().equals(castOther.getProductLabel())))
				&& ((this.getYear() == castOther.getYear()) || (this.getYear() != null
						&& castOther.getYear() != null && this.getYear()
						.equals(castOther.getYear())))
				&& ((this.getClassifying() == castOther.getClassifying()) || (this
						.getClassifying() != null
						&& castOther.getClassifying() != null && this
						.getClassifying().equals(castOther.getClassifying())))
				&& ((this.getCompanyName() == castOther.getCompanyName()) || (this
						.getCompanyName() != null
						&& castOther.getCompanyName() != null && this
						.getCompanyName().equals(castOther.getCompanyName())))
				&& ((this.getCompanyType() == castOther.getCompanyType()) || (this
						.getCompanyType() != null
						&& castOther.getCompanyType() != null && this
						.getCompanyType().equals(castOther.getCompanyType())))
				&& ((this.getOrganizationNumber() == castOther
						.getOrganizationNumber()) || (this
						.getOrganizationNumber() != null
						&& castOther.getOrganizationNumber() != null && this
						.getOrganizationNumber().equals(
								castOther.getOrganizationNumber())))
				&& ((this.getOrgExpirationTime() == castOther
						.getOrgExpirationTime()) || (this
						.getOrgExpirationTime() != null
						&& castOther.getOrgExpirationTime() != null && this
						.getOrgExpirationTime().equals(
								castOther.getOrgExpirationTime())))
				&& ((this.getSelectLv() == castOther.getSelectLv()) || (this
						.getSelectLv() != null
						&& castOther.getSelectLv() != null && this
						.getSelectLv().equals(castOther.getSelectLv())))
				&& ((this.getComCertificateType() == castOther
						.getComCertificateType()) || (this
						.getComCertificateType() != null
						&& castOther.getComCertificateType() != null && this
						.getComCertificateType().equals(
								castOther.getComCertificateType())))
				&& ((this.getComCertficateNumber() == castOther
						.getComCertficateNumber()) || (this
						.getComCertficateNumber() != null
						&& castOther.getComCertficateNumber() != null && this
						.getComCertficateNumber().equals(
								castOther.getComCertficateNumber())))
				&& ((this.getComCertificateTime() == castOther
						.getComCertificateTime()) || (this
						.getComCertificateTime() != null
						&& castOther.getComCertificateTime() != null && this
						.getComCertificateTime().equals(
								castOther.getComCertificateTime())))
				&& ((this.getLegalName() == castOther.getLegalName()) || (this
						.getLegalName() != null
						&& castOther.getLegalName() != null && this
						.getLegalName().equals(castOther.getLegalName())))
				&& ((this.getSProvince() == castOther.getSProvince()) || (this
						.getSProvince() != null
						&& castOther.getSProvince() != null && this
						.getSProvince().equals(castOther.getSProvince())))
				&& ((this.getSCity() == castOther.getSCity()) || (this
						.getSCity() != null && castOther.getSCity() != null && this
						.getSCity().equals(castOther.getSCity())))
				&& ((this.getSCounty() == castOther.getSCounty()) || (this
						.getSCounty() != null && castOther.getSCounty() != null && this
						.getSCounty().equals(castOther.getSCounty())))
				&& ((this.getAddress() == castOther.getAddress()) || (this
						.getAddress() != null && castOther.getAddress() != null && this
						.getAddress().equals(castOther.getAddress())))
				&& ((this.getCompanyMobile() == castOther.getCompanyMobile()) || (this
						.getCompanyMobile() != null
						&& castOther.getCompanyMobile() != null && this
						.getCompanyMobile()
						.equals(castOther.getCompanyMobile())))
				&& ((this.getContactName() == castOther.getContactName()) || (this
						.getContactName() != null
						&& castOther.getContactName() != null && this
						.getContactName().equals(castOther.getContactName())))
				&& ((this.getConCertType() == castOther.getConCertType()) || (this
						.getConCertType() != null
						&& castOther.getConCertType() != null && this
						.getConCertType().equals(castOther.getConCertType())))
				&& ((this.getConCertNumber() == castOther.getConCertNumber()) || (this
						.getConCertNumber() != null
						&& castOther.getConCertNumber() != null && this
						.getConCertNumber()
						.equals(castOther.getConCertNumber())))
				&& ((this.getContactEmail() == castOther.getContactEmail()) || (this
						.getContactEmail() != null
						&& castOther.getContactEmail() != null && this
						.getContactEmail().equals(castOther.getContactEmail())))
				&& ((this.getContactPhone() == castOther.getContactPhone()) || (this
						.getContactPhone() != null
						&& castOther.getContactPhone() != null && this
						.getContactPhone().equals(castOther.getContactPhone())))
				&& ((this.getContactTel() == castOther.getContactTel()) || (this
						.getContactTel() != null
						&& castOther.getContactTel() != null && this
						.getContactTel().equals(castOther.getContactTel())))
				&& ((this.getContactSex() == castOther.getContactSex()) || (this
						.getContactSex() != null
						&& castOther.getContactSex() != null && this
						.getContactSex().equals(castOther.getContactSex())))
				&& ((this.getName() == castOther.getName()) || (this.getName() != null
						&& castOther.getName() != null && this.getName()
						.equals(castOther.getName())))
				&& ((this.getIdCard() == castOther.getIdCard()) || (this
						.getIdCard() != null && castOther.getIdCard() != null && this
						.getIdCard().equals(castOther.getIdCard())))
				&& ((this.getEmail() == castOther.getEmail()) || (this
						.getEmail() != null && castOther.getEmail() != null && this
						.getEmail().equals(castOther.getEmail())))
				&& ((this.getIssuerDn() == castOther.getIssuerDn()) || (this
						.getIssuerDn() != null
						&& castOther.getIssuerDn() != null && this
						.getIssuerDn().equals(castOther.getIssuerDn())))
				&& ((this.getSubjectDn() == castOther.getSubjectDn()) || (this
						.getSubjectDn() != null
						&& castOther.getSubjectDn() != null && this
						.getSubjectDn().equals(castOther.getSubjectDn())))
				&& ((this.getNotafter() == castOther.getNotafter()) || (this
						.getNotafter() != null
						&& castOther.getNotafter() != null && this
						.getNotafter().equals(castOther.getNotafter())))
				&& ((this.getNotbefore() == castOther.getNotbefore()) || (this
						.getNotbefore() != null
						&& castOther.getNotbefore() != null && this
						.getNotbefore().equals(castOther.getNotbefore())))
				&& ((this.getSerialnumber() == castOther.getSerialnumber()) || (this
						.getSerialnumber() != null
						&& castOther.getSerialnumber() != null && this
						.getSerialnumber().equals(castOther.getSerialnumber())))
				&& ((this.getKeySn() == castOther.getKeySn()) || (this
						.getKeySn() != null && castOther.getKeySn() != null && this
						.getKeySn().equals(castOther.getKeySn())))
				&& ((this.get证书类型() == castOther.get证书类型()) || (this.get证书类型() != null
						&& castOther.get证书类型() != null && this.get证书类型()
						.equals(castOther.get证书类型())))
				&& ((this.get应用名() == castOther.get应用名()) || (this.get应用名() != null
						&& castOther.get应用名() != null && this.get应用名().equals(
						castOther.get应用名())))
				&& ((this.get单证or双证() == castOther.get单证or双证()) || (this
						.get单证or双证() != null && castOther.get单证or双证() != null && this
						.get单证or双证().equals(castOther.get单证or双证())))
				&& ((this.get专用or通用() == castOther.get专用or通用()) || (this
						.get专用or通用() != null && castOther.get专用or通用() != null && this
						.get专用or通用().equals(castOther.get专用or通用())))
				&& ((this.get多证书编号() == castOther.get多证书编号()) || (this
						.get多证书编号() != null && castOther.get多证书编号() != null && this
						.get多证书编号().equals(castOther.get多证书编号())))
				&& ((this.getKey编码Usb口的编码() == castOther.getKey编码Usb口的编码()) || (this
						.getKey编码Usb口的编码() != null
						&& castOther.getKey编码Usb口的编码() != null && this
						.getKey编码Usb口的编码().equals(castOther.getKey编码Usb口的编码())))
				&& ((this.get证书天数() == castOther.get证书天数()) || (this.get证书天数() != null
						&& castOther.get证书天数() != null && this.get证书天数()
						.equals(castOther.get证书天数())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getAppId() == null ? 0 : this.getAppId().hashCode());
		result = 37 * result
				+ (getProductId() == null ? 0 : this.getProductId().hashCode());
		result = 37
				* result
				+ (getProductLabel() == null ? 0 : this.getProductLabel()
						.hashCode());
		result = 37 * result
				+ (getYear() == null ? 0 : this.getYear().hashCode());
		result = 37
				* result
				+ (getClassifying() == null ? 0 : this.getClassifying()
						.hashCode());
		result = 37
				* result
				+ (getCompanyName() == null ? 0 : this.getCompanyName()
						.hashCode());
		result = 37
				* result
				+ (getCompanyType() == null ? 0 : this.getCompanyType()
						.hashCode());
		result = 37
				* result
				+ (getOrganizationNumber() == null ? 0 : this
						.getOrganizationNumber().hashCode());
		result = 37
				* result
				+ (getOrgExpirationTime() == null ? 0 : this
						.getOrgExpirationTime().hashCode());
		result = 37 * result
				+ (getSelectLv() == null ? 0 : this.getSelectLv().hashCode());
		result = 37
				* result
				+ (getComCertificateType() == null ? 0 : this
						.getComCertificateType().hashCode());
		result = 37
				* result
				+ (getComCertficateNumber() == null ? 0 : this
						.getComCertficateNumber().hashCode());
		result = 37
				* result
				+ (getComCertificateTime() == null ? 0 : this
						.getComCertificateTime().hashCode());
		result = 37 * result
				+ (getLegalName() == null ? 0 : this.getLegalName().hashCode());
		result = 37 * result
				+ (getSProvince() == null ? 0 : this.getSProvince().hashCode());
		result = 37 * result
				+ (getSCity() == null ? 0 : this.getSCity().hashCode());
		result = 37 * result
				+ (getSCounty() == null ? 0 : this.getSCounty().hashCode());
		result = 37 * result
				+ (getAddress() == null ? 0 : this.getAddress().hashCode());
		result = 37
				* result
				+ (getCompanyMobile() == null ? 0 : this.getCompanyMobile()
						.hashCode());
		result = 37
				* result
				+ (getContactName() == null ? 0 : this.getContactName()
						.hashCode());
		result = 37
				* result
				+ (getConCertType() == null ? 0 : this.getConCertType()
						.hashCode());
		result = 37
				* result
				+ (getConCertNumber() == null ? 0 : this.getConCertNumber()
						.hashCode());
		result = 37
				* result
				+ (getContactEmail() == null ? 0 : this.getContactEmail()
						.hashCode());
		result = 37
				* result
				+ (getContactPhone() == null ? 0 : this.getContactPhone()
						.hashCode());
		result = 37
				* result
				+ (getContactTel() == null ? 0 : this.getContactTel()
						.hashCode());
		result = 37
				* result
				+ (getContactSex() == null ? 0 : this.getContactSex()
						.hashCode());
		result = 37 * result
				+ (getName() == null ? 0 : this.getName().hashCode());
		result = 37 * result
				+ (getIdCard() == null ? 0 : this.getIdCard().hashCode());
		result = 37 * result
				+ (getEmail() == null ? 0 : this.getEmail().hashCode());
		result = 37 * result
				+ (getIssuerDn() == null ? 0 : this.getIssuerDn().hashCode());
		result = 37 * result
				+ (getSubjectDn() == null ? 0 : this.getSubjectDn().hashCode());
		result = 37 * result
				+ (getNotafter() == null ? 0 : this.getNotafter().hashCode());
		result = 37 * result
				+ (getNotbefore() == null ? 0 : this.getNotbefore().hashCode());
		result = 37
				* result
				+ (getSerialnumber() == null ? 0 : this.getSerialnumber()
						.hashCode());
		result = 37 * result
				+ (getKeySn() == null ? 0 : this.getKeySn().hashCode());
		result = 37 * result
				+ (get证书类型() == null ? 0 : this.get证书类型().hashCode());
		result = 37 * result
				+ (get应用名() == null ? 0 : this.get应用名().hashCode());
		result = 37 * result
				+ (get单证or双证() == null ? 0 : this.get单证or双证().hashCode());
		result = 37 * result
				+ (get专用or通用() == null ? 0 : this.get专用or通用().hashCode());
		result = 37 * result
				+ (get多证书编号() == null ? 0 : this.get多证书编号().hashCode());
		result = 37
				* result
				+ (getKey编码Usb口的编码() == null ? 0 : this.getKey编码Usb口的编码()
						.hashCode());
		result = 37 * result
				+ (get证书天数() == null ? 0 : this.get证书天数().hashCode());
		return result;
	}

}