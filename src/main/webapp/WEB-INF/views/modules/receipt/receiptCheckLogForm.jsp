<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>盘点信息管理</title>
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
		<li><a href="${ctx}/receipt/receiptCheckLog/">盘点信息列表</a></li>
		<li class="active"><a href="${ctx}/receipt/receiptCheckLog/form?id=${receiptCheckLog.id}">盘点信息<shiro:hasPermission name="receipt:receiptCheckLog:edit">${not empty receiptCheckLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="receipt:receiptCheckLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form id="inputForm" action = "${ctx}/receipt/receiptCheckLog/save" name = "" method="post"  class="form-horizontal">
		<div class="control-group">
			<label class="control-label">库房名称:</label>
			<div class="controls">
				${receiptDepotInfo.receiptName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所在区域:</label>
			<div class="controls">
				${receiptDepotInfo.office.areaName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属网点:</label>
			<div class="controls">
				${receiptDepotInfo.office.name }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">金额总量:</label>
			<div class="controls">
				${numMoney }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">金额总量:</label>
			<div class="controls">
				${numMoney }
			</div>
		</div>
	</form>
</body>
</html>
