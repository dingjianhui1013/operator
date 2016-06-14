package com.itrus.ca.modules.profile.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * ConfigRaAccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_ra_account")
public class ConfigRaAccount implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigRaAccountExtendInfo configRaAccountExtendInfo;
	private ConfigProduct configProduct;
	private String accountOrgUnit;//RA发证机构单位
	private String accountOrganization;//RA发证机构
	private String accountHash;
	private String raProtocol;//CA类型
	private String raName;//RA模板名称
	private String serviceUrl;//服务地址
	private Boolean isSingleCert;//单双证标识
	private String mobileDeviceUrl;//移动设备服务地址
	private String caName;//CA名称
	private Boolean isTest;
	private String multiCaName;
	private String ip;
	private String port;
	private String aaPass;
	private String keyType;
	private Integer keyLen;
	private String raVersion;
	private String crlListUrl;
	private String aaPassword;
	private Integer defaultOverrideValidity;
	
	
	// Constructors

	/** default constructor */
	public ConfigRaAccount() {
	}

	/** minimal constructor */
	public ConfigRaAccount(String accountOrgUnit, String accountOrganization,
			String raProtocol, String raName, String serviceUrl,
			Integer raCertificateIssuingMode) {
		this.accountOrgUnit = accountOrgUnit;
		this.accountOrganization = accountOrganization;
		this.raProtocol = raProtocol;
		this.raName = raName;
		this.serviceUrl = serviceUrl;
	}

	/** full constructor */
	public ConfigRaAccount(ConfigRaAccountExtendInfo configRaAccountExtendInfo,
			ConfigProduct configProduct, String accountOrgUnit,
			String accountOrganization, String accountHash, String raProtocol,
			String raName, String serviceUrl, Integer raCertificateIssuingMode,
			Boolean isTest, String multiCaName, Boolean isSingleCert,
			String ip, String port, String aaPass, String keyType,
			Integer keyLen, String raVersion, String crlListUrl,
			Integer defaultOverrideValidity) {
		this.configRaAccountExtendInfo = configRaAccountExtendInfo;
		this.configProduct = configProduct;
		this.accountOrgUnit = accountOrgUnit;
		this.accountOrganization = accountOrganization;
		this.accountHash = accountHash;
		this.raProtocol = raProtocol;
		this.raName = raName;
		this.serviceUrl = serviceUrl;
		this.isTest = isTest;
		this.multiCaName = multiCaName;
		this.isSingleCert = isSingleCert;
		this.ip = ip;
		this.port = port;
		this.aaPass = aaPass;
		this.keyType = keyType;
		this.keyLen = keyLen;
		this.raVersion = raVersion;
		this.crlListUrl = crlListUrl;
		this.defaultOverrideValidity = defaultOverrideValidity;
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

	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "extend_info")
	@Transient
	public ConfigRaAccountExtendInfo getConfigRaAccountExtendInfo() {
		return this.configRaAccountExtendInfo;
	}

	public void setConfigRaAccountExtendInfo(
			ConfigRaAccountExtendInfo configRaAccountExtendInfo) {
		this.configRaAccountExtendInfo = configRaAccountExtendInfo;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "product_id")
	@Transient
	public ConfigProduct getConfigProduct() {
		return this.configProduct;
	}

	public void setConfigProduct(ConfigProduct configProduct) {
		this.configProduct = configProduct;
	}

	@Column(name = "account_org_unit", nullable = false)
	public String getAccountOrgUnit() {
		return this.accountOrgUnit;
	}

	public void setAccountOrgUnit(String accountOrgUnit) {
		this.accountOrgUnit = accountOrgUnit;
	}

	@Column(name = "account_organization", nullable = false)
	public String getAccountOrganization() {
		return this.accountOrganization;
	}

	public void setAccountOrganization(String accountOrganization) {
		this.accountOrganization = accountOrganization;
	}

	@Column(name = "account_hash")
	public String getAccountHash() {
		return this.accountHash;
	}

	public void setAccountHash(String accountHash) {
		this.accountHash = accountHash;
	}

	@Column(name = "ra_protocol", nullable = false)
	public String getRaProtocol() {
		return this.raProtocol;
	}

	public void setRaProtocol(String raProtocol) {
		this.raProtocol = raProtocol;
	}

	@Column(name = "ra_name", nullable = false)
	public String getRaName() {
		return this.raName;
	}

	public void setRaName(String raName) {
		this.raName = raName;
	}

	@Column(name = "service_url", nullable = false)
	public String getServiceUrl() {
		return this.serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Column(name = "is_test")
	public Boolean getIsTest() {
		return this.isTest==null? false:this.isTest;
	}

	public void setIsTest(Boolean isTest) {
		this.isTest = isTest;
	}

	@Column(name = "multi_ca_name")
	public String getMultiCaName() {
		return this.multiCaName;
	}

	public void setMultiCaName(String multiCaName) {
		this.multiCaName = multiCaName;
	}

	@Column(name = "is_single_cert")
	public Boolean getIsSingleCert() {
		return this.isSingleCert;
	}

	public void setIsSingleCert(Boolean isSingleCert) {
		this.isSingleCert = isSingleCert;
	}

	@Column(name = "ip")
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "port")
	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "aa_pass")
	public String getAaPass() {
		return this.aaPass;
	}

	public void setAaPass(String aaPass) {
		this.aaPass = aaPass;
	}

	@Column(name = "key_type")
	public String getKeyType() {
		return this.keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	@Column(name = "key_len")
	public Integer getKeyLen() {
		return this.keyLen;
	}

	public void setKeyLen(Integer keyLen) {
		this.keyLen = keyLen;
	}

	@Column(name = "ra_version")
	public String getRaVersion() {
		return this.raVersion;
	}

	public void setRaVersion(String raVersion) {
		this.raVersion = raVersion;
	}

	@Column(name = "crl_list_url")
	public String getCrlListUrl() {
		return this.crlListUrl;
	}

	public void setCrlListUrl(String crlListUrl) {
		this.crlListUrl = crlListUrl;
	}

	@Column(name = "default_override_validity")
	public Integer getDefaultOverrideValidity() {
		return this.defaultOverrideValidity;
	}

	public void setDefaultOverrideValidity(Integer defaultOverrideValidity) {
		this.defaultOverrideValidity = defaultOverrideValidity;
	}

	@Column(name = "aa_password")
	public String getAaPassword() {
		return aaPassword;
	}

	public void setAaPassword(String aaPassword) {
		this.aaPassword = aaPassword;
	}

	@Column(name = "mobile_device_url")
	public String getMobileDeviceUrl() {
		return mobileDeviceUrl;
	}

	public void setMobileDeviceUrl(String mobileDeviceUrl) {
		this.mobileDeviceUrl = mobileDeviceUrl;
	}
	@Column(name = "ca_name" ,nullable = false)
	public String getCaName() {
		return caName;
	}

	public void setCaName(String caName) {
		this.caName = caName;
	}
}