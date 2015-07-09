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
		<li ><a href="${ctx}/work/workDealInfoAudit/list">鉴证业务</a></li>
		<shiro:hasPermission name="userenroll:trustdevice:audit">
		<li><a href="${ctx}/work/workDealInfoAudit/listTrustCountApply">审核移动设备数量</a></li>
		</shiro:hasPermission>
		<li class="active"><a href="${ctx}/work/workDealInfoAudit/exceptionList?dealInfoStatus=1">异常业务</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo" action="${ctx}/work/workDealInfoAudit/exceptionList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		<label>Key序列号：</label><form:input path="keySn" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>客户名称：</label><form:input path="workCompany.companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>联系人姓名：</label><form:input path="workUser.contactName" htmlEscape="false" maxlength="50" class="input-medium"/><br/>
		</div>
		<div style="margin-top: 8px">
		<label>&nbsp;&nbsp;固定电话：</label><form:input path="workUser.contactTel" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>&nbsp;&nbsp;&nbsp;手机号：</label><form:input path="workUser.contactPhone" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>用户名称</th>
				<th>用户类型</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>业务状态</th>
				<th>操作</th>
		<tbody>
		<c:forEach items="${page.list}" var="workDealInfo" varStatus="status">
			<tr>
				<td>${workDealInfo.svn }</td>
				<td><a href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.workCompany.companyName}</a></td>
				<td>
					<c:if test="${workDealInfo.workCompany.companyType==1}">企业</c:if>
					<c:if test="${workDealInfo.workCompany.companyType==2}">事业单位</c:if>
					<c:if test="${workDealInfo.workCompany.companyType==3}">政府机构</c:if>
				</td>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}</td>
				<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
				<td>
					<a href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}">编辑</a>
					<a href="${ctx}/work/workDealInfoOperation/make?id=${workDealInfo.id}">制证</a>

				

					<a href="${ctx}/work/workDealInfoAudit/delete?id=${workDealInfo.id}" onclick="return confirmx('确认要删除该异常业务信息吗？', this.href)">删除</a>

				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
