<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信配置管理</title>
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
		<li class="active"><a href="${ctx}/message/smsConfiguration/">短信配置列表</a></li>
		<li><a href="${ctx}/message/smsConfiguration/form">短信配置添加</a></li>
		<li><a href="${ctx}/message/smsConfiguration/form">消息发送</a></li>
		<li><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>
		
	</ul>
	<form:form id="searchForm" modelAttribute="smsConfiguration" action="${ctx}/message/smsConfiguration/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>短信模板名称 ：</label><form:input path="messageName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;&nbsp;
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>序号</th><th>短信模板名称</th><shiro:hasPermission name="message:smsConfiguration:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="smsConfiguration">
			<tr>
				<td>${smsConfiguration.id}</a></td>
				<td><a href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">${smsConfiguration.messageName}</a></td>
				<shiro:hasPermission name="message:smsConfiguration:edit"><td>
    				<a href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">修改</a>
					<a href="${ctx}/message/smsConfiguration/delete?id=${smsConfiguration.id}" onclick="return confirmx('确认要删除该短信配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
