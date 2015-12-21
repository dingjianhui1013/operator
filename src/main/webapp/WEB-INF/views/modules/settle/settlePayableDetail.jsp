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
			<select onchange="setApp(this)" name="comAgent" >
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
		
	</div>
</body>
</html>
