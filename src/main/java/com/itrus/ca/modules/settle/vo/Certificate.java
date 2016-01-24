package com.itrus.ca.modules.settle.vo;

public class Certificate {
	// 新增企业
	private Integer xzqyxjadd1;
	private Integer xzqypoadd1;
	private Integer xzqyxjadd2;
	private Integer xzqypoadd2;
	private Integer xzqyxjadd4;
	private Integer xzqypoadd4;
	private Integer xzqyxjadd5;
	private Integer xzqypoadd5;
	// 新增个人（企业）
	private Integer xzgrQxjadd1;
	private Integer xzgrQpoadd1;
	private Integer xzgrQxjadd2;
	private Integer xzgrQpoadd2;
	private Integer xzgrQxjadd4;
	private Integer xzgrQpoadd4;
	private Integer xzgrQxjadd5;
	private Integer xzgrQpoadd5;
	// 新增个人（个人）
	private Integer xzgrGxjadd1;
	private Integer xzgrGpoadd1;
	private Integer xzgrGxjadd2;
	private Integer xzgrGpoadd2;
	private Integer xzgrGxjadd4;
	private Integer xzgrGpoadd4;
	private Integer xzgrGxjadd5;
	private Integer xzgrGpoadd5;
	// 更新企业
	private Integer gxqyxjadd1;
	private Integer gxqypoadd1;
	private Integer gxqyxjadd2;
	private Integer gxqypoadd2;
	private Integer gxqyxjadd4;
	private Integer gxqypoadd4;
	private Integer gxqyxjadd5;
	private Integer gxqypoadd5;
	// 更新个人（企业）
	private Integer gxgrQxjadd1;
	private Integer gxgrQpoadd1;
	private Integer gxgrQxjadd2;
	private Integer gxgrQpoadd2;
	private Integer gxgrQxjadd4;
	private Integer gxgrQpoadd4;
	private Integer gxgrQxjadd5;
	private Integer gxgrQpoadd5;
	// 更新个人（个人）
	private Integer gxgrGxjadd1;
	private Integer gxgrGpoadd1;
	private Integer gxgrGxjadd2;
	private Integer gxgrGpoadd2;
	private Integer gxgrGxjadd4;
	private Integer gxgrGpoadd4;
	private Integer gxgrGxjadd5;
	private Integer gxgrGpoadd5;
	private Integer lostCeratexj;// 遗失补办
	private Integer lostCeratepo;
	private Integer damageCertificatexj;// 损坏更换
	private Integer damageCertificatepo;
	private Integer modifyNumxj;// 变更数量
	private Integer modifyNumpo;
	private Integer updateChangeNumxj;// 更新+变更
	private Integer updateChangeNum2xj;// 更新+变更 2年
	private Integer updateChangeNum4xj;// 更新+变更 4年
	private Integer updateChangeNum5xj;// 更新+变更 5年
	private Integer updateChangeNumpo;
	private Integer updateChangeNum2po;
	private Integer updateChangeNum4po;
	private Integer updateChangeNum5po;
	private Integer updateLostNumxj;// 更新+遗失补办
	private Integer updateLostNum2xj;// 更新+遗失补办 2年
	private Integer updateLostNum4xj;// 更新+遗失补办 4年
	private Integer updateLostNum5xj;// 更新+遗失补办 5年
	private Integer updateLostNumpo;
	private Integer updateLostNum2po;
	private Integer updateLostNum4po;
	private Integer updateLostNum5po;
	private Integer updateReplaceNumxj;// 更新+损坏更换
	private Integer updateReplaceNum2xj;// 更新+损坏更换 2年
	private Integer updateReplaceNum4xj;// 更新+损坏更换 4年
	private Integer updateReplaceNum5xj;// 更新+损坏更换 5年
	private Integer updateReplaceNumpo;
	private Integer updateReplaceNum2po;
	private Integer updateReplaceNum4po;
	private Integer updateReplaceNum5po;
	private Integer changeLostNumxj;// 变更+遗失补办
	private Integer changeLostNumpo;
	private Integer changeReplaceNumxj;// 变更+损坏更换
	private Integer changeReplaceNumpo;
	private Integer changeUpdateLostNumxj;// 更新+变更+遗失补办
	private Integer changeUpdateLostNum2xj;// 更新+变更+遗失补办 2年
	private Integer changeUpdateLostNum4xj;// 更新+变更+遗失补办 4年
	private Integer changeUpdateLostNum5xj;// 更新+变更+遗失补办 5年
	private Integer changeUpdateLostNumpo;
	private Integer changeUpdateLostNum2po;
	private Integer changeUpdateLostNum4po;
	private Integer changeUpdateLostNum5po;
	private Integer changeUpdateReplaceNumxj;// 更新+变更+损坏更换
	private Integer changeUpdateReplaceNum2xj;// 更新+变更+损坏更换 2年
	private Integer changeUpdateReplaceNum4xj;// 更新+变更+损坏更换 4年
	private Integer changeUpdateReplaceNum5xj;// 更新+变更+损坏更换 5年
	private Integer changeUpdateReplaceNumpo;
	private Integer changeUpdateReplaceNum2po;
	private Integer changeUpdateReplaceNum4po;
	private Integer changeUpdateReplaceNum5po;
	
