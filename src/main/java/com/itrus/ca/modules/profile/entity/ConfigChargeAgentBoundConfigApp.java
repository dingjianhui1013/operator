/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import com.itrus.ca.common.persistence.DataEntity;


/**
 * 应用与签章计费策略模版表Entity
 * @author CYC
 * @version 2015-07-17
 */
@Entity
@Table(name = "config_agent_bound_app")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigChargeAgentBoundConfigApp extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private ConfigChargeAgent agent;
	private ConfigApp app;
	

	public ConfigChargeAgentBoundConfigApp() {
		super();
	}

	public ConfigChargeAgentBoundConfigApp(ConfigChargeAgent agent,ConfigApp app){
		this.agent = agent;
		this.app = app;
	}
	
	
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_AGENT_BOUND_A_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_AGENT_BOUND_A_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_AGENT_BOUND_A_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "agent_id")
	public ConfigChargeAgent getAgent() {
		return agent;
	}

	public void setAgent(ConfigChargeAgent agent) {
		this.agent = agent;
	}

	@ManyToOne
	@JoinColumn(name = "app_id")
	public ConfigApp getApp() {
		return app;
	}

	public void setApp(ConfigApp app) {
		this.app = app;
	}
	
}


