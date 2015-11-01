<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>应用管理</title>
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
		function addOffice() {
			var areaId = $("#area").prop('value');
			var url = "${ctx}/sys/office/addOffices?areaId=";
			$.getJSON(url + areaId+"&_="+new Date().getTime(), function(data) {
				var html = "";
				//console.log(data);
				html += "<option value=\""+""+"\">请选择</ooption>";
				$.each(data, function(idx, ele) {
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">" + ele.name
							+ "</ooption>"
				});

				$("#office").html(html);
			});

		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/profile/configApp/">应用列表</a></li>
		<shiro:hasPermission name="profile:configApp:edit"><li><a href="${ctx}/profile/configApp/form">新建应用</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configApp" action="${ctx}/profile/configApp/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>应用名称 ：</label><form:input path="appName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;
			<label>选择区域 ：</label>
		<select name="area" id="area" onchange="addOffice()">
			<c:if test="${offsListSize!=1 }">

				<option value="">请选择</option>
			</c:if>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>
		<select name="office" id="office">
			<c:if test="${offsListSize!=1 }">
				<option value="">请选择</option>
			</c:if>
			<c:forEach items="${offices}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==office}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>

		</select>

		&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="快速搜索"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
					<th>序号</th>
					<th>应用名称</th>
					<th>别名</th>
					<th>项目类型</th>
					<th>产品</th>
					<th>应用描述</th>
					<shiro:hasPermission name="profile:configApp:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="configApp" varStatus="status">
			<tr>
			
				<td>${ status.index + 1}</td>
				<td width="140px" style="word-break: break-all"><a href="${ctx}/profile/configApp/form?id=${configApp.id}">${configApp.appName}</a></td>
				<td>${ configApp.alias}</td>
				<td>${ configApp.configProjectType.projectName}</td>
				<td>${ configApp.productName}</td>
				<td width="140px" style="word-break: break-all">${configApp.appDescription}</td>
				<td><shiro:hasPermission name="profile:configApp:edit">
    				<a href="${ctx}/profile/configApp/form?id=${configApp.id}">编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="profile:configProduct:edit">
    				<a href="${ctx}/profile/configProduct?appId=${configApp.id}">产品管理</a>
    				</shiro:hasPermission>
					<shiro:hasPermission name="profile:configProduct:edit">
    				<a href="${ctx}/profile/office/officeView?appId=${configApp.id}">网点授权</a>
    				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
