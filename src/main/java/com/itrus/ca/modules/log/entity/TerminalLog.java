package com.itrus.ca.modules.log.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * TerminalLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "terminal_log")
public class TerminalLog  implements java.io.Serializable {

	// Fields

	private Long id;
	private String hostId;
	private String info;
	private String ip;
	private String keySn;
	private String type;
	private String remarks;
	private Date createDate;

	// Constructors

	/** default constructor */
	public TerminalLog() {
	}

	/** minimal constructor */
	public TerminalLog(String hostId, String info, String ip, String keySn,
			String type) {
		this.hostId = hostId;
		this.info = info;
		this.ip = ip;
		this.keySn = keySn;
		this.type = type;
	}

	/** full constructor */
	public TerminalLog(String hostId, String info, String ip, String keySn,
			String type, Integer version, Long project, Long ownerId,
			String ownerName) {
		this.hostId = hostId;
		this.info = info;
		this.ip = ip;
		this.keySn = keySn;
		this.type = type;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="TERMINAL_LOG_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="TERMINAL_LOG_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "TERMINAL_LOG_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "host_id", nullable = false)
	public String getHostId() {
		return this.hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	@Column(name = "info", nullable = false, length = 2048)
	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Column(name = "ip", nullable = false)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "key_sn", nullable = false)
	public String getKeySn() {
		return this.keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name = "type", nullable = false)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
	
}