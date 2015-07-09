<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>支付信息查看</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/finance/financePaymentInfo/show?id=${financePaymentInfo.id}">支付信息查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="financePaymentInfo" action="${ctx}/finance/financePaymentInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">付款编号:</label>
			<div class="controls">
				${financePaymentInfo.id}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易流水号:</label>
			<div class="controls">
				${financePaymentInfo.serialNum}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls">
				${financePaymentInfo.company}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款银行:</label>
			<div class="controls">
				${financePaymentInfo.paymentBank}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款账号:</label>
			<div class="controls">
				${financePaymentInfo.paymentAccount}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款金额:</label>
			<div class="controls">
			${financePaymentInfo.paymentMoney}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">付款方式:</label>
			<div class="controls">
				${financePaymentInfo.paymentMethodName}
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
				<fmt:formatDate value="${financePaymentInfo.payDate	}" pattern="yyyy-MM-dd"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">绑定次数:</label>
			<div class="controls">
			
				<a
						href="${ctx}/work/workDealInfo/financeList?financePaymentInfoId=${financePaymentInfo.id}">
							已绑定${financePaymentInfo.bingdingTimes}次</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收费窗口:</label>
			<div class="controls">			
			${financePaymentInfo.createBy.office.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录人员:</label>
			<div class="controls">			
			${financePaymentInfo.createBy.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录时间:</label>
			<div class="controls">			
			<fmt:formatDate value="${financePaymentInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				${financePaymentInfo.remarks}
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
