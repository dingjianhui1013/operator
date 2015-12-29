<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>统计信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		var windowH=$(window).height();
		$('.windowHeight').height(windowH);
		$("#scrollBar").scroll(function(){
			var leftWidth=$("#scrollBar").scrollLeft();
			$("#searchForm").css("margin-left",leftWidth);
			$("#ulId").css("margin-left",leftWidth);
		});
		
		
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
		var endTime=$("#endTime").val();
		var appId=$("#appId").val();
		var startTime=$("#startTime").val();
		if(endTime=="")
			{
				$("#endTime").val(startTime);
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
		var office=$("#office").val();
		if(endTime=="")
		{
			$("#endTime").val(startTime);
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
				window.location.href="${ctx}/work/workDealInfo/exportProjectPayment?area="+area+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime+"&office="+office;
			}
	}
	
	
	
</script>

</head>
<body>
	<div style="overflow:auto;" class="windowHeight" id="scrollBar" >
	<ul class="nav nav-tabs" id="ulId" style="width:100%;">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/StatisticalDayList">项目回款统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/StatisticalProjectList"
		method="post" class="breadcrumb form-search" style="width:100%;">
		<div style="margin-top: 9px;" >
		<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;项目名称：</label>
		<select name="appId" id="appId">
				<option value="">请选择</option>
			<c:forEach items="${appList}" var="app">
				<option value="${app.id}"
					<c:if test="${app.id==appId}">
					selected="selected"
					</c:if>>${app.appName}</option>
			</c:forEach>
		</select>
		<label>选择区域 ：</label>
		<select name="area" id="area" onchange="addOffice()">
			<option value="">请选择</option>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==area}">
						selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>
		<select name="office" id="office">
			<option value="">请选择</option>
			<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
		</select>
		</div>
		<div style="margin-top: 9px">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label>统计时间：</label>
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
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th  rowspan="3" style="text-align:center;">统计日期</th>
				<th  rowspan="3" style="text-align:center;">项目名称</th>
				<c:forEach items="${office_District}" var="office_District">
					<c:set var="index" value="0" />
					<c:set var="qu" value="0" />
					<c:forEach items="${office_District.value}" var="district">
						<c:set var="qu" value="${qu+1}" />
						<c:forEach items="${district_payMethod}" var="district_payMethod">
								<c:if test="${district_payMethod.key==district}">
									<c:forEach items="${district_payMethod.value}" var="dpv">
										<c:set var="index" value="${index+1}" />
									</c:forEach>
								</c:if>
						</c:forEach>
					</c:forEach>
					<c:if test="${index==1}">
						<c:if test="${qu==1}">
							<th rowspan="2" style="text-align:center;">${office_District.key}</th>
						</c:if>
						<c:if test="${qu>1}">
							<th colspan="${qu}" style="text-align:center;">${office_District.key}</th>
						</c:if>
					</c:if>
					<c:if test="${index>1}">
					<c:set var="pays" value="0"/>
						<c:forEach items="${office_District.value}" var="district">
							<c:forEach items="${district_payMethod}" var="district_payMethod">
								<c:if test="${district_payMethod.key==district}">
								<c:if test="${fn:length(district_payMethod.value)==1}">
									<c:set var="pays" value="${pays+1}" />
								</c:if>
									<c:if test="${fn:length(district_payMethod.value)>1}">
										<c:forEach items="${district_payMethod.value}" var="dpvs">
											<c:set var="pays" value="${pays+1}" />
										</c:forEach>
									</c:if>
								</c:if>
							</c:forEach>
						</c:forEach>
						<th colspan="${pays}" style="text-align:center;">${office_District.key}</th>
					</c:if>
				</c:forEach>
				<th rowspan="3" style="text-align:center;">合计</th>
			</tr>
			<tr>
				<c:forEach items="${office_District}" var="office_District">
					<c:set var="index" value="0" />
					<c:set var="qu" value="0" />
					<c:forEach items="${office_District.value}" var="district">
					<c:set var="qu" value="${qu+1}" />
						<c:forEach items="${district_payMethod}" var="district_payMethod">
								<c:if test="${district_payMethod.key==district}">
									<c:forEach items="${district_payMethod.value}" var="dpv">
										<c:set var="index" value="${index+1}" />
									</c:forEach>
								</c:if>
						</c:forEach>
					</c:forEach>
					<c:if test="${index==1}">
						<c:if test="${qu>1}">
								<th style="text-align:center;">${district}</th>
						</c:if>
					</c:if>
					<c:if test="${index>1}">
					<c:forEach items="${office_District.value}" var="district">
					<c:set var="count" value="0" />
						<c:forEach items="${district_payMethod}" var="district_payMethod">
								<c:if test="${district_payMethod.key==district}">
									<c:forEach items="${district_payMethod.value}" var="dpv">
										<c:set var="count" value="${count+1}" />
									</c:forEach>
								</c:if>
						</c:forEach>
						<c:if test="${count==1}">
							<th style="text-align:center;">${district}</th>
						</c:if>
						<c:if test="${count>1}">
							<th style="text-align:center;" colspan="${count}">${district}</th>
						</c:if>
					</c:forEach>
					</c:if>
				</c:forEach>
			</tr>
			<tr>
				<c:forEach items="${office_District}" var="office_District">
					<c:forEach items="${office_District.value}" var="district">
						<c:forEach items="${district_payMethod}" var="district_payMethod">
								<c:if test="${district_payMethod.key==district}">
									<c:forEach items="${district_payMethod.value}" var="dpv">
										<th>${dpv}</th>
									</c:forEach>
								</c:if>
						</c:forEach>
					</c:forEach>
				</c:forEach>
			</tr> 
		</thead>
		<tbody>
		<c:forEach items="${dates}" var = "dates">
			<tr>
				<td>${dates}</td>
				<td>${appName}</td>
				<c:forEach items="${office_District}" var="od">
					<c:forEach items="${od.value}" var="odv">
						<c:forEach items="${odms}" var="odms" >
							<c:if test = "${odms.date == dates&&odms.districtName==odv}">
								<c:if test="${odms.postMoney}">
										<td>${0.0+odms.countPostMoney}</td>
									</c:if>
									<c:if test="${odms.bankMoney}">
										<td>${0.0+odms.countBankMoney}</td>
									</c:if>
									<c:if test="${odms.xjMoney}">
										<td>${0.0+odms.countXjMoney}</td>
									</c:if>
									<c:if test="${odms.alipayMoney}">
										<td>${0.0+odms.countAlipayMoney}</td>
									</c:if>
							</c:if>
						</c:forEach>
					
					</c:forEach>
				</c:forEach>
					<c:forEach items="${workDate_Mone}" var="workDate_Mone">
						<c:if test="${workDate_Mone.date==dates}">
							<td>${0.0+workDate_Mone.countMoney}</td>
						</c:if>
					</c:forEach>
			<tr>
		</c:forEach>
		<tr>
		<td>合计</td>
		<td></td>
		<c:forEach items="${office_District}" var="office_District">
			<c:forEach items="${office_District.value}" var="odv">
				<c:forEach items="${district_Moneys}" var="district_Moneys">
					<c:if test="${district_Moneys.key==odv}">
					<c:forEach items="${district_Moneys.value}" var="dmv">
						<td>${dmv}</td>
					</c:forEach>
					</c:if>
			</c:forEach>
		</c:forEach>
		</c:forEach>
		<td>${moneys}</td>
		</tr>
		</tbody>
	</table>
	</div>
</body>
</html>
