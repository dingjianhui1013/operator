<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>天威诚信i信客户端管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/iLetter/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/iLetter/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
		function backToMain(){
		window.location=external.GetCookie("zhengshufuwu"); 
	}
</script>
</head>
<body oncontextmenu="return false" onselect="return false"
	oncanplay="return false">
	<!--body中加入了禁止复制功能 可根据需求删除-->
	<div class="main2">
		<div class="zsfw_gxgx_top">
			<div class="zsfw_gxgx_top_T fl">
				<img src="${ctxStatic}/iLetter/images/btn_bb_T.png" width="165"
					height="51" />
			</div>
			<div class="btn_fanhuizsfw fl">
				<a href="javascript:backToMain()"></a>
			</div>
		</div>
		<div class="clear"></div>
		<div class="zsfw_gxgx_bd" style="padding-top: 20px">
			<div class="zsfw_gxgx_info1HW">
				
     	        下载、填写<a href="${ctxILetter}/enroll/downloadDoc?fileName=apply.docx" target="_blank" class="ablue">《数字证书业务申请表》</a>，并按要求提交资料及费用。
        <br/>
        	具体流程请参照公司网站，<a href="http://www.scca.com.cn/" target="_blank" class="ablue">点击进入</a>
         <br/>
        <div class="zsfw_js_step2_c hong">遗失补办费用：50.0元，&nbsp;&nbsp;&nbsp;&nbsp;损坏更换费用：50.0元
     	</div>
     	<div class="btn_sqdx">
	 </div>
				
			</div>
			<span id="certsn" style="display: none"></span>
			<div class="btn_sqdx" style="padding-top: 0px">
				<div id="nextt" class="btn_nextStep" style="text-align: center">
					<a href="javascript:bbfw_from()" id="aHref"></a>
				</div>
			</div>
		</div>
		<div class="clear"></div>
		<div class="n-tispsBox" id="msg" style="color: red; margin-top: -70px">${msg }</div>
	</div>
</body>
</html>
