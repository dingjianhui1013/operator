package com.itrus.ca.modules.work.entity;

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
import javax.persistence.Table;

/**
 * WorkUserHis entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_user_his")
public class WorkUserHis implements java.io.Serializable {

	// Fields

	private Long id;
	private WorkCompanyHis workCompanyHis;
	private String address;
	private String conCertNumber;
	private String conCertType;
	private String contactEmail;
	private String contactName;
	private String contactPhone;
	private String contactSex;
	private String contactTel;
	private String department;
	private Integer source;
	private Integer status;
	private Integer workType;
	private String userSn;

	// Constructors

	/** default constructor */
	public WorkUserHis() {
	}

	/** full constructor */
	public WorkUserHis(WorkCompany workCompany, String address,
			String conCertNumber, String conCertType, String contactEmail,
			String contactName, String contactPhone, String contactSex,
			String contactTel, String department, Integer source,
			Integer status, Integer workType, String userSn) {
		this.address = address;
		this.conCertNumber = conCertNumber;
		this.conCertType = conCertType;
		this.contactEmail = contactEmail;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.contactSex = contactSex;
		this.contactTel = contactTel;
		this.department = department;
		this.source = source;
		this.status = status;
		this.workType = workType;
		this.userSn = userSn;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORK_USER_HIS_SEQUENCE")
//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="WORK_USER_HIS_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="WORK_USER_HIS_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_his_id")
	public WorkCompanyHis getWorkCompanyHis() {
		return workCompanyHis;
	}

	public void setWorkCompanyHis(WorkCompanyHis workCompanyHis) {
		this.workCompanyHis = workCompanyHis;
	}

	@Column(name = "address", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "con_cert_number", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getConCertNumber() {
		return this.conCertNumber;
	}

	public void setConCertNumber(String conCertNumber) {
		this.conCertNumber = conCertNumber;
	}

	@Column(name = "con_cert_type", length = 100,columnDefinition = "NVARCHAR2(3)")
	public String getConCertType() {
		return this.conCertType;
	}

	public void setConCertType(String conCertType) {
		this.conCertType = conCertType;
	}

	@Column(name = "contact_email", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "contact_name", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "contact_phone", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column(name = "contact_sex", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactSex() {
		return this.contactSex;
	}

	public void setContactSex(String contactSex) {
		this.contactSex = contactSex;
	}

	@Column(name = "contact_tel", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactTel() {
		return this.contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	@Column(name = "department", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "source")
	public Integer getSource() {
		return this.source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "work_type")
	public Integer getWorkType() {
		return this.workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	@Column(name = "user_sn",columnDefinition = "NVARCHAR2(255)")
	public String getUserSn() {
		return this.userSn;
	}

	public void setUserSn(String userSn) {
		this.userSn = userSn;
	}

}