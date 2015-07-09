<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>厂商信息管理</title>
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
		<li class="active"><a href="${ctx}/profile/configSupplier/listByKey">厂商管理</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configSupplier"
		action="${ctx}/profile/configSupplier/listByKey" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden"  value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<label>厂商名称 ：</label>
		<form:input path="supplierName" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="搜索" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>厂商名称</th>
				<th>联系人</th>
				<th>电话号码</th>
				<th>厂商地址</th>
				<th>备注</th>
				<shiro:hasPermission name="key:keyManufacturer:edit">
					<th>操作</th>
				</shiro:hasPermission> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="configSupplier">
				<tr>
					<td>${configSupplier.supplierName}</td>
					<td>${configSupplier.supplierCommUsername}</td>
					<td>${configSupplier.supplierCommMobile}</td>
					<td width="140px" style="word-break: break-all">${configSupplier.supplierAddress}</td>
					<td>${configSupplier.supplierRemarks}</td>
					<shiro:hasPermission name="key:keyManufacturer:edit">
						<td>
							<a href="${ctx}/key/keyGeneralInfo?supplierId=${configSupplier.id}">key类型标识</a>
							</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
