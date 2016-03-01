package com.itrus.ca.modules.settle.vo;
/**
 * 证书结算实体
 * @author qt
 *
 */
public class CertificateF {
	// 新增企业
	private Integer xzqyadd1;
	private Integer xzqyadd2;
	private Integer xzqyadd3;
	private Integer xzqyadd4;
	private Integer xzqyadd5;

	// 新增个人（企业）

	private Integer xzgrQadd1;
	private Integer xzgrQadd2;
	private Integer xzgrQadd3;
	private Integer xzgrQadd4;
	private Integer xzgrQadd5;

	// 新增个人（机构）
	private Integer xzgrGadd1;
	private Integer xzgrGadd2;
	private Integer xzgrGadd3;
	private Integer xzgrGadd4;
	private Integer xzgrGadd5;

	// 新增机构
	private Integer xzjgadd1;
	private Integer xzjgadd2;
	private Integer xzjgadd3;
	private Integer xzjgadd4;
	private Integer xzjgadd5;

	// 更新企业
	private Integer gxqyadd1;
	private Integer gxqyadd2;
	private Integer gxqyadd3;
	private Integer gxqyadd4;
	private Integer gxqyadd5;

	// 更新个人（企业）

	private Integer gxgrQadd1;
	private Integer gxgrQadd2;
	private Integer gxgrQadd3;
	private Integer gxgrQadd4;
	private Integer gxgrQadd5;

	// 更新个人（机构）
	private Integer gxgrGadd1;
	private Integer gxgrGadd2;
	private Integer gxgrGadd3;
	private Integer gxgrGadd4;
	private Integer gxgrGadd5;

	// 更新机构
	private Integer gxjgadd1;
	private Integer gxjgadd2;
	private Integer gxjgadd3;
	private Integer gxjgadd4;
	private Integer gxjgadd5;
	private Integer lostCerateqy;// 遗失补办
	private Integer lostCerategrQ;
	private Integer lostCerategrG;
	private Integer lostCeratejg;
	private Integer damageCertificateqy;// 损坏更换
	private Integer damageCertificategrQ;
	private Integer damageCertificategrG;
	private Integer damageCertificatejg;
	private Integer modifyNumqy;// 变更数量
	private Integer modifyNumgrQ;
	private Integer modifyNumgrG;
	private Integer modifyNumjg;
	private Integer updateChangeqyNum;// 更新+变更
	private Integer updateChangeqyNum2;// 更新+变更 2年
	private Integer updateChangeqyNum3;// 更新+变更 3年
	private Integer updateChangeqyNum4;// 更新+变更 4年
	private Integer updateChangeqyNum5;// 更新+变更 5年
	private Integer updateChangegrQNum;
	private Integer updateChangegrQNum2;
	private Integer updateChangegrQNum3;
	private Integer updateChangegrQNum4;
	private Integer updateChangegrQNum5;
	private Integer updateChangegrGNum;
	private Integer updateChangegrGNum2;
	private Integer updateChangegrGNum3;
	private Integer updateChangegrGNum4;
	private Integer updateChangegrGNum5;
	private Integer updateChangejgNum;
	private Integer updateChangejgNum2;
	private Integer updateChangejgNum3;
	private Integer updateChangejgNum4;
	private Integer updateChangejgNum5;
	private Integer updateLostqyNum;// 更新+遗失补办
	private Integer updateLostqyNum2;
	private Integer updateLostqyNum3;
	private Integer updateLostqyNum4;
	private Integer updateLostqyNum5;
	private Integer updateLostgrQNum;
	private Integer updateLostgrQNum2;
	private Integer updateLostgrQNum3;
	private Integer updateLostgrQNum4;
	private Integer updateLostgrQNum5;
	private Integer updateLostgrGNum;
	private Integer updateLostgrGNum2;
	private Integer updateLostgrGNum3;
	private Integer updateLostgrGNum4;
	private Integer updateLostgrGNum5;
	private Integer updateLostjgNum;
	private Integer updateLostjgNum2;
	private Integer updateLostjgNum3;
	private Integer updateLostjgNum4;
	private Integer updateLostjgNum5;
	private Integer updateReplaceqyNum;// 更新+损坏更换
	private Integer updateReplaceqyNum2;// 更新+损坏更换 2年
	private Integer updateReplaceqyNum3;// 更新+损坏更换 3年
	private Integer updateReplaceqyNum4;// 更新+损坏更换 4年
	private Integer updateReplaceqyNum5;// 更新+损坏更换 5年
	private Integer updateReplacegrQNum;
	private Integer updateReplacegrQNum2;
	private Integer updateReplacegrQNum3;
	private Integer updateReplacegrQNum4;
	private Integer updateReplacegrQNum5;
	private Integer updateReplacegrGNum;
	private Integer updateReplacegrGNum2;
	private Integer updateReplacegrGNum3;
	private Integer updateReplacegrGNum4;
	private Integer updateReplacegrGNum5;
	private Integer updateReplacejgNum;
	private Integer updateReplacejgNum2;
	private Integer updateReplacejgNum3;
	private Integer updateReplacejgNum4;
	private Integer updateReplacejgNum5;
	private Integer changeLostqyNum;// 变更+遗失补办
	private Integer changeLostgrQNum;
	private Integer changeLostgrGNum;
	private Integer changeLostjgNum;
	private Integer changeReplaceqyNum;// 变更+损坏更换
	private Integer changeReplacegrQNum;
	private Integer changeReplacegrGNum;
	private Integer changeReplacejgNum;
	private Integer changeUpdateLostqyNum;// 更新+变更+遗失补办
	private Integer changeUpdateLostqyNum2;// 更新+变更+遗失补办 2年
	private Integer changeUpdateLostqyNum3;// 更新+变更+遗失补办 3年
	private Integer changeUpdateLostqyNum4;// 更新+变更+遗失补办 4年
	private Integer changeUpdateLostqyNum5;// 更新+变更+遗失补办 5年
	private Integer changeUpdateLostgrQNum;
	private Integer changeUpdateLostgrQNum2;
	private Integer changeUpdateLostgrQNum3;
	private Integer changeUpdateLostgrQNum4;
	private Integer changeUpdateLostgrQNum5;
	private Integer changeUpdateLostgrGNum;
	private Integer changeUpdateLostgrGNum2;
	private Integer changeUpdateLostgrGNum3;
	private Integer changeUpdateLostgrGNum4;
	private Integer changeUpdateLostgrGNum5;
	private Integer changeUpdateLostjgNum;
	private Integer changeUpdateLostjgNum2;
	private Integer changeUpdateLostjgNum3;
	private Integer changeUpdateLostjgNum4;
	private Integer changeUpdateLostjgNum5;
	private Integer changeUpdateReplaceqyNum;// 更新+变更+损坏更换
	private Integer changeUpdateReplaceqyNum2;// 更新+变更+损坏更换 2年
	private Integer changeUpdateReplaceqyNum3;// 更新+变更+损坏更换 3年
	private Integer changeUpdateReplaceqyNum4;// 更新+变更+损坏更换 4年
	private Integer changeUpdateReplaceqyNum5;// 更新+变更+损坏更换 5年
	private Integer changeUpdateReplacegrQNum;
	private Integer changeUpdateReplacegrQNum2;
	private Integer changeUpdateReplacegrQNum3;
	private Integer changeUpdateReplacegrQNum4;
	private Integer changeUpdateReplacegrQNum5;
	private Integer changeUpdateReplacegrGNum;
	private Integer changeUpdateReplacegrGNum2;
	private Integer changeUpdateReplacegrGNum3;
	private Integer changeUpdateReplacegrGNum4;
	private Integer changeUpdateReplacegrGNum5;
	private Integer changeUpdateReplacejgNum;
	private Integer changeUpdateReplacejgNum2;
	private Integer changeUpdateReplacejgNum3;
	private Integer changeUpdateReplacejgNum4;
	private Integer changeUpdateReplacejgNum5;
	
	
	public CertificateF() {
		
	}


	


