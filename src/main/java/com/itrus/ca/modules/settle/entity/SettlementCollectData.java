package com.itrus.ca.modules.settle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

@Entity
@Table(name = "settlement_collect_data")
public class SettlementCollectData extends DataEntity{

	private Long id;
	private Long settlementLogId;
	private String productName;
	private String productId;
	private Integer productLabel;
	
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

	
	public SettlementCollectData(){
		
	}
	
	public SettlementCollectData(String productId,Integer productLabel){
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
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "log_id")
	public Long getSettlementLogId() {
		return settlementLogId;
	}
	public void setSettlementLogId(Long settlementLogId) {
		this.settlementLogId = settlementLogId;
	}
	
	@Column(name = "product_name")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name = "add1")
	public Integer getAdd1() {
		return add1;
	}
	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}
	@Column(name = "add2")
	public Integer getAdd2() {
		return add2;
	}
	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}
	@Column(name = "add3")
	public Integer getAdd3() {
		return add3;
	}
	public void setAdd3(Integer add3) {
		this.add3 = add3;
	}
	@Column(name = "add4")
	public Integer getAdd4() {
		return add4;
	}
	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}
	@Column(name = "add5")
	public Integer getAdd5() {
		return add5;
	}
	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}
	@Column(name = "update1")
	public Integer getUpdate1() {
		return update1;
	}
	public void setUpdate1(Integer update1) {
		this.update1 = update1;
	}
	@Column(name = "update2")
	public Integer getUpdate2() {
		return update2;
	}
	public void setUpdate2(Integer update2) {
		this.update2 = update2;
	}
	@Column(name = "update3")
	public Integer getUpdate3() {
		return update3;
	}
	public void setUpdate3(Integer update3) {
		this.update3 = update3;
	}
	@Column(name = "update4")
	public Integer getUpdate4() {
		return update4;
	}
	public void setUpdate4(Integer update4) {
		this.update4 = update4;
	}
	@Column(name = "update5")
	public Integer getUpdate5() {
		return update5;
	}
	public void setUpdate5(Integer update5) {
		this.update5 = update5;
	}

	@Column(name = "productId")
	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name = "productLabel")
	public Integer getProductLabel() {
		return productLabel;
	}


	public void setProductLabel(Integer productLabel) {
		this.productLabel = productLabel;
	}
	
	
	
	
	
	
}
