<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>VTN管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var productInput;
	var appInput;
	var yuan;
	var countInput;
	var priceInput;
	var total;
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
			
			var type = ${appType};
			if (type==0) {
				$("#appInput").hide();
			}else{
				var name = ${settleVTN.appName};
				alert(name);
				$("#appInput").val(name);
			}
		});
		
		
		function hiddenText(){
			var appName= $("#appSelect").val();
			//var appName = $(obj).parent().parent().find("[name='appInput']").find("select:first").val(); 
			alert(appName);
			if (appName=="0") {
				$("#appInput").show();
			//	$(obj).parent().parent().find("[name='appInput']").find("input:first").show();	
			}else{
				$("#appInput").hide();
				$("#appInput").val("");
				//$(obj).parent().parent().find("[name='appInput']").find("input:first").hide();	
				//$(obj).parent().parent().find("[name='appInput']").find("input:first").val("");
			}
		
		//	console.log(appName);
			//console.log($(obj).parent().parent().find("[name='appInput']").find("input:first").val());
			
			 
		}
		
		 function peration(){
			var price = $("#price").val();
			var count = $("#count").val();	
			if (price!=""&&count!="") {
				var total = price*count;
				$("#totalPrice").html(total);		
			}
		} 
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/settlevtn/settleVTN/">VTN管理列表</a></li>
		<li class="active"><a href="${ctx}/settlevtn/settleVTN/update?id=${settleVTN.id }">VTN管理修改</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="settleVTN" action="${ctx}/settlevtn/settleVTN/saveUpdate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
			<div class="control-group">	
				<label class="control-label">供应商名称:</label>
				<div class="controls">
					<select name="configSupplierId" id="configSupplierId"  class="required" >
						<c:if test="${supplierSize==0 }">
							<option value="">没有证书供应商类型</option>
						</c:if>
						<c:if test="${supplierSize!=0 }">
						<option value="">请选择</option>
						<c:forEach items="${configSuppliers}" var="configSupplier">
							<option value="${configSupplier.id }" 
							
							<c:if test="${settleVTN.configSupplier.id ==configSupplier.id }">selected</c:if>
							
							>${configSupplier.supplierName }</option>
						</c:forEach>
						</c:if>
					</select>
				</div>
			</div>
			<div class="control-group">	
				<label class="control-label">证书名称:</label>
				<div class="controls">
					<input type="text" 
								 name="productName" id="productName" maxlength="50" class="required"  value="${settleVTN.productName }" />
					
				</div>
			</div>
			
			<div class="control-group">	
				<label class="control-label">应用名称:</label>
				<div class="controls">
					<select name="appName" id="appSelect" onchange="hiddenText()">
							<option value="0">请选择</option>
								<c:forEach items="${apps }" var="app" >
									<option value="${app.appName }"
										<c:if test="${settleVTN.appName ==app.appName}">selected</c:if>
									>${app.appName }</option>
								</c:forEach>
							</select>
							<input type="text"  name="appName" id="appInput"  id="appNameInput"  maxlength="40"  />
				</div>
			</div>
			<div class="control-group">	
				<label class="control-label">单位:</label>
				<div class="controls">
					<label style="margin-top: 3px;">张</label>
				</div>
			</div>
			<div class="control-group">	
				<label class="control-label">数量:</label>
				<div class="controls">
					<input type="text" 
								onkeyup="value=value.replace(/[^\d]/g,'')" 
								 name="count" id="count" maxlength="9" class="required" onblur="peration()" value="${settleVTN.count}" /> 
				</div>
			</div>
			<div class="control-group">	
				<label class="control-label">单价/元:</label>
				<div class="controls">
						<input type="text" 
								onkeyup="value=value.replace(/[^\d\.]/g,'')" 
								 name="price" id="price" maxlength="9" class="required" onblur="peration()" value="${settleVTN.price }" />
				</div>
			</div>
			<div class="control-group">	
				<label class="control-label">小计/元:</label>
				<div class="controls">
					<label style="margin-top: 3px;" id="totalPrice" >${settleVTN.countPrice}</label>
				</div>
			</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
