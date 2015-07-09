package com.itrus.ca.modules.work.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itrus.ca.modules.sys.entity.Office;

/**
 * WorkCertApplyInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_cert_apply_info")
public class WorkCertApplyInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private Office Office;
	private String addtionalField1;
	private String addtionalField2;
	private String addtionalField3;
	private String addtionalField4;
	private String addtionalField5;
	private String addtionalField6;
	private String addtionalField7;
	private String addtionalField8;
	private String addtionalField9;
	private String addtionalField10;
	private String country;
	private String description;
	private String dns;
	private String email;
	private String idRandom;
	private String ip;
	private String locality;
	private String mobilePhone;
	private String name;
	private String organization;
	private String orgunit;
	private String serialnumber;
	private Integer state;
	private String street;
	private String surname;
	private String title;
	private String province;
	private String city;
	private String telPhone;
	private String position;
	private String fax;
	private String domain;
	private String contactEmail;
	private String commonName;
	private String idCard;
	private Set<WorkCertInfo> workCertInfos = new HashSet<WorkCertInfo>(0);

	// Constructors

	/** default constructor */
	public WorkCertApplyInfo() {
	}

	/** full constructor */
	public WorkCertApplyInfo(Office Office,
			String addtionalField1, String addtionalField2,
			String addtionalField3, String addtionalField4,
			String addtionalField5, String addtionalField6,
			String addtionalField7, String addtionalField8,
			String addtionalField9, String addtionalField10, String country,
			String description, String dns, String email, String idRandom,
			String ip, String locality, String mobilePhone, String name,
			String organization, String orgunit, String serialnumber,
			Integer state, String street, String surname, String title,
			String province, String city, String telPhone, String position,
			String fax, String domain, String contactEmail, String commonName,
			Set<WorkCertInfo> workCertInfos) {
		this.Office = Office;
		this.addtionalField1 = addtionalField1;
		this.addtionalField2 = addtionalField2;
		this.addtionalField3 = addtionalField3;
		this.addtionalField4 = addtionalField4;
		this.addtionalField5 = addtionalField5;
		this.addtionalField6 = addtionalField6;
		this.addtionalField7 = addtionalField7;
		this.addtionalField8 = addtionalField8;
		this.addtionalField9 = addtionalField9;
		this.addtionalField10 = addtionalField10;
		this.country = country;
		this.description = description;
		this.dns = dns;
		this.email = email;
		this.idRandom = idRandom;
		this.ip = ip;
		this.locality = locality;
		this.mobilePhone = mobilePhone;
		this.name = name;
		this.organization = organization;
		this.orgunit = orgunit;
		this.serialnumber = serialnumber;
		this.state = state;
		this.street = street;
		this.surname = surname;
		this.title = title;
		this.province = province;
		this.city = city;
		this.telPhone = telPhone;
		this.position = position;
		this.fax = fax;
		this.domain = domain;
		this.contactEmail = contactEmail;
		this.commonName = commonName;
		this.workCertInfos = workCertInfos;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.Office;
	}

	public void setOffice(Office Office) {
		this.Office = Office;
	}

	@Column(name = "addtional_field1")
	public String getAddtionalField1() {
		return this.addtionalField1;
	}

	public void setAddtionalField1(String addtionalField1) {
		this.addtionalField1 = addtionalField1;
	}

	@Column(name = "addtional_field2")
	public String getAddtionalField2() {
		return this.addtionalField2;
	}

	public void setAddtionalField2(String addtionalField2) {
		this.addtionalField2 = addtionalField2;
	}

	@Column(name = "addtional_field3")
	public String getAddtionalField3() {
		return this.addtionalField3;
	}

	public void setAddtionalField3(String addtionalField3) {
		this.addtionalField3 = addtionalField3;
	}

	@Column(name = "addtional_field4")
	public String getAddtionalField4() {
		return this.addtionalField4;
	}

	public void setAddtionalField4(String addtionalField4) {
		this.addtionalField4 = addtionalField4;
	}

	@Column(name = "addtional_field5")
	public String getAddtionalField5() {
		return this.addtionalField5;
	}

	public void setAddtionalField5(String addtionalField5) {
		this.addtionalField5 = addtionalField5;
	}

	@Column(name = "addtional_field6")
	public String getAddtionalField6() {
		return this.addtionalField6;
	}

	public void setAddtionalField6(String addtionalField6) {
		this.addtionalField6 = addtionalField6;
	}

	@Column(name = "addtional_field7")
	public String getAddtionalField7() {
		return this.addtionalField7;
	}

	public void setAddtionalField7(String addtionalField7) {
		this.addtionalField7 = addtionalField7;
	}

	@Column(name = "addtional_field8")
	public String getAddtionalField8() {
		return this.addtionalField8;
	}

	public void setAddtionalField8(String addtionalField8) {
		this.addtionalField8 = addtionalField8;
	}

	@Column(name = "addtional_field9")
	public String getAddtionalField9() {
		return this.addtionalField9;
	}

	public void setAddtionalField9(String addtionalField9) {
		this.addtionalField9 = addtionalField9;
	}

	@Column(name = "addtional_field10")
	public String getAddtionalField10() {
		return this.addtionalField10;
	}

	public void setAddtionalField10(String addtionalField10) {
		this.addtionalField10 = addtionalField10;
	}

	@Column(name = "country")
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "dns")
	public String getDns() {
		return this.dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "id_random")
	public String getIdRandom() {
		return this.idRandom;
	}

	public void setIdRandom(String idRandom) {
		this.idRandom = idRandom;
	}

	@Column(name = "ip")
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "locality")
	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Column(name = "mobile_phone")
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "organization")
	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Column(name = "orgunit")
	public String getOrgunit() {
		return this.orgunit;
	}

	public void setOrgunit(String orgunit) {
		this.orgunit = orgunit;
	}

	@Column(name = "serialnumber")
	public String getSerialnumber() {
		return this.serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	@Column(name = "state")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "street")
	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "surname")
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "province")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "tel_phone")
	public String getTelPhone() {
		return this.telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	@Column(name = "position")
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "fax")
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "domain")
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "contact_email")
	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "common_name")
	public String getCommonName() {
		return this.commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workCertApplyInfo")
	public Set<WorkCertInfo> getWorkCertInfos() {
		return this.workCertInfos;
	}

	public void setWorkCertInfos(Set<WorkCertInfo> workCertInfos) {
		this.workCertInfos = workCertInfos;
	}
	
	@Column(name = "id_card")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

}