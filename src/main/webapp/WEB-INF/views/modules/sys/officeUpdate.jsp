<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>机构管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/office/">网点配置</a></li>
		<li class="active"><a href="${ctx}/sys/office/updateFrom?id=${office.id}">修改网点</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/updateOffice" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>选择区域:</label>
			<input type = "hidden" name = "parent.id" value ="1"/>
			<div class="controls">
				<select name="areaId" class="bor selwidth1">
					<c:forEach items="${areas}" var="areas">
						<option value="${areas.id }" <c:if test="${office.areaName==areas.name }">selected="selected"</c:if>>${areas.name }</option>
					</c:forEach>
				</select>	
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>网点名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="54" class="required" value="${office.name}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">网点客服电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="50"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">网点地址:</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:office:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>