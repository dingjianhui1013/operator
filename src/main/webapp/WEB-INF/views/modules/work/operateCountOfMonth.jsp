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
				window.location.href="${ctx}/statistic/StatisticDayData/exportCountMonth?startTime="+startTime+"&endTime="+endTime+"&office="+office;
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
				<input id="exportCountMonth" class="btn btn-primary" type="button"  onclick="dcCountMonth()" value="导出" />

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
					<th colspan="43"
						style="text-align: center; vertical-align: middle;">${appMonthDataList[0].app.appName}</th>


				</tr>

				<tr>
					<th colspan="40" style="text-align: center; vertical-align: middle;">业务办理</th>
					<th colspan="3" style="text-align: center; vertical-align: middle;">小计</th>
				</tr>
				<tr>

					<th colspan="5" style="text-align: center; vertical-align: middle;">新增</th>
					<th colspan="5" style="text-align: center; vertical-align: middle;">更新</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">变更</th>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">遗失补办</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">损坏更换</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新+变更</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新+遗失补办</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新+损坏更换</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">变更+遗失补办</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">变更+损坏更换</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新+变更+遗失补办</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新+变更+损坏更换</td>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">证书</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">KEY</th>
					<th rowspan="2" style="text-align: center; vertical-align: middle;">发票</th>

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
					
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${appMonthDataList}" var="appMonthData">
				
				<c:if test="${appMonthData.certTotal==0}">
				<tr>
					<td style="text-align:center; vertical-align: middle;" ><fmt:formatDate value="${appMonthData.statisticDate}" pattern="yyyy-MM-dd"/></td>
					<td colspan="36" >${appMonthData.app.appName}应用当月没办理数据
					</td>
				</tr>
				</c:if>
				<c:if test="${appMonthData.certTotal!=0}">
					<tr>
						<td style="text-align:center; vertical-align: middle;" ><fmt:formatDate value="${appMonthData.statisticDate}" pattern="yyyy-MM"/></td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.add1}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.add2}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.add3}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.add4}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.add5}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renew1}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renew2}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renew3}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renew4}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.renew5}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.modifyNum}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.reissueNum}</td>
						
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.lostReplaceNum}</td>
					
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateChangeNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateChangeNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateChangeNum3}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateChangeNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateChangeNum5}</td>
					
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateLostNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateLostNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateLostNum3}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateLostNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateLostNum5}</td>
					
					
					
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateReplaceNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateReplaceNum3}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateReplaceNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.updateReplaceNum5}</td>
					
					
					
					
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeLostNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateLostNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateLostNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateLostNum3}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateLostNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateLostNum5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateReplaceNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateReplaceNum3}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateReplaceNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appMonthData.changeUpdateReplaceNum5}</td>
						
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.certTotal}</td>
						<td style="text-align:center; vertical-align: middle;" >${appMonthData.keyTotal}</td>
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
