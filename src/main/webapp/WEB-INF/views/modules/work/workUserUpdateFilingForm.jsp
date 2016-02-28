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
		<li class="active"><a href="${ctx}/work/workDealInfoFiling/updateUserFrom?id=${workUser.id}">修改联系人</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="workUser" action="${ctx}/work/workDealInfoFiling/updateUser" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input path="contactName" htmlEscape="false" maxlength="11" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">性别:</label>
			<div class="controls">
				<input type ="radio" name = "contactSex" checked="checked" value = "男"/> 男
				<input type ="radio" name = "contactSex"  value = "女"/> 女
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">移动电话:</label>
			<div class="controls">
				
				<input type="text" name="contactPhone" maxlength="11" class="required" value="<fmt:formatNumber pattern="#">${workUser.contactPhone}</fmt:formatNumber>">
				
				
				
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">固定电话:</label>
			<div class="controls">
				<form:input path="contactTel" maxlength="14" />
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">证件类型:</label>
			<div class="controls">
				<c:if test="${workUser.conCertType==0 }">身份证</c:if>
				<c:if test="${workUser.conCertType==1 }">军官证</c:if>
				<c:if test="${workUser.conCertType==2 }">其他</c:if>
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">证件号码:</label>
			<div class="controls">
				${workUser.conCertNumber }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门名称:</label>
			<div class="controls">
				<form:input path="department" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人编码:</label>
			<div class="controls">
				<form:input path="userSn" maxlength="50" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电子邮件:</label>
			<div class="controls">
				<form:input path="contactEmail" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系地址:</label>
			<div class="controls">
				<form:input path="address" maxlength="50"/>
			</div>
		</div>
		<input type = "hidden" name = "status" value = "${workUser.status }"/>
		<input type = "hidden" name = "conCertType" value = "${workUser.conCertType }"/>
		<input type = "hidden" name = "conCertNumber" value = "${workUser.conCertNumber }"/>
		<input type = "hidden" name = "source" value = "${workUser.source }"/>
		<input type = "hidden" name = "workType" value = "${workUser.workType }"/>
		<input type = "hidden" name = "comId" value = "${workUser.workCompany.id }"/>
		<input type = "hidden" name = "workDealInfoId" value = "${workDealInfoId }"/>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
