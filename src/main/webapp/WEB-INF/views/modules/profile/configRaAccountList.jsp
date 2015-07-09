<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>RA配置管理</title>
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

		function deleteRa(raId){
			var id = raId;

			$.ajax({
				url:"${ctx}/profile/configRaAccount/checkUsed?raId="+id+"&_="+new Date().getTime(),
				dataType:'json',
				success:function(data){
					if(data.status == '0'){
						top.$.jBox.tip(data.msg);
					}else if(data.status == '1'){
						top.$.jBox.confirm("确认要删除该RA配置吗",'系统提示',function(v,h,f){
							if(v=='ok'){
								loading('正在提交，请稍等...');
								location = "${ctx}/profile/configRaAccount/delete?id="+id;
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');
					}
				}
			});

		}

		function modifyRa(raId){
			var id = raId;

			$.ajax({
				url:"${ctx}/profile/configRaAccount/checkUsedByModify?raId="+id+"&_="+new Date().getTime(),
				dataType:'json',
				success:function(data){
					if(data.status == '0'){
						//top.$.jBox.tip(data.msg);
						top.$.jBox.confirm(data.msg,'系统提示',function(v,h,f){
							if(v=='ok'){
								location = "${ctx}/profile/configRaAccount/form?id="+id;
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');
					}else if(data.status == '1'){

						location = "${ctx}/profile/configRaAccount/form?id="+id;
					}
				}
			});
		}

		function testPing(raId){

			var id = raId;
			$.ajax({
				url:"${ctx}/profile/configRaAccount/testPing?id="+id+"&_="+new Date().getTime(),
				async:false,
				dataType:'json',
				beforeSend:function(){
				},
				success:function(data){
					if(data.status == '0'){
						top.$.jBox.tip(data.msg);
					}
				}
			});

		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/profile/configRaAccount/">RA模板列表</a></li>
		<shiro:hasPermission name="profile:configRaAccount:edit"><li><a href="${ctx}/profile/configRaAccount/form">新增RA模板</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configRaAccount" action="${ctx}/profile/configRaAccount/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>RA模板名称 ：</label><form:input path="raName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>RA模板名称</th><!-- <th>备注</th> --><shiro:hasPermission name="profile:configRaAccount:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="configRaAccount">
			<tr>
				<td><a href="${ctx}/profile/configRaAccount/form?id=${configRaAccount.id}">${configRaAccount.raName}</a></td>

				<shiro:hasPermission name="profile:configRaAccount:edit"><td>
    				<!--<a href="${ctx}/profile/configRaAccount/form?id=${configRaAccount.id}">修改</a>-->
					<a href="#" onclick="modifyRa(${configRaAccount.id})">修改</a>

					<a href="#" onclick="deleteRa(${configRaAccount.id})">删除</a>
					<a href="#" onclick="testPing(${configRaAccount.id})">测试</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
