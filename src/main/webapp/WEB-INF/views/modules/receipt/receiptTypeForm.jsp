<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发票类型管理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
	
	function valid(obj){
		var regu = new RegExp("/^[1-9]+$/");
		alert($(obj).val().match(regu));
		if(!$(obj).val().match(regu)){
			top.$.jBox.tip("类型输入错误");
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/receipt/receiptType/">发票类型管理列表</a></li>
		<li class="active"><a
			href="${ctx}/receipt/receiptType/form?id=${receiptType.id}">发票类型管理<shiro:hasPermission
					name="receipt:receiptType:edit">${not empty receiptType.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="receipt:receiptType:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="receiptType"
		action="${ctx}/receipt/receiptType/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">发票类型:</label>
			<div class="controls">
				<form:input path="typeName" htmlEscape="false" onkeyup="value=value.match(/^[0-9]+$/,'')" 
				maxlength="3"/>元&nbsp;&nbsp;<label>例如：2元、10元、100元</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="receipt:receiptType:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
