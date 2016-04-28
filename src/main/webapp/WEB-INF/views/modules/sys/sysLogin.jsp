
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
      .form-signin{position:relative;text-align:left;width:330px;padding:15px 20px 5px;margin:0 auto 20px;background-color:#fff;border:1px solid #e5e5e5;
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
      .selectzsBox{background:#fff;width:350px;padding:15px 10px; margin:0px auto 10px; overflow:hidden;border:1px solid #e5e5e5;-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;}
      .selectzsBox form{margin-bottom:5px;}
      /* .caInfo{ text-align:left; padding:10px 10px 0px; border-top:1px solid #ddd; margin-bottom:10px;}
      .caInfo p{margin-bottom:5px; display:block} */
    </style>
    <script src="${ctxStatic}/js/pta.js" type="text/javascript"></script>
	<script type="text/javascript">
		//颁发者CA的DN项
		var arrayIssuerDN;
		var arrayCerts;
		$(document).ready(function() {
			
			if(${type==0}){
				login();
			}
			
			/* arrayIssuerDN = ["C=CN, O=CFCA Operation CA2","CN=天诚安信测试RSA1024用户CA, OU=TOPCA, O=TOPCA, C=CN","C=CN, O=四川省数字证书认证管理中心有限公司, OU=SCEB CA, CN=SCEB CA"]; */
			/* arrayIssuerDN = ${subjects}; */
		 	/* arrayIssuerDN=["CN=天诚安信测试RSA1024用户CA, OU=TOPCA, O=TOPCA, C=CN"]; */
		 	
		 	
		 	if(${crlList}.list==""){
		 		arrayIssuerDN = "";
		 	}else{
		 		arrayIssuerDN = ${crlList}.list;	
		 		
		 	}
		 	
		 	listCerts(arrayIssuerDN);
		 	
		 	
		 	
			
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
		
		function logonSign(){
			var index = SignForm.CertList.value;
			
			if(index == -1) {
				// 没有找到证书，是否允许登陆？
				// return true;允许登陆，后来判断是否签名
				// return false;无证书不允许登陆
				return false;
			} else {					
				var signedData = PTA.signLogonData($("#randomString").val(),arrayCerts[index]);
				if(signedData.length > 0){
					$("#signedData").val(signedData);
					return true;
				} else
					return false;
			}
		}
		
		function getCertInfo(){
			var index = SignForm.CertList.value;
			var CurCert = arrayCerts[index];
			/* $("#caInfo").show();
			$("#qfz").html(CurCert.Issuer);
			$("#zt").html(CurCert.Subject);
			$("#xlh").html(CurCert.SerialNumber);
			$("#ks").html(CurCert.ValidFrom+"");
			$("#js").html(CurCert.ValidTo+""); */
		}
		
		function tabSccaLogin(){
			$("#plogin").css("display","none");
			$("#sub").css("display","none");
			$("#sccaLoginId").show();
			$("#passlogin").show();
			/* $("#caInfo").show(); */
			$("#certLogin").show();
			$("#caInfo").show();
			$(".selectzsBox").show();
		}
		
		function listCerts(arrayIssuerDN){
			if(arrayIssuerDN==""){
				arrayCerts = new Array();
			}else{
				arrayCerts = PTA.filterSignCerts(arrayIssuerDN);
			}
			
			
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
				var CurCert = arrayCerts[0];
				/* $("#caInfo").show();
				$("#qfz").html(CurCert.Issuer);
				$("#zt").html(CurCert.Subject);
				$("#xlh").html(CurCert.SerialNumber);
				$("#ks").html(CurCert.ValidFrom+"");
				$("#js").html(CurCert.ValidTo+""); */
			}
			
		}
		
		function sccaLogin(){	
			$("#plogin").css("display","none");
			$("#sub").css("display","none");
			$("#sccaLoginId").show();
			$("#passlogin").show();
			/* $("#caInfo").show(); */
			$("#certLogin").show();
			
		 	if(logonSign()){
				var index = SignForm.CertList.value;
				var CurCert = arrayCerts[index];
				if(CurCert){
					$("#type").val("1");
					$("#username").val(CurCert.CommonName);
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
		/* $("#caInfo").hide(); */
		$(".selectzsBox").hide();
		$("#certLogin").hide();
	}
	</script>
</head>
<body>
	<!--[if lte IE 6]><br/><div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a><h4>温馨提示：</h4><p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的 Chrome、Firefox、Safari 等。</p></div><![endif]-->
	<div class="header"><%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
	
		<c:if test="${type==0 }">
		<div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
				<label id="loginError" class="error"><%=error==null?"":"com.itrus.ca.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"com.itrus.ca.modules.sys.security.LoginTypeException".equals(error)?"该管理员不支持密码登录.":"用户或密码错误, 请重试." %></label>
			</div>
		</c:if>
		<c:if test="${type==1 }">
			<div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>				
				<label><%=request.getAttribute("shiroLoginFailure") %></label>
			</div>
		</c:if>	
	
	</div>
	<h1 class="form-signin-heading">${fns:getConfig('productName')}</h1>
	<div  class="selectzsBox">
		<form name="SignForm" id="certLogin" action="${ctx}/sys/certLogin" method="post">
				<strong>请选择证书：</strong>
				<select name="CertList" onchange="getCertInfo();" style="width:71%;"></select>
				
				
			
		</form>
		<!-- <div id="caInfo" class="caInfo">
			<p>颁发者:<span id="qfz"></span></p>
			<p>主题:<span id="zt"></span></p>
			<p>序列号:<span id="xlh"></span></p>
			<p>开始:<span id="ks"></span></p>
			<p>结束:<span id="js"></span></p>
		</div>	 -->
	</div>
	<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
		
		<input type="hidden" value="${randomString }" id="randomString" />
		
		<div style="display:none" id="plogin">
			<label class="input-label" for="username">登录名</label>
			<input name="signedData" type="hidden" id="signedData" />
			<input type="text" id="username" name="username" class="input-block-level required" value="${username}">
			<label class="input-label" for="password">密码</label>
			<input type="hidden" id="type" name="type" value = "0" />
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
				<input class="btn btn-large btn-primary" type="button" onclick="tabSccaLogin();" value="证书登录" id="sccaLoginId" style="width:45%;"/>
			</div>
			<label for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我（公共场所慎用）</label>
		</div>
		
	</form>
	
	
	 Copyright &copy; 2014-${fns:getConfig('copyrightYear')} <a href="${pageContext.request.contextPath}">${fns:getConfig('productName')}</a> 
	
	<%-- Copyright &copy; 2014-${fns:getConfig('copyrightYear')} ${fns:getConfig('companyName')} --%>
</body>
</html>