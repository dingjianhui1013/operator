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
		<li ><a href="${ctx}/key/keyUsbKeyDepot/">库存管理</a></li>
		<li class="active"><a href="${ctx}/key/keyUsbKey/list?depotId=${depotId}">入库信息列表</a></li>
		<c:if test="${depot.canDelTotal()}">
		<li><a href="${ctx}/key/keyUsbKey/form?depotId=${depotId}">入库操作</a></li>
		</c:if>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKey"
		action="${ctx}/key/keyUsbKey/list?depotId=${depotId}" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"value="${page.pageSize}" />
		<%-- <label>名称 ：</label>
		<form:input path="name" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" /> --%>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>库房名称</th>
				<th>厂商名称</th>
				<th>KEY类型</th>
				<th>入库数量</th>
				<th>入库时间</th>
				<th>入库原因</th>
				<th>操作人员</th>
				<th>入库备注</th>
				<c:if test="${depot.canDelTotalBad() }">
				<th>发货库房名称</th>
				</c:if>
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
					<td>
					<fmt:formatDate value="${keyUsbKey.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>${keyUsbKey.inReasonName}</td>
					<td>${keyUsbKey.createBy.name}</td>
					<td width="140px" style="word-break: break-all">${keyUsbKey.description}</td>
					<c:if test="${depot.canDelTotalBad() }">
					<td>${keyUsbKey.fromkeyUsbKeyDepot.depotName}</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
