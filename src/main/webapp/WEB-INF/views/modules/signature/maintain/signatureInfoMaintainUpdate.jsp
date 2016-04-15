<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>印章业务管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		var agentId = $("#agentId").val();
		if(agentId == 0){
			top.$.jBox.tip("请绑定印章计费策略模板!");
		}
		
		if($("#types").val()=="1" || $("#types").val()=="2")
			{
				if ($("#year1").is(":checked")) {
					$("#type4").html($("#money1").val());
					$("#type1").val($("#payInfoMoney").val());
	
				}
				if ($("#year2").is(":checked")) {
					$("#type4").html($("#money2").val());
					$("#type1").val($("#payInfoMoney").val());
	
				}
				if ($("#year3").is(":checked")) {
					$("#type4").html($("#money3").val());
					$("#type1").val($("#payInfoMoney").val());
	
				}
				if ($("#year4").is(":checked")) {
					$("#type4").html($("#money4").val());
					$("#type1").val($("#payInfoMoney").val());
	
				}
				if ($("#year5").is(":checked")) {
					$("#type4").html($("#money5").val());
					$("#type1").val($("#payInfoMoney").val());
	
				}
			}
		//变更维护页面初始化
		if($("#type").val()=="1")
			{
				var money = $("#type2").val();
				$("#sumMoney").html(money);
				$("#workTotalMoney").val(money);
			}
		//变更修改初始化页面的 本次收费明细、业务应收合计、
		if($("#types").val()=="1")
		{
			var totalMoney = $("#moneyC").val();
			$("#sumMoney").html(totalMoney);
			$("#settle").val("删除");
			$("#settle").attr("onclick","deletePay()");
			$("#type2").attr("disabled","disabled");
			$("#methodPos").attr("disabled","disabled");
			$("#methodMoney").attr("disabled","disabled");
			if(totalMoney>0)
			{
				$("#workTotalMoney").val(totalMoney);
				
				$("#collectMoney").val(totalMoney);
				
				if($("#isReceipt").val()=="true")
				{
					$("#receiptAmount").val(totalMoney);
					$("#sff0").attr("checked","checked");
				}else
					{
						$("#receiptAmount").val(0);	
						$("#sff1").attr("checked","checked");
					}
			}
			
		}
		//续期修改进行初始化页面的 本次收费明细、业务应收合计、发票
		if($("#types").val()=="2")
			{
				var totalMoney = $("#payInfoMoney").val();
				$("#sumMoney").html(totalMoney);
				$("#settle").val("删除");
				$("#settle").attr("onclick","deletePay()");
				$("#type1").attr("disabled","disabled");
				$("#methodPos").attr("disabled","disabled");
				$("#methodMoney").attr("disabled","disabled");
				if(totalMoney>0)
					{
						$("#workTotalMoney").val(totalMoney);
						$("#collectMoney").val(totalMoney);
						if($("#isReceipt").val()=="true")
						{
							$("#receiptAmount").val(totalMoney);
							$("#sff0").attr("checked","checked");
						}else
							{
								$("#receiptAmount").val(0);	
								$("#sff1").attr("checked","checked");
							}
					}
			}
	});
	
</script>

</head>
<body style="overflow: scroll">
<script type="text/javascript">
function setMoney(){
	if ($("#year1").is(":checked")) {
		$("#type4").html($("#money1").val());
		$("#type1").val($("#money1").val());
		
		changeYear();
		onSumMoney();
	}
	if ($("#year2").is(":checked")) {
		$("#type4").html($("#money2").val());
		$("#type1").val($("#money2").val());
		
		changeYear();
		
		onSumMoney();
	}
	if ($("#year3").is(":checked")) {
		$("#type4").html($("#money3").val());
		$("#type1").val($("#money3").val());
		
		changeYear();
		
		onSumMoney();
	}
	if ($("#year4").is(":checked")) {
		$("#type4").html($("#money4").val());
		$("#type1").val($("#money4").val());
		
		changeYear();
		
		onSumMoney();
	}
	if ($("#year5").is(":checked")) {
		$("#type4").html($("#money5").val());
		$("#type1").val($("#money5").val());
		
		changeYear();
		
		onSumMoney();
	}
	
}


