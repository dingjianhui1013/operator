package com.itrus.ca.modules.signature.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.User;



/**
 * 代理商计费策略
 * 
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "signature_config_charge_agent")
public class SignatureConfigChargeAgent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2232504676339799607L;
	private Long id;
	private ConfigApp configApp;

	private Boolean chargeMethodPos;
	private Boolean chargeMethodMoney;
	private Boolean chargeMethodBank;
	private Boolean chargeMethodAlipay;
	private Boolean chargeMethodGov;
	private Boolean chargeMethodContract;
	private Set<SignatureConfigChargeAgentDetail> signatureconfigChargeAgentDetails = new HashSet<SignatureConfigChargeAgentDetail>(
			0);
	private String tempName;// 模板名称
	private String tempStyle;// 模板类型 1标准 2政府统一采购 3合同采购

	private String isBind;

	// Constructors

	/** default constructor */
	public SignatureConfigChargeAgent() {
	}

	/** full constructor */
	public SignatureConfigChargeAgent(ConfigApp configApp, Boolean chargeMethodPos, Boolean chargeMethodMoney,
			Boolean chargeMethodBank, Boolean chargeMethodAlipay, Boolean chargeMethodGov, Boolean chargeMethodContract,
			Set<SignatureConfigChargeAgentDetail> signatureconfigChargeAgentDetails,
			Date createDate, User createBy
			) {
		this.configApp = configApp;

		this.chargeMethodPos = chargeMethodPos;
		this.chargeMethodMoney = chargeMethodMoney;
		this.chargeMethodBank = chargeMethodBank;
		this.chargeMethodAlipay = chargeMethodAlipay;
		this.chargeMethodGov = chargeMethodGov;
		this.chargeMethodContract = chargeMethodContract;
		this.signatureconfigChargeAgentDetails = signatureconfigChargeAgentDetails;

	}

	// Property accessors
	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@Id
	@GeneratedValue(generator = "COMMON_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Transient
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@Column(name = "charge_method_pos")
	public Boolean getChargeMethodPos() {
		return this.chargeMethodPos;
	}

	public void setChargeMethodPos(Boolean chargeMethodPos) {
		this.chargeMethodPos = chargeMethodPos;
	}

	@Column(name = "charge_method_money")
	public Boolean getChargeMethodMoney() {
		return this.chargeMethodMoney;
	}

	public void setChargeMethodMoney(Boolean chargeMethodMoney) {
		this.chargeMethodMoney = chargeMethodMoney;
	}

	@Column(name = "charge_method_bank")
	public Boolean getChargeMethodBank() {
		return this.chargeMethodBank;
	}

	public void setChargeMethodBank(Boolean chargeMethodBank) {
		this.chargeMethodBank = chargeMethodBank;
	}

	@Column(name = "charge_method_alipay")
	public Boolean getChargeMethodAlipay() {
		return this.chargeMethodAlipay;
	}

	public void setChargeMethodAlipay(Boolean chargeMethodAlipay) {
		this.chargeMethodAlipay = chargeMethodAlipay;
	}

	@Column(name = "charge_method_gov")
	public Boolean getChargeMethodGov() {
		return this.chargeMethodGov;
	}

	public void setChargeMethodGov(Boolean chargeMethodGov) {
		this.chargeMethodGov = chargeMethodGov;
	}

	@Column(name = "charge_method_contract")
	public Boolean getChargeMethodContract() {
		return this.chargeMethodContract;
	}

	public void setChargeMethodContract(Boolean chargeMethodContract) {
		this.chargeMethodContract = chargeMethodContract;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "signatureConfigChargeAgent")
	public Set<SignatureConfigChargeAgentDetail> getSignatureConfigChargeAgentDetails() {
		return this.signatureconfigChargeAgentDetails;
	}

	public void setSignatureConfigChargeAgentDetails(Set<SignatureConfigChargeAgentDetail> SignatureconfigChargeAgentDetails) {
		this.signatureconfigChargeAgentDetails = SignatureconfigChargeAgentDetails;
	}

	@Column(name = "temp_name")
	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	@Column(name = "temp_style")
	public String getTempStyle() {
		return tempStyle;
	}

	public void setTempStyle(String tempStyle) {
		this.tempStyle = tempStyle;
	}

	@Transient
	public String getIsBind() {
		return isBind;
	}

	public void setIsBind(String isBind) {
		this.isBind = isBind;
	}

}