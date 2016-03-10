<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>年费结算保存管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var windowH=$(window).height();
			$('.windowHeight').height(windowH);
			$("#scrollBar").scroll(function(){
				var tableWidth=$("#contentTable").width();
				var formWidth=$("#searchForm").width();
				var leftWidth=$("#scrollBar").scrollLeft();
				if((tableWidth-formWidth)-leftWidth>0)
					{
						$("#searchForm").css("margin-left",leftWidth);
						$("#ulId").css("margin-left",leftWidth);
					}
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function setApp(obj){
			var comAgentId = obj.value;
			var url = "${ctx}/settle/settlementLog/setApps?comAgentId="+comAgentId + "&_="+new Date().getTime();
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
	</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/settle/settlePayableDetail/list">年限结算表</a></li>
		<li class="active"><a href="${ctx}/settle/settlementLog/">年费结算保存列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="settlementLog" action="${ctx}/settle/settlementLog/" method="post" class="breadcrumb form-search">
<%-- 		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/> --%>
<%-- 		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> --%>
			<div class="control-group">
			<label>代理商 ：</label>
			<select onchange="setApp(this)" name="comAgentId" id="comAgentId" class="required" >
				<option value="">请选择</option>
				<c:forEach items="${comAgents }" var="comAgent" >
					<option value="${comAgent.id }" <c:if test="${comAgent.id == comAgentId}">
					selected="selected"
					</c:if> >${comAgent.agentName }</option>
				</c:forEach>
			</select>
			<label>应用名称 ：</label> 
			<select id="appId" name ="appId">
				<option value="">请选择</option>
				<c:forEach items="${relationByComAgentId }" var="app" >
					<option value="${app.configApp.id }" <c:if test="${app.configApp.id == appId}">
					selected="selected"
					</c:if> >${app.configApp.appName }</option>
				</c:forEach>
			</select>
<!-- 		</div> -->
<!-- 		<div style="width: 800px">                  -->
			   <label>结算时间：</label>
				 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/>至
				<input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" id="endTime"/>
<!-- 				<input id="btnExport" class="btn btn-primary" onclick="searchForm()" -->
<!-- 				type="button" value="查询" /> -->
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>代理商合同有效期</th>
				<th>代理商</th>
				<th>应用名称</th>
				<th>产品名称</th>
				<th>业务办理起始时间</th>
				<th>业务办理结束时间</th>
				<th>结算人员</th>
				<th>结算时间</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page}" var="settlementLog">
				<tr>
					<td>${settlementLog.comagentValidity}</td>
					<td>${settlementLog.comagentName}</td>
					<td>${settlementLog.appName}</td>
					<td>
						<c:set var="prductName" value="${fn:split(settlementLog.productName,' ')}" />
						<c:forEach items="${prductName}" var="prductName">
							${proType[prductName]}
						</c:forEach>
					</td>
					<td>${settlementLog.startTime}</td>
					<td>${settlementLog.endTime}</td>
					<td>${settlementLog.userName}</td>
					<td>${settlementLog.createDate}</td>
					<td>${settlementLog.remarks}</td>
					<td><a href="${ctx}/settle/settlementLog/see?id=${settlementLog.id}">查看详细</a></td>
				</tr>			
		</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>