<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var code = $("#respCode").val();
	if(code.length==0){
		window.location.href="${ctxILetter}/enroll/jsfw1form";
	}
	var keySn = external.ukeyserial;
	$("#keySn").html(keySn);
});

function nextStep(){
	pass1();
	pass2();
	//alert($("#respCode").val());
	//alert($("#pass1").val());
	if(pass1()&&pass2()){
		var result = external.unlockresetpin($("#respCode").val(),$("#pass1").val());
		$.getJSON("${ctxILetter}/unlock/jump?_"+new Date().getTime(),function(data){
			if(result==100){
				$("#jsfw3form").submit();
			}else{
				var url = "${ctxILetter}/enroll/jsfw3Nextform?msg="+result+"&keySn="+$("#keySn").html()+"&id="+$("#unlockId").val()+"&_="+new Date().getTime();
				window.location.href = url;
			}
		});
	}
}
function pass1(){
	var len = $("#pass1").val().length;
	if(len<6||len>8){
		$("#tip1").show();
		return false;
	}else{
		$("#tip1").hide();
		return true;
	}
}
function pass2(){
	if($("#pass1").val()!=$("#pass2").val()){
		$("#tip2").show();
		return false;
	}else{
		$("#tip2").hide();
		return true;
	}
}
function backToMain(){
	 window.location=external.GetCookie("zhengshufuwu");
}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/unlock/unlock" id="jsfw3form" name="jsfw3form" method="post" >
<input type="hidden" name="respCode" id="respCode" value="${respCode}">
<input type="hidden" name="id" id="unlockId" value="${id}">
<div class="main2">
   <div class="zsfw_js_top">
      <div class="zsfw_js_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_js_T.png" width="157" height="46" /></div>
      <div class="zsfw_js_step3 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:backToMain()"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_js_bd" style="height:auto;">
     <div class="zsfw_js_step3_c2">
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="5%" height="10"></td>
            <td width="54%" height="10"></td>
            <td width="41%" height="10"></td>
          </tr>
          <tr>
            <td height="35">&nbsp;</td>
            <td height="35" colspan="2">管理员已经审核通过了您的解锁申请。请在<strong class="lv" style="font-size:14px">3天内</strong>完成解锁！</td>
         
          </tr>

          <tr>
            <td height="35">&nbsp;</td>
            <td height="35">解锁key：<span class="lan" id="keySn"></span></td>
            <td height="35"><span class="lv">请插入您申请解锁时的key！</span></td>
          </tr>
          
         </table>
     </div>
     <div class="zsfw_js_step3_c2">
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="5%" height="7"></td>
        <td width="16%" height="7"></td>
        <td width="39%" height="7"></td>
        <td width="40%" height="7"></td>
      </tr>
      <tr>
        <td height="35">&nbsp;</td>
        <td height="35">设置新口令：</td>
        <td height="35"><input type="password" name="textfield" id="pass1" class="inputtxt2" /></td>
        <td height="35" style="display: none" id="tip1">请设置为6-8位的数字或字母</td>
      </tr>
      <tr>
        <td height="35">&nbsp;</td>
        <td height="35">确认新口令：</td>
        <td height="35"><input type="password" name="textfield" id="pass2" class="inputtxt2" /></td>
        <td height="35" style="display: none" id="tip2">两次口令输入不一致</td>
      </tr>
    </table>

     </div>
     <div class="btn_ljjs"><a href="javascript:void(0)" onclick="nextStep()"></a></div>
   </div>
   <div class="clear"></div>
</div>
</form>
</body>
</html>
