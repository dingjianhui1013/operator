<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="default" />
<title>支付信息退费管理</title>
</head>
<body>
<ul class="nav nav-tabs">
		<li><a href="${ctx}/finance/financePaymentInfo/payList">支付信息列表</a></li>
		<li class="active"><a
			href="${ctx}/finance/financePaymentInfo/quitMoney?id=${financePaymentInfo.id}">支付信息<shiro:hasPermission
					name="finance:financePaymentInfo:quit">退费</shiro:hasPermission>
				<shiro:lacksPermission name="finance:financePaymentInfo:quit">查看</shiro:lacksPermission></a></li>
	</ul>
</body>
</html>