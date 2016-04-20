<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>印章日经营统计</title>
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
	function dcCountMonth(){
		if ($("#startTime").val()==""||$("#endTime").val()=="") {
			top.$.jBox.tip("请选定时间范围");
		} else {
			if ($("#office").val()==""){
				top.$.jBox.tip("请选定网点");
			} else {
				var startTime = document.getElementById("startTime").value;
				var	endTime = document.getElementById("endTime").value; 
				var office = document.getElementById("office").value;
				window.location.href="${ctx}/statistic/statisticSealDayData/exportCountMonth?startTime="+startTime+"&endTime="+endTime+"&office="+office;
			}
		}
	}
</script>
</head>
<body>


	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/statistic/StatisticDayData/listMonth">印章月经营统计表</a></li>

	</ul>


	<form:form id="searchForm" modelAttribute="statisticSealDayData"
		action="${ctx}/statistic/statisticSealDayData/listMonth" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<div>
			<label>选择日期：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false,maxDate:'#F{$dp.$D(\'endTime\')}'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM"/>" />


			<label>选择网点 ：</label> <select name="office" id="office">

				<option value="">请选择</option>

				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==office}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>

			</select> &nbsp; &nbsp; &nbsp; &nbsp; <input id="btnSubmit"
				class="btn btn-primary" type="button"  onclick="onSubmit()" value="查询" />
				<input id="exportCountMonth" class="btn btn-primary" type="button"  onclick="dcCountMonth()" value="导出" />

		</div>
	</form:form>

	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">日期</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">入库</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">总量</th>
				<th colspan="3" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">月结</th>
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
			<c:forEach items="${sumList}" var="StatisticMonthData">
				<c:set var="count" value="${StatisticMonthData.receiptIn+
				StatisticMonthData.receiptTotal+StatisticMonthData.sealDay+StatisticMonthData.sealDay+StatisticMonthData.sealMoney+
				StatisticMonthData.receiptDay+StatisticMonthData.receiptSurplus}"/>
				<c:if test="${count==0}">
					<tr>
						<td style="text-align:center; vertical-align: middle;"><fmt:formatDate value="${StatisticMonthData.statisticDate}" pattern="yyyy-MM"/></td>
						<td colspan="6">当月没有办理业务</td>
					</tr>
				</c:if>
				<c:if test="${count!=0}">
					<tr>
						<td style="text-align:center; vertical-align: middle;"><fmt:formatDate value="${StatisticMonthData.statisticDate}" pattern="yyyy-MM"/></td>
						<td>${StatisticMonthData.receiptIn}</td>
						<td>${StatisticMonthData.receiptTotal}</td>
						<td>${StatisticMonthData.sealDay}</td>
						<td>${StatisticMonthData.sealMoney}</td>
						<td>${StatisticMonthData.receiptDay}</td>
						<td>${StatisticMonthData.receiptSurplus}</td>
					</tr>
				</c:if>
			</c:forEach>
		</tbody>
	</table>

	<c:forEach items="${appSumList}" var="appMonthDataList">

		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th rowspan="4" style="text-align:center; vertical-align: middle;">日期</th>
					<th colspan="19" style="text-align:center; vertical-align: middle;">${appMonthDataList[0].app.appName}</th>
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
				<c:forEach items="${appMonthDataList}" var="appMonthData">
				
				<c:if test="${appMonthData.sealTotal==0}">
				<tr>
					<td style="text-align:center; vertical-align: middle;" ><fmt:formatDate value="${appMonthData.statisticDate}" pattern="yyyy-MM"/></td>
					<td colspan="18" >${appMonthData.app.appName}应用当月没办理数据
					</td>
				</tr>
				</c:if>
				<c:if test="${appMonthData.sealTotal!=0}">
					<tr>
						<td style="text-align:center; vertical-align: middle;" >
						<fmt:formatDate value="${appMonthData.statisticDate}" pattern="yyyy-MM"/></td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.addOne}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.addTwo}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.addThree}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.addFour}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.addFive}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renewOne}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renewTwo}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renewThree}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renewFour}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renewFive}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeOne}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeTwo}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeThree}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeFour}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeFive}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.sealTotal}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.sealMoney}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.receiptTotal}</td>
			   	</tr>
				</c:if>	
				</c:forEach>
			</tbody>
		</table>
	</c:forEach>
	<div class="pagination">${page}</div>
</body>
</html>
