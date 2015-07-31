<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function buttonFrom() {
		window.location.href = "${ctx}/work/workDealInfoAudit/makeDealInfo?id=${workDealInfo.id}";
	}
	function backMoney() {
		window.location.href = "${ctx}/work/workDealInfoAudit/list";
	}
	function refuse() {
		top.$.jBox
				.open(
						"iframe:${ctx}/work/workDealInfoAudit/jujueFrom?id=${workDealInfo.id}",
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
								var suggest = h.find("iframe")[0].contentWindow
										.$("#suggest")[0].value;
								//nodes = selectedTree.getSelectedNodes();
								if (v == "ok") {
									window.location.href = "${ctx}/work/workDealInfoAudit/jujue?id=${workDealInfo.id}&remarks="
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
</head>
<body>
	<div class="row-fluid">
		<div class="span12">
			<table class="table table-striped table-bordered table-condensed">
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
						<td><c:if
								test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if>
							<c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if></td>
					</tr>
					<tr>
						<th>业务类型：</th>
						<td>${dealInfoType}&nbsp;&nbsp;${dealInfoType1 }</td>
						<th>年限：</th>
						<td>${workDealInfo.year }</td>
					</tr>
				</tbody>
			</table>

		</div>
	</div>
	<div class="row-fluid" id="view"
		style="width: 80%; margin: 0px auto; float: center">
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
						<th>剩余金额</th>
						<th>收费备注</th>
					</tr>
				</thead>
				<tbody id="financeTd">
					<c:forEach items="${fpir }" var="list">
						<tr>
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
							<td>${list.financePaymentInfo.residueMoney }</td>
							<td>${list.financePaymentInfo.remarks }</td>
						</tr>
					</c:forEach>
					<c:forEach var="info" items="${infos }">
						<tr>
							<c:forEach var="i" items="${info }">
							<td>${i }</td>
							</c:forEach>
							<td>&nbsp;</td>
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
						<td align="center">${workPayInfo.openAccountMoney }<c:if test="${workPayInfo.openAccountMoney==null }">0</c:if></td>

						<td align="center">${workPayInfo.addCert }<c:if test="${workPayInfo.addCert==null }">0</c:if></td>
						<td align="center">${workPayInfo.updateCert }<c:if test="${workPayInfo.updateCert==null }">0</c:if></td>
						<td align="center">${workPayInfo.errorReplaceCert }<c:if test="${workPayInfo.errorReplaceCert==null }">0</c:if></td>
						<td align="center">${workPayInfo.lostReplaceCert }<c:if test="${workPayInfo.lostReplaceCert==null }">0</c:if></td>
						<td align="center">${workPayInfo.infoChange }<c:if test="${workPayInfo.infoChange==null }">0</c:if></td>
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
					style="margin: 0px auto; float: center; vertical-align: inherit;">
					<tbody>
						<tr>
							<th colspan="4">本次收费明细</th>
						</tr>
						<tr>
							<td><input type="checkbox" value="1" name="methodMoney"
								id="mc" disabled="disabled" onclick="changeInputStatus(this)"
								<c:if test="${workDealInfo.workPayInfo.methodMoney }">checked="checked"</c:if>>现金<input
								type="text" name="money" id="money" onchange="fixMoney()"
								value="${workDealInfo.workPayInfo.money }" readonly="readonly"
								value="0"></td>
							<td><input type="checkbox" value="1" name="methodPos"
								id="pc" disabled="disabled" onclick="changeInputStatus(this)"
								<c:if test="${workDealInfo.workPayInfo.methodPos }">checked="checked"</c:if>>POS收款<input
								type="text" name="posMoney" id="pos" onchange="fixMoney()"
								value="${workDealInfo.workPayInfo.posMoney }"
								readonly="readonly" value="0"></td>
							<td><input type="checkbox" value="1" name="methodBank"
								id="bc" disabled="disabled" onclick="changeInputStatus(this)"
								<c:if test="${workDealInfo.workPayInfo.methodBank }">checked="checked"</c:if>>银行转账<input
								type="text" name="bankMoney" id="bank" onchange="fixMoney()"
								value="${workDealInfo.workPayInfo.bankMoney }"
								readonly="readonly" value="0"></td>
							<td><input type="checkbox" value="1" name="methodAlipay"
								id="ac" disabled="disabled" onclick="changeInputStatus(this)"
								<c:if test="${workDealInfo.workPayInfo.methodAlipay }">checked="checked"</c:if>>支付宝转账<input
								type="text" name="alipayMoney" id="alipay"
								value="${workDealInfo.workPayInfo.alipayMoney }"
								onchange="fixMoney()" readonly="readonly" value="0"></td>
						</tr>
						<tr>
							<td><input type="checkbox" value="1" name="methodGov" methodval="0"
								onclick="choose(this)" id="gc" disabled="disabled"
								<c:if test="${workDealInfo.workPayInfo.methodGov }">checked="checked"</c:if>>政府统一采购</td>
							<td><input type="checkbox" value="1" name="methodContract" methodval="1"
								onclick="choose(this)" id="cc" disabled="disabled"
								<c:if test="${workDealInfo.workPayInfo.methodContract }">checked="checked"</c:if>>合同采购</td>
							<td>
									<input type="hidden" id="officeNameHid" value="${officeName }">
									<input type="hidden" id="userNameHid" value="${userName }">
									<input type="hidden" id="dateHid" value="${date }">
									<input type="hidden" id="companyHid" value="${workDealInfo.workCompany.companyName }">
							</td>
							<td></td>
						</tr>
						<tr>
							<td>业务应收合计:</td>
							<td><input type="text" id="sumMoney" name="workTotalMoney"
								disabled="disabled"
								value="${workDealInfo.workPayInfo.workTotalMoney }"></td>
							<td><input type="hidden" name="workPayedMoney" 
								id="collectMoney" disabled="disabled" value="${workDealInfo.workPayInfo.workReceivaMoney }"></td>

						</tr>
						<tr>
							<td>本次实收金额：</td>
							<td><input type="text" name="workReceivaMoney"
								id="shouldMoney" disabled="disabled"
								value="${workDealInfo.workPayInfo.workPayedMoney }"></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<th colspan="4">发票信息</th>
						</tr>
						<tr>
							<td>是否开具发票：</td>
							<td><input type="radio" name="userReceipt" value="true"
								<c:if test="${workDealInfo.workPayInfo.userReceipt }">checked="checked"</c:if>
								disabled="disabled">是 <input type="radio"
								name="userReceipt" value="false"
								<c:if test="${!workDealInfo.workPayInfo.userReceipt }">checked="checked"</c:if>
								disabled="disabled">否</td>
							<td>发票金额：</td>
							<td><input type="text" name="receiptAmount"
								disabled="disabled"
								value="${workPayInfo.receiptAmount }">元</td>
						</tr>
					</tbody>
				</table>
				
			</div>
			
				<div class="control-group span12">
				<div class="span12">
					<table class="table">
						<tbody>
							<tr>
								<td style="text-align: center; width: 100%; border-top: none;"
									colspan="2">&nbsp; <input class="btn btn-primary"
									type="button" value="通 过" onclick="javascript:buttonFrom();" />&nbsp;
									<input id="btnCancel" class="btn" type="button" value="拒  绝"
									onclick="javascript:refuse();" />

								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
