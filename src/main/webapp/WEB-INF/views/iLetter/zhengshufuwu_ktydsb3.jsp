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
	window.location=external.GetCookie("kexinshebei");
}
</script>
</head>
<body  oncontextmenu="return false"   onselect="return false"  oncanplay="return false">
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/enroll/ktydsb1Nextform" id="ktydsb1form" name="ktydsb1form" method="post" >
<div class="main2">
  <div class="zsfw_gxgx_top">
    <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_ydsb_T.png" width="154" height="54" /></div>
    <div class="zsfw_ydsb_step3 fl"></div>
    <div class="btn_fanhuizsfw fl btn_fanhuiydsb"><a href="javascript:window.history.back()"></a></div>
  </div>
  <div class="clear"></div>
  <div class="zsfw_gxgx_bd">
   			<c:if test="${status==1 }">
   				<div style="text-align: center;padding-top: 30px;font-size: 16px" class="hong	">
   					申请开通移动设备数量被拒绝<br/>
   					${suggest }
   				</div>
   			</c:if>
   			<c:if test="${status==0 }">
		   		<div class="zsfw_js_step2_c hong">
		   			您已成功申请开通移动设备<span style="color:#0084ff">${count}</span>个。<br />有效期至：<span style="color:#0084ff">${date }</span>
		   		</div>
   			</c:if>
   </div>
  <div class="clear"></div>
</div>
</form>
</body>
</html>
