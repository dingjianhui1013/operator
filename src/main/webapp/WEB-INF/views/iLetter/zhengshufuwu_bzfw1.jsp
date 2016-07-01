<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<%-- <!-- [if IE 6] -->
<script src="${ctxStatic}/iLetter/js/DD_belatedPNG.js" type="text/javascript"></script>
<script>
DD_belatedPNG.fix('.nav1 a,.nav2 a,.nav3 a,.nav4 a,.nav5 a,.nav6 a,.nav1_cur a,.nav2_cur a,.nav3_cur a,.nav4_cur a,.nav5_cur a,.nav6_cur a,.btn_shezhi a,.btn_zxh a,.btn_close a,.pop_close a,.zsfw_xgpass_form,.zsfw_xgpass_ok,.zsfw_js_step2_c,.zsfw_js_step4_c,.zsfw_gxgx_error,.font_yyz,b,img');
</script>
<!-- [endif]--> --%>
<script type="text/javascript">
	var flag = false;
$(document).ready(function(){
	var keySn = external.ukeyserial;
	
	$("#serNo").html(keySn);
	
	var certSn; 
	if(external. certcount>0)
	{
		certSn=external. certInfo(0,"SerialNumber");
		$("#certsn").html(certSn);
	}
	var url = "${ctxILetter}/enroll/bbfw?keySn="+keySn+"&certSn="+certSn+"&dealInfoType="+4+"&"+new Date().getTime();
	 $.getJSON(url,function(data){
		if(data.status=="1"){
			$("#money").html(data.money);
			$("#appName").html(data.appName);
			$("#certCN").html(data.certCN.length>20?data.certCN.substring(0,20)+"...":data.certCN);
			$("#certA").attr("title",data.certCN);
			$("#certdate").html("从"+data.startDate+"到"+data.endDate);
			flag = true;
		}else{
			//$("#msg").html("当前key未办理业务");
			$("#nextt").removeClass().addClass("gray_nextStep"); 
			$('#aHref').attr('href','javascript:void(0);');
			$("#wzTip").show();
			return false;
		}
	});
});

function backToMain(){
	 window.location=external.GetCookie("zhengshufuwu");
}

function bzfw_from(){
	if(flag){
		var appName = $("#appName").html();
		appName = encodeURI(encodeURI(appName));
		window.location.href="${ctxILetter}/enroll/bzfw2form?appName="+appName;
	}else{
		$("#msg").html("当前key未办理业务");
	}
}
function openSCCA(){	
	window.open('http://www.scca.com.cn/');
}
</script>
</head>
<body  oncontextmenu="return false"   onselect="return false"  oncanplay="return false">
<!--body中加入了禁止复制功能 可根据需求删除-->
<div class="main2">
  <div class="zsfw_gxgx_top">
    <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_gx_T.png" width="165" height="51" /></div>
    <div class="zsfw_bgfw_step1 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.back()"></a></div>
  </div>
  <div class="clear"></div>
  <div style="height: 5px"></div>
  <div class="zsfw_gxgx_bd">
    <div class="blank10"></div>
 <div class="zsfw_gxgx_info1" style="margin-top: -10px;">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="10" colspan="3"></td>
        </tr>
        <tr>
          <td  width="19%" align="right" valign="top">待变更证书：</td>
          <td  width="2%"></td>
          <td  width="79%" valign="top"> 	
          		key序列号：<span id="serNo"> </span><br />
	  <a id="certA" href="#" data-toggle="tooltip" data-placement="top" 
   title="">
   证书CN：</a><span  id="certCN"></span><br />
  证书有效期：<span id="certdate"></span></td>
        </tr>
      </table>
    </div> 
     <div style="height: 5px"></div>
    <div class="blank10" style="margin-top: -10px;"></div>
      <div class="zsfw_gxgx_info1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="10" colspan="3"></td>
        </tr>
        <tr>
          <td width="19%" align="right" valign="top">所属应用：</td>
          <td width="2%"></td>
          <td width="79%" valign="top" style="color:#0089fe">
          <span id="appName"></span>
          </td>
        </tr>
        <tr>
            <td width="19%" align="right" valign="top">待缴金额：</td>
              <td width="2%"></td>
              <td width="79%" valign="top">
                <span style="color:#0089fe" id="money"></span>元
              </td>
        </tr>
      </table>
    </div>
     <div style="height: 5px"></div>
    <div class="btn_sqjd" style="padding-top:7px;margin-top: -5px;">
	    <div id="nextt" class="btn_nextStep">
	    	<a href="javascript:bzfw_from();"  id="aHref"></a>
	    </div>
	 </div>
  </div>
  <div id="wzTip" style="display:none; margin-top: -40px;padding-left: 160px;font-size: 14px;color: black;font-weight: bold;">您的证书暂时无法通过I信办理此项业务，请通过四川CA网站查看业务办理方式<br>
         网址：<a href="javascript:openSCCA()" >www.scca.com.cn</a>
  </div>
  <div class="clear"></div>
  <div class="n-tispsBox"  id = "msg"  style="color: red;margin-top: -50px"></div>
</div>
</body>
</html>
