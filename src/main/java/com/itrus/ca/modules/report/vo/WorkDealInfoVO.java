package com.itrus.ca.modules.report.vo;

import java.util.Date;

import com.itrus.ca.common.utils.excel.annotation.ExcelField;

public class WorkDealInfoVO {
	private Long appId;                  //应用id
	private String appName;              //应用名称
	
	private Integer validCount;          //有效数量
	private Integer newCount;            //新增数量
	private Integer updateCount;         //更新数量
	private Integer unUpdateCount;       //未更新数量
	private Integer maintenanceCount;    //维护数量

	
	
	private String companyName;
	private String contactName;
	private Integer dealInfoType;        
	private Integer dealInfoType1;
	private Integer dealInfoType2;
	private Integer dealInfoType3;
	
	private Date businessCardDate;
	
	@ExcelField(title="应用名称", align=2)
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@ExcelField(title="有效数量", align=2)
	public Integer getValidCount() {
		return validCount;
	}
	public void setValidCount(Integer validCount) {
		this.validCount = validCount;
	}
	
	@ExcelField(title="新增数量", align=2)
	public Integer getNewCount() {
		return newCount;
	}
	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}
	
	@ExcelField(title="已更新数量", align=2)
	public Integer getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}
	
	@ExcelField(title="待更新数量", align=2)
	public Integer getUnUpdateCount() {
		return unUpdateCount;
	}
	public void setUnUpdateCount(Integer unUpdateCount) {
		this.unUpdateCount = unUpdateCount;
	}
	
	@ExcelField(title="维护数量", align=2)
	public Integer getMaintenanceCount() {
		return maintenanceCount;
	}
	public void setMaintenanceCount(Integer maintenanceCount) {
		this.maintenanceCount = maintenanceCount;
	}
	
	
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Integer getDealInfoType() {
		return dealInfoType;
	}
	public void setDealInfoType(Integer dealInfoType) {
		this.dealInfoType = dealInfoType;
	}
	public Integer getDealInfoType1() {
		return dealInfoType1;
	}
	public void setDealInfoType1(Integer dealInfoType1) {
		this.dealInfoType1 = dealInfoType1;
	}
	public Integer getDealInfoType2() {
		return dealInfoType2;
	}
	public void setDealInfoType2(Integer dealInfoType2) {
		this.dealInfoType2 = dealInfoType2;
	}
	public Integer getDealInfoType3() {
		return dealInfoType3;
	}
	public void setDealInfoType3(Integer dealInfoType3) {
		this.dealInfoType3 = dealInfoType3;
	}
	public Date getBusinessCardDate() {
		return businessCardDate;
	}
	public void setBusinessCardDate(Date businessCardDate) {
		this.businessCardDate = businessCardDate;
	}
	
	
	
	
}

