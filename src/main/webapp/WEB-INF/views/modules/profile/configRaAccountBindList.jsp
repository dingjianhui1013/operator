<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>RA配置管理</title>
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
		<li class="active"><a href="${ctx}/profile/configRaAccount/">RA模板列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configRaAccount" action="${ctx}/profile/configRaAccount/bindList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="configProductId" name="configProductId" type="hidden" value="${configProductId}"/>
		<label>RA模板名称 ：</label><form:input path="raName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>RA模板名称</th><!--<th>备注</th><shiro:hasPermission name="profile:configRaAccount:edit">--><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configRaAccount">
			<tr>
				<%-- <td><a href="${ctx}/profile/configRaAccount/form?id=${configRaAccount.id}">${configRaAccount.raName}</a></td> --%>
				<td>${configRaAccount.raName}</td>
				<shiro:hasPermission name="profile:configProduct:edit"><td>
				<a href="${ctx}/profile/configProduct/bindSave?productId=${configProductId}&raAccountId=${configRaAccount.id}">绑定RA模板</a>
				
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
