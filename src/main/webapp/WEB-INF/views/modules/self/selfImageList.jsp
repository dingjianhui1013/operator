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
		<li class="active"><a href="${ctx}/self/selfImage/">计费策略模版详情历史列表</a></li>
		<shiro:hasPermission name="self:selfImage:edit"><li><a href="${ctx}/self/selfImage/form">计费策略模版详情历史添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="selfImage" action="${ctx}/self/selfImage/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="self:selfImage:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="selfImage">
			<tr>
				<td><a href="${ctx}/self/selfImage/form?id=${selfImage.id}">${selfImage.name}</a></td>
				<td>${selfImage.remarks}</td>
				<shiro:hasPermission name="self:selfImage:edit"><td>
    				<a href="${ctx}/self/selfImage/form?id=${selfImage.id}">修改</a>
					<a href="${ctx}/self/selfImage/delete?id=${selfImage.id}" onclick="return confirmx('确认要删除该计费策略模版详情历史吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
