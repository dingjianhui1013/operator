<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>网点配置</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<%@include file="/WEB-INF/views/include/treeview.jsp" %>
<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 5});
			var setting = {check:{enable:false,nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
			
			// 用户-菜单
			var zNodes=[
					<c:forEach items="${ooo}" var="off">{id:${off.id}, pId:${not empty off.parent.id?off.parent.id:0}, name:"${not empty off.parent.id?off.name:'区域网点'}"},
		            </c:forEach>];
			// 初始化树结构
			var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			tree.expandAll(true);
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
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
					top.$.jBox.confirm("该区域存在已发生实际业务的网点，是否禁用？", "提示", submit);
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
					top.$.jBox.confirm("删除区域后，区域及以下网点将被删除？", "提示", submit);
				} else {
					var submit = function (v, h, f) {
					    if (v == 'ok'){
					    }
					   		 return true; //close
					};
					top.$.jBox.confirm("该区域存在已发生实际业务的网点，不允许删除！", "警告", submit);
				}
				
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/office?appId=${appId}">区域网点配置</a></li>
		<li><a href="${ctx}/sys/office/addAreaFrom">添加区域</a></li>
		<li><a href="${ctx}/sys/office/isnertFrom">添加网点</a></li>
	</ul>
	<div class="container-fluid">
	<div class="row-fluid">
		<div class="span3">
		<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
		</div>
		<div class="span9">
		<form:form id="searchForm" modelAttribute="office"
		action="${ctx}/sys/office/officeView?appId=${appId}" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>区域名称 ：</label>
		<form:input path="name" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="treeTable"
		class="table table-striped table-bordered table-condensed">
		<tr>
			<th width="20%">区域名称</th>
			<th width="40%">操作</th>
			<c:forEach items="${page.list}" var="office" varStatus="status">
				<tr id="${office.id}">
					<td style="word-wrap:break-word;word-break:break-all;" width="200"
					>${office.name}</td>
					<td><a href="${ctx}/sys/office/listL?id=${office.id}">查看网点</a>
						<shiro:hasPermission name="sys:office:areaDisable"><c:if test="${office.delFlag=='0' }"><a href="javascript:void(0)" onclick="return ondisable('${office.id}')">禁用区域</a></c:if></shiro:hasPermission>
						<shiro:hasPermission name="sys:office:areaAble"><c:if test="${office.delFlag=='1' }"><a href="${ctx}/sys/office/enable?id=${office.id}" >启用区域</a></c:if></shiro:hasPermission>
						<shiro:hasPermission name="sys:office:areaDel"><a href="javascript:void(0)" onclick="return ondelete('${office.id}')">删除区域</a></shiro:hasPermission>
						
						</td>
				</tr>
			</c:forEach>
	</table>
	<div class="pagination">${page}</div>
		</div>
	</div>
	</div>
</body>
</html>