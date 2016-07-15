package com.itrus.ca.modules.profile.entity;

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
 * ConfigChargeAgentDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_agent_detail_history")
public class ConfigChargeAgentDetailHistory implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigChargeAgentHistory agent;
	private Integer workType;
	private Integer chargeYear;
	private Double money;
	private Integer productType;

	// Constructors

	/** default constructor */
	public ConfigChargeAgentDetailHistory() {
	}

	/** full constructor */
	public ConfigChargeAgentDetailHistory(ConfigChargeAgentHistory agent,
			Integer workType, Integer chargeYear, Double money,
			Integer productType) {
		this.agent = agent;
		this.workType = workType;
		this.chargeYear = chargeYear;
		this.money = money;
		this.productType = productType;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_AGENT_DETAIL__SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_AGENT_DETAIL__SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_AGENT_DETAIL__SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	public ConfigChargeAgentHistory getAgent() {
		return agent;
	}

	public void setAgent(ConfigChargeAgentHistory agent) {
		this.agent = agent;
	}

	
	
	

	
	@Column(name = "work_type")
	public Integer getWorkType() {
		return this.workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	@Column(name = "charge_year")
	public Integer getChargeYear() {
		return this.chargeYear;
	}

	public void setChargeYear(Integer chargeYear) {
		this.chargeYear = chargeYear;
	}

	@Column(name = "money", precision = 20, scale = 3)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "product_type")
	public Integer getProductType() {
		return this.productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

}