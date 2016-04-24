<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>信任源管理管理</title>
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
		<li><a href="${ctx}/sys/sysCrlContext/list">信任源管理列表</a></li>
		<li class="active"><a href="${ctx}/sys/sysCrlContext/insertFrom">信任源管理添加</a></li>
	</ul><br/>
	<form:form id="inputForm" action="${ctx}/sys/sysCrlContext/save" method="post" enctype="multipart/form-data" class="form-horizontal" >
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">信任源名称：</label>
			<div class="controls">
				<input id="crlName" name="crlName" type="text" size="100">
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">CA证书(DER编码)：</label>
			<div class="controls">
				<input type="file" name="file" />
			</div>
		</div>
		
		<div class="control-group">
				<label class="control-label">CRL颁发点：</label>
				<div class="controls">
				<input id="crlUrl" name="crlUrl" type="text" size="100">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">CRL下载重试策略：</label>
			<div class="controls">
				<input id="retryPolicy" name="retryPolicy" type="text" size="100">
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<label><font color="#A8A8A8" id="mess" style="margin-top:9px;">&nbsp;例如(单位：秒)：60,600,6000</font></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">CRL检查：</label>
			<div class="controls">
				<select name="checkCrl" class="bor selwidth1">
					<option value="true">是</option>
					<option value="false">否</option>
				</select>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
