package com.itrus.ca.modules.settle.entity;


import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;

public class AgentSettle {
	private ConfigApp configApp;
	private ConfigProduct configProduct;
	private Integer addSum;
	private Integer updateSum;
	private Integer lostSum;
	private Integer reissueSum;
	private Integer revokeSum;
	private Integer changeSum;
	private Integer sealSum;
	
	public ConfigApp getConfigApp() {
		return configApp;
	}
	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}
	
	public ConfigProduct getConfigProduct() {
		return configProduct;
	}
	public void setConfigProduct(ConfigProduct configProduct) {
		this.configProduct = configProduct;
	}
	
	public Integer getAddSum() {
		return addSum == null?0:addSum;
	}
	public void setAddSum(Integer addSum) {
		this.addSum = addSum;
	}
	
	public Integer getUpdateSum() {
		return updateSum == null?0:updateSum;
	}
	public void setUpdateSum(Integer updateSum) {
		this.updateSum = updateSum;
	}
	
	public Integer getLostSum() {
		return lostSum == null?0:lostSum;
	}
	public void setLostSum(Integer lostSum) {
		this.lostSum = lostSum;
	}
	
	public Integer getReissueSum() {
		return reissueSum == null?0:reissueSum;
	}
	public void setReissueSum(Integer reissueSum) {
		this.reissueSum = reissueSum;
	}
	
	public Integer getRevokeSum() {
		return revokeSum == null?0:revokeSum;
	}
	public void setRevokeSum(Integer revokeSum) {
		this.revokeSum = revokeSum;
	}
	
	public Integer getChangeSum() {
		return changeSum == null?0:changeSum;
	}
	public void setChangeSum(Integer changeSum) {
		this.changeSum = changeSum;
	}
	
	public Integer getSealSum() {
		return sealSum == null?0:sealSum;
	}
	public void setSealSum(Integer sealSum) {
		this.sealSum = sealSum;
	}

}
