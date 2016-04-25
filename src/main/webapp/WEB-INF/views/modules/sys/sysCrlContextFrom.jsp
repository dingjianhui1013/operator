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
		<li><a href="${ctx}/sys/sysCrlContext/list">证书登录配置列表</a></li>
		<li class="active"><a href="${ctx}/sys/sysCrlContext/insertFrom">证书登录配置添加</a></li>
	</ul><br/>
	<div class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
				<label class="control-label">CA证书签发者：</label>
			<div class="controls">
				<label>${sysCrlContext.issuerdn}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">CA证书主题：</label>
			<div class="controls">
				<label>${sysCrlContext.certSubject}</label>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">CA证书开始时间：</label>
			<div class="controls">
				<label>${sysCrlContext.certStartTime}</label>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">CA证书结束时间：</label>
			<div class="controls">
				<label>${sysCrlContext.certEndTime}</label>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">CRL有效性检查：</label>
			<div class="controls">
				<label>
					<c:if test="${sysCrlContext.checkCrl==true }">强制检查</c:if>
					<c:if test="${sysCrlContext.checkCrl==false }">不检查</c:if>
				</label>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">CRL颁发点：</label>
			<div class="controls">
				<label>${sysCrlContext.crlUrl}</label>
			</div>
		</div>
		<div class="control-group">
				<label class="control-label">CRL下载重试策略：</label>
			<div class="controls">
				<label>${sysCrlContext.retryPolicy}</label>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
</body>
</html>
