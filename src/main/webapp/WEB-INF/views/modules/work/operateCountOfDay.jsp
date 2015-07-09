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
		<li class="active"><a href="${ctx}/statistic/StatisticDayData/list">日经营统计表</a></li>
		
	</ul>
	
	
	
	
	<form:form id="searchForm" modelAttribute="statisticDayData"
		action="${ctx}/statistic/StatisticDayData/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

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
			 
			 </div>
	</form:form>

	
	
	
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">日期</th>
				<th colspan="2" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">入库</th>
				<th colspan="2" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">总量</th>
				<!-- <th colspan="11" style="text-align:center; vertical-align: middle;">四川省地税通用证书</th> -->
				<th colspan="3" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth"> 日结</th>
				<th colspan="2" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">余量</th>
				
			</tr>
			  
			<!-- <tr > 
			     <th colspan="8" style="text-align:center; vertical-align: middle;">业务办理</th>
				 <th colspan="3" style="text-align:center; vertical-align: middle;">小计</th> 
			</tr> -->
			<tr>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<!-- <th colspan="3" style="text-align:center; vertical-align: middle;">新增</th>
				<th colspan="3" style="text-align:center; vertical-align: middle;">更新</th>
				<th rowspan="2" style="text-align:center; vertical-align: middle;">变更</th>
				<th rowspan="2" style="text-align:center; vertical-align: middle;">补办</th>
				<th rowspan="2" style="text-align:center; vertical-align: middle;">证书</th>
				<th rowspan="2" style="text-align:center; vertical-align: middle;">KEY</th>
				<th rowspan="2" style="text-align:center; vertical-align: middle;">发票</th> -->
				<td rowspan="1" style="text-align:center; vertical-align: middle;">证书</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">费用</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
			</tr>
			<!-- <tr>
				<th style="text-align:center; vertical-align: middle;">1年</th>
				<th style="text-align:center; vertical-align: middle;">2年</th>
				<th style="text-align:center; vertical-align: middle;">4年</th>
				<th style="text-align:center; vertical-align: middle;">1年</th>
				<th style="text-align:center; vertical-align: middle;">2年</th>
				<th style="text-align:center; vertical-align: middle;">4年</th>
			</tr> -->
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="StatisticDayData">
				<tr>
					<td><fmt:formatDate value="${StatisticDayData.statisticDate}" pattern="yyyy-MM-dd"/></td>
					<td>${StatisticDayData.keyIn}</td>
					<td>${StatisticDayData.receiptIn}</td>
					<td>${StatisticDayData.keyTotal}</td>
					<td>${StatisticDayData.receiptTotal}</td>
					<td>${StatisticDayData.certTotal}</td>
					<td>${StatisticDayData.certMoneyTotal}</td>
					<td>${StatisticDayData.keyOver}</td>
				    <td>${StatisticDayData.keyStoreTotal}</td>
				    <td>${StatisticDayData.receiptStoreTotal}</td>
				    
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
				<th colspan="11" style="text-align:center; vertical-align: middle;">
				<c:if test="${fn:length(appDataList) >0 }">${appDataList.get(0).app.appName}</c:if>
				</th> 
				
				
			</tr>
			  
			<tr > 
			     <th colspan="10" style="text-align:center; vertical-align: middle;">业务办理</th>
				 <th colspan="3" style="text-align:center; vertical-align: middle;">小计</th> 
			</tr> 
			<tr>
				
				<td colspan="4" style="text-align:center; vertical-align: middle;">新增</td>
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">变更</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">补办</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">证书</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">发票</td> 
			
			</tr>
			<tr>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
			</tr> 
		</thead>
		<tbody>
			<c:forEach items="${appDataList}" var="appData">
				<tr>
					<td><fmt:formatDate value="${appData.statisticDate}" pattern="yyyy-MM-dd"/></td>
					<td>${appData.add1}</td>
					<td>${appData.add2}</td>
					<td>${appData.add4}</td>
					<td>${appData.add5}</td>
					<td>${appData.renew1}</td>
					<td>${appData.renew2}</td>
					<td>${appData.renew4}</td>
					<td>${appData.renew5}</td>
					<td>${appData.modifyNum}</td>
					<td>${appData.reissueNum}</td>
					<td>${appData.certTotal}</td>
					<td>${appData.keyTotal}</td>
					<td>${appData.receiptTotal}</td>
					
				    
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	</c:forEach>
	
	
	
	
	<div class="pagination">${page}</div>
</body>
</html>
