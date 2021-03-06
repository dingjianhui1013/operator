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
		<li ><a href="${ctx}/work/workDealInfoFiling/formF?uid=${user.id}">单位基本信息查看</a></li>
		<li class="active"><a href="#">联系人列表</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/loglist?uid=${user.id}">客服记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workUser" action="${ctx}/work/workUser/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<input class="btn btn-primary" type="button"
		onclick="window.location.href='${ctx}/work/workUser/insertWorkUserFromU?comId=${user.workCompany.id}'" value="新建联系人" />
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>姓名</th>
				<th>性别</th>
				<th>移动电话</th>
				<th>电子邮箱</th>
				<th>固定电话</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workUser" varStatus="status">
			<tr>
				<td>${ status.index + 1}</td>
				<td>${workUser.contactName}</td>
				<td>${workUser.contactSex}</td>
				<td>${workUser.contactPhone}</td>
				<td>${workUser.contactEmail}</td>
				<td>${workUser.contactTel}</td>
				<td>
    				<a href="${ctx}/work/workDealInfoFiling/updateUserFrom?id=${workUser.id}">编辑</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
