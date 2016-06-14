<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>CA配置</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery.form.js"></script>
<script type="text/javascript">

	$(document).ready(
			function() {
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
				
				

				var raProtocol = $("#raProtocol").val();
				if(raProtocol=="WEBSERVICE"){
					$("#password").show();
				}else{
					$("#password").hide();
				}
				
				
			});
	
	function caCertOn() {
		var fileName = $("#caCert1File").val().toString();
		if(fileName==null||fileName==""){
			top.$.jBox.tip("上传失败：您没有添加证书文件，请上传一个base64格式的ca证书文件");
		}
		var options = {
			type : 'post',
			dataType : 'json',
			success : function(data) {
				if(data.caCert=='caCert'){
					top.$.jBox.tip("请上传一个base64格式的ca证书文件");
					 return false;
				}
				if(data.status=='1'){
					top.$.jBox.tip("上传成功");
					$("#caCert").val(data.caCertFile);
				}else{
					top.$.jBox.tip("上传失败："+data.errorMsg);
				}
			}
		};
		$('#caCert1FileForm').ajaxSubmit(options);
	}
	
	function raDeviceCertFile() {
		var fileName = $("#raDeviceCert1File").val().toString();
		if(fileName==null||fileName==""){
			top.$.jBox.tip("上传失败：您没有添加证书文件，请上传一个base64格式的ra设备证书文件");
		}
		var options = {
			type : 'post',
			dataType : 'json',
			success : function(data) {
				if(data.raDevice=='raDevice'){
					top.$.jBox.tip("请上传一个base64格式的ra设备证书文件");
					 return false;
				}
				if(data.status=='1'){
					top.$.jBox.tip("上传成功");
					$("#raDeviceCert").val(data.raDeviceCert);

					$("#accountOrganization").val(data.OU);
					$("#accountOrganization1").val(data.OU);
					$("#accountOrgUnit").val(data.O);
					$("#accountOrgUnit1").val(data.O);
					$("#accountHash").val(data.accountHash);
				}else{
					top.$.jBox.tip("上传失败："+data.errorMsg);
				}
			}
		};
		$('#raCert1FileForm').ajaxSubmit(options);
	}

	function checkRa(){
		var raId = $("#RaId").val();
		var raName = $("#raName").val();
		if(raName==""){
			top.$.jBox.tip("RA模板名称不能为空");
			return false;
		}
		if($("#raProtocol").val()==""){
			top.$.jBox.tip("RA模板名称不能为空");
			return false;
		}
		if($("#caName").val()==""){
			top.$.jBox.tip("CA名称不能为空");
			return false;
		}
		if($("#serviceUrl").val()==""){
			top.$.jBox.tip("服务地址不能为空");
			return false;
		}
		if($("#mobileDeviceUrl").val()==""){
			top.$.jBox.tip("移动设备服务地址不能为空");
			return false;
		}
		if($("#accountOrganization").val()==""){
			top.$.jBox.tip("RA发证机构不能为空");
			return false;
		}
		if($("#accountOrgUnit").val()==""){
			top.$.jBox.tip("RA发证机构单位不能为空");
			return false;
		}
		
		if(!$("#key1").is(":checked")&&!$("#key2").is(":checked")&&!$("#key3").is(":checked")){
			top.$.jBox.tip("请选择秘钥长度");
			return false;
		}
		
		$.ajax({
			url:"${ctx}/profile/configRaAccount/checkRa?raId="+raId+"&_="+new Date().getTime(),
			type:'POST',
			dataType : 'json',
			data:{
				raId:raId,
				raName:raName
			},
			success:function(data){
				if(data.status==0){
					top.$.jBox.tip(data.msg);
				}else if(data.status == 1){
					top.$.jBox.confirm(data.msg,'系统提示',function(v,h,f){
						if(v=='ok'){
							$("#raAccountForm").submit();
						}
					});
				}else if(data.status == 2){
					$("#raAccountForm").submit();
				}
			}
		});
	}
	
	function showAAPWD(){
	
		var raProtocol = $("#raProtocol").val();
		if(raProtocol=="WEBSERVICE"){
			$("#password").show();
		}else{
			$("#password").hide();
		}
	}
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configRaAccount/">RA模板列表</a></li>
		<shiro:hasPermission name="profile:configRaAccount:edit"><li class="active"><a href="${ctx}/profile/configRaAccount/form"><c:if test="${configRaAccount.id==null}">新增RA模板</c:if><c:if test="${configRaAccount.id!=null}">修改RA模板</c:if></a></li></shiro:hasPermission>
	</ul>
	<form:form id="raAccountForm" modelAttribute="configRaAccount"
		action="${ctx}/profile/configRaAccount/save" method="post"
		class="form-horizontal">
		
		<input type="hidden" id="RaId" name="RaId" value=${configRaAccount.id}>
		<tags:message content="${message}" />

		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;RA模板名称:</label>
			<div class="controls">
				<form:hidden path="id" htmlEscape="false" maxlength="50"
							 class="required" />
				<form:input path="raName" htmlEscape="false" maxlength="50"
							class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;CA类型:</label>
			<div class="controls">
				<form:select onchange="showAAPWD()" path="raProtocol">
					<form:option value="ICA">ICA</form:option>
					<form:option value="TCA">TCA</form:option>
					<form:option value="WEBSERVICE">WEBSERVICE</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group" id="password" style="display: none;">
			<label class="control-label"><span style="color : red">*</span>&nbsp;AA密码:</label>
			<div class="controls">
				<form:input  path="aaPassword" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;CA名称:</label>
			<div class="controls">
				<form:hidden path="id" htmlEscape="false" maxlength="50"
					class="required" />
				<form:input path="caName" htmlEscape="false" maxlength="50"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;单双证标识:</label>
			<div class="controls">				
				<form:radiobutton path="isSingleCert" value="1" id="radio1" checked="checked"/>单证
				<form:radiobutton path="isSingleCert" value="0" id="radio2"/>双证
			</div>
		</div>

		<!--<div class="control-group">
			<label class="control-label">是否测试:</label>
			<div class="controls">				
				<form:radiobutton path="isTest" value="1"/>是
				<form:radiobutton path="isTest" value="0"/>否
			</div>
		</div>-->
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;服务地址:</label>
			<div class="controls">
				<form:input path="serviceUrl" htmlEscape="false" maxlength="256"
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;移动设备服务地址:</label>
			<div class="controls">
				<form:input path="mobileDeviceUrl" htmlEscape="false" maxlength="256"
					class="required" />
			</div>
		</div>
		<!--<div class="control-group">
			<label class="control-label">主机IP:</label>
			<div class="controls">
				<form:input path="ip" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主机端口:</label>
			<div class="controls">
				<form:input path="port" htmlEscape="false" maxlength="50"/>
			</div>
		</div>-->
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;RA发证机构:</label>
			<div class="controls">
					<form:input path="accountOrganization" htmlEscape="false" 
					class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;RA发证机构单位:</label>
			<div class="controls">
					<form:input path="accountOrgUnit" htmlEscape="false" 
					class="required" />
			</div>
		</div>
		<!--<div class="control-group">
			<label class="control-label">key类型标识:</label>
			<div class="controls">
					<form:input path="keyType" htmlEscape="false" maxlength="50"/>
			</div>
		</div>-->
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;秘钥长度:</label>
			<div class="controls" >
					<input name="keyLen" type="radio" id="key1" value="256"  <c:if test="${configRaAccount.keyLen==256}">checked="checked"</c:if> /><span>256</span>
					<input name="keyLen" type="radio" id="key2" value="1024" <c:if test="${configRaAccount.keyLen==1024 }">checked="checked"</c:if> /><span>1024</span>
					<input name="keyLen" type="radio" id="key3" value="2048" <c:if test="${configRaAccount.keyLen==2048 }">checked="checked"</c:if> /><span>2048</span>
			</div>
		</div>
		<!--<div class="control-group">
			<label class="control-label">证书默认有效期:</label>
			<div class="controls">
				<form:input path="defaultOverrideValidity" htmlEscape="false" readonly="" maxlength="50"
					class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">吊销列表地址:</label>
			<div class="controls">
				<form:input path="crlListUrl" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">RA版本:</label>
			<div class="controls">
				<form:input path="raVersion" htmlEscape="false" maxlength="200"/>
			</div>
		</div>-->
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="checkRa();"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
