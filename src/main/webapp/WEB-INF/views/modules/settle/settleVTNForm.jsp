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
			productInput = $("#productInput").html();
			appInput = $("#appInput").html();
			yuan = $("#yuan").html();
			countInput = $("#countInput").html();
			priceInput = $("#priceInput").html();
			total = $("#total").html();

			$("#addLine").click(function() {
				var html = "<tr><td>"+productInput+"</td>";
				html += "<td name='appInput'>"+appInput+"</td>";
				html += "<td>"+yuan+"</td>";
				html += "<td name='countName'>"+countInput+"</td>";
				html += "<td name='priceName'>"+priceInput+"</td>";
				html += "<td name='totalName' >"+total+"</td></tr>";
				$("#contentTable").append(html);
			});
			
			$("#removeLine").click(function() {
				if($("#contentTable").find("tr").length==2) return;
				$("#contentTable").find("tr:last").remove();
			});
			
		});
		
		
		function hiddenText(obj){
			
			var appName = $(obj).parent().parent().find("[name='appInput']").find("select:first").val(); 
			if (appName=="0") {
				
				$(obj).parent().parent().find("[name='appInput']").find("input:first").show();	
			}else{
				$(obj).parent().parent().find("[name='appInput']").find("input:first").hide();	
				$(obj).parent().parent().find("[name='appInput']").find("input:first").val("");
			}
			
			
			console.log(appName);
			console.log($(obj).parent().parent().find("[name='appInput']").find("input:first").val());
			
			 
		}
		
		function multiply(obj){
			var price = $(obj).val();
			var count = $(obj).parent().parent().find("[name='countName']").find("input:first").val();	
			var total = price*count;
			$(obj).parent().parent().find("td:last").html(total);		
			console.log(total);
			
		
			//$(obj).parent().parent().find("td:last").html("123");		
	
			//console.log($(obj).parent().parent().find("[name='countName']").find("input:first").val());
			//console.log($(obj).val());
	
			//console.log($(obj).parent().parent().find("td:last"));
			//console.log($(obj).parent().parent().find("totalName").html());
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/settlevtn/settleVTN/">VTN管理列表</a></li>
		<li class="active"><a href="${ctx}/settlevtn/settleVTN/form">VTN管理添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="settleVTN" action="${ctx}/settlevtn/settleVTN/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
			<div class="control-group">	
			<div class="controls">
			<label class="control-label">供应商名称:</label>
			<select name="configSupplierId" id="configSupplierId"  class="required" >
					<c:if test="${supplierSize==0 }">
						<option value="">没有证书供应商类型</option>
					</c:if>
					<c:if test="${supplierSize!=0 }">
					<option value="">请选择</option>
					<c:forEach items="${configSuppliers}" var="configSupplier">
						<option value="${configSupplier.id }" >${configSupplier.supplierName }</option>
					</c:forEach>
					</c:if>
				</select>
			</div></div>
			<div class="control-group">	
		<div class="controls">
						<input type="button" class="btn btn-success" value="+" style="width:34px" id="addLine"/>
						<input type="button" class="btn btn-info" value="-" style="width:34px" id="removeLine"/>
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed" style="width: 60%">
					<tr>
						<th>产品名称</th>
						<th>应用名称</th>
						<th>单位</th>
						<th>数量</th>
						<th>单价/元</th>
						<th>小计/元</th>
					</tr>
					<tr>
						<td id="productInput">
							<input type="text"  name="productName"  maxlength="40"  class="required" />
						</td>
						<td id="appInput" name="appInput">
							<select name="appName" id="appName" onchange="hiddenText(this)">
							<option value="0">请选择</option>
								<c:forEach items="${apps }" var="app" >
									<option value="${app.appName }">${app.appName }</option>
								</c:forEach>
							</select>
							<input type="text"  name="appName"  maxlength="40"  />
						</td>
						<td id="yuan" >
							张
						</td>
						<td id="countInput"  name="countName">
							<input type="text" 
								onkeyup="value=value.replace(/[^\d]/g,'')" 
								 name="count" maxlength="9" class="required"/>
						</td>
						<td id="priceInput"  name="priceName">
							<input type="text" 
								onkeyup="value=value.replace(/[^\d\.]/g,'')" 
								 name="price" maxlength="9" class="required" onblur="multiply(this)"/>
						</td>
						<td id="total" name="totalName">
						</td>
					</tr>
				</table>
		</div></div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
