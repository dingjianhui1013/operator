<%@page import="com.itrus.ca.modules.constant.ProductType"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>应用管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configProduct?appId=${appId}">产品列表</a></li>
		<li class="active"><a href="${ctx}/profile/configProduct/form?id=${configProduct.id}&appId=${appId}">产品<shiro:hasPermission name="profile:configProduct:edit">${not empty configProduct.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="profile:configProduct:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configProduct" action="${ctx}/profile/configProduct/save?appId=${appId}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<select id="productName" name="productName">
						<c:forEach items="${pros}" var="pro">
							<option value="${pro.id}"
							<c:if test="${proName eq pro.name }">
								 selected="selected"
							</c:if>>${pro.name}</option>
						</c:forEach>
				</select>
				
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品类型:</label>
			<div class="controls">
			
				<input type="radio" id="productLabel" name="productLabel"  checked="checked" value="1">专用
				<c:if test="${ap.supportCommon }">
				<input type="radio" id="productLabel" name="productLabel"  <c:if test="${configProduct.productLabel==0}"> checked="checked" </c:if>   value="0">通用
				</c:if>	
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">自动审核申请移动设备:</label>
			<div class="controls">
				<form:radiobutton path="deviceApplyAutoPass" value="0"/>否
				<form:radiobutton path="deviceApplyAutoPass" value="1"/>是
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">RA模板:</label>
			<div class="controls">
				<select id="raAccountId" name="raAccountId">
				       <option value="">请选择</option>
						<c:forEach items="${configRaAccounts}" var="raConfig">
							<option value="${raConfig.id}"
							<c:if test="${productRaAccount eq raConfig.id }">
								 selected="selected"
							</c:if>>${raConfig.raName}</option>
						</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书模板:</label>
			<div class="controls">
				<select id="raAccountExtedId" name="raAccountExtedId">
				        <option value="">请选择</option>
						<c:forEach items="${raAccountExtendInfos}" var="certConfig">
							<option value="${certConfig.id}"
							<c:if test="${productCertAccount eq certConfig.id }">
								 selected="selected"
							</c:if>>${certConfig.certName}</option>
						</c:forEach>
				</select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:input path="remarks" htmlEscape="false" maxlength="50" />
				<input  type="hidden"  id="appId" name="appId"  value="${appId}"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configProduct:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
