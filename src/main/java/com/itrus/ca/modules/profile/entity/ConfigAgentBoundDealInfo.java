/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 产品绑定计费策略模版表Entity
 * @author HuHao
 * @version 2015-07-17
 */
@Entity
@Table(name = "config_agent_bound_deal_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigAgentBoundDealInfo extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private ConfigChargeAgent agent;
	private WorkDealInfo dealInfo;
	

	public ConfigAgentBoundDealInfo() {
		super();
	}

	public ConfigAgentBoundDealInfo(ConfigChargeAgent agent,WorkDealInfo dealInfo){
		this.agent = agent;
		this.dealInfo = dealInfo;
	}
	
	
	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "COMMON_SEQUENCE",strategy=GenerationType.SEQUENCE)
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
	@JoinColumn(name = "deal_info")
	public WorkDealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(WorkDealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

}


