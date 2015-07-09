<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>日经营统计管理</title>
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
		<li class="active"><a href="${ctx}/statistic/statisticAppData/">日经营统计列表</a></li>
		<shiro:hasPermission name="statistic:statisticAppData:edit"><li><a href="${ctx}/statistic/statisticAppData/form">日经营统计添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="statisticAppData" action="${ctx}/statistic/statisticAppData/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="statistic:statisticAppData:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="statisticAppData">
			<tr>
				<td><a href="${ctx}/statistic/statisticAppData/form?id=${statisticAppData.id}">${statisticAppData.name}</a></td>
				<td>${statisticAppData.remarks}</td>
				<shiro:hasPermission name="statistic:statisticAppData:edit"><td>
    				<a href="${ctx}/statistic/statisticAppData/form?id=${statisticAppData.id}">修改</a>
					<a href="${ctx}/statistic/statisticAppData/delete?id=${statisticAppData.id}" onclick="return confirmx('确认要删除该日经营统计吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
