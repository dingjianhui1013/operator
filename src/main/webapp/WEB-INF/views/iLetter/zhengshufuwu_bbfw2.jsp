<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<%-- <!-- [if IE 6] -->
<script src="${ctxStatic}/iLetter/js/DD_belatedPNG.js" type="text/javascript"></script>
<script>
DD_belatedPNG.fix('.nav1 a,.nav2 a,.nav3 a,.nav4 a,.nav5 a,.nav6 a,.nav1_cur a,.nav2_cur a,.nav3_cur a,.nav4_cur a,.nav5_cur a,.nav6_cur a,.btn_shezhi a,.btn_zxh a,.btn_close a,.pop_close a,.zsfw_xgpass_form,.zsfw_xgpass_ok,.zsfw_js_step2_c,.zsfw_js_step4_c,.zsfw_gxgx_error,.font_yyz,b,img');
</script>
<!-- [endif]--> --%>
<script type="text/javascript">
	function backToMain(){
		 window.location=external.GetCookie("zhengshufuwu");
	}
</script>
</head>
<body  oncontextmenu="return false"   onselect="return false"  oncanplay="return false">
<!--body中加入了禁止复制功能 可根据需求删除-->
<div class="main2">
   <div class="zsfw_gxgx_top">
      <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_bb_T.png" width="165" height="51" /></div>
      <div class="zsfw_bbfw_step2 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:backToMain()"></a></div>
   </div>
   <div class="clear"></div>
   <div id = "msg" style="text-align: center;color: red"></div>
   <div class="zsfw_gxgx_bd">
   		<div class="zsfw_js_step2_c hong">遗失补办费用：${bb0 }元，&nbsp;&nbsp;&nbsp;&nbsp;损坏更换费用：${bb1 }元<br/>
   		<br/>请下载<a href="${ctxILetter}/enroll/downloadDoc?fileName=apply.docx" target="_blank" class="ablue">《数字证书业务申请表》</a>填写,根据说明提交资料并完成缴费。<br />您办理<span style="color:#0054fd; font-weight:bold;">${appName }</span>的应用可以通过查看网点就近选择任一网点进行办理
     	<br/>
     	</div>
     	<div class="btn_sqdx">
	    <div class="btn_nextStep" id="nextt">
	    	<a href="${ctxILetter}/enroll/bbfw2Nextform?configAppid=${configAppId}" id="aHref"></a>
	    </div>
	 </div>
   </div>
   <div class="clear"></div>
   <div class="n-tispsBox"  id = "msg"  style="color: red"></div>
</div>
</body>
</html>
