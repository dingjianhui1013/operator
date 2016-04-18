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
		//颁发者CA的DN项
		var arrayIssuerDN = setArrayIssuerDN();
	
		function setArrayIssuerDN(){
			var array = new Array();
			$.ajax({
				url:"${ctx}/itrust/getCrlContextJson",
				type:"GET",
				dataType:"JSON",
				async: false,
				success:function(d){
					$.each(d.crlList,function(i,e){
						array[i] = e.certSubject;
					})
				}
			});
			return array;
		}
	
		$(document).ready(function() {
			var loginType = "${user.loginType}";
			if(loginType=='1'){
				logonSign();
				$(".srk").hide();
				$("#selectCN").show();
			}
			
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
					if($("#sccaLogin").attr("checked")=="checked"){
						var index = SignForm.CertList.value;
						var CurCert = arrayCerts[index];
						if(CurCert&&CurCert.SerialNumber&&CurCert.SerialNumber!="${user.sccaNumber}"){
							var Result=PTA.VerifyStringSignature(SignForm.SignedData.value,SignForm.ToSign.value);
							if(Result){
								var seeaPassword = $("#sccaPasssword").val();
								if(seeaPassword!=null&&seeaPassword!="${user.sccaNumber}"){
									form.submit();
								}else{
									alert("证书登录设置失败,请重新	!");
								}
							}else{
								alert("证书登录设置失败,请查看您的证书是否有效合法!");
							}
						}else{
							form.submit();
						}
					}else{
						var seeaPassword1 = $("#sccaPasssword1").val();
						var seeaPassword = $("#sccaPasssword").val();
						if(seeaPassword1==seeaPassword){
							loading('正在提交，请稍等...');
							form.submit();
						}else{
							alert("密码与确认密码不相同请重新输入!");
						}
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
		
		var PD = "${user.password}";
		// ------------------------证书JS
		function showCertInfo() {
			var index = SignForm.CertList.value;
			if(index == -1) {
				// 没有找到证书
			} else {
				var certEntityInfo = new CertEntityInfo(arrayCerts[index]);
				var CertInfo = "";
				CertInfo = CertInfo + "实体名称=" + certEntityInfo._Name + "\n";
				CertInfo = CertInfo + "实体证件号码=" + certEntityInfo._IdentityCode + "\n";
				CertInfo = CertInfo + "企业执照号=" + certEntityInfo._ICRegistrationNumber + "\n";
				CertInfo = CertInfo + "多证书编号=" + certEntityInfo._MultiCertNumber + "\n";  
				CertInfo = CertInfo + "个人所属单位名称=" + certEntityInfo._OrgNameOfPerson + "\n";
				CertInfo = CertInfo + "个人所属单位组织机构代码=" + certEntityInfo._OrgIDOfPerson + "\n";
				alert(CertInfo);	
				
				alert("证书公钥："+arrayCerts[index].PublicKey);
				alert("主题密钥标识符："+PTA.GetSKIEx(arrayCerts[index]));
				
				//return ;
			}
		}

		
		function stringSignAndVerify(){
			var index = SignForm.CertList.value;
			if(index == -1) { 
				alert("没有找到数字证书");
				return false;
			}
			var CurCert = arrayCerts[index];
			alert(CurCert.SerialNumber);
			var plainData = SignForm.ToSign.value;	
			var signedData = PTA.SignString(plainData,CurCert,false,true);	
			SignForm.SignedData.value = signedData;
			alert("【不包含原文的字符串签名】签名值="+signedData);
			
			var Result=PTA.VerifyStringSignature(signedData,plainData);
			if(Result){
				alert("【不包含原文的字符串签名】验证成功");
			}else{
				alert("【不包含原文的字符串签名】验证失败");
			}
			
			var Cert = PTA.GetCertFromSign(signedData);
			alert(Cert.Subject);
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			var signedData=PTA.SignString(plainData,CurCert,true,true);
			SignForm.SignedData.value = signedData;
			alert("【包含原文的字符串签名】签名值="+signedData);
				
			var Result=PTA.VerifyStringSignature(signedData);
			if(Result){
				alert("【包含原文的字符串签名】验证成功");
			}else{
				alert("【包含原文的字符串签名】验证失败");
			}
			
			var Cert = PTA.GetCertFromSign(signedData);
			alert(Cert.Subject);
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			var Content = PTA.GetContentFromSign(signedData);
			SignForm.SignedData.value = Content;
			alert("【包含的原文内容文本显示】="+Content);
		
		}
		
		function fileSignAndVerify(){
		
			/*
			var signedFileName="d:\\签名后文件.p7s";
			
			var Result=PTA.VerifyFileP7S(signedFileName);
			if(Result){
				alert("【包含原文的P7S文件签名】验证成功");
			}else{
				alert("【包含原文的P7S文件签名】验证失败");
			}
			
			var Cert = PTA.GetCertFromP7S(signedFileName);
			alert("P7S文件签名中的签名证书主题=【"+Cert.Subject+"】");
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			var outFileName=signedFileName+".ori";
			alert(outFileName);
			var Content = PTA.GetContentFromP7S(signedFileName,"D:\\2.bid");
			SignForm.SignedData.value = signedData;
			alert("【P7S文件签名中包含的原文内容文本显示】="+Content);
			
			return  ;	
			//*/
			
			var index = SignForm.CertList.value;
			if(index == -1) { 
				alert("没有找到数字证书");
				return false;
			}
			var CurCert = arrayCerts[index];
			
			var srcFileName="D:\\CS007.pfx";//inkaNet_1.0.12.exe";//Thinking_in_Java.pdf";//env_root.txt";
			
			///*
			var signedFileName=srcFileName+".p7s";
			
			var Result=PTA.SignFileP7S(srcFileName,signedFileName,CurCert,true);
			if(Result){
				alert("【包含原文的P7S文件签名】签名成功，输出P7S签名文件="+signedFileName);
			}else{
				alert("【包含原文的P7S文件签名】签名失败");
			}
			
			var Result=PTA.VerifyFileP7S(signedFileName);
			if(Result){
				alert("【包含原文的P7S文件签名】验证成功");
			}else{
				alert("【包含原文的P7S文件签名】验证失败");
			}
			
			var Cert = PTA.GetCertFromP7S(signedFileName);
			alert("P7S文件签名中的签名证书主题=【"+Cert.Subject+"】");
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			var outFileName=srcFileName+".ori";
			var Result = PTA.GetContentFromP7S(signedFileName,outFileName);
			if (Result){
				alert("【从P7S签名中导出原文】成功，输出原始文件="+outFileName);
			}else{
				alert("【从P7S签名中导出原文】失败");
			};
		
		//return ;
			
			var signedData=PTA.SignFile(srcFileName,CurCert,false,true);
			SignForm.SignedData.value = signedData;
			alert("【不包含原文的文件签名】签名值="+signedData);
			
			var Result=PTA.VerifyFileSignature(signedData,srcFileName);
			if(Result){
				alert("【不包含原文的文件签名】验证成功");
			}else{
				alert("【不包含原文的文件签名】验证失败");
			}
			
			var Cert = PTA.GetCertFromSign(signedData);
			alert(Cert.Subject);
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			//*/
			
			//PTA.SignFile(srcFileName,CurCert,true,true);
			
			///*
			var signedData=PTA.SignFile(srcFileName,CurCert,true,true);
			SignForm.SignedData.value = signedData;
			alert("【包含原文的文件签名】签名值="+signedData);
				
			var Result=PTA.VerifyFileSignature(signedData);
			if(Result){
				alert("【包含原文的文件签名】验证成功");
			}else{
				alert("【包含原文的文件签名】验证失败");
			}
			///*
			var Cert = PTA.GetCertFromSign(signedData);
			alert(Cert.Subject);
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			var Content = PTA.GetContentFromSign(signedData);
			SignForm.SignedData.value = signedData;
			alert("【包含的原文内容文本显示】="+Content);
			//*/
			
			var signedFileName=srcFileName+".sign";
			
			var Result=PTA.SignFileEx(srcFileName,signedFileName,CurCert,false,true);
			if(Result){
				alert("【不包含原文的文件签名】签名成功，输出签名文件="+signedFileName);
			}else{
				alert("【不包含原文的文件签名】签名失败");
			}
			
			var Result=PTA.VerifyFileSignatureEx(signedFileName,srcFileName);
			if(Result){
				alert("【不包含原文的文件签名】验证成功");
			}else{
				alert("【不包含原文的文件签名】验证失败");
			}
			
			var Result=PTA.SignFileEx(srcFileName,signedFileName,CurCert,true,true);
			if(Result){
				alert("【包含原文的文件签名】签名成功，输出签名文件="+signedFileName);
			}else{
				alert("【包含原文的文件签名】签名失败");
			}
			
			var Result=PTA.VerifyFileSignatureEx(signedFileName);
			if(Result){
				alert("【包含原文的文件签名】验证成功");
			}else{
				alert("【包含原文的文件签名】验证失败");
			}
			
			var Cert = PTA.GetCertFromSign(signedFileName,"File");
			alert(Cert.Subject);
			//alert(Cert.CommonName);
			//alert(Cert.GetEncodedCert(2));
			
			var outFileName=srcFileName+".ori";
			
			var Result = PTA.GetContentFromSign(signedFileName,"File",outFileName);
			if (Result){
				alert("【从包含原文的文件签名中导出原文】成功，输出原始文件="+outFileName);
			}else{
				alert("【从包含原文的文件签名中导出原文】失败");
			};
		
		}

		function logonSign(){
			var index = SignForm.CertList.value;
			if(index == -1) {
				// 没有找到证书，是否允许登陆？
				// return true;允许登陆，后来判断是否签名
				// return false;无证书不允许登陆
				return false;
			} else {					
				if (SignForm.RandStr.value.indexOf("LOGONDATA:") == -1){
					SignForm.RandStr.value = "LOGONDATA:" + SignForm.RandStr.value;
				}		
				var signedData = PTA.signLogonData(SignForm.RandStr.value,arrayCerts[index]);
				if(signedData.length > 0){
					SignForm.ToSign.value = SignForm.RandStr.value;
					SignForm.SignedData.value = signedData;						
					var CurCert = arrayCerts[index];
					$("#sccaPasssword").val(CurCert.SerialNumber);
					alert(CurCert.SerialNumber);
					alert(CurCert.ValidFrom);
					return true;
				} else
					return false;
			}
		}

		function logonVerify(){
			var Result=PTA.VerifyStringSignature(SignForm.SignedData.value,SignForm.ToSign.value);
			if(Result){
				alert("证书验证通过");	
			}else{
				alert("证书验证失败");
			}						
		}
		
		function validationT(){
			if($("#sccaLogin").attr("checked")=="checked"){
				$(".srk").hide();
				$("#validationZS").show();
				$("#selectCN").show();
				logonSign();
			}else{
				$("#selectCN").hide();
				$(".srk").show();
				$("#sccaPasssword").val("");
				$("#validationZS").hide();
			}
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}"><shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'创建'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission>管理员</a></li>
	</ul><br/>
	<form name="SignForm" class="form-horizontal" style="display:none" id="selectCN">
		<div class="control-group">
			<label class="control-label">请选择证书：</label>
			<div class="controls">
                <select name="CertList"></select>
				<input name="RandStr" type="hidden" value="2134783982345" />
			</div>
		</div>
		<div style="display:none">
			<br>
			待签名原文：
			<textarea name="ToSign" cols="50" rows="10">小王于2006年12月13日提款10万元</textarea>
			<br>
			签名后结果：
			<textarea name="SignedData" cols="50" rows="10"></textarea>
			<br>
		</div>
	</form>
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
		<%-- <div class="control-group">
			<label class="control-label">工号:</label>
			<div class="controls">
				<form:input path="no" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登录类型:</label>
			<div class="controls">
				<input type = "radio" onclick="javascript:validationT();" <c:if test="${user.loginType!='1' }"> checked="checked" </c:if> name = "loginType" value ="0" /> 密码登录
				<input type = "radio" id="sccaLogin" onclick="javascript:validationT();" <c:if test="${user.loginType=='1' }"> checked="checked" </c:if> name = "loginType" value ="1" /> 证书登录
				<a class="ml20" href="javascript:logonVerify();" id="validationZS" <c:if test="${user.loginType!='1' }"> style="display:none" </c:if> margin-left:50px" >验证证书</a>
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
	
	<script language="javascript">
		var arrayCerts = PTA.filterSignCerts();
		var objCertList = SignForm.CertList; //定义<select>对象
		//先清空CertList
		for(var i = objCertList.length - 1; i >= 0 ; i--)
			objCertList.options[i] = null;
		
		//把满足要求的证书加入CertList
		if(arrayCerts.length == 0) {
			var el = document.createElement("option");
			el.text = "没有找到数字证书";
			el.value = -1;
			objCertList.add(el);
		} else {
			for(var i = 0; i < arrayCerts.length ; i++) {
				var el = document.createElement("option");
				el.text = arrayCerts[i].CommonName;
				el.value = i;
				objCertList.add(el);
			}
		}
	</script>
</body>
</html>