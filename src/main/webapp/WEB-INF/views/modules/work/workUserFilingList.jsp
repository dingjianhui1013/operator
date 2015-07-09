<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>联系人管理</title>
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
		<li class="active"><a href="${ctx}/work/workDealInfoFiling/list?status=1">已归档用户</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/list?status=0">未归档用户</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/list?status=2">咨询档用户</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workUser" action="${ctx}/work/workUser/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="contactName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>用户名称</th>
				<th>用户类型</th>
				<th>所属应用</th>
				<th>产品名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workUser">
			<tr>
				<td>${workUser.userSn}</td>
				<td><a href="${ctx}/work/workUser/form?id=${workUser.id}">${workUser.contactName}</a></td>
				<td><c:forEach items="${workUser.workDealInfos}" var="workDealInfo">${workDealInfo.configApp.appName}</c:forEach></td>
				<td>
    				<a href="${ctx}/work/workUserFiling/alreadyform?id=${workUser.id}">查看</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
