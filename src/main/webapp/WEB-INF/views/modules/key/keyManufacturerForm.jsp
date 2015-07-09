<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>厂商信息管理</title>
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
		
		
		function checkMobile(obj) { 
			var mobie = $(obj).val();
			var regu = /^[1][0-9][0-9]{9}$/; 
			var re = new RegExp(regu); 
			if (re.test(mobie)) { 
				if($("#phonepro").text()!=""){
					$("#phonepro").hide();
				}
				return true; 
			} else { 
				if($("#phonepro").text()!=""){
					$(obj).focus(); //让手机文本框获得焦点 
					return false; 
				}
				$("#contactPhone").after("<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
				/* top.$.jBox.tip("请输入正确的手机号码!");  */
				$(obj).focus(); //让手机文本框获得焦点 
				return false; 
			} 
		} 
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/key/keyManufacturer/">厂商管理</a></li>
		<li class="active"><a href="${ctx}/key/keyManufacturer/form?id=${keyManufacturer.id}"><shiro:hasPermission name="key:keyManufacturer:edit">${not empty keyManufacturer.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="key:keyManufacturer:edit">查看</shiro:lacksPermission>厂商</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="keyManufacturer" action="${ctx}/key/keyManufacturer/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">厂商名称:</label>
			<div class="controls">
				<input type="text" id="name" name="name" maxlength="20" 
				<c:if  test="${not empty keyManufacturer.id}">
					disabled="true" 
				</c:if>
				value="${keyManufacturer.name}"
				class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人姓名:</label>
			<div class="controls">
				<form:input path="linkman"  htmlEscape="false" maxlength="16" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人电话:</label>
			<div class="controls">
				<form:input path="linkmanPhone" htmlEscape="false" id="contactPhone" 
				maxlength="11"  cssClass="required mobile" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">厂商地址:</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" maxlength="60" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="description"  htmlEscape="false" maxlength="50"></form:textarea>	
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="key:keyManufacturer:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
