<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<!-- 
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<link href="${ctxStatic}/bootstrap/3.0.3/css/bootstrap.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/3.0.3/js/bootstrap.js" type="text/javascript"></script>
-->
<script type="text/javascript" src="${ctxStatic}/cert/pta_topca.js"></script>
<script type="text/javascript" src="${ctxStatic}/cert/xenroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/msg/msg.js"></script>

<script type="text/javascript">
	var sn;
	var keySn = external.ukeyserial;
	var keySN;
	var day;
	var csr;
	var cspStr;
	var baseDay = parseInt("${workDealInfo.year*365+workDealInfo.lastDays }");
	var vProgressTimeTAG; 
	var timeout = 1;
	var keystutus = 1;
	var jinduStatus=1;
	var zzStatus = 1;

	function close_show(){
		$("#show_key").hide();
		$("#jindutiao").show();	
		setCount();
	}


function setCount() {
	timeout = timeout + 1;
	
	if (timeout < 0) {
		timeout = 0;
	}
	if(timeout==4){
		keySN = keySn;
		day = baseDay;
		
		var len = 1024;
		cspStr = external.cspname;
		csr = getCsrByOldCert(len);
		csr = filter(csr);
		if (csr == ""){
			show_msg("异常业务");
			return false;
		}
		timeout=35;
	}
	if(timeout==35){
		cspStr = encodeURI(encodeURI(cspStr));
		var url = "${ctx}/ca/enrollMakeCert?reqOverrideValidity=" + day
			+ "&certProvider=" + cspStr + "&keySn=" + keySN + "&csr=" + csr
			+ "&dealInfoId=${workDealInfo.id}&_="+new Date().getTime();
		$.ajax({
			url : url,
			async : false,
			dataType : 'json',
			success : function(data) {
				if (data.status == 1) {
					DoInstallCert1(data);
					
					zzStatus=0;
				} else {
					show_msg(data.msg);
					jinduStatus = 0;
				}
			}
		});
	}
	if(timeout==80&&zzStatus==0){
		var updateUrl = "${ctx}/ca/installResult4Enroll?dealInfoId=${workDealInfo.id}&result=1&_="+new Date().getTime();
		$.getJSON(updateUrl,function(res) {
			window.location.href="${ctxILetter}/enroll/gxfw3Nextform?id=${workDealInfo.id}";
		});
	}
	
	if (timeout > 100) {
		timeout = 100;
		clearTimeout(vProgressTimeTAG);
		if(keystutus==1){
			document.getElementById('play3').innerHTML = "更新完成";
		}else{
			document.getElementById('play3').innerHTML = "更新失败";
		}
	}
	if(jinduStatus==1){
		var widthSize = document.getElementById('play1').clientWidth;	//获得控件width的值
		var size = (widthSize / 100) * timeout;
		document.getElementById('play2').style.width = size;
		document.getElementById('play4').innerHTML = timeout+"%";
		clearTimeout(vProgressTimeTAG);
		vProgressTimeTAG = setTimeout("setCount()",100);	
	}
}



	$(document).ready(function(){
		$("#keySnDiv").html("key序列号："+keySn);
	});
	
	function nextStep(){
		var url = "${ctxILetter}/enroll/gxfw3Submit?_="+new Date().getTime();
	 	$.getJSON(url,function(data){
			if(data.status=="1"){
				$("#msg").html("提交成功");	
				$("#gxfw3form").submit();
			}else{
				$("#msg").html("提交失败");
				$("#gxfw3falseform").submit();
			}
		});
 	}

	function getCsrByOldCert(len) {
		var useOldKey = true;
		var certArray = filterCerts("", 0, "${signSerialNumber}");//查找当前第一张证书,被更新的
		var objOldCert;
		var csp = external.cspname;
		for (var i = 0; i < certArray.length; i++) {
			if (certArray[i].CSP == csp) {
				objOldCert = certArray[i];
				break;
			}
		}
		try {
			var csr = genRenewCSR(csp, 1, len, objOldCert,useOldKey);
			if (csr.length == 0) {
				return "";
			}
			return filter(csr);
		} catch (e) {
			//return false;
		}
	}

	function show_msg(msg){
		$("#msg").html(msg);
		$("#close_a").attr("onclick","javascript:void(0)");
	}

	function quick() {
		var keySN = keySn;
		var day = baseDay;
		var csr;
		var len = 1024;
		cspStr = external.cspname;
		//if ($("[name=provider]").val().length > 0) {
		//	csr = genEnrollCSR($("[name=provider]")[0], len, 1);
		//}
		//如果是更新的:
		csr = getCsrByOldCert(len);
		csr = filter(csr);
		if (csr == "") {//异常业务
			$("#msg").html("异常业务");
		}else{
			cspStr = encodeURI(encodeURI(cspStr));
			var url = "${ctx}/ca/enrollMakeCert?reqOverrideValidity=" + day
					+ "&certProvider=" + cspStr + "&keySn=" + keySN + "&csr=" + csr
					+ "&dealInfoId=${workDealInfo.id}&_="+new Date().getTime();
			
			$.ajax({
				url : url,
				async : false,
				dataType : 'json',
				success : function(data) {
					if (data.status == 1) {
						DoInstallCert1(data);
						//$("#msg").html("制证成功");
						var updateUrl = "${ctx}/ca/installResult4Enroll?dealInfoId=${workDealInfo.id}&result=1&_="+new Date().getTime();
						$.getJSON(updateUrl,function(res) {
							window.location.href="${ctxILetter}/enroll/gxfw3Nextform?id=${workDealInfo.id}";
						});
					} else {
						$("#msg").html(data.msg);
					}
				}
			});
		}
	}
	function backToMain(){
		 window.location=external.GetCookie("zhengshufuwu");
	}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/enroll/gxfw3Nextform" id="gxfw3form" name="gxfw3form" method="post" >
