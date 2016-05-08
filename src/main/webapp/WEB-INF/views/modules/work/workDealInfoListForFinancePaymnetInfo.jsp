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
	<form:form id="searchForm" class="breadcrumb form-search" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/financeList?financePaymentInfoId=${financePaymentInfoId}" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	<div style="margin-top:9px">
	<lable>选择应用：</lable>
	<select name="appName">
		<option value="">请选择</option>
		<c:forEach items="${appNames}" var="appNames">
			<option value="${appNames.appName }">${appNames.appName }</option>
		</c:forEach>
	</select>
	
	
	<label>付款时间：</label>

		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
					&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp; <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th colspan="7">总共 ${count} 数据</th>
		</tr>
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
					<td>${workDealInfo.certSn}</td>
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
