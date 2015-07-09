<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>消息管理</title>
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
		<li class="active"><a href="${ctx}/profile/configMsg/">消息列表</a></li>
		<shiro:hasPermission name="profile:configMsg:edit"><li><a href="${ctx}/profile/configMsg/form">消息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configMsg" action="${ctx}/profile/configMsg/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="msgName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
		<th>序号</th>
		<th>消息名称</th>
		<th>消息主题</th>
		<th>消息类型</th>
		<th>消息状态</th>
		<th>发送方式</th>
		<th>发送时间</th>
		<th>发送内容</th>
		<shiro:hasPermission name="profile:configMsg:edit"><th>操作</th></shiro:hasPermission></tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="configMsg" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td><a href="${ctx}/profile/configMsg/form?id=${configMsg.id}">${configMsg.msgName}</a></td>
				<td>${configMsg.msgTitle}</td>
				<td>
				<c:if test="${configMsg.msgType==0}">key库存提醒</c:if>
				<c:if test="${configMsg.msgType==1}">发票库存提醒</c:if>
				</td>
				<td>
				<c:if test="${configMsg.delFlag==0}">
					启用中
				</c:if>
				<c:if test="${configMsg.delFlag==1}">
					已停用
				</c:if>
				</td>
				<td>
				<c:if test="${configMsg.msgSendMethod==0}">
					邮件
				</c:if>
				<c:if test="${configMsg.msgSendMethod==1}">
					短信
				</c:if>
				</td>
				<td>
				<c:if test="${configMsg.msgSendTimeType==0}">
					每天8点
				</c:if>
				<c:if test="${configMsg.msgSendTimeType==1}">
					立即
				</c:if>
				</td>
				<td width="80px" style="word-break: break-all">${configMsg.msgContent}</td>
				<shiro:hasPermission name="profile:configMsg:edit"><td>
    				<a href="${ctx}/profile/configMsg/form?id=${configMsg.id}">修改</a>
    				<c:if test="${configMsg.delFlag==0}">
					<a href="${ctx}/profile/configMsg/delete?id=${configMsg.id}" onclick="return confirmx('确认要停用该消息吗？', this.href)">停用</a>
					</c:if>
					<c:if test="${configMsg.delFlag==1}">
					<a href="${ctx}/profile/configMsg/delete?id=${configMsg.id}" onclick="return confirmx('确认要启用该消息吗？', this.href)">启用</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
