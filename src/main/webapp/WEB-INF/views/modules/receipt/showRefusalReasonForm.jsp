<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>调拨管理管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="assessmentForm" modelAttribute="receiptAllocateApply" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		</br>
		<div class="control-group" id="reason" >
			<label class="control-label">拒绝原因:</label>
			<div class="controls">
				<textarea rows="5" cols="5">${apply.remarks}</textarea>
				
			</div>
		</div>
	</form:form>
</body>
</html>
