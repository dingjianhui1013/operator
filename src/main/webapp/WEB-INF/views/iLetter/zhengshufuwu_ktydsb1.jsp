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
var key = true;
$(document).ready(function(){
	var keySn = external.ukeyserial;
	$("#keySnHid").val(keySn);
	if(external. certcount>0)
	{
		var certSn=external. certInfo(0,"SerialNumber");
		$("#certsn").html(certSn);
		$("#certsnHid").val(certSn);
		
	}
	var count = $("#count").val();
	var url = "${ctxILetter}/enroll/ktydsb1Submit?keySn="+keySn+"&certSn="+certSn+"&count="+count+"&_="+new Date().getTime();
	 $.getJSON(url,function(data){
		if(data.status==0){
			window.location.href="${ctxILetter}/enroll/ktydsbShow?money="+data.money+"&id="+data.id;
		}else if(data.status==-1){
			window.location.href="${ctxILetter}/enroll/ktydsb2Nextform?status=-1&id="+data.id;
		}else if(data.status==3){
			key =false;
			$("#email").html(data.email);
			$("#certdate").html("从"+data.startDate+"到"+data.endDate);	
			$("#msg").html("当前key不允许办理业务！");
		}else if(data.status==1){
			window.location.href="${ctxILetter}/enroll/ktydsb2Nextform?count="+data.count1+"&date="+data.date+"&id="+data.id;
		}else{
			if(data.startDate!=undefined){
				$("#email").html(data.email);
				$("#certdate").html("从"+data.startDate+"到"+data.endDate);	
				$("#inex").show();
				$("#nextt").hide();
			}else{
				$("#msg").html("当前key未办理业务");
				$("#nextt").removeClass().addClass("gray_nextStep"); 
				$('#aHref').attr('href','javascript:void(0);');
				return false;
			}
		}
	});
});
function nextStep(){
	if(key){
		var url = "${ctxILetter}/enroll/ktydsb1Nextform?count="+$("#count").val()+"&certsn="+$("#certsnHid").val()+"&keySn="+$("#keySnHid").val()+"&_="+new Date().getTime();
		$.getJSON(url,function(data){
			if(data.status==1){
				window.location.href="${ctxILetter}/enroll/ktydsbShow?money="+data.money+"&msg="+data.msg+"&id="+data.id;
			}else if(data.status==-1){
				var msg = encodeURI(encodeURI(data.msg));
				window.location.href="${ctxILetter}/enroll/ktydsbShow?msg="+msg+"&id="+data.id;
			}else if(data.status == 2){
				var msg = encodeURI(encodeURI(data.msg));
				window.location.href="${ctxILetter}/enroll/ktydsb2Nextform?count="+data.count+"&date="+data.date+"&id="+data.id;
			}else if(data.status == 4){
				$("#msg").html(data.msg);
			}else{
				$("#msg").html(data.msg);
			}
		});
	}else{
		window.location.reload();
	}
}	

function backToMain(){
	window.location=external.GetCookie("kexinshebei");
}
</script>
</head>
<body  oncontextmenu="return false"   onselect="return false"  oncanplay="return false">
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/enroll/ktydsb1Nextform" id="ktydsb1form" name="ktydsb1form">
<div class="main2">
  <div class="zsfw_gxgx_top">
    <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_ydsb_T.png" width="154" height="54" /></div>
    <div class="zsfw_ydsb_step1 fl"></div>
    <div class="btn_fanhuizsfw fl btn_fanhuiydsb"><a href="javascript:backToMain()"></a></div>
  </div>
  <div class="clear"></div>
  <div class="zsfw_gxgx_bd">
    <div class="blank10"></div>
 <div class="zsfw_gxgx_info1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="10" colspan="3"></td>
        </tr>
        <tr>
          <td width="16%" align="right" valign="top" rowspan="3">主身份证书：</td>
          <td width="2%" rowspan="3"></td>
          <td width="16%">证书序列号：</td>
          <td width="66%" id="certsn"></td>
        </tr>
        <tr>
        	<td width="16%">经办人邮箱：</td>
	        <td width="66%" id="email"></td>
        </tr>
        <tr>
        	<td width="16%">证书有效期：</td>
 	        <td width="66%" id="certdate"></td>
        </tr>
      </table>
    </div> 
    <div class="blank10"></div>
      <div class="zsfw_gxgx_info1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="25" colspan="3"></td>
        </tr>
        <tr>
          <td width="25%" align="right" valign="top">申请移动设备数量：</td>
          <td width="2%"></td>
          <td width="73%" valign="top">
          	<input type="text" id="count"  name="count" style="width:80px; margin-right:10px;"/>个
          	<input type="hidden" name="certsn" value="" id="certsnHid">
          	<input type="hidden" name="keySn" value="" id="keySnHid">
          </td>
        </tr>
      </table>
    </div>
    <div class="btn_nextStep">
	    <div style="margin-top: 10px" id="nextt">
	    </div>
    	<div id="inex" style="display:none;padding-top: 10px">
    		<a href="javascript:nextStep();" id="aHref"></a>
    	</div>
	 </div>
  </div>
  <div class="clear"></div>
	    <div class="n-tispsBox"  id = "msg"  style="color: red;margin-top: -42px"></div>
</div>
</form>
</body>
</html>
