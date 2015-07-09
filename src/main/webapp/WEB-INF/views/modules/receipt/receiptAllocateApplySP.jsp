<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>调拨信息管理</title>
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
		
		function change(){
			$("#tsChange").html("拒绝原因");
		}
		
		function changeT(){
			$("#tsChange").html("备注");
		}
		
	</script>
</head>
<body>
	<form id="receiptAllSP" method="post" class="form-horizontal">
		<input type = "hidden" name = "id" value = "${receiptAllocateApply.id }" />
		<br/>
		<br/>
		<div class="control-group">
			<label class="control-label">审批意见:</label>
			<div class="controls">
				<input type="radio" name="state" onclick="javascript:changeT()" value="1" checked="checked"/>是
				<input type="radio" name="state" onclick="javascript:change()" value="2"/>否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" id= "tsChange">备注:</label>
			<div class="controls">
				<textarea id="remarks" class="valid" cols="4" rows="4" style="resize: none;" name="remarks"></textarea>
			</div>
		</div>
	</form>
</body>
</html>
