<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>调拨管理管理</title>
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
		
		function show() {
			$("#reason").show();
			$("#remarks").hide();
		}
		function hide(){
			$("#reason").hide();
			$("#remarks").show();
		}
	</script>
</head>
<body>
	<form:form id="assessmentForm" modelAttribute="keyAllocateApply" action="${ctx}/key/keyAllocateApply/assessmentSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="applyId" id="applyId" value="${applyId }"/>
		
		<div class="control-group">
			<label class="control-label">审批意见:</label>
			<div class="controls">
				<input type="radio"  name="approvalOpinion" id="ra1" value="0" onclick="hide()" checked="checked"/>是
				<input type="radio"  name="approvalOpinion" id="ra2" value="1" onclick="show()"/>否
			</div>
		</div>
		<div class="control-group" id="remarks" >
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea rows="4" cols="4" name="remarks"></textarea>
			</div>
		</div>
		<div class="control-group" id="reason" style="display: none">
			<label class="control-label">拒绝原因:</label>
			<div class="controls">
				<textarea rows="4" cols="4" name="refusalReason"></textarea>
			</div>
		</div>
	</form:form>
</body>
</html>
