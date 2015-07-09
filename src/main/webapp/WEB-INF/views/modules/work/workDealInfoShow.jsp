<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>已办理证书明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"></li>
	</ul>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>编号</th>
		<th>应用名称</th>
		<th>证书类型</th>
		<th>应用标识</th>
        <th>证书有效期</th>
		<tbody>
		<c:forEach items="${page.list}" var="cert">
			<tr>
				<td>${cert.id }</td>
				<td>${cert.configApp.appName }</td>
				<td>${pro[cert.configProduct.productName] }</td>
				<td><c:if test="${cert.configProduct.productLabel==1 }">专用</c:if>
				<c:if test="${cert.configProduct.productLabel==0 }">通用</c:if></td>
				<td>
				<fmt:formatDate value="${cert.workCertInfo.notbefore}" pattern="yyyy-MM-dd"/>
				至
				<fmt:formatDate value="${cert.workCertInfo.notafter}" pattern="yyyy-MM-dd"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