	public CertificateF(Integer xzqyadd1, Integer xzqyadd2, Integer xzqyadd3,
			Integer xzqyadd4, Integer xzqyadd5, Integer xzgrQadd1,
			Integer xzgrQadd2, Integer xzgrQadd3, Integer xzgrQadd4,
			Integer xzgrQadd5, Integer xzgrGadd1, Integer xzgrGadd2,
			Integer xzgrGadd3, Integer xzgrGadd4, Integer xzgrGadd5,
			Integer xzjgadd1, Integer xzjgadd2, Integer xzjgadd3,
			Integer xzjgadd4, Integer xzjgadd5, Integer gxqyadd1,
			Integer gxqyadd2, Integer gxqyadd3, Integer gxqyadd4,
			Integer gxqyadd5, Integer gxgrQadd1, Integer gxgrQadd2,
			Integer gxgrQadd3, Integer gxgrQadd4, Integer gxgrQadd5,
			Integer gxgrGadd1, Integer gxgrGadd2, Integer gxgrGadd3,
			Integer gxgrGadd4, Integer gxgrGadd5, Integer gxjgadd1,
			Integer gxjgadd2, Integer gxjgadd3, Integer gxjgadd4,
			Integer gxjgadd5, Integer lostCerateqy, Integer lostCerategrQ,
			Integer lostCerategrG, Integer lostCeratejg,
			Integer damageCertificateqy, Integer damageCertificategrQ,
			Integer damageCertificategrG, Integer damageCertificatejg,
			Integer modifyNumqy, Integer modifyNumgrQ, Integer modifyNumgrG,
			Integer modifyNumjg, Integer updateChangeqyNum,
			Integer updateChangeqyNum2, Integer updateChangeqyNum3,
			Integer updateChangeqyNum4, Integer updateChangeqyNum5,
			Integer updateChangegrQNum, Integer updateChangegrQNum2,
			Integer updateChangegrQNum3, Integer updateChangegrQNum4,
			Integer updateChangegrQNum5, Integer updateChangegrGNum,
			Integer updateChangegrGNum2, Integer updateChangegrGNum3,
			Integer updateChangegrGNum4, Integer updateChangegrGNum5,
			Integer updateChangejgNum, Integer updateChangejgNum2,
			Integer updateChangejgNum3, Integer updateChangejgNum4,
			Integer updateChangejgNum5, Integer updateLostqyNum,
			Integer updateLostqyNum2, Integer updateLostqyNum3,
			Integer updateLostqyNum4, Integer updateLostqyNum5,
			Integer updateLostgrQNum, Integer updateLostgrQNum2,
			Integer updateLostgrQNum3, Integer updateLostgrQNum4,
			Integer updateLostgrQNum5, Integer updateLostgrGNum,
			Integer updateLostgrGNum2, Integer updateLostgrGNum3,
			Integer updateLostgrGNum4, Integer updateLostgrGNum5,
			Integer updateLostjgNum, Integer updateLostjgNum2,
			Integer updateLostjgNum3, Integer updateLostjgNum4,
			Integer updateLostjgNum5, Integer updateReplaceqyNum,
			Integer updateReplaceqyNum2, Integer updateReplaceqyNum3,
			Integer updateReplaceqyNum4, Integer updateReplaceqyNum5,
			Integer updateReplacegrQNum, Integer updateReplacegrQNum2,
			Integer updateReplacegrQNum3, Integer updateReplacegrQNum4,
			Integer updateReplacegrQNum5, Integer updateReplacegrGNum,
			Integer updateReplacegrGNum2, Integer updateReplacegrGNum3,
			Integer updateReplacegrGNum4, Integer updateReplacegrGNum5,
			Integer updateReplacejgNum, Integer updateReplacejgNum2,
			Integer updateReplacejgNum3, Integer updateReplacejgNum4,
			Integer updateReplacejgNum5, Integer changeLostqyNum,
			Integer changeLostgrQNum, Integer changeLostgrGNum,
			Integer changeLostjgNum, Integer changeReplaceqyNum,
			Integer changeReplacegrQNum, Integer changeReplacegrGNum,
			Integer changeReplacejgNum, Integer changeUpdateLostqyNum,
			Integer changeUpdateLostqyNum2, Integer changeUpdateLostqyNum3,
			Integer changeUpdateLostqyNum4, Integer changeUpdateLostqyNum5,
			Integer changeUpdateLostgrQNum, Integer changeUpdateLostgrQNum2,
			Integer changeUpdateLostgrQNum3, Integer changeUpdateLostgrQNum4,
			Integer changeUpdateLostgrQNum5, Integer changeUpdateLostgrGNum,
			Integer changeUpdateLostgrGNum2, Integer changeUpdateLostgrGNum3,
			Integer changeUpdateLostgrGNum4, Integer changeUpdateLostgrGNum5,
			Integer changeUpdateLostjgNum, Integer changeUpdateLostjgNum2,
			Integer changeUpdateLostjgNum3, Integer changeUpdateLostjgNum4,
			Integer changeUpdateLostjgNum5, Integer changeUpdateReplaceqyNum,
			Integer changeUpdateReplaceqyNum2,
			Integer changeUpdateReplaceqyNum3,
			Integer changeUpdateReplaceqyNum4,
			Integer changeUpdateReplaceqyNum5,
			Integer changeUpdateReplacegrQNum,
			Integer changeUpdateReplacegrQNum2,
			Integer changeUpdateReplacegrQNum3,
			Integer changeUpdateReplacegrQNum4,
			Integer changeUpdateReplacegrQNum5,
			Integer changeUpdateReplacegrGNum,
			Integer changeUpdateReplacegrGNum2,
			Integer changeUpdateReplacegrGNum3,
			Integer changeUpdateReplacegrGNum4,
			Integer changeUpdateReplacegrGNum5,
			Integer changeUpdateReplacejgNum,
			Integer changeUpdateReplacejgNum2,
			Integer changeUpdateReplacejgNum3,
			Integer changeUpdateReplacejgNum4, Integer changeUpdateReplacejgNum5) {
		super();
		this.xzqyadd1 = xzqyadd1;
		this.xzqyadd2 = xzqyadd2;
		this.xzqyadd3 = xzqyadd3;
		this.xzqyadd4 = xzqyadd4;
		this.xzqyadd5 = xzqyadd5;
		this.xzgrQadd1 = xzgrQadd1;
		this.xzgrQadd2 = xzgrQadd2;
		this.xzgrQadd3 = xzgrQadd3;
		this.xzgrQadd4 = xzgrQadd4;
		this.xzgrQadd5 = xzgrQadd5;
		this.xzgrGadd1 = xzgrGadd1;
		this.xzgrGadd2 = xzgrGadd2;
		this.xzgrGadd3 = xzgrGadd3;
		this.xzgrGadd4 = xzgrGadd4;
		this.xzgrGadd5 = xzgrGadd5;
		this.xzjgadd1 = xzjgadd1;
		this.xzjgadd2 = xzjgadd2;
		this.xzjgadd3 = xzjgadd3;
		this.xzjgadd4 = xzjgadd4;
		this.xzjgadd5 = xzjgadd5;
		this.gxqyadd1 = gxqyadd1;
		this.gxqyadd2 = gxqyadd2;
		this.gxqyadd3 = gxqyadd3;
		this.gxqyadd4 = gxqyadd4;
		this.gxqyadd5 = gxqyadd5;
		this.gxgrQadd1 = gxgrQadd1;
		this.gxgrQadd2 = gxgrQadd2;
		this.gxgrQadd3 = gxgrQadd3;
		this.gxgrQadd4 = gxgrQadd4;
		this.gxgrQadd5 = gxgrQadd5;
		this.gxgrGadd1 = gxgrGadd1;
		this.gxgrGadd2 = gxgrGadd2;
		this.gxgrGadd3 = gxgrGadd3;
		this.gxgrGadd4 = gxgrGadd4;
		this.gxgrGadd5 = gxgrGadd5;
		this.gxjgadd1 = gxjgadd1;
		this.gxjgadd2 = gxjgadd2;
		this.gxjgadd3 = gxjgadd3;
		this.gxjgadd4 = gxjgadd4;
		this.gxjgadd5 = gxjgadd5;
		this.lostCerateqy = lostCerateqy;
		this.lostCerategrQ = lostCerategrQ;
		this.lostCerategrG = lostCerategrG;
		this.lostCeratejg = lostCeratejg;
		this.damageCertificateqy = damageCertificateqy;
		this.damageCertificategrQ = damageCertificategrQ;
		this.damageCertificategrG = damageCertificategrG;
		this.damageCertificatejg = damageCertificatejg;
		this.modifyNumqy = modifyNumqy;
		this.modifyNumgrQ = modifyNumgrQ;
		this.modifyNumgrG = modifyNumgrG;
		this.modifyNumjg = modifyNumjg;
		this.updateChangeqyNum = updateChangeqyNum;
		this.updateChangeqyNum2 = updateChangeqyNum2;
		this.updateChangeqyNum3 = updateChangeqyNum3;
		this.updateChangeqyNum4 = updateChangeqyNum4;
		this.updateChangeqyNum5 = updateChangeqyNum5;
		this.updateChangegrQNum = updateChangegrQNum;
		this.updateChangegrQNum2 = updateChangegrQNum2;
		this.updateChangegrQNum3 = updateChangegrQNum3;
		this.updateChangegrQNum4 = updateChangegrQNum4;
		this.updateChangegrQNum5 = updateChangegrQNum5;
		this.updateChangegrGNum = updateChangegrGNum;
		this.updateChangegrGNum2 = updateChangegrGNum2;
		this.updateChangegrGNum3 = updateChangegrGNum3;
		this.updateChangegrGNum4 = updateChangegrGNum4;
		this.updateChangegrGNum5 = updateChangegrGNum5;
		this.updateChangejgNum = updateChangejgNum;
		this.updateChangejgNum2 = updateChangejgNum2;
		this.updateChangejgNum3 = updateChangejgNum3;
		this.updateChangejgNum4 = updateChangejgNum4;
		this.updateChangejgNum5 = updateChangejgNum5;
		this.updateLostqyNum = updateLostqyNum;
		this.updateLostqyNum2 = updateLostqyNum2;
		this.updateLostqyNum3 = updateLostqyNum3;
		this.updateLostqyNum4 = updateLostqyNum4;
		this.updateLostqyNum5 = updateLostqyNum5;
		this.updateLostgrQNum = updateLostgrQNum;
		this.updateLostgrQNum2 = updateLostgrQNum2;
		this.updateLostgrQNum3 = updateLostgrQNum3;
		this.updateLostgrQNum4 = updateLostgrQNum4;
		this.updateLostgrQNum5 = updateLostgrQNum5;
		this.updateLostgrGNum = updateLostgrGNum;
		this.updateLostgrGNum2 = updateLostgrGNum2;
		this.updateLostgrGNum3 = updateLostgrGNum3;
		this.updateLostgrGNum4 = updateLostgrGNum4;
		this.updateLostgrGNum5 = updateLostgrGNum5;
		this.updateLostjgNum = updateLostjgNum;
		this.updateLostjgNum2 = updateLostjgNum2;
		this.updateLostjgNum3 = updateLostjgNum3;
		this.updateLostjgNum4 = updateLostjgNum4;
		this.updateLostjgNum5 = updateLostjgNum5;
		this.updateReplaceqyNum = updateReplaceqyNum;
		this.updateReplaceqyNum2 = updateReplaceqyNum2;
		this.updateReplaceqyNum3 = updateReplaceqyNum3;
		this.updateReplaceqyNum4 = updateReplaceqyNum4;
		this.updateReplaceqyNum5 = updateReplaceqyNum5;
		this.updateReplacegrQNum = updateReplacegrQNum;
		this.updateReplacegrQNum2 = updateReplacegrQNum2;
		this.updateReplacegrQNum3 = updateReplacegrQNum3;
		this.updateReplacegrQNum4 = updateReplacegrQNum4;
		this.updateReplacegrQNum5 = updateReplacegrQNum5;
		this.updateReplacegrGNum = updateReplacegrGNum;
		this.updateReplacegrGNum2 = updateReplacegrGNum2;
		this.updateReplacegrGNum3 = updateReplacegrGNum3;
		this.updateReplacegrGNum4 = updateReplacegrGNum4;
		this.updateReplacegrGNum5 = updateReplacegrGNum5;
		this.updateReplacejgNum = updateReplacejgNum;
		this.updateReplacejgNum2 = updateReplacejgNum2;
		this.updateReplacejgNum3 = updateReplacejgNum3;
		this.updateReplacejgNum4 = updateReplacejgNum4;
		this.updateReplacejgNum5 = updateReplacejgNum5;
		this.changeLostqyNum = changeLostqyNum;
		this.changeLostgrQNum = changeLostgrQNum;
		this.changeLostgrGNum = changeLostgrGNum;
		this.changeLostjgNum = changeLostjgNum;
		this.changeReplaceqyNum = changeReplaceqyNum;
		this.changeReplacegrQNum = changeReplacegrQNum;
		this.changeReplacegrGNum = changeReplacegrGNum;
		this.changeReplacejgNum = changeReplacejgNum;
		this.changeUpdateLostqyNum = changeUpdateLostqyNum;
		this.changeUpdateLostqyNum2 = changeUpdateLostqyNum2;
		this.changeUpdateLostqyNum3 = changeUpdateLostqyNum3;
		this.changeUpdateLostqyNum4 = changeUpdateLostqyNum4;
		this.changeUpdateLostqyNum5 = changeUpdateLostqyNum5;
		this.changeUpdateLostgrQNum = changeUpdateLostgrQNum;
		this.changeUpdateLostgrQNum2 = changeUpdateLostgrQNum2;
		this.changeUpdateLostgrQNum3 = changeUpdateLostgrQNum3;
		this.changeUpdateLostgrQNum4 = changeUpdateLostgrQNum4;
		this.changeUpdateLostgrQNum5 = changeUpdateLostgrQNum5;
		this.changeUpdateLostgrGNum = changeUpdateLostgrGNum;
		this.changeUpdateLostgrGNum2 = changeUpdateLostgrGNum2;
		this.changeUpdateLostgrGNum3 = changeUpdateLostgrGNum3;
		this.changeUpdateLostgrGNum4 = changeUpdateLostgrGNum4;
		this.changeUpdateLostgrGNum5 = changeUpdateLostgrGNum5;
		this.changeUpdateLostjgNum = changeUpdateLostjgNum;
		this.changeUpdateLostjgNum2 = changeUpdateLostjgNum2;
		this.changeUpdateLostjgNum3 = changeUpdateLostjgNum3;
		this.changeUpdateLostjgNum4 = changeUpdateLostjgNum4;
		this.changeUpdateLostjgNum5 = changeUpdateLostjgNum5;
		this.changeUpdateReplaceqyNum = changeUpdateReplaceqyNum;
		this.changeUpdateReplaceqyNum2 = changeUpdateReplaceqyNum2;
		this.changeUpdateReplaceqyNum3 = changeUpdateReplaceqyNum3;
		this.changeUpdateReplaceqyNum4 = changeUpdateReplaceqyNum4;
		this.changeUpdateReplaceqyNum5 = changeUpdateReplaceqyNum5;
		this.changeUpdateReplacegrQNum = changeUpdateReplacegrQNum;
		this.changeUpdateReplacegrQNum2 = changeUpdateReplacegrQNum2;
		this.changeUpdateReplacegrQNum3 = changeUpdateReplacegrQNum3;
		this.changeUpdateReplacegrQNum4 = changeUpdateReplacegrQNum4;
		this.changeUpdateReplacegrQNum5 = changeUpdateReplacegrQNum5;
		this.changeUpdateReplacegrGNum = changeUpdateReplacegrGNum;
		this.changeUpdateReplacegrGNum2 = changeUpdateReplacegrGNum2;
		this.changeUpdateReplacegrGNum3 = changeUpdateReplacegrGNum3;
		this.changeUpdateReplacegrGNum4 = changeUpdateReplacegrGNum4;
		this.changeUpdateReplacegrGNum5 = changeUpdateReplacegrGNum5;
		this.changeUpdateReplacejgNum = changeUpdateReplacejgNum;
		this.changeUpdateReplacejgNum2 = changeUpdateReplacejgNum2;
		this.changeUpdateReplacejgNum3 = changeUpdateReplacejgNum3;
		this.changeUpdateReplacejgNum4 = changeUpdateReplacejgNum4;
		this.changeUpdateReplacejgNum5 = changeUpdateReplacejgNum5;
	}





