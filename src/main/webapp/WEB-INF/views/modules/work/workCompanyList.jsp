<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>单位名称管理</title>
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
		<li class="active"><a href="${ctx}/work/workCompany/">单位名称列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workCompany" action="${ctx}/work/workCompany/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>选择</th><th>单位名称</th><th>工商注册号</th><th>省份</th><th>市</th>		
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="workCompany">
			<tr>
				<td>
				<input name="companyId" type="radio" value="${workCompany.id}&${workCompany.companyName}"/>
				<td>${workCompany.companyName}</td>
				<td>${workCompany.businessNumber}</td>
				<td>${workCompany.province}</td>
				<td>${workCompany.city}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
