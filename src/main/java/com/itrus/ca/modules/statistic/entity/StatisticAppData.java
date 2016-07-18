package com.itrus.ca.modules.statistic.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 日经营统计的列表
 */
@Entity
@Table(name = "statistic_app_data")
public class StatisticAppData implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private ConfigApp app;
	private Double keyOver;
	private Double receiptOver;
	private Date statisticDate;
	private Integer add1;
	private Integer add2;
	private Integer add3;
	private Integer add4;
	private Integer add5;
	private Integer renew1;
	private Integer renew2;
	private Integer renew3;
	private Integer renew4;
	private Integer renew5;
	private Integer modifyNum;//变更数量
	private Integer reissueNum;//补办数量 ---遗失补办
	
	private Integer lostReplaceNum;//补办数量
	
	private Integer certTotal;
	private Integer keyTotal;
	private Double receiptTotal;
	private Double certMoneyTotal;

	private Integer updateChangeNum;//更新+变更 
	private Integer updateChangeNum2;//更新+变更 2年
	private Integer updateChangeNum3;//更新+变更 3年
	private Integer updateChangeNum4;//更新+变更 4年
	private Integer updateChangeNum5;//更新+变更 5年
	
	private Integer updateLostNum;//更新+遗失补办 
	private Integer updateLostNum2;//更新+遗失补办 2年
	private Integer updateLostNum3;//更新+遗失补办 3年
	private Integer updateLostNum4;//更新+遗失补办 4年
	private Integer updateLostNum5;//更新+遗失补办 5年
	
	private Integer updateReplaceNum;//更新+损坏更换 
	private Integer updateReplaceNum2;//更新+损坏更换 2年
	private Integer updateReplaceNum3;//更新+损坏更换 3年
	private Integer updateReplaceNum4;//更新+损坏更换 4年
	private Integer updateReplaceNum5;//更新+损坏更换 5年
	
	private Integer changeLostNum;//变更+遗失补办 
	private Integer changeReplaceNum;//变更+损坏更换 
	
	private Integer changeUpdateLostNum;//更新+变更+遗失补办  
	private Integer changeUpdateLostNum2;//更新+变更+遗失补办  2年
	private Integer changeUpdateLostNum3;//更新+变更+遗失补办  3年
	private Integer changeUpdateLostNum4;//更新+变更+遗失补办 4年
	private Integer changeUpdateLostNum5;//更新+变更+遗失补办  5年
	
	private Integer changeUpdateReplaceNum;//更新+变更+损坏更换 
	private Integer changeUpdateReplaceNum2;//更新+变更+损坏更换 2年
	private Integer changeUpdateReplaceNum3;//更新+变更+损坏更换 3年
	private Integer changeUpdateReplaceNum4;//更新+变更+损坏更换 4年
	private Integer changeUpdateReplaceNum5;//更新+变更+损坏更换 5年
	
	
	// Constructors

	/** default constructor */
	public StatisticAppData() {
	}

	/** full constructor */
	public StatisticAppData(Office sysOffice, Integer keyIn, Double keyOver,
			Integer keyStoreTotal, Double receiptIn, Double receiptOver,
			Double receiptStoreTotal, Date statisticDate, Integer add1,
			Integer add2,Integer add3, Integer add4,Integer add5, Integer renew1, Integer renew2,Integer renew3,
			Integer renew4,Integer renew5, Integer modifyNum, Integer reissueNum,
			Integer certTotal, Integer keyTotal, Double receiptTotal,
			Double certMoneyTotal,Integer lostReplaceNum,
			Integer updateChangeNum,
			Integer updateLostNum,
			Integer updateReplaceNum,
			Integer changeLostNum,
			Integer changeReplaceNum,
			Integer changeUpdateLostNum,
			Integer changeUpdateReplaceNum,
			Integer updateChangeNum2,
			Integer updateChangeNum3,
			Integer updateChangeNum4,
			Integer updateChangeNum5,
			
			Integer updateLostNum2,
			Integer updateLostNum3,
			Integer updateLostNum4,
			Integer updateLostNum5,
			
			Integer updateReplaceNum2,
			Integer updateReplaceNum4,
			Integer updateReplaceNum3,
			Integer updateReplaceNum5,
			
			Integer changeUpdateLostNum2,
			Integer changeUpdateLostNum3,
			Integer changeUpdateLostNum4,
			Integer changeUpdateLostNum5,
			
			Integer changeUpdateReplaceNum2,
			Integer changeUpdateReplaceNum3,
			Integer changeUpdateReplaceNum4,
			Integer changeUpdateReplaceNum5
			) {
		this.sysOffice = sysOffice;
		this.keyOver = keyOver;
		this.receiptOver = receiptOver;
		this.statisticDate = statisticDate;
		this.add1 = add1;
		this.add2 = add2;
		this.add3 = add3;
		this.add4 = add4;
		this.add5 = add5;
		this.renew1 = renew1;
		this.renew2 = renew2;
		this.renew3 = renew3;
		this.renew4 = renew4;
		this.renew5 = renew5;
		this.modifyNum = modifyNum;
		this.reissueNum = reissueNum;
		this.certTotal = certTotal;
		this.keyTotal = keyTotal;
		this.receiptTotal = receiptTotal;
		this.certMoneyTotal = certMoneyTotal;
		this.lostReplaceNum = lostReplaceNum;
		this.updateChangeNum = updateChangeNum;
		this.updateLostNum = updateLostNum;
		this.updateReplaceNum =updateReplaceNum;
		this.changeLostNum = changeLostNum;
		this.changeReplaceNum = changeReplaceNum;
		this.changeUpdateLostNum = changeUpdateLostNum;
		this.changeUpdateReplaceNum = changeUpdateReplaceNum;
		this.updateChangeNum2 = updateChangeNum2;
		this.updateChangeNum3 = updateChangeNum3;
		this.updateChangeNum4 = updateChangeNum4;
		this.updateChangeNum5 = updateChangeNum5;
		
		this.updateLostNum2 = updateLostNum2;
		this.updateLostNum3 = updateLostNum3;
		this.updateLostNum4 = updateLostNum4;
		this.updateLostNum5 = updateLostNum5;
		
		this.updateReplaceNum2 = updateReplaceNum2;
		this.updateReplaceNum3 = updateReplaceNum3;
		this.updateReplaceNum4 = updateReplaceNum4;
		this.updateReplaceNum5 = updateReplaceNum5;
		
		this.changeUpdateLostNum2 = changeUpdateLostNum2;
		this.changeUpdateLostNum3 = changeUpdateLostNum3;
		this.changeUpdateLostNum4 = changeUpdateLostNum4;
		this.changeUpdateLostNum5 = changeUpdateLostNum5;
		
		this.changeUpdateReplaceNum2 = changeUpdateReplaceNum2;
		this.changeUpdateReplaceNum3 = changeUpdateReplaceNum3;
		this.changeUpdateReplaceNum4 = changeUpdateReplaceNum4;
		this.changeUpdateReplaceNum5 = changeUpdateReplaceNum5;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="STATISTIC_APP_DATA_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="STATISTIC_APP_DATA_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "STATISTIC_APP_DATA_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}


	@Column(name = "key_over", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getKeyOver() {
		return this.keyOver;
	}

	public void setKeyOver(Double keyOver) {
		this.keyOver = keyOver;
	}


	@Column(name = "receipt_over", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getReceiptOver() {
		return this.receiptOver;
	}

	public void setReceiptOver(Double receiptOver) {
		this.receiptOver = receiptOver;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "statistic_date", length = 10)
	public Date getStatisticDate() {
		return this.statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}

	@Column(name = "add1",columnDefinition = "NUMBER(11)")
	public Integer getAdd1() {
		return this.add1;
	}

	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}

	@Column(name = "add2",columnDefinition = "NUMBER(11)")
	public Integer getAdd2() {
		return this.add2;
	}

	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}

	@Column(name = "add3",columnDefinition = "NUMBER(10)")
	public Integer getAdd3() {
		return add3;
	}

	public void setAdd3(Integer add3) {
		this.add3 = add3;
	}

	@Column(name = "add4",columnDefinition = "NUMBER(11)")
	public Integer getAdd4() {
		return this.add4;
	}

	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}

	@Column(name = "add5",columnDefinition = "NUMBER(10)")
	public Integer getAdd5() {
		return add5;
	}

	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}
	
	@Column(name = "renew1",columnDefinition = "NUMBER(11)")
	public Integer getRenew1() {
		return this.renew1;
	}

	public void setRenew1(Integer renew1) {
		this.renew1 = renew1;
	}

	@Column(name = "renew2",columnDefinition = "NUMBER(11)")
	public Integer getRenew2() {
		return this.renew2;
	}

	public void setRenew2(Integer renew2) {
		this.renew2 = renew2;
	}

	@Column(name = "renew3",columnDefinition = "NUMBER(10)")
	public Integer getRenew3() {
		return renew3;
	}

	public void setRenew3(Integer renew3) {
		this.renew3 = renew3;
	}

	@Column(name = "renew4",columnDefinition = "NUMBER(11)")
	public Integer getRenew4() {
		return this.renew4;
	}

	public void setRenew4(Integer renew4) {
		this.renew4 = renew4;
	}

	@Column(name = "renew5",columnDefinition = "NUMBER(10)")
	public Integer getRenew5() {
		return renew5;
	}

	public void setRenew5(Integer renew5) {
		this.renew5 = renew5;
	}
	
	@Column(name = "modify_num",columnDefinition = "NUMBER(11)")
	public Integer getModifyNum() {
		return this.modifyNum;
	}

	public void setModifyNum(Integer modifyNum) {
		this.modifyNum = modifyNum;
	}

	@Column(name = "reissue_num",columnDefinition = "NUMBER(11)")
	public Integer getReissueNum() {
		return this.reissueNum;
	}

	public void setReissueNum(Integer reissueNum) {
		this.reissueNum = reissueNum;
	}

	@Column(name = "cert_total",columnDefinition = "NUMBER(11)")
	public Integer getCertTotal() {
		return this.certTotal;
	}

	public void setCertTotal(Integer certTotal) {
		this.certTotal = certTotal;
	}

	@Column(name = "key_total")
	public Integer getKeyTotal() {
		return this.keyTotal;
	}

	public void setKeyTotal(Integer keyTotal) {
		this.keyTotal = keyTotal;
	}

	@Column(name = "receipt_total", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getReceiptTotal() {
		return this.receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}

	@Column(name = "cert_money_total", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getCertMoneyTotal() {
		return this.certMoneyTotal;
	}

	public void setCertMoneyTotal(Double certMoneyTotal) {
		this.certMoneyTotal = certMoneyTotal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_id")
	public ConfigApp getApp() {
		return app;
	}

	public void setApp(ConfigApp appId) {
		this.app = appId;
	}

	@Column(name = "lost_replace_num")
	public Integer getLostReplaceNum() {
		return lostReplaceNum;
	}

	public void setLostReplaceNum(Integer lostReplaceNum) {
		this.lostReplaceNum = lostReplaceNum;
	}

	@Column(name = "update_change_num")
	public Integer getUpdateChangeNum() {
		return updateChangeNum;
	}

	public void setUpdateChangeNum(Integer updateChangeNum) {
		this.updateChangeNum = updateChangeNum;
	}

	@Column(name = "update_lost_num")
	public Integer getUpdateLostNum() {
		return updateLostNum;
	}

	public void setUpdateLostNum(Integer updateLostNum) {
		this.updateLostNum = updateLostNum;
	}

	@Column(name = "update_replace_num")
	public Integer getUpdateReplaceNum() {
		return updateReplaceNum;
	}

	public void setUpdateReplaceNum(Integer updateReplaceNum) {
		this.updateReplaceNum = updateReplaceNum;
	}

	@Column(name = "change_lost_num")
	public Integer getChangeLostNum() {
		return changeLostNum;
	}

	public void setChangeLostNum(Integer changeLostNum) {
		this.changeLostNum = changeLostNum;
	}

	@Column(name = "change_replace_num")
	public Integer getChangeReplaceNum() {
		return changeReplaceNum;
	}

	public void setChangeReplaceNum(Integer changeReplaceNum) {
		this.changeReplaceNum = changeReplaceNum;
	}

	@Column(name = "change_update_lost_num")
	public Integer getChangeUpdateLostNum() {
		return changeUpdateLostNum;
	}

	public void setChangeUpdateLostNum(Integer changeUpdateLostNum) {
		this.changeUpdateLostNum = changeUpdateLostNum;
	}

	@Column(name = "change_update_replace_num")
	public Integer getChangeUpdateReplaceNum() {
		return changeUpdateReplaceNum;
	}

	public void setChangeUpdateReplaceNum(Integer changeUpdateReplaceNum) {
		this.changeUpdateReplaceNum = changeUpdateReplaceNum;
	}

	@Column(name = "update_change_num2")
	public Integer getUpdateChangeNum2() {
		return updateChangeNum2;
	}

	public void setUpdateChangeNum2(Integer updateChangeNum2) {
		this.updateChangeNum2 = updateChangeNum2;
	}

	@Column(name = "update_change_num4")
	public Integer getUpdateChangeNum4() {
		return updateChangeNum4;
	}

	public void setUpdateChangeNum4(Integer updateChangeNum4) {
		this.updateChangeNum4 = updateChangeNum4;
	}

	@Column(name = "update_change_num5")
	public Integer getUpdateChangeNum5() {
		return updateChangeNum5;
	}

	public void setUpdateChangeNum5(Integer updateChangeNum5) {
		this.updateChangeNum5 = updateChangeNum5;
	}

	@Column(name = "update_lost_num2")
	public Integer getUpdateLostNum2() {
		return updateLostNum2;
	}

	public void setUpdateLostNum2(Integer updateLostNum2) {
		this.updateLostNum2 = updateLostNum2;
	}

	@Column(name = "update_lost_num4")
	public Integer getUpdateLostNum4() {
		return updateLostNum4;
	}

	public void setUpdateLostNum4(Integer updateLostNum4) {
		this.updateLostNum4 = updateLostNum4;
	}

	@Column(name = "update_lost_num5")
	public Integer getUpdateLostNum5() {
		return updateLostNum5;
	}

	public void setUpdateLostNum5(Integer updateLostNum5) {
		this.updateLostNum5 = updateLostNum5;
	}

	@Column(name = "update_replace_num2")
	public Integer getUpdateReplaceNum2() {
		return updateReplaceNum2;
	}

	public void setUpdateReplaceNum2(Integer updateReplaceNum2) {
		this.updateReplaceNum2 = updateReplaceNum2;
	}

	@Column(name = "update_replace_num4")
	public Integer getUpdateReplaceNum4() {
		return updateReplaceNum4;
	}

	public void setUpdateReplaceNum4(Integer updateReplaceNum4) {
		this.updateReplaceNum4 = updateReplaceNum4;
	}

	@Column(name = "update_replace_num5")
	public Integer getUpdateReplaceNum5() {
		return updateReplaceNum5;
	}

	public void setUpdateReplaceNum5(Integer updateReplaceNum5) {
		this.updateReplaceNum5 = updateReplaceNum5;
	}

	@Column(name = "change_update_lost_num2")
	public Integer getChangeUpdateLostNum2() {
		return changeUpdateLostNum2;
	}

	public void setChangeUpdateLostNum2(Integer changeUpdateLostNum2) {
		this.changeUpdateLostNum2 = changeUpdateLostNum2;
	}

	@Column(name = "change_update_lost_num4")
	public Integer getChangeUpdateLostNum4() {
		return changeUpdateLostNum4;
	}

	public void setChangeUpdateLostNum4(Integer changeUpdateLostNum4) {
		this.changeUpdateLostNum4 = changeUpdateLostNum4;
	}

	@Column(name = "change_update_lost_num5")
	public Integer getChangeUpdateLostNum5() {
		return changeUpdateLostNum5;
	}

	public void setChangeUpdateLostNum5(Integer changeUpdateLostNum5) {
		this.changeUpdateLostNum5 = changeUpdateLostNum5;
	}

	@Column(name = "change_update_replace_num2")
	public Integer getChangeUpdateReplaceNum2() {
		return changeUpdateReplaceNum2;
	}

	public void setChangeUpdateReplaceNum2(Integer changeUpdateReplaceNum2) {
		this.changeUpdateReplaceNum2 = changeUpdateReplaceNum2;
	}

	@Column(name = "change_update_replace_num4")
	public Integer getChangeUpdateReplaceNum4() {
		return changeUpdateReplaceNum4;
	}

	public void setChangeUpdateReplaceNum4(Integer changeUpdateReplaceNum4) {
		this.changeUpdateReplaceNum4 = changeUpdateReplaceNum4;
	}

	@Column(name = "change_update_replace_num5")
	public Integer getChangeUpdateReplaceNum5() {
		return changeUpdateReplaceNum5;
	}

	public void setChangeUpdateReplaceNum5(Integer changeUpdateReplaceNum5) {
		this.changeUpdateReplaceNum5 = changeUpdateReplaceNum5;
	}

	@Column(name = "update_change_num3")
	public Integer getUpdateChangeNum3() {
		return updateChangeNum3;
	}

	public void setUpdateChangeNum3(Integer updateChangeNum3) {
		this.updateChangeNum3 = updateChangeNum3;
	}

	@Column(name = "update_lost_num3")
	public Integer getUpdateLostNum3() {
		return updateLostNum3;
	}

	public void setUpdateLostNum3(Integer updateLostNum3) {
		this.updateLostNum3 = updateLostNum3;
	}

	@Column(name = "update_replace_num3")
	public Integer getUpdateReplaceNum3() {
		return updateReplaceNum3;
	}

	public void setUpdateReplaceNum3(Integer updateReplaceNum3) {
		this.updateReplaceNum3 = updateReplaceNum3;
	}

	@Column(name = "change_update_lost_num3")
	public Integer getChangeUpdateLostNum3() {
		return changeUpdateLostNum3;
	}

	public void setChangeUpdateLostNum3(Integer changeUpdateLostNum3) {
		this.changeUpdateLostNum3 = changeUpdateLostNum3;
	}

	@Column(name = "change_update_replace_num3")
	public Integer getChangeUpdateReplaceNum3() {
		return changeUpdateReplaceNum3;
	}

	public void setChangeUpdateReplaceNum3(Integer changeUpdateReplaceNum3) {
		this.changeUpdateReplaceNum3 = changeUpdateReplaceNum3;
	}
	
	
}