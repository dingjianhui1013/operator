/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import com.itrus.ca.modules.work.entity.WorkLog;

/**
 * 年限结算保存Entity
 * @author DingJianHui
 * @version 2016-03-07
 */
@Entity
@Table(name = "work_deal_Info_settlement_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkDealInfo_settlementLog extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private Long workDealInfoId;
	public WorkDealInfo_settlementLog() {
		super();
	}

	public WorkDealInfo_settlementLog(Long id, String comagentName, String appName,
			String productName, Date startTime, Date endTime, String userName,
			Set<WorkDealInfo> workDealInfos) {
		super();
		this.id = id;
	}

	public WorkDealInfo_settlementLog(Long id, Long workDealInfoId) {
		super();
		this.id = id;
		this.workDealInfoId = workDealInfoId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_settle_settlementLog")
	//@SequenceGenerator(name = "seq_settle_settlementLog", sequenceName = "seq_settle_settlementLog")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "work_deal_inf_id")
	public Long getWorkDealInfoId() {
		return workDealInfoId;
	}
	public void setWorkDealInfoId(Long workDealInfoId) {
		this.workDealInfoId = workDealInfoId;
	}
	

	
}


