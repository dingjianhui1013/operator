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
	function repeatName() {
		var name = $("#messageName").val();
		var RaId = $("#RaId").val();
		var url = "${ctx}/message/smsConfiguration/checkRepeat";
		$.ajax({
			url : url,
			type : 'POST',
			data : {
				messageName : name,
				RaId : RaId,
				_ : new Date().getTime()
			},
			dataType : 'json',
			success : function(data) {
				if (data.status == 0) {
					if (data.type == 1) {
						top.$.jBox.tip("模板名称已存在！");
						repeat = 1;
					} else {
						repeat = 0;
					}
				} else {
					top.$.jBox.tip("系统出现异常！");
				}
			}

		});
	}
	function onSubmit() {
		var name = $("#messageName").val();
		var content = $("#messageContent").val();
		if (name == "") {
			top.$.jBox.tip("请填写模板名称！");
			return false;
		}
		if (content == "") {
			top.$.jBox.tip("模板内容不得为空！");
			return false;
		}
		if (repeat == 1) {
			top.$.jBox.tip("模板名称已存在！");
			return false;
		}
		return true;
	}
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
			href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">短信配置<shiro:hasPermission
					name="message:smsConfiguration:edit">${not empty smsConfiguration.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="message:smsConfiguration:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<form:form id="smsConfiguForm" modelAttribute="smsConfiguration"
		action="${ctx}/message/smsConfiguration/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<input type="hidden" id="RaId" name="Id" value=${smsConfiguration.id}>

		<div class="control-group">
			<label class="control-label"><span style="color: red">*</span>&nbsp;短信模板名称:</label>
			<div class="controls">
				<form:hidden path="id" htmlEscape="false" maxlength="50"
					class="required" />
				<form:input path="messageName" htmlEscape="false" maxlength="50"
					class="required" onblur="repeatName()" />
			</div>
			<br>

			<div class="control-group">
				<label class="control-label"><span style="color: red;">*</span>&nbsp;短信模板内容:</label>
				<div class="controls">
					<form:textarea path="messageContent" cols="20" rows="15"
						maxlength="150" style="resize: none;"></form:textarea>
				</div>
			</div>

		</div>
		</div>



		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				onclick="return onSubmit()" value="保 存" />&nbsp; <input
				id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>

</body>
</html>
