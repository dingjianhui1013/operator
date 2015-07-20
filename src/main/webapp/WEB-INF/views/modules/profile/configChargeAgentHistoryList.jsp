<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>计费策略模版详情历史管理</title>
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a
			href="${ctx}/profile/configChargeAgent/getChargeAgentList">计费策略模板列表</a></li>
		<li class="active"><a
			href="${ctx}/profile/configChargeAgent/changeChargeAgentInfoList?agentHisId=${agentHisId }">计费策略模版详情历史列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configChargeAgentHistory"
		action="${ctx}/profile/configChargeAgent/changeChargeAgentInfoList?agentHisId=${agentHisId }"
		method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed"  > 
		<thead >
			<tr >
				<th style="vertical-align: middle;text-align: center;"  rowspan="3">计费策略模版名称</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="3">模版类型</th>
				<th style="vertical-align: middle;text-align: center;" colspan="12">业务类型</th>
				<th style="vertical-align: middle;text-align: center;" colspan="2" >可信移动设备</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="3" >缴费方式</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="3">配置数量</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="3">剩余数量</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="3">已用数量</th>
			</tr>
			<tr>
				<th style="vertical-align: middle;text-align: center;" colspan="4">新增</th>
				
				<th style="vertical-align: middle;text-align: center;" colspan="4">更新</th>
				<th style="vertical-align: middle;text-align: center;" colspan="2">补办</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="2">变更</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="2">开户费</th>
				<th style="vertical-align: middle;text-align: center;" rowspan="2">半年</th>
				<th style="vertical-align: middle;text-align: center;"  rowspan="2">一年</th>
			</tr>
			<tr>
				<th style="vertical-align: middle;text-align: center;">一年</th>
				<th style="vertical-align: middle;text-align: center;">两年</th>
				<th style="vertical-align: middle;text-align: center;">四年</th>
				<th style="vertical-align: middle;text-align: center;">五年</th>
				<th style="vertical-align: middle;text-align: center;">一年</th>
				<th style="vertical-align: middle;text-align: center;">两年</th>
				<th style="vertical-align: middle;text-align: center;">四年</th>
				<th style="vertical-align: middle;text-align: center;">五年</th>
				<th style="vertical-align: middle;text-align: center;">遗失补办</th>
				<th style="vertical-align: middle;text-align: center;">损坏更换</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="configChargeAgentHistory">
				<tr >
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.tempName }</td>
					<td style="vertical-align: middle;text-align: center;">
					<c:if test="${configChargeAgentHistory.tempStyle == 1 }">
					标准 
					</c:if>
					<c:if test="${configChargeAgentHistory.tempStyle == 2 }">
					政府统一采购
					</c:if>
					<c:if test="${configChargeAgentHistory.tempStyle == 3 }">
					合同采购
					</c:if>
					
					</td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.xz1}   </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.xz2}  </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.xz4}  </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.xz5}  </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.gx1} </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.gx2} </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.gx4} </td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.map.gx5} </td>
					<td style="vertical-align: middle;text-align: center;"> 
					
					${configChargeAgentHistory.map.bb0}
					</td>
					<td style="vertical-align: middle;text-align: center;">
					${configChargeAgentHistory.map.bb1}
					
					</td>
					<td style="vertical-align: middle;text-align: center;">
					${configChargeAgentHistory.map.th}
					
					</td>
					<td style="vertical-align: middle;text-align: center;">
					${configChargeAgentHistory.map.khf}
					
					</td>
					<td style="vertical-align: middle;text-align: center;">
					${configChargeAgentHistory.map.trustDevice0}
					</td>
					<td style="vertical-align: middle;text-align: center;">
					${configChargeAgentHistory.map.trustDevice1}
					
					</td>
					<td style="vertical-align: middle;text-align: center;">
					<c:if test="${configChargeAgentHistory.chargeMethodPos}">
					pos
					</c:if>
					<c:if test="${configChargeAgentHistory.chargeMethodMoney}">
					&nbsp;&nbsp;&nbsp;&nbsp;现金缴费
					</c:if>
					<c:if test="${configChargeAgentHistory.chargeMethodBank}">
					&nbsp;&nbsp;&nbsp;&nbsp;银行转账
					</c:if>
					</td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.configureNum }</td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.surplusNum }</td>
					<td style="vertical-align: middle;text-align: center;">${configChargeAgentHistory.availableNum }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
