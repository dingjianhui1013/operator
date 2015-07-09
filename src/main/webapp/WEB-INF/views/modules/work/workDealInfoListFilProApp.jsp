<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>应用产品列表</title>
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
		<li class="active"><a href="#">应用产品列表</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/applist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}">业务办理信息</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/loglist?comId=${workDealInfo.workCompany.id}&id=${workDealInfo.id}">客服记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo" action="${ctx}/work/workDealInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>应用名称</th>
				<th>产品名称</th>
				<th>产品状态</th>
				<th>服务有效期</th>
				<th>联系人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workDealInfo">
			<tr>
				<td>${workDealInfo.configApp.appName}</td>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>
					<c:set var="productDate">
						<fmt:formatDate value="${workDealInfo.workCertInfo.notafter}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</c:set>
					<c:set var="nowDate">
						<fmt:formatDate value="${now_date}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</c:set>
					<c:if test="${workDealInfo.status ==6 }">
						已吊销
					</c:if>
					<c:if test="${workDealInfo.status != 6}">
						<c:if test="${productDate le nowDate }">
							已过期
						</c:if>	
						<c:if test="${productDate ge nowDate }">
							有效
						</c:if>	
					</c:if>
				</td>
				<td>
					<fmt:formatDate value="${workDealInfo.workCertInfo.notbefore}" pattern="yyyy-MM-dd HH:mm:ss"/>－
					<fmt:formatDate value="${workDealInfo.workCertInfo.notafter}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${workDealInfo.workUser.contactName}</td>
				<td>
    				<a href="${ctx}/work/workDealInfoFiling/fromApp?id=${workDealInfo.id}">查看</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
