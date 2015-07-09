<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>联系人管理</title>
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
		<li class="active"><a href="#">新建联系人</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="workUser" action="${ctx}/work/workUser/insertWorkUserU" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<input type = "hidden" name = "workCompany.id" value = "${comId }"/>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>姓名:</label>
			<div class="controls">
				<form:input path="contactName" htmlEscape="false" maxlength="11" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">性别:</label>
			<div class="controls">
				<form:radiobutton path="contactSex" value = "男"/>男
				<form:radiobutton path="contactSex" value = "女"/>女
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>电子邮件:</label>
			<div class="controls">
				<form:input path="contactEmail" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>移动电话:</label>
			<div class="controls">
				<form:input path="contactPhone" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">固定电话:</label>
			<div class="controls">
				<form:input path="contactTel" maxlength="50"/>
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">证件类型:</label>
			<div class="controls">
				<select name="conCertType" >
					<option value="0" id="conCertType0"
						<c:if test="${workUser.conCertType==0 }">selected</c:if>>身份证</option>
					<option value="1" id="conCertType1"
						<c:if test="${workUser.conCertType==1 }">selected</c:if>>军官证</option>
					<option value="2" id="conCertType2"
						<c:if test="${workUser.conCertType==2 }">selected</c:if>>其他</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>证件号码:</label>
			<div class="controls">
				<form:input path="conCertNumber" maxlength="18" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门名称:</label>
			<div class="controls">
				<form:input path="department" maxlength="50" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人编码:</label>
			<div class="controls">
				<form:input path="userSn" maxlength="50" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系地址:</label>
			<div class="controls">
				<form:input path="address" maxlength="50"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
