<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctxStatic}/msg/msg.js"></script>
<!-- 
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
-->
<script type="text/javascript">
	var dealInfoId ;
	var keySn = external.ukeyserial;
	var certSn = external.certInfo(0,"SerialNumber");
	var flag = false;
	$(document).ready(function(){
		addMsg();
	});
	function nextStep(){
		$("#certSn").attr("value",certSn);
		$("#keySn").attr("value",keySn);
		$.ajax({
			url:"${ctxILetter}/enroll/gxfw1?_"+Math.random() * 10,
			data:$("#updateDealInfo_form").serialize(),
			dataType:"json",
			success:function(d){
				dealInfoId =d.dealInfoId;
				if(d.status == 5 || d.status == 12){
					window.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+d.id+"&mmssgg=SQ4X85Q63F25G8R9E63R";
				}else if(d.status == -1){
					$("#msg").html("当前业务数据状态不允许申请更新，请查证");
				}else if(d.status == 1){
					//$("#msg").html("证书下载异常");
					window.location.href="${ctxILetter}/enroll/gxfw2Nextform?id="+d.id;
				}else if(d.status == 2){
					$("#msg").html("退费用户");
					flag = true;
				}else if(d.status == 11|| d.status == 13){
					//$("#msg").html("审核通过");
					<!--indow.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+dealInfoId;-->
					window.location.href="${ctxILetter}/enroll/gxfw2Nextform?id="+d.id;
				}else if(d.status == 4){
					window.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+d.id+"&mmssgg=审核未通过,拒绝原因："+d.remarks;
					flag = true;
				}else if(d.status == 103){
					$("#msg").html("当前key未办理业务");
				}else if(d.status == 104){
					flag = true;
				}
				$("#dealInfoId").attr("value",d.dealInfoId);
				$("#ceSn").html(d.certSn);
				$("#userEmail").html(d.userEmail);
				$("#certTime").html(d.certTime);
			}
		});
	}
	function backToMain(){
		 window.location=external.GetCookie("zhengshufuwu");
	}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false"  oncanplay="return false" >
<!--body中加入了禁止复制功能 可根据需求删除-->
<form action="${ctxILetter}/enroll/gxfw2Nextform" id="gxfw2form" name="gxfw2form" method="post" >
<div class="main2">
   <div class="zsfw_gxgx_top">
      <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_gx_gx_T.png" width="165" height="51" /></div>
      <div class="zsfw_gxgx_step2 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.back()"></a></div>
   </div>
   <div class="clear"></div>
   <div class="zsfw_gxgx_bd">
   		<div class="zsfw_js_step2_c hong" id="mssgg">
   			<c:if test="${mmssgg!=null }">
   				${mmssgg }
   			</c:if>
   			<c:if test="${mmssgg==null }">
	         	请下载<a href="${ctxILetter}/enroll/downloadDoc?fileName=apply.docx" target="_blank" class="ablue">《数字证书业务申请表》</a>填写并查看注意事项。
			   	<br/><span id="payMsg" style="text-align: center;padding-top: 10px;font-size: 16px" class="hong"></span>
   			</c:if>
     	</div>
	    <div class="btn_nextStep">
	    	<a href="javascript:void(0)" onclick="nextStep()"></a>
	 	</div>
   </div> 
   <div class="clear"></div>
   <div class="n-tispsBox"  id = "msg"  style="color: red;margin-top: -70px"></div>
</div>
</form>
<div style="display: none;">
 	<form  id = "updateDealInfo_form" method="post"  action="">
		<input  type ="hidden" name = "keySn" id = "keySn" />
		<input type = "hidden" name = "certSn" id = "certSn"/>
	    <input name="year"  type="radio" value="1" /> 1年<br />
	    <input name="year"  type="radio" value="2" /> 2年<br />
	    <input name="year"  type="radio" value="3" /> 4年
	</form>
</div>
</body>
</html>
