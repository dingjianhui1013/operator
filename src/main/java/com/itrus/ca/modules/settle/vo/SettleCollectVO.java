package com.itrus.ca.modules.settle.vo;

/**
 * 结算汇总展示
 */
public class SettleCollectVO {
 
	private String productId;     //产品Id
	private Integer productLabel; //产品标识
	
	private String productName;   //产品名称
	
	private Integer add1;         //新增1
	private Integer add2;         //新增2
	private Integer add3;         //新增3
	private Integer add4;         //新增4
	private Integer add5;         //新增5
	
	
	private Integer update1;      //更新1
	private Integer update2;      //更新2
	private Integer update3;      //更新3
	private Integer update4;      //更新4
	private Integer update5;      //更新5
	
	
	public SettleCollectVO(String productId,Integer productLabel){
		this.productId = productId;
		this.productLabel = productLabel;
		this.add1 = 0;
		this.add2 = 0;
		this.add3 = 0;
		this.add4 = 0;
		this.add5 = 0;
		this.update1 = 0;
		this.update2 = 0;
		this.update3 = 0;
		this.update4 = 0;
		this.update5 = 0;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Integer getAdd1() {
		return add1;
	}
	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}
	public Integer getAdd2() {
		return add2;
	}
	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}
	public Integer getAdd3() {
		return add3;
	}
	public void setAdd3(Integer add3) {
		this.add3 = add3;
	}
	public Integer getAdd4() {
		return add4;
	}
	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}
	public Integer getAdd5() {
		return add5;
	}
	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}
	public Integer getUpdate1() {
		return update1;
	}
	public void setUpdate1(Integer update1) {
		this.update1 = update1;
	}
	public Integer getUpdate2() {
		return update2;
	}
	public void setUpdate2(Integer update2) {
		this.update2 = update2;
	}
	public Integer getUpdate3() {
		return update3;
	}
	public void setUpdate3(Integer update3) {
		this.update3 = update3;
	}
	public Integer getUpdate4() {
		return update4;
	}
	public void setUpdate4(Integer update4) {
		this.update4 = update4;
	}
	public Integer getUpdate5() {
		return update5;
	}
	public void setUpdate5(Integer update5) {
		this.update5 = update5;
	}
	public Integer getProductLabel() {
		return productLabel;
	}
	public void setProductLabel(Integer productLabel) {
		this.productLabel = productLabel;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
}
