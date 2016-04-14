package com.itrus.ca.modules.signature.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.profile.entity.ConfigApp;



/**
 * 签章计费策略
 * 
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "signature_config_charge_agent")
public class SignatureConfigChargeAgent extends DataEntity implements java.io.Serializable {

	private static final long serialVersionUID = 2232504676339799607L;
	private Long id;
	private ConfigApp configApp;

	private Boolean chargeMethodPos;
	private Boolean chargeMethodMoney;
	private Set<SignatureConfigChargeAgentDetail> signatureconfigChargeAgentDetails = new HashSet<SignatureConfigChargeAgentDetail>(
			0);
	private String tempName;// 模板名称
	private String isBind;
	
	private Long boundId;

	// Constructors

	/** default constructor */
	public SignatureConfigChargeAgent() {
	}

	/** full constructor */
	public SignatureConfigChargeAgent(ConfigApp configApp, 
			Boolean chargeMethodPos, 
			Boolean chargeMethodMoney,
			Set<SignatureConfigChargeAgentDetail> signatureconfigChargeAgentDetails,
			String tempName,String isBind
			) {
		this.configApp = configApp;
		this.chargeMethodPos = chargeMethodPos;
		this.chargeMethodMoney = chargeMethodMoney;
		this.signatureconfigChargeAgentDetails = signatureconfigChargeAgentDetails;
		this.tempName = tempName;
		this.isBind = isBind;
		

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

	@Transient
	public String getIsBind() {
		return isBind;
	}

	public void setIsBind(String isBind) {
		this.isBind = isBind;
	}

	@Transient
	public Long getBoundId() {
		return boundId;
	}

	public void setBoundId(Long boundId) {
		this.boundId = boundId;
	}
	
}