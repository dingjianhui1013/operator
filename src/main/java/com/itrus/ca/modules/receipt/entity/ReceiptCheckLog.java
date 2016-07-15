package com.itrus.ca.modules.receipt.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.itrus.ca.modules.sys.entity.Office;

/**
 * 发票盘点记录
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "receipt_check_log")
public class ReceiptCheckLog implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice; //网点
	private ReceiptDepotInfo receiptDepotInfo; //库房
	private Timestamp checkTime; //检查时间
	private Double beforeTotal; //之前总量
	private Double beforeOut;	//之前出库量
	private Double beforeResidue; //之前剩余量
	private Long afterResidue; //以后剩余量
	private String fixRemark;  //固定记录
	private Integer fixType;  //固定类型
	private Long afterTotal;
	private Long afterOut; 
	private Date startDate; //开始时间
	private Date endDate;  //结束时间
	private Integer status; //盘点状态
	private Integer times;//盘点次数
	private Double lastMoney; //上次盘点余额
	
	private List<ReceiptCheckLogDetail> lastsStrings = new ArrayList<ReceiptCheckLogDetail>();
	private List<ReceiptCheckLogDetail> inStrings = new ArrayList<ReceiptCheckLogDetail>();
	private List<ReceiptCheckLogDetail> outStrings = new ArrayList<ReceiptCheckLogDetail>();
	private List<ReceiptCheckLogDetail> yuStrings = new ArrayList<ReceiptCheckLogDetail>();
	private List<ReceiptCheckLogDetail> afterStrings = new ArrayList<ReceiptCheckLogDetail>();
	

	// Constructors

	/** default constructor */
	public ReceiptCheckLog() {
	}

	/** full constructor */
	public ReceiptCheckLog(Office sysOffice, Timestamp checkTime,
			Double beforeTotal, Double beforeOut, Double beforeResidue,
			Long afterResidue, String fixRemark, Integer fixType,
			Long afterTotal, Long afterOut, Date startDate, Date endDate,
			List<ReceiptCheckLogDetail> lastsStrings ,
			List<ReceiptCheckLogDetail> inStrings ,
			List<ReceiptCheckLogDetail> outStrings,
			List<ReceiptCheckLogDetail> yuStrings,
			List<ReceiptCheckLogDetail> afterStrings
			) {
		this.sysOffice = sysOffice;
		this.checkTime = checkTime;
		this.beforeTotal = beforeTotal;
		this.beforeOut = beforeOut;
		this.beforeResidue = beforeResidue;
		this.afterResidue = afterResidue;
		this.fixRemark = fixRemark;
		this.fixType = fixType;
		this.afterTotal = afterTotal;
		this.afterOut = afterOut;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lastsStrings = lastsStrings;
		this.inStrings = inStrings;
		this.outStrings = outStrings;
		this.yuStrings = yuStrings;
		this.afterStrings = afterStrings;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="RECEIPT_CHECK_LOG_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="RECEIPT_CHECK_LOG_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "RECEIPT_CHECK_LOG_SEQUENCE",strategy=GenerationType.SEQUENCE)
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

	@Column(name = "check_time", length = 19)
	public Timestamp getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "before_total", precision = 11, scale = 3)
	public Double getBeforeTotal() {
		return this.beforeTotal;
	}

	public void setBeforeTotal(Double beforeTotal) {
		this.beforeTotal = beforeTotal;
	}

	@Column(name = "before_out", precision = 11, scale = 3)
	public Double getBeforeOut() {
		return this.beforeOut;
	}

	public void setBeforeOut(Double beforeOut) {
		this.beforeOut = beforeOut;
	}

	@Column(name = "before_residue", precision = 11, scale = 3)
	public Double getBeforeResidue() {
		return this.beforeResidue;
	}

	public void setBeforeResidue(Double beforeResidue) {
		this.beforeResidue = beforeResidue;
	}

	@Column(name = "after_residue", precision = 11, scale = 0)
	public Long getAfterResidue() {
		return this.afterResidue;
	}

	public void setAfterResidue(Long afterResidue) {
		this.afterResidue = afterResidue;
	}

	@Column(name = "fix_remark")
	public String getFixRemark() {
		return this.fixRemark;
	}

	public void setFixRemark(String fixRemark) {
		this.fixRemark = fixRemark;
	}

	@Column(name = "fix_type")
	public Integer getFixType() {
		return this.fixType;
	}

	public void setFixType(Integer fixType) {
		this.fixType = fixType;
	}

	@Column(name = "after_total", precision = 11, scale = 0)
	public Long getAfterTotal() {
		return this.afterTotal;
	}

	public void setAfterTotal(Long afterTotal) {
		this.afterTotal = afterTotal;
	}

	@Column(name = "after_out", precision = 11, scale = 0)
	public Long getAfterOut() {
		return this.afterOut;
	}

	public void setAfterOut(Long afterOut) {
		this.afterOut = afterOut;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date")
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depot_id")
	public ReceiptDepotInfo getReceiptDepotInfo() {
		return receiptDepotInfo;
	}

	public void setReceiptDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		this.receiptDepotInfo = receiptDepotInfo;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "times")
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	@Column(name = "lastMoney")
	public Double getLastMoney() {
		return lastMoney;
	}

	public void setLastMoney(Double lastMoney) {
		this.lastMoney = lastMoney;
	}

	@Transient
	public List<ReceiptCheckLogDetail> getLastsStrings() {
		return lastsStrings;
	}

	public void setLastsStrings(List<ReceiptCheckLogDetail> lastsStrings) {
		this.lastsStrings = lastsStrings;
	}

	@Transient
	public List<ReceiptCheckLogDetail> getInStrings() {
		return inStrings;
	}

	public void setInStrings(List<ReceiptCheckLogDetail> inStrings) {
		this.inStrings = inStrings;
	}

	@Transient
	public List<ReceiptCheckLogDetail> getOutStrings() {
		return outStrings;
	}

	public void setOutStrings(List<ReceiptCheckLogDetail> outStrings) {
		this.outStrings = outStrings;
	}

	@Transient
	public List<ReceiptCheckLogDetail> getYuStrings() {
		return yuStrings;
	}

	public void setYuStrings(List<ReceiptCheckLogDetail> yuStrings) {
		this.yuStrings = yuStrings;
	}

	@Transient
	public List<ReceiptCheckLogDetail> getAfterStrings() {
		return afterStrings;
	}

	public void setAfterStrings(List<ReceiptCheckLogDetail> afterStrings) {
		this.afterStrings = afterStrings;
	}
	
}