	public Certificate() {
		
	}

	public Certificate(Integer xzqyxjadd1, Integer xzqypoadd1, Integer xzqyxjadd2, Integer xzqypoadd2,
			Integer xzqyxjadd4, Integer xzqypoadd4, Integer xzqyxjadd5, Integer xzqypoadd5, Integer xzgrQxjadd1,
			Integer xzgrQpoadd1, Integer xzgrQxjadd2, Integer xzgrQpoadd2, Integer xzgrQxjadd4, Integer xzgrQpoadd4,
			Integer xzgrQxjadd5, Integer xzgrQpoadd5, Integer xzgrGxjadd1, Integer xzgrGpoadd1, Integer xzgrGxjadd2,
			Integer xzgrGpoadd2, Integer xzgrGxjadd4, Integer xzgrGpoadd4, Integer xzgrGxjadd5, Integer xzgrGpoadd5,
			Integer gxqyxjadd1, Integer gxqypoadd1, Integer gxqyxjadd2, Integer gxqypoadd2, Integer gxqyxjadd4,
			Integer gxqypoadd4, Integer gxqyxjadd5, Integer gxqypoadd5, Integer gxgrQxjadd1, Integer gxgrQpoadd1,
			Integer gxgrQxjadd2, Integer gxgrQpoadd2, Integer gxgrQxjadd4, Integer gxgrQpoadd4, Integer gxgrQxjadd5,
			Integer gxgrQpoadd5, Integer gxgrGxjadd1, Integer gxgrGpoadd1, Integer gxgrGxjadd2, Integer gxgrGpoadd2,
			Integer gxgrGxjadd4, Integer gxgrGpoadd4, Integer gxgrGxjadd5, Integer gxgrGpoadd5, Integer lostCeratexj,
			Integer lostCeratepo, Integer damageCertificatexj, Integer damageCertificatepo, Integer modifyNumxj,
			Integer modifyNumpo, Integer updateChangeNumxj, Integer updateChangeNum2xj, Integer updateChangeNum4xj,
			Integer updateChangeNum5xj, Integer updateChangeNumpo, Integer updateChangeNum2po,
			Integer updateChangeNum4po, Integer updateChangeNum5po, Integer updateLostNumxj, Integer updateLostNum2xj,
			Integer updateLostNum4xj, Integer updateLostNum5xj, Integer updateLostNumpo, Integer updateLostNum2po,
			Integer updateLostNum4po, Integer updateLostNum5po, Integer updateReplaceNumxj, Integer updateReplaceNum2xj,
			Integer updateReplaceNum4xj, Integer updateReplaceNum5xj, Integer updateReplaceNumpo,
			Integer updateReplaceNum2po, Integer updateReplaceNum4po, Integer updateReplaceNum5po,
			Integer changeLostNumxj, Integer changeLostNumpo, Integer changeReplaceNumxj, Integer changeReplaceNumpo,
			Integer changeUpdateLostNumxj, Integer changeUpdateLostNum2xj, Integer changeUpdateLostNum4xj,
			Integer changeUpdateLostNum5xj, Integer changeUpdateLostNumpo, Integer changeUpdateLostNum2po,
			Integer changeUpdateLostNum4po, Integer changeUpdateLostNum5po, Integer changeUpdateReplaceNumxj,
			Integer changeUpdateReplaceNum2xj, Integer changeUpdateReplaceNum4xj, Integer changeUpdateReplaceNum5xj,
			Integer changeUpdateReplaceNumpo, Integer changeUpdateReplaceNum2po, Integer changeUpdateReplaceNum4po,
			Integer changeUpdateReplaceNum5po) {
		super();
		this.xzqyxjadd1 = xzqyxjadd1;
		this.xzqypoadd1 = xzqypoadd1;
		this.xzqyxjadd2 = xzqyxjadd2;
		this.xzqypoadd2 = xzqypoadd2;
		this.xzqyxjadd4 = xzqyxjadd4;
		this.xzqypoadd4 = xzqypoadd4;
		this.xzqyxjadd5 = xzqyxjadd5;
		this.xzqypoadd5 = xzqypoadd5;
		this.xzgrQxjadd1 = xzgrQxjadd1;
		this.xzgrQpoadd1 = xzgrQpoadd1;
		this.xzgrQxjadd2 = xzgrQxjadd2;
		this.xzgrQpoadd2 = xzgrQpoadd2;
		this.xzgrQxjadd4 = xzgrQxjadd4;
		this.xzgrQpoadd4 = xzgrQpoadd4;
		this.xzgrQxjadd5 = xzgrQxjadd5;
		this.xzgrQpoadd5 = xzgrQpoadd5;
		this.xzgrGxjadd1 = xzgrGxjadd1;
		this.xzgrGpoadd1 = xzgrGpoadd1;
		this.xzgrGxjadd2 = xzgrGxjadd2;
		this.xzgrGpoadd2 = xzgrGpoadd2;
		this.xzgrGxjadd4 = xzgrGxjadd4;
		this.xzgrGpoadd4 = xzgrGpoadd4;
		this.xzgrGxjadd5 = xzgrGxjadd5;
		this.xzgrGpoadd5 = xzgrGpoadd5;
		this.gxqyxjadd1 = gxqyxjadd1;
		this.gxqypoadd1 = gxqypoadd1;
		this.gxqyxjadd2 = gxqyxjadd2;
		this.gxqypoadd2 = gxqypoadd2;
		this.gxqyxjadd4 = gxqyxjadd4;
		this.gxqypoadd4 = gxqypoadd4;
		this.gxqyxjadd5 = gxqyxjadd5;
		this.gxqypoadd5 = gxqypoadd5;
		this.gxgrQxjadd1 = gxgrQxjadd1;
		this.gxgrQpoadd1 = gxgrQpoadd1;
		this.gxgrQxjadd2 = gxgrQxjadd2;
		this.gxgrQpoadd2 = gxgrQpoadd2;
		this.gxgrQxjadd4 = gxgrQxjadd4;
		this.gxgrQpoadd4 = gxgrQpoadd4;
		this.gxgrQxjadd5 = gxgrQxjadd5;
		this.gxgrQpoadd5 = gxgrQpoadd5;
		this.gxgrGxjadd1 = gxgrGxjadd1;
		this.gxgrGpoadd1 = gxgrGpoadd1;
		this.gxgrGxjadd2 = gxgrGxjadd2;
		this.gxgrGpoadd2 = gxgrGpoadd2;
		this.gxgrGxjadd4 = gxgrGxjadd4;
		this.gxgrGpoadd4 = gxgrGpoadd4;
		this.gxgrGxjadd5 = gxgrGxjadd5;
		this.gxgrGpoadd5 = gxgrGpoadd5;
		this.lostCeratexj = lostCeratexj;
		this.lostCeratepo = lostCeratepo;
		this.damageCertificatexj = damageCertificatexj;
		this.damageCertificatepo = damageCertificatepo;
		this.modifyNumxj = modifyNumxj;
		this.modifyNumpo = modifyNumpo;
		this.updateChangeNumxj = updateChangeNumxj;
		this.updateChangeNum2xj = updateChangeNum2xj;
		this.updateChangeNum4xj = updateChangeNum4xj;
		this.updateChangeNum5xj = updateChangeNum5xj;
		this.updateChangeNumpo = updateChangeNumpo;
		this.updateChangeNum2po = updateChangeNum2po;
		this.updateChangeNum4po = updateChangeNum4po;
		this.updateChangeNum5po = updateChangeNum5po;
		this.updateLostNumxj = updateLostNumxj;
		this.updateLostNum2xj = updateLostNum2xj;
		this.updateLostNum4xj = updateLostNum4xj;
		this.updateLostNum5xj = updateLostNum5xj;
		this.updateLostNumpo = updateLostNumpo;
		this.updateLostNum2po = updateLostNum2po;
		this.updateLostNum4po = updateLostNum4po;
		this.updateLostNum5po = updateLostNum5po;
		this.updateReplaceNumxj = updateReplaceNumxj;
		this.updateReplaceNum2xj = updateReplaceNum2xj;
		this.updateReplaceNum4xj = updateReplaceNum4xj;
		this.updateReplaceNum5xj = updateReplaceNum5xj;
		this.updateReplaceNumpo = updateReplaceNumpo;
		this.updateReplaceNum2po = updateReplaceNum2po;
		this.updateReplaceNum4po = updateReplaceNum4po;
		this.updateReplaceNum5po = updateReplaceNum5po;
		this.changeLostNumxj = changeLostNumxj;
		this.changeLostNumpo = changeLostNumpo;
		this.changeReplaceNumxj = changeReplaceNumxj;
		this.changeReplaceNumpo = changeReplaceNumpo;
		this.changeUpdateLostNumxj = changeUpdateLostNumxj;
		this.changeUpdateLostNum2xj = changeUpdateLostNum2xj;
		this.changeUpdateLostNum4xj = changeUpdateLostNum4xj;
		this.changeUpdateLostNum5xj = changeUpdateLostNum5xj;
		this.changeUpdateLostNumpo = changeUpdateLostNumpo;
		this.changeUpdateLostNum2po = changeUpdateLostNum2po;
		this.changeUpdateLostNum4po = changeUpdateLostNum4po;
		this.changeUpdateLostNum5po = changeUpdateLostNum5po;
		this.changeUpdateReplaceNumxj = changeUpdateReplaceNumxj;
		this.changeUpdateReplaceNum2xj = changeUpdateReplaceNum2xj;
		this.changeUpdateReplaceNum4xj = changeUpdateReplaceNum4xj;
		this.changeUpdateReplaceNum5xj = changeUpdateReplaceNum5xj;
		this.changeUpdateReplaceNumpo = changeUpdateReplaceNumpo;
		this.changeUpdateReplaceNum2po = changeUpdateReplaceNum2po;
		this.changeUpdateReplaceNum4po = changeUpdateReplaceNum4po;
		this.changeUpdateReplaceNum5po = changeUpdateReplaceNum5po;
	}