</form>
<form action="${ctxILetter}/enroll/gxfw3Nextform" id="gxfw3falseform" name="gxfw3falseform" method="post" >
</form>
<div class="main2">
   <div class="zsfw_gxgx_top">
      <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_gx_gx_T.png" width="165" height="51" /></div>
      <div class="zsfw_gxgx_step3 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.go(-2)"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_gxgx_bd">
    <div id="jindutiao" class="zsfw_gxgx_3info3" style="padding-top:70px;display:none" >
	        <div id="play1" style="width: 328px; height: 19px; left: 0px; top: 0px; color: #c1ff84; z-index: 2;background-color: transparent; background-image: url(${ctxStatic}/images/progressbar_bg.png); background-repeat: no-repeat;"></div>
			<div id="play2" style="margin-top:-19px; width:0px; height: 19px; left: 0px; top: 0px; color: #c1ff84; z-index: 3; background-color: transparent;background-image: url(${ctxStatic}/images/progressbar.png); background-repeat: no-repeat;""></div>
			<div id="play3" style="margin-top:-19px;margin-left:100px;left: 80px; z-index: 5;top: 0px; line-height:19px;">正在更新...请勿拨出USBKEY！</div>
			<div id="play4" style="margin-top:-19px;margin-left:340px;"></div>
	</div>
   <div id="show_key" >
   
   
   <table  width="100%" border="0" cellspacing="0" cellpadding="0">
     		<tr>
         		<td height="10" colspan="3"></td>
        	</tr>
        	<tr>
        		<td width="24%"></td>
        		<td width="2%"></td>
        		<td width="74%" valign="top">
					Key序列号：${keySN } <br />
					  <a id="certA" href="#" data-toggle="tooltip" data-placement="top" title="${certCN }">
   证书CN：</a> ${certCNOmit } <br />
					证书有效期：${notbefore }到  ${notafter }<br />
					审核状态：<span style="font-weight:bold;">${status}</span>
        		</td>
        	</tr>
     	</table>
   
   
   		<%--  <div class="zsfw_gxgx_3info3" id="keySnDiv">
        	Key序列号：${workDealInfo.keySn }
        </div>    <div class="zsfw_gxgx_3info3">
        	证书CN：${certCN }
        </div>    <div class="zsfw_gxgx_3info3">
        	审核状态：<span style="font-weight:bold;">${status}</span>
        </div> --%>
        
        
		</div>
        <div class="btn_nextStep">
			<div class="n-tispsBox"  id = "msg"  style="color: red"></div>
			<div class="btn_gxzs">
				<a id ="close_a" href="javascript:void(0)" onclick="close_show()" ></a>
			</div>
		 </div>
   </div>
   <div class="clear"></div>
</div>
</body>
</html>