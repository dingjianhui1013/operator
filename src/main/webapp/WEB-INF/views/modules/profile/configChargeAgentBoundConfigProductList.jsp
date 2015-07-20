<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费策略模版详情历史管理</title>
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
		<li class="active"><a href="${ctx}/profile/configChargeAgentBoundConfigProduct/">计费策略模版详情历史列表</a></li>
		<shiro:hasPermission name="profile:configChargeAgentBoundConfigProduct:edit"><li><a href="${ctx}/profile/configChargeAgentBoundConfigProduct/form">计费策略模版详情历史添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configChargeAgentBoundConfigProduct" action="${ctx}/profile/configChargeAgentBoundConfigProduct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="profile:configChargeAgentBoundConfigProduct:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configChargeAgentBoundConfigProduct">
			<tr>
				<td><a href="${ctx}/profile/configChargeAgentBoundConfigProduct/form?id=${configChargeAgentBoundConfigProduct.id}">${configChargeAgentBoundConfigProduct.name}</a></td>
				<td>${configChargeAgentBoundConfigProduct.remarks}</td>
				<shiro:hasPermission name="profile:configChargeAgentBoundConfigProduct:edit"><td>
    				<a href="${ctx}/profile/configChargeAgentBoundConfigProduct/form?id=${configChargeAgentBoundConfigProduct.id}">修改</a>
					<a href="${ctx}/profile/configChargeAgentBoundConfigProduct/delete?id=${configChargeAgentBoundConfigProduct.id}" onclick="return confirmx('确认要删除该计费策略模版详情历史吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
