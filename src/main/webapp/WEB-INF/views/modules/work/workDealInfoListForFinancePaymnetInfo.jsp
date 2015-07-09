<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
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
		<li class="active"><a href="${ctx}/work/workDealInfo/financeList?financePaymentInfoId=${financePaymentInfoId}">绑定信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/financeList?financePaymentInfoId=${financePaymentInfoId}" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>收费流水号</th>
				<th>绑定单位名称</th>
				<th>应用名称</th>
				<th>金额</th>
				<th>证书类型</th>
				<th>业务类型</th>
				<th>付款时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td>${workDealInfo.workPayInfo.sn}</td>
					<td>${workDealInfo.workCompany.companyName}</td>
					<td>${workDealInfo.configApp.appName}</td>
					<td>${workDealInfo.moneyDouble}</td>
					<td>${pro[workDealInfo.configProduct]}</td>
					<td>${infoType[workDealInfo.dealInfoType]}&nbsp; ${infoType[workDealInfo.dealInfoType1]}&nbsp; ${infoType[workDealInfo.dealInfoType2]}&nbsp;${infoType[workDealInfo.dealInfoType3]}</td>
					<td>
					<fmt:formatDate value="${workDealInfo.workPayInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
