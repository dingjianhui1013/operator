<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	function nextStep(){
		$("#jsfw3form").submit();
	}
 function backToMain(){
	 window.location=external.GetCookie("zhengshufuwu");
}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/enroll/jsfw3Nextform" id="jsfw3form" name="jsfw3form" method="post" >
<div class="main2">
   <div class="zsfw_js_top">
      <div class="zsfw_js_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_js_T.png" width="157" height="46" /></div>
      <div class="zsfw_js_step4 fl"></div>
      <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.go(-4)"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_js_bd">
     <div class="zsfw_js_step4_c hong">
        ${status}
     </div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_js_bot"><span class="lv">为确保安全，解锁过程需在同一台计算机上操作</span></div>
</div>
</form>
</body>
</html>
