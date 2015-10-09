<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>证书发放量统计</title>
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
		
		function addOffice(){
			var areaId = $("#areaId").prop('value');
			var url = "${ctx}/sys/office/addOffices?areaId=";
			 $.getJSON(url+areaId+"&_="+new Date().getTime(),function(data){
				var html = "";
				//console.log(data);
				html += "<option value=\""+""+"\">请选择</ooption>";
				$.each(data,function(idx,ele){
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">"+ele.name	+"</ooption>"
				});
				
				$("#officeId").html(html);
			});
			
		 }
		
		function showDealInfo(appId,productId,dealInfoType,year){
			var url = "${ctx}/settle/agentSettle/showDealInfo?appId="+appId+"&productId="+productId+"&dealInfoType="+dealInfoType+"&year="+year+"&type=TG";
			top.$.jBox.open("iframe:" + url, "业务明细", 800, 420, {
				buttons : {
					"确定" : "ok",
					"关闭" : true
				},
				submit : function(v, h, f) {
				}
			});
		}
		/* function onSubmit(){
			if ($("#startTime").val()==""||$("#endTime").val()=="") {
				top.$.jBox.tip("请选定时间范围");
				return false;
			} else {
				if ($("#officeId").val()==""){
					top.$.jBox.tip("请选定网点");
					return false;
				} else {
					return true;
				}
			}
		} */
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/statistic/statisticCertDataProduct">证书发放量统计</a></li>
		<li ><a href="${ctx}/statistic/statisticCertData">项目发放证书统计</a></li>
	</ul>
	<form:form id="searchForm"  action="${ctx}/statistic/statisticCertDataProduct" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		<label>选择区域 ：</label>
		<select name="areaId" id="areaId" onchange="addOffice()">
			<option value="">请选择</option>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>
			<select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
			</select>
			</div>
			<div style="margin-top: 8px">
		<label>统计时间 ：</label>
		<input id="startTime" name="startDate" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"
			value="${startDate}"/>&nbsp;-&nbsp;
				<input id="endTime" name="endDate" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="${endDate}" />
		&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary"  type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	
	 <div class="form-horizontal">
	 <table class="table table-striped table-bordered table-condensed">
			<thead >
			<tr>
					<th colspan="${monthList.size()+3 }">证书发放量统计</th>
				</tr>
				<tr>
					<th>产品名称</th>
					<th>年限</th>
					<c:forEach items="${monthList }" var="month">
						<th>${month }</th>
					</c:forEach>
					<th>小计</th>
				</tr>
			</thead>
			
			<c:forEach items="${sumList }" var="sum">
			<c:set var="oneCount" value="0"/>
			<c:set var="twoCount" value="0"/>
			<c:set var="fourCount" value="0"/>
			<c:set var="fiveCount" value="0"/>
			<tr>
				<td rowspan="4">${pro[sum.productType] }</td>
				<td>一年期</td>
				<c:forEach items="${sum.months }" var="sm1">
					<td>${sm1.count1 }</td>
				<c:set var="oneCount" value="${oneCount + sm1.count1 }" />
				</c:forEach>
				<td>${oneCount }</td>
			</tr>
			<tr>
				<td>两年期</td>
				<c:forEach items="${sum.months }" var="sm2">
					<td>${sm2.count2 }</td>
				<c:set var="twoCount" value="${twoCount + sm2.count2 }" />
				</c:forEach>
				<td>${twoCount }</td>
			</tr>
			<tr>
				<td>四年期</td>
				<c:forEach items="${sum.months }" var="sm4">
					<td>${sm4.count4 }</td>
				<c:set var="fourCount" value="${fourCount + sm4.count4 }" />
				</c:forEach>
				<td>${fourCount }</td>
			</tr>
			<tr>
				<td>五年期</td>
				<c:forEach items="${sum.months }" var="sm5">
					<td>${sm5.count5 }</td>
				<c:set var="fiveCount" value="${fiveCount + sm5.count5 }" />
				</c:forEach>
				<td>${fiveCount }</td>
			</tr>
			</c:forEach>
			<tr>
				<td>总计</td>
				<td></td>
				
				<c:forEach items="${monthList }" var="sum" varStatus="status">
				<c:set var="allCount" value="0"/>
					<td>
					<c:forEach items="${sumList}" var="sum" varStatus="countYear">	
						<c:set var="oneYear" value = "${oneYear+sum.months.get(status.index).count1 }"></c:set>						
						<c:set var="twoYear" value = "${twoYear+sum.months.get(status.index).count2 }"></c:set>						
						<c:set var="fourYear" value = "${fourYear+sum.months.get(status.index).count4 }"></c:set>
						<c:set var="fiveYear" value = "${fiveYear+sum.months.get(status.index).count5 }"></c:set>						
					</c:forEach>
					<c:set var="allCount" value="${allCount + oneYear+twoYear+fourYear+fiveYear}" />
						${oneYear+twoYear+fourYear+fiveYear}
						<c:set var="oneYear" value = "0"></c:set>						
						<c:set var="twoYear" value = "0"></c:set>						
						<c:set var="fourYear" value = "0"></c:set>
						<c:set var="fiveYear" value = "0"></c:set>
					</td>
				</c:forEach>
				<td>${allCount}</td>
			</tr>
			<c:set var="total" value="0"/>
		</table>
		</div>
</body>
</html>
