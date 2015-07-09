]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>网点列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	function ondisable(id){
		var url="${ctx}/sys/office/deleteOffice?id="+id;
		var submit = function (v, h, f) {
		    if (v == 'ok'){
		    	window.location.href="${ctx}/sys/office/delete?id="+ id;
		    }
		   		 return true; //close
		};
		$.getJSON(url,function(data){
			if(data.status==1){
				top.$.jBox.confirm("禁用网点后，网点下用户无法登陆，确认禁用？", "提示", submit);
			} else {
				top.$.jBox.confirm("该网点已实际发生业务，是否禁用？", "提示", submit);
			}
			
		});
	}
	function ondelete(id){
		var url="${ctx}/sys/office/deleteOffice?id="+id;
		$.getJSON(url,function(data){
			if(data.status==1){
				var submit = function (v, h, f) {
				    if (v == 'ok'){
				    	window.location.href="${ctx}/sys/office/deleteL?id="+ id;
				    }
				   		 return true; //close
				};
				top.$.jBox.confirm("删除网点后，网点将被删除？", "提示", submit);
			} else {
				var submit = function (v, h, f) {
				    if (v == 'ok'){
				    }
				   		 return true; //close
				};
				top.$.jBox.confirm("该网点已实际发生业务，不允许删除！", "警告", submit);
			}
			
		});
	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/office?appId=${appId}">区域网点配置</a></li>
		<li><a href="${ctx}/sys/office/addAreaFrom">添加区域</a></li>
		<li><a href="${ctx}/sys/office/isnertFrom">添加网点</a></li>
		<li class="active"><a href="${ctx}/sys/office/listL?id=${officeId}">网点列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>网点名称</th><th>操作</th></tr>
		<c:forEach items="${page.list}" var="office">
			<tr>
				<td>${office.name}</td>
				<td><a href="${ctx}/sys/office/updateFrom?id=${office.id}">修改网点</a>
						<shiro:hasPermission name="sys:office:officeDisable"><c:if test="${office.delFlag=='0' }"><a href="javascript:void(0)" onclick="return ondisable('${office.id}')">禁用网点</a></c:if></shiro:hasPermission>
						<shiro:hasPermission name="sys:office:officeAble"><c:if test="${office.delFlag=='1' }"><a href="${ctx}/sys/office/enable?id=${office.id}" >启用网点</a></c:if></shiro:hasPermission>
					    <shiro:hasPermission name="sys:office:officeDel"><a href="javascript:void(0)" onclick="return ondelete('${office.id}')">删除网点</a> </shiro:hasPermission>
						
						</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>