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
		<li ><a href="${ctx}/work/workLog/list">项目客服统计</a></li>
		<li class="active"><a href="${ctx}/work/workLog/rclist">日常客服统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/workLog/rclist" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<div style="margin-top: 9px">
		客服人员：<input type="text" name ="name" id="name" value="${name}">
		<label>应用名称:</label>
		<select name="appId" id="appId">
			<option value="" >请选择</option>
			<c:forEach items="${apps}" var="app">
				<option value="${app.id}" <c:if test="${app.id==appId }">selected="selected"</c:if>>${app.appName }</option>
				
			</c:forEach>
		</select>
		</div>
		<div style="margin-top:9px;">
		<label>服务时间:</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align:center">客服接入</th>
				<th colspan="9" style="text-align:center">业务咨询</th>
				<th rowspan="2" style="text-align:center">业务系统</th>
				<th rowspan="2" style="text-align:center">业务操作</th>
				<th rowspan="2" style="text-align:center">其他</th>
			</tr>
			<tr>
				<th style="text-align:center">新办</th>
				<th style="text-align:center">更新</th>
				<th style="text-align:center">解锁</th>
				<th style="text-align:center">变更</th>
				<th style="text-align:center">补办</th>
				<th style="text-align:center">用途</th>
				<th style="text-align:center">密码</th>
				<th style="text-align:center">授权</th>
				<th style="text-align:center">合作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${access_count}" var="access_count">
			<tr>
				<td style="text-align:center">${access_count.key}</td>
				<c:forEach items="${access_count.value}" var="acv">
				<td style="text-align:center">${acv}</td>
				</c:forEach>
			</tr>
		</c:forEach>
		<tr>
			<td style="text-align:center">总计</td>
			<c:forEach items="${zj }" var="zj">
					<td style="text-align:center">${zj}</td>
			</c:forEach>
		</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
