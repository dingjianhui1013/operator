<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {

				top.$.jBox.closeTip();
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		var target, code, tag;
		if (!event) {
			event = window.event; //针对ie浏览器  
			target = event.srcElement;
			code = event.keyCode;
			if (code == 13) {
				tag = target.tagName;
				if (tag == "TEXTAREA") {
					return true;
				} else {
					return false;
				}
			}
		} else {
			target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
			code = event.keyCode;
			if (code == 13) {
				tag = target.tagName;
				if (tag == "INPUT") {
					return false;
				} else {
					return true;
				}
			}
		}
	});
</script>
<script type="text/javascript">

	function changeInputStatus(obj) {
		var nextInput = $(obj).next("input")[0];
		//console.log($(obj).prop("checked"));
		if ($(obj).prop("checked")) {
			$(nextInput).removeAttr("readonly");
			$(nextInput).val("");
		} else {
			//console.log("yyyy");
			$(nextInput).attr("readonly", "readonly");
			$(nextInput).val("0");
			fixMoney();
		}
	}
</script>
<script type="text/javascript">
	function payment() {
		var load = $("#loadValue").val();
		load = encodeURI(encodeURI(load));
		url = "${ctx}/work/workDealInfo/payment?load=" + load+"&appId=${workDealInfo.configApp.id}&_="+new Date().getTime();
		$
				.getJSON(
						url,
						function(data) {
							var html = "";
							//console.log(data);
							$
									.each(
											data,
											function(idx, ele) {
												html += "<tr id='trtr"+ele.financePayInfoId+"'>";
												html += "<td>"
														+ ele.companyName
														+ "</td>";
												html += "<td>"
														+ ele.paymentMoney
														+ "</td>";
												html += "<td>" + ele.officeName
														+ "</td>";
												html += "<td>" + ele.ceartName
														+ "</td>";
												html += "<td>" + ele.createDate
														+ "</td>";
												html += "<td>"
														+ ele.paymentMethod
														+ "</td>";
												html += "<td>" + ele.remarks
														+ "</td>";
												html += "<td>"+ ele.residueMoney
															+ "</td>";
												html += "<td><input type='checkbox' companyName=\""+ele.companyName+"\" officeName=\""+ele.officeName+"\" ceartName=\""+ele.ceartName+"\" createDate=\""+ele.createDate+"\" paymentMethod=\""+ele.paymentMethod+"\" remarks=\""+ele.remarks+"\" financePayInfoId=\""+ele.financePayInfoId+"\" residueMoney=\""+ele.residueMoney+"\"/></td>";
												html += "</tr>";
											});

							$("#payment").html(html);
							$("#payTable").removeAttr("style");

						});
	}
	function onSubmit(){
			if ($("#settle").val()!="删除"){
				top.$.jBox.tip("请确认支付金额!");
				return false;
			} else {
				$("#sumMoney").removeAttr("disabled");
				$("#collectMoney").removeAttr("disabled");
				$("#shouldMoney").removeAttr("disabled");
				$("#mc").removeAttr("disabled");
				$("#bc").removeAttr("disabled");
				$("#pc").removeAttr("disabled");
				$("#ac").removeAttr("disabled");
				$("#cc").removeAttr("disabled");
				$("#gc").removeAttr("disabled");
				return true;
			}
	}

	function forbid() {
		top.$.jBox
				.open(
						"iframe:${ctx}/work/workCertTrustApply/forbidForm?applyId="
								+ $("#applyId").val(),
						"审核拒绝",
						600,
						400,
						{
							buttons : {
								"确定" : "ok",
								"取消" : true
							},
							bottomText : "填写审核拒绝的原因",
							submit : function(v, h, f) {
								var suggest = h.find("iframe")[0].contentWindow.suggest.value;
								//nodes = selectedTree.getSelectedNodes();
								if (v == "ok") {
									window.location.href = "${ctx}/work/workCertTrustApply/audit?agree=0&applyId="
											+ $("#applyId").val()
											+ "&suggest="
											+ suggest;
									return true;
								}
							},
							loaded : function(h) {
								$(".jbox-content", top.document).css(
										"overflow-y", "hidden");
							}
						});
	}
