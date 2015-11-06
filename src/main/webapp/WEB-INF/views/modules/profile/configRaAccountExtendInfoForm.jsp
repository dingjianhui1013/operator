<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>证书模版配置添加</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
var repeat = 0 ;
$(document).ready(
		function() {
			$("#raAccountExtendInfoForm")
					.validate(
							{
								submitHandler : function(form) {
									loading('正在提交，请稍等...');
									form.submit();
								},
								errorContainer : "#messageBox",
								errorPlacement : function(error, element) {
									$("#messageBox").text("输入有误，请先更正。");
									if (element.is(":checkbox")
											|| element.is(":radio")
											|| element.parent().is(
													".input-append")) {
										error.appendTo(element.parent()
												.parent());
									} else {
										error.insertAfter(element);
									}
								}
							});
		});
		
		function repeatName(){
			var name = $("#certName").val();
			var extendId = $("#extendId").val();
			var url = "${ctx}/profile/configRaAccountExtendInfo/checkRepeat";
			$.ajax({
					url:url,
					data:{certName:name,extendId:extendId,_:new Date().getTime()},
					dataType:'json',
					success:function(data)
					{
						if(data.status==0){
							if(data.type==1){
								top.$.jBox.tip("证书名称已存在！");
								repeat = 1;
							}else {
								repeat = 0;
							}
						}else{
							top.$.jBox.tip("系统出现异常！");
						}
					}
				
			});
		}
		function onSubmit(){
			if (repeat==1) {
				top.$.jBox.tip("证书名称已存在！");
				 return false;
			}
			 return true;
		} 
		
		
</script>
</head>
<body>
		<ul class="nav nav-tabs">
		<li ><a
			href="${ctx}/profile/configRaAccountExtendInfo/">证书模版配置列表</a></li>
		
		<li class="active"><a href="${ctx}/profile/configRaAccountExtendInfo/form?id=${configRaAccountExtendInfo.id}">证书模版配置<shiro:hasPermission name="profile:configRaAccountExtendInfo:edit">${not empty configRaAccountExtendInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="profile:configRaAccountExtendInfo:edit">查看</shiro:lacksPermission></a></li>
	
		
		
		
	</ul>
	<form:form id="raAccountExtendInfoForm"
		modelAttribute="configRaAccountExtendInfo"
		action="${ctx}/profile/configRaAccountExtendInfo/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<input type="hidden"  id="extendId"  value="${configRaAccountExtendInfo.id }">
		<div class="control-group">
			<label class="control-label">证书名称:</label>
			<div class="controls">					
					<form:input path="certName" htmlEscape="false" onblur="repeatName()" maxlength="50" class="required"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书CN:</label>
			<div class="controls">					
					<form:select path="commonNameDisplayName" >
						<form:option value="0">单位名称</form:option>
						<form:option value="1">证书持有人名称</form:option>
						<form:option value="2">经办人姓名</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书SN:</label>
			<div class="controls">					
					<form:select path="nameDisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">单位名称</form:option>
						<form:option value="1">证书持有人名称</form:option>
						<form:option value="2">经办人姓名</form:option>
						<form:option value="3">组织机构代码</form:option>
						<form:option value="4">证书持有人身份证号</form:option>
						
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">accountOrgunit:</label>
			<div class="controls">					
					<form:select path="orgunitDisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">应用名称</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书邮件:</label>
			<div class="controls">					
					<form:select path="emailDisplayName">
						<form:option value="0">证书持有人邮箱</form:option>
					<%-- 	<form:option value="1">证书申请人邮箱</form:option> --%>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField1:</label>
			<div class="controls">					
					<form:select path="addtionalField1DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">多证书编号</form:option>
						<form:option value="1">单位名称</form:option>
						<form:option value="2">应用名称</form:option>
						<form:option value="3">证书持有人身份证号</form:option>
						<form:option value="4">组织机构代码</form:option>
						<form:option value="5">经办人身份证号</form:option>
						<form:option value="6">证书类型</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField2:</label>
			<div class="controls">					
					<form:select path="addtionalField2DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">多证书编号</form:option>
						<form:option value="1">单位名称</form:option>
						<form:option value="2">业务系统UID</form:option>
						<form:option value="3">证书类型</form:option>
						<form:option value="4">证书持有人身份证号</form:option>
						<form:option value="5">组织机构代码</form:option>
						<form:option value="6">经办人身份证号</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField3:</label>
			<div class="controls">					
					<form:select path="addtionalField3DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">多证书编号</form:option>
						<form:option value="1">应用名称</form:option>
						<form:option value="2">业务系统UID</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField4:</label>
			<div class="controls">					
					<form:select path="addtionalField4DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">工商营业执照注册号</form:option>
						<form:option value="1">业务系统UID</form:option>
						<form:option value="2">证书持有人身份证号</form:option>
						<form:option value="3">多证书编号</form:option>
						<form:option value="4">经办人身份证号</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField5:</label>
			<div class="controls">					
					<form:select path="addtionalField5DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">多证书编号</form:option>
						<form:option value="1">证书类型</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField6:</label>
			<div class="controls">					
					<form:select path="addtionalField6DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">组织机构代码</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField7:</label>
			<div class="controls">					
					<form:select path="addtionalField7DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">工商营业执照注册号</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField8:</label>
			<div class="controls">					
					<form:select path="addtionalField8DisplayName">
						<form:option value="-1">无</form:option>
						<form:option value="0">多证书编号</form:option>
						<form:option value="1">单位名称</form:option>
						<form:option value="2">应用名称</form:option>
						<form:option value="3">证书持有人身份证号</form:option>
						<form:option value="3">证书类型</form:option>
						<form:option value="4">组织机构代码</form:option>
						<form:option value="5">业务系统UID</form:option>
						<form:option value="6">经办人身份证号</form:option>
					</form:select>
			</div>
		</div>
			<div class="control-group">
			<label class="control-label">userAdditionalField9:</label>
			<div class="controls">					
					<form:select path="addtionalField9DisplayName">
						<form:option value="-1">无</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">userAdditionalField10:</label>
			<div class="controls">					
					<form:select path="addtionalField10DisplayName">
						<form:option value="-1">无</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Personal:</label>
			<div class="controls">					
					<form:select path="personal">
						<form:option value="-1">无</form:option>
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Organization:</label>
			<div class="controls">					
					<form:select path="organization">
						<form:option value="-1">无</form:option>
					</form:select>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  onclick="return onSubmit()"
					value="保 存" />&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
			
	</form:form>
</body>
</html>
