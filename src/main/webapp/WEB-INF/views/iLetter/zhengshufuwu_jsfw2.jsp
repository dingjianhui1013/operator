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
		var keySn = external.ukeyserial;
		var certSn=external. certInfo(0,"SerialNumber");
		var validateStatus = "${ctxILetter}/unlock/query?keySn="+keySn+"&certSn="+certSn+"&_="+new Date().getTime();
		$.getJSON(validateStatus,function(data){
			if(data.unlockstatus=="FORBID"&&data.state!=1){
				window.location.href="${ctxILetter}/enroll/jsfw1Nextform?status="+data.unlockstatus+"&id="+data.id;
			}else if(data.status=='OK'&&(data.unlockstatus=='APPROVE'||data.unlockstatus!='ENROLL')&&data.respCode!=""){
				window.location.href="${ctxILetter}/enroll/jsfw2Nextform?id="+data.id;
			}else{
				$("#mmssgg").html("解锁申请正在审批中，请耐心等待！");
			}
		});			
 }
	
	
	function shenqing(){
		$("#jsfw1form").submit();
	}
	
	function backToMain(){
		 window.location=external.GetCookie("zhengshufuwu");
	}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/unlock/enroll" id="jsfw1form" name="jsfw1form" id="jsfw1form" method="post" >
<input type="hidden" name = "keySn" id="keySn" value="${keySn }">
<input name="reqCode" type="hidden" id="reqCode" value="${reqCode }">
<input type="hidden" name="certCn" id="certCn" value="${certCn }">
<input type="hidden" name="lastId" id="lastId" value="${lastId }">
</form>

<form action="${ctxILetter}/enroll/jsfw2Nextform" id="jsfw2form" name="jsfw2form" method="post" >
<div class="main2">
   <div class="zsfw_js_top">
      <div class="zsfw_js_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_js_T.png" width="157" height="46" /></div>
      <div class="zsfw_js_step2 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:backToMain()"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_js_bd">
     <div class="zsfw_js_step2_c hong" id="mmssgg">
     ${status}<br /><c:if test="${msg!=null }">${msg }<br/></c:if> 如有疑问可直接联系客服！
     </div>
     <div class="zsfw_gxgx_info1" style="margin:-43px auto 0;">
     	<div style="padding:8px 0 0 20px">
			请下载<a href="${ctxILetter}/enroll/downloadDoc?fileName=apply.docx" target="_blank" class="ablue">《数字证书业务申请表》</a>填写，并加盖公章。<br />传真至四川省数字证书认证管理中心（028-85336171-808）。
     	</div>
     </div>
     
     
     <c:if test="${msg==null }">
	     <div class="btn_cxsqpj" style="margin-top:10px;"><a href="javascript:void(0)" onclick="nextStep()"></a></div>
     </c:if>
     <c:if test="${msg!=null }">
     <div class="btn_sqjs">
	    <div style="padding-top:10px">
	    	<a href="javascript:shenqing();" id="aHref"></a>
	    </div>
	 </div>
     </c:if>
     
   </div>
   <div class="clear"></div>
   <div class="zsfw_js_bot"><span class="lv">为确保安全，解锁过程需在同一台计算机上操作</span></div>
</div>
</form>
</body>
</html>
