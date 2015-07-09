package com.itrus.ca.modules.settle.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.sys.entity.User;

/**
 * SettleKey entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "settle_key")
public class SettleKey implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigSupplier configSupplier;
	private KeyGeneralInfo keyGeneralInfo;
	private User sysUser;
	private Timestamp createDate;
	private Timestamp comeDate;
	private Timestamp backDate;
//	private Integer num;
//	private String staSn;
//	private String endSn;
//	
	private String keySn;
	

	// Constructors

	/** default constructor */
	public SettleKey() {
	}

	/** minimal constructor */
	public SettleKey(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SettleKey(Long id, 
			KeyGeneralInfo keyGeneralInfo, User sysUser,
			Timestamp createDate, Timestamp comeDate, Timestamp backDate,
			String keySn,ConfigSupplier configSupplier) {
		this.id = id;
		this.configSupplier = configSupplier;
		this.keyGeneralInfo = keyGeneralInfo;
		this.sysUser = sysUser;
		this.createDate = createDate;
		this.comeDate = comeDate;
		this.backDate = backDate;
		this.keySn = keySn;;
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
	@JoinColumn(name = "config_supplier_id")
	public ConfigSupplier getConfigSupplier() {
		return configSupplier;
	}

	public void setConfigSupplier(ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
	}

	
	
	
	
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_id")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return this.keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_by")
	public User getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(User sysUser) {
		this.sysUser = sysUser;
	}

	@Column(name = "create_date", length = 19)
	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	@Column(name = "come_date", length = 19)
	public Timestamp getComeDate() {
		return this.comeDate;
	}

	public void setComeDate(Timestamp comeDate) {
		this.comeDate = comeDate;
	}

	@Column(name = "back_date", length = 19)
	public Timestamp getBackDate() {
		return this.backDate;
	}

	public void setBackDate(Timestamp backDate) {
		this.backDate = backDate;
	}

	@Column(name = "key_sn")
	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

}