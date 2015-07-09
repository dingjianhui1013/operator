<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>出库信息管理</title>
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
		<li class="active"><a href="${ctx}/receipt/receiptDepotInfo/list">库房管理</a></li>
		<li class="active"><a href="${ctx}/receipt/receiptInvoice/">出库信息列表</a></li>
	</ul>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>库房名称</th>
				<th>出库对象</th>
				<th>发票金额</th>
				<th>出库时间</th>
				<th>操作人员</th>
				<th>出库原因</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptInvoice" varStatus="status">
			<tr>
				<td>${status.index+1}</td>
				<td>${receiptInvoice.receiptDepotInfo.receiptName }</td>
				<td>${receiptInvoice.companyName}</td>
				<td>${receiptInvoice.receiptMoney}</td>
				<td><fmt:formatDate value="${receiptInvoice.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${receiptInvoice.createBy.name}</td>
				<td>
					<c:if test="${receiptInvoice.receiptType==0}">销售出库</c:if>
					<c:if test="${receiptInvoice.receiptType==1}">调拨出库</c:if>
					<c:if test="${receiptInvoice.receiptType==2}">异常出库</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
