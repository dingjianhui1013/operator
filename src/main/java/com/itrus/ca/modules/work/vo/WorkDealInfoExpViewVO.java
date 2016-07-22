/**
 *2016年7月19日 下午2:06:28
 */
package com.itrus.ca.modules.work.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * @author: liubin
 *
 */
public class WorkDealInfoExpViewVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WorkDealInfo workDealInfo;
	private Long areaId;
	private Long officeId;
	private Long apply;
	private String certType;
	private Integer workType;
	private Integer year;
	private String payMethod;
	private Date luruStartTime;
	private Date luruEndTime;
	private Date daoqiStartTime;
	private Date daoqiEndTime;
	private Date paymentStartTime;
	private Date paymentEndTime;
	private Date zhizhengStartTime;
	private Date zhizhengEndTime;
	private List<Long> officeIds;
	private List<Long> offices;

	public WorkDealInfo getWorkDealInfo() {
		return workDealInfo;
	}

	public void setWorkDealInfo(WorkDealInfo workDealInfo) {
		this.workDealInfo = workDealInfo;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public Integer getWorkType() {
		return workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public Date getLuruStartTime() {
		return luruStartTime;
	}

	public void setLuruStartTime(Date luruStartTime) {
		this.luruStartTime = luruStartTime;
	}

	public Date getLuruEndTime() {
		return luruEndTime;
	}

	public void setLuruEndTime(Date luruEndTime) {
		this.luruEndTime = luruEndTime;
	}

	public Date getDaoqiStartTime() {
		return daoqiStartTime;
	}

	public void setDaoqiStartTime(Date daoqiStartTime) {
		this.daoqiStartTime = daoqiStartTime;
	}

	public Date getDaoqiEndTime() {
		return daoqiEndTime;
	}

	public void setDaoqiEndTime(Date daoqiEndTime) {
		this.daoqiEndTime = daoqiEndTime;
	}

	public Date getPaymentStartTime() {
		return paymentStartTime;
	}

	public void setPaymentStartTime(Date paymentStartTime) {
		this.paymentStartTime = paymentStartTime;
	}

	public Date getPaymentEndTime() {
		return paymentEndTime;
	}

	public void setPaymentEndTime(Date paymentEndTime) {
		this.paymentEndTime = paymentEndTime;
	}

	public Date getZhizhengStartTime() {
		return zhizhengStartTime;
	}

	public void setZhizhengStartTime(Date zhizhengStartTime) {
		this.zhizhengStartTime = zhizhengStartTime;
	}

	public Date getZhizhengEndTime() {
		return zhizhengEndTime;
	}

	public void setZhizhengEndTime(Date zhizhengEndTime) {
		this.zhizhengEndTime = zhizhengEndTime;
	}

	public Long getApply() {
		return apply;
	}

	public void setApply(Long apply) {
		this.apply = apply;
	}

	public List<Long> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<Long> officeIds) {
		this.officeIds = officeIds;
	}

	public List<Long> getOffices() {
		return offices;
	}

	public void setOffices(List<Long> offices) {
		this.offices = offices;
	}
}
