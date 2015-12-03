<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信配置管理</title>
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
		function checkRa(){
			
			var id = $("#id").val();
			var messageName = $("#messageName").val();
			if(messageName==""){
				top.$.jBox.tip("短信模板名称不能为空");
				return false;
			}
			if($("#jgmc").val()==""){
				top.$.jBox.tip("机构名称不能为空");
				return false;
			}
			if($("#jgdm").val()==""){
				top.$.jBox.tip("机构代码不能为空");
				return false;
			}
			if($("#frdb").val()==""){
				top.$.jBox.tip("法人代表不能为空");
				return false;
			}
			if($("#zcdm").val()==""){
				top.$.jBox.tip("注册代码不能为空");
				return false;
			}
			if($("#keySn").val()==""){
				top.$.jBox.tip("key编码不能为空");
				return false;
			}
			if($("#jgdz").val()==""){
				top.$.jBox.tip("机构地址不能为空");
				return false;
			}
			if($("#jbrxm").val()==""){
				top.$.jBox.tip("经办人姓名不能为空");
				return false;
			}
			if($("#zszt").val()==""){
				top.$.jBox.tip("证书状态不能为空");
				return false;
			}
			if($("#xmmc").val()==""){
				top.$.jBox.tip("项目名称不能为空");
				return false;
			}
			if($("#zsdqr").val()==""){
				top.$.jBox.tip("证书到期日不能为空");
				return false;
			}
			$.ajax({
				url:"${ctx}/message/smsConfiguration/checkMessage?id="+id+"&_="+new Date().getTime(),
				type:'POST',
				dataType : 'json',
				data:{
					id:id,
					messageName:messageName
				},
				success:function(data){
					if(data.status==0){
						top.$.jBox.tip(data.msg);
					}else if(data.status == 1){
						top.$.jBox.confirm(data.msg,'系统提示',function(v,h,f){
							if(v=='ok'){
								$("#smsConfiguForm").submit();
							}
						});
					}else if(data.status == 2){
						$("#smsConfiguForm").submit();
					}
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置列表</a></li>
		<li class="active"><a href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">短信配置<shiro:hasPermission name="message:smsConfiguration:edit">${not empty smsConfiguration.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="message:smsConfiguration:edit">查看</shiro:lacksPermission></a></li>
	</ul>	
		<form:form id="smsConfiguForm" modelAttribute="smsConfiguration"
		action="${ctx}/message/smsConfiguration/save" method="post"
		class="form-horizontal">
		
		<input type="hidden" id="RaId" name="Id" value=${SmsConfiguration.id}>
		<tags:message content="${message}" />
		
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;短信模板名称:</label>
			<div class="controls">
				<form:hidden path="id" htmlEscape="false" maxlength="50"
							 class="required" />
				<form:input path="messageName" htmlEscape="false" maxlength="50"
							class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;机构名称:</label>
			<div class="controls">
				<form:input  path="jgmc" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;机构代码:</label>
			<div class="controls">
				<form:input  path="jgdm" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;法人代表:</label>
			<div class="controls">
				<form:input path="frdb" htmlEscape="false" maxlength="50"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;注册代码:</label>
			<div class="controls">				
				<form:input path="zcdm" htmlEscape="false" maxlength="50"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;key编码:</label>
			<div class="controls">
				<form:input path="keySn" htmlEscape="false" maxlength="256"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;机构地址:</label>
			<div class="controls">
				<form:input path="jgdz" htmlEscape="false" maxlength="256"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;经办人姓名:</label>
			<div class="controls">
					<form:input path="jbrxm" htmlEscape="false" 
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;证书状态:</label>
			<div class="controls">
					<form:input path="zszt" htmlEscape="false" 
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;项目名称:</label>
			<div class="controls">
					<form:input path="xmmc" htmlEscape="false" maxlength="50" id="xmmc"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;证书到期日:</label>
			<div class="controls">
					<input class="input-medium Wdate" type="text" required="required"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
								value="<fmt:formatDate value="${configChargeAgent.zsdqr}" pattern="yyyy-MM-dd"/>"
								name="zsdqr" id="zsdqr" maxlength="20"
								readonly="readonly" />
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="checkRa();"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>

</body>
</html>