</script>
<style type="text/css">
.table td{
vertical-align: inherit
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">

	</ul>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed"
					style="width: 80%; margin: 0px auto; float: center">
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
							<td>${product[workDealInfo.configProduct.productName] }</td>
							<th>应用标识：</th>
							<td><c:if
									test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if>
								<c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if></td>
						</tr>
						<tr>
							<th>业务类型：</th>
							<td>申请可信移动设备</td>
							<th>费用合计：</th>
							<td>${apply.money }</td>
						</tr>
					</tbody>
				</table>

			</div>
		</div>
	</div>
	<br>
	<form:form id="inputForm"
		action="${ctx}/work/workCertTrustApply/saveWorkPayInfo" method="POST"
		class="form-horizontal">
		<div class="row-fluid" id="view"
			style="display: none; width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>付款单位名称</th>
							<th>收费金额</th>
							<th>收费窗口</th>
							<th>收费人</th>
							<th>收费时间</th>
							<th>收费方式</th>
							<th>剩余金额</th>
							<th>收费备注</th>
						</tr>
					</thead>
					<tbody id="financeTd">
					</tbody>
				</table>

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">收费查询：</label>
			<div class="controls">
				<input type="text" id="loadValue"> <input
					class="input-medium Wdate" type="text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
					readonly="readonly" id="startTime" />至 <input
					class="input-medium Wdate" type="text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});" maxlength="20"
					readonly="readonly" id="endTime" /> <input onclick="payment()"
					class="btn btn-primary" type="button" id="ks" value="快速搜索" />
			</div>
		</div>


		<div class="row-fluid" id="payTable"
			style="display: none; width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>付款单位名称</th>
							<th>收费金额</th>
							<th>收费窗口</th>
							<th>收费人</th>
							<th>收费时间</th>
							<th>收费方式</th>
							<th>收费备注</th>
							<th>剩余金额</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="payment">

					</tbody>
				</table>
			</div>
		</div>
		<br>


		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<table class="table table-striped table-bordered table-condensed"
						style=" margin: 0px auto; float: center;vertical-align: inherit;">
						<tbody>
							<tr>
								<th colspan="4">本次收费明细</th>
							</tr>
							<tr>
								<td><c:if test="${chargeAgent.chargeMethodPos==true }">
										<input type="checkbox" value="1" name="methodMoney" id="mc"
											onclick="changeInputStatus(this)">现金<input
											type="text" name="money" id="money" readonly="readonly"
											value="0">
									</c:if>
									<c:if test="${chargeAgent.chargeMethodPos!=true }">
										<input type="checkbox" disabled="disabled">现金<input
											type="text" disabled="disabled" value="0">
										<input type="hidden" name="money" id="money"
											readonly="readonly" value="0">
									</c:if></td>
								<td><c:if test="${chargeAgent.chargeMethodPos==true }">
										<input type="checkbox" value="1" name="methodPos" id="pc"
											onclick="changeInputStatus(this)">POS收款<input
											type="text" name="posMoney" id="pos" readonly="readonly"
											value="0">
									</c:if>
									<c:if test="${chargeAgent.chargeMethodPos!=true }">
										<input type="checkbox" disabled="disabled">pos收款<input
											type="text" disabled="disabled" value="0">
										<input type="hidden" name="posMoney" id="pos"
											readonly="readonly" value="0">
									</c:if></td>
								<td><c:if test="${chargeAgent.chargeMethodBank==true }">
										<input type="checkbox" value="1" name="methodBank" id="bc"
											onclick="changeInputStatus(this)">银行转账<input
											type="text" name="bankMoney" id="bank" readonly="readonly"
											value="0">
									</c:if> <c:if test="${chargeAgent.chargeMethodBank!=true }">
										<input type="checkbox" disabled="disabled">银行转帐<input
											type="text" disabled="disabled" value="0">
										<input type="hidden" name="bankMoney" id="bank"
											readonly="readonly" value="0">
									</c:if></td>
								<td><c:if test="${chargeAgent.chargeMethodAlipay==true }">
										<input type="checkbox" value="1" name="methodAlipay" id="ac"
											onclick="changeInputStatus(this)">支付宝转账<input
											type="text" name="alipayMoney" id="alipay"
											readonly="readonly" value="0">
									</c:if> <c:if test="${chargeAgent.chargeMethodAlipay!=true }">
										<input type="checkbox" disabled="disabled">支付宝转账<input
											type="text" disabled="disabled" value="0">
										<input type="hidden" name="alipayMoney" id="alipay"
											readonly="readonly" value="0">
									</c:if></td>
							</tr>
							<tr>
								<td><c:if test="${chargeAgent.chargeMethodGov==true }">
										<input type="checkbox" value="0" name="methodGov"
											onclick="choose(this)" id="gc">政府统一采购</c:if>
									<c:if test="${chargeAgent.chargeMethodGov!=true }">
										<input type="checkbox" disabled="disabled">政府统一采购</c:if></td>
								<td><c:if test="${chargeAgent.chargeMethodContract==true }">
										<input type="checkbox" value="1" name="methodContract"
											onclick="choose(this)" id="cc">合同采购</c:if>
									<c:if test="${chargeAgent.chargeMethodContract!=true }">
										<input type="checkbox" disabled="disabled">合同采购</c:if></td>
								<td><input type="button" onclick="addPayInfoToList()"
									class="btn btn-primary" id="settle" value="确认" /> <input
									type="hidden" id="officeNameHid" value="${officeName }">
									<input type="hidden" id="userNameHid" value="${userName }">
									<input type="hidden" id="dateHid" value="${date }"> <input
									type="hidden" id="companyHid"
									value="${workDealInfo.workCompany.companyName }"></td>
								<td></td>
							</tr>
								<tr>
								<td>业务应收合计:</td>
								<td><input type="text" id="sumMoney" name="workTotalMoney" disabled="disabled" value="${apply.money }"></td>
								<td><!-- 本次应收金额 -->
									<input type="hidden" name="workReceivaMoney" id="shouldMoney" disabled="disabled" value="${apply.money }">
								</td>
									
							</tr>
							<tr>
							<td>本次实收金额：</td>
								<td>
								<input type="text" name="workPayedMoney" id="collectMoney" disabled="disabled"  value="0"></td>
									<td></td><td></td>
							</tr>
							<tr>
								<th colspan="4">发票信息</th>
							</tr>
							<tr>
								<td>是否开具发票：</td>
								<td><input type="radio" name="userReceipt" value="true" checked="checked" id="sff0">是
									<input type="radio" name="userReceipt" value="false" id="sff1">否</td>
								<td>发票金额：</td>
								<td><input type="text" name="receiptAmount"
									value="0">元</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<input type="hidden" id="paymentMoney1" value="0" />
		<input type="hidden" name="applyId" id="applyId" value="${apply.id }">
		<input type="hidden" name="agree" value="1">
		<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					onclick="return onSubmit()" value="通过" />&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="button"
					onclick="forbid()" value="拒绝" />
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>

</body>
</html>
