<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商管理</title>
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
		
		function isUsed(id,type){
			if (type!=0) {
				var url = "${ctx}/profile/configSupplier/used?id="+id+"&type="+type+"&_="+new Date().getTime();
				$.getJSON(url ,function(data){
					if(data.status==0){
						if(data.type==1){
							var submit = function (v, h, f) {
							    if (v == 'ok'){
							    	window.location.href="${ctx}/profile/configSupplier/form?id="+id;
							    }
							    return true; //close
							};
							top.$.jBox.confirm("该供应商'"+data.supplierName+"'已被使用，是否继续进行修改？", "提示", submit);
						}else{
							window.location.href="${ctx}/profile/configSupplier/form?id="+id;
						}
					}else{
						top.$.jBox.tip("系统出现异常！");
					}
				});
			}else {
				window.location.href="${ctx}/profile/configSupplier/form?id="+id;
			}
		}
		
		function deletUsed(id,type){
			if (type!=0) {
				var url = "${ctx}/profile/configSupplier/used?id="+id+"&type="+type+"&_="+new Date().getTime();
				$.getJSON(url ,function(data){
					if(data.status==0){
						if(data.type==1){
							top.$.jBox.tip("该供应商'"+data.supplierName+"'已被使用，请取消绑定使用后再进行删除！");
						}else{
							var submit = function (v, h, f) {
							    if (v == 'ok')
							    	window.location.href="${ctx}/profile/configSupplier/delete?id="+id;
							    return true; //close
							};
							top.$.jBox.confirm("确认要删除该证书'"+data.certName+"'模版吗？", "提示", submit);
						}
					}else{
						top.$.jBox.tip("系统出现异常！");
					}
				});
			}else {
				window.location.href="${ctx}/profile/configSupplier/delete?id="+id;
			}
			
			
		}
		
		
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/profile/configSupplier/">供应商列表</a></li>
		<shiro:hasPermission name="profile:configSupplier:edit"><li><a href="${ctx}/profile/configSupplier/form">供应商添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configSupplier" action="${ctx}/profile/configSupplier/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>供应商名称 ：</label><form:input path="supplierName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>供应商名称</th>
		<th>供应商类型</th>
		<th>供应产品</th>
		<th>联系人</th>
		<th>电话号码</th>
		<th>技术联系人</th>
		<th>技术联系人电话</th>
		<shiro:hasPermission name="profile:configSupplier:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		
		<c:forEach items="${page.list}" var="configSupplier">
			<tr>
				<td><a href="${ctx}/profile/configSupplier/form?id=${configSupplier.id}">${configSupplier.supplierName}</a></td>
				<td>${typeMap[configSupplier.supplierType]}</td>
				<td>
				<c:forEach items="${configSupplier.configSupplierProductRelations}" var="list">
				${productTypeMap[list.productType] }
				</c:forEach>
				</td>
				<td>${configSupplier.supplierCommUsername}</td>
				<td>${configSupplier.supplierCommMobile}</td>
				<td>${configSupplier.supplierTechnicalName}</td>
				<td>${configSupplier.supplierTechnicalMobile}</td>
				
				<shiro:hasPermission name="profile:configSupplier:edit"><td>
				
				<a href="javascript:isUsed(${configSupplier.id},${configSupplier.supplierType})">修改</a>
				<a href="javascript:deletUsed(${configSupplier.id},${configSupplier.supplierType})">删除</a>
    				
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
