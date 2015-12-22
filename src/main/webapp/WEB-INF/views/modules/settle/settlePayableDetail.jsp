<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>代理商管理</title>
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
	
	function setApp(obj){
		var comAgentId = obj.value;
		var url = "${ctx}/settle/settlePayableDetail/setApps?comAgentId="+comAgentId + "&_="+new Date().getTime();
		$.getJSON(url, function(data) {
			var html = "";
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#appId").html(html);
		});
	}
	
	function setProduct(obj){
		var appId = obj.value;
		var url = "${ctx}/settle/settlePayableDetail/setProducts?appId="+appId + "&_="+new Date().getTime();
		$.getJSON(url, function(data) {
			var html = "";
			$.each(data, function(idx, ele) {
				html += "<input name='productIds' type='checkbox' value='"+ele.id+"'\">" + ele.name
			});

			$("#productId").html(html);
		});
	}
	

</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/settle/agentSettle/list">年限结算表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/settle/settlePayableDetail/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="control-group">
			<label><font color="red" >*</font>代理商 ：</label>
			<select onchange="setApp(this)" name="comAgentId" >
				<option value="0">请选择</option>
				<c:forEach items="${comAgents }" var="comAgent" >
					<option value="${comAgent.id }">${comAgent.agentName }</option>
				</c:forEach>
			</select>
			
			<label>应用名称 ：</label> 
			<select id="appId" name ="appId" onchange="setProduct(this)">
				<option value="0">请选择</option>
			</select>
			<label>产品名称 ：</label><label id="productId"></label>
		</div>
		<div style="width: 800px">                 
			<label>业务办理时间：</label>	    
				 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" />
		  	<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" />
				&nbsp; <input id="btnExport" class="btn btn-primary"
				type="button" value="导出" />
		</div>
	
	
		
	</form:form>
	<tags:message content="${message}" />	
	<div class="form-horizontal" >
		<table id="contentTable"
					class="table table-striped table-bordered table-condensed" style="width: 60%">
					<tr>
						<th colspan="${7+lenth*4 }" >统计周期：<fmt:formatDate pattern="yyyy-MM-dd" value="${startTime}"/>&nbsp;-&nbsp;<fmt:formatDate pattern="yyyy-MM-dd" value="${endTime}"/></th>
					</tr>
					<tr>
						<th colspan="${7+lenth*4 }">本期结算证书年限：（本次结算年数总数）</th>
					</tr>
					<tr>
						<th rowspan="2">序号</th>
						<th rowspan="2">单位名称</th>
						<th rowspan="2">经办人姓名</th>
						<th rowspan="2">产品名称</th>
						<c:forEach var="a" begin="1" end="${lenth}">
						
						<th colspan="4">第${a}次结算</th>
						</c:forEach>
						
						<th colspan="3">结算年限统计</th>
					</tr>
					<tr>
						
						<c:forEach begin="1" end="${lenth}">
						<th>起始时间</th>
						<th>结束时间</th>
						<th>业务类型</th>
						<th>结算(年)</th>
						</c:forEach>
						<th>已结算（年）</th>
						<th>本期结算（年）</th>
						<th>剩余结算（年）</th>
					</tr>
					<c:forEach items="${dealInfos }" var="dealInfo" varStatus="status">
						<tr>
							<td>${status.index + 1}</td>
							<td>${dealInfo.workCompany.companyName}</td>
							<td>${dealInfo.workCertInfo.workCertApplyInfo.name}</td>
							<td>${dealInfo.configProduct.productName}</td>
							<c:forEach items="${dealInfo.detailList }" var="detail">
								<td><fmt:formatDate	value="${detail.startDate }" pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatDate value="${detail.endDate }" pattern="yyyy-MM-dd" /></td>
								<td>${detail.dealInfoType }</td>
								<td>${detail.settleYear }</td>
							</c:forEach>
							<c:forEach begin="1" end="${lenth - dealInfo.detailList.size() }">
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:forEach>
							<td>${dealInfo.yyNum}</td>
							<td>${dealInfo.yyNum}</td>
							<td>${dealInfo.totalNum-dealInfo.yyNum}</td>
						</tr>
					</c:forEach>
				</table>
		
	
	
		
	</div>
</body>
</html>
