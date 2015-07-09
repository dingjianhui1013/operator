package com.itrus.ca.modules.profile.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 代理商
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "config_commercial_agent")
public class ConfigCommercialAgent implements java.io.Serializable {

	// Fields

	private Long id;
	private String agentName;
	private Boolean agentType1;
	private Boolean agentType2;
	private String agentCommUserName;
	private String agentCommMobile;
	private String agentAddress;
	private Timestamp agentContractStart;
	private Timestamp agentContractEnd;
	private String agentRemark;
	private Integer del_flag;
	private Set<ConfigAgentOfficeRelation> configAgentOfficeRelations = new HashSet<ConfigAgentOfficeRelation>(
			0);
	private Set<ConfigAgentAppRelation> configAgentAppRelations = new HashSet<ConfigAgentAppRelation>(
			0);

	// Constructors

	/** default constructor */
	public ConfigCommercialAgent() {
	}

	/** minimal constructor */
	public ConfigCommercialAgent(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ConfigCommercialAgent(Long id, String agentName, Boolean agentType1,
			Boolean agentType2, String agentCommUserName,
			String agentCommMobile, String agentAddress,
			Timestamp agentContractStart, Timestamp agentContractEnd,
			String agentRemark,
			Set<ConfigAgentOfficeRelation> configAgentOfficeRelations,
			Set<ConfigAgentAppRelation> configAgentAppRelations) {
		this.id = id;
		this.agentName = agentName;
		this.agentType1 = agentType1;
		this.agentType2 = agentType2;
		this.agentCommUserName = agentCommUserName;
		this.agentCommMobile = agentCommMobile;
		this.agentAddress = agentAddress;
		this.agentContractStart = agentContractStart;
		this.agentContractEnd = agentContractEnd;
		this.agentRemark = agentRemark;
		this.configAgentOfficeRelations = configAgentOfficeRelations;
		this.configAgentAppRelations = configAgentAppRelations;
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

	@Column(name = "agent_name")
	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Column(name = "agent_type_1")
	public Boolean getAgentType1() {
		return this.agentType1;
	}

	public void setAgentType1(Boolean agentType1) {
		this.agentType1 = agentType1;
	}

	@Column(name = "agent_type_2")
	public Boolean getAgentType2() {
		return this.agentType2;
	}

	public void setAgentType2(Boolean agentType2) {
		this.agentType2 = agentType2;
	}

	@Column(name = "agent_comm_user_name")
	public String getAgentCommUserName() {
		return this.agentCommUserName;
	}

	public void setAgentCommUserName(String agentCommUserName) {
		this.agentCommUserName = agentCommUserName;
	}

	@Column(name = "agent_comm_mobile")
	public String getAgentCommMobile() {
		return this.agentCommMobile;
	}

	public void setAgentCommMobile(String agentCommMobile) {
		this.agentCommMobile = agentCommMobile;
	}

	@Column(name = "agent_address")
	public String getAgentAddress() {
		return this.agentAddress;
	}

	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}

	@Column(name = "agent_contract_start", length = 19)
	public Timestamp getAgentContractStart() {
		return this.agentContractStart;
	}

	public void setAgentContractStart(Timestamp agentContractStart) {
		this.agentContractStart = agentContractStart;
	}

	@Column(name = "agent_contract_end", length = 19)
	public Timestamp getAgentContractEnd() {
		return this.agentContractEnd;
	}

	public void setAgentContractEnd(Timestamp agentContractEnd) {
		this.agentContractEnd = agentContractEnd;
	}

	@Column(name = "agent_remark")
	public String getAgentRemark() {
		return this.agentRemark;
	}

	public void setAgentRemark(String agentRemark) {
		this.agentRemark = agentRemark;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configCommercialAgent")
	public Set<ConfigAgentOfficeRelation> getConfigAgentOfficeRelations() {
		return this.configAgentOfficeRelations;
	}

	public void setConfigAgentOfficeRelations(
			Set<ConfigAgentOfficeRelation> configAgentOfficeRelations) {
		this.configAgentOfficeRelations = configAgentOfficeRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configCommercialAgent")
	public Set<ConfigAgentAppRelation> getConfigAgentAppRelations() {
		return this.configAgentAppRelations;
	}

	public void setConfigAgentAppRelations(
			Set<ConfigAgentAppRelation> configAgentAppRelations) {
		this.configAgentAppRelations = configAgentAppRelations;
	}

	@Column(name = "del_flag")
	public Integer getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(Integer del_flag) {
		this.del_flag = del_flag;
	}
}