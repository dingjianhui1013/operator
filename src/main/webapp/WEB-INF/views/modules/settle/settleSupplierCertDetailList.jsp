<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商证书结算管理</title>
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
		<li class="active"><a href="${ctx}/settle/settleSupplierCertDetail/">供应商证书结算列表</a></li>
		<shiro:hasPermission name="settle:settleSupplierCertDetail:edit"><li><a href="${ctx}/settle/settleSupplierCertDetail/form">供应商证书结算添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="settleSupplierCertDetail" action="${ctx}/settle/settleSupplierCertDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="settle:settleSupplierCertDetail:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="settleSupplierCertDetail">
			<tr>
				<td><a href="${ctx}/settle/settleSupplierCertDetail/form?id=${settleSupplierCertDetail.id}">${settleSupplierCertDetail.name}</a></td>
				<td>${settleSupplierCertDetail.remarks}</td>
				<shiro:hasPermission name="settle:settleSupplierCertDetail:edit"><td>
    				<a href="${ctx}/settle/settleSupplierCertDetail/form?id=${settleSupplierCertDetail.id}">修改</a>
					<a href="${ctx}/settle/settleSupplierCertDetail/delete?id=${settleSupplierCertDetail.id}" onclick="return confirmx('确认要删除该供应商证书结算吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
