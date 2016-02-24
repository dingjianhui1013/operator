<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>签章计费策略管理</title>
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
		<shiro:hasPermission name="signature:signatureChargeAgent:edit">
		<li class="active"><a href="${ctx}/signature/signatureChargeAgent/">签章计费</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="signature:signatureChargeAgent:edit">
		<li><a href="${ctx}/signature/signatureChargeAgent/getSignatureChargeAgentList">签章计费策略模板</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configApp" action="${ctx}/signature/signatureChargeAgent/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>应用名称 ：</label><form:input path="appName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>序号</th>
		<th>应用名称</th>
		<th>签章计费模板类型</th>
		<shiro:hasPermission name="signature:signatureChargeAgent:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configApp" varStatus="config">
			<tr>
				<td>${config.index+1 }</td>
			
				<td>${configApp.appName}</td>
				<td>
			
				</td>
				<shiro:hasPermission name="signature:signatureChargeAgent:edit"><td>
    				
					
					<a  href="${ctx}/signature/signatureChargeAgent/bindListNew?appId=${configApp.id}" class="btn btn-link" style="_padding-top:6px;">签章计费策略模板绑定</a>
					
					
			
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
