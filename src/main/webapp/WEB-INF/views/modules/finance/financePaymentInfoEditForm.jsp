<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>支付信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery.bigautocomplete.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
				/* $("#menuButton1").bind("click",showCompanyList); */
			});

</script>
<script type="text/javascript">
	$(document).ready(function() {
		var nameUrl = "${ctx}/finance/financePaymentInfo/companyName"+"?_="+new Date().getTime();
		$.getJSON(nameUrl, function(da) {
			/* alert(da); */
			$("#company").bigAutocomplete({
				data : da.list
			});
		});
	})
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/finance/financePaymentInfo/payList">支付信息列表</a></li>
		<li class="active"><a
			href="${ctx}/finance/financePaymentInfo/editForm?id=${financePaymentInfo.id}">支付信息修改</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="financePaymentInfo"
		action="${ctx}/finance/financePaymentInfo/editSave" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
			<div class="control-group">
				<label class="control-label">付款编号:</label>
				<div class="controls">
					${financePaymentInfo.remark }	
				</div>
			</div>	
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls">
					${financePaymentInfo.company}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应用名称:</label>
			<div class="controls">
				${not empty emfinancePaymentInfo.configApp.appName?emfinancePaymentInfo.configApp.appName:"所有应用" }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款金额(元):</label>
			<div class="controls">
					${financePaymentInfo.paymentMoney}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款方式:</label>
			<div class="controls">
				<c:if test="${financePaymentInfo.paymentMethod==1}">现金</c:if>
				<c:if test="${financePaymentInfo.paymentMethod==2}">POS收款</c:if>
				<c:if test="${financePaymentInfo.paymentMethod==3}">银行转账</c:if>
				<c:if test="${financePaymentInfo.paymentMethod==4}">支付宝转账</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人:</label>
			<div class="controls">
					${financePaymentInfo.commUserName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
					${financePaymentInfo.commMobile}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款时间:</label>
			<div class="controls">
				<fmt:formatDate value="${financePaymentInfo.payDate}" pattern="yyyy-MM-dd"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" cols="4" rows="4" 
					style="resize: none;"></form:textarea>
			</div>
		</div>
		
		<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
