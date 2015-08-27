<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<!-- [if IE 6] -->
<%-- <script type="text/javascript"  src="${ctxStatic}/iLetter/js/DD_belatedPNG.js" ></script>
<script>
DD_belatedPNG.fix('.nav1 a,.nav2 a,.nav3 a,.nav4 a,.nav5 a,.nav6 a,.nav1_cur a,.nav2_cur a,.nav3_cur a,.nav4_cur a,.nav5_cur a,.nav6_cur a,.btn_shezhi a,.btn_zxh a,.btn_close a,.pop_close a,.zsfw_xgpass_form,.zsfw_xgpass_ok,.zsfw_js_step2_c,.zsfw_js_step4_c,.zsfw_gxgx_error,.font_yyz,b,img');
</script> --%>
<!-- [end if]-->
<script type="text/javascript">
	function changeApp() {
		var appId = $("#configAppId").val();
		if (appId != "") {
			$.ajax({
				url : "${ctxILetter}/enroll/changeApp?configAppId=" + appId
						+ "&_=" + new Date().getTime(),
				dataType : "json",
				success : function(d) {
					var html = "";
					html += "<option value=\""+""+"\">请选择产品</ooption>";
					for (var i = 0; i < d.length; i++) {
						html += "<option value=\""+d[i].id+"\">"
								+ d[i].productName + "</ooption>";
					}
					$("#products").html(html);
				}
			});
		}
	}
	
	
	function changeAgent(){
		var products =$("#products").val();
		if (products != "") {
			$.ajax({
				url : "${ctxILetter}/enroll/changeAgent?products=" + products
						+ "&_=" + new Date().getTime(),
				dataType : "json",
				success : function(d) {
					var html = "";
					html += "<option value=\""+""+"\">请选择计费策略</ooption>";
					for (var i = 0; i < d.length; i++) {
						html += "<option value=\""+d[i].id+"\">"
								+ d[i].productName + "</ooption>";
					}
					$("#configAgentId").html(html);
				}
			});
		}
		
		
	}
	
	
	

	function bbfw_from() {
		var productId = $("#products").val();
		if (productId != "") {
			document.bbfw2_form.submit();
		} else {
			$("#msg").html("请选择产品类型！");
		}
	}

	function changeProduct() {
		var productId = $("#products").val();
		if (productId != "") {
			$.ajax({
				url : "?productId=" + productId + "&_=" + new Date().getTime(),
				dataType : "json",
				success : function(d) {

				}
			});
		}
	}
	
	function backToMain(){
		window.location=external.GetCookie("zhengshufuwu"); 
	}
</script>
</head>
<body oncontextmenu="return false" onselect="return false"
	oncanplay="return false">
	<!--body中加入了禁止复制功能 可根据需求删除-->
	<div class="main2">
		<div class="zsfw_gxgx_top">
			<div class="zsfw_gxgx_top_T fl">
				<img src="${ctxStatic}/iLetter/images/btn_bb_T.png" width="165"
					height="51" />
			</div>
			<div class="zsfw_bbfw_step1 fl"></div>
			<div class="btn_fanhuizsfw fl">
				<a href="javascript:backToMain()"></a>
			</div>
		</div>
		<div class="clear"></div>
		<div class="zsfw_gxgx_bd" style="padding-top: 20px">
			<div class="zsfw_gxgx_info1HW">
				<form action="${ctxILetter}/enroll/bbfw2form" method="post"
					name="bbfw2_form">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="10" colspan="3"></td>
						</tr>
						<tr>
							<td width="19%" align="right" valign="top"></td>
							<td width="2%"></td>
							<td width="79%" valign="top">&nbsp;请&nbsp;选&nbsp;择&nbsp;应&nbsp;用&nbsp;： <select
								name="configAppId" id="configAppId" onchange="changeApp();">
									<option value="">请选择应用</option>
									<c:forEach items="${configApps }" var="configApp">
										<option value="${configApp.id }">${configApp.appName }</option>
									</c:forEach>
							</select> <br /> &nbsp;请&nbsp;选&nbsp;择&nbsp;产&nbsp;品&nbsp;： <select name="configProductId" id="products" onchange="changeAgent()">
									<option value="">请选择产品</option>
							</select><br /> 请选择计费策略： <select name="configAgentId" id="configAgentId">
									<option value="">请选择计费策略</option>
							</select>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<span id="certsn" style="display: none"></span>
			<div class="btn_sqdx" style="padding-top: 30px">
				<div id="nextt" class="btn_nextStep" style="text-align: center">
					<a href="javascript:bbfw_from()" id="aHref"></a>
				</div>
			</div>
		</div>
		<div class="clear"></div>
		<div class="n-tispsBox" id="msg" style="color: red; margin-top: -55px">${msg }</div>
	</div>
</body>
</html>
