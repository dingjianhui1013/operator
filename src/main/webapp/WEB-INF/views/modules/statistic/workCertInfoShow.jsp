<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>证书信息管理</title>
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
<form:form id="inputForm" modelAttribute="workCertInfo" action="" method="post" class="form-horizontal">
<br/>
		<div class="control-group">
			<label class="control-label">办理时间:</label>
			<div class="controls">
				<fmt:formatDate value="${dealInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经办人姓名:</label>
			<div class="controls">
				${dealInfo.workUser.contactName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls" >
				${dealInfo.workCompany.companyName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应用名称:</label>
			<div class="controls">
				${dealInfo.configApp.appName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称:</label>
			<div class="controls">
				${proType[dealInfo.configProduct.productName]}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务类型:</label>
			<div class="controls">
				${businessType[dealInfo.dealInfoType]}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">办理年限:</label>
			<div class="controls">
				${dealInfo.year}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">办理费用:</label>
			<div class="controls">
				${dealInfo.workPayInfo.workTotalMoney}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">证书DN:</label>
			<div class="controls">
				${dealInfo.workCertInfo.subjectDn}
			</div>
		</div>
	</form:form>
</body>
</html>