	public Integer getXzqyadd1() {
		return xzqyadd1;
	}





	public void setXzqyadd1(Integer xzqyadd1) {
		this.xzqyadd1 = xzqyadd1;
	}





	public Integer getXzqyadd2() {
		return xzqyadd2;
	}





	public void setXzqyadd2(Integer xzqyadd2) {
		this.xzqyadd2 = xzqyadd2;
	}





	public Integer getXzqyadd3() {
		return xzqyadd3;
	}





	public void setXzqyadd3(Integer xzqyadd3) {
		this.xzqyadd3 = xzqyadd3;
	}





	public Integer getXzqyadd4() {
		return xzqyadd4;
	}





	public void setXzqyadd4(Integer xzqyadd4) {
		this.xzqyadd4 = xzqyadd4;
	}





	public Integer getXzqyadd5() {
		return xzqyadd5;
	}





	public void setXzqyadd5(Integer xzqyadd5) {
		this.xzqyadd5 = xzqyadd5;
	}





	public Integer getXzgrQadd1() {
		return xzgrQadd1;
	}





	public void setXzgrQadd1(Integer xzgrQadd1) {
		this.xzgrQadd1 = xzgrQadd1;
	}





	public Integer getXzgrQadd2() {
		return xzgrQadd2;
	}





