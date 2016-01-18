<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>新建记录</title>
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
						//alert(element[0].tagName);
						(element.parent().find('label:last-child')).after(error);
					} else {
						//alert(element[0].tagName);
						error.insertAfter(element);
					}
				},
				rules: {
					 completeType: {
							required :true
					}
			 },
				   messages: {
					   completeType: {
						   required: "请选择"
					}
				   }
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
<%-- 		<li ><a href="${ctx}/work/workDealInfoFiling/ulist">咨询类用户</a></li> --%>
		<li  class="active"><a href="${ctx}/work/customer/insertUser?id=${workDealInfo.id}">咨询记录</a></li>
	</ul><br/>
	<form name = "customerInsert" id = "inputForm" action ="${ctx}/work/customer/insertComCustomerT"  method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<input type = "hidden" name = "workCompanyId" value = "${workCompany.id}"/>
		<input type = "hidden" name = "state" value = "1"/>
		<div class="control-group">
			<label class="control-label">服务对象:</label>
			<div class="controls">
				<select name="configApp.id">
					<option></option>
					<c:forEach items="${configApp}" var="configApp">
						<option value="${configApp.id}">${configApp.appName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">客服接入:</label>
			<div class="controls">
				<select name = "access">
					<option value = "电话">电话</option>
					<option value = "QQ" selected="selected">QQ</option>
					<option value = "QQ远程">QQ远程</option>
					<option value = "在线工具">在线工具</option>
					<option value = "邮件">邮件</option>
					<option value = "短信">短信</option>
					<option value = "其他">其他</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
			<input type="text" maxlength="11" name="tel">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;服务主题:</label>
			<div class="controls">
				<input type = "text" name = "serTitle" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">详细记录:</label>
			<div class="controls">
				<textarea id="remarks" class="valid" cols="4" rows="4" style="resize: none;" name="recordContent"></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">&nbsp;记录人员:</label>
			<div class="controls">
				${user.name }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录时间:</label>
			<div class="controls">
				${nowDate }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;">*</span>&nbsp;&nbsp;&nbsp;&nbsp;是否完成:</label>
			<div class="controls">
				<input type="radio" value = "0" name = "completeType" />是
				<label><input type="radio" value = "1" name = "completeType" />否</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">遗留问题:</label>
			<div class="controls">
				<textarea id="remarks" class="valid" cols="4" rows="4" style="resize: none;" name="leftoverProblem"></textarea>
			</div>
		</div>
		<input type="hidden" value="1" name="distinguish"/>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>&nbsp;&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
