<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
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
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configCommercialAgent/">代理商列表</a></li>
		<li class="active"><a href="${ctx}/profile/configCommercialAgent/form?id=${configCommercialAgent.id}">代理商<shiro:hasPermission name="profile:configCommercialAgent:edit">${not empty configCommercialAgent.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="profile:configCommercialAgent:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configCommercialAgent" action="${ctx}/profile/configCommercialAgent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">代理商名称:</label>
			<div class="controls">
				<form:input path="agentName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代理商类型:</label>
			<div class="controls">
				<table>
					<tr>
						<td width="100px">
							<form:checkbox path="agentType1"/>市场推广</from>
						</td><td></td>
					</tr>
					<tr>
						<td></td><td><c:forEach items="${configApps}" var="configApp">${configApp.appName }<br/></c:forEach></td>
					</tr>
					<tr>
						<td>
							<form:checkbox path="agentType2"/>劳务关系</from>
						</td><td></td>
					</tr>
					<tr>
						<td></td><td><c:forEach items="${offices}" var="office">${office.name }<br/></c:forEach></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人姓名:</label>
			<div class="controls">
				<form:input path="agentCommUserName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人电话:</label>
			<div class="controls">
				<form:input path="agentCommMobile" htmlEscape="false" maxlength="11" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代理商地址:</label>
			<div class="controls">
				<form:input path="agentAddress" htmlEscape="false" maxlength="50" class="required" onkeup=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代理合同有效期:</label>
			<div class="controls">
				<label class="lbl">
				</label>
				<input id="timeStart" name="timeStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${configCommercialAgent.agentContractStart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				至
				<input id="timeEnd" name="timeEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${configCommercialAgent.agentContractEnd}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'timeStart\')}'});"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="agentRemark" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
