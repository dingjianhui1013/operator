<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>调拨信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var typeSelect;
	var numInput;
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
			
			typeSelect = $("#receiptTypeTD").html();
			numInput = $("#applyNumTD").html();
			$("#addLine").click(function() {
				var html = "<tr><td  id='receiptTypeTD' name='receiptTypeTD'>"+typeSelect+"</td>";
				html += "<td id='applyNumTD' name='applyNumTD'>"+numInput+"</td></tr>";
				$("#contentTable").append(html);
			});
			
			$("#removeLine").click(function() {
				if($("#contentTable").find("tr").length==2) return;
				$("#contentTable").find("tr:last").remove();
			});
			
		});
		
		/* function addOffice(){
			var areaId = $("#areaId").val();
			var url = "${ctx}/receipt/receiptAllocateApply/selectDepotByOfficeId?officeId=";
			$.getJSON(url+areaId+"&_="+new Date().getTime(),function(data){
				$("#depotName").attr("value",data.status);
			});
		 } */
		
		function multiply(){
			var table = $("#contentTable").find("tr");
			var total = 0;
			for (var a = 1; a <table.length; a++) {
				var countSS =0;
				var priceSS = 0;
				var tr = $($("#contentTable").find("tr")[a]);
				var count = tr.find("[name='applyNumTD']").find("input:first").val();
				if (count=="") {
					 tr.find("[name='applyNumTD']").find("label:first").html("不能为空！");
				}else{
					 tr.find("[name='applyNumTD']").find("label:first").html("");
					 countSS = parseInt(count);
				}
				var money = tr.find("[name='receiptTypeTD']").find("select:first option:selected").text();
				if (money=="请选择") {
					tr.find("[name='receiptTypeTD']").find("label:first").html("不能为空！");
				}else{
					tr.find("[name='receiptTypeTD']").find("label:first").html("");
					priceSS = parseInt(money);
				}
				total+=countSS*priceSS;
				
			}
			$("#totalPrice").html(total);
			$("#money").val(total);
		}
		
		
		function checkTime(){
			var nowDate = $("#reqDate").val();
		    var  d   =   new   Date(nowDate.replace(/-/g,   "/")); 
		 
		    if ( d < new Date()) {
		    	top.$.jBox.tip(" 到货日期录入有误，请重新录入!");
		    	return false;
			}
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/receipt/receiptAllocateApply/">调拨管理</a></li>
		<li class="active"><a href="${ctx}/receipt/receiptAllocateApply/form">调拨申请</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="receiptAllocateApply" action="${ctx}/receipt/receiptAllocateApply/save" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>采购网点:</label>
			<div class="controls">
				<select name="officeId" id="areaId"  class="required" onchange="addOffice()" >
					<%-- <option value="">请选择</option>
					<c:forEach items="${offices}" var="office">
						<option value="${office.id }" >${office.name }</option>
					</c:forEach> --%>
					<option value="${office.id}">${office.name}</option>
					
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>采购库房:</label>
			<div class="controls">
				<input type = "text" id = "depotName"  value="${depot.receiptName }" readonly="readonly"/> 
			</div>
		</div>
		<div class="control-group">	
			<label class="control-label"><span style="color : red">*</span>申请详情:</label>
			<div class="controls">
			<input type="button" class="btn btn-success" value="+" style="width:34px" id="addLine"/>
			<input type="button" class="btn btn-info" value="-" style="width:34px" id="removeLine"/>
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed" style="width: 60%">
					<tr>
						<th>发票类型/元</th>
						<th>数量/张</th>
					</tr>
					<tr>
						<td id="receiptTypeTD" name="receiptTypeTD">
						<select name="receiptType" id="receiptType" 	class="required" onchange="multiply()" >
								<option value="">请选择</option>
								<c:forEach items="${types }" var="type">
									<option value="${type.id }">${type.typeName } </option>
								</c:forEach>
						</select>
						<label style="color: red;"></label>
						</td>
						<td id="applyNumTD" name="applyNumTD">
						<input type="text" 
						onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
						onkeyup="value=value.replace(/[^\d]/g,'')" 
						 name="applyNum" maxlength="9" class="required"  onblur="multiply()" />
						 <label style="color: red;"></label>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">调拨申请金额总量:</label>
			<div class="controls">
			<label name="totalPrice" id="totalPrice"></label>
			<input type="hidden" name="money" id="money"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">购买类型紧急程度:</label>
			<div class="controls">
				<form:radiobutton path="leavel" value = "0"/>一般
				<form:radiobutton path="leavel" value = "1"/>紧急
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>要求到货日期:</label>
			<div class="controls"> 
				<input  class="input-medium Wdate required" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20" readonly="readonly" name="reqDate" id="reqDate"></input>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  onclick="return checkTime()" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
