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
import com.itrus.ca.modules.work.entity.WorkLog;

/**
 * 年限结算保存Entity
 * @author DingJianHui
 * @version 2016-03-07
 */
@Entity
@Table(name = "settlement_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SettlementLog extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private String comagentName; //代理商
	private String appName;		// 应用名称
	private String productName;// 产品名称
	private Date startTime;// 业务办理开始时间
	private Date endTime;//	业务办理结束时间
	private String userName;//结算人员
	private String comagentValidity;//代理商有效期
	private Set<WorkDealInfo_settlementLog> workDealInfos = new HashSet<WorkDealInfo_settlementLog>(0);// 满足查询项的业务id

	public SettlementLog() {
		super();
	}
	public SettlementLog(Long id, String comagentName, String appName,
			String productName, Date startTime, Date endTime, String userName,
			String comagentValidity, Set<WorkDealInfo_settlementLog> workDealInfos) {
		super();
		this.id = id;
		this.comagentName = comagentName;
		this.appName = appName;
		this.productName = productName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.userName = userName;
		this.comagentValidity = comagentValidity;
		this.workDealInfos = workDealInfos;
	}
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTLEMENT_LOG_SEQUENCE")
	//@SequenceGenerator(name = "seq_settle_settlementLog", sequenceName = "seq_settle_settlementLog")
	@SequenceGenerator(name="SETTLEMENT_LOG_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SETTLEMENT_LOG_SEQUENCE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "comagent_name", length = 100)
	public String getComagentName() {
		return comagentName;
	}

	public void setComagentName(String comagentName) {
		this.comagentName = comagentName;
	}
	
	@Column(name = "app_name",length = 100)
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Column(name = "product_name")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@OneToMany(fetch = FetchType.EAGER,targetEntity = WorkDealInfo_settlementLog.class,cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "work_deal_infos_id")
	public Set<WorkDealInfo_settlementLog> getWorkDealInfos() {
		return workDealInfos;
	}

	public void setWorkDealInfos(Set<WorkDealInfo_settlementLog> workDealInfos) {
		this.workDealInfos = workDealInfos;
	}
	
	@Column(name = "user_name",length=100)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "comagent_validity")
	public String getComagentValidity() {
		return comagentValidity;
	}
	public void setComagentValidity(String comagentValidity) {
		this.comagentValidity = comagentValidity;
	}
	
	
}


