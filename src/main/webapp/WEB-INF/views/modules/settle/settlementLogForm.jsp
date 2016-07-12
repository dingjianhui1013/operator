<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>年费结算保存管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var windowH=$(window).height();
			var headerH=$('#header').height();
			var navH=$('.nav').height();
			var windowHBox=windowH-headerH-navH-32;
			$('.windowHeight').height(windowHBox);
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs" id="ulWidth">
		<li><a href="${ctx}/settle/settlementLog/">年费结算保存列表</a></li>
		<li class="active"><a href="${ctx}/settle/settlementLog/see?id=${settlementLog.id}">年费结算保存详细列表</a></li>
	</ul><br/>
	<tags:message content="${message}" />	
	<div class="windowHeight" style=" overflow-x:scroll;">
	<table id="contentTable"
					class="table table-striped table-bordered table-condensed" style="width: 60%" >
					<tr>
						<th colspan="11" >统计周期：<fmt:formatDate pattern="yyyy-MM-dd" value="${settlementLog.startTime}"/>&nbsp;-&nbsp;<fmt:formatDate pattern="yyyy-MM-dd" value="${settlementLog.endTime}"/></th>
					</tr>
					<tr>
						<th colspan="11">本期结算证书年限：（本次结算年数总数）代理商：${settlementLog.comagentName} 应用名称：${settlementLog.appName}</th>
					</tr>
					<tr>
						<th>业务类型</th>
						<th colspan="5">新增</th>
						<th colspan="5">更新</th>
					</tr>
					<tr>
						<th>结算年限</th>
						<th>1年期</th>
						<th>2年期</th>
						<th>3年期</th>
						<th>4年期</th>
						<th>5年期</th>
						<th>1年期</th>
						<th>2年期</th>
						<th>3年期</th>
						<th>4年期</th>
						<th>5年期</th>
					</tr>
				<tbody>
			<c:forEach items="${collect}" var="collect">
				<tr>
					<td>${collect.productName }</td>
					<td>${collect.add1 }</td>
					<td>${collect.add2 }</td>
					<td>${collect.add3 }</td>
					<td>${collect.add4 }</td>
					<td>${collect.add5 }</td>
					<td>${collect.update1 }</td>
					<td>${collect.update2 }</td>
					<td>${collect.update3 }</td>
					<td>${collect.update4 }</td>
					<td>${collect.update5 }</td>
						
				</tr>
			</c:forEach>
		</tbody>
					
				</table>
				</div>
</body>
</html>
