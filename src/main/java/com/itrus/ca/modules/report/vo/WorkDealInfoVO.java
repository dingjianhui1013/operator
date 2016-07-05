package com.itrus.ca.modules.report.vo;

import com.itrus.ca.common.utils.excel.annotation.ExcelField;

public class WorkDealInfoVO {
	private Long appId;                  //应用id
	private String appName;              //应用名称
	
	private Integer validCount;          //有效数量
	private Integer newCount;            //新增数量
	private Integer updateCount;         //更新数量
	private Integer unUpdateCount;       //未更新数量
	private Integer maintenanceCount;    //维护数量
	
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
	
	
	
	
}

