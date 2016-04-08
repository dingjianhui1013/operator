<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>邮箱提取管理</title>
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
		function dca() {
			
				
					var apply = $("#apply").val();
					var workType = $("#workType").val();
					var dealInfoStatus = $("#dealInfoStatus").val();
					var companyName=$("#companyName").val();
					
					window.location.href = "${ctx }/message/emailExtraction/export?apply="
							+ apply
							+ "&workType="
							+ workType
							+ "&dealInfoStatus="
							+ dealInfoStatus
							+"&companyName="
							+companyName;
				}
			
		function onSubmit() {
			
				if ($("#apply").val() == "") {
					top.$.jBox.tip("请选择应用");
					return false;
				} else {
					return true;
				}
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置</a></li>
		<li><a href="${ctx}/message/messageSending/list">消息发送</a></li>
		<li><a href="${ctx}/message/messageSending/search">消息查看</a></li>
		<li class="active"><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="emailExtraction" action="${ctx}/message/emailExtraction/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
	
		<div>
			<label>代办应用：</label> 
				<select name="apply"
					id="apply">
					<option value="">请选择应用</option>
					<c:forEach items="${configAppList}" var="app">
						<option value="${app.id}"
							<c:if test="${app.id==applyId}">
						selected="selected"
						</c:if>>${app.appName}</option>
					</c:forEach>
				</select>
			<label>单位名称  ：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName"/>
				<br>
				<br>
				<label>业务类型：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> 
				<label>业务状态 ：</label>
			<select name="dealInfoStatus" id="dealInfoStatus">
				<option value="">请选择业务类型</option>
				<c:forEach items="${wdiStatus}" var="type">
					<option value="${type.key}"
						<c:if test="${type.key==wdiStatu}">
					selected="selected"
					</c:if>>${type.value}</option>
				</c:forEach>
			</select> 	
			<br>
			<br>
			<label>制证时间：</label> 
		
		<input id="makeCertStart" name="makeCertStart"	type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value="${makeCertStart}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="makeCertEnd" name="makeCertEnd" type="text"  readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'makeCertStart\')}'});"
				value="<fmt:formatDate value="${makeCertEnd}" pattern="yyyy-MM-dd"/>" />
		
		
		<label>到期时间：</label> 
		
		<input id="expiredStart" name="expiredStart"	type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value="${expiredStart}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="expiredEnd" name="expiredEnd" type="text"  readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'expiredStart\')}'});"
				value="<fmt:formatDate value="${expiredEnd}" pattern="yyyy-MM-dd"/>" />
			
					
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return onSubmit();" value="查询"/>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:dca()" class="btn btn-primary">导出</a>
		</div>
		</form:form>
		<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>单位名称</th>
				<th>应用名称</th>
				<th>经办人名称</th>
				<th>经办人邮箱</th>
				<th>证书持有人</th>
				<th>证书持有人邮箱</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					
					
					<td>${workDealInfo.workCompany.companyName}</td>
					<td>${workDealInfo.configApp.alias}</td>
					<td>${workDealInfo.workCertInfo.workCertApplyInfo.name}</td>
					<td>${workDealInfo.workCertInfo.workCertApplyInfo.email}</td>
					<td>${workDealInfo.workUser.contactName}</td>
					<td>${workDealInfo.workUser.contactEmail}</td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
