<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>天威诚信i信客户端管理</title>
<meta name="keywords" content="天威诚信,i信,天威诚信客户端" />
<meta name="description" content="天威诚信i信客户端管理系统" />
<meta name="author" content="iTrusChina" />
<meta name="copyright" content="iTrusChina" />
<link href='../userEnroll/css/main.css' rel='stylesheet' type='text/css'>

<script type="text/javascript" src="../userEnroll/js/jquery-1.8.3.min.js"></script>

<script type="text/javascript" src="#springUrl('/')resources/js/pta_topca.js"></script>
<script type="text/javascript" src="#springUrl('/')resources/js/xenroll.js"></script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncopy="return false"> <!--body中加入了禁止复制功能 可根据需求删除-->
<script language="javascript">
var keySn = external.ukeyserial;
var vProgressTimeTAG ; 
var timeout = 1;
var reqBuf;
var keystutus = 1;
var csr;
var id;
function setCount() {
	timeout = timeout + 1;
	
	if (timeout < 0) {
		timeout = 0;
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
	var widthSize = document.getElementById('play1').clientWidth;	//获得控件width的值
	var size = (widthSize / 100) * timeout;
	document.getElementById('play2').style.width = size;
	document.getElementById('play4').innerHTML = timeout;
	clearTimeout(vProgressTimeTAG);
   	vProgressTimeTAG = setTimeout("setCount()",100);
}
window.onload=setCount;
</script>
<script type="text/javascript" src="../userEnroll/js/DD_belatedPNG.js"></script>
<div class="main2">
   <div class="zsfw_xgpass_top" style="margin-left:30px;">
      <div class="btn_fanhuizsfw fr mt10"><a href="zhengshufuwu.html"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_gx_bd">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
        <td height="50">&nbsp;</td>
        <td height="50">&nbsp;</td>
        <td height="50" valign="top">&nbsp;</td>
        <td height="50">&nbsp;</td>
      </tr>

      <tr>
        <td width="19%" height="100">&nbsp;</td>
        <td width="15%" height="100"></td>
        <td width="49%" height="100" valign="top">
        <div >
	        <div id="play1" style="position: absolute; width: 328px; height: 19px; left: 0px; top: 0px; color: #c1ff84; z-index: 2;background-color: #eee;  background-repeat: no-repeat;"></div>
			<div id="play2" style="position: absolute; width:0px; height: 19px; left: 0px; top: 0px; color: #c1ff84; z-index: 3; background-color: #bbb; background-repeat: no-repeat;"></div>
			<div id="play3" style="position: absolute; left: 80px; z-index: 5;top: 0px; line-height:19px;">正在更新...请勿拨出天威盾！</div>
			<div id="play4" style="position: absolute;left: 335px; top: 0px;" class="none"></div>
		</div>
        </td>
        <td width="17%" height="100"><!--取消按钮-->
        <div class="btn_xiufu fl mr10 none"><a href="#"></a></div><!--一键修复按钮--><!--返回按钮-->
        </td>
      </tr>
      </table>
   </div>
</div>
	<form id="updateCert_form" method="post" action="">
		<input type = "hidden" name="code" id="pin" value ="${pin}"/>
        <input type = "hidden" id = "ukeySn" name = "productSn" />
        <input type = "hidden" id = "reqBuf" name = "reqBuf" />
     </form>
</body>
</html>

