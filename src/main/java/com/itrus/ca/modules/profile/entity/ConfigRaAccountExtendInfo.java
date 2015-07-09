package com.itrus.ca.modules.profile.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * ConfigRaAccountExtendInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_ra_account_extend_info")
public class ConfigRaAccountExtendInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String certName;
	private String commonNameDisplayName;
	private String nameDisplayName;//	3.组织机构代码 4.经办人身份证号
	private String orgunitDisplayName;
	private String emailDisplayName;
	private String addtionalField1DisplayName;
	private String addtionalField2DisplayName;
	private String addtionalField3DisplayName;
	private String addtionalField4DisplayName;
	private String addtionalField5DisplayName;
	private String addtionalField6DisplayName;
	private String addtionalField7DisplayName;
	private String addtionalField8DisplayName;
	private String addtionalField9DisplayName;
	private String addtionalField10DisplayName;
	private String personal;
	private String organization;
	private String organizationDisplayName;
	
	private Set<ConfigRaAccount> configRaAccounts = new HashSet<ConfigRaAccount>(
			0);

	// Constructors

	/** default constructor */
	public ConfigRaAccountExtendInfo() {
	}

	/** full constructor */
	public ConfigRaAccountExtendInfo(
			String certName,
			String commonNameDisplayName,
			String nameDisplayName,
			String orgunitDisplayName,
			String emailDisplayName,
			String addtionalField1DisplayName,
			String addtionalField2DisplayName,
			String addtionalField3DisplayName,
			String addtionalField4DisplayName,
			String addtionalField5DisplayName,
			String addtionalField6DisplayName,
			String addtionalField7DisplayName,
			String addtionalField8DisplayName,
			String addtionalField9DisplayName,
			String addtionalField10DisplayName,
			String personal,
			String organization,
			String organizationDisplayName,
			Set<ConfigRaAccount> configRaAccounts 
			) {
		
		this.personal = personal;
		this.organization = organization;
		this.certName = certName;
		this.orgunitDisplayName = orgunitDisplayName;
		this.commonNameDisplayName = commonNameDisplayName;
		this.emailDisplayName = emailDisplayName;
		this.nameDisplayName = nameDisplayName;
		this.addtionalField1DisplayName = addtionalField1DisplayName;
		this.addtionalField2DisplayName = addtionalField2DisplayName;
		this.addtionalField3DisplayName = addtionalField3DisplayName;
		this.addtionalField4DisplayName = addtionalField4DisplayName;
		this.addtionalField5DisplayName = addtionalField5DisplayName;
		this.addtionalField6DisplayName = addtionalField6DisplayName;
		this.addtionalField7DisplayName = addtionalField7DisplayName;
		this.addtionalField8DisplayName = addtionalField8DisplayName;
		this.addtionalField9DisplayName = addtionalField9DisplayName;
		this.addtionalField10DisplayName = addtionalField10DisplayName;
		this.configRaAccounts = configRaAccounts;
		this.organizationDisplayName = organizationDisplayName;
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
	
	@Column(name = "cert_name")
	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	@Column(name = "orgunit_display_name")
	public String getOrgunitDisplayName() {
		return this.orgunitDisplayName;
	}

	public void setOrgunitDisplayName(String orgunitDisplayName) {
		this.orgunitDisplayName = orgunitDisplayName;
	}

	@Column(name = "common_name_display_name")
	public String getCommonNameDisplayName() {
		return this.commonNameDisplayName;
	}

	public void setCommonNameDisplayName(String commonNameDisplayName) {
		this.commonNameDisplayName = commonNameDisplayName;
	}

	@Column(name = "email_display_name")
	public String getEmailDisplayName() {
		return this.emailDisplayName;
	}

	public void setEmailDisplayName(String emailDisplayName) {
		this.emailDisplayName = emailDisplayName;
	}

	@Column(name = "name_display_name")
	public String getNameDisplayName() {
		return this.nameDisplayName;
	}

	public void setNameDisplayName(String nameDisplayName) {
		this.nameDisplayName = nameDisplayName;
	}

	@Column(name = "addtional_field1_display_name")
	public String getAddtionalField1DisplayName() {
		return this.addtionalField1DisplayName;
	}

	public void setAddtionalField1DisplayName(String addtionalField1DisplayName) {
		this.addtionalField1DisplayName = addtionalField1DisplayName;
	}

	@Column(name = "addtional_field2_display_name")
	public String getAddtionalField2DisplayName() {
		return this.addtionalField2DisplayName;
	}

	public void setAddtionalField2DisplayName(String addtionalField2DisplayName) {
		this.addtionalField2DisplayName = addtionalField2DisplayName;
	}

	@Column(name = "addtional_field3_display_name")
	public String getAddtionalField3DisplayName() {
		return this.addtionalField3DisplayName;
	}

	public void setAddtionalField3DisplayName(String addtionalField3DisplayName) {
		this.addtionalField3DisplayName = addtionalField3DisplayName;
	}

	@Column(name = "addtional_field4_display_name")
	public String getAddtionalField4DisplayName() {
		return this.addtionalField4DisplayName;
	}

	public void setAddtionalField4DisplayName(String addtionalField4DisplayName) {
		this.addtionalField4DisplayName = addtionalField4DisplayName;
	}

	@Column(name = "addtional_field5_display_name")
	public String getAddtionalField5DisplayName() {
		return this.addtionalField5DisplayName;
	}

	public void setAddtionalField5DisplayName(String addtionalField5DisplayName) {
		this.addtionalField5DisplayName = addtionalField5DisplayName;
	}

	@Column(name = "addtional_field6_display_name")
	public String getAddtionalField6DisplayName() {
		return this.addtionalField6DisplayName;
	}

	public void setAddtionalField6DisplayName(String addtionalField6DisplayName) {
		this.addtionalField6DisplayName = addtionalField6DisplayName;
	}

	@Column(name = "addtional_field7_display_name")
	public String getAddtionalField7DisplayName() {
		return this.addtionalField7DisplayName;
	}

	public void setAddtionalField7DisplayName(String addtionalField7DisplayName) {
		this.addtionalField7DisplayName = addtionalField7DisplayName;
	}

	@Column(name = "addtional_field8_display_name")
	public String getAddtionalField8DisplayName() {
		return this.addtionalField8DisplayName;
	}

	public void setAddtionalField8DisplayName(String addtionalField8DisplayName) {
		this.addtionalField8DisplayName = addtionalField8DisplayName;
	}

	@Column(name = "addtional_field9_display_name")
	public String getAddtionalField9DisplayName() {
		return this.addtionalField9DisplayName;
	}

	public void setAddtionalField9DisplayName(String addtionalField9DisplayName) {
		this.addtionalField9DisplayName = addtionalField9DisplayName;
	}

	@Column(name = "addtional_field10_display_name")
	public String getAddtionalField10DisplayName() {
		return this.addtionalField10DisplayName;
	}

	public void setAddtionalField10DisplayName(
			String addtionalField10DisplayName) {
		this.addtionalField10DisplayName = addtionalField10DisplayName;
	}

	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configRaAccountExtendInfo")
	@Transient
	public Set<ConfigRaAccount> getConfigRaAccounts() {
		return this.configRaAccounts;
	}

	public void setConfigRaAccounts(Set<ConfigRaAccount> configRaAccounts) {
		this.configRaAccounts = configRaAccounts;
	}


	@Column(name = "personal")
	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	@Column(name = "organization")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Column(name = "organization_display_name")
	public String getOrganizationDisplayName() {
		return organizationDisplayName;
	}

	public void setOrganizationDisplayName(String organizationDisplayName) {
		this.organizationDisplayName = organizationDisplayName;
	}
	
}