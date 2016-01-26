package com.itrus.ca.modules.settle.vo;

import java.util.List;
import java.util.Set;

public class DealInfoType_Year {
	private String date;
	private Integer deal;
	private String producType;
	private Set<Integer> year;
	private List<Long> workCount;
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
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Long> getWorkCount() {
		return workCount;
	}
	public void setWorkCount(List<Long> count) {
		this.workCount = count;
	}

}
