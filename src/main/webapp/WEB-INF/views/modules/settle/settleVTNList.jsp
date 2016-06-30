<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>VTN管理管理</title>
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/settlevtn/settleVTN/">VTN管理列表</a></li>
		<shiro:hasPermission name="settlevtn:settleVTN:edit">
			<li><a href="${ctx}/settlevtn/settleVTN/form">VTN管理添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="settleVTN"
		action="${ctx}/settlevtn/settleVTN/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>产品名称：</label>
		<form:input path="productName" htmlEscape="false" maxlength="50"
			class="input-medium" />
		<label>应用名称：</label>
		<form:input path="appName" htmlEscape="false" maxlength="50"
			class="input-medium" />
			<br/><br/>
		<label>创建时间：</label>
			<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		&nbsp;&nbsp;&nbsp;&nbsp;
		
		<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>产品名称</th>
				<th>应用名称</th>
				<th>单位</th>
				<th>数量</th>
				<th>单价/元</th>
				<th>小计/元</th>
				<th>创建时间</th>
				<shiro:hasPermission name="settlevtn:settleVTN:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="settleVTN">
				<tr>
					<td><a
						href="${ctx}/settlevtn/settleVTN/form?id=${settleVTN.id}">${settleVTN.productName}</a></td>
					<td>${settleVTN.appName}</td>
					<td>张</td>
					<td>${settleVTN.count }</td>
					<td>${settleVTN.price }</td>
					<td>${settleVTN.countPrice }</td>
					<td>
					<fmt:formatDate value="${settleVTN.createTime}" pattern="yyyy-MM-dd"/>
					</td>
					<shiro:hasPermission name="settlevtn:settleVTN:edit">
						<td><a
							href="${ctx}/settlevtn/settleVTN/update?id=${settleVTN.id}">修改</a>
							<a href="${ctx}/settlevtn/settleVTN/delete?id=${settleVTN.id}"
							onclick="return confirmx('确认要删除该VTN管理吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
