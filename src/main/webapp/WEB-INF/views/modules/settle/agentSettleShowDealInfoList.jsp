<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
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
	<div class="form-horizontal">
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<!-- 	<th>办理时间</th>
				<th>单位名称</th>
				<th>应用名称</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>年限</th>
				<th>办理费用</th> -->
				<th>应用名称</th>
				<th>产品名称</th>
				<th>应结算年限</th>
				<th>已结算年限</th>
				<th>本次结算年限</th>
				<th>剩余结算年限</th>
				
				<!-- 应用名称、产品名称、应结算年限、已结算年限、本次结算年限、剩余结算年限 -->
		<tbody>
		<c:forEach items="${page.list}" var="workDealInfo">
			<tr>
				<%-- <td><fmt:formatDate value="${workDealInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
				<td>${workDealInfo.configApp.appName}</td>
				<td>${proType[workDealInfo.configProduct.productName]}${workDealInfo.configProduct.productLabel==0? "(通用)":"(专用)"}</td>
			<!-- 	应结算年限 == year
				已结算年限 == 已结算年限
				本次结算年限 == 实际结算年限
				 剩余结算年限 == 剩余结算年限 -->
				 <td>${workDealInfo.year}</td>
				<td>${workDealInfo.settledLife}</td>
				<td>${workDealInfo.physicalLife }</td>
				<td>${workDealInfo.residualLife}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</div>
</body>
</html>
