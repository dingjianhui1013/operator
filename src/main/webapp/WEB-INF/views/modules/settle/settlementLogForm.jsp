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
						<th colspan="${7+lenth*5 }" >统计周期：<fmt:formatDate pattern="yyyy-MM-dd" value="${settlementLogs.startTime}"/>&nbsp;-&nbsp;<fmt:formatDate pattern="yyyy-MM-dd" value="${settlementLogs.endTime}"/></th>
					</tr>
					<tr>
						<th colspan="${7+lenth*5 }">本期结算证书年限：（本次结算年数总数）代理商：${settlementLog.comagentName} 应用名称：${settlementLog.appName}</th>
					</tr>
					<tr>
						<th rowspan="2">序号</th>
						<th rowspan="2">单位名称</th>
						<th rowspan="2">经办人姓名</th>
						<th rowspan="2">产品名称</th>
						<c:forEach var="a" begin="1" end="${lenth}">
						
						<th colspan="5">第${a}次结算</th>
						</c:forEach>
						
						<th colspan="3">结算年限统计</th>
					</tr>
					<tr>
						
						<c:forEach begin="1" end="${lenth}">
						<th>缴费类型</th>
						<th>起始时间</th>
						<th>结束时间</th>
						<th>业务类型</th>
						<th>结算(年)</th>
						</c:forEach>
						<th>已结算（年）</th>
						<th>本期结算（年）</th>
						<th>剩余结算（年）</th>
					</tr>
					<c:forEach items="${dealInfos }" var="dealInfo" varStatus="status">
						<tr>
							<td>${status.index + 1}</td>
							<td>${dealInfo.workCompany.companyName}</td>
							<td>${dealInfo.workCertInfo.workCertApplyInfo.name}</td>
							<td>${proType[dealInfo.configProduct.productName]}</td>
							<c:forEach items="${dealInfo.detailList }" var="detail">

									<c:if test="${detail.method==1}"><td>标准</td></c:if> 
	 								<c:if test="${detail.method==2}"><td>政府统一采购</td></c:if> 
	 								<c:if test="${detail.method==3}"><td>合同采购</td></c:if> 
								<td><fmt:formatDate	value="${detail.startDate }" pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatDate value="${detail.endDate }" pattern="yyyy-MM-dd" /></td>
								<td><c:if test="${detail.dealInfoType=='null '}"> </c:if> 
								<c:if test="${detail.dealInfoType!='null '}">${detail.dealInfoType}</c:if> </td>
								<td>${detail.settleYear }</td>
							</c:forEach>
							<c:forEach begin="1" end="${lenth - dealInfo.detailList.size() }">
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:forEach>
							<td>${dealInfo.yyNum}</td>
							<td>${dealInfo.lastNum}</td>
							<td><c:if test="${dealInfo.totalNum - dealInfo.yyNum - dealInfo.lastNum-dealInfo.occupy<0}">0</c:if>
							
								<c:if test="${dealInfo.totalNum - dealInfo.yyNum - dealInfo.lastNum-dealInfo.occupy>=0}">${dealInfo.totalNum - dealInfo.yyNum - dealInfo.lastNum-dealInfo.occupy}</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				</div>
</body>
</html>
