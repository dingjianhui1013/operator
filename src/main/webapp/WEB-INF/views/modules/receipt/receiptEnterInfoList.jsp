<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>入库信息管理</title>
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
		<li class="active"><a href="${ctx}/receipt/receiptEnterInfo/list">入库信息列表</a></li>
		<li ><a href="${ctx}/receipt/receiptDepotInfo/rukuFrom?id=${receiptDepotInfo.id}">入库操作</a></li>
	</ul>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>库房名称</th>
				<th>上次剩余金额</th>
				<th>本次入库金额</th>
				<th>本次结余金额</th>
				<th>入库时间</th>
				<th>入库原因</th>
				<th>操作人员</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptEnterInfo"  varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${receiptEnterInfo.receiptDepotInfo.receiptName }</td>
				<td>${receiptEnterInfo.beforMoney }</td>
				<td>${receiptEnterInfo.receiptMoney}</td>
				<td>${receiptEnterInfo.now_Money }</td>
				<td><fmt:formatDate value="${receiptEnterInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<c:if test="${receiptEnterInfo.receiptType==0}">采购入库</c:if>
					<c:if test="${receiptEnterInfo.receiptType==1}">调拨入库</c:if>
					<c:if test="${receiptEnterInfo.receiptType==2}">异常入库</c:if>
					<c:if test="${receiptEnterInfo.receiptType==3}">退费入库</c:if>
				</td>
				<td>${receiptEnterInfo.createBy.name}</td>
				<td>${receiptEnterInfo.remarks}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