	public void setXzgrQadd2(Integer xzgrQadd2) {
		this.xzgrQadd2 = xzgrQadd2;
	}





	public Integer getXzgrQadd3() {
		return xzgrQadd3;
	}





	public void setXzgrQadd3(Integer xzgrQadd3) {
		this.xzgrQadd3 = xzgrQadd3;
	}





	public Integer getXzgrQadd4() {
		return xzgrQadd4;
	}





	public void setXzgrQadd4(Integer xzgrQadd4) {
		this.xzgrQadd4 = xzgrQadd4;
	}





	public Integer getXzgrQadd5() {
		return xzgrQadd5;
	}





	public void setXzgrQadd5(Integer xzgrQadd5) {
		this.xzgrQadd5 = xzgrQadd5;
	}





	public Integer getXzgrGadd1() {
		return xzgrGadd1;
	}





	public void setXzgrGadd1(Integer xzgrGadd1) {
		this.xzgrGadd1 = xzgrGadd1;
	}





	public Integer getXzgrGadd2() {
		return xzgrGadd2;
	}





	public void setXzgrGadd2(Integer xzgrGadd2) {
		this.xzgrGadd2 = xzgrGadd2;
	}





	public Integer getXzgrGadd3() {
		return xzgrGadd3;
	}





	public void setXzgrGadd3(Integer xzgrGadd3) {
		this.xzgrGadd3 = xzgrGadd3;
	}





	public Integer getXzgrGadd4() {
		return xzgrGadd4;
	}





	public void setXzgrGadd4(Integer xzgrGadd4) {
		this.xzgrGadd4 = xzgrGadd4;
	}





	public Integer getXzgrGadd5() {
		return xzgrGadd5;
	}





	public void setXzgrGadd5(Integer xzgrGadd5) {
		this.xzgrGadd5 = xzgrGadd5;
	}





	public Integer getXzjgadd1() {
		return xzjgadd1;
	}





	public void setXzjgadd1(Integer xzjgadd1) {
		this.xzjgadd1 = xzjgadd1;
	}





	public Integer getXzjgadd2() {
		return xzjgadd2;
	}





	public void setXzjgadd2(Integer xzjgadd2) {
		this.xzjgadd2 = xzjgadd2;
	}





	public Integer getXzjgadd3() {
		return xzjgadd3;
	}





	public void setXzjgadd3(Integer xzjgadd3) {
		this.xzjgadd3 = xzjgadd3;
	}





	public Integer getXzjgadd4() {
		return xzjgadd4;
	}





	public void setXzjgadd4(Integer xzjgadd4) {
		this.xzjgadd4 = xzjgadd4;
	}





	public Integer getXzjgadd5() {
		return xzjgadd5;
	}





	public void setXzjgadd5(Integer xzjgadd5) {
		this.xzjgadd5 = xzjgadd5;
	}





	public Integer getGxqyadd1() {
		return gxqyadd1;
	}





	public void setGxqyadd1(Integer gxqyadd1) {
		this.gxqyadd1 = gxqyadd1;
	}





	public Integer getGxqyadd2() {
		return gxqyadd2;
	}





	public void setGxqyadd2(Integer gxqyadd2) {
		this.gxqyadd2 = gxqyadd2;
	}





	public Integer getGxqyadd3() {
		return gxqyadd3;
	}





	public void setGxqyadd3(Integer gxqyadd3) {
		this.gxqyadd3 = gxqyadd3;
	}





	public Integer getGxqyadd4() {
		return gxqyadd4;
	}





	public void setGxqyadd4(Integer gxqyadd4) {
		this.gxqyadd4 = gxqyadd4;
	}





	public Integer getGxqyadd5() {
		return gxqyadd5;
	}





	public void setGxqyadd5(Integer gxqyadd5) {
		this.gxqyadd5 = gxqyadd5;
	}





	public Integer getGxgrQadd1() {
		return gxgrQadd1;
	}





	public void setGxgrQadd1(Integer gxgrQadd1) {
		this.gxgrQadd1 = gxgrQadd1;
	}





	public Integer getGxgrQadd2() {
		return gxgrQadd2;
	}





	public void setGxgrQadd2(Integer gxgrQadd2) {
		this.gxgrQadd2 = gxgrQadd2;
	}





	public Integer getGxgrQadd3() {
		return gxgrQadd3;
	}





	public void setGxgrQadd3(Integer gxgrQadd3) {
		this.gxgrQadd3 = gxgrQadd3;
	}





	public Integer getGxgrQadd4() {
		return gxgrQadd4;
	}





	public void setGxgrQadd4(Integer gxgrQadd4) {
		this.gxgrQadd4 = gxgrQadd4;
	}





	public Integer getGxgrQadd5() {
		return gxgrQadd5;
	}





	public void setGxgrQadd5(Integer gxgrQadd5) {
		this.gxgrQadd5 = gxgrQadd5;
	}





	public Integer getGxgrGadd1() {
		return gxgrGadd1;
	}





	public void setGxgrGadd1(Integer gxgrGadd1) {
		this.gxgrGadd1 = gxgrGadd1;
	}





	public Integer getGxgrGadd2() {
		return gxgrGadd2;
	}





	public void setGxgrGadd2(Integer gxgrGadd2) {
		this.gxgrGadd2 = gxgrGadd2;
	}





	public Integer getGxgrGadd3() {
		return gxgrGadd3;
	}





	public void setGxgrGadd3(Integer gxgrGadd3) {
		this.gxgrGadd3 = gxgrGadd3;
	}





	public Integer getGxgrGadd4() {
		return gxgrGadd4;
	}





	public void setGxgrGadd4(Integer gxgrGadd4) {
		this.gxgrGadd4 = gxgrGadd4;
	}





	public Integer getGxgrGadd5() {
		return gxgrGadd5;
	}





	public void setGxgrGadd5(Integer gxgrGadd5) {
		this.gxgrGadd5 = gxgrGadd5;
	}





	public Integer getGxjgadd1() {
		return gxjgadd1;
	}





	public void setGxjgadd1(Integer gxjgadd1) {
		this.gxjgadd1 = gxjgadd1;
	}





