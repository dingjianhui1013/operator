<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发票类型管理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/receipt/receiptType/">发票类型管理列表</a></li>
		<shiro:hasPermission name="receipt:receiptType:edit">
			<li><a href="${ctx}/receipt/receiptType/form">发票类型管理添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="receiptType"
		action="${ctx}/receipt/receiptType/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型名称</th>
				<th>备注</th>
				<shiro:hasPermission name="receipt:receiptType:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="receiptType">
				<tr>
					<td><a
						href="${ctx}/receipt/receiptType/form?id=${receiptType.id}">${receiptType.typeName}元</a></td>
					<td>${receiptType.remark}</td>
					<shiro:hasPermission name="receipt:receiptType:edit">
						<td><a
							href="${ctx}/receipt/receiptType/form?id=${receiptType.id}">修改</a>
							<a href="${ctx}/receipt/receiptType/delete?id=${receiptType.id}"
							onclick="return confirmx('确认要删除该发票类型吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
