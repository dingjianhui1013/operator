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
		<li class="active"><a
			href="${ctx}/work/workDealInfo/statisticalDealPayListShow?dealInfoId=${workDealInfo.id}">统计信息查看</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="workDealInfo"
		action="" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls">
				${workDealInfo.workCompany.companyName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经办人:</label>
			<div class="controls">
				${workDealInfo.workUser.contactName}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">证书类型:</label>
			<div class="controls">
				${proType[workDealInfo.configProduct.productName] }
			</div>
		</div>

		</div>
		<div class="control-group">
			<label class="control-label">业务类型:</label>
			<div class="controls">
				${wType[workDealInfo.dealInfoType] }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;年限:</label>
			<div class="controls">
				${workDealInfo.year }
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
