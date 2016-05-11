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
      <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_gx_gx_T.png" width="165" height="51" /></div>
      <div class="zsfw_gxgx_step4 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.back()"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_gxgx_bd">
   		<div class="zsfw_js_step4_c hong">
         证书更新成功！
   	    </div>
        <div class="zsfw_gxgx_okinfo">
        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="31%"></td>
                <td width="9%" valign="top">更新后证书：</td>
                <td width="60%" valign="top">证书序列号：${sn }<br />
                经办人邮箱：${email }<br /> 证书有效期：从${notbefore} 到 ${notafter }</td>
              </tr>
            </table>
        </div>
   </div>
   <div class="clear"></div>
</div>
</body>
</html>
