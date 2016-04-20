<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>印章业务</title>
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
		<li class="active"><a href="${ctx}/signature/signatureInfo/sealLog">印章业务列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="signatureInfo" action="${ctx}/signature/signatureInfo/sealLog" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>单位名称 ：</label><form:input path="workDealInfo.workCompany.companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>操作人 ：</label><form:input path="enterUser.name" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>印章业务类型 ：</label>
		<form:select path="signatureInfoType">
			<form:option value="">请选择</form:option>
			<c:forEach items="${signatureTypeMap}" var="signatureTypes">
				<form:option value="${signatureTypes.key}">${signatureTypes.value}</form:option>
			</c:forEach>
		</form:select>
		<br />
		<br />
		<label>录入时间：</label>
		<input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" /> 
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
<!-- 				<th>序号</th> -->
				<th>证书业务编号</th>
				<th>签章业务编号</th>
				<th>单位名称</th>
				<th>操作人</th>
				<th>申请人</th>
				<th>印章名称</th>
				<th>印章类型</th>
				<th>录入时间</th>
				<th>印章开始时间</th>
				<th>印章结束时间</th>
				<th>印章状态</th>
				<th>业务类型</th>
				<th>业务状态</th>	
				<th>受理网点</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="signatureInfo" varStatus="status">
			<tr>
<%-- 				<td>${status.index+1}</td> --%>
				<td>${signatureInfo.workDealInfo.svn}</td>
				<td>${signatureInfo.svn }</td>
				<td>${signatureInfo.workDealInfo.workCompany.companyName }</td>
				<td>${signatureInfo.enterUser.name}</td>
				<td>${signatureInfo.workDealInfo.workUser.contactName }</td>
				<td>${signatureInfo.signatureAppName}</td>
				<td>
					<c:if test="${signatureInfo.signatureType == 1 }">财务章</c:if>
					<c:if test="${signatureInfo.signatureType == 2 }">合同章</c:if>
					<c:if test="${signatureInfo.signatureType == 3 }">个人章</c:if>
					<c:if test="${signatureInfo.signatureType == 4 }">公章</c:if>
				</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.createDate}"/></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.startDate}"/></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.endDate}"/></td>
				<td>${signatureInfostatus[signatureInfo.status]}</td>
				<td>${signatureTypeMap[signatureInfo.signatureInfoType] }</td>
				<td>${infoStatus[signatureInfo.signatureInfoStatus]}</td>
				<td>
					<c:forEach items="${offices}" var="offices">
						<c:if test="${offices.id==signatureInfo.officeId}">${offices.name}</c:if>
					</c:forEach>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
