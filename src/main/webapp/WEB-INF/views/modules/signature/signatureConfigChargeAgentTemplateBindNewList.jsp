<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>印章计费策略模板管理</title>
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
		
		function checkPD(appId,agentId){
			$("#"+agentId+"").attr("href","javascript:void()");
			var url = "${ctx}/signature/signatureChargeAgent/boundAgentApp?appId="+appId+"&agentId="+agentId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if (data.status=="1") {
					top.$.jBox.tip("绑定成功！");
					window.location.href="${ctx}/signature/signatureChargeAgent/bindListNew?appId="+appId;
				}else{
					top.$.jBox.tip("系统异常！绑定失败！");
					$("#"+agentId+"").attr("href",href);
				}
			});		
		}
		
		
		
	
		function isBound(obj){
			var boundId = obj;
			var url = "${ctx}/signature/signatureChargeAgent/unBoundAgentApp?boundId="+boundId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if(data.status=="-1"){
					top.$.jBox.tip("该模板已办理过业务,无法取消绑定！");
				}else if (data.status=="1") {
					top.$.jBox.tip("取消绑定成功！");
					window.location.href="${ctx}/signature/signatureChargeAgent/bindListNew?appId=${appId}";
				}else{
					top.$.jBox.tip("系统异常！取消绑定失败！");
				}
			});
		}
		
		function showChargeAgentTemp(signaturechargeAgentId){
			var url = "${ctx}/signature/signatureChargeAgent/form?id="+signaturechargeAgentId+"&view=1";
			top.$.jBox.open("iframe:"+url, "计费策略查看", 600, 710, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){

				}
			});
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="signature:signatureChargeAgent:edit">
		<li><a href="${ctx}/signature/signatureChargeAgent/">印章计费</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="signature:signatureChargeAgent:view">
		<li class="active"><a href="${ctx}/signature/signatureChargeAgent/bindListNew?appId=${appId}">印章计费策略模版绑定</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="signatureConfigChargeAgent" 
	action="${ctx}/signature/signatureChargeAgent/bindListNew?appId=${appId}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>印章计费策略模板名称 ：</label>
		<form:input path="tempName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>序号</th>
		<th>计费策略模板名称</th>
		<shiro:hasPermission name="signature:signatureChargeAgent:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="signatureConfigChargeAgent" varStatus="config">
			<tr>
				<td>${config.index+1 }</td>
				<td>
				
				<a onclick="showChargeAgentTemp(${signatureConfigChargeAgent.id})"  href="#">
					${signatureConfigChargeAgent.tempName}
				</a>
				
				<shiro:hasPermission name="signature:signatureChargeAgent:edit">
				<td>
					
				<c:if test="${signatureConfigChargeAgent.isBind==2 }">
				
				
				<a href="javascript:isBound(${signatureConfigChargeAgent.boundId})">取消绑定</a>
				
				
				
				
				</c:if>
				<c:if test="${signatureConfigChargeAgent.isBind==1 }">
			
				<a id="${signatureConfigChargeAgent.id}" href="javascript:checkPD(${appId},${signatureConfigChargeAgent.id})">绑定</a>
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
