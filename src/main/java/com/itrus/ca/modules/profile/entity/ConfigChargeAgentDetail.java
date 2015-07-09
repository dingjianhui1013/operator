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
@Table(name = "config_charge_agent_detail")
public class ConfigChargeAgentDetail implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigChargeAgent configChargeAgent;
	private Integer workType;
	private Integer chargeYear;
	private Double money;
	private Integer productType;

	// Constructors

	/** default constructor */
	public ConfigChargeAgentDetail() {
	}

	/** full constructor */
	public ConfigChargeAgentDetail(ConfigChargeAgent configChargeAgent,
			Integer workType, Integer chargeYear, Double money,
			Integer productType) {
		this.configChargeAgent = configChargeAgent;
		this.workType = workType;
		this.chargeYear = chargeYear;
		this.money = money;
		this.productType = productType;
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
	@JoinColumn(name = "charge_agent_id")
	public ConfigChargeAgent getConfigChargeAgent() {
		return this.configChargeAgent;
	}

	public void setConfigChargeAgent(ConfigChargeAgent configChargeAgent) {
		this.configChargeAgent = configChargeAgent;
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