	public Integer getXzqyxjadd1() {
		return xzqyxjadd1;
	}

	public void setXzqyxjadd1(Integer xzqyxjadd1) {
		this.xzqyxjadd1 = xzqyxjadd1;
	}

	public Integer getXzqypoadd1() {
		return xzqypoadd1;
	}

	public void setXzqypoadd1(Integer xzqypoadd1) {
		this.xzqypoadd1 = xzqypoadd1;
	}

	public Integer getXzqyxjadd2() {
		return xzqyxjadd2;
	}

	public void setXzqyxjadd2(Integer xzqyxjadd2) {
		this.xzqyxjadd2 = xzqyxjadd2;
	}

	public Integer getXzqypoadd2() {
		return xzqypoadd2;
	}

	public void setXzqypoadd2(Integer xzqypoadd2) {
		this.xzqypoadd2 = xzqypoadd2;
	}

	public Integer getXzqyxjadd4() {
		return xzqyxjadd4;
	}

	public void setXzqyxjadd4(Integer xzqyxjadd4) {
		this.xzqyxjadd4 = xzqyxjadd4;
	}

	public Integer getXzqypoadd4() {
		return xzqypoadd4;
	}

	public void setXzqypoadd4(Integer xzqypoadd4) {
		this.xzqypoadd4 = xzqypoadd4;
	}

