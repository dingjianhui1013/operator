package com.itrus.ca.modules.profile.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itrus.ca.modules.sys.entity.User;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * 代理商计费策略
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "config_charge_agent_history")
public class ConfigChargeAgentHistory implements java.io.Serializable {

	// Fields

	private Long id;
	private Boolean chargeMethodPos;
	private Boolean chargeMethodMoney;
	private Boolean chargeMethodBank;
//	private Boolean chargeMethodAlipay;
//	private Boolean chargeMethodGov;
//	private Boolean chargeMethodContract;
	private Set<ConfigChargeAgentDetailHistory> configChargeAgentDetailHistorys = new HashSet<ConfigChargeAgentDetailHistory>(
			0);
	private String tempName;//模板名称
	private String tempStyle;//模板类型 1通用 2政府统一采购 3合同采购
	
	private Integer configureNum;//配置新增数量
	private Integer configureUpdateNum;//配置更新数量
	private Date htStartTime;//开始时间
	private Date htEndTime;//结束时间
	
	
	private Integer surplusNum;//新增剩余数量
	private Integer surplusUpdateNum;//剩余更新数量
	private Integer availableNum;//已用新增数量
	private Integer availableUpdateNum;//已用更新数量
	private Integer reserveNum;//新增预留数量
	private Integer reserveUpdateNum;//预留更新数量
	
	private Long superiorId;//计费策略正式编号
	
	private Date createDate; //创建人
	private User createBy;//创建时间
	

	private HashMap<String, String> map = new HashMap<String, String>();
	
	
	// Constructors

	/** default constructor */
	public ConfigChargeAgentHistory() {
	}

	/** full constructor */
	public ConfigChargeAgentHistory(
			Boolean chargeMethodPos, Boolean chargeMethodMoney,
			Boolean chargeMethodBank, 
			Set<ConfigChargeAgentDetailHistory> configChargeAgentDetailHistorys,
			Integer configureNum,
			Integer surplusNum,
			Integer availableNum,
			Long superiorId,
			Integer reserveNum,
			Date createDate,
			User createBy,
			Integer configureUpdateNum,
			Date htStartTime,
			Date htEndTime,
			Integer surplusUpdateNum,
			Integer availableUpdateNum,
			Integer reserveUpdateNum
			) {
		
		this.chargeMethodPos = chargeMethodPos;
		this.chargeMethodMoney = chargeMethodMoney;
		this.chargeMethodBank = chargeMethodBank;
		this.configChargeAgentDetailHistorys = configChargeAgentDetailHistorys;
		
		this.configureNum=configureNum;
		this.surplusNum=surplusNum;
		this.availableNum=availableNum;
		this.superiorId = superiorId;
		this.reserveNum = reserveNum;
		this.createDate = createDate;
		this.createBy = createBy;
		this.configureUpdateNum=configureUpdateNum;
		this.htStartTime=htStartTime;
		this.htEndTime=htEndTime;
		this.surplusUpdateNum=surplusUpdateNum;
		this.availableUpdateNum=availableUpdateNum;
		this.reserveUpdateNum=reserveUpdateNum;
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

	@Column(name = "charge_method_bank")
	public Boolean getChargeMethodBank() {
		return this.chargeMethodBank;
	}

	public void setChargeMethodBank(Boolean chargeMethodBank) {
		this.chargeMethodBank = chargeMethodBank;
	}
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "agent")
	public Set<ConfigChargeAgentDetailHistory> getConfigChargeAgentDetailHistorys() {
		return configChargeAgentDetailHistorys;
	}

	public void setConfigChargeAgentDetailHistorys(
			Set<ConfigChargeAgentDetailHistory> configChargeAgentDetailHistorys) {
		this.configChargeAgentDetailHistorys = configChargeAgentDetailHistorys;
	}
	
	
	

	@Column(name = "temp_name")
	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	@Column(name = "temp_style")
	public String getTempStyle() {
		return tempStyle;
	}

	public void setTempStyle(String tempStyle) {
		this.tempStyle = tempStyle;
	}

	@Column(name = "configure_num" , length=11)
	public Integer getConfigureNum() {
		return configureNum;
	}

	public void setConfigureNum(Integer configureNum) {
		this.configureNum = configureNum;
	}

	@Column(name = "surplus_num" , length=11)
	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

	@Column(name = "available_num" , length=11)
	public Integer getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(Integer availableNum) {
		this.availableNum = availableNum;
	}

	@Column(name = "superior_id")
	public Long getSuperiorId() {
		return superiorId;
	}

	public void setSuperiorId(Long superiorId) {
		this.superiorId = superiorId;
	}
	
	
	@Column(name = "reserve_num" , length=11)
	public Integer getReserveNum() {
		return reserveNum;
	}

	public void setReserveNum(Integer reserveNum) {
		this.reserveNum = reserveNum;
	}
	
	

	@Transient
	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "config_update_num" , length=11)
	public Integer getConfigureUpdateNum() {
		return configureUpdateNum;
	}

	public void setConfigureUpdateNum(Integer configureUpdateNum) {
		this.configureUpdateNum = configureUpdateNum;
	}

	@Column(name = "ht_start_time")
	public Date getHtStartTime() {
		return htStartTime;
	}

	public void setHtStartTime(Date htStartTime) {
		this.htStartTime = htStartTime;
	}

	@Column(name = "ht_end_time")
	public Date getHtEndTime() {
		return htEndTime;
	}

	public void setHtEndTime(Date htEndTime) {
		this.htEndTime = htEndTime;
	}

	
	@Column(name = "surplus_update_num" , length=11)
	public Integer getSurplusUpdateNum() {
		return surplusUpdateNum;
	}

	public void setSurplusUpdateNum(Integer surplusUpdateNum) {
		this.surplusUpdateNum = surplusUpdateNum;
	}

	@Column(name = "available_update_num" , length=11)
	public Integer getAvailableUpdateNum() {
		return availableUpdateNum;
	}

	public void setAvailableUpdateNum(Integer availableUpdateNum) {
		this.availableUpdateNum = availableUpdateNum;
	}

	@Column(name = "reserve_update_num" , length=11)
	public Integer getReserveUpdateNum() {
		return reserveUpdateNum;
	}

	public void setReserveUpdateNum(Integer reserveUpdateNum) {
		this.reserveUpdateNum = reserveUpdateNum;
	}

	
	
}