<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商应用计费策略模板管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			$("input[name='tempStyle']").bind("click",this,function(){
				var v = $(this).val();
				if(v=="1"){
					$("#chargeMethods").show();
				}else{
					$("#chargeMethods").hide();
				}
			});
		});


	</script>
	
	<script type="text/javascript">
		var add = '<select name="addYear" id="addDiv"><option <c:if test="${configChargeAgentDetail.chargeYear==1}">selected</c:if> value="1">一年</option><option <c:if test="${configChargeAgentDetail.chargeYear==2}">selected</c:if> value="2">二年</option><option <c:if test="${configChargeAgentDetail.chargeYear==4}">selected</c:if> value="4">四年</option></select><input type="text" name="addMoney">';
		var update = '';
	
	
		function addLine(obj){
		}

	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="configChargeAgent" action="${ctx}/profile/configChargeAgent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">计费策略模板名称:</label>
			<label class="control-label" style="text-align:left">&nbsp;&nbsp;&nbsp;${configChargeAgent.tempName }</label>

		</div>
		<input type="hidden" name="configChargeAgentId" value="${configChargeAgent.id}"  >
		<div class="control-group">
			<label class="control-label">模板类型:</label>
			<label class="control-label" style="text-align:left">
				&nbsp;&nbsp;&nbsp;<c:if test="${configChargeAgent.tempStyle=='1'}">标准</c:if>
				<c:if test="${configChargeAgent.tempStyle=='2'}">政府统一采购</c:if>
				<c:if test="${configChargeAgent.tempStyle=='3'}">合同采购</c:if>
			</label>

		</div>

		<div class="control-group">
			<label class="control-label">新增:</label>
			<div class="controls">
				一年：${xz1}<br/>
				两年：${xz2}<br/>
				四年：${xz4}<br/>
				五年：${xz5}<br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">更新:</label>
			<div class="controls" id="updateDiv">
				一年：${gx1}<br/>
				两年：${gx2}<br/>
				四年：${gx4}<br/>
				五年：${gx5}<br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">补办:</label>
			<div class="controls" id="reissueDiv">
				遗失补办:${bb0}<br/>
				损坏更换:${bb1}<br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">变更:</label>
			<div class="controls">
				${th}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户费:</label>
			<div class="controls">
				${khf}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">可信移动设备:</label>
			<div class="controls">
				半年:${trustDevice0}
				一年:${trustDevice1}
			</div>
		</div>
		
		
				<%-- <select name="workType">
					<option <c:if test="${configChargeAgentDetail.workType==0}">selected</c:if> value="0">新增</option>
					<option <c:if test="${configChargeAgentDetail.workType==1}">selected</c:if>  value="1">更新</option>
					<option <c:if test="${configChargeAgentDetail.workType==2}">selected</c:if>  value="2">补办</option>
					<option <c:if test="${configChargeAgentDetail.workType==3}">selected</c:if>  value="3">变更</option>
					<option <c:if test="${configChargeAgentDetail.workType==4}">selected</c:if>  value="4">开户费</option>
				</select> --%>

		<div class="control-group" id="chargeMethods">
			<label class="control-label">缴费方式:</label>
			<div class="controls" >
				<c:if test="${configChargeAgent.chargeMethodPos}">pos</c:if> &nbsp;&nbsp;&nbsp;
				<c:if test="${configChargeAgent.chargeMethodMoney}">现金缴费</c:if> &nbsp;&nbsp;&nbsp;
				<c:if test="${configChargeAgent.chargeMethodBank}">银行转账</c:if> &nbsp;&nbsp;&nbsp;<br>
			</div>
		</div>
		<c:if test="${configChargeAgent.tempStyle!='1'}">
		<div class="control-group" >
			<label class="control-label">配置新增数量:</label>
			<div class="controls" >${ configChargeAgent.configureNum}
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">配置更新数量:</label>
			<div class="controls" >${ configChargeAgent.configureUpdateNum}
			</div>
		</div>
		
		<div class="control-group" >
			<label class="control-label">新增剩余数量:</label>
			<div class="controls" >${ configChargeAgent.surplusUpdateNum}
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">更新剩余数量:</label>
			<div class="controls" >${ configChargeAgent.surplusUpdateNum}
			</div>
		</div>
		
		
		<div class="control-group" >
			<label class="control-label">新增已用数量:</label>
			<div class="controls" >${ configChargeAgent.availableNum + configChargeAgent.reserveNum}
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">更新已用数量:</label>
			<div class="controls" >${ configChargeAgent.availableUpdateNum + configChargeAgent.reserveUpdateNum}
			</div>
		</div>
		</c:if>
	</form:form>
</body>
</html>