	public Integer getXzqyxjadd5() {
		return xzqyxjadd5;
	}

	public void setXzqyxjadd5(Integer xzqyxjadd5) {
		this.xzqyxjadd5 = xzqyxjadd5;
	}

	public Integer getXzqypoadd5() {
		return xzqypoadd5;
	}

	public void setXzqypoadd5(Integer xzqypoadd5) {
		this.xzqypoadd5 = xzqypoadd5;
	}

	public Integer getXzgrQxjadd1() {
		return xzgrQxjadd1;
	}

	public void setXzgrQxjadd1(Integer xzgrQxjadd1) {
		this.xzgrQxjadd1 = xzgrQxjadd1;
	}

	public Integer getXzgrQpoadd1() {
		return xzgrQpoadd1;
	}

	public void setXzgrQpoadd1(Integer xzgrQpoadd1) {
		this.xzgrQpoadd1 = xzgrQpoadd1;
	}

	public Integer getXzgrQxjadd2() {
		return xzgrQxjadd2;
	}

	public void setXzgrQxjadd2(Integer xzgrQxjadd2) {
		this.xzgrQxjadd2 = xzgrQxjadd2;
	}

	public Integer getXzgrQpoadd2() {
		return xzgrQpoadd2;
	}

	public void setXzgrQpoadd2(Integer xzgrQpoadd2) {
		this.xzgrQpoadd2 = xzgrQpoadd2;
	}

	public Integer getXzgrQxjadd4() {
		return xzgrQxjadd4;
	}

	public void setXzgrQxjadd4(Integer xzgrQxjadd4) {
		this.xzgrQxjadd4 = xzgrQxjadd4;
	}

	public Integer getXzgrQpoadd4() {
		return xzgrQpoadd4;
	}

	public void setXzgrQpoadd4(Integer xzgrQpoadd4) {
		this.xzgrQpoadd4 = xzgrQpoadd4;
	}

	public Integer getXzgrQxjadd5() {
		return xzgrQxjadd5;
	}

	public void setXzgrQxjadd5(Integer xzgrQxjadd5) {
		this.xzgrQxjadd5 = xzgrQxjadd5;
	}

	public Integer getXzgrQpoadd5() {
		return xzgrQpoadd5;
	}

	public void setXzgrQpoadd5(Integer xzgrQpoadd5) {
		this.xzgrQpoadd5 = xzgrQpoadd5;
	}

	public Integer getXzgrGxjadd1() {
		return xzgrGxjadd1;
	}

	public void setXzgrGxjadd1(Integer xzgrGxjadd1) {
		this.xzgrGxjadd1 = xzgrGxjadd1;
	}

	public Integer getXzgrGpoadd1() {
		return xzgrGpoadd1;
	}

	public void setXzgrGpoadd1(Integer xzgrGpoadd1) {
		this.xzgrGpoadd1 = xzgrGpoadd1;
	}

	public Integer getXzgrGxjadd2() {
		return xzgrGxjadd2;
	}

