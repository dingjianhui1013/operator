<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>初始化key</title>
<meta name="decorator" content="default" />
<META NAME="GENERATOR" Content="Microsoft Visual Studio 6.0">
<script type="text/javascript" src="${ctxStatic}/cert/pta_topca.js"></script>
<script type="text/javascript" src="${ctxStatic}/cert/xenroll.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		GetSN();//获得keysn
		FT_Initialize();//初始化
	});

	function FT_Initialize() {
		var myatl, lRet;
		var p11;
		myatl = document.all("sicca");
		p11 = $("#p11").val();
		lRet = myatl.FT_Initialize(p11);
		FT_GetTokenAttributes();
		if($("#keySn").val()==""){
			GetSN();//获得keysn
		}
	}
	/*
	 * 函数名称：FT_GetTokenAttributes
	 * 功能：获取令牌属性
	 * 返回：0 —— 成功；非0 —— 失败
	 */
	function FT_GetTokenAttributes() {
		var myatl, lRet;
		myatl = document.all("sicca");
		var tokenName = $("#keyName").val();
		lRet = myatl.FT_GetTokenAttributes(tokenName);
		if (0 != lRet) {
			//未读取到相关信息
			top.$.jBox.tip("获取信息失败，请重新获取:"+lRet);
		} else {
			$("#keyDevProId").val(myatl.OutputString1);
			$("#devNo").val(myatl.OutputString2);
			$("#devSn").val(myatl.OutputString3);
			$("#csp").val(myatl.OutputString4);
			$("#publicSpace").val(myatl.OutputNum1);
			$("#privateSpace").val(myatl.OutputNum3);
		}
	}
	
	function GetSN()
	{
		var myocx, rtn;
		myocx = document.getElementById("FTKeyCtrl");
		rtn = myocx.GetSN();
		alert(rtn);
		$("#keySn").val(rtn);
	}
	//低级格式化
	function FT_LowInitToken()
	{
		$("#initBut").hide();
		var myatl, lRet;
		myatl = document.all("sicca");
		var tokenName = $("#keyName").val();
		var sopin = $("#adminPin").val();
		var newsopin = $("#adminNewPin").val();
		var newuserpin = $("#userNewPin").val();
		var newTokenName = $("#newKeyName").val();
		var soEC = $("#adminReTimes").val();
		var userEC = $("#userReTimes").val();
		var pubsize = $("#newPublicSpace").val();
		var prvsize = $("#newPrivateSpace").val();
		lRet = myatl.FT_LowInitToken(tokenName, sopin, newsopin, newuserpin, newTokenName, soEC, userEC, pubsize, prvsize);
		if(lRet==0){
			top.$.jBox.tip("初始化成功！");
		}else{
			top.$.jBox.tip("初始化失败:"+lRet);
			$("#initBut").show();
		}	
		return false;
	}
</script>
</head>
<body>
	<div style="display: none">
		<object classid="clsid:E94F72A4-77E9-409A-856C-D4FC4EAECACC"
			codebase="download/ePassIF.dll#version=1.0.13.1126" id="sicca" name="sicca"
			height="0" width="0"></object>

		<!-- <object id="FTKeyCtrl"
			classid="clsid:407FB808-725E-4dc7-8105-44B0DEEAE615"
			codebase="download/FTSCCACtrl_GetSN.ocx#version=1.0.13.1220" width="0" height="0" style="display: none"> </object>
		 -->
		<OBJECT id="FTKeyCtrl" classid="clsid:407FB808-725E-4dc7-8105-44B0DEEAE615" 
 codebase="download/FTSCCACtrl_GetSN.ocx#version=1.0.13.1220
VIEWASTEXT width="0" height="0" style="display:none"></OBJECT>	
		
			
			
		<!-- <object classid="clsid:E94F72A4-77E9-409A-856C-D4FC4EAECACC"
			codebase="ePassIF.CAB#version=1.0.13.1126" id="sicca" name="sicca"
			height="0" width="0"> </object> -->
	</div>
	&nbsp;&nbsp;&nbsp;令牌名称:
	<input type="text" id="keyName" value="SCCA" />&nbsp;&nbsp; key编号:
	<input type="text" id="keySn" readonly="readonly" value="${keySn }" />
	<br> &nbsp;P11库名称:
	<input type="text" id="p11" value="SccaCSP11.dll">
	<table  id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="2">UKEY属性<input class="btn btn-info" value="重新获取" type="button" onclick="FT_Initialize()"/></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="width: 40%">设备生产商ID</td>
				<td style="width: 60%"><input type="text" id="keyDevProId">
				</td>
			</tr>
			<tr>
				<td>设备型号</td>
				<td><input type="text" id="devNo"></td>
			</tr>
			<tr>
				<td>设备序列号</td>
				<td><input type="text" id="devSn"></td>
			</tr>
			<tr>
				<td>CSP名称</td>
				<td><input type="text" id="csp"></td>
			</tr>
			<tr>
				<td>公有区大小</td>
				<td><input type="text" id="publicSpace"></td>
			</tr>
			<tr>
				<td>私有区大小</td>
				<td><input type="text" id="privateSpace"></td>
			</tr>
		</tbody>
	</table>
	<form id="initForm">
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="2">初始化信息</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="width: 40%">管理员PIN码</td>
				<td style="width: 60%"><input type="text" id="adminPin" class="required" value="${adminPin }">
				</td>
			</tr>
			<tr>
				<td>管理员的新PIN码</td>
				<td><input type="text" id="adminNewPin" class="required" value="${adminPin }"></td>
			</tr>
			<tr>
				<td>管理员重试次数</td>
				<td><input type="text" id="adminReTimes" class="required" value="8"></td>
			</tr>
			<tr>
				<td>用户的新PIN码</td>
				<td><input type="text" id="userNewPin" class="required" value="${userPin }"></td>
			</tr>
			<tr>
				<td>用户重试次数</td>
				<td><input type="text" id="userReTimes" class="required" value="15"></td>
			</tr>
			<tr>
				<td>公有区大小</td>
				<td><input type="text" id="newPublicSpace" class="required" value="22000"></td>
			</tr>
			<tr>
				<td>私有区大小</td>
				<td><input type="text" id="newPrivateSpace" class="required" value="3000"></td>
			</tr>
			<tr>
				<td>令牌的新名称</td>
				<td><input type="text" id="newKeyName" class="required" value="SCCA"></td>
			</tr>
		</tbody>
	</table>
	<div class="form-actions"
		style="text-align: center; width: 100%; border-top: none;">
		<input class="btn btn-primary" type="button"
			value="初始化" id="initBut" onclick="FT_LowInitToken()"/>&nbsp;
	</div>
	</form>
	
	<div style="display: none">		
		<object id="ukeyadmin" codeBase="itrusukeyadmin.cab#version=3,1,13,705" classid="clsid:05395F06-244C-4599-A359-5F442B857C28"></object>
	</div>
</body>
</html>
