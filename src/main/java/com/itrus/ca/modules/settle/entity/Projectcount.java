package com.itrus.ca.modules.settle.entity;

import javax.persistence.Transient;

/**
 * 项目发证明细数量统计Entity
 * @author qt
 * @version 2015-11-18
 */
public class Projectcount {
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
	private Integer lostCerate;//遗失补办
	private Integer damageCertificate;//损坏更换
	private Integer modifyNum;//变更数量
	private Integer updateChangeNum;//更新+变更 
	private Integer updateChangeNum2;//更新+变更 2年
	private Integer updateChangeNum3;
	private Integer updateChangeNum4;//更新+变更 4年
	private Integer updateChangeNum5;//更新+变更 5年
	private Integer updateLostNum;//更新+遗失补办 
	private Integer updateLostNum2;//更新+遗失补办 2年
	private Integer updateLostNum3;
	private Integer updateLostNum4;//更新+遗失补办 4年
	private Integer updateLostNum5;//更新+遗失补办 5年
	private Integer updateReplaceNum;//更新+损坏更换 
	private Integer updateReplaceNum2;//更新+损坏更换 2年
	private Integer updateReplaceNum3;
	private Integer updateReplaceNum4;//更新+损坏更换 4年
	private Integer updateReplaceNum5;//更新+损坏更换 5年
	private Integer changeLostNum;//变更+遗失补办 
	private Integer changeReplaceNum;//变更+损坏更换 
	private Integer changeUpdateLostNum;//更新+变更+遗失补办  
	private Integer changeUpdateLostNum2;//更新+变更+遗失补办  2年
	private Integer changeUpdateLostNum3;
	private Integer changeUpdateLostNum4;//更新+变更+遗失补办 4年
	private Integer changeUpdateLostNum5;//更新+变更+遗失补办  5年
	private Integer changeUpdateReplaceNum;//更新+变更+损坏更换 
	private Integer changeUpdateReplaceNum2;//更新+变更+损坏更换 2年
	private Integer changeUpdateReplaceNum3;
	private Integer changeUpdateReplaceNum4;//更新+变更+损坏更换 4年
	private Integer changeUpdateReplaceNum5;//更新+变更+损坏更换 5年
	public Projectcount() {
		
	}
	
	
	public Projectcount(Integer add1, Integer add2, Integer add3, Integer add4, Integer add5, Integer renew1, Integer renew2, Integer renew3,
			Integer renew4, Integer renew5, Integer lostCerate, Integer damageCertificate, Integer modifyNum,
			Integer updateChangeNum, Integer updateChangeNum2, Integer updateChangeNum3, Integer updateChangeNum4, Integer updateChangeNum5,
			Integer updateLostNum, Integer updateLostNum2, Integer updateLostNum3, Integer updateLostNum4, Integer updateLostNum5,
			Integer updateReplaceNum, Integer updateReplaceNum2, Integer updateReplaceNum3,Integer updateReplaceNum4, Integer updateReplaceNum5,
			Integer changeLostNum, Integer changeReplaceNum, Integer changeUpdateLostNum, Integer changeUpdateLostNum2, Integer changeUpdateLostNum3,
			Integer changeUpdateLostNum4, Integer changeUpdateLostNum5, Integer changeUpdateReplaceNum,
			Integer changeUpdateReplaceNum2, Integer changeUpdateReplaceNum3, Integer changeUpdateReplaceNum4, Integer changeUpdateReplaceNum5) {
		super();
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
		this.lostCerate = lostCerate;
		this.damageCertificate = damageCertificate;
		this.modifyNum = modifyNum;
		this.updateChangeNum = updateChangeNum;
		this.updateChangeNum2 = updateChangeNum2;
		this.updateChangeNum3 = updateChangeNum3;
		this.updateChangeNum4 = updateChangeNum4;
		this.updateChangeNum5 = updateChangeNum5;
		this.updateLostNum = updateLostNum;
		this.updateLostNum2 = updateLostNum2;
		this.updateLostNum3 = updateLostNum3;
		this.updateLostNum4 = updateLostNum4;
		this.updateLostNum5 = updateLostNum5;
		this.updateReplaceNum = updateReplaceNum;
		this.updateReplaceNum2 = updateReplaceNum2;
		this.updateReplaceNum3 = updateReplaceNum3;
		this.updateReplaceNum4 = updateReplaceNum4;
		this.updateReplaceNum5 = updateReplaceNum5;
		this.changeLostNum = changeLostNum;
		this.changeReplaceNum = changeReplaceNum;
		this.changeUpdateLostNum = changeUpdateLostNum;
		this.changeUpdateLostNum2 = changeUpdateLostNum2;
		this.changeUpdateLostNum3 = changeUpdateLostNum3;
		this.changeUpdateLostNum4 = changeUpdateLostNum4;
		this.changeUpdateLostNum5 = changeUpdateLostNum5;
		this.changeUpdateReplaceNum = changeUpdateReplaceNum;
		this.changeUpdateReplaceNum2 = changeUpdateReplaceNum2;
		this.changeUpdateReplaceNum3 = changeUpdateReplaceNum3;
		this.changeUpdateReplaceNum4 = changeUpdateReplaceNum4;
		this.changeUpdateReplaceNum5 = changeUpdateReplaceNum5;
	}
	
