<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>工作记录管理</title>
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
		<li ><a href="${ctx}/work/workDealInfoFiling/formF?uid=${user.id}">单位基本信息查看</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/userlist?uid=${user.id}">联系人列表</a></li>
		<li class="active"><a href="#">客服记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/workDealInfoFiling/loglist?uid=${user.id}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<shiro:hasPermission name="work:customer:addKFJL">
	<input class="btn btn-primary" type="button"
		onclick="window.location.href='${ctx}/work/customer/insertCustomerFromF?id=${user.id}'" value="新增客服记录" />
	</shiro:hasPermission>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>记录编号</th>
				<th>服务类型</th>
				<th>服务主题</th>
				<th>详细记录</th>
				<th>完成状态</th>
				<th>记录人员</th>
				<th>记录时间</th>
			</tr>
		<tbody>
		<c:forEach items="${page.list}" var="workLog">
			<tr>
				<td>${workLog.id}</td>
				<td>${workLog.serType}</td>
				<td>${workLog.serTitle}</td>
				<td width="80px" style="word-break: break-all">${workLog.recordContent}</td>
				<td>
					<c:if test="${workLog.completeType==1 }">未完成</c:if>
					<c:if test="${workLog.completeType==0 }">已完成</c:if>
				</td>
				<td>${workLog.createBy.name}</td>
				<td>${workLog.creatTime}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
