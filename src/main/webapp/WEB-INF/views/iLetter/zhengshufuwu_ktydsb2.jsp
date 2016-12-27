<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>

<script type="text/javascript" src="${ctxStatic}/msg/msg.js"></script>
<%-- <!-- [if IE 6] -->
<script src="${ctxStatic}/iLetter/js/DD_belatedPNG.js" type="text/javascript"></script>
<script>
DD_belatedPNG.fix('.nav1 a,.nav2 a,.nav3 a,.nav4 a,.nav5 a,.nav6 a,.nav1_cur a,.nav2_cur a,.nav3_cur a,.nav4_cur a,.nav5_cur a,.nav6_cur a,.btn_shezhi a,.btn_zxh a,.btn_close a,.pop_close a,.zsfw_xgpass_form,.zsfw_xgpass_ok,.zsfw_js_step2_c,.zsfw_js_step4_c,.zsfw_gxgx_error,.font_yyz,b,img');
</script>
<!-- [endif]--> --%>
<script type="text/javascript">
	$(document).ready(function(){
		if($("#money").html()!=""){
			addMsg();	
		}
	});
	function nextStep(){
		var keySn = external.ukeyserial;
		var certSn=external. certInfo(0,"SerialNumber");
		var id = ${id};
		if(id==-1){
			return false;
		}
	var url = "${ctxILetter}/enroll/ktydsb2Submit?ukey="+keySn+"&certSn="+certSn+"&_="+new Date().getTime();
	 $.getJSON(url,function(data){
		if(data.status=="1"){
			$("#truId").val(data.id);
			$("#count").val(data.count);
			$("#date").val(data.date);
			$("#ktydsb2form").submit();
		}else if(data.status==-1){
			window.location.href="${ctxILetter}/enroll/ktydsb2Nextform?status=-1&id="+data.id;
		}else{
			$("#msgess").html("正在审核，请耐心等待！");
		}
	});
 }
	function backToMain(){
		window.location=external.GetCookie("kexinshebei");
	}
</script>
</head>
<body  oncontextmenu="return false"   onselect="return false"  oncanplay="return false">
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/enroll/ktydsb2Nextform" id="ktydsb2form" name="ktydsb2form" method="post" >
<div class="main2">
  <div class="zsfw_gxgx_top">
    <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_ydsb_T.png" width="154" height="54" /></div>
    <div class="zsfw_ydsb_step2 fl"></div>
    <div class="btn_fanhuizsfw fl btn_fanhuiydsb"><a href="javascript:window.history.back()"></a></div>
  </div>
  <div class="clear"></div>
  <div class="zsfw_gxgx_bd">
  <input type="hidden" name="id" id="truId">
  <input type="hidden" name="count" id="count">
  <input type="hidden" name="date" id="date">
   		<div class="zsfw_js_step2_c hong" id="msgess" style="padding-top:10px">${msg }<span style="color:#0054ff" id="money">${money }</span><br/>
	   	<span id="payMsg" style="text-align: center;padding-top: 10px;font-size: 16px" class="hong"></span></div>
     	<div class="btn_cxsqpj"><a href="javascript:void(0)" onclick="nextStep()"></a></div>
   </div>
   <div id="payMsg" style="text-align: center;padding-top: 10px;font-size: 16px" class="hong"></div>
   
  <div class="clear"></div>
</div>
</form>
</body>
</html>
