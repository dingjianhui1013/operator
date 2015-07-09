<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>key出库详情管理</title>
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
		<li><a href="${ctx}/key/keyUsbKeyInvoice/list?depotId=${depotId}">出库信息列表</a></li>
		<li class="active">
			<a href="${ctx}/key/keyUsbKeyInvoice/form?id=${depotId}">添加出库信息</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="keyUsbKeyInvoice" action="${ctx}/key/keyUsbKeyInvoice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">key类型名称:</label>
			<div class="controls">					
					<select name="keyGeneralInfoId" id="keyGeneralInfoId"  class="required" >
					<c:if test="${genesSize==0 }">
						<option value="">库存中没有添加KEY类型产品</option>
					</c:if>
					<c:if test="${genesSize!=0 }">
					<option value="">请选择</option>
					<c:forEach items="${generalInfos}" var="gene">
						<option value="${gene.id }"    
							<c:if test="${keyGeneralInfoId==gene.id }">
							selected="selected"
					</c:if>
						
						>${gene.manuKeyName }</option>
					</c:forEach>
					</c:if>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出库数量:</label>
			<div class="controls">
				<form:input path="deliveryNum" htmlEscape="false" maxlength="9" class="required" 
				 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
				 onkeyup="value=value.replace(/[^\d]/g,'') "
				/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出库原因:</label>
			<div class="controls">
				<select name="outReason" id="outReason"  class="required" >
					<option value="">请选择</option>
					<option value="1"
					
					<c:if test="${keyUsbKeyInvoice.outReason==1 }">
							selected="selected"
					</c:if>
					>维修品出库</option>
					<c:if test="${depot.canDelTotal()}">
					<option value="6"
					<c:if test="${keyUsbKeyInvoice.outReason==6 }">
							selected="selected"
					</c:if>
					>其他</option>
					</c:if>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出库时间:</label>
			<div class="controls">
				<fmt:formatDate value="${time}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<input type="hidden"  name="depotId" id="depotId" value="${depotId}">
				<input type="hidden"  name="time" id="time" value="<fmt:formatDate value="${time}" pattern="yyyy-MM-dd HH:mm:ss"/>">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="description"  htmlEscape="false" maxlength="50"></form:textarea>	
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="key:keyUsbKey:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
