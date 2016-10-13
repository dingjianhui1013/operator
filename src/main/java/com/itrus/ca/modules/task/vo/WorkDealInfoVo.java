package com.itrus.ca.modules.task.vo;

public class WorkDealInfoVo {
	
	private Long level;
	
	private Long id;
	
	private Long prevId;
	
	private String firstCertSN;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	public String getFirstCertSN() {
		return firstCertSN;
	}

	public void setFirstCertSN(String firstCertSN) {
		this.firstCertSN = firstCertSN;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "[level:" + level + " id=" + id + " prevId=" + prevId + " firstCertSN=" + firstCertSN
				+ "]";
	}
	
	
	
	
}
