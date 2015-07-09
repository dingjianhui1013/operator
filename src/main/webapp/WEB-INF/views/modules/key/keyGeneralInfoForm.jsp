<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key类型信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/cert/xenroll.js"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				
				if ($("#defaultSoPinType").val() == 'nonauto') {
					$("#adminPin").show();
				} else {
					$("#adminPin").hide();
					$("#defaultSoPin").val("");
				}
				$("#name").focus();
				$("#inputForm")
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
				var opName = "${keyType.name}";
				$.each(legibleNameMap, function(idx, value, ele) {
					if(idx==opName){
						$("#name").append("<option value='"+idx+"' selected='selected'>" + idx + "</option>");
					}else{
						$("#name").append("<option value='"+idx+"'>" + idx + "</option>");						
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
		<li><a
			href="${ctx}/key/keyGeneralInfo?supplierId=${supplierId }">key类型信息列表</a></li>
		<li class="active"><a
			href="${ctx}/key/keyGeneralInfo/form?id=${keyGeneralInfo.id}">添加key类型标识</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="keyGeneralInfo"
		action="${ctx}/key/keyGeneralInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<input type="hidden" id="supplierId" name="supplierId"
			value="${supplierId }">
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>Key类型名称:</label>
			<div class="controls">
				<select name="name" id="name" class="required"
					<c:if test="${not empty keyGeneralInfo.id}">
					disabled="disabled"
						</c:if>>
					<c:if test="${not empty keyGeneralInfo.id}">
					<option value=" ${keyGeneralInfo.id}">${keyGeneralInfo.name}</option>
					</c:if>
					
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>Key类型标识:</label>
			<div class="controls">
				<input type="text" id="model" name="model" maxlength="11"
					<c:if  test="${not empty keyGeneralInfo.id}">
					disabled="true" 
				</c:if>
					value="${keyGeneralInfo.model}" class="required" /> 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>技术支持姓名:</label>
			<div class="controls">
				<form:input path="linkman" htmlEscape="false" maxlength="11"  
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>技术支持电话:</label>
			<div class="controls">
				<form:input path="linkmanPhone" htmlEscape="false" maxlength="11" id="contactPhone"  onchange="checkMobile(this)"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>用户初始PIN码:</label>
			<div class="controls">
				<form:input path="defaultUserPin" htmlEscape="false" maxlength="11"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">管理员PIN码类型:</label>
			<div class="controls">
				<form:select path="defaultSoPinType" id="defaultSoPinType"
					onchange="changeDefaultSoPinDis()">
					<form:option value="nonauto">固定值</form:option>
					<form:option value="autoht">海泰</form:option>
					<form:option value="autoft">飞天</form:option>
					<form:option value="autokoal">格尔</form:option>
				</form:select>
			</div>
		</div>
		<script type="text/javascript">
			function changeDefaultSoPinDis() {
				if ($("#defaultSoPinType").val() == 'nonauto') {
					$("#adminPin").show();
				} else {
					$("#adminPin").hide();
					$("#defaultSoPin").val("");
				}
			}
		</script>
		<div class="control-group" id="adminPin">
			<label class="control-label"><font color="red">*</font>管理员初始PIN码:</label>
			<div class="controls">
				<form:input path="defaultSoPin" id="defaultSoPin" htmlEscape="false"
					maxlength="50" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="description" htmlEscape="false" maxlength="50"></form:textarea>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="key:keyGeneralInfo:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
