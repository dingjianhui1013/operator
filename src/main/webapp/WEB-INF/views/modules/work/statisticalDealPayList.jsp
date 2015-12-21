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
		$.getJSON(url + areaId + "&_=" + new Date().getTime(), function(data) {
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
	function dc() {
		var area = $("#area").val();
		var office = $("#office").val();
		var appId = $("#appId").val();
		var payMethod = $("#payMethod").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		window.location.href = "${ctx}/work/workDealInfo/exportDealPayList?area="
				+ area
				+ "&office="
				+ office
				+ "&appId="
				+ appId
				+ "&payMethod="
				+ payMethod
				+ "&startTime="
				+ startTime
				+ "&endTime=" + endTime;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/StatisticalDealPayList">支付信息统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/StatisticalDealPayList" method="post"
		class="breadcrumb form-search">

		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div style="margin-top: 9px">
			<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选择区域 ：</label> <select
				name="area" id="area" onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> <label>选择网点 ：</label> <select name="office" id="office">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==office}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			
			</select> <label>应用名称 ：</label> <select name="appId" id="appId">
				<option value="">请选择</option>
				<c:forEach items="${appList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==appId}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>

			</select> 
			</div>
			<br>
			<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;付款方式 ：</label> <select name="payMethod" id="payMethod">
				<option value="0"
					<c:if test="${payMethod=='0'}"> selected="selected" </c:if>>全部</option>
				<option value="1"
					<c:if test="${payMethod=='1'}"> selected="selected" </c:if>>现金</option>
				<option value="2"
					<c:if test="${payMethod=='2'}"> selected="selected" </c:if>>POS收款</option>
				<option value="3"
					<c:if test="${payMethod=='3'}"> selected="selected" </c:if>>银行转帐</option>
				<option value="4"
					<c:if test="${payMethod=='4'}"> selected="selected" </c:if>>支付宝转账</option>
				<option value="5"
					<c:if test="${payMethod=='5'}"> selected="selected" </c:if>>政府统一采购</option>
				<option value="6"
					<c:if test="${payMethod=='6'}"> selected="selected" </c:if>>合同采购</option>
			</select>
		
		
			<label>业务办理时间&nbsp;：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="10"
				class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp; <input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="10" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
				id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			<a href="javascript:dc()" class="btn btn-primary">导出</a>
		
	</form:form>
	<tags:message content="${message}" />
	<c:if test="${dealCountSta!=null }">
		<table id="dealCountStaTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>区域名称</th>
					<th>网点名称</th>
					<th>现金支付数量</th>
					<th>pos付款数量</th>
					<th>银行付款数量</th>
					<th>支付宝付款数量</th>
					<th>政府统一采购付款数量</th>
					<th>合同采购付款数量</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${dealCountSta.areaName}</td>
					<td>${dealCountSta.officeName}</td>
					<td>${dealCountSta.moneyCount}</td>
					<td>${dealCountSta.posCount}</td>
					<td>${dealCountSta.bankCount}</td>
					<td>${dealCountSta.alipayCount}</td>
					<td>${dealCountSta.govCount}</td>
					<td>${dealCountSta.contractCount}</td>
				</tr>
				<tr>
					<th colspan="9">${dealMsg}</th>
				</tr>
			</tbody>
		</table>
	</c:if>







	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>付款单位名称</th>
				<th>付款金额/元</th>
				<th>应用名称</th>
				<th>联系人</th>
				<th>联系方式</th>
				<th>付款时间</th>
				<th>付款方式</th>
				<th>区域</th>
				<th>网点</th>
				<th>记录人员</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="configDealPay">
				<tr>
					<td>${configDealPay.workCompany.companyName}</td>
					<td>${configDealPay.workPayInfo.workPayedMoney}</td>

					<td>${configDealPay.configApp.appName }</td>

					<td>${configDealPay.workUser.contactName}</td>
					<td>${configDealPay.workUser.contactPhone}</td>
					<td><fmt:formatDate
							value="${configDealPay.workPayInfo.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><c:choose>
							<c:when test="${configDealPay.workPayInfo.methodPos==true}">
							POS收款
						</c:when>
							<c:when test="${configDealPay.workPayInfo.methodMoney==true}">
							现金
						</c:when>
							<c:when test="${configDealPay.workPayInfo.methodBank==true}">
							银行转账
						</c:when>
							<c:when test="${configDealPay.workPayInfo.methodAlipay==true}">
							支付宝
						</c:when>
							<c:when test="${configDealPay.workPayInfo.methodContract==true}">
							合同采购
						</c:when>
							<c:when test="${configDealPay.workPayInfo.methodGov==true}">
							政府统一采购
						</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${configDealPay.workPayInfo.relationMethod==0}">
									现金
								</c:when>
									<c:when test="${configDealPay.workPayInfo.relationMethod==1}">
									POS收款
								</c:when>
									<c:when test="${configDealPay.workPayInfo.relationMethod==2}">
									银行转账
								</c:when>
									<c:when test="${configDealPay.workPayInfo.relationMethod==3}">
									支付宝
								</c:when>
									<c:otherwise>
									未知付款方式
								</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose></td>
					<td>${configDealPay.createBy.office.areaName}</td>
					<td>${configDealPay.createBy.office.name}</td>
					<td>${configDealPay.workPayInfo.createBy.name}</td>
					<td><a
						href="${ctx}/work/workDealInfo/statisticalDealPayListShow?dealInfoId=${configDealPay.id}">查看</a>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
