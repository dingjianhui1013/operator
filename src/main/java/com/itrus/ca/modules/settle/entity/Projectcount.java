package com.itrus.ca.modules.settle.entity;

import javax.persistence.Transient;

/**
 * 项目发证明细数量统计Entity
 * @author qt
 * @version 2015-11-18
 */
public class Projectcount {
	private Integer addPersonalYearCertificate;
	private Integer addPersonalTwoYearCertificate;
	private Integer addPersonalFourYearCertificate;
	private Integer addcompanyYearCertificate;
	private Integer addcompanyTwoYearCertificate;
	private Integer addcompanyFourYearCertificate;
	private Integer updatePersonalYearCertificate;
	private Integer updatePersonalTwoYearCertificate;
	private Integer updatePersonalFourYearCertificate;
	private Integer updatecompanyYearCertificate;
	private Integer updatecompanyTwoYearCertificate;
	private Integer updateFourYearCertificate;
	private Integer lostCerate;
	private Integer damageCertificate;
	public Projectcount() {
		
	}
	public Projectcount(Integer addPersonalYearCertificate, Integer addPersonalTwoYearCertificate,
			Integer addPersonalFourYearCertificate, Integer addcompanyYearCertificate,
			Integer addcompanyTwoYearCertificate, Integer addcompanyFourYearCertificate,
			Integer updatePersonalYearCertificate, Integer updatePersonalTwoYearCertificate,
			Integer updatePersonalFourYearCertificate, Integer updatecompanyYearCertificate,
			Integer updatecompanyTwoYearCertificate, Integer updateFourYearCertificate, Integer lostCerate,
			Integer damageCertificate) {
		
		this.addPersonalYearCertificate = addPersonalYearCertificate;
		this.addPersonalTwoYearCertificate = addPersonalTwoYearCertificate;
		this.addPersonalFourYearCertificate = addPersonalFourYearCertificate;
		this.addcompanyYearCertificate = addcompanyYearCertificate;
		this.addcompanyTwoYearCertificate = addcompanyTwoYearCertificate;
		this.addcompanyFourYearCertificate = addcompanyFourYearCertificate;
		this.updatePersonalYearCertificate = updatePersonalYearCertificate;
		this.updatePersonalTwoYearCertificate = updatePersonalTwoYearCertificate;
		this.updatePersonalFourYearCertificate = updatePersonalFourYearCertificate;
		this.updatecompanyYearCertificate = updatecompanyYearCertificate;
		this.updatecompanyTwoYearCertificate = updatecompanyTwoYearCertificate;
		this.updateFourYearCertificate = updateFourYearCertificate;
		this.lostCerate = lostCerate;
		this.damageCertificate = damageCertificate;
	}
	@Transient
	public Integer getAddPersonalYearCertificate() {
		return addPersonalYearCertificate;
	}
	public void setAddPersonalYearCertificate(Integer addPersonalYearCertificate) {
		this.addPersonalYearCertificate = addPersonalYearCertificate;
	}
	@Transient
	public Integer getAddPersonalTwoYearCertificate() {
		return addPersonalTwoYearCertificate;
	}
	public void setAddPersonalTwoYearCertificate(Integer addPersonalTwoYearCertificate) {
		this.addPersonalTwoYearCertificate = addPersonalTwoYearCertificate;
	}
	@Transient
	public Integer getAddPersonalFourYearCertificate() {
		return addPersonalFourYearCertificate;
	}
	public void setAddPersonalFourYearCertificate(Integer addPersonalFourYearCertificate) {
		this.addPersonalFourYearCertificate = addPersonalFourYearCertificate;
	}
	@Transient
	public Integer getAddcompanyYearCertificate() {
		return addcompanyYearCertificate;
	}
	public void setAddcompanyYearCertificate(Integer addcompanyYearCertificate) {
		this.addcompanyYearCertificate = addcompanyYearCertificate;
	}
	@Transient
	public Integer getAddcompanyTwoYearCertificate() {
		return addcompanyTwoYearCertificate;
	}
	public void setAddcompanyTwoYearCertificate(Integer addcompanyTwoYearCertificate) {
		this.addcompanyTwoYearCertificate = addcompanyTwoYearCertificate;
	}
	@Transient
	public Integer getAddcompanyFourYearCertificate() {
		return addcompanyFourYearCertificate;
	}
	public void setAddcompanyFourYearCertificate(Integer addcompanyFourYearCertificate) {
		this.addcompanyFourYearCertificate = addcompanyFourYearCertificate;
	}
	@Transient
	public Integer getUpdatePersonalYearCertificate() {
		return updatePersonalYearCertificate;
	}
	public void setUpdatePersonalYearCertificate(Integer updatePersonalYearCertificate) {
		this.updatePersonalYearCertificate = updatePersonalYearCertificate;
	}
	@Transient
	public Integer getUpdatePersonalTwoYearCertificate() {
		return updatePersonalTwoYearCertificate;
	}
	public void setUpdatePersonalTwoYearCertificate(Integer updatePersonalTwoYearCertificate) {
		this.updatePersonalTwoYearCertificate = updatePersonalTwoYearCertificate;
	}
	@Transient
	public Integer getUpdatePersonalFourYearCertificate() {
		return updatePersonalFourYearCertificate;
	}
	public void setUpdatePersonalFourYearCertificate(Integer updatePersonalFourYearCertificate) {
		this.updatePersonalFourYearCertificate = updatePersonalFourYearCertificate;
	}
	@Transient
	public Integer getUpdatecompanyYearCertificate() {
		return updatecompanyYearCertificate;
	}
	public void setUpdatecompanyYearCertificate(Integer updatecompanyYearCertificate) {
		this.updatecompanyYearCertificate = updatecompanyYearCertificate;
	}
	@Transient
	public Integer getUpdatecompanyTwoYearCertificate() {
		return updatecompanyTwoYearCertificate;
	}
	public void setUpdatecompanyTwoYearCertificate(Integer updatecompanyTwoYearCertificate) {
		this.updatecompanyTwoYearCertificate = updatecompanyTwoYearCertificate;
	}
	@Transient
	public Integer getUpdateFourYearCertificate() {
		return updateFourYearCertificate;
	}
	public void setUpdateFourYearCertificate(Integer updateFourYearCertificate) {
		this.updateFourYearCertificate = updateFourYearCertificate;
	}
	@Transient
	public Integer getLostCerate() {
		return lostCerate;
	}
	public void setLostCerate(Integer lostCerate) {
		this.lostCerate = lostCerate;
	}
	@Transient
	public Integer getDamageCertificate(){
		
		return damageCertificate;
	}
	public void setDamageCertificate(Integer damageCertificate) {
		this.damageCertificate = damageCertificate;
	}
	
	
}