	public void setXzgrGxjadd2(Integer xzgrGxjadd2) {
		this.xzgrGxjadd2 = xzgrGxjadd2;
	}

	public Integer getXzgrGpoadd2() {
		return xzgrGpoadd2;
	}

	public void setXzgrGpoadd2(Integer xzgrGpoadd2) {
		this.xzgrGpoadd2 = xzgrGpoadd2;
	}

	public Integer getXzgrGxjadd4() {
		return xzgrGxjadd4;
	}

	public void setXzgrGxjadd4(Integer xzgrGxjadd4) {
		this.xzgrGxjadd4 = xzgrGxjadd4;
	}

	public Integer getXzgrGpoadd4() {
		return xzgrGpoadd4;
	}

	public void setXzgrGpoadd4(Integer xzgrGpoadd4) {
		this.xzgrGpoadd4 = xzgrGpoadd4;
	}

	public Integer getXzgrGxjadd5() {
		return xzgrGxjadd5;
	}

	public void setXzgrGxjadd5(Integer xzgrGxjadd5) {
		this.xzgrGxjadd5 = xzgrGxjadd5;
	}

	public Integer getXzgrGpoadd5() {
		return xzgrGpoadd5;
	}

	public void setXzgrGpoadd5(Integer xzgrGpoadd5) {
		this.xzgrGpoadd5 = xzgrGpoadd5;
	}

	public Integer getGxqyxjadd1() {
		return gxqyxjadd1;
	}

	public void setGxqyxjadd1(Integer gxqyxjadd1) {
		this.gxqyxjadd1 = gxqyxjadd1;
	}

	public Integer getGxqypoadd1() {
		return gxqypoadd1;
	}

	public void setGxqypoadd1(Integer gxqypoadd1) {
		this.gxqypoadd1 = gxqypoadd1;
	}

	public Integer getGxqyxjadd2() {
		return gxqyxjadd2;
	}

	public void setGxqyxjadd2(Integer gxqyxjadd2) {
		this.gxqyxjadd2 = gxqyxjadd2;
	}

	public Integer getGxqypoadd2() {
		return gxqypoadd2;
	}

	public void setGxqypoadd2(Integer gxqypoadd2) {
		this.gxqypoadd2 = gxqypoadd2;
	}

	public Integer getGxqyxjadd4() {
		return gxqyxjadd4;
	}

	public void setGxqyxjadd4(Integer gxqyxjadd4) {
		this.gxqyxjadd4 = gxqyxjadd4;
	}

	public Integer getGxqypoadd4() {
		return gxqypoadd4;
	}

	public void setGxqypoadd4(Integer gxqypoadd4) {
		this.gxqypoadd4 = gxqypoadd4;
	}

	public Integer getGxqyxjadd5() {
		return gxqyxjadd5;
	}

	public void setGxqyxjadd5(Integer gxqyxjadd5) {
		this.gxqyxjadd5 = gxqyxjadd5;
	}

	public Integer getGxqypoadd5() {
		return gxqypoadd5;
	}

	public void setGxqypoadd5(Integer gxqypoadd5) {
		this.gxqypoadd5 = gxqypoadd5;
	}

	public Integer getGxgrQxjadd1() {
		return gxgrQxjadd1;
	}

	public void setGxgrQxjadd1(Integer gxgrQxjadd1) {
		this.gxgrQxjadd1 = gxgrQxjadd1;
	}

	public Integer getGxgrQpoadd1() {
		return gxgrQpoadd1;
	}

	public void setGxgrQpoadd1(Integer gxgrQpoadd1) {
		this.gxgrQpoadd1 = gxgrQpoadd1;
	}

	public Integer getGxgrQxjadd2() {
		return gxgrQxjadd2;
	}

	public void setGxgrQxjadd2(Integer gxgrQxjadd2) {
		this.gxgrQxjadd2 = gxgrQxjadd2;
	}

	public Integer getGxgrQpoadd2() {
		return gxgrQpoadd2;
	}

	public void setGxgrQpoadd2(Integer gxgrQpoadd2) {
		this.gxgrQpoadd2 = gxgrQpoadd2;
	}

	public Integer getGxgrQxjadd4() {
		return gxgrQxjadd4;
	}

	public void setGxgrQxjadd4(Integer gxgrQxjadd4) {
		this.gxgrQxjadd4 = gxgrQxjadd4;
	}

	public Integer getGxgrQpoadd4() {
		return gxgrQpoadd4;
	}

	public void setGxgrQpoadd4(Integer gxgrQpoadd4) {
		this.gxgrQpoadd4 = gxgrQpoadd4;
	}

	public Integer getGxgrQxjadd5() {
		return gxgrQxjadd5;
	}

	public void setGxgrQxjadd5(Integer gxgrQxjadd5) {
		this.gxgrQxjadd5 = gxgrQxjadd5;
	}

	public Integer getGxgrQpoadd5() {
		return gxgrQpoadd5;
	}

