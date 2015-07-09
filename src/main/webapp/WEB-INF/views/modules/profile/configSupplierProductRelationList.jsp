<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商产品配置管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		
		<shiro:hasPermission name="profile:configChargeAgent:edit">
		
		<li><a href="${ctx}/profile/configChargeAgent/">代理商应用计费</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="profile:configSupplierProductRelation:view">
		<li class="active"><a href="${ctx}/profile/configSupplierProductRelation/">按供应商产品计费</a></li>
		</shiro:hasPermission>
		
		
		
		
	</ul>
	<form:form id="searchForm" modelAttribute="configSupplierProductRelation" action="${ctx}/profile/configSupplierProductRelation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>产品名称 ：</label>
			<form:select path="productType">
				<form:option value="">请选择</form:option>
				<c:forEach items="${proList }" var="pro">
					<form:option value="${pro.id }">${pro.name }</form:option>
				</c:forEach>
			</form:select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>序号</th>
		<th>供应商名称</th>
		<th>产品名称</th>
		<shiro:hasPermission name="profile:configSupplierProductRelation:edit"><th>操作</th></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configSupplierProductRelation" varStatus="sta">
			<tr>
				<td>${sta.index+1 }</td>
				<td><a href="${ctx}/profile/configSupplierProductRelation/form?id=${configSupplierProductRelation.id}">${configSupplierProductRelation.configSupplier.supplierName}</a></td>
				<td>
				${productType[configSupplierProductRelation.productType] }
				</td>
				<shiro:hasPermission name="profile:configSupplierProductRelation:edit"><td>
    				<a href="${ctx}/profile/configSupplierProductRelation/form?id=${configSupplierProductRelation.id}">计费配置</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
