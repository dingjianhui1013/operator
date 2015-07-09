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
		<li><a href="${ctx}/work/workDealInfoAudit/list">鉴证业务</a></li>
		<shiro:hasPermission name="userenroll:trustdevice:audit">
		<li class="active"><a href="${ctx}/work/workDealInfoAudit/listTrustCountApply">审核移动设备数量</a></li>
		</shiro:hasPermission>
		<li ><a href="${ctx}/work/workDealInfoAudit/exceptionList?dealInfoStatus=1">异常业务</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workCertTrustApply" action="${ctx}/work/workDealInfoAudit/listTrustCountApply" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>Key序列号:</label><form:input path="keySn" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>证书序列号:</label><form:input path="certSn" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>业务编号</th>
				<th>key序列号</th>
				<th>证书序列号</th>
				<th>申请数量</th>
				<th>申请时间</th>
				<th>业务状态</th>
				<th>操作</th>
		<tbody>
		<c:forEach items="${page.list}" var="apply" varStatus="status">
			<tr>
				<td>${apply.sn}</td>
				<td>${apply.keySn}</td>
				<td>${apply.certSn }</td>
				<td>${apply.applyCount}</td>
				<td>
					<fmt:formatDate value="${apply.applyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<c:if test="${apply.status==0 }">待审核</c:if>
					<c:if test="${apply.status==1 }">审核通过</c:if>
					<c:if test="${apply.status==-1 }">审核拒绝</c:if>
				</td>
				<td>
					<c:if test="${apply.status==0 }">
						<a href="${ctx}/work/workCertTrustApply/auditFrom?id=${apply.id}">审核</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
