<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>统计信息</title>
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
	function addOffice() {
		var areaId = $("#area").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#office").html(html);
		});

	}
	function sub()
	{
		var appId = $("#appId").val();
		var startTime = $("#startTime").val();
		if(appId=="")
			{
				top.$.jBox.tip("请选择项目");
				return false;
			}
		if(startTime=="")
			{
				top.$.jBox.tip("请选择统计时间");
				return false;
			}
		return true;
	}
	function changeEndTime()
	{
		var startTime=$("#startTime").val();
		$("#endTime").val(startTime);
		
	}
	function dc()
	{
		var area = $("#area").val();
		var appId = $("#appId").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		window.location.href="${ctx}/work/workDealInfo/exportMonthPayment?area="+area+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/StatisticalMonthList">月回款统计</a></li>
	</ul>
	
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/statisticalMonthList" method="post"
		class="breadcrumb form-search">
		<div style="margin-top: 9px">
		<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选择区域 ：</label>
		<select name="area" id="area" onchange="addOffice()">
			<option value="">请选择</option>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==area}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>项目名称：</label>
		<select name="appId" id="appId">
				<option value="">请选择</option>
			<c:forEach items="${appList}" var="app">
				<option value="${app.id}"
					<c:if test="${app.id==appId}">
					selected="selected"
					</c:if>>${app.appName}</option>
			</c:forEach>
		</select>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<label>统计时间&nbsp;：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM"/>" onblur="changeEndTime()"/>
					&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;
			<input type="hidden" value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM"/>" id="endTime" name="endTime"/>
			<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return sub()"
			value="查询" />
			<a href="javascript:dc()" class="btn btn-primary">导出</a>
		</div>
	</form:form>
		<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>统计日期</th>
				<th>项目名称</th>
				<c:forEach items="${payMethod}" var="Method">
						<th>${Method}</th>
				</c:forEach>
				<c:forEach items="${offices}" var="office">
					<c:if test="${office.value>0}">
						<th>${office.key}</th>
					</c:if>
				</c:forEach>
				<th>总计</th>
			</tr>  
		</thead>
		<tbody>
				<tr>
					<td><fmt:formatDate value="${startTime}" pattern="yyyy-MM"/></td>
					<td>${appName }</td>
					<c:forEach items="${money}" var="money">
						<td>${money}</td>
					</c:forEach>
					<c:forEach items="${offices}" var="office">
					<c:if test="${office.value>0}">
						<td>${office.value}</td>
					</c:if>
				</c:forEach>
					<td>${countMoneys}</td>
				</tr>
		</tbody>
		</table>
</body>
</html>
