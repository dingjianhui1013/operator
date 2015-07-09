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
document.onkeydown = function(event) {
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
};
</script>
<script type="text/javascript">

	function changeInputStatus(obj) {
		var nextInput = $(obj).next("input")[0];
		if ($(obj).prop("checked")) {
			$(nextInput).removeAttr("readonly");
			$(nextInput).val("");
		} else {
			//console.log("yyyy");
			$(nextInput).attr("readonly", "readonly");
			$(nextInput).val("0");
		}
	}
</script>
<script type="text/javascript">
function payment(){
	var load = $("#loadValue").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	load = encodeURI(encodeURI(load));
	url="${ctx}/work/workDealInfo/payment?load="+load+"&startTime="+startTime+"&endTime="+endTime+"&appId=${workDealInfo.configApp.id}&_="+new Date().getTime();
	$.getJSON(url,function(data){
		var html = "";
		//console.log(data);
		$.each(data,function(idx,ele){
			html += "<tr id='trtr"+ele.financePayInfoId+"'>";
			html += "<td>"+ele.companyName+"</td>";
			html += "<td>"+ele.paymentMoney+"</td>";
			html += "<td>"+ele.officeName+"</td>";
			html += "<td>"+ele.ceartName+"</td>";
			html += "<td>"+ele.createDate+"</td>";
			html += "<td>"+ele.paymentMethod+"</td>";
			html += "<td>"+ele.remarks+"</td>";
			html += "<td>"+ele.residueMoney+"</td>";
			html += "<td><input type='checkbox' companyName=\""+ele.companyName+"\" officeName=\""+ele.officeName+"\" ceartName=\""+ele.ceartName+"\" createDate=\""+ele.createDate+"\" paymentMethod=\""+ele.paymentMethod+"\" remarks=\""+ele.remarks+"\" financePayInfoId=\""+ele.financePayInfoId+"\" residueMoney=\""+ele.residueMoney+"\"/></td>";
			html += "</tr>";
		});
		$("#payment").html(html);
		$("#payTable").removeAttr("style");
		$("#recovery").show();
		$("#recovery").val("隐藏");
	});
}
function onSubmit(){
	if ($("#settle").val()!="删除"){
		top.$.jBox.tip("请确认支付金额!");
		return false;
	} else {
		if ($("input[name='userReceipt']:checked").val()) {
			var url = "${ctx}/receipt/receiptInvoice/verifiInvoice?_="+new Date().getTime();
			$.getJSON(url,function(data) {
				if (!data.status) {
					top.$.jBox.tip("该网点没有发票库房无法开具发票!");
					return false;
				} else {
					$("#collectMoney").removeAttr("disabled");
					$("#shouldMoney").removeAttr("disabled");
					$("#mc").removeAttr("disabled");
					$("#pc").removeAttr("disabled");
					$("#workTotalMoney").val($("#sumMoney").html());
					$("#inputForm").submit();
				}
			});
		} else {
			$("#collectMoney").removeAttr("disabled");
			$("#shouldMoney").removeAttr("disabled");
			$("#mc").removeAttr("disabled");
			$("#pc").removeAttr("disabled");
			$("#workTotalMoney").val($("#sumMoney").html());
			$("#inputForm").submit();
		}
	}
	
}
function back(){
	window.location.href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}";
}

function delPay(obj){
	$("#f"+obj).hide();
	$("#f"+obj).html("");
}
function hideOrShow(){
	if ($("#recovery").val() == "隐藏") {
		$("#payTable").hide();
		$("#recovery").val("显示");
	} else {
		$("#payTable").show();
		$("#recovery").val("隐藏");
	}
}
</script>