	public Integer getGxjgadd2() {
		return gxjgadd2;
	}





	public void setGxjgadd2(Integer gxjgadd2) {
		this.gxjgadd2 = gxjgadd2;
	}





	public Integer getGxjgadd3() {
		return gxjgadd3;
	}





	public void setGxjgadd3(Integer gxjgadd3) {
		this.gxjgadd3 = gxjgadd3;
	}





	public Integer getGxjgadd4() {
		return gxjgadd4;
	}





	public void setGxjgadd4(Integer gxjgadd4) {
		this.gxjgadd4 = gxjgadd4;
	}





	public Integer getGxjgadd5() {
		return gxjgadd5;
	}





	public void setGxjgadd5(Integer gxjgadd5) {
		this.gxjgadd5 = gxjgadd5;
	}





	public Integer getLostCerateqy() {
		return lostCerateqy;
	}





	public void setLostCerateqy(Integer lostCerateqy) {
		this.lostCerateqy = lostCerateqy;
	}





	public Integer getLostCerategrQ() {
		return lostCerategrQ;
	}





	public void setLostCerategrQ(Integer lostCerategrQ) {
		this.lostCerategrQ = lostCerategrQ;
	}





	public Integer getLostCerategrG() {
		return lostCerategrG;
	}





	public void setLostCerategrG(Integer lostCerategrG) {
		this.lostCerategrG = lostCerategrG;
	}





	public Integer getLostCeratejg() {
		return lostCeratejg;
	}





	public void setLostCeratejg(Integer lostCeratejg) {
		this.lostCeratejg = lostCeratejg;
	}





	public Integer getDamageCertificateqy() {
		return damageCertificateqy;
	}





	public void setDamageCertificateqy(Integer damageCertificateqy) {
		this.damageCertificateqy = damageCertificateqy;
	}





	public Integer getDamageCertificategrQ() {
		return damageCertificategrQ;
	}





	public void setDamageCertificategrQ(Integer damageCertificategrQ) {
		this.damageCertificategrQ = damageCertificategrQ;
	}





	public Integer getDamageCertificategrG() {
		return damageCertificategrG;
	}





	public void setDamageCertificategrG(Integer damageCertificategrG) {
		this.damageCertificategrG = damageCertificategrG;
	}





	public Integer getDamageCertificatejg() {
		return damageCertificatejg;
	}





	public void setDamageCertificatejg(Integer damageCertificatejg) {
		this.damageCertificatejg = damageCertificatejg;
	}





	public Integer getModifyNumqy() {
		return modifyNumqy;
	}





	public void setModifyNumqy(Integer modifyNumqy) {
		this.modifyNumqy = modifyNumqy;
	}





	public Integer getModifyNumgrQ() {
		return modifyNumgrQ;
	}





	public void setModifyNumgrQ(Integer modifyNumgrQ) {
		this.modifyNumgrQ = modifyNumgrQ;
	}





	public Integer getModifyNumgrG() {
		return modifyNumgrG;
	}





	public void setModifyNumgrG(Integer modifyNumgrG) {
		this.modifyNumgrG = modifyNumgrG;
	}





	public Integer getModifyNumjg() {
		return modifyNumjg;
	}





	public void setModifyNumjg(Integer modifyNumjg) {
		this.modifyNumjg = modifyNumjg;
	}





	public Integer getUpdateChangeqyNum() {
		return updateChangeqyNum;
	}





	public void setUpdateChangeqyNum(Integer updateChangeqyNum) {
		this.updateChangeqyNum = updateChangeqyNum;
	}





	public Integer getUpdateChangeqyNum2() {
		return updateChangeqyNum2;
	}





	public void setUpdateChangeqyNum2(Integer updateChangeqyNum2) {
		this.updateChangeqyNum2 = updateChangeqyNum2;
	}





	public Integer getUpdateChangeqyNum3() {
		return updateChangeqyNum3;
	}





	public void setUpdateChangeqyNum3(Integer updateChangeqyNum3) {
		this.updateChangeqyNum3 = updateChangeqyNum3;
	}





	public Integer getUpdateChangeqyNum4() {
		return updateChangeqyNum4;
	}





	public void setUpdateChangeqyNum4(Integer updateChangeqyNum4) {
		this.updateChangeqyNum4 = updateChangeqyNum4;
	}





	public Integer getUpdateChangeqyNum5() {
		return updateChangeqyNum5;
	}





	public void setUpdateChangeqyNum5(Integer updateChangeqyNum5) {
		this.updateChangeqyNum5 = updateChangeqyNum5;
	}





	public Integer getUpdateChangegrQNum() {
		return updateChangegrQNum;
	}





	public void setUpdateChangegrQNum(Integer updateChangegrQNum) {
		this.updateChangegrQNum = updateChangegrQNum;
	}





	public Integer getUpdateChangegrQNum2() {
		return updateChangegrQNum2;
	}





	public void setUpdateChangegrQNum2(Integer updateChangegrQNum2) {
		this.updateChangegrQNum2 = updateChangegrQNum2;
	}





	public Integer getUpdateChangegrQNum3() {
		return updateChangegrQNum3;
	}





	public void setUpdateChangegrQNum3(Integer updateChangegrQNum3) {
		this.updateChangegrQNum3 = updateChangegrQNum3;
	}





	public Integer getUpdateChangegrQNum4() {
		return updateChangegrQNum4;
	}





	public void setUpdateChangegrQNum4(Integer updateChangegrQNum4) {
		this.updateChangegrQNum4 = updateChangegrQNum4;
	}





	public Integer getUpdateChangegrQNum5() {
		return updateChangegrQNum5;
	}





	public void setUpdateChangegrQNum5(Integer updateChangegrQNum5) {
		this.updateChangegrQNum5 = updateChangegrQNum5;
	}





	public Integer getUpdateChangegrGNum() {
		return updateChangegrGNum;
	}





	public void setUpdateChangegrGNum(Integer updateChangegrGNum) {
		this.updateChangegrGNum = updateChangegrGNum;
	}





	public Integer getUpdateChangegrGNum2() {
		return updateChangegrGNum2;
	}





	public void setUpdateChangegrGNum2(Integer updateChangegrGNum2) {
		this.updateChangegrGNum2 = updateChangegrGNum2;
	}





	public Integer getUpdateChangegrGNum3() {
		return updateChangegrGNum3;
	}





	public void setUpdateChangegrGNum3(Integer updateChangegrGNum3) {
		this.updateChangegrGNum3 = updateChangegrGNum3;
	}





	public Integer getUpdateChangegrGNum4() {
		return updateChangegrGNum4;
	}





	public void setUpdateChangegrGNum4(Integer updateChangegrGNum4) {
		this.updateChangegrGNum4 = updateChangegrGNum4;
	}





	public Integer getUpdateChangegrGNum5() {
		return updateChangegrGNum5;
	}





	public void setUpdateChangegrGNum5(Integer updateChangegrGNum5) {
		this.updateChangegrGNum5 = updateChangegrGNum5;
	}





	public Integer getUpdateChangejgNum() {
		return updateChangejgNum;
	}





	public void setUpdateChangejgNum(Integer updateChangejgNum) {
		this.updateChangejgNum = updateChangejgNum;
	}





	public Integer getUpdateChangejgNum2() {
		return updateChangejgNum2;
	}





	public void setUpdateChangejgNum2(Integer updateChangejgNum2) {
		this.updateChangejgNum2 = updateChangejgNum2;
	}





	public Integer getUpdateChangejgNum3() {
		return updateChangejgNum3;
	}





	public void setUpdateChangejgNum3(Integer updateChangejgNum3) {
		this.updateChangejgNum3 = updateChangejgNum3;
	}





	public Integer getUpdateChangejgNum4() {
		return updateChangejgNum4;
	}





	public void setUpdateChangejgNum4(Integer updateChangejgNum4) {
		this.updateChangejgNum4 = updateChangejgNum4;
	}





	public Integer getUpdateChangejgNum5() {
		return updateChangejgNum5;
	}





