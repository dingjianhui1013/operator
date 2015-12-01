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
		var area = $("#area").val();
		var endTime=$("#endTime").val();
		var appId=$("#appId").val();
		var startTime=$("#startTime").val();
		if(endTime=="")
			{
				$("#endTime").val(startTime);
			}
		if(area=="")
			{
				top.$.jBox.tip("请选择区域");
				return false;
			}
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
	function dc()
	{
		var area = $("#area").val();
		var appId = $("#appId").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		if(endTime=="")
		{
			$("#endTime").val(startTime);
		}else
		if(area=="")
		{
			top.$.jBox.tip("请选择区域");
		}else
		if(appId=="")
		{
			top.$.jBox.tip("请选择项目");
		}else
		if(startTime=="")
		{
			top.$.jBox.tip("请选择统计时间");
		}else
			{
				window.location.href="${ctx}/work/workDealInfo/exportDayPayment?area="+area+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime;
			}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/StatisticalDayList">日回款统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/StatisticalDayList"
		method="post" class="breadcrumb form-search">
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
		<div style="margin-top: 9px">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label>统计时间&nbsp;：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>"/> -
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return sub()"
			value="查询" />
			<a href="javascript:dc()" class="btn btn-primary">导出</a>
		</div>
			</div>
			
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>统计日期</th>
				<th>项目名称</th>
				<c:set var="post" value="0" />
				<c:set var="bank" value="0" />
				<c:set var="xj" value="0" />
				<c:set var="alipay" value="0" />
				<c:forEach items="${workDate_Mone}" var="workDate_Mone">
						<c:if test="${workDate_Mone.countPostMoney>0}">
							<c:set var="post" value="${post+1}" />
						</c:if>
						<c:if test="${workDate_Mone.countBankMoney>0}">
							<c:set var="bank" value="${bank+1}" />
						</c:if>
						<c:if test="${workDate_Mone.countXjMoney>0}">
							<c:set var="xj" value="${xj+1}" />
						</c:if>
						<c:if test="${workDate_Mone.countAlipayMoney>0}">
							<c:set var="alipay" value="${alipay+1}" />
						</c:if>
				</c:forEach>
				<c:if test="${post>0 }">
					<th>Post付款</th>
				</c:if>
				<c:if test="${bank>0 }">
					<th>银行转账付款</th>
				</c:if>
				<c:if test="${xj>0 }">
					<th>现金付款</th>
				</c:if>
				<c:if test="${alipay>0 }">
					<th>支付宝</th>
				</c:if>
				<c:forEach items="${office_Moneys}" var="officeMoneys">
					<c:if test="${officeMoneys.value>0}">
						<th>${officeMoneys.key}</th>
					</c:if>
					<c:if test="${officeMoneys.value==0}">
						<c:set var="officename" value="${officeMoneys.key}" />
					</c:if>
				</c:forEach>
				<th>合计</th>
			</tr>  
		</thead>
		<tbody>
		<c:set var="postMoney" value="0" />
		<c:set var="bankMoney" value="0" />
		<c:set var="xjMoney" value="0" />
		<c:set var="alipaymoney" value="0" />
		<c:set var="zj" value="0" />
		<c:forEach items="${workDate_Mone}" var="workDate_Mone">
				<tr>
				<td>${workDate_Mone.date}</td>
				<td>${appName}</td>
					<c:if test="${workDate_Mone.countPostMoney>0}">
					<c:set var="postMoney" value="${postMoney+workDate_Mone.countPostMoney}"/>
						<td>${workDate_Mone.countPostMoney}</td>
					</c:if>
					<c:if test="${workDate_Mone.countPostMoney==0&&post>0}">
						<td>0.0</td>
					</c:if>
					<c:if test="${workDate_Mone.countBankMoney>0}">
					<c:set var="bankMoney" value="${bankMoney+workDate_Mone.countBankMoney}"/>
						<td>${workDate_Mone.countBankMoney}</td>
					</c:if>
					<c:if test="${workDate_Mone.countBankMoney==0&&bank>0}">
						<td>0.0</td>
					</c:if>
					<c:if test="${workDate_Mone.countXjMoney>0}">
					<c:set var="xjMoney" value="${xjMoney+workDate_Mone.countXjMoney}"/>
						<td>${workDate_Mone.countXjMoney}</td>
					</c:if>
					<c:if test="${workDate_Mone.countXjMoney==0&&xj>0}">
						<td>0.0</td>
					</c:if>
					<c:if test="${workDate_Mone.countAlipayMoney>0}">
					<c:set var="alipayMoney" value="${alipayMoney+workDate_Mone.countAlipayMoney}"/>
						<td>${workDate_Mone.countAlipayMoney}</td>
					</c:if>
					<c:if test="${workDate_Mone.countAlipayMoney==0&&alipay>0}">
						<td>0.0</td>
					</c:if>
					<c:forEach items="${workoffice_MoneyVo}" var="workoffice_MoneyVo">
						<c:if test="${workoffice_MoneyVo.date==workDate_Mone.date&&workoffice_MoneyVo.officeName!=officename}">
							<td>${workoffice_MoneyVo.countMoney}</td>
						</c:if>
					</c:forEach>
					<td>
						<c:set var="zj" value="${zj+workDate_Mone.countMoney }" />
						${workDate_Mone.countMoney}
					</td>
				</tr>
		</c:forEach>
		<tr>
			<td>合计</td>	
			<td></td>
			<c:if test="${postMoney>0}">
				<td>${postMoney}</td>
			</c:if>
			<c:if test="${bankMoney>0}">
				<td>${bankMoney}</td>
			</c:if>
			<c:if test="${xjMoney>0}">
				<td>${xjMoney}</td>
			</c:if>
			<c:if test="${alipayMoney>0}">
				<td>${alipayMoney}</td>
			</c:if>
			<c:forEach items="${office_Moneys}" var="officeMoneys">
					<c:if test="${officeMoneys.value>0}">
						<td>${officeMoneys.value}</td>
					</c:if>
			</c:forEach>
			<td>${zj}</td>
		</tr>
		</tbody>
		</table>
</body>
</html>
