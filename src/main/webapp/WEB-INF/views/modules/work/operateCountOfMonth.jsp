<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>日经营统计</title>
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
</script>
</head>
<body>




	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/statistic/StatisticDayData/listMonth">月经营统计表</a></li>

	</ul>




	<form:form id="searchForm" modelAttribute="statisticDayData"
		action="${ctx}/statistic/StatisticDayData/listMonth" method="post"
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

		</div>
	</form:form>





	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align: center; vertical-align: middle;">日期</th>
				<th colspan="2" rowspan="1"
					style="text-align: center; vertical-align: middle;">入库</th>
				<th colspan="2" rowspan="1"
					style="text-align: center; vertical-align: middle;">总量</th>

				<th colspan="3" rowspan="1"
					style="text-align: center; vertical-align: middle;">月结</th>
				<th colspan="2" rowspan="1"
					style="text-align: center; vertical-align: middle;">余量</th>

			</tr>


			<tr>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">KEY</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">发票</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">KEY</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">发票</th>

				<th rowspan="1" style="text-align: center; vertical-align: middle;">证书</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">费用</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">KEY</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">KEY</th>
				<th rowspan="1" style="text-align: center; vertical-align: middle;">发票</th>
			</tr>

		</thead>
		<tbody>
			<c:forEach items="${sumList}" var="StatisticMonthData">
				<tr>
					<td><fmt:formatDate value="${StatisticMonthData.createDate}" pattern="yyyy-MM"/></td>
					<td>${StatisticMonthData.keyIn}</td>
					<td>${StatisticMonthData.receiptIn}</td>
					<td>${StatisticMonthData.keyTotal}</td>
					<td>${StatisticMonthData.receiptTotal}</td>
					<td>${StatisticMonthData.certTotal}</td>
					<td>${StatisticMonthData.certMoneyTotal}</td>
					<td>${StatisticMonthData.keyOver}</td>
					<td>${StatisticMonthData.keyStoreTotal}</td>
					<td>${StatisticMonthData.receiptStoreTotal}</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>













	<c:forEach items="${appSumList}" var="appMonthDataList">

		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th rowspan="4" style="text-align: center; vertical-align: middle;">日期</th>
					<th colspan="11"
						style="text-align: center; vertical-align: middle;">${appMonthDataList.get(0).app.appName}</th>


				</tr>

				<tr>
					<th colspan="8" style="text-align: center; vertical-align: middle;">业务办理</th>
					<th colspan="3" style="text-align: center; vertical-align: middle;">小计</th>
				</tr>
				<tr>

					<th colspan="4" style="text-align: center; vertical-align: middle;">新增</th>
					<th colspan="4" style="text-align: center; vertical-align: middle;">更新</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">变更</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">补办</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">证书</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">KEY</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">发票</th>

				</tr>
				<tr>
					<th style="text-align: center; vertical-align: middle;">1年</th>
					<th style="text-align: center; vertical-align: middle;">2年</th>
					<th style="text-align: center; vertical-align: middle;">4年</th>
					<th style="text-align: center; vertical-align: middle;">5年</th>
					<th style="text-align: center; vertical-align: middle;">1年</th>
					<th style="text-align: center; vertical-align: middle;">2年</th>
					<th style="text-align: center; vertical-align: middle;">4年</th>
					<th style="text-align: center; vertical-align: middle;">5年</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${appMonthDataList}" var="appMonthData">
					<tr>
						<td><fmt:formatDate value="${appMonthData.statisticDate}" pattern="yyyy-MM"/></td>
						<td>${appMonthData.add1}</td>
						<td>${appMonthData.add2}</td>
						<td>${appMonthData.add4}</td>
						<td>${appMonthData.add5}</td>
						<td>${appMonthData.renew1}</td>
						<td>${appMonthData.renew2}</td>
						<td>${appMonthData.renew4}</td>
						<td>${appMonthData.renew5}</td>
						<td>${appMonthData.modifyNum}</td>
						<td>${appMonthData.reissueNum}</td>
						<td>${appMonthData.certTotal}</td>
						<td>${appMonthData.keyTotal}</td>
						<td>${appMonthData.receiptTotal}</td>


					</tr>
				</c:forEach>
			</tbody>
		</table>

	</c:forEach>




	<div class="pagination">${page}</div>
</body>
</html>
