<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>统计报表查看</title>
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workFinancePayInfoRelation/statisticalList">绑定信息列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workFinancePayInfoRelation/statisticalShow?id=${statistical.id}">绑定信息详情</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="workFinancePayInfoRelation"
		action="${ctx}/work/workFinancePayInfoRelation/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">付款编号:</label>
			<div class="controls">
				${workFinancePayInfoRelation.workPayInfo.sn}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls">
				${workFinancePayInfoRelation.financePaymentInfo.company}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款金额:</label>
			<div class="controls">
				${workFinancePayInfoRelation.money}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人:</label>
			<div class="controls">
				${workFinancePayInfoRelation.financePaymentInfo.commUserName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
				${workFinancePayInfoRelation.financePaymentInfo.commMobile}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款时间:</label>
			<div class="controls">
			<fmt:formatDate value="${workFinancePayInfoRelation.workPayInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应用名称:</label>
			<div class="controls">
				${workFinancePayInfoRelation.financePaymentInfo.configApp.appName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息:</label>
			<div class="controls">
				${workFinancePayInfoRelation.workPayInfo.remarks}
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
