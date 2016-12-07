<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css" rel="stylesheet"/>
<!-- 
<script type="text/javascript" src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<link href="${ctxStatic}/bootstrap/3.0.3/css/bootstrap.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/3.0.3/js/bootstrap.js" type="text/javascript"></script>
 -->
<script type="text/javascript">
 	var dealInfoId ;
 	var keySn = external.ukeyserial;
 	var certSn = external.certInfo(0,"SerialNumber");
 	//var certSn = "77D38704879DD7D95C4EE9EE7148BFEFA144E787";
 	var flag = false;
	$(function(){
		$("#certSn").attr("value",certSn);
		$("#keySn").attr("value",keySn);
		$.ajax({
			url:"${ctxILetter}/enroll/gxfw1?_"+new Date().getTime(),
			data:$("#updateDealInfo_form").serialize(),
			dataType:"json",
			success:function(d){
				dealInfoId =d.dealInfoId;
				
				if(d.status == 5 || d.status == 12){
					$("#msg").html("证书未审核请耐心等待！");
					window.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+d.id;
				}else if(d.status == 1){
					$("#msg").html("证书下载异常");
					window.location.href="${ctxILetter}/enroll/gxfw2Nextform?id="+d.id;
				}else if(d.status == 2){
					$("#msg").html("退费用户");
					flag = true; 
				}else if(d.status == 11|| d.status == 13){
					$("#msg").html("审核通过");
					<!--indow.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+dealInfoId;-->
					window.location.href="${ctxILetter}/enroll/gxfw2Nextform?id="+d.id;
				}else if(d.status == 4){
					if(d.archiveNo!=1){
						window.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+d.id+"&mmssgg=审核未通过,拒绝原因："+d.remarks;
					}
					flag = true;
				}else if(d.status == 103){
					//$("#msg").html("当前key未办理业务");
					$("#nextt").removeClass().addClass("gray_nextStep"); 
					$('#aHref').attr('href','javascript:void(0);');
					$("#wzTip").show();
				}else if(d.status == 110){
					$("#msg").html("当前业务正在平台办理业务！");
					$("#nextt").removeClass().addClass("gray_nextStep"); 
					$('#aHref').attr('href','javascript:void(0);');
				}else if(d.status == 104){
					flag = true;
				}else if(d.status == 0){
					flag = true;
				}else if(d.status == 7){
					flag = true;
				}else if(d.status == 105){
					$("#msg").html("此证书未到更新时间!");
					$("#nextt").removeClass().addClass("gray_nextStep"); 
					$('#aHref').attr('href','javascript:void(0);');
				} 
				//如果后台办理更新，待制证状态可在i信端下载证书
				if (d.updateStatus == 106){
					window.location.href="${ctxILetter}/enroll/gxfw2Nextform?id="+d.id;
				}
				$("#dealInfoId").attr("value",d.dealInfoId);
				$("#ceSn").html(keySn);
				/* $("#ceSn").html(d.certSn); */
				$("#certCN").html(d.certCN.length>20?d.certCN.substring(0,20)+"...":d.certCN);
				$("#certA").attr("title",d.certCN);
				$("#certTime").html(d.certTime);
			}
		});
		
	})
	

	function nextStep(){
		$('#aHref').attr('href','javascript:void(0);');
		if(!flag){
			$("#msg").html("UKEY状态错误，不允许申请");
			$("#nextt").removeClass().addClass("gray_nextStep"); 
			$('#aHref').attr('href','javascript:nextStep()');
		}else{
			$.ajax({
				url:"${ctxILetter}/enroll/gxfw1Submit?dealInfoId="+dealInfoId+"&_"+Math.random() * 10,
				data:$("#updateDealInfo_form").serialize(),
				dataType:"json",
				success:function(data){
					if(data.status=="1"){
						window.location.href="${ctxILetter}/enroll/gxfw1Nextform?dealInfoId="+data.id;
					}else{
						$("#msg").html("提交失败");
						$("#nextt").removeClass().addClass("gray_nextStep"); 
						$('#aHref').attr('href','javascript:nextStep()');
					}
				}
			});
		}
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
<div class="main2">
  <div class="zsfw_gxgx_top">
      <div class="zsfw_gxgx_top_T fl"><img src="${ctxStatic}/iLetter/images/btn_gx_gx_T.png" width="165" height="51" /></div>
      <div class="zsfw_gxgx_step1 fl"></div>
    <div class="btn_fanhuizsfw fl"><a href="javascript:window.history.back()"></a></div>
   </div>
  <div class="clear"></div>
  <div class="zsfw_gxgx_gx_bd">
    <div class="blank10"></div>
 <div class="zsfw_gxgx_gx_info1">
  <div class="zsfw_gxgx_info1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="10" colspan="3"></td>
        </tr>
        <tr>
          <td width="19%" align="right" valign="top">待更新证书：</td>
          <td width="2%"></td>
          <td width="79%" valign="top">Key序列号：<span id="ceSn"></span><br />
           <a id="certA" href="#" data-toggle="tooltip" data-placement="top" 
   title="">
            证书CN：</a><span id="certCN"></span><br />
            证书有效期：<span id="certTime"></span></td>
        </tr>
      </table>
      </div>
    </div> 
    <div class="blank10"></div>
    <div class="btn_sqgx" style="padding-top:7px">
	    <div class="btn_nextStep" id="nextt">
			<a href="javascript:nextStep()" id="aHref"></a>
		</div>
	 </div>
  </div>
  <div class="clear"></div>
  <div class="n-tispsBox"  id = "msg"  style="color: red;margin-top: -37px"></div>
  
  
  <br><br>
  <div id="wzTip" style="display:none; red;margin-top: -37px;padding-left: 160px;font-size: 14px;color: black;font-weight: bold;">您的证书暂时无法通过I信办理此项业务，请通过四川CA网站查看业务办理方式<br>
         网址：<a href="javascript:openSCCA()" >www.scca.com.cn</a>
  </div>
  
  
</div>

       	<form  id = "updateDealInfo_form" method="post"  action="">
		 	<input  type ="hidden" name = "keySn" id = "keySn" />
			<input type = "hidden" name = "certSn" id = "certSn"/>
			<input type = "hidden" name = "year"  value ="1"/>
		</form>
</body>
</html>
