<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>退费</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/cert/pta_topca.js"></script>
<script type="text/javascript" src="${ctxStatic}/cert/xenroll.js"></script>
<script type="text/javascript">
	function buttonFrom(){
		if (${bgType}){
			var type = 0;//政府
			if($("#contract").prop("checked")){
				type = 1;
			}
			window.location.href="${ctx}/work/workDealInfoAudit/backMoney?id=${workDealInfo.id}&method="+type+"&receiptAmount="+$("#tfpVal").val();
		} else {
			var keySn = "";
			var revoke = 1;//不吊销
			if($("#revoke").prop("checked")){
				revoke = 0;//吊销
			}
			
			if($("#tKey").prop("checked")){
				var same = $("#isSame").val();
				if(same=="buyiyang"){
					alert("当前KEY与办理业务使用的KEY不一致！无法进行退费操作！");
				}else{
					keySn = $("#keySn").val();
					window.location.href="${ctx}/work/workDealInfoAudit/backMoney1?id=${workDealInfo.id}&revoke="+revoke+"&keySn="+keySn+"&receiptAmount="+$("#tfpVal").val();
				}
			}else{
				keySn = $("#keySn").val();
				window.location.href="${ctx}/work/workDealInfoAudit/backMoney1?id=${workDealInfo.id}&revoke="+revoke+"&keySn="+keySn+"&receiptAmount="+$("#tfpVal").val();
			}
		}
	}
	
	function selectKeyNum() {
		var dealKeySn = "${workDealInfo.keySn}";
		try {
			var keys = ukeyadmin.refresh(); //检测KEY
			if (keys == 0) {
				alert("没有检测到UKEY");
			} else {
				ukeyadmin.openKey(0);
				var keySn = ukeyadmin.getkeyserialnumber(0);
				sn = keySn;
				$("#keySn").attr("value", keySn);
				$("#keySn").css("color", "red");
				if (dealKeySn!=keySn) {
					alert("当前KEY与办理业务使用的KEY不一致！");
					$("#isSame").val("buyiyang");
				}else{
					$("#isSame").val("yiyang");
				}
			}
		} catch (e) {
			alert("没有检测到UKEY");
		}
	}
	function changeInputStatus(obj) {
		var nextInput = $(obj).parent().next().find("input")[0];
		var nextButton = $(nextInput).parent().next().find("input")[0];
		if ($(obj).prop("checked")) {
			$(nextInput).removeAttr("readonly");
			if (nextButton!=""){
				$(nextButton).show();
			}
		} else {
			//console.log("yyyy");
			$(nextInput).attr("readonly", "readonly");
			$(nextInput).val("");
			if (nextButton!=""){
				$(nextButton).hide();
			}
		}
	}
