<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>邮件配置管理</title>
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
<script type="text/javascript">
function testSendMail(){
    var port = $("#port").val();
	var isSSL = $("#isSSL").prop("checked");
	var user = $("#name").val();
	var pass = $("#pass").val();
	var smtp = $("#url").val();
	var toEmail = $("#toEmail").val();
	
	var url="${ctx}/sys/sysConfig/testSendMail"+"?_="+new Date().getTime();
	$.getJSON(url,{"toEmail":toEmail,"port":port,"isSSL":isSSL,"user":user,"pass":pass,"smtp":smtp},function(data){
		if(data.test==true){
			top.$.jBox.tip('测试通过');
		}else{
			top.$.jBox.tip('发送失败','warning');
		}
		
	});
	
}

</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/sysConfig/form">邮件配置</a></li>
	</ul><br/>
		<form:form id="inputForm" modelAttribute="sysConfig" action="${ctx}/sys/sysConfig/save" method="post" class="form-horizontal">
			<tags:message content="${message}"/>
			
			<div class="control-group">
			<label class="control-label">服务器地址: </label>
			<div class="controls">
				<input id="url" name="url" type="text" class="bor txtth" value="${email_url}"></input>
			</div>
			</div>
			<div class="control-group">
			<label class="control-label">服务器端口: </label>
			<div class="controls">
				<input id="port" name="port" type="text" class="bor txtth" value="${port}"></input>
			</div>
			</div>
			<div class="control-group">
			<label class="control-label"></label>
			<div class="controls">
				<input type="radio" value="true" name="isSSL" id="isSSL"  <c:if test='${is_need_ssl==true}'>checked</c:if>>SSL
				<input type="radio" value="false" name="isSSL"  <c:if test='${is_need_ssl==false}'>checked</c:if>>TLS
			</div>
			</div>
			<div class="control-group">
			<label class="control-label">用户名：</label>
			<div class="controls">
				<input id="name" name="name" type="text" class="bor txtth" value="${email_user_name}"></input>
			</div>
			</div>
			<div class="control-group">
			<label class="control-label">密码：</label>
			<div class="controls">
				<input id="pass" name="pass" type="password" class="bor txtth" value="${email_user_pass}"></input>
				<input type="button" value="测 试" onclick="testSendMail()" class="btn btn-info"/>
			</div>
			</div>
			<%-- <div class="control-group">
			<label class="control-label">管理员邮箱：</label>
			<div class="controls">
				<input id="userEmail"  name="userEmail" value="${admin_eamil}" type="text" class="bor txtth"></input>
			</div>
			</div>
			<div class="control-group">
			<label class="control-label">接收者邮箱：</label>
			<div class="controls">
				<input id="toEmail" name="toEmail" type="text" value="${receiver_email}" class="bor txtth" ></input>
			</div> 
			</div> --%>
			
			<br/>
			
			<input type="hidden" name="passId" value="${passId}" />
			<input type="hidden" name="portId" value="${portId}" />
			<input type="hidden" name="sslId" value="${sslId}" />
			<input type="hidden" name="urlId" value="${urlId}" />
			<input type="hidden" name="nameId" value="${nameId}" />
			<input type="hidden" name="adminId" value="${adminId}" />
			<input type="hidden" name="receiverId" value="${receiverId}" />
	

		<div class="form-actions">
			<shiro:hasPermission name="sys:sysConfig:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	
</body>
</html>
