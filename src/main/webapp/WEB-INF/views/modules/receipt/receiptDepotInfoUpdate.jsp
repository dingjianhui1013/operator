<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>发票信息管理</title>
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/receipt/receiptDepotInfo/">库房管理</a></li>
		<li class="active"><a href="${ctx}/receipt/receiptDepotInfo/form?id=${receiptDepotInfo.id}">编辑库存</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="receiptDepotInfo" action="${ctx}/receipt/receiptDepotInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name = "receiptResidue" value = "${receiptDepotInfo.receiptResidue }" />
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">区域名称:</label>
			<div class="controls">
				${receiptDepotInfo.area.name }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">网点名称:</label>
			<div class="controls">
				${receiptDepotInfo.office.name }
			</div>
		</div>
		<input type = "hidden" value = "${receiptDepotInfo.office.id }" name = "officeId" />
		<div class="control-group">
			<label class="control-label">库存名称:</label>
			<div class="controls">
				<form:input path="receiptName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">预警人姓名:</label>
			<div class="controls">
				<form:input path="warningName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<h4 class="control-label">联系人信息</h4>
		</div>
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">联系人姓名:</label>
						<div class="controls">
							<form:input path="receiptCommUser" htmlEscape="false" maxlength="50" class="required"/>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">部门名称:</label>
						<div class="controls">
							<form:input path="department" htmlEscape="false" maxlength="50" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">性别:</label>
						<div class="controls">
							<form:radiobutton path="sex" value="0" />男
							<form:radiobutton path="sex" value="1" />女
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">邮政编码:</label>
						<div class="controls">
							<form:input path="codeZip" htmlEscape="false" maxlength="50" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">移动电话	:</label>
						<div class="controls">
							<form:input path="phone" htmlEscape="false" maxlength="50" cssClass="required mobile" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">电子邮箱:</label>
						<div class="controls">
							<form:input path="email" htmlEscape="false" maxlength="50" 
							cssClass="required email" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">座机电话:</label>
						<div class="controls">
							<form:input path="receiptCommMobile" htmlEscape="false" maxlength="50" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">联系地址:</label>
						<div class="controls">
							<form:input path="address" htmlEscape="false" maxlength="50" />
						</div>
					</div>
				</td>
			</tr>			
		</table>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
