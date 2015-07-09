package com.itrus.ca.modules.bean;

import java.util.List;


public class StaticProductMonth {
	/**
	 * @see{ProductType}
	 */
	private Integer productType;
	private List<StaticMonth> months;
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public List<StaticMonth> getMonths() {
		return months;
	}
	public void setMonths(List<StaticMonth> months) {
		this.months = months;
	}
	
	
}
