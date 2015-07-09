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
		<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>厂商名称</th>
				<th>Key类型名称</th>
				<th>调拨数量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${kaads}" var="kaad" varStatus="status">
				<tr>
					<td>${status.index+1 }</td>
					<td>${kaad.keyGeneralInfo.configSupplier.supplierName}</td>
					<td>${kaad.keyGeneralInfo.name}</td>
					<td>${kaad.applyNewNum}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
