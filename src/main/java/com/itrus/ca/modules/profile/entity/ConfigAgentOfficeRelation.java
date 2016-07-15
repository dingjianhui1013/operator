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

import com.itrus.ca.modules.sys.entity.Office;

/**
 * ConfigAgentOfficeRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_agent_office_relation")
public class ConfigAgentOfficeRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private ConfigCommercialAgent configCommercialAgent;

	// Constructors

	/** default constructor */
	public ConfigAgentOfficeRelation() {
	}

	/** full constructor */
	public ConfigAgentOfficeRelation(Office sysOffice,
			ConfigCommercialAgent configCommercialAgent) {
		this.sysOffice = sysOffice;
		this.configCommercialAgent = configCommercialAgent;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_AGENT_OFFICE__SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_AGENT_OFFICE__SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_AGENT_OFFICE__SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	public ConfigCommercialAgent getConfigCommercialAgent() {
		return this.configCommercialAgent;
	}

	public void setConfigCommercialAgent(
			ConfigCommercialAgent configCommercialAgent) {
		this.configCommercialAgent = configCommercialAgent;
	}

}