function changeYear(){
			$("#posMoney").val(0);
			$("#cashMoney").val(0);
			$("#collectMoney").val(0);
			$("#posMoney").attr("disabled","disabled");				
			$("#cashMoney").attr("disabled","disabled");
			$("#type1").removeAttr("disabled","disabled");
			$("#methodPos").removeAttr("disabled","disabled");
			$("#methodMoney").removeAttr("disabled","disabled");
			$("#methodPos").attr("checked",false);
			$("#methodMoney").attr("checked",false);
			$("#settle").val("确定");
			$("#settle").attr("onclick","surePay()");	
			
			$("#sff0").attr("checked",true);
}


function onSumMoney(){
	if($("#type0").val()==""){
		$("#type1").val(0);
	}
	if($("#type1").val()==""){
		$("#type1").val(0);
	}
	if($("#type2").val()==""){
		$("#type1").val(0);
	}
	var totalMoney = parseInt($("#type0").val())+
	parseInt($("#type1").val())+
	parseInt($("#type2").val());
	$("#sumMoney").html(totalMoney);
	$("#workTotalMoney").val(totalMoney);
	$("#receiptAmount").val(totalMoney);
}

function setReceiptMoneyNull(){
	if ($("#sff1").is(":checked")) {
		$("#receiptAmount").val(0);
	}
	if ($("#sff0").is(":checked")) {
		$("#receiptAmount").val($("#workTotalMoney").val());
	}
}

function changeInputStatus(obj) {
	var nextInput = $(obj).next("input")[0];
	if ($(obj).prop("checked")) {
		$(nextInput).removeAttr("disabled","disabled");
		$(nextInput).val("");
	} else {
	
		$(nextInput).attr("disabled", "disabled");
		$(nextInput).val("0");
	}
}
function surePay(){
	 var list= $('input:radio[name="year"]:checked').val();
    if(list==null){
   	 top.$.jBox.tip("请选择办理印章续期的年限！");
   	 return false;
    }
    
    
    //两个不能同时为空
    if(($("#cashMoney").val()==null||$("#cashMoney").val()=="")&&($("#posMoney").val()==null||$("#posMoney").val()=="")){
  	  top.$.jBox.tip("所选支付金额不能为空！");
   	 return false;
    }
    
    //如果为空或为0,将方式unchecked
    if($("#cashMoney").val()==null||$("#cashMoney").val()==""||$("#cashMoney").val()==0){
   	 $("#cashMoney").val(0);
   	 $("#methodMoney").removeAttr("checked","checked");
    }else{
   	 $("#methodMoney").attr("checked","checked");
    }
    
    if($("#posMoney").val()==null||$("#posMoney").val()==""||$("#posMoney").val()==0){
   	 $("#posMoney").val(0);
   	 $("#methodPos").removeAttr("checked","checked");
    }else{
   	 $("#methodPos").attr("checked","checked");
    }
   
    
    var totalMoney = parseInt($("#posMoney").val())+parseInt($("#cashMoney").val());
    var ytotal = $("#workTotalMoney").val();
    if (totalMoney>parseInt(ytotal)) {
		
   	  top.$.jBox.tip("您的支付金额多于本次应付金额，请确认支付金额!");
   	 return false;
	}else if(totalMoney<parseInt(ytotal)){
		top.$.jBox.tip("您的支付金额少于本次应付金额，请确认支付金额!");
		return false;
	}else{
		$("#methodMoney").attr("disabled","disabled");
		$("#cashMoney").attr("disabled","disabled");
		$("#methodPos").attr("disabled","disabled");
		$("#posMoney").attr("disabled","disabled");
		$("#settle").val("删除");
		$("#settle").attr("onclick","deletePay()");
		
		$("#type1").attr("disabled","disabled");
		$("#type2").attr("disabled","disabled");
		
		$("#collectMoney").val($("#workTotalMoney").val());
		
	}
}


