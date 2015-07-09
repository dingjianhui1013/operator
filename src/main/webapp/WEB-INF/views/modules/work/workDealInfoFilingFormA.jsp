<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css" rel="stylesheet" />
</head>	
<body>
	<ul class="nav nav-tabs">
		<li class="active">查看业务信息</li>
	</ul>
	<div class="form-horizontal">
		<div class="control-group">
			<label class="control-label">应用名称:</label>
			<div class="controls">
				${workDealInfo.configApp.appName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称:</label>
			<div class="controls">
				<td>${proType[workDealInfo.configProduct.productName]}</td>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书应用标识:</label>
			<div class="controls">
				<c:if test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if>
				<c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">多证书编号:</label>
			<div class="controls">
				${workDealInfo.certSort }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书详细信息:</label>
			<div class="controls">
				${workDealInfo.workCertInfo.issuerDn}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务有效期:</label>
			<div class="controls">
				${workDealInfo.workCertInfo.notbefore} - ${workDealInfo.workCertInfo.notafter}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经办人姓名:</label>
			<div class="controls">
				${workDealInfo.workUser.contactName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">KEY ID:</label>
			<div class="controls">
				${workDealInfo.keySn}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">移动设备授权数量:</label>
			<div class="controls">
				<c:if test="${workDealInfo.workCertInfo.trustDeviceCount == null}">0</c:if>
				<c:if test="${workDealInfo.workCertInfo.trustDeviceCount != null}">${workDealInfo.workCertInfo.trustDeviceCount}</c:if>
			</div>
		</div>
		<tags:message content="${message}" />
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</div>
</body>
</html>
