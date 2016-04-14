<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		
		
		var agentId = $("#agentId").val();
		if(agentId == 0){
			top.$.jBox.tip("请绑定印章计费策略模板！");
			window.location.href="${ctx}/signature/signatureChargeAgent/";
		}
		
		if($("#workDealInfoId").val()!=""){
			showYear();
		}
		
		var status = $("#settle").val();
		if(status=="删除"){
			$("#methodMoney").attr("disabled","disabled");
			$("#methodPos").attr("disabled","disabled");
		}
		
	});
	 function init()
	{
		$("#type3").html(0);
		$("#type0").val(0);
		$("#sumMoney").html(0);
		$("#workTotalMoney").val(0);
		$("#receiptAmount").val(0);
		$("#sff0").attr("checked",true);
		$("#posMoney").val(0);
		$("#cashMoney").val(0);
		$("#collectMoney").val(0);
		$("#type0").removeAttr("disabled","disabled");
		$("#methodPos").removeAttr("disabled","disabled");
		$("#methodMoney").removeAttr("disabled","disabled");
		$("#methodPos").attr("checked",false);
		$("#methodMoney").attr("checked",false);
		$("#settle").val("确定");
		$("#year1").attr("checked",false);
		$("#year2").attr("checked",false);
		$("#year3").attr("checked",false);
		$("#year4").attr("checked",false);
		$("#year5").attr("checked",false);
		
	} 

	
	
	function showYear(){
		var agentId = $("#agentId").val();
		var url = "${ctx}/signature/signatureInfo/showYearNew?agentId="+agentId+"&workType=0&_="+new Date().getTime();
		
		$.getJSON(url, function(data) {
			 init(); 
			if (data.year1) {
				$("#year1").show();
				$("#word1").show();
				$("#money1").val(data.money1);
			} else {
				$("#year1").hide();
				$("#word1").hide();
			}
			if (data.year2) {
				$("#year2").show();
				$("#word2").show();
				$("#money2").val(data.money2);
			} else {
				$("#year2").hide();
				$("#word2").hide();
			}
			if (data.year3) {
				$("#year3").show();
				$("#word3").show();
				$("#money3").val(data.money3);
			} else {
				$("#year3").hide();
				$("#word3").hide();
			}
			if (data.year4) {
				$("#year4").show();
				$("#word4").show();
				$("#money4").val(data.money4);
			} else {
				$("#year4").hide();
				$("#word4").hide();
			}
			if (data.year5) {
				$("#year5").show();
				$("#word5").show();
				$("#money5").val(data.money5);
			} else {
				$("#year5").hide();
				$("#word5").hide();
			}
			
			if(data.methodPos){
				$("#methodPos").removeAttr("disabled");
			}else{
				$("#methodPos").attr("disabled","disabled");	
			}
			if(data.methodMoney){
				$("#methodMoney").removeAttr("disabled");
			}else{
				$("#methodMoney").attr("disabled","disabled");
			}
			
			var totalMoney = parseInt($("#type0"));
			$("#sumMoney").html(totalMoney);
			
			
			
		});
		
	}
	
	function setMoney(){
		if ($("#year1").is(":checked")) {
			$("#money1").val();
			$("#type3").html($("#money1").val());
			$("#type4").html(0);
			$("#type5").html(0);
			$("#type0").val($("#money1").val());
			$("#type1").val(0);
			$("#type2").val(0);
			changeYear();
			onSumMoney();
		}
		if ($("#year2").is(":checked")) {
			$("#money2").val();
			$("#type3").html($("#money2").val());
			$("#type4").html(0);
			$("#type5").html(0);
			$("#type0").val($("#money2").val());
			$("#type1").val(0);
			$("#type2").val(0);
			changeYear();
			onSumMoney();
		}
		if ($("#year3").is(":checked")) {
			$("#money3").val();
			$("#type3").html($("#money3").val());
			$("#type4").html(0);
			$("#type5").html(0);
			$("#type0").val($("#money3").val());
			$("#type1").val(0);
			$("#type2").val(0);
			changeYear();
			onSumMoney();
		}
		if ($("#year4").is(":checked")) {
			$("#money4").val();
			$("#type3").html($("#money4").val());
			$("#type4").html(0);
			$("#type5").html(0);
			$("#type0").val($("#money4").val());
			$("#type1").val(0);
			$("#type2").val(0);
			changeYear();
			onSumMoney();
		}
		if ($("#year5").is(":checked")) {
			$("#money1").val();
			$("#type3").html($("#money5").val());
			$("#type4").html(0);
			$("#type5").html(0);
			$("#type0").val($("#money5").val());
			$("#type1").val(0);
			$("#type2").val(0);
			changeYear();
			onSumMoney();
		}
		
	}
	
	function changeYear(){
			$("#methodPos").val("0");
			$("#methodMoney").val("0");
			$("#posMoney").val(0);
			$("#cashMoney").val(0);
			$("#posMoney").attr("disabled","disabled");
			$("#cashMoney").attr("disabled","disabled");
			$("#collectMoney").val(0);
			$("#methodPos").removeAttr("disabled","disabled");
			$("#methodMoney").removeAttr("disabled","disabled");
			$("#methodPos").attr("checked",false);
			$("#methodMoney").attr("checked",false);
			$("#settle").val("确定");
			$("#settle").attr("onclick","surePay()");
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
		$("#receiptAmount").val($("#workTotalMoney").val());
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
			$(obj).val("1");
			$(nextInput).removeAttr("disabled");
			$(nextInput).val("");
		} else {
			
			$(obj).val("0");
			$(nextInput).attr("disabled", "disabled");
			$(nextInput).val("0");
		}
	}
	
	function surePay(){
		
		 var list= $('input:radio[name="year"]:checked').val();
         if(list==null){
        	 top.$.jBox.tip("请选择办理印章的年限！");
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
         $("#collectMoney").val(totalMoney);
         if (totalMoney>parseInt(ytotal)) {
			
        	  top.$.jBox.tip("您的支付金额多于本次应付金额，请确认支付金额!");
        	 return false;
		}else if(totalMoney<parseInt(ytotal)){
			top.$.jBox.tip("您的支付金额少于本次应付金额，请确认支付金额!");
			return false;
		}else{
			$("#type0").attr("disabled","disabled");
			$("#methodMoney").attr("disabled","disabled");
			$("#cashMoney").attr("disabled","disabled");
			$("#methodPos").attr("disabled","disabled");
			$("#posMoney").attr("disabled","disabled");
			$("#settle").val("删除");
			$("#settle").attr("onclick","deletePay()");
		}
	}
	
	
	function deletePay(){
			$("#methodPos").removeAttr("disabled","disabled");
			$("#posMoney").removeAttr("disabled","disabled");
			
			$("#methodMoney").removeAttr("disabled","disabled");
			$("#cashMoney").removeAttr("disabled","disabled");
		
			$("#type0").removeAttr("disabled","disabled");
			$("#collectMoney").val(0);
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
        	 top.$.jBox.tip("请选择办理印章的年限！");
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
	}
	
	
	
	
	
</script>

</head>
<body style="overflow: scroll">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/signature/signatureInfo/">印章业务列表</a></li>
		<li class="active">
		<c:if test="${not empty workDealInfo.id }"><a href="${ctx}/signature/signatureInfo/form?workDealInfoId=${workDealInfo.id}">印章新增</a></c:if>
		<c:if test="${not empty signatureInfo.id }"><a href="${ctx}/signature/signatureInfo/form?signatureInfoId=${signatureInfo.id}">印章新增修改</a></c:if>
		</li> 
	</ul>
	<form:form id="inputForm" action="${ctx}/signature/signatureInfo/save"
		method="POST" class="form-horizontal">
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
							<th style="width:15%"><span class="prompt" style="color: red; display: none;">*</span>应用名称：</th>
							<td style="width:45%"><input type="text" name="configApp"  disabled="disabled"
								value="${configApp.appName }" id="app" />
								<input type="hidden" name="dealInfoId" id="workDealInfoId" value="${workDealInfo.id}">
								<input type="hidden" name="signatureInfoId" id="signatureInfoId" value="${signatureInfo.id}">
							</td>
							<th style="width:10%"><span class="prompt" style="color: red; display: none;">*</span>印章类型：</th>
				            <td style="width:30%">
				            	<input type="radio" name="signatureType" value="1"  <c:if test="${signatureInfo.signatureType==1 }">checked="checked"</c:if> <c:if test="${empty signatureInfo.signatureType }">checked="checked"</c:if> />
				            	<span>财务章</span> 
				            	<input type="radio" name="signatureType" value="2"  <c:if test="${signatureInfo.signatureType==2 }">checked="checked"</c:if> />
				            	<span>合同章 </span>
				            	<input type="radio" name="signatureType" value="3" <c:if test="${signatureInfo.signatureType==3 }">checked="checked"</c:if>  />
				            	<span>个人章</span>
				            	<input type="radio" name="signatureType" value="4" <c:if test="${signatureInfo.signatureType==4 }">checked="checked"</c:if> />
				            	<span>公章</span>
				            </td>
						</tr>
						<tr>

							<th style="width: 150px;"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略模板：</th>
							<td style="width: 550px;">
								<select id="agentId" onchange="showYear()"
									name="agentId">
									<c:if test="${signatureAgents.size()<1}">
										<option value="0">请选择</option>
									</c:if>
										<c:forEach items="${signatureAgents }" var="agent">
											<option value="${agent.id }"   <c:if test="${signatureInfo.signatureAgent.id==agent.id }">selected="selected"</c:if>>${agent.tempName }
										</c:forEach>
								</select>
							</td>

							<th style="width: 150px;"><span class="prompt"
								style="color: red; display: none;">*</span>印章年限：</th>
							<td>
							
								<c:if test="${not empty signatureInfo.id&&year1==true }">
								<input type="radio" onclick="setMoney()" name="year" value="1" id="year1" <c:if test="${signatureInfo.year==1}">checked="checked"</c:if> />
								<span id="word1">1年</span> <input type="hidden" id="money1"  value="${money1}">
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year1!=true }">
								<input style="display: none" type="radio" onclick="setMoney()" name="year" value="1" id="year1" />
								<span style="display: none" id="word1">1年</span> <input type="hidden" id="money1" >
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year2==true }">
								<input type="radio" onclick="setMoney()" name="year" value="2" id="year2" <c:if test="${signatureInfo.year==2}">checked="checked"</c:if> />
								<span id="word2">2年 </span> <input type="hidden" id="money2" value="${money2}">
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year2!=true }">
								<input style="display: none" type="radio" onclick="setMoney()" name="year" value="2" id="year2" />
								<span style="display: none" id="word2">2年</span> <input type="hidden" id="money2" >
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year3==true }">
								<input type="radio" onclick="setMoney()" name="year" value="3" id="year3" <c:if test="${signatureInfo.year==3}">checked="checked"</c:if> />
								<span id="word3">3年</span> <input type="hidden" id="money3" value="${money3}">
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year3!=true }">
								<input style="display: none" type="radio" onclick="setMoney()" name="year" value="3" id="year3" />
								<span style="display: none" id="word3">3年</span> <input type="hidden" id="money3" >
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year4==true }">
								<input type="radio" onclick="setMoney()" name="year" value="4" id="year4" <c:if test="${signatureInfo.year==4}">checked="checked"</c:if> />
								<span id="word4">4年</span> <input type="hidden" id="money4"  value="${money4}">
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year4!=true }">
								<input style="display: none" type="radio" onclick="setMoney()" name="year" value="4" id="year4" />
								<span style="display: none" id="word4">4年</span> <input type="hidden" id="money4" >
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year5==true }">
								<input type="radio" onclick="setMoney()" name="year" value="5" id="year5" <c:if test="${signatureInfo.year==5}">checked="checked"</c:if> />
								<span id="word5">5年</span> <input type="hidden" id="money5" value="${money5}">
								</c:if>
								
								<c:if test="${not empty signatureInfo.id&&year5!=true }">
								<input style="display: none" type="radio" onclick="setMoney()" name="year" value="5" id="year5" />
								<span style="display: none" id="word5">5年</span> <input type="hidden" id="money5" >
								</c:if>
							
								
							<c:if test="${not empty workDealInfo.id }">
								
								<input type="radio" onclick="setMoney()" name="year" value="1" id="year1"  />
								<span id="word1">1年</span> <input type="hidden" id="money1" >
								
								
								<input type="radio" onclick="setMoney()" name="year" value="2" id="year2"  />
								<span id="word2">2年 </span> <input type="hidden" id="money2" >
								
								
								<input type="radio" onclick="setMoney()" name="year" value="3" id="year3"  />
								<span id="word3">3年</span> <input type="hidden" id="money3" >
								
								
								<input type="radio" onclick="setMoney()" name="year" value="4" id="year4"  />
								<span id="word4">4年</span> <input type="hidden" id="money4" >
								
								
								<input type="radio" onclick="setMoney()" name="year" value="5" id="year5"  />
								<span id="word5">5年</span> <input type="hidden" id="money5" >
								
							</c:if>	
								
								
								
							</td>

						</tr>
						
						<tr>
							
							<th style="width: 150px;"><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							<td><input type="checkbox" checked="checked"
								disabled="disabled">印章授权 </td>
								
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
							<td align="center" id="type3"><c:if test="${not empty detail}">${detail.money }</c:if><c:if test="${empty detail}">0</c:if></td>
							<td align="center" id="type4">0</td>
							<td align="center" id="type5">0</td>
							
						</tr>
						<tr>
							<td></td>
							<td align="center"><input type="text" id="type0" onkeyup="value=value.replace(/[^\d]/g,'')"  onblur="onSumMoney()" name="addMoney" style="width: 50%" 
							<c:if test="${not empty payInfo}">value="${payInfo.totalMoney }" disabled="disabled"</c:if>
							<c:if test="${empty payInfo}">value="0"</c:if>>
							</td>
							
							<td align="center"><input type="text" id="type1"  name="updateMoney" style="width: 50%" value="0" disabled="disabled"></td>
							<td align="center"><input type="text" id="type2"  name="changeMoney" style="width: 50%" value="0" disabled="disabled"></td>
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
								
									<input type="checkbox" value="0" id="methodMoney" disabled="disabled" onclick="changeInputStatus(this)" name="methodMoney"
									<c:if test="${payInfo.methodMoney==true }">checked="checked"</c:if>>
									现金
									<input type="text" disabled="disabled" id="cashMoney" name="cashMoney" 
									<c:if test="${payInfo.cashMoney>0}">value="${payInfo.cashMoney}"</c:if> 
									<c:if test="${payInfo.cashMoney==0}">value="0"</c:if>>
								</td>
								<td style="width:30%">
									<input type="checkbox" value="0" id="methodPos" disabled="disabled" onclick="changeInputStatus(this)" name="methodPos"
									<c:if test="${payInfo.methodPos==true }">checked="checked"</c:if>>
									pos
									<input type="text" disabled="disabled" id="posMoney" name="posMoney" 
									<c:if test="${payInfo.posMoney>0}">value="${payInfo.posMoney}"</c:if> 
									<c:if test="${payInfo.posMoney==0}">value="0"</c:if>>
								</td>
								<td style="width:10%"><input type="button" 
									class="btn btn-primary" id="settle"  
									<c:if test="${empty signatureInfo.id }">value="确定" onclick="surePay()"</c:if>
									<c:if test="${not empty signatureInfo.id }">value="删除" onclick="deletePay()"</c:if> /> 
								</td>
								<td>
									<input type="hidden" name="methodMoney" />
									<input type="hidden" name="cashMoney" />
									<input type="hidden" name="methodPos" />
									<input type="hidden" name="posMoney" />
								</td>
								</tr>
							
							<tr>
								<td>业务应收合计:</td>
								<td><span id="sumMoney"><c:if test="${not empty payInfo}">${payInfo.totalMoney }</c:if>
								</span>
								</td>
								<td><input type="hidden" name="workTotalMoney" id="workTotalMoney" <c:if test="${not empty payInfo}">value="${payInfo.totalMoney }"</c:if>>
								</td>
								<td>
								</td>
							</tr>
							<tr>
							<td>本次实收金额：</td>
								<td>
								<input type="text" name="workPayedMoney" id="collectMoney" disabled="disabled" <c:if test="${not empty payInfo}">value="${payInfo.totalMoney }"</c:if>
								<c:if test="${empty payInfo}">value="0"</c:if>>
								</td>
									<td></td><td></td>
							</tr>
							<tr>
								<th colspan="4">发票信息</th>
							</tr>
							<tr>
								<td>是否开具发票：</td>
								<td><input type="radio" name="userReceipt" onclick="setReceiptMoneyNull()" value="true" <c:if test="${payInfo.isReceipt==true }">checked="checked"</c:if> <c:if test="${not empty workDealInfo.id }">checked="checked"</c:if> id="sff0">是
									<input type="radio" name="userReceipt" onclick="setReceiptMoneyNull()"  value="false" <c:if test="${payInfo.isReceipt==false }">checked="checked"</c:if> id="sff1">否</td>
								<td>发票金额：</td>
								<td><input type="text" name="receiptAmount" id = "receiptAmount" 
									<c:if test="${not empty receiptInvoice}">value="${receiptInvoice.receiptMoney }"</c:if>
									<c:if test="${empty receiptInvoice}">value="0"</c:if>>　元</td>
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
