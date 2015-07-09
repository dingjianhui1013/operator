<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商业务关联管理</title>
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
		<li class="active"><a href="${ctx}/profile/configAgentAppRelation/">代理商业务关联列表</a></li>
		<shiro:hasPermission name="profile:configAgentAppRelation:edit"><li><a href="${ctx}/profile/configAgentAppRelation/form">代理商业务关联添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configAgentAppRelation" action="${ctx}/profile/configAgentAppRelation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="profile:configAgentAppRelation:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configAgentAppRelation">
			<tr>
				<td><a href="${ctx}/profile/configAgentAppRelation/form?id=${configAgentAppRelation.id}">${configAgentAppRelation.name}</a></td>
				<td>${configAgentAppRelation.remarks}</td>
				<shiro:hasPermission name="profile:configAgentAppRelation:edit"><td>
    				<a href="${ctx}/profile/configAgentAppRelation/form?id=${configAgentAppRelation.id}">修改</a>
					<a href="${ctx}/profile/configAgentAppRelation/delete?id=${configAgentAppRelation.id}" onclick="return confirmx('确认要删除该代理商业务关联吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
