<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自助申请表管理</title>
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
		<li class="active"><a href="${ctx}/self/selfApplication/">自助申请表管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="selfApplication" action="${ctx}/self/selfApplication/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>单位名称 ：</label><form:input path="companyName" htmlEscape="false" maxlength="50" class="input-medium"/>&nbsp;
		<label>经办人：</label><form:input path="transactorName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<label>受理状态：</label>
		<select name="status"id="appluStatus">
			<option value=""<c:if test="${status==''|| status == null}">selected="selected"</c:if>>请选择状态</option>
			<option value="0" <c:if test="${status=='0'}">selected="selected"</c:if>>未审核</option>
			<option value="1" <c:if test="${status=='1'}">selected="selected"</c:if>>审核完成,等待下载</option>
			<option value="2" <c:if test="${status=='2'}">selected="selected"</c:if> >下载完成,等待缴费</option>
			<option value="3" <c:if test="${status=='3'}">selected="selected"</c:if>>正在受理</option>
			<option value="4" <c:if test="${status=='4'}">selected="selected"</c:if>>业务完成</option>
			<option value="5" <c:if test="${status=='5'}">selected="selected"</c:if>>等待制证</option>
			<option value="6" <c:if test="${status=='6'}">selected="selected"</c:if>>审核未通过</option>
		</select>
		&nbsp;
		<div style="margin-top: 8px">
			<label>申请日期：&nbsp;</label> &nbsp;&nbsp;
			<input class="input-medium Wdate" type="text"required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly" name="startTime" id="startTime"/> 
			至 <input class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"name="endTime" /> 
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>单位名称</th>
				<th>单位证件号码</th>
				<th>经办人</th>
				<th>费用</th>
				<th>申请时间</th>
				<th>业务编号</th>
				<th>受理状态</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="selfApplication">
			<tr>
				<td>${selfApplication.companyName }</td>
				<td>${selfApplication.companyTypeNumber }</td>
				<td>${selfApplication.transactorName }</td>
				<td>
					<c:if test = "${selfApplication.status == '0' ||selfApplication.status == '6'}">
	                	--
	                </c:if>
	                <c:if test = "${selfApplication.status != '0'}">
	                	${selfApplication.money}
	                </c:if>
				</td>
				<td><fmt:formatDate value="${selfApplication.createdate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
				<td>${selfApplication.no }</td>
				<td>
					<c:if test = "${selfApplication.status == '0'}">
               			未审核
               		</c:if>
                		<c:if test = "${selfApplication.status == '1' }">
               			审核完成,等待下载
               		</c:if>
                		<c:if test = "${selfApplication.status == '2' }">
               			下载完成,等待缴费
               		</c:if>
               		<c:if test = "${selfApplication.status == '3' }">
               			正在受理
               		</c:if>
                		<c:if test = "${selfApplication.status == '4' }">
               			业务完成
               		</c:if>
               		<c:if test = "${selfApplication.status == '5' }">
               			等待制证
               		</c:if>
               		<c:if test = "${selfApplication.status == '6' }">
               			审核未通过
               		</c:if>
				</td>
				<td>${selfApplication.denyText} </td>
				<td>
					<c:if test = "${selfApplication.status == '0'}">
	                	<a class="btn btn-warning" href="${ctx}/self/selfApplication/form?id=${selfApplication.id}" role="button">审核</a>
	                </c:if>
	                <c:if test = "${selfApplication.status != '0'}">
	                	<a class="btn btn-primary" href="${ctx}/self/selfApplication/form?id=${selfApplication.id}" role="button">查看</a>
	                </c:if>
   				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
