<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
 
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<link href="${ctxStatic}/bootstrap/3.0.3/css/bootstrap.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/3.0.3/js/bootstrap.js" type="text/javascript"></script>

<script type="text/javascript">
var flag = false;
var keySn = external.ukeyserial;
var certSn=external. certInfo(0,"SerialNumber");
$(document).ready(function(){
	$("#keySn").val(keySn);
	$("#certCn").val(certSn);
	var validateStatus = "${ctxILetter}/unlock/query?keySn="+keySn+"&certSn="+certSn+"&_="+new Date().getTime();
	$.getJSON(validateStatus,function(data){
		if(data.status=='OK'&&data.unlockstatus!='UNLOCK'&&data.respCode!=""&&data.unlockstatus!='FORBID'){
			if(data.shix=="shix"){
				$("#msg").html("解锁码失效！");
			}else if(data.status=='OK'&&(data.unlockstatus=='DOWNLOAD'||data.unlockstatus=='APPROVE')&&data.respCode!=""){
				window.location.href="${ctxILetter}/enroll/jsfw2Nextform?id="+data.id;
			}else {
				window.location.href="${ctxILetter}/enroll/jsfw1Nextform?status="+data.unlockstatus;
			}
		}
		if(data.unlockstatus=='FORBID'&&data.state!=1){
			window.location.href="${ctxILetter}/enroll/jsfw1Nextform?status="+data.unlockstatus+"&id="+data.id;
		}
		if(data.status==1){
			//$("#msg").html("当前key未办理业务");
			$("#nextt").removeClass().addClass("gray_nextStep"); 
			$('#aHref').attr('href','javascript:void(0);');
			$("#wzTip").show();
			return false;
		}
		$("#certCN").html(data.certCN.length>20?data.certCN.substring(0,20)+"...":data.certCN);
		$("#certA").attr("title",data.certCN);
		$("#start").html(data.start);
		$("#end").html(data.end);
	});
	var certsn;
	if(external.certcount>0){
		certsn=external.certInfo(0,"SerialNumber");
	}
	$("#certCn").val(certsn);
	$("#serNo").html(keySn);
});


	function nextStep(){
		var validateStatus = "${ctxILetter}/unlock/query?keySn="+keySn+"&certSn="+certSn+"&_="+new Date().getTime();
		$.getJSON(validateStatus,function(data){
			if(data.status!='OK'){//该key无待处理的解锁申请
				try{
					var reqCode = external.unlockreqcode();
				}catch(e){
				}
				$("#reqCode").val(reqCode);
				$("#jsfw1form").submit();
			}
		});
 }
	function backToMain(){
		 window.location=external.GetCookie("zhengshufuwu");
	}
function openSCCA(){	
		window.open('http://www.scca.com.cn/');
	}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/unlock/enroll" id="jsfw1form" name="jsfw1form" id="jsfw1form" method="post" >
<input type="hidden" name = "keySn" id="keySn">
<input name="reqCode" type="hidden" id="reqCode">
<input type="hidden" name="certCn" id="certCn">
<div class="main2">
   <div class="zsfw_js_top">
      <div class="zsfw_js_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_js_T.png" width="157" height="46" /></div>
      <div class="zsfw_js_step1 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.back()"></a></div>
   </div>
   <div class="clear"></div>
   <div style="height: 5px"></div>
   <div class="zsfw_js_bd">
     <div class="zsfw_gxgx_info1">
     	<table   width="100%" border="0" cellspacing="0" cellpadding="0">
     		<tr>
         		<td height="10" colspan="3"></td>
        	</tr>
        	<tr>
        		<td width="19%" align="right" valign="top">申请解锁：</td>
        		<td width="2%"></td>
        		<td width="79%" valign="top">
					Key序列号：<span id="serNo"></span> <br />
					<a id="certA" href="#" data-toggle="tooltip" data-placement="top" 
   title="">
   证书CN：</a> <span id="certCN"></span><br />
					证书有效期：从 <span id="start"></span>到 <span id="end"></span><br />
        		</td>
        	</tr>
     	</table>
	</div>
	<div style="height: 5px"></div>
	
      <div class="zsfw_gxgx_info1" style="margin-top:1px">
     	<div style="padding:8px 0 0 20px" class=".customer_font_size">
			请下载<a href="${ctxILetter}/enroll/downloadDoc?fileName=apply.docx" target="_blank" class="ablue">《数字证书业务申请表》</a>填写，并加盖公章。<br />传真至四川省数字证书认证管理中心（028-85336171-808）。
     	</div>
     </div>
     <div style="height: 8px"></div>
     <div class="btn_sqjs" >
	    <div style="padding-top:1px">
	    	<a href="javascript:nextStep();" id="aHref"></a>
	    </div>
	 </div>
   </div>
   <div class="n-tispsBox"  id = "msg"  style="color: red;float: right;margin: -55px"></div>
   <div class="clear"></div>
   
   <div style="height: 5px"></div>
  <div id="wzTip" style="display:none;margin-top: -30px;padding-left: 160px;font-size: 14px;color: black;font-weight: bold;">您的证书暂时无法通过I信办理此项业务，请通过四川CA网站查看业务办理方式<br>
         网址：<a href="javascript:openSCCA()" >www.scca.com.cn</a>
  </div>
   <div class="zsfw_js_bot" style="margin-top: -20px;"><span class="lv">为确保安全，解锁过程需在同一台计算机上操作</span></div>
</div>
</form>
</body>
</html>
