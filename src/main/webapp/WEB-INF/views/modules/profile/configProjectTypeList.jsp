<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目类型管理管理</title>
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
		function showAppList(proId){
			
			var url="${ctx}/profile/configProjectType/showAppList?proId="+proId+"&_="+new Date().getTime;
			$.ajax({
				url:url,
				type:'POST',
				dataType:'JSON',
				success:function(data){
					if(data.status==1)
						{
							var html="<table class='table table-striped table-bordered table-condensed'><thead><th>项目名称</th></thead><tbody>";
							$.each(data.list,function(i,value){
								html+="<tr><td>"+value.appName+"</td></tr>";
							});
							html+="</tbody></table>";
							top.$.jBox(html, { title: "绑定应用详细",buttons:{"确定":true,"关闭":true}});
						}else
							{
								top.$.jBox.tip("此项目类型没有绑定应用！");
							}
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/profile/configProjectType/">项目类型管理列表</a></li>
		<shiro:hasPermission name="profile:configProjectType:edit"><li><a href="${ctx}/profile/configProjectType/form">项目类型管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configProjectType" action="${ctx}/profile/configProjectType/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>项目类型名称 ：</label><form:input path="projectName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>项目类型名称</th><th>备注</th><shiro:hasPermission name="profile:configProjectType:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configProjectType">
			<tr>
				<td><a href="${ctx}/profile/configProjectType/form?id=${configProjectType.id}">${configProjectType.projectName}</a></td>
				<td>${configProjectType.remarks}</td>
				<shiro:hasPermission name="profile:configProjectType:edit"><td>
    				<a href="${ctx}/profile/configProjectType/form?id=${configProjectType.id}">修改</a>
					<a href="${ctx}/profile/configProjectType/delete?id=${configProjectType.id}" onclick="return confirmx('确认要删除该项目类型管理吗？', this.href)">删除</a>
					<a href="javascript:showAppList('${configProjectType.id}')">查看绑定应用明细</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
