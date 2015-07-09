<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
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
		<li class="active"><a href="${ctx}/profile/configCommercialAgent/">代理商列表</a></li>
		<li><a href="${ctx}/profile/configCommercialAgent/insertFrom">代理商添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configCommercialAgent" action="${ctx}/profile/configCommercialAgent/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="agentName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>代理商名称</th>
				<th>代理商类型</th>
				<th>联系人</th>
				<th>电话号码</th>
				<th>代理商地址</th>
				<th>代理合同有效期</th>
				<th width="140px">备注</th>
				<th>操作</th>
			</thead>
		<tbody>
		<c:forEach items="${page.list}" var="configCommercialAgent">
			<tr>
				<td style="word-wrap:break-word;word-break:break-all;" width="150"><a href="${ctx}/profile/configCommercialAgent/updateFrom?id=${configCommercialAgent.id}">${configCommercialAgent.agentName}</a></td>
				<td>
					<c:if test="${configCommercialAgent.agentType1==true}">市场推广 </c:if>
					<c:if test="${configCommercialAgent.agentType2==true}"> 劳务关系</c:if>
				</td>
				<td>${configCommercialAgent.agentCommUserName}</td>
				<td>${configCommercialAgent.agentCommMobile}</td>
				<td style="word-wrap:break-word;word-break:break-all;" width="200">${configCommercialAgent.agentAddress}</td>
				<td>
				<fmt:formatDate value="${configCommercialAgent.agentContractStart}" pattern="yyyy-MM-dd"/>
				至
				<fmt:formatDate value="${configCommercialAgent.agentContractEnd}" pattern="yyyy-MM-dd"/></td>
				<td width="140px" style="word-break: break-all">${configCommercialAgent.agentRemark}</td>
				<td>
    				<a href="${ctx}/profile/configCommercialAgent/updateFrom?id=${configCommercialAgent.id}">修改</a>
					<a href="${ctx}/profile/configCommercialAgent/delete?id=${configCommercialAgent.id}" onclick="return confirmx('确认要删除该代理商吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
