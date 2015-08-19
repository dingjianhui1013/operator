package com.itrus.ca.modules.profile.entity;

import java.util.HashMap;
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
@Table(name = "config_charge_agent_history")
public class ConfigChargeAgentHistory implements java.io.Serializable {

	// Fields

	private Long id;
	private Boolean chargeMethodPos;
	private Boolean chargeMethodMoney;
	private Boolean chargeMethodBank;
//	private Boolean chargeMethodAlipay;
//	private Boolean chargeMethodGov;
//	private Boolean chargeMethodContract;
	private Set<ConfigChargeAgentDetailHistory> configChargeAgentDetailHistorys = new HashSet<ConfigChargeAgentDetailHistory>(
			0);
	private String tempName;//模板名称
	private String tempStyle;//模板类型 1通用 2政府统一采购 3合同采购
	
	private Integer configureNum;//配置数量
	private Integer surplusNum;//剩余数量
	private Integer availableNum;//已用数量
	private Integer reserveNum;//预留数量
	
	private Long superiorId;//计费策略正式编号
	

	private HashMap<String, String> map = new HashMap<String, String>();
	
	
	// Constructors

	/** default constructor */
	public ConfigChargeAgentHistory() {
	}

	/** full constructor */
	public ConfigChargeAgentHistory(
			Boolean chargeMethodPos, Boolean chargeMethodMoney,
			Boolean chargeMethodBank, 
			Set<ConfigChargeAgentDetailHistory> configChargeAgentDetailHistorys,
			Integer configureNum,
			Integer surplusNum,
			Integer availableNum,
			Long superiorId,
			Integer reserveNum
			) {
		
		this.chargeMethodPos = chargeMethodPos;
		this.chargeMethodMoney = chargeMethodMoney;
		this.chargeMethodBank = chargeMethodBank;
		this.configChargeAgentDetailHistorys = configChargeAgentDetailHistorys;
		
		this.configureNum=configureNum;
		this.surplusNum=surplusNum;
		this.availableNum=availableNum;
		this.superiorId = superiorId;
		this.reserveNum = reserveNum;
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
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "agent")
	public Set<ConfigChargeAgentDetailHistory> getConfigChargeAgentDetailHistorys() {
		return configChargeAgentDetailHistorys;
	}

	public void setConfigChargeAgentDetailHistorys(
			Set<ConfigChargeAgentDetailHistory> configChargeAgentDetailHistorys) {
		this.configChargeAgentDetailHistorys = configChargeAgentDetailHistorys;
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

	@Column(name = "superior_id")
	public Long getSuperiorId() {
		return superiorId;
	}

	public void setSuperiorId(Long superiorId) {
		this.superiorId = superiorId;
	}
	
	
	@Column(name = "reserve_num" , length=11)
	public Integer getReserveNum() {
		return reserveNum;
	}

	public void setReserveNum(Integer reserveNum) {
		this.reserveNum = reserveNum;
	}
	
	

	@Transient
	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
	
	
	
	
}