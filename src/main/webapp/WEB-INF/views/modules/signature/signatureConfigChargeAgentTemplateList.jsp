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

		function checkUsed(obj){
			var id = obj;
			top.$.jBox.confirm("确认要删除该计费策略配置吗",'系统提示',function(v,h,f){
				if(v=='ok'){
					$.ajax({
						url:"${ctx}//signature/signatureChargeAgent/checkUsed?agentId="+id+"&_="+new Date().getTime(),
						dataType:'json',
						success:function(data){
							if(data.status == '1'){
								top.$.jBox.tip("该计费策略已经被绑定，不能删除！");
							}else if(data.status == '2'){
								loading('正在提交，请稍等...');
								location = "${ctx}/signature/signatureChargeAgent/deleteByAgentId?agentId="+id;
							}else if(data.status == '0'){
								top.$.jBox.tip("系统异常");
							}
						}
					});
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}
		
		function showChargeAgentTemp(signaturechargeAgentId){
			var url = "${ctx}/signature/signatureChargeAgent/form?id="+signaturechargeAgentId+"&view=1";
			top.$.jBox.open("iframe:"+url, "计费策略查看", 600, 710, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){

				}
			});
		}
		
		function returnBack(){
			window.location.href="${ctx}/signature/signatureChargeAgent/";
		}
		

		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/signature/signatureChargeAgent/getSignatureChargeAgentList">印章计费策略模板列表</a></li>
		<li><a href="${ctx}/signature/signatureChargeAgent/form">印章计费策略模板添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="signatureConfigChargeAgent" action="${ctx}/signature/signatureChargeAgent/getSignatureChargeAgentList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>印章计费策略模板名称 ：</label><form:input path="tempName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" onclick="javascript:returnBack()" type="button" value="返回"/>
		
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>序号</th>
		<th>印章计费策略模板名称</th>
		<shiro:hasPermission name="signature:signatureChargeAgent:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="signatureConfigChargeAgent" varStatus="config">
			<tr>
				<td>${config.index+1 }</td>
				<td>
				<a onclick="showChargeAgentTemp(${signatureConfigChargeAgent.id})"  href="#">
					${signatureConfigChargeAgent.tempName}
				</a></td>
				<shiro:hasPermission name="signature:signatureChargeAgent:edit"><td>
					<a href="${ctx}/signature/signatureChargeAgent/form?id=${signatureConfigChargeAgent.id}">修改</a>
					<a href="#" onclick="checkUsed(${signatureConfigChargeAgent.id});">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
