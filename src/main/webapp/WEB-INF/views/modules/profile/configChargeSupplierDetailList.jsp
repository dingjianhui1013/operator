<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商计费配置管理</title>
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
		<li class="active"><a href="${ctx}/profile/configChargeSupplierDetail/">供应商计费配置列表</a></li>
		<shiro:hasPermission name="profile:configChargeSupplierDetail:edit"><li><a href="${ctx}/profile/configChargeSupplierDetail/form">供应商计费配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configChargeSupplierDetail" action="${ctx}/profile/configChargeSupplierDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="profile:configChargeSupplierDetail:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configChargeSupplierDetail">
			<tr>
				<td><a href="${ctx}/profile/configChargeSupplierDetail/form?id=${configChargeSupplierDetail.id}">${configChargeSupplierDetail.name}</a></td>
				<td>${configChargeSupplierDetail.remarks}</td>
				<shiro:hasPermission name="profile:configChargeSupplierDetail:edit"><td>
    				<a href="${ctx}/profile/configChargeSupplierDetail/form?id=${configChargeSupplierDetail.id}">修改</a>
					<a href="${ctx}/profile/configChargeSupplierDetail/delete?id=${configChargeSupplierDetail.id}" onclick="return confirmx('确认要删除该供应商计费配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
