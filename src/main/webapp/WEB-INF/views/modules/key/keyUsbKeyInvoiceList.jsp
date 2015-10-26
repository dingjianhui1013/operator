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
		<li ><a href="${ctx}/key/keyUsbKeyDepot/">库存管理</a></li>
		<li class="active"><a href="${ctx}/key/keyUsbKeyInvoice/list?depotId=${depotId}">出库信息列表</a></li>
		<c:if test="${!depot.canDel()}">
		<li><a href="${ctx}/key/keyUsbKeyInvoice/form?depotId=${depotId}">添加出库信息</a></li>
		</c:if>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKeyInvoice"
		action="${ctx}/key/keyUsbKeyInvoice/list?depotId=${depotId}" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
				<th>出库总量</th>
				<th>${count }</th>
				<th>当天出库量</th>
				<th colspan="9" align="left">${dayCount}</th>
			</tr>
			<tr>
				<th>编号</th>
				<th>库房名称</th>
				<th>厂商名称</th>
				<th>key类型名称</th>
				<th>出库数量</th>
				<th>key序列号</th>
				<th>出库原因</th>
				<th>出库时间</th>
				<th>出库对象</th>
				<th>接收网点</th>
				<th>操作人员</th>
				<th>出库备注</th>			
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
					<td>
					<fmt:formatDate value="${keyUsbKeyInvoice.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>${keyUsbKeyInvoice.companyName}</td>
					<td>${keyUsbKeyInvoice.keyUsbKeyDepotReceive.depotName}</td>
					<td>${keyUsbKeyInvoice.createBy.name}</td>
					<td width="80px" style="word-break: break-all">${keyUsbKeyInvoice.description}</td>
					
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
