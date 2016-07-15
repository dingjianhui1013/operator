package com.itrus.ca.modules.key.entity;

import static javax.persistence.GenerationType.IDENTITY;

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
import javax.persistence.Transient;

/**
 * 申请调拨详细信息
 * @author CaoYichao
 *
 */
@Entity
@Table(name = "key_allocate_apply_detailed")

public class KeyAllocateApplyDetailed implements java.io.Serializable{
    //Fields
	private Long id;
	private KeyAllocateApply keyAllocateApply;
	private KeyGeneralInfo keyGeneralInfo;
	private Integer applyNewNum;
	
	private Integer badKeyNum;
	private KeyGeneralInfo badKeyGeneralInfo;
	
	
	public KeyAllocateApplyDetailed() {
		
	}
	
	//构造方法
	public KeyAllocateApplyDetailed(Long id, KeyAllocateApply keyAllocateApply,
			KeyGeneralInfo keyGeneralInfo, Integer applyNewNum,Integer badKeyNum,KeyGeneralInfo badKeyGeneralInfo) {
		super();
		this.id = id;
		this.keyAllocateApply = keyAllocateApply;
		this.keyGeneralInfo = keyGeneralInfo;
		this.applyNewNum = applyNewNum;
	
		this.badKeyNum = badKeyNum;
		this.badKeyGeneralInfo = badKeyGeneralInfo;
		
		
	}
	
	
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="KEY_ALLOCATE_APPLY_D_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="KEY_ALLOCATE_APPLY_D_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "KEY_ALLOCATE_APPLY_D_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "apply_key_type")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return this.keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}
	
	
	@Column(name = "apply_new_num")
	public Integer getApplyNewNum() {
		return this.applyNewNum;
	}

	public void setApplyNewNum(Integer applyNewNum) {
		this.applyNewNum = applyNewNum;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "apply_id")
	public KeyAllocateApply getKeyAllocateApply() {
		return this.keyAllocateApply;
	}

	public void setKeyAllocateApply(KeyAllocateApply keyAllocateApply) {
		this.keyAllocateApply = keyAllocateApply;
	}
	
	
	
	@Column(name = "bad_key_num")
	public Integer getBadKeyNum() {
		return badKeyNum;
	}

	public void setBadKeyNum(Integer badKeyNum) {
		this.badKeyNum = badKeyNum;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bad_key_type")
	public KeyGeneralInfo getBadKeyGeneralInfo() {
		return badKeyGeneralInfo;
	}

	public void setBadKeyGeneralInfo(KeyGeneralInfo badKeyGeneralInfo) {
		this.badKeyGeneralInfo = badKeyGeneralInfo;
	}
	
	
	
	
	
}
