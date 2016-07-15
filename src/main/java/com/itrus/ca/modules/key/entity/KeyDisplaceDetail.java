package com.itrus.ca.modules.key.entity;

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
 * 申请调拨详情
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "key_displace_detail")
public class KeyDisplaceDetail implements java.io.Serializable {

	// Fields

	private Long id;
	private KeyAllocateApply keyAllocateApply;
	private KeyGeneralInfo keyGeneralInfo;
	private Integer displaceNum;

	// Constructors

	/** default constructor */
	public KeyDisplaceDetail() {
	}

	/** full constructor */
	public KeyDisplaceDetail(KeyAllocateApply keyAllocateApply,
			KeyGeneralInfo keyGeneralInfo, Integer displaceNum) {
		this.keyAllocateApply = keyAllocateApply;
		this.keyGeneralInfo = keyGeneralInfo;
		this.displaceNum = displaceNum;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="KEY_DISPLACE_DETAIL_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="KEY_DISPLACE_DETAIL_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "KEY_DISPLACE_DETAIL_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "allocate_id")
	public KeyAllocateApply getKeyAllocateApply() {
		return this.keyAllocateApply;
	}

	public void setKeyAllocateApply(KeyAllocateApply keyAllocateApply) {
		this.keyAllocateApply = keyAllocateApply;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_general_info")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return this.keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}

	@Column(name = "displace_num")
	public Integer getDisplaceNum() {
		return this.displaceNum;
	}

	public void setDisplaceNum(Integer displaceNum) {
		this.displaceNum = displaceNum;
	}

}