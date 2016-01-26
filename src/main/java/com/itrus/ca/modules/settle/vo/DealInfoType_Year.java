package com.itrus.ca.modules.settle.vo;

import java.util.Set;

public class DealInfoType_Year {
	private Integer deal;
	private String producType;
	private Set<Integer> year;
	public Integer getDeal() {
		return deal;
	}
	public void setDeal(Integer deal) {
		this.deal = deal;
	}
	public String getProducType() {
		return producType;
	}
	public void setProducType(String producType) {
		this.producType = producType;
	}
	public Set<Integer> getYear() {
		return year;
	}
	public void setYear(Set<Integer> year) {
		this.year = year;
	}

}