</head>
<body>
	<ul class="nav nav-tabs">

	</ul>
	<form:form id="inputForm" action="${ctx}/work/workPayInfo/errorSavePayInfo" method="POST"
		class="form-horizontal">
		<tags:message content="${message}" />
	<div class="container-fluid" style="width: 80%; margin: 0px auto; float: center">
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed"
					>
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
							<td>${lable }</td>
						</tr>
						<tr>
							<th>业务类型：</th>
							<td>${dealInfoType}</td>
							<th>年限：</th>
							<td>${workDealInfo.year }</td>
						</tr>
					</tbody>
				</table>

			</div>
		</div>
	</div>
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
							<th>收费备注</th>
							<th>剩余金额</th>
						</tr>
					</thead>
					<tbody id="financeTd">
					</tbody>
				</table>

			</div>
		</div>
			<div class="row-fluid" id="errorPay"
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
						<th>收费备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${fpir }" var="list">
						<tr id="f+${list.sn}">
							<td>${list.sn }</td>
							<td>${list.financePaymentInfo.company }</td>
							<td>${list.financePaymentInfo.paymentMoney }</td>
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
							<td>
							<input type="button" class="btn btn-primary" onclick="removePayinfoTr(this)" value="删除">
							</td>
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
							<td align="center">${type0 }<c:if test="${type0==null }">0</c:if></td>

							<td align="center">${type1 }<c:if test="${type1==null }">0</c:if></td>
							<td align="center">${type2 }<c:if test="${type2==null }">0</c:if></td>
							<td align="center">${type3 }<c:if test="${type3==null }">0</c:if></td>
							<td align="center">${type4 }<c:if test="${type4==null }">0</c:if></td>
							<td align="center">${type5 }<c:if test="${type5==null }">0</c:if></td>
						</tr>
						<tr>
							<td></td>
							<td align="center"><input type="text" id="type0" onchange="onSumMoney()" name="openAccountMoney" style="width: 50%" value="${empty type0?0:type0 }"></td>
							<td align="center"><input type="text" id="type1" onchange="onSumMoney()" name="addCert" style="width: 50%" value="${empty type1?0:type1 }"></td>
							<td align="center"><input type="text" id="type2" onchange="onSumMoney()" name="updateCert" style="width: 50%" value="${empty type2?0:type2 }"></td>
							<td align="center"><input type="text" id="type3" onchange="onSumMoney()" name="errorReplaceCert" style="width: 50%" value="${empty type3?0:type3 }"></td>
							<td align="center"><input type="text" id="type4" onchange="onSumMoney()" name="lostReplaceCert" style="width: 50%" value="${empty type4?0:type4 }"></td>
							<td align="center"><input type="text" id="type5" onchange="onSumMoney()" name="infoChange" style="width: 50%" value="${empty type5?0:type5 }"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<input type="hidden" name="openAccountMoney" value="${type0 }">
		<input type="hidden" name="addCert" value="${type1 }">
		<input type="hidden" name="updateCert" value="${type2 }">
		<input type="hidden" name="errorReplaceCert" value="${type3 }">
		<input type="hidden" name="lostReplaceCert" value="${type4 }">
		<input type="hidden" name="infoChange" value="${type5 }">
		<input type="hidden" name="electron" value="${type6 }">
		
		<div class="control-group">
			<label class="control-label">收费查询：</label>
			<div class="controls">
				<input type="text" id="loadValue" >
				<input class="input-medium Wdate" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" 
								id="startTime" />至
				<input class="input-medium Wdate" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
								maxlength="20" readonly="readonly" 
								id="endTime" />
				<input onclick="payment()" class="btn btn-primary" type="button" 
					id="ks" value="快速搜索" <c:if test="${!chargeAgent.chargeMethodMoney }">disabled="disabled"</c:if> />
					<input id="recovery" style="display: none" class="btn btn-primary" onclick="hideOrShow()" type="button" value="隐藏">
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
								<td><c:if test="${chargeAgent.chargeMethodMoney==true }"><input type="checkbox" value="1" name="methodMoney" id="mc"
									onclick="changeInputStatus(this)" <c:if test="${workDealInfo.workPayInfo.methodMoney }">checked="checked"</c:if>>现金<input
									type="text" name="money" id="money"  <c:if test="${!workDealInfo.workPayInfo.methodMoney }">readonly="readonly"</c:if>
									value="<c:if test='${workDealInfo.workPayInfo.money==0||workDealInfo.workPayInfo.money==null }'>0</c:if><c:if test='${workDealInfo.workPayInfo.money!=0&&workDealInfo.workPayInfo.money!=null }'>${workDealInfo.workPayInfo.money }</c:if>"></c:if>
									<c:if test="${chargeAgent.chargeMethodMoney!=true }"><input type="checkbox" disabled="disabled">现金<input
									type="text" disabled="disabled" value="0"><input
									type="hidden" name="money" id="money" value="0"></c:if></td>
								<td><c:if test="${chargeAgent.chargeMethodPos==true }"><input type="checkbox" value="1" name="methodPos" id="pc"
									onclick="changeInputStatus(this)" <c:if test="${workDealInfo.workPayInfo.methodPos }">checked="checked"</c:if>>POS收款<input
									type="text" name="posMoney" id="pos"  <c:if test="${!workDealInfo.workPayInfo.methodPos }">readonly="readonly"</c:if>
									value="<c:if test='${workDealInfo.workPayInfo.posMoney==0||workDealInfo.workPayInfo.posMoney==null }'>0</c:if><c:if test='${workDealInfo.workPayInfo.posMoney!=0&&workDealInfo.workPayInfo.posMoney!=null }'>${workDealInfo.workPayInfo.posMoney}</c:if>"></c:if>
									<c:if test="${chargeAgent.chargeMethodPos!=true }"><input type="checkbox" disabled="disabled">POS收款<input
									type="text" disabled="disabled" value="0"><input
									type="hidden" name="posMoney" id="pos" value="0"></c:if></td>
								<td>
									<input type="button" onclick="addPayInfoToList()" id="settle" class="btn btn-primary" value="确认" />
									<input type="hidden" id="officeNameHid" value="${officeName }">
									<input type="hidden" id="userNameHid" value="${userName }">
									<input type="hidden" id="dateHid" value="${date }">
									<input type="hidden" id="companyHid" value="${workDealInfo.workCompany.companyName }">
									</td>
								<td></td>
								</tr>
							<tr>
								<td>业务应收合计:</td>
								<td><span id="sumMoney">${type0+type1+type2+type3+type4+type5}</span>
								<input type="hidden"  id="allTotalMoney" value="${type0+type1+type2+type3+type4+type5}" />
								</td>
								<td><input type="hidden" name="workTotalMoney" id="workTotalMoney">
								<td><!-- 本次应收金额 -->
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
								<td><input type="text" name="receiptAmount" value="0">元</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<input type="hidden" id="paymentMoney1" value="0"/>
		<input type="hidden" id="errorPayType" value="0"/>
		<input type="hidden" name="workDealInfoId" value="${workDealInfo.id }">
		<div class="form-actions" style="text-align: center;  border-top: none;">
			<shiro:hasPermission name="work:workDealInfo:edit">
				<input id="btnSubmit" class="btn btn-primary" type="button" onclick="onSubmit()"
					value="提  交" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="back()" />
		</div>
	</form:form>

</body>
</html>
