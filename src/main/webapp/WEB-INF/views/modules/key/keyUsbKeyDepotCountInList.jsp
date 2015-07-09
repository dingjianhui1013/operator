<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key入库详情管理</title>
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
		<li class="active"><a
			href="${ctx}/key/keyUsbKey/depotCountInList?startTime=${startTime}&endTime=${endTime}&keyId=${keyId}&supplierId=${supplierId}&depotId=${depotId}">入库信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKey"
		action="${ctx}/key/keyUsbKey/depotCountInList?startTime=${startTime}&endTime=${endTime}&keyId=${keyId}&supplierId=${supplierId}&depotId=${depotId}"
		method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="startTime" name="startTime" type="hidden"
			value="${startTime }" />
		<input id="endTime" name="endTime" type="hidden" value="${endTime }" />
		<input id="keyId" name="keyId" type="hidden" value="${keyId }" />
		<input id="manufacturerId" name="manufacturerId" type="hidden"
			value="${manufacturerId }" />
		<input id="depotId" name="depotId" type="hidden" value="${depotId }" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>库存名称</th>
				<th>厂商名称</th>
				<th>key类型名称</th>
				<th>入库数量</th>
				<th>入库原因</th>
				<th>入库备注</th>
				<th>操作人员</th>
				<th>入库时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyUsbKey" varStatus="status">
				<tr>
					<td>${status.index + 1}</td>
					<td>${keyUsbKey.keyUsbKeyDepot.depotName}</td>
					<td>${keyUsbKey.keyGeneralInfo.configSupplier.supplierName}</td>
					<td>${keyUsbKey.keyGeneralInfo.name}</td>
					<td>${keyUsbKey.count}</td>
					<td>${keyUsbKey.inReasonName}</td>
					<td>${keyUsbKey.description}</td>
					<td>${keyUsbKey.createBy.name}</td>
					<td><fmt:formatDate value="${keyUsbKey.startDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