</script>
</head>
<body>
 	<div style="display: none">	
		<object id="ukeyadmin" codeBase="itrusukeyadmin.cab#version=3,1,13,705" classid="clsid:05395F06-244C-4599-A359-5F442B857C28"></object>
	</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed" style="width:80%;margin:0px auto;float:center">
					<tbody>
						<tr>
							<th colspan="4">收费信息</th>
						</tr>
						<tr>
							<th>单位名称:</th>
							<td>${workDealInfo.workCompany.companyName }</td>
							<th>经办人:</th>
							<td>${workDealInfo.workUser.contactName }</td>
						</tr>
						<tr>
							<th>证书类型:</th>
							<td>${product }</td>
							<th>应用标识：</th>
							<td><c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if>
							<c:if test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if></td>
						</tr>
						<tr>
							<th>业务类型：</th>
							<td>${workDealInfo.dealInfoType}</td>
							<th>年限：</th>
							<td>${workDealInfo.year }</td>
						</tr>							
					</tbody>
				</table>

			</div>
		</div>
		<div class="row-fluid" id="view"
			style=" width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>收款编号</th>
							<th>付款单位名称</th>
							<th>收费金额</th>
							<th>收费窗口</th>
							<th>收费人</th>
							<th>收费时间</th>
							<th>收费方式</th>
							<th>收费备注</th>
						</tr>
					</thead>
					<tbody id="financeTd">
							<c:forEach items="${fpir }" var="list">
						<tr id="f+${list.sn}">
							<td>${list.sn }</td>
							<td>${list.financePaymentInfo.company }</td>
							<td>${list.money }</td>
							<td>${list.financePaymentInfo.createBy.office.name }</td>
							<td>${list.financePaymentInfo.createBy.name }</td>
							<td>
							<fmt:formatDate value="${list.financePaymentInfo.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td><c:if test="${list.financePaymentInfo.paymentMethod==1 }">现金</c:if>
							<c:if test="${list.financePaymentInfo.paymentMethod==2 }">POS收款</c:if>
							<c:if test="${list.financePaymentInfo.paymentMethod==3 }">银行转账</c:if>
							<c:if test="${list.financePaymentInfo.paymentMethod==4 }">支付宝付款</c:if></td>
							<td>${list.financePaymentInfo.remarks }</td>
						</tr>
					</c:forEach>
					<c:forEach var="info" items="${infos }">
						<tr>
							<c:forEach var="i" items="${info }">
							<td>${i }</td>
							</c:forEach>
						</tr>
					</c:forEach>
						
					</tbody>
				</table>

			</div>
		</div>
		<div class="row-fluid"
			style="width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>收费基准</th>
							<th>开户</th>
							<th>新增</th>
							<th>更新</th>
							<th>遗失补办</th>
							<th>损坏更换</th>
							<th>信息变更</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td></td>
							<td align="center">${workDealInfo.workPayInfo.openAccountMoney==null?0:workDealInfo.workPayInfo.openAccountMoney }</td>

							<td align="center">${workDealInfo.workPayInfo.addCert==null?0:workDealInfo.workPayInfo.addCert }</td>
							<td align="center">${workDealInfo.workPayInfo.updateCert==null?0:workDealInfo.workPayInfo.updateCert }</td>
							<td align="center">${workDealInfo.workPayInfo.errorReplaceCert==null?0:workDealInfo.workPayInfo.errorReplaceCert }</td>
							<td align="center">${workDealInfo.workPayInfo.lostReplaceCert==null?0:workDealInfo.workPayInfo.lostReplaceCert }</td>
							<td align="center">${workDealInfo.workPayInfo.infoChange==null?0:workDealInfo.workPayInfo.infoChange }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		

		<div class="row-fluid" id="payTable"
			style="display: none; width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>

							<th>收款编号</th>
							<th>付款单位名称</th>
							<th>收费金额</th>
							<th>收费窗口</th>
							<th>收费人</th>
							<th>收费时间</th>
							<th>收费方式</th>
							<th>收费备注</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="payment">
			
					</tbody>
				</table>
			</div>
		</div>
				<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<table class="table table-striped table-bordered table-condensed"
						style=" margin: 0px auto; float: center;vertical-align: inherit;">
						<tbody>
							<tr>
								<th colspan="4">明细</th>
							</tr>
							<tr>
								<td><input type="checkbox" value="1" name="methodMoney" id="mc" disabled="disabled"
									onclick="changeInputStatus(this)"  <c:if test="${workDealInfo.workPayInfo.methodMoney }">checked="checked"</c:if>>现金<input
									type="text" name="money" id="money" onchange="fixMoney()"
									readonly="readonly"  value="${workDealInfo.workPayInfo.money }"></td>
								<td><input type="checkbox" value="1" name="methodPos" id="pc" disabled="disabled"
									onclick="changeInputStatus(this)"  <c:if test="${workDealInfo.workPayInfo.methodPos }">checked="checked"</c:if>>POS收款
									<input
									type="text" name="posMoney" id="pos" onchange="fixMoney()"
									readonly="readonly"  value="${workDealInfo.workPayInfo.posMoney }">
									
									</td>
								<td><!-- <input type="checkbox" value="1" name="methodBank" id="bc" disabled="disabled"
									onclick="changeInputStatus(this)"  <c:if test="${workDealInfo.workPayInfo.methodBank }">checked="checked"</c:if>>银行转账<input
									type="text" name="bankMoney" id="bank" onchange="fixMoney()"
									readonly="readonly"  value="${workDealInfo.workPayInfo.bankMoney }"> --></td>
								<td><!-- <input type="checkbox" value="1" name="methodAlipay" id="ac" disabled="disabled"
									onclick="changeInputStatus(this)"  <c:if test="${workDealInfo.workPayInfo.methodAlipay }">checked="checked"</c:if>>支付宝转账<input
									type="text" name="alipayMoney" id="alipay"
									onchange="fixMoney()" readonly="readonly" 
									value="${workDealInfo.workPayInfo.alipayMoney }"> --></td>
								</tr>
							<tr>
								<td><input type="checkbox" value="1" name="methodGov" onclick="choose(this)" id="gc" disabled="disabled"
								 <c:if test="${workDealInfo.workPayInfo.methodGov }">checked="checked"</c:if>>政府统一采购</td>
								<td><input type="checkbox" value="1" name="methodContract" onclick="choose(this)" id="cc" disabled="disabled"
								 <c:if test="${workDealInfo.workPayInfo.methodContract }">checked="checked"</c:if>>合同采购</td>
									<td>
									<input type="hidden" id="officeNameHid" value="${officeName }">
									<input type="hidden" id="userNameHid" value="${userName }">
									<input type="hidden" id="dateHid" value="${date }">
									<input type="hidden" id="companyHid" value="${workDealInfo.workCompany.companyName }">
									</td><td></td>
							</tr>
							<tr>
								<td>业务应收合计:</td>
								<td><input type="text" id="sumMoney" name="workTotalMoney" disabled="disabled" value="${type0+type1+type2+type3+type4+type5+type6 }"></td>
								<td>已收发票</td>
								<td>${workDealInfo.workPayInfo.receiptAmount }</td>
							</tr>
							<tr>
							<c:if test="${bgType }">
								<td>变更缴费类型为：</td>
								<td>
									<input type="radio" id="gov" name="payType" checked="checked">政府统一采购
									<input type="radio" id="contract" name="payType">合同采购
								</td>
								<td><input type="checkbox" id="thfapiao" onclick="changeInputStatus(this)">退还发票</td>
								<td>发票金额：<input type="text" id="tfpVal" readonly="readonly"></td>
							</c:if>
							<c:if test="${!bgType }">
								<td><input type="checkbox" id="thfapiao"
									onclick="changeInputStatus(this)">退还发票</td>
								<td>发票金额：<input type="text" id="tfpVal" readonly="readonly"></td>
								<td></td><td></td>
							</c:if>
						</tr>
						 <c:if test="${revoke }">
							<tr>
								<td><input type="checkbox" id="revoke" <c:if test="${revoke }">checked="checked" disabled="disabled"</c:if> <c:if test="${!revoke }">disabled</c:if>>吊销</td>
								<td><input type="checkbox" id="tKey" <c:if test="${!tKey }">disabled</c:if> onclick="changeInputStatus(this)">退还key</td>
								<td>key序列号：<input type="text" id="keySn" name="keySn" readonly="readonly"></td>
								<td><input type="button" class="btn btn-primary" value="检测key" id="keyButton" style="display: none"
									onclick="javascript:selectKeyNum()" />
									<input type="hidden" id="isSame"/>
									</td>
							</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="form-actions"  style="text-align: center; width: 100%; border-top: none;">
				<input class="btn btn-primary" type="button" value="退费" 
			    onclick="javascript:buttonFrom();"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="取 消"
				onclick="history.go(-1)" />
		</div>
</body>
</html>
