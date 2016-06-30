<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>项目客服统计</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/work/customer/userStatic">咨询类用户统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/customer/userStatic" method="post" class="breadcrumb form-search">
		客服人员：<select name="userId">
				<option selected="selected" value="-1">所有客服</option>
				<c:forEach items="${users}" var="user">
					<option <c:if test="${user.ID == userId }">selected="selected"</c:if> value="${user.ID}">${user.NAME }</option>
				</c:forEach>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		服务时间：
		<input id="startTime" name="startDate" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;

				<input id="endTime" name="endDate" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startDate\')}'});"
			value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>客服接入（方式）</th>
				<th>业务咨询</th>
				<th>环境</th>
				<th>驱动</th>
				<th>key</th>
				<th>网络</th>
				<th>解锁</th>
				<th>更新操作</th>
				<th>业务系统问题</th>
				<th>业务操作问题</th>
				<th>其他</th>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${logList}" var="log">
			<tr>
				<td>${log.access }</td>
				<td>${log.YWZXcount}</td>
				<td>${log.HJcount}</td>
				<td>${log.QDcount}</td>
				<td>${log.KEYcount}</td>
				<td>${log.WLcount}</td>
				<td>${log.JScount}</td>
				<td>${log.GXCZcount}</td>
				<td>${log.YWXTcount}</td>
				<td>${log.YWCZcount}</td>
				<td>${log.QTcount}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
