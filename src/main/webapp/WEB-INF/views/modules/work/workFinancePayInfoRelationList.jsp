<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>支付信息统计报表管理</title>
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
		<li class="active"><a href="${ctx}/work/workFinancePayInfoRelation/">支付信息统计报表列表</a></li>
		<shiro:hasPermission name="work:workFinancePayInfoRelation:edit"><li><a href="${ctx}/work/workFinancePayInfoRelation/form">支付信息统计报表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workFinancePayInfoRelation" action="${ctx}/work/workFinancePayInfoRelation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="work:workFinancePayInfoRelation:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="workFinancePayInfoRelation">
			<tr>
				<td><a href="${ctx}/work/workFinancePayInfoRelation/form?id=${workFinancePayInfoRelation.id}">${workFinancePayInfoRelation.name}</a></td>
				<td>${workFinancePayInfoRelation.remarks}</td>
				<shiro:hasPermission name="work:workFinancePayInfoRelation:edit"><td>
    				<a href="${ctx}/work/workFinancePayInfoRelation/form?id=${workFinancePayInfoRelation.id}">修改</a>
					<a href="${ctx}/work/workFinancePayInfoRelation/delete?id=${workFinancePayInfoRelation.id}" onclick="return confirmx('确认要删除该支付信息统计报表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
