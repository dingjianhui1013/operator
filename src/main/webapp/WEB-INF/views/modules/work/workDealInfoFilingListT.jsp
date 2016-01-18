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
		<li class="active"><a href="${ctx}/work/workDealInfoFiling/ulist?distinguish=1">咨询类用户</a></li>
		<li><a href="${ctx}/work/customer/insertUser">新增用户</a></li>
	</ul>
<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/workDealInfoFiling/ulist?distinguish=1" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>服务对象 ：</label>
		
		<form:select path="configApp.id">
			<form:option value="">请选择服务对象</form:option>
			<c:forEach items="${appNames}" var="configApp">
				<form:option value="${configApp.id}">${configApp.appName}</form:option>
			</c:forEach>
		</form:select>
		
<%-- 		<form:input path="configApp.appName" htmlEscape="false" maxlength="50" class="input-medium"/> --%>
<%-- 		<label>服务主题 ：</label><form:input path="serTitle" htmlEscape="false" maxlength="50" class="input-medium"/> --%>
		<label>完成状态 ：</label>
		<form:select path="completeType"  class="input-medium">
			<form:option value="">请选择状态</form:option>
			<form:option value="1">未完成</form:option>
			<form:option value="0">已完成</form:option>
		</form:select>
		<label>接入方式：</label>
		<form:select path="access"  class="input-medium">
					<form:option value ="">请选择接入方式</form:option>
					<form:option value = "电话">电话</form:option>
					<form:option value = "QQ" >QQ</form:option>
					<form:option value = "QQ远程">QQ远程</form:option>
					<form:option value = "在线工具">在线工具</form:option>
					<form:option value = "邮件">邮件</form:option>
					<form:option value = "短信">短信</form:option>
					<form:option value = "其他">其他</form:option>
		</form:select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>服务对象</th>
				<th>服务主题</th>
				<th>客服接入</th>
				<th>联系方式</th>
				<th>详细记录</th>
				<th>完成状态</th>
				<th>记录人员</th>
				<th>记录时间</th>
				<th>遗留问题</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workLog">
			<tr>
<%-- 				<td><a href="${ctx}/work/workDealInfoFiling/formF?id=${workLog.workDealInfo.id}">${workLog.workCompany.companyName}</a></td> --%>
				<td>${workLog.configApp.appName}</td>
				<td>${workLog.serTitle}</td>
				<td>${workLog.access}</td>
				<td>${workLog.tel}</td>
				<td>${workLog.recordContent}</td>
				<td>
					<c:if test="${workLog.completeType==1 }">未完成</c:if>
					<c:if test="${workLog.completeType==0 }">已完成</c:if>
				</td>
				<td>${workLog.createBy.name }</td>
				<td><fmt:formatDate value="${workLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					${workLog.leftoverProblem}
				</td>
				<td>
					<c:if test="${workLog.completeType==1 }"><a href="${ctx}/work/workLog/updateFromFi?id=${workLog.id}">编辑</a></c:if> 
    				<a href="${ctx}/work/workLog/formFiling?id=${workLog.id}">查看</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
