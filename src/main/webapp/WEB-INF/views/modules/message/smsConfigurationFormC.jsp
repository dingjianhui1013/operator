<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>短信配置管理</title>
<meta name="decorator" content="default" />

<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {

	});
</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="" action="${ctx}/message/smsConfiguration/import"
			method="post" enctype="multipart/form-data"
			style="padding-left: 20px; text-align: center;">
			<br /> <input id="uploadFile" name="file" type="file"
				style="width: 330px" /><br /> <br /> <input id="btnImportSubmit"
				class="btn btn-primary" type="submit" value="   导    入   " />
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置</a></li>
		<li class="active"><a
			href="${ctx}/message/smsConfiguration/form1?id=${smsConfiguration.id}">短信模板查看</a>

		</li>
	</ul>
	<form:form id="smsConfiguForm" modelAttribute="smsConfiguration"
		action="${ctx}/message/smsConfiguration/save" method="post"
		class="form-horizontal">

		<input type="hidden" id="RaId" name="Id" value=${SmsConfiguration.id}>
		<tags:message content="${message}" />

		<div class="control-group">
			<label class="control-label"><span style="color: red">*</span>&nbsp;短信模板名称:</label>
			<div class="controls">
				<form:hidden path="id" htmlEscape="false" maxlength="50"
					class="required" />
				<form:input path="messageName" htmlEscape="false" maxlength="50"
					class="required" readonly="true" />
			</div>
			<br>

			<div class="control-group">
				<label class="control-label"><span style="color: red;">*</span>&nbsp;短信模板内容:</label>
				<div class="controls">
					<form:textarea path="messageContent" cols="20" rows="15"
						maxlength="150" style="resize: none;" readonly="true"></form:textarea>
				</div>
			</div>

		</div>
		</div>



		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>

</body>
</html>
