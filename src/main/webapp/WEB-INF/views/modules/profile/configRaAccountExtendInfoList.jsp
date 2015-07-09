<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>证书模版配置管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function isUsedCertName(id){
		var url = "${ctx}/profile/configRaAccountExtendInfo/used?extendId="+id+"&_="+new Date().getTime();
		$.getJSON(url ,function(data){
			if(data.status==0){
				if(data.type==1){
					var submit = function (v, h, f) {
					    if (v == 'ok'){
					    	window.location.href="${ctx}/profile/configRaAccountExtendInfo/form?id="+id;
					    }
					    return true; //close
					};
					top.$.jBox.confirm("该证书'"+data.certName+"'模版已被使用，是否继续进行修改？", "提示", submit);
				}else{
					window.location.href="${ctx}/profile/configRaAccountExtendInfo/form?id="+id;
				}
			}else{
				top.$.jBox.tip("系统出现异常！");
			}
		});
	}
	function deletUsed(id){
		var url = "${ctx}/profile/configRaAccountExtendInfo/used?extendId="+id+"&_="+new Date().getTime();
		$.getJSON(url ,function(data){
			if(data.status==0){
				if(data.type==1){
					top.$.jBox.tip("该证书'"+data.certName+"'模版已被使用，请取消绑定使用后再进行删除！");
				}else{
					var submit = function (v, h, f) {
					    if (v == 'ok')
					    	window.location.href="${ctx}/profile/configRaAccountExtendInfo/delete?id="+id+"&certName="+data.certName;
					    return true; //close
					};
					top.$.jBox.confirm("确认要删除该证书'"+data.certName+"'模版吗？", "提示", submit);
				}
			}else{
				top.$.jBox.tip("系统出现异常！");
			}
		});
	}
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/profile/configRaAccountExtendInfo/">证书模版配置列表</a></li>
		<shiro:hasPermission name="profile:configRaAccountExtendInfo:edit">
			<li><a href="${ctx}/profile/configRaAccountExtendInfo/form">证书模版配置添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configRaAccountExtendInfo"
		action="${ctx}/profile/configRaAccountExtendInfo/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>证书模版名称 ：</label>
		<form:input path="certName" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>证书模版名称</th>
				<shiro:hasPermission name="profile:configRaAccountExtendInfo:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="configRaAccountExtendInfo">
				<tr>
					<td><a
						href="${ctx}/profile/configRaAccountExtendInfo/form?id=${configRaAccountExtendInfo.id}">${configRaAccountExtendInfo.certName}</a></td>
				
					<shiro:hasPermission name="profile:configRaAccountExtendInfo:edit">
						<td>
							<a
							href="javascript:isUsedCertName(${configRaAccountExtendInfo.id}) ">修改</a>
							<a
							href="javascript:deletUsed(${configRaAccountExtendInfo.id})">删除</a>
							
							
							</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
