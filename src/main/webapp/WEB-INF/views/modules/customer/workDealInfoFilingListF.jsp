<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			submitHandler : function(form) {
				loading('正在提交，请稍等...');
				form.submit();
			}
		});
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
		<li class="active"><a href="${ctx}/work/customer/list">业务记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/customer/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
		<label>Key序列号:</label>
		<form:input path="keySn" htmlEscape="false" maxlength="50"
			class="input-medium" />
		<label>经办人邮箱:</label>
		<form:input path="workUser.contactEmail" htmlEscape="false"
			maxlength="30" cssClass="input-medium email" />
		<label>单位电话:</label>
		<form:input path="workCompany.companyMobile" htmlEscape="false"
			maxlength="12" class="input-medium" />
		</div>
		<div style="margin-top: 8px">
		<label>&nbsp;&nbsp;&nbsp;单位名称:</label>
		<form:input path="workCompany.companyName" htmlEscape="false"
			maxlength="50" class="input-medium" />
		<label>经办人姓名:</label>
		<form:input path="workUser.contactName" htmlEscape="false"
			maxlength="50" class="input-medium" />
		<label>应用名称:</label>
		<form:select path="configApp.appName">
			<form:option value="" label="请选择"/>
			<c:forEach items="${apps}" var="app">
				<form:option value="${app.appName }">${appName }</form:option>
			</c:forEach>
		</form:select>
		</div>
		<div style="margin-top: 8px">
		<label>证书到期时间：</label> 
		<!-- class="required" -->
		<input id="startTime" name="startTime"	type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="endTime" name="endTime" type="text"  readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
			<!-- 	class="Wdate required" -->
		&nbsp; &nbsp; &nbsp; &nbsp; 
		
		<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
<!-- 			<input id="fuzzyRecord" class="btn btn-primary" type="button" -->
<!-- 			value="模糊记录" /> -->
			<a id="fuzzyRecord" class="btn btn-primary" href="${ctx}/work/customer/insertFuzzy">模糊记录</a>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>单位名称</th>
				<th>单位类型</th>
				<th>单位电话</th>
				<th>应用名称</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>Key序列号</th>
				<th>经办人电话</th>
				<th>证书到期时间</th>
				<th>操作</th>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td>${workDealInfo.id}</td>
					<td width="20%"><a
						href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.workCompany.companyName}</a></td>
					<td>
					<c:if test="${workDealInfo.workCompany.companyType==1}">企业</c:if>
					<c:if test="${workDealInfo.workCompany.companyType==2}">事业单位</c:if>
					<c:if test="${workDealInfo.workCompany.companyType==3}">政府机构</c:if>
					</td>
					<td>${workDealInfo.workCompany.companyMobile}</td>
					<td>${workDealInfo.configApp.appName}</td>
					<td>${proType[workDealInfo.configProduct.productName]}</td>
					<td>${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}</td>
					<td>${workDealInfo.keySn }</td>
					<td>${workDealInfo.workUser.contactPhone}</td>
					<td><fmt:formatDate value="${workDealInfo.workCertInfo.notafter}" pattern="yyyy-MM-dd"/></td>
					<td><a
						href="${ctx}/work/customer/insertCustomerFrom?id=${workDealInfo.id}">新增客服记录</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
