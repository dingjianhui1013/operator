package com.itrus.ca.modules.profile.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itrus.ca.modules.sys.entity.User;

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
	private String tempStyle;//模板类型 1标准 2政府统一采购 3合同采购
	
	private Integer configureNum;//配置新增数量
	private Integer configureUpdateNum;//配置更新数量
	private Date htStartTime;//开始时间
	private Date htEndTime;//结束时间
	
	
	private Integer surplusNum;//剩余新增数量
	private Integer surplusUpdateNum;//剩余更新数量
	
	private Integer availableNum;//已用新增数量
	private Integer availableUpdateNum;//已用更新数量
	
	
	private Integer reserveNum;//预留数量
	private Integer reserveUpdateNum;//预留更新数量
	
	//16.6.11
	private Integer isSupportExpirationDate;//是否支持指定证书到期时间
	
	private String isBind;

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
			Integer availableNum,
			Integer reserveNum,
			Date createDate,
			User createBy,
			Integer configureUpdateNum,
			Date htStartTime,
			Date htEndTime,
			Integer surplusUpdateNum,
			Integer availableUpdateNum,
			Integer reserveUpdateNum
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
		this.reserveNum=reserveNum;
		
		this.configureUpdateNum=configureUpdateNum;
		this.htStartTime=htStartTime;
		this.htEndTime=htEndTime;
		this.surplusUpdateNum=surplusUpdateNum;
		this.availableUpdateNum=availableUpdateNum;
		this.reserveUpdateNum=reserveUpdateNum;
		
		
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_CHARGE_AGENT_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_CHARGE_AGENT_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_CHARGE_AGENT_SEQUENCE",strategy=GenerationType.SEQUENCE)
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
	
	@Column(name = "reserve_num" , length=11)
	public Integer getReserveNum() {
		return reserveNum;
	}

	public void setReserveNum(Integer reserveNum) {
		this.reserveNum = reserveNum;
	}
	

	
	
	
	@Column(name = "config_update_num" , length=11)
	public Integer getConfigureUpdateNum() {
		return configureUpdateNum;
	}

	public void setConfigureUpdateNum(Integer configureUpdateNum) {
		this.configureUpdateNum = configureUpdateNum;
	}

	@Column(name = "ht_start_time")
	public Date getHtStartTime() {
		return htStartTime;
	}

	public void setHtStartTime(Date htStartTime) {
		this.htStartTime = htStartTime;
	}

	@Column(name = "ht_end_time")
	public Date getHtEndTime() {
		return htEndTime;
	}

	public void setHtEndTime(Date htEndTime) {
		this.htEndTime = htEndTime;
	}

	
	@Column(name = "surplus_update_num" , length=11)
	public Integer getSurplusUpdateNum() {
		return surplusUpdateNum;
	}

	public void setSurplusUpdateNum(Integer surplusUpdateNum) {
		this.surplusUpdateNum = surplusUpdateNum;
	}

	@Column(name = "available_update_num" , length=11)
	public Integer getAvailableUpdateNum() {
		return availableUpdateNum;
	}

	public void setAvailableUpdateNum(Integer availableUpdateNum) {
		this.availableUpdateNum = availableUpdateNum;
	}

	@Column(name = "reserve_update_num" , length=11)
	public Integer getReserveUpdateNum() {
		return reserveUpdateNum;
	}

	public void setReserveUpdateNum(Integer reserveUpdateNum) {
		this.reserveUpdateNum = reserveUpdateNum;
	}
	
	
	
	
	@Column(name = "is_support_expiration_date")
	public void setIsSupportExpirationDate(Integer isSupportExpirationDate) {
		this.isSupportExpirationDate = isSupportExpirationDate;
	}

	public void setIsBind(String isBind) {
		this.isBind = isBind;
	}

	@Transient
	public String getIsBind() {
		return isBind;
	}

	public Integer getIsSupportExpirationDate() {
		return isSupportExpirationDate;
	}

	

	
}