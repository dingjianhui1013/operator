<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>出库信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate(
					{
						submitHandler : function(form) {
							loading('正在提交，请稍等...');
							form.submit();
						},
					});
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
		<li class="active"><a href="#">查看</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/receipt/tong/litj" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input name="id" type="hidden" value="${receiptDepotInfo.id}"/>
		<label>单位名称 ：</label>
		<input name="comName" type="text" value="${comName}"/>
		<input id="startTime" name="startTime" type="hidden" readonly="readonly"
			maxlength="20" class="input-medium Wdate" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
				<input id="endTime" name="endTime" type="hidden" readonly="readonly"
			maxlength="20" class="input-medium Wdate" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
			
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<tags:message content="${message}"/>
	<%-- <table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="2">发票统计</th>
			</tr>
		</thead>
		<tr>
			<td>
				库房名称：${receiptDepotInfo.receiptName }<br/>
				所在区域：${receiptDepotInfo.office.areaName }<br/>
				所属网点：${receiptDepotInfo.office.name }
			</td>
			<td>
				入库金额/元：${numMoney }<br/>
				出库金额/元：${chuku }<br/>
				剩余金额/元：${receiptDepotInfo.receiptResidue }
			</td>
		</tr>
	</table> --%>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>库房名称</th>
				<th>发票金额</th>
				<th>出库原因</th>
				<th>出库时间</th>
				<th>出库对象</th>
				<th>接收库房</th>
				<th>操作人员</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${receiptDepotInfo.id==zkid }">
				<c:forEach items="${page.list}" var="receiptInvoice" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${receiptInvoice.receiptDepotInfo.receiptName }</td>
						<td>${receiptInvoice.receiptMoney}</td>
						<td>
							<c:if test="${receiptInvoice.receiptType==0}">销售出库</c:if>
							<c:if test="${receiptInvoice.receiptType==1}">调拨出库</c:if>
							<c:if test="${receiptInvoice.receiptType==2}">异常出库</c:if>
						</td>
						<td><fmt:formatDate value="${receiptInvoice.createDate}" pattern="yyyy-MM-dd"/></td>
						<td></td>
						<td>${receiptInvoice.companyName}</td>
						<td>${receiptInvoice.createBy.name}</td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${receiptDepotInfo.id!=zkid }">
				<c:forEach items="${page.list}" var="receiptInvoice" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${receiptInvoice.receiptDepotInfo.receiptName }</td>
						<td>${receiptInvoice.receiptMoney}</td>
						<td>
							<c:if test="${receiptInvoice.receiptType==0}">销售出库</c:if>
							<c:if test="${receiptInvoice.receiptType==1}">调拨出库</c:if>
							<c:if test="${receiptInvoice.receiptType==2}">异常出库</c:if>
						</td>
						<td><fmt:formatDate value="${receiptInvoice.createDate}" pattern="yyyy-MM-dd"/></td>
						<td>${receiptInvoice.companyName}</td>
						<td></td>
						<td>${receiptInvoice.createBy.name}</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
