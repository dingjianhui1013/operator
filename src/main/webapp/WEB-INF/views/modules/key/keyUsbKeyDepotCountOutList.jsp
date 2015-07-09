<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key出库详情管理</title>
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
		<li class="active"><a href="${ctx}/key/keyUsbKeyInvoice/depotCountOutList?startTime=${startTime}&endTime=${endTime}&keyId=${keyId}&supplierId=${supplierId}&depotId=${depotId}">出库信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKeyInvoice"
		action="${ctx}/key/keyUsbKeyInvoice/depotCountOutList?startTime=${startTime}&endTime=${endTime}&keyId=${keyId}&supplierId=${supplierId}&depotId=${depotId}" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="startTime" name="startTime" type="hidden" value="${startTime }" />
		<input id="endTime" name="endTime" type="hidden" value="${endTime }" />
		<input id="keyId" name="keyId" type="hidden" value="${keyId }" />
		<input id="supplierId" name="supplierId" type="hidden" value="${supplierId }" />
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
				<th>出库数量</th>
				<th>key序列号</th>
				<th>出库原因</th>
				<th>出库备注</th>
				<th>接收网点</th>
				<th>操作人员</th>
				<th>出库时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyUsbKeyInvoice" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${keyUsbKeyInvoice.keyUsbKeyDepot.depotName}</td>
					<td>${keyUsbKeyInvoice.keyGeneralInfo.configSupplier.supplierName}</td>
					<td>${keyUsbKeyInvoice.keyGeneralInfo.name}</td>
					<td>${keyUsbKeyInvoice.deliveryNum}</td>
					<td>${keyUsbKeyInvoice.keySn}</td>
					<td>${keyUsbKeyInvoice.outReasonName}</td>
					<td>${keyUsbKeyInvoice.description}</td>
					<td>${keyUsbKeyInvoice.keyUsbKeyDepotReceive.depotName}</td>
					<td>${keyUsbKeyInvoice.createBy.name}</td>
					<td>
					<fmt:formatDate value="${keyUsbKeyInvoice.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
