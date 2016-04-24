<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>信任源管理管理</title>
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
		<li class="active"><a href="${ctx}/sys/sysCrlContext/list">证书配置列表</a></li>
		<li><a href="${ctx}/sys/sysCrlContext/insertFrom">证书配置添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="sysCrlContext" action="${ctx}/sys/sysCrlContext/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th >信任源名称</th>
    			<th >主题</th>
    			<th >序号</th>
    			<th >颁发者</th>
    			<th >操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysCrlContext">
			<tr>
				<td>${sysCrlContext.crlName}</td>
				<td><a href="${ctx}/sys/sysCrlContext/form?id=${sysCrlContext.id}">${sysCrlContext.certSubject}</a></td>
				<td>${sysCrlContext.certSn}</td>
				<td>${sysCrlContext.issuerdn}</td>
				<td>
					<a href="${ctx}/sys/sysCrlContext/update?id=${sysCrlContext.id}" >修改</a>
					<a href="${ctx}/sys/sysCrlContext/delete?id=${sysCrlContext.id}" onclick="return confirmx('确认要删除该信任源管理吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div> 
</body>
</html>
