<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商返修Key记录管理</title>
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
		function addGene() {
			var supplierId = $("#manu").prop('value');
			var url = "${ctx}/key/keyGeneralInfo/addGeneralInfo?supplierId=";
			$.getJSON(url + supplierId+"&_="+new Date().getTime(), function(data) {
				var html = "";
				//console.log(data);
				html += "<option value=\""+""+"\">请选择</ooption>";
				$.each(data, function(idx, ele) {
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">" + ele.name
							+ "</ooption>"
				});
				$("#gene").html(html);
			});
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/settle/settleKey/">供应商返修Key记录列表</a></li>
		<li class="active"><a href="${ctx}/settle/settleKey/form?id=${settleKey.id}">供应商返修Key记录<shiro:hasPermission name="settle:settleKey:edit">${not empty settleKey.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="settle:settleKey:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="settleKey" action="${ctx}/settle/settleKey/save" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>到货时间:</label>
			<div class="controls"> 
				<input  class="input-medium Wdate required" value="<fmt:formatDate value="${coDate}" pattern="yyyy-MM-dd"/>"  type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20" readonly="readonly" name="coDate"></input>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">返修时间:</label>
			<div class="controls"> 
				<input  class="input-medium Wdate" value="<fmt:formatDate value="${baDate}" pattern="yyyy-MM-dd"/>" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20" readonly="readonly" name="baDate"></input>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>厂商名称:</label>
			<div class="controls">
				<select name = "configSupplier.id"  id="manu"  class="required" onchange="addGene()">
					<option value="">请选择</option>
					<c:forEach items="${suppliers }" var="supplier">
						<option value = "${supplier.id }">${supplier.supplierName }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>KEY类型名称:</label>
			<div class="controls">
				<select name = "keyGeneralInfo.id"  id="gene" class="required">
					<option value="">请选择</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>Key编码:</label>
			<div class="controls">
				<form:input path="keySn" htmlEscape="false" maxlength="20" class="required"/>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
