<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>工作记录</title>
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
		<li class="active"><a href="${ctx}/work/customer/list">工作记录列表</a></li>
		<li ><a href="${ctx}/work/customer/insertCustomerFrom">新增用户</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workCompany" action="${ctx}/work/customer/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>单位名称 ：</label><form:input path="companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>联系人姓名 ：</label><form:input path="legalName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>固定电话 ：</label><form:input path="companyMobile" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>手机号码 ：</label><form:input path="comPhone" htmlEscape="false" maxlength="50" class="input-medium"/>
		
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>用户名称</th>
				<th>用户类型</th>
				<th>服务级别</th>		
				<th>操作</th>		
		</tr></thead>
		<tbody>
		<c:forEach items="${pagec.list}" var="workCompany">
			<tr>
				<td>${workCompany.id }</td>
				<td><a href="${ctx}/work/customer/form?id=${workCompany.id}">${workCompany.companyName}</a></td>
				<td>${workCompany.companyType}</td>
				<td>${workCompany.selectLv}</td>
				<td>${workCompany.city}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>记录编号</th>
				<th>用户名称</th>
				<th>服务类型</th>
				<th>服务主题</th>		
				<th>详细记录</th>		
				<th>记录人员</th>		
				<th>记录时间</th>		
				<th>操作</th>		
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="workLog">
			<tr>
				<td>${workLog.id }</td>
				<td>${workLog.workCompany.companyName}</td>
				<td>${workLog.serType}</td>
				<td>${workLog.serTitle}</td>
				<td>${workLog.creatTime}</td>
				<td>${workLog.leftoverProblem}</td>
				<td>${workLog.creatTime}</td>
				<td><a href="#">操作</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
