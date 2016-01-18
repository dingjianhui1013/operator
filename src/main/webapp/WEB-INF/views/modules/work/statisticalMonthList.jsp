<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>统计信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		
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
		var area =$("#area").val();
		var endTime=$("#endTime").val();
		var appId = $("#appId").val();
		var startTime = $("#startTime").val();
		if(endTime=="")
			{
				$("#endTime").val(startTime);
			}
// 		if(area=="")
// 		{
// 			top.$.jBox.tip("请选择区域");
// 			return false;
// 		}
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
		var office= $("#office").val();
		if(endTime=="")
		{
			$("#endTime").val(startTime);
		}
// 		else
// 		if(area=="")
// 		{
// 			top.$.jBox.tip("请选择区域");
// 		}
		else
		if(appId=="")
		{
			top.$.jBox.tip("请选择项目");
		}else
		if(startTime=="")
		{
			top.$.jBox.tip("请选择统计时间");
		}else
			{
				window.location.href="${ctx}/work/workDealInfo/exportMonthPayment?area="+area+"&appId="+appId+"&startTime="+startTime+"&endTime="+endTime+"&office="+office;
			}
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
		<label>统计时间&nbsp;：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM"/>"/>
			-
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM"/>"/>
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
				<th  rowspan="2" style="text-align:center;">统计日期</th>
				<th  rowspan="2" style="text-align:center;">项目名称</th>
				<c:forEach items="${office_payMethod}" var="office_payMethod">
					<c:set var="index" value="0" />
					<c:forEach items="${office_payMethod.value}">
						<c:set var="index" value="${index+1}" />
					</c:forEach>
					<c:if test="${index==1}">
						<th colspan="${index}" rowspan="2" style="text-align:center;">${office_payMethod.key}</th>
					</c:if>
					<c:if test="${index>1}">
						<th colspan="${index}" style="text-align:center;">${office_payMethod.key}</th>
					</c:if>
				</c:forEach>
				<th rowspan="2" style="text-align:center;">合计</th>
			</tr>
			<tr>
				<c:forEach items="${office_payMethod}" var="office_payMethod">
					<c:set var="index" value="0" />
					<c:forEach items="${office_payMethod.value}">
						<c:set var="index" value="${index+1}" />
					</c:forEach>
					<c:if test="${index>1}">
					<c:forEach items="${office_payMethod.value}" var="payMethod">
						<th style="text-align:center;">${payMethod}</th>
					</c:forEach>
					</c:if>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
				<c:set var="post" value="0" />
				<c:set var="bank" value="0" />
				<c:set var="xj" value="0" />
				<c:set var="alipay" value="0" />
				<c:set var="index" value="0" />
				<c:forEach items="${workoffice_MoneyVo}" var="workoffice_MoneyVo">
						<c:if test="${workoffice_MoneyVo.countPostMoney>0}">
							<c:set var="post" value="${post+1}" />
						</c:if>
						<c:if test="${workoffice_MoneyVo.countBankMoney>0}">
							<c:set var="bank" value="${bank+1}" />
						</c:if>
						<c:if test="${workoffice_MoneyVo.countXjMoney>0}">
							<c:set var="xj" value="${xj+1}" />
						</c:if>
						<c:if test="${workoffice_MoneyVo.countAlipayMoney>0}">
							<c:set var="alipay" value="${alipay+1}" />
						</c:if>
				</c:forEach>
		<c:set var="postMoney" value="0" />
		<c:set var="bankMoney" value="0" />
		<c:set var="xjMoney" value="0" />
		<c:set var="alipaymoney" value="0" />
		<c:set var="zj" value="0" />
		<c:forEach items="${dates}" var = "dates">
			<tr>
				<td>${dates}</td>
				<td>${appName}</td>
				<c:forEach items="${office_payMethod }" var="office_payMethod">
					<c:forEach items="${workoffice_MoneyVo}" var="workoffice_MoneyVo" >
						<c:if test = "${workoffice_MoneyVo.date == dates &&workoffice_MoneyVo.officeName==office_payMethod.key}">
							<c:if test="${workoffice_MoneyVo.postMoney}">
								<c:set var="postMoney" value="${postMoney+workoffice_MoneyVo.countPostMoney}"/>
									<td>${workoffice_MoneyVo.countPostMoney}</td>
								</c:if>
								<c:if test="${workoffice_MoneyVo.bankMoney}">
								<c:set var="bankMoney" value="${bankMoney+workoffice_MoneyVo.countBankMoney}"/>
									<td>${workoffice_MoneyVo.countBankMoney}</td>
								</c:if>
								<c:if test="${workoffice_MoneyVo.xjMoney}">
								<c:set var="xjMoney" value="${xjMoney+workoffice_MoneyVo.countXjMoney}"/>
									<td>${workoffice_MoneyVo.countXjMoney}</td>
								</c:if>
								<c:if test="${workoffice_MoneyVo.alipayMoney}">
								<c:set var="alipayMoney" value="${alipayMoney+workoffice_MoneyVo.countAlipayMoney}"/>
									<td>${workoffice_MoneyVo.countAlipayMoney}</td>
								</c:if>
						</c:if>
						</c:forEach>
					</c:forEach>
					<c:forEach items="${workDate_Mone}" var="workDate_Mone">
						<c:if test="${workDate_Mone.date==dates}">
							<td>${workDate_Mone.countMoney}</td>
						</c:if>
					</c:forEach>
			<tr>
		</c:forEach>
		<tr>
			<td>合计</td>
			<td></td>
			<c:if test="${moneys==null}">
				<td>0.0</td>
			</c:if>
			<c:if test="${moneys!=null}">
				<c:forEach items="${moneys}" var="moneys">
					<td>${moneys}</td>
				</c:forEach>
			</c:if>
		</tr>
		</tbody>
	</table>
</body>
</html>
