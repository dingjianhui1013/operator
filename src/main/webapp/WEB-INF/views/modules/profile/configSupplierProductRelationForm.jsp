<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商产品配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configSupplierProductRelation/">按供应商产品计费</a></li>
		<li class="active"><a href="${ctx}/profile/configSupplierProductRelation/form?id=${configSupplierProductRelation.id}">计费配置</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configSupplierProductRelation" action="${ctx}/profile/configSupplierProductRelation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">供应商名称:</label>
			<div class="controls">
				<form:input path="configSupplier.supplierName" htmlEscape="false" maxlength="11" class="required" disabled="true"/>
				<input type="hidden" name="id" value="${configSupplierProductRelation.id}">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称:</label>
			<div class="controls">
			<c:if test="${configSupplierProductRelation.configSupplier.supplierType==0 }">
			<input name="productType" type="text"  disabled value="${productTypeMap[configSupplierProductRelation.productType] }"/>
				</c:if>
				<c:if test="${configSupplierProductRelation.configSupplier.supplierType==1 }">
					<input name="productType" type="text"  disabled value="${configSupplierProductRelation.keyGeneralInfo.name}"/>
				</c:if>
			</div>
		</div>
		<c:if test="${configSupplierProductRelation.productType!=4&&configSupplierProductRelation.productType!=5 && configSupplierProductRelation.configSupplier.supplierType==0  }">
		<div class="control-group">
			<label class="control-label">收费标准:</label>
			<div class="controls">
				一年:<input type="text" name="money" value="${money1 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
				两年:<input type="text" name="money" value="${money2 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
				三年:<input type="text" name="money" value="${money3 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
				四年:<input type="text" name="money" value="${money4 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
				五年:<input type="text" name="money" value="${money5 }"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/>
			</div>
		</div>
		</c:if>
		<c:if test="${configSupplierProductRelation.productType==4||configSupplierProductRelation.productType==5 || configSupplierProductRelation.configSupplier.supplierType==1 }">
		<div class="control-group">
			<label class="control-label">收费标准:</label>
			<div class="controls">
				<input name="money" value="${money1}" type="text" maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		</c:if>
		
		
		
		<div class="form-actions">
			<shiro:hasPermission name="profile:configSupplierProductRelation:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
