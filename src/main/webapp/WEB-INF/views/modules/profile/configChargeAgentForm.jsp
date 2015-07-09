<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商应用计费策略管理</title>
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
		});
	</script>
	
	<script type="text/javascript">
		var add = '<select name="addYear" id="addDiv"><option <c:if test="${configChargeAgentDetail.chargeYear==1}">selected</c:if> value="1">一年</option><option <c:if test="${configChargeAgentDetail.chargeYear==2}">selected</c:if> value="2">二年</option><option <c:if test="${configChargeAgentDetail.chargeYear==4}">selected</c:if> value="4">四年</option></select><input type="text" name="addMoney">';
		var update = ''
	
	
		function addLine(obj){
			
			
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configChargeAgent/">代理商应用计费</a></li>
		<li class="active"><a href="${ctx}/profile/configChargeAgent/form?id=${configChargeAgent.id}">计费策略</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configProduct" action="${ctx}/profile/configChargeAgent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">产品标识:</label>
			<div class="controls">
				<input type="text" <c:if test="${configProduct.productLabel==0}">value="通用"</c:if>
				 <c:if test="${configProduct.productLabel==1}">value="专用"</c:if>  disabled style="color:red;">
				 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应用名称:</label>
			<div class="controls">
				<input type="text" name="appName" value="${configProduct.configApp.appName }" disabled>
				<input type="hidden" name="appId" value="${configProduct.configApp.id }">
				<input type="hidden" name="productId" value="${configProduct.id }">
				<input type="hidden" name="productLabel" value="${configProduct.productLabel }">
				<input type="hidden" name="configChargeAgentDetailId" value="${configChargeAgentDetail.id }">
				<input type="hidden" name="configChargeAgentId" value="${configChargeAgent.id }">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称:</label>
			<div class="controls">
				<input type="text" name="productName" value="${productTypeMap[configProduct.productName] }" disabled>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务类型:</label>
			<div class="controls">
				
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新增:</label>
			<div class="controls">
				一年：<input type="text" name="addMoney" value="${xz1}"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
				两年：<input type="text" name="addMoney" value="${xz2}"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
				四年：<input type="text" name="addMoney" value="${xz4}"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">更新:</label>
			<div class="controls" id="updateDiv">
				一年：<input type="text" name="updateMoney" value="${gx1}"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br>
				两年：<input type="text" name="updateMoney" value="${gx2}"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br>
				四年：<input type="text" name="updateMoney" value="${gx4}"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">补办:</label>
			<div class="controls" id="reissueDiv">
				遗失补办:<input type="text" name="reissueMoney0" value="${bb0}"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
				损坏更换:<input type="text" name="reissueMoney1" value="${bb1}"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">变更:</label>
			<div class="controls">
				<input type="text" name="changeMoney" value="${th }"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户费:</label>
			<div class="controls">
				<input type="text" name="openAccountMoney" value="${khf }"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">可信移动设备:</label>
			<div class="controls">
				半年:<input type="text" name="trustDeviceMoney" value="${trustDevice0 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
				一年:<input type="text" name="trustDeviceMoney" value="${trustDevice1 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		
		
				<%-- <select name="workType">
					<option <c:if test="${configChargeAgentDetail.workType==0}">selected</c:if> value="0">新增</option>
					<option <c:if test="${configChargeAgentDetail.workType==1}">selected</c:if>  value="1">更新</option>
					<option <c:if test="${configChargeAgentDetail.workType==2}">selected</c:if>  value="2">补办</option>
					<option <c:if test="${configChargeAgentDetail.workType==3}">selected</c:if>  value="3">变更</option>
					<option <c:if test="${configChargeAgentDetail.workType==4}">selected</c:if>  value="4">开户费</option>
				</select> --%>
		
		<div class="control-group">
			<label class="control-label">缴费方式:</label>
			<div class="controls">
				<input name="chargeMethodPos" <c:if test="${configChargeAgent.chargeMethodPos}">checked</c:if> type="checkbox" value="0">pos&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodMoney" <c:if test="${configChargeAgent.chargeMethodMoney}">checked</c:if> type="checkbox" value="0">现金缴费&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodBank" <c:if test="${configChargeAgent.chargeMethodBank}">checked</c:if> type="checkbox" value="0">银行转账	&nbsp;&nbsp;&nbsp;<br>
				<input name="chargeMethodAlipay" <c:if test="${configChargeAgent.chargeMethodAlipay}">checked</c:if> type="checkbox" value="0">支付宝&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodGov" <c:if test="${configChargeAgent.chargeMethodGov}">checked</c:if> type="checkbox" value="0">政府统一采购&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodContract" <c:if test="${configChargeAgent.chargeMethodContract}">checked</c:if> type="checkbox" value="0">合同采购&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configChargeAgent:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