function deletePay(){
	$("#methodPos").removeAttr("disabled","disabled");
	$("#posMoney").removeAttr("disabled","disabled");
	
	$("#methodMoney").removeAttr("disabled","disabled");
	$("#cashMoney").removeAttr("disabled","disabled");
	
	$("#collectMoney").val(0);
	
	
	if($("#type1").val()>0){
		$("#type1").removeAttr("disabled","disabled");	
	}
	if($("#type2").val()>0){
		$("#type2").removeAttr("disabled","disabled");	
	}
	
	$("#settle").val("确定");
	$("#settle").attr("onclick","surePay()");
}

function checkSave(){
	var agentId = $("#agentId").val();
	if(agentId == 0){
		top.$.jBox.tip("请绑定印章计费策略模板！");
	}
	 var list= $('input:radio[name="year"]:checked').val();
    if(list==null){
   	 top.$.jBox.tip("请选择办理印章的续期年限！");
   	 return false;
    }
	if($("#settle").val()=="确定"){
		 top.$.jBox.tip("请确定缴费信息！");
  	 	 return false;
	}
	
	
	$("input[name='methodMoney']").val($("#methodMoney").val());
	$("input[name='methodPos']").val($("#methodPos").val());
	$("input[name='cashMoney']").val($("#cashMoney").val());
	$("input[name='posMoney']").val($("#posMoney").val());
	
	$("#inputForm").submit();
	
	$("#btnSubmit").attr("disabled","disabled");
}

	
</script>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/signature/signatureInfo/">印章业务列表</a></li>
		<li class="active"><a
			href="${ctx}/signature/signatureInfo/modifyForm?id=${signatureInfo.id}">业务<c:if test="${not empty update}">续期</c:if><c:if test="${not empty change}">变更</c:if></a></li>
	</ul>
	<form:form id="inputForm" action="${ctx}/signature/signatureInfo/maintainSaveUpdate"
		method="POST" class="form-horizontal">
		<input type="hidden" name="types" id="types" value="${types}"/>
		<input type="hidden" name="type" id="type" value="${type}"/>
		<input type="hidden" name="payInfoMoney" id="payInfoMoney" value="${signaturePayInfo.totalMoney}"/>
		<input type="hidden" name="moneyC" id="moneyC" value="${moneyChange}"/>
		<input type="hidden" name="difference" id="difference" value="${difference}"/>
		<input type="hidden" name="isReceipt" id="isReceipt" value="${signaturePayInfo.isReceipt}"/>
		<tags:message content="${message}" />
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="6" style="font-size: 20px;"><span
								class="prompt" style="color: red; display: none;">*</span>基本信息</th>
						</tr>
						<tr>
							<th style="width:15%;"><span class="prompt" style="color: red; display: none;">*</span>应用名称：</th>
							<td style="width:45%" ><input type="text" name="configApp"  disabled="disabled"
								value="${signatureInfo.configApp.appName }" id="app" />
								<input type="hidden" name="signatureInfoId" value="${signatureInfo.id}">
								</td>
							<th style="width:10%;"><span class="prompt" style="color: red; display: none;">*</span>印章类型：</th>
				            <td style="width:30%">
				            	
				            	<c:if test="${not empty update || not empty change }">
					            	<input type="radio" disabled="disabled"  value="1" <c:if test="${signatureInfo.signatureType==1}">checked="checked"</c:if>>
					            	<span>财务章</span> 
					            	<input type="radio" disabled="disabled"  value="2" <c:if test="${signatureInfo.signatureType==2}">checked="checked"</c:if>>
					            	<span>合同章 </span>
					            	<input type="radio" disabled="disabled"  value="3" <c:if test="${signatureInfo.signatureType==3}">checked="checked"</c:if>>
					            	<span>个人章</span>
					            	<input type="radio" disabled="disabled"  value="4" <c:if test="${signatureInfo.signatureType==4}">checked="checked"</c:if>>
					            	<span>公章</span>
					            	<input type="hidden" name="signatureType" value="${signatureInfo.signatureType}" />
				            	</c:if>
				            </td>
						</tr>
						<tr>
						
							<th style="width: 150px;"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略模板：</th>
							<td style="width: 550px;">
								<input type="text" name="configApp"  disabled="disabled"
								value="${signatureInfo.signatureAgent.tempName }" />
								<input type="hidden" id="agentId" name="agentId" value="${signatureInfo.signatureAgent.id}">
							</td>
						
							

							<c:if test="${not empty update}">
							<th style="width: 150px;"><span class="prompt"
								style="color: red; display: none;">*</span>续期年限：</th>
							<td>
							<c:if test="${year1==true }">
								
								<input type="radio" onclick="setMoney()" name="year" value="1" id="year1"  <c:if test="${years!=null&&years==1}">checked="checked"</c:if>/>
								<span id="word1">1年</span> <input type="hidden" id="money1" value="${money1}">
							</c:if>
							<c:if test="${year2==true }">
								<input type="radio" onclick="setMoney()" name="year" value="2" id="year2" <c:if test="${years!=null&&years==2}">checked="checked"</c:if> />
								<span id="word2">2年 </span> <input type="hidden" id="money2" value="${money2}" >
							</c:if>
							<c:if test="${year3==true }">	
								<input type="radio" onclick="setMoney()" name="year" value="3" id="year3" <c:if test="${years!=null&&years==3}">checked="checked"</c:if> />
								<span id="word3">3年</span> <input type="hidden" id="money3" value="${money3}">
							</c:if>
							<c:if test="${year4==true }">	
								<input type="radio" onclick="setMoney()" name="year" value="4" id="year4" <c:if test="${years!=null&&years==4}">checked="checked"</c:if> />
								<span id="word4">4年</span> <input type="hidden" id="money4" value="${money4}">
							</c:if>
							<c:if test="${year5==true }">		
								<input type="radio" onclick="setMoney()" name="year" value="5" id="year5" <c:if test="${years!=null&&years==5}">checked="checked"</c:if> />
								<span id="word5">5年</span> <input type="hidden" id="money5" value="${money5}">
							</c:if>	
							</td>
							</c:if>
							
							
							<c:if test="${not empty change}">
							<th style="width: 150px;"><span class="prompt"
								style="color: red; display: none;">*</span>申请年限：</th>
							<td><input type="radio" id="delay" checked="checked"
								name="year" value="0" disabled="disabled"> <span>不延期</span>

							</td>	
								
							</c:if>

						</tr>
						
						<tr>
							
							<th style="width: 150px;"><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							
							
							<c:if test="${not empty update}">
							<td><input type="checkbox" checked="checked"
								disabled="disabled">印章续期 <input type="hidden"
								name="dealInfoType" value="2"></td>
							</c:if>
							
							<c:if test="${not empty change}">
							<td><input type="checkbox" checked="checked"
								disabled="disabled">印章变更 <input type="hidden"
								name="dealInfoType" value="3"></td>
							</c:if>	
							<th></th>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th style="width:15%">收费基准</th>
							<th style="width:45%">新增</th>
							<th style="width:10%">续期</th>
							<th style="width:30%">变更</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td></td>
							<td align="center" id="type3">0</td>
							<td align="center" id="type4">0</td>
							<c:if test="${empty moneyChange }">
								<td align="center" id="type5">0</td>
							</c:if>
							<c:if test="${not empty moneyChange }">
							<c:if test="${signaturePayInfo.totalMoney!=null}"><td align="center" id="type5">${signaturePayInfo.totalMoney}</td></c:if>
							<c:if test="${signaturePayInfo.totalMoney==null}"><td align="center" id="type5">${moneyChange}</td></c:if>
							</c:if>
						</tr>
						<tr>
							<td></td>
							
							<c:if test="${not empty update }">
							<td align="center"><input type="text" id="type0"   disabled="disabled" name="addMoney" style="width: 50%" value="0"></td>
							<td align="center"><input type="text" id="type1" onkeyup="value=value.replace(/[^\d]/g,'')"  onblur="onSumMoney()" name="updateMoney" style="width: 50%" value="0"></td>
							<td align="center"><input type="text" id="type2"   disabled="disabled" name="changeMoney" style="width: 50%" value="0"></td>
							</c:if>
							<c:if test="${not empty change }">
							<td align="center"><input type="text" id="type0"   disabled="disabled" name="addMoney" style="width: 50%" value="0"></td>
							<td align="center"><input type="text" id="type1"  disabled="disabled"   name="updateMoney" style="width: 50%" value="0"></td>
							<td align="center"><input type="text" id="type2"  onkeyup="value=value.replace(/[^\d]/g,'')" onblur="onSumMoney()"  name="changeMoney" style="width: 50%" value="${moneyChange}"></td>
							</c:if>
							
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed"
						style="margin: 0px auto; float: center; vertical-align: inherit;">
						<tbody>
							<tr>
								<th colspan="4">本次收费明细</th>
							</tr>
							
							<tr>
								<td style="width:30%">
									<input type="checkbox" value="0" id="methodMoney"
									<c:if test="${signaturePayInfo.methodMoney==true}">checked="checked"</c:if>
									<c:if test="${signatureInfo.signatureAgent.chargeMethodMoney!=true }">disabled="disabled"</c:if>
									 onclick="changeInputStatus(this)" >
									现金
									
									<input type="text"  id="cashMoney"  disabled="disabled" 
									<c:if test="${signaturePayInfo.methodMoney==null}">value="0" </c:if>
									<c:if test="${signaturePayInfo.methodMoney==true}">value="${signaturePayInfo.cashMoney}"</c:if>
									<c:if test="${signaturePayInfo.methodMoney==false}">value="0"</c:if>>
								</td>
								<td style="width:30%">
									<input type="checkbox" value="0" id="methodPos"
									<c:if test="${signatureInfo.signatureAgent.chargeMethodPos!=true }">disabled="disabled"</c:if>
									<c:if test="${signaturePayInfo.methodPos==true}">checked="checked"</c:if>
									onclick="changeInputStatus(this)" >
									pos
									<input type="text" id="posMoney"   disabled="disabled" 
									<c:if test="${signaturePayInfo.methodPos==null}">value="0" </c:if>
									<c:if test="${signaturePayInfo.methodPos==true}">value="${signaturePayInfo.posMoney}" </c:if>
									<c:if test="${signaturePayInfo.methodPos==false}">value="0"</c:if>>
								</td>
								<td style="width:10%"><input type="button" onclick="surePay()"
									class="btn btn-primary" id="settle" value="确定" /> 
									</td>
									<td>
										<input type="hidden"  name="methodPos"  />
										<input type="hidden"  name="methodMoney"  />
										<input type="hidden"  name="posMoney" />
										<input type="hidden"  name="cashMoney" />
									
									</td>
								</tr>
							
							<tr>
								<td>业务应收合计:</td>
								<td><span id="sumMoney"></span>
								</td>
								<td><input type="hidden" name="workTotalMoney" id="workTotalMoney">
								</td>
								<td>
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
								<td><input type="radio" name="userReceipt" onclick="setReceiptMoneyNull()" value="true" checked="checked" id="sff0">是
									<input type="radio" name="userReceipt" onclick="setReceiptMoneyNull()"  value="false" id="sff1">否</td>
								<td>发票金额：</td>
								<td><input type="text" name="receiptAmount" id = "receiptAmount" 
									<c:if test="${not empty moneyChange}">value="${moneyChange}"</c:if>
									<c:if test="${empty moneyChange}">value="0"</c:if>>元</td>
							</tr>
						</tbody>
					</table>
				
				
			</div>
		</div>
		
	
		<div class="row-fluid">
			<div class="span12">
				<table class="table">
					<tbody>
						<tr class="form-actions">
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2">&nbsp; 
								<input id="btnCancel" class="btn" type="button" value="返 回"
								onclick="history.go(-1)" /> 
								<input id="btnSubmit"
								class="btn  btn-primary" type="button" value="保存业务信息"
								onclick="checkSave()" /> 
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>

</html>
