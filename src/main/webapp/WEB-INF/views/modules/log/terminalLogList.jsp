<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>前台日志管理</title>
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
		<li class="active"><a href="${ctx}/log/terminalLog/">前台日志列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="terminalLog" action="${ctx}/log/terminalLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>类型 ：</label><form:input path="type" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>序号</th><th>类型</th><th>详情</th><th>key序列号</th><th>操作时间</th><th>备注</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="terminalLog" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${terminalLog.type}</td>
				<td>${terminalLog.info}</td>
				<td>${terminalLog.keySn}</td>
				<td>
				 <fmt:formatDate value="${terminalLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				 </td>
				 <td>${terminalLog.remarks}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
