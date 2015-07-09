<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>应用管理</title>
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
		
		function addAttach() {
			var options = {
				type : 'post',
				dataType : 'json',
				success : function(data) {
					//console.log(data);
					if(data.status=='1'){
						
						
						$("#appImg").val(data.attach);
						top.$.jBox.tip("上传成功");
					}else{
						top.$.jBox.tip("上传失败："+data.errorMsg);
					}
				}
			};
			$('#materialImport').ajaxSubmit(options);
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configApp/">应用列表</a></li>
		<li class="active"><a href="${ctx}/profile/configApp/form?id=${configApp.id}"><shiro:hasPermission name="profile:configApp:edit">${not empty configApp.id?'修改':'新建'}</shiro:hasPermission><shiro:lacksPermission name="profile:configApp:edit">查看</shiro:lacksPermission>应用</a></li>
	</ul><br/>
	<form:form id="inputForm"  modelAttribute="configApp" action="${ctx}/profile/configApp/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>应用名称:</label>
			<div class="controls">
				<form:input path="appName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>别名:</label>
			<div class="controls">
				<form:input path="alias" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否支持通用:</label>
			<div class="controls">
				<form:checkbox path="supportCommon"/>支持
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">政府统一采购设备证书最大数量:</label>
			<div class="controls">
				<form:input path="govDeviceAmount" htmlEscape="false" maxlength="15"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应用描述:</label>
			<div class="controls">
				<form:textarea path="appDescription"  cols="4" rows="4" maxlength="200" style="resize: none;" ></form:textarea><span style="color:red;">不超过200字</span>
			</div>
		</div>
		
		<div class="control-group" style="display: none">
			<label class="control-label">上传图片:</label>
			<div class="controls">
					<a id="btnImport" data-toggle="modal" href="#declareDiv" class="btn btn-primary">上传图片</a>
					<form:hidden path="appImg" htmlEscape="false" maxlength="11" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configApp:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	<div id="declareDiv" class="modal hide fade">
		<div class="modal-header">
			<h3>上传图片</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/profile/configApp/addAttach"
				enctype="multipart/form-data">
				<input id="material" name="fileName" type="file" multiple="multiple" />
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" 
				onclick="addAttach()" class="btn btn-primary">导入</a>
		</div>
	</div>
</body>
</html>
