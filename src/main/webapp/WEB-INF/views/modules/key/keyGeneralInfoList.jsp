<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key类型信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/key/keyGeneralInfo?supplierId=${supplierId }">key类型信息列表</a></li>
		<shiro:hasPermission name="key:keyGeneralInfo:edit">
			<li><a href="${ctx}/key/keyGeneralInfo/form?supplierId=${supplierId }">添加key类型标识</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="keyGeneralInfo"
		action="${ctx}/key/keyGeneralInfo?supplierId=${supplierId }" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<%-- <label>Key类型名称 ：</label>
		<form:input path="name" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" /> --%>
	</form:form>
	<tags:message content="${message}" />
	<input type="hidden" name="supplierId"  id="supplierId" value="${supplierId }">
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>Key类型名称</th>
				<th>Key类型标识</th>
				<th>技术支持姓名</th>
				<th>技术支持电话</th>
				<th>备注</th>
				<shiro:hasPermission name="key:keyGeneralInfo:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyGeneralInfo">
				<tr>
					<td><a
						href="${ctx}/key/keyGeneralInfo/form?id=${keyGeneralInfo.id}&supplierId=${supplierId }">${keyGeneralInfo.name}</a></td>
					<td>${keyGeneralInfo.model}</td>
					<td>${keyGeneralInfo.linkman}</td>
					<td>${keyGeneralInfo.linkmanPhone}</td>
					<td>${keyGeneralInfo.description}</td>
					
					<shiro:hasPermission name="key:keyGeneralInfo:edit">
						<td><a
							href="${ctx}/key/keyGeneralInfo/form?id=${keyGeneralInfo.id}&supplierId=${supplierId }" >编辑</a>
							<a
							href="${ctx}/key/keyGeneralInfo/delete?id=${keyGeneralInfo.id}&supplierId=${manufacturerId }"
							onclick="return confirmx('确认要删除该key类型信息吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
