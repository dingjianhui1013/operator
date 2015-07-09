<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>邮件配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#url").focus();
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
		<li class="active"><a href="${ctx}/sys/sysConfig/smsForm">短信服务配置</a></li>
	</ul><br/>
		<form:form id="inputForm" modelAttribute="sysConfig" action="${ctx}/sys/sysConfig/smsSave" method="post" class="form-horizontal">
			<tags:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">短信服务地址：</label>
				<input id="url" name="smsUrl" type="text" class="bor txtth" value="${sms_url}"></input>
			</div>
			<div class="control-group">
				<label class="control-label">用户名：</label>
				<input id="name" name="smsName" type="text" class="bor txtth" value="${sms_name}"></input>
			</div>
			<div class="control-group">
				<label class="control-label">密码：</label>
				<input id="pass" name="smsPassword" type="password" class="bor txtth" value="${sms_password}"></input>
			</div> 
		<div class="form-actions">
			<shiro:hasPermission name="sys:sysConfig:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	
</body>
</html>
