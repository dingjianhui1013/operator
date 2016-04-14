 <%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>印章计费策略模板管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});

</script>

<script type="text/javascript">
	var add = '<select name="addYear" id="addDiv"><option value="1">一年</option><option value="2">二年</option><option value="4">四年</option></select><input type="text" name="addMoney">';
	var update = ''
	function addLine(obj) {
	}
</script>
</head>
<body>
	<form:form class="form-horizontal">
		<tags:message content="${message}" />
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">计费策略模板名称:</label>
						<div class="controls" style="margin-top: 3px;">
							${signatureConfigChargeAgent.tempName}</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">业务类型:</label>
						<div class="controls"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">新增:</label>
						<div class="controls" style="margin-top: 2px;">
							一年：
							<fmt:formatNumber pattern="#,##0.00">${xz1}</fmt:formatNumber>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;两年：
						<fmt:formatNumber pattern="#,##0.00">${xz2}</fmt:formatNumber>
					</div>
				</td>
			</tr>
			
			<tr>
				<td><div class="control-group">
						<div class="controls" style="margin-top: 2px;">
							三年：
							<fmt:formatNumber pattern="#,##0.00">${xz3}</fmt:formatNumber>
						</div>
					</div></td>
				<td><div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;四年：
						<fmt:formatNumber pattern="#,##0.00">${xz4}</fmt:formatNumber>
					</div></td>
			</tr>
			
			<tr>
				<td><div class="control-group">
						<div class="controls" style="margin-top: 2px;">
							五年：
							<fmt:formatNumber pattern="#,##0.00">${xz5}</fmt:formatNumber>
						</div>
					</div></td>
				
			</tr>
		
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">更新:</label>
						<div class="controls" style="margin-top: 2px;">
							一年：
							<fmt:formatNumber pattern="#,##0.00">${gx1}</fmt:formatNumber>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;两年：
						<fmt:formatNumber pattern="#,##0.00">${gx2}</fmt:formatNumber>
					</div>
				</td>
			</tr>
			
			
			<tr>
				<td><div class="control-group">
						<div class="controls" style="margin-top: 2px;">
							三年：
							<fmt:formatNumber pattern="#,##0.00">${gx3}</fmt:formatNumber>
						</div>
					</div></td>
				<td><div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;四年：
						<fmt:formatNumber pattern="#,##0.00">${gx4}</fmt:formatNumber>
					</div></td>
			</tr>	
			
			
			<tr>
				<td><div class="control-group">
						<div class="controls" style="margin-top: 2px;">
							五年：
							<fmt:formatNumber pattern="#,##0.00">${gx5}</fmt:formatNumber>
						</div>
					</div></td>
			
			</tr>	
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">变更:</label>
						<div class="controls">${th}</div>
					</div>
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td>
					<div class="control-group" id="chargeMethods">
						<label class="control-label">付款方式:</label>
						<div class="controls">
							<c:if test="${signatureConfigChargeAgent.chargeMethodPos}">pos</c:if>
							&nbsp;&nbsp;&nbsp;
							<c:if test="${signatureConfigChargeAgent.chargeMethodMoney}">现金缴费</c:if>
							&nbsp;&nbsp;&nbsp;<br>
						</div>
					</div>
				</td>
				<td></td>
			</tr>
		</table>
	</form:form>
</body>
</html>
