<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>项目调整统计</title>
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
	function dc()
	{
		var company=$("#company").val();
		var companyName=$("#companyName").val();
		var dkstartTime=$("#dkstartTime").val();
		var dkendTime=$("#dkendTime").val();
		var zzstartTime=$("#zzstartTime").val();
		var zzendTime=$("#zzendTime").val();
		window.location.href="${ctx}/work/workDealInfo/exportAdjustment?company="+company+"&companyName="+companyName+"&dkstartTime="+dkstartTime+"&dkendTime="+dkendTime
				+"&zzstartTime="+zzstartTime+"&zzendTime="+zzendTime;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/statisticalAdjustment">项目调整统计表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="financePaymentInfo"
		action="${ctx}/work/workDealInfo/statisticalAdjustment" method="post"
		class="breadcrumb form-search">
<%-- 		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" /> --%>
<!-- 		<input id="pageSize" name="pageSize" type="hidden" -->
<%-- 			value="${page.pageSize}" /> --%>
		<div>
		<label>单位名称 ：</label>
		<form:input path="company" htmlEscape="false"
			maxlength="50" class="input-medium" id="company"/>
		<label>应用名称 ：</label>
		<select name="companyName" id="companyName">
				<option value="">请选择</option>
			<c:forEach items="${companys}" var="companys">
				<option value="${companys.appName}"
					<c:if test="${companys.appName==companyName}">
						selected="selected"
					</c:if>>${companys.appName}</option>
			</c:forEach>
		</select>
<%-- 		<input type="text" value="${companyName}" name="companyName" class="input-medium" id="companyName"/> --%>
		<label>到款日期 ：</label>
		<input id="dkstartTime" class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								value="<fmt:formatDate value="${dkstartTime}" pattern="yyyy-MM-dd"/>"
								maxlength="20" readonly="readonly" name="dkstartTime" />至
		<input id="dkendTime" class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
								value="<fmt:formatDate value="${dkendTime}" pattern="yyyy-MM-dd"/>"
								maxlength="20" readonly="readonly" name="dkendTime" />
		</div>
		<div style="margin-top: 8px">
		<label>制证日期 ：</label>
		<input id="zzstartTime" class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								value="<fmt:formatDate value="${zzstartTime}" pattern="yyyy-MM-dd"/>"
								maxlength="20" readonly="readonly" name="zzstartTime" />至
		<input id="zzendTime" class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
								value="<fmt:formatDate value="${zzendTime}" pattern="yyyy-MM-dd"/>"
								maxlength="20" readonly="readonly" name="zzendTime" />
		&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
			&nbsp;&nbsp;&nbsp;&nbsp;<input id="exportZS" style="text-align:center" class="btn btn-primary" onclick="dc()" type="button" value="导出">
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 200px">支付款项录入时间</th>
				<th>业务使用款项</th>
				<th>支付款项单位名称</th>
				<th>应用名称</th>
				<th style="width: 200px">业务制证时间</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${workpaymentInfo_dealinfoVo}" var="p_d">
			<tr>
				<td>${p_d.dealPayDate}</td>
				<td>${p_d.payMoney}</td>
				<td>${p_d.companyName}</td>
				<td>${p_d.aliasName}</td>
				<td>${p_d.signDate}</td>
				<td>${p_d.remarks}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>
