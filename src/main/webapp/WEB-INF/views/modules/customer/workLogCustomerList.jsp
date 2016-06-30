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
		<li class="active"><a href="${ctx}/work/workLog/list">项目客服统计</a></li>
		<li ><a href="${ctx}/work/workLog/rclist">日常客服统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workLog" action="${ctx}/work/workLog/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		客服人员：<input type="text" name ="name" id=" name" value="${name}">
		服务时间：
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>应用名称</th>
				<th>日常客服</th>
				<th>温馨提示</th>
				<th>更新提示</th>
				<th>回访</th>

				<!-- <th>提示</th> -->

				<th>培训</th>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${logList}" var="log">
			<tr>
				<td>${log.DEALINFOID }</td>
				<td>${log.RC}</td>
				<td>${log.WX}</td>
				<td>${log.GX}</td>
				<td>${log.HF}</td>

				<%-- <td>${log.ts}</td> --%>

				<td>${log.PX}</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
