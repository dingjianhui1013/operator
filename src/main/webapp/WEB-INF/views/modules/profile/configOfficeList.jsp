<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>授权区域</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 5});
			var s = document.getElementById("shouquanwangdian").innerText;
			s=s.substring(0,s.length-2);
			$("#shouquanwangdian").text(s);
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
		<li class="active"><a href="${ctx}/profile/office/officeView?appId=${appId}">网点授权</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="office" action="${ctx}/profile/office/officeView?appId=${appId}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>网点名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<div >
		已授权网点：
		<span id="shouquanwangdian">
		<c:forEach items="${findAllByDel}"  var="office"  varStatus="s">
			<c:choose>					
				<c:when test="${office.onfous==true}"> 
					${office.name},
				</c:when>
			</c:choose>
		</c:forEach>
		</span>
	</div>
	<tags:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<tr>
			<th>序号</th>
			<th>网点名称</th>
			<th>归属区域</th><shiro:hasPermission name="sys:office:edit">
			<th>操作</th></shiro:hasPermission>
		</tr>
		<c:forEach items="${page.list}" var="office" varStatus="status">
			<tr id="${office.id}" >
				<td>${ status.index + 1}</td>
				<td>${office.name}</td>
				<td>${office.parent.name}</td>
				<shiro:hasPermission name="sys:office:edit"><td>
				<c:choose>					
						<c:when test="${office.onfous==true}"> 
   						<a href="${ctx}/profile/office/deleteAppOffice?id=${office.id}&appId=${appId}" onclick="return confirmx('确认要取消授权吗？', this.href)">取消授权</a>
   						</c:when>
   						<c:otherwise> 
   						<a href="${ctx}/profile/office/saveAppOffice?id=${office.id}&appId=${appId}" onclick="return confirmx('确认要授权给该网点吗？', this.href)">
   						授权</a>
   						</c:otherwise>
					</c:choose>			
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>