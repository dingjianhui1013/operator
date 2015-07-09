<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>消息管理</title>
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
		<li><a href="${ctx}/profile/configMsg/">消息列表</a></li>
		<li class="active"><a href="${ctx}/profile/configMsg/form?id=${configMsg.id}">消息<shiro:hasPermission name="profile:configMsg:edit">${not empty configMsg.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="profile:configMsg:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configMsg" action="${ctx}/profile/configMsg/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">消息名称:</label>
			<div class="controls">
				<form:input path="msgName" htmlEscape="false" maxlength="11" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">消息主题:</label>
			<div class="controls">
				<form:input path="msgTitle" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">消息类型:</label>
			<div class="controls">
				<form:radiobutton path="msgType"  value="0"/>key库存提醒
				<form:radiobutton path="msgType"  value="1"/>发票库存提醒
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送方式:</label>
			<div class="controls">
				<form:radiobutton path="msgSendMethod" value="0"/>邮件
				<%-- <form:radiobutton path="msgSendMethod" value="1"/>短信 --%>
			</div>
		</div>
		<div class="control-group" style="display: none">
			<label class="control-label">发送时间:</label>
			<div class="controls">
				<form:radiobutton path="msgSendTimeType" value="0"/>每天8点
				<form:radiobutton path="msgSendTimeType" value="1"/>立即
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">消息内容:</label>
			<div class="controls">
				<form:textarea path="msgContent" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configMsg:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
