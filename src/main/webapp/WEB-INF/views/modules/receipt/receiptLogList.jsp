<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>发票定时统计管理</title>
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
		<li class="active"><a href="${ctx}/receipt/receiptLog/">发票定时统计列表</a></li>
		<shiro:hasPermission name="receipt:receiptLog:edit"><li><a href="${ctx}/receipt/receiptLog/form">发票定时统计添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="receiptLog" action="${ctx}/receipt/receiptLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="receipt:receiptLog:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptLog">
			<tr>
				<td><a href="${ctx}/receipt/receiptLog/form?id=${receiptLog.id}">${receiptLog.name}</a></td>
				<td>${receiptLog.remarks}</td>
				<shiro:hasPermission name="receipt:receiptLog:edit"><td>
    				<a href="${ctx}/receipt/receiptLog/form?id=${receiptLog.id}">修改</a>
					<a href="${ctx}/receipt/receiptLog/delete?id=${receiptLog.id}" onclick="return confirmx('确认要删除该发票定时统计吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
