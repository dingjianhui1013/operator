<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>年项目统计</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function addConfigProjectType() {
		var configProjectTypeId = $("#configProjectTypeId").prop('value');
		var url = "${ctx}/work/workDealInfo/addConfigProjectType?configProjectTypeId=";
		$.getJSON(url + configProjectTypeId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择项目名称</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#appId").html(html);
		});

	}
	function dc()
	{
		var configProjectTypeId=$("#configProjectTypeId").val();
		var appId=$("#appId").val();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		window.location.href="${ctx}/work/workDealInfo/exportYearProjectList?configProjectTypeId="+configProjectTypeId+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime;
	}
	function changeEndTime()
	{
		$("#endTime").val($("#startTime").val());
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/statisticalYearProjectList">年项目统计表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configProjectType"
		action="${ctx}/work/workDealInfo/statisticalYearProjectList" method="post"
		class="breadcrumb form-search">
<%-- 		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" /> --%>
<!-- 		<input id="pageSize" name="pageSize" type="hidden" -->
<%-- 			value="${page.pageSize}" /> --%>
		<div>
		<label>项目类型：</label>
			<select name="configProjectTypeId" id="configProjectTypeId" onchange="addConfigProjectType()">
				<option value="">请选择项目类型</option>
				<c:forEach items="${configProjectTypes}" var="configProjectTypes">
					<option value="${configProjectTypes.id}" <c:if test="${configProjectTypes.id==configProjectTypeId}">selected="selected"</c:if>>${configProjectTypes.projectName}</option>
				</c:forEach>
			</select>
		<label>项目名称 ：</label>
		<select name="appId" id="appId">
				<option value="">请选择项目名称</option>
				<c:forEach items="${appList}" var="appList">
					<option value="${appList.id}"  <c:if test="${appList.id==appid}">selected="selected"</c:if>>${appList.appName}</option>
				</c:forEach>
			</select>
		<label>年份：</label>
			<input id="startTime" name="startTime" class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy"/>" maxlength="20" readonly="readonly" onblur="changeEndTime()"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="hidden" value="<fmt:formatDate value="${startTime}" pattern="yyyy"/> " name="endTime" id="endTime"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		&nbsp;&nbsp;&nbsp;&nbsp;<input id="exportYP" style="text-align:center" class="btn btn-primary" onclick="dc()" type="button" value="导出">
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 20px">序号</th>
				<th colspan="2">项目</th>
					<c:set var="month" value="0"/>
				<c:forEach items="${months}" var="months">
					<c:set var="month" value="${month+1}"/>
					<th>${month}月</th>
				</c:forEach>
				<th>
				<fmt:formatDate value="${startTime}" pattern="yyyy"/>年合计</th>
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${w_months}" var="w_months" varStatus="status">
		<tr>
		<td>${status.count}</td>
			<td>${w_months.key.configApp.configProjectType.projectName}</td>
			<td>${w_months.key.configApp.appName}</td>
			<c:forEach items="${w_months.value}" var="w_mv">
				<td>${w_mv}</td>
			</c:forEach>
		</tr>
		</c:forEach>
		<tr>
			<td></td>
			<td colspan="2">项目收入合计</td>
			<c:forEach items="${zj}" var="zjmoney">
				<td>${zjmoney}</td>
			</c:forEach>
			<c:if test="${fn:length(zj)==0}">
				<td>0.0</td>
			</c:if>
		</tr>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>
