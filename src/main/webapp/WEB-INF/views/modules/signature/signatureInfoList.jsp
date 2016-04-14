<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>印章业务管理</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
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
		<li class="active"><a href="${ctx}/signature/signatureInfo/">印章列表</a></li>
		
	</ul>
	<form:form id="searchForm" modelAttribute="signatureInfo"
		action="${ctx}/signature/signatureInfo/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="signId" type="hidden" value="${signatureInfo.id}" />
		<div>
			
		
			&nbsp;&nbsp;<label>单位名称：</label>
			&nbsp;&nbsp; <form:input path="workDealInfo.workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" />
			
			
			
			<label>印章类型：</label>
			<select name="signatureType" id="signatureType">
				<option value="">请选择印章类型</option>
				<option value="1" <c:if test="${signatureType==1}">selected="selected"</c:if>>财务章</option>
				<option value="2" <c:if test="${signatureType==2}">selected="selected"</c:if>>合同章</option>
				<option value="3" <c:if test="${signatureType==3}">selected="selected"</c:if>>个人章</option>
				<option value="4" <c:if test="${signatureType==4}">selected="selected"</c:if>>公章</option>
			</select>
			
			&nbsp;&nbsp;<label>印章创建时间：&nbsp;</label> &nbsp;&nbsp;<input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" /> 
				
				&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit"
				class="btn btn-primary" type="submit" value="查询" /> 			
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>签章业务编号</th>
				<th>申请人</th>
				<th>印章名称</th>
				<th>印章类型</th>
				<th>录入时间</th>
				<th>印章开始时间</th>
				<th>印章结束时间</th>
				<th>印章状态</th>
				<th>业务类型</th>
				<th>业务状态</th>				
				<th>操作</th>
		<tbody>
			<c:forEach items="${page.list}" var="signatureInfo" >
				<tr>
					<td>${signatureInfo.svn }</td>
					<td>${signatureInfo.workDealInfo.workUser.contactName }</td>
					<td>${signatureInfo.workDealInfo.workCompany.companyName }</td>
					<td>
						<c:if test="${signatureInfo.signatureType == 1 }">财务章</c:if>
						<c:if test="${signatureInfo.signatureType == 2 }">合同章</c:if>
						<c:if test="${signatureInfo.signatureType == 3 }">个人章</c:if>
						<c:if test="${signatureInfo.signatureType == 4 }">公章</c:if>
					</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.createDate}"/><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.createDate}" var="createDate"/></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.startDate}"/></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${signatureInfo.endDate}"/></td>
					<td>${status[signatureInfo.status]}</td>
					<td>${signatureTypeMap[signatureInfo.signatureInfoType] }</td>
					<td>${infoStatus[signatureInfo.signatureInfoStatus]}</td>
					<td>
						<c:if test="${(signatureInfo.signatureInfoStatus == 0 || signatureInfo.signatureInfoStatus == 2)&&signatureInfo.signatureInfoType==0 && createDate==date}">
							
							<a href="${ctx}/signature/signatureInfo/form?signatureInfoId=${signatureInfo.id}">修改</a>
						</c:if>
					
						<c:if test="${(signatureInfo.signatureInfoStatus == 0 || signatureInfo.signatureInfoStatus == 2)&&(signatureInfo.signatureInfoType==1||signatureInfo.signatureInfoType==2) && createDate==date}">
							<a href="${ctx}/signature/signatureInfo/modifyForm?signatureInfoId=${signatureInfo.id}">修改</a>
						</c:if>
						<c:if test="${signatureInfo.signatureInfoStatus==0 && createDate==date}"><a onclick="return confirmx('确认要删除该信息吗？', this.href)" href="${ctx}/signature/signatureInfo/delete?signarureId=${signatureInfo.id}">删除</a></c:if>
						<c:if test="${signatureInfo.signatureInfoStatus == 1 }">
							<a href="${ctx}/signature/signatureInfo/typeForm?id=${signatureInfo.id}&&dealType=1">变更</a>
							<a href="${ctx}/signature/signatureInfo/typeForm?id=${signatureInfo.id}&&dealType=2">续期</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<span id="msg" style="color: red;"></span>
	

</body>
</html>
