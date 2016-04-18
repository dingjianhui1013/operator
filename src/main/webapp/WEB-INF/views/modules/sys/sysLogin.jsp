<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
      html,body,table{background-color:#f5f5f5;width:100%;height:500px;text-align:center;}.form-signin-heading{font-size:36px;margin-bottom:20px;color:#0663a2;}
      .form-signin{position:relative;text-align:left;width:300px;padding:45px 29px 29px;margin:0 auto 20px;background-color:#fff;border:1px solid #e5e5e5;
        	-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:0 1px 2px rgba(0,0,0,.05);-moz-box-shadow:0 1px 2px rgba(0,0,0,.05);box-shadow:0 1px 2px rgba(0,0,0,.05);}
      .form-signin .checkbox{margin-bottom:10px;color:#0663a2;} .form-signin .input-label{font-size:16px;line-height:23px;color:#999;}
      .form-signin .input-block-level{font-size:16px;height:auto;margin-bottom:15px;padding:7px;*width:283px;*padding-bottom:0;_padding:7px 7px 9px 7px;}
      .form-signin .btn.btn-large{font-size:16px;} .form-signin #themeSwitch{position:absolute;right:15px;bottom:10px;}
      .form-signin div.validateCode {padding-bottom:15px;} .mid{vertical-align:middle;}
      .header{height:60px;padding-top:30px;} .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
      label.error{background:none;padding:2px;font-weight:normal;color:inherit;margin:0;}
      .form-group-sign{ margin-bottom:20px;}
      .form-group-sign .btn.btn-large{width:100%;}
      .btnBox{margin-bottom:15px; }
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
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
		});
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement && self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
		
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
		
		function getCrlContextJson(){
			
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
					return true;
				} else
					return false;
			}
		}
		
		function sccaLogin(){
			$("#plogin").css("display","none");
			$("#sub").css("display","none");
			$("#sccaLoginId").show();
			$("#passlogin").show();
			$("#selectCN").show();
			if(logonSign()){
				var index = SignForm.CertList.value;
				var CurCert = arrayCerts[index];
				if(CurCert){
					$("#username").val(CurCert.SerialNumber);
					$("#password").val("SCCALOGIN"+CurCert.SerialNumber);
					$("#ltype").val("1");
					$("#loginForm").submit();
				}
			}else{
				alert("未获取到证书或证书验证失败.	");
			}
		}
	function login()
	{
		$("#plogin").css("display","block");
		$("#sub").css("display","block");
		$("#sccaLoginId").hide();
		$("#passlogin").hide();
		$("#selectCN").hide();
	}
	</script>
</head>
<body>
	<!--[if lte IE 6]><br/><div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a><h4>温馨提示：</h4><p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的 Chrome、Firefox、Safari 等。</p></div><![endif]-->
	<div class="header"><%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
		<c:if test="${sccaError!='error'}">
			<div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
				<label id="loginError" class="error"><%=error==null?"":"com.itrus.ca.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
			</div>
		</c:if>
	</div>
	<h1 class="form-signin-heading">${fns:getConfig('productName')}</h1>
	<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
		<div style="display:none" id="plogin">
			<label class="input-label" for="username">登录名</label>
			<input type="text" id="username" name="username" class="input-block-level required" value="${username}">
			<label class="input-label" for="password">密码</label>
			<input type="hidden" id="ltype" name="type" value = "0" />
			<input type="password" id="password" name="password" class="input-block-level required">
		</div>
		<c:if test="${isValidateCodeLogin}"><div class="validateCode">
			<label class="input-label mid" for="validateCode">验证码</label>
			<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
		</div></c:if>
		<div class="form-group-sign">
			<input class="btn btn-large btn-primary" type="button" onclick="sccaLogin();" value="证书登录" id="sccaLoginId"/>
		</div>
		<div class="form-group-sign">
			<input class="btn btn-large btn-primary" type="button" onclick="login();" value="密码登录" id="passlogin"/>
		</div>
		<div style="display:none" id="sub">
			<div class="btnBox">
				<input class="btn btn-large btn-primary" type="submit" value="登  录"  style="width:45%;margin-right:25px;"/>
				<input class="btn btn-large btn-primary" type="button" onclick="sccaLogin();" value="证书登录" id="sccaLoginId" style="width:45%;"/>
			</div>
			<label for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我（公共场所慎用）</label>
		</div>
		<%-- <div id="themeSwitch" class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}<b class="caret"></b></a>
			<ul class="dropdown-menu">
			  <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
			</ul>
			<!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
		</div> --%>
	</form>
	<form name="SignForm" id="selectCN">
			<div >
				请选择证书：
				<select name="CertList"></select>
				<input name="RandStr" type="hidden" value="2134783982345" />
				<div style="display:none">
					<br>
					待签名原文：
					<textarea name="ToSign" cols="50" rows="10">小王于2006年12月13日提款10万元</textarea>
					<br>
					签名后结果：
					<textarea name="SignedData" cols="50" rows="10"></textarea>
					<br>
				</div>
			</div>
		</form>
	
	Copyright &copy; 2014-${fns:getConfig('copyrightYear')} <a href="${pageContext.request.contextPath}">${fns:getConfig('productName')}</a>
	
		<script language="javascript">
			var arrayCerts = PTA.filterSignCerts();
			alert(arrayCerts.length);
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