	public void setGxgrQpoadd5(Integer gxgrQpoadd5) {
		this.gxgrQpoadd5 = gxgrQpoadd5;
	}

	public Integer getGxgrGxjadd1() {
		return gxgrGxjadd1;
	}

	public void setGxgrGxjadd1(Integer gxgrGxjadd1) {
		this.gxgrGxjadd1 = gxgrGxjadd1;
	}

	public Integer getGxgrGpoadd1() {
		return gxgrGpoadd1;
	}

	public void setGxgrGpoadd1(Integer gxgrGpoadd1) {
		this.gxgrGpoadd1 = gxgrGpoadd1;
	}

	public Integer getGxgrGxjadd2() {
		return gxgrGxjadd2;
	}

	public void setGxgrGxjadd2(Integer gxgrGxjadd2) {
		this.gxgrGxjadd2 = gxgrGxjadd2;
	}

	public Integer getGxgrGpoadd2() {
		return gxgrGpoadd2;
	}

	public void setGxgrGpoadd2(Integer gxgrGpoadd2) {
		this.gxgrGpoadd2 = gxgrGpoadd2;
	}

	public Integer getGxgrGxjadd4() {
		return gxgrGxjadd4;
	}

	public void setGxgrGxjadd4(Integer gxgrGxjadd4) {
		this.gxgrGxjadd4 = gxgrGxjadd4;
	}

	public Integer getGxgrGpoadd4() {
		return gxgrGpoadd4;
	}

	public void setGxgrGpoadd4(Integer gxgrGpoadd4) {
		this.gxgrGpoadd4 = gxgrGpoadd4;
	}

	public Integer getGxgrGxjadd5() {
		return gxgrGxjadd5;
	}

	public void setGxgrGxjadd5(Integer gxgrGxjadd5) {
		this.gxgrGxjadd5 = gxgrGxjadd5;
	}

	public Integer getGxgrGpoadd5() {
		return gxgrGpoadd5;
	}

	public void setGxgrGpoadd5(Integer gxgrGpoadd5) {
		this.gxgrGpoadd5 = gxgrGpoadd5;
	}

	public Integer getLostCeratexj() {
		return lostCeratexj;
	}

	public void setLostCeratexj(Integer lostCeratexj) {
		this.lostCeratexj = lostCeratexj;
	}

	public Integer getLostCeratepo() {
		return lostCeratepo;
	}

	public void setLostCeratepo(Integer lostCeratepo) {
		this.lostCeratepo = lostCeratepo;
	}

	public Integer getDamageCertificatexj() {
		return damageCertificatexj;
	}

	public void setDamageCertificatexj(Integer damageCertificatexj) {
		this.damageCertificatexj = damageCertificatexj;
	}

	public Integer getDamageCertificatepo() {
		return damageCertificatepo;
	}

	public void setDamageCertificatepo(Integer damageCertificatepo) {
		this.damageCertificatepo = damageCertificatepo;
	}

	public Integer getModifyNumxj() {
		return modifyNumxj;
	}

	public void setModifyNumxj(Integer modifyNumxj) {
		this.modifyNumxj = modifyNumxj;
	}

	public Integer getModifyNumpo() {
		return modifyNumpo;
	}

	public void setModifyNumpo(Integer modifyNumpo) {
		this.modifyNumpo = modifyNumpo;
	}

	public Integer getUpdateChangeNumxj() {
		return updateChangeNumxj;
	}

	public void setUpdateChangeNumxj(Integer updateChangeNumxj) {
		this.updateChangeNumxj = updateChangeNumxj;
	}

	public Integer getUpdateChangeNum2xj() {
		return updateChangeNum2xj;
	}

	public void setUpdateChangeNum2xj(Integer updateChangeNum2xj) {
		this.updateChangeNum2xj = updateChangeNum2xj;
	}

	public Integer getUpdateChangeNum4xj() {
		return updateChangeNum4xj;
	}

	public void setUpdateChangeNum4xj(Integer updateChangeNum4xj) {
		this.updateChangeNum4xj = updateChangeNum4xj;
	}

	public Integer getUpdateChangeNum5xj() {
		return updateChangeNum5xj;
	}

	public void setUpdateChangeNum5xj(Integer updateChangeNum5xj) {
		this.updateChangeNum5xj = updateChangeNum5xj;
	}

	public Integer getUpdateChangeNumpo() {
		return updateChangeNumpo;
	}

	public void setUpdateChangeNumpo(Integer updateChangeNumpo) {
		this.updateChangeNumpo = updateChangeNumpo;
	}

	public Integer getUpdateChangeNum2po() {
		return updateChangeNum2po;
	}

	public void setUpdateChangeNum2po(Integer updateChangeNum2po) {
		this.updateChangeNum2po = updateChangeNum2po;
	}

	public Integer getUpdateChangeNum4po() {
		return updateChangeNum4po;
	}

	public void setUpdateChangeNum4po(Integer updateChangeNum4po) {
		this.updateChangeNum4po = updateChangeNum4po;
	}

