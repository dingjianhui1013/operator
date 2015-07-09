<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发票盘点详情管理</title>
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
		<li class="active"><a href="${ctx}/receipt/receiptCheckLogDetail/">发票盘点详情列表</a></li>
		<shiro:hasPermission name="receipt:receiptCheckLogDetail:edit"><li><a href="${ctx}/receipt/receiptCheckLogDetail/form">发票盘点详情添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="receiptCheckLogDetail" action="${ctx}/receipt/receiptCheckLogDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="receipt:receiptCheckLogDetail:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptCheckLogDetail">
			<tr>
				<td><a href="${ctx}/receipt/receiptCheckLogDetail/form?id=${receiptCheckLogDetail.id}">${receiptCheckLogDetail.name}</a></td>
				<td>${receiptCheckLogDetail.remarks}</td>
				<shiro:hasPermission name="receipt:receiptCheckLogDetail:edit"><td>
    				<a href="${ctx}/receipt/receiptCheckLogDetail/form?id=${receiptCheckLogDetail.id}">修改</a>
					<a href="${ctx}/receipt/receiptCheckLogDetail/delete?id=${receiptCheckLogDetail.id}" onclick="return confirmx('确认要删除该发票盘点详情吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
