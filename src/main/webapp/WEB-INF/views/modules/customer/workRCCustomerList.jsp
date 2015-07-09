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
				<option value="${app.id }">${app.appName }</option>
			</c:forEach>
		</select>
		</div>
		<div style="margin-top:9px;">
		<label>服务时间:</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>客服接入</th>
				<th>业务咨询</th>
				<th>环境</th>
				<th>驱动</th>
				<th>key</th>
				<th>网络</th>
				<th>更新操作</th>
				<th>解锁</th>
				<th>业务系统</th>
				<th>业务操作</th>
				<th>其他</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${logList}" var="log">
			<tr>
				<td>${log.CUSTOMER_SERVICE_ACCESS}</td>
				<td>${log.ZX}</td>
				<td>${log.HJ}</td>
				<td>${log.QD}</td>
				<td>${log.KY}</td>
				<td>${log.WL}</td>
				<td>${log.GX}</td>
				<td>${log.JS}</td>
				<td>${log.XT}</td>
				<td>${log.CZ}</td>
				<td>${log.QT}</td>
			</tr>
		</c:forEach>
		<tr>
			<td>总计</td>
			<td>
				<c:forEach items="${logList}" var="log">
					<c:set var="zxsum" value="${zxsum+log.ZX }"></c:set>	
					<c:set var="hjsum" value="${hjsum+log.HJ }"></c:set>	
					<c:set var="qdsum" value="${qdsum+log.QD }"></c:set>	
					<c:set var="kysum" value="${kysum+log.KY }"></c:set>	
					<c:set var="wlsum" value="${wlsum+log.WL }"></c:set>	
					<c:set var="gxsum" value="${gxsum+log.GX }"></c:set>	
					<c:set var="jssum" value="${jssum+log.JS }"></c:set>	
					<c:set var="xtsum" value="${xtsum+log.XT }"></c:set>	
					<c:set var="czsum" value="${czsum+log.CZ }"></c:set>	
					<c:set var="qtsum" value="${qtsum+log.QT }"></c:set>	
				</c:forEach>
				${zxsum }
			</td>
			<td>${hjsum }</td>
			<td>${qdsum }</td>
			<td>${kysum }</td>
			<td>${wlsum }</td>
			<td>${gxsum }</td>
			<td>${jssum }</td>
			<td>${xtsum }</td>
			<td>${czsum }</td>
			<td>${qtsum }</td>
		</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
