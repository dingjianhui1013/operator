<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>支付信息统计</title>
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
	function onSubmit(){
		var areaId = $("#area").prop('value');
		var officeId = $("#office").prop('value');
		if (areaId != "" && officeId == "") {
			top.$.jBox.tip("请先选择网点！");
		}
	}
	function exportExcel(){
		var url = "${ctx }/work/workFinancePayInfoRelation/exportData";
		$.getJSON(url, function(data) {
			top.$.jBox.tip(data.msg);
		});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workFinancePayInfoRelation/statisticalList">支付信息统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workPayInfo"
		action="${ctx}/work/workFinancePayInfoRelation/statisticalList"
		method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />


		<div style="margin-top: 9px">
		<label>选择区域 ：</label>
		<select name="area" id="area" onchange="addOffice()">
			<c:if test="${offsListSize!=1 }">
		
			<option value="">请选择</option>
			</c:if>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>

		<select name="office" id="office">
			<c:if test="${offsListSize!=1 }">
				<option value="">请选择</option>
			</c:if>
			<c:forEach items="${offices}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==office}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
			 </div>
			 <div style="margin-top: 9px">
		<label>付款方式 ：</label>
		
		<select name="payMethod" id="payMethod">
				<option value=""  <c:if test="${payMethod==null}"> selected="selected" </c:if> >全部</option>	
				<option value="1"  <c:if test="${payMethod=='1'}"> selected="selected" </c:if>>现金</option>
				<option value="2"  <c:if test="${payMethod=='2'}"> selected="selected" </c:if>>POS收款</option>
				<option value="3"  <c:if test="${payMethod=='3'}"> selected="selected" </c:if>>银行转帐</option>
				<option value="4"  <c:if test="${payMethod=='4'}"> selected="selected" </c:if>>支付宝转账</option>
			</select> 
		
	
		
		<label>付款时间：</label>

		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
					&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return onSubmit()"
			value="查询" />
			&nbsp;<a href="${ctx }/work/workFinancePayInfoRelation/exportData" class="btn btn-primary">导出</a>
			</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>导入数据共有 ${count} 条</th>
				<th>金额有剩余共有 ${money } 条</th>
				<th colspan="9">金额无剩余共有  ${moneyIsNull} 条</th>
			</tr>
			<tr>
				<th>付款单位名称</th>
				<th>付款金额/元</th>
				<th>未确认款项/元</th>
				<th>联系人</th>
				<th>联系方式</th>
				<th>付款时间</th>
				<th>付款方式</th>
				<th>记录方式</th>
				<th>记录人员</th>
				<th>记录时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="financePayInfo">
				<tr>
					<td>${financePayInfo.company}</td>
					<td><fmt:formatNumber value="${financePayInfo.paymentMoney-financePayInfo.residueMoney}" type="number"/></td>
					<td><fmt:formatNumber value="${financePayInfo.residueMoney}" type="number"/></td>
					<td>${financePayInfo.commUserName}</td>
					<td>${financePayInfo.commMobile}</td>
					<td>
					<fmt:formatDate value="${financePayInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td><c:if test="${financePayInfo.paymentMethod==1}">现金</c:if>
						<c:if test="${financePayInfo.paymentMethod==2}">POS收款</c:if>
						<c:if test="${financePayInfo.paymentMethod==3}">银行转账</c:if>
						<c:if test="${financePayInfo.paymentMethod==4}">支付宝转账</c:if>
					</td>
					<td>
						<c:choose>
							<c:when test="${financePayInfo.distinguish==0}">手动添加</c:when>
							<c:otherwise>批量导入</c:otherwise>
						</c:choose>
					</td>
					<td>${financePayInfo.createBy.name}</td>
					<td>
					<fmt:formatDate value="${financePayInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td><a
						href="${ctx}/work/workDealInfo/financeList?financePaymentInfoId=${financePayInfo.id}">查看</a>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
