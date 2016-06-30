<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>计费策略模版详情历史管理</title>
<meta name="decorator" content="default" />
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
	<div style="overflow:auto;" class="windowHeight" id="scrollBar" >
	<ul class="nav nav-tabs" id="ulId" style="width:100%;">
		<li><a href="${ctx}/profile/configChargeAgent/getChargeAgentList">计费策略模板列表</a></li>
		<li class="active"><a
			href="${ctx}/profile/configChargeAgent/changeChargeAgentInfoList?agentHisId=${agentHisId }">修改记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configChargeAgentHistory"
		action="${ctx}/profile/configChargeAgent/changeChargeAgentInfoList?agentHisId=${agentHisId }"
		method="post" class="breadcrumb form-search" style="width:100%;">
		
		
		<div class="control-group">
			<label>修改人：</label>
			<input type="text" name="createName" value="${createName }"/>
				<label>修改时间 ：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate" required="required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
				
				<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
				
				<%-- <input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" /> --%>
				
				
				
		&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		</div>
		
		
		
		
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</form:form>
	<tags:message content="${message}" />
	
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="vertical-align: middle; text-align: center;" rowspan="3">计费策略模版名称</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="3">模版类型</th>
				<th style="vertical-align: middle; text-align: center;" colspan="14">业务类型</th>
				<th style="vertical-align: middle; text-align: center;" colspan="2">可信移动设备</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="3" >付款方式</th>
				<th style="vertical-align: middle; text-align: center;width:10%;" colspan="2" rowspan="2">配置数量</th>
				<th style="vertical-align: middle; text-align: center;" colspan="2" rowspan="2">剩余数量</th>
				<th style="vertical-align: middle; text-align: center;" colspan="2" rowspan="2">已用数量</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="3">修改人</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="3">开始时间</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="3">截止时间</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="3">修改时间</th>
			</tr>
			<tr>
				<th style="vertical-align: middle; text-align: center;" colspan="5">新增</th>

				<th style="vertical-align: middle; text-align: center;" colspan="5">更新</th>
				<th style="vertical-align: middle; text-align: center;" colspan="2">补办</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="2">变更</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="2">开户费</th>
				<th style="vertical-align: middle; text-align: center;" rowspan="2">半年</th>
				<th style="vertical-align: middle; text-align: center;"  rowspan="2">一年</th>
				
			</tr>
			<tr>
				<th style="vertical-align: middle; text-align: center;">一年</th>
				<th style="vertical-align: middle; text-align: center;">两年</th>
				
				<th style="vertical-align: middle; text-align: center;">三年</th>
				
				<th style="vertical-align: middle; text-align: center;">四年</th>
				<th style="vertical-align: middle; text-align: center;">五年</th>
				<th style="vertical-align: middle; text-align: center;">一年</th>
				<th style="vertical-align: middle; text-align: center;">两年</th>
				
				<th style="vertical-align: middle; text-align: center;">三年</th>
				
				<th style="vertical-align: middle; text-align: center;">四年</th>
				<th style="vertical-align: middle; text-align: center;">五年</th>
				<th style="vertical-align: middle; text-align: center;">遗失补办</th>
				<th style="vertical-align: middle; text-align: center;">损坏更换</th>
				<th style="vertical-align: middle; text-align: center;">新增</th>
				<th style="vertical-align: middle; text-align: center;">更新</th>
				<th style="vertical-align: middle; text-align: center;">新增</th>
				<th style="vertical-align: middle; text-align: center;">更新</th>
				<th style="vertical-align: middle; text-align: center;">新增</th>
				<th style="vertical-align: middle; text-align: center;">更新</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="configChargeAgentHistory">
				<tr>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.tempName }</td>
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle == 1 }">
					标准 
					</c:if> <c:if test="${configChargeAgentHistory.tempStyle == 2 }">
					政府统一采购
					</c:if> <c:if test="${configChargeAgentHistory.tempStyle == 3 }">
					合同采购
					</c:if></td>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.xz1}
					</td>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.xz2}
					</td>
					
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.xz3}
					</td>
					
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.xz4}
					</td>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.xz5}
					</td>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.gx1}
					</td>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.gx2}
					</td>
					
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.gx3}
					</td>
					
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.gx4}
					</td>
					<td style="vertical-align: middle; text-align: center;">${configChargeAgentHistory.map.gx5}
					</td>
					<td style="vertical-align: middle; text-align: center;">

						${configChargeAgentHistory.map.bb0}</td>
					<td style="vertical-align: middle; text-align: center;">
						${configChargeAgentHistory.map.bb1}</td>
					<td style="vertical-align: middle; text-align: center;">
						${configChargeAgentHistory.map.th}</td>
					<td style="vertical-align: middle; text-align: center;">
						${configChargeAgentHistory.map.khf}</td>
					<td style="vertical-align: middle; text-align: center;">
						${configChargeAgentHistory.map.trustDevice0}</td>
					<td style="vertical-align: middle; text-align: center;">
						${configChargeAgentHistory.map.trustDevice1}</td>
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.chargeMethodPos}">
					pos
					</c:if> <c:if test="${configChargeAgentHistory.chargeMethodMoney}">
					&nbsp;&nbsp;&nbsp;&nbsp;现金缴费
					</c:if> <c:if test="${configChargeAgentHistory.chargeMethodBank}">
					&nbsp;&nbsp;&nbsp;&nbsp;银行转账
					</c:if></td>
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle != 1 }">
					${configChargeAgentHistory.configureNum }
					</c:if></td>
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle != 1 }">
					${configChargeAgentHistory.configureUpdateNum }
					</c:if></td>
					
					
					
					
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle != 1 }">
						${configChargeAgentHistory.surplusNum }
						</c:if></td>
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle != 1 }">
						${configChargeAgentHistory.surplusUpdateNum }
						</c:if></td>	
					
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle != 1 }">
						${configChargeAgentHistory.availableNum + configChargeAgentHistory.reserveNum }
						</c:if></td>
					<td style="vertical-align: middle; text-align: center;"><c:if
							test="${configChargeAgentHistory.tempStyle != 1 }">
						${configChargeAgentHistory.availableUpdateNum + configChargeAgentHistory.reserveUpdateNum }
						</c:if></td>
						
					<td style="vertical-align: middle; text-align: center;">
						${configChargeAgentHistory.createBy.name }
					</td>
					<td style="vertical-align: middle; text-align: center;">
						<fmt:formatDate value="${configChargeAgentHistory.htStartTime}" pattern="yyyy-MM-dd"/>
					</td>
					<td style="vertical-align: middle; text-align: center;">
						<fmt:formatDate value="${configChargeAgentHistory.htEndTime}" pattern="yyyy-MM-dd"/>
					</td>
					<td style="vertical-align: middle; text-align: center;">
						<fmt:formatDate value="${configChargeAgentHistory.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	<div class="pagination">${page}</div>
</body>
</html>
