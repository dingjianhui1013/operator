<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>代理商管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			submitHandler : function(form) {
				loading('正在提交，请稍等...');
				form.submit();
			},
		});
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function showTable_f() {
		document.showTable.submit();
	}

	function judgement() {
		var i = 0;
		$("#startmes").html("");
		$("#endmes").html("");
		if (i == 1) {
			return false;
		}
	}
	function onChange(){
		var supplierId = $("#supplierId").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		if (startTime==null||startTime=='') {
			top.$.jBox.tip("请选择开始时间！");
			return;
		}
		if (endTime==null||endTime=='') {
			top.$.jBox.tip("请选择结束时间！");
			return;
		}
		window.location.href="${ctx }/settle/supplierSettle/exportDZ?startTime="+startTime+"&endTime="+endTime+"&supplierId="+supplierId;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">四川CA对账统计表</a></li>
		<li><a href="${ctx}/settle/supplierSettle/showTableT">付款结算清单</a></li>
		<li><a href="${ctx}/settle/settleKey/list">供应KEY数量统计</a></li>
	</ul>
	<br />
	<form:form id="searchForm" modelAttribute="configSupplier"
		action="${ctx}/settle/supplierSettle/showTableF" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>供应商名称：</label>
		<form:select path="id" id="supplierId">
			<c:forEach items="${liers }" var="lier">
				<form:option value="${lier.id }">${lier.supplierName }</form:option>
			</c:forEach>
		</form:select>
		<label>结算时间 ：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="${startTime}" required="required" />
		<label id="startmes" style="color: red;"></label>&nbsp;-&nbsp;
				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="${endTime}" required="required" />
		<label id="endmes" style="color: red;"></label>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
			&nbsp;<a href="javascript:onChange()"   class="btn btn-primary">导出</a>
	</form:form>
	<tags:message content="${message}"/>
	<div class="form-horizontal">
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="14"><h3>四川CA对账统计表</h3></th>
				</tr>
			</thead>
			<tr>
				<th></th>
				<th colspan="13">时间：${startTime } — ${endTime}</th>
			</tr>
			<tr>
				<th>类别</th>
				<th>证书类型</th>
				<th>总签发证书<br />数量
				</th>
				<th>1替换证书<br />数量
				</th>
				<th>2吊销证书<br />数量
				</th>
				<th>3测试证书<br />数量
				</th>
				<th>4因KEY原因<br />(非人为)补<br />签证书数量
				</th>
				<th>5因KEY原因<br />(人为)补<br />签证书数量
				</th>
				<th>6因变更用<br />户信息补签<br />证书数量
				</th>
				<th>一年期<br />有效证<br />书数量
				</th>
				<th>两年期<br />有效证<br />书数量
				</th>
				<th>四年期<br />有效证<br />书数量
				</th>
				<th>五年期<br />有效证<br />书数量
				</th>
				<th>备注</th>
			</tr>
			<c:forEach items="${suppliers }" var="supplier">
			<tr>
					<th>${supplier.OU}</th>
					<%-- <th>${productType[supplier.PRODUCT_TYPE]}</th> --%>
					<th>
						<c:if test='${supplier.PRODUCT_TYPE=="1"}'>企业证书</c:if>
						<c:if test='${supplier.PRODUCT_TYPE=="2"}'>个人证书(企业)</c:if> 
						<c:if test='${supplier.PRODUCT_TYPE=="3"}'>机构证书</c:if> 
						<c:if test='${supplier.PRODUCT_TYPE=="4"}'>可信移动设备</c:if> 
						<%-- <c:if test='${supplier.PRODUCT_TYPE=="5"}'>电子签章</c:if> --%>
						<c:if test='${supplier.PRODUCT_TYPE=="6"}'>个人证书(机构)</c:if> 
					</th>
					<th>
						<c:set var="totalAmount" value = "${totalAmount+supplier.TOTAL_AMOUNT}"></c:set>		
					${supplier.TOTAL_AMOUNT }</th>
					<th>
						<c:set var="replaceAmount" value = "${replaceAmount+supplier.REPLACE_AMOUNT}"></c:set>		
					${supplier.REPLACE_AMOUNT }</th>
					<th>
						<c:set var="revokeAmount" value = "${revokeAmount+supplier.REVOKE_AMOUNT}"></c:set>
					${supplier.REVOKE_AMOUNT }</th>
					<th>
						<c:set var="testAmount" value = "${testAmount+supplier.TEST_AMOUNT}"></c:set>
					${supplier.TEST_AMOUNT }</th>
					<th>
						<c:set var="frwBbAmount" value = "${frwBbAmount+supplier.FRW_BB_AMOUNT}"></c:set>
					${supplier.FRW_BB_AMOUNT }</th>
					<th>
						<c:set var="rwBbAmount" value = "${rwBbAmount+supplier.RW_BB_AMOUNT}"></c:set>
					${supplier.RW_BB_AMOUNT }</th>
					<th>
						<c:set var="changeAmount" value = "${changeAmount+supplier.CHANGE_AMOUNT}"></c:set>
					${supplier.CHANGE_AMOUNT }</th>
					<th>
						<c:set var="year1" value = "${year1+supplier.YEAR1}"></c:set>
					${supplier.YEAR1 }</th>
					<th>
						<c:set var="year2" value = "${year2+supplier.YEAR2}"></c:set>
					${supplier.YEAR2 }</th>
					<th>
						<c:set var="year4" value = "${year4+supplier.YEAR4}"></c:set>
					${supplier.YEAR4 }</th>
					<th>
						<c:set var="year5" value = "${year5+supplier.YEAR5}"></c:set>
					${supplier.YEAR5 }</th>
					<th></th>
				</tr>
				<%-- <tr>
					<th>${supplier.OU }</th>
					<th>${WorkDealInfoType[supplier.product_type]}</th>
					<th>
						<c:set var="totalAmount" value = "${totalAmount+supplier.total_amount}"></c:set>		
					${supplier.total_amount }</th>
					<th>
						<c:set var="replaceAmount" value = "${replaceAmount+supplier.replace_amount}"></c:set>		
					${supplier.replace_amount }</th>
					<th>
						<c:set var="revokeAmount" value = "${revokeAmount+supplier.revoke_amount}"></c:set>
					${supplier.revoke_amount }</th>
					<th>
						<c:set var="testAmount" value = "${testAmount+supplier.test_amount}"></c:set>
					${supplier.test_amount }</th>
					<th>
						<c:set var="frwBbAmount" value = "${frwBbAmount+supplier.frw_bb_amount}"></c:set>
					${supplier.frw_bb_amount }</th>
					<th>
						<c:set var="rwBbAmount" value = "${rwBbAmount+supplier.rw_bb_amount}"></c:set>
					${supplier.rw_bb_amount }</th>
					<th>
						<c:set var="changeAmount" value = "${changeAmount+supplier.change_amount}"></c:set>
					${supplier.change_amount }</th>
					<th>
						<c:set var="year1" value = "${year1+supplier.year1}"></c:set>
					${supplier.year1 }</th>
					<th>
						<c:set var="year2" value = "${year2+supplier.year2}"></c:set>
					${supplier.year2 }</th>
					<th>
						<c:set var="year4" value = "${year4+supplier.year4}"></c:set>
					${supplier.year4 }</th>
					<th></th>
					<th>
						<c:set var="year5" value = "${year5+supplier.year5}"></c:set>
					${supplier.year5 }</th>
					<th></th>
				</tr> --%>
			</c:forEach>
			<tr>
				<th>小计</th>
				<th></th>
				<th>${totalAmount }</th>
				<th>${replaceAmount }</th>
				<th>${revokeAmount }</th>
				<th>${testAmount }</th>
				<th>${frwBbAmount }</th>
				<th>${rwBbAmount }</th>
				<th>${changeAmount }</th>
				<th>${year1 }</th>
				<th>${year2 }</th>
				<th>${year4 }</th>
				<th>${year5 }</th>
				<th></th>
			</tr>
			<!-- <tr>
				<th rowspan="2">VIN服务器<br/>证书证书</th>
				<th colspan="12">成都市劳动保障信息中心&nbsp;&nbsp;&nbsp;数量：&nbsp; &nbsp;&nbsp;&nbsp;新增，域名：https://gr.cdhrss.gov.cn&nbsp;实施时间：2013-10-11</th>
			</tr>
			<tr>
				<th colspan="12">成都市软件产业发展中心&nbsp;&nbsp;&nbsp;数量：&nbsp; &nbsp;&nbsp;&nbsp;更新，域名：https://www.cdcredit.com.cn&nbsp;实施时间：2013-9-27</th>
			</tr>
			<tr>
				<th>小计</th>
				<th colspan="12"></th>
			</tr>
			<tr>
				<th rowspan="2">CTN服务器<br/>证书证书</th>
				<th colspan="12" height="25px"></th>
			</tr>
			<tr>
				<th colspan="12" height="25px"></th>
			</tr>
			<tr>
				<th>小计</th>
				<th colspan="12"></th>
			</tr> -->
		</table>
	</div>
	<form action="${ctx}/settle/agentSettle/showT" name="showTable"
		id="showTable">
		<input id="showTable_officeId" type="hidden" name="officeId"
			value="${officeId }" /> <input id="showTable_agentId" type="hidden"
			name="id" value="${id }" /> <input id="showTable_startTime"
			type="hidden" name="startDate" value="${startDate }" /> <input
			id="showTable_endTime" type="hidden" name="endDate"
			value="${endDate }" />
	</form>
</body>
</html>
