<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商应用计费策略管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.table th,.table td{vertical-align:middle;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		/**
		*显示待绑定计费策略模板
		* @param productId
		 */
		function showChargeAgentTempList(productId){
			var url;
			url = "${ctx}/profile/configChargeAgent/bindList?productId="+productId;
			top.$.jBox.open("iframe:"+url, "计费策略绑定", 800, 600, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v=='ok'){
						location.href="${ctx}/profile/configChargeAgent/list";
					}else{
						location.href="${ctx}/profile/configChargeAgent/list";

				}
				}
			});
		}

		function showChargeAgentTemp(chargeAgentId){
			var url = "${ctx}/profile/configChargeAgent/form?id="+chargeAgentId+"&view=1";
			top.$.jBox.open("iframe:"+url, "计费策略查看", 600, 710, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){

				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="profile:configChargeAgent:edit">
		<li class="active"><a href="${ctx}/profile/configChargeAgent/">代理商应用计费</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="profile:configSupplierProductRelation:view">
		<li><a href="${ctx}/profile/configSupplierProductRelation/">按供应商产品计费</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configProduct" action="${ctx}/profile/configChargeAgent/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>应用名称 ：</label><form:input path="configApp.appName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;&nbsp;&nbsp;
		<input id="btnConfigchargeAgen" class="btn btn-primary" type="button"  onclick="javascript:window.location.href='${ctx}/profile/configChargeAgent/getChargeAgentList'" value="计费策略模板">
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>序号</th>
		<th>应用名称</th>
		<th>产品名称</th>
		<th>已绑定的模板名称</th>
		<shiro:hasPermission name="profile:configChargeAgent:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configProduct" varStatus="config">
			<tr>
				<td>${config.index+1 }</td>
				<!--<td><a href="${ctx}/profile/configChargeAgent/form?id=${configProduct.id}">${configProduct.configApp.appName}-->
				<td>${configProduct.configApp.appName}
				</a></td>
				<td>
				${typeMap[configProduct.productName]}
				<c:if test="${configProduct.productLabel==0 }">(通用)</c:if>	<c:if test="${configProduct.productLabel==1 }">(专用)</c:if>
				</td>
				<td>
					 <c:forEach items="${configProduct.configChargeAgents}" var="chargeAgent" >
						${chargeAgent.tempName}
						<br/>
					</c:forEach> 
				</td>
				<shiro:hasPermission name="profile:configChargeAgent:edit"><td>
    				<!--<a href="${ctx}/profile/configChargeAgent/form?id=${configProduct.id}">计费策略模板绑定</a>-->
				<%-- 	<c:if test="${configProduct.chargeAgentId==null}">
						<a onclick="showChargeAgentTempList(${configProduct.id})"  href="javascript:" class="btn btn-link" style="_padding-top:6px;">计费策略模板绑定</a>
					</c:if> --%>
					
					<a  href="${ctx}/profile/configChargeAgent/bindListNew?productId=${configProduct.id}" class="btn btn-link" style="_padding-top:6px;">计费策略模板绑定</a>
					
					
				<%-- 	<c:if test="${configProduct.chargeAgentId!=null}">
						<a onclick="showChargeAgentTemp(${configProduct.chargeAgentId})"  href="#" class="btn btn-link" style="_padding-top:6px;">查看绑定</a>
					</c:if>
					<c:if test="${configProduct.chargeAgentId!=null}">
						<a onclick="return confirmx('确认要取消绑定该计费策略吗？', this.href)"  href="${ctx}/profile/configChargeAgent/unBindSave?productId=${configProduct.id}" class="btn btn-link" style="_padding-top:6px;">取消绑定</a>
					</c:if> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
