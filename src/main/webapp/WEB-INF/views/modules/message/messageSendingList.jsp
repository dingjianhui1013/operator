<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息发送管理</title>
<meta name="decorator" content="default" />
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
		<li ><a href="${ctx}/message/smsConfiguration/">短信配置列表</a></li>
		<li><a href="${ctx}/message/smsConfiguration/form">短信配置添加</a></li>
		<li class="active"><a href="${ctx}/message/messageSending/list">消息发送</a></li>
		<li><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="messageSending"
		action="${ctx}/message/messageSending/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>

			<label>应用名称：</label> <select name="apply" id="apply">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName" />
		</div>
		<br>
		<div>
			<br>
			<br>&nbsp;&nbsp;&nbsp;&nbsp; <label>证书持有人：</label>
			<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="16" class="input-medium" id="contactName" />

			<label>业务类型：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select>
		</div>
		<div>
			<br>
			<br>&nbsp;&nbsp;&nbsp;&nbsp; <label>&nbsp;&nbsp;证书状态
				：&nbsp;&nbsp;</label> <select name="dealInfoStatus" id="dealInfoStatus">
				<option value="">请选择业务类型</option>
				<c:forEach items="${wdiStatus}" var="type">
					<option value="${type.key}"
						<c:if test="${type.key==wdiStatu}">
					selected="selected"
					</c:if>>${type.value}</option>
				</c:forEach>
			</select> <label>&nbsp;&nbsp;业务状态 ：&nbsp;&nbsp;</label> <select
				name="dealInfoStatus" id="dealInfoStatus">
				<option value="">请选择业务类型</option>
				<c:forEach items="${wdiStatus}" var="type">
					<option value="${type.key}"
						<c:if test="${type.key==wdiStatu}">
					selected="selected"
					</c:if>>${type.value}</option>
				</c:forEach>
			</select>

		</div>
		<div>
			<br>
			<br>&nbsp;&nbsp;&nbsp;&nbsp; <label>受理区域：</label><select
				name="area" id="area" onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==area}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> <label>受理网点：</label> <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select>
		</div>
		<br>
		
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
	</form:form>
	<tags:message content="${message}" />



	<div class="pagination">${page}</div>
</body>
</html>
