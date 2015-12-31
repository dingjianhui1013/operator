<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>查看短信返回信息管理</title>
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
		function alarmValue(checkMessageId){
			
			var url = "${ctx}/message/messageSending/showWorkDeal?checkMessageId="+checkMessageId;
			top.$.jBox.open("iframe:"+url, "查看", 800, 200, {
					buttons:{"关闭":true}, submit:function(v, h, f){
						
					}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置</a></li>
		<li><a href="${ctx}/message/messageSending/list">消息发送</a></li>
		<li class="active"><a href="${ctx}/message/messageSending/search">消息查看</a></li>
		<li><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>
	</ul>
	 <form:form id="searchForm" modelAttribute="messageSending" action="${ctx}/message/messageSending/search" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		<label>应用名称：</label> <select name="apply" id="apply">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select>
			 <label>单位名称 ：</label>
		<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName"/>
				<label>经办人名称：</label>
		<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="16" class="input-medium" id="contactName"/> 
				</div>
				<br>
				<label>消息状态  ：</label>
				<form:select path="returnStatus">
				<form:option value="">请选择</form:option>
				<form:option value="1">成功</form:option>
				<form:option value="0">失败</form:option>
				</form:select>
				<label>发送时间：</label>
				<input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				
				<th>应用名称</th>
				<th>单位名称</th>
				<th>经办人名称</th>
				<th>发送时间</th>
				<th>模板名称</th>
				<th>消息状态</th>
				<th>操作</th>
			
			</tr>	
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="checkMessage">
			<tr>
					
					
					<td>${checkMessage.configApp.alias}</td>
					<td>${checkMessage.workCompany.companyName}</td>
					<td>${checkMessage.workUser.contactName}</td>
					<td>${checkMessage.smsSendDate }</td>
					<td>${checkMessage.smsConfiguration.messageName}</td>
					<td>
					<c:if test="${checkMessage.returnStatus==1}">成功</c:if>
					<c:if test="${checkMessage.returnStatus==0}">失败 ；原因：${checkMessage.returnStatus}</c:if>
					</td>
					
					<td><a href="javaScript:alarmValue( ${checkMessage.id} )">查看 </a></td>
				</tr>
		</c:forEach>
		</tbody>
	</table> 
	<div class="pagination">${page}</div>
</body>
</html>
