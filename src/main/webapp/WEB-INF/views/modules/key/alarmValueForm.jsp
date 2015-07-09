<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>预警值设置</title>
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
	<form:form id="assessmentForm" modelAttribute="keyDepotGeneralStatistics" action="${ctx}/key/keyDepotGeneralStatistics/saveAlarm" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="depotId" id="depotId" value="${depotId}"/>
		</br>
		<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th> 库房名称</th>
				<th>key类型名称</th>
				<th>预警值</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page}" var="statistics" varStatus="status">
				<tr>
					<td>${statistics.keyUsbKeyDepot.depotName}</td>
					<td>${statistics.keyGeneralInfo.name}</td>
					<td>
					<input type="text" name="alarm" value="${not empty statistics.alarmValue? statistics.alarmValue:0}" class="required" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</form:form>
</body>
</html>
