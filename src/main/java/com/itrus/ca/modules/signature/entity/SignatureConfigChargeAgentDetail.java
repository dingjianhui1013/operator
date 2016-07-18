package com.itrus.ca.modules.signature.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ConfigChargeAgentDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "signature_agent_detail")
public class SignatureConfigChargeAgentDetail implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 7357037691944741085L;

	private Long id;
	private SignatureConfigChargeAgent signatureConfigChargeAgent;
	private Integer workType;
	private Integer chargeYear;
	private Double money;
	private Integer productType;

	// Constructors

	/** default constructor */
	public SignatureConfigChargeAgentDetail() {
	}

	/** full constructor */
	public SignatureConfigChargeAgentDetail(
			SignatureConfigChargeAgent signatureconfigChargeAgent,
			Integer workType, Integer chargeYear, Double money,
			Integer productType) {
		this.signatureConfigChargeAgent = signatureconfigChargeAgent;
		this.workType = workType;
		this.chargeYear = chargeYear;
		this.money = money;
		this.productType = productType;
	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "SIGNATURE_AGENT_DETA_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "SIGNATURE_AGENT_DETA_SEQUENCE")
	@Id
	@GeneratedValue(generator = "SIGNATURE_AGENT_DETA_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "signature_charge_agent_id")
	public SignatureConfigChargeAgent getSignatureConfigChargeAgent() {
		return this.signatureConfigChargeAgent;
	}

	public void setSignatureConfigChargeAgent(
			SignatureConfigChargeAgent signatureconfigChargeAgent) {
		this.signatureConfigChargeAgent = signatureconfigChargeAgent;
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

	@Column(name = "money", precision = 20, scale = 3, columnDefinition = "FLOAT(126)")
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