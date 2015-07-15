package com.itrus.ca.modules.profile.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * 代理商计费策略
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "config_charge_agent")
public class ConfigChargeAgent implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigApp configApp;
	private ConfigProduct configProduct;
	private ConfigCommercialAgent configCommercialAgent;
	private Boolean chargeMethodPos;
	private Boolean chargeMethodMoney;
	private Boolean chargeMethodBank;
	private Boolean chargeMethodAlipay;
	private Boolean chargeMethodGov;
	private Boolean chargeMethodContract;
	private Set<ConfigChargeAgentDetail> configChargeAgentDetails = new HashSet<ConfigChargeAgentDetail>(
			0);
	private String tempName;//模板名称
	private String tempStyle;//模板类型 1通用 2政府统一采购 3合同采购
	
	private Integer configureNum;//配置数量
	private Integer surplusNum;//剩余数量
	private Integer availableNum;//已用数量
	

	// Constructors

	/** default constructor */
	public ConfigChargeAgent() {
	}

	/** full constructor */
	public ConfigChargeAgent(ConfigApp configApp,ConfigProduct configProduct,
			ConfigCommercialAgent configCommercialAgent,
			Boolean chargeMethodPos, Boolean chargeMethodMoney,
			Boolean chargeMethodBank, Boolean chargeMethodAlipay,
			Boolean chargeMethodGov, Boolean chargeMethodContract,
			Set<ConfigChargeAgentDetail> configChargeAgentDetails,
			Integer configureNum,
			Integer surplusNum,
			Integer availableNum
			
			) {
		this.configApp = configApp;
		this.configProduct = configProduct;
		this.configCommercialAgent = configCommercialAgent;
		this.chargeMethodPos = chargeMethodPos;
		this.chargeMethodMoney = chargeMethodMoney;
		this.chargeMethodBank = chargeMethodBank;
		this.chargeMethodAlipay = chargeMethodAlipay;
		this.chargeMethodGov = chargeMethodGov;
		this.chargeMethodContract = chargeMethodContract;
		this.configChargeAgentDetails = configChargeAgentDetails;
		
		this.configureNum=configureNum;
		this.surplusNum=surplusNum;
		this.availableNum=availableNum;
		
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

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "app_id")
	@Transient
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
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

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "agent_id")
	@Transient
	public ConfigCommercialAgent getConfigCommercialAgent() {
		return this.configCommercialAgent;
	}

	public void setConfigCommercialAgent(
			ConfigCommercialAgent configCommercialAgent) {
		this.configCommercialAgent = configCommercialAgent;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configChargeAgent")
	public Set<ConfigChargeAgentDetail> getConfigChargeAgentDetails() {
		return this.configChargeAgentDetails;
	}

	public void setConfigChargeAgentDetails(
			Set<ConfigChargeAgentDetail> configChargeAgentDetails) {
		this.configChargeAgentDetails = configChargeAgentDetails;
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

	@Column(name = "configure_num" , length=11)
	public Integer getConfigureNum() {
		return configureNum;
	}

	public void setConfigureNum(Integer configureNum) {
		this.configureNum = configureNum;
	}

	@Column(name = "surplus_num" , length=11)
	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

	@Column(name = "available_num" , length=11)
	public Integer getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(Integer availableNum) {
		this.availableNum = availableNum;
	}
	
	
	
	
	
}