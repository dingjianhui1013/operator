<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>业务办理管理</title>
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
		<li class="active"><a href="${ctx}/work/workDealInfoAudit/list">鉴证业务</a></li>
		<shiro:hasPermission name="userenroll:trustdevice:audit">
		<li><a href="${ctx}/work/workDealInfoAudit/listTrustCountApply">审核移动设备数量</a></li>
		</shiro:hasPermission>
		<li ><a href="${ctx}/work/workDealInfoAudit/exceptionList?dealInfoStatus=1">异常业务</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo" action="${ctx}/work/workDealInfoAudit/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>KeyID：</label><form:input path="keySn" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>单位名称：</label><form:input path="workCompany.companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;&nbsp;&nbsp;&nbsp;<label>联系人姓名：</label><form:input path="workUser.contactName" htmlEscape="false" maxlength="50" class="input-medium"/><br/>
		</div>
		<div style="margin-top: 8px">
		<label>&nbsp;&nbsp;固定电话：</label><form:input path="workUser.contactTel" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>&nbsp;&nbsp;&nbsp;手机号：</label><form:input path="workUser.contactPhone" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<label>&nbsp;&nbsp;业务类型：</label><select name="dealInfoType" style="width: 180px;">
				<option value="-1">所有类型</option>
				<option value="0" <c:if test="${workDealInfo.dealInfoType==0 }">selected</c:if>>新增证书</option>
				<option value="1" <c:if test="${workDealInfo.dealInfoType==1 }">selected</c:if>>在线更新</option>
		</select>
		</div>
		<div style="margin-top: 8px"> 
		<label>&nbsp;&nbsp;业务状态：</label><select name="dealInfoStatus" style="width: 180px;">
				<option selected="selected" value="-1">所有状态</option>
				<option value="0" <c:if test="${workDealInfo.dealInfoStatus==0 }">selected</c:if>>已缴费，待审核</option>
				<option value="12" <c:if test="${workDealInfo.dealInfoStatus==12 }">selected</c:if>>未缴费，待审核</option>
		</select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>业务编号</th>
				<th>企业名称</th>
				
				<th>证书持有人名称</th>
				<th>经办人名称</th>
				
				<th>企业类型</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>业务状态</th>
				<th>操作</th>
		<tbody>
		<c:forEach items="${page.list}" var="workDealInfo" varStatus="status">
			<tr>
				<td>${workDealInfo.svn}</td>
				<td width="18%"><a href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.workCompany.companyName}</a></td>
				<td>
								${workDealInfo.workUser.contactName}
					</td>
					<td>
								${workDealInfo.workCertInfo.workCertApplyInfo.name}
					</td>
				<td><c:if test="${workDealInfo.workCompany.companyType==1}">企业</c:if>
				<c:if test="${workDealInfo.workCompany.companyType==2}">事业单位</c:if>
				<c:if test="${workDealInfo.workCompany.companyType==3}">政府机关</c:if>
				<c:if test="${workDealInfo.workCompany.companyType==4}">社会团体</c:if>
				<c:if test="${workDealInfo.workCompany.companyType==5}">其他</c:if>
				</td>

				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}</td>
				<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
				<td>
					<c:if test="${workDealInfo.dealInfoStatus == 12 || workDealInfo.dealInfoStatus == 0}">
					<shiro:hasPermission name="work:workDealInfo:jianzheng">
					<a href="${ctx}/work/workDealInfoAudit/auditFrom?id=${workDealInfo.id}">鉴证</a>
					</shiro:hasPermission>
					</c:if>
					
					<c:if test="${workDealInfo.dealInfoStatus == 13}">
					<shiro:hasPermission name="work:workDealInfo:makezheng">
					<a href="${ctx}/work/workDealInfoAudit/makeDealInfo?id=${workDealInfo.id}">制证</a>
					</shiro:hasPermission>
					</c:if>
					
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