	@Transient
	public Integer getAdd1() {
		return add1;
	}


	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}

	@Transient
	public Integer getAdd2() {
		return add2;
	}


	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}

	@Transient
	public Integer getAdd4() {
		return add4;
	}


	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}

	@Transient
	public Integer getAdd5() {
		return add5;
	}


	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}

	@Transient
	public Integer getRenew1() {
		return renew1;
	}


	public void setRenew1(Integer renew1) {
		this.renew1 = renew1;
	}

	@Transient
	public Integer getRenew2() {
		return renew2;
	}


	public void setRenew2(Integer renew2) {
		this.renew2 = renew2;
	}

	@Transient
	public Integer getRenew4() {
		return renew4;
	}


	public void setRenew4(Integer renew4) {
		this.renew4 = renew4;
	}

	@Transient
	public Integer getRenew5() {
		return renew5;
	}


	public void setRenew5(Integer renew5) {
		this.renew5 = renew5;
	}


	@Transient
	public Integer getLostCerate() {
		return lostCerate;
	}
	public void setLostCerate(Integer lostCerate) {
		this.lostCerate = lostCerate;
	}
	@Transient
	public Integer getDamageCertificate(){
		
		return damageCertificate;
	}
	public void setDamageCertificate(Integer damageCertificate) {
		this.damageCertificate = damageCertificate;
	}
	@Transient
	public Integer getModifyNum() {
		return modifyNum;
	}
	
	public void setModifyNum(Integer modifyNum) {
		this.modifyNum = modifyNum;
	}
	@Transient
	public Integer getUpdateChangeNum() {
		return updateChangeNum;
	}

	public void setUpdateChangeNum(Integer updateChangeNum) {
		this.updateChangeNum = updateChangeNum;
	}
	@Transient
	public Integer getUpdateChangeNum2() {
		return updateChangeNum2;
	}

	public void setUpdateChangeNum2(Integer updateChangeNum2) {
		this.updateChangeNum2 = updateChangeNum2;
	}
	@Transient
	public Integer getUpdateChangeNum4() {
		return updateChangeNum4;
	}

	public void setUpdateChangeNum4(Integer updateChangeNum4) {
		this.updateChangeNum4 = updateChangeNum4;
	}
	@Transient
	public Integer getUpdateChangeNum5() {
		return updateChangeNum5;
	}

	public void setUpdateChangeNum5(Integer updateChangeNum5) {
		this.updateChangeNum5 = updateChangeNum5;
	}
	@Transient
	public Integer getUpdateLostNum() {
		return updateLostNum;
	}

	public void setUpdateLostNum(Integer updateLostNum) {
		this.updateLostNum = updateLostNum;
	}
	@Transient
	public Integer getUpdateLostNum2() {
		return updateLostNum2;
	}

	public void setUpdateLostNum2(Integer updateLostNum2) {
		this.updateLostNum2 = updateLostNum2;
	}
	@Transient
	public Integer getUpdateLostNum4() {
		return updateLostNum4;
	}

	public void setUpdateLostNum4(Integer updateLostNum4) {
		this.updateLostNum4 = updateLostNum4;
	}
	@Transient
	public Integer getUpdateLostNum5() {
		return updateLostNum5;
	}

	public void setUpdateLostNum5(Integer updateLostNum5) {
		this.updateLostNum5 = updateLostNum5;
	}
	@Transient
	public Integer getUpdateReplaceNum() {
		return updateReplaceNum;
	}

	public void setUpdateReplaceNum(Integer updateReplaceNum) {
		this.updateReplaceNum = updateReplaceNum;
	}
	@Transient
	public Integer getUpdateReplaceNum2() {
		return updateReplaceNum2;
	}

	public void setUpdateReplaceNum2(Integer updateReplaceNum2) {
		this.updateReplaceNum2 = updateReplaceNum2;
	}
	@Transient
	public Integer getUpdateReplaceNum4() {
		return updateReplaceNum4;
	}

	public void setUpdateReplaceNum4(Integer updateReplaceNum4) {
		this.updateReplaceNum4 = updateReplaceNum4;
	}
	@Transient
	public Integer getUpdateReplaceNum5() {
		return updateReplaceNum5;
	}

	public void setUpdateReplaceNum5(Integer updateReplaceNum5) {
		this.updateReplaceNum5 = updateReplaceNum5;
	}
	@Transient
	public Integer getChangeLostNum() {
		return changeLostNum;
	}

	public void setChangeLostNum(Integer changeLostNum) {
		this.changeLostNum = changeLostNum;
	}
	@Transient
	public Integer getChangeReplaceNum() {
		return changeReplaceNum;
	}

	public void setChangeReplaceNum(Integer changeReplaceNum) {
		this.changeReplaceNum = changeReplaceNum;
	}
	@Transient
	public Integer getChangeUpdateLostNum() {
		return changeUpdateLostNum;
	}

	public void setChangeUpdateLostNum(Integer changeUpdateLostNum) {
		this.changeUpdateLostNum = changeUpdateLostNum;
	}
	@Transient
	public Integer getChangeUpdateLostNum2() {
		return changeUpdateLostNum2;
	}

	public void setChangeUpdateLostNum2(Integer changeUpdateLostNum2) {
		this.changeUpdateLostNum2 = changeUpdateLostNum2;
	}
	@Transient
	public Integer getChangeUpdateLostNum4() {
		return changeUpdateLostNum4;
	}
	
	public void setChangeUpdateLostNum4(Integer changeUpdateLostNum4) {
		this.changeUpdateLostNum4 = changeUpdateLostNum4;
	}
	@Transient
	public Integer getChangeUpdateLostNum5() {
		return changeUpdateLostNum5;
	}

	public void setChangeUpdateLostNum5(Integer changeUpdateLostNum5) {
		this.changeUpdateLostNum5 = changeUpdateLostNum5;
	}
	@Transient
	public Integer getChangeUpdateReplaceNum() {
		return changeUpdateReplaceNum;
	}

	public void setChangeUpdateReplaceNum(Integer changeUpdateReplaceNum) {
		this.changeUpdateReplaceNum = changeUpdateReplaceNum;
	}
	@Transient
	public Integer getChangeUpdateReplaceNum2() {
		return changeUpdateReplaceNum2;
	}

	public void setChangeUpdateReplaceNum2(Integer changeUpdateReplaceNum2) {
		this.changeUpdateReplaceNum2 = changeUpdateReplaceNum2;
	}
	@Transient
	public Integer getChangeUpdateReplaceNum4() {
		return changeUpdateReplaceNum4;
	}

	public void setChangeUpdateReplaceNum4(Integer changeUpdateReplaceNum4) {
		this.changeUpdateReplaceNum4 = changeUpdateReplaceNum4;
	}
	@Transient
	public Integer getChangeUpdateReplaceNum5() {
		return changeUpdateReplaceNum5;
	}

	public void setChangeUpdateReplaceNum5(Integer changeUpdateReplaceNum5) {
		this.changeUpdateReplaceNum5 = changeUpdateReplaceNum5;
	}

	@Transient
	public Integer getAdd3() {
		return add3;
	}


	public void setAdd3(Integer add3) {
		this.add3 = add3;
	}

	@Transient
	public Integer getRenew3() {
		return renew3;
	}


	public void setRenew3(Integer renew3) {
		this.renew3 = renew3;
	}

	@Transient
	public Integer getUpdateChangeNum3() {
		return updateChangeNum3;
	}


	public void setUpdateChangeNum3(Integer updateChangeNum3) {
		this.updateChangeNum3 = updateChangeNum3;
	}

	@Transient
	public Integer getUpdateLostNum3() {
		return updateLostNum3;
	}


	public void setUpdateLostNum3(Integer updateLostNum3) {
		this.updateLostNum3 = updateLostNum3;
	}

	@Transient
	public Integer getUpdateReplaceNum3() {
		return updateReplaceNum3;
	}


	public void setUpdateReplaceNum3(Integer updateReplaceNum3) {
		this.updateReplaceNum3 = updateReplaceNum3;
	}

	@Transient
	public Integer getChangeUpdateLostNum3() {
		return changeUpdateLostNum3;
	}


	public void setChangeUpdateLostNum3(Integer changeUpdateLostNum3) {
		this.changeUpdateLostNum3 = changeUpdateLostNum3;
	}

	@Transient
	public Integer getChangeUpdateReplaceNum3() {
		return changeUpdateReplaceNum3;
	}


	public void setChangeUpdateReplaceNum3(Integer changeUpdateReplaceNum3) {
		this.changeUpdateReplaceNum3 = changeUpdateReplaceNum3;
	}
	
	
	
	
	
}