	public void setUpdateChangejgNum5(Integer updateChangejgNum5) {
		this.updateChangejgNum5 = updateChangejgNum5;
	}





	public Integer getUpdateLostqyNum() {
		return updateLostqyNum;
	}





	public void setUpdateLostqyNum(Integer updateLostqyNum) {
		this.updateLostqyNum = updateLostqyNum;
	}





	public Integer getUpdateLostqyNum2() {
		return updateLostqyNum2;
	}





	public void setUpdateLostqyNum2(Integer updateLostqyNum2) {
		this.updateLostqyNum2 = updateLostqyNum2;
	}





	public Integer getUpdateLostqyNum3() {
		return updateLostqyNum3;
	}





	public void setUpdateLostqyNum3(Integer updateLostqyNum3) {
		this.updateLostqyNum3 = updateLostqyNum3;
	}





	public Integer getUpdateLostqyNum4() {
		return updateLostqyNum4;
	}





	public void setUpdateLostqyNum4(Integer updateLostqyNum4) {
		this.updateLostqyNum4 = updateLostqyNum4;
	}





	public Integer getUpdateLostqyNum5() {
		return updateLostqyNum5;
	}





	public void setUpdateLostqyNum5(Integer updateLostqyNum5) {
		this.updateLostqyNum5 = updateLostqyNum5;
	}





	public Integer getUpdateLostgrQNum() {
		return updateLostgrQNum;
	}





	public void setUpdateLostgrQNum(Integer updateLostgrQNum) {
		this.updateLostgrQNum = updateLostgrQNum;
	}





	public Integer getUpdateLostgrQNum2() {
		return updateLostgrQNum2;
	}





	public void setUpdateLostgrQNum2(Integer updateLostgrQNum2) {
		this.updateLostgrQNum2 = updateLostgrQNum2;
	}





	public Integer getUpdateLostgrQNum3() {
		return updateLostgrQNum3;
	}





	public void setUpdateLostgrQNum3(Integer updateLostgrQNum3) {
		this.updateLostgrQNum3 = updateLostgrQNum3;
	}





	public Integer getUpdateLostgrQNum4() {
		return updateLostgrQNum4;
	}





	public void setUpdateLostgrQNum4(Integer updateLostgrQNum4) {
		this.updateLostgrQNum4 = updateLostgrQNum4;
	}





	public Integer getUpdateLostgrQNum5() {
		return updateLostgrQNum5;
	}





	public void setUpdateLostgrQNum5(Integer updateLostgrQNum5) {
		this.updateLostgrQNum5 = updateLostgrQNum5;
	}





	public Integer getUpdateLostgrGNum() {
		return updateLostgrGNum;
	}





	public void setUpdateLostgrGNum(Integer updateLostgrGNum) {
		this.updateLostgrGNum = updateLostgrGNum;
	}





	public Integer getUpdateLostgrGNum2() {
		return updateLostgrGNum2;
	}





	public void setUpdateLostgrGNum2(Integer updateLostgrGNum2) {
		this.updateLostgrGNum2 = updateLostgrGNum2;
	}





	public Integer getUpdateLostgrGNum3() {
		return updateLostgrGNum3;
	}





	public void setUpdateLostgrGNum3(Integer updateLostgrGNum3) {
		this.updateLostgrGNum3 = updateLostgrGNum3;
	}





	public Integer getUpdateLostgrGNum4() {
		return updateLostgrGNum4;
	}





	public void setUpdateLostgrGNum4(Integer updateLostgrGNum4) {
		this.updateLostgrGNum4 = updateLostgrGNum4;
	}





	public Integer getUpdateLostgrGNum5() {
		return updateLostgrGNum5;
	}





	public void setUpdateLostgrGNum5(Integer updateLostgrGNum5) {
		this.updateLostgrGNum5 = updateLostgrGNum5;
	}





	public Integer getUpdateLostjgNum() {
		return updateLostjgNum;
	}





	public void setUpdateLostjgNum(Integer updateLostjgNum) {
		this.updateLostjgNum = updateLostjgNum;
	}





	public Integer getUpdateLostjgNum2() {
		return updateLostjgNum2;
	}





	public void setUpdateLostjgNum2(Integer updateLostjgNum2) {
		this.updateLostjgNum2 = updateLostjgNum2;
	}





	public Integer getUpdateLostjgNum3() {
		return updateLostjgNum3;
	}





	public void setUpdateLostjgNum3(Integer updateLostjgNum3) {
		this.updateLostjgNum3 = updateLostjgNum3;
	}





	public Integer getUpdateLostjgNum4() {
		return updateLostjgNum4;
	}





	public void setUpdateLostjgNum4(Integer updateLostjgNum4) {
		this.updateLostjgNum4 = updateLostjgNum4;
	}





	public Integer getUpdateLostjgNum5() {
		return updateLostjgNum5;
	}





	public void setUpdateLostjgNum5(Integer updateLostjgNum5) {
		this.updateLostjgNum5 = updateLostjgNum5;
	}





	public Integer getUpdateReplaceqyNum() {
		return updateReplaceqyNum;
	}





	public void setUpdateReplaceqyNum(Integer updateReplaceqyNum) {
		this.updateReplaceqyNum = updateReplaceqyNum;
	}





	public Integer getUpdateReplaceqyNum2() {
		return updateReplaceqyNum2;
	}





	public void setUpdateReplaceqyNum2(Integer updateReplaceqyNum2) {
		this.updateReplaceqyNum2 = updateReplaceqyNum2;
	}





	public Integer getUpdateReplaceqyNum3() {
		return updateReplaceqyNum3;
	}





	public void setUpdateReplaceqyNum3(Integer updateReplaceqyNum3) {
		this.updateReplaceqyNum3 = updateReplaceqyNum3;
	}





	public Integer getUpdateReplaceqyNum4() {
		return updateReplaceqyNum4;
	}





	public void setUpdateReplaceqyNum4(Integer updateReplaceqyNum4) {
		this.updateReplaceqyNum4 = updateReplaceqyNum4;
	}





	public Integer getUpdateReplaceqyNum5() {
		return updateReplaceqyNum5;
	}





	public void setUpdateReplaceqyNum5(Integer updateReplaceqyNum5) {
		this.updateReplaceqyNum5 = updateReplaceqyNum5;
	}





	public Integer getUpdateReplacegrQNum() {
		return updateReplacegrQNum;
	}





	public void setUpdateReplacegrQNum(Integer updateReplacegrQNum) {
		this.updateReplacegrQNum = updateReplacegrQNum;
	}





	public Integer getUpdateReplacegrQNum2() {
		return updateReplacegrQNum2;
	}





	public void setUpdateReplacegrQNum2(Integer updateReplacegrQNum2) {
		this.updateReplacegrQNum2 = updateReplacegrQNum2;
	}





	public Integer getUpdateReplacegrQNum3() {
		return updateReplacegrQNum3;
	}





	public void setUpdateReplacegrQNum3(Integer updateReplacegrQNum3) {
		this.updateReplacegrQNum3 = updateReplacegrQNum3;
	}





	public Integer getUpdateReplacegrQNum4() {
		return updateReplacegrQNum4;
	}





	public void setUpdateReplacegrQNum4(Integer updateReplacegrQNum4) {
		this.updateReplacegrQNum4 = updateReplacegrQNum4;
	}





	public Integer getUpdateReplacegrQNum5() {
		return updateReplacegrQNum5;
	}





	public void setUpdateReplacegrQNum5(Integer updateReplacegrQNum5) {
		this.updateReplacegrQNum5 = updateReplacegrQNum5;
	}





	public Integer getUpdateReplacegrGNum() {
		return updateReplacegrGNum;
	}





	public void setUpdateReplacegrGNum(Integer updateReplacegrGNum) {
		this.updateReplacegrGNum = updateReplacegrGNum;
	}





	public Integer getUpdateReplacegrGNum2() {
		return updateReplacegrGNum2;
	}





	public void setUpdateReplacegrGNum2(Integer updateReplacegrGNum2) {
		this.updateReplacegrGNum2 = updateReplacegrGNum2;
	}





