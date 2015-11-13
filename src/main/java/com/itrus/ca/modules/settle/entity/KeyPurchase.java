package com.itrus.ca.modules.settle.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * SettleKey entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "key_purchase")
public class KeyPurchase extends DataEntity implements java.io.Serializable{
		/**
	 * 
	 */
		private Long id; // 编号
		private String appName; // 产品名称
		private Date storageDate;// 入库时间
		private Long startCode; // 起始码
		private Long endCode; // 终止码
		private Integer count; // 数量
		private Double money; // 价格
		private Integer status; // 1为已付0为未付
		
		public KeyPurchase(Long id)
		{
			this.id=id;
		}
		public KeyPurchase(String appName, Date storageDate, Long startCode, Long endCode, Integer count,
				Double money, Integer status) {
			this.appName = appName;
			this.storageDate = storageDate;
			this.startCode = startCode;
			this.endCode = endCode;
			this.count = count;
			this.money = money;
			this.status = status;
		}
		public KeyPurchase(){
		}

		@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
		@Id 
		@GeneratedValue(generator = "COMMON_SEQUENCE",strategy=GenerationType.SEQUENCE)
		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		@Column(name = "appName")
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		@Column(name = "storageDate")
		public Date getStorageDate() {
			return storageDate;
		}
		public void setStorageDate(Date storageDate) {
			this.storageDate = storageDate;
		}
		@Column(name = "startCode")
		public Long getStartCode() {
			return startCode;
		}
		public void setStartCode(Long startCode) {
			this.startCode = startCode;
		}
		@Column(name = "endCode")
		public Long getEndCode() {
			return endCode;
		}
		public void setEndCode(Long endCode) {
			this.endCode = endCode;
		}
		@Column(name = "count")
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
		@Column(name = "money")
		public Double getMoney() {
			return money;
		}
		public void setMoney(Double money) {
			this.money = money;
		}
		@Column(name = "status")
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
}