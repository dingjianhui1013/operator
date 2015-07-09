<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>区域管理</title>
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
		<li><a href="${ctx}/sys/office?appId=${appId}">网点配置</a></li>
		<li class="active"><a href="${ctx}/sys/office/addAreaFrom">添加区域</a></li>
		<li><a href="${ctx}/sys/office/isnertFrom">添加网点</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/office/saveArea" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>区域名称:</label>
			<div class="controls">
				<input type="text" id="area" name = "areaName"  maxlength="20" 
				 class="required"/>
			</div>
		</div>
		<script type="text/javascript">
			function copyTextToOffice(){
				if($("#office").val()!='') return;
				
				$("#office").val($("#area").val()+"网点");
				
			}
		</script>
		<div class="control-group">
			<label class="control-label">网点名称:</label>
			<div class="controls">
				<input name = "name" type = "text" id="office" maxlength="22" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:area:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>