	public Integer getUpdateChangeNum5po() {
		return updateChangeNum5po;
	}

	public void setUpdateChangeNum5po(Integer updateChangeNum5po) {
		this.updateChangeNum5po = updateChangeNum5po;
	}

	public Integer getUpdateLostNumxj() {
		return updateLostNumxj;
	}

	public void setUpdateLostNumxj(Integer updateLostNumxj) {
		this.updateLostNumxj = updateLostNumxj;
	}

	public Integer getUpdateLostNum2xj() {
		return updateLostNum2xj;
	}

	public void setUpdateLostNum2xj(Integer updateLostNum2xj) {
		this.updateLostNum2xj = updateLostNum2xj;
	}

	public Integer getUpdateLostNum4xj() {
		return updateLostNum4xj;
	}

	public void setUpdateLostNum4xj(Integer updateLostNum4xj) {
		this.updateLostNum4xj = updateLostNum4xj;
	}

	public Integer getUpdateLostNum5xj() {
		return updateLostNum5xj;
	}

	public void setUpdateLostNum5xj(Integer updateLostNum5xj) {
		this.updateLostNum5xj = updateLostNum5xj;
	}

	public Integer getUpdateLostNumpo() {
		return updateLostNumpo;
	}

	public void setUpdateLostNumpo(Integer updateLostNumpo) {
		this.updateLostNumpo = updateLostNumpo;
	}

	public Integer getUpdateLostNum2po() {
		return updateLostNum2po;
	}

	public void setUpdateLostNum2po(Integer updateLostNum2po) {
		this.updateLostNum2po = updateLostNum2po;
	}

	public Integer getUpdateLostNum4po() {
		return updateLostNum4po;
	}

	public void setUpdateLostNum4po(Integer updateLostNum4po) {
		this.updateLostNum4po = updateLostNum4po;
	}

	public Integer getUpdateLostNum5po() {
		return updateLostNum5po;
	}

	public void setUpdateLostNum5po(Integer updateLostNum5po) {
		this.updateLostNum5po = updateLostNum5po;
	}

	public Integer getUpdateReplaceNumxj() {
		return updateReplaceNumxj;
	}

	public void setUpdateReplaceNumxj(Integer updateReplaceNumxj) {
		this.updateReplaceNumxj = updateReplaceNumxj;
	}

	public Integer getUpdateReplaceNum2xj() {
		return updateReplaceNum2xj;
	}

	public void setUpdateReplaceNum2xj(Integer updateReplaceNum2xj) {
		this.updateReplaceNum2xj = updateReplaceNum2xj;
	}

	public Integer getUpdateReplaceNum4xj() {
		return updateReplaceNum4xj;
	}

	public void setUpdateReplaceNum4xj(Integer updateReplaceNum4xj) {
		this.updateReplaceNum4xj = updateReplaceNum4xj;
	}

	public Integer getUpdateReplaceNum5xj() {
		return updateReplaceNum5xj;
	}

	public void setUpdateReplaceNum5xj(Integer updateReplaceNum5xj) {
		this.updateReplaceNum5xj = updateReplaceNum5xj;
	}

	public Integer getUpdateReplaceNumpo() {
		return updateReplaceNumpo;
	}

	public void setUpdateReplaceNumpo(Integer updateReplaceNumpo) {
		this.updateReplaceNumpo = updateReplaceNumpo;
	}

	public Integer getUpdateReplaceNum2po() {
		return updateReplaceNum2po;
	}

	public void setUpdateReplaceNum2po(Integer updateReplaceNum2po) {
		this.updateReplaceNum2po = updateReplaceNum2po;
	}

	public Integer getUpdateReplaceNum4po() {
		return updateReplaceNum4po;
	}

	public void setUpdateReplaceNum4po(Integer updateReplaceNum4po) {
		this.updateReplaceNum4po = updateReplaceNum4po;
	}

	public Integer getUpdateReplaceNum5po() {
		return updateReplaceNum5po;
	}

	public void setUpdateReplaceNum5po(Integer updateReplaceNum5po) {
		this.updateReplaceNum5po = updateReplaceNum5po;
	}

	public Integer getChangeLostNumxj() {
		return changeLostNumxj;
	}

	public void setChangeLostNumxj(Integer changeLostNumxj) {
		this.changeLostNumxj = changeLostNumxj;
	}

	public Integer getChangeLostNumpo() {
		return changeLostNumpo;
	}

	public void setChangeLostNumpo(Integer changeLostNumpo) {
		this.changeLostNumpo = changeLostNumpo;
	}

	public Integer getChangeReplaceNumxj() {
		return changeReplaceNumxj;
	}

	public void setChangeReplaceNumxj(Integer changeReplaceNumxj) {
		this.changeReplaceNumxj = changeReplaceNumxj;
	}

	public Integer getChangeReplaceNumpo() {
		return changeReplaceNumpo;
	}

