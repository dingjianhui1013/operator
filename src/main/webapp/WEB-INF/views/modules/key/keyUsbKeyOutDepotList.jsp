<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key入库信息管理</title>
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
		<li class="active"><a href="${ctx}/key/keyUsbKeyDepot/">key入库信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKeyDepot"
		action="${ctx}/key/keyUsbKeyDepot/outList" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>库房名称 ：</label>
		<form:input path="depotName" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>库房名称</th>
				<th>库房数量</th>
				<th>所在区域</th>
				<th>所属网点</th>
				<th>key类型名称</th>
				<th>key类型标识</th>
				<th>key类型余量</th>
				<th>联系人</th>
				<th>电话</th>
				<shiro:hasPermission name="key:keyUsbKeyDepot:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyUsbKeyDepot">
				<tr>
					<td>${keyUsbKeyDepot.depotName}</td>
					<td></td>
					<td>${keyUsbKeyDepot.office.parent.name}</td>
					<td>${keyUsbKeyDepot.office.name}</td>
					<td>${keyUsbKeyDepot.remarks}</td>
					<td>${keyUsbKeyDepot.remarks}</td>
					<td>${keyUsbKeyDepot.remarks}</td>
					<td>${keyUsbKeyDepot.linkmanName}</td>
					<td>${keyUsbKeyDepot.linkmanMobilePhone}</td>
					<shiro:hasPermission name="key:keyUsbKeyDepot:edit">
						<td>
						<a
							href="${ctx}/key/keyUsbKeyInvoice/list?depotId=${keyUsbKeyDepot.id}">查看</a>
						<a
							href="${ctx}/key/keyUsbKeyInvoice/form?depotId=${keyUsbKeyDepot.id}">出库</a>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
