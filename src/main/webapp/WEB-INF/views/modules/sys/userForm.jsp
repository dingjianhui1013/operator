<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.ml20{margin-left:30px}
	</style>
	<script src="${ctxStatic}/js/pta.js" type="text/javascript"></script>
	<script type="text/javascript">
		
		$(document).ready(function() {
			$("#loginName").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){

						var seeaPassword1 = $("#sccaPasssword1").val();
						var seeaPassword = $("#sccaPasssword").val();
						if(seeaPassword1==seeaPassword){
							loading('正在提交，请稍等...');
							form.submit();
						}else{
							alert("密码与确认密码不相同请重新输入!");
						}
					
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
			
			$("#roleDiv").find("span").append("<br/>");
		});
		
		
		
		
		function identityNumberValid(){
			var code = $("#identityNumber").val();
			
			if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
				
				if($("#phonepro").text()!=""){

					return false; 
				}
                $("#identityNumber").after("<span id='phonepro' style='color:red'>身份证号格式错误</span>");
                return false;
            }
			
			if($("#phonepro").text()!=""){
				$("#phonepro").hide();
			}
			
			
		}
		
			
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}"><shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'创建'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission>管理员</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">归属区域:</label>
			<div class="controls">
                <tags:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="区域" url="/sys/office/treeData?type=1" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属网点:</label>
			<div class="controls">
                <tags:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="网点" url="/sys/office/treeData?type=2" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>登录名:</label>
			<div class="controls">
				<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="required userName"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登录类型:</label>
			<div class="controls">
				<input type = "radio"  <c:if test="${user.loginType!='1' }"> checked="checked" </c:if> name = "loginType" value ="0" /> 密码登录
				<input type = "radio"  <c:if test="${user.loginType=='1' }"> checked="checked" </c:if> name = "loginType" value ="1" /> 证书登录
				
			</div>
		</div>
		<div class="control-group srk">
			<label class="control-label"><font color="red">*</font>密码:</label>
			<div class="controls">
				<input id="sccaPasssword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="${empty user.id?'required':''}"/>
				<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
			</div>
		</div>
		<div class="control-group srk">
			<label class="control-label">确认密码:</label>
			<div class="controls">
				<input id="sccaPasssword1" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#sccaPasssword"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>身份证号:</label>
			<div class="controls">
				<form:input path="identityNumber" onblur="javascript:identityNumberValid()" id="identityNumber" htmlEscape="false" maxlength="18" class="required"/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="100" class="email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>手机:</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="100" cssClass="required mobile"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group" style="display: none">
			<label class="control-label">用户类型:</label>
			<div class="controls">
				<form:select path="userType">
					<form:option value="3" label="普通用户"/>
<%-- 					<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
 --%>				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>用户角色:</label>
			<div class="controls" id="roleDiv">
				<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" class="required"/>
			</div>
		</div>
		<c:if test="${not empty user.id}">
			<div class="control-group">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<label class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">最后登陆:</label>
				<div class="controls">
					<label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
				</div>
			</div>
		</c:if>
		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>