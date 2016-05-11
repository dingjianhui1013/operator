<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
function backToMain(){
	 window.location=external.GetCookie("zhengshufuwu");
}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<div class="main2">
   <div class="zsfw_gxgx_top">
      <div class="zsfw_gxgx_top_T fl"><img src="../images/btn_gx_T.png" width="165" height="51" /></div>
      <div class="zsfw_gxgx_step4 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.back()"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_gxgx_bd">
   		<div class="zsfw_gxgx_error">
         更新证书安装到key异常！<a href="#">重新安装</a>
   	    </div>
   </div>
   <div class="clear"></div>
</div>
</body>
</html>
