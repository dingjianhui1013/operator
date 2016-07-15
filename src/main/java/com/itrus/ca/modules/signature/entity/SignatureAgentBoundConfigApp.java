
package com.itrus.ca.modules.signature.entity;


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
import com.itrus.ca.modules.profile.entity.ConfigApp;

/**
 * 计费策略模版详情历史Entity
 * @author HuHao
 * @version 2016-03-20
 */
@Entity
@Table(name = "signature_agent_bound_app")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SignatureAgentBoundConfigApp extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private SignatureConfigChargeAgent agent;
	private ConfigApp app;

	public SignatureAgentBoundConfigApp() {
		super();
	}

	public SignatureAgentBoundConfigApp(Long id,SignatureConfigChargeAgent agent,ConfigApp app){
		this();
		this.id = id;
		this.agent = agent;
		this.app = app;
	}
	
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="SIGNATURE_AGENT_BOUN_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SIGNATURE_AGENT_BOUN_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "SIGNATURE_AGENT_BOUN_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "agent_id")
	public SignatureConfigChargeAgent getAgent() {
		return agent;
	}

	public void setAgent(SignatureConfigChargeAgent agent) {
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


