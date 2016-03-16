<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目类型管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		
		function checkName(){
			var name = $("#projectName").val();
			var id = $("#id").val();
			var url = "${ctx}/profile/configProjectType/checkProName";
			$.ajax({
				url:url,
				data:{proName:name,proId:id,_:new Date().getTime()},
				dataType:'json',success: function(data){
					if(data.status==1){
						if(data.isAdd==0){
							$("#inputForm").submit();
						}else{
							top.$.jBox.tip("项目类型名称:"+name+"已经添加！");
							return false;
						}
					}else{
						top.$.jBox.tip("系统异常！");
						return false;
					}
				}
			});
			return false;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configProjectType/">项目类型管理列表</a></li>
		<li class="active"><a href="${ctx}/profile/configProjectType/form?id=${configProjectType.id}">项目类型管理<shiro:hasPermission name="profile:configProjectType:edit">${not empty configProjectType.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="profile:configProjectType:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configProjectType" action="${ctx}/profile/configProjectType/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">项目类型名称:</label>
			<div class="controls">
				<form:input path="projectName" htmlEscape="false" maxlength="20" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:input path="remarks" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configProjectType:edit">
			
			<input id="btnSubmit" class="btn btn-primary" onclick="return checkName()" type="submit" value="保 存"/>
			
			
			
			&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