	public Integer getUpdateReplacegrGNum3() {
		return updateReplacegrGNum3;
	}





	public void setUpdateReplacegrGNum3(Integer updateReplacegrGNum3) {
		this.updateReplacegrGNum3 = updateReplacegrGNum3;
	}





	public Integer getUpdateReplacegrGNum4() {
		return updateReplacegrGNum4;
	}





	public void setUpdateReplacegrGNum4(Integer updateReplacegrGNum4) {
		this.updateReplacegrGNum4 = updateReplacegrGNum4;
	}





	public Integer getUpdateReplacegrGNum5() {
		return updateReplacegrGNum5;
	}





	public void setUpdateReplacegrGNum5(Integer updateReplacegrGNum5) {
		this.updateReplacegrGNum5 = updateReplacegrGNum5;
	}





	public Integer getUpdateReplacejgNum() {
		return updateReplacejgNum;
	}





	public void setUpdateReplacejgNum(Integer updateReplacejgNum) {
		this.updateReplacejgNum = updateReplacejgNum;
	}





	public Integer getUpdateReplacejgNum2() {
		return updateReplacejgNum2;
	}





	public void setUpdateReplacejgNum2(Integer updateReplacejgNum2) {
		this.updateReplacejgNum2 = updateReplacejgNum2;
	}





	public Integer getUpdateReplacejgNum3() {
		return updateReplacejgNum3;
	}





	public void setUpdateReplacejgNum3(Integer updateReplacejgNum3) {
		this.updateReplacejgNum3 = updateReplacejgNum3;
	}





	public Integer getUpdateReplacejgNum4() {
		return updateReplacejgNum4;
	}





	public void setUpdateReplacejgNum4(Integer updateReplacejgNum4) {
		this.updateReplacejgNum4 = updateReplacejgNum4;
	}





	public Integer getUpdateReplacejgNum5() {
		return updateReplacejgNum5;
	}





	public void setUpdateReplacejgNum5(Integer updateReplacejgNum5) {
		this.updateReplacejgNum5 = updateReplacejgNum5;
	}





	public Integer getChangeLostqyNum() {
		return changeLostqyNum;
	}





	public void setChangeLostqyNum(Integer changeLostqyNum) {
		this.changeLostqyNum = changeLostqyNum;
	}





	public Integer getChangeLostgrQNum() {
		return changeLostgrQNum;
	}





	public void setChangeLostgrQNum(Integer changeLostgrQNum) {
		this.changeLostgrQNum = changeLostgrQNum;
	}





	public Integer getChangeLostgrGNum() {
		return changeLostgrGNum;
	}





	public void setChangeLostgrGNum(Integer changeLostgrGNum) {
		this.changeLostgrGNum = changeLostgrGNum;
	}





	public Integer getChangeLostjgNum() {
		return changeLostjgNum;
	}





	public void setChangeLostjgNum(Integer changeLostjgNum) {
		this.changeLostjgNum = changeLostjgNum;
	}





	public Integer getChangeReplaceqyNum() {
		return changeReplaceqyNum;
	}





	public void setChangeReplaceqyNum(Integer changeReplaceqyNum) {
		this.changeReplaceqyNum = changeReplaceqyNum;
	}





	public Integer getChangeReplacegrQNum() {
		return changeReplacegrQNum;
	}





	public void setChangeReplacegrQNum(Integer changeReplacegrQNum) {
		this.changeReplacegrQNum = changeReplacegrQNum;
	}





	public Integer getChangeReplacegrGNum() {
		return changeReplacegrGNum;
	}





	public void setChangeReplacegrGNum(Integer changeReplacegrGNum) {
		this.changeReplacegrGNum = changeReplacegrGNum;
	}





	public Integer getChangeReplacejgNum() {
		return changeReplacejgNum;
	}





	public void setChangeReplacejgNum(Integer changeReplacejgNum) {
		this.changeReplacejgNum = changeReplacejgNum;
	}





	public Integer getChangeUpdateLostqyNum() {
		return changeUpdateLostqyNum;
	}





	public void setChangeUpdateLostqyNum(Integer changeUpdateLostqyNum) {
		this.changeUpdateLostqyNum = changeUpdateLostqyNum;
	}





	public Integer getChangeUpdateLostqyNum2() {
		return changeUpdateLostqyNum2;
	}





	public void setChangeUpdateLostqyNum2(Integer changeUpdateLostqyNum2) {
		this.changeUpdateLostqyNum2 = changeUpdateLostqyNum2;
	}





	public Integer getChangeUpdateLostqyNum3() {
		return changeUpdateLostqyNum3;
	}





	public void setChangeUpdateLostqyNum3(Integer changeUpdateLostqyNum3) {
		this.changeUpdateLostqyNum3 = changeUpdateLostqyNum3;
	}





	public Integer getChangeUpdateLostqyNum4() {
		return changeUpdateLostqyNum4;
	}





	public void setChangeUpdateLostqyNum4(Integer changeUpdateLostqyNum4) {
		this.changeUpdateLostqyNum4 = changeUpdateLostqyNum4;
	}





	public Integer getChangeUpdateLostqyNum5() {
		return changeUpdateLostqyNum5;
	}





	public void setChangeUpdateLostqyNum5(Integer changeUpdateLostqyNum5) {
		this.changeUpdateLostqyNum5 = changeUpdateLostqyNum5;
	}





	public Integer getChangeUpdateLostgrQNum() {
		return changeUpdateLostgrQNum;
	}





	public void setChangeUpdateLostgrQNum(Integer changeUpdateLostgrQNum) {
		this.changeUpdateLostgrQNum = changeUpdateLostgrQNum;
	}





	public Integer getChangeUpdateLostgrQNum2() {
		return changeUpdateLostgrQNum2;
	}





	public void setChangeUpdateLostgrQNum2(Integer changeUpdateLostgrQNum2) {
		this.changeUpdateLostgrQNum2 = changeUpdateLostgrQNum2;
	}





	public Integer getChangeUpdateLostgrQNum3() {
		return changeUpdateLostgrQNum3;
	}





	public void setChangeUpdateLostgrQNum3(Integer changeUpdateLostgrQNum3) {
		this.changeUpdateLostgrQNum3 = changeUpdateLostgrQNum3;
	}





	public Integer getChangeUpdateLostgrQNum4() {
		return changeUpdateLostgrQNum4;
	}





	public void setChangeUpdateLostgrQNum4(Integer changeUpdateLostgrQNum4) {
		this.changeUpdateLostgrQNum4 = changeUpdateLostgrQNum4;
	}





	public Integer getChangeUpdateLostgrQNum5() {
		return changeUpdateLostgrQNum5;
	}





	public void setChangeUpdateLostgrQNum5(Integer changeUpdateLostgrQNum5) {
		this.changeUpdateLostgrQNum5 = changeUpdateLostgrQNum5;
	}





	public Integer getChangeUpdateLostgrGNum() {
		return changeUpdateLostgrGNum;
	}





	public void setChangeUpdateLostgrGNum(Integer changeUpdateLostgrGNum) {
		this.changeUpdateLostgrGNum = changeUpdateLostgrGNum;
	}





	public Integer getChangeUpdateLostgrGNum2() {
		return changeUpdateLostgrGNum2;
	}





	public void setChangeUpdateLostgrGNum2(Integer changeUpdateLostgrGNum2) {
		this.changeUpdateLostgrGNum2 = changeUpdateLostgrGNum2;
	}





	public Integer getChangeUpdateLostgrGNum3() {
		return changeUpdateLostgrGNum3;
	}





	public void setChangeUpdateLostgrGNum3(Integer changeUpdateLostgrGNum3) {
		this.changeUpdateLostgrGNum3 = changeUpdateLostgrGNum3;
	}





	public Integer getChangeUpdateLostgrGNum4() {
		return changeUpdateLostgrGNum4;
	}





	public void setChangeUpdateLostgrGNum4(Integer changeUpdateLostgrGNum4) {
		this.changeUpdateLostgrGNum4 = changeUpdateLostgrGNum4;
	}





