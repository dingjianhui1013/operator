<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="default" />
<title>支付信息退费管理</title>
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
		<li class="active"><a href="${ctx}/finance/financeQuitMoney/">退费信息列表</a></li>
	</ul>
	
	<form id="searchForm"
				action="${ctx}/finance/financeQuitMoney/list" method="post"
				class="breadcrumb form-search" >
		<input type="hidden" id="pageNo" name="pageNo" value="${page.pageNo}" />
		<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize }" />
		<div>
			<label>&nbsp;&nbsp;联系人&nbsp;  ：</label>
			<input name="commUserName" class="input-medium" />
			<label>&nbsp;&nbsp;支付时间&nbsp;  ：</label>			
			<input id="payStartTime" class="input-medium Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								value="${payStartTime }"
								maxlength="20" readonly="readonly" name="payStartTime" />&nbsp;&nbsp;至&nbsp;&nbsp;
			<input class="input-medium Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'payStartTime\')}'});"
								value="${payEndTime }"
								maxlength="20" readonly="readonly" name="payEndTime" />
		<div style="margin-top: 8px">
			<label>退费时间：</label>
			<input id="quitStartTime" class="input-medium Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								value="${quitStartTime }"
								maxlength="20" readonly="readonly" name="quitStartTime" />&nbsp;&nbsp;至&nbsp;&nbsp;
			<input class="input-medium Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'quitStartTime\')}'});"
								value="${quitEndTime }"
								maxlength="20" readonly="readonly" name="quitEndTime" />
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		</div>
		</div>
	</form>
	
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>付款编号</th>
				<th>付款单位名称</th>
				<th>支付金额</th>
				<th>联系人</th>
				<th>联系方式</th>
				<th>付款时间</th>
				<th>付款方式</th>
				<th>退费金额</th>
				<th>退费时间</th>
				<th>退费原因</th>
				<th>受理窗口</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list }" var="financeQuitMoney" varStatus="f">
				<tr>
					<td>${financeQuitMoney.id }</td>
					<td>${financeQuitMoney.financePaymentInfo.company }</td>
					<td><fmt:formatNumber value="${financeQuitMoney.financePaymentInfo.paymentMoney}" type="number" /></td>
					<td>${financeQuitMoney.financePaymentInfo.commUserName}</td>
					<td>${financeQuitMoney.financePaymentInfo.commMobile}</td>
					<td><fmt:formatDate value="${financeQuitMoney.financePaymentInfo.payDate}" pattern="yyyy-MM-dd" /></td>
					<td>${financeQuitMoney.financePaymentInfo.paymentMethodName}</td>
					<td><fmt:formatNumber value="${financeQuitMoney.quitMoney }" type="number" /></td>
					<td><fmt:formatDate value="${financeQuitMoney.quitDate }" pattern="yyyy-MM-dd" /></td>
					<td width="80px" style="word-break: break-all">${financeQuitMoney.quitReason }</td>
					<td>${financeQuitMoney.quitWindow }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>