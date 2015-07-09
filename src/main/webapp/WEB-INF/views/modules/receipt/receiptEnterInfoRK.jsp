<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>入库操作</title>
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
			$("#money").html(total);
			$("#receiptMoney").val(total);
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">入库操作</a></li>
	</ul><br/>
	<form action = "${ctx}/receipt/receiptEnterInfo/saveRK" id = "inputForm" method="post" class="form-horizontal">
		<input type = "hidden" name = "receiptDepotInfo.id" value ="${receiptDepotInfo.id}" />
		<div class="control-group">
			<label class="control-label">库房名称:</label>
			<div class="controls">
				${receiptDepotInfo.receiptName }
			</div>
		</div>
			<div class="control-group">
			<label class="control-label">操作人员:</label>
			<div class="controls">
				${userName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请详情:</label>
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
			<label class="control-label">入库金额:</label>
			<div class="controls">
				<label id="money"></label>
				<input type= "hidden" name = "receiptMoney" id="receiptMoney" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入库时间:</label>
			<div class="controls">
				${now_date } 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" class="valid" cols="4" rows="4" style="resize: none;" name="remarks"></textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
