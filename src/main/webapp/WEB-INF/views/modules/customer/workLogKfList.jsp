<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>客服记录</title>
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
		<li class="active"><a href="${ctx}/work/workLog/">客服记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/workLog/kflist" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>单位名称 ：</label><form:input path="workCompany.companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
<%-- 		<label>服务主题 ：</label><form:input path="serTitle" htmlEscape="false" maxlength="50" class="input-medium"/> --%>
		<label>客服接入 ：</label>
		<form:select path="access"  class="input-medium">
			<form:option value="">请选择接入方式</form:option>
			<form:option value="电话">电话</form:option>
			<form:option value="QQ">QQ</form:option>
			<form:option value="QQ远程">QQ远程</form:option>
			<form:option value="在线工具">在线工具</form:option>
			<form:option value="邮件">邮件</form:option>
			<form:option value="短信">短信</form:option>
			<form:option value="其他">其他</form:option>
		</form:select>
		<label>服务类型 ：</label>
		<form:select path="serType"  class="input-medium">
			<form:option value="">请选择服务类型</form:option>
			<form:option value="日常客服">日常客服</form:option>
			<form:option value="温馨提示">温馨提示</form:option>
			<form:option value="更新提示">更新提示</form:option>
			<form:option value="回访">回访</form:option>
			<form:option value="培训">培训</form:option>
		</form:select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>单位名称</th>
				<th>应用项目名称</th>
				<th>客服接入</th>
				<th>服务类型</th>
				<th>业务操作</th>
				<th>业务咨询</th>
				<th>业务系统</th>
				<th>完成状态</th>
				<th>记录人员</th>
				<th>记录时间</th>
				<th>记录方式</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workLog">
			<tr>
				<td><a href="${ctx}/work/workDealInfoFiling/formF?id=${workLog.workDealInfo.id}">${workLog.workCompany.companyName}</a></td>
				<td>${workLog.appName}</td>
				<td>${workLog.access }</td>
				<td>${workLog.serType }</td>
				<td>${workLog.ywcz }</td>
				<td>${workLog.ywzx }</td>
				<td>${workLog.ywxt }</td>
				<td>
					<c:if test="${workLog.completeType==1 }">未完成</c:if>
					<c:if test="${workLog.completeType==0 }">已完成</c:if>
				</td>
				<td>${workLog.createBy.name }</td>
				<td>
				<fmt:formatDate value="${workLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<c:choose> 
						<c:when test="${workLog.distinguish==1}">咨询处添加</c:when>
						<c:when test="${workLog.distinguish==0}">客服处添加</c:when>
					</c:choose>
				</td>
				<td>
					<c:if test="${workLog.completeType==1 }"><a href="${ctx}/work/workLog/updateFromK?id=${workLog.id}">编辑</a></c:if> 
    				<a href="${ctx}/work/workLog/form?id=${workLog.id}">查看</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
