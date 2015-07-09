<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>后台日志管理</title>
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
		<li class="active"><a href="${ctx}/log/sysLog/">后台日志列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="sysOperateLog" action="${ctx}/log/sysLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>类型 ：</label><form:input path="type" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>操作人：</label><form:input path="createBy.name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>序号</th><th>类型</th><th>详情</th><th>创建时间</th><th>操作人</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysLog" varStatus="status">
			<tr>
				<td>${status.count }</td>
				<td>${sysLog.type }</td>
				<td>${sysLog.remarks}</td>
				<td>
					<fmt:formatDate value="${sysLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${sysLog.createBy.name}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