	public void setChangeReplaceNumpo(Integer changeReplaceNumpo) {
		this.changeReplaceNumpo = changeReplaceNumpo;
	}

	public Integer getChangeUpdateLostNumxj() {
		return changeUpdateLostNumxj;
	}

	public void setChangeUpdateLostNumxj(Integer changeUpdateLostNumxj) {
		this.changeUpdateLostNumxj = changeUpdateLostNumxj;
	}

	public Integer getChangeUpdateLostNum2xj() {
		return changeUpdateLostNum2xj;
	}

	public void setChangeUpdateLostNum2xj(Integer changeUpdateLostNum2xj) {
		this.changeUpdateLostNum2xj = changeUpdateLostNum2xj;
	}

	public Integer getChangeUpdateLostNum4xj() {
		return changeUpdateLostNum4xj;
	}

	public void setChangeUpdateLostNum4xj(Integer changeUpdateLostNum4xj) {
		this.changeUpdateLostNum4xj = changeUpdateLostNum4xj;
	}

	public Integer getChangeUpdateLostNum5xj() {
		return changeUpdateLostNum5xj;
	}

	public void setChangeUpdateLostNum5xj(Integer changeUpdateLostNum5xj) {
		this.changeUpdateLostNum5xj = changeUpdateLostNum5xj;
	}

	public Integer getChangeUpdateLostNumpo() {
		return changeUpdateLostNumpo;
	}

	public void setChangeUpdateLostNumpo(Integer changeUpdateLostNumpo) {
		this.changeUpdateLostNumpo = changeUpdateLostNumpo;
	}

	public Integer getChangeUpdateLostNum2po() {
		return changeUpdateLostNum2po;
	}

	public void setChangeUpdateLostNum2po(Integer changeUpdateLostNum2po) {
		this.changeUpdateLostNum2po = changeUpdateLostNum2po;
	}

	public Integer getChangeUpdateLostNum4po() {
		return changeUpdateLostNum4po;
	}

	public void setChangeUpdateLostNum4po(Integer changeUpdateLostNum4po) {
		this.changeUpdateLostNum4po = changeUpdateLostNum4po;
	}

	public Integer getChangeUpdateLostNum5po() {
		return changeUpdateLostNum5po;
	}

	public void setChangeUpdateLostNum5po(Integer changeUpdateLostNum5po) {
		this.changeUpdateLostNum5po = changeUpdateLostNum5po;
	}

	public Integer getChangeUpdateReplaceNumxj() {
		return changeUpdateReplaceNumxj;
	}

	public void setChangeUpdateReplaceNumxj(Integer changeUpdateReplaceNumxj) {
		this.changeUpdateReplaceNumxj = changeUpdateReplaceNumxj;
	}

	public Integer getChangeUpdateReplaceNum2xj() {
		return changeUpdateReplaceNum2xj;
	}

	public void setChangeUpdateReplaceNum2xj(Integer changeUpdateReplaceNum2xj) {
		this.changeUpdateReplaceNum2xj = changeUpdateReplaceNum2xj;
	}

	public Integer getChangeUpdateReplaceNum4xj() {
		return changeUpdateReplaceNum4xj;
	}

	public void setChangeUpdateReplaceNum4xj(Integer changeUpdateReplaceNum4xj) {
		this.changeUpdateReplaceNum4xj = changeUpdateReplaceNum4xj;
	}

	public Integer getChangeUpdateReplaceNum5xj() {
		return changeUpdateReplaceNum5xj;
	}

	public void setChangeUpdateReplaceNum5xj(Integer changeUpdateReplaceNum5xj) {
		this.changeUpdateReplaceNum5xj = changeUpdateReplaceNum5xj;
	}

	public Integer getChangeUpdateReplaceNumpo() {
		return changeUpdateReplaceNumpo;
	}

	public void setChangeUpdateReplaceNumpo(Integer changeUpdateReplaceNumpo) {
		this.changeUpdateReplaceNumpo = changeUpdateReplaceNumpo;
	}

	public Integer getChangeUpdateReplaceNum2po() {
		return changeUpdateReplaceNum2po;
	}

	public void setChangeUpdateReplaceNum2po(Integer changeUpdateReplaceNum2po) {
		this.changeUpdateReplaceNum2po = changeUpdateReplaceNum2po;
	}

	public Integer getChangeUpdateReplaceNum4po() {
		return changeUpdateReplaceNum4po;
	}

	public void setChangeUpdateReplaceNum4po(Integer changeUpdateReplaceNum4po) {
		this.changeUpdateReplaceNum4po = changeUpdateReplaceNum4po;
	}

	public Integer getChangeUpdateReplaceNum5po() {
		return changeUpdateReplaceNum5po;
	}

	public void setChangeUpdateReplaceNum5po(Integer changeUpdateReplaceNum5po) {
		this.changeUpdateReplaceNum5po = changeUpdateReplaceNum5po;
	}
	
}