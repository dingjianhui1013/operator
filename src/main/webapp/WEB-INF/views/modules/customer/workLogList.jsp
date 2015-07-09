<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>工作记录管理</title>
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
		<li class="active"><a href="${ctx}/work/workLog/">工作记录列表</a></li>
		<shiro:hasPermission name="work:workLog:edit"><li><a href="${ctx}/work/workLog/form">工作记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/workLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>应用名称</th>
				<th>服务类型</th>
				<th>问题类型</th>
				<th>客服人员</th>
				<th>时间</th>
				<th>数量</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workLog">
			<tr>
				<td>${workLog.configApp.appName }</td>
				<td>${workLog.serType}</td>
				<td>${workLog.probleType}</td>
				<td>${workLog.createBy.name}</td>
				<shiro:hasPermission name="work:workLog:edit"><td>
    				<a href="${ctx}/work/workLog/form?id=${workLog.id}">修改</a>
					<a href="${ctx}/work/workLog/delete?id=${workLog.id}" onclick="return confirmx('确认要删除该工作记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
