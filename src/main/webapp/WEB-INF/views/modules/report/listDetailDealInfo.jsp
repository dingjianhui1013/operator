<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.input-medium {
	width: 206px;
}
</style>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	function exportDetail(){
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		var appId=$("#appId").val();
		var method=$("#method").val();
		
		
		window.open("${ctx}/report/businessReport/exportDetail?startTime="+startTime+"&endTime="+endTime+"&appId="+appId+"&method="+method);
	}
	
	
	function returnList(){
		
		loading('正在提交，请稍等...');
		
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		
		window.location.href = "${ctx}/report/businessReport/listByDate?startTime="+startTime+"&endTime="+endTime;
	}
	
</script>



</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/report/businessReport/listByDate">证书发放汇总列表</a></li>
		<shiro:hasPermission name="report:businessReport:view">
		<li class="active"><a href="#">证书发放详情列表</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/report/businessReport/listDetailDealInfo" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
			
		<input id="appId" name="appId" type="hidden" value="${appId}" />
		<input id="startTime" name="startTime" type="hidden" value="${startTime}" />
		<input id="endTime" name="endTime" type="hidden" value="${endTime}" />
		<input id="method" name="method" type="hidden" value="${method}" />
			
		
		<div>
			<label>&nbsp;&nbsp;应用名称 ：&nbsp;&nbsp;<b>${appName }</b></label>
			<label>&nbsp;&nbsp;时间范围 ：&nbsp;&nbsp;<b>${startTime } - ${endTime }</b></label>&nbsp;&nbsp;&nbsp;&nbsp;
			<input style="text-align:center" class="btn btn-info" onclick="exportDetail()" type="button" value="导出">
			&nbsp;&nbsp;
			<input class="btn btn-primary" type="button" onclick="returnList()"  value="返回" />
		</div>

	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>


				<th>单位名称</th>
				<th>经办人</th>
				<th>制证日期</th>
				<th>业务类型</th>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>


					<td>${workDealInfo.workCompany.companyName}</td>
					<td>${workDealInfo.workUserHis.contactName }</td>
					<td><fmt:formatDate
							value="${workDealInfo.businessCardUserDate}"
							pattern="yyyy-MM-dd" /></td>
					<td><c:if test="${not empty workDealInfo.dealInfoType}">${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;</c:if>
					<c:if test="${not empty workDealInfo.dealInfoType1}">${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;</c:if>
					<c:if test="${not empty workDealInfo.dealInfoType2}">${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;</c:if>
					<c:if test="${not empty workDealInfo.dealInfoType3}">${wdiType[workDealInfo.dealInfoType3]}&nbsp;&nbsp;</c:if></td>




				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="pagination">${page}</div>



</body>
</html>
