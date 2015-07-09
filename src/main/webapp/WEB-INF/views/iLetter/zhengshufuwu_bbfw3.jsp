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
<%-- <!-- [if IE 6] -->
<script src="${ctxStatic}/iLetter/js/DD_belatedPNG.js" type="text/javascript"></script>
<script>
DD_belatedPNG.fix('.nav1 a,.nav2 a,.nav3 a,.nav4 a,.nav5 a,.nav6 a,.nav1_cur a,.nav2_cur a,.nav3_cur a,.nav4_cur a,.nav5_cur a,.nav6_cur a,.btn_shezhi a,.btn_zxh a,.btn_close a,.pop_close a,.zsfw_xgpass_form,.zsfw_xgpass_ok,.zsfw_js_step2_c,.zsfw_js_step4_c,.zsfw_gxgx_error,.font_yyz,b,img');
</script>
<!-- [endif]--> --%>
<script type="text/javascript">
	function backToMain() {
		window.location = external.GetCookie("zhengshufuwu");
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
			<div class="zsfw_bbfw_step3 fl"></div>
			<div class="btn_fanhuizsfw fl">
				<a href="javascript:backToMain()"></a>
			</div>
		</div>
		<div class="clear"></div>
		<div class="zsfw_show_bd">
			<table style="width: 100%">
				<tr class="tr_T" class="zsfw_show_bd" style="width: 100%">
					<td width="19%" height="32" align="center">区域</td>
					<td width="24%" height="32" align="center">网点</td>
					<td width="24%" height="32" align="center">网点客服电话</td>
					<td width="33%" height="32" align="center">网点地址</td>
				</tr>
			</table>
		</div>
		<div class="zsfw_show_bd" style="height: 195px; overflow-y: auto;overflow-x: hidden;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="width: 99%">
				<c:forEach items="${offices}" var="office">
					<tr class="tr_list" style="width:100%">
						<td width="19%" height="50" align="center"><span>${office.parent.name}</span></td>
						<td width="24%" height="50" align="center">${office.name }</td>
						<td width="24%" height="50" align="center">${office.phone }</td>
						<td width="33%" height="50" align="center">${office.address }</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="clear"></div>
	</div>

</body>
</html>