	public Integer getChangeUpdateLostgrGNum5() {
		return changeUpdateLostgrGNum5;
	}





	public void setChangeUpdateLostgrGNum5(Integer changeUpdateLostgrGNum5) {
		this.changeUpdateLostgrGNum5 = changeUpdateLostgrGNum5;
	}





	public Integer getChangeUpdateLostjgNum() {
		return changeUpdateLostjgNum;
	}





	public void setChangeUpdateLostjgNum(Integer changeUpdateLostjgNum) {
		this.changeUpdateLostjgNum = changeUpdateLostjgNum;
	}





	public Integer getChangeUpdateLostjgNum2() {
		return changeUpdateLostjgNum2;
	}





	public void setChangeUpdateLostjgNum2(Integer changeUpdateLostjgNum2) {
		this.changeUpdateLostjgNum2 = changeUpdateLostjgNum2;
	}





	public Integer getChangeUpdateLostjgNum3() {
		return changeUpdateLostjgNum3;
	}





	public void setChangeUpdateLostjgNum3(Integer changeUpdateLostjgNum3) {
		this.changeUpdateLostjgNum3 = changeUpdateLostjgNum3;
	}





	public Integer getChangeUpdateLostjgNum4() {
		return changeUpdateLostjgNum4;
	}





	public void setChangeUpdateLostjgNum4(Integer changeUpdateLostjgNum4) {
		this.changeUpdateLostjgNum4 = changeUpdateLostjgNum4;
	}





	public Integer getChangeUpdateLostjgNum5() {
		return changeUpdateLostjgNum5;
	}





	public void setChangeUpdateLostjgNum5(Integer changeUpdateLostjgNum5) {
		this.changeUpdateLostjgNum5 = changeUpdateLostjgNum5;
	}





	public Integer getChangeUpdateReplaceqyNum() {
		return changeUpdateReplaceqyNum;
	}





	public void setChangeUpdateReplaceqyNum(Integer changeUpdateReplaceqyNum) {
		this.changeUpdateReplaceqyNum = changeUpdateReplaceqyNum;
	}





	public Integer getChangeUpdateReplaceqyNum2() {
		return changeUpdateReplaceqyNum2;
	}





	public void setChangeUpdateReplaceqyNum2(Integer changeUpdateReplaceqyNum2) {
		this.changeUpdateReplaceqyNum2 = changeUpdateReplaceqyNum2;
	}





	public Integer getChangeUpdateReplaceqyNum3() {
		return changeUpdateReplaceqyNum3;
	}





	public void setChangeUpdateReplaceqyNum3(Integer changeUpdateReplaceqyNum3) {
		this.changeUpdateReplaceqyNum3 = changeUpdateReplaceqyNum3;
	}





	public Integer getChangeUpdateReplaceqyNum4() {
		return changeUpdateReplaceqyNum4;
	}





	public void setChangeUpdateReplaceqyNum4(Integer changeUpdateReplaceqyNum4) {
		this.changeUpdateReplaceqyNum4 = changeUpdateReplaceqyNum4;
	}





	public Integer getChangeUpdateReplaceqyNum5() {
		return changeUpdateReplaceqyNum5;
	}





	public void setChangeUpdateReplaceqyNum5(Integer changeUpdateReplaceqyNum5) {
		this.changeUpdateReplaceqyNum5 = changeUpdateReplaceqyNum5;
	}





	public Integer getChangeUpdateReplacegrQNum() {
		return changeUpdateReplacegrQNum;
	}





	public void setChangeUpdateReplacegrQNum(Integer changeUpdateReplacegrQNum) {
		this.changeUpdateReplacegrQNum = changeUpdateReplacegrQNum;
	}





	public Integer getChangeUpdateReplacegrQNum2() {
		return changeUpdateReplacegrQNum2;
	}





	public void setChangeUpdateReplacegrQNum2(Integer changeUpdateReplacegrQNum2) {
		this.changeUpdateReplacegrQNum2 = changeUpdateReplacegrQNum2;
	}





	public Integer getChangeUpdateReplacegrQNum3() {
		return changeUpdateReplacegrQNum3;
	}





	public void setChangeUpdateReplacegrQNum3(Integer changeUpdateReplacegrQNum3) {
		this.changeUpdateReplacegrQNum3 = changeUpdateReplacegrQNum3;
	}





	public Integer getChangeUpdateReplacegrQNum4() {
		return changeUpdateReplacegrQNum4;
	}





	public void setChangeUpdateReplacegrQNum4(Integer changeUpdateReplacegrQNum4) {
		this.changeUpdateReplacegrQNum4 = changeUpdateReplacegrQNum4;
	}





	public Integer getChangeUpdateReplacegrQNum5() {
		return changeUpdateReplacegrQNum5;
	}





	public void setChangeUpdateReplacegrQNum5(Integer changeUpdateReplacegrQNum5) {
		this.changeUpdateReplacegrQNum5 = changeUpdateReplacegrQNum5;
	}





	public Integer getChangeUpdateReplacegrGNum() {
		return changeUpdateReplacegrGNum;
	}





	public void setChangeUpdateReplacegrGNum(Integer changeUpdateReplacegrGNum) {
		this.changeUpdateReplacegrGNum = changeUpdateReplacegrGNum;
	}





	public Integer getChangeUpdateReplacegrGNum2() {
		return changeUpdateReplacegrGNum2;
	}





	public void setChangeUpdateReplacegrGNum2(Integer changeUpdateReplacegrGNum2) {
		this.changeUpdateReplacegrGNum2 = changeUpdateReplacegrGNum2;
	}





	public Integer getChangeUpdateReplacegrGNum3() {
		return changeUpdateReplacegrGNum3;
	}





	public void setChangeUpdateReplacegrGNum3(Integer changeUpdateReplacegrGNum3) {
		this.changeUpdateReplacegrGNum3 = changeUpdateReplacegrGNum3;
	}





	public Integer getChangeUpdateReplacegrGNum4() {
		return changeUpdateReplacegrGNum4;
	}





	public void setChangeUpdateReplacegrGNum4(Integer changeUpdateReplacegrGNum4) {
		this.changeUpdateReplacegrGNum4 = changeUpdateReplacegrGNum4;
	}





	public Integer getChangeUpdateReplacegrGNum5() {
		return changeUpdateReplacegrGNum5;
	}





	public void setChangeUpdateReplacegrGNum5(Integer changeUpdateReplacegrGNum5) {
		this.changeUpdateReplacegrGNum5 = changeUpdateReplacegrGNum5;
	}





	public Integer getChangeUpdateReplacejgNum() {
		return changeUpdateReplacejgNum;
	}





	public void setChangeUpdateReplacejgNum(Integer changeUpdateReplacejgNum) {
		this.changeUpdateReplacejgNum = changeUpdateReplacejgNum;
	}





	public Integer getChangeUpdateReplacejgNum2() {
		return changeUpdateReplacejgNum2;
	}





	public void setChangeUpdateReplacejgNum2(Integer changeUpdateReplacejgNum2) {
		this.changeUpdateReplacejgNum2 = changeUpdateReplacejgNum2;
	}





	public Integer getChangeUpdateReplacejgNum3() {
		return changeUpdateReplacejgNum3;
	}





	public void setChangeUpdateReplacejgNum3(Integer changeUpdateReplacejgNum3) {
		this.changeUpdateReplacejgNum3 = changeUpdateReplacejgNum3;
	}





	public Integer getChangeUpdateReplacejgNum4() {
		return changeUpdateReplacejgNum4;
	}





	public void setChangeUpdateReplacejgNum4(Integer changeUpdateReplacejgNum4) {
		this.changeUpdateReplacejgNum4 = changeUpdateReplacejgNum4;
	}





	public Integer getChangeUpdateReplacejgNum5() {
		return changeUpdateReplacejgNum5;
	}





	public void setChangeUpdateReplacejgNum5(Integer changeUpdateReplacejgNum5) {
		this.changeUpdateReplacejgNum5 = changeUpdateReplacejgNum5;
	}





	
}