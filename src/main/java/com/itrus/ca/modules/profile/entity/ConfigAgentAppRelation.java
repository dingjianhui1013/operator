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
 * 代理商应用关系
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "config_agent_app_relation")
public class ConfigAgentAppRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigApp configApp;
	private ConfigCommercialAgent configCommercialAgent;

	// Constructors

	/** default constructor */
	public ConfigAgentAppRelation() {
	}

	/** full constructor */
	public ConfigAgentAppRelation(ConfigApp configApp,
			ConfigCommercialAgent configCommercialAgent) {
		this.configApp = configApp;
		this.configCommercialAgent = configCommercialAgent;
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
	@JoinColumn(name = "app_id", nullable = false)
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	public ConfigCommercialAgent getConfigCommercialAgent() {
		return this.configCommercialAgent;
	}

	public void setConfigCommercialAgent(
			ConfigCommercialAgent configCommercialAgent) {
		this.configCommercialAgent = configCommercialAgent;
	}

}