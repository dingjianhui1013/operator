<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商应用计费策略模板管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		
		function checkPD(productId,agentId){
			var href = $("#"+agentId+"").attr("href");
			$("#"+agentId+"").attr("href","javascript:void()");
			var proId = productId;
			var url = "${ctx}/profile/configChargeAgent/checkBound?proId="+proId+"&agentId="+agentId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if (data.isBZ=="0") {
					top.$.jBox.tip(data.msg);
					$("#"+agentId+"").attr("href",href);
				}else{
					if (data.isNum=="0") {
						top.$.jBox.tip(data.msg);
						$("#"+agentId+"").attr("href",href);
					}else{
						window.location.href="${ctx}/profile/configChargeAgent/bindingNew?productId=${productId}&agentId="+agentId;
					}
					
				}
			});		
		}
		
		function showChargeAgentTemp(chargeAgentId){
			var url = "${ctx}/profile/configChargeAgent/form?id="+chargeAgentId+"&view=1";
			top.$.jBox.open("iframe:"+url, "计费策略查看", 600, 690, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){

				}
			});
		}
	
		function isBound(obj){
			var productId = "${productId}";
			var agentId = obj;
			var url = "${ctx}/profile/configChargeAgent/isBoundCheck?agentId="+agentId+"&productId="+productId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if(data.isUsed=="yes"){
					top.$.jBox.tip("该模版也已经办理过业务，不能取消绑定！");
				}else{
					window.location.href="${ctx}/profile/configChargeAgent/deleteBindingNew?productId="+productId+"&agentId="+agentId;
					
				}
			});
			
			
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="profile:configChargeAgent:edit">
		<li><a href="${ctx}/profile/configChargeAgent/">代理商应用计费</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="profile:configChargeAgent:view">
		<li class="active"><a href="${ctx}/profile/configChargeAgent/bindListNew?productId=${productId}">计费策略模版绑定</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configChargeAgent" 
	action="${ctx}/profile/configChargeAgent/bindListNew?productId=${productId}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>计费策略模板名称 ：</label>
		<form:input path="tempName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>序号</th>
		<th>计费策略模板名称</th>
		<th>计费策略模板类型</th>
		<shiro:hasPermission name="profile:configChargeAgent:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configChargeAgent" varStatus="config">
			<tr>
				<td>${config.index+1 }</td>
				<td><a onclick="showChargeAgentTemp(${configChargeAgent.id})"  href="#">${configChargeAgent.tempName}</a></td>
				<td>
					<c:if test="${configChargeAgent.tempStyle == 1}">标准</c:if>
					<c:if test="${configChargeAgent.tempStyle == 2}">政府统一采购</c:if>
					<c:if test="${configChargeAgent.tempStyle == 3}">合同采购</c:if>
				</td>
				
				<shiro:hasPermission name="profile:configChargeAgent:edit">
				<td>
					<%-- <a href="${ctx}/profile/configChargeAgent/bindSave?productId=${productId}&chargeAgentId=${configChargeAgent.id}">绑定</a> --%>
				&nbsp;&nbsp;
				<c:if test="${configChargeAgent.isBind==2 }">
				
				
				<a href="javascript:isBound(${configChargeAgent.id})">取消绑定</a>
				
				
				
				
				</c:if>
				<c:if test="${configChargeAgent.isBind==1 }">
				<%-- <a href="${ctx}/profile/configChargeAgent/bindingNew?productId=${productId}&agentId=${configChargeAgent.id}">绑定</a> --%>
				<a id="${configChargeAgent.id}" href="javascript:checkPD(${productId},${configChargeAgent.id})">绑定</a>
				</c:if>
				
				
				</td>
				</shiro:hasPermission>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
