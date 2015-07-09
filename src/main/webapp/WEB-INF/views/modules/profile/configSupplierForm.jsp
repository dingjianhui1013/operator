<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var checkName=0;
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
		
		function check(){
			var name = $("#supplierName").val();
			var supplierId = $("#supplierId").val();
			var url = "${ctx}/profile/configSupplier/checkName?supplierName="+name+"&supplierId="+supplierId+"&_="+new Date().getTime();
			 $.getJSON(url,function(data){
					 if (data.status==0) {
						if (data.type==1) {
							top.$.jBox.tip("供应商名称已存在！");
							checkName=1;
						}else{
							checkName=0;
						}
					}
				});
		}
		
		
 		function onSubmit(){
			if (checkName==1) {
				top.$.jBox.tip("供应商名称已存在！");
				 return false;
			}
			 return true;
		} 
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configSupplier/">供应商列表</a></li>
		<li class="active"><a href="${ctx}/profile/configSupplier/form?id=${configSupplier.id}">供应商<shiro:hasPermission name="profile:configSupplier:edit">${not empty configSupplier.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="profile:configSupplier:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configSupplier" action="${ctx}/profile/configSupplier/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<input type="hidden"  id="supplierId"  value="${configSupplier.id }">
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>供应商名称:</label>
			<div class="controls">
				<form:input path="supplierName" htmlEscape="false"  onblur="check()" maxlength="25" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">供应商类型: </label>
			<div class="controls">
				<form:radiobutton path="supplierType" onclick="showProduct(1)" value="0"></form:radiobutton>证书
					<c:if test="${configSupplier.id==null||configSupplier.id=='' }">
				<form:radiobutton path="supplierType" onclick="showProduct(0)" value="1" checked="checked"></form:radiobutton>key 
				</c:if>
					<c:if test="${configSupplier.id!=null&&configSupplier.id!='' }">
				<form:radiobutton path="supplierType" onclick="showProduct(0)" value="1"></form:radiobutton>key 
				</c:if>
			<%-- 	<form:radiobutton path="supplierType" onclick="showProduct(0)" value="2"></form:radiobutton>签章 --%>
			</div>
		</div>
		<script type="text/javascript">
			function showProduct(obj){
				if(obj==1){
					$("#ppppp").show();
				}else{
					$("#ppppp").hide();
				}
			}
		</script>
		<div class="control-group" id="ppppp" 
		<c:if test="${configSupplier.supplierType==1|| configSupplier.supplierType==null}">style="display:none"</c:if>
		>
			<label class="control-label">供应产品:</label>
			<div class="controls">
			
			<c:forEach items="${list}" var="list">
				<input type="checkbox" 
				<c:if test="${list.kebl}">checked</c:if> value="${list.id }" name="product"/>
				${list.name}
			</c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>商务联系人:</label>
			<div class="controls">
				<form:input path="supplierCommUsername" htmlEscape="false" maxlength="15" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商务联系电话:</label>
			<div class="controls">
				<form:input path="supplierCommMobile" htmlEscape="false" maxlength="11" class="mobile"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>技术联系人:</label>
			<div class="controls">
				<form:input path="supplierTechnicalName" htmlEscape="false" maxlength="15" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">技术联系人电话:</label>
			<div class="controls">
				<form:input path="supplierTechnicalMobile" htmlEscape="false" maxlength="11" class="mobile"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">供应商地址:</label>
			<div class="controls">
				<form:input path="supplierAddress" htmlEscape="false" maxlength="40" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:input path="supplierRemarks" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configSupplier:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit"   onclick="return onSubmit()" value="保 存"/>
			&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
