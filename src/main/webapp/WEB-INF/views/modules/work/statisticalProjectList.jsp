<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>统计信息</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		var windowH=$(window).height();
		$('.windowHeight').height(windowH);
		$("#scrollBar").scroll(function(){
			var leftWidth=$("#scrollBar").scrollLeft();
			var tableWidth=$("#contentTable").width();
			var formWidth=$("#searchForm").width();
			if((tableWidth-formWidth)-leftWidth>0)
				{
					$("#searchForm").css("margin-left",leftWidth);
					$("#ulId").css("margin-left",leftWidth);
				}
		});
		
		$("#searchForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					//alert(element[0].tagName);
					(element.parent().find('label:last-child')).after(error);
				} else {
					//alert(element[0].tagName);
					error.insertAfter(element);
				}
			}
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
			
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#office").html(html);
		});

	}

	function dc()
	{
		var area = $("#area").val();
		var appId = $("#appId").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var office=$("#office").val();
		
		window.location.href="${ctx}/work/workDealInfo/exportProjectPayment?area="+area+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime+"&office="+office;
			
	}
	
	
	
</script>

</head>
<body>
	<div style="overflow:auto;" class="windowHeight" id="scrollBar" >
	<ul class="nav nav-tabs" id="ulId" style="width:100%;">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/StatisticalProjectList">项目回款统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/StatisticalProjectList"
		method="post" class="breadcrumb form-search" style="width:100%;">
		<div style="margin-top: 9px;" >
		<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;项目名称：</label>
		<select name="appId" id="appId" data-placeholder="选择应用" class="editable-select">
				<option value=""></option>
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
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>"/> -
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" 
			value="查询" />
			<a href="javascript:dc()" class="btn btn-primary">导出</a>
		</div>
	</form:form>
	<tags:message content="${message}" />
	
	
	<c:forEach items="${receivedPayments }" var="receivedPayment">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="7%" rowspan="3" style="text-align:center;vertical-align:middle;">统计日期</th>
				<th width="15%" rowspan="3" style="text-align:center;vertical-align:middle;">项目名称</th>
				
				<c:forEach items="${receivedPayment.officeDistrictPayMethod }" var="officeDistrictPayMethod">
				
				<c:set var="qu" value="0" />
				
				
				<c:forEach items="${officeDistrictPayMethod.value }" var="odpmv">
				
				<c:forEach items="${odpmv.value }">
					<c:set var="qu" value="${qu+1}" />
					</c:forEach>
				
				
				</c:forEach>
				
				
				<th colspan="${qu}" style="text-align:center;">${officeDistrictPayMethod.key}</th>
				
				</c:forEach>
				

				<th width="5%" rowspan="3" style="text-align:center; vertical-align:middle;">合计</th>
			</tr>
			<tr>
			
			<c:forEach items="${receivedPayment.officeDistrictPayMethod }" var="officeDistrictPayMethod">
					<c:forEach items="${officeDistrictPayMethod.value }" var="odpmv">
					
					<c:set var="qu" value="0" />
					<c:forEach items="${odpmv.value }">
					<c:set var="qu" value="${qu+1}" />
					</c:forEach>
					
					<th colspan="${qu }" style="text-align:center;">${odpmv.key}</th>	
					</c:forEach>
				
				
				
				</c:forEach>
			
			
			</tr>
			<tr>
				<c:forEach items="${receivedPayment.officeDistrictPayMethod }" var="officeDistrictPayMethod">
					<c:forEach items="${officeDistrictPayMethod.value }" var="odpmv">
				<c:forEach items="${odpmv.value }"  var="odpmvv">
				<th style="text-align:center;">${odpmvv}</th>	
				</c:forEach>
					
					
					</c:forEach>
				
				
				
				</c:forEach>
			</tr> 
		</thead>
		<tbody>
		<c:forEach items="${receivedPayment.dates}" var = "dates">
		<c:set var="countMoneys" value="0"/>
			<tr>
				<td>${dates}</td>
				<td>${receivedPayment.appName}</td>
				<c:forEach items="${receivedPayment.officeDistrict}" var="od">
					<c:forEach items="${od.value}" var="odv">
						<c:forEach items="${receivedPayment.officeDistrictMoneyVo}" var="odms" >
							<c:if test = "${odms.date == dates&&odms.districtName==odv&&od.key==odms.officeName}">
								<c:if test="${odms.postMoney}">
										<td>${odms.countPostMoney}</td>
										<c:set var="countMoneys" value="${countMoneys+odms.countPostMoney}"/>
									</c:if>
									<c:if test="${odms.bankMoney}">
										<td>${odms.countBankMoney}</td>
										<c:set var="countMoneys" value="${countMoneys+odms.countBankMoney}"/>
									</c:if>
									<c:if test="${odms.xjMoney}">
										<td>${odms.countXjMoney}</td>
										<c:set var="countMoneys" value="${countMoneys+odms.countXjMoney}"/>
									</c:if>
									<c:if test="${odms.alipayMoney}">
										<td>${odms.countAlipayMoney}</td>
										<c:set var="countMoneys" value="${countMoneys+odms.countAlipayMoney}"/>
									</c:if>
							</c:if>
						</c:forEach>
					
					</c:forEach>
				</c:forEach>

					<td>${countMoneys}</td>
			<tr>
		</c:forEach>
		<tr>
		<td>合计</td>
		<td></td>
		<c:set var="money" value="0"/>
		<%-- <c:forEach items="${receivedPayment.officeDistrict}" var="office_District">
			<c:forEach items="${office_District.value}" var="odv">
				<c:forEach items="${receivedPayment.districtMoneys}" var="district_Moneys">
					<c:if test="${district_Moneys.key==odv}">
					<c:forEach items="${district_Moneys.value}" var="dmv">
						
						<td>${dmv}</td>
						<c:set var="money" value="${money+dmv}"/>
						
						
					</c:forEach>
					</c:if>
			</c:forEach>
		</c:forEach>
		</c:forEach> --%>
		
		
		<c:forEach items="${receivedPayment.officeDistrictMoney }" var="office_District_Money">
		 <c:forEach items="${office_District_Money.value }" var="district_money">
		 	<c:forEach items="${district_money.value }" var="dmv">
		 		<td>${dmv}</td>
						<c:set var="money" value="${money+dmv}"/>
			</c:forEach>			
		 </c:forEach>
		</c:forEach>
		 
		
		
		<td>${money}</td>
		</tr>
		</tbody>
	</table>
	</c:forEach>
	

	</div>
	<script type="text/javascript">
	$(function(){
	    $('.editable-select').chosen();
	});
	</script>
</body>
</html>
