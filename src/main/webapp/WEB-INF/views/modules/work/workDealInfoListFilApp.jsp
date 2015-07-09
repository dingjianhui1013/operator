<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>业务办理管理</title>
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
		<li ><a href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">单位基本信息查看</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/userlist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}">联系人列表</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/proApplist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}">应用产品列表</a></li>
		<li class="active"><a href="${ctx}/work/workDealInfoFiling/applist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}">业务办理信息</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/loglist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}">客服记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo" action="${ctx}/work/workDealInfoFiling/applist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>业务编号</th>
				<th>应用名称</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>业务状态</th>
				<th>完成时间</th>
				<th>证书经办人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workDealInfo">
			<tr>
				<td>${workDealInfo.svn}</td>
				<td>${workDealInfo.configApp.appName}</td>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}</td>
				<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
				<td><fmt:formatDate value="${workDealInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${workDealInfo.workUserHis.contactName}</td>
				<td>
    				<a href="${ctx}/work/workDealInfoFiling/fromProApp?id=${workDealInfo.id}">查看</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
