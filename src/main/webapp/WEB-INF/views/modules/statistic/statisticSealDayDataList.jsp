<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>印章日经营管理</title>
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
		function onSubmit(){
			if ($("#startTime").val()==""||$("#endTime").val()=="") {
				top.$.jBox.tip("请选定时间范围");
				return false;
			} else {
				if ($("#office").val()==""){
					top.$.jBox.tip("请选定网点");
					return false;
				} else {
					$("#searchForm").submit();
					//return true;
				}
			}
		}
		function dcCountDay(){
			if ($("#startTime").val()==""||$("#endTime").val()=="") {
				top.$.jBox.tip("请选定时间范围");
				return false;
			} else {
				if ($("#office").val()==""){
					top.$.jBox.tip("请选定网点");
				} else {
					var startTime = document.getElementById("startTime").value;
					var endTime = document.getElementById("endTime").value;
					var office = document.getElementById("office").value;
					window.location.href="${ctx}/statistic/statisticSealDayData/exportCountDay?startTime="+startTime+"&endTime="+endTime+"&office="+office;
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/statistic/statisticSealDayData/">印章日经营列表</a></li>
		<shiro:hasPermission name="statistic:statisticSealDayData:edit"><li><a href="${ctx}/statistic/statisticSealDayData/form">印章日经营添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="statisticSealDayData" action="${ctx}/statistic/statisticSealDayData/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<div>
			<label>选择日期：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			    &nbsp;-&nbsp;<input id="endTime" name="endTime" type="text" readonly="readonly"
				maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		<label>选择网点 ：</label>
		<select name="office" id="office">
				<option value="">请选择</option>
			<c:forEach items="${offices}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==office}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		&nbsp; &nbsp; &nbsp; &nbsp; <input
			id="btnSubmit" class="btn btn-primary" type="button" onclick="onSubmit()"
			 value="查询" />
			 <input id="exportCountDay" class="btn btn-primary" type="button" onclick="dcCountDay()" value="导出" />
			 </div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">日期</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">入库</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">总量</th>
				<th colspan="3" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth"> 日结</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">余量</th>
				
			</tr>
			 <tr>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">印章</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">费用</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
			</tr>			
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="statisticSealDayData">
			<tr>
				<td><fmt:formatDate value="${statisticSealDayData.statisticDate}" pattern="yyyy-MM-dd"/></td>
				<td>${statisticSealDayData.receiptIn}</td>
				<td>${statisticSealDayData.receiptTotal}</td>
				<td>${statisticSealDayData.sealDay}</td>
				<td>${statisticSealDayData.sealMoney}</td>
				<td>${statisticSealDayData.receiptDay}</td>
				<td>${statisticSealDayData.receiptSurplus}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<c:set var="appDatas" value="${requestScope.appDatas}"></c:set>	
	<c:forEach items="${appDatas}" var="appDataList"> 
		<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="4" style="text-align:center; vertical-align: middle;">日期</th>
				<th colspan="18" style="text-align:center; vertical-align: middle;">
				<c:if test="${fn:length(appDataList) >0 }">${appDataList[0].app.appName}</c:if></th>
			</tr>
			<tr> 
				    <th colspan="15" style="text-align:center; vertical-align: middle;">业务办理</th>
					<th colspan="3" style="text-align:center; vertical-align: middle;">小计</th> 
				</tr> 
				<tr>
					<td colspan="5" style="text-align:center; vertical-align: middle;">新增</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">变更</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">印章</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">费用</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">发票</td> 
				
				</tr>
				<tr>
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</tr>
		</thead>
		<tbody>
			<c:forEach items="${appDataList}" var="appData">
				<c:if test="${appData.sealTotal==0}">
					<tr>
						<td style="text-align:center; vertical-align: middle;" ><fmt:formatDate value="${appData.statisticDate}" pattern="yyyy-MM-dd"/></td>
						<td colspan="18" >${appData.app.appName}应用当天没办理数据</td>
					</tr>
				</c:if>
				<c:if test="${appData.sealTotal!=0}">
					<tr>
						<td style="text-align:center; vertical-align: middle;" >
						<fmt:formatDate value="${appData.statisticDate}" pattern="yyyy-MM-dd"/></td>
						<td style="text-align:center; vertical-align: middle;" >${appData.addOne}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.addTwo}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.addThree}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.addFour}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.addFive}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.renewOne}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.renewTwo}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.renewThree}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.renewFour}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.renewFive}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.changeOne}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.changeTwo}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.changeThree}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.changeFour}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.changeFive}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.sealTotal}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.sealMoney}</td>
						<td style="text-align:center; vertical-align: middle;" >${appData.receiptTotal}</td>
			   	</tr>
				</c:if>
			</c:forEach>
		</tbody>
	</table>
	</c:forEach>
	<div class="pagination">${page}</div>
</body>